/*Static IP switch was causing a Network Connect even when not connected.*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index 6ea35f5..aab7110 100644

//Synthetic comment -- @@ -1039,11 +1039,10 @@

case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
/**
		 *  We should call or handle a configuration succeeded event if we
		 *  are already associated.
*/
                if (mWifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
break;
}
mReconnectCount = 0;
//Synthetic comment -- @@ -1841,7 +1840,7 @@
}
checkUseStaticIp();

            if (mWifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
return;
}








