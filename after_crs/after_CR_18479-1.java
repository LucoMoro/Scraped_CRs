/*Set the maximum acceptable length of the WIFI SSID to 32

The maximum SSID length is 32. When the length of SSID
is longer than 32, we disable the save button. This could
avoid problem when there are wrong implementation in
the lower WIFI layer.
Author: Poseng_Lin <Poseng_Lin@acer.com.tw>

Change-Id:Ia4452e3c2716941a1a1e819ad25e9cb9b147d669*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApDialog.java b/src/com/android/settings/wifi/WifiApDialog.java
//Synthetic comment -- index 43289d2..d1d277b 100644

//Synthetic comment -- @@ -52,6 +52,8 @@
private static final int OPEN_INDEX = 0;
private static final int WPA_INDEX = 1;

    private static final int MAX_SSID_LENGTH = 32;

private View mView;
private TextView mSsid;
private int mSecurityType = AccessPoint.SECURITY_NONE;
//Synthetic comment -- @@ -142,7 +144,7 @@
}

private void validate() {
        if ((mSsid != null && (mSsid.length() == 0 || mSsid.length() > MAX_SSID_LENGTH)) ||
(mSecurityType == AccessPoint.SECURITY_PSK && mPassword.length() < 8)) {
getButton(BUTTON_SUBMIT).setEnabled(false);
} else {







