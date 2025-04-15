/*Minor UI cleanup to trace options dialog

- Fix default message shown for activity
- Make trace file path text editable
- Save/Restore last used device

Change-Id:I762a054e16c8ac5418375a5ac2086add7f631e45*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index ac752e7..2359791 100644

//Synthetic comment -- @@ -43,6 +43,7 @@
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -53,6 +54,8 @@

private static final String PREF_APPNAME = "gl.trace.appname";
private static final String PREF_TRACEFILE = "gl.trace.destfile";
    private static final String PREF_DEVICE = "gl.trace.device";
    private String mLastUsedDevice;

private static String sSaveToFolder = System.getProperty("user.home");;

//Synthetic comment -- @@ -69,6 +72,7 @@
private boolean mCollectFbOnEglSwap = true;
private boolean mCollectFbOnGlDraw = false;
private boolean mCollectTextureData = false;
    private IDevice[] mDevices;

public GLTraceOptionsDialog(Shell parentShell) {
super(parentShell);
//Synthetic comment -- @@ -86,10 +90,11 @@
c.setLayoutData(new GridData(GridData.FILL_BOTH));

createLabel(c, "Device:");
        mDevices = AndroidDebugBridge.getBridge().getDevices();
        createDeviceDropdown(c, mDevices);

createLabel(c, "Activity:");
        createAppToTraceText(c, "e.g. com.example.package/.ActivityName");

createLabel(c, "Capture Image:");
createCaptureImageOptions(c);
//Synthetic comment -- @@ -126,9 +131,14 @@
c.setLayoutData(new GridData(GridData.FILL_BOTH));

mTraceFilePathText = new Text(c, SWT.BORDER);
mTraceFilePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mTraceFilePathText.setText(mTraceFilePath);
        mTraceFilePathText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateAndSetMessage();
            }
        });

Button browse = new Button(c, SWT.PUSH);
browse.setText("Browse...");
//Synthetic comment -- @@ -230,8 +240,15 @@
}
items.add(name);
}
        mDeviceCombo.setItems(items.toArray(new String[items.size()]));

        int index = 0;
        if (mLastUsedDevice != null) {
            index = items.indexOf(mLastUsedDevice);
        }
        if (index >= 0 && index < items.size()) {
            mDeviceCombo.select(index);
        }
return mDeviceCombo;
}

//Synthetic comment -- @@ -260,14 +277,25 @@
}

private DialogStatus validateDialog() {
        if (mDevices.length == 0) {
            return new DialogStatus(false, "No connected devices.");
        }

if (mActivityToTraceText.getText().trim().length() == 0) {
return new DialogStatus(false, "Provide an application name");
}

        String traceFile = mTraceFilePathText.getText().trim();
        if (traceFile.isEmpty()) {
return new DialogStatus(false, "Specify the location where the trace will be saved.");
}

        File f = new File(traceFile).getParentFile();
        if (f != null && !f.exists()) {
            return new DialogStatus(false,
                    String.format("Folder %s does not exist", f.getAbsolutePath()));
        }

return new DialogStatus(true, null);
}

//Synthetic comment -- @@ -286,6 +314,7 @@
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
prefs.put(PREF_APPNAME, mActivityToTrace);
prefs.put(PREF_TRACEFILE, mTraceFilePath);
        prefs.put(PREF_DEVICE, mSelectedDevice);
try {
prefs.flush();
} catch (BackingStoreException e) {
//Synthetic comment -- @@ -297,6 +326,7 @@
IEclipsePreferences prefs = new InstanceScope().getNode(GlTracePlugin.PLUGIN_ID);
mActivityToTrace = prefs.get(PREF_APPNAME, "");
mTraceFilePath = prefs.get(PREF_TRACEFILE, "");
        mLastUsedDevice = prefs.get(PREF_DEVICE, "");
}

public TraceOptions getTraceOptions() {







