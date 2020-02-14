package braviner.caching_dates;

import java.time.LocalDateTime;

public class LocalDateTimeCachingReader extends CachingReader<LocalDateTime> {

    @Override
    LocalDateTime decode(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond*1_000_000);
    }
}
