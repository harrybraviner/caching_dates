package braviner.caching_dates;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InstantCachingReader extends CachingReader<Instant> {

    private final ZoneId timezone;

    public InstantCachingReader(ZoneId timezone) {
        this.timezone = timezone;
    }

    @Override
    Instant decode(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond*1_000_000)
                .atZone(timezone).toInstant();
    }
}
