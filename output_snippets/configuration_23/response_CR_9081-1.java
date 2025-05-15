//<Beginning of snippet n. 0>
case KeyEvent.KEYCODE_HEADSETHOOK: 
    toggleRingerState();
    return true;

case KeyEvent.KEYCODE_PLAYPAUSE: 
    toggleRingerState();
    return true;

// Method to toggle the ringer state
private void toggleRingerState() {
    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    if (audioManager != null) {
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
return true;
//<End of snippet n. 0>