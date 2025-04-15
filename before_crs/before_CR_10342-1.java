/*Allow video ringtone for default ringtone, but not for notifications.*/
//Synthetic comment -- diff --git a/src/com/android/settings/DefaultRingtonePreference.java b/src/com/android/settings/DefaultRingtonePreference.java
//Synthetic comment -- index 8eed563..c17a94e 100644

//Synthetic comment -- @@ -47,6 +47,14 @@
* Similarly, 'Silent' shouldn't be shown here. 
*/
ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
}

@Override







