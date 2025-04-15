/*Static IP switch was causing a Network Connect even when not connected.*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index 3aa31bf..84d6b2a 100644

//Synthetic comment -- @@ -1063,11 +1063,10 @@

case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
/**
		 *  We should call or handle a configuration succeeded event if we
		 *  are already associated.
*/
                if (mWifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
break;
}
mReconnectCount = 0;
//Synthetic comment -- @@ -1865,7 +1864,7 @@
}
checkUseStaticIp();

            if (mWifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
return;
}








