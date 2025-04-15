/*fix the mess thumbnail when export 1080p/720p video to 848x480 resolution

Root cause: After export the video, the output frame size will be rewrote by the export
resolution, when rotate the phone, the frame will use the export resolution to display
which causes the mess thumbnail.
Fix: store the old output frame size and reset the size after export.

Change-Id:I05b6b6a87187969213fb9e44d95e7e0df2f06afaAuthor: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Tong, Bo <box.tong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 32193*/
//Synthetic comment -- diff --git a/media/java/android/media/videoeditor/MediaArtistNativeHelper.java b/media/java/android/media/videoeditor/MediaArtistNativeHelper.java
//Synthetic comment -- index f4fccbe..a1e00a5 100644

//Synthetic comment -- @@ -3690,6 +3690,7 @@
mPreviewEditSettings.outputFile = mOutputFilename = filePath;

int aspectRatio = mVideoEditor.getAspectRatio();
mPreviewEditSettings.videoFrameSize = findVideoResolution(aspectRatio, height);
mPreviewEditSettings.videoFormat = mExportVideoCodec;
mPreviewEditSettings.audioFormat = mExportAudioCodec;
//Synthetic comment -- @@ -3741,6 +3742,7 @@
}

mExportProgressListener = null;
}

/**







