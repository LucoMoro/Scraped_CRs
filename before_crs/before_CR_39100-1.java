/*mediacodec: Don't suggest calling getOutputFormat() immediately

Calling getOutputFormat() at this point currently crashes in
native code. (After a fix to the native code, this gives an
IllegalStateException instead.)

Change-Id:Ia45c4820bb3d9ed435a0aeef1ff8c230524f2e1f*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaCodec.java b/media/java/android/media/MediaCodec.java
//Synthetic comment -- index 560c549..99db066 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
* codec.start();
* ByteBuffer[] inputBuffers = codec.getInputBuffers();
* ByteBuffer[] outputBuffers = codec.getOutputBuffers();
 * MediaFormat format = codec.getOutputFormat();
* for (;;) {
*   int inputBufferIndex = codec.dequeueInputBuffer(timeoutUs);
*   if (inputBufferIndex &gt;= 0) {
//Synthetic comment -- @@ -51,7 +50,7 @@
*     outputBuffers = codec.getOutputBuffers();
*   } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
*     // Subsequent data will conform to new format.
 *     format = codec.getOutputFormat();
*     ...
*   }
* }







