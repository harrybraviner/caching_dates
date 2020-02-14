package braviner.caching_dates;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InstantCachingWriter {

    private final ZoneId zoneId;
    private final CachingWriter writer = new CachingWriter();

    public InstantCachingWriter(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public void disableAutoLatching() {
        writer.disableAutoLatching();
    }

    public void latch() {
        writer.latch();
    }

    public String compressTimestamp(Instant timestamp) {
        LocalDateTime localDt = LocalDateTime.ofInstant(timestamp, zoneId);
        return writer.compressTimestamp(localDt.getYear(), localDt.getMonth().getValue(), localDt.getDayOfMonth(),
                localDt.getHour(), localDt.getMinute(), localDt.getSecond(), localDt.getNano() / 1_000_000);
    }

}
