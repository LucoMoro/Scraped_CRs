/*Check connection status before cleaning up the connnection.

The Mms application calls beginMmsConnectivity in TransactionService.java
consecutively while we receive or send mms, then enableApnType in
GsmDataConnectionTracker.java will be called consecutively also.
So we need to check connection status before cleanup the connnection.

Change-Id:Ie07b2336c591d904aeb9a983f377b3271da4007b*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 7b60474..f654d61 100644

//Synthetic comment -- @@ -1070,7 +1070,9 @@

// TODO:  To support simultaneous PDP contexts, this should really only call
// cleanUpConnection if it needs to free up a PdpConnection.
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
}

protected boolean onTrySetupData(String reason) {







