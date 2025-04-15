/*Static IP switch was causing a Network Connect even when not connected.*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index 6ea35f5..aab7110 100644

//Synthetic comment -- @@ -1039,11 +1039,10 @@

case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
/**
                 * Since this event is sent from another thread, it might have been
                 * sent after we closed our connection to the supplicant in the course
                 * of disabling Wi-Fi. In that case, we should just ignore the event.
*/
                if (mWifiInfo.getSupplicantState() == SupplicantState.UNINITIALIZED) {
break;
}
mReconnectCount = 0;
//Synthetic comment -- @@ -1841,7 +1840,7 @@
}
checkUseStaticIp();

            if (mWifiInfo.getSupplicantState() == SupplicantState.UNINITIALIZED) {
return;
}








