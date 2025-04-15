/*Reproduce issue steps:
Step 1 > Add a playlist with only 1 song
Step 2 > Enable shuffle
Step 3 > Play the playlist till completed

And the state of Music Player will be incorrect.

Change-Id:I10f1629deeeabe661f73c2f1d4d57dbe6b80af6c*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 85bb797..0278f15 100644

//Synthetic comment -- @@ -1251,6 +1251,8 @@
} else {
// all done
gotoIdleState();
                        notifyChange(PLAYBACK_COMPLETE);
                        mIsSupposedToBePlaying = false;
return;
}
}







