package com.gwghk.crm.config;

import com.gwghk.crm.check.vo.BaseInVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    //定义切入点 该包下的所有函数
    @Pointcut("execution(public * com.gwghk.crm.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog() && args(in)")
    public void doBefore(JoinPoint joinPoint, BaseInVO<Object> in) throws Exception {
        in.check();//注解校验入参
        in = (BaseInVO<Object>) in.toRightVO();
        in.extCheck();//转换参数后，额外校验
    }

}
