package com.gwghk.crm.check.checker;

import com.google.common.base.Strings;
import com.gwghk.crm.exception.SystemException;

/**
 * 非空校验注释处理
 */
public class BaseChecker {
    public static final String key = "BaseChecker";

    public void check(Object value) {
    }

    public void check(Object value, boolean allowNull) {
        if (allowNull) {
            if (value != null && !Strings.isNullOrEmpty(value.toString())) {
                check(value);
            }
        } else {
            if (value == null || Strings.isNullOrEmpty(value.toString())) {
                throw new SystemException("0001", "参数不能为空");
            } else {
                check(value);
            }
        }
    }
}
