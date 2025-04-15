/*Improve nio IllegalArgumentException detail messages.

Bug: 7005326
Change-Id:Ibab8b776865dbed5da062cc683f834a79f068b32*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/Buffer.java b/luni/src/main/java/java/nio/Buffer.java
//Synthetic comment -- index c3840a5..b90744b 100644

//Synthetic comment -- @@ -271,7 +271,7 @@

final void checkWritable() {
if (isReadOnly()) {
            throw new IllegalArgumentException("read-only buffer");
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/ByteBuffer.java b/luni/src/main/java/java/nio/ByteBuffer.java
//Synthetic comment -- index ef725c1..6a3624a 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
*/
public static ByteBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteHeapByteBuffer(capacity);
}
//Synthetic comment -- @@ -63,7 +63,7 @@
*/
public static ByteBuffer allocateDirect(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteDirectByteBuffer(capacity);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/CharBuffer.java b/luni/src/main/java/java/nio/CharBuffer.java
//Synthetic comment -- index 03bac04..6429ae2 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
*/
public static CharBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteCharArrayBuffer(capacity);
}
//Synthetic comment -- @@ -500,7 +500,7 @@
*/
public CharBuffer put(CharBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();
//Synthetic comment -- @@ -734,7 +734,7 @@
if (remaining == 0) {
return -1;
}
            throw new IllegalArgumentException();
}
if (remaining == 0) {
return limit > 0 && target.remaining() == 0 ? 0 : -1;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/DoubleBuffer.java b/luni/src/main/java/java/nio/DoubleBuffer.java
//Synthetic comment -- index c495592..8d90f89 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
*/
public static DoubleBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteDoubleArrayBuffer(capacity);
}
//Synthetic comment -- @@ -438,7 +438,7 @@
*/
public DoubleBuffer put(DoubleBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FileChannelImpl.java b/luni/src/main/java/java/nio/FileChannelImpl.java
//Synthetic comment -- index 76bad51..075243a 100644

//Synthetic comment -- @@ -442,7 +442,7 @@
public FileChannel truncate(long size) throws IOException {
checkOpen();
if (size < 0) {
            throw new IllegalArgumentException("size: " + size);
}
checkWritable();
if (size < size()) {
//Synthetic comment -- @@ -457,7 +457,7 @@

public int write(ByteBuffer buffer, long position) throws IOException {
if (position < 0) {
            throw new IllegalArgumentException("position: " + position);
}
return writeImpl(buffer, position);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FloatBuffer.java b/luni/src/main/java/java/nio/FloatBuffer.java
//Synthetic comment -- index ec361d6..814eb53 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
*/
public static FloatBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteFloatArrayBuffer(capacity);
}
//Synthetic comment -- @@ -437,7 +437,7 @@
*/
public FloatBuffer put(FloatBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/IntBuffer.java b/luni/src/main/java/java/nio/IntBuffer.java
//Synthetic comment -- index 9cc05ff..0ff758a 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
*/
public static IntBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteIntArrayBuffer(capacity);
}
//Synthetic comment -- @@ -423,7 +423,7 @@
*/
public IntBuffer put(IntBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/LongBuffer.java b/luni/src/main/java/java/nio/LongBuffer.java
//Synthetic comment -- index 27edd2e..1254ddb 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
*/
public static LongBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteLongArrayBuffer(capacity);
}
//Synthetic comment -- @@ -427,7 +427,7 @@
*/
public LongBuffer put(LongBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/MappedByteBuffer.java b/luni/src/main/java/java/nio/MappedByteBuffer.java
//Synthetic comment -- index 39c4986..0e8bf09 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
MappedByteBuffer(ByteBuffer directBuffer) {
super(directBuffer.capacity, directBuffer.block);
if (!directBuffer.isDirect()) {
            throw new IllegalArgumentException();
}
this.wrapped = (DirectByteBuffer) directBuffer;
this.mapMode = null;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/SelectorImpl.java b/luni/src/main/java/java/nio/SelectorImpl.java
//Synthetic comment -- index 05a6497..02fdf54 100644

//Synthetic comment -- @@ -150,7 +150,7 @@

@Override public int select(long timeout) throws IOException {
if (timeout < 0) {
            throw new IllegalArgumentException();
}
// Our timeout is interpreted differently to Unix's --- 0 means block. See selectNow.
return selectInternal((timeout == 0) ? -1 : timeout);








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/ShortBuffer.java b/luni/src/main/java/java/nio/ShortBuffer.java
//Synthetic comment -- index 052cf6b..d12a49e 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
*/
public static ShortBuffer allocate(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
return new ReadWriteShortArrayBuffer(capacity);
}
//Synthetic comment -- @@ -426,7 +426,7 @@
*/
public ShortBuffer put(ShortBuffer src) {
if (src == this) {
            throw new IllegalArgumentException();
}
if (src.remaining() > remaining()) {
throw new BufferOverflowException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/SocketChannelImpl.java b/luni/src/main/java/java/nio/SocketChannelImpl.java
//Synthetic comment -- index 9b26812..a9d9c53 100644

//Synthetic comment -- @@ -412,7 +412,7 @@
*/
static InetSocketAddress validateAddress(SocketAddress socketAddress) {
if (socketAddress == null) {
            throw new IllegalArgumentException();
}
if (!(socketAddress instanceof InetSocketAddress)) {
throw new UnsupportedAddressTypeException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/channels/FileLock.java b/luni/src/main/java/java/nio/channels/FileLock.java
//Synthetic comment -- index 0916be0..4cdcc27 100644

//Synthetic comment -- @@ -98,7 +98,7 @@
*/
protected FileLock(FileChannel channel, long position, long size, boolean shared) {
if (position < 0 || size < 0 || position + size < 0) {
            throw new IllegalArgumentException();
}
this.channel = channel;
this.position = position;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/charset/CharsetDecoder.java b/luni/src/main/java/java/nio/charset/CharsetDecoder.java
//Synthetic comment -- index f67dbbc..1559db4 100644

//Synthetic comment -- @@ -590,7 +590,7 @@
*/
public final CharsetDecoder onMalformedInput(CodingErrorAction newAction) {
if (newAction == null) {
            throw new IllegalArgumentException();
}
malformedInputAction = newAction;
implOnMalformedInput(newAction);
//Synthetic comment -- @@ -612,7 +612,7 @@
*/
public final CharsetDecoder onUnmappableCharacter(CodingErrorAction newAction) {
if (newAction == null) {
            throw new IllegalArgumentException();
}
unmappableCharacterAction = newAction;
implOnUnmappableCharacter(newAction);








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/charset/CharsetEncoder.java b/luni/src/main/java/java/nio/charset/CharsetEncoder.java
//Synthetic comment -- index 28b2cb8..8f02f96 100644

//Synthetic comment -- @@ -706,11 +706,11 @@
throw new IllegalArgumentException("replacement.length == 0");
}
if (replacement.length > maxBytesPerChar()) {
            throw new IllegalArgumentException("replacement length > maxBytesPerChar: " +
replacement.length + " > " + maxBytesPerChar());
}
if (!isLegalReplacement(replacement)) {
            throw new IllegalArgumentException("bad replacement: " + Arrays.toString(replacement));
}
// It seems like a bug, but the RI doesn't clone, and we have tests that check we don't.
this.replacementBytes = replacement;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/charset/CoderResult.java b/luni/src/main/java/java/nio/charset/CoderResult.java
//Synthetic comment -- index 221cb32..3cc2673 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
return r;
}
}
        throw new IllegalArgumentException("Length must be greater than 0; was " + length);
}

/**
//Synthetic comment -- @@ -149,7 +149,7 @@
return r;
}
}
        throw new IllegalArgumentException("Length must be greater than 0; was " + length);
}

/**







