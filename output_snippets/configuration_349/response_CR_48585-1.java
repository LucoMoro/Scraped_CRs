//<Beginning of snippet n. 0>
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        || keyCode == KeyEvent.KEYCODE_VOLUME_UP
        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
        || keyCode == KeyEvent.KEYCODE_CAMERA) {
    return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
    // Hide the controller bar immediately upon exiting full-screen mode
    hideControllerBar();
    return super.dispatchKeyEvent(event);
} else {
    // Explicitly manage the full-screen progress bar visibility
    if (isFullScreen()) {
        showFullScreenProgressBar();
    }
}
//<End of snippet n. 0>