//<Beginning of snippet n. 0>
case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
    // Logic for handling playback buttons
    break;

case KeyEvent.KEYCODE_RINGER_ON:
    if (isDeviceInSilentMode()) {
        setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        return true;
    }
    break;

case KeyEvent.KEYCODE_RINGER_OFF:
    if (isDeviceInNormalMode()) {
        setRingerMode(AudioManager.RINGER_MODE_SILENT);
        return true;
    }
    break;

return true;
//<End of snippet n. 0>