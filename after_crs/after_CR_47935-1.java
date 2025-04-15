/*More java.util.zip documentation improvements.

Change-Id:I8ebc23b8a7a7affee0fd3756c3861cf5b6c07ee4*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/Deflater.java b/luni/src/main/java/java/util/zip/Deflater.java
//Synthetic comment -- index 13f53c1..1593a60 100644

//Synthetic comment -- @@ -175,7 +175,7 @@
/**
* Constructs a new {@code Deflater} instance with a specific compression
* level. If {@code noHeader} is true, no ZLIB header is added to the
     * output. In a zip file, every entry (compressed file) comes with such a
* header. The strategy can be specified using {@link #setStrategy}.
*
* @param level








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index c313666..4e10642 100644

//Synthetic comment -- @@ -29,15 +29,11 @@
import libcore.io.HeapBufferIterator;

/**
 * An entry within a zip file.
 * An entry has attributes such as its name (which is actually a path) and the uncompressed size
 * of the corresponding data. An entry does not contain the data itself, but can be used as a key
 * with {@link ZipFile#getInputStream}. The class documentation for {@link ZipInputStream} and
 * {@link ZipOutputStream} shows how {@code ZipEntry} is used in conjunction with those two classes.
*/
public class ZipEntry implements ZipConstants, Cloneable {
String name;
//Synthetic comment -- @@ -55,7 +51,7 @@
byte[] extra;

int nameLength = -1;
    long localHeaderRelOffset = -1;

/**
* Zip entry state: Deflated.
//Synthetic comment -- @@ -68,10 +64,9 @@
public static final int STORED = 0;

/**
     * Constructs a new {@code ZipEntry} with the specified name. The name is actually a path,
     * and may contain {@code /} characters.
*
* @throws IllegalArgumentException
*             if the name length is outside the range (> 0xFFFF).
*/
//Synthetic comment -- @@ -87,7 +82,7 @@

/**
* Returns the comment for this {@code ZipEntry}, or {@code null} if there is no comment.
     * If we're reading a zip file using {@code ZipInputStream}, the comment is not available.
*/
public String getComment() {
return comment;
//Synthetic comment -- @@ -181,7 +176,7 @@

/**
* Sets the comment for this {@code ZipEntry}.
    @throws IllegalArgumentException if the comment is >= 64 Ki UTF-8 bytes.
*/
public void setComment(String comment) {
if (comment == null) {
//Synthetic comment -- @@ -225,10 +220,7 @@
/**
* Sets the extra information for this {@code ZipEntry}.
*
     * @throws IllegalArgumentException if the data length >= 64 KiB.
*/
public void setExtra(byte[] data) {
if (data != null && data.length > 0xffff) {
//Synthetic comment -- @@ -322,7 +314,7 @@
modDate = ze.modDate;
extra = ze.extra;
nameLength = ze.nameLength;
        localHeaderRelOffset = ze.localHeaderRelOffset;
}

/**
//Synthetic comment -- @@ -381,7 +373,7 @@

// This is a 32-bit value in the file, but a 64-bit field in this object.
it.seek(42);
        localHeaderRelOffset = ((long) it.readInt()) & 0xffffffffL;

byte[] nameBytes = new byte[nameLength];
Streams.readFully(in, nameBytes, 0, nameBytes.length);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipError.java b/luni/src/main/java/java/util/zip/ZipError.java
//Synthetic comment -- index 5718575..d4694d5 100644

//Synthetic comment -- @@ -18,7 +18,7 @@
package java.util.zip;

/**
 * Thrown when an unrecoverable zip error has occurred.
* @since 1.6
*/
public class ZipError extends InternalError {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipException.java b/luni/src/main/java/java/util/zip/ZipException.java
//Synthetic comment -- index b77943e..27203fc 100644

//Synthetic comment -- @@ -21,7 +21,7 @@

/**
* This runtime exception is thrown by {@code ZipFile} and {@code
 * ZipInputStream} when the file or stream is not a valid zip file.
*
* @see ZipFile
* @see ZipInputStream








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipFile.java b/luni/src/main/java/java/util/zip/ZipFile.java
//Synthetic comment -- index c57185d..4dacb82 100644

//Synthetic comment -- @@ -34,14 +34,13 @@
import libcore.io.Streams;

/**
 * This class provides random read access to a zip file. You pay more to read
* the zip file's central directory up front (from the constructor), but if you're using
* {@link #getEntry} to look up multiple files by name, you get the benefit of this index.
*
* <p>If you only want to iterate through all the files (using {@link #entries}, you should
* consider {@link ZipInputStream}, which provides stream-like read access to a zip file and
 * has a lower up-front cost because you don't pay to build an in-memory index.
*
* <p>If you want to create a zip file, use {@link ZipOutputStream}. There is no API for updating
* an existing zip file.
//Synthetic comment -- @@ -69,28 +68,28 @@
static final int GPBF_UTF8_FLAG = 1 << 11;

/**
     * Open zip file for reading.
*/
public static final int OPEN_READ = 1;

/**
     * Delete zip file when closed.
*/
public static final int OPEN_DELETE = 4;

    private final String filename;

    private File fileToDeleteOnClose;

    private RandomAccessFile raf;

    private final LinkedHashMap<String, ZipEntry> entries = new LinkedHashMap<String, ZipEntry>();

private final CloseGuard guard = CloseGuard.get();

/**
* Constructs a new {@code ZipFile} allowing read access to the contents of the given file.
     * @throws ZipException if a zip error occurs.
* @throws IOException if an {@code IOException} occurs.
*/
public ZipFile(File file) throws ZipException, IOException {
//Synthetic comment -- @@ -116,19 +115,19 @@
* @throws IOException if an {@code IOException} occurs.
*/
public ZipFile(File file, int mode) throws IOException {
        filename = file.getPath();
if (mode != OPEN_READ && mode != (OPEN_READ | OPEN_DELETE)) {
throw new IllegalArgumentException("Bad mode: " + mode);
}

if ((mode & OPEN_DELETE) != 0) {
            fileToDeleteOnClose = file;
            fileToDeleteOnClose.deleteOnExit();
} else {
            fileToDeleteOnClose = null;
}

        raf = new RandomAccessFile(filename, "r");

readCentralDir();
guard.open("close");
//Synthetic comment -- @@ -149,43 +148,47 @@
}

/**
     * Closes this zip file. This method is idempotent. This method may cause I/O if the
     * zip file needs to be deleted.
*
* @throws IOException
*             if an IOException occurs.
*/
public void close() throws IOException {
guard.close();

        RandomAccessFile localRaf = raf;
        if (localRaf != null) { // Only close initialized instances
            synchronized (localRaf) {
                raf = null;
                localRaf.close();
}
            if (fileToDeleteOnClose != null) {
                fileToDeleteOnClose.delete();
                fileToDeleteOnClose = null;
}
}
}

private void checkNotClosed() {
        if (raf == null) {
throw new IllegalStateException("Zip file closed");
}
}

/**
* Returns an enumeration of the entries. The entries are listed in the
     * order in which they appear in the zip file.
*
     * <p>If you only need to iterate over the entries in a zip file, and don't
     * need random-access entry lookup by name, you should probably use {@link ZipInputStream}
     * instead, to avoid paying to construct the in-memory index.
     *
     * @throws IllegalStateException if this zip file has been closed.
*/
public Enumeration<? extends ZipEntry> entries() {
checkNotClosed();
        final Iterator<ZipEntry> iterator = entries.values().iterator();

return new Enumeration<ZipEntry>() {
public boolean hasMoreElements() {
//Synthetic comment -- @@ -201,13 +204,9 @@
}

/**
     * Returns the zip entry with the given name, or null if there is no such entry.
*
     * @throws IllegalStateException if this zip file has been closed.
*/
public ZipEntry getEntry(String entryName) {
checkNotClosed();
//Synthetic comment -- @@ -215,9 +214,9 @@
throw new NullPointerException("entryName == null");
}

        ZipEntry ze = entries.get(entryName);
if (ze == null) {
            ze = entries.get(entryName + "/");
}
return ze;
}
//Synthetic comment -- @@ -230,7 +229,7 @@
* @return an input stream of the data contained in the {@code ZipEntry}.
* @throws IOException
*             if an {@code IOException} occurs.
     * @throws IllegalStateException if this zip file has been closed.
*/
public InputStream getInputStream(ZipEntry entry) throws IOException {
// Make sure this ZipEntry is in this Zip file.  We run it through the name lookup.
//Synthetic comment -- @@ -240,20 +239,20 @@
}

// Create an InputStream at the right part of the file.
        RandomAccessFile localRaf = raf;
        synchronized (localRaf) {
// We don't know the entry data's start position. All we have is the
// position of the entry's local header. At position 28 we find the
// length of the extra data. In some cases this length differs from
// the one coming in the central header.
            RAFStream rafStream = new RAFStream(localRaf, entry.localHeaderRelOffset + 28);
DataInputStream is = new DataInputStream(rafStream);
int localExtraLenOrWhatever = Short.reverseBytes(is.readShort());
is.close();

// Skip the name and this "extra" data or whatever it is:
rafStream.skip(entry.nameLength + localExtraLenOrWhatever);
            rafStream.length = rafStream.offset + entry.compressedSize;
if (entry.compressionMethod == ZipEntry.DEFLATED) {
int bufSize = Math.max(1024, (int)Math.min(entry.getSize(), 65535L));
return new ZipInflaterInputStream(rafStream, new Inflater(true), bufSize, entry);
//Synthetic comment -- @@ -269,18 +268,18 @@
* @return the file name of this {@code ZipFile}.
*/
public String getName() {
        return filename;
}

/**
* Returns the number of {@code ZipEntries} in this {@code ZipFile}.
*
* @return the number of entries in this file.
     * @throws IllegalStateException if this zip file has been closed.
*/
public int size() {
checkNotClosed();
        return entries.size();
}

/**
//Synthetic comment -- @@ -297,17 +296,13 @@
* it though, so we're in good company if this fails.
*/
private void readCentralDir() throws IOException {
        // Scan back, looking for the End Of Central Directory field. If the zip file doesn't
        // have an overall comment (unrelated to any per-entry comments), we'll hit the EOCD
        // on the first try.
        // No need to synchronize raf here -- we only do this when we first open the zip file.
        long scanOffset = raf.length() - ENDHDR;
if (scanOffset < 0) {
            throw new ZipException("File too short to be a zip file: " + raf.length());
}

long stopOffset = scanOffset - 65536;
//Synthetic comment -- @@ -317,21 +312,21 @@

final int ENDHEADERMAGIC = 0x06054b50;
while (true) {
            raf.seek(scanOffset);
            if (Integer.reverseBytes(raf.readInt()) == ENDHEADERMAGIC) {
break;
}

scanOffset--;
if (scanOffset < stopOffset) {
                throw new ZipException("EOCD not found; not a zip file?");
}
}

// Read the End Of Central Directory. We could use ENDHDR instead of the magic number 18,
// but we don't actually need all the header.
byte[] eocd = new byte[18];
        raf.readFully(eocd);

// Pull out the information we need.
BufferIterator it = HeapBufferIterator.iterator(eocd, 0, eocd.length, ByteOrder.LITTLE_ENDIAN);
//Synthetic comment -- @@ -350,12 +345,12 @@
// We have to do this now (from the constructor) rather than lazily because the
// public API doesn't allow us to throw IOException except from the constructor
// or from getInputStream.
        RAFStream rafStream = new RAFStream(raf, centralDirOffset);
BufferedInputStream bufferedStream = new BufferedInputStream(rafStream, 4096);
byte[] hdrBuf = new byte[CENHDR]; // Reuse the same buffer for each entry.
for (int i = 0; i < numEntries; ++i) {
ZipEntry newEntry = new ZipEntry(hdrBuf, bufferedStream);
            entries.put(newEntry.getName(), newEntry);
}
}

//Synthetic comment -- @@ -368,18 +363,18 @@
* <p>We could support mark/reset, but we don't currently need them.
*/
static class RAFStream extends InputStream {
        private final RandomAccessFile sharedRaf;
        private long length;
        private long offset;

        public RAFStream(RandomAccessFile raf, long initialOffset) throws IOException {
            sharedRaf = raf;
            offset = initialOffset;
            length = raf.length();
}

@Override public int available() throws IOException {
            return (offset < length ? 1 : 0);
}

@Override public int read() throws IOException {
//Synthetic comment -- @@ -387,14 +382,14 @@
}

@Override public int read(byte[] b, int off, int len) throws IOException {
            synchronized (sharedRaf) {
                sharedRaf.seek(offset);
                if (len > length - offset) {
                    len = (int) (length - offset);
}
                int count = sharedRaf.read(b, off, len);
if (count > 0) {
                    offset += count;
return count;
} else {
return -1;
//Synthetic comment -- @@ -403,17 +398,19 @@
}

@Override public long skip(long byteCount) throws IOException {
            if (byteCount > length - offset) {
                byteCount = length - offset;
}
            offset += byteCount;
return byteCount;
}

public int fill(Inflater inflater, int nativeEndBufSize) throws IOException {
            synchronized (sharedRaf) {
                int len = Math.min((int) (length - offset), nativeEndBufSize);
                int cnt = inflater.setFileInput(sharedRaf.getFD(), offset, nativeEndBufSize);
                // setFileInput read from the file, so we need to get the OS and RAFStream back
                // in sync...
skip(cnt);
return len;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipInputStream.java b/luni/src/main/java/java/util/zip/ZipInputStream.java
//Synthetic comment -- index 57d9034..97aa350 100644

//Synthetic comment -- @@ -29,25 +29,25 @@
import libcore.io.Streams;

/**
 * Used to read (decompress) the data from zip files.
*
 * <p>A zip file (or "archive") is a collection of (possibly) compressed files.
 * When reading from a {@code ZipInputStream}, you call {@link #getNextEntry}
 * which returns a {@link ZipEntry} of metadata corresponding to the userdata that follows.
 * When you appear to have hit the end of this stream (which is really just the end of the current
 * entry's userdata), call {@code getNextEntry} again. When it returns null,
 * there are no more entries in the input file.
*
 * <p>Although {@code InflaterInputStream} can only read compressed zip
* entries, this class can read non-compressed entries as well.
*
* <p>Use {@link ZipFile} if you need random access to entries by name, but use this class
 * if you just want to iterate over all entries.
*
* <h3>Example</h3>
* <p>Using {@code ZipInputStream} is a little more complicated than {@link GZIPInputStream}
 * because zip files are containers that can contain multiple files. This code pulls all the
 * files out of a zip file, similar to the {@code unzip(1)} utility.
* <pre>
* InputStream is = ...
* ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
//Synthetic comment -- @@ -91,10 +91,7 @@
private char[] charBuf = new char[256];

/**
     * Constructs a new {@code ZipInputStream} to read zip entries from the given input stream.
*/
public ZipInputStream(InputStream stream) {
super(new PushbackInputStream(stream, BUF_SIZE), new Inflater(true));
//Synthetic comment -- @@ -118,7 +115,7 @@
}

/**
     * Closes the current zip entry and prepares to read the next entry.
*
* @throws IOException
*             if an {@code IOException} occurs.








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipOutputStream.java b/luni/src/main/java/java/util/zip/ZipOutputStream.java
//Synthetic comment -- index 9f7a4ad..5db2262 100644

//Synthetic comment -- @@ -26,21 +26,20 @@
import libcore.util.EmptyArray;

/**
 * Used to write (compress) data into zip files.
*
* <p>{@code ZipOutputStream} is used to write {@link ZipEntry}s to the underlying
* stream. Output from {@code ZipOutputStream} can be read using {@link ZipFile}
* or {@link ZipInputStream}.
*
 * <p>While {@code DeflaterOutputStream} can write compressed zip file
 * entries, this extension can write uncompressed entries as well.
* Use {@link ZipEntry#setMethod} or {@link #setMethod} with the {@link ZipEntry#STORED} flag.
*
* <h3>Example</h3>
* <p>Using {@code ZipOutputStream} is a little more complicated than {@link GZIPOutputStream}
 * because zip files are containers that can contain multiple files. This code creates a zip
 * file containing several files, similar to the {@code zip(1)} utility.
* <pre>
* OutputStream os = ...
* ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));
//Synthetic comment -- @@ -346,7 +345,7 @@

/**
* Sets the comment associated with the file being written.
     * @throws IllegalArgumentException if the comment is >= 64 Ki UTF-8 bytes.
*/
public void setComment(String comment) {
if (comment == null) {







