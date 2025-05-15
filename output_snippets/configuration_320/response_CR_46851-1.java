//<Beginning of snippet n. 0>

/**
* Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
*
* <p>This method always uses UTC, regardless of the system's time zone.
* This is often called "Unix time" or "epoch time".
* Use a {@link java.time.Instant} instance to format this time for display to a human.
*
* <p>It is almost always necessary and desirable to include the timezone in the output.
* It may also be desirable to set the formatter's time zone to UTC (to ease comparison, or to
* make logs more readable, for example). It is often best to avoid formatting completely when
* writing dates/times in machine-readable form. Simply sending the "Unix time" as an integer is
* cheaper and unambiguous, and can be formatted any way the recipient deems appropriate.
*
* <h4>Synchronization</h4>
* {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for
* each thread if using {@code SimpleDateFormat}. Consider using {@code java.time.format.DateTimeFormatter}
* for thread-safe formatting and parsing.
*
* @return the current time in milliseconds since epoch in UTC
*/
public static long currentTimeMillis() {
    return System.currentTimeMillis();
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

/**
* {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for
* each thread if using {@code SimpleDateFormat}. Consider alternative APIs such as 
* {@link java.time.LocalDateTime} or {@link java.time.ZonedDateTime} for better thread safety.
*
* When working with timestamps, it's recommended to convert to the desired time zone only when displaying 
* to users. To keep internal representations consistent, use UTC throughout the application.
*/

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

import java.io.Serializable;
import java.time.Instant;

/**
* The Date class represents specific instant in time, with millisecond precision.
* <p>It is recommended to use the newer Java date/time API starting from Java 8. 
* Use {@link java.time.Instant} for timestamps and {@link java.time.LocalDateTime} for date without timezone.
*/
public class Date implements Serializable, Cloneable, Comparable<Date> {
    private Instant instant;

    public Date() {
        this.instant = Instant.now();
    }

    // Other methods
}

//<End of snippet n. 2>