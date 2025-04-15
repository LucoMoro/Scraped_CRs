/*To replace duplicated data of Audio3, Audio4 and Audio5 to new music data.
Due to HTC’s Media Provider won't allow duplicated data in database (this can improve user experience),
it will let AP doesn't query repeat record.

Change-Id:Id06d4b4aa3cb758d84667777477f6570506d299b*/
//Synthetic comment -- diff --git a/tests/src/android/provider/cts/MediaStoreAudioTestHelper.java b/tests/src/android/provider/cts/MediaStoreAudioTestHelper.java
//Synthetic comment -- index 7bcd0c7..dddbeb2 100644

//Synthetic comment -- @@ -221,6 +221,234 @@
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
import android.provider.cts.MediaStoreAudioTestHelper.MockAudioMediaInfo;
import android.test.InstrumentationTestCase;

//Synthetic comment -- @@ -114,9 +117,9 @@
mContentResolver = getInstrumentation().getContext().getContentResolver();
mIdOfAudio1 = insertAudioItem(Audio1.getInstance());
mIdOfAudio2 = insertAudioItem(Audio2.getInstance());
        mIdOfAudio3 = insertAudioItem(Audio1.getInstance());
        mIdOfAudio4 = insertAudioItem(Audio1.getInstance());
        mIdOfAudio5 = insertAudioItem(Audio1.getInstance());
}

@Override







