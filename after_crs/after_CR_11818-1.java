/*Mms application will call beginMmsConnectivity in TransactionService.java consecutively while we receive or send mms,
then enableApnType in GsmDataConnectionTracker.java will be called consecutively also.
So we need to check connection status before cleanup the connnection.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index d48c941..b5e523a 100644

//Synthetic comment -- @@ -1233,7 +1233,9 @@
protected void onEnableNewApn() {
// TODO:  To support simultaneous PDP contexts, this should really only call
// cleanUpConnection if it needs to free up a PdpConnection.
        if (state == State.FAILED) {
            cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
        }
}

protected void onTrySetupData(String reason) {







