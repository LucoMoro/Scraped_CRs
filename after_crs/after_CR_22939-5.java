/*SdkMan2: Use IDE's log when available.

Change-Id:Iede7f4f9c1a7bbfbdcbb096f489616478ba94465*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
//Synthetic comment -- index 1683808..71ce0ad 100644

//Synthetic comment -- @@ -28,24 +28,57 @@

public void error(Throwable t, String errorFormat, Object... args) {
if (errorFormat != null) {
            String msg = String.format("Error: " + errorFormat, args);

            if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                    !msg.endsWith("\r\n") &&
                    msg.endsWith("\n")) {
                // remove last \n so that println can use \r\n as needed.
                msg = msg.substring(0, msg.length() - 1);
            }

            System.err.print(msg);

            if (!msg.endsWith("\n")) {
                System.err.println();
}
}
if (t != null) {
            System.err.println(String.format("Error: %1$s%2$s", t.getMessage()));
}
}

public void warning(String warningFormat, Object... args) {
        String msg = String.format("Warning: " + warningFormat, args);

        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        System.out.print(msg);

        if (!msg.endsWith("\n")) {
            System.out.println();
}
}

public void printf(String msgFormat, Object... args) {
        String msg = String.format(msgFormat, args);

        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS &&
                !msg.endsWith("\r\n") &&
                msg.endsWith("\n")) {
            // remove last \n so that println can use \r\n as needed.
            msg = msg.substring(0, msg.length() - 1);
        }

        System.out.print(msg);

        if (!msg.endsWith("\n")) {
            System.out.println();
        }
}
}








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
//Synthetic comment -- index c5727e0..f8ec4ff 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;

//Synthetic comment -- @@ -49,6 +50,8 @@
/** The current mode of operation of the dialog. */
private State mState = State.IDLE;



// UI fields
private final Label mLabel;
private final Control mStopButton;
//Synthetic comment -- @@ -61,18 +64,31 @@
*/
private final StringBuilder mLogText = new StringBuilder();

    /** Logger object. Can be null. */
    private final ISdkLog mLog;

    private String mLastLogMsg = null;

/**
* Creates a new {@link ProgressView} object, a simple "holder" for the various
* widgets used to display and update a progress + status bar.
*
     * @param label The label to display titles of status updates (e.g. task titles and
     *      calls to {@link #setDescription(String)}.) Must not be null.
     * @param progressBar The progress bar to update during a task. Must not be null.
     * @param stopButton The stop button. It will be disabled when there's no task that can
     *      be interrupted. A selection listener will be attached to it. Optional. Can be null.
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
//Synthetic comment -- @@ -162,8 +178,15 @@
mLabel.setText(description);
}
});

        if (acceptLog(description)) {
            if (mLog != null) {
                mLog.printf("%1$s", description);
            } else {
                synchronized (mLogText) {
                    mLogText.append(description);
                }
            }
}
}

//Synthetic comment -- @@ -172,8 +195,14 @@
* This method can be invoked from a non-UI thread.
*/
public void log(String log) {
        if (acceptLog(log)) {
            if (mLog != null) {
                mLog.printf("  %1$s", log);
            } else {
                synchronized (mLogText) {
                    mLogText.append(" ").append(log);
                }
            }
}
}

//Synthetic comment -- @@ -182,8 +211,14 @@
* This method can be invoked from a non-UI thread.
*/
public void logError(String log) {
        if (acceptLog(log)) {
            if (mLog != null) {
                mLog.error(null, "  %1$s", log);
            } else {
                synchronized (mLogText) {
                    mLogText.append("ERROR: ").append(log);
                }
            }
}
}

//Synthetic comment -- @@ -193,8 +228,14 @@
* This method can be invoked from a non-UI thread.
*/
public void logVerbose(String log) {
        if (acceptLog(log)) {
            if (mLog != null) {
                mLog.printf("    %1$s", log);
            } else {
                synchronized (mLogText) {
                    mLogText.append("  ").append(log);
                }
            }
}
}

//Synthetic comment -- @@ -261,4 +302,32 @@

return result[0];
}

    // ----

    /**
     * Filter messages displayed in the log: <br/>
     * - Messages with a % are typical part of a progress update and shouldn't be in the log. <br/>
     * - Messages that are the same as the same output message should be output a second time.
     *
     * @param msg The potential log line to print.
     * @return True if the log line should be printed, false otherwise.
     */
    private boolean acceptLog(String msg) {
        if (msg == null) {
            return false;
        }

        msg = msg.trim();
        if (msg.indexOf('%') != -1) {
            return false;
        }

        if (msg.equals(mLastLogMsg)) {
            return false;
        }

        mLastLogMsg = msg;
        return true;
    }
}







