/*"ImageCapture.java" modifies the way memory is measured during "#testImageCapture"

- Has some additional fixes related to package names

Change-Id:Ia72f3284672800855e38995922bf258c52dfa58eSigned-off-by: Emilian Peev <epeev@ti.com>*/
//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/ImageCapture.java b/tests/src/com/android/camera/stress/ImageCapture.java
//Synthetic comment -- index cd2bd29..db8a78e 100755

//Synthetic comment -- @@ -191,8 +191,6 @@
File imageCaptureMemFile = new File(camera_mem_out);

mStartPid = getMediaserverPid();
        mStartMemory = getMediaserverVsize();
        Log.v(TAG, "start memory : " + mStartMemory);

try {
Writer output = new BufferedWriter(new FileWriter(imageCaptureMemFile, true));
//Synthetic comment -- @@ -209,6 +207,18 @@
inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
Thread.sleep(WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN);
if (( i % NO_OF_LOOPS_TAKE_MEMORY_SNAPSHOT) == 0){
Log.v(TAG, "value of i :" + i);
getMemoryWriteToLog(output);
//Synthetic comment -- @@ -262,7 +272,7 @@
mOut.write("loop: ");
// Switch to the video mode
Intent intent = new Intent();
            intent.setClassName("com.google.android.camera",
"com.android.camera.VideoCamera");
getActivity().startActivity(intent);
for (int i = 0; i < total_num_of_videos; i++) {







