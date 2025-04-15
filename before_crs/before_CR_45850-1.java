/*Rewind the position before invoking copyPixelsFromBuffer().

After copying pixels to intBuf1, its position is changed and the method "copyPixelsFromBuffer" would copy the pixels from the buffer, beginning at the current position.
If it didn’t rewind the position of intBuf1, it would cause the wrong position to copy back, so before copy pixels from intBuf1, it should rewind intBuf1 then finally intBuf1 and intBuf2 will be the same.

Change-Id:I4e4406e70b42d449f1b2264dcf20e5a79037721f*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/BitmapTest.java b/tests/tests/graphics/src/android/graphics/cts/BitmapTest.java
//Synthetic comment -- index c8edfd7..b468e58 100644

//Synthetic comment -- @@ -158,12 +158,13 @@

Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
mBitmap.getConfig());
bitmap.copyPixelsFromBuffer(intBuf1);
IntBuffer intBuf2 = IntBuffer.allocate(pixSize);
bitmap.copyPixelsToBuffer(intBuf2);

        assertEquals(intBuf1.position(), intBuf2.position());
        int size = intBuf1.position();
intBuf1.position(0);
intBuf2.position(0);
for (int i = 0; i < size; i++) {







