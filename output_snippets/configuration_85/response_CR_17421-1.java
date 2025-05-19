//<Beginning of snippet n. 0>

static private final boolean AUTO_REDRAW_HACK = false;
private boolean mAutoRedraw;
static final String LOGTAG = "webview";

public void destroy() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        new Handler(Looper.getMainLooper()).post(this::destroy);
        return;
    }
    if (dialog != null) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
    if (mWebViewCore != null) {
        mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
    }
}

//<End of snippet n. 0>