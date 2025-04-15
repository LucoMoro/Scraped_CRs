/*Telephony: Adding HSPAP (HSPA+) support in Android

HSPA+ management is missing in JB source.

The value NETWORK_TYPE_HSPAP should be added in
switch cases handling network type.

Change-Id:Ia86bfce0baec63b5ee6a3a7195981e2429fc74b9Author: Philippe Regnier <philippex.regnier@intel.com>
Signed-off-by: Philippe Regnier <philippex.regnier@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 41786*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c3256df..aac76d8 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;

import android.content.BroadcastReceiver;
import android.content.Context;
//Synthetic comment -- @@ -3299,6 +3300,8 @@
radioType = NETWORK_TYPE_HSUPA;
} else if (radioString.equals("HSPA")) {
radioType = NETWORK_TYPE_HSPA;
} else {
radioType = NETWORK_TYPE_UNKNOWN;
}







