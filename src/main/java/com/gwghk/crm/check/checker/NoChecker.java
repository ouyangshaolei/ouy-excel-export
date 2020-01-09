package com.gwghk.crm.check.checker;

import com.google.common.primitives.Longs;
import com.gwghk.crm.common.ApiErrorCode;
import com.gwghk.crm.exception.SystemException;

/**
 * 数字校验注释处理
 */
public class NoChecker extends BaseChecker {
    public static final String key = "NoChecker";

    @Override
    public void check(Object value) {
        Long l = Longs.tryParse(value.toString());
        if (l == null) {
            throw new SystemException("0001", "数值格式错误");
        }
    }
}
