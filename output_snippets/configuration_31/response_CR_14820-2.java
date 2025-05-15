//<Beginning of snippet n. 0>

import dalvik.annotation.TestTargets;
import android.content.Context;
import android.os.Looper;
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
        // Ensure mTelephonyManager is initialized
        assertNotNull("TelephonyManager is not initialized", mTelephonyManager);
        String deviceId = mTelephonyManager.getDeviceId();

        // Device ID verification
        if (deviceId != null) {
            if (phoneTypeIsGSM()) {
                assertTrue("IMEI should be 15 digits", deviceId.matches("\\d{15}"));
                assertTrue("IMEI should be valid", isValidIMEI(deviceId));
            } else if (phoneTypeIsCDMA()) {
                assertTrue("MEID should be 14 hex digits", deviceId.matches("[0-9A-Fa-f]{14}"));
            } else {
                assertFalse("Device type is unknown", true);
            }
        }
    }

    public void testGetDeviceSoftwareVersion() {
        String softwareVersion = mTelephonyManager.getDeviceSoftwareVersion();
        assertNotNull("Device software version is null", softwareVersion);
    }

    private boolean phoneTypeIsGSM() {
        // Logic to determine if the phone type is GSM
        return mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM;
    }

    private boolean phoneTypeIsCDMA() {
        // Logic to determine if the phone type is CDMA
        return mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA;
    }

    private boolean isValidIMEI(String imei) {
        return isValidLuhn(imei);
    }

    private boolean isValidLuhn(String imei) {
        int sum = 0;
        boolean alternate = false;
        for (int i = imei.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(imei.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = n - 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}

//<End of snippet n. 0>