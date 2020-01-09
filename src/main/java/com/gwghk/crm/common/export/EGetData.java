package com.gwghk.crm.common.export;


import com.gwghk.crm.common.ApiErrorCode;
import com.gwghk.crm.common.SpringContext;
import com.gwghk.crm.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum EGetData {
    MYSQL(ExcelExportConstants.DATA_HEAD_DB_MYSQL) {
        @Override
        public List<Map<String, Object>> queryData(String mapperStr, String methodName, Map map) {
            Object mapper = SpringContext.getBean(mapperStr);
            List<Map<String, Object>> list;
            Class<?> aClass = mapper.getClass();
            try {
                Method method = aClass.getMethod(methodName, Map.class);
                list = (List<Map<String, Object>>) method.invoke(mapper, map);
            } catch (Exception e) {
                logger.error("数据查询异常", e);
                throw new SystemException("0001","数据查询异常");
            }
            return list;
        }
    }, MONGODB(ExcelExportConstants.DATA_HEAD_DB_MONGODB) {
        @Override
        public List<Map<String, Object>> queryData(String mapperStr, String methodName, Map map) {
            //TODO 暂时没有mongo需求
            return null;
        }
    };
    // TODO: 2019/10/31 后期的跟进要求
    private String type;
    private static final Logger logger = LoggerFactory.getLogger(EGetData.class);

    EGetData(String t) {
        this.type = t;
    }

    public static EGetData getType(String t) {
        for (EGetData c : EGetData.values()) {
            if (Objects.equals(c.getType(), t)) {
                return c;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public abstract List<Map<String, Object>> queryData(String mapperStr, String methodName, Map map);
}
