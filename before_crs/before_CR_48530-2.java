/*FileChannelImpl: Ignore ftruncate failures on non-regular files

Truncate only works on regular files.  On other files, such as
UNIX character devices, truncate returns EINVAL, which
causes an IOException to be thrown.

With this change, we now have support for creating a MappedByteBuffer
on UNIX character devices such as /dev/zero, if the underlying
device node supports mmap().

Change-Id:I094371d821b187abe3da32edc411ff76a81b047e*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/FileChannelImpl.java b/luni/src/main/java/java/nio/FileChannelImpl.java
//Synthetic comment -- index 075243a..98be748 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import java.util.SortedSet;
import java.util.TreeSet;
import libcore.io.ErrnoException;
import libcore.io.IoUtils;
import libcore.io.Libcore;
import libcore.io.StructFlock;
import libcore.util.MutableLong;
//Synthetic comment -- @@ -236,7 +235,13 @@
try {
Libcore.os.ftruncate(fd, position + size);
} catch (ErrnoException errnoException) {
                throw errnoException.rethrowAsIOException();
}
}
long alignment = position - position % Libcore.os.sysconf(_SC_PAGE_SIZE);








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/BufferTest.java b/luni/src/test/java/libcore/java/nio/BufferTest.java
//Synthetic comment -- index 2a895fc..4c500a5 100644

//Synthetic comment -- @@ -35,6 +35,62 @@
return result;
}

public void testByteSwappedBulkGetDirect() throws Exception {
testByteSwappedBulkGet(ByteBuffer.allocateDirect(10));
}







