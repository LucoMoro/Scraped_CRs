//<Beginning of snippet n. 0>


/**
 * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
 *
 * <p>This method always uses UTC, regardless of the system's time zone.
 * This is often called "Unix time" or "epoch time".
 * Use a {@link java.time.format.DateTimeFormatter} instance to format this time for display to a human.
 * 
 * <p>It's almost always necessary and desirable to include the timezone in the output.
 * It may also be desirable to set the formatter's time zone to UTC to ease comparison
 * or make logs more readable. It's often best to avoid formatting completely when
 * writing dates/times in machine-readable form. Simply sending the "Unix time" as an integer is
 * cheaper and unambiguous, and can be formatted any way the recipient deems appropriate.
 *
 * <h4>Synchronization</h4>
 * {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for 
 * each thread or use {@link java.time.format.DateTimeFormatter} which is immutable and thread-safe.
 * 
 * <h4>Time Zone Management</h4>
 * It’s crucial to manage time zones carefully in applications with multiple users
 * to avoid issues stemming from improper handling.
 */
public class Date implements Serializable, Cloneable, Comparable<Date> {


//<End of snippet n. 0>