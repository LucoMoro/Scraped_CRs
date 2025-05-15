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
private Runnable refreshRunnable;

mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(SelectionEvent e) {
        mFrameSelectionSpinner.setEnabled(false);
        int selectedFrame = mFrameSelectionSpinner.getSelection();
        mFrameSelectionScale.setSelection(selectedFrame);
        selectFrame(selectedFrame);
        
        ScheduleRefresh(selectedFrame);
    }
});

private void ScheduleRefresh(int selectedFrame) {
    if (refreshRunnable != null) {
        Display.getDefault().asyncExec(() -> {
            if (refreshRunnable != null) {
                refreshRunnable.run();
            }
        });
    }

    refreshRunnable = () -> {
        try {
            Thread.sleep(250);
            Display.getDefault().asyncExec(() -> {
                mFrameSelectionSpinner.setEnabled(true);
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
            });
        } catch (InterruptedException ex) {
            // Handle thread interruption
        } finally {
            refreshRunnable = null;
        }
    };
}

// Assuming the additional code required to handle context changes includes the right logic here.
private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {
    // Refresh logic here
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
                // Update stats logic here
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
            // Further image processing here
        }
    }
}
//<End of snippet n. 1>