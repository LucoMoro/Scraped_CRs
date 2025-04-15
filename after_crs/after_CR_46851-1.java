/*Further clarification of System.currentTimeMillis/Date/SimpleDateFormat.

Change-Id:I91c5bc888e5ae2d368e4c063cd5741e840a21dec*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index 9fe83b5..f8f9146 100644

//Synthetic comment -- @@ -156,7 +156,7 @@
/**
* Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
*
     * <p>This method always returns UTC times, regardless of the system's time zone.
* This is often called "Unix time" or "epoch time".
* Use a {@link java.text.DateFormat} instance to format this time for display to a human.
*








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index 0bfce0a..7c3187c 100644

//Synthetic comment -- @@ -188,8 +188,9 @@
* reason it is almost always necessary and desirable to include the timezone in the output.
* It may also be desirable to set the formatter's time zone to UTC (to ease comparison, or to
* make logs more readable, for example). It is often best to avoid formatting completely when
 * writing dates/times in machine-readable form. Simply sending the "Unix time" as a {@code long}
 * or as the string corresponding to the long is cheaper and unambiguous, and can be formatted any
 * way the recipient deems appropriate.
*
* <h4>Synchronization</h4>
* {@code SimpleDateFormat} is not thread-safe. Users should create a separate instance for








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Date.java b/luni/src/main/java/java/util/Date.java
//Synthetic comment -- index 2e5f8b6..b639003 100644

//Synthetic comment -- @@ -39,6 +39,8 @@
* by {@code DateFormat}, and this class' deprecated computational functionality is
* now provided by {@code Calendar}. Both of these other classes (and their subclasses)
* allow you to interpret a {@code Date} in a given time zone.
 *
 * <p>Note that, surprisingly, instances of this class are mutable.
*/
public class Date implements Serializable, Cloneable, Comparable<Date> {








