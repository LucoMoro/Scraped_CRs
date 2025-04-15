/*gltrace: cleanup collect trace flow

This CL improves the initial flow to obtain the trace options
from the user and launch the application in trace mode.
 - A separate text box is provided if a non-default activity
   should be launched for tracing.
 - If that application to trace is already running, it needs
   to be killed before starting it in trace mode. The tracer
   will now wait (upto a timeout value) until it detects that
   the application was killed before launching it in trace mode.
 - Similarly, the tracer waits until it knows that the app
   has launched before attempting to connect to it.

Change-Id:Iea5be6d76b6e78ea68a05b893aef993099363555*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 1085f3f..42b4f54 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -40,7 +41,9 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class CollectTraceAction implements IWorkbenchWindowActionDelegate {
/** Abstract Unix Domain Socket Name used by the gltrace device code. */
//Synthetic comment -- @@ -52,6 +55,12 @@
/** Activity name to use for a system activity that has already been launched. */
private static final String SYSTEM_APP = "system";          //$NON-NLS-1$

@Override
public void run(IAction action) {
connectToDevice();
//Synthetic comment -- @@ -88,7 +97,7 @@

try {
if (!SYSTEM_APP.equals(traceOptions.activityToTrace)) {
                startActivity(device, traceOptions.activityToTrace);
}
} catch (Exception e) {
MessageDialog.openError(shell, "Setup GL Trace",
//Synthetic comment -- @@ -96,13 +105,6 @@
return;
}

        try {
            // wait a couple of seconds for the application to launch on the device
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // can't be interrupted
        }

// if everything went well, the app should now be waiting for the gl debugger
// to connect
startTracing(shell, traceOptions, LOCAL_FORWARDED_PORT);
//Synthetic comment -- @@ -171,21 +173,26 @@
}
}

    private void startActivity(IDevice device, String appName)
throws TimeoutException, AdbCommandRejectedException,
ShellCommandUnresponsiveException, IOException, InterruptedException {
        killApp(device, appName); // kill app if it is already running

String startAppCmd = String.format(
"am start --opengl-trace %s -a android.intent.action.MAIN -c android.intent.category.LAUNCHER", //$NON-NLS-1$
                appName);

Semaphore launchCompletionSempahore = new Semaphore(0);
StartActivityOutputReceiver receiver = new StartActivityOutputReceiver(
launchCompletionSempahore);
device.executeShellCommand(startAppCmd, receiver);

        // wait until shell finishes launch
launchCompletionSempahore.acquire();

// throw exception if there was an error during launch
//Synthetic comment -- @@ -193,6 +200,9 @@
if (output.contains("Error")) {             //$NON-NLS-1$
throw new RuntimeException(output);
}
}

private void killApp(IDevice device, String appName) {
//Synthetic comment -- @@ -202,6 +212,51 @@
}
}

private void setupForwarding(IDevice device, int i)
throws TimeoutException, AdbCommandRejectedException, IOException {
device.createForward(i, GLTRACE_UDS, DeviceUnixSocketNamespace.ABSTRACT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index ef94452..43db5d8 100644

//Synthetic comment -- @@ -52,9 +52,10 @@
private static final String TITLE = "OpenGL ES Trace Options";
private static final String DEFAULT_MESSAGE = "Provide the application and activity to be traced.";

    private static final String PREF_APPNAME = "gl.trace.appname";      //$NON-NLS-1$
    private static final String PREF_TRACEFILE = "gl.trace.destfile";   //$NON-NLS-1$
    private static final String PREF_DEVICE = "gl.trace.device";        //$NON-NLS-1$
private String mLastUsedDevice;

private static String sSaveToFolder = System.getProperty("user.home"); //$NON-NLS-1$
//Synthetic comment -- @@ -62,10 +63,12 @@
private Button mOkButton;

private Combo mDeviceCombo;
private Text mActivityToTraceText;
private Text mTraceFilePathText;

private String mSelectedDevice = "";
private String mActivityToTrace = "";
private String mTraceFilePath = "";

//Synthetic comment -- @@ -93,10 +96,13 @@
mDevices = AndroidDebugBridge.getBridge().getDevices();
createDeviceDropdown(c, mDevices);

        createLabel(c, "Activity:");
        createAppToTraceText(c, "e.g. com.example.package/.ActivityName");

        createLabel(c, "Capture Image:");
createCaptureImageOptions(c);

createSeparator(c);
//Synthetic comment -- @@ -207,6 +213,23 @@
}

private Text createAppToTraceText(Composite parent, String defaultMessage) {
mActivityToTraceText = new Text(parent, SWT.BORDER);
mActivityToTraceText.setMessage(defaultMessage);
mActivityToTraceText.setText(mActivityToTrace);
//Synthetic comment -- @@ -277,7 +300,7 @@
return new DialogStatus(false, "No connected devices.");
}

        if (mActivityToTraceText.getText().trim().length() == 0) {
return new DialogStatus(false, "Provide an application name");
}

//Synthetic comment -- @@ -297,8 +320,12 @@

@Override
protected void okPressed() {
        mActivityToTrace = mActivityToTraceText.getText();
        mTraceFilePath = mTraceFilePathText.getText();
mSelectedDevice = mDeviceCombo.getText();

savePreferences();
//Synthetic comment -- @@ -308,7 +335,8 @@

private void savePreferences() {
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
        prefs.put(PREF_APPNAME, mActivityToTrace);
prefs.put(PREF_TRACEFILE, mTraceFilePath);
prefs.put(PREF_DEVICE, mSelectedDevice);
try {
//Synthetic comment -- @@ -320,13 +348,14 @@

private void loadPreferences() {
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
        mActivityToTrace = prefs.get(PREF_APPNAME, "");
mTraceFilePath = prefs.get(PREF_TRACEFILE, "");
mLastUsedDevice = prefs.get(PREF_DEVICE, "");
}

public TraceOptions getTraceOptions() {
        return new TraceOptions(mSelectedDevice, mActivityToTrace.trim(), mTraceFilePath,
                mCollectFbOnEglSwap, mCollectFbOnGlDraw, mCollectTextureData);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java
//Synthetic comment -- index d67d167..e7ad17e 100644

//Synthetic comment -- @@ -20,6 +20,9 @@
/** Device on which the application should be run. */
public final String device;

/** Activity to trace. */
public final String activityToTrace;

//Synthetic comment -- @@ -35,9 +38,10 @@
/** Flag indicating whether texture data should be captured on glTexImage*() */
public final boolean collectTextureData;

    public TraceOptions(String device, String activity, String destinationPath,
boolean collectFbOnEglSwap, boolean collectFbOnGlDraw, boolean collectTextureData) {
this.device = device;
this.activityToTrace = activity;
this.traceDestination = destinationPath;
this.collectFbOnEglSwap = collectFbOnEglSwap;







