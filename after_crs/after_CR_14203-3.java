/*telephony: Fix CID when CID is unknown

Unknown CID is indicated as 0xFFFFFFFF by ril. When telephony receives that
value, set CID to UNKNOWN.*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/NeighboringCellInfo.java b/telephony/java/android/telephony/NeighboringCellInfo.java
//Synthetic comment -- index ad7dfc9..2f7666d 100644

//Synthetic comment -- @@ -133,8 +133,11 @@
case NETWORK_TYPE_GPRS:
case NETWORK_TYPE_EDGE:
mNetworkType = radioType;
                // check if 0xFFFFFFFF for UNKNOWN_CID
                if (!location.equalsIgnoreCase("FFFFFFFF")) {
                    mCid = Integer.valueOf(location.substring(4), 16);
                    mLac = Integer.valueOf(location.substring(0, 4), 16);
                }
break;
case NETWORK_TYPE_UMTS:
case NETWORK_TYPE_HSDPA:
//Synthetic comment -- @@ -293,4 +296,4 @@
return new NeighboringCellInfo[size];
}
};
\ No newline at end of file
}







