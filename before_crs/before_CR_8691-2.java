/*Make play() call next() if we are at end of track.*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6071719..ae02c09 100644

//Synthetic comment -- @@ -905,6 +905,11 @@
*/
public void play() {
if (mPlayer.isInitialized()) {
mPlayer.start();
setForeground(true);
mWasPlaying = true;







