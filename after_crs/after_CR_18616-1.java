/*onSavePassword dialog can leak when WebView is destroyed.

The AlertDialog creates in onSavePassword method leaks if
WebView is destroyed when the dialog is shown.

Change-Id:I81f20e1dd138467a6413766c0a081b389b334ae0*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 0ea89a8..eaeacd4 100644

//Synthetic comment -- @@ -311,6 +311,9 @@
// Reference to the AlertDialog displayed by InvokeListBox.
// It's used to dismiss the dialog in destroy if not done before.
private AlertDialog mListBoxDialog = null;
    // reference to save password dialog so it can be dimissed if WebView
    // is destroyed when the dialog is open.
    private AlertDialog mSavePasswordDialog = null;

static final String LOGTAG = "webview";

//Synthetic comment -- @@ -1047,30 +1050,34 @@
neverRemember.getData().putString("password", password);
neverRemember.obj = resumeMsg;

            mSavePasswordDialog = new AlertDialog.Builder(getContext())
.setTitle(com.android.internal.R.string.save_password_label)
.setMessage(com.android.internal.R.string.save_password_message)
.setPositiveButton(com.android.internal.R.string.save_password_notnow,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
resumeMsg.sendToTarget();
                            mSavePasswordDialog = null;
}
})
.setNeutralButton(com.android.internal.R.string.save_password_remember,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
remember.sendToTarget();
                            mSavePasswordDialog = null;
}
})
.setNegativeButton(com.android.internal.R.string.save_password_never,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
neverRemember.sendToTarget();
                            mSavePasswordDialog = null;
}
})
.setOnCancelListener(new OnCancelListener() {
public void onCancel(DialogInterface dialog) {
resumeMsg.sendToTarget();
                            mSavePasswordDialog = null;
}
}).show();
// Return true so that WebViewCore will pause while the dialog is
//Synthetic comment -- @@ -1240,6 +1247,10 @@
mListBoxDialog.dismiss();
mListBoxDialog = null;
}
        if(mSavePasswordDialog != null) {
            mSavePasswordDialog.dismiss();
            mSavePasswordDialog = null;
        }
if (mWebViewCore != null) {
// Set the handlers to null before destroying WebViewCore so no
// more messages will be posted.







