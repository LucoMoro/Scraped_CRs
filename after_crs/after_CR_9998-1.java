/*Allow video ringtone for default ringtone, but not for notifications.*/




//Synthetic comment -- diff --git a/src/com/android/settings/DefaultRingtonePreference.java b/src/com/android/settings/DefaultRingtonePreference.java
//Synthetic comment -- index 8eed563..52e602e 100644

//Synthetic comment -- @@ -47,6 +47,14 @@
* Similarly, 'Silent' shouldn't be shown here. 
*/
ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);

        /*
         * It is fine for the default ringtone to be a video, but not notifications. 
         */
        if (getRingtoneType() == RingtoneManager.TYPE_RINGTONE) {
            ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                                          RingtoneManager.TYPE_RINGTONE|RingtoneManager.TYPE_VIDEO);
        }
}

@Override







