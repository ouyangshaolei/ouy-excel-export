package com.gwghk.crm.service;


import com.google.common.base.Strings;
import com.gwghk.crm.check.ParamMap;
import com.gwghk.crm.check.vo.export.F00000;
import com.gwghk.crm.common.SpringContext;
import com.gwghk.crm.common.export.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {
    @Value("${crmConfig.crmDownload.path}")
    private String path;
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
    private static DateService dateService = SpringContext.getBean(DateService.class);

    public void export(F00000 vo, HttpServletResponse response) {
        List<Map<String, Object>> list1 = getDataList(vo, ExcelExportConstants.DATA_METHOD);
        List<Map<String, Object>> list2 = getDataList(vo, ExcelExportConstants.DATA_METHODEXT);
        ExcelExportContext context = new ExcelExportContext(vo.getTmplateCode());
        String filePath = export(context, vo, list1, list2);
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        try {
            htmlDownload(filePath, fileName, response);
        } catch (Exception e) {
            logger.error("导出失败", e);
        }

    }

    public void htmlDownload(String filePath, String fileName, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            FileInputStream input = new FileInputStream(filePath);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            input.close();
        } catch (IOException e) {
            throw e;
        }
    }

    public List<Map<String, Object>> retunData(F00000 vo) {
        List<Map<String, Object>> list1 = getDataList(vo, ExcelExportConstants.DATA_METHOD);
//        List<Map<String, Object>> list2 = getDataList(vo, ExcelExportConstants.DATA_METHODEXT);
        return list1;
    }


    private List<Map<String, Object>> getDataList(F00000 vo, String dataMethod) {
        Element originElement = ExcelExportUtil.getElement(vo.getTmplateCode());
        Element dataElement = originElement.element(ExcelExportConstants.TABLE_DATA);
        Element methodElement = dataElement.element(dataMethod);
        if (methodElement == null) {
            logger.error("xml文件中data配置异常");
            return null;
        }
        Element mapperElement = dataElement.element(ExcelExportConstants.DATA_HEAD_MAPPER);
        ParamMap map = vo.toMap(false);
        EGetData type = EGetData.getType(methodElement.attributeValue(ExcelExportConstants.DATA_HEAD_DB));
        String mapper = mapperElement.getText();
        String methodStr = methodElement.getText();
        if (Strings.isNullOrEmpty(mapper) || Strings.isNullOrEmpty(methodStr) || methodStr.equalsIgnoreCase("null")) {
            return null;
        }
        return type.queryData(mapper, methodStr, map);
    }

    /**
     * @param context
     * @param vo
     * @param dataList    表格数据
     * @param dataListext 额外数据（数据库中不能在同一条数据中的 信息）
     * @return
     */
    public String export(ExcelExportContext context, F00000 vo, List<Map<String, Object>> dataList, List<Map<String, Object>> dataListext) {
//        CustomerServiceVO cs = vo.getCs();
//        String nickname = cs.getNickname();
        String nickname = "当前登录用户名";
        String filesName = vo.getFilesName();
        String tableName = vo.getTableName();
        String sheetName = vo.getSheetName();
        Integer type = context.getType();
        Integer headRowCount = context.getHeadRowCount();
        Integer headRowEnd = context.getHeadRowEnd();
        Element structure = context.getStructure();
        Element structureExt = context.getStructureExt();
        SXSSFWorkbook workbook = context.getWorkbook();
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.createFreezePane(1, headRowCount, 1, headRowCount);
        //1. 根据type  类型  创建不同类型的表格  并填充数据
        context = EExportType.getType(type).action(context, dataList, dataListext, headRowEnd, structure, sheet);
        //2. 设置各种函数:目前只包括 总计 平均值
        if (structureExt != null) {
            Iterator iterator = structureExt.elementIterator();
            while (iterator.hasNext()) {
                Element node = (Element) iterator.next();
                //求和   平均值  等.........
                context = EFormula.getType(node.getName()).setFormula(context, headRowEnd, sheet, node);
            }
        }
        //3. 绘制数据单元格边框 (表头 和 总计  平均值..行除)
        ExcelExportUtil.setDataBorderStyle(context, sheetName);
        String date = dateService.getRightDateTime().toLocalDate().toString().replaceAll("-", "");
        String dataName = filesName + "_" + date + "_" + nickname;//文件名称+日期+nickname
        String excelTyep = ".xlsx";
        String filePathName = path + File.separator + date + File.separator + dataName + excelTyep;//    /root/crm-api/20191011/excelExport/.....
        try {
            File f = new File(filePathName);
            if (!f.getParentFile().exists()) {
                File parentFile = f.getParentFile();
                parentFile.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(filePathName);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("报表导出失败", e);
        }
        return filePathName;
    }
}
