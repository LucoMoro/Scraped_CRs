//<Beginning of snippet n. 0>


/**
 * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
 *
 * <p>This method always uses UTC, regardless of the system's time zone.
 * This is often called "Unix time" or "epoch time".
 * Use a {@link java.time.format.DateTimeFormatter} instance to format this time for display to a human.
 * <h4>Synchronization</h4>
 * {@code DateTimeFormatter} is thread-safe. Users can reuse the same instance across threads.
 * It is often best to avoid formatting completely when writing dates/times in machine-readable form. 
 * Simply sending the "Unix time" as an integer is cheaper and unambiguous, and can be formatted 
 * any way the recipient deems appropriate.
 *
 * <h4>Deprecation</h4>
 * The {@code java.util.Date} class is deprecated. Users are encouraged to use 
 * the {@code java.time} API for date and time functionality.
 * Both {@code ZonedDateTime} and {@code Instant} provide better handling of date and time with timezone support.
 * 
 * <h4>Use Cases</h4>
 * Precise timing is critical for logging and performance measurement. For example, recording the
 * execution time of a task can be done like so:
 * long startTime = System.currentTimeMillis();
 * // ... perform some task ...
 * long endTime = System.currentTimeMillis();
 * long duration = endTime - startTime;
 * System.out.println("Execution time: " + duration + "ms");
 */

//<End of snippet n. 0>


//<Beginning of snippet n. 1>

 * It is almost always necessary and desirable to include the timezone in the output.
 * It may also be desirable to set the formatter's time zone to UTC (to ease comparison, or to
 * make logs more readable, for example).
 *
 * <h4>Synchronization</h4>
 * Users should utilize {@code ThreadLocal<DateTimeFormatter>} for thread-safe formatting.
 * {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for 
 * each thread that requires it.
 * It is recommended to use the {@code java.time} API for new code, as it provides better 
 * support for time zones and formatting.
 * <h4>Examples</h4>
 * Here is an example of getting the current time:
 * long currentTimeMillis = System.currentTimeMillis();
 * Instant instant = Instant.ofEpochMilli(currentTimeMillis);
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
 *                                           .withZone(ZoneOffset.UTC);
 * String formattedTime = formatter.format(instant);
 * System.out.println("Formatted time: " + formattedTime);
 * 
 * // Example of parsing a date string 
 * String dateString = "2023-10-01T10:15:30Z";
 * ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
 * System.out.println("Parsed ZonedDateTime: " + zonedDateTime);
 * 
 * // Example of using ThreadLocal for date formatting
 * ThreadLocal<DateTimeFormatter> threadLocalFormatter = ThreadLocal.withInitial(() -> 
 *     DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC));
 * String formattedTimeThreadSafe = threadLocalFormatter.get().format(instant);
 * System.out.println("Thread-safe formatted time: " + formattedTimeThreadSafe);

//<End of snippet n. 1>


//<Beginning of snippet n. 2>

 * by {@code DateFormat}, and this class' deprecated computational functionality is
 * now provided by {@code Calendar}. Both of these other classes (and their subclasses)
 * allow you to interpret a {@code LocalDateTime} in a given time zone.
 */
public class Date implements Serializable, Cloneable, Comparable<Date> {

    // Replaced deprecated java.util.Date with java.time API where applicable
    private ZonedDateTime zonedDateTime;

    public Date() {
        this.zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    @Override
    public int compareTo(Date o) {
        return this.zonedDateTime.compareTo(o.getZonedDateTime());
    }

    // Other necessary methods to support functionality can be added here
}

//<End of snippet n. 2>