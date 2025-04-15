/*Allow contacts to have video ringtones.*/




//Synthetic comment -- diff --git a/src/com/android/contacts/EditContactActivity.java b/src/com/android/contacts/EditContactActivity.java
//Synthetic comment -- index b89573b..e6e5595 100644

//Synthetic comment -- @@ -721,7 +721,8 @@
// Allow user to pick 'Default'
intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
// Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                        RingtoneManager.TYPE_RINGTONE | RingtoneManager.TYPE_VIDEO);
// Don't show 'Silent'
intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);








