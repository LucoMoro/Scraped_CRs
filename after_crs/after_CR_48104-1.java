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

public static final long SECOND_IN_MILLIS = 1000;
public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
//Synthetic comment -- @@ -616,19 +611,18 @@
}

/**
     * Formats an elapsed time in a format like "MM:SS" or "H:MM:SS" (using a form
     * suited to the current locale), similar to that used on the call-in-progress
     * screen.
*
     * @param recycle {@link StringBuilder} to recycle, or null to use a temporary one.
* @param elapsedSeconds the elapsed time in seconds.
*/
public static String formatElapsedTime(StringBuilder recycle, long elapsedSeconds) {
        // Break the elapsed seconds into hours, minutes, and seconds.
long hours = 0;
long minutes = 0;
long seconds = 0;
if (elapsedSeconds >= 3600) {
hours = elapsedSeconds / 3600;
elapsedSeconds -= hours * 3600;
//Synthetic comment -- @@ -639,70 +633,23 @@
}
seconds = elapsedSeconds;

        // Create a StringBuilder if we weren't given one to recycle.
        // TODO: if we cared, we could have a thread-local temporary StringBuilder.
        StringBuilder sb = recycle;
        if (sb == null) {
            sb = new StringBuilder(8);
        } else {
            sb.setLength(0);
        }

        // Format the broken-down time in a locale-appropriate way.
        // TODO: use icu4c when http://unicode.org/cldr/trac/ticket/3407 is fixed.
        Formatter f = new Formatter(sb, Locale.getDefault());
        initFormatStrings();
if (hours > 0) {
            return f.format(sElapsedFormatHMMSS, hours, minutes, seconds).toString();
} else {
            return f.format(sElapsedFormatMMSS, minutes, seconds).toString();
}
}








