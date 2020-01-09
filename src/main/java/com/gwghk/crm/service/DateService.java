package com.gwghk.crm.service;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.gwghk.crm.config.YmlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DateService {
    public static final DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter pattern2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter pattern3 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter pattern4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter pattern5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(DateService.class);
    private final YmlConfig config;

    @Autowired
    public DateService(YmlConfig config) {
        this.config = config;
    }

    /**
     * 验证bi数据返回的是否为时间格式
     * 形如: "2018-05-24 07:53:54"
     * 形如: "2018-05-24 07:53:54.000"
     * 形如: "2018-05-24 07:53:54+00"
     * 形如: "2018-05-24 07:53:54+00:00"
     * 形如: "2018-05-24 07:53:54.000+00"
     * 形如: "2018-05-24 07:53:54.000+00:00"
     * <p>
     * 形如: "2018-05-24T07:53:54"
     * 形如: "2018-05-24T07:53:54.000"
     * 形如: "2018-05-24T07:53:54+00"
     * 形如: "2018-05-24T07:53:54+00:00"
     * 形如: "2018-05-24T07:53:54.000+00"
     * 形如: "2018-05-24T07:53:54.000+00:00"
     * <p>
     * +00:00  和 +00 的会转化成+0 然后再转换成相应的时区
     *
     * @param dateStr
     * @return
     */
    public static boolean validateTimeFormat(String dateStr) {
        String eL = "^(((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[13579][26])00))-02-29))(T|\\s)+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])((.[0-9]{3})?((\\+[0-9]{2})?|(\\+[0-9]{4})?|(\\+[0-9]{2}:[0-9]{2}))?)?)$";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dateStr);
        return m.matches();
    }

    public ZoneId getZone() {
        String hours = config.getTimezone();
        Integer ih = Ints.tryParse(hours);
        if (ih == null) {
            logger.error("未配置时区，默认GMT+8");
            ih = 8;
        }

        ZoneId zone = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(ih));

        return zone;
    }

    /**
     * 根据时区获取时间
     *
     * @return
     */
    public LocalDateTime getRightDateTime() {
        ZoneId zone = getZone();
        return LocalDateTime.now(zone);
    }

    public String getRightDateTimeString() {
        LocalDateTime now = getRightDateTime();
        return now.format(pattern1);
    }

    public String getRightDateTimeString(LocalDateTime ldt) {
        if (ldt == null) {
            return "";
        }
        return ldt.format(pattern1);
    }

    public String getRightDateTimeSequence() {
        LocalDateTime now = getRightDateTime();
        return now.format(pattern2);
    }

    public String getRightDateSequence() {
        LocalDateTime now = getRightDateTime();
        return now.format(pattern3);
    }

    /**
     * 将0时区的字符串转化成指定时区的LocalDateTime
     *
     * @param dateStr
     * @return
     */
    public LocalDateTime strToTimeWithZone(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        LocalDateTime parse = LocalDateTime.parse(dateStr, pattern1);
        int hours = Integer.parseInt(config.getTimezone());
        LocalDateTime localDateTime = parse.plusHours(hours);
        return localDateTime;
    }
    public String strToTimeStrWithZone(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        LocalDateTime parse = LocalDateTime.parse(dateStr, pattern1);
        int hours = Integer.parseInt(config.getTimezone());
        LocalDateTime localDateTime = parse.plusHours(hours);
        return localDateTime.format(pattern1);
    }
    public LocalDateTime strToTime(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, pattern1);
    }

    public String getRightDateStringFromLong(Long millis) {
        LocalDateTime dateTime = getRightDateFromLong(millis);
        return dateTime.format(pattern1);
    }

    public LocalDateTime getRightDateFromLong(Long millis) {
        ZoneId zone = getZone();
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zone);
        return dateTime;
    }

    public LocalDateTime strToYYYYMMDDHHMM(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, pattern4);
    }

    public LocalDateTime strToYYYYMMDDHHMMSS(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, pattern1);
    }

    public LocalDate strToYYYYMMDD(String dateStr) {
        if (Strings.isNullOrEmpty(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, pattern5);
    }

    public String getRightStringFromDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(pattern5);
    }

    public String YYYYMMDDHHMMToStr(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(pattern4);
    }

    //减去系统配置时间
    public String getSystemMinusDateTime(LocalDateTime date) {
        Long hours = getSystemZoneHours();
        if (date != null) {
            return getRightDateTimeString(date.minusHours(hours));
        }
        return null;
    }

    public Long getSystemZoneHours() {
        return Long.valueOf(config.getTimezone());
    }

    public LocalDateTime notifyTime(LocalDateTime date, int during) {
        return date.minusMinutes(during);
    }

    /**
     * 针对单个时间字符串做 时区转换
     *
     * @param time
     * @return
     */
    public String fomatTimeStr(Object time, String companyId) {
        if (time != null) {
            String timeStr = time.toString();
            Map<String, Integer> appidTimeZone = config.getAppidTimeZone();
            Integer timezone = appidTimeZone.get("crm-" + companyId);//配置中获取companyId
            if (!Strings.isNullOrEmpty(timeStr) && validateTimeFormat(timeStr)) {
                String rightTimeStr = getTimeStr(timeStr.replace("T", " "), timezone);
                return rightTimeStr;
            }
            return time.toString();
        }
        return null;
    }

    public String getTimeStr(String timeStr, Integer timezone) {
        if (timeStr.contains("+")) {
            // 处理时间格式入 2018-05-24 07:53:54.000+08:00  2018-05-24 07:53:54.000+08  先转换成0时区
            String substring = timeStr.substring(0, 19);
            LocalDateTime parse = LocalDateTime.parse(substring, pattern1);
            int hour = Integer.parseInt(timeStr.substring(timeStr.indexOf("+") + 1, timeStr.indexOf("+") + 3));
            // 这行代码的作用是将从bi获取的时间先转化成+0 然后按照application.yml 配置的时间修改实际那
            LocalDateTime localDateTime = parse.plusHours(timezone - hour);
            return localDateTime.format(pattern1);
        } else {
            String substring = timeStr.substring(0, 19);
            LocalDateTime parse = LocalDateTime.parse(substring, pattern1);
            LocalDateTime localDateTime = parse.plusHours(timezone);
            return localDateTime.format(pattern1);
        }
    }
}
