package com.gwghk.crm.common;


import com.alibaba.druid.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @description 验证工具类
 * @date 2017-09-05 14:31
 */
public class ValidatorUtil {
    /**
     * 正则表达式：验证用户名
     */
    private static final String REGEX_USERNAME = "^[a-zA-Z0-9]{6,10}$";

    /**
     * 正则表达式：验证密码
     */
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,8}$";

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^1\\d{10}$";

    /**
     * 正则表达式：验证邮箱
     */
    private static final String REGEX_EMAIL = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    private static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证名字
     */
    private static final String REGEX_NAME = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*";

    /**
     * 正则表达式：验证URL
     */
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    private static final String REGEX_IP_ADDR = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

    /**
     * 正则表达式：验证名称（由汉字、数字、字母、下划线，下划线组成）
     */
    private static final String REGEX_ROLENAME = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{3,20}$";

    public static final String REGEX_NO = "^\\d+$";

    /**
     * 校验名称
     *
     * @param crmName
     * @return
     */
    public static boolean isCRMName(String crmName) {
        return Pattern.matches(REGEX_ROLENAME, crmName);
    }

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验姓名
     *
     * @param name
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isName(String name) {
        return Pattern.matches(REGEX_NAME, name);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * 验证日期格式 YYYY-mm-dd hh:mi:ss
     *
     * @param time
     * @return
     */
    public static boolean isYYYYMMDDHHMISS(String time) {
        String formatTime = time.replace(":", "");
        if (time.length() - 2 != formatTime.length()) {
            return false;
        }
        formatTime = formatTime.replace(" ", "");
        if (time.length() - 3 != formatTime.length()) {
            return false;
        }
        formatTime = formatTime.replace("-", "");
        if (time.length() - 5 != formatTime.length()) {
            return false;
        }

        if (formatTime.length() == 14 && StringUtils.isNumber(formatTime)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证日期格式 YYYY-mm-dd hh:mi
     *
     * @param time
     * @return
     */
    public static boolean isYYYYMMDDHHMI(String time) {
        String formatTime = time.replace(":", "");
        if (time.length() - 1 != formatTime.length()) {
            return false;
        }
        formatTime = formatTime.replace(" ", "");
        if (time.length() - 2 != formatTime.length()) {
            return false;
        }
        formatTime = formatTime.replace("-", "");
        if (time.length() - 4 != formatTime.length()) {
            return false;
        }

        if (formatTime.length() == 12 && StringUtils.isNumber(formatTime)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证日期格式 YYYY-mm-dd
     *
     * @param time
     * @return
     */
    public static boolean isYYYYMMDD(String time) {
        String formatTime = time.replace("-", "");
        if (time.length() - 2 != formatTime.length()) {
            return false;
        }

        if (formatTime.length() == 8 && StringUtils.isNumber(formatTime)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证日期格式 YYYY-mm
     *
     * @param time
     * @return
     */
    public static boolean isYYYYMM(String time) {
        String formatTime = time.replace("-", "");
        if (time.length() - 1 != formatTime.length()) {
            return false;
        }
        if (formatTime.length() == 6 && StringUtils.isNumber(formatTime)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNo(String no) {
        return Pattern.matches(REGEX_NO, no);
    }
}
