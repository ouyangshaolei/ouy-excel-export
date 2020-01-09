package com.gwghk.crm.check.annotation;

import com.gwghk.crm.check.checker.DateChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期校验注释
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {
    String key() default DateChecker.key;

    boolean allowNull() default false;
}
