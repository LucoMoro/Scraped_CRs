/*Add VGA resolution for timelapse video recording

Note: This patch is related to a change in platforms/frameworks/av

Change-Id:I79f3c95934f95485e5e627ea286586ff7416ffc3Author: Guoliang Ji <guoliang.ji@intel.com>
Signed-off-by: Liu Bolun <bolunx.liu@intel.com>
Signed-off-by: Guoliang Ji <guoliang.ji@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39342*/
//Synthetic comment -- diff --git a/media/java/android/media/CamcorderProfile.java b/media/java/android/media/CamcorderProfile.java
//Synthetic comment -- index 511111c..9253ee1 100644

//Synthetic comment -- @@ -134,9 +134,15 @@
*/
public static final int QUALITY_TIME_LAPSE_QVGA = 1007;

// Start and end of timelapse quality list
private static final int QUALITY_TIME_LAPSE_LIST_START = QUALITY_TIME_LAPSE_LOW;
    private static final int QUALITY_TIME_LAPSE_LIST_END = QUALITY_TIME_LAPSE_QVGA;

/**
* Default recording duration in seconds before the session is terminated.








//Synthetic comment -- diff --git a/media/java/android/media/MediaRecorder.java b/media/java/android/media/MediaRecorder.java
//Synthetic comment -- index 9af201d..6046043 100644

//Synthetic comment -- @@ -322,7 +322,7 @@
setVideoEncodingBitRate(profile.videoBitRate);
setVideoEncoder(profile.videoCodec);
if (profile.quality >= CamcorderProfile.QUALITY_TIME_LAPSE_LOW &&
             profile.quality <= CamcorderProfile.QUALITY_TIME_LAPSE_QVGA) {
// Nothing needs to be done. Call to setCaptureRate() enables
// time lapse video recording.
} else {







