/*WiFi static IP configuration bug

This fix makes sure that WiFi does not get connected
when configuring static IP and not associated to any
access point.

Change-Id:I42909008de9d0a0bd33115e151161ca01dd0d884*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index aef93c6..84e4b16 100644

//Synthetic comment -- @@ -1112,7 +1112,7 @@
* sent after we closed our connection to the supplicant in the course
* of disabling Wi-Fi. In that case, we should just ignore the event.
*/
                if (mWifiInfo.getSupplicantState() == SupplicantState.UNINITIALIZED) {
break;
}
mReconnectCount = 0;
//Synthetic comment -- @@ -2448,7 +2448,7 @@
}
checkUseStaticIp();

            if (mWifiInfo.getSupplicantState() == SupplicantState.UNINITIALIZED) {
return;
}








