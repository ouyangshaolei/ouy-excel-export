package com.gwghk.crm.check.vo.export;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwghk.crm.check.annotation.Base;
import com.gwghk.crm.check.vo.BaseInVO;
import com.gwghk.crm.common.SpringContext;
import com.gwghk.crm.common.export.ExcelExportConstants;
import com.gwghk.crm.common.export.ExcelExportUtil;
import com.gwghk.crm.config.YmlConfig;
import com.gwghk.crm.exception.SystemException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ExcelExportInVO extends BaseInVO<ExcelExportInVO> {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportInVO.class);
    private static YmlConfig ymlConfig = SpringContext.getBean(YmlConfig.class);
    @Base
    private String paramJson; //json 入参
    private F00000 vo; //json 校验转化后的vo对象


    @Override
    public ExcelExportInVO toRightVO() {
        return this;
    }

    @Override
    public void extCheck() throws Exception {
        try {
            JSON.parse(paramJson);
        } catch (Exception e) {
            throw new SystemException("0001", "json格式有误");
        }
        JSONObject jsonObject = JSON.parseObject(paramJson);
        String tmplateCode = jsonObject.getString(ExcelExportConstants.VO_TMPLATECODE);
        if (Objects.equals(tmplateCode, ExcelExportConstants.VO_F00000)) {//F00000 为配置示例
            throw new SystemException("0001", "tmplateCode不能为F00000");
        }
        String aPackage = this.getClass().getPackage().getName();
        Class<?> aClass;
        try {
            aClass = Class.forName(aPackage + "." + tmplateCode);
        } catch (ClassNotFoundException e) {
            aClass = Class.forName(aPackage + "." + ExcelExportConstants.VO_F00000);//没有对应类  则使用F00000
        }
        Object object = JSONObject.parseObject(paramJson, aClass);
        F00000 epVO = (F00000) object;
        //vo 中设置 xml 解析的信息
        Element el = ExcelExportUtil.getElement(tmplateCode);
        if (el == null) {
            throw new SystemException("0001", "tmplateCode" + tmplateCode + "无效");
        }
        String type = el.element(ExcelExportConstants.TABLE_TYPE).getText();
        epVO.setTmplateCode(tmplateCode);
        if (epVO.getFilesName() == null) {
            String filesName = el.element(ExcelExportConstants.TABLE_FILESNAME).getText();
            epVO.setFilesName(filesName);
        }
        if (epVO.getTableName() == null) {
            String tableName = el.element(ExcelExportConstants.TABLE_TABLENAME).getText();
            epVO.setTableName(tableName);
        }
        if (epVO.getSheetName() == null) {
            String sheetName = el.element(ExcelExportConstants.TABLE_SHEETNAME).getText();
            epVO.setSheetName(sheetName);
        }
        epVO.setCompanyId(Integer.parseInt(ymlConfig.getCompanyId()));
//        epVO.setCs(this.getCs());  导出名称加上当前登录用户
        //校验入参
        epVO.check();
        epVO.extCheck();
        //校验成功 设置vo
        this.vo = epVO;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public F00000 getVo() {
        return vo;
    }

    public void setVo(F00000 vo) {
        this.vo = vo;
    }

}
