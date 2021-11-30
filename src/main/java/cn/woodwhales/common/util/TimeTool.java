package cn.woodwhales.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * 时间工具
 * @author woodwhales on 2021-11-30 14:34
 */
public class TimeTool {

    /**
     * date 的默认时间格式
     */
    private final static String date_pattern = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     * 默认输出时间格式
     */
    private final static String default_pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * local_date_time 的默认时间格式
     */
    private final static String local_date_time_pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS";

    /**
     * <p>
     * 格式化时间字符串：
     * EEE MMM dd HH:mm:ss zzz yyyy 时间转 yyyy-MM-dd HH:mm:ss
     * uuuu-MM-dd'T'HH:mm:ss.SSS 时间转 yyyy-MM-dd HH:mm:ss
     * </p>
     * @param dateStr 需要格式化的时间字符串
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String convertDateStr(String dateStr) {
        if(StringUtils.isBlank(dateStr)) {
            return "";
        }

        DateTimeFormatter dateTimeFormatter = null;
        if(StringUtils.length(dateStr) == date_pattern.length()) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(date_pattern, Locale.ENGLISH);
        } else if(StringUtils.length(dateStr) == local_date_time_pattern.length()) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(local_date_time_pattern, Locale.ENGLISH);
        }

        if(Objects.isNull(dateTimeFormatter)) {
            return null;
        }

        final LocalDateTime parseLocalDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        final DateTimeFormatter defaultTimeFormatter = DateTimeFormatter.ofPattern(default_pattern);
        return defaultTimeFormatter.format(parseLocalDateTime);

    }

    /**
     * 获取北京时间
     * @return LocalDateTime
     */
    public static LocalDateTime getBeijingTime() {
        return LocalDateTime.now(ZoneOffset.of("+8"));
    }

    /**
     * 获取墨西哥时间
     * @return LocalDateTime
     */
    public static LocalDateTime getMexicoTime() {
        return LocalDateTime.now(ZoneOffset.of("-6"));
    }

    /**
     * 获取格林威治时间（0 时区）
     * @return LocalDateTime
     */
    public static LocalDateTime getGMTTime() {
        return LocalDateTime.now(ZoneOffset.of("+0"));
    }

}
