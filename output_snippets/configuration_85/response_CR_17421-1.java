//<Beginning of snippet n. 0>

static private final boolean AUTO_REDRAW_HACK = false;
private boolean mAutoRedraw;
private AlertDialog dialog;

static final String LOGTAG = "webview";

public void destroy() {
    clearTextEntry(false);
    if (dialog != null) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null; // Reset dialog after dismissal
                }
            }
        });
    }
    if (mWebViewCore != null) {
        mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
    }
}

private void showDialog() {
    AlertDialog.Builder b = new AlertDialog.Builder(context); // assuming context is defined
    ListView listView = new ListView(context); // assuming context is defined
    // Setup adapter and other listView configurations...

    dialog = b.create(); // Assign to class-level dialog
    listView.setAdapter(adapter);
    listView.setFocusableInTouchMode(true);
    
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, (int) id, 0);
            if (dialog != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null; // Reset dialog after dismissal
                        }
                    }
                });
            }
        }
    });

    if (mSelection != -1) {
        adapter.registerDataSetObserver(observer);
    }

    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
            mWebViewCore.sendMessage(EventHub.SINGLE_LISTBOX_CHOICE, -2, 0);
        }
    });

    dialog.show();
}

//<End of snippet n. 0>