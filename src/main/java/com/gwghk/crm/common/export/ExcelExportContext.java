package com.gwghk.crm.common.export;

import com.gwghk.crm.check.vo.export.DataCellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExportContext {
    //xml 解析的通用变量
    private String tmplateCode; //模板编号
    private Integer type; //模板类型   1 固定表头 2 一层动态  3 两层动态
    private Element structure;//表结构
    private Element structureExt;//额外结构
    private String firstHead;//第一列
    private String flexHead;//一层动态
    private String flexHead2;//二层动态
    private Integer headRowCount = 0;  //表头的行数
    //下面的是每张报表通用的变量
    private Integer headRowEnd;//表头使用列(下次新增表头 使用此列)
    private Integer headUsedCol = 0;//表头使用列(下次新增表头 使用此列)
    private Integer headUsedRow = -1;//表头使用行 已经使用到了此行
    private Integer dataUsedRow;//数据使用行 已经使用到了此行
    private Integer dataUsedRowExount = 0;
    private Integer readCount = 0;//读了几条数据
    private SXSSFWorkbook workbook = new SXSSFWorkbook();
    private Map<String, Integer> indexList = new HashMap<>();// 记录 第一列的行数  其他列的列数 动态列父表头的起始列
    private Map<String, DataCellStyle> dataCellStyleMap = new HashMap<>();//记录 每列的 条件等式不等式 以及相应的单元格样式   key = 节点名称/字段名称    vlaue = if
    private Map<String, String> nodeKeyMap = new HashMap<>();//记录  生成节点名称  和 父节点名称

    public ExcelExportContext(String tmplateCode) {
        Element element = ExcelExportUtil.getElement(tmplateCode);
        this.structure = element.element(ExcelExportConstants.TABLE_STRUCTURE);
        this.structureExt = element.element(ExcelExportConstants.TABLE_STRUCTUREEXT);
        this.tmplateCode = tmplateCode;
        this.type = Integer.parseInt(element.element(ExcelExportConstants.TABLE_TYPE).getText());
        initHeadRowCount(structure, 0);
        this.headRowEnd = headRowCount - 1;
        this.dataUsedRow = headRowCount - 1;
        this.firstHead = element.element(ExcelExportConstants.TABLE_FIRSTHEAD).getText();
        if (element.element(ExcelExportConstants.TABLE_FLEXHEAD) != null) {
            this.flexHead = element.element(ExcelExportConstants.TABLE_FLEXHEAD).getText();
        }
        if (element.element(ExcelExportConstants.TABLE_FLEXHEAD2) != null) {
            this.flexHead2 = element.element(ExcelExportConstants.TABLE_FLEXHEAD2).getText();
        }
        setNodeKeyMap(structure, "root");
    }

    private void setNodeKeyMap(Element node, String parentKey) {
        //当前节点的名称、文本内容和属性
        String name = node.getName();
        nodeKeyMap.put(name, parentKey);
        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            this.setNodeKeyMap(e, name);//递归
        }
    }

    /**
     * 解析出 模板的表结构 表头行数
     * @param node
     * @param i
     * @return
     */
    private Integer initHeadRowCount(Element node, Integer i) {
        System.out.println(node.getName());
        if (node.hasMixedContent()) {
            i++;
            if (i > headRowCount) {
                headRowCount = i;
            }
            List<Element> listElement = node.elements();
            for (Element e : listElement) {
                System.out.println("递归" + e.getName());
                Integer j = this.initHeadRowCount(e, i);
                if (j > headRowCount) {
                    headRowCount = i;
                }
            }
        }
        return i;
    }

    public String getTmplateCode() {
        return tmplateCode;
    }

    public void setTmplateCode(String tmplateCode) {
        this.tmplateCode = tmplateCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Element getStructure() {
        return structure;
    }

    public void setStructure(Element structure) {
        this.structure = structure;
    }

    public Element getStructureExt() {
        return structureExt;
    }

    public void setStructureExt(Element structureExt) {
        this.structureExt = structureExt;
    }

    public String getFirstHead() {
        return firstHead;
    }

    public void setFirstHead(String firstHead) {
        this.firstHead = firstHead;
    }

    public String getFlexHead() {
        return flexHead;
    }

    public void setFlexHead(String flexHead) {
        this.flexHead = flexHead;
    }

    public String getFlexHead2() {
        return flexHead2;
    }

    public void setFlexHead2(String flexHead2) {
        this.flexHead2 = flexHead2;
    }

    public Integer getHeadRowCount() {
        return headRowCount;
    }

    public void setHeadRowCount(Integer headRowCount) {
        this.headRowCount = headRowCount;
    }

    public Integer getHeadRowEnd() {
        return headRowEnd;
    }

    public void setHeadRowEnd(Integer headRowEnd) {
        this.headRowEnd = headRowEnd;
    }

    public Integer getHeadUsedCol() {
        return headUsedCol;
    }

    public void setHeadUsedCol(Integer headUsedCol) {
        this.headUsedCol = headUsedCol;
    }

    public Integer getHeadUsedRow() {
        return headUsedRow;
    }

    public void setHeadUsedRow(Integer headUsedRow) {
        this.headUsedRow = headUsedRow;
    }

    public Integer getDataUsedRow() {
        return dataUsedRow;
    }

    public void setDataUsedRow(Integer dataUsedRow) {
        this.dataUsedRow = dataUsedRow;
    }

    public Integer getDataUsedRowExount() {
        return dataUsedRowExount;
    }

    public void setDataUsedRowExount(Integer dataUsedRowExount) {
        this.dataUsedRowExount = dataUsedRowExount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(SXSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public Map<String, Integer> getIndexList() {
        return indexList;
    }

    public void setIndexList(Map<String, Integer> indexList) {
        this.indexList = indexList;
    }

    public Map<String, DataCellStyle> getDataCellStyleMap() {
        return dataCellStyleMap;
    }

    public void setDataCellStyleMap(Map<String, DataCellStyle> dataCellStyleMap) {
        this.dataCellStyleMap = dataCellStyleMap;
    }

    public Map<String, String> getNodeKeyMap() {
        return nodeKeyMap;
    }

    public void setNodeKeyMap(Map<String, String> nodeKeyMap) {
        this.nodeKeyMap = nodeKeyMap;
    }
}
