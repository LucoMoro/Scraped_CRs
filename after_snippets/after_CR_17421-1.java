
//<Beginning of snippet n. 0>


static private final boolean AUTO_REDRAW_HACK = false;
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;
    // AlertDialog instance, used to dismiss the dialog.
    private AlertDialog mDialog = null;

static final String LOGTAG = "webview";

*/
public void destroy() {
clearTextEntry(false);
        // Dismiss the alertdialog created in InvokeListBox
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
if (mWebViewCore != null) {
// Set the handlers to null before destroying WebViewCore so no
// more messages will be posted.
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
}});
}
            mDialog = b.create();
listView.setAdapter(adapter);
listView.setFocusableInTouchMode(true);
// There is a bug (1250103) where the checks in a ListView with
int position, long id) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, (int)id, 0);
                        mDialog.dismiss();
                        mDialog = null;
}
});
if (mSelection != -1) {
adapter.registerDataSetObserver(observer);
}
}
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
public void onCancel(DialogInterface dialog) {
mWebViewCore.sendMessage(
EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
}
});
            mDialog.show();
}
}


//<End of snippet n. 0>








