/*logcat: Fix scroll lock icon

Change-Id:I3825ba3149c644e834744f347a3973e9c8cd07ce*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 233c7c1..e2fb22f 100644

//Synthetic comment -- @@ -135,7 +135,7 @@
private static final String IMAGE_SAVE_LOG_TO_FILE = "save.png"; //$NON-NLS-1$
private static final String IMAGE_CLEAR_LOG = "clear.png"; //$NON-NLS-1$
private static final String IMAGE_DISPLAY_FILTERS = "displayfilters.png"; //$NON-NLS-1$
    private static final String IMAGE_SCROLL_LOCK = "pause_logcat.png"; //$NON-NLS-1$

private static final int[] WEIGHTS_SHOW_FILTERS = new int[] {15, 85};
private static final int[] WEIGHTS_LOGCAT_ONLY = new int[] {0, 100};







