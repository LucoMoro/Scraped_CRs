/*Pull libcore's time zone data from the new single 'tzdata' file.

Bug: 7012465
Change-Id:I5ce1a372755e31f8882fbfc2ea2b582fec1858b3*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index cc7cc4f..0fbfe7c 100644

//Synthetic comment -- @@ -35,36 +35,16 @@
import org.apache.harmony.luni.internal.util.TimezoneGetter;

/**
 * A class used to initialize the time zone database. This implementation uses the
 * Olson tzdata as the source of time zone information. However, to conserve
 * disk space (inodes) and reduce I/O, all the data is concatenated into a single file,
 * with an index to indicate the starting position of each time zone record.
*
* @hide - used to implement TimeZone
*/
public final class ZoneInfoDB {
private static final Object LOCK = new Object();

/**
* Rather than open, read, and close the big data file each time we look up a time zone,
* we map the big data file during startup, and then just use the MemoryMappedFile.
//Synthetic comment -- @@ -72,68 +52,68 @@
* At the moment, this "big" data file is about 500 KiB. At some point, that will be small
* enough that we'll just keep the byte[] in memory.
*/
    private static final MemoryMappedFile TZDATA = mapData();

    private static String version;

/**
* The 'ids' array contains time zone ids sorted alphabetically, for binary searching.
* The other two arrays are in the same order. 'byteOffsets' gives the byte offset
     * of each time zone, and 'rawUtcOffsets' gives the time zone's raw UTC offset.
*/
private static String[] ids;
private static int[] byteOffsets;
private static int[] rawUtcOffsets;

static {
        readHeader();
}

private ZoneInfoDB() {
}

    private static void readHeader() {
        // byte[12] tzdata_version  -- "tzdata2012f\0"
        // int file_format_version  -- 1
        // int index_offset
        // int data_offset
        // int zonetab_offset
        BufferIterator it = TZDATA.bigEndianIterator();

        byte[] tzdata_version = new byte[12];
        it.readByteArray(tzdata_version, 0, tzdata_version.length);
        String magic = new String(tzdata_version, 0, 6, Charsets.US_ASCII);
        if (!magic.equals("tzdata") || tzdata_version[11] != 0) {
            throw new RuntimeException("bad tzdata magic: " + Arrays.toString(tzdata_version));
}
        version = new String(tzdata_version, 6, 5, Charsets.US_ASCII);

        int file_format_version = it.readInt();
        if (file_format_version != 1) {
            throw new RuntimeException("unknown tzdata file format version: " + file_format_version);
        }

        int index_offset = it.readInt();
        int data_offset = it.readInt();
        int zonetab_offset = it.readInt();
        if (zonetab_offset != 0) {
            throw new RuntimeException("non-zero zonetab offset: " + zonetab_offset);
        }

        readIndex(it, index_offset, data_offset);
}

private static MemoryMappedFile mapData() {
try {
            String path = System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/tzdata";
            return MemoryMappedFile.mmapRO(path);
} catch (ErrnoException errnoException) {
throw new AssertionError(errnoException);
}
}

    private static void readIndex(BufferIterator it, int indexOffset, int dataOffset) {
        it.seek(indexOffset);

// The database reserves 40 bytes for each id.
final int SIZEOF_TZNAME = 40;
//Synthetic comment -- @@ -141,18 +121,22 @@
final int SIZEOF_TZINT = 4;

byte[] idBytes = new byte[SIZEOF_TZNAME];
        int indexSize = (dataOffset - indexOffset);
        int entryCount = indexSize / (SIZEOF_TZNAME + 3*SIZEOF_TZINT);

        char[] idChars = new char[entryCount * SIZEOF_TZNAME];
        int[] idEnd = new int[entryCount];
int idOffset = 0;

        byteOffsets = new int[entryCount];
        rawUtcOffsets = new int[entryCount];

        for (int i = 0; i < entryCount; i++) {
it.readByteArray(idBytes, 0, idBytes.length);

byteOffsets[i] = it.readInt();
            byteOffsets[i] += dataOffset; // TODO: change the file format so this is included.

int length = it.readInt();
if (length < 44) {
throw new AssertionError("length in index file < sizeof(tzhead)");
//Synthetic comment -- @@ -174,8 +158,8 @@
// We create one string containing all the ids, and then break that into substrings.
// This way, all ids share a single char[] on the heap.
String allIds = new String(idChars, 0, idOffset);
        ids = new String[entryCount];
        for (int i = 0; i < entryCount; i++) {
ids[i] = allIds.substring(i == 0 ? 0 : idEnd[i - 1], idEnd[i]);
}
}
//Synthetic comment -- @@ -187,7 +171,7 @@
return null;
}

        BufferIterator data = TZDATA.bigEndianIterator();
data.skip(byteOffsets[index]);

// Variable names beginning tzh_ correspond to those in "tzfile.h".
//Synthetic comment -- @@ -235,7 +219,7 @@

public static String[] getAvailableIDs(int rawOffset) {
List<String> matches = new ArrayList<String>();
        for (int i = 0, end = rawUtcOffsets.length; i < end; ++i) {
if (rawUtcOffsets[i] == rawOffset) {
matches.add(ids[i]);
}
//Synthetic comment -- @@ -260,6 +244,6 @@
}

public static String getVersion() {
        return version;
}
}







