package nftspy.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    private int second;
    private int minute;
    private int hour;

    private int year;
    private int month;
    private int day;

    public DateTime(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public DateTime(int second, int minute, int hour, int year, int month, int day) {
        this(year, month, day);
        setSecond(second);
        setMinute(minute);
        setHour(hour);
    }

    public void backInMonth(int months) {
        if (month - months <= 0) {
            int numberOfYears = months / 12;
            int remainingMonths = months - 12 * numberOfYears;
            setYear(year - numberOfYears);
            setMonth(month - remainingMonths);
        } else {
            setMonth(month - months);
        }
    }

    public static DateTime now() {
        LocalDateTime t = LocalDateTime.now();
        return new DateTime(
                t.getSecond(),
                t.getMinute(),
                t.getHour(),
                t.getYear(),
                t.getMonthValue(),
                t.getDayOfMonth());
    }

    public static DateTime fromString(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d h:m:s");
        LocalDateTime t = LocalDate.parse(s, formatter).atStartOfDay();
        return new DateTime(
                t.getSecond(),
                t.getMinute(),
                t.getHour(),
                t.getYear(),
                t.getMonthValue(),
                t.getDayOfMonth());
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        assert 0 <= hour && hour <= 23;
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        assert 0 <= minute && minute <= 59;
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        assert 0 <= second && second <= 59;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        assert 1 <= day && day <= 31;
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        assert 1 <= month && month <= 12;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        assert year >= 1;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                year, month, day, hour, minute, second);
    }
}
