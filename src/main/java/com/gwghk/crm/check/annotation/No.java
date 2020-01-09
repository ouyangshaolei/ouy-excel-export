package com.gwghk.crm.check.annotation;

import com.gwghk.crm.check.checker.NoChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字校验注释
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface No {
    String key() default NoChecker.key;

    boolean allowNull() default false;
}
