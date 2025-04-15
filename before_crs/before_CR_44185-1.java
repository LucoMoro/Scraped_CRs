/*Slim down ZoneInfo.toString.

Dalvik Explorer can now do a better job of helping examine transition data,
and regular developers don't need to see it. For America/Los_Angeles, for
example, toString used to output 186 lines. Now it just returns:

  libcore.util.ZoneInfo[id="America/Los_Angeles",mRawOffset=-28800000,mEarliestRawOffset=-28800000,mUseDst=true,mDstSavings=3600000,transitions=185]

Also fix an incorrect comment from the TimeZone documentation.

Change-Id:I5748845a7b4f911e99a0e1c2e1a0786742288518*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 85011bc..a5e0ad7 100644

//Synthetic comment -- @@ -55,14 +55,11 @@
*
* <p>Note the type returned by the factory methods {@link #getDefault} and {@link #getTimeZone} is
* implementation dependent. This may introduce serialization incompatibility issues between
 * different implementations. Android returns instances of {@link SimpleTimeZone} so that
 * the bytes serialized by Android can be deserialized successfully on other
 * implementations, but the reverse compatibility cannot be guaranteed.
*
* @see Calendar
* @see GregorianCalendar
* @see SimpleDateFormat
 * @see SimpleTimeZone
*/
public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfo.java b/luni/src/main/java/libcore/util/ZoneInfo.java
//Synthetic comment -- index ac48b23..18e4f21 100644

//Synthetic comment -- @@ -16,10 +16,8 @@

package libcore.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

/**
//Synthetic comment -- @@ -43,15 +41,14 @@
};

private int mRawOffset;

private final int mEarliestRawOffset;

private final int[] mTransitions;
private final int[] mOffsets;
private final byte[] mTypes;
private final byte[] mIsDsts;
    private final boolean mUseDst;
    private final int mDstSavings; // Implements TimeZone.getDSTSavings.

ZoneInfo(String name, int[] transitions, byte[] types, int[] gmtOffsets, byte[] isDsts) {
mTransitions = transitions;
//Synthetic comment -- @@ -254,29 +251,12 @@

@Override
public String toString() {
        StringBuilder sb = new StringBuilder();
        // First the basics...
        sb.append(getClass().getName() + "[" + getID() + ",mRawOffset=" + mRawOffset +
                ",mUseDst=" + mUseDst + ",mDstSavings=" + mDstSavings + "]");
        // ...followed by a zdump(1)-like description of all our transition data.
        sb.append("\n");
        Formatter f = new Formatter(sb);
        for (int i = 0; i < mTransitions.length; ++i) {
            int type = mTypes[i] & 0xff;
            String utcTime = formatTime(mTransitions[i], TimeZone.getTimeZone("UTC"));
            String localTime = formatTime(mTransitions[i], this);
            int offset = mOffsets[type];
            int gmtOffset = mRawOffset/1000 + offset;
            f.format("%4d : time=%11d %s = %s isDst=%d offset=%5d gmtOffset=%d\n",
                    i, mTransitions[i], utcTime, localTime, mIsDsts[type], offset, gmtOffset);
        }
        return sb.toString();
    }

    private static String formatTime(int s, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy zzz");
        sdf.setTimeZone(tz);
        long ms = ((long) s) * 1000L;
        return sdf.format(new Date(ms));
}
}







