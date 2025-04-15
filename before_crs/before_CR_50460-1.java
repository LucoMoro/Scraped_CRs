/*Phone: Remove stale DELAYED_CLEANUP_AFTER_DISCONNECT

During phone switch due to RAT change, any pending
DELAYED_CLEANUP_AFTER_DISCONNECT message in InCallScreen
should be dropped. If they are not dropped they will be
accessing destroyed phone objects even after phone switch.

Change-Id:Iec801f4150fbbcd36aaa3e207eb116c9597ba2c2CRs-Fixed: 370584*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index f8ba9c7..eb2b3bc 100755

//Synthetic comment -- @@ -1070,6 +1070,8 @@
mCM.unregisterForSuppServiceFailed(mHandler);
mCM.unregisterForIncomingRing(mHandler);
mCM.unregisterForNewRingingConnection(mHandler);
mRegisteredForPhoneStates = false;
}








