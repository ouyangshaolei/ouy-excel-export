package com.gwghk.crm.check.vo.export;

import com.gwghk.crm.check.annotation.Date;

public class F00004 extends F00000 {
    private String empName;
    @Date(allowNull = true)
    private String reportDayStart;
    @Date(allowNull = true)
    private String reportDayEnd;


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
