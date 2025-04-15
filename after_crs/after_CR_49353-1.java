/*Telephony: Update WiFi country code

Upon use of foreign SIM card, WiFi country
code should be the MCC of the registered network.
Currently, country code of the registered operator
is not updated.

This patch updates the WiFi country code with
the country code received as part of the
registration messages.

Change-Id:I789636e87417d97896bee2eba01dcec14840ff43Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57324*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bd13374..604d5ab 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -730,6 +731,21 @@
mSignalStrength = new SignalStrength(true);
}

    private void setWifiCountryCode(String countryCode) {
        if (countryCode != null && !countryCode.isEmpty()) {
            if (DBG) {
                log("WIFI_COUNTRY_CODE set to " + countryCode);
            }

            WifiManager wm =
                    (WifiManager) phone.getContext().getSystemService(Context.WIFI_SERVICE);
            if (wm != null) {
                //persist
                wm.setCountryCode(countryCode, true);
            }
        }
    }

/**
* A complete "service state" from our perspective is
* composed of a handful of separate requests to the radio.
//Synthetic comment -- @@ -919,6 +935,8 @@
phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY, iso);
mGotCountryCode = true;

                setWifiCountryCode(iso);

TimeZone zone = null;

if (!mNitzUpdatedTime && !mcc.equals("000") && !TextUtils.isEmpty(iso) &&







