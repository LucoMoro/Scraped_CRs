/*Ensuring thread-safe usage of DateFormat.

DateFormat is inherently unsafe for multithreaded use. This patch adds
proper synchronization. The 'sLock' is re-used. To avoid two consecutive
lockings, locked version of initFormatStrings() method is created and
used where appropriate, i.e. in original method AND in added synchronized
block.

Change-Id:Id3c34613623e743e703aadb2342aa788679dda8a*/
//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index a95dad7..353b628 100644

//Synthetic comment -- @@ -642,14 +642,18 @@

private static void initFormatStrings() {
synchronized (sLock) {
            Resources r = Resources.getSystem();
            Configuration cfg = r.getConfiguration();
            if (sLastConfig == null || !sLastConfig.equals(cfg)) {
                sLastConfig = cfg;
                sStatusTimeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT);
                sElapsedFormatMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_mm_ss);
                sElapsedFormatHMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_h_mm_ss);
            }
}
}

//Synthetic comment -- @@ -659,8 +663,10 @@
* @hide
*/
public static final CharSequence timeString(long millis) {
        initFormatStrings();
        return sStatusTimeFormat.format(millis);
}

/**







