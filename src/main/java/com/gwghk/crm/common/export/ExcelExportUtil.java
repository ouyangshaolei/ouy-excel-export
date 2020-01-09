package com.gwghk.crm.common.export;

import com.gwghk.crm.check.vo.export.DataCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * @Author: ouyangshaolei
 * @Date: 2019/7/4 14:56
 */
@Service
public class ExcelExportUtil {
    // 正则匹配所有 整数小数    (填入数据时数字不能以文字形式填入)
    private static final Pattern pattern = Pattern.compile("^(([^0][0-9]+|0)\\.([0-9]*)$)|^(([^0][0-9]+|0)$)|^(([1-9]+)\\.([0-9]*)$)|^(([1-9]+)$)");
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * 设置 公式求 和 平均值 的样式
     * 其他地方设置样式会导致 精度等样式失效
     *
     * @param context
     * @param node
     * @param dataCell
     * @return
     */
    public static void setDataStyleOfFormula(ExcelExportContext context, Element node, SXSSFCell dataCell) {
        SXSSFWorkbook workbook = context.getWorkbook();
        String pr = node.attributeValue(ExcelExportConstants.HEAD_PRECISION); //小数精度
        String pe = node.attributeValue(ExcelExportConstants.HEAD_PERCENT); //百分数精度
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        //设置边框
        setAllBorderStyle(cellStyle);
        //设置 背景 字体颜色
        String fillColor = node.attributeValue(ExcelExportConstants.HEAD_DATAFILLCOLOR);//data  背景颜色
        String fontColor = node.attributeValue(ExcelExportConstants.HEAD_DATAFONTCOLOR);//data  字体颜色

        //背景景颜色
        if (fillColor != null) {
            cellStyle.setFillForegroundColor(fromStrToARGB(fillColor));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        //设置字体 颜色 大小
        XSSFFont font = (XSSFFont) workbook.createFont();
        if (fontColor != null) {
            font.setColor(fromStrToARGB(fontColor));
        }
        cellStyle.setFont(font);
        //设置精度
        if (pr != null) {
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(pr)); //例如   p = 0.00
        }
        if (pe != null) {
            cellStyle.setDataFormat(workbook.createDataFormat().getFormat(pe));
        }
        dataCell.setCellStyle(cellStyle);
    }

    public static void setHeadStyleOfFormula(ExcelExportContext context, Element node, SXSSFCell dataCell) {
        SXSSFWorkbook workbook = context.getWorkbook();
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        //设置边框
        setAllBorderStyle(cellStyle);
        //设置 背景 字体颜色
        String fillColor = node.attributeValue(ExcelExportConstants.HEAD_FILLCOLOR);//表头  背景颜色
        String fontColor = node.attributeValue(ExcelExportConstants.HEAD_FONTCOLOR);//表头  字体颜色
        //背景景颜色
        if (fillColor != null) {
            cellStyle.setFillForegroundColor(fromStrToARGB(fillColor));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        //设置字体 颜色 大小
        XSSFFont font = (XSSFFont) workbook.createFont();
        if (fontColor != null) {
            font.setColor(fromStrToARGB(fontColor));
        }
        cellStyle.setFont(font);
        dataCell.setCellStyle(cellStyle);
    }

    private static void setAllBorderStyle(XSSFCellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
    }

    /**
     * 填充数据 设置样式
     *
     * @param context
     * @param key     xml节点名称
     * @param value   数据值
     * @param cell    单元格对象
     */
    public static void cellFillData(ExcelExportContext context, String key, Object value, SXSSFCell cell) {
        if (key != null) {
            String s = value == null ? "" : value.toString();
            boolean matches = pattern.matcher(s).matches();
            if (matches) {
                cell.setCellValue(Double.parseDouble(s));
            } else {
                cell.setCellValue(s);
            }
        }
        System.out.println("行_列_value:" + cell.getRowIndex() + "_" + cell.getColumnIndex() + ":" + value);
        //设置通用样式
        //满足条件 改变单元格颜色或者字体颜色
        changeCellStyle(context, key, value, cell);
    }

    /**
     * 处理高亮  字体变色
     *
     * @param context
     * @param key
     * @param valueObj
     * @param cell
     */
    private static void changeCellStyle(ExcelExportContext context, String key, Object valueObj, SXSSFCell cell) {
        Map<String, DataCellStyle> dataCellStyleMap = context.getDataCellStyleMap();
        DataCellStyle dataCellStyle = dataCellStyleMap.get(key);
        XSSFCellStyle ordinaryCellStyle = dataCellStyle.getOrdinaryCellStyle();
        if (dataCellStyle != null && dataCellStyle.getIfSymbol() != null) {
            String value = valueObj == null ? null : valueObj.toString();
            String symbol = dataCellStyle.getIfSymbol();
            String target = dataCellStyle.getIfTarget();
            XSSFCellStyle conditionCellStyle = dataCellStyle.getConditionCellStyle();
            boolean flag = false;
            assert value != null;
            boolean matches = pattern.matcher(value).matches();
            if (matches) {
                //如果是比较 目标中含有% 则先去除%
                if (target.contains("%")) {
                    value = value.replaceAll("%", "");
                    target = target.replaceAll("%", "");
                }
                BigDecimal valueB = new BigDecimal(value);
                BigDecimal targetB = new BigDecimal(target);
                // 1  0  -1  大于  等于  小于
                int i = valueB.compareTo(targetB);
                switch (symbol) {
                    case "=":
                        if (i == 0) {
                            flag = true;
                        }
                        break;
                    case ">":
                        if (i > 0) {
                            flag = true;
                        }
                        break;
                    case ">=":
                        if (i >= 0) {
                            flag = true;
                        }
                        break;
                    case "<":
                        if (i < 0) {
                            flag = true;
                        }
                        break;
                    case "<=":
                        if (i <= 0) {
                            flag = true;
                        }
                        break;
                    default:
                        System.out.println("无效的符号");
                        break;
                }
            } else {//字符串判断
                switch (symbol) {
                    case "=":
                        if (Objects.equals(value, target)) {
                            flag = true;
                        }
                    case "!=":
                        if (Objects.equals(value, target)) {
                            flag = true;
                        }
                    default:
                        System.out.println("无效的符号");
                }
            }
            if (flag) {
                cell.setCellStyle(conditionCellStyle);
            }
        } else {
            cell.setCellStyle(ordinaryCellStyle);
        }
    }

    /**
     * 额外列:指 动态表头模式中,非动态列(除第一行),这种数据需要单独查出再根据第一列对应写入
     * <p>
     * 动态表头模式
     * 创建表头 同时插入数据
     */
    public static ExcelExportContext createHeadAndFillData(ExcelExportContext context, Integer headRowStart, Integer headRowEnd, SXSSFSheet sheet, Element structureElement, Map<String, Object> map) {
        String firstHead = context.getFirstHead();
        String flexHead = context.getFlexHead();
        Integer headUsedRow = context.getHeadUsedRow();
        Integer dataUsedRow = context.getDataUsedRow();
        Integer headUsedCol = context.getHeadUsedCol();
        Integer readCount = context.getReadCount();
        Iterator iterator = structureElement.elementIterator();
        SXSSFRow headRow;
        if (headRowStart <= headUsedRow) {//不能重复create 同一行
            headRow = sheet.getRow(headRowStart);
        } else {
            headRow = sheet.createRow(headRowStart);
            context.setHeadUsedRow(headRowStart);
        }
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            String key = node.getName();//xml  节点名称   字段名  <key></key>
            int size = node.elements().size();//子节点 数量  决定表头的合并单元格的列数
            String parentNodeName = node.getParent().getName();// 父节点  名称
            String head_name = node.attributeValue(ExcelExportConstants.HEAD_NAME);
            String columnWidth = node.attributeValue(ExcelExportConstants.HEAD_COLUMNWIDTH);
            boolean needHeadUsedColAdd = false;
            //不含子表头
            Map<String, Integer> indexList = context.getIndexList();
            if (!node.hasMixedContent()) {
                //1. 设置表头-----------------------------------------------------
                SXSSFCell headCell;
                //!!!!!!!这个判断的目的是, 类似第一列的这种非动态列不能重复创建的表头(靠其他部分逻辑填充数据)!!!!!
                //不是第一条数据  且 此节点没有子节点 且 没有父节点  则不根据节点创建head
                //flexHead 没有子节点满足if
                if (!(Objects.equals(parentNodeName, ExcelExportConstants.TABLE_STRUCTURE) && readCount > 0) || Objects.equals(key, flexHead)) {
                    //处理 条件 等式 不等式样式
                    initDataCellStyle(context, node, key);
                    headCell = headRow.createCell(headUsedCol);
                    //设置表头样式
                    CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                    headCell.setCellStyle(cellStyle);
                    //设置表头名称
                    if (Objects.equals(key, flexHead)) {
                        headCell.setCellValue(map.get(key) == null ? "" : map.get(key).toString());
                    } else {
                        headCell.setCellValue(head_name);
                    }
                    setColumWidthByCol(sheet, headUsedCol, columnWidth);
                    //记录每一列的位置  除了第一列(第一列记录的是行数)
                    String filesKey; //写入数据在哪一行写 只需根据第一列字段判断
                    if (!Objects.equals(key, firstHead)) {
                        // 父节点值_此节点名称   例如  musk_phone
                        filesKey = flexHead + "|" + map.get(flexHead) + "|" + key;
                    } else {
                        filesKey = key;
                    }
                    indexList.put(filesKey, headUsedCol);
                    //合并表头单元格(行)
                    if (headRowEnd > 0 & headRowEnd > headRowStart) {//超过一行
                        CellRangeAddress region = new CellRangeAddress(headRowStart, headRowEnd, headUsedCol, headUsedCol);
                        sheet.addMergedRegion(region);
                    }
                    needHeadUsedColAdd = true;
                    //记录额外列
                }
                //2. 写入正在读取的这条数据--------------------------------------------------------
                String firstHeadRowCountKey = firstHead + "|" + map.get(firstHead); //写入数据在哪一行写 只需根据第一列字段判断
                SXSSFRow dataRow;
                if (indexList.get(firstHeadRowCountKey) != null) {
                    dataRow = sheet.getRow(indexList.get(firstHeadRowCountKey));//!!!!!这里不能用createRow 否则会清空之前本行的数据
                } else {
                    context.setDataUsedRow(++dataUsedRow);
                    dataRow = sheet.createRow(dataUsedRow);
                    indexList.put(firstHeadRowCountKey, dataUsedRow);
                    SXSSFCell cell = dataRow.createCell(0);
                    String value = map.get(firstHead).toString();
                    cellFillData(context, firstHead, value, cell);//填入首列 数据
                }
                SXSSFCell dataCell = dataRow.createCell(headUsedCol);
                String value = map.get(key) == null ? "" : map.get(key).toString();
                cellFillData(context, key, value, dataCell);
                if (needHeadUsedColAdd) {
                    context.setHeadUsedCol(++headUsedCol);//下一个字段
                }
                context.setIndexList(indexList);
            } else {//递归-------------------------------------------------
                //处理 条件 等式 不等式样式
                initDataCellStyle(context, node, key);
                //设置表头文字
                SXSSFCell headCell = headRow.createCell(headUsedCol);
                //设置表头样式
                CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                headCell.setCellStyle(cellStyle);
                //设置表头名称
                String value = map.get(node.getName()).toString();
                headCell.setCellValue(value);
                //单元格 自适应宽度
                setColumWidthByCol(sheet, headUsedCol, columnWidth);
                //合并单元格  (列)
                int lastCol = headUsedCol + size - 1;
                if (headUsedCol < lastCol) {
                    CellRangeAddress region = new CellRangeAddress(headRowStart, headRowStart, headUsedCol, lastCol);
                    sheet.addMergedRegion(region);
                }
                headRowStart++;//子表头 行+1
                createHeadAndFillData(context, headRowStart, headRowEnd, sheet, node, map);
            }
        }
        return context;
    }

    private static void setColumWidthByCol(SXSSFSheet sheet, Integer colNum, String columnWidth) {
        if (columnWidth != null) {
            int i = Integer.parseInt(columnWidth);
            sheet.setColumnWidth(colNum, (2 * i + 1) * 256);
        }
    }

    /**
     * @param firstFlexHeadExist 一级动态列是否已存在
     * @param context
     * @param headRowStart
     * @param headRowEnd
     * @param sheet
     * @param structureElement
     * @param map
     * @return
     */
    public static ExcelExportContext createHeadAndFillDataDuble(boolean firstFlexHeadExist, ExcelExportContext context, Integer headRowStart, Integer headRowEnd, SXSSFSheet sheet, Element structureElement, Map<String, Object> map) {
        String firstHead = context.getFirstHead();
        String flexHead = context.getFlexHead();
        String flexHead2 = context.getFlexHead2();
        Integer headUsedRow = context.getHeadUsedRow();
        Integer dataUsedRow = context.getDataUsedRow();
        Integer headUsedCol = context.getHeadUsedCol();
        Integer readCount = context.getReadCount();
        Iterator iterator = structureElement.elementIterator();
        Map<String, String> nodeKeyMap = context.getNodeKeyMap();
        SXSSFRow headRow;
        if (headRowStart <= headUsedRow) {//不能重复create 同一行
            headRow = sheet.getRow(headRowStart);
        } else {
            headRow = sheet.createRow(headRowStart);
            context.setHeadUsedRow(headRowStart);
        }
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            String key = node.getName();//xml  节点名称   字段名  <key></key>
            int size = node.elements().size();//子节点 数量  决定表头的合并单元格的列数
            String parentNodeName = node.getParent().getName();// 父节点  名称
            String head_name = node.attributeValue(ExcelExportConstants.HEAD_NAME);
            String columnWidth = node.attributeValue(ExcelExportConstants.HEAD_COLUMNWIDTH);
            boolean needHeadUsedColAdd = false;
            String pKey = nodeKeyMap.get(key);
            Map<String, Integer> indexList = context.getIndexList();
            if (!node.hasMixedContent()) {
                //1. 设置表头-----------------------------------------------------
                SXSSFCell headCell;
                // 不是第一条数据   一级非动态列  和 一级动态列下的非动态列不再无限添加
                if (!(Objects.equals(parentNodeName, ExcelExportConstants.TABLE_STRUCTURE) && readCount > 0)
                        && !(Objects.equals(pKey, flexHead) && flexHead2 != null && firstFlexHeadExist && readCount > 0)) {
                    //处理 条件 等式 不等式样式
                    initDataCellStyle(context, node, key);
                    headCell = headRow.createCell(headUsedCol);
                    //设置表头样式
                    CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                    headCell.setCellStyle(cellStyle);
                    //设置表头名称
                    if (Objects.equals(key, flexHead) || Objects.equals(key, flexHead2)) {// TODO: 2019/11/20 应该用不到  动态表头到不了这个逻辑
                        headCell.setCellValue(map.get(key) == null ? "" : map.get(key).toString());
                    } else {
                        headCell.setCellValue(head_name);
                        System.out.println("行_列_value:" + headCell.getRowIndex() + "|" + headCell.getColumnIndex() + ":" + head_name);
                    }
                    //单元格 自适应宽度
                    setColumWidthByCol(sheet, headUsedCol, columnWidth);
                    //记录表头位置(每一列)  !!!!!除了第一列(第一列记录的是行数)
                    String filedKey;
                    if (Objects.equals(flexHead, pKey)) {
                        filedKey = flexHead + "|" + map.get(flexHead) + "|" + key;
                    } else if (Objects.equals(flexHead2, pKey)) {
                        filedKey = flexHead + "|" + map.get(flexHead) + "|" + flexHead2 + "|" + map.get(flexHead2) + "|" + key;
                    } else {
                        filedKey = key;
                    }
                    indexList.put(filedKey, headUsedCol);
                    //合并表头单元格(行)   !!!非动态列需要合并行
                    if (headRowEnd > 0 & headRowEnd > headRowStart) {//超过一行
                        CellRangeAddress region = new CellRangeAddress(headRowStart, headRowEnd, headUsedCol, headUsedCol);
                        System.out.println("合并行:" + headRowStart + "_" + headRowEnd + "_" + headUsedCol + "_" + headUsedCol);
                        sheet.addMergedRegion(region);
                    }
                    needHeadUsedColAdd = true;
                    //记录额外列
                }
                //2. 写入正在读取的这条数据--------------------------------------------------------
                String firstHeadRowCountKey = firstHead + "|" + map.get(firstHead); //写入数据在哪一行写 只需根据第一列字段判断
                SXSSFRow dataRow;
                if (indexList.get(firstHeadRowCountKey) != null) {
                    dataRow = sheet.getRow(indexList.get(firstHeadRowCountKey));//!!!!!这里不能用createRow 否则会清空之前本行的数据
                } else {
                    context.setDataUsedRow(++dataUsedRow);
                    dataRow = sheet.createRow(dataUsedRow);
                    indexList.put(firstHeadRowCountKey, dataUsedRow);
                    SXSSFCell cell = dataRow.createCell(0);
                    String value = map.get(firstHead).toString();
                    cellFillData(context, firstHead, value, cell);//填入首列 数据
                }
                SXSSFCell dataCell = dataRow.createCell(headUsedCol);
                String value = map.get(key) == null ? "" : map.get(key).toString();
                cellFillData(context, key, value, dataCell);
                if (needHeadUsedColAdd) {
                    context.setHeadUsedCol(++headUsedCol);//下一个字段
                }
                context.setIndexList(indexList);
            } else {//递归-------------------------------------------------
                if (firstFlexHeadExist) {//一级动态列下增加了新的列  重新合并父级动态列
                    String i = flexHead + "|" + map.get(flexHead);
                    Integer parentNodeColNum = getParentNodeColNum(indexList, i);
                    //1.拆分原有合并单元格
                    Integer oldMergedColEnd = splitMergedRegion(sheet, 0, parentNodeColNum);
                    //2.合并新的单元格
                    CellRangeAddress region = new CellRangeAddress(0, 0, parentNodeColNum, oldMergedColEnd + 1);
                    System.out.println("合并列:" + 0 + "_" + 0 + "_" + parentNodeColNum + "_" + (oldMergedColEnd + 1));
                    sheet.addMergedRegion(region);
                }
                //处理 条件 等式 不等式样式
                initDataCellStyle(context, node, key);
                //设置表头文字
                SXSSFCell headCell = headRow.createCell(headUsedCol);
                //设置表头样式
                CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                headCell.setCellStyle(cellStyle);
                //设置表头名称
                String value = map.get(node.getName()).toString();
                headCell.setCellValue(value);
                //单元格 自适应宽度
                setColumWidthByCol(sheet, headUsedCol, columnWidth);
                System.out.println("递归:行_列_value:" + headCell.getRowIndex() + "_" + headCell.getColumnIndex() + ":" + value);
                //合并单元格  (列)
                int lastCol = headUsedCol + size - 1;
                if (headUsedCol < lastCol) {
                    CellRangeAddress region = new CellRangeAddress(headRowStart, headRowStart, headUsedCol, lastCol);
                    System.out.println("合并列:" + headRowStart + "_" + headRowStart + "_" + headUsedCol + "_" + lastCol);
                    sheet.addMergedRegion(region);
                }
                headRowStart++;//子表头 行+1
                createHeadAndFillDataDuble(false, context, headRowStart, headRowEnd, sheet, node, map);
            }
        }
        return context;
    }

    private static Integer getParentNodeColNum(Map<String, Integer> indexList, String i) {
        Integer num = null;
        for (String s : indexList.keySet()) {
            if (s.startsWith(i)) {
                Integer col = indexList.get(s);
                if (num == null || col < num) {
                    num = col;
                }
            }
        }
        return num;
    }

    private static Integer splitMergedRegion(SXSSFSheet sheet, int row, int col) {
        //遍历sheet中的所有的合并区域
        for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() == row && region.getFirstColumn() == col) {
                System.out.println("要拆:" + row + col);
                System.out.println(region);
                System.out.println("拆分单元格:" + region.getFirstRow() + "_" + region.getLastRow() + "_" + region.getFirstColumn() + "_" + region.getLastColumn());
                sheet.removeMergedRegion(i);
                return region.getLastColumn();
            }
        }
        return null;
    }

    /**
     * 固定表头模式  创建表头   添加样式集合
     *
     * @param context
     * @param headRowStart     表头开始行  从0开始  递归后变化
     * @param headRowEnd       表头结束行  xml 指定
     * @param sheet
     * @param structureElement 表结构dom对象
     */
    public static ExcelExportContext createFixedHead(ExcelExportContext context, Integer headRowStart, Integer headRowEnd, SXSSFSheet sheet, Element structureElement) {
        Integer headUsedRow = context.getHeadUsedRow();
        Integer headUsedCol = context.getHeadUsedCol();
        Iterator iterator = structureElement.elementIterator();
        SXSSFRow headRow;
        if (headRowStart <= headUsedRow) {//不能重复create 同一行
            headRow = sheet.getRow(headRowStart);
        } else {
            headRow = sheet.createRow(headRowStart);
            context.setHeadUsedRow(headRowStart);
        }
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            String key = node.getName();//xml  节点名称   字段名  <key></key>
            int size = node.elements().size();//子节点 数量  决定表头的合并单元格的列数
            String head_name = node.attributeValue(ExcelExportConstants.HEAD_NAME);
            Map<String, Integer> indexList = context.getIndexList();
            if (!node.hasMixedContent()) {
                //处理 条件 等式 不等式样式
                initDataCellStyle(context, node, key);
                //设置表头-----------------------------------------------------
                SXSSFCell headCell;
                headCell = headRow.createCell(headUsedCol);
                //设置表头样式
                CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                headCell.setCellStyle(cellStyle);
                //设置表头名称
                headCell.setCellValue(head_name);
                //记录每一列的位置  除了第一列(第一列记录的是行数)
                indexList.put(key, headUsedCol);//写入数据在哪一行写 只需根据第一列字段判断
                context.setIndexList(indexList);
                //合并表头单元格(行)
                if (headRowEnd > 0 & headRowEnd > headRowStart) {//超过一行
                    CellRangeAddress region = new CellRangeAddress(headRowStart, headRowEnd, headUsedCol, headUsedCol);
                    sheet.addMergedRegion(region);
                    setMergedRegionStyle(sheet, region);
                }
                context.setHeadUsedCol(++headUsedCol);//下一个字段
            } else {//递归-------------------------------------------------
                //处理 条件 等式 不等式样式
                initDataCellStyle(context, node, key);
                //设置表头文字
                SXSSFCell headCell = headRow.createCell(headUsedCol);
                //设置表头样式
                CellStyle cellStyle = initSetHeadCellStyle(context, node, key);
                headCell.setCellStyle(cellStyle);
                //设置表头名称
                headCell.setCellValue(head_name);
                //合并单元格  (列)
                CellRangeAddress region = new CellRangeAddress(headRowStart, headRowStart, headUsedCol, headUsedCol + size - 1);
                sheet.addMergedRegion(region);
                headRowStart++;//子表头 行+1
                createFixedHead(context, headRowStart, headRowEnd, sheet, node);
            }
        }
        return context;
    }


    /**
     * 设置样式
     *
     * @param context
     * @param node
     * @param key     动态表头 和 固定表头的  传入 key 都直接用字段名做做key即可
     *                用到了两个强转类 便于直接使用rgb颜色
     */
    private static CellStyle initSetHeadCellStyle(ExcelExportContext context, Element node, String key) {
        SXSSFWorkbook workbook = context.getWorkbook();
        XSSFCellStyle headCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        String fillColor = node.attributeValue(ExcelExportConstants.HEAD_FILLCOLOR);
        String fontColor = node.attributeValue(ExcelExportConstants.HEAD_FONTCOLOR);
        //设置 边框  背景
        setAllBorderStyle(headCellStyle);
        //设置填充颜色
        if (fillColor != null) {
            //背景景颜色
            headCellStyle.setFillForegroundColor(fromStrToARGB(fillColor));
            headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        //设置字体 颜色 大小
        XSSFFont font = (XSSFFont) workbook.createFont();
        if (fontColor != null) {
            font.setColor(fromStrToARGB(fontColor));
        }
        font.setBold(true);
        headCellStyle.setFont(font);
        return headCellStyle;
    }

    /**
     * 对所有没有边框的单元格设置边框   不支持粗细自定义
     *
     * @param context
     * @param sheetName
     */
    public static void setDataBorderStyle(ExcelExportContext context, String sheetName) {
        SXSSFWorkbook workbook = context.getWorkbook();
        XSSFCellStyle dataCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        Integer headUsedCol = context.getHeadUsedCol();
        Integer dataUsedRow = context.getDataUsedRow();
        Integer headUsedRow = context.getHeadUsedRow();
        SXSSFSheet sheet = workbook.getSheet(sheetName);
        //设置 边框
        setAllBorderStyle(dataCellStyle);
        // TODO: 2019/11/11  大后期再优化吧  需要在写数据前知道 行和列的最大值
        for (int j = headUsedRow + 1; j <= dataUsedRow; j++) {
            SXSSFRow row = sheet.getRow(j);
            for (int i = 0; i < headUsedCol; i++) {
                SXSSFCell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                CellStyle cellStyle = cell.getCellStyle();
                BorderStyle borderRightEnum = cellStyle.getBorderRightEnum();
                if (borderRightEnum == BorderStyle.NONE) {//没有设置有边框  即此单元格没有用设置边
                    cell.setCellStyle(dataCellStyle);
                }
            }
        }
    }

    /**
     * 处理条件判断高亮 字体变色: 例如   该列数值大于5 字体变成红色 背景变成蓝色
     * 解读表头 初始化每列的样式
     *
     * @param context
     * @param node
     * @param key
     */
    private static ExcelExportContext initDataCellStyle(ExcelExportContext context, Element node, String key) {

        SXSSFWorkbook workbook = context.getWorkbook();
        Map<String, DataCellStyle> dataCellStyleMap = context.getDataCellStyleMap();
        String pr = node.attributeValue(ExcelExportConstants.HEAD_PRECISION); //小数精度
        String pe = node.attributeValue(ExcelExportConstants.HEAD_PERCENT); //百分数精度
        String ifSymbol = node.attributeValue(ExcelExportConstants.HEAD_IFSYMBOL);
        String ifTarget = node.attributeValue(ExcelExportConstants.HEAD_IFTARGET);
        DataCellStyle condition = new DataCellStyle();
        //1.创建普通样式ordinaryCellStyle
        XSSFCellStyle ordinaryCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        setAllBorderStyle(ordinaryCellStyle);
        if (pr != null) {
            ordinaryCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(pr)); //例如   p = 0.00
        }
        if (pe != null) {
            ordinaryCellStyle.setDataFormat(workbook.createDataFormat().getFormat(pe));
        }
        if (node.attributeValue(ExcelExportConstants.HEAD_DATAFILLCOLOR) != null) {
            String fillColor = node.attributeValue(ExcelExportConstants.HEAD_DATAFILLCOLOR);
            ordinaryCellStyle.setFillForegroundColor(fromStrToARGB(fillColor));
            ordinaryCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (node.attributeValue(ExcelExportConstants.HEAD_DATAFONTCOLOR) != null) {
            String fontColor = node.attributeValue(ExcelExportConstants.HEAD_DATAFONTCOLOR);
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setColor(fromStrToARGB(fontColor));
            ordinaryCellStyle.setFont(font);
        }
        condition.setOrdinaryCellStyle(ordinaryCellStyle);//设置普通样式
        //2.创建条件样式conditionCellStyle
        if (ifSymbol != null) {
            // TODO: 2019/10/22  后期补全校验   只执行允许的符号
            XSSFCellStyle conditionCellStyle = (XSSFCellStyle) workbook.createCellStyle();
            //设置边框
            setAllBorderStyle(conditionCellStyle);
            //设置条件样式
            if (node.attributeValue(ExcelExportConstants.HEAD_CHANGEDATAFILLCOLOR) != null) {
                String fillColor = node.attributeValue(ExcelExportConstants.HEAD_CHANGEDATAFILLCOLOR);
                conditionCellStyle.setFillForegroundColor(fromStrToARGB(fillColor));
                conditionCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            if (node.attributeValue(ExcelExportConstants.HEAD_CHANGEDATAFONTCOLOR) != null) {
                String fontColor = node.attributeValue(ExcelExportConstants.HEAD_CHANGEDATAFONTCOLOR);
                XSSFFont font = (XSSFFont) workbook.createFont();
                font.setColor(fromStrToARGB(fontColor));
                conditionCellStyle.setFont(font);
            }
            //设置精度
            if (pr != null) {
                conditionCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(pr)); //例如   p = 0.00
            }
            if (pe != null) {
                conditionCellStyle.setDataFormat(workbook.createDataFormat().getFormat(pe));
            }
            condition.setIfTarget(ifTarget);
            condition.setIfSymbol(ifSymbol);
            condition.setConditionCellStyle(conditionCellStyle);//设置条件样式
        }
        dataCellStyleMap.put(key, condition);
        context.setDataCellStyleMap(dataCellStyleMap);
        return context;
    }

    //合并后的单元格设置样式 和 未合并的设置样式有区别
    private static void setMergedRegionStyle(Sheet sheet, CellRangeAddress region) {
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
    }

    //十六进制色码转成poi 可用颜色对象
    private static XSSFColor fromStrToARGB(String hex) {
        int red = Integer.valueOf(hex.substring(1, 3), 16);
        int green = Integer.valueOf(hex.substring(3, 5), 16);
        int blue = Integer.valueOf(hex.substring(5, 7), 16);
        return new XSSFColor(new java.awt.Color(red, green, blue));
    }

    /**
     * @param tmplateCode 指定模板的根节点名称
     * @return
     */
    public static Element getElement(String tmplateCode) {
        //获取Document对象
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/excelConfig.xml");
            document = reader.read(in);
        } catch (Exception e) {
            logger.error("加载报表配置文件异常");
        }
        assert document != null;
        Element rootElement = document.getRootElement();//整个xml 对象
        if (document.getRootElement() != null && rootElement.element(tmplateCode) == null) {
//            logger.error("没有" + tmplateCode + "的相关配置");
            // TODO: 2019/10/30  throw
        }
        return rootElement.element(tmplateCode);
    }

    /**
     * 填充额外字段数据
     *
     * @param context
     * @param dataListext
     * @param sheet
     */
    public static void setExtData(ExcelExportContext context, List<Map<String, Object>> dataListext, SXSSFSheet sheet) {
        String firstHead = context.getFirstHead();
        Map<String, Integer> indexList = context.getIndexList();
        for (int i = 0; i < dataListext.size(); i++) {
            System.out.println("+++++++++++++++++++++++++++++++++第" + i + "条数据");
            Map<String, Object> map = dataListext.get(i);
            //行列 都存在     直接填写数据
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!Objects.equals(entry.getKey(), firstHead)) { //第一列已经填入数据 步用管
                    String rowKey = firstHead + "_" + map.get(firstHead); //行
                    Integer rowId = indexList.get(rowKey);
                    Integer colId = indexList.get(entry.getKey());
                    SXSSFRow extRow = sheet.getRow(rowId);
                    if (colId != null && rowId != null) {
                        SXSSFCell cell = extRow.createCell(colId);
                        ExcelExportUtil.cellFillData(context, entry.getKey(), entry.getValue(), cell);
                    }
                }
            }
        }
    }
}
