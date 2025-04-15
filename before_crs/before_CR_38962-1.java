/*Null pointer protection in AudioPreview.java for Music app

Added protection from null pointer exception of PreviewPlayer.
It can happen if user presses play-pause or drags SeekBar
and exits from AudioPreview simultaneously, so pointer to
PreviewPlayer will be used after nullification but before
the exiting from AudioPreview activity will be finished.

Change-Id:Idb3a903fdaa95727ad63dda8cab4589b5cfacd6a*/
//Synthetic comment -- diff --git a/src/com/android/music/AudioPreview.java b/src/com/android/music/AudioPreview.java
old mode 100644
new mode 100755
//Synthetic comment -- index 846ed14..3f22c6c

//Synthetic comment -- @@ -333,6 +333,10 @@
if (!fromuser) {
return;
}
mPlayer.seekTo(progress);
}
public void onStopTrackingTouch(SeekBar bar) {
//Synthetic comment -- @@ -352,6 +356,10 @@
}

public void playPauseClicked(View v) {
if (mPlayer.isPlaying()) {
mPlayer.pause();
} else {







