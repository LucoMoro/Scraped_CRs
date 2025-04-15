/*p2p: use pbc as default config method.

device found event based on beacons result in
config method as 0. when user tries to connect
we should be using pbc as default config.

Change-Id:If5c7ed7bff701f75716469dd7aa96c6c33cdf768Signed-off-by: mahesh kumar <mahe209069@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/p2p/WifiP2pSettings.java b/src/com/android/settings/wifi/p2p/WifiP2pSettings.java
//Synthetic comment -- index 83659ee..bd40390 100644

//Synthetic comment -- @@ -371,9 +371,10 @@
config.wps.setup = WpsInfo.PBC;
} else if (mSelectedWifiPeer.device.wpsKeypadSupported()) {
config.wps.setup = WpsInfo.KEYPAD;
                    } else {
config.wps.setup = WpsInfo.DISPLAY;
                    }
}

mWifiP2pManager.connect(mChannel, config,







