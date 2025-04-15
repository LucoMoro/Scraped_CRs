/*gltrace: Do not save thumbnail images in memory

These were originally saved with the idea that if the trace file
itself was overwritten after it was parsed, we could display
the thumbnail image atleast since we don't have access to the full
image anymore.

However, this hasn't turned out to be a common case, but it just
uses up memory for large traces.

Change-Id:I1152e23f29563f47b6818b89c5a50bf5ce2a5084*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java
//Synthetic comment -- index 8671ca3..d08b726 100644

//Synthetic comment -- @@ -78,29 +78,4 @@

return new Image(display, imageData);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java
//Synthetic comment -- index f525657..b90c004 100644

//Synthetic comment -- @@ -27,8 +27,6 @@

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -48,10 +46,6 @@
private static final GLMessageFormatter sGLMessageFormatter =
new GLMessageFormatter(GLAPISpec.getSpecs());

private String mTraceFilePath;
private RandomAccessFile mFile;

//Synthetic comment -- @@ -63,33 +57,20 @@
/**
* Construct a GL Trace file parser.
* @param path path to trace file
*/
    public TraceFileParserTask(String path) {
try {
mFile = new RandomAccessFile(path, "r"); //$NON-NLS-1$
} catch (FileNotFoundException e) {
throw new IllegalArgumentException(e);
}

mTraceFilePath = path;
mGLCalls = new ArrayList<GLCall>();
mGLContextIds = new TreeSet<Integer>();
}

private void addMessage(int index, long traceFileOffset, GLMessage msg, long startTime) {
String formattedMsg;
try {
formattedMsg = sGLMessageFormatter.formatGLMessage(msg);
//Synthetic comment -- @@ -101,7 +82,6 @@
startTime,
traceFileOffset,
formattedMsg,
msg.getFunction(),
msg.hasFb(),
msg.getContextId(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index 03d6f68..50ee717 100644

//Synthetic comment -- @@ -105,12 +105,6 @@
private static final String DEFAULT_FILTER_MESSAGE = "Filter list of OpenGL calls. Accepts Java regexes.";
private static final String NEWLINE = System.getProperty("line.separator"); //$NON-NLS-1$

private static Image sExpandAllIcon;

private static String sLastExportedToFolder;
//Synthetic comment -- @@ -231,8 +225,7 @@

public void setInput(Shell shell, String tracePath) {
ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
        TraceFileParserTask parser = new TraceFileParserTask(mFilePath);
try {
dlg.run(true, true, parser);
} catch (InvocationTargetException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java
//Synthetic comment -- index aae91c0..f7841cb 100644

//Synthetic comment -- @@ -21,8 +21,6 @@
import com.android.ide.eclipse.gltrace.state.transforms.IStateTransform;
import com.android.sdklib.util.SparseArray;

import java.util.Collections;
import java.util.List;

//Synthetic comment -- @@ -60,9 +58,6 @@
/** Flag indicating whether the original protobuf message included FB data. */
private final boolean mHasFb;

/** Full string representation of this call. */
private final String mDisplayString;

//Synthetic comment -- @@ -88,12 +83,11 @@
private SparseArray<Object> mProperties;

public GLCall(int index, long startTime, long traceFileOffset, String displayString,
            Function function, boolean hasFb, int contextId,
int wallTime, int threadTime) {
mIndex = index;
mStartTime = startTime;
mTraceFileOffset = traceFileOffset;
mDisplayString = displayString;
mFunction = function;
mHasFb = hasFb;
//Synthetic comment -- @@ -126,10 +120,6 @@
return mHasFb;
}

public long getStartTime() {
return mStartTime;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLTrace.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLTrace.java
//Synthetic comment -- index 2c4772e..7667644 100644

//Synthetic comment -- @@ -82,14 +82,14 @@
}

if (isTraceFileModified()) {
            return null;
}

RandomAccessFile file;
try {
file = new RandomAccessFile(mTraceFileInfo.getPath(), "r"); //$NON-NLS-1$
} catch (FileNotFoundException e1) {
            return null;
}

GLMessage m = null;







