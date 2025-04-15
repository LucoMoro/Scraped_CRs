/*Phone: Display Short Name/MCCMNC if Long Name is empty in manual search.

The operator name for each operator obtained in the Manual Network Search
should be derived as per 3GPP spec TS 22.101 A.3(Country/PLMN indication).
While deriving the name for current operator (already registered operator), as
per the spec if NITZ name has to be displayed, then some times the long name
can be empty in MM information. Since the operator information should be derived
from a single source, MM inforamtion cannot be combined with other sources
like ME DB information for long name. In such cases, Short Name or MCCMNC string
should be displayed in the Manual Network Selection.

Change-Id:I6f24cd73ff582421506cec3ad419f012df827174*/
//Synthetic comment -- diff --git a/src/com/android/phone/NetworkSetting.java b/src/com/android/phone/NetworkSetting.java
//Synthetic comment -- index cfad1ae..39e1a29 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.android.internal.telephony.CommandException;
//Synthetic comment -- @@ -403,7 +404,7 @@
// confusing mcc/mnc.
for (NetworkInfo ni : result) {
Preference carrier = new Preference(this, null);
                    carrier.setTitle(ni.getOperatorAlphaLong());
carrier.setPersistent(false);
mNetworkList.addPreference(carrier);
mNetworkMap.put(carrier, ni);
//Synthetic comment -- @@ -417,6 +418,25 @@
}
}

private void clearList() {
for (Preference p : mNetworkMap.keySet()) {
mNetworkList.removePreference(p);







