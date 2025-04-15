/*Dismissing AlertDialog before destroying WebView.

Added dismiss method for the Alertdialog in destroy to avoid
a leaked window.

Change-Id:Ia6a6e733b8bdd583dae15b854e4d69ef4f5cbff1*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 8e363d6..751e13f 100644

//Synthetic comment -- @@ -307,6 +307,8 @@
static private final boolean AUTO_REDRAW_HACK = false;
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;

static final String LOGTAG = "webview";

//Synthetic comment -- @@ -1232,6 +1234,11 @@
*/
public void destroy() {
clearTextEntry(false);
if (mWebViewCore != null) {
// Set the handlers to null before destroying WebViewCore so no
// more messages will be posted.
//Synthetic comment -- @@ -7072,7 +7079,7 @@
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
}});
}
            final AlertDialog dialog = b.create();
listView.setAdapter(adapter);
listView.setFocusableInTouchMode(true);
// There is a bug (1250103) where the checks in a ListView with
//Synthetic comment -- @@ -7094,7 +7101,8 @@
int position, long id) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, (int)id, 0);
                        dialog.dismiss();
}
});
if (mSelection != -1) {
//Synthetic comment -- @@ -7106,13 +7114,13 @@
adapter.registerDataSetObserver(observer);
}
}
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
public void onCancel(DialogInterface dialog) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
}
});
            dialog.show();
}
}








