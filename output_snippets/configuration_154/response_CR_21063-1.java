//<Beginning of snippet n. 0>
@Override 
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (mContextMenu != null && mContextMenu.isShowing()) {
        mContextMenu.dismiss();
    }

    boolean visible = mWindowVisible;
    int showFlags = mShowInputFlags;
    boolean showingInput = mShowInputRequested;

    if (mExtractEditText != null) {
        // Handle your ExtractEditText logic here
    }
}
//<End of snippet n. 0>