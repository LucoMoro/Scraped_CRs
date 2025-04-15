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
import com.android.ide.eclipse.gltrace.editors.GLFunctionTraceViewer;
import com.google.common.util.concurrent.SimpleTimeLimiter;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.io.DataInputStream;
import java.io.DataOutputStream;
//Synthetic comment -- @@ -128,6 +140,76 @@

// once tracing is complete, remove port forwarding
disablePortForwarding(device, LOCAL_FORWARDED_PORT);

        // and finally open the editor to view the file
        openInEditor(shell, traceOptions.traceDestination);
    }

    private void openInEditor(Shell shell, String traceFilePath) {
        final IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(traceFilePath));
        if (!fileStore.fetchInfo().exists()) {
            return;
        }

        final IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null) {
            return;
        }

        IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        // if there is a editor already open, then refresh its model
        GLFunctionTraceViewer viewer = getOpenTraceViewer(page, traceFilePath);
        if (viewer != null) {
            viewer.setInput(shell, traceFilePath);
        }

        // open the editor (if not open), or bring it to foreground if it is already open
        try {
            IDE.openEditorOnFileStore(page, fileStore);
        } catch (PartInitException e) {
            GlTracePlugin.getDefault().logMessage(
                    "Unexpected error while opening gltrace file in editor: " + e);
            return;
        }
    }

    /**
     * Returns the editor part that has the provided file path open.
     * @param page page containing editors
     * @param traceFilePath file that should be open in an editor
     * @return if given trace file is already open, then a reference to that editor part,
     *         null otherwise
     */
    private GLFunctionTraceViewer getOpenTraceViewer(IWorkbenchPage page, String traceFilePath) {
        IEditorReference[] editorRefs = page.getEditorReferences();
        for (IEditorReference ref : editorRefs) {
            String id = ref.getId();
            if (!GLFunctionTraceViewer.ID.equals(id)) {
                continue;
            }

            IEditorInput input = null;
            try {
                input = ref.getEditorInput();
            } catch (PartInitException e) {
                continue;
            }

            if (!(input instanceof IURIEditorInput)) {
                continue;
            }

            if (traceFilePath.equals(((IURIEditorInput) input).getURI().getPath())) {
                return (GLFunctionTraceViewer) ref.getEditor(true);
            }
        }

        return null;
}

private void startTracing(Shell shell, TraceOptions traceOptions, int port) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/DurationMinimap.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/DurationMinimap.java
//Synthetic comment -- index 7f468e1..9b4c57c 100644

//Synthetic comment -- @@ -59,10 +59,10 @@
private static final int MAX_DURATION_LENGTH_SCALE = 6;

/** List of GL Calls in the trace. */
    private List<GLCall> mCalls;

/** Number of GL contexts in the trace. */
    private int mContextCount;

/** Starting call index of currently displayed frame. */
private int mStartCallIndex;
//Synthetic comment -- @@ -102,8 +102,7 @@
public DurationMinimap(Composite parent, GLTrace trace) {
super(parent, SWT.NO_BACKGROUND);

        setInput(trace);

initializeColors();
initializeFonts();
//Synthetic comment -- @@ -161,6 +160,16 @@
});
}

    public void setInput(GLTrace trace) {
        if (trace != null) {
            mCalls = trace.getGLCalls();
            mContextCount = trace.getContexts().size();
        } else {
            mCalls = null;
            mContextCount = 1;
        }
    }

@Override
public void dispose() {
disposeColors();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLCallGroups.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLCallGroups.java
//Synthetic comment -- index 053c92d..226d483 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.gltrace.model.GLTrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

//Synthetic comment -- @@ -135,6 +136,10 @@
*/
public static List<GLCallNode> constructCallHierarchy(GLTrace trace, int start, int end,
int contextToGroup) {
        if (trace == null) {
            return Collections.emptyList();
        }

if (contextToGroup < 0 || contextToGroup > trace.getContexts().size()) {
return flatHierarchy(trace, start, end);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index db69ffe..eff272a 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.gltrace.editors;

import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
import com.android.ide.eclipse.gltrace.GLProtoBuf.GLMessage.Function;
import com.android.ide.eclipse.gltrace.SwtUtils;
import com.android.ide.eclipse.gltrace.TraceFileParserTask;
//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -122,7 +123,9 @@
private boolean mShowContextSwitcher;
private int mCurrentlyDisplayedContext = -1;

    private StateViewPage mStateViewPage;
private FrameSummaryViewPage mFrameSummaryViewPage;
    private DetailsPage mDetailsPage;

public GLFunctionTraceViewer() {
mGldrawTextColor = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
//Synthetic comment -- @@ -170,28 +173,7 @@
GridData gd = new GridData(GridData.FILL_BOTH);
c.setLayoutData(gd);

        setInput(parent.getShell(), mFilePath);

createFrameSelectionControls(c);
createFilterBar(c);
//Synthetic comment -- @@ -199,13 +181,6 @@

getSite().setSelectionProvider(mFrameTreeViewer);

IActionBars actionBars = getEditorSite().getActionBars();
actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
new Action("Copy") {
//Synthetic comment -- @@ -232,10 +207,50 @@
});
}

    public void setInput(Shell shell, String tracePath) {
        ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
        TraceFileParserTask parser = new TraceFileParserTask(mFilePath, shell.getDisplay(),
                THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        try {
            dlg.run(true, true, parser);
        } catch (InvocationTargetException e) {
            // exception while parsing, display error to user
            MessageDialog.openError(shell,
                    "Error parsing OpenGL Trace File",
                    e.getCause().getMessage());
            return;
        } catch (InterruptedException e) {
            // operation canceled by user, just return
            return;
        }

        mTrace = parser.getTrace();
        mShowContextSwitcher = (mTrace == null) ? false : mTrace.getContexts().size() > 1;
        if (mStateViewPage != null) {
            mStateViewPage.setInput(mTrace);
        }
        if (mFrameSummaryViewPage != null) {
            mFrameSummaryViewPage.setInput(mTrace);
        }
        if (mDetailsPage != null) {
            mDetailsPage.setInput(mTrace);
        }
        if (mDurationMinimap != null) {
            mDurationMinimap.setInput(mTrace);
        }

        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                refreshUI();
            }
        });
    }

private void refreshUI() {
int nFrames = 0;

        nFrames = mTrace == null ? 1 : mTrace.getFrames().size();
setFrameCount(nFrames);
selectFrame(1);
}
//Synthetic comment -- @@ -302,9 +317,13 @@
mFrameSelectionScale.setSelection(selectedFrame);
mFrameSelectionSpinner.setSelection(selectedFrame);

        if (mTrace != null) {
            GLFrame f = mTrace.getFrame(selectedFrame - 1);
            mCallStartIndex = f.getStartIndex();
            mCallEndIndex = f.getEndIndex();
        } else {
            mCallStartIndex = mCallEndIndex = 0;
        }

// update tree view in the editor
refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
//Synthetic comment -- @@ -693,7 +712,11 @@
}

public StateViewPage getStateViewPage() {
        if (mStateViewPage == null) {
            mStateViewPage = new StateViewPage(mTrace);
        }

        return mStateViewPage;
}

public FrameSummaryViewPage getFrameSummaryViewPage() {
//Synthetic comment -- @@ -705,7 +728,11 @@
}

public DetailsPage getDetailsPage() {
        if (mDetailsPage == null) {
            mDetailsPage = new DetailsPage(mTrace);
        }

        return mDetailsPage;
}

private void copySelectionToClipboard() {
//Synthetic comment -- @@ -784,4 +811,8 @@
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
}

    public String getInputPath() {
        return mFilePath;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/StateViewPage.java
//Synthetic comment -- index 7c71d91..bca75e5 100644

//Synthetic comment -- @@ -61,11 +61,11 @@
public static final String ID = "com.android.ide.eclipse.gltrace.views.GLState"; //$NON-NLS-1$
private static final ILock sGlStateLock = Job.getJobManager().newLock();

    private GLTrace mTrace;
    private List<GLCall> mGLCalls;

/** OpenGL State as of call {@link #mCurrentStateIndex}. */
    private IGLProperty mState;
private int mCurrentStateIndex;

private String[] TREE_PROPERTIES = { "Name", "Value" };
//Synthetic comment -- @@ -73,11 +73,24 @@
private StateLabelProvider mLabelProvider;

public StateViewPage(GLTrace trace) {
        setInput(trace);
    }

    public void setInput(GLTrace trace) {
mTrace = trace;
        if (trace != null) {
            mGLCalls = trace.getGLCalls();
        } else {
            mGLCalls = null;
        }

mState = GLState.createDefaultState();
mCurrentStateIndex = -1;

        if (mTreeViewer != null) {
            mTreeViewer.setInput(mState);
            mTreeViewer.refresh();
        }
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java
//Synthetic comment -- index fbe131a..64e9ac1 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
* OpenGL Calls present in the frame.
*/
public class FrameSummaryViewPage extends Page {
    private GLTrace mTrace;

private int mCurrentFrame;

//Synthetic comment -- @@ -92,6 +92,10 @@
mTrace = trace;
}

    public void setInput(GLTrace trace) {
        mTrace = trace;
    }

@Override
public void createControl(Composite parent) {
mSash = new SashForm(parent, SWT.VERTICAL);
//Synthetic comment -- @@ -207,6 +211,9 @@

public void setSelectedFrame(int frame) {
mCurrentFrame = frame;
        if (mTrace == null) {
            return;
        }

updateImageCanvas();
updateFrameStats();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/DetailsPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/DetailsPage.java
//Synthetic comment -- index 10e2c40..510898d 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
import java.util.List;

public class DetailsPage extends Page implements ISelectionListener {
    private GLTrace mTrace;

private IToolBarManager mToolBarManager;
private Composite mTopComposite;
//Synthetic comment -- @@ -61,6 +61,10 @@
mTrace = trace;
}

    public void setInput(GLTrace trace) {
        mTrace = trace;
    }

@Override
public void createControl(Composite parent) {
mTopComposite = new Composite(parent, SWT.NONE);







