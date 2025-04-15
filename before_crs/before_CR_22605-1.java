/*For some HW limitation, we need to take longer time to switch from main camera to 2nd camera.
Solution: set WAIT_FOR_COMMAND_TO_COMPLETE from 1500 to 3000.

Change-Id:I4b02b8ebb1385283e23b0389e968043947ea44f9*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 60b8459..bb66ce9 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
private boolean mErrorCallbackResult = false;
private boolean mAutoFocusSucceeded = false;

    private static final int WAIT_FOR_COMMAND_TO_COMPLETE = 1500;  // Milliseconds.
private static final int WAIT_FOR_FOCUS_TO_COMPLETE = 3000;
private static final int WAIT_FOR_SNAPSHOT_TO_COMPLETE = 5000;








