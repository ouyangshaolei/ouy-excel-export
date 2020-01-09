package com.gwghk.crm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description:
 * @date 2017年7月5日
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        //判断异常的类型,返回不一样的返回值
        if (ex instanceof SystemException) {
            logger.error(ex.getMessage());
            map.put("code", ((SystemException) ex).getCode());
            map.put("msg", ((SystemException) ex).getMsg());
        }
        return map;
    }
}
