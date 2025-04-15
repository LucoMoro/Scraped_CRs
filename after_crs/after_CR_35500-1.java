/*Skip mediastress test if no camera is defined*/




//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index 6e96f0d..2891c4b 100644

//Synthetic comment -- @@ -211,6 +211,11 @@
mSurfaceHolder = MediaFrameworkTest.getSurfaceView().getHolder();
File stressOutFile = new File(WorkDir.getTopDir(), MEDIA_STRESS_OUTPUT);
Writer output = new BufferedWriter(new FileWriter(stressOutFile, true));

        if (!hasRearCamera && !hasFrontCamera) {
                output.write("No camera found. Skipping recorder stress test\n");
                return;
        }
output.write("H263 video record- reset after prepare Stress test\n");
output.write("Total number of loops:" +
NUMBER_OF_RECORDER_STRESS_LOOPS + "\n");
//Synthetic comment -- @@ -347,6 +352,11 @@
File stressOutFile = new File(WorkDir.getTopDir(), MEDIA_STRESS_OUTPUT);
Writer output = new BufferedWriter(
new FileWriter(stressOutFile, true));

        if (!hasRearCamera && !hasFrontCamera) {
                output.write("No camera found. Skipping video record and play back stress test\n");
                return;
        }
output.write("Video record and play back stress test:\n");
output.write("Total number of loops:"
+ NUMBER_OF_RECORDERANDPLAY_STRESS_LOOPS + "\n");







