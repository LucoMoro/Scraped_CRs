/*onSavePassword dialog can leak when WebViewClassic is destroyed.

The AlertDialog creates in onSavePassword method leaks if
WebViewClassic is destroyed when the dialog is shown.

Change-Id:I81f20e1dd138467a6413766c0a081b389b334ae0*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index 84a6129..34c08ef 100644

//Synthetic comment -- @@ -686,6 +686,10 @@
// It's used to dismiss the dialog in destroy if not done before.
private AlertDialog mListBoxDialog = null;

static final String LOGTAG = "webview";

private ZoomManager mZoomManager;
//Synthetic comment -- @@ -1811,7 +1815,7 @@
neverRemember.getData().putString("password", password);
neverRemember.obj = resumeMsg;

            new AlertDialog.Builder(mContext)
.setTitle(com.android.internal.R.string.save_password_label)
.setMessage(com.android.internal.R.string.save_password_message)
.setPositiveButton(com.android.internal.R.string.save_password_notnow,
//Synthetic comment -- @@ -1822,6 +1826,7 @@
resumeMsg.sendToTarget();
mResumeMsg = null;
}
}
})
.setNeutralButton(com.android.internal.R.string.save_password_remember,
//Synthetic comment -- @@ -1832,6 +1837,7 @@
remember.sendToTarget();
mResumeMsg = null;
}
}
})
.setNegativeButton(com.android.internal.R.string.save_password_never,
//Synthetic comment -- @@ -1842,6 +1848,7 @@
neverRemember.sendToTarget();
mResumeMsg = null;
}
}
})
.setOnCancelListener(new OnCancelListener() {
//Synthetic comment -- @@ -1851,6 +1858,7 @@
resumeMsg.sendToTarget();
mResumeMsg = null;
}
}
}).show();
// Return true so that WebViewCore will pause while the dialog is
//Synthetic comment -- @@ -2090,6 +2098,10 @@
mListBoxDialog.dismiss();
mListBoxDialog = null;
}
if (mWebViewCore != null) {
// Tell WebViewCore to destroy itself
synchronized (this) {







