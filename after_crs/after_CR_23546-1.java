/*Avoid memory leak in the Shutdown confirmation dialog.

How to reproduce:
1) Lock the screen.
2) Open the Phone options menu by long pressing the power
   button.
3) Tap "Power off" to display the confirmation dialog.
4) Repeat step 2 and 3 a few times (without closing the
   confirmation dialog.

Each time the confirmation dialog is displayed, a new
instance is created. A stack of confirmation dialogs are
created on the screen.

This is fixed by making sure the previous dialog is
dismissed before launching a new dialog.

Change-Id:I6b6c61ccc56364b66eed3528019f761e75bbe268*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ShutdownThread.java b/core/java/com/android/internal/app/ShutdownThread.java
//Synthetic comment -- index cd1b569..7b3503f 100644

//Synthetic comment -- @@ -74,6 +74,8 @@
private PowerManager.WakeLock mCpuWakeLock;
private PowerManager.WakeLock mScreenWakeLock;
private Handler mHandler;

    private static AlertDialog sConfirmDialog;

private ShutdownThread() {
}
//Synthetic comment -- @@ -99,7 +101,10 @@
Log.d(TAG, "Notifying thread to start radio shutdown");

if (confirm) {
            if (sConfirmDialog != null) {
                sConfirmDialog.dismiss();
            }
            sConfirmDialog = new AlertDialog.Builder(context)
.setIcon(android.R.drawable.ic_dialog_alert)
.setTitle(com.android.internal.R.string.power_off)
.setMessage(com.android.internal.R.string.shutdown_confirm)
//Synthetic comment -- @@ -110,12 +115,12 @@
})
.setNegativeButton(com.android.internal.R.string.no, null)
.create();
            sConfirmDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
if (!context.getResources().getBoolean(
com.android.internal.R.bool.config_sf_slowBlur)) {
                sConfirmDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
}
            sConfirmDialog.show();
} else {
beginShutdownSequence(context);
}







