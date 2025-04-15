/*New AVD Manager window

(this is not ready for review... actually
there isn't much to see here since I got
sidetracked by something else. TBL.)

Change-Id:Id6272ca87481890809e483eee6d57e715d8ad517*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index d3e3235..50c673c 100644

//Synthetic comment -- @@ -232,7 +232,7 @@
// We don't support a specific GUI for this.
// If the user forces a gui mode to see this list, simply launch the regular GUI.
if (!mSdkCommandLine.getFlagNoUI(verb)) {
                    showMainWindow(false /*autoUpdate*/);
} else {
displayRemoteSdkListNoUI();
}
//Synthetic comment -- @@ -279,13 +279,21 @@
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
//Synthetic comment -- @@ -295,7 +303,7 @@
moveAvd();

} else if (verb == null && directObject == null) {
            showMainWindow(false /*autoUpdate*/);

} else {
mSdkCommandLine.printHelpAndExit(null);
//Synthetic comment -- @@ -303,9 +311,9 @@
}

/**
     * Display the main SdkManager app window
*/
    private void showMainWindow(boolean autoUpdate) {
try {
MessageBoxLog errorLogger = new MessageBoxLog(
"SDK Manager",
//Synthetic comment -- @@ -331,6 +339,33 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 0fd0db2..ae378bb 100755

//Synthetic comment -- @@ -25,7 +25,7 @@
import org.eclipse.swt.widgets.Shell;

/**
 * Opens an SDK Updater Window.
*
* This is the public interface for using the window.
*/







