//<Beginning of snippet n. 0>
@Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (isContextMenuVisible()) {
        dismissContextMenu();
    }

    boolean visible = mWindowVisible;
    int showFlags = mShowInputFlags;
    boolean showingInput = mShowInputRequested;

    // Additional logic to handle configuration changes can be added here
}
//<End of snippet n. 0>