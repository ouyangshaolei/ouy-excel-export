package com.gwghk.crm.common;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: Util
 * @Description: 工具类
 * @date 2017年5月16日
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * @param obj
     * @return Map<String, Object>
     * @MethodName: obj2Map
     * @Description: 把对象转成map
     */
    public static Map<String, Object> obj2Map(Object obj, boolean isFilterNull) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fieldArr = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(fieldArr));
        }
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(fields.size());
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object objVal = field.get(obj);
                if (!field.getName().equalsIgnoreCase("serialVersionUID")) {
                    if (isFilterNull && (objVal == null || (objVal instanceof String && Strings.isNullOrEmpty(objVal.toString())))) {
                        continue;
                    }
                    map.put(field.getName(), objVal);
                }
            }
        } catch (Exception e) {
            logger.error("Util->obj2Map", e);
        }
        return map;
    }
}
