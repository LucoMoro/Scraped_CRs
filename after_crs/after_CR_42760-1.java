/*gltrace: Allow fully qualified activity names

Add a separate checkbox to allow the user to indicate whether
the input activity name is fully qualified. In such a case, don't
prefix the activity name with a period.

Also fixes a formatting issue with the trace file size.

Change-Id:Ib4bf2b716e218e86a273200748fbd06c86918ce3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 9acff50..5e97305 100644

//Synthetic comment -- @@ -126,7 +126,8 @@

try {
if (!SYSTEM_APP.equals(traceOptions.appToTrace)) {
                startActivity(device, traceOptions.appToTrace, traceOptions.activityToTrace,
                        traceOptions.isActivityNameFullyQualified);
}
} catch (Exception e) {
MessageDialog.openError(shell, "Setup GL Trace",
//Synthetic comment -- @@ -272,19 +273,24 @@
}
}

    private void startActivity(IDevice device, String appPackage, String activity,
            boolean isActivityNameFullyQualified)
throws TimeoutException, AdbCommandRejectedException,
ShellCommandUnresponsiveException, IOException, InterruptedException {
killApp(device, appPackage); // kill app if it is already running
waitUntilAppKilled(device, appPackage, KILL_TIMEOUT);

        StringBuilder activityPath = new StringBuilder(appPackage);
if (!activity.isEmpty()) {
            activityPath.append('/');
            if (!isActivityNameFullyQualified) {
                activityPath.append('.');
            }
            activityPath.append(activity);
}
String startAppCmd = String.format(
"am start --opengl-trace %s -a android.intent.action.MAIN -c android.intent.category.LAUNCHER", //$NON-NLS-1$
                activityPath.toString());

Semaphore launchCompletionSempahore = new Semaphore(0);
StartActivityOutputReceiver receiver = new StartActivityOutputReceiver(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceCollectorDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceCollectorDialog.java
//Synthetic comment -- index 4dfcafa..d657ac1 100644

//Synthetic comment -- @@ -35,11 +35,13 @@
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.text.DecimalFormat;

/** Dialog displayed while the trace is being streamed from device to host. */
public class GLTraceCollectorDialog extends TitleAreaDialog {
private static final String TITLE = "OpenGL ES Trace";
private static final String DEFAULT_MESSAGE = "Trace collection in progress.";
    private static final DecimalFormat SIZE_FORMATTER = new DecimalFormat("#.##"); //$NON-NLS-1$

private TraceOptions mTraceOptions;
private final TraceFileWriter mTraceFileWriter;
//Synthetic comment -- @@ -194,7 +196,7 @@

double fileSize = mTraceFileWriter.getCurrentFileSize();
fileSize /= (1024 * 1024); // convert to size in MB
                final String frameSize = SIZE_FORMATTER.format(fileSize);

Display.getDefault().syncExec(new Runnable() {
@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index 1ddb6d8..17c8953 100644

//Synthetic comment -- @@ -64,6 +64,7 @@
private Combo mDeviceCombo;
private Text mAppPackageToTraceText;
private Text mActivityToTraceText;
    private Button mIsActivityFullyQualifiedButton;
private Text mTraceFilePathText;

private String mSelectedDevice = "";
//Synthetic comment -- @@ -74,6 +75,7 @@
private static boolean sCollectFbOnEglSwap = true;
private static boolean sCollectFbOnGlDraw = false;
private static boolean sCollectTextureData = false;
    private static boolean sIsActivityFullyQualified = false;
private IDevice[] mDevices;

public GLTraceOptionsDialog(Shell parentShell) {
//Synthetic comment -- @@ -95,12 +97,19 @@
mDevices = AndroidDebugBridge.getBridge().getDevices();
createDeviceDropdown(c, mDevices);

        createSeparator(c);

createLabel(c, "Application Package:");
createAppToTraceText(c, "e.g. com.example.package");

createLabel(c, "Activity to launch:");
createActivityToTraceText(c, "Leave blank to launch default activity");

        createLabel(c, "");
        createIsFullyQualifedActivityButton(c, "Activity name is fully qualified, do not prefix with package name");

        createSeparator(c);

createLabel(c, "Data Collection Options:");
createCaptureImageOptions(c);

//Synthetic comment -- @@ -245,6 +254,15 @@
return mActivityToTraceText;
}

    private Button createIsFullyQualifedActivityButton(Composite parent, String message) {
        mIsActivityFullyQualifiedButton = new Button(parent, SWT.CHECK);
        mIsActivityFullyQualifiedButton.setText(message);
        mIsActivityFullyQualifiedButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mIsActivityFullyQualifiedButton.setSelection(sIsActivityFullyQualified);

        return mIsActivityFullyQualifiedButton;
    }

private void validateAndSetMessage() {
DialogStatus status = validateDialog();
mOkButton.setEnabled(status.valid);
//Synthetic comment -- @@ -324,6 +342,7 @@
if (mActivityToTrace.startsWith(".")) { //$NON-NLS-1$
mActivityToTrace = mActivityToTrace.substring(1);
}
        sIsActivityFullyQualified = mIsActivityFullyQualifiedButton.getSelection();
mTraceFilePath = mTraceFilePathText.getText().trim();
mSelectedDevice = mDeviceCombo.getText();

//Synthetic comment -- @@ -333,7 +352,7 @@
}

private void savePreferences() {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(GlTracePlugin.PLUGIN_ID);
prefs.put(PREF_APP_PACKAGE, mAppPackageToTrace);
prefs.put(PREF_ACTIVITY, mActivityToTrace);
prefs.put(PREF_TRACEFILE, mTraceFilePath);
//Synthetic comment -- @@ -346,7 +365,7 @@
}

private void loadPreferences() {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(GlTracePlugin.PLUGIN_ID);
mAppPackageToTrace = prefs.get(PREF_APP_PACKAGE, "");
mActivityToTrace = prefs.get(PREF_ACTIVITY, "");
mTraceFilePath = prefs.get(PREF_TRACEFILE, "");
//Synthetic comment -- @@ -355,6 +374,7 @@

public TraceOptions getTraceOptions() {
return new TraceOptions(mSelectedDevice, mAppPackageToTrace, mActivityToTrace,
                sIsActivityFullyQualified, mTraceFilePath, sCollectFbOnEglSwap,
                sCollectFbOnGlDraw, sCollectTextureData);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java
//Synthetic comment -- index e7ad17e..ac9fb6b 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
/** Activity to trace. */
public final String activityToTrace;

    public final boolean isActivityNameFullyQualified;

/** Path where the trace file should be saved. */
public final String traceDestination;

//Synthetic comment -- @@ -38,11 +40,13 @@
/** Flag indicating whether texture data should be captured on glTexImage*() */
public final boolean collectTextureData;

    public TraceOptions(String device, String appPackage, String activity,
            boolean isActivityNameFullyQualified, String destinationPath,
boolean collectFbOnEglSwap, boolean collectFbOnGlDraw, boolean collectTextureData) {
this.device = device;
this.appToTrace = appPackage;
this.activityToTrace = activity;
        this.isActivityNameFullyQualified = isActivityNameFullyQualified;
this.traceDestination = destinationPath;
this.collectFbOnEglSwap = collectFbOnEglSwap;
this.collectFbOnGlDraw = collectFbOnGlDraw;







