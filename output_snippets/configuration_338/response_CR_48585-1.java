//<Beginning of snippet n. 0>
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        || keyCode == KeyEvent.KEYCODE_VOLUME_UP
        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
        || keyCode == KeyEvent.KEYCODE_CAMERA) {
    return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
    if (isFullScreen) {
        exitFullScreen();
        controllerBar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        return true;
    }
//<End of snippet n. 0>