
//<Beginning of snippet n. 0>


@Override public void onConfigurationChanged(Configuration newConfig) {
super.onConfigurationChanged(newConfig);

        mWindow.getWindow().closePanel(Window.FEATURE_CONTEXT_MENU);

boolean visible = mWindowVisible;
int showFlags = mShowInputFlags;
boolean showingInput = mShowInputRequested;

//<End of snippet n. 0>








