package tr.ozanbey.agricalc.webapp.webapp.util.helpers;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DateHelper {

    public static final String MYSQL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static ZoneId getSystemDefaultTimeZone() {
        return ZoneId.systemDefault();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();

    }

    public static LocalDate today() {
        return LocalDate.now();

    }

    public static String formatDateTime(String pattern, Temporal dateTime) {
        if (dateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = (LocalDateTime) dateTime;
        return localDateTime.format(formatter);

    }

    public static String formatLocalDate(String pattern, LocalDate date) {
        if (date == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);

    }

    public static String toMySQLDateFormat(LocalDateTime date) {
        return formatDateTime(MYSQL_DATE_PATTERN, date);

    }

    public static LocalDateTime addDaysToDateTime(LocalDateTime target, int days) {
        return target.plusDays(days);

    }

    public static LocalDateTime subtractDaysFromDateTime(LocalDateTime target, int days) {
        return target.minusDays(days);

    }

    public static LocalDate addDaysToDate(LocalDate target, int days) {
        return target.plusDays(days);

    }

    public static LocalDate subtractDaysFromDate(LocalDate target, int days) {
        return target.minusDays(days);

    }

    public static LocalDateTime addMinutes(LocalDateTime target, int minutes) {
        return target.plusMinutes(minutes);

    }

    public static LocalDateTime subtractMinutes(LocalDateTime target, int minutes) {
        return target.minusMinutes(minutes);

    }

    public static LocalDateTime addSeconds(LocalDateTime target, int seconds) {
        return target.plusSeconds(seconds);

    }

    public static LocalDateTime subtractSeconds(LocalDateTime target, int seconds) {
        return target.minusSeconds(seconds);

    }

    public static long dayDiff(Temporal start, Temporal end) {
        return ChronoUnit.DAYS.between(start, end);

    }

    private static LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());

    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return convertDateToLocalDateTime(date).toLocalDate();

    }

    public static List<LocalDate> getInstallmentMonthsStartingFromNextMonthTo(int to) {
        List<LocalDate> monthList = new ArrayList<>();
        LocalDate now = handleEndOfMonthForInstallmentDate(LocalDate.now());

        for (int i = 0; i < to; i++) {
            LocalDate futureDate = now.withDayOfMonth(1).plusMonths(i + 1);
            monthList.add(futureDate);
        }

        return monthList;

    }

    public static List<LocalDate> getInstallmentMonthsEndingFromNowTo(int to) {
        List<LocalDate> monthList = new ArrayList<>();
        LocalDate now = handleEndOfMonthForInstallmentDate(LocalDate.now());

        for (int i = 0; i < to; i++) {
            LocalDate backDate = now.withDayOfMonth(1).minusMonths(i);
            monthList.add(backDate);
        }

        return monthList;

    }

    public static List<LocalDate> getInstallmentMonthsStartingFromNowTo(int to) {
        List<LocalDate> monthList = new ArrayList<>();
        LocalDate now = handleEndOfMonthForInstallmentDate(LocalDate.now());

        for (int i = 0; i < to; i++) {
            LocalDate futureDate = now.withDayOfMonth(1).plusMonths(i);
            monthList.add(futureDate);
        }

        return monthList;

    }

    public static LocalDate addMonthsToDateForInstallment(LocalDate date, int months) {
        LocalDate handledDate = handleEndOfMonthForInstallmentDate(date);
        return handledDate.withDayOfMonth(1).plusMonths(months);

    }

    public static List<LocalDate> addMonthsToDateForInstallmentDatesStartingNextMonth(LocalDate date, int months) {
        List<LocalDate> monthList = new ArrayList<>();
        LocalDate handledDate = handleEndOfMonthForInstallmentDate(date);

        for (int i = 0; i < months; i++) {
            LocalDate futureDate = handledDate.withDayOfMonth(1).plusMonths(i + 1);
            monthList.add(futureDate);
        }

        return monthList;

    }

    public static List<LocalDate> addMonthsToDateForInstallmentDatesStartingNow(LocalDate date, int months) {
        List<LocalDate> monthList = new ArrayList<>();
        LocalDate handledDate = handleEndOfMonthForInstallmentDate(date);

        for (int i = 0; i < months; i++) {
            LocalDate futureDate = handledDate.withDayOfMonth(1).plusMonths(i);
            monthList.add(futureDate);
        }

        return monthList;

    }

    private static LocalDate handleEndOfMonthForInstallmentDate(LocalDate date) {
        if (date.getDayOfMonth() == 29 || date.getDayOfMonth() == 30 || date.getDayOfMonth() == 31) {
            return date.withDayOfMonth(1).plusMonths(1);
        } else {
            return date;

        }

    }

    public static Long daysBetweenToday(Temporal date) {
        LocalDate now = LocalDate.now();
        return ChronoUnit.DAYS.between(now, date);

    }

    public static long monthsBetweenToday(Temporal date) {
        LocalDate now = LocalDate.now();
        return ChronoUnit.MONTHS.between(now, date);

    }

    public static long yearsBetweenToday(Temporal date) {
        LocalDate now = LocalDate.now();
        return ChronoUnit.YEARS.between(now, date);

    }

    public static long daysBetweenTwoDate(Temporal date1, Temporal date2) {
        return ChronoUnit.DAYS.between(date1, date2);

    }

    public static long monthsBetweenTwoDate(Temporal date1, Temporal date2) {
        return ChronoUnit.MONTHS.between(date1, date2);

    }

    public static long yearsBetweenTwoDate(Temporal date1, Temporal date2) {
        return ChronoUnit.YEARS.between(date1, date2);

    }

    public static Date convertLocalDateToDate(Temporal target) {
        if (target instanceof LocalDate dateToConvert) {
            return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else if (target instanceof LocalDateTime dateToConvert) {
            return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

}
