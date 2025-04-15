/*Added missing duration field to database record insertion.

This addresses AOSP Google Code Issue 15841.
When an audio recording sample is saved to the media
database, attributes like audio title are stored in a
database record.  However, the duration of the sample
was not being stored.  Therefore, an application that
queried attributes for a sample would get an incorrect
record; the record would contain a default duration of
0 ms.  The fix was to add the recorded sample's length
to the list of attributes put into the database record.
Note that the sample's length is in seconds and had to
be converted to milliseconds as described in the
documentation for the duration audio column.

This bug could have been prevented if the database
schema for duration audio column required a non-null
value with no default.

Change-Id:I80e4f8feb7d9f1050baf1e7c44323123b9d6b6b0Signed-off-by: Sahil Verma <vermasque@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/soundrecorder/SoundRecorder.java b/src/com/android/soundrecorder/SoundRecorder.java
//Synthetic comment -- index a138625..0fa48e7 100644

//Synthetic comment -- @@ -586,6 +586,7 @@
SimpleDateFormat formatter = new SimpleDateFormat(
res.getString(R.string.audio_db_title_format));
String title = formatter.format(date);
        long sampleLengthMillis = mRecorder.sampleLength() * 1000L;

// Lets label the recorded audio file as NON-MUSIC so that the file
// won't be displayed automatically, except for in the playlist.
//Synthetic comment -- @@ -595,6 +596,7 @@
cv.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
cv.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
cv.put(MediaStore.Audio.Media.DATE_MODIFIED, (int) (modDate / 1000));
        cv.put(MediaStore.Audio.Media.DURATION, sampleLengthMillis);
cv.put(MediaStore.Audio.Media.MIME_TYPE, mRequestedType);
cv.put(MediaStore.Audio.Media.ARTIST,
res.getString(R.string.audio_db_artist_name));







