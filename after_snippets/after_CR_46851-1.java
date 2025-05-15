
//<Beginning of snippet n. 0>


/**
* Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
*
     * <p>This method always returns UTC times, regardless of the system's time zone.
* This is often called "Unix time" or "epoch time".
* Use a {@link java.text.DateFormat} instance to format this time for display to a human.
*

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


* reason it is almost always necessary and desirable to include the timezone in the output.
* It may also be desirable to set the formatter's time zone to UTC (to ease comparison, or to
* make logs more readable, for example). It is often best to avoid formatting completely when
 * writing dates/times in machine-readable form. Simply sending the "Unix time" as a {@code long}
 * or as the string corresponding to the long is cheaper and unambiguous, and can be formatted any
 * way the recipient deems appropriate.
*
* <h4>Synchronization</h4>
* {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


* by {@code DateFormat}, and this class' deprecated computational functionality is
* now provided by {@code Calendar}. Both of these other classes (and their subclasses)
* allow you to interpret a {@code Date} in a given time zone.
 *
 * <p>Note that, surprisingly, instances of this class are mutable.
*/
public class Date implements Serializable, Cloneable, Comparable<Date> {


//<End of snippet n. 2>








