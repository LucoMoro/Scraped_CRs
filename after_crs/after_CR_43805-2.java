/*ddms-plugin: Add preference for method profiler buffer size

Fixeshttp://code.google.com/p/android/issues/detail?id=38000Change-Id:I3465d1df1c85853ea1cb9fb01c92998842b884c5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java
//Synthetic comment -- index 2415e20..254b2c5 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.ddms.preferences;

import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.DdmUiPreferences;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.ide.eclipse.ddms.LogCatMonitor;
import com.android.ide.eclipse.ddms.views.DeviceView.HProfHandler;
import com.android.ide.eclipse.ddms.views.LogCatView;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
//Synthetic comment -- @@ -81,6 +81,9 @@
public final static String ATTR_PERSPECTIVE_ID =
DdmsPlugin.PLUGIN_ID + ".perspectiveId"; //$NON-NLS-1$

    public static final String ATTR_PROFILER_BUFSIZE_MB =
        DdmsPlugin.PLUGIN_ID + ".profilerBufferSizeMb"; //$NON-NLS-1$

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -99,6 +102,8 @@
store.setDefault(ATTR_DEFAULT_HEAP_UPDATE,
DdmPreferences.DEFAULT_INITIAL_HEAP_UPDATE);

        store.setDefault(ATTR_PROFILER_BUFSIZE_MB, DdmPreferences.DEFAULT_PROFILER_BUFFER_SIZE_MB);

store.setDefault(ATTR_THREAD_INTERVAL, DdmUiPreferences.DEFAULT_THREAD_REFRESH_INTERVAL);

String homeDir = System.getProperty("user.home"); //$NON-NLS-1$
//Synthetic comment -- @@ -141,6 +146,7 @@
DdmPreferences.setLogLevel(store.getString(ATTR_LOG_LEVEL));
DdmPreferences.setInitialThreadUpdate(store.getBoolean(ATTR_DEFAULT_THREAD_UPDATE));
DdmPreferences.setInitialHeapUpdate(store.getBoolean(ATTR_DEFAULT_HEAP_UPDATE));
        DdmPreferences.setProfilerBufferSizeMb(store.getInt(ATTR_PROFILER_BUFSIZE_MB));
DdmUiPreferences.setThreadRefreshInterval(store.getInt(ATTR_THREAD_INTERVAL));
DdmPreferences.setTimeOut(store.getInt(ATTR_TIME_OUT));
DdmPreferences.setUseAdbHost(store.getBoolean(ATTR_USE_ADBHOST));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java
//Synthetic comment -- index c3c705c..e469dfd 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.ddms.preferences;

import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.PortFieldEditor;
import com.android.ide.eclipse.base.InstallDetails;
//Synthetic comment -- @@ -38,6 +39,7 @@

private BooleanFieldEditor mUseAdbHost;
private StringFieldEditor mAdbHostValue;
    private IntegerFieldEditor mProfilerBufsize;

public PreferencePage() {
super(GRID);
//Synthetic comment -- @@ -85,6 +87,11 @@
addField(cfe);
}

        mProfilerBufsize = new IntegerFieldEditor(PreferenceInitializer.ATTR_PROFILER_BUFSIZE_MB,
                "Method Profiler buffer size (MB):",
                getFieldEditorParent());
        addField(mProfilerBufsize);

ife = new IntegerFieldEditor(PreferenceInitializer.ATTR_TIME_OUT,
Messages.PreferencePage_ADB_Connection_Time_Out, getFieldEditorParent());
addField(ife);
//Synthetic comment -- @@ -129,9 +136,10 @@

@Override
public void propertyChange(PropertyChangeEvent event) {
if (event.getSource().equals(mUseAdbHost)) {
mAdbHostValue.setEnabled(mUseAdbHost.getBooleanValue(), getFieldEditorParent());
        } else if (event.getSource().equals(mProfilerBufsize)) {
            DdmPreferences.setProfilerBufferSizeMb(mProfilerBufsize.getIntValue());
}
}








