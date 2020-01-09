package com.gwghk.crm.check.vo.export;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class DataCellStyle {
    String ifSymbol;      //符号  > >=  <  <=  =
    String ifTarget;      //比较对象    50    50%
    XSSFCellStyle conditionCellStyle; //满足条件 单元格样式
    XSSFCellStyle ordinaryCellStyle;  //普通单元格样式

    public String getIfSymbol() {
        return ifSymbol;
    }

    public void setIfSymbol(String ifSymbol) {
        this.ifSymbol = ifSymbol;
    }

    public String getIfTarget() {
        return ifTarget;
    }

    public void setIfTarget(String ifTarget) {
        this.ifTarget = ifTarget;
    }

    public XSSFCellStyle getConditionCellStyle() {
        return conditionCellStyle;
    }

    public void setConditionCellStyle(XSSFCellStyle conditionCellStyle) {
        this.conditionCellStyle = conditionCellStyle;
    }

    public XSSFCellStyle getOrdinaryCellStyle() {
        return ordinaryCellStyle;
    }

    public void setOrdinaryCellStyle(XSSFCellStyle ordinaryCellStyle) {
        this.ordinaryCellStyle = ordinaryCellStyle;
    }
}
