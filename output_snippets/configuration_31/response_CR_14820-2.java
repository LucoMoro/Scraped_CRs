//<Beginning of snippet n. 0>

import dalvik.annotation.TestTargets;
import android.content.Context;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {
    private TelephonyManager mTelephonyManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        assertNotNull("TelephonyManager is null", mTelephonyManager);
    }

    public void testDeviceId() {
        String deviceId = mTelephonyManager.getDeviceId();
        boolean isValidDeviceId = validateDeviceId(deviceId);
        assertTrue("Invalid Device ID: " + getErrorMessage(deviceId), isValidDeviceId);
    }

    private boolean validateDeviceId(String deviceId) {
        if (deviceId == null) {
            return false;
        }

        if (deviceId.length() == 15) {
            return isValidIMEI(deviceId);
        } else if (deviceId.length() == 14 && deviceId.matches("[0-9A-Fa-f]+")) {
            return true; // MEID (Hexadecimal)
        } else if (deviceId.length() == 18 && deviceId.matches("[0-9]+")) {
            return validateDecimalMEID(deviceId); // MEID (Decimal)
        } else if (deviceId.length() == 17 && deviceId.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")) {
            return true; // MAC Address
        }

        return false;
    }

    private boolean isValidIMEI(String imei) {
        return imei.length() == 15 && luhnCheck(imei);
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

    private boolean validateDecimalMEID(String meid) {
        return meid.length() == 18 && meid.matches("[0-9]+");
    }

    private String getErrorMessage(String deviceId) {
        if (deviceId == null) return "Device ID is null";
        if (deviceId.length() == 15) return "IMEI format is invalid";
        if (deviceId.length() == 14 && !deviceId.matches("[0-9A-Fa-f]+")) return "MEID (Hexadecimal) format is invalid";
        if (deviceId.length() == 18 && !deviceId.matches("[0-9]+")) return "MEID (Decimal) format is invalid";
        if (deviceId.length() == 17 && !deviceId.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")) return "MAC Address format is invalid";
        return "Unknown format";
    }
}

//<End of snippet n. 0>