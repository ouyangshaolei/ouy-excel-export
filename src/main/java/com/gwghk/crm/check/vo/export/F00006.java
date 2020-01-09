package com.gwghk.crm.check.vo.export;

import com.gwghk.crm.check.annotation.DateMon;

public class F00006 extends F00000 {

    private String empName;
    @DateMon(allowNull = false)
    private String reportDay;//2019-03

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getReportDay() {
        return reportDay;
    }

    public void setReportDay(String reportDay) {
        this.reportDay = reportDay;
    }

    @Override
    public void extCheck() throws Exception {
        this.empName = getMultiStrParams(empName);
    }
}
