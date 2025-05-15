//<Beginning of snippet n. 0>

static private final boolean AUTO_REDRAW_HACK = false;
// true means redraw the screen all-the-time. Only with AUTO_REDRAW_HACK
private boolean mAutoRedraw;

static final String LOGTAG = "webview";

public void destroy() {
    clearTextEntry(false);
    if (dialog != null && dialog.isShowing()) {
        dialog.dismiss();
    }
    if (mWebViewCore != null) {
        // Set the handlers to null before destroying WebViewCore so no
        // more messages will be posted.
        mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
    }
}

//<End of snippet n. 0>