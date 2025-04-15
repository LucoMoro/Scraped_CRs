/*telephony: Handle RIL_UNSOL_RESPONSE_TETHERED_MODE_STATE_CHANGED

- Upon receipt of this indication with MODE as OFF, reset the
retry timer for the data call. This will ensure that the embedded call
will be established immediately after the tethered call has completed
- Disable retry during active tethered call
- If v4 tethered call is started with a v4v6 embedded call, retry will
 still occur, during this time, multiple failures lead to
 re-registrering to the network which breaks the tethered call. This
 fix will skip re-registering to the network during a tethered call.

Change-Id:I991675b8fa0f65db0b46090cb198c96983b6e499*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnContext.java b/src/java/com/android/internal/telephony/ApnContext.java
//Synthetic comment -- index b6083ad..b431431 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -201,7 +202,7 @@
}

public boolean isReady() {
        return mDataEnabled.get() && mDependencyMet.get();
}

public void setEnabled(boolean enabled) {
//Synthetic comment -- @@ -243,4 +244,12 @@
public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
pw.println("ApnContext: " + this.toString());
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnSetting.java b/src/java/com/android/internal/telephony/ApnSetting.java
//Synthetic comment -- index b84c69c..cc1ae45 100755

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -51,6 +52,8 @@
*/
public final int bearer;

public ApnSetting(int id, String numeric, String carrier, String apn,
String proxy, String port,
String mmsc, String mmsProxy, String mmsPort,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 49d3c76..b9e1332 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -71,6 +72,7 @@
protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
protected RegistrantList mIccRefreshRegistrants = new RegistrantList();

protected Registrant mGsmSmsRegistrant;
protected Registrant mCdmaSmsRegistrant;
//Synthetic comment -- @@ -508,6 +510,16 @@
mRingbackToneRegistrants.remove(h);
}

public void registerForResendIncallMute(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mResendIncallMuteRegistrants.add(r);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d9c3dc7..2db3ddd 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -126,6 +127,13 @@
void unregisterForVoiceRadioTechChanged(Handler h);

/**
* Fires on any transition into RadioState.isOn()
* Fires immediately if currently in that state
* In general, actions should be idempotent. State may change








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index f1855d2..80287c0 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -431,6 +432,9 @@
mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, DctConstants.EVENT_ICC_CHANGED, null);

IntentFilter filter = new IntentFilter();
filter.addAction(getActionIntentReconnectAlarm());
filter.addAction(Intent.ACTION_SCREEN_ON);
//Synthetic comment -- @@ -476,6 +480,7 @@
mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
mDataRoamingSettingObserver.unregister(mPhone.getContext());
mUiccController.unregisterForIccChanged(this);
}

protected void broadcastMessenger() {
//Synthetic comment -- @@ -596,6 +601,7 @@
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);
protected abstract void onUpdateIcc();

@Override
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -705,7 +711,9 @@
case DctConstants.EVENT_ICC_CHANGED:
onUpdateIcc();
break;

default:
Rlog.e("DATA", "Unidentified event msg=" + msg);
break;
//Synthetic comment -- @@ -1137,6 +1145,44 @@
}
}

protected void resetAllRetryCounts() {
for (ApnContext ac : mApnContexts.values()) {
ac.setRetryCount(0);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index fbce476..d149efc 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -103,6 +104,7 @@
static final String REASON_NW_TYPE_CHANGED = "nwTypeChanged";
static final String REASON_DATA_DEPENDENCY_MET = "dependencyMet";
static final String REASON_DATA_DEPENDENCY_UNMET = "dependencyUnmet";

// Used for band mode selection methods
static final int BM_UNSPECIFIED = 0; // selected by baseband automatically








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c3256df..1f47d4a 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -2516,6 +2517,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2873,6 +2875,18 @@
notifyRegistrantsRilConnectionChanged(((int[])ret)[0]);
break;
}
}
}

//Synthetic comment -- @@ -3657,7 +3671,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            default: return "<unknown reponse>";
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 0e68125..a58e46f 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -822,10 +823,18 @@
// Check for an active or dormant connection element in
// the DATA_CALL_LIST array
for (int index = 0; index < dataCallStates.size(); index++) {
                connectionState = dataCallStates.get(index).active;
if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
isActiveOrDormantConnectionPresent = true;
break;
}
}

//Synthetic comment -- @@ -949,6 +958,11 @@
return (mState == DctConstants.State.CONNECTED);
}

@Override
protected void log(String s) {
Rlog.d(LOG_TAG, "[CdmaDCT] " + s);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index e8b662c..2713ae5 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -1270,6 +1271,13 @@
log("onDataStateChanged(ar): inactive, cleanup apns=" + connectedApns);
}
apnsToCleanup.addAll(connectedApns);
} else {
// Its active so update the DataConnections link properties
UpdateLinkPropertyResult result =
//Synthetic comment -- @@ -1453,12 +1461,16 @@
// We've re-registerd once now just retry forever.
apnContext.getDataConnection().retryForeverUsingLastTimeout();
} else {
                    // Try to Re-register to the network.
                    if (DBG) log("reconnectAfterFail: activate failed, Reregistering to network");
                    mReregisterOnReconnectFailure = true;
                    mPhone.getServiceStateTracker().reRegisterNetwork(null);
                    apnContext.setRetryCount(0);
                    return;
}
}

//Synthetic comment -- @@ -2412,6 +2424,14 @@
}

@Override
protected void log(String s) {
Rlog.d(LOG_TAG, "[GsmDCT] "+ s);
}







