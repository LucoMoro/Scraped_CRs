/*Telephony: Notify the data connection failed if data is not possible

Changes in CdmaDCT to notify the data connection failure whenever
it is trying to setup data and if the data connection is not possible.

Changes in GsmDCT to try intiate the data call if the conditions are
met whenever updating the enabled/met flags of the ApnContext to make
sure that the proper data connection state is broadcast.

Change-Id:I18ae177c1b7602de82e52385fdc9840264fefed2*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 0a1b1e4..cf4499d 100644

//Synthetic comment -- @@ -285,6 +285,10 @@
notifyOffApnsOfAvailability(reason);
return retValue;
} else {
            if (!mRequestedApnType.equals(Phone.APN_TYPE_DEFAULT)
                    && (mState == State.IDLE || mState == State.SCANNING)) {
                mPhone.notifyDataConnectionFailed(reason, mRequestedApnType);
            }
notifyOffApnsOfAvailability(reason);
return false;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 95ea107..71cd288 100644

//Synthetic comment -- @@ -1757,13 +1757,16 @@
apnContext.getDependencyMet() +"))");
}
if (apnContext.isReady()) {
            if (enabled && met) {
                trySetup = true;
} else {
                if (!enabled) {
                    apnContext.setReason(Phone.REASON_DATA_DISABLED);
                } else {
                    apnContext.setReason(Phone.REASON_DATA_DEPENDENCY_UNMET);
                }
                cleanup = true;
}
} else {
if (enabled && met) {
if (apnContext.isEnabled()) {







