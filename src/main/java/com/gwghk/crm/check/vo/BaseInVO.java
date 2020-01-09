package com.gwghk.crm.check.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.gwghk.crm.check.CheckUtil;
import com.gwghk.crm.check.ParamMap;
import com.gwghk.crm.check.annotation.Base;
import com.gwghk.crm.check.annotation.Date;
import com.gwghk.crm.check.annotation.DateMon;
import com.gwghk.crm.check.annotation.No;
import com.gwghk.crm.common.Util;
import com.gwghk.crm.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseInVO<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseInVO.class);
    @JSONField(name = "trace")
    private String track;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public abstract T toRightVO();
    public ParamMap toMap() {
        return toMap(true);
    }
    public ParamMap toMap(boolean isClean) {
        return new ParamMap(Util.obj2Map(this, isClean));
    }
    public final void check() throws Exception {
        //获取子类，父类所有属性
        List<Field> fs = new ArrayList<Field>();
        for (Class<?> clazz = this.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fieldArr = clazz.getDeclaredFields();
            fs.addAll(Arrays.asList(fieldArr));
        }

        for (Field f : fs) {
            try {
                f.setAccessible(true);
                Annotation[] as = f.getAnnotations();
                for (Annotation a : as) {
                    if (a instanceof No) {
                        No no = (No) a;
                        CheckUtil.checkers.get(no.key()).check(f.get(this), no.allowNull());
                    } else if (a instanceof Base) {
                        Base b = (Base) a;
                        CheckUtil.checkers.get(b.key()).check(f.get(this), b.allowNull());
                    } else if (a instanceof Date) {
                        Date t = (Date) a;
                        CheckUtil.checkers.get(t.key()).check(f.get(this), t.allowNull());
                    } else if (a instanceof DateMon) {
                        DateMon t = (DateMon) a;
                        CheckUtil.checkers.get(t.key()).check(f.get(this), t.allowNull());
                    }
                }
            } catch (Throwable e) {
                if (e instanceof SystemException) {
                    throw e;
                }
                logger.error("校验参数异常", e);
            }
        }
    }

    /**
     * 额外校验
     *
     * @throws Exception
     */
    public void extCheck() throws Exception {
    }
    public ReturnVO returnInfo() {
        return new ReturnVO(getToken(), getTrack());
    }
}
