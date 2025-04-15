/*packages/apps/Settings: Change transmit power on hotspot enable/disable

Request the modem to reduce the transmit power when wireless hotspot
is enalbed and increase the transmit power when wireless hotspot is
disabled.

Change-Id:I383833b1550a8977f693af4c647168b54f9681ed*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApEnabler.java b/src/com/android/settings/wifi/WifiApEnabler.java
//Synthetic comment -- index e907cf7..dfd5efa 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.settings.wifi;

import com.android.settings.R;
import com.android.settings.WirelessSettings;

//Synthetic comment -- @@ -33,6 +35,8 @@
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.provider.Settings;
//Synthetic comment -- @@ -41,6 +45,7 @@
import android.widget.Toast;

public class WifiApEnabler implements Preference.OnPreferenceChangeListener {
private final Context mContext;
private final CheckBoxPreference mCheckBox;
private final CharSequence mOriginalSummary;
//Synthetic comment -- @@ -50,6 +55,7 @@

ConnectivityManager mCm;
private String[] mWifiRegexs;

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
@Override
//Synthetic comment -- @@ -111,6 +117,31 @@

final ContentResolver cr = mContext.getContentResolver();
boolean enable = (Boolean)value;

/**
* Disable Wifi if enabling tethering
//Synthetic comment -- @@ -122,10 +153,8 @@
Settings.Secure.putInt(cr, Settings.Secure.WIFI_SAVED_STATE, 1);
}

        if (mWifiManager.setWifiApEnabled(null, enable)) {
            /* Disable here, enabled on receiving success broadcast */
            mCheckBox.setEnabled(false);
        } else {
mCheckBox.setSummary(R.string.wifi_error);
}

//Synthetic comment -- @@ -209,6 +238,35 @@
mCheckBox.setChecked(false);
mCheckBox.setSummary(R.string.wifi_error);
enableWifiCheckBox();
}
}
}







