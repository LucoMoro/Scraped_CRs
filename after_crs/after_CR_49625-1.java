/*FM volume does not change when FmRx App in background

FM service only modify volume if volume change event
occurs on a STREAM_MUSIC stream type.
This behavior should not be change to keep volume consistencies
between appli in foreground/background.
Therefore AudioService must return a STREAM_MUSIC stream type
when FM is active

Change-Id:Ia7a48ab643310754d4fc883b0300ccbd3a3a527cAuthor: Sylvain Pichon <sylvainx.pichon@intel.com>
Signed-off-by: Sylvain Pichon <sylvainx.pichon@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39656*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 22f699f..e3465df 100644

//Synthetic comment -- @@ -179,6 +179,8 @@
// protects mRingerMode
private final Object mSettingsLock = new Object();

    private int mFmRxMode;

private boolean mMediaServerOk;

private SoundPool mSoundPool;







