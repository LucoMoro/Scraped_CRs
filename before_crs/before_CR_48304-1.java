/*music: fix javacrash when music stopped after launch Camera2.0.

mPlayer.isInitialized() in getAudioId() will cause NullPointerException.

check if mPlayer is null, if true return -1 then it won't cause
NullPointerException exception any more.

Change-Id:I2493437540c0300f492a0ec82263a45c0193fabcAuthor: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 68851*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..1870342 100644

//Synthetic comment -- @@ -1700,6 +1700,9 @@
*/
public long getAudioId() {
synchronized (this) {
if (mPlayPos >= 0 && mPlayer.isInitialized()) {
return mPlayList[mPlayPos];
}







