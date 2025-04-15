/*FileChannelImpl: Don't call truncate on non-regular files

Truncate only works on regular files.  On other files, such as
UNIX character devices, truncate returns an error, which
causes an IOException to be thrown.

This change adds support for creating a MappedByteBuffer on
UNIX character devices such as /dev/zero, if the underlying
device supports mmap().

Change-Id:I094371d821b187abe3da32edc411ff76a81b047e*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FileChannelImpl.java b/luni/src/main/java/java/nio/FileChannelImpl.java
//Synthetic comment -- index 075243a..90f4de0 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import libcore.io.ErrnoException;
import libcore.io.IoUtils;
import libcore.io.Libcore;
import libcore.io.StructFlock;
import libcore.util.MutableLong;
import static libcore.io.OsConstants.*;
//Synthetic comment -- @@ -230,7 +231,7 @@
} else if (accessMode == O_WRONLY) {
throw new NonReadableChannelException();
}
        if (position + size > size()) {
// We can't defer to FileChannel.truncate because that will only make a file shorter,
// and we only care about making our backing file longer here.
try {
//Synthetic comment -- @@ -349,6 +350,14 @@
}
}

public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
checkOpen();
if (!src.isOpen()) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/BufferTest.java b/luni/src/test/java/libcore/java/nio/BufferTest.java
//Synthetic comment -- index 2a895fc..4c500a5 100644

//Synthetic comment -- @@ -35,6 +35,62 @@
return result;
}

public void testByteSwappedBulkGetDirect() throws Exception {
testByteSwappedBulkGet(ByteBuffer.allocateDirect(10));
}







