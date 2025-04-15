/*Telephony: Send disconnect when disposing DCTs

Send disconnect when disposing DCTs. This is invoked multiple times
during Inter-RAT (IRAT) handovers and sending disconnect to lower layers
keeps things in-sync and avoid race conditions as seen in field tests.

Change-Id:I7c0c32a0448934981f0ebbeb9ee2702b87f9b5d9*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1088131..2194c2d 100644

//Synthetic comment -- @@ -147,7 +147,7 @@

@Override
public void dispose() {
        cleanUpConnection(false, null, false);

super.dispose();









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 016513c..45e75b5 100644

//Synthetic comment -- @@ -228,7 +228,7 @@
@Override
public void dispose() {
if (DBG) log("GsmDCT.dispose");
        cleanUpAllConnections(false, null);

super.dispose();








