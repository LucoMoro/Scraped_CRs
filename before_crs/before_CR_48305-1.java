/*Music: fix NullPointerException when MediaPlaybackService.stop.

This fix is avoiding NullPointerException when call MediaPlaybackService.stop,
otherwise JAVACRASH will happen.

Change-Id:Iad8c242c5ba422fb7a8483d34ebbff5e38ed4f2bAuthor: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 53246*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..e6e23c1 100644

//Synthetic comment -- @@ -1212,8 +1212,10 @@
}

private void stop(boolean remove_status_icon) {
        if (mPlayer.isInitialized()) {
            mPlayer.stop();
}
mFileToPlay = null;
if (mCursor != null) {







