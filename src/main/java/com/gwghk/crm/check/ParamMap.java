package com.gwghk.crm.check;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 2018-04-07.
 */
public class ParamMap extends HashMap<String, Object> {
    public ParamMap(Map<? extends String, ?> m) {
        super(m);
    }

    public String getString(String key) {
        Object value = get(key);
        if (value != null) return value.toString();
        return null;
    }
    public Long getLong(String key) {
        Object value = get(key);
        if (value != null) return Long.parseLong(value.toString());
        return null;
    }

    public Object getObject(String key) {
        Object value = get(key);
        if (value != null) return value;
        return null;
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (value != null) return Integer.parseInt(value.toString());
        return null;
    }

    public Integer[] getIntegerArray(String key) {
        Object value = get(key);
        if (value != null) {
            Integer[] ia = (Integer[]) value;
            return ia;
        }
        return null;
    }

    public String[] getStringArray(String key) {
        Object value = get(key);
        if (value != null) {
            String[] ia = (String[]) value;
            return ia;
        }
        return null;
    }

    public LocalDateTime getLocalDateTime(String key) {
        Object value = get(key);
        if (value != null) {
            return (LocalDateTime) value;
        }

        return null;
    }

    public LocalDate getLocalDate(String key) {
        Object value = get(key);
        if (value != null) {
            return (LocalDate) value;
        }

        return null;
    }
}
