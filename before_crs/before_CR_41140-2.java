/*According to external test, delete playlists after inserting internal data.
It will keep clean environment.

Change-Id:I2116cd305f40015affc410211d83a890a6bf13fb*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_PlaylistsTest.java b/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_PlaylistsTest.java
//Synthetic comment -- index 19fa2e1c..0d36212 100644

//Synthetic comment -- @@ -114,9 +114,16 @@
values.put(Playlists.DATE_ADDED, dateAdded);
long dateModified = System.currentTimeMillis();
values.put(Playlists.DATE_MODIFIED, dateModified);
Uri uri = mContentResolver.insert(Playlists.INTERNAL_CONTENT_URI, values);
assertNotNull(uri);
        assertTrue(Pattern.matches("content://media/internal/audio/playlists/\\d+",
                uri.toString()));
}
}








//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java b/tests/tests/provider/src/android/provider/cts/MediaStore_Audio_Playlists_MembersTest.java
//Synthetic comment -- index 9ae42ae..aa524aa 100644

//Synthetic comment -- @@ -456,9 +456,16 @@
values.put(Playlists.DATE_ADDED, dateAdded);
long dateModified = System.currentTimeMillis();
values.put(Playlists.DATE_MODIFIED, dateModified);
Uri uri = mContentResolver.insert(Playlists.INTERNAL_CONTENT_URI, values);
assertNotNull(uri);
        assertTrue(Pattern.matches("content://media/internal/audio/playlists/\\d+",
                uri.toString()));
}
}







