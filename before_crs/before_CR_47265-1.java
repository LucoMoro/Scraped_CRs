/*gltrace: support calling glGetError at user defined intervals.

Change-Id:Ic0d7be4abd8f163a816a1b34950477eaf97c07a8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 5e97305..f861285 100644

//Synthetic comment -- @@ -49,6 +49,7 @@

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -152,6 +153,10 @@
return;
}

final IWorkbench workbench = PlatformUI.getWorkbench();
IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
if (window == null) {
//Synthetic comment -- @@ -241,7 +246,10 @@
try {
traceCommandWriter.setTraceOptions(traceOptions.collectFbOnEglSwap,
traceOptions.collectFbOnGlDraw,
                    traceOptions.collectTextureData);
} catch (IOException e) {
MessageDialog.openError(shell,
"OpenGL Trace",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceCollectorDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceCollectorDialog.java
//Synthetic comment -- index 56dc8e9..55815ff 100644

//Synthetic comment -- @@ -103,7 +103,10 @@
boolean glTexImage = glTexImageCheckBox.getSelection();

try {
                    mTraceCommandWriter.setTraceOptions(eglSwap, glDraw, glTexImage);
} catch (IOException e) {
eglSwapCheckBox.setEnabled(false);
glDrawCheckBox.setEnabled(false);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index 764ba98..faf0d44 100644

//Synthetic comment -- @@ -75,6 +75,11 @@
private static boolean sCollectFbOnEglSwap = true;
private static boolean sCollectFbOnGlDraw = false;
private static boolean sCollectTextureData = false;
private static boolean sIsActivityFullyQualified = false;
private IDevice[] mDevices;

//Synthetic comment -- @@ -116,6 +121,11 @@

createSeparator(c);

createLabel(c, "Destination File: ");
createSaveToField(c);

//Synthetic comment -- @@ -221,6 +231,38 @@
readTextureDataCheckBox.addSelectionListener(l);
}

private Text createAppToTraceText(Composite parent, String defaultMessage) {
mAppPackageToTraceText = new Text(parent, SWT.BORDER);
mAppPackageToTraceText.setMessage(defaultMessage);
//Synthetic comment -- @@ -375,7 +417,8 @@

public TraceOptions getTraceOptions() {
return new TraceOptions(mSelectedDevice, mAppPackageToTrace, mActivityToTrace,
                sIsActivityFullyQualified, mTraceFilePath, sCollectFbOnEglSwap,
                sCollectFbOnGlDraw, sCollectTextureData);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceCommandWriter.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceCommandWriter.java
//Synthetic comment -- index dd5cd3c..86e5672 100644

//Synthetic comment -- @@ -30,6 +30,9 @@
private static final int READ_FB_ON_EGLSWAP_BIT = 0;
private static final int READ_FB_ON_GLDRAW_BIT = 1;
private static final int READ_TEXTURE_DATA_ON_GLTEXIMAGE_BIT = 2;

private final DataOutputStream mStream;;

//Synthetic comment -- @@ -38,12 +41,17 @@
}

public void setTraceOptions(boolean readFbOnEglSwap, boolean readFbOnGlDraw,
            boolean readTextureOnGlTexImage) throws IOException {
int eglSwap = readFbOnEglSwap ? (1 << READ_FB_ON_EGLSWAP_BIT) : 0;
int glDraw = readFbOnGlDraw ? (1 << READ_FB_ON_GLDRAW_BIT) : 0;
        int tex = readTextureOnGlTexImage ? ( 1 << READ_TEXTURE_DATA_ON_GLTEXIMAGE_BIT) : 0;

        int cmd = eglSwap | glDraw | tex;

mStream.writeInt(CMD_SIZE);
mStream.writeInt(cmd);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java
//Synthetic comment -- index b90c004..e247d2e 100644

//Synthetic comment -- @@ -93,12 +93,28 @@
try {
c.setStateTransformations(StateTransformFactory.getTransformsFor(msg));
} catch (Exception e) {
            c.setStateTransformationCreationError(e.getMessage());
GlTracePlugin.getDefault().logMessage("Error while creating transformations for "
+ c.toString() + ":");
GlTracePlugin.getDefault().logMessage(e.getMessage());
}

mGLCalls.add(c);
mGLContextIds.add(Integer.valueOf(c.getContextId()));
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceOptions.java
//Synthetic comment -- index ac9fb6b..3258ae9 100644

//Synthetic comment -- @@ -40,16 +40,33 @@
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
this.collectTextureData = collectTextureData;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java
//Synthetic comment -- index f7841cb..39cb0f1 100644

//Synthetic comment -- @@ -76,8 +76,8 @@
/** List of state transformations performed by this call. */
private List<IStateTransform> mStateTransforms = Collections.emptyList();

    /** Error conditions while creating state transforms for this call. */
    private String mStateTransformationCreationErrorMessage;

/** List of properties associated to this call. */
private SparseArray<Object> mProperties;
//Synthetic comment -- @@ -136,16 +136,16 @@
mStateTransforms = transforms;
}

    public void setStateTransformationCreationError(String errorMessage) {
        mStateTransformationCreationErrorMessage = errorMessage;
}

public boolean hasErrors() {
        return mStateTransformationCreationErrorMessage != null;
}

public String getError() {
        return mStateTransformationCreationErrorMessage;
}

public List<IStateTransform> getStateTransformations() {







