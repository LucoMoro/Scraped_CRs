/*Telephony: Add HSPAP to getTcpBufferSizesPropName

When device is camped on HSPAP TCP Buffer sizes need to be configured
properly to support higher data rates. Without this change, default TCP
buffer sizes will be used and throughput will be low.

Change-Id:Ica5884b35851a32e57c82c63b148df5be580ae83*/




//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index 5612943..4882370 100644

//Synthetic comment -- @@ -327,6 +327,7 @@
networkTypeStr = "hsupa";
break;
case TelephonyManager.NETWORK_TYPE_HSPA:
        case TelephonyManager.NETWORK_TYPE_HSPAP:
networkTypeStr = "hspa";
break;
case TelephonyManager.NETWORK_TYPE_CDMA:







