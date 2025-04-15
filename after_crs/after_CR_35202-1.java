/*Skip mediastress test if no camera is defined*/




//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index 6e96f0d..4acb4ed 100644

//Synthetic comment -- @@ -224,6 +224,9 @@
mRecorder = new MediaRecorder();
}
});
            if (!hasRearCamera && !hasFrontCamera) {
                break;
            }
Log.v(TAG, "counter = " + i);
filename = OUTPUT_FILE + i + OUTPUT_FILE_EXT;
Log.v(TAG, filename);
//Synthetic comment -- @@ -361,6 +364,9 @@
mRecorder = new MediaRecorder();
}
});
            if (!hasRearCamera && !hasFrontCamera) {
                break;
            }
Log.v(TAG, "iterations : " + i);
Log.v(TAG, "videoEncoder : " + mVideoEncoder);
Log.v(TAG, "audioEncoder : " + mAudioEncoder);







