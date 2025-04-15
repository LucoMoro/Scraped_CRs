/*Changed priority of SensorManager thread

	* changes:
	changed priority of SensorManager thread to be THREAD_PRIORITY_URGENT_DISPLAY so that samples from the sensors will arrive more regularly even under higher load.*/




//Synthetic comment -- diff --git a/core/java/android/hardware/SensorManager.java b/core/java/android/hardware/SensorManager.java
//Synthetic comment -- index 271f973..01c67cf 100644

//Synthetic comment -- @@ -364,7 +364,7 @@
final float[] values = new float[3];
final int[] status = new int[1];
final long timestamp[] = new long[1];
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);

if (!open()) {
return;







