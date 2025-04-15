/*Music: fix the force close at com.android.music when turn on usb storage

After MusicPlaybackService receiving an "Intent.ACTION_MEDIA_EJECT" intent after unMount sdcard,
it calls saveQueue() to backup some resources. In this case, if onDestroy() be called,
mPlayer will be set to null. So we need check if mPlayer is null before call mPlayer.isInitialized(),
or force close may happens.

Change-Id:I804d8d2cd4af2a8c1a7ae2b83cf160829e5626d0Author: Xidong Zhang <Xidong.Zhang@borqs.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18616*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index f872da2..c4f3a93 100644

//Synthetic comment -- @@ -458,9 +458,17 @@
}
}
ed.putInt("curpos", mPlayPos);
        if (mPlayer.isInitialized()) {
ed.putLong("seekpos", mPlayer.position());
}
ed.putInt("repeatmode", mRepeatMode);
ed.putInt("shufflemode", mShuffleMode);
SharedPreferencesCompat.apply(ed);







