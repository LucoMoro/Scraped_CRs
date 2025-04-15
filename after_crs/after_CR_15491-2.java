/*Display FDN/ADN records in alphabetical order

As per GSM-BTR-1-5632, terminal must display FDN/ADN records in alphabetical
order. The FDN records as read from SIM are not sorted, so sorting to comply
with spec.

Change-Id:I71de2de0a46dd22bd3cdbb6e4b4c22adeadab464*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 3471ec2..70c6f0cc3 100644

//Synthetic comment -- @@ -30,6 +30,8 @@
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.internal.telephony.IccConstants;
//Synthetic comment -- @@ -215,6 +217,14 @@

private boolean mSimulator;

    private static class AdnComparator implements Comparator<AdnRecord> {
        public final int compare(AdnRecord a, AdnRecord b) {
            String alabel = a.getAlphaTag();
            String blabel = b.getAlphaTag();
            return alabel.compareToIgnoreCase(blabel);
        }
    }

@Override
public boolean onCreate() {
String device = SystemProperties.get("ro.product.device");
//Synthetic comment -- @@ -476,6 +486,7 @@
private ArrayList<ArrayList> loadFromEf(int efType) {
ArrayList<ArrayList> results = new ArrayList<ArrayList>();
List<AdnRecord> adnRecords = null;
        AdnComparator adnComparator = new AdnComparator();

if (DBG) log("loadFromEf: efType=" + efType);

//Synthetic comment -- @@ -495,8 +506,22 @@

int N = adnRecords.size();
if (DBG) log("adnRecords.size=" + N);
            // Making a local copy of records which are non empty
            List<AdnRecord> newAdn = new ArrayList<AdnRecord>();
            for (int i = 0; i < N; i++) {
                AdnRecord record = adnRecords.get(i);
                if (!record.isEmpty()) {
                    newAdn.add(record);
                }
            }
            // Sort the list in ascending order of names
            Collections.sort(newAdn, adnComparator);

            if (DBG) log("loadFromEf: results =" + newAdn);

            N = newAdn.size();
            for (int i = 0; i < N; i++) {
                loadRecord((AdnRecord) newAdn.get(i), results);
}
} else {
// No results to load







