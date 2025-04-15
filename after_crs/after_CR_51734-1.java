/*Stop using KeyStore.state()

Change-Id:Id99ac670a1b96d2defbfff51878e85e9f95e82ba*/




//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index 59cd110..b51f2f3 100644

//Synthetic comment -- @@ -407,9 +407,9 @@
Settings.System.TEXT_SHOW_PASSWORD, 1) != 0);
}

if (mResetCredentials != null) {
            KeyStore keyStore = KeyStore.getInstance();
            mResetCredentials.setEnabled(!keyStore.isUnlocked());
}
}









//Synthetic comment -- diff --git a/src/com/android/settings/vpn2/VpnSettings.java b/src/com/android/settings/vpn2/VpnSettings.java
//Synthetic comment -- index 9d15435..96ac392 100644

//Synthetic comment -- @@ -155,7 +155,7 @@
super.onResume();

// Check KeyStore here, so others do not need to deal with it.
        if (!mKeyStore.isUnlocked()) {
if (!mUnlocking) {
// Let us unlock KeyStore. See you later!
Credentials.getInstance().unlock(getActivity());








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiSettings.java b/src/com/android/settings/wifi/WifiSettings.java
//Synthetic comment -- index dea1618..e607930 100644

//Synthetic comment -- @@ -412,8 +412,7 @@
}

getActivity().registerReceiver(mReceiver, mFilter);
        if (mKeyStoreNetworkId != INVALID_NETWORK_ID && KeyStore.getInstance().isUnlocked()) {
mWifiManager.connect(mKeyStoreNetworkId, mConnectListener);
}
mKeyStoreNetworkId = INVALID_NETWORK_ID;
//Synthetic comment -- @@ -701,8 +700,7 @@
}

private boolean requireKeyStore(WifiConfiguration config) {
        if (WifiConfigController.requireKeyStore(config) && !KeyStore.getInstance().isUnlocked()) {
mKeyStoreNetworkId = config.networkId;
Credentials.getInstance().unlock(getActivity());
return true;







