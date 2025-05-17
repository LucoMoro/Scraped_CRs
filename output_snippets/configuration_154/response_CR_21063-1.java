//<Beginning of snippet n. 0>
@Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    
    dismissContextMenu(); // Unconditionally dismiss context menu

    mWindowVisible = true; // Update state variables
    mShowInputFlags = showFlags; // Update with new flags if necessary
    mShowInputRequested = showingInput; // Update requested input state
}
//<End of snippet n. 0>