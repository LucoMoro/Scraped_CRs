/*Telephony: Send disconnect all

Send disconnect to all data connections when telephony is tearing down
all apn contexts. This gives a chance to the lower layers to stay in
sync.

Change-Id:I904419e319b458f69265b47e6258434792209749*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1088131..a6a65e8 100644

//Synthetic comment -- @@ -147,7 +147,7 @@

@Override
public void dispose() {
        cleanUpConnection(true, null, true);

super.dispose();









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 016513c..0cdfb29 100644

//Synthetic comment -- @@ -825,7 +825,7 @@

for (ApnContext apnContext : mApnContexts.values()) {
apnContext.setReason(reason);
            cleanUpConnection(tearDown, apnContext, true);
}

stopNetStatPoll();
//Synthetic comment -- @@ -851,7 +851,7 @@
cleanUpAllConnections(true, cause);
}

    private void cleanUpConnection(boolean tearDown, ApnContext apnContext, boolean doAll) {

if (apnContext == null) {
if (DBG) log("cleanUpConnection: apn context is null");
//Synthetic comment -- @@ -1335,7 +1335,7 @@
// Cleanup those dropped connections
if (DBG) log("onDataStateChange(ar): apnsToCleanup=" + apnsToCleanup);
for (ApnContext apnContext : apnsToCleanup) {
            cleanUpConnection(true, apnContext, false);
}

if (DBG) log("onDataStateChanged(ar): X");
//Synthetic comment -- @@ -1848,7 +1848,7 @@
}
apnContext.setEnabled(enabled);
apnContext.setDependencyMet(met);
        if (cleanup) cleanUpConnection(true, apnContext, false);
if (trySetup) trySetupData(apnContext);
}

//Synthetic comment -- @@ -1976,7 +1976,7 @@
}

if (getOverallState() != DctConstants.State.IDLE) {
            cleanUpConnection(true, null, false);
}
}

//Synthetic comment -- @@ -2221,7 +2221,7 @@
ApnContext apnContext = mApnContexts.get(apnIdToType(apnId));
if (apnContext != null) {
apnContext.setReason(reason);
            cleanUpConnection(tearDown, apnContext, false);
}
}

//Synthetic comment -- @@ -2580,7 +2580,7 @@
boolean tearDown = (msg.arg1 == 0) ? false : true;
if (DBG) log("EVENT_CLEAN_UP_CONNECTION tearDown=" + tearDown);
if (msg.obj instanceof ApnContext) {
                    cleanUpConnection(tearDown, (ApnContext)msg.obj, false);
} else {
loge("EVENT_CLEAN_UP_CONNECTION request w/o apn context");
}







