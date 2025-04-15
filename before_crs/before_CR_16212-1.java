/*Remove unused imports from WirelessSettings.java

Change-Id:I6433faae26b1eb216becd90a655939901f5d2105*/
//Synthetic comment -- diff --git a/src/com/android/settings/WirelessSettings.java b/src/com/android/settings/WirelessSettings.java
//Synthetic comment -- index 78cf8cf..281592f 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.SystemProperties;
//Synthetic comment -- @@ -29,7 +28,6 @@
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
//Synthetic comment -- @@ -145,21 +143,21 @@
@Override
protected void onResume() {
super.onResume();
        
mAirplaneModeEnabler.resume();
mWifiEnabler.resume();
mBtEnabler.resume();
}
    
@Override
protected void onPause() {
super.onPause();
        
mAirplaneModeEnabler.pause();
mWifiEnabler.pause();
mBtEnabler.pause();
}
    
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if (requestCode == REQUEST_CODE_EXIT_ECM) {







