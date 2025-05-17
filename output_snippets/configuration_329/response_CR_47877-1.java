//<Beginning of snippet n. 0>

import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

private Color mGldrawTextColor;
private Color mGlCallErrorColor;

private int mCallStartIndex;
private int mCallEndIndex;
private final ReentrantLock lock = new ReentrantLock();

private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
private Runnable refreshTask;

mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        mFrameSelectionSpinner.setEnabled(false);

        int selectedFrame = mFrameSelectionSpinner.getSelection();
        mFrameSelectionScale.setSelection(selectedFrame);
        selectFrame(selectedFrame);

        if (refreshTask != null) {
            scheduler.shutdownNow();
        }

        refreshTask = () -> {
            try {
                if (mTrace != null) {
                    GLFrame f = mTrace.getFrame(selectedFrame - 1);
                    lock.lock();
                    try {
                        mCallStartIndex = f.getStartIndex();
                        mCallEndIndex = f.getEndIndex();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    lock.lock();
                    try {
                        mCallStartIndex = mCallEndIndex = 0;
                    } finally {
                        lock.unlock();
                    }
                }

                refreshTree(mCallStartIndex, mCallEndIndex, mCurrentlyDisplayedContext);
                mDurationMinimap.setCallRangeForCurrentFrame(mCallStartIndex, mCallEndIndex);

                if (mFrameSummaryViewPage != null) {
                    mFrameSummaryViewPage.setSelectedFrame(selectedFrame - 1);
                }
            } catch (Exception ex) {
                ErrorDialog.openError(mFrameSelectionSpinner.getShell(), "Error", "Error updating the tree", new Status(Status.ERROR, "pluginId", ex.getMessage(), ex));
            } finally {
                mFrameSelectionSpinner.setEnabled(true);
            }
        };

        scheduler.schedule(refreshTask, 250, TimeUnit.MILLISECONDS);
    }
});
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
                    // implement the actual logic for updating frame stats here
                    return Status.OK_STATUS;
                } catch (Exception e) {
                    return new Status(Status.ERROR, "pluginId", "Error updating frame stats: " + e.getMessage(), e);
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
            // Add logic to display the image in the appropriate canvas
        }
    }
}
//<End of snippet n. 1>