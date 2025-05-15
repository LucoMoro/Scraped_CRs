
//<Beginning of snippet n. 0>


package java.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.icu.TimeZones;
import libcore.util.ZoneInfoDB;

public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;

    private static final Pattern CUSTOM_ZONE_ID_PATTERN = Pattern.compile("^GMT[-+](\\d{1,2})(:?(\\d\\d))?$");

/**
* The short display name style, such as {@code PDT}. Requests for this
* style may yield GMT offsets like {@code GMT-08:00}.
* as {@code America/Los_Angeles}. The {@link #getAvailableIDs} method returns
* the supported names.
*
     * <p>This method can also create a custom {@code TimeZone} given an id with the following
     * syntax: {@code GMT[+|-]hh[[:]mm]}. For example, {@code "GMT+05:00"}, {@code "GMT+05"},
     * {@code "GMT+5:00"}, and {@code "GMT+5"} all return an object with a raw offset of +5 hours
     * from UTC, and which does <i>not</i> use daylight savings. These are rarely useful, because
     * they don't correspond to time zones actually in use by humans.
*
* <p>Other than the special cases "UTC" and "GMT" (which are synonymous in this context,
* both corresponding to UTC), Android does not support the deprecated three-letter time
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
*/
public abstract boolean inDaylightTime(Date time);

/**
* Overrides the default time zone for the current process only.
*

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



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
        assertEquals("GMT", TimeZone.getTimeZone("junk").getID());
    }
}

//<End of snippet n. 1>








