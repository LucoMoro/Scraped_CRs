/*Change the checking status*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index b5e523a..0a823de 100644

//Synthetic comment -- @@ -1233,7 +1233,7 @@
protected void onEnableNewApn() {
// TODO:  To support simultaneous PDP contexts, this should really only call
// cleanUpConnection if it needs to free up a PdpConnection.
        if (state == State.FAILED) {
cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
}
}







