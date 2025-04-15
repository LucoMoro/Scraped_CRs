/*New standalone AVD Manager window.

This is the the current "AVD Manager" page wrapped
as a window so that it can be used either in standalone
or from the new SDK Manager 2.

Change-Id:Id6272ca87481890809e483eee6d57e715d8ad517*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index ccac9b7..dd9f6cd 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.repository.UpdaterPage;
//Synthetic comment -- @@ -232,7 +233,7 @@
// We don't support a specific GUI for this.
// If the user forces a gui mode to see this list, simply launch the regular GUI.
if (!mSdkCommandLine.getFlagNoUI(verb)) {
                    showMainWindow(false /*autoUpdate*/);
} else {
displayRemoteSdkListNoUI();
}
//Synthetic comment -- @@ -279,13 +280,21 @@
if (mSdkCommandLine.getFlagNoUI(verb)) {
updateSdkNoUI();
} else {
                    showMainWindow(true /*autoUpdate*/);
}

} else if (SdkCommandLine.OBJECT_ADB.equals(directObject)) {
updateAdb();

}
} else if (SdkCommandLine.VERB_DELETE.equals(verb) &&
SdkCommandLine.OBJECT_AVD.equals(directObject)) {
deleteAvd();
//Synthetic comment -- @@ -295,7 +304,7 @@
moveAvd();

} else if (verb == null && directObject == null) {
            showMainWindow(false /*autoUpdate*/);

} else {
mSdkCommandLine.printHelpAndExit(null);
//Synthetic comment -- @@ -303,9 +312,9 @@
}

/**
     * Display the main SdkManager app window
*/
    private void showMainWindow(boolean autoUpdate) {
try {
MessageBoxLog errorLogger = new MessageBoxLog(
"SDK Manager",
//Synthetic comment -- @@ -332,6 +341,34 @@
}
}

private void displayRemoteSdkListNoUI() {
boolean force = mSdkCommandLine.getFlagForce();
boolean useHttp = mSdkCommandLine.getFlagNoHttps();








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index fb15cb5..7b73f65 100644

//Synthetic comment -- @@ -38,11 +38,12 @@
*   or optional) for the given action.
*/

    public final static String VERB_LIST   = "list";                                //$NON-NLS-1$
    public final static String VERB_CREATE = "create";                              //$NON-NLS-1$
    public final static String VERB_MOVE   = "move";                                //$NON-NLS-1$
    public final static String VERB_DELETE = "delete";                              //$NON-NLS-1$
    public final static String VERB_UPDATE = "update";                              //$NON-NLS-1$

public static final String OBJECT_SDK            = "sdk";                       //$NON-NLS-1$
public static final String OBJECT_AVD            = "avd";                       //$NON-NLS-1$
//Synthetic comment -- @@ -143,8 +144,14 @@
"Updates adb to support the USB devices declared in the SDK add-ons." },

{ VERB_UPDATE, OBJECT_SDK,
                "Updates the SDK by suggesting new platforms to install if available." }
        };

public SdkCommandLine(ISdkLog logger) {
super(logger, ACTIONS);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java
new file mode 100755
//Synthetic comment -- index 0000000..719de4c

//Synthetic comment -- @@ -0,0 +1,539 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index dda5b36..d70df85 100755

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -104,6 +105,26 @@
}

/**
* Opens the window.
* @wbp.parser.entryPoint
*/
//Synthetic comment -- @@ -262,6 +283,12 @@
if (mContext == InvocationContext.STANDALONE) {
MenuItem manageAvds = new MenuItem(menuTools, SWT.NONE);
manageAvds.setText("Manage AVDs...");
}

MenuItem manageSources = new MenuItem(menuTools,
//Synthetic comment -- @@ -505,6 +532,27 @@
}
}

// End of hiding from SWT Designer
//$hide<<$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index a8c1570..83803b7 100755

//Synthetic comment -- @@ -26,7 +26,7 @@
import org.eclipse.swt.widgets.Shell;

/**
 * Opens an SDK Updater Window.
*
* This is the public entry point for using the window.
*/
//Synthetic comment -- @@ -49,12 +49,21 @@
* For SdkMan2, we also have a menu bar and link to the AVD manager.
*/
STANDALONE,
/**
* The SDK Manager is invoked from an IDE.
* In this mode, we do not modify the menu bar. There is no about box
* and no settings (e.g. HTTP proxy settings are inherited from Eclipse.)
*/
IDE,
/**
* The SDK Manager is invoked from the AVD Selector.
* For SdkMan1, this means the AVD page will be displayed first.







