/*MediaPlayertest#testVideoSurfaceResetting :
Change the Tolerance value back to original 150 msecs as that covers
vast range of devices with varying audio latency range.

Change-Id:I48a17ba69bbf5bce75129317d88ae3b32a45217a*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaPlayerTest.java b/tests/tests/media/src/android/media/cts/MediaPlayerTest.java
//Synthetic comment -- index 9c03a0b..724350e 100644

//Synthetic comment -- @@ -111,7 +111,7 @@
* from the time setDisplay() was called
*/
public void testVideoSurfaceResetting() throws Exception {
        final int tolerance = 150;
final int seekPos = 1500;

final CountDownLatch seekDone = new CountDownLatch(1);







