/*Removed Calls to deprecated API

Change-Id:Ia94734637df6aa7b8b08417c49a3485f34f1b94c*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java b/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.java
//Synthetic comment -- index 0ef79b3..f5e8f66 100644

//Synthetic comment -- @@ -135,13 +135,13 @@
mProgressDialog.setTitle(R.string.select_dialog);
mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
mProgressDialog.setMax(MAX_PROGRESS);
            mProgressDialog.setButton(getText(R.string.alert_dialog_hide), new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked Yes so do some stuff */
}
});
            mProgressDialog.setButton2(getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

/* User clicked No so do some stuff */







