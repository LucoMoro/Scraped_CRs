/*Display FDN/ADN records in alphabetical order

As per GSM-BTR-1-5632, terminal must display FDN/ADN records in alphabetical
order. The FDN records as read from SIM are not sorted, so sorting to comply
with spec.

Change-Id:I71de2de0a46dd22bd3cdbb6e4b4c22adeadab464*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 3471ec2..0218ca7 100644

//Synthetic comment -- @@ -30,6 +30,8 @@
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -215,6 +217,15 @@

private boolean mSimulator;

@Override
public boolean onCreate() {
String device = SystemProperties.get("ro.product.device");
//Synthetic comment -- @@ -494,9 +505,25 @@
// Load the results

int N = adnRecords.size();
            if (DBG) log("adnRecords.size=" + N);
            for (int i = 0; i < N ; i++) {
                loadRecord(adnRecords.get(i), results);
}
} else {
// No results to load







