/*Fix no data connection issue

Observed an issue that after IMS re-registration failed with "400
Bad request", phone was not re-attach to network.
The root cause for the issue is when retry time reached the max value,
the apn state was set to FAILED, so app's requst will not get handled.
To fix it, in addition to checking apn context is ready, check
disconnected state as well.

Change-Id:Icb0c56ef988f9c3c4f62f38b694edf6708d548c4*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 0969ec8..c1d68d9 100644

//Synthetic comment -- @@ -1852,9 +1852,10 @@
if (DBG) {
log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
"(" + apnContext.isEnabled() + "), " + met + "(" +
                    apnContext.getDependencyMet() +"))" +
                    " apnContext.getState()=" + apnContext.getState());
}
        if (apnContext.isReady() && !apnContext.isDisconnected()) {
if (enabled && met) return;
if (!enabled) {
apnContext.setReason(Phone.REASON_DATA_DISABLED);







