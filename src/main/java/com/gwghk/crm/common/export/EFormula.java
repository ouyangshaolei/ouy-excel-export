package com.gwghk.crm.common.export;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dom4j.Element;

import java.util.Map;
import java.util.Objects;

public enum EFormula {
    /**
     * 总计  平均的行数使用了dataUsedRow,数据填写完毕后dataUsedRow 不应该再改变
     * 所以添加了 dataUsedRowExtCount字段
     */
    TOTAL(ExcelExportConstants.EXT_HEAD_SUM) {
        @Override
        public ExcelExportContext setFormula(ExcelExportContext context, Integer headRowEnd, SXSSFSheet sheet, Element node) {
            Integer dataUsedRow = context.getDataUsedRow();
            Integer headUsedCol = context.getHeadUsedCol();
            Integer dataUsedRowExtCount = context.getDataUsedRowExount();
            SXSSFRow row = sheet.createRow(dataUsedRow + dataUsedRowExtCount + 1);//dataUsedRow不在变化  含增加通过dataUsedRowExtCount 实现
            SXSSFCell headCell = row.createCell(0);
            headCell.setCellValue(node.attributeValue(ExcelExportConstants.HEAD_NAME));
            ExcelExportUtil.setHeadStyleOfFormula(context, node, headCell);//使用每统一的样式
            for (int i = 1; i < headUsedCol; i++) { //从第二列开始求 列求和
                setSum(context, headRowEnd, node, dataUsedRow, row, i);
            }
            context.setDataUsedRowExount(++dataUsedRowExtCount);
            return context;
        }
    }, AVERAGE(ExcelExportConstants.EXT_HEAD_AVERAGE) {
        @Override
        public ExcelExportContext setFormula(ExcelExportContext context, Integer headRowEnd, SXSSFSheet sheet, Element node) {
            Integer dataUsedRow = context.getDataUsedRow();
            Integer headUsedCol = context.getHeadUsedCol();
            Integer dataUsedRowExtCount = context.getDataUsedRowExount();
            SXSSFRow row = sheet.createRow(dataUsedRow + dataUsedRowExtCount + 1);//dataUsedRow不在变化  含增加通过dataUsedRowExtCount 实现
            SXSSFCell headCell = row.createCell(0);
            headCell.setCellValue(node.attributeValue(ExcelExportConstants.HEAD_NAME));
            ExcelExportUtil.setHeadStyleOfFormula(context, node, headCell);//使用每统一的样式
            for (int i = 1; i < headUsedCol; i++) { //从第二列开始求 列求和
                setAverage(context, headRowEnd, node, dataUsedRow, row, i);
            }
            context.setDataUsedRowExount(++dataUsedRowExtCount);
            return context;
        }
    }, MIXFORMULA(ExcelExportConstants.EXT_HEAD_MIXFORMULA) {
        //混合函数  每一列可以定义不同的函数   包含上面的average  sum
        @Override
        public ExcelExportContext setFormula(ExcelExportContext context, Integer headRowEnd, SXSSFSheet sheet, Element node) {
            String firstHead = context.getFirstHead();
            Integer dataUsedRow = context.getDataUsedRow();
            Integer headUsedCol = context.getHeadUsedCol();
            Map<String, Integer> indexList = context.getIndexList();
            Element structure = context.getStructure();
            Integer dataUsedRowExtCount = context.getDataUsedRowExount();
            int rownum = dataUsedRow + dataUsedRowExtCount + 1;
            SXSSFRow row = sheet.createRow(rownum);//dataUsedRow不在变化  含增加通过dataUsedRowExtCount 实现
            SXSSFCell headCell = row.createCell(0);
            headCell.setCellValue(node.attributeValue(ExcelExportConstants.HEAD_NAME));
            ExcelExportUtil.setHeadStyleOfFormula(context, node, headCell);//使用每个字段的样式
            for (int i = 1; i < headUsedCol; i++) { //从第二列开始求 列求和
                String key = null;//当前列对应的key
                Element elementByKey = null;//当前列对应的key 对一个的节点信息
                for (Map.Entry<String, Integer> entry : indexList.entrySet()) {
                    if (i == entry.getValue() && !entry.getKey().contains(firstHead)) {//找到对应列的key(indexList 记录了首列的行号 对第一个判断==有干扰)
                        key = entry.getKey();
                        elementByKey = getElementByKey(structure, key);
                    }
                }
                String formula = elementByKey.attributeValue(ExcelExportConstants.HEAD_FORMULA);// average   sum ......
                if (formula == null) {//没有函数
                    continue;
                }
                switch (formula) {
                    case ExcelExportConstants.EXT_HEAD_AVERAGE:
                        setAverage(context, headRowEnd, elementByKey, dataUsedRow, row, i);
                        break;
                    case ExcelExportConstants.EXT_HEAD_SUM:
                        setSum(context, headRowEnd, elementByKey, dataUsedRow, row, i);
                        break;
                    case ExcelExportConstants.HEAD_FORMULA_FORMULAEXT001:
                        formulaExtF00001(context, elementByKey, row, i, key);
                        break;
                    case ExcelExportConstants.HEAD_FORMULA_FORMULAEXT002:
                        formulaExtF00002(context, elementByKey, row, i);
                        break;
                    default:
                        break;
                }
            }
            context.setDataUsedRowExount(++dataUsedRowExtCount);
            return context;
        }
    };

    /**
     * template = F00001 的特殊计算规则
     *
     * @param context
     * @param node
     * @param row
     * @param i
     * @param key
     */
    private static void formulaExtF00001(ExcelExportContext context, Element node, SXSSFRow row, int i, String key) {
        String substring = key.substring(0, key.lastIndexOf("|") + 1);
        String totalKey;
        if (key.endsWith("firstFollowValidRate")) { //F00001 F00002a首次有效二次跟进率  的分母不是total
            totalKey = substring + "firstFollowValidTotal";
        } else {
            totalKey = substring + "total";
        }
        Map<String, Integer> indexList = context.getIndexList();
        Integer totalCol = indexList.get(totalKey);
        SXSSFCell dataCell = row.createCell(i);//设置公式前，一定要先建立表格
        //设置样式  总计  平均值
        ExcelExportUtil.setDataStyleOfFormula(context, node, dataCell);
        int space;
        if (Objects.equals(context.getTmplateCode(), "F00002a")) {//F00002a  和 F00002  F00001  excel表结构有区别
            space = 4;//根据 跟进报表格式 这里-4
        } else {
            space = 5;//根据 跟进报表格式 这里-5
        }
        String colString = CellReference.convertNumToColString(i - space) + (row.getRowNum() + 1);  //长度转成ABC列
        String totalColStr = CellReference.convertNumToColString(totalCol) + (row.getRowNum() + 1);  //长度转成ABC列
        String sumstring = colString + "/" + totalColStr;//跟进次数  除以 总数
        System.out.println(sumstring);
        dataCell.setCellFormula(sumstring);
    }

    /**
     * template = F00002 的特殊计算规则
     *
     * @param context
     * @param node
     * @param row
     * @param i
     */
    private static void formulaExtF00002(ExcelExportContext context, Element node, SXSSFRow row, int i) {
        SXSSFCell dataCell = row.createCell(i);//设置公式前，一定要先建立表格
        ExcelExportUtil.setDataStyleOfFormula(context, node, dataCell);
        String colString = CellReference.convertNumToColString(1) + (row.getRowNum() + 1);  //长度转成ABC列
        String totalColStr = CellReference.convertNumToColString(2) + (row.getRowNum() + 1);  //长度转成ABC列
        String sumstring = colString + "/" + totalColStr;//跟进次数  除以 总数
        System.out.println(sumstring);
        dataCell.setCellFormula(sumstring);
    }

    private static void setSum(ExcelExportContext context, Integer headRowEnd, Element node, Integer dataUsedRow, SXSSFRow row, int i) {
        SXSSFCell dataCell = row.createCell(i);//设置公式前，一定要先建立表格
        //设置样式  总计  平均值
        ExcelExportUtil.setDataStyleOfFormula(context, node, dataCell);
        String colString = CellReference.convertNumToColString(i);  //长度转成ABC列
        int i1 = headRowEnd + 2;//SUM(B1:B5)    1 和5 表示的是execel 看到的第一行  第五行   与其他逻辑不同(行列从0开始)
        int i2 = dataUsedRow + 1;//dataUsedRow - dataUsedRowExtCount 不统计其他统计求和所在的行
        String sumstring = "SUM(" + colString + i1 + ":" + colString + i2 + ")";//求和公式
        dataCell.setCellFormula(sumstring);
    }

    private static void setAverage(ExcelExportContext context, Integer headRowEnd, Element node, Integer dataUsedRow, SXSSFRow row, int i) {
        SXSSFCell dataCell = row.createCell(i);//设置公式前，一定要先建立表格
        ExcelExportUtil.setDataStyleOfFormula(context, node, dataCell);
        String colString = CellReference.convertNumToColString(i);  //长度转成ABC列
        int i1 = headRowEnd + 2;//SUM(B1:B5)    1 和5 表示的是execel 看到的第一行  第五行   与其他逻辑不同(行列从0开始)
        int i2 = dataUsedRow + 1;//dataUsedRow - dataUsedRowExtCount 不统计其他统计求和所在的行
        String range = "(" + colString + i1 + ":" + colString + i2 + ")";
        String sumstring = "SUM" + range + "/(COUNTBLANK" + range + "+" + "COUNTA" + range + ")";//AVERAGE 不统计空单元格 所用用sum 取平均值
        dataCell.setCellFormula(sumstring);
    }

    private static Element getElementByKey(Element structure, String key) {
        String[] split = key.split("\\|");
        for (int i = 0; i < split.length; i++) {
            if (i % 2 == 0) {// 0 和 偶数
                String nodeKey = split[i];
                structure = structure.element(nodeKey);
            }
        }
        return structure;
    }

    private String type;

    EFormula(String t) {
        this.type = t;
    }

    public static EFormula getType(String t) {
        for (EFormula c : EFormula.values()) {
            if (Objects.equals(c.getType(), t)) {
                return c;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public abstract ExcelExportContext setFormula(ExcelExportContext context, Integer headRowEnd, SXSSFSheet sheet, Element node);


}
