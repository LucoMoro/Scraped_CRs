/*Be more careful when parsing custom zone ids.

The old code was allowing invalid ids.

Bug: 6556561
Change-Id:I691d33fa133527a76bbffa4e3b56a023c389ca8f*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 0abb609..96cc4cf 100644

//Synthetic comment -- @@ -18,6 +18,8 @@
package java.util;

import java.io.Serializable;
import libcore.icu.TimeZones;
import libcore.util.ZoneInfoDB;

//Synthetic comment -- @@ -64,6 +66,8 @@
public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;

/**
* The short display name style, such as {@code PDT}. Requests for this
* style may yield GMT offsets like {@code GMT-08:00}.
//Synthetic comment -- @@ -271,11 +275,11 @@
* as {@code America/Los_Angeles}. The {@link #getAvailableIDs} method returns
* the supported names.
*
     * <p>This method can also create a custom {@code TimeZone} using the following
     * syntax: {@code GMT[+|-]hh[[:]mm]}. For example, {@code TimeZone.getTimeZone("GMT+14:00")}
     * would return an object with a raw offset of +14 hours from UTC, and which does <i>not</i>
     * use daylight savings. These are rarely useful, because they don't correspond to time
     * zones actually in use.
*
* <p>Other than the special cases "UTC" and "GMT" (which are synonymous in this context,
* both corresponding to UTC), Android does not support the deprecated three-letter time
//Synthetic comment -- @@ -299,63 +303,34 @@
* Returns a new SimpleTimeZone for an id of the form "GMT[+|-]hh[[:]mm]", or null.
*/
private static TimeZone getCustomTimeZone(String id) {
        char sign = id.charAt(3);
        if (sign != '+' && sign != '-') {
return null;
}
        int[] position = new int[1];
        String formattedName = formatTimeZoneName(id, 4);
        int hour = parseNumber(formattedName, 4, position);
        if (hour < 0 || hour > 23) {
            return null;
        }
        int index = position[0];
        if (index == -1) {
            return null;
        }
        int raw = hour * 3600000;
        if (index < formattedName.length() && formattedName.charAt(index) == ':') {
            int minute = parseNumber(formattedName, index + 1, position);
            if (position[0] == -1 || minute < 0 || minute > 59) {
                return null;
}
            raw += minute * 60000;
        } else if (hour >= 30 || index > 6) {
            raw = (hour / 100 * 3600000) + (hour % 100 * 60000);
}
if (sign == '-') {
raw = -raw;
}
        return new SimpleTimeZone(raw, formattedName);
    }

    private static String formatTimeZoneName(String name, int offset) {
        StringBuilder buf = new StringBuilder();
        int index = offset, length = name.length();
        buf.append(name.substring(0, offset));

        while (index < length) {
            if (Character.digit(name.charAt(index), 10) != -1) {
                buf.append(name.charAt(index));
                if ((length - (index + 1)) == 2) {
                    buf.append(':');
                }
            } else if (name.charAt(index) == ':') {
                buf.append(':');
            }
            index++;
        }

        if (buf.toString().indexOf(":") == -1) {
            buf.append(':');
            buf.append("00");
        }

        if (buf.toString().indexOf(":") == 5) {
            buf.insert(4, '0');
        }

        return buf.toString();
}

/**
//Synthetic comment -- @@ -377,17 +352,6 @@
*/
public abstract boolean inDaylightTime(Date time);

    private static int parseNumber(String string, int offset, int[] position) {
        int index = offset, length = string.length(), digit, result = 0;
        while (index < length
                && (digit = Character.digit(string.charAt(index), 10)) != -1) {
            index++;
            result = result * 10 + digit;
        }
        position[0] = index == offset ? -1 : index;
        return result;
    }

/**
* Overrides the default time zone for the current process only.
*








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/TimeZoneTest.java b/luni/src/test/java/libcore/java/util/TimeZoneTest.java
//Synthetic comment -- index 39ac19d..66b0e83 100644

//Synthetic comment -- @@ -144,8 +144,34 @@

// http://code.google.com/p/android/issues/detail?id=11918
public void testHasSameRules() throws Exception {
        TimeZone denver = TimeZone.getTimeZone ("America/Denver") ;
        TimeZone phoenix = TimeZone.getTimeZone ("America/Phoenix") ;
assertFalse(denver.hasSameRules(phoenix));
}
}







