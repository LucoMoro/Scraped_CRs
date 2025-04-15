/*Fix for F/C in WifiConfigInfo

1. Turn off Wi-Fi
2. Open the dialer and input *#*#4636#*#*
3. Navigate to Wi-Fi information -> Wi-Fi config and observe "Unfortunately,
Settings has stopped" dialog.

Change-Id:I6ed59fc157e9bd8a46e00184b91a1a9f0412d198Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiConfigInfo.java b/src/com/android/settings/wifi/WifiConfigInfo.java
//Synthetic comment -- index 2ed4f02..9b680a8 100644

//Synthetic comment -- @@ -31,15 +31,9 @@
*/
public class WifiConfigInfo extends Activity {

    private static final String TAG = "WifiConfigInfo";

private TextView mConfigList;
private WifiManager mWifiManager;

    //============================
    // Activity lifecycle
    //============================

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -52,12 +46,16 @@
@Override
protected void onResume() {
super.onResume();
        final List<WifiConfiguration> wifiConfigs = mWifiManager.getConfiguredNetworks();
        StringBuffer configList  = new StringBuffer();
        for (int i = wifiConfigs.size() - 1; i >= 0; i--) {
            configList.append(wifiConfigs.get(i));
}
        mConfigList.setText(configList);
}

}







