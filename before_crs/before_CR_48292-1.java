/*Music: Force close at com.android.music when turn on usb storage

After MusicPlaybackService receives an "Intent.ACTION_MEDIA_EJECT" intent after unMount sdcard,
it calls saveQueue() to backup some resources. In this case, if onDestroy() be called,
mPlayer will be set to null. So we need check if mPlayer is null before call mPlayer.isInitialized(),
or force close may happens.

Change-Id:I1397cebde1a3ad6fcc7f4ef5358f4534878c02e5Author: Jun Wu <junx.wu@intel.com>
Signed-off-by: b557 <Xidong.Zhang@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18616*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..ec91338 100644

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







