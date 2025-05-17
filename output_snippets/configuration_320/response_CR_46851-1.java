//<Beginning of snippet n. 0>


/**
 * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
 *
 * <p>This method always uses UTC, regardless of the system's time zone.
 * This is often called "Unix time" or "epoch time".
 * It is recommended to avoid formatting when writing dates/times in machine-readable form.
 * Simply sending the Unix time as a long integer is more efficient and unambiguous.
 * Use {@link java.time.format.DateTimeFormatter} for human-readable formatting.
 *
 * <h4>Synchronization</h4>
 * Use {@code java.time.format.DateTimeFormatter} which is thread-safe.
 * Avoid using {@code SimpleDateFormat} in multi-threaded environments.
 * Both {@code ZonedDateTime} and {@code Instant} allow you to interpret a 
 * current time in a given time zone.
 * Note that {@code Date}, {@code SimpleDateFormat}, and {@code DateFormat} are deprecated.
 * Prefer using classes from the {@code java.time} package from Java 8 onwards.
 */
public class Date implements Serializable, Cloneable, Comparable<Date> {


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


/**
 * Reason it is almost always necessary and desirable to include the timezone in the output.
 * It may also be desirable to set the formatter's time zone to UTC (to ease comparison or 
 * make logs more readable, for example). Avoid formatting completely when writing 
 * dates/times in machine-readable form. Simply sending the Unix time as an integer is 
 * cheaper and unambiguous, and can be formatted any way the recipient deems appropriate.
 *
 * <h4>Examples</h4>
 * - For machine readability, use: 
 *   `long currentTimeMillis = System.currentTimeMillis();`
 * - For human readability, use:
 *   `DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);`
 *   `String formattedDate = ZonedDateTime.now(ZoneOffset.UTC).format(formatter);` 
//<End of snippet n. 1>










//<Beginning of snippet n. 2>


/**
 * By {@code DateFormat}, and this class's deprecated computational functionality is
 * now provided by {@code Calendar}. All classes in this context should be transitioned 
 * to use the new date and time API introduced in Java 8, which includes the classes 
 * {@code LocalDateTime}, {@code ZonedDateTime}, and {@code Instant}.
 */
public class Date implements Serializable, Cloneable, Comparable<Date> {


//<End of snippet n. 2>







