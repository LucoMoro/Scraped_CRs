/*Default ringtone cannot be previewed as alarm stream type.

When choosing a ringtone for alarm use by "Android System"
ringtone picker (RingtonePickerActivity), default ringtone is
not played for preview with volume of alarm stream type but
with that of normal stream type.

It is because stream type is not reflected on mDefaultRingtone
instance of RingtonePickerActivity.

Change-Id:Idc71c9b620e40e4109604b2337677134bb74a8cd*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/RingtonePickerActivity.java b/src/com/android/providers/media/RingtonePickerActivity.java
//Synthetic comment -- index 2669fb7..7db1f53 100644

//Synthetic comment -- @@ -305,6 +305,13 @@
if (mDefaultRingtone == null) {
mDefaultRingtone = RingtoneManager.getRingtone(this, mUriForDefaultItem);
}
           /*
            * Stream type of mDefaultRingtone is not set explicitly here.
            * It should be set in accordance with mRingtoneManager of this Activity.
            */
            if (mDefaultRingtone != null) {
                mDefaultRingtone.setStreamType(mRingtoneManager.inferStreamType());
            }
ringtone = mDefaultRingtone;

/*







