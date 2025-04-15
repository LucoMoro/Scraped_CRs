/*Music: Music Force Close when the last song is end and repeat is closed.

getCursorForId(mPlayList[mPlayPos]) will cause ArrayIndexOutOfBoundsException
so need to check the index of mPlayList before use it.

Change-Id:I31e69c48a241310ec9418d147cc980bdcc81e31eAuthor: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57322*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..6364fae 100644

//Synthetic comment -- @@ -203,7 +203,9 @@
mCursor.close();
mCursor = null;
}
                    mCursor = getCursorForId(mPlayList[mPlayPos]);
notifyChange(META_CHANGED);
updateNotification();
setNextTrack();







