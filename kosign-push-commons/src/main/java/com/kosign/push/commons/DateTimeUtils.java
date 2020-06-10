package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils extends org.joda.time.DateTimeUtils {

    private static final String BNK_PATTERN_DATE8 = "yyyyMMdd";
    private static final String BNK_PATTERN_DTM14 = "yyyyMMddHHmmss";
    private static final String BNK_PATTERN_TIME6 = "HHmmss";
    private static final String BNK_PATTERN_YYYYMM = "yyyyMM";
    private static final String BNK_PATTERN_YYMM = "yyMM";
    private static final String BNK_PATTERN_YYYY = "yyyy";
    private static final String BNK_PATTERN_MM = "MM";
    public static final DateTimeFormatter BNK_FORMATTER_DATE8 = DateTimeFormat.forPattern(BNK_PATTERN_DATE8);
    public static final DateTimeFormatter BNK_FORMATTER_DTM14 = DateTimeFormat.forPattern(BNK_PATTERN_DTM14);
    public static final DateTimeFormatter BNK_FORMATTER_TIME6 = DateTimeFormat.forPattern(BNK_PATTERN_TIME6);
    public static final DateTimeFormatter BNK_FORMATTER_YYYYMM = DateTimeFormat.forPattern(BNK_PATTERN_YYYYMM);
    public static final DateTimeFormatter BNK_FORMATTER_YYMM = DateTimeFormat.forPattern(BNK_PATTERN_YYMM);
    public static final DateTimeFormatter BNK_FORMATTER_YYYY = DateTimeFormat.forPattern(BNK_PATTERN_YYYY);
    public static final DateTimeFormatter BNK_FORMATTER_MM = DateTimeFormat.forPattern(BNK_PATTERN_MM);

    public static LocalDateTime toUtc(String dateString) {
        return toUtc(dateString, BNK_FORMATTER_DTM14);
    }

//    private static DateTimeFormatter getFormatter(String formatString) {
//        return DateTimeFormat.forPattern(formatString).withZone(DateTimeZone.getDefault());
//    }

//    public static DateTime toUtc(String dateString, String formatString) {
//        return toUtc(dateString, DateTimeFormat.forPattern(formatString));
//    }

    public static LocalDateTime toUtc(String dateString, DateTimeFormatter formatter) {
        LocalDateTime originalDateTime = LocalDateTime.parse(dateString, formatter);
        return toUtc(originalDateTime);
    }

    public static LocalDateTime toUtc(LocalDate originalDate, LocalTime originalTime) {
        LocalDateTime originalDateTime = LocalDateTime.parse(originalDate.toString(BNK_FORMATTER_DATE8) + originalTime.toString(BNK_FORMATTER_TIME6));
        return toUtc(originalDateTime);
    }

    // main fromUtc method
    public static LocalDateTime fromUtc(String dateString) {
        return fromUtc(TimeZone.getDefault(), dateString);
    }

    public static LocalDateTime fromUtc(TimeZone tz, String dateString) {
        return fromUtc(tz, dateString, BNK_FORMATTER_DTM14);
    }

    public static LocalDateTime fromUtc(TimeZone tz, String dateString, DateTimeFormatter formatter) {
//        LocalDateTime originalDateTime = LocalDateTime.parse(dateString, formatter);
//        return convertDateTime(ldt.toDateTime(DateTimeZone.UTC), DateTimeZone.forTimeZone(tz)).toLocalDateTime();
        return convertDateTime(dateString, formatter, DateTimeZone.UTC.toTimeZone(), tz).toLocalDateTime();
    }

    public static DateTime utcNow() {
        return now(TimeZone.getTimeZone("UTC"));
    }

    /*public static DateTime now() {
        return now(TimeZone.getDefault());
    }*/

    public static DateTime now(TimeZone timeZone) {
        return DateTime.now(DateTimeZone.forTimeZone(timeZone));
    }


//    public static LocalDateTime fromUtc(HttpServletRequest request, String dateString, DateTimeFormatter formatter) {
//        return fromUtc(GeoUtils.getTimeZone(request), dateString, formatter);
//    }

//    public static String normalizeFromUtc(HttpServletRequest request, String dateString, DateTimeFormatter formatter) {
//        return fromUtc(request, dateString, formatter).toString(formatter);
//    }
    
    /*
    public static String normalizeFromUtc(WbUser loginUser, String dateString, DateTimeFormatter formatter) {
        return fromUtc(loginUser.getTimeZone(), dateString, formatter).toString(formatter);
    }

    public static String normalize(WbUser loginUser, LocalDateTime ldt, DateTimeFormatter formatter) {
        if(TimeZone.getDefault() == loginUser.getTimeZone()) {
            return ldt.toString(formatter);
        }
        else {
            return convertDateTime(ldt, DateTimeZone.forTimeZone(loginUser.getTimeZone())).toString(formatter);
        }
    }
    */

    public static DateTime convertDateTime(DateTime originalDateTime, TimeZone newZone) {
        return convertDateTime(originalDateTime, DateTimeZone.forTimeZone(newZone));
    }

    public static DateTime convertDateTime(DateTime originalDateTime, DateTimeZone newZone) {
//        System.out.println("originalZone = " + originalZone);
//        System.out.println("newZone = " + newZone);
//        return originalDateTime.toDateTime(newZone);
    	return originalDateTime.toDateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())).withZone(newZone);
    }

    public static DateTime convertDateTime(LocalDateTime ldt, DateTimeZone newZone) {
//        System.out.println("originalZone = " + originalZone);
//        System.out.println("newZone = " + newZone);
        return convertDateTime(ldt.toDateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())), newZone);
    }

    public static DateTime convertDateTime(LocalDateTime ldt, TimeZone newZone) {
        return convertDateTime(ldt.toDateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())), newZone);
    }

    public static DateTime convertDateTime(String dateString, DateTimeFormatter formatter, DateTimeZone originalZone, DateTimeZone newZone) {
        return convertDateTime(dateString, formatter, originalZone.toTimeZone(), newZone.toTimeZone());
    }

    public static DateTime convertDateTime(String dateString, DateTimeFormatter formatter, TimeZone originalZone, TimeZone newZone) {
        DateTime originalDateTime = DateTime.parse(dateString, formatter.withZone(DateTimeZone.forTimeZone(originalZone)));
        return convertDateTime(originalDateTime, newZone);
    }

    public static LocalDateTime toUtc(DateTime originalDateTime) {
    	return convertDateTime(originalDateTime, DateTimeZone.UTC).toLocalDateTime();
    }

    public static LocalDateTime toUtc(LocalDateTime originalDateTime) {
    	return toUtc(originalDateTime.toDateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())));
    }

    public static String normalizeInput(String str, TimeZone originalZone) {
        AppLogManager.debug((originalZone.getRawOffset() != TimeZone.getDefault().getRawOffset())+"");

        if(     originalZone != null
//                &&  originalZone.getRawOffset() != TimeZone.getDefault().getRawOffset()
                &&  StringUtils.isNotBlank(str)
        ) {
            return convertDateTime(str, BNK_FORMATTER_DTM14, originalZone, TimeZone.getTimeZone("UTC")).toString(BNK_FORMATTER_DTM14);
        }

        return str;
    }

    public static String normalizeResult(String str, TimeZone timeZone) {
        if(     timeZone != null
//            &&  timeZone != TimeZone.getDefault()     // unexpected
//            &&  timeZone.getRawOffset() != TimeZone.getDefault().getRawOffset()
            &&  StringUtils.isNotBlank(str)
        ) {
            AppLogManager.debug(DateTimeUtils.fromUtc(timeZone, str).toString());
            return DateTimeUtils.fromUtc(timeZone, str).toString(BNK_FORMATTER_DTM14);
        }

        return str;
    }

    public static void main(String[] args) {
//        String dateString = "20200110032301";
//        String formatString = "yyyyMMddHHmmss";
//
//        System.out.println(toUtc(dateString, formatString));
//
//        LocalDateTime dt = toUtc(DateTime.now());
//        System.out.println(dt);
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
//        System.out.println(DateTime.parse("2015-02-12T09:58:20.323+0100", formatter));
//        AppLogManager.debug(convertDateTime(LocalDateTime.now(), DateTimeZone.forID("Asia/Seoul")).toString());

        //TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
        //AppLogManager.debug(TimeZone.getDefault().toString());

        AppLogManager.debug(normalizeInput("20200601214611", TimeZone.getTimeZone("Asia/Phnom_Penh")));

        LocalDateTime useDt = DateTimeUtils.fromUtc("20200601144611");
        AppLogManager.debug(useDt.toString());

        LocalDateTime useDtUtc = DateTimeUtils.toUtc(useDt);
        AppLogManager.debug(useDtUtc.toString(DateTimeUtils.BNK_FORMATTER_DTM14));

//        AppLogManager.debug(normalizeResult("20200601144611", TimeZone.getTimeZone("Asia/Phnom_Penh")));
//        AppLogManager.debug(normalizeResult("20200602074618", TimeZone.getTimeZone("Asia/Phnom_Penh")));


//        AppLogManager.debug(convertDateTime(ldt, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Seoul"))).toString(BNK_FORMATTER_DTM14));
//        AppLogManager.debug(ldt.DateTimTimeZone.getTimeZone("Asia/Seoul"), ))
//        AppLogManager.debug(convertDateTime(ldt, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok"))).toString(BNK_FORMATTER_DTM14));
//        AppLogManager.debug(DateTimeUtils.normalizeFromUtc(TimeZone.getTimeZone("Asia/Bangkok"), "20200601131629", DateTimeUtils.BNK_FORMATTER_DTM14));
//        AppLogManager.debug(ldt.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok"))).toString(DateTimeUtils.BNK_FORMATTER_DTM14));
//        normalize(new WbUser(), ldt, DateTimeUtils.BNK_FORMATTER_DTM14);
    }

    public static LocalDate parseLocalDate(String dateString) {
        return parseLocalDate(dateString, BNK_FORMATTER_DATE8);
    }

    public static LocalDate parseLocalDate(String dateString, DateTimeFormatter formatter) {
        try {
            if(StringUtils.isNotBlank(dateString)) {
                return LocalDate.parse(dateString, formatter);
            }
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static LocalDateTime parseLocalDateTime(String dateString) {
        return parseLocalDateTime(dateString, BNK_FORMATTER_DTM14);
    }

    public static LocalDateTime parseLocalDateTime(String dateString, DateTimeFormatter formatter) {
        try {
            if(StringUtils.isNotBlank(dateString)) {
                return LocalDateTime.parse(dateString, formatter);
            }
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static DateTime parseDateTime(String dateString) {
        return parseDateTime(dateString, BNK_FORMATTER_DTM14);
    }

    public static DateTime parseDateTime(String dateString, DateTimeFormatter formatter) {
        try {
            if(StringUtils.isNotBlank(dateString)) {
                return DateTime.parse(dateString, formatter);
            }
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static boolean isValidPeriod(LocalDate start, LocalDate end) {
        return end.compareTo(start) >= 0;
    }

    public static LocalTime parseLocalTime(String timeString) {
        return parseLocalTime(timeString, BNK_FORMATTER_TIME6);
    }

    public static LocalTime parseLocalTime(String timeString, DateTimeFormatter formatter) {
        try {
            if(StringUtils.isNotBlank(timeString)) {
                return LocalTime.parse(timeString, formatter);
            }
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static Years parseYears(String str) {
        try {
            return Years.years(NumberUtils.toInt(str));
        } catch (Exception e) {
            AppLogManager.error(e);
        }
        return null;
    }

    public static YearMonth parseYearMonth(String str) {
        return parseYearMonth(str, BNK_FORMATTER_YYYYMM);
    }

    public static YearMonth parseYearMonth(String str, DateTimeFormatter fmt) {
        if (StringUtils.isNotBlank(str)) {
            try {
                return YearMonth.parse(str, fmt);
            } catch (Exception e) {
                AppLogManager.error(e);
            }
        }

        return null;
    }

    public static Months parseMonths(String str) {
        try {
            return Months.months(NumberUtils.toInt(str));
        } catch (Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static String getShortDayName(DateTime dt) {
        return getShortDayName(dt, Locale.ENGLISH);
    }
    
    /*
    public static String getShortDayName(LocalDateTime ldt) {
    	return getShortDayName(ldt, Locale.ENGLISH);
    }
    
    public static String getShortDayName(LocalDateTime ldt, Locale locale) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("EE");
        return fmt.withLocale(locale).print(ldt);
    }
    */
    
    public static String getShortDayName(DateTime dt, Locale locale) {
    	DateTimeFormatter fmt = DateTimeFormat.forPattern("EE");
        return fmt.withLocale(locale).print(dt);
    }
    
    public static LocalDate today() {
    	return LocalDate.now();
    }

}
