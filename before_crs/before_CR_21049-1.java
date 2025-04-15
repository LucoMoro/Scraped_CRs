/*Fix MediaStore_Audio_Playlists_MembersTest

Bug 3188260

Replaced brittle test of the getContentUri with tests that check the
format of the uri. The test marked with "needs investigation" seems to
work fine now...so remove the annotation for it...

Change-Id:I649289571b1bedb146425b1cbd09272cf24382ae*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java b/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java
//Synthetic comment -- index 6b15bb9..12d080e 100644

//Synthetic comment -- @@ -16,16 +16,13 @@

package android.provider.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.ToBeFixed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
//Synthetic comment -- @@ -127,42 +124,13 @@
method = "getContentUri",
args = {String.class, long.class}
)
    @BrokenTest("brittle test")
public void testGetContentUri() {
        // this verification seems brittle - will break if there happens to be a playlist with Id 1
        // present in external volume
        // setUp should create a playlist which this method should verify can be queried
        Cursor c = mContentResolver.query(
                Members.getContentUri(MediaStoreAudioTestHelper.EXTERNAL_VOLUME_NAME, 1),
                mMembersProjection, null, null, Members.DEFAULT_SORT_ORDER);
        assertEquals(0, c.getCount());
        c.close();

        // test querying media provider with null projection, should return all columns
        c = mContentResolver.query(
                Members.getContentUri(MediaStoreAudioTestHelper.EXTERNAL_VOLUME_NAME, 1), null,
                Members.ALBUM + "=?", new String[] { Audio1.ALBUM },
                Members.DEFAULT_SORT_ORDER);
        assertEquals(0, c.getCount());
        // TODO: need a way to verify all expected columns are returned. Purely testing for number
        // of columns returned is brittle
        assertEquals(31, c.getColumnCount());
        c.close();

        try {
            mContentResolver.query(
                    Members.getContentUri(MediaStoreAudioTestHelper.INTERNAL_VOLUME_NAME, 1),
                    mMembersProjection, null, null, Members.DEFAULT_SORT_ORDER);
            fail("Should throw SQLException as the internal datatbase has no playlist");
        } catch (SQLException e) {
            // expected
        }

        String volume = "fakeVolume";
        assertNull(mContentResolver.query(Members.getContentUri(volume, 1), null, null, null,
                null));
}
    @BrokenTest("needs investigation")
public void testStoreAudioPlaylistsMembersExternal() {
ContentValues values = new ContentValues();
values.put(Playlists.NAME, "My favourites");







