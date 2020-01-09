package com.gwghk.crm.common;


public enum ApiErrorCode {
    //系统错误
    DB_DATA_DONE_ERROR("0002", "数据新增或修改异常，请检查！"),
    INPUT_ERROR("1001", "%s错误，请检查！"),
    NULL_ERROR("1008", "%s不能为空，请检查！"),
    SELF_DEFINING_ERROR("1014", "%s");
    private String value;
    private String desc;

    ApiErrorCode(String value, String desc) {
        this.setValue(value);
        this.setDesc(desc);
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {

        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "[" + this.value + "]" + this.desc;
    }

}
