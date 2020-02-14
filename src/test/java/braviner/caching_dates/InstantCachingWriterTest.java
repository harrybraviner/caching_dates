package braviner.caching_dates;

import org.junit.Assert;
import org.junit.Test;
import sun.util.calendar.ZoneInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantCachingWriterTest {

    private static final ZoneId testingTimezone = ZoneId.of("America/Toronto");

    private Instant makeInstant(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
        return ZonedDateTime.of(
                LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond*1_000_000),
                testingTimezone
        ).toInstant();
    }

    @Test
    public void testAutoLatching() {
        InstantCachingWriter writer = new InstantCachingWriter(testingTimezone);

        Assert.assertEquals("2020-01-02T10:30:00.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 30, 0, 0)));
        Assert.assertEquals("34:00.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 0, 0)));
        Assert.assertEquals("100",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 0, 100)));
        Assert.assertEquals("01.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 1, 0)));
        Assert.assertEquals("02.500",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 2, 500)));
    }

    @Test
    public void testManualLatching() {
        InstantCachingWriter writer = new InstantCachingWriter(testingTimezone);
        writer.disableAutoLatching();

        Assert.assertEquals("2020-01-02T10:30:00.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 30, 0, 0)));
        Assert.assertEquals("2020-01-02T10:34:00.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 0, 0)));
        writer.latch();
        Assert.assertEquals("100",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 0, 100)));
        Assert.assertEquals("01.000",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 1, 0)));
        Assert.assertEquals("02.500",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 2, 500)));
        Assert.assertEquals("02.600",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 2, 600)));
        writer.latch();
        Assert.assertEquals("700",
                writer.compressTimestamp(makeInstant(2020, 1, 2, 10, 34, 2, 700)));
    }

}
