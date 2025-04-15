/*Loop doesn't compare the whole bitmap image

The length of loop should be the size of the buffer. Currently,
the comparison makes only one-fourth of passed bitmap.
And also the pixel shoud be gotten by a multiple of four.

Change-Id:Ib23c6669a26b0a70d6910cee00b15cc289d573f3*/
//Synthetic comment -- diff --git a/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java b/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 052ddea..73351dc

//Synthetic comment -- @@ -178,17 +178,16 @@
return false;
}

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

ByteBuffer buffer1 = ByteBuffer.allocate(bitmap.getByteCount());
            ByteBuffer buffer2 = ByteBuffer.allocate(reference.getByteCount());

bitmap.copyPixelsToBuffer(buffer1);
reference.copyPixelsToBuffer(buffer2);

            final int length = w*h;
            for (int i = 0; i < length; i++) {
int pel1 = buffer1.getInt(i);
int pel2 = buffer2.getInt(i);
int dr = (pel1 & 0x000000FF) - (pel2 & 0x000000FF);







