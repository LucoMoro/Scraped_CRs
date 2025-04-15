/*packages/apps/Settings: Change transmit power on hotspot enable/disable

Request the modem to reduce the transmit power when wireless hotspot
is enalbed and increase the transmit power when wireless hotspot is
disabled.

Change-Id:I383833b1550a8977f693af4c647168b54f9681ed*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApEnabler.java b/src/com/android/settings/wifi/WifiApEnabler.java
//Synthetic comment -- index e907cf7..c2aa2b8 100644

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
//Synthetic comment -- @@ -195,6 +200,10 @@
mCheckBox.setChecked(true);
/* Doesnt need the airplane check */
mCheckBox.setEnabled(true);
break;
case WifiManager.WIFI_AP_STATE_DISABLING:
mCheckBox.setSummary(R.string.wifi_stopping);
//Synthetic comment -- @@ -204,6 +213,10 @@
mCheckBox.setChecked(false);
mCheckBox.setSummary(mOriginalSummary);
enableWifiCheckBox();
break;
default:
mCheckBox.setChecked(false);
//Synthetic comment -- @@ -211,4 +224,25 @@
enableWifiCheckBox();
}
}
}







