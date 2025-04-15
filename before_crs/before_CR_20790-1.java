/*WPA rekeying displayed as Obtaining IP

Successful rekeying is finished with the supplicantState completed.
This state is mapped to the UI-string Obtaining IP address.
Looking at the current IP address can separate when state is
Obtaining IP and when actually already connected.

Change-Id:I95e1df4351fcd44629a48592cfb858d09c70ea8d*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiEnabler.java b/src/com/android/settings/wifi/WifiEnabler.java
//Synthetic comment -- index ef9f346..f4c0ead 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
//Synthetic comment -- @@ -143,6 +144,9 @@
if (state != null && mCheckBox.isChecked()) {
WifiInfo info = mWifiManager.getConnectionInfo();
if (info != null) {
mCheckBox.setSummary(Summary.get(mContext, info.getSSID(), state));
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiSettings.java b/src/com/android/settings/wifi/WifiSettings.java
//Synthetic comment -- index d389cae..f6ae708 100644

//Synthetic comment -- @@ -437,7 +437,12 @@
}

if (state == DetailedState.OBTAINING_IPADDR) {
            mScanner.pause();
} else {
mScanner.resume();
}







