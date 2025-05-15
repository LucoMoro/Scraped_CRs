
//<Beginning of snippet n. 0>


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.internal.telephony.IccConstants;

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
private ArrayList<ArrayList> loadFromEf(int efType) {
ArrayList<ArrayList> results = new ArrayList<ArrayList>();
List<AdnRecord> adnRecords = null;
        AdnComparator adnComparator = new AdnComparator();

if (DBG) log("loadFromEf: efType=" + efType);

if (adnRecords != null) {
// Load the results

            // Making a local copy of records which are non empty
            List<AdnRecord> newAdn = new ArrayList<AdnRecord>();
            for (AdnRecord record : adnRecords) {
                if (!record.isEmpty()) {
                    newAdn.add(record);
                }
            }
            // Sort the list in ascending order of names
            Collections.sort(newAdn, adnComparator);

            if (DBG) log("loadFromEf: results =" + newAdn);

            for (AdnRecord record : newAdn) {
                loadRecord(record, results);
}
} else {
// No results to load

//<End of snippet n. 0>








