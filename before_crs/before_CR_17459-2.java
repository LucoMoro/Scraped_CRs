/*Allow the user to set the ADBHOST variable.

With those changes the ADBHOST variable can be
directly set in the Preferences of DDMS and Eclipse,
so that the user can reset adb from inside those
two programs.

Change-Id:I94b24744e8daa3b3b2fe5a3db39203b39cb093ac*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/PrefsDialog.java b/ddms/app/src/com/android/ddms/PrefsDialog.java
//Synthetic comment -- index fbdbfe6..418b8ba 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -85,6 +86,8 @@
private final static String PREFS_THREAD_REFRESH_INTERVAL = "threadStatusInterval"; //$NON-NLS-1$
private final static String PREFS_LOG_LEVEL = "ddmsLogLevel"; //$NON-NLS-1$
private final static String PREFS_TIMEOUT = "timeOut"; //$NON-NLS-1$


/**
//Synthetic comment -- @@ -151,6 +154,8 @@
DdmPreferences.setInitialThreadUpdate(mPrefStore.getBoolean(PREFS_DEFAULT_THREAD_UPDATE));
DdmPreferences.setInitialHeapUpdate(mPrefStore.getBoolean(PREFS_DEFAULT_HEAP_UPDATE));
DdmPreferences.setTimeOut(mPrefStore.getInt(PREFS_TIMEOUT));

// some static values
String out = System.getenv("ANDROID_PRODUCT_OUT"); //$NON-NLS-1$
//Synthetic comment -- @@ -185,6 +190,9 @@
mPrefStore.setDefault(PREFS_SELECTED_DEBUG_PORT,
DdmPreferences.DEFAULT_SELECTED_DEBUG_PORT);

mPrefStore.setDefault(PREFS_DEFAULT_THREAD_UPDATE, true);
mPrefStore.setDefault(PREFS_DEFAULT_HEAP_UPDATE, false);
mPrefStore.setDefault(PREFS_THREAD_REFRESH_INTERVAL,
//Synthetic comment -- @@ -239,6 +247,10 @@
(String) event.getNewValue());
} else if (changed.equals(PREFS_TIMEOUT)) {
DdmPreferences.setTimeOut(mPrefStore.getInt(PREFS_TIMEOUT));
} else {
Log.v("ddms", "Preference change: " + event.getProperty()
+ ": '" + event.getOldValue()
//Synthetic comment -- @@ -301,6 +313,9 @@
*/
private static class DebuggerPrefs extends FieldEditorPreferencePage {

/**
* Basic constructor.
*/
//Synthetic comment -- @@ -323,6 +338,24 @@
ife = new PortFieldEditor(PREFS_SELECTED_DEBUG_PORT,
"Port of Selected VM:", getFieldEditorParent());
addField(ife);
}
}









//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index d9ea3f1..6b305fa 100644

//Synthetic comment -- @@ -769,12 +769,25 @@
}
});

// configure Action items based on current state
actionMenu.addMenuListener(new MenuAdapter() {
@Override
public void menuShown(MenuEvent e) {
actionHaltItem.setEnabled(mTBHalt.getEnabled() && mCurrentClient != null);
actionCauseGcItem.setEnabled(mTBCauseGc.getEnabled() && mCurrentClient != null);
}
});









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 01cdf0a..f736ab3 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -898,7 +899,16 @@
Log.d(DDMS,
String.format("Launching '%1$s %2$s' to ensure ADB is running.", //$NON-NLS-1$
mAdbOsLocation, command[1]));
            proc = Runtime.getRuntime().exec(command);

ArrayList<String> errorOutput = new ArrayList<String>();
ArrayList<String> stdOutput = new ArrayList<String>();








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DdmPreferences.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DdmPreferences.java
//Synthetic comment -- index 8044ab1..51561c3 100644

//Synthetic comment -- @@ -42,6 +42,9 @@
public final static LogLevel DEFAULT_LOG_LEVEL = LogLevel.ERROR;
/** Default timeout values for adb connection (milliseconds) */
public static final int DEFAULT_TIMEOUT = 5000; // standard delay, in ms

private static boolean sThreadUpdate = DEFAULT_INITIAL_THREAD_UPDATE;
private static boolean sInitialHeapUpdate = DEFAULT_INITIAL_HEAP_UPDATE;
//Synthetic comment -- @@ -51,6 +54,9 @@
private static LogLevel sLogLevel = DEFAULT_LOG_LEVEL;
private static int sTimeOut = DEFAULT_TIMEOUT;

/**
* Returns the initial {@link Client} flag for thread updates.
* @see #setInitialThreadUpdate(boolean)
//Synthetic comment -- @@ -158,6 +164,36 @@
}

/**
* Non accessible constructor.
*/
private DdmPreferences() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java
//Synthetic comment -- index 7d2c36b..10ba76b 100644

//Synthetic comment -- @@ -213,6 +213,12 @@
} else if (PreferenceInitializer.ATTR_TIME_OUT.equals(property)) {
DdmPreferences.setTimeOut(
eclipseStore.getInt(PreferenceInitializer.ATTR_TIME_OUT));
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java
//Synthetic comment -- index 751bc4f..52e8dbc 100644

//Synthetic comment -- @@ -64,6 +64,12 @@
public final static String ATTR_TIME_OUT =
DdmsPlugin.PLUGIN_ID + ".timeOut"; //$NON-NLS-1$

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -95,6 +101,9 @@
store.setDefault(ATTR_HPROF_ACTION, HProfHandler.ACTION_OPEN);

store.setDefault(ATTR_TIME_OUT, DdmPreferences.DEFAULT_TIMEOUT);
}

/**
//Synthetic comment -- @@ -110,5 +119,7 @@
DdmPreferences.setInitialHeapUpdate(store.getBoolean(ATTR_DEFAULT_HEAP_UPDATE));
DdmUiPreferences.setThreadRefreshInterval(store.getInt(ATTR_THREAD_INTERVAL));
DdmPreferences.setTimeOut(store.getInt(ATTR_TIME_OUT));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java
//Synthetic comment -- index fb852f5..47445f8 100644

//Synthetic comment -- @@ -26,12 +26,17 @@
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

public PreferencePage() {
super(GRID);
setPreferenceStore(DdmsPlugin.getDefault().getPreferenceStore());
//Synthetic comment -- @@ -87,8 +92,26 @@
},
getFieldEditorParent(), true);
addField(rgfe);
}

public void init(IWorkbench workbench) {
}
}







