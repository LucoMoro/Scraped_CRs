//<Beginning of snippet n. 0>

import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

private Color mGldrawTextColor;
private Color mGlCallErrorColor;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
private Runnable refreshTask;
private long refreshDelay = 250;

mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        // Disable spinner until all necessary action is complete.
        mFrameSelectionSpinner.setEnabled(false);

        int selectedFrame = mFrameSelectionSpinner.getSelection();
        mFrameSelectionScale.setSelection(selectedFrame);
        selectFrame(selectedFrame);

        if (mTrace != null) {
            GLFrame f = mTrace.getFrame(selectedFrame - 1);
            mCallStartIndex = f.getStartIndex();
            mCallEndIndex = f.getEndIndex();
        } else {
            mCallStartIndex = mCallEndIndex = 0;
        }

        // Schedule tree view refresh
        scheduleRefresh();
        
        // Update minimap view
        mDurationMinimap.setCallRangeForCurrentFrame(mCallStartIndex, mCallEndIndex);
        
        // Update the frame summary view
        if (mFrameSummaryViewPage != null) {
            mFrameSummaryViewPage.setSelectedFrame(selectedFrame - 1);
        }
    }
});

private void scheduleRefresh() {
    if (refreshTask != null) {
        Display.getDefault().asyncExec(() -> {
            refreshTask = null; // Cancel any existing task
        });
    }

    refreshTask = () -> {
        try {
            refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
        } catch (Exception ex) {
            Display.getDefault().asyncExec(() -> {
                ErrorDialog.openError(new Shell(), "Error", "An error occurred while refreshing the tree: " + ex.getMessage() + "\n" + ExceptionUtils.getStackTrace(ex), null);
            });
        } finally {
            refreshTask = null; // Reset the task once done
            Display.getDefault().asyncExec(() -> {
                mFrameSelectionSpinner.setEnabled(true); // Re-enable spinner after refresh completion
            });
        }
    };

    Display.getDefault().timerExec(refreshDelay, refreshTask);
}

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {
    // Tree refreshing logic
}

//<End of snippet n. 0>

``` 

```java
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
                // Your logic for calculating statistics
                long milliSeconds = 0; // Replace with actual calculation
                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    private void updateImageCanvas() {
        int lastCallIndex = mTrace.getFrame(mCurrentFrame).getEndIndex() - 1;
        if (lastCallIndex >= 0 && lastCallIndex < mTrace.getGLCalls().size()) {
            GLCall call = mTrace.getGLCalls().get(lastCallIndex);
            final Image image = mTrace.getImage(call);
            // Load image in canvas
        }
    }
}

//<End of snippet n. 1>