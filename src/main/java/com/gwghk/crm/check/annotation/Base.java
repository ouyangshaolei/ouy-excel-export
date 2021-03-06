package com.gwghk.crm.check.annotation;

import com.gwghk.crm.check.checker.BaseChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非空校验注释
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Base {
    String key() default BaseChecker.key;

    boolean allowNull() default false;
}
