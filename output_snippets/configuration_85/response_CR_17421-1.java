//<Beginning of snippet n. 0>

static private final boolean AUTO_REDRAW_HACK = false;
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;

static final String LOGTAG = "webview";

public void destroy() {
    clearTextEntry(false);
    if (mWebViewCore != null) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        // Set the handlers to null before destroying WebViewCore so no
        // more messages will be posted.
        mWebViewCore.destroy();
        mWebViewCore = null;
    }
}

// Assuming other code relevant to dialog creation is here
final AlertDialog dialog = b.create();
listView.setAdapter(adapter);
listView.setFocusableInTouchMode(true);
// There is a bug (1250103) where the checks in a ListView with
int position, long id) {
    mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, (int)id, 0);
}

if (mSelection != -1) {
    adapter.registerDataSetObserver(observer);
}

dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
    public void onCancel(DialogInterface dialog) {
        mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
    }
});

dialog.show();

//<End of snippet n. 0>