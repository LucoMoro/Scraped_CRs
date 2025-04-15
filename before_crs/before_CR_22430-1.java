/*Closing stream after class pre-loading.

Change-Id:I409f49717a17adedf9f0a3d07635cad0c0468702*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ZygoteInit.java b/core/java/com/android/internal/os/ZygoteInit.java
//Synthetic comment -- index f0e5517..4cd74f6 100644

//Synthetic comment -- @@ -277,9 +277,10 @@
runtime.runFinalizationSync();
Debug.startAllocCounting();

try {
                BufferedReader br
                    = new BufferedReader(new InputStreamReader(is), 256);

int count = 0;
String line;
//Synthetic comment -- @@ -324,6 +325,11 @@
} catch (IOException e) {
Log.e(TAG, "Error reading " + PRELOADED_CLASSES + ".", e);
} finally {
// Restore default.
runtime.setTargetHeapUtilization(defaultUtilization);








