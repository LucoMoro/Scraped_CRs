/*Make it possible to share libcore.util.ZoneInfo with bionic.

The code that generates the single big file from the directory of
zoneinfo files needs to be able to get the raw offset for each zone,
so we can implement TimeZone.getAvailableIDs(int) cheaply.

Bug: 7012465
Change-Id:Icc8355c086dd8e62589c2930fb7f892feea5a217*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfo.java b/luni/src/main/java/libcore/util/ZoneInfo.java
//Synthetic comment -- index 18e4f21..54ee667 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import libcore.io.BufferIterator;

/**
* Our concrete TimeZone implementation, backed by zoneinfo data.
//Synthetic comment -- @@ -50,7 +51,48 @@
private final byte[] mTypes;
private final byte[] mIsDsts;

    public static TimeZone makeTimeZone(String id, BufferIterator it) {
        // Variable names beginning tzh_ correspond to those in "tzfile.h".

        // Check tzh_magic.
        if (it.readInt() != 0x545a6966) { // "TZif"
            return null;
        }

        // Skip the uninteresting part of the header.
        it.skip(28);

        // Read the sizes of the arrays we're about to read.
        int tzh_timecnt = it.readInt();
        int tzh_typecnt = it.readInt();

        it.skip(4); // Skip tzh_charcnt.

        int[] transitions = new int[tzh_timecnt];
        it.readIntArray(transitions, 0, transitions.length);

        byte[] type = new byte[tzh_timecnt];
        it.readByteArray(type, 0, type.length);

        int[] gmtOffsets = new int[tzh_typecnt];
        byte[] isDsts = new byte[tzh_typecnt];
        for (int i = 0; i < tzh_typecnt; ++i) {
            gmtOffsets[i] = it.readInt();
            isDsts[i] = it.readByte();
            // We skip the abbreviation index. This would let us provide historically-accurate
            // time zone abbreviations (such as "AHST", "YST", and "AKST" for standard time in
            // America/Anchorage in 1982, 1983, and 1984 respectively). ICU only knows the current
            // names, though, so even if we did use this data to provide the correct abbreviations
            // for en_US, we wouldn't be able to provide correct abbreviations for other locales,
            // nor would we be able to provide correct long forms (such as "Yukon Standard Time")
            // for any locale. (The RI doesn't do any better than us here either.)
            it.skip(1);
        }

        return new ZoneInfo(id, transitions, type, gmtOffsets, isDsts);
    }

    private ZoneInfo(String name, int[] transitions, byte[] types, int[] gmtOffsets, byte[] isDsts) {
mTransitions = transitions;
mTypes = types;
mIsDsts = isDsts;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index 0fbfe7c..d27231e 100644

//Synthetic comment -- @@ -171,46 +171,10 @@
return null;
}

        BufferIterator it = TZDATA.bigEndianIterator();
        it.skip(byteOffsets[index]);

        return ZoneInfo.makeTimeZone(id, it);
}

public static String[] getAvailableIDs() {







