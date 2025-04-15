/*Telephony: TZ update when the country code is NOK

3GPP Conformance 26.7.6.1.1 is failing because the telephony
framework doesn't update NITZ timezeone correctly.

Steps to Reproduce: Execute 3GPP Conformance 26.7.6.1.1
1. UE receives MM INFORMATION containing "0 hour" as a timezone.
Expected Results: UE updates local timezone with GMT+0 value.
Actual Results: UE doesn't update local timezone and keeps
preceding value.

This patch integrates the TZ update when the check TZ is not OK
(Country Code NOK)

Change-Id:Ie3b4d2889a08a49c84067cab03f16140525d9ee0Author: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 33279*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bd13374..aacc95b 100644

//Synthetic comment -- @@ -1450,6 +1450,7 @@
mZoneOffset  = tzOffset;
mZoneDst     = dst != 0;
mZoneTime    = c.getTimeInMillis();
}

if (zone != null) {







