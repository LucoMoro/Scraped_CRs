/*Fix TimeZone's handling of Australia/Lord_Howe.

Australia/Lord_Howe has a half hour difference between standard and daylight
time, rather than the usual hour. Our ZoneInfo implementation ignored this.
Fix that oversight, make SimpleDateFormat actually use this information, and
prevent TimeZone.getTimeZone("GMT") and TimeZone.getTimeZone("UTC") from
being quite as expensive as they accidentally were.

Longer term I think we should probably remove all uses of getDSTSavings from
libcore in favor of TimeZone.getOffset, but this is probably a useful step
forwards anyway. It fixes Australia/Lord_Howe in the meantime and it means
that anyone else who's using getDSTSavings won't be bitten (even if they too
really ought to be using TimeZone.getOffset).

Bug: 4723412
Bug:http://code.google.com/p/android/issues/detail?id=24684Change-Id:I5d50afecbe1453157e9c8f0b88305a258a3ba2e0*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index 1f9b694..5ed51a1 100644

//Synthetic comment -- @@ -1174,14 +1174,18 @@
}
int raw = zone.getRawOffset();
if (j == TimeZones.LONG_NAME_DST || j == TimeZones.SHORT_NAME_DST) {
                        // Not all time zones use a one-hour difference, so we need to query
                        // the TimeZone. (Australia/Lord_Howe is the usual example of this.)
                        int dstSavings = zone.getDSTSavings();
                        // One problem with TimeZone.getDSTSavings is that it will return 0 if the
                        // time zone has stopped using DST, even if we're parsing a date from
                        // the past. In that case, assume the default.
                        if (dstSavings == 0) {
                            // TODO: we should change this to use TimeZone.getOffset(long),
                            // but that requires the complete date to be parsed first.
                            dstSavings = 3600000;
                        }
                        raw += dstSavings;
}
calendar.setTimeZone(new SimpleTimeZone(raw, ""));
return offset + element[j].length();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/GregorianCalendar.java b/luni/src/main/java/java/util/GregorianCalendar.java
//Synthetic comment -- index c0fd521..4d93aef 100644

//Synthetic comment -- @@ -1416,7 +1416,7 @@
*/
public void setGregorianChange(Date date) {
gregorianCutover = date.getTime();
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
cal.setTime(date);
changeYear = cal.get(YEAR);
if (cal.get(ERA) == BC) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 969e164..b7d7da1 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

package java.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -80,7 +81,8 @@
*/
public static final int LONG = 1;

    private static final TimeZone GMT = new SimpleTimeZone(0, "GMT");
    private static final TimeZone UTC = new SimpleTimeZone(0, "UTC");

private static TimeZone defaultTimeZone;

//Synthetic comment -- @@ -218,16 +220,27 @@
}

/**
     * Returns the latest daylight savings in milliseconds for this time zone, relative
     * to this time zone's regular UTC offset (as returned by {@link #getRawOffset}).
*
     * <p>This class returns {@code 3600000} (1 hour) for time zones
     * that use daylight savings time and {@code 0} for timezones that do not,
     * leaving it to subclasses to override this method for other daylight savings
     * offsets. (There are time zones, such as {@code Australia/Lord_Howe},
     * that use other values.)
     *
     * <p>Note that this method doesn't tell you whether or not to <i>apply</i> the
* offset: you need to call {@code inDaylightTime} for the specific time
* you're interested in. If this method returns a non-zero offset, that only
* tells you that this {@code TimeZone} sometimes observes daylight savings.
     *
     * <p>Note also that this method doesn't necessarily return the value you need
     * to apply to the time you're working with. This value can and does change over
     * time for a given time zone.
     *
     * <p>It's highly unlikely that you should ever call this method. You
     * probably want {@link #getOffset} instead, which tells you the offset
     * for a specific point in time, and takes daylight savings into account for you.
*/
public int getDSTSavings() {
return useDaylightTime() ? 3600000 : 0;
//Synthetic comment -- @@ -291,17 +304,32 @@
if (id == null) {
throw new NullPointerException("id == null");
}

        // Special cases? These can clone an existing instance.
        // TODO: should we just add a cache to ZoneInfoDB instead?
        if (id.length() == 3) {
            if (id.equals("GMT")) {
                return (TimeZone) GMT.clone();
            }
            if (id.equals("UTC")) {
                return (TimeZone) UTC.clone();
            }
}

        // In the database?
        TimeZone zone = null;
        try {
            zone = ZoneInfoDB.makeTimeZone(id);
        } catch (IOException ignored) {
        }

        // Custom time zone?
if (zone == null && id.length() > 3 && id.startsWith("GMT")) {
zone = getCustomTimeZone(id);
}

        // We never return null; on failure we return the equivalent of "GMT".
        return (zone != null) ? zone : (TimeZone) GMT.clone();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfo.java b/luni/src/main/java/libcore/util/ZoneInfo.java
//Synthetic comment -- index 5a8caf2..ac48b23 100644

//Synthetic comment -- @@ -51,29 +51,46 @@
private final byte[] mTypes;
private final byte[] mIsDsts;
private final boolean mUseDst;
    private final int mDstSavings; // Implements TimeZone.getDSTSavings.

    ZoneInfo(String name, int[] transitions, byte[] types, int[] gmtOffsets, byte[] isDsts) {
mTransitions = transitions;
        mTypes = types;
mIsDsts = isDsts;
setID(name);

        // Find the latest daylight and standard offsets (if any).
        int lastStd = 0;
        boolean haveStd = false;
        int lastDst = 0;
        boolean haveDst = false;
        for (int i = mTransitions.length - 1; (!haveStd || !haveDst) && i >= 0; --i) {
            int type = mTypes[i] & 0xff;
            if (!haveStd && mIsDsts[type] == 0) {
                haveStd = true;
                lastStd = i;
            }
            if (!haveDst && mIsDsts[type] != 0) {
                haveDst = true;
                lastDst = i;
}
}

        // Use the latest non-daylight offset (if any) as the raw offset.
if (lastStd >= mTypes.length) {
mRawOffset = gmtOffsets[0];
} else {
mRawOffset = gmtOffsets[mTypes[lastStd] & 0xff];
}

        // Use the latest transition's pair of offsets to compute the DST savings.
        // This isn't generally useful, but it's exposed by TimeZone.getDSTSavings.
        if (lastDst >= mTypes.length) {
            mDstSavings = 0;
        } else {
            mDstSavings = Math.abs(gmtOffsets[mTypes[lastStd] & 0xff] - gmtOffsets[mTypes[lastDst] & 0xff]) * 1000;
        }

// Cache the oldest known raw offset, in case we're asked about times that predate our
// transition data.
int firstStd = -1;
//Synthetic comment -- @@ -111,6 +128,7 @@
}
mUseDst = usesDst;

        // tzdata uses seconds, but Java uses milliseconds.
mRawOffset *= 1000;
mEarliestRawOffset = earliestRawOffset * 1000;
}
//Synthetic comment -- @@ -185,6 +203,10 @@
mRawOffset = off;
}

    @Override public int getDSTSavings() {
        return mUseDst ? mDstSavings: 0;
    }

@Override public boolean useDaylightTime() {
return mUseDst;
}
//Synthetic comment -- @@ -235,7 +257,7 @@
StringBuilder sb = new StringBuilder();
// First the basics...
sb.append(getClass().getName() + "[" + getID() + ",mRawOffset=" + mRawOffset +
                ",mUseDst=" + mUseDst + ",mDstSavings=" + mDstSavings + "]");
// ...followed by a zdump(1)-like description of all our transition data.
sb.append("\n");
Formatter f = new Formatter(sb);








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfoDB.java b/luni/src/main/java/libcore/util/ZoneInfoDB.java
//Synthetic comment -- index 69b6784..cc7cc4f 100644

//Synthetic comment -- @@ -180,7 +180,7 @@
}
}

    public static TimeZone makeTimeZone(String id) throws IOException {
// Work out where in the big data file this time zone is.
int index = Arrays.binarySearch(ids, id);
if (index < 0) {
//Synthetic comment -- @@ -259,17 +259,6 @@
}
}

public static String getVersion() {
return VERSION;
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/TimeZoneTest.java b/luni/src/test/java/libcore/java/util/TimeZoneTest.java
//Synthetic comment -- index 484668b..d5fa558 100644

//Synthetic comment -- @@ -186,4 +186,10 @@
assertEquals("GMT", TimeZone.getTimeZone("junk").getID());
assertEquals("GMT", TimeZone.getTimeZone("gmt+5:00").getID());
}

    public void test_getDSTSavings() throws Exception {
      assertEquals(0, TimeZone.getTimeZone("UTC").getDSTSavings());
      assertEquals(3600000, TimeZone.getTimeZone("America/Los_Angeles").getDSTSavings());
      assertEquals(1800000, TimeZone.getTimeZone("Australia/Lord_Howe").getDSTSavings());
    }
}







