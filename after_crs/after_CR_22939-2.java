/*SdkMan2: Use IDE's log when available.

Change-Id:Iede7f4f9c1a7bbfbdcbb096f489616478ba94465*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index ee81897..8f256e9 100755

//Synthetic comment -- @@ -395,7 +395,8 @@
private boolean postCreateContent() {
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
                mStatusText, mProgressBar, mButtonStop,
                mContext == InvocationContext.IDE ? mUpdaterData.getSdkLog() : null));
mUpdaterData.setTaskFactory(factory);

setWindowImage(mShell);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index 9878c85..052343e 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;

//Synthetic comment -- @@ -61,14 +62,29 @@
*/
private final StringBuilder mLogText = new StringBuilder();

    private final ISdkLog mLog;


/**
* Creates a new {@link ProgressView} object, a simple "holder" for the various
* widgets used to display and update a progress + status bar.
     *
     * @param label The label to display titles of status updates (e.g. task titles and
     *      calls to {@link #setDescription(String)}.)
     * @param progressBar The progress bar to update during a task.
     * @param stopButton The stop button. It will be disabled when there's no task that can
     *      be interrupted. A selection listener will be attached to it.
     * @param log An <em>optional</em> logger object that will be used to report all the log.
     *      If null, all logging will be collected here with a little UI to display it.
*/
    public ProgressView(
            Label label,
            ProgressBar progressBar,
            Control stopButton,
            ISdkLog log) {
mLabel = label;
mProgressBar = progressBar;
        mLog = log;
mProgressBar.setEnabled(false);

mStopButton = stopButton;
//Synthetic comment -- @@ -156,8 +172,12 @@
mLabel.setText(description);
}
});
        if (mLog != null) {
            mLog.printf("%1$s", description);
        } else {
            synchronized (mLogText) {
                mLogText.append(description);
            }
}
}

//Synthetic comment -- @@ -166,8 +186,12 @@
* This method can be invoked from a non-UI thread.
*/
public void log(String log) {
        if (mLog != null) {
            mLog.printf("  %1$s", log);
        } else {
            synchronized (mLogText) {
                mLogText.append(" ").append(log);
            }
}
}

//Synthetic comment -- @@ -176,8 +200,12 @@
* This method can be invoked from a non-UI thread.
*/
public void logError(String log) {
        if (mLog != null) {
            mLog.error(null, "  %1$s", log);
        } else {
            synchronized (mLogText) {
                mLogText.append("ERROR: ").append(log);
            }
}
}

//Synthetic comment -- @@ -187,8 +215,12 @@
* This method can be invoked from a non-UI thread.
*/
public void logVerbose(String log) {
        if (mLog != null) {
            mLog.printf("    %1$s", log);
        } else {
            synchronized (mLogText) {
                mLogText.append("  ").append(log);
            }
}
}








