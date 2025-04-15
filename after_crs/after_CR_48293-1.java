/*Music: JAVACRASH on com.android.music at com.android.music.MediaPlaybackService.open.

mPlayer.setDataSource(mFileToPlay) can throw
NullPointerException exception.

Need to check if mPlayer is null before use it.

Change-Id:Ib58cb05b1a593d2f594030d8358214b46a7ab943Author: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57173*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..0d2c469 100644

//Synthetic comment -- @@ -1097,6 +1097,10 @@
return false;
}

            if (mPlayer == null) {
                return false;
            }

// if mCursor is null, try to associate path with a database cursor
if (mCursor == null) {








