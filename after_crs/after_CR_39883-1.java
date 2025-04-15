/*Remove useless checkIndex calls from the asXBuffer adapter classes.

Also add tests that we throw the expected exceptions.

Bug: 6085292
Change-Id:Ibcc44bdb546cba5365cdf557847e068fea77e1ca*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/CharToByteBufferAdapter.java b/luni/src/main/java/java/nio/CharToByteBufferAdapter.java
//Synthetic comment -- index b9100a2..86c7186 100644

//Synthetic comment -- @@ -93,7 +93,6 @@

@Override
public char get(int index) {
return byteBuffer.getChar(index * SizeOf.CHAR);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/DoubleToByteBufferAdapter.java b/luni/src/main/java/java/nio/DoubleToByteBufferAdapter.java
//Synthetic comment -- index 8b1e084..ead4ef0 100644

//Synthetic comment -- @@ -93,7 +93,6 @@

@Override
public double get(int index) {
return byteBuffer.getDouble(index * SizeOf.DOUBLE);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FloatToByteBufferAdapter.java b/luni/src/main/java/java/nio/FloatToByteBufferAdapter.java
//Synthetic comment -- index 0ed944b..f7733c1 100644

//Synthetic comment -- @@ -92,7 +92,6 @@

@Override
public float get(int index) {
return byteBuffer.getFloat(index * SizeOf.FLOAT);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/IntToByteBufferAdapter.java b/luni/src/main/java/java/nio/IntToByteBufferAdapter.java
//Synthetic comment -- index 1af5f86..29aff41 100644

//Synthetic comment -- @@ -93,7 +93,6 @@

@Override
public int get(int index) {
return byteBuffer.getInt(index * SizeOf.INT);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/LongToByteBufferAdapter.java b/luni/src/main/java/java/nio/LongToByteBufferAdapter.java
//Synthetic comment -- index e8bf8df..7fb033b 100644

//Synthetic comment -- @@ -93,7 +93,6 @@

@Override
public long get(int index) {
return byteBuffer.getLong(index * SizeOf.LONG);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/ShortToByteBufferAdapter.java b/luni/src/main/java/java/nio/ShortToByteBufferAdapter.java
//Synthetic comment -- index ee36709..e53e355 100644

//Synthetic comment -- @@ -92,7 +92,6 @@

@Override
public short get(int index) {
return byteBuffer.getShort(index * SizeOf.SHORT);
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/BufferTest.java b/luni/src/test/java/libcore/java/nio/BufferTest.java
//Synthetic comment -- index 06a8e94..41ee3f7 100644

//Synthetic comment -- @@ -675,4 +675,74 @@
}
assertFalse(bb.hasArray());
}

    public void testBug6085292() {
        ByteBuffer b = ByteBuffer.allocateDirect(1);

        try {
            b.asCharBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asCharBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            b.asDoubleBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asDoubleBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            b.asFloatBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asFloatBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            b.asIntBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asIntBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            b.asLongBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asLongBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            b.asShortBuffer().get();
            fail();
        } catch (BufferUnderflowException expected) {
        }
        try {
            b.asShortBuffer().get(0);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
    }
}







