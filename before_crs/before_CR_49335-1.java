/*Telephony: Fix issue in automatic timezone update

Some networks provides the timezone offset including
daylight saving time but doesn't provide the daylight
saving time separately. Android framework compares
both the timezone offset and dst to fetch the correct
zone information. Since the NITZ provided DST doesn't
match with the one stored in device side, correct
zone information is not fetched resulting in automatic
timezone update not happening properly.

If the daylight saving time is not provided, then
force the android to fetch the zone by informing
that the daylight saving time is provided.

Change-Id:I69b1d8deddc4d04a67c0a4bc098fd63ec97585d6Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 29186*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bd13374..63bac3e 100644

//Synthetic comment -- @@ -999,6 +999,10 @@
} else {
zone = TimeUtils.getTimeZone(mZoneOffset, mZoneDst, mZoneTime, iso);
if (DBG) log("pollStateDone: using getTimeZone(off, dst, time, iso)");
}

mNeedFixZoneAfterNitz = false;
//Synthetic comment -- @@ -1428,9 +1432,16 @@

if (mGotCountryCode) {
if (iso != null && iso.length() > 0) {
                        zone = TimeUtils.getTimeZone(tzOffset, dst != 0,
c.getTimeInMillis(),
iso);
} else {
// We don't have a valid iso country code.  This is
// most likely because we're on a test network that's







