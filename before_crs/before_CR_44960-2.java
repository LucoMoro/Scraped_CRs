/*Pull libcore's time zone data from the new single 'tzdata' file.

Bug: 7012465
Change-Id:I5ce1a372755e31f8882fbfc2ea2b582fec1858b3*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index cc7cc4f..0fbfe7c 100644

//Synthetic comment -- @@ -35,36 +35,16 @@
import org.apache.harmony.luni.internal.util.TimezoneGetter;

/**
 * A class used to initialize the time zone database.  This implementation uses the
 * 'zoneinfo' database as the source of time zone information.  However, to conserve
 * disk space the data for all time zones are concatenated into a single file, and a
 * second file is used to indicate the starting position of each time zone record.  A
 * third file indicates the version of the zoneinfo database used to generate the data.
*
* @hide - used to implement TimeZone
*/
public final class ZoneInfoDB {
    /**
     * The directory containing the time zone database files.
     */
    private static final String ZONE_DIRECTORY_NAME =
            System.getenv("ANDROID_ROOT") + "/usr/share/zoneinfo/";

    /**
     * The name of the file containing the concatenated time zone records.
     */
    private static final String ZONE_FILE_NAME = ZONE_DIRECTORY_NAME + "zoneinfo.dat";

    /**
     * The name of the file containing the index to each time zone record within
     * the zoneinfo.dat file.
     */
    private static final String INDEX_FILE_NAME = ZONE_DIRECTORY_NAME + "zoneinfo.idx";

private static final Object LOCK = new Object();

    private static final String VERSION = readVersion();

/**
* Rather than open, read, and close the big data file each time we look up a time zone,
* we map the big data file during startup, and then just use the MemoryMappedFile.
//Synthetic comment -- @@ -72,68 +52,68 @@
* At the moment, this "big" data file is about 500 KiB. At some point, that will be small
* enough that we'll just keep the byte[] in memory.
*/
    private static final MemoryMappedFile ALL_ZONE_DATA = mapData();

/**
* The 'ids' array contains time zone ids sorted alphabetically, for binary searching.
* The other two arrays are in the same order. 'byteOffsets' gives the byte offset
     * into "zoneinfo.dat" of each time zone, and 'rawUtcOffsets' gives the time zone's
     * raw UTC offset.
*/
private static String[] ids;
private static int[] byteOffsets;
private static int[] rawUtcOffsets;
static {
        readIndex();
}

private ZoneInfoDB() {
}

    /**
     * Reads the file indicating the database version in use.
     */
    private static String readVersion() {
        try {
            byte[] bytes = IoUtils.readFileAsByteArray(ZONE_DIRECTORY_NAME + "zoneinfo.version");
            return new String(bytes, 0, bytes.length, Charsets.ISO_8859_1).trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
}
}

private static MemoryMappedFile mapData() {
try {
            return MemoryMappedFile.mmapRO(ZONE_FILE_NAME);
} catch (ErrnoException errnoException) {
throw new AssertionError(errnoException);
}
}

    /**
     * Traditionally, Unix systems have one file per time zone. We have one big data file, which
     * is just a concatenation of regular time zone files. To allow random access into this big
     * data file, we also have an index. We read the index at startup, and keep it in memory so
     * we can binary search by id when we need time zone data.
     *
     * The format of this file is, I believe, Android's own, and undocumented.
     *
     * All this code assumes strings are US-ASCII.
     */
    private static void readIndex() {
        MemoryMappedFile mappedFile = null;
        try {
            mappedFile = MemoryMappedFile.mmapRO(INDEX_FILE_NAME);
            readIndex(mappedFile);
        } catch (Exception ex) {
            throw new AssertionError(ex);
        } finally {
            IoUtils.closeQuietly(mappedFile);
        }
    }

    private static void readIndex(MemoryMappedFile mappedFile) throws ErrnoException, IOException {
        BufferIterator it = mappedFile.bigEndianIterator();

// The database reserves 40 bytes for each id.
final int SIZEOF_TZNAME = 40;
//Synthetic comment -- @@ -141,18 +121,22 @@
final int SIZEOF_TZINT = 4;

byte[] idBytes = new byte[SIZEOF_TZNAME];
        int numEntries = (int) mappedFile.size() / (SIZEOF_TZNAME + 3*SIZEOF_TZINT);

        char[] idChars = new char[numEntries * SIZEOF_TZNAME];
        int[] idEnd = new int[numEntries];
int idOffset = 0;

        byteOffsets = new int[numEntries];
        rawUtcOffsets = new int[numEntries];

        for (int i = 0; i < numEntries; i++) {
it.readByteArray(idBytes, 0, idBytes.length);
byteOffsets[i] = it.readInt();
int length = it.readInt();
if (length < 44) {
throw new AssertionError("length in index file < sizeof(tzhead)");
//Synthetic comment -- @@ -174,8 +158,8 @@
// We create one string containing all the ids, and then break that into substrings.
// This way, all ids share a single char[] on the heap.
String allIds = new String(idChars, 0, idOffset);
        ids = new String[numEntries];
        for (int i = 0; i < numEntries; i++) {
ids[i] = allIds.substring(i == 0 ? 0 : idEnd[i - 1], idEnd[i]);
}
}
//Synthetic comment -- @@ -187,7 +171,7 @@
return null;
}

        BufferIterator data = ALL_ZONE_DATA.bigEndianIterator();
data.skip(byteOffsets[index]);

// Variable names beginning tzh_ correspond to those in "tzfile.h".
//Synthetic comment -- @@ -235,7 +219,7 @@

public static String[] getAvailableIDs(int rawOffset) {
List<String> matches = new ArrayList<String>();
        for (int i = 0, end = rawUtcOffsets.length; i < end; i++) {
if (rawUtcOffsets[i] == rawOffset) {
matches.add(ids[i]);
}
//Synthetic comment -- @@ -260,6 +244,6 @@
}

public static String getVersion() {
        return VERSION;
}
}







