package com.gwghk.crm.common.export;

import com.alibaba.fastjson.JSON;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum EExportType {
    FIXED(ExcelExportConstants.TABLE_TYPE_1) {
        @Override
        public ExcelExportContext action(ExcelExportContext context, List<Map<String, Object>> dataList, List<Map<String, Object>> dataListext, Integer headRowEnd, Element structureElement, SXSSFSheet sheet) {
            Integer dataUsedRow = context.getDataUsedRow();
            Map<String, Integer> indexList = context.getIndexList();
            // 无动态列 所有表头按照配置写死
            ExcelExportUtil.createFixedHead(context, 0, headRowEnd, sheet, structureElement);
            for (int i = 0; i < dataList.size(); i++) {
                context.setDataUsedRow(++dataUsedRow);//每条数据另起一行
                SXSSFRow row1 = sheet.createRow(dataUsedRow);
                System.out.println("+++++++++++++++++++++++++++++++++第" + i + "条数据");
                Map<String, Object> map = dataList.get(i);
                //行列 都存在     直接填写数据
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Integer col = indexList.get(entry.getKey());
                    if (col != null) {// 有些字段并未出现在报表中 加此判断 否则空指针
                        SXSSFCell cell = row1.createCell(col);
                        ExcelExportUtil.cellFillData(context, entry.getKey(), entry.getValue(), cell);
                    }
                }
            }
            return context;
        }
    }, FLEX(ExcelExportConstants.TABLE_TYPE_2) {
        @Override
        public ExcelExportContext action(ExcelExportContext context, List<Map<String, Object>> dataList, List<Map<String, Object>> dataListext, Integer headRowEnd, Element structureElement, SXSSFSheet sheet) {
            String firstHead = context.getFirstHead();
            String flexHead = context.getFlexHead();
            Map<String, String> nodeKeyMap = context.getNodeKeyMap();
            Integer readCount = context.getReadCount();
            // 有动态列 的情况
            // ！！！！！！！！！！！！！！！！！！！！！！！有循环的时候注意 循环内部的调用其他的方法会导致context 内部的数据变化 每次context需要重新获取
            for (int i = 0; i < dataList.size(); i++) {
                Integer dataUsedRow = context.getDataUsedRow();
                Map<String, Integer> indexList = context.getIndexList();
                Map<String, Object> map = dataList.get(i);
//                System.out.println("+++++++++++++++++++++++++++++++++第" + i + "条数据:" + JSON.toJSONString(map));
                String rowKey = firstHead + "|" + map.get(firstHead); //行
                String colKey = flexHead + "|" + map.get(flexHead); //root 动态列
                boolean b = existKeyStartWith(indexList, colKey);
                if (!b) {
                    //列不存在 需要新增列
                    ExcelExportUtil.createHeadAndFillData(context, 0, headRowEnd, sheet, structureElement, map);
                } else {
                    //列 存在     直接填写数据
                    Integer row = indexList.get(rowKey);
                    if (row == null) {
                        row = dataUsedRow + 1;
                        sheet.createRow(row);
                        indexList.put(rowKey, row);
                        context.setDataUsedRow(row);//新建行 在写数据
                        context.setIndexList(indexList);
                    }
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String pKey = nodeKeyMap.get(entry.getKey());
                        if (pKey != null) {//pKey不存在说明 字段并未出现在报表中 无需写入 首列数据覆盖无所谓  省去第一次写入时的判断
                            String key = Objects.equals(flexHead, pKey) ? flexHead + "|" + map.get(flexHead) + "|" + entry.getKey() : entry.getKey();
                            Integer col = indexList.get(key);
                            if (col != null) {// flexHead 有子表头时，flexHead列也无数据可填入，indexList中不记录flexHead的列， 加此判断 否则空指针
                                SXSSFCell cell = sheet.getRow(row).createCell(col);
                                ExcelExportUtil.cellFillData(context, entry.getKey(), entry.getValue(), cell);
                            }
                        }
                    }
                }
                context.setReadCount(++readCount);
            }
            //2. 额外字段填入数据
            if (dataListext != null && dataListext.size() > 0) {
                ExcelExportUtil.setExtData(context, dataListext, sheet);
            }
            return context;
        }
    }, DOUBLEFLEX(ExcelExportConstants.TABLE_TYPE_3) {
        @Override
        public ExcelExportContext action(ExcelExportContext context, List<Map<String, Object>> dataList, List<Map<String, Object>> dataListext, Integer headRowEnd, Element structureElement, SXSSFSheet sheet) {
            String firstHead = context.getFirstHead();
            String flexHead = context.getFlexHead();
            String flexHead2 = context.getFlexHead2();
            Map<String, String> nodeKeyMap = context.getNodeKeyMap();
            Integer readCount = context.getReadCount();
            // 有动态列 的情况
            // ！！！！！！！！！！！！！！！！！！！！！！！有循环的时候注意 循环内部的调用其他的方法会导致context 内部的数据变化 每次context需要重新获取
            for (int i = 0; i < dataList.size(); i++) {
                Integer dataUsedRow = context.getDataUsedRow();
                Map<String, Integer> indexList = context.getIndexList();
                Map<String, Object> map = dataList.get(i);
//                System.out.println("+++++++++++++++++++++++++++++++++第" + i + "条数据:" + JSON.toJSONString(map));
                String rowKey = firstHead + "|" + map.get(firstHead); //行
                String colKey1 = flexHead + "|" + map.get(flexHead); //root 动态列
                String colKey2 = flexHead + "|" + map.get(flexHead) + "|" + flexHead2 + "|" + map.get(flexHead2); //root 动态列
                boolean b1 = existKeyStartWith(indexList, colKey1);
                boolean b2 = existKeyStartWith(indexList, colKey2);
                if (!b1) {//一级动态列 不存在
                    //列不存在 需要新增列
                    ExcelExportUtil.createHeadAndFillDataDuble(false, context, 0, headRowEnd, sheet, structureElement, map);
                } else if (b1 && !b2) { //二级动态列不存在
                    Element element = structureElement.element(flexHead);
                    //创建出相应的列
                    ExcelExportUtil.createHeadAndFillDataDuble(true, context, 1, headRowEnd, sheet, element, map);
                    //可以直接写入数据
                    fillInData(context, sheet, flexHead, flexHead2, nodeKeyMap, dataUsedRow, indexList, map, rowKey);
                } else {//直接写入数据
                    fillInData(context, sheet, flexHead, flexHead2, nodeKeyMap, dataUsedRow, indexList, map, rowKey);
                }
                context.setReadCount(++readCount);
            }
            //2. 额外字段填入数据
            if (dataListext != null && dataListext.size() > 0) {
                ExcelExportUtil.setExtData(context, dataListext, sheet);
            }
            return context;
        }

        //写入数据
        private void fillInData(ExcelExportContext context, SXSSFSheet sheet, String flexHead, String flexHead2, Map<String, String> nodeKeyMap, Integer dataUsedRow, Map<String, Integer> indexList, Map<String, Object> map, String rowKey) {
            //列 存在     直接填写数据
            Integer row = indexList.get(rowKey);
            if (row == null) {
                row = dataUsedRow + 1;
                sheet.createRow(row);
                indexList.put(rowKey, row);
                context.setDataUsedRow(row);//新建行 在写数据
                context.setIndexList(indexList);
            }
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String pKey = nodeKeyMap.get(entry.getKey());
                if (pKey != null) {//pKey不存在说明 字段并未出现在报表中 无需写入 首列数据覆盖无所谓  省去第一次写入时的判断
                    String key;
                    if (Objects.equals(flexHead, pKey)) {
                        key = flexHead + "|" + map.get(flexHead) + "|" + entry.getKey();
                    } else if (Objects.equals(flexHead2, pKey)) {
                        key = flexHead + "|" + map.get(flexHead) + "|" + flexHead2 + "|" + map.get(flexHead2) + "|" + entry.getKey();
                    } else {
                        key = entry.getKey();
                    }
                    Integer col = indexList.get(key);
                    if (col != null) {// flexHead 有子表头时，flexHead列也无数据可填入，indexList中不记录flexHead的列， 加此判断 否则空指针
                        SXSSFCell cell = sheet.getRow(row).createCell(col);
                        ExcelExportUtil.cellFillData(context, entry.getKey(), entry.getValue(), cell);
                    }
                }
            }
        }
    };


    /**
     * 判断map中是否已经存在相似的key  rowKey开头
     *
     * @param indexList
     * @param rowKey
     * @return
     */
    public boolean existKeyStartWith(Map<String, Integer> indexList, String rowKey) {
        if (indexList == null) {
            return false;
        }
        for (String s : indexList.keySet()) {
            if (s.startsWith(rowKey)) {
                return true;
            }
        }
        return false;
    }

    private Integer type;

    EExportType(Integer t) {
        this.type = t;
    }

    public static EExportType getType(Integer t) {
        for (EExportType c : EExportType.values()) {
            if (c.getType().equals(t)) {
                return c;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public abstract ExcelExportContext action(ExcelExportContext context, List<Map<String, Object>> dataList, List<Map<String, Object>> dataListext, Integer headRowEnd, Element structureElement, SXSSFSheet sheet);
}
