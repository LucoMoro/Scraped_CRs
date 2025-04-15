/*Be more careful when parsing custom zone ids.

The old code was allowing invalid ids.

Bug: 6556561
Change-Id:I691d33fa133527a76bbffa4e3b56a023c389ca8f*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 0abb609..5e17d1b 100644

//Synthetic comment -- @@ -18,6 +18,8 @@
package java.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.icu.TimeZones;
import libcore.util.ZoneInfoDB;

//Synthetic comment -- @@ -28,7 +30,7 @@
* <p>Most applications will use {@link #getDefault} which returns a {@code TimeZone} based on
* the time zone where the program is running.
*
 * <p>You can also get a specific {@code TimeZone} {@link #getTimeZone by Olson ID}.
*
* <p>It is highly unlikely you'll ever want to use anything but the factory methods yourself.
* Let classes like {@link Calendar} and {@link java.text.SimpleDateFormat} do the date
//Synthetic comment -- @@ -64,6 +66,8 @@
public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;

    private static final Pattern CUSTOM_ZONE_ID_PATTERN = Pattern.compile("^GMT[-+](\\d{1,2})(:?(\\d\\d))?$");

/**
* The short display name style, such as {@code PDT}. Requests for this
* style may yield GMT offsets like {@code GMT-08:00}.
//Synthetic comment -- @@ -265,17 +269,17 @@
public abstract int getRawOffset();

/**
     * Returns a {@code TimeZone} corresponding to the given {@code id}, or {@code GMT} on failure.
*
     * <p>An ID can be an Olson name of the form <i>Area</i>/<i>Location</i>, such
* as {@code America/Los_Angeles}. The {@link #getAvailableIDs} method returns
* the supported names.
*
     * <p>This method can also create a custom {@code TimeZone} given an ID with the following
     * syntax: {@code GMT[+|-]hh[[:]mm]}. For example, {@code "GMT+05:00"}, {@code "GMT+05"},
     * {@code "GMT+5:00"}, and {@code "GMT+5"} all return an object with a raw offset of +5 hours
     * from UTC, and which does <i>not</i> use daylight savings. These are rarely useful, because
     * they don't correspond to time zones actually in use by humans.
*
* <p>Other than the special cases "UTC" and "GMT" (which are synonymous in this context,
* both corresponding to UTC), Android does not support the deprecated three-letter time
//Synthetic comment -- @@ -299,63 +303,34 @@
* Returns a new SimpleTimeZone for an id of the form "GMT[+|-]hh[[:]mm]", or null.
*/
private static TimeZone getCustomTimeZone(String id) {
        Matcher m = CUSTOM_ZONE_ID_PATTERN.matcher(id);
        if (!m.matches()) {
return null;
}

        int hour;
        int minute = 0;
        try {
            hour = Integer.parseInt(m.group(1));
            if (m.group(3) != null) {
                minute = Integer.parseInt(m.group(3));
}
        } catch (NumberFormatException impossible) {
            throw new AssertionError(impossible);
}

        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            return null;
        }

        char sign = id.charAt(3);
        int raw = (hour * 3600000) + (minute * 60000);
if (sign == '-') {
raw = -raw;
}

        String cleanId = String.format("GMT%c%02d:%02d", sign, hour, minute);
        return new SimpleTimeZone(raw, cleanId);
}

/**
//Synthetic comment -- @@ -377,17 +352,6 @@
*/
public abstract boolean inDaylightTime(Date time);

/**
* Overrides the default time zone for the current process only.
*








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/TimeZoneTest.java b/luni/src/test/java/libcore/java/util/TimeZoneTest.java
//Synthetic comment -- index 39ac19d..34f05c4 100644

//Synthetic comment -- @@ -144,8 +144,36 @@

// http://code.google.com/p/android/issues/detail?id=11918
public void testHasSameRules() throws Exception {
        TimeZone denver = TimeZone.getTimeZone("America/Denver");
        TimeZone phoenix = TimeZone.getTimeZone("America/Phoenix");
assertFalse(denver.hasSameRules(phoenix));
}

    // http://b.corp.google.com/issue?id=6556561
    public void testCustomZoneIds() throws Exception {
        // These are all okay (and equivalent).
        assertEquals("GMT+05:00", TimeZone.getTimeZone("GMT+05:00").getID());
        assertEquals("GMT+05:00", TimeZone.getTimeZone("GMT+5:00").getID());
        assertEquals("GMT+05:00", TimeZone.getTimeZone("GMT+0500").getID());
        assertEquals("GMT+05:00", TimeZone.getTimeZone("GMT+500").getID());
        assertEquals("GMT+05:00", TimeZone.getTimeZone("GMT+5").getID());
        // These aren't.
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5.5").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:5").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:0").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:005").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:000").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+005:00").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+05:99").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+28:00").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+05:00.00").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+05:00:00").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+junk").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5junk").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:junk").getID());
        assertEquals("GMT", TimeZone.getTimeZone("GMT+5:00junk").getID());
        assertEquals("GMT", TimeZone.getTimeZone("junkGMT+5:00").getID());
        assertEquals("GMT", TimeZone.getTimeZone("junk").getID());
        assertEquals("GMT", TimeZone.getTimeZone("gmt+5:00").getID());
    }
}







