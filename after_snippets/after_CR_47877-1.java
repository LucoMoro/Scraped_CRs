
//<Beginning of snippet n. 0>


import com.google.common.io.Files;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
private Color mGldrawTextColor;
private Color mGlCallErrorColor;

    /**
     * Job to refresh the tree view & frame summary view.
     *
     * When the currently displayed frame is changed, either via the {@link #mFrameSelectionScale}
     * or via {@link #mFrameSelectionSpinner}, we need to update the displayed tree of calls for
     * that frame, and the frame summary view. Both these operations need to happen on the UI
     * thread, but are time consuming. This works out ok if the frame selection is not changing
     * rapidly (i.e., when the spinner or scale is moved to the target frame in a single action).
     * However, if the spinner is constantly pressed, then the user is scrolling through a sequence
     * of frames, and rather than refreshing the details for each of the intermediate frames,
     * we create a job to refresh the details and schedule the job after a short interval
     * {@link #TREE_REFRESH_INTERVAL}. This allows us to stay responsive to the spinner/scale,
     * and not do the costly refresh for each of the intermediate frames.
     */
    private Job mTreeRefresherJob;
    private final Object mTreeRefresherLock = new Object();
    private static final int TREE_REFRESH_INTERVAL_MS = 250;

    private int mCurrentFrame;

// Currently displayed frame's start and end call indices.
private int mCallStartIndex;
private int mCallEndIndex;
mFrameSelectionSpinner.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
int selectedFrame = mFrameSelectionSpinner.getSelection();
mFrameSelectionScale.setSelection(selectedFrame);
selectFrame(selectedFrame);
}
});
}
mFrameSelectionScale.setSelection(selectedFrame);
mFrameSelectionSpinner.setSelection(selectedFrame);

        synchronized (mTreeRefresherLock) {
            if (mTrace != null) {
                GLFrame f = mTrace.getFrame(selectedFrame - 1);
                mCallStartIndex = f.getStartIndex();
                mCallEndIndex = f.getEndIndex();
            } else {
                mCallStartIndex = mCallEndIndex = 0;
            }

            mCurrentFrame = selectedFrame - 1;

            scheduleNewRefreshJob();
        }

// update minimap view
mDurationMinimap.setCallRangeForCurrentFrame(mCallStartIndex, mCallEndIndex);
}

/**
return;
}

        synchronized (mTreeRefresherLock) {
            mCurrentlyDisplayedContext = context;
            scheduleNewRefreshJob();
        }
    }

    private void scheduleNewRefreshJob() {
        if (mTreeRefresherJob != null) {
            return;
        }

        mTreeRefresherJob = new Job("Refresh GL Trace View Tree") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                final int start, end, context;

                synchronized (mTreeRefresherLock) {
                    start = mCallStartIndex;
                    end = mCallEndIndex;
                    context = mCurrentlyDisplayedContext;

                    mTreeRefresherJob = null;
                }

                // update tree view in the editor
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        refreshTree(start, end, context);

                        // update the frame summary view
                        if (mFrameSummaryViewPage != null) {
                            mFrameSummaryViewPage.setSelectedFrame(mCurrentFrame);
                        }
                    }
                });
                return Status.OK_STATUS;
            }
        };
        mTreeRefresherJob.setPriority(Job.SHORT);
        mTreeRefresherJob.schedule(TREE_REFRESH_INTERVAL_MS);
}

private void refreshTree(int startCallIndex, int endCallIndex, int contextToDisplay) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public class FrameSummaryViewPage extends Page {
private GLTrace mTrace;

    private final Object mLock = new Object();
    private Job mRefresherJob;
private int mCurrentFrame;

private SashForm mSash;
}

public void setSelectedFrame(int frame) {
if (mTrace == null) {
return;
}

        synchronized (mLock) {
            mCurrentFrame = frame;

            if (mRefresherJob != null) {
                return;
            }

            mRefresherJob = new Job("Update Frame Summary Task") {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    final int currentFrame;
                    synchronized (mLock) {
                        currentFrame = mCurrentFrame;
                        mRefresherJob = null;
                    };

                    updateImageCanvas(currentFrame);
                    updateFrameStats(currentFrame);

                    return Status.OK_STATUS;
                }
            };
            mRefresherJob.setPriority(Job.SHORT);
            mRefresherJob.schedule(500);
        };
}

    private void updateFrameStats(int frame) {
        final List<GLCall> calls = mTrace.getGLCallsForFrame(frame);

Job job = new Job("Update Frame Statistics") {
@Override
return String.format("%.2f ms", milliSeconds);          //$NON-NLS-1$
}

    private void updateImageCanvas(int frame) {
        int lastCallIndex = mTrace.getFrame(frame).getEndIndex() - 1;
if (lastCallIndex >= 0 && lastCallIndex < mTrace.getGLCalls().size()) {
GLCall call = mTrace.getGLCalls().get(lastCallIndex);
final Image image = mTrace.getImage(call);

//<End of snippet n. 1>








