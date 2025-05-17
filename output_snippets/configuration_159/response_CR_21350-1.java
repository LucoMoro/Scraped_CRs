//<Beginning of snippet n. 0>
private static final Object sLock = new Object();
private static Configuration sLastConfig = null;
private static DateFormat sStatusTimeFormat;
private static String sElapsedFormatMMSS;
private static String sElapsedFormatHMMSS;

private static void initFormatStrings() {
    synchronized (sLock) {
        if (sLastConfig == null || !sLastConfig.equals(Resources.getSystem().getConfiguration())) {
            Resources r = Resources.getSystem();
            sLastConfig = r.getConfiguration();
            sStatusTimeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT);
            sElapsedFormatMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_mm_ss);
            sElapsedFormatHMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_h_mm_ss);
        }
    }
}

/**
 * @hide
 */
public static final CharSequence timeString(long millis) {
    synchronized (sLock) {
        initFormatStrings();
        return sStatusTimeFormat.format(millis);
    }
}
//<End of snippet n. 0>