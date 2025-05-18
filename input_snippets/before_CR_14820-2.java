
//<Beginning of snippet n. 0>


import dalvik.annotation.TestTargets;

import android.content.Context;
import android.os.Looper;
import android.os.cts.TestThread;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
private TelephonyManager mTelephonyManager;
mTelephonyManager.getDeviceId();
mTelephonyManager.getDeviceSoftwareVersion();
}
}

//<End of snippet n. 0>








