/*Modify the behavior of next when not shuffling so that it pauses at the beginning
of the first song in the playlist rather than the end of the last song.  This
ensures that the first song, not the last song, will begin playing if the user
hits play again after the playlist finishes.*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6071719..2acd1c2 100644

//Synthetic comment -- @@ -1118,14 +1118,17 @@
mPlayPos++;
} else {
if (mPlayPos >= mPlayListLen - 1) {
// we're at the end of the list
if (mRepeatMode == REPEAT_NONE && !force) {
// all done
gotoIdleState();
notifyChange(PLAYBACK_COMPLETE);
return;
                    } else if (mRepeatMode == REPEAT_ALL || force) {
                        mPlayPos = 0;
}
} else {
mPlayPos++;







