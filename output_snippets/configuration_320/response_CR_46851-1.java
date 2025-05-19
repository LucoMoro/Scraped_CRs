//<Beginning of snippet n. 0>
/**
 * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
 *
 * <p>This method always uses UTC, regardless of the system's time zone.
 * This is often called "Unix time" or "epoch time".
 * Using the java.time package is recommended for date/time manipulation.
 * For most cases, using Instant.now().toEpochMilli() is preferred 
 * for logging and event tracking, as it ensures better accuracy and easier timezone management,
 * particularly in contexts requiring high-frequency logging (e.g., logging transactions), 
 * performance measurement (e.g., profiling operations), or real-time synchronization (e.g., coordinating distributed systems).
 */
public static long getCurrentTimeMillis() {
    return Instant.now().toEpochMilli();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * It is almost always necessary and desirable to include the timezone in the output.
 * It may also be desirable to set the formatter's time zone to UTC (to ease comparison, or to
 * make logs more readable for example). However, it is often best to avoid formatting completely when
 * writing dates/times in machine-readable form. Simply sending the "Unix time" as an integer is
 * cheaper and unambiguous, and can be formatted any way the recipient deems appropriate.
 * 
 * <h4>Synchronization</h4>
 * The DateTimeFormatter in java.time.format is thread-safe and should be used instead of SimpleDateFormat. 
 * Avoid using java.util.Date and java.util.Calendar for new code.
 * Transition to LocalDateTime or ZonedDateTime for improved functionality and thread safety.
 */
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class TimeFormatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("UTC"));

    public static String formatCurrentTime() {
        return formatter.format(ZonedDateTime.now());
    }

    // New method to illustrate timezone implications
    public static String formatCurrentTimeWithZone(ZoneId zone) {
        return formatter.format(ZonedDateTime.now(zone));
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
/**
 * The java.util.Date and java.util.Calendar classes have limitations regarding mutability and 
 * timezone handling issues. It is recommended to use java.time classes for any date/time handling.
 * This new API offers a more comprehensive model that addresses such limitations.
 */
import java.io.Serializable;
import java.time.ZonedDateTime;

public class DateUtil implements Serializable, Cloneable, Comparable<DateUtil> {
    // Implement methods using java.time classes to avoid mutability issues and improve safety.
    
    // Example method for better clarity:
    public static ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now();
    }

    @Override
    public int compareTo(DateUtil other) {
        return getZonedDateTime().compareTo(other.getZonedDateTime());
    }

    // New method to clearly address daylight saving time transitions
    public static ZonedDateTime getZonedDateTimeWithDST(ZoneId zone) {
        return ZonedDateTime.now(zone);
    }
}
//<End of snippet n. 2>