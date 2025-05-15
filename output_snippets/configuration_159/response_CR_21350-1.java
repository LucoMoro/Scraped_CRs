//<Beginning of snippet n. 0>

private static volatile java.text.DateFormat sStatusTimeFormat;
private static volatile Configuration sLastConfig;
private static final Object sLock = new Object();

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

/** @hide */
public static final CharSequence timeString(long millis) {
    initFormatStrings();
    return java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(millis);
}

//<End of snippet n. 0>