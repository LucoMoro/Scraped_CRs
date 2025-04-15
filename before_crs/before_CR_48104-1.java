/*Fix DateUtils.formatElapsedTime.

More reuse of StringBuilders, less broken home-grown formatting code.

Long-term, we should hand this over to icu4c, but they're not ready yet.

Bug:http://code.google.com/p/android/issues/detail?id=41401Bug: 7736688
Change-Id:Ib3c1e1aad05827df646aa18645cce19dffb7551f*/
//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 1060bd8..ddd62a8 100644

//Synthetic comment -- @@ -43,11 +43,6 @@
private static String sElapsedFormatMMSS;
private static String sElapsedFormatHMMSS;

    private static final String FAST_FORMAT_HMMSS = "%1$d:%2$02d:%3$02d";
    private static final String FAST_FORMAT_MMSS = "%1$02d:%2$02d";
    private static final char TIME_SEPARATOR = ':';


public static final long SECOND_IN_MILLIS = 1000;
public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
//Synthetic comment -- @@ -616,19 +611,18 @@
}

/**
     * Formats an elapsed time in the form "MM:SS" or "H:MM:SS"
     * for display on the call-in-progress screen.
*
     * @param recycle {@link StringBuilder} to recycle, if possible
* @param elapsedSeconds the elapsed time in seconds.
*/
public static String formatElapsedTime(StringBuilder recycle, long elapsedSeconds) {
        initFormatStrings();

long hours = 0;
long minutes = 0;
long seconds = 0;

if (elapsedSeconds >= 3600) {
hours = elapsedSeconds / 3600;
elapsedSeconds -= hours * 3600;
//Synthetic comment -- @@ -639,70 +633,23 @@
}
seconds = elapsedSeconds;

        String result;
if (hours > 0) {
            return formatElapsedTime(recycle, sElapsedFormatHMMSS, hours, minutes, seconds);
} else {
            return formatElapsedTime(recycle, sElapsedFormatMMSS, minutes, seconds);
        }
    }

    private static void append(StringBuilder sb, long value, boolean pad, char zeroDigit) {
        if (value < 10) {
            if (pad) {
                sb.append(zeroDigit);
            }
        } else {
            sb.append((char) (zeroDigit + (value / 10)));
        }
        sb.append((char) (zeroDigit + (value % 10)));
    }

    /**
     * Fast formatting of h:mm:ss.
     */
    private static String formatElapsedTime(StringBuilder recycle, String format, long hours,
            long minutes, long seconds) {
        if (FAST_FORMAT_HMMSS.equals(format)) {
            char zeroDigit = LocaleData.get(Locale.getDefault()).zeroDigit;

            StringBuilder sb = recycle;
            if (sb == null) {
                sb = new StringBuilder(8);
            } else {
                sb.setLength(0);
            }
            append(sb, hours, false, zeroDigit);
            sb.append(TIME_SEPARATOR);
            append(sb, minutes, true, zeroDigit);
            sb.append(TIME_SEPARATOR);
            append(sb, seconds, true, zeroDigit);
            return sb.toString();
        } else {
            return String.format(format, hours, minutes, seconds);
        }
    }

    /**
     * Fast formatting of mm:ss.
     */
    private static String formatElapsedTime(StringBuilder recycle, String format, long minutes,
            long seconds) {
        if (FAST_FORMAT_MMSS.equals(format)) {
            char zeroDigit = LocaleData.get(Locale.getDefault()).zeroDigit;

            StringBuilder sb = recycle;
            if (sb == null) {
                sb = new StringBuilder(8);
            } else {
                sb.setLength(0);
            }
            append(sb, minutes, false, zeroDigit);
            sb.append(TIME_SEPARATOR);
            append(sb, seconds, true, zeroDigit);
            return sb.toString();
        } else {
            return String.format(format, minutes, seconds);
}
}








