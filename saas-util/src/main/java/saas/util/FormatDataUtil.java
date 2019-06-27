package saas.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FormatDataUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER_YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_YMD1 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static String toDateYMD(Date date) {
        return DATE_TIME_FORMATTER_YMD.format(dateToLocalDateTime(date));
    }

    public static String toDateYMD1(Date date) {
        return DATE_TIME_FORMATTER_YMD1.format(dateToLocalDateTime(date));
    }

    // TODO: 2019/6/26 切换成Apache.commons的方法
    public static Date ymdToDate(String ymd) throws ParseException {
        LocalDateTime localDateTime = stringToLocalDateTime(ymd, DATE_TIME_FORMATTER_YMD);
        Instant instant = localDateTime.atZone(ZONE_ID).toInstant();
        return Date.from(instant);
    }

    public static Date ymd1ToDate(String ymd1) throws ParseException {
        LocalDateTime localDateTime = stringToLocalDateTime(ymd1, DATE_TIME_FORMATTER_YMD);
        Instant instant = localDateTime.atZone(ZONE_ID).toInstant();
        return Date.from(instant);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    private static LocalDateTime stringToLocalDateTime(String dateS, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateS, dateTimeFormatter);
    }

    private static Date localDateTimeTodate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static void main(String[] args) {

//        LocalDateTime rightNow=LocalDateTime.now();
//        String date=DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(rightNow);
//        System.out.println(date);
//
//        Date date1 = new Date();
//
//        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("YYYY-MM-dd");
//        System.out.println(formatter.format(rightNow));
//        System.out.println(formatter.format(dateToLocalDateTime(date1)));

        String daS = "2019-06-11";
        LocalDateTime localDateTime = stringToLocalDateTime(daS, DATE_TIME_FORMATTER_YMD);
        Date date2 = localDateTimeTodate(localDateTime);
        System.out.println(DATE_TIME_FORMATTER_YMD.format(localDateTime));
    }

}
