package braviner.caching_dates;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantCachingReaderTest {

    private static final ZoneId testingTimezone = ZoneId.of("America/Toronto");

    @SuppressWarnings("SameParameterValue")
    private Instant makeInstant(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
        return ZonedDateTime.of(
                LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond*1_000_000),
                testingTimezone
        ).toInstant();
    }

    @Test
    public void testFewRows() {
        InstantCachingReader instantCachingReader = new InstantCachingReader(testingTimezone);

        Assert.assertEquals(makeInstant(2020, 1, 2, 10, 30, 0, 0),
                instantCachingReader.decompressTimestamp("2020-01-02T10:30:00.000"));
        Assert.assertEquals(makeInstant(2020, 1, 2, 10, 35, 0, 0),
                instantCachingReader.decompressTimestamp("35:00.000"));
        Assert.assertEquals(makeInstant(2020, 1, 2, 10, 35, 0, 500),
                instantCachingReader.decompressTimestamp("500"));
        Assert.assertEquals(makeInstant(2020, 1, 2, 10, 40, 30, 500),
                instantCachingReader.decompressTimestamp("40:30.500"));
        Assert.assertEquals(makeInstant(2022, 1, 2, 10, 30, 0, 0),
                instantCachingReader.decompressTimestamp("2022-01-02T10:30:00.000"));
        Assert.assertEquals(makeInstant(2022, 1, 5, 10, 30, 0, 0),
                instantCachingReader.decompressTimestamp("2022-01-05T10:30:00.000"));
        Assert.assertEquals(makeInstant(2022, 1, 5, 11, 30, 0, 0),
                instantCachingReader.decompressTimestamp("11:30:00.000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnPartialDateFirst() {
        InstantCachingReader instantCachingReader = new InstantCachingReader(testingTimezone);
        // This is the first timestamp we've read. We need the date component, but we don't get it.
        instantCachingReader.decompressTimestamp("10:30:00.000");
    }
}
