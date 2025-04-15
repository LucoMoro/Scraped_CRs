/*BatteryHistoryDetail to use batteryinfo service for stats

Change-Id:I357dea9a1695bb76a36ddfc84bcd376b9d79a028*/




//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/BatteryHistoryDetail.java b/src/com/android/settings/fuelgauge/BatteryHistoryDetail.java
//Synthetic comment -- index c673ce3..2784825 100644

//Synthetic comment -- @@ -20,18 +20,40 @@
import android.os.Bundle;
import android.os.Parcel;

import com.android.internal.app.IBatteryStats;
import android.util.Log;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.os.BatteryStatsImpl;
import com.android.settings.R;

public class BatteryHistoryDetail extends Activity {
public static final String EXTRA_STATS = "stats";

    private static final String TAG = "BatteryHistoryDetail";

    IBatteryStats mBatteryInfo;
private BatteryStatsImpl mStats;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
byte[] data = getIntent().getByteArrayExtra(EXTRA_STATS);

        if (data == null) {

             mBatteryInfo = IBatteryStats.Stub.asInterface(
                     ServiceManager.getService("batteryinfo"));

             if (mBatteryInfo != null) {
                 try {
                     data = mBatteryInfo.getStatistics();
                 } catch(RemoteException e) {
                     Log.e(TAG, "RemoteException - fetch of stats failed");
                 }
             }
        }

Parcel parcel = Parcel.obtain();
parcel.unmarshall(data, 0, data.length);
parcel.setDataPosition(0);







