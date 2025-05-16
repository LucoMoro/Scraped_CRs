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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void testGetDeviceId() {
        String deviceId = mTelephonyManager.getDeviceId();
        assertNotNull("Device ID should not be null", deviceId);
        assertTrue("Device ID should be 15 digits long", deviceId.length() == 15 && isValidIMEI(deviceId));
    }

    public void testGetDeviceSoftwareVersion() {
        String deviceSoftwareVersion = mTelephonyManager.getDeviceSoftwareVersion();
        assertNotNull("Device Software Version should not be null", deviceSoftwareVersion);
    }

    private boolean isValidIMEI(String imei) {
        return imei.matches("\\d{15}") && luhnCheck(imei);
    }

    private boolean luhnCheck(String imei) {
        int sum = 0;
        boolean alternate = false;
        for (int i = imei.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(imei.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = n % 10 + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
//<End of snippet n. 0>