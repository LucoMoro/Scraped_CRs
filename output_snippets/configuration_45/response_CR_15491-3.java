//<Beginning of snippet n. 0>


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import com.android.internal.telephony.IccConstants;

private boolean mSimulator;

@Override
public boolean onCreate() {
    String device = SystemProperties.get("ro.product.device");
}

private ArrayList<ArrayList> loadFromEf(int efType) {
    ArrayList<ArrayList> results = new ArrayList<ArrayList>();
    List<AdnRecord> adnRecords = null;

    if (DBG) log("loadFromEf: efType=" + efType);

    if (adnRecords != null && !adnRecords.isEmpty()) {
        // Sort records in alphabetical order
        Collections.sort(adnRecords, new Comparator<AdnRecord>() {
            @Override
            public int compare(AdnRecord a1, AdnRecord a2) {
                return a1.getName().toLowerCase().compareTo(a2.getName().toLowerCase());
            }
        });

        // Load the results
        int N = adnRecords.size();
        if (DBG) log("adnRecords.size=" + N);
        for (int i = 0; i < N; i++) {
            loadRecord(adnRecords.get(i), results);
        }
    } else {
        // No results to load
    }

//<End of snippet n. 0>