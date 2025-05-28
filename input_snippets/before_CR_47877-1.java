
//<Beginning of snippet n. 0>


import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
private Color mGldrawTextColor;
private Color mGlCallErrorColor;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
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
return;
}

        mCurrentlyDisplayedContext = context;
        refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
}

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public class FrameSummaryViewPage extends Page {
private GLTrace mTrace;

private int mCurrentFrame;

private SashForm mSash;
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
return String.format("%.2f ms", milliSeconds);          //$NON-NLS-1$
}

    private void updateImageCanvas() {
        int lastCallIndex = mTrace.getFrame(mCurrentFrame).getEndIndex() - 1;
if (lastCallIndex >= 0 && lastCallIndex < mTrace.getGLCalls().size()) {
GLCall call = mTrace.getGLCalls().get(lastCallIndex);
final Image image = mTrace.getImage(call);

//<End of snippet n. 1>








