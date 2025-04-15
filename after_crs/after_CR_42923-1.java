/*Telephony: Request for sim_status even if there is no UNSL_SIM_STATUS_CHANGED

Some radios/rils lose initial UNSOL_SIM_STATUS_CHANGED indication
(because Telephony wasn't connected to rild when the event happened).
Query for sim_status on power_on.

Bug: 7116148
Change-Id:I448f1b939837a61b7b060068ea1062947a5f6138*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 43404be..8d0868e 100644

//Synthetic comment -- @@ -196,7 +196,7 @@
mCi = ci;
mCi.registerForIccStatusChanged(this, EVENT_ICC_STATUS_CHANGED, null);
// TODO remove this once modem correctly notifies the unsols
        mCi.registerForOn(this, EVENT_ICC_STATUS_CHANGED, null);
}

private synchronized void onGetIccCardStatusDone(AsyncResult ar) {







