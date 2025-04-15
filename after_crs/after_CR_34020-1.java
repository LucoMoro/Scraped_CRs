/*Take care lower performance device on mediastress test

On added test case (mediastress test) of CTS2.3 r12, it will be fail with lower performance CPU such as MSM7627 because the device need much time to decode the contents. (need software decode to perform this test case)

Signed-off-by: nao <nao.tanaka.cy@kyocera.jp>*/




//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/CodecTest.java b/tests/tests/mediastress/src/android/mediastress/cts/CodecTest.java
//Synthetic comment -- index c349ac0..d97f9c8 100644

//Synthetic comment -- @@ -802,7 +802,7 @@
waittime = duration - mMediaPlayer.getCurrentPosition();
synchronized(mOnCompletion) {
try {
                    mOnCompletion.wait(waittime + 3600000);
} catch (Exception e) {
Log.v(TAG, "playMediaSamples are interrupted");
return false;







