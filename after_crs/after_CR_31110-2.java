/*Hiding media from folder with .nomedia file.

This patch allows media files present in a folder with
a .nomedia file to be hidden after these files have been
previously indexed.

Bug:http://code.google.com/p/android/issues/detail?id=24162Change-Id:Ia42133d0a7b119c76b5eaaffabd054aa1a739817Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 386986e..f8aa817 100644

//Synthetic comment -- @@ -475,6 +475,16 @@
key = path.toLowerCase();
}
FileCacheEntry entry = mFileCache.get(key);
            if (noMedia) {
                // In case the file is known and now is under a
                // .nomedia folder mark as not seen in order to
                // be removed from files table in the post scan.
                if (entry != null) {
                    entry.mSeenInFileSystem = false;
                }
                return null;
            }

// add some slack to avoid a rounding error
long delta = (entry != null) ? (lastModified - entry.mLastModified) : 0;
boolean wasModified = delta > 1 || delta < -1;







