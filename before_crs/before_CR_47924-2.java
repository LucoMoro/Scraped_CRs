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
            ZipFile.RAFStream is = (ZipFile.RAFStream)in;
            synchronized (is.mSharedRaf) {
                long len = is.mLength - is.mOffset;
                if (len > nativeEndBufSize) len = nativeEndBufSize;
                int cnt = inf.setFileInput(is.mSharedRaf.getFD(), is.mOffset, nativeEndBufSize);
                is.skip(cnt);
            }
} else {
if ((len = in.read(buf)) > 0) {
inf.setInput(buf, 0, len);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index 3e58727..8ff9ffc 100644

//Synthetic comment -- @@ -40,11 +40,16 @@
* @see ZipOutputStream
*/
public class ZipEntry implements ZipConstants, Cloneable {
    String name, comment;

    long compressedSize = -1, crc = -1, size = -1;

    int compressionMethod = -1, time = -1, modDate = -1;

byte[] extra;

//Synthetic comment -- @@ -80,11 +85,8 @@
}

/**
     * Gets the comment for this {@code ZipEntry}.
     *
     * @return the comment for this {@code ZipEntry}, or {@code null} if there
     *         is no comment. If we're reading an archive with
     *         {@code ZipInputStream} the comment is not available.
*/
public String getComment() {
return comment;
//Synthetic comment -- @@ -178,13 +180,17 @@

/**
* Sets the comment for this {@code ZipEntry}.
     *
     * @param comment
     *            the comment for this entry.
*/
public void setComment(String comment) {
        if (comment != null && comment.length() > 0xffff) {
            throw new IllegalArgumentException("Comment too long: " + comment.length());
}
this.comment = comment;
}
//Synthetic comment -- @@ -221,7 +227,7 @@
* @param data
*            a byte array containing the extra information.
* @throws IllegalArgumentException
     *             when the length of data is greater than 0xFFFF bytes.
*/
public void setExtra(byte[] data) {
if (data != null && data.length > 0xffff) {
//Synthetic comment -- @@ -369,7 +375,7 @@

nameLength = it.readShort();
int extraLength = it.readShort();
        int commentLength = it.readShort();

// This is a 32-bit value in the file, but a 64-bit field in this object.
it.seek(42);
//Synthetic comment -- @@ -381,9 +387,9 @@

// The RI has always assumed UTF-8. (If GPBF_UTF8_FLAG isn't set, the encoding is
// actually IBM-437.)
        if (commentLength > 0) {
            byte[] commentBytes = new byte[commentLength];
            Streams.readFully(in, commentBytes, 0, commentLength);
comment = new String(commentBytes, 0, commentBytes.length, Charsets.UTF_8);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipFile.java b/luni/src/main/java/java/util/zip/ZipFile.java
//Synthetic comment -- index 20bdf42..8508767 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
static final int GPBF_UTF8_FLAG = 1 << 11;

/**
     * Open ZIP file for read.
*/
public static final int OPEN_READ = 1;

//Synthetic comment -- @@ -79,9 +79,9 @@
*/
public static final int OPEN_DELETE = 4;

    private final String fileName;

    private File fileToDeleteOnClose;

private RandomAccessFile mRaf;

//Synthetic comment -- @@ -90,61 +90,49 @@
private final CloseGuard guard = CloseGuard.get();

/**
     * Constructs a new {@code ZipFile} with the specified file.
     *
     * @param file
     *            the file to read from.
     * @throws ZipException
     *             if a ZIP error occurs.
     * @throws IOException
     *             if an {@code IOException} occurs.
*/
public ZipFile(File file) throws ZipException, IOException {
this(file, OPEN_READ);
}

/**
     * Opens a file as <i>ZIP-archive</i>. "mode" must be {@code OPEN_READ} or
     * {@code OPEN_DELETE} . The latter sets the "delete on exit" flag through a
     * file.
     *
     * @param file
     *            the ZIP file to read.
     * @param mode
     *            the mode of the file open operation.
     * @throws IOException
     *             if an {@code IOException} occurs.
*/
public ZipFile(File file, int mode) throws IOException {
        fileName = file.getPath();
if (mode != OPEN_READ && mode != (OPEN_READ | OPEN_DELETE)) {
throw new IllegalArgumentException("Bad mode: " + mode);
}

if ((mode & OPEN_DELETE) != 0) {
            fileToDeleteOnClose = file; // file.deleteOnExit();
} else {
            fileToDeleteOnClose = null;
}

        mRaf = new RandomAccessFile(fileName, "r");

readCentralDir();
guard.open("close");
}

    /**
     * Opens a ZIP archived file.
     *
     * @param name
     *            the name of the ZIP file.
     * @throws IOException
     *             if an IOException occurs.
     */
    public ZipFile(String name) throws IOException {
        this(new File(name), OPEN_READ);
    }

@Override protected void finalize() throws IOException {
try {
if (guard != null) {
//Synthetic comment -- @@ -174,9 +162,9 @@
mRaf = null;
raf.close();
}
            if (fileToDeleteOnClose != null) {
                fileToDeleteOnClose.delete();
                fileToDeleteOnClose = null;
}
}
}
//Synthetic comment -- @@ -257,19 +245,19 @@
// position of the entry's local header. At position 28 we find the
// length of the extra data. In some cases this length differs from
// the one coming in the central header.
            RAFStream rafstrm = new RAFStream(raf, entry.mLocalHeaderRelOffset + 28);
            DataInputStream is = new DataInputStream(rafstrm);
int localExtraLenOrWhatever = Short.reverseBytes(is.readShort());
is.close();

// Skip the name and this "extra" data or whatever it is:
            rafstrm.skip(entry.nameLength + localExtraLenOrWhatever);
            rafstrm.mLength = rafstrm.mOffset + entry.compressedSize;
if (entry.compressionMethod == ZipEntry.DEFLATED) {
int bufSize = Math.max(1024, (int)Math.min(entry.getSize(), 65535L));
                return new ZipInflaterInputStream(rafstrm, new Inflater(true), bufSize, entry);
} else {
                return rafstrm;
}
}
}
//Synthetic comment -- @@ -280,7 +268,7 @@
* @return the file name of this {@code ZipFile}.
*/
public String getName() {
        return fileName;
}

/**
//Synthetic comment -- @@ -351,18 +339,21 @@
int numEntries = it.readShort() & 0xffff;
int totalNumEntries = it.readShort() & 0xffff;
it.skip(4); // Ignore centralDirSize.
        int centralDirOffset = it.readInt();

if (numEntries != totalNumEntries || diskNumber != 0 || diskWithCentralDir != 0) {
throw new ZipException("spanned archives not supported");
}

// Seek to the first CDE and read all entries.
        RAFStream rafs = new RAFStream(mRaf, centralDirOffset);
        BufferedInputStream bin = new BufferedInputStream(rafs, 4096);
byte[] hdrBuf = new byte[CENHDR]; // Reuse the same buffer for each entry.
for (int i = 0; i < numEntries; ++i) {
            ZipEntry newEntry = new ZipEntry(hdrBuf, bin);
mEntries.put(newEntry.getName(), newEntry);
}
}
//Synthetic comment -- @@ -376,14 +367,13 @@
* <p>We could support mark/reset, but we don't currently need them.
*/
static class RAFStream extends InputStream {

        RandomAccessFile mSharedRaf;
        long mOffset;
        long mLength;

        public RAFStream(RandomAccessFile raf, long pos) throws IOException {
mSharedRaf = raf;
            mOffset = pos;
mLength = raf.length();
}

//Synthetic comment -- @@ -411,28 +401,34 @@
}
}

        @Override
        public long skip(long byteCount) throws IOException {
if (byteCount > mLength - mOffset) {
byteCount = mLength - mOffset;
}
mOffset += byteCount;
return byteCount;
}
}

static class ZipInflaterInputStream extends InflaterInputStream {

        ZipEntry entry;
        long bytesRead = 0;

public ZipInflaterInputStream(InputStream is, Inflater inf, int bsize, ZipEntry entry) {
super(is, inf, bsize);
this.entry = entry;
}

        @Override
        public int read(byte[] buffer, int off, int nbytes) throws IOException {
int i = super.read(buffer, off, nbytes);
if (i != -1) {
bytesRead += i;
//Synthetic comment -- @@ -440,8 +436,7 @@
return i;
}

        @Override
        public int available() throws IOException {
if (closed) {
// Our superclass will throw an exception, but there's a jtreg test that
// explicitly checks that the InputStream returned from ZipFile.getInputStream








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipOutputStream.java b/luni/src/main/java/java/util/zip/ZipOutputStream.java
//Synthetic comment -- index 77a993b..9b88291 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.nio.charset.Charsets;
import java.util.Arrays;
import java.util.HashSet;

/**
* This class provides an implementation of {@code FilterOutputStream} that
//Synthetic comment -- @@ -76,13 +77,13 @@

private static final int ZIPLocalHeaderVersionNeeded = 20;

    private String comment;

private final HashSet<String> entries = new HashSet<String>();

    private int compressMethod = DEFLATED;

    private int compressLevel = Deflater.DEFAULT_COMPRESSION;

private ByteArrayOutputStream cDir = new ByteArrayOutputStream();

//Synthetic comment -- @@ -95,14 +96,11 @@
private byte[] nameBytes;

/**
     * Constructs a new {@code ZipOutputStream} with the specified output
     * stream.
     *
     * @param p1
     *            the {@code OutputStream} to write the data to.
*/
    public ZipOutputStream(OutputStream p1) {
        super(p1, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
}

/**
//Synthetic comment -- @@ -131,7 +129,7 @@
*             If an error occurs closing the entry.
*/
public void closeEntry() throws IOException {
        checkClosed();
if (currentEntry == null) {
return;
}
//Synthetic comment -- @@ -186,12 +184,13 @@
} else {
writeShort(cDir, 0);
}
        String c;
        if ((c = currentEntry.getComment()) != null) {
            writeShort(cDir, c.length());
        } else {
            writeShort(cDir, 0);
}
writeShort(cDir, 0); // Disk Start
writeShort(cDir, 0); // Internal File Attributes
writeLong(cDir, 0); // External File Attributes
//Synthetic comment -- @@ -202,8 +201,8 @@
cDir.write(currentEntry.extra);
}
offset += curOffset;
        if (c != null) {
            cDir.write(c.getBytes());
}
currentEntry = null;
crc.reset();
//Synthetic comment -- @@ -220,7 +219,7 @@
*/
@Override
public void finish() throws IOException {
        // TODO: is there a bug here? why not checkClosed?
if (out == null) {
throw new IOException("Stream is closed");
}
//Synthetic comment -- @@ -242,11 +241,9 @@
writeShort(cDir, entries.size()); // Number of entries
writeLong(cDir, cdirSize); // Size of central dir
writeLong(cDir, offset); // Offset of central dir
        if (comment != null) {
            writeShort(cDir, comment.length());
            cDir.write(comment.getBytes());
        } else {
            writeShort(cDir, 0);
}
// Write the central directory.
cDir.writeTo(out);
//Synthetic comment -- @@ -269,18 +266,33 @@
if (currentEntry != null) {
closeEntry();
}
        if (ze.getMethod() == STORED || (compressMethod == STORED && ze.getMethod() == -1)) {
            if (ze.crc == -1) {
                throw new ZipException("CRC mismatch");
}
            if (ze.size == -1 && ze.compressedSize == -1) {
                throw new ZipException("Size mismatch");
}
            if (ze.size != ze.compressedSize && ze.compressedSize != -1 && ze.size != -1) {
                throw new ZipException("Size mismatch");
}
}
        checkClosed();
if (entries.contains(ze.name)) {
throw new ZipException("Entry already exists: " + ze.name);
}
//Synthetic comment -- @@ -294,16 +306,15 @@
throw new IllegalArgumentException("Name too long: " + nameLength + " UTF-8 bytes");
}

        def.setLevel(compressLevel);
currentEntry = ze;
entries.add(currentEntry.name);
        if (currentEntry.getMethod() == -1) {
            currentEntry.setMethod(compressMethod);
        }

// Local file header.
// http://www.pkware.com/documents/casestudies/APPNOTE.TXT
        int flags = currentEntry.getMethod() == STORED ? 0 : ZipFile.GPBF_DATA_DESCRIPTOR_FLAG;
// Java always outputs UTF-8 filenames. (Before Java 7, the RI didn't set this flag and used
// modified UTF-8. From Java 7, it sets this flag and uses normal UTF-8.)
flags |= ZipFile.GPBF_UTF8_FLAG;
//Synthetic comment -- @@ -344,16 +355,20 @@
}

/**
     * Sets the {@code ZipFile} comment associated with the file being written.
     *
     * @param comment
     *            the comment associated with the file.
*/
public void setComment(String comment) {
        if (comment.length() > 0xFFFF) {
            throw new IllegalArgumentException("Comment too long: " + comment.length() + " characters");
}
        this.comment = comment;
}

/**
//Synthetic comment -- @@ -369,7 +384,7 @@
if (level < Deflater.DEFAULT_COMPRESSION || level > Deflater.BEST_COMPRESSION) {
throw new IllegalArgumentException("Bad level: " + level);
}
        compressLevel = level;
}

/**
//Synthetic comment -- @@ -384,7 +399,7 @@
if (method != STORED && method != DEFLATED) {
throw new IllegalArgumentException("Bad method: " + method);
}
        compressMethod = method;
}

private long writeLong(OutputStream os, long i) throws IOException {
//Synthetic comment -- @@ -423,7 +438,7 @@
crc.update(buffer, offset, byteCount);
}

    private void checkClosed() throws IOException {
if (cDir == null) {
throw new IOException("Stream is closed");
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java b/luni/src/test/java/libcore/java/util/zip/ZipFileTest.java
//Synthetic comment -- index 7e8286e..08a575f 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
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
        ZipFile zipFile = new ZipFile(createZipFile(originalSize));
for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
ZipEntry zipEntry = e.nextElement();
assertTrue("This test needs >64 KiB of compressed data to exercise Inflater",
//Synthetic comment -- @@ -57,72 +57,281 @@
public void testInflatingStreamsRequiringZipRefill() throws IOException {
int originalSize = 1024 * 1024;
byte[] readBuffer = new byte[8192];
        ZipInputStream in = new ZipInputStream(new FileInputStream(createZipFile(originalSize)));
while (in.getNextEntry() != null) {
while (in.read(readBuffer, 0, readBuffer.length) != -1) {}
}
in.close();
}

    /**
     * Compresses a single random file into a .zip archive.
     */
    private File createZipFile(int uncompressedSize) throws IOException {
        File result = File.createTempFile("ZipFileTest", "zip");
        result.deleteOnExit();

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(result));
        ZipEntry entry = new ZipEntry("random");
        out.putNextEntry(entry);

byte[] writeBuffer = new byte[8192];
Random random = new Random();
        for (int i = 0; i < uncompressedSize; i += writeBuffer.length) {
            random.nextBytes(writeBuffer);
            out.write(writeBuffer, 0, Math.min(writeBuffer.length, uncompressedSize - i));
}

        out.closeEntry();
out.close();
return result;
      }

      public void testHugeZipFile() throws IOException {
          int expectedEntryCount = 64*1024 - 1;
          File f = createHugeZipFile(expectedEntryCount);
          ZipFile zipFile = new ZipFile(f);
          int entryCount = 0;
          for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
              ZipEntry zipEntry = e.nextElement();
              ++entryCount;
          }
          assertEquals(expectedEntryCount, entryCount);
          zipFile.close();
      }

      public void testZip64Support() throws IOException {
          try {
              createHugeZipFile(64*1024);
              fail(); // Make this test more like testHugeZipFile when we have Zip64 support.
          } catch (ZipException expected) {
          }
      }

      /**
       * Compresses the given number of empty files into a .zip archive.
       */
      private File createHugeZipFile(int count) throws IOException {
          File result = File.createTempFile("ZipFileTest", "zip");
          result.deleteOnExit();

          ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(result)));
          for (int i = 0; i < count; ++i) {
              ZipEntry entry = new ZipEntry(Integer.toHexString(i));
              out.putNextEntry(entry);
              out.closeEntry();
          }

          out.close();
          return result;
      }
}







