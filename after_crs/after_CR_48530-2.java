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
import libcore.io.Libcore;
import libcore.io.StructFlock;
import libcore.util.MutableLong;
//Synthetic comment -- @@ -236,7 +235,13 @@
try {
Libcore.os.ftruncate(fd, position + size);
} catch (ErrnoException errnoException) {
                // EINVAL can be thrown if we're dealing with non-regular
                // files, for example, character devices such as /dev/zero.
                // In those cases, we ignore the failed truncation and
                // continue on.
                if (errnoException.errno != EINVAL) {
                    throw errnoException.rethrowAsIOException();
                }
}
}
long alignment = position - position % Libcore.os.sysconf(_SC_PAGE_SIZE);








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/BufferTest.java b/luni/src/test/java/libcore/java/nio/BufferTest.java
//Synthetic comment -- index 2a895fc..4c500a5 100644

//Synthetic comment -- @@ -35,6 +35,62 @@
return result;
}

    /**
     * Try to create a {@link MappedByteBuffer} from /dev/zero, to see if
     * we support mapping UNIX character devices.
     */
    public void testDevZeroMap() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("/dev/zero", "r");
        try {
            MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, 65536);

            // Create an array initialized to all "(byte) 1"
            byte[] buf1 = new byte[65536];
            Arrays.fill(buf1, (byte) 1);

            // Read from mapped /dev/zero, and overwrite this array.
            mbb.get(buf1);

            // Verify that everything is zero
            for (int i = 0; i < 65536; i++) {
                assertEquals((byte) 0, buf1[i]);
            }
        } finally {
            raf.close();
        }
    }

    /**
     * Same as {@link libcore.java.nio.BufferTest#testDevZeroMap()}, but try to see
     * if we can write to the UNIX character device.
     */
    public void testDevZeroMapRW() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("/dev/zero", "rw");
        try {
            MappedByteBuffer mbb = raf.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 65536, 131072);

            // Create an array initialized to all "(byte) 1"
            byte[] buf1 = new byte[65536];
            Arrays.fill(buf1, (byte) 1);

            // Put all "(byte) 1"s into the /dev/zero MappedByteBuffer.
            mbb.put(buf1);

            mbb.position(0);

            byte[] buf2 = new byte[65536];
            mbb.get(buf2);

            // Verify that everything is one
            for (int i = 0; i < 65536; i++) {
                assertEquals((byte) 1, buf2[i]);
            }
        } finally {
            raf.close();
        }
    }

public void testByteSwappedBulkGetDirect() throws Exception {
testByteSwappedBulkGet(ByteBuffer.allocateDirect(10));
}







