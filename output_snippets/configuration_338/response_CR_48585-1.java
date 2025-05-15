//<Beginning of snippet n. 0>
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        || keyCode == KeyEvent.KEYCODE_VOLUME_UP
        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
        || keyCode == KeyEvent.KEYCODE_CAMERA) {
    // don't show the controls for volume adjustment
    return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
    if (isFullScreen()) {
        hideControllerBar();
        exitFullScreen();
        return true; // Consume the event
    }
//<End of snippet n. 0>