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

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
//Synthetic comment -- @@ -127,42 +124,13 @@
method = "getContentUri",
args = {String.class, long.class}
)
public void testGetContentUri() {
        assertEquals("content://media/external/audio/playlists/1337/members",
                Members.getContentUri("external", 1337).toString());
        assertEquals("content://media/internal/audio/playlists/3007/members",
                Members.getContentUri("internal", 3007).toString());
}

public void testStoreAudioPlaylistsMembersExternal() {
ContentValues values = new ContentValues();
values.put(Playlists.NAME, "My favourites");







