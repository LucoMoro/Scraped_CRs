/*Stop using getDSTSavings.

The original code was actually correct, but code calling inDaylightTime
and getDSTSavings directly is inherently suspect, so I want to clean up
this false positive along with the real abusers.

Bug: 6901488
Change-Id:I6c89e7aa29d88b81ed2c7fd6c915e0346b90a442*/




//Synthetic comment -- diff --git a/src/com/android/settings/DateTimeSettings.java b/src/com/android/settings/DateTimeSettings.java
//Synthetic comment -- index 9586933..81415f3 100644

//Synthetic comment -- @@ -38,8 +38,10 @@
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSettings extends SettingsPreferenceFragment
//Synthetic comment -- @@ -337,8 +339,6 @@
}
}

/* package */ static void setDate(int year, int month, int day) {
Calendar c = Calendar.getInstance();

//Synthetic comment -- @@ -366,45 +366,40 @@
}
}

    /*  Helper routines to format timezone */

/* package */ static String getTimeZoneText(TimeZone tz) {
        // Similar to new SimpleDateFormat("'GMT'Z, zzzz").format(new Date()), but
        // we want "GMT-03:00" rather than "GMT-0300".
        Date now = new Date();
        return formatOffset(new StringBuilder(), tz, now).
append(", ").
            append(tz.getDisplayName(tz.inDaylightTime(now), TimeZone.LONG)).toString();
}

    private static StringBuilder formatOffset(StringBuilder sb, TimeZone tz, Date d) {
        int off = tz.getOffset(d.getTime()) / 1000 / 60;

        sb.append("GMT");
if (off < 0) {
            sb.append('-');
off = -off;
} else {
            sb.append('+');
}

int hours = off / 60;
int minutes = off % 60;

        sb.append((char) ('0' + hours / 10));
        sb.append((char) ('0' + hours % 10));

        sb.append(':');

        sb.append((char) ('0' + minutes / 10));
        sb.append((char) ('0' + minutes % 10));

        return sb;
}

private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {







