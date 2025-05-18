//<Beginning of snippet n. 0>
case KeyEvent.KEYCODE_RINGER_ON:
    if (!isRingerOn) {
        enableRingerMode();
        updateVolumeSettings();
        isRingerOn = true;
        logger.info("Ringer is now ON."); // Use structured logging
        return true;
    } else {
        logger.info("Ringer is already ON."); // Use structured logging
    }
    return false;

case KeyEvent.KEYCODE_RINGER_OFF:
    if (isRingerOn) {
        muteVolume();
        isRingerOn = false;
        logger.info("Ringer is now OFF."); // Use structured logging
        return true;
    } else {
        logger.info("Ringer is already OFF."); // Use structured logging
    }
    return false;

case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
//<End of snippet n. 0>