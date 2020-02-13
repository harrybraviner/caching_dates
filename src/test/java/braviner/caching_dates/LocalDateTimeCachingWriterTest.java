package braviner.caching_dates;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class LocalDateTimeCachingWriterTest {

    @Test
    public void testAutoLatching() {
        LocalDateTimeCachingWriter writer = new LocalDateTimeCachingWriter();

        Assert.assertEquals("2020-01-02T10:30:00.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 30, 0, 0)));
        Assert.assertEquals("34:00.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 0, 0)));
        Assert.assertEquals("100",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 0, 100*1_000_000)));
        Assert.assertEquals("01.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 1, 0)));
        Assert.assertEquals("02.500",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 2, 500*1_000_000)));
    }

    @Test
    public void testManualLatching() {
        LocalDateTimeCachingWriter writer = new LocalDateTimeCachingWriter();
        writer.disableAutoLatching();

        Assert.assertEquals("2020-01-02T10:30:00.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 30, 0, 0)));
        Assert.assertEquals("2020-01-02T10:34:00.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 0, 0)));
        writer.latch();
        Assert.assertEquals("100",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 0, 100*1_000_000)));
        Assert.assertEquals("01.000",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 1, 0)));
        Assert.assertEquals("02.500",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 2, 500*1_000_000)));
        Assert.assertEquals("02.600",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 2, 600*1_000_000)));
        writer.latch();
        Assert.assertEquals("700",
                writer.compressTimestamp(LocalDateTime.of(2020, 1, 2, 10, 34, 2, 700*1_000_000)));
    }
}
