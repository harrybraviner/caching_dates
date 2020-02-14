package braviner.caching_dates;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class LocalDateTimeCachingReaderTests {
    @Test
    public void testFewRows() {
        LocalDateTimeCachingReader localDateTimeCachingReader = new LocalDateTimeCachingReader();

        Assert.assertEquals(LocalDateTime.of(2020, 1, 2,
                10, 30, 0, 0),
                localDateTimeCachingReader.decompressTimestamp("2020-01-02T10:30:00.000"));
        Assert.assertEquals(LocalDateTime.of(2020, 1, 2, 10,
                35, 0, 0),
                localDateTimeCachingReader.decompressTimestamp("35:00.000"));
        Assert.assertEquals(LocalDateTime.of(2020, 1, 2,
                10, 35, 0, 500*1_000_000),
                localDateTimeCachingReader.decompressTimestamp("500"));
        Assert.assertEquals(LocalDateTime.of(2020, 1, 2,
                10, 40, 30, 500*1_000_000),
                localDateTimeCachingReader.decompressTimestamp("40:30.500"));
        Assert.assertEquals(LocalDateTime.of(2022, 1, 2,
                10, 30, 0, 0),
                localDateTimeCachingReader.decompressTimestamp("2022-01-02T10:30:00.000"));
        Assert.assertEquals(LocalDateTime.of(2022, 1, 5,
                10, 30, 0, 0),
                localDateTimeCachingReader.decompressTimestamp("2022-01-05T10:30:00.000"));
        Assert.assertEquals(LocalDateTime.of(2022, 1, 5,
                11, 30, 0, 0),
                localDateTimeCachingReader.decompressTimestamp("11:30:00.000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnPartialDateFirst() {
        LocalDateTimeCachingReader localDateTimeCachingReader = new LocalDateTimeCachingReader();
        // This is the first timestamp we've read. We need the date component, but we don't get it.
        localDateTimeCachingReader.decompressTimestamp("10:30:00.000");
    }
}
