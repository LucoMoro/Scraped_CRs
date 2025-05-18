//<Beginning of snippet n. 0>

import dalvik.annotation.TestTargets;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
    private TelephonyManager mTelephonyManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        
        if (mTelephonyManager != null) {
            String deviceId = mTelephonyManager.getDeviceId();
            if (deviceId != null) {
                validateDeviceId(deviceId);
            } else {
                throw new IllegalArgumentException("Device ID cannot be null");
            }
        } else {
            throw new IllegalStateException("TelephonyManager is not available");
        }
    }
    
    private void validateDeviceId(String deviceId) {
        if (deviceId.matches("\\d{15}") && luhnCheck(deviceId)) {
            // Valid IMEI
        } else if (deviceId.matches("[0-9A-Fa-f]{14}") || deviceId.matches("[0-9A-Fa-f]{18}")) {
            // Valid MEID
        } else if (!deviceId.matches("[0-9A-Fa-f:]{17}")) { // MAC Address pattern
            throw new IllegalArgumentException("Invalid Device ID format: must be 15 digit IMEI, 14 or 18 character MEID, or 17 character MAC address");
        }
    }

    private boolean luhnCheck(String imei) {
        int sum = 0;
        boolean alternate = false;
        for (int i = imei.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(imei.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    
    // Add more test cases for edge scenarios here
}

//<End of snippet n. 0>