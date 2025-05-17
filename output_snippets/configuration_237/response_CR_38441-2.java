//<Beginning of snippet n. 0>
package java.util;

import java.io.Serializable;
import libcore.icu.TimeZones;
import libcore.util.ZoneInfoDB;

public abstract class TimeZone implements Serializable, Cloneable {
    private static final long serialVersionUID = 3581463369166924961L;

    private static TimeZone getCustomTimeZone(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Time zone identifier cannot be null or empty.");
        }

        id = id.trim();
        
        if (!id.matches("GMT[+|-](0?[0-9]|1[0-4])(:[0-5][0-9])?")) {
            throw new IllegalArgumentException("Invalid time zone format. Expected format: GMT[+|-]hh(:mm)?");
        }

        char sign = id.charAt(3);
        int[] position = new int[1];
        String formattedName = formatTimeZoneName(id, 4);
        int hour = parseNumber(formattedName, 4, position);
        
        if (hour < 0 || hour > 14) {
            throw new IllegalArgumentException("Hour value must be between 0 and 14.");
        }

        int index = position[0];
        
        if (index == -1) {
            throw new IllegalArgumentException("Invalid time zone identifier.");
        }

        int raw = hour * 3600000;
        if (index < formattedName.length() && formattedName.charAt(index) == ':') {
            int minute = parseNumber(formattedName, index + 1, position);
            if (position[0] == -1 || minute < 0 || minute > 59) {
                throw new IllegalArgumentException("Minute value must be between 0 and 59.");
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

    public abstract boolean inDaylightTime(Date time);

    private static int parseNumber(String string, int offset, int[] position) {
        int index = offset, length = string.length(), digit, result = 0;
        while (index < length && (digit = Character.digit(string.charAt(index), 10)) != -1) {
            index++;
            result = result * 10 + digit;
        }
        position[0] = index == offset ? -1 : index;
        return result;
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
// http://code.google.com/p/android/issues/detail?id=11918
public void testHasSameRules() throws Exception {
    TimeZone denver = TimeZone.getTimeZone("America/Denver");
    TimeZone phoenix = TimeZone.getTimeZone("America/Phoenix");
    assertFalse(denver.hasSameRules(phoenix));
}
//<End of snippet n. 1>