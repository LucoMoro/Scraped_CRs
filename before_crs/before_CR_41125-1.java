/*gltrace: open trace file immediately after capture

 - Automatically open the trace file after trace has been captured
   rather than user having to manually open it.
 - If the file is already open, then the editor needs to be able
   to just update its internal model. Likewise for all the associated
   views (Details, State and Summary views).
 - Fix a few NPE's that may occur if the trace file is invalid.

Change-Id:I2c311bdd93fe4214fa2ebfbc49ff40f7cb5c2170*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 9da6c27..9acff50 100644

//Synthetic comment -- @@ -24,16 +24,28 @@
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.google.common.util.concurrent.SimpleTimeLimiter;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
//Synthetic comment -- @@ -128,6 +140,76 @@

// once tracing is complete, remove port forwarding
disablePortForwarding(device, LOCAL_FORWARDED_PORT);
}

private void startTracing(Shell shell, TraceOptions traceOptions, int port) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/DurationMinimap.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/DurationMinimap.java
//Synthetic comment -- index 7f468e1..9b4c57c 100644

//Synthetic comment -- @@ -59,10 +59,10 @@
private static final int MAX_DURATION_LENGTH_SCALE = 6;

/** List of GL Calls in the trace. */
    private final List<GLCall> mCalls;

/** Number of GL contexts in the trace. */
    private final int mContextCount;

/** Starting call index of currently displayed frame. */
private int mStartCallIndex;
//Synthetic comment -- @@ -102,8 +102,7 @@
public DurationMinimap(Composite parent, GLTrace trace) {
super(parent, SWT.NO_BACKGROUND);

        mCalls = trace.getGLCalls();
        mContextCount = trace.getContexts().size();

initializeColors();
initializeFonts();
//Synthetic comment -- @@ -161,6 +160,16 @@
});
}

@Override
public void dispose() {
disposeColors();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLCallGroups.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLCallGroups.java
//Synthetic comment -- index 053c92d..226d483 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.gltrace.model.GLTrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//Synthetic comment -- @@ -135,6 +136,10 @@
*/
public static List<GLCallNode> constructCallHierarchy(GLTrace trace, int start, int end,
int contextToGroup) {
if (contextToGroup < 0 || contextToGroup > trace.getContexts().size()) {
return flatHierarchy(trace, start, end);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index db69ffe..eff272a 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.gltrace.editors;

import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ide.eclipse.gltrace.GLProtoBuf.GLMessage.Function;
import com.android.ide.eclipse.gltrace.SwtUtils;
import com.android.ide.eclipse.gltrace.TraceFileParserTask;
//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -122,7 +123,9 @@
private boolean mShowContextSwitcher;
private int mCurrentlyDisplayedContext = -1;

private FrameSummaryViewPage mFrameSummaryViewPage;

public GLFunctionTraceViewer() {
mGldrawTextColor = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
//Synthetic comment -- @@ -170,28 +173,7 @@
GridData gd = new GridData(GridData.FILL_BOTH);
c.setLayoutData(gd);

        ProgressMonitorDialog dlg = new ProgressMonitorDialog(parent.getShell());
        TraceFileParserTask parser = new TraceFileParserTask(mFilePath, parent.getDisplay(),
                THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        try {
            dlg.run(true, true, parser);
        } catch (InvocationTargetException e) {
            // exception while parsing, display error to user
            MessageDialog.openError(parent.getShell(),
                    "Error parsing OpenGL Trace File",
                    e.getCause().getMessage());
            return;
        } catch (InterruptedException e) {
            // operation canceled by user, just return
            return;
        }

        mTrace = parser.getTrace();
        if (mTrace == null) {
            return;
        }

        mShowContextSwitcher = mTrace.getContexts().size() > 1;

createFrameSelectionControls(c);
createFilterBar(c);
//Synthetic comment -- @@ -199,13 +181,6 @@

getSite().setSelectionProvider(mFrameTreeViewer);

        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                refreshUI();
            }
        });

IActionBars actionBars = getEditorSite().getActionBars();
actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
new Action("Copy") {
//Synthetic comment -- @@ -232,10 +207,50 @@
});
}

private void refreshUI() {
int nFrames = 0;

        nFrames = mTrace.getFrames().size();
setFrameCount(nFrames);
selectFrame(1);
}
//Synthetic comment -- @@ -302,9 +317,13 @@
mFrameSelectionScale.setSelection(selectedFrame);
mFrameSelectionSpinner.setSelection(selectedFrame);

        GLFrame f = mTrace.getFrame(selectedFrame - 1);
        mCallStartIndex = f.getStartIndex();
        mCallEndIndex = f.getEndIndex();

// update tree view in the editor
refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
//Synthetic comment -- @@ -693,7 +712,11 @@
}

public StateViewPage getStateViewPage() {
        return new StateViewPage(mTrace);
}

public FrameSummaryViewPage getFrameSummaryViewPage() {
//Synthetic comment -- @@ -705,7 +728,11 @@
}

public DetailsPage getDetailsPage() {
        return new DetailsPage(mTrace);
}

private void copySelectionToClipboard() {
//Synthetic comment -- @@ -784,4 +811,8 @@
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java
//Synthetic comment -- index 7c71d91..bca75e5 100644

//Synthetic comment -- @@ -61,11 +61,11 @@
public static final String ID = "com.android.ide.eclipse.gltrace.views.GLState"; //$NON-NLS-1$
private static final ILock sGlStateLock = Job.getJobManager().newLock();

    private final GLTrace mTrace;
    private final List<GLCall> mGLCalls;

/** OpenGL State as of call {@link #mCurrentStateIndex}. */
    private final IGLProperty mState;
private int mCurrentStateIndex;

private String[] TREE_PROPERTIES = { "Name", "Value" };
//Synthetic comment -- @@ -73,11 +73,24 @@
private StateLabelProvider mLabelProvider;

public StateViewPage(GLTrace trace) {
mTrace = trace;
        mGLCalls = trace.getGLCalls();

mState = GLState.createDefaultState();
mCurrentStateIndex = -1;
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java
//Synthetic comment -- index fbe131a..64e9ac1 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
* OpenGL Calls present in the frame.
*/
public class FrameSummaryViewPage extends Page {
    private final GLTrace mTrace;

private int mCurrentFrame;

//Synthetic comment -- @@ -92,6 +92,10 @@
mTrace = trace;
}

@Override
public void createControl(Composite parent) {
mSash = new SashForm(parent, SWT.VERTICAL);
//Synthetic comment -- @@ -207,6 +211,9 @@

public void setSelectedFrame(int frame) {
mCurrentFrame = frame;

updateImageCanvas();
updateFrameStats();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/DetailsPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/DetailsPage.java
//Synthetic comment -- index 10e2c40..510898d 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
import java.util.List;

public class DetailsPage extends Page implements ISelectionListener {
    private final GLTrace mTrace;

private IToolBarManager mToolBarManager;
private Composite mTopComposite;
//Synthetic comment -- @@ -61,6 +61,10 @@
mTrace = trace;
}

@Override
public void createControl(Composite parent) {
mTopComposite = new Composite(parent, SWT.NONE);







