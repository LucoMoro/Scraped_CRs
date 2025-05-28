```
<<Beginning of snippet n. 0>>
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
|| keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
|| keyCode == KeyEvent.KEYCODE_CAMERA) {
    return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
    hideControllerBar();
    if (isInFullScreenMode()) {
        // Call the showProgressBar() here for consistency
        showProgressBar();
    }
    return true;
} else if (isInFullScreenMode()) {
    showProgressBar();
    return super.dispatchKeyEvent(event);
} else {
    return false;
}
<<End of snippet n. 0>>