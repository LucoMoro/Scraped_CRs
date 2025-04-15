/*bug fix: InstanceScope.INSTANCE is not available in Galileo

Change-Id:If96389ad844e19eeed86008edc40bc81778dd0d7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index e18427e..764ba98 100644

//Synthetic comment -- @@ -353,7 +353,7 @@
}

private void savePreferences() {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(GlTracePlugin.PLUGIN_ID);
prefs.put(PREF_APP_PACKAGE, mAppPackageToTrace);
prefs.put(PREF_ACTIVITY, mActivityToTrace);
prefs.put(PREF_TRACEFILE, mTraceFilePath);
//Synthetic comment -- @@ -366,7 +366,7 @@
}

private void loadPreferences() {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(GlTracePlugin.PLUGIN_ID);
mAppPackageToTrace = prefs.get(PREF_APP_PACKAGE, "");
mActivityToTrace = prefs.get(PREF_ACTIVITY, "");
mTraceFilePath = prefs.get(PREF_TRACEFILE, "");







