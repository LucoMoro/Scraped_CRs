/*onSavePassword dialog can leak when WebView is destroyed.

The AlertDialog creates in onSavePassword method leaks if
WebView is destroyed when the dialog is shown.

Change-Id:I81f20e1dd138467a6413766c0a081b389b334ae0*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 24eebd7..0a4de9f 100644

//Synthetic comment -- @@ -363,6 +363,10 @@
// It's used to dismiss the dialog in destroy if not done before.
private AlertDialog mListBoxDialog = null;

static final String LOGTAG = "webview";

private ZoomManager mZoomManager;
//Synthetic comment -- @@ -1399,30 +1403,34 @@
neverRemember.getData().putString("password", password);
neverRemember.obj = resumeMsg;

            new AlertDialog.Builder(getContext())
.setTitle(com.android.internal.R.string.save_password_label)
.setMessage(com.android.internal.R.string.save_password_message)
.setPositiveButton(com.android.internal.R.string.save_password_notnow,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
resumeMsg.sendToTarget();
}
})
.setNeutralButton(com.android.internal.R.string.save_password_remember,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
remember.sendToTarget();
}
})
.setNegativeButton(com.android.internal.R.string.save_password_never,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
neverRemember.sendToTarget();
}
})
.setOnCancelListener(new OnCancelListener() {
public void onCancel(DialogInterface dialog) {
resumeMsg.sendToTarget();
}
}).show();
// Return true so that WebViewCore will pause while the dialog is
//Synthetic comment -- @@ -1661,6 +1669,10 @@
mListBoxDialog.dismiss();
mListBoxDialog = null;
}
// remove so that it doesn't cause events
if (mWebTextView != null) {
mWebTextView.remove();







