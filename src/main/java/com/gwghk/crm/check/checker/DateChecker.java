package com.gwghk.crm.check.checker;

import com.gwghk.crm.common.ApiErrorCode;
import com.gwghk.crm.common.ValidatorUtil;
import com.gwghk.crm.exception.SystemException;

/**
 * 日期校验注释处理
 */
public class DateChecker extends BaseChecker {
    public static final String key = "DateChecker";

    @Override
    public void check(Object value) {
        String time = value.toString();

        boolean result = ValidatorUtil.isYYYYMMDD(time);
        if (!result) {
            throw new SystemException("0001", "时间格式错误");
        }
    }
}
