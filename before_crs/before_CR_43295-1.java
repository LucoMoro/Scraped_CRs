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

    /**
     * Obtains the image stored in provided protocol buffer message scaled to the
     * provided dimensions.
     */
    public static Image getScaledImage(Display display, GLMessage glMsg, int width, int height) {
        if (!glMsg.hasFb()) {
            return null;
        }

        ImageData imageData = null;
        try {
            imageData = getImageData(glMsg);
        } catch (Exception e) {
            GlTracePlugin.getDefault().logMessage(
                    "Unexpected error while retrieving framebuffer image: " + e);
            return null;
        }

        if (imageData == null) {
            return null;
        }

        return new Image(display, imageData.scaledTo(width, height));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java
//Synthetic comment -- index f525657..b90c004 100644

//Synthetic comment -- @@ -27,8 +27,6 @@

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -48,10 +46,6 @@
private static final GLMessageFormatter sGLMessageFormatter =
new GLMessageFormatter(GLAPISpec.getSpecs());

    private final Display mDisplay;
    private final int mThumbHeight;
    private final int mThumbWidth;

private String mTraceFilePath;
private RandomAccessFile mFile;

//Synthetic comment -- @@ -63,33 +57,20 @@
/**
* Construct a GL Trace file parser.
* @param path path to trace file
     * @param thumbDisplay display to use to create thumbnail images
     * @param thumbWidth width of thumbnail images
     * @param thumbHeight height of thumbnail images
*/
    public TraceFileParserTask(String path, Display thumbDisplay, int thumbWidth,
            int thumbHeight) {
try {
mFile = new RandomAccessFile(path, "r"); //$NON-NLS-1$
} catch (FileNotFoundException e) {
throw new IllegalArgumentException(e);
}

        mDisplay = thumbDisplay;
        mThumbWidth = thumbWidth;
        mThumbHeight = thumbHeight;

mTraceFilePath = path;
mGLCalls = new ArrayList<GLCall>();
mGLContextIds = new TreeSet<Integer>();
}

private void addMessage(int index, long traceFileOffset, GLMessage msg, long startTime) {
        Image previewImage = null;
        if (mDisplay != null) {
            previewImage = ProtoBufUtils.getScaledImage(mDisplay, msg, mThumbWidth, mThumbHeight);
        }

String formattedMsg;
try {
formattedMsg = sGLMessageFormatter.formatGLMessage(msg);
//Synthetic comment -- @@ -101,7 +82,6 @@
startTime,
traceFileOffset,
formattedMsg,
                                previewImage,
msg.getFunction(),
msg.hasFb(),
msg.getContextId(),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index 03d6f68..50ee717 100644

//Synthetic comment -- @@ -105,12 +105,6 @@
private static final String DEFAULT_FILTER_MESSAGE = "Filter list of OpenGL calls. Accepts Java regexes.";
private static final String NEWLINE = System.getProperty("line.separator"); //$NON-NLS-1$

    /** Width of thumbnail images of the framebuffer. */
    private static final int THUMBNAIL_WIDTH = 50;

    /** Height of thumbnail images of the framebuffer. */
    private static final int THUMBNAIL_HEIGHT = 50;

private static Image sExpandAllIcon;

private static String sLastExportedToFolder;
//Synthetic comment -- @@ -231,8 +225,7 @@

public void setInput(Shell shell, String tracePath) {
ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
        TraceFileParserTask parser = new TraceFileParserTask(mFilePath, shell.getDisplay(),
                THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
try {
dlg.run(true, true, parser);
} catch (InvocationTargetException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLCall.java
//Synthetic comment -- index aae91c0..f7841cb 100644

//Synthetic comment -- @@ -21,8 +21,6 @@
import com.android.ide.eclipse.gltrace.state.transforms.IStateTransform;
import com.android.sdklib.util.SparseArray;

import org.eclipse.swt.graphics.Image;

import java.util.Collections;
import java.util.List;

//Synthetic comment -- @@ -60,9 +58,6 @@
/** Flag indicating whether the original protobuf message included FB data. */
private final boolean mHasFb;

    /** Thumbnail image of the framebuffer if available. */
    private final Image mThumbnailImage;

/** Full string representation of this call. */
private final String mDisplayString;

//Synthetic comment -- @@ -88,12 +83,11 @@
private SparseArray<Object> mProperties;

public GLCall(int index, long startTime, long traceFileOffset, String displayString,
            Image thumbnailImage, Function function, boolean hasFb, int contextId,
int wallTime, int threadTime) {
mIndex = index;
mStartTime = startTime;
mTraceFileOffset = traceFileOffset;
        mThumbnailImage = thumbnailImage;
mDisplayString = displayString;
mFunction = function;
mHasFb = hasFb;
//Synthetic comment -- @@ -126,10 +120,6 @@
return mHasFb;
}

    public Image getThumbnailImage() {
        return mThumbnailImage;
    }

public long getStartTime() {
return mStartTime;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLTrace.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/model/GLTrace.java
//Synthetic comment -- index 2c4772e..7667644 100644

//Synthetic comment -- @@ -82,14 +82,14 @@
}

if (isTraceFileModified()) {
            return c.getThumbnailImage();
}

RandomAccessFile file;
try {
file = new RandomAccessFile(mTraceFileInfo.getPath(), "r"); //$NON-NLS-1$
} catch (FileNotFoundException e1) {
            return c.getThumbnailImage();
}

GLMessage m = null;







