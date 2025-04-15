/*Clean up ZipFile a little, fix a few bugs.

The main bug fixed is fixing support for .zip files larger than 2GiB (where
the central directory offset's top bit is set).

We were checking character counts rather than byte counts in several places
too, which could lead to corrupt zip files.

I've also added a comment to readCentralDirectory because I keep coming back
to this code, wanting to make it lazy, and never remember (until I've done
half the work) why that's not possible.

Bug:http://code.google.com/p/android/issues/detail?id=36187Change-Id:Iaa8eadc501ead7c70528bd9063d5893a325dcea1*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterInputStream.java b/luni/src/main/java/java/util/zip/InflaterInputStream.java
//Synthetic comment -- index 397637e..371c80a 100644

//Synthetic comment -- @@ -189,13 +189,8 @@
protected void fill() throws IOException {
checkClosed();
if (nativeEndBufSize > 0) {
            ZipFile.RAFStream is = (ZipFile.RAFStream) in;
            len = is.fill(inf, nativeEndBufSize);
} else {
if ((len = in.read(buf)) > 0) {
inf.setInput(buf, 0, len);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index 3e58727..8ff9ffc 100644

//Synthetic comment -- @@ -40,11 +40,16 @@
* @see ZipOutputStream
*/
public class ZipEntry implements ZipConstants, Cloneable {
    String name;
    String comment;

    long compressedSize = -1;
    long crc = -1;
    long size = -1;

    int compressionMethod = -1;
    int time = -1;
    int modDate = -1;

byte[] extra;

//Synthetic comment -- @@ -80,11 +85,8 @@
}

/**
     * Returns the comment for this {@code ZipEntry}, or {@code null} if there is no comment.
     * If we're reading an archive with {@code ZipInputStream} the comment is not available.
*/
public String getComment() {
return comment;
//Synthetic comment -- @@ -178,13 +180,17 @@

/**
* Sets the comment for this {@code ZipEntry}.
     * @throws IllegalArgumentException if the comment is longer than 64 KiB.
*/
public void setComment(String comment) {
        if (comment == null) {
            this.comment = null;
            return;
        }

        byte[] commentBytes = comment.getBytes(Charsets.UTF_8);
        if (commentBytes.length > 0xffff) {
            throw new IllegalArgumentException("Comment too long: " + commentBytes.length);
}
this.comment = comment;
}
//Synthetic comment -- @@ -221,7 +227,7 @@
* @param data
*            a byte array containing the extra information.
* @throws IllegalArgumentException
     *             when the length of data is greater than 64 KiB.
*/
public void setExtra(byte[] data) {
if (data != null && data.length > 0xffff) {
//Synthetic comment -- @@ -369,7 +375,7 @@

nameLength = it.readShort();
int extraLength = it.readShort();
        int commentByteCount = it.readShort();

// This is a 32-bit value in the file, but a 64-bit field in this object.
it.seek(42);
//Synthetic comment -- @@ -381,9 +387,9 @@

// The RI has always assumed UTF-8. (If GPBF_UTF8_FLAG isn't set, the encoding is
// actually IBM-437.)
        if (commentByteCount > 0) {
            byte[] commentBytes = new byte[commentByteCount];
            Streams.readFully(in, commentBytes, 0, commentByteCount);
comment = new String(commentBytes, 0, commentBytes.length, Charsets.UTF_8);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipFile.java b/luni/src/main/java/java/util/zip/ZipFile.java
//Synthetic comment -- index 20bdf42..8508767 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
static final int GPBF_UTF8_FLAG = 1 << 11;

/**
     * Open ZIP file for reading.
*/
public static final int OPEN_READ = 1;

//Synthetic comment -- @@ -79,9 +79,9 @@
*/
public static final int OPEN_DELETE = 4;

    private final String mFilename;

    private File mFileToDeleteOnClose;

private RandomAccessFile mRaf;

//Synthetic comment -- @@ -90,61 +90,49 @@
private final CloseGuard guard = CloseGuard.get();

/**
     * Constructs a new {@code ZipFile} allowing read access to the contents of the given file.
     * @throws ZipException if a ZIP error occurs.
     * @throws IOException if an {@code IOException} occurs.
*/
public ZipFile(File file) throws ZipException, IOException {
this(file, OPEN_READ);
}

/**
     * Constructs a new {@code ZipFile} allowing read access to the contents of the given file.
     * @throws IOException if an IOException occurs.
     */
    public ZipFile(String name) throws IOException {
        this(new File(name), OPEN_READ);
    }

    /**
     * Constructs a new {@code ZipFile} allowing access to the given file.
     * The {@code mode} must be either {@code OPEN_READ} or {@code OPEN_READ|OPEN_DELETE}.
     * If the {@code OPEN_DELETE} flag is supplied, the file will be deleted at or before the
     * time that the {@code ZipFile} is closed (the contents will remain accessible until
     * this {@code ZipFile} is closed).
     * @throws IOException if an {@code IOException} occurs.
*/
public ZipFile(File file, int mode) throws IOException {
        mFilename = file.getPath();
if (mode != OPEN_READ && mode != (OPEN_READ | OPEN_DELETE)) {
throw new IllegalArgumentException("Bad mode: " + mode);
}

if ((mode & OPEN_DELETE) != 0) {
            mFileToDeleteOnClose = file;
            mFileToDeleteOnClose.deleteOnExit();
} else {
            mFileToDeleteOnClose = null;
}

        mRaf = new RandomAccessFile(mFilename, "r");

readCentralDir();
guard.open("close");
}

@Override protected void finalize() throws IOException {
try {
if (guard != null) {
//Synthetic comment -- @@ -174,9 +162,9 @@
mRaf = null;
raf.close();
}
            if (mFileToDeleteOnClose != null) {
                mFileToDeleteOnClose.delete();
                mFileToDeleteOnClose = null;
}
}
}
//Synthetic comment -- @@ -257,19 +245,19 @@
// position of the entry's local header. At position 28 we find the
// length of the extra data. In some cases this length differs from
// the one coming in the central header.
            RAFStream rafStream = new RAFStream(raf, entry.mLocalHeaderRelOffset + 28);
            DataInputStream is = new DataInputStream(rafStream);
int localExtraLenOrWhatever = Short.reverseBytes(is.readShort());
is.close();

// Skip the name and this "extra" data or whatever it is:
            rafStream.skip(entry.nameLength + localExtraLenOrWhatever);
            rafStream.mLength = rafStream.mOffset + entry.compressedSize;
if (entry.compressionMethod == ZipEntry.DEFLATED) {
int bufSize = Math.max(1024, (int)Math.min(entry.getSize(), 65535L));
                return new ZipInflaterInputStream(rafStream, new Inflater(true), bufSize, entry);
} else {
                return rafStream;
}
}
}
//Synthetic comment -- @@ -280,7 +268,7 @@
* @return the file name of this {@code ZipFile}.
*/
public String getName() {
        return mFilename;
}

/**
//Synthetic comment -- @@ -351,18 +339,21 @@
int numEntries = it.readShort() & 0xffff;
int totalNumEntries = it.readShort() & 0xffff;
it.skip(4); // Ignore centralDirSize.
        long centralDirOffset = ((long) it.readInt()) & 0xffffffffL;

if (numEntries != totalNumEntries || diskNumber != 0 || diskWithCentralDir != 0) {
throw new ZipException("spanned archives not supported");
}

// Seek to the first CDE and read all entries.
        // We have to do this now (from the constructor) rather than lazily because the
        // public API doesn't allow us to throw IOException except from the constructor
        // or from getInputStream.
        RAFStream rafStream = new RAFStream(mRaf, centralDirOffset);
        BufferedInputStream bufferedStream = new BufferedInputStream(rafStream, 4096);
byte[] hdrBuf = new byte[CENHDR]; // Reuse the same buffer for each entry.
for (int i = 0; i < numEntries; ++i) {
            ZipEntry newEntry = new ZipEntry(hdrBuf, bufferedStream);
mEntries.put(newEntry.getName(), newEntry);
}
}
//Synthetic comment -- @@ -376,14 +367,13 @@
* <p>We could support mark/reset, but we don't currently need them.
*/
static class RAFStream extends InputStream {
        private RandomAccessFile mSharedRaf;
        private long mOffset;
        private long mLength;

        public RAFStream(RandomAccessFile raf, long offset) throws IOException {
mSharedRaf = raf;
            mOffset = offset;
mLength = raf.length();
}

//Synthetic comment -- @@ -411,28 +401,34 @@
}
}

        @Override public long skip(long byteCount) throws IOException {
if (byteCount > mLength - mOffset) {
byteCount = mLength - mOffset;
}
mOffset += byteCount;
return byteCount;
}

        public int fill(Inflater inflater, int nativeEndBufSize) throws IOException {
            synchronized (mSharedRaf) {
                int len = Math.min((int) (mLength - mOffset), nativeEndBufSize);
                int cnt = inflater.setFileInput(mSharedRaf.getFD(), mOffset, nativeEndBufSize);
                skip(cnt);
                return len;
            }
        }
}

static class ZipInflaterInputStream extends InflaterInputStream {
        private ZipEntry entry;
        private long bytesRead = 0;

public ZipInflaterInputStream(InputStream is, Inflater inf, int bsize, ZipEntry entry) {
super(is, inf, bsize);
this.entry = entry;
}

        @Override public int read(byte[] buffer, int off, int nbytes) throws IOException {
int i = super.read(buffer, off, nbytes);
if (i != -1) {
bytesRead += i;
//Synthetic comment -- @@ -440,8 +436,7 @@
return i;
}

        @Override public int available() throws IOException {
if (closed) {
// Our superclass will throw an exception, but there's a jtreg test that
// explicitly checks that the InputStream returned from ZipFile.getInputStream








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipOutputStream.java b/luni/src/main/java/java/util/zip/ZipOutputStream.java
//Synthetic comment -- index 77a993b..9b88291 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.nio.charset.Charsets;
import java.util.Arrays;
import java.util.HashSet;
import libcore.util.EmptyArray;

/**
* This class provides an implementation of {@code FilterOutputStream} that
//Synthetic comment -- @@ -76,13 +77,13 @@

private static final int ZIPLocalHeaderVersionNeeded = 20;

    private byte[] commentBytes = EmptyArray.BYTE;

private final HashSet<String> entries = new HashSet<String>();

    private int defaultCompressionMethod = DEFLATED;

    private int compressionLevel = Deflater.DEFAULT_COMPRESSION;

private ByteArrayOutputStream cDir = new ByteArrayOutputStream();

//Synthetic comment -- @@ -95,14 +96,11 @@
private byte[] nameBytes;

/**
     * Constructs a new {@code ZipOutputStream} that writes a zip file
     * to the given {@code OutputStream}.
*/
    public ZipOutputStream(OutputStream os) {
        super(os, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
}

/**
//Synthetic comment -- @@ -131,7 +129,7 @@
*             If an error occurs closing the entry.
*/
public void closeEntry() throws IOException {
        checkOpen();
if (currentEntry == null) {
return;
}
//Synthetic comment -- @@ -186,12 +184,13 @@
} else {
writeShort(cDir, 0);
}

        String comment = currentEntry.getComment();
        byte[] commentBytes = EmptyArray.BYTE;
        if (comment != null) {
            commentBytes = comment.getBytes(Charsets.UTF_8);
}
        writeShort(cDir, commentBytes.length); // Comment length.
writeShort(cDir, 0); // Disk Start
writeShort(cDir, 0); // Internal File Attributes
writeLong(cDir, 0); // External File Attributes
//Synthetic comment -- @@ -202,8 +201,8 @@
cDir.write(currentEntry.extra);
}
offset += curOffset;
        if (commentBytes.length > 0) {
            cDir.write(commentBytes);
}
currentEntry = null;
crc.reset();
//Synthetic comment -- @@ -220,7 +219,7 @@
*/
@Override
public void finish() throws IOException {
        // TODO: is there a bug here? why not checkOpen?
if (out == null) {
throw new IOException("Stream is closed");
}
//Synthetic comment -- @@ -242,11 +241,9 @@
writeShort(cDir, entries.size()); // Number of entries
writeLong(cDir, cdirSize); // Size of central dir
writeLong(cDir, offset); // Offset of central dir
        writeShort(cDir, commentBytes.length);
        if (commentBytes.length > 0) {
            cDir.write(commentBytes);
}
// Write the central directory.
cDir.writeTo(out);
//Synthetic comment -- @@ -269,18 +266,33 @@
if (currentEntry != null) {
closeEntry();
}

        // Did this ZipEntry specify a method, or should we use the default?
        int method = ze.getMethod();
        if (method == -1) {
            method = defaultCompressionMethod;
        }

        // If the method is STORED, check that the ZipEntry was configured appropriately.
        if (method == STORED) {
            if (ze.getCompressedSize() == -1) {
                ze.setCompressedSize(ze.getSize());
            } else if (ze.getSize() == -1) {
                ze.setSize(ze.getCompressedSize());
}
            if (ze.getCrc() == -1) {
                throw new ZipException("STORED entry missing CRC");
}
            if (ze.getSize() == -1) {
                throw new ZipException("STORED entry missing size");
            }
            if (ze.size != ze.compressedSize) {
                throw new ZipException("STORED entry size/compressed size mismatch");
}
}

        checkOpen();

if (entries.contains(ze.name)) {
throw new ZipException("Entry already exists: " + ze.name);
}
//Synthetic comment -- @@ -294,16 +306,15 @@
throw new IllegalArgumentException("Name too long: " + nameLength + " UTF-8 bytes");
}

        def.setLevel(compressionLevel);
        ze.setMethod(method);

currentEntry = ze;
entries.add(currentEntry.name);

// Local file header.
// http://www.pkware.com/documents/casestudies/APPNOTE.TXT
        int flags = (method == STORED) ? 0 : ZipFile.GPBF_DATA_DESCRIPTOR_FLAG;
// Java always outputs UTF-8 filenames. (Before Java 7, the RI didn't set this flag and used
// modified UTF-8. From Java 7, it sets this flag and uses normal UTF-8.)
flags |= ZipFile.GPBF_UTF8_FLAG;
//Synthetic comment -- @@ -344,16 +355,20 @@
}

/**
     * Sets the comment associated with the file being written.
     * @throws IllegalArgumentException if the comment is longer than 64 KiB.
*/
public void setComment(String comment) {
        if (comment == null) {
            this.commentBytes = null;
            return;
}

        byte[] newCommentBytes = comment.getBytes(Charsets.UTF_8);
        if (newCommentBytes.length > 0xffff) {
            throw new IllegalArgumentException("Comment too long: " + newCommentBytes.length + " bytes");
        }
        this.commentBytes = newCommentBytes;
}

/**
//Synthetic comment -- @@ -369,7 +384,7 @@
if (level < Deflater.DEFAULT_COMPRESSION || level > Deflater.BEST_COMPRESSION) {
throw new IllegalArgumentException("Bad level: " + level);
}
        compressionLevel = level;
}

/**
//Synthetic comment -- @@ -384,7 +399,7 @@
if (method != STORED && method != DEFLATED) {
throw new IllegalArgumentException("Bad method: " + method);
}
        defaultCompressionMethod = method;
}

private long writeLong(OutputStream os, long i) throws IOException {
//Synthetic comment -- @@ -423,7 +438,7 @@
crc.update(buffer, offset, byteCount);
}

    private void checkOpen() throws IOException {
if (cDir == null) {
throw new IOException("Stream is closed");
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java b/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java
//Synthetic comment -- index 7e8286e..08a575f 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
//Synthetic comment -- @@ -32,7 +33,6 @@
import junit.framework.TestCase;

public final class ZipFileTest extends TestCase {
/**
* Exercise Inflater's ability to refill the zlib's input buffer. As of this
* writing, this buffer's max size is 64KiB compressed bytes. We'll write a
//Synthetic comment -- @@ -42,7 +42,7 @@
public void testInflatingFilesRequiringZipRefill() throws IOException {
int originalSize = 1024 * 1024;
byte[] readBuffer = new byte[8192];
        ZipFile zipFile = new ZipFile(createZipFile(1, originalSize));
for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
ZipEntry zipEntry = e.nextElement();
assertTrue("This test needs >64 KiB of compressed data to exercise Inflater",
//Synthetic comment -- @@ -57,72 +57,281 @@
public void testInflatingStreamsRequiringZipRefill() throws IOException {
int originalSize = 1024 * 1024;
byte[] readBuffer = new byte[8192];
        ZipInputStream in = new ZipInputStream(new FileInputStream(createZipFile(1, originalSize)));
while (in.getNextEntry() != null) {
while (in.read(readBuffer, 0, readBuffer.length) != -1) {}
}
in.close();
}

    public void testZipFileWithLotsOfEntries() throws IOException {
        int expectedEntryCount = 64*1024 - 1;
        File f = createZipFile(expectedEntryCount, 0);
        ZipFile zipFile = new ZipFile(f);
        int entryCount = 0;
        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
            ZipEntry zipEntry = e.nextElement();
            ++entryCount;
        }
        assertEquals(expectedEntryCount, entryCount);
        zipFile.close();
    }

    // http://code.google.com/p/android/issues/detail?id=36187
    public void testZipFileLargerThan2GiB() throws IOException {
        if (false) { // TODO: this test requires too much time and too much disk space!
            File f = createZipFile(1024, 3*1024*1024);
            ZipFile zipFile = new ZipFile(f);
            int entryCount = 0;
            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
                ZipEntry zipEntry = e.nextElement();
                ++entryCount;
            }
            assertEquals(1024, entryCount);
            zipFile.close();
        }
    }

    public void testZip64Support() throws IOException {
        try {
            createZipFile(64*1024, 0);
            fail(); // Make this test more like testHugeZipFile when we have Zip64 support.
        } catch (ZipException expected) {
        }
    }

    /**
     * Compresses the given number of files, each of the given size, into a .zip archive.
     */
    private File createZipFile(int entryCount, int entrySize) throws IOException {
        File result = createTemporaryZipFile();

byte[] writeBuffer = new byte[8192];
Random random = new Random();

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(result)));
        for (int entry = 0; entry < entryCount; ++entry) {
            ZipEntry ze = new ZipEntry(Integer.toHexString(entry));
            out.putNextEntry(ze);

            for (int i = 0; i < entrySize; i += writeBuffer.length) {
                random.nextBytes(writeBuffer);
                int byteCount = Math.min(writeBuffer.length, entrySize - i);
                out.write(writeBuffer, 0, byteCount);
            }

            out.closeEntry();
}

out.close();
return result;
    }

    private File createTemporaryZipFile() throws IOException {
        File result = File.createTempFile("ZipFileTest", "zip");
        result.deleteOnExit();
        return result;
    }

    private ZipOutputStream createZipOutputStream(File f) throws IOException {
        return new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
    }

    public void testSTORED() throws IOException {
        ZipOutputStream out = createZipOutputStream(createTemporaryZipFile());
        CRC32 crc = new CRC32();

        // Missing CRC, size, and compressed size => failure.
        try {
            ZipEntry ze = new ZipEntry("a");
            ze.setMethod(ZipEntry.STORED);
            out.putNextEntry(ze);
            fail();
        } catch (ZipException expected) {
            expected.printStackTrace();
        }

        // Missing CRC and compressed size => failure.
        try {
            ZipEntry ze = new ZipEntry("a");
            ze.setMethod(ZipEntry.STORED);
            ze.setSize(0);
            out.putNextEntry(ze);
            fail();
        } catch (ZipException expected) {
            expected.printStackTrace();
        }

        // Missing CRC and size => failure.
        try {
            ZipEntry ze = new ZipEntry("a");
            ze.setMethod(ZipEntry.STORED);
            ze.setSize(0);
            ze.setCompressedSize(0);
            out.putNextEntry(ze);
            fail();
        } catch (ZipException expected) {
            expected.printStackTrace();
        }

        // Missing size and compressed size => failure.
        try {
            ZipEntry ze = new ZipEntry("a");
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(crc.getValue());
            out.putNextEntry(ze);
            fail();
        } catch (ZipException expected) {
            expected.printStackTrace();
        }

        // Missing size is copied from compressed size.
        {
            ZipEntry ze = new ZipEntry("okay1");
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(crc.getValue());

            assertEquals(-1, ze.getSize());
            assertEquals(-1, ze.getCompressedSize());

            ze.setCompressedSize(0);

            assertEquals(-1, ze.getSize());
            assertEquals(0, ze.getCompressedSize());

            out.putNextEntry(ze);

            assertEquals(0, ze.getSize());
            assertEquals(0, ze.getCompressedSize());
        }

        // Missing compressed size is copied from size.
        {
            ZipEntry ze = new ZipEntry("okay2");
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(crc.getValue());

            assertEquals(-1, ze.getSize());
            assertEquals(-1, ze.getCompressedSize());

            ze.setSize(0);

            assertEquals(0, ze.getSize());
            assertEquals(-1, ze.getCompressedSize());

            out.putNextEntry(ze);

            assertEquals(0, ze.getSize());
            assertEquals(0, ze.getCompressedSize());
        }

        // Mismatched size and compressed size => failure.
        try {
            ZipEntry ze = new ZipEntry("a");
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(crc.getValue());
            ze.setCompressedSize(1);
            ze.setSize(0);
            out.putNextEntry(ze);
            fail();
        } catch (ZipException expected) {
        }

        // Everything present => success.
        ZipEntry ze = new ZipEntry("okay");
        ze.setMethod(ZipEntry.STORED);
        ze.setCrc(crc.getValue());
        ze.setSize(0);
        ze.setCompressedSize(0);
        out.putNextEntry(ze);

        out.close();
    }

    private String makeString(int count, String ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public void testComment() throws Exception {
        String expectedFileComment = "1 \u0666 2";
        String expectedEntryComment = "a \u0666 b";

        File file = createTemporaryZipFile();
        ZipOutputStream out = createZipOutputStream(file);

        // Is file comment length checking done on bytes or characters? (Should be bytes.)
        out.setComment(null);
        out.setComment(makeString(0xffff, "a"));
        try {
            out.setComment(makeString(0xffff + 1, "a"));
            fail();
        } catch (IllegalArgumentException expected) {
        }
        try {
            out.setComment(makeString(0xffff, "\u0666"));
            fail();
        } catch (IllegalArgumentException expected) {
        }

        ZipEntry ze = new ZipEntry("a");

        // Is entry comment length checking done on bytes or characters? (Should be bytes.)
        ze.setComment(null);
        ze.setComment(makeString(0xffff, "a"));
        try {
            ze.setComment(makeString(0xffff + 1, "a"));
            fail();
        } catch (IllegalArgumentException expected) {
        }
        try {
            ze.setComment(makeString(0xffff, "\u0666"));
            fail();
        } catch (IllegalArgumentException expected) {
        }

        ze.setComment(expectedEntryComment);
        out.putNextEntry(ze);
        out.closeEntry();

        out.setComment(expectedFileComment);
        out.close();

        ZipFile zipFile = new ZipFile(file);
        // TODO: there's currently no API for reading the file comment --- strings(1) the file?
        assertEquals(expectedEntryComment, zipFile.getEntry("a").getComment());
        zipFile.close();
    }

    public void testNameLengthChecks() throws IOException {
        // Is entry name length checking done on bytes or characters?
        // Really it should be bytes, but the RI only checks characters at construction time.
        // Android does the same, because it's cheap...
        try {
            new ZipEntry((String) null);
            fail();
        } catch (NullPointerException expected) {
        }
        new ZipEntry(makeString(0xffff, "a"));
        try {
            new ZipEntry(makeString(0xffff + 1, "a"));
            fail();
        } catch (IllegalArgumentException expected) {
        }

        // ...but Android won't let you create a zip file with a truncated name.
        ZipOutputStream out = createZipOutputStream(createTemporaryZipFile());
        ZipEntry ze = new ZipEntry(makeString(0xffff, "\u0666"));
        try {
            out.putNextEntry(ze);
            fail(); // The RI fails this test; it just checks the character count at construction time.
        } catch (IllegalArgumentException expected) {
        }
        out.closeEntry();
        out.putNextEntry(new ZipEntry("okay")); // ZipOutputStream.close throws if you add nothing!
        out.close();
    }
}







