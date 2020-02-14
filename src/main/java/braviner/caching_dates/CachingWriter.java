package braviner.caching_dates;

/**
 * Class that will do the actual caching work for the other writing classes.
 * Doesn't know anything about java datetime formats, since our caching method involves splitting these up anyway.
 */
class CachingWriter {

    // Track if we've seen any date yet.
    // If false, the values in seenYear, ..., seenMillisecond are meaningless.
    private boolean anyDateSeenYet = false;
    // Separately track if any date has been latched yet.
    private boolean anyDateLatchedYet = false;

    // These values are updated to reflect the most recent time seen.
    private int seenYear = 0;
    private int seenMonth = 0;
    private int seenDayOfMonth = 0;
    private int seenHour = 0;
    private int seenMinute = 0;
    private int seenSecond = 0;

    // When latch() is called, the seen values are copied.
    // These are the values that will be used to determine what parts of the date are serialized.
    private int latchedYear = 0;
    private int latchedMonth = 0;
    private int latchedDayOfMonth = 0;
    private int latchedHour = 0;
    private int latchedMinute = 0;
    private int latchedSecond = 0;

    private boolean autoLatching = true;

    public void disableAutoLatching() {
        autoLatching = false;
    }

    public String compressTimestamp(int year, int month, int dayOfMonth, int hour,
                                    int minute, int second, int millisecond) {
        seenYear = year;
        seenMonth = month;
        seenDayOfMonth = dayOfMonth;
        seenHour = hour;
        seenMinute = minute;
        seenSecond = second;

        String returnString;

        if (!anyDateLatchedYet || (latchedYear != year || latchedMonth != month || latchedDayOfMonth != dayOfMonth)) {
            // Either this is the first timestamp we've received, or the date has changed.
            // In either case we need to output the entire timestamp string.
            anyDateSeenYet = true;
            returnString = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%03d",
                    year, month, dayOfMonth, hour, minute, second, millisecond);
        } else if (latchedHour != hour) {
            returnString = String.format("%02d:%02d:%02d.%03d", hour, minute, second, millisecond);
        } else if (latchedMinute != minute) {
            returnString = String.format("%02d:%02d.%03d", minute, second, millisecond);
        } else if (latchedSecond != second) {
            returnString = String.format("%02d.%03d", second, millisecond);
        } else {
            // We output the millisecond component even if it has not changed.
            returnString = String.format("%03d", millisecond);
        }

        if (autoLatching) {
            internalLatch();
        }

        return returnString;
    }

    public void latch() {
        if (!autoLatching) {
            internalLatch();
        } else {
            throw new IllegalStateException("Called latch when autoLatching is enabled.");
        }
    }

    /**
     * Copy the latched values to the seen values.
     */
    private void internalLatch() {
        if (!anyDateSeenYet) {
            throw new IllegalStateException("Attempting to latch a date before any date has been seen by the "
                + "compressTimestamp method. If this did not result from a call to latch() then this is an internal "
                + "logic error.");
        }

        latchedYear = seenYear;
        latchedMonth = seenMonth;
        latchedDayOfMonth = seenDayOfMonth;
        latchedHour = seenHour;
        latchedMinute = seenMinute;
        latchedSecond = seenSecond;
        anyDateLatchedYet = true;
    }


}
