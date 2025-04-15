/*Stop using getDSTSavings.

The original code was actually correct, but code calling inDaylightTime
and getDSTSavings directly is inherently suspect, so I want to clean up
this false positive along with the real abusers.

Bug: 6901488
Change-Id:I6c89e7aa29d88b81ed2c7fd6c915e0346b90a442*/
//Synthetic comment -- diff --git a/src/com/android/settings/DateTimeSettings.java b/src/com/android/settings/DateTimeSettings.java
//Synthetic comment -- index 9586933..30d4f0a 100644

//Synthetic comment -- @@ -337,8 +337,6 @@
}
}

    /*  Helper routines to format timezone */

/* package */ static void setDate(int year, int month, int day) {
Calendar c = Calendar.getInstance();

//Synthetic comment -- @@ -366,45 +364,40 @@
}
}

/* package */ static String getTimeZoneText(TimeZone tz) {
        boolean daylight = tz.inDaylightTime(new Date());
        StringBuilder sb = new StringBuilder();

        sb.append(formatOffset(tz.getRawOffset() +
                               (daylight ? tz.getDSTSavings() : 0))).
append(", ").
            append(tz.getDisplayName(daylight, TimeZone.LONG));

        return sb.toString();
}

    private static char[] formatOffset(int off) {
        off = off / 1000 / 60;

        char[] buf = new char[9];
        buf[0] = 'G';
        buf[1] = 'M';
        buf[2] = 'T';

if (off < 0) {
            buf[3] = '-';
off = -off;
} else {
            buf[3] = '+';
}

int hours = off / 60;
int minutes = off % 60;

        buf[4] = (char) ('0' + hours / 10);
        buf[5] = (char) ('0' + hours % 10);

        buf[6] = ':';

        buf[7] = (char) ('0' + minutes / 10);
        buf[8] = (char) ('0' + minutes % 10);

        return buf;
}

private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {







