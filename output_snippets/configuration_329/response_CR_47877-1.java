//<Beginning of snippet n. 0>


import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.SpinButton;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.SashForm;

private Color mGldrawTextColor;
private Color mGlCallErrorColor;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
private Runnable pendingRefreshJob = null;

private final long DEBOUNCE_DELAY_MS = 250;

mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
    private long lastSelectionTime = 0;

    @Override
    public void widgetSelected(SelectionEvent e) {
        mFrameSelectionSpinner.setEnabled(false);

        int selectedFrame = mFrameSelectionSpinner.getSelection();
        mFrameSelectionScale.setSelection(selectedFrame);
        selectFrame(selectedFrame);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSelectionTime < DEBOUNCE_DELAY_MS) {
            cancelPendingRefresh();
        }
        lastSelectionTime = currentTime;

        scheduleRefresh(selectedFrame);
    }

    private void scheduleRefresh(int selectedFrame) {
        cancelPendingRefresh();
        pendingRefreshJob = () -> {
            refreshFrameData(selectedFrame);
            pendingRefreshJob = null;
            mFrameSelectionSpinner.setEnabled(true);
        };
        executorService.schedule(pendingRefreshJob, DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    private void cancelPendingRefresh() {
        if (pendingRefreshJob != null) {
            pendingRefreshJob = null;
        }
    }

    private void refreshFrameData(int selectedFrame) {
        try {
            if (mTrace != null) {
                GLFrame f = mTrace.getFrame(selectedFrame - 1);
                mCallStartIndex = f.getStartIndex();
                mCallEndIndex = f.getEndIndex();
            } else {
                mCallStartIndex = mCallEndIndex = 0;
            }

            refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
            mDurationMinimap.setCallRangeForCurrentFrame(mCallStartIndex, mCallEndIndex);

            if (mFrameSummaryViewPage != null) {
                mFrameSummaryViewPage.setSelectedFrame(selectedFrame - 1);
            }
        } catch (Exception e) {
            ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error", "Failed to refresh frame data", 
            new Status(IStatus.ERROR, "MyPluginId", e.getMessage(), e));
            mFrameSelectionSpinner.setEnabled(true); // Ensure spinner is enabled in case of error
        }
    }
});

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {
    // Implementation for refreshing the tree goes here.
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public class FrameSummaryViewPage extends Page {
    private GLTrace mTrace;

    private int mCurrentFrame;

    private SashForm mSash;

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
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    // Do frame stats calculation logic here
                    return Status.OK_STATUS;
                } catch (Exception e) {
                    // Handle error gracefully, potentially logging it.
                    return Status.CANCEL_STATUS;
                }
            }
        };
        job.schedule();
    }

    private void updateImageCanvas() {
        int lastCallIndex = mTrace.getFrame(mCurrentFrame).getEndIndex() - 1;
        if (lastCallIndex >= 0 && lastCallIndex < mTrace.getGLCalls().size()) {
            GLCall call = mTrace.getGLCalls().get(lastCallIndex);
            final Image image = mTrace.getImage(call);
            // Update image canvas with new image
        }
    }
}
//<End of snippet n. 1>