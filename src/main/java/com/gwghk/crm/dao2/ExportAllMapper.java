package com.gwghk.crm.dao2;


import java.util.List;
import java.util.Map;

public interface ExportAllMapper {
    //F00001
    List<Map<String, Object>> selectNCustFollowDaily(Map<String, Object> params);

    //F00002
    List<Map<String, Object>> selectOtherCustFollowDaily(Map<String, Object> params);

    //F00003 日报表
    List<Map<String, Object>> selectCustAssignDaily(Map<String, Object> params);

    //F00004
    List<Map<String, Object>> selectCustAssignDailyTotal(Map<String, Object> params);

    //F00005
    List<Map<String, Object>> selectCustAssignMonthly(Map<String, Object> params);

    //F00006
    List<Map<String, Object>> selectCustPerformance(Map<String, Object> params);

    //测试两层动态
    List<Map<String, Object>> selectTEST(Map<String, Object> params);
}