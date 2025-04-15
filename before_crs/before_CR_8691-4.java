/*Make play() call next() if we are at end of track (and not REPEAT_CURRENT).*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6071719..7aca206 100644

//Synthetic comment -- @@ -905,6 +905,12 @@
*/
public void play() {
if (mPlayer.isInitialized()) {
mPlayer.start();
setForeground(true);
mWasPlaying = true;







