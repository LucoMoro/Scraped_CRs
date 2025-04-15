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
 * different implementations, or different versions of Android.
*
* @see Calendar
* @see GregorianCalendar
* @see SimpleDateFormat
*/
public abstract class TimeZone implements Serializable, Cloneable {
private static final long serialVersionUID = 3581463369166924961L;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/ZoneInfo.java b/luni/src/main/java/libcore/util/ZoneInfo.java
//Synthetic comment -- index ac48b23..18e4f21 100644

//Synthetic comment -- @@ -16,10 +16,8 @@

package libcore.util;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
//Synthetic comment -- @@ -43,15 +41,14 @@
};

private int mRawOffset;
private final int mEarliestRawOffset;
    private final boolean mUseDst;
    private final int mDstSavings; // Implements TimeZone.getDSTSavings.

private final int[] mTransitions;
private final int[] mOffsets;
private final byte[] mTypes;
private final byte[] mIsDsts;

ZoneInfo(String name, int[] transitions, byte[] types, int[] gmtOffsets, byte[] isDsts) {
mTransitions = transitions;
//Synthetic comment -- @@ -254,29 +251,12 @@

@Override
public String toString() {
        return getClass().getName() + "[id=\"" + getID() + "\"" +
            ",mRawOffset=" + mRawOffset +
            ",mEarliestRawOffset=" + mEarliestRawOffset +
            ",mUseDst=" + mUseDst +
            ",mDstSavings=" + mDstSavings +
            ",transitions=" + mTransitions.length +
            "]";
}
}







