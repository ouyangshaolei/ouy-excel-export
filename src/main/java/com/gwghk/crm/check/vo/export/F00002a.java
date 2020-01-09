package com.gwghk.crm.check.vo.export;

import com.gwghk.crm.check.annotation.Base;
import com.gwghk.crm.check.annotation.Date;

public class F00002a extends F00000 {

    @Base
    private String custType; //名单名称
    private String empName;
    @Date(allowNull = true)
    private String reportDayStart;
    @Date(allowNull = true)
    private String reportDayEnd;

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getReportDayStart() {
        return reportDayStart;
    }

    public void setReportDayStart(String reportDayStart) {
        this.reportDayStart = reportDayStart;
    }

    public String getReportDayEnd() {
        return reportDayEnd;
    }

    public void setReportDayEnd(String reportDayEnd) {
        this.reportDayEnd = reportDayEnd;
    }

    @Override
    public void extCheck() throws Exception {
        this.empName = getMultiStrParams(empName);
    }
}
