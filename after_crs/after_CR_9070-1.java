/*Implementation for RINGER_ON & RINGER_OFF keys, with a default behaviour.*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindow.java b/phone/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index 80a2120..db9ae44 100644

//Synthetic comment -- @@ -1135,6 +1135,29 @@
}
return true;
}
            case KeyEvent.KEYCODE_RINGER_ON:
            case KeyEvent.KEYCODE_RINGER_OFF: {
                AudioManager audioManager = (AudioManager) getContext().getSystemService(
                        Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    if (keyCode == KeyEvent.KEYCODE_RINGER_ON) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    } else {
                        // keyCode == KeyEvent.KEYCODE_RINGER_OFF
                        int vibSetting = audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
                        int ringerMode = AudioManager.RINGER_MODE_SILENT;
                        if (vibSetting == AudioManager.VIBRATE_SETTING_ON || vibSetting == AudioManager.VIBRATE_SETTING_ONLY_SILENT)
                            ringerMode = AudioManager.RINGER_MODE_VIBRATE;

                        audioManager.setRingerMode(ringerMode);
                    }
                    audioManager.adjustSuggestedStreamVolume(
                            AudioManager.ADJUST_SAME,
                            AudioManager.STREAM_RING,
                            AudioManager.FLAG_SHOW_UI);
                }
                return true;
            }

case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 







