/*Dismissing AlertDialog before destroying WebView.

Added dismiss method for the Alertdialog in destroy to avoid
a leaked window.

Change-Id:Ia6a6e733b8bdd583dae15b854e4d69ef4f5cbff1*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 8e363d6..0ea89a8 100644

//Synthetic comment -- @@ -308,6 +308,10 @@
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;

    // Reference to the AlertDialog displayed by InvokeListBox.
    // It's used to dismiss the dialog in destroy if not done before.
    private AlertDialog mListBoxDialog = null;

static final String LOGTAG = "webview";

private static class ExtendedZoomControls extends FrameLayout {
//Synthetic comment -- @@ -1232,6 +1236,10 @@
*/
public void destroy() {
clearTextEntry(false);
        if (mListBoxDialog != null) {
            mListBoxDialog.dismiss();
            mListBoxDialog = null;
        }
if (mWebViewCore != null) {
// Set the handlers to null before destroying WebViewCore so no
// more messages will be posted.
//Synthetic comment -- @@ -7072,7 +7080,7 @@
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
}});
}
            mListBoxDialog = b.create();
listView.setAdapter(adapter);
listView.setFocusableInTouchMode(true);
// There is a bug (1250103) where the checks in a ListView with
//Synthetic comment -- @@ -7094,7 +7102,8 @@
int position, long id) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, (int)id, 0);
                        mListBoxDialog.dismiss();
                        mListBoxDialog = null;
}
});
if (mSelection != -1) {
//Synthetic comment -- @@ -7106,13 +7115,14 @@
adapter.registerDataSetObserver(observer);
}
}
            mListBoxDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
public void onCancel(DialogInterface dialog) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
                    mListBoxDialog = null;
}
});
            mListBoxDialog.show();
}
}








