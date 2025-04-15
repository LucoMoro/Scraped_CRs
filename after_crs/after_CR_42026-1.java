/*Make setCaptureRate locale safe

Dont use String.format in setCaptureRate as it is both locale
unsafe and less efficient than regular string concatenation.

Change-Id:I29418ec0352ef8c79967592b6eb8ff66cc9c99f0*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaRecorder.java b/media/java/android/media/MediaRecorder.java
//Synthetic comment -- index 9af201d..613354f 100644

//Synthetic comment -- @@ -351,12 +351,11 @@
*/
public void setCaptureRate(double fps) {
// Make sure that time lapse is enabled when this method is called.
        setParameter("time-lapse-enable=1");

double timeBetweenFrameCapture = 1 / fps;
int timeBetweenFrameCaptureMs = (int) (1000 * timeBetweenFrameCapture);
        setParameter("time-between-time-lapse-frame-capture=" + timeBetweenFrameCaptureMs);
}

/**







