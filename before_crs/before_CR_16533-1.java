/*Allow reports to be automatically submitted when "Force Close" is hit.

This is a useful setting for pre-production builds.

Also make ApplicationErrorReport's ctor public to allow the
easy reconstruction of a report outside the framework.

Change-Id:Ic4d8767db67dc924ca281a474e18edabedbc14d0*/
//Synthetic comment -- diff --git a/core/java/android/app/ApplicationErrorReport.java b/core/java/android/app/ApplicationErrorReport.java
//Synthetic comment -- index ec39ac7..b638d90 100644

//Synthetic comment -- @@ -134,7 +134,7 @@
* Create an instance of {@link ApplicationErrorReport} initialized from
* a parcel.
*/
    ApplicationErrorReport(Parcel in) {
readFromParcel(in);
}









//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 7b0e560..721454c 100644

//Synthetic comment -- @@ -2903,6 +2903,13 @@
public static final String SEND_ACTION_APP_ERROR = "send_action_app_error";

/**
* Nonzero causes Log.wtf() to crash.
* @hide
*/








//Synthetic comment -- diff --git a/services/java/com/android/server/am/AppErrorDialog.java b/services/java/com/android/server/am/AppErrorDialog.java
//Synthetic comment -- index 3a1aad6..4cae809 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;

class AppErrorDialog extends BaseErrorDialog {
//Synthetic comment -- @@ -60,11 +61,16 @@

setCancelable(false);

setButton(DialogInterface.BUTTON_POSITIVE,
res.getText(com.android.internal.R.string.force_close),
                mHandler.obtainMessage(FORCE_QUIT));

        if (app.errorReportReceiver != null) {
setButton(DialogInterface.BUTTON_NEGATIVE,
res.getText(com.android.internal.R.string.report),
mHandler.obtainMessage(FORCE_QUIT_AND_REPORT));







