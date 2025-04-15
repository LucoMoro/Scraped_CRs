/*Repair the damage I did removing "useless" checkIndex calls from the asXBuffer adapter classes.

The checkIndex calls were unnecessary as far as throwing the right Exception
subclass goes, but they're necessary if you want the detail message to use
the units of the wrapping buffer rather than the wrapped buffer. And we want
that. An IndexOutOfBoundsException that says "offset=0, limit=1" is only
going to confuse developers.

(Fixes bug introduced by 26841c0bb838d54fea2e1a06390e413fab561e27.)

Bug: 6085292
Change-Id:I9a1a5991ad383b7712642e40bced9af91440bed2*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/CharToByteBufferAdapter.java b/luni/src/main/java/java/nio/CharToByteBufferAdapter.java
//Synthetic comment -- index 86c7186..b9100a2 100644

//Synthetic comment -- @@ -93,6 +93,7 @@

@Override
public char get(int index) {
return byteBuffer.getChar(index * SizeOf.CHAR);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/DoubleToByteBufferAdapter.java b/luni/src/main/java/java/nio/DoubleToByteBufferAdapter.java
//Synthetic comment -- index ead4ef0..8b1e084 100644

//Synthetic comment -- @@ -93,6 +93,7 @@

@Override
public double get(int index) {
return byteBuffer.getDouble(index * SizeOf.DOUBLE);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FloatToByteBufferAdapter.java b/luni/src/main/java/java/nio/FloatToByteBufferAdapter.java
//Synthetic comment -- index f7733c1..0ed944b 100644

//Synthetic comment -- @@ -92,6 +92,7 @@

@Override
public float get(int index) {
return byteBuffer.getFloat(index * SizeOf.FLOAT);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/IntToByteBufferAdapter.java b/luni/src/main/java/java/nio/IntToByteBufferAdapter.java
//Synthetic comment -- index 29aff41..1af5f86 100644

//Synthetic comment -- @@ -93,6 +93,7 @@

@Override
public int get(int index) {
return byteBuffer.getInt(index * SizeOf.INT);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/LongToByteBufferAdapter.java b/luni/src/main/java/java/nio/LongToByteBufferAdapter.java
//Synthetic comment -- index 7fb033b..e8bf8df 100644

//Synthetic comment -- @@ -93,6 +93,7 @@

@Override
public long get(int index) {
return byteBuffer.getLong(index * SizeOf.LONG);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/ShortToByteBufferAdapter.java b/luni/src/main/java/java/nio/ShortToByteBufferAdapter.java
//Synthetic comment -- index e53e355..ee36709 100644

//Synthetic comment -- @@ -92,6 +92,7 @@

@Override
public short get(int index) {
return byteBuffer.getShort(index * SizeOf.SHORT);
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/BufferTest.java b/luni/src/test/java/libcore/java/nio/BufferTest.java
//Synthetic comment -- index 41ee3f7..2a895fc 100644

//Synthetic comment -- @@ -688,6 +688,7 @@
b.asCharBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}

try {
//Synthetic comment -- @@ -699,6 +700,7 @@
b.asDoubleBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}

try {
//Synthetic comment -- @@ -710,6 +712,7 @@
b.asFloatBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}

try {
//Synthetic comment -- @@ -721,6 +724,7 @@
b.asIntBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}

try {
//Synthetic comment -- @@ -732,6 +736,7 @@
b.asLongBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}

try {
//Synthetic comment -- @@ -743,6 +748,7 @@
b.asShortBuffer().get(0);
fail();
} catch (IndexOutOfBoundsException expected) {
}
}
}







