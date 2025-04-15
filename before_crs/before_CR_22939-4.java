/*SdkMan2: Use IDE's log when available.

Change-Id:Iede7f4f9c1a7bbfbdcbb096f489616478ba94465*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
//Synthetic comment -- index 1683808..71ce0ad 100644

//Synthetic comment -- @@ -28,24 +28,57 @@

public void error(Throwable t, String errorFormat, Object... args) {
if (errorFormat != null) {
            System.err.printf("Error: " + errorFormat, args);
            if (!errorFormat.endsWith("\n")) {
                System.err.printf("\n");
}
}
if (t != null) {
            System.err.printf("Error: %s\n", t.getMessage());
}
}

public void warning(String warningFormat, Object... args) {
        System.out.printf("Warning: " + warningFormat, args);
        if (!warningFormat.endsWith("\n")) {
            System.out.printf("\n");
}
}

public void printf(String msgFormat, Object... args) {
        System.out.printf(msgFormat, args);
}
}








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
//Synthetic comment -- index c5727e0..52f0b20 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;

//Synthetic comment -- @@ -49,6 +51,8 @@
/** The current mode of operation of the dialog. */
private State mState = State.IDLE;

// UI fields
private final Label mLabel;
private final Control mStopButton;
//Synthetic comment -- @@ -61,18 +65,31 @@
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
//Synthetic comment -- @@ -162,8 +179,15 @@
mLabel.setText(description);
}
});
        synchronized (mLogText) {
            mLogText.append("** ").append(description);
}
}

//Synthetic comment -- @@ -172,8 +196,14 @@
* This method can be invoked from a non-UI thread.
*/
public void log(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}

//Synthetic comment -- @@ -182,8 +212,14 @@
* This method can be invoked from a non-UI thread.
*/
public void logError(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}

//Synthetic comment -- @@ -193,8 +229,14 @@
* This method can be invoked from a non-UI thread.
*/
public void logVerbose(String log) {
        synchronized (mLogText) {
            mLogText.append("=> ").append(log);
}
}

//Synthetic comment -- @@ -261,4 +303,29 @@

return result[0];
}
}







