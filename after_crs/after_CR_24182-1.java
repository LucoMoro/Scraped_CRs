/*Add a preference for the profiler buffer size.

8MB is getting to be too small for some purposes.

Change-Id:Ic711c426febf2e42634bd73a5be6211cfe66f4c0*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/PrefsDialog.java b/ddms/app/src/com/android/ddms/PrefsDialog.java
//Synthetic comment -- index c957a89..da31a3b 100644

//Synthetic comment -- @@ -86,6 +86,7 @@
private final static String PREFS_THREAD_REFRESH_INTERVAL = "threadStatusInterval"; //$NON-NLS-1$
private final static String PREFS_LOG_LEVEL = "ddmsLogLevel"; //$NON-NLS-1$
private final static String PREFS_TIMEOUT = "timeOut"; //$NON-NLS-1$
    private final static String PREFS_PROFILER_BUFFER_SIZE_MB = "profilerBufferSizeMb"; //$NON-NLS-1$
private final static String PREFS_USE_ADBHOST = "useAdbHost"; //$NON-NLS-1$
private final static String PREFS_ADBHOST_VALUE = "adbHostValue"; //$NON-NLS-1$

//Synthetic comment -- @@ -154,6 +155,7 @@
DdmPreferences.setInitialThreadUpdate(mPrefStore.getBoolean(PREFS_DEFAULT_THREAD_UPDATE));
DdmPreferences.setInitialHeapUpdate(mPrefStore.getBoolean(PREFS_DEFAULT_HEAP_UPDATE));
DdmPreferences.setTimeOut(mPrefStore.getInt(PREFS_TIMEOUT));
        DdmPreferences.setProfilerBufferSizeMb(mPrefStore.getInt(PREFS_PROFILER_BUFFER_SIZE_MB));
DdmPreferences.setUseAdbHost(mPrefStore.getBoolean(PREFS_USE_ADBHOST));
DdmPreferences.setAdbHostValue(mPrefStore.getString(PREFS_ADBHOST_VALUE));

//Synthetic comment -- @@ -204,6 +206,8 @@
mPrefStore.setDefault(PREFS_LOG_LEVEL, "info"); //$NON-NLS-1$

mPrefStore.setDefault(PREFS_TIMEOUT, DdmPreferences.DEFAULT_TIMEOUT);
        mPrefStore.setDefault(PREFS_PROFILER_BUFFER_SIZE_MB,
                DdmPreferences.DEFAULT_PROFILER_BUFFER_SIZE_MB);

// choose a default font for the text output
FontData fdat = new FontData("Courier", 10, SWT.NORMAL); //$NON-NLS-1$
//Synthetic comment -- @@ -247,6 +251,9 @@
(String) event.getNewValue());
} else if (changed.equals(PREFS_TIMEOUT)) {
DdmPreferences.setTimeOut(mPrefStore.getInt(PREFS_TIMEOUT));
            } else if (changed.equals(PREFS_PROFILER_BUFFER_SIZE_MB)) {
                DdmPreferences.setProfilerBufferSizeMb(
                        mPrefStore.getInt(PREFS_PROFILER_BUFFER_SIZE_MB));
} else if (changed.equals(PREFS_USE_ADBHOST)) {
DdmPreferences.setUseAdbHost(mPrefStore.getBoolean(PREFS_USE_ADBHOST));
} else if (changed.equals(PREFS_ADBHOST_VALUE)) {
//Synthetic comment -- @@ -458,6 +465,10 @@
"ADB connection time out (ms):", getFieldEditorParent());
addField(ife);

            ife = new IntegerFieldEditor(PREFS_PROFILER_BUFFER_SIZE_MB,
                    "Profiler buffer size (MB):", getFieldEditorParent());
            addField(ife);

dfe = new DirectoryFieldEditor("textSaveDir",
"Default text save dir:", getFieldEditorParent());
addField(dfe);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Client.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Client.java
//Synthetic comment -- index 582ac09..5b03462 100644

//Synthetic comment -- @@ -255,13 +255,14 @@
HandleProfiling.sendMPRE(this);
}
} else {
                int bufferSize = DdmPreferences.getProfilerBufferSizeMb() * 1024 * 1024;
if (canStream) {
                    HandleProfiling.sendMPSS(this, bufferSize, 0 /*flags*/);
} else {
String file = "/sdcard/" +
mClientData.getClientDescription().replaceAll("\\:.*", "") +
DdmConstants.DOT_TRACE;
                    HandleProfiling.sendMPRS(this, file, bufferSize, 0 /*flags*/);
}
}
} catch (IOException e) {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DdmPreferences.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DdmPreferences.java
//Synthetic comment -- index 51561c3..d286917 100644

//Synthetic comment -- @@ -42,6 +42,8 @@
public final static LogLevel DEFAULT_LOG_LEVEL = LogLevel.ERROR;
/** Default timeout values for adb connection (milliseconds) */
public static final int DEFAULT_TIMEOUT = 5000; // standard delay, in ms
    /** Default profiler buffer size (megabytes) */
    public static final int DEFAULT_PROFILER_BUFFER_SIZE_MB = 8;
/** Default values for the use of the ADBHOST environment variable. */
public final static boolean DEFAULT_USE_ADBHOST = false;
public final static String DEFAULT_ADBHOST_VALUE = "127.0.0.1";
//Synthetic comment -- @@ -53,6 +55,7 @@
private static int sDebugPortBase = DEFAULT_DEBUG_PORT_BASE;
private static LogLevel sLogLevel = DEFAULT_LOG_LEVEL;
private static int sTimeOut = DEFAULT_TIMEOUT;
    private static int sProfilerBufferSizeMb = DEFAULT_PROFILER_BUFFER_SIZE_MB;

private static boolean sUseAdbHost = DEFAULT_USE_ADBHOST;
private static String sAdbHostValue = DEFAULT_ADBHOST_VALUE;
//Synthetic comment -- @@ -164,6 +167,21 @@
}

/**
     * Returns the profiler buffer size (megabytes).
     */
    public static int getProfilerBufferSizeMb() {
        return sProfilerBufferSizeMb;
    }

    /**
     * Sets the profiler buffer size value.
     * @param bufferSizeMb the buffer size (megabytes).
     */
    public static void setProfilerBufferSizeMb(int bufferSizeMb) {
        sProfilerBufferSizeMb = bufferSizeMb;
    }

    /**
* Returns a boolean indicating that the user uses or not the variable ADBHOST.
*/
public static boolean getUseAdbHost() {







