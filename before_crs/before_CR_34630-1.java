/*Fix interface name for tethering

Fix the interface name used in stopTethering

Change-Id:I3a8fb80c3c6cd382e7641d1735bff85d8a938ee9Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 4539c6b..72585b1 100644

//Synthetic comment -- @@ -1178,14 +1178,14 @@
ip settings */
InterfaceConfiguration ifcg = null;
try {
            ifcg = mNwService.getInterfaceConfig(mInterfaceName);
if (ifcg != null) {
ifcg.addr = new LinkAddress(NetworkUtils.numericToInetAddress(
"0.0.0.0"), 0);
                mNwService.setInterfaceConfig(mInterfaceName, ifcg);
}
} catch (Exception e) {
            loge("Error resetting interface " + mInterfaceName + ", :" + e);
}

if (mCm.untether(mTetherInterfaceName) != ConnectivityManager.TETHER_ERROR_NO_ERROR) {







