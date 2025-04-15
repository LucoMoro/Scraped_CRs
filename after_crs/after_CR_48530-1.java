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
import libcore.io.OsConstants;
import libcore.io.StructFlock;
import libcore.util.MutableLong;
import static libcore.io.OsConstants.*;
//Synthetic comment -- @@ -230,7 +231,7 @@
} else if (accessMode == O_WRONLY) {
throw new NonReadableChannelException();
}
        if (isRegularFile() && (position + size > size())) {
// We can't defer to FileChannel.truncate because that will only make a file shorter,
// and we only care about making our backing file longer here.
try {
//Synthetic comment -- @@ -349,6 +350,14 @@
}
}

    private boolean isRegularFile() throws IOException {
        try {
            return OsConstants.S_ISREG(Libcore.os.fstat(fd).st_mode);
        } catch (ErrnoException errnoException) {
            throw errnoException.rethrowAsIOException();
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







