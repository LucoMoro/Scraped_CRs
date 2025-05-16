//<Beginning of snippet n. 0>


static private final boolean AUTO_REDRAW_HACK = false;
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;

static final String LOGTAG = "webview";

public void destroy() {
    clearTextEntry(false);
    if (mWebViewCore != null) {
        // Set the handlers to null before destroying WebViewCore so no
        // more messages will be posted.
        mWebViewCore = null;
    }
    final AlertDialog dialog = b.create();
    listView.setAdapter(adapter);
    listView.setFocusableInTouchMode(true);
    
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
            if (mWebViewCore != null) {
                mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
            }
            dialog.dismiss();
        }
    });
    
    if (mSelection != -1) {
        adapter.registerDataSetObserver(observer);
    }
    
    dialog.show();
    
    if (dialog.isShowing()) {
        dialog.dismiss();
    }
}


//<End of snippet n. 0>