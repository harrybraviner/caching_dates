package braviner.caching_dates;

import java.time.LocalDateTime;

public class LocalDateTimeCachingWriter {

    private final CachingWriter writer = new CachingWriter();

    public void disableAutoLatching() {
        writer.disableAutoLatching();
    }

    public void latch() {
        writer.latch();
    }

    public String compressTimestamp(LocalDateTime date) {
        return writer.compressTimestamp(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth(),
                date.getHour(), date.getMinute(), date.getSecond(), date.getNano() / 1_000_000);
    }

}
