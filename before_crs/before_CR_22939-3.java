/*SdkMan2: Use IDE's log when available.

Change-Id:Iede7f4f9c1a7bbfbdcbb096f489616478ba94465*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index ee81897..8f256e9 100755

//Synthetic comment -- @@ -395,7 +395,8 @@
private boolean postCreateContent() {
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
                mStatusText, mProgressBar, mButtonStop));
mUpdaterData.setTaskFactory(factory);

setWindowImage(mShell);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index c5727e0..3352f9f 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;

//Synthetic comment -- @@ -61,18 +62,29 @@
*/
private final StringBuilder mLogText = new StringBuilder();


/**
* Creates a new {@link ProgressView} object, a simple "holder" for the various
* widgets used to display and update a progress + status bar.
*
     * @param label The label of the current operation. Must not be null.
     * @param progressBar The progress bar showing the current progress. Must not be null.
     * @param stopButton The stop button. Optional. Can be null.
*/
    public ProgressView(Label label, ProgressBar progressBar, Control stopButton) {
mLabel = label;
mProgressBar = progressBar;
mProgressBar.setEnabled(false);

mStopButton = stopButton;
//Synthetic comment -- @@ -162,8 +174,12 @@
mLabel.setText(description);
}
});
        synchronized (mLogText) {
            mLogText.append("** ").append(description);
}
}

//Synthetic comment -- @@ -172,8 +188,12 @@
* This method can be invoked from a non-UI thread.
*/
public void log(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}

//Synthetic comment -- @@ -182,8 +202,12 @@
* This method can be invoked from a non-UI thread.
*/
public void logError(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}

//Synthetic comment -- @@ -193,8 +217,12 @@
* This method can be invoked from a non-UI thread.
*/
public void logVerbose(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}








