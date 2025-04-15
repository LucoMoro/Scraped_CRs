/*SoftAP: wrong state variable in setWifiApEnabledState()

setWifiApEnabledState() has not been unloading the
driver when called with WIFI_AP_STATE_FAILED and
DriverAction.NO_DRIVER_UNLOAD states.
State variable name corrected.

Change-Id:I34e9ab78e0e33fba4ab4868179c2a49768eafd8cSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index 5aa0111..c422e69 100644

//Synthetic comment -- @@ -779,7 +779,7 @@
/**
* Unload the driver if going to a failed state
*/
        if ((mWifiApState == WIFI_AP_STATE_FAILED) && (flag == DriverAction.DRIVER_UNLOAD)) {
mWifiStateTracker.unloadDriver();
}








