/*packages/apps/Music: Fix for auto resume of Music after Voice call

-Music app is getting resumed once voice call ends even if user has paused
while Voice call is in progess.
-Add a state variable to keep a track if user has paused it.

Change-Id:I76769cc2cd321286061eb36ce6a2c65906e947c3*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackActivity.java b/src/com/android/music/MediaPlaybackActivity.java
//Synthetic comment -- index 1367c5a..cc3ee93 100644

//Synthetic comment -- @@ -955,6 +955,7 @@
try {
if(mService != null) {
if (mService.isPlaying()) {
                    MediaPlaybackService.mPausedByTransientLossOfFocus = false;
mService.pause();
} else {
mService.play();








//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 7c0bbed..a0fb6d8 100644

//Synthetic comment -- @@ -138,7 +138,7 @@
private AudioManager mAudioManager;
private boolean mQueueIsSaveable = true;
// used to track what type of audio focus loss caused the playback to pause
    public static boolean mPausedByTransientLossOfFocus = false;

private SharedPreferences mPreferences;
// We use this to distinguish between different cards when saving/restoring playlists.







