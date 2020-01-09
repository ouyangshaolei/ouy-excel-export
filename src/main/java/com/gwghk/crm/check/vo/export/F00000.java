package com.gwghk.crm.check.vo.export;


import com.google.common.base.Strings;
import com.gwghk.crm.check.annotation.Base;
import com.gwghk.crm.check.vo.BaseInVO;

public class F00000<T> extends BaseInVO<F00000> {
    @Base
    private String tmplateCode;//tmplateCode 为必填参数
    private String filesName;//可根据前端传参自定义,前端不传参则按照xml 配置的
    private String tableName;//可根据前端传参自定义,前端不传参则按照xml 配置的
    private String sheetName;//可根据前端传参自定义,前端不传参则按照xml 配置的

    private Integer pageNo = 0;
    private Integer pageSize = 100;
    private String startTime;
    private String endTime;
    @Base
    private Integer companyId;

    public String getTmplateCode() {
        return tmplateCode;
    }

    public void setTmplateCode(String tmplateCode) {
        this.tmplateCode = tmplateCode;
    }

    public String getFilesName() {
        return filesName;
    }

    public void setFilesName(String filesName) {
        this.filesName = filesName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public F00000 toRightVO() {
        return this;
    }
    public String getMultiStrParams(String params) {
        String multP;
        if (!Strings.isNullOrEmpty(params) && params.contains("|")) {
            String s = "\"" + params.replaceAll("\\|", "\",\"") + "\"";
            multP = "in" + "(" + s + ")";
        } else if (!Strings.isNullOrEmpty(params) && !params.contains(",")) {
            multP = "=" + "\"" + params + "\"";
        } else {
            multP = null;
        }
        return multP;
    }
}
