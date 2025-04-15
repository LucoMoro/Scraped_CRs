/*Minor UI cleanup to trace options dialog

- Fix default message shown for activity
- Make trace file path text editable
- Save/Restore last used device

Change-Id:I762a054e16c8ac5418375a5ac2086add7f631e45*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index ac752e7..2038174 100644

//Synthetic comment -- @@ -43,6 +43,7 @@
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -51,8 +52,10 @@
private static final String TITLE = "OpenGL ES Trace Options";
private static final String DEFAULT_MESSAGE = "Provide the application and activity to be traced.";

    private static final String PREF_APPNAME = "gl.trace.appname";
    private static final String PREF_TRACEFILE = "gl.trace.destfile";

private static String sSaveToFolder = System.getProperty("user.home");;

//Synthetic comment -- @@ -69,6 +72,7 @@
private boolean mCollectFbOnEglSwap = true;
private boolean mCollectFbOnGlDraw = false;
private boolean mCollectTextureData = false;

public GLTraceOptionsDialog(Shell parentShell) {
super(parentShell);
//Synthetic comment -- @@ -86,10 +90,11 @@
c.setLayoutData(new GridData(GridData.FILL_BOTH));

createLabel(c, "Device:");
        createDeviceDropdown(c, AndroidDebugBridge.getBridge().getDevices());

createLabel(c, "Activity:");
        createAppToTraceText(c, "e.g. com.example.android.apis");

createLabel(c, "Capture Image:");
createCaptureImageOptions(c);
//Synthetic comment -- @@ -126,9 +131,14 @@
c.setLayoutData(new GridData(GridData.FILL_BOTH));

mTraceFilePathText = new Text(c, SWT.BORDER);
        mTraceFilePathText.setEditable(false);
mTraceFilePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mTraceFilePathText.setText(mTraceFilePath);

Button browse = new Button(c, SWT.PUSH);
browse.setText("Browse...");
//Synthetic comment -- @@ -230,8 +240,15 @@
}
items.add(name);
}
        mDeviceCombo.setItems(items.toArray(new String[0]));
        mDeviceCombo.select(0);
return mDeviceCombo;
}

//Synthetic comment -- @@ -260,14 +277,25 @@
}

private DialogStatus validateDialog() {
if (mActivityToTraceText.getText().trim().length() == 0) {
return new DialogStatus(false, "Provide an application name");
}

        if (mTraceFilePathText.getText().trim().length() == 0) {
return new DialogStatus(false, "Specify the location where the trace will be saved.");
}

return new DialogStatus(true, null);
}

//Synthetic comment -- @@ -286,6 +314,7 @@
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
prefs.put(PREF_APPNAME, mActivityToTrace);
prefs.put(PREF_TRACEFILE, mTraceFilePath);
try {
prefs.flush();
} catch (BackingStoreException e) {
//Synthetic comment -- @@ -297,6 +326,7 @@
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
mActivityToTrace = prefs.get(PREF_APPNAME, "");
mTraceFilePath = prefs.get(PREF_TRACEFILE, "");
}

public TraceOptions getTraceOptions() {







