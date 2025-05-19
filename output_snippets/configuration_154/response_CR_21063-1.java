<<Beginning of snippet n. 0>>
@Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (mContextMenu != null && mContextMenu.isShowing()) {
        mContextMenu.dismiss();
    }
    
    mShowInputFlags = checkUpdatedInputFlags();
    mShowInputRequested = checkIfInputRequested();
}
//<End of snippet n. 0>>