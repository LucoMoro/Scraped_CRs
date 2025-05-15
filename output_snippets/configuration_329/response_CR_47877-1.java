//<Beginning of snippet n. 0>

import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

private Color mGldrawTextColor;
private Color mGlCallErrorColor;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
private Runnable refreshTask;
private Shell shell; // Assuming a reference to the shell

mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        // Disable spinner until all necessary action is complete.
        mFrameSelectionSpinner.setEnabled(false);

        int selectedFrame = mFrameSelectionSpinner.getSelection();
        mFrameSelectionScale.setSelection(selectedFrame);
        selectFrame(selectedFrame);

        mCurrentlyDisplayedContext = context; // Capture context here

        if (refreshTask != null) {
            executorService.shutdownNow(); // Clear previous scheduled tasks
        }

        refreshTask = () -> {
            try {
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
            } catch (Exception ex) {
                Display.getDefault().asyncExec(() -> {
                    ErrorDialog.openError(shell, "Error", "An error occurred during refresh.", null); 
                });
            } finally {
                // Re-enable spinner
                Display.getDefault().asyncExec(() -> mFrameSelectionSpinner.setEnabled(true));
            }
        };

        executorService.schedule(refreshTask, 250, TimeUnit.MILLISECONDS);
    }
});

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {
    // Implementation of the refreshTree functionality goes here
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
                // Perform statistics calculation here
                // Assuming some logic to calculate milliSeconds
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
            // Code to update image canvas with the retrieved image
        }
    }
}

//<End of snippet n. 1>