//<Beginning of snippet n. 0>
private static volatile Configuration sLastConfig;
private static final Object sLock = new Object();
private static final ThreadLocal<java.text.DateFormat> sStatusTimeFormat = ThreadLocal.withInitial(() -> java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT));
private static String sElapsedFormatMMSS;
private static String sElapsedFormatHMMSS;

private static void initFormatStrings() {
    Configuration cfg = Resources.getSystem().getConfiguration();
    synchronized (sLock) {
        if (sLastConfig == null || !sLastConfig.equals(cfg)) {
            sLastConfig = cfg;
            Resources r = Resources.getSystem();
            sElapsedFormatMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_mm_ss);
            sElapsedFormatHMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_h_mm_ss);
        }
    }
}

/** @hide */
public static final CharSequence timeString(long millis) {
    synchronized (sLock) {
        initFormatStrings();
        java.text.DateFormat format = sStatusTimeFormat.get();
        if (format != null) {
            return format.format(millis);
        }
        return "";
    }
}
//<End of snippet n. 0>