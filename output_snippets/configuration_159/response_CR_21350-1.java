//<Beginning of snippet n. 0>
private static volatile Configuration sLastConfig;
private static DateFormat sStatusTimeFormat;
private static final Object sLock = new Object();

private static void initSynchronizedFormatStrings() {
    if (sLastConfig == null || !sLastConfig.equals(Resources.getSystem().getConfiguration())) {
        synchronized (sLock) {
            Resources r = Resources.getSystem();
            Configuration cfg = r.getConfiguration();
            // Recheck after acquiring lock
            if (sLastConfig == null || !sLastConfig.equals(cfg)) {
                sLastConfig = cfg;
                sStatusTimeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT);
                sElapsedFormatMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_mm_ss);
                sElapsedFormatHMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_h_mm_ss);
            }
        }
    }
}

/** @hide */
public static final CharSequence timeString(long millis) {
    initSynchronizedFormatStrings();
    synchronized (sLock) {
        return sStatusTimeFormat.format(millis);
    }
}
//<End of snippet n. 0>