/*Cache the expensive 12/24 hour display mode lookup.*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 07a6854..f37a9e4 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//Synthetic comment -- @@ -95,6 +96,13 @@

public static final int READ_THREAD   = 1;

private MessageUtils() {
// Forbidden being instantiated.
}
//Synthetic comment -- @@ -378,7 +386,7 @@
resId = R.string.time_stamp_full;
if (then.year == now.year) {
if ((then.month == now.month) && (then.monthDay == now.monthDay)) {
                    resId = get24HourMode(context) ? R.string.time_stamp_same_day_24_format
: R.string.time_stamp_same_day_12_format;
} else {
resId = R.string.time_stamp_same_year;
//Synthetic comment -- @@ -391,8 +399,14 @@
/**
* @return true if clock is set to 24-hour mode
*/
    static boolean get24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
}

/**







