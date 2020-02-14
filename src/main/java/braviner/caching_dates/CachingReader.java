package braviner.caching_dates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class CachingReader<T> {

    // If we haven't seen a date then we need a full date to be able to do a valid read.
    private boolean anyDateSeenYet = false;


    // These values are updated to reflect the most recent time read.
    private int prevYear = 0;
    private int prevMonth = 0;
    private int prevDayOfMonth = 0;
    private int prevHour = 0;
    private int prevMinute = 0;
    private int prevSecond = 0;

    private static final Pattern fullDatePattern =
            Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");
    private static final Pattern timePattern =
            Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");
    private static final Pattern minutesPattern =
            Pattern.compile("(\\d{2}):(\\d{2}).(\\d{3})");
    private static final Pattern secondsPattern =
            Pattern.compile("(\\d{2}).(\\d{3})");
    private static final Pattern millisecondsPattern =
            Pattern.compile("(\\d{3})");


    public T decompressTimestamp(String timestamp) {
        if (!anyDateSeenYet && timestamp.length() != 23) {
            throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a full timestamp, "
                + "and we have not yet read a timestamp.");
        }

        Matcher matcher;
        switch (timestamp.length()) {
            case 23:
                matcher = fullDatePattern.matcher(timestamp);
                if (matcher.matches()) {
                    return cacheAndDecode(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)),
                            Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)),
                            Integer.parseInt(matcher.group(7)));
                } else {
                    throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
                }
            case 12:
                matcher = timePattern.matcher(timestamp);
                if (matcher.matches()) {
                    return cacheAndDecode(prevYear, prevMonth, prevDayOfMonth, Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4)));
                } else {
                    throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
                }
            case 9:
                matcher = minutesPattern.matcher(timestamp);
                if (matcher.matches()) {
                    return cacheAndDecode(prevYear, prevMonth, prevDayOfMonth, prevHour,
                            Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3)));
                } else {
                    throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
                }
            case 6:
                matcher = secondsPattern.matcher(timestamp);
                if (matcher.matches()) {
                    return cacheAndDecode(prevYear, prevMonth, prevDayOfMonth, prevHour, prevMinute,
                            Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                } else {
                    throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
                }
            case 3:
                matcher = millisecondsPattern.matcher(timestamp);
                if (matcher.matches()) {
                    return cacheAndDecode(prevYear, prevMonth, prevDayOfMonth, prevHour, prevMinute, prevSecond,
                            Integer.parseInt(matcher.group(1)));
                } else {
                    throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
                }
            default:
                throw new IllegalArgumentException("Unable to parse \"" + timestamp + "\" as a timestamp.");
        }
    }

    private T cacheAndDecode(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
        anyDateSeenYet = true;
        prevYear = year;
        prevMonth = month;
        prevDayOfMonth = dayOfMonth;
        prevHour = hour;
        prevMinute = minute;
        prevSecond = second;

        return decode(year, month, dayOfMonth, hour, minute, second, millisecond);
    }

    /**
     * Convert the year, ..., millisecond data into format T.
     */
    abstract T decode(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond);

}
