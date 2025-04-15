/*packages/apps/Settings: Change transmit power on hotspot enable/disable

Request the modem to reduce the transmit power when wireless hotspot
is enalbed and increase the transmit power when wireless hotspot is
disabled.

Depends on changes:
 I13f8
 I09dd
 Ic450

Change-Id:I383833b1550a8977f693af4c647168b54f9681ed*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApEnabler.java b/src/com/android/settings/wifi/WifiApEnabler.java
//Synthetic comment -- index e907cf7..dfd5efa 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.settings.wifi;

import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.RILConstants;
import com.android.settings.R;
import com.android.settings.WirelessSettings;

//Synthetic comment -- @@ -33,6 +35,8 @@
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.provider.Settings;
//Synthetic comment -- @@ -41,6 +45,7 @@
import android.widget.Toast;

public class WifiApEnabler implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "WifiApEnabler";
private final Context mContext;
private final CheckBoxPreference mCheckBox;
private final CharSequence mOriginalSummary;
//Synthetic comment -- @@ -50,6 +55,7 @@

ConnectivityManager mCm;
private String[] mWifiRegexs;
    private int oldPowerLevel;

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
@Override
//Synthetic comment -- @@ -111,6 +117,31 @@

final ContentResolver cr = mContext.getContentResolver();
boolean enable = (Boolean)value;
        boolean result = false;

        /* Disable here, enabled on receiving success broadcast */
        mCheckBox.setEnabled(false);

        // Change transmit power based on FCC regulation (CFR47 2.1093) before
        // enabling/disabling the WiFi hotspot
        if (enable) {
            // Request modem to reduce the transmit power when
            // hotspot is enabled
            result = setTransmitPower(RILConstants.TRANSMIT_POWER_WIFI_HOTSPOT);
            oldPowerLevel = RILConstants.TRANSMIT_POWER_DEFAULT;
        } else {
            // Request modem to restore the transmit power to default values
            // when hotspot is disabled
            result = setTransmitPower(RILConstants.TRANSMIT_POWER_DEFAULT);
            oldPowerLevel = RILConstants.TRANSMIT_POWER_WIFI_HOTSPOT;
        }

        if (result == false) {
            Log.d(TAG, "Failed to set the transmit power");
            mCheckBox.setEnabled(true);
            mCheckBox.setSummary(R.string.wifi_error);
            return false;
        }

/**
* Disable Wifi if enabling tethering
//Synthetic comment -- @@ -122,10 +153,8 @@
Settings.Secure.putInt(cr, Settings.Secure.WIFI_SAVED_STATE, 1);
}

        if (mWifiManager.setWifiApEnabled(null, enable) == false) {
            mCheckBox.setEnabled(true);
mCheckBox.setSummary(R.string.wifi_error);
}

//Synthetic comment -- @@ -209,6 +238,35 @@
mCheckBox.setChecked(false);
mCheckBox.setSummary(R.string.wifi_error);
enableWifiCheckBox();

                // In case of failure in enabling/disabling WiFi hotspot restore
                // the transmit power level to the old power level
                Log.e(TAG, "Fail to enable/disable the WiFi hotspot, " +
                           "reverting the transmit power level");
                setTransmitPower(oldPowerLevel);
}
}

    /**
     * Sets the transmit power level
     *
     * @param powerLevel
     */
    private boolean setTransmitPower(int powerLevel) {
        boolean result = false;
        ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
        if (phone == null) {
            Log.e(TAG, "ITelephony interface is null, can not set transmit power");
            return false;
        }

        // Request modem to change the transmit power
        try {
            Log.d(TAG, "Setting transmit power to " + powerLevel);
            result = phone.setTransmitPower(powerLevel);
        } catch (RemoteException ex) {
            Log.e(TAG, "RemoteException during setting max transmit power", ex);
        }
        return result;
    }
}







