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
//Synthetic comment -- index 77d0c97..96a6428 100644

//Synthetic comment -- @@ -76,6 +76,8 @@
private PowerManager.WakeLock mCpuWakeLock;
private PowerManager.WakeLock mScreenWakeLock;
private Handler mHandler;

private ShutdownThread() {
}
//Synthetic comment -- @@ -108,7 +110,10 @@

if (confirm) {
final CloseDialogReceiver closer = new CloseDialogReceiver(context);
            final AlertDialog dialog = new AlertDialog.Builder(context)
.setTitle(com.android.internal.R.string.power_off)
.setMessage(resourceId)
.setPositiveButton(com.android.internal.R.string.yes, new DialogInterface.OnClickListener() {
//Synthetic comment -- @@ -118,10 +123,10 @@
})
.setNegativeButton(com.android.internal.R.string.no, null)
.create();
            closer.dialog = dialog;
            dialog.setOnDismissListener(closer);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
            dialog.show();
} else {
beginShutdownSequence(context);
}







