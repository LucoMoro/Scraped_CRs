/*gltrace: Refresh tree view in a separate task.

When the currently displayed frame is changed, we immediately
refresh the tree displaying the list of calls, and the frame summary
view displaying the frame buffer as part of the frame change event
handler.

This CL moves these two operations to a separate task that is
scheduled after a short delay (250 ms). This allows us to combine
multiple frame change events and respond just once for all of them
together.

Change-Id:I4edade1e55cbd7394cba03d97b2ee61ca3a258b6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index d118130..b809ddd 100644

//Synthetic comment -- @@ -33,7 +33,9 @@
import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -124,6 +126,26 @@
private Color mGldrawTextColor;
private Color mGlCallErrorColor;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
//Synthetic comment -- @@ -309,18 +331,9 @@
mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                // Disable spinner until all necessary action is complete.
                // This seems to be necessary (atleast on Linux) for the spinner to not get
                // stuck in a pressed state if it is pressed for more than a few seconds
                // continuously.
                mFrameSelectionSpinner.setEnabled(false);

int selectedFrame = mFrameSelectionSpinner.getSelection();
mFrameSelectionScale.setSelection(selectedFrame);
selectFrame(selectedFrame);

                // re-enable spinner
                mFrameSelectionSpinner.setEnabled(true);
}
});
}
//Synthetic comment -- @@ -338,24 +351,22 @@
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

// update minimap view
mDurationMinimap.setCallRangeForCurrentFrame(mCallStartIndex, mCallEndIndex);

        // update the frame summary view
        if (mFrameSummaryViewPage != null) {
            mFrameSummaryViewPage.setSelectedFrame(selectedFrame - 1);
        }
}

/**
//Synthetic comment -- @@ -368,8 +379,47 @@
return;
}

        mCurrentlyDisplayedContext = context;
        refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
}

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java
//Synthetic comment -- index e2c20ce..25de48f 100644

//Synthetic comment -- @@ -63,6 +63,8 @@
public class FrameSummaryViewPage extends Page {
private GLTrace mTrace;

private int mCurrentFrame;

private SashForm mSash;
//Synthetic comment -- @@ -217,17 +219,39 @@
}

public void setSelectedFrame(int frame) {
        mCurrentFrame = frame;
if (mTrace == null) {
return;
}

        updateImageCanvas();
        updateFrameStats();
}

    private void updateFrameStats() {
        final List<GLCall> calls = mTrace.getGLCallsForFrame(mCurrentFrame);

Job job = new Job("Update Frame Statistics") {
@Override
//Synthetic comment -- @@ -278,8 +302,8 @@
return String.format("%.2f ms", milliSeconds);          //$NON-NLS-1$
}

    private void updateImageCanvas() {
        int lastCallIndex = mTrace.getFrame(mCurrentFrame).getEndIndex() - 1;
if (lastCallIndex >= 0 && lastCallIndex < mTrace.getGLCalls().size()) {
GLCall call = mTrace.getGLCalls().get(lastCallIndex);
final Image image = mTrace.getImage(call);







