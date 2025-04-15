/*Telephony: Notify the data connection failed if data is not possible

Changes in CdmaDCT to notify the data connection failure whenever
it is trying to setup data and if the data connection is not possible.

Changes in GsmDCT to try intiate the data call if the conditions are
met whenever updating the enabled/met flags of the ApnContext to make
sure that the proper data connection state is broadcast.

Change-Id:I7528b9789a5622f397d555b944b8cb9b43b5160e*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index c0dbf9f..1d0d285 100644

//Synthetic comment -- @@ -292,6 +292,10 @@
notifyOffApnsOfAvailability(reason);
return retValue;
} else {
notifyOffApnsOfAvailability(reason);
return false;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ebb41fc..d9f54f4 100644

//Synthetic comment -- @@ -1564,13 +1564,16 @@
apnContext.getDependencyMet() +"))");
}
if (apnContext.isReady()) {
            if (enabled && met) return;
            if (!enabled) {
                apnContext.setReason(Phone.REASON_DATA_DISABLED);
} else {
                apnContext.setReason(Phone.REASON_DATA_DEPENDENCY_UNMET);
}
            cleanup = true;
} else {
if (enabled && met) {
if (apnContext.isEnabled()) {







