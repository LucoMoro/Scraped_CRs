/*To replace duplicated data of Audio3, Audio4 and Audio5 to new music data.
Due to HTC’s Media Provider won't allow duplicated data in database (this can improve user experience),
it will let AP doesn't query repeat record.

Change-Id:Id06d4b4aa3cb758d84667777477f6570506d299b*/




//Synthetic comment -- diff --git a/tests/src/android/provider/cts/MediaStoreAudioTestHelper.java b/tests/src/android/provider/cts/MediaStoreAudioTestHelper.java
//Synthetic comment -- index 7bcd0c7..dddbeb2 100644

//Synthetic comment -- @@ -221,6 +221,234 @@
}
}

    public static class Audio3 extends MockAudioMediaInfo {
        private Audio3() {
        }

        private static Audio3 sInstance = new Audio3();

        public static Audio3 getInstance() {
            return sInstance;
        }

        public static final int IS_RINGTONE = 0;

        public static final int IS_NOTIFICATION = 0;

        public static final int IS_ALARM = 0;

        public static final int IS_MUSIC = 1;

        public static final int IS_DRM = 0;

        public static final int YEAR = 1992;

        public static final int TRACK = 1;

        public static final int DURATION = 340000;

        public static final String COMPOSER = "May Day";

        public static final String ARTIST = "May Day";

        public static final String ALBUM = "May Day";

        public static final String TITLE = "May Day";

        public static final int SIZE = 2737870;

        public static final String MIME_TYPE = "audio/x-mpeg";

        public static final String DISPLAY_NAME = "May Day";

        public static final String INTERNAL_DATA =
            "/data/data/com.android.cts.stub/files/MayDay.mp3";

        public static final String FILE_NAME = "MayDay.mp3";

        public static final String EXTERNAL_DATA = Environment.getExternalStorageDirectory() +
                "/" + FILE_NAME;

        public static final long DATE_MODIFIED = System.currentTimeMillis() / 1000;

        public static final String GENRE = "POP";
        @Override
        public ContentValues getContentValues(boolean isInternal) {
            ContentValues values = new ContentValues();
            values.put(Media.DATA, isInternal ? INTERNAL_DATA : EXTERNAL_DATA);
            values.put(Media.DATE_MODIFIED, DATE_MODIFIED);
            values.put(Media.DISPLAY_NAME, DISPLAY_NAME);
            values.put(Media.MIME_TYPE, MIME_TYPE);
            values.put(Media.SIZE, SIZE);
            values.put(Media.TITLE, TITLE);
            values.put(Media.ALBUM, ALBUM);
            values.put(Media.ARTIST, ARTIST);
            values.put(Media.COMPOSER, COMPOSER);
            values.put(Media.DURATION, DURATION);
            values.put(Media.TRACK, TRACK);
            values.put(Media.YEAR, YEAR);
            values.put(Media.IS_MUSIC, IS_MUSIC);
            values.put(Media.IS_ALARM, IS_ALARM);
            values.put(Media.IS_NOTIFICATION, IS_NOTIFICATION);
            values.put(Media.IS_RINGTONE, IS_RINGTONE);
            values.put(Media.IS_DRM, IS_DRM);

            return values;
        }
    }

    public static class Audio4 extends MockAudioMediaInfo {
        private Audio4() {
        }

        private static Audio4 sInstance = new Audio4();

        public static Audio4 getInstance() {
            return sInstance;
        }

        public static final int IS_RINGTONE = 0;

        public static final int IS_NOTIFICATION = 0;

        public static final int IS_ALARM = 0;

        public static final int IS_MUSIC = 1;

        public static final int IS_DRM = 0;

        public static final int YEAR = 1992;

        public static final int TRACK = 1;

        public static final int DURATION = 340000;

        public static final String COMPOSER = "Jay Chou";

        public static final String ARTIST = "Jay Chou";

        public static final String ALBUM = "Jay Chou";

        public static final String TITLE = "Jay Chou";

        public static final int SIZE = 2737870;

        public static final String MIME_TYPE = "audio/x-mpeg";

        public static final String DISPLAY_NAME = "Jay Chou";

        public static final String INTERNAL_DATA =
            "/data/data/com.android.cts.stub/files/JayChou.mp3";

        public static final String FILE_NAME = "JayChou.mp3";

        public static final String EXTERNAL_DATA = Environment.getExternalStorageDirectory() +
                "/" + FILE_NAME;

        public static final long DATE_MODIFIED = System.currentTimeMillis() / 1000;

        public static final String GENRE = "POP";
        @Override
        public ContentValues getContentValues(boolean isInternal) {
            ContentValues values = new ContentValues();
            values.put(Media.DATA, isInternal ? INTERNAL_DATA : EXTERNAL_DATA);
            values.put(Media.DATE_MODIFIED, DATE_MODIFIED);
            values.put(Media.DISPLAY_NAME, DISPLAY_NAME);
            values.put(Media.MIME_TYPE, MIME_TYPE);
            values.put(Media.SIZE, SIZE);
            values.put(Media.TITLE, TITLE);
            values.put(Media.ALBUM, ALBUM);
            values.put(Media.ARTIST, ARTIST);
            values.put(Media.COMPOSER, COMPOSER);
            values.put(Media.DURATION, DURATION);
            values.put(Media.TRACK, TRACK);
            values.put(Media.YEAR, YEAR);
            values.put(Media.IS_MUSIC, IS_MUSIC);
            values.put(Media.IS_ALARM, IS_ALARM);
            values.put(Media.IS_NOTIFICATION, IS_NOTIFICATION);
            values.put(Media.IS_RINGTONE, IS_RINGTONE);
            values.put(Media.IS_DRM, IS_DRM);

            return values;
        }
    }

    public static class Audio5 extends MockAudioMediaInfo {
        private Audio5() {
        }

        private static Audio5 sInstance = new Audio5();

        public static Audio5 getInstance() {
            return sInstance;
        }

        public static final int IS_RINGTONE = 0;

        public static final int IS_NOTIFICATION = 0;

        public static final int IS_ALARM = 0;

        public static final int IS_MUSIC = 1;

        public static final int IS_DRM = 0;

        public static final int YEAR = 1992;

        public static final int TRACK = 1;

        public static final int DURATION = 340000;

        public static final String COMPOSER = "Shin";

        public static final String ARTIST = "Shin";

        public static final String ALBUM = "Shin";

        public static final String TITLE = "Shin";

        public static final int SIZE = 2737870;

        public static final String MIME_TYPE = "audio/x-mpeg";

        public static final String DISPLAY_NAME = "Shin";

        public static final String INTERNAL_DATA =
            "/data/data/com.android.cts.stub/files/Shin.mp3";

        public static final String FILE_NAME = "Shin.mp3";

        public static final String EXTERNAL_DATA = Environment.getExternalStorageDirectory() +
                "/" + FILE_NAME;

        public static final long DATE_MODIFIED = System.currentTimeMillis() / 1000;

        public static final String GENRE = "POP";
        @Override
        public ContentValues getContentValues(boolean isInternal) {
            ContentValues values = new ContentValues();
            values.put(Media.DATA, isInternal ? INTERNAL_DATA : EXTERNAL_DATA);
            values.put(Media.DATE_MODIFIED, DATE_MODIFIED);
            values.put(Media.DISPLAY_NAME, DISPLAY_NAME);
            values.put(Media.MIME_TYPE, MIME_TYPE);
            values.put(Media.SIZE, SIZE);
            values.put(Media.TITLE, TITLE);
            values.put(Media.ALBUM, ALBUM);
            values.put(Media.ARTIST, ARTIST);
            values.put(Media.COMPOSER, COMPOSER);
            values.put(Media.DURATION, DURATION);
            values.put(Media.TRACK, TRACK);
            values.put(Media.YEAR, YEAR);
            values.put(Media.IS_MUSIC, IS_MUSIC);
            values.put(Media.IS_ALARM, IS_ALARM);
            values.put(Media.IS_NOTIFICATION, IS_NOTIFICATION);
            values.put(Media.IS_RINGTONE, IS_RINGTONE);
            values.put(Media.IS_DRM, IS_DRM);

            return values;
        }
    }

// These constants are not part of the public API
public static final String EXTERNAL_VOLUME_NAME = "external";
public static final String INTERNAL_VOLUME_NAME = "internal";








//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java b/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java
//Synthetic comment -- index aa524aa..bf97848 100644

//Synthetic comment -- @@ -27,6 +27,9 @@
import android.provider.MediaStore.Audio.Playlists.Members;
import android.provider.cts.MediaStoreAudioTestHelper.Audio1;
import android.provider.cts.MediaStoreAudioTestHelper.Audio2;
import android.provider.cts.MediaStoreAudioTestHelper.Audio3;
import android.provider.cts.MediaStoreAudioTestHelper.Audio4;
import android.provider.cts.MediaStoreAudioTestHelper.Audio5;
import android.provider.cts.MediaStoreAudioTestHelper.MockAudioMediaInfo;
import android.test.InstrumentationTestCase;

//Synthetic comment -- @@ -114,9 +117,9 @@
mContentResolver = getInstrumentation().getContext().getContentResolver();
mIdOfAudio1 = insertAudioItem(Audio1.getInstance());
mIdOfAudio2 = insertAudioItem(Audio2.getInstance());
        mIdOfAudio3 = insertAudioItem(Audio3.getInstance());
        mIdOfAudio4 = insertAudioItem(Audio4.getInstance());
        mIdOfAudio5 = insertAudioItem(Audio5.getInstance());
}

@Override







