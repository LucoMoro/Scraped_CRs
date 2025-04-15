/*Cache the expensive 12/24 hour display mode lookup.*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 07a6854..f37a9e4 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//Synthetic comment -- @@ -95,6 +96,13 @@

public static final int READ_THREAD   = 1;

    // Cache the most previous lookup of whether we're in 24-hour
    // display mode, as that's an expensive operation based on
    // traceview results (as of 2008-12-27). These are both guarded
    // by a class lock.
    private static WeakReference<Context> s24HourLastContext;
    private static boolean sCached24HourMode;

private MessageUtils() {
// Forbidden being instantiated.
}
//Synthetic comment -- @@ -378,7 +386,7 @@
resId = R.string.time_stamp_full;
if (then.year == now.year) {
if ((then.month == now.month) && (then.monthDay == now.monthDay)) {
                    resId = is24HourMode ? R.string.time_stamp_same_day_24_format
: R.string.time_stamp_same_day_12_format;
} else {
resId = R.string.time_stamp_same_year;
//Synthetic comment -- @@ -391,8 +399,14 @@
/**
* @return true if clock is set to 24-hour mode
*/
    static synchronized boolean get24HourMode(final Context context) {
        if (s24HourLastContext != null &&
            s24HourLastContext.get() == context) {
            return sCached24HourMode;
        }
        s24HourLastContext = new WeakReference<Context>(context);
        sCached24HourMode = android.text.format.DateFormat.is24HourFormat(context);
        return sCached24HourMode;
}

/**







