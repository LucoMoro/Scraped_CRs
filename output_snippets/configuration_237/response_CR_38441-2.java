//<Beginning of snippet n. 0>


package java.util;

import java.io.Serializable;
import libcore.icu.TimeZones;
import libcore.util.ZoneInfoDB;

public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;

/**
* The short display name style, such as {@code PDT}. Requests for this
* style may yield GMT offsets like {@code GMT-08:00}.
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
* Returns a new SimpleTimeZone for an id of the form "GMT[+|-]hh[[:]mm]", or null.
*/
private static TimeZone getCustomTimeZone(String id) {
        if (id == null || id.length() < 6) {
            throw new IllegalArgumentException("ID is too short: " + id);
        }
        if (!id.matches("^GMT[+-](0[0-9]|1[0-4])(:[0-5][0-9])?$")) {
            throw new IllegalArgumentException("Invalid time zone ID: " + id);
        }

        char sign = id.charAt(3);
        int[] position = new int[1];
        String formattedName = formatTimeZoneName(id, 4);
        int hour = parseNumber(formattedName, 4, position);
        int index = position[0];
        if (index == -1) {
            throw new IllegalArgumentException("Invalid index after parsing hour: " + index);
        }
        int raw = hour * 3600000;
        if (index < formattedName.length() && formattedName.charAt(index) == ':') {
            int minute = parseNumber(formattedName, index + 1, position);
            if (position[0] == -1 || minute < 0 || minute > 59) {
                throw new IllegalArgumentException("Invalid minute found: " + minute);
            }
            raw += minute * 60000;
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

//<End of snippet n. 0>