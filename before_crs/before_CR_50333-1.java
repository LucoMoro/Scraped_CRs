/*Fix WPS configuration method of BSS enrollee.

Remove keypad from WPS configuration method of BSS enrollee
becuase JB UI does NOT support keypad, only supports pbc and
display.

Change-Id:I9ab6f1514805e8307b46e38261f1c657568aeb59Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 8a22e96..2d9cc29 100644

//Synthetic comment -- @@ -2358,7 +2358,7 @@
if (!mWifiNative.setSerialNumber(detail)) {
loge("Failed to set serial number " + detail);
}
            if (!mWifiNative.setConfigMethods("physical_display virtual_push_button keypad")) {
loge("Failed to set WPS config methods");
}
if (!mWifiNative.setDeviceType(mPrimaryDeviceType)) {







