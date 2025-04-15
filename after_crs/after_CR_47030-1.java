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
//Synthetic comment -- index 4817a7b..65741da4 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -201,7 +202,7 @@
}

public boolean isReady() {
        return mDataEnabled.get() && mDependencyMet.get() && !getTetheredCallOn();
}

public void setEnabled(boolean enabled) {
//Synthetic comment -- @@ -243,4 +244,12 @@
public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
pw.println("ApnContext: " + this.toString());
}

    public void setTetheredCallOn(boolean tetheredCallOn) {
        if (mApnSetting != null) mApnSetting.mTetheredCallOn = tetheredCallOn;
    }

    public boolean getTetheredCallOn() {
        return mApnSetting == null ? false : mApnSetting.mTetheredCallOn;
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnSetting.java b/src/java/com/android/internal/telephony/ApnSetting.java
//Synthetic comment -- index b84c69c..cc1ae45 100755

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -51,6 +52,8 @@
*/
public final int bearer;

    public boolean mTetheredCallOn = false;

public ApnSetting(int id, String numeric, String carrier, String apn,
String proxy, String port,
String mmsc, String mmsProxy, String mmsPort,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/BaseCommands.java b/src/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 1b54656..9db978f 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -71,6 +72,7 @@
protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
protected RegistrantList mIccRefreshRegistrants = new RegistrantList();
    protected RegistrantList mTetheredModeStateRegistrants = new RegistrantList();

protected Registrant mGsmSmsRegistrant;
protected Registrant mCdmaSmsRegistrant;
//Synthetic comment -- @@ -508,6 +510,16 @@
mRingbackToneRegistrants.remove(h);
}

    public void registerForTetheredModeStateChanged(Handler h, int what,
            Object obj) {
        Registrant r = new Registrant(h, what, obj);
        mTetheredModeStateRegistrants.add(r);
    }

    public void unregisterForTetheredModeStateChanged(Handler h) {
        mTetheredModeStateRegistrants.remove(h);
    }

public void registerForResendIncallMute(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mResendIncallMuteRegistrants.add(r);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..d37015e 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -125,6 +126,13 @@
void unregisterForVoiceRadioTechChanged(Handler h);

/**
     * Indications for tethered mode calls. ON/OFF indications should trigger
     * immediate data call retries.
     */
    void registerForTetheredModeStateChanged(Handler h, int what, Object obj);
    void unregisterForTetheredModeStateChanged(Handler h);

    /**
* Fires on any transition into RadioState.isOn()
* Fires immediately if currently in that state
* In general, actions should be idempotent. State may change








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index a2980be..3b2cc7e 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -430,6 +431,9 @@
mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, DctConstants.EVENT_ICC_CHANGED, null);

        mPhone.mCM.registerForTetheredModeStateChanged(this,
                DctConstants.EVENT_TETHERED_MODE_STATE_CHANGED, null);

IntentFilter filter = new IntentFilter();
filter.addAction(getActionIntentReconnectAlarm());
filter.addAction(Intent.ACTION_SCREEN_ON);
//Synthetic comment -- @@ -475,6 +479,7 @@
mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
mDataRoamingSettingObserver.unregister(mPhone.getContext());
mUiccController.unregisterForIccChanged(this);
        mPhone.mCM.unregisterForTetheredModeStateChanged(this);
}

protected void broadcastMessenger() {
//Synthetic comment -- @@ -595,6 +600,7 @@
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);
protected abstract void onUpdateIcc();
    protected abstract void clearTetheredStateOnStatus();

@Override
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -704,7 +710,9 @@
case DctConstants.EVENT_ICC_CHANGED:
onUpdateIcc();
break;
            case DctConstants.EVENT_TETHERED_MODE_STATE_CHANGED:
                onTetheredModeStateChanged((AsyncResult) msg.obj);
                break;
default:
Log.e("DATA", "Unidentified event msg=" + msg);
break;
//Synthetic comment -- @@ -1136,6 +1144,44 @@
}
}

    private void onTetheredModeStateChanged(AsyncResult ar) {
        int[] ret = (int[]) ar.result;

        if (ret == null || ret.length != 1) {
            if (DBG)
                log("Error: Invalid Tethered mode received");
            return;
        }

        int mode = ret[0];
        if (DBG)
            log("onTetheredModeStateChanged: mode:" + mode);

        switch (mode) {
        case RILConstants.RIL_TETHERED_MODE_ON:
            // Indicates that an internal data call was created in the modem.
            if (DBG)
                log("Unsol Indication: RIL_TETHERED_MODE_ON");
            break;
        case RILConstants.RIL_TETHERED_MODE_OFF:
            if (DBG)
                log("Unsol Indication: RIL_TETHERED_MODE_OFF");
            /*
             * This indicates that an internal modem data call (e.g. tethered)
             * had ended. Reset the retry count for all Data Connections and
             * attempt to bring up all data calls
             */
            resetAllRetryCounts();
            clearTetheredStateOnStatus();
            sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA, 0, 0,
                    Phone.REASON_TETHERED_MODE_STATE_CHANGED));
            break;
        default:
            if (DBG)
            log("Error: Invalid Tethered mode:" + mode);
        }
    }

protected void resetAllRetryCounts() {
for (ApnContext ac : mApnContexts.values()) {
ac.setRetryCount(0);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 71912b6..5ebf165 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2007 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -103,6 +104,7 @@
static final String REASON_NW_TYPE_CHANGED = "nwTypeChanged";
static final String REASON_DATA_DEPENDENCY_MET = "dependencyMet";
static final String REASON_DATA_DEPENDENCY_UNMET = "dependencyUnmet";
    static final String REASON_TETHERED_MODE_STATE_CHANGED = "tetheredModeStateChanged";

// Used for band mode selection methods
static final int BM_UNSPECIFIED = 0; // selected by baseband automatically








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index faae72a..a27ab78 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -2509,6 +2510,7 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_RIL_CONNECTED: ret = responseInts(p); break;
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseInts(p); break;
            case RIL_UNSOL_TETHERED_MODE_STATE_CHANGED: ret = responseInts(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2866,6 +2868,18 @@
notifyRegistrantsRilConnectionChanged(((int[])ret)[0]);
break;
}

            case RIL_UNSOL_TETHERED_MODE_STATE_CHANGED:
                if (RILJ_LOGD) unsljLogvRet(response, ret);
                if (mTetheredModeStateRegistrants != null) {
                    if (ret != null) {
                        mTetheredModeStateRegistrants.notifyRegistrants(
                                new AsyncResult (null, ret, null));
                    } else {
                        Log.e(LOG_TAG, "null returned, expected non-null");
                    }
                }
                break;
}
}

//Synthetic comment -- @@ -3650,7 +3664,8 @@
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_RIL_CONNECTED: return "UNSOL_RIL_CONNECTED";
case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            case RIL_UNSOL_TETHERED_MODE_STATE_CHANGED: return "RIL_UNSOL_TETHERED_MODE_STATE_CHANGED";
            default: return "<unknown response>";
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 2acc5f9..a3f8701 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -822,10 +823,18 @@
// Check for an active or dormant connection element in
// the DATA_CALL_LIST array
for (int index = 0; index < dataCallStates.size(); index++) {
                DataCallState dcState = dataCallStates.get(index);
                connectionState = dcState.active;
if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
isActiveOrDormantConnectionPresent = true;
break;
                } else {
                    /* Check if this was brought down due to a tethered call */
                    if (FailCause.fromInt(dcState.status) == FailCause.TETHERED_CALL_ACTIVE) {
                        // Mark apn as busy in a tethered call
                        if (DBG) log("setTetheredCallOn for apn:" + mActiveApn.toString());
                        mActiveApn.mTetheredCallOn = true;
                    }
}
}

//Synthetic comment -- @@ -949,6 +958,11 @@
return (mState == DctConstants.State.CONNECTED);
}

    protected void clearTetheredStateOnStatus() {
        if (DBG) log("clearTetheredStateOnStatus()");
        mActiveApn.mTetheredCallOn = false;
    }

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[CdmaDCT] " + s);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4e68450..9c31bd2 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -1270,6 +1271,13 @@
log("onDataStateChanged(ar): inactive, cleanup apns=" + connectedApns);
}
apnsToCleanup.addAll(connectedApns);
                    if (FailCause.fromInt(newState.status) == FailCause.TETHERED_CALL_ACTIVE) {
                        // Mark apnContexts as busy in a tethered call
                        for (ApnContext apnc : connectedApns) {
                            if (DBG) log("setTetheredCallOn for apncontext:" + apnc.toString());
                            apnc.setTetheredCallOn(true);
                        }
                    }
} else {
// Its active so update the DataConnections link properties
UpdateLinkPropertyResult result =
//Synthetic comment -- @@ -1453,12 +1461,16 @@
// We've re-registerd once now just retry forever.
apnContext.getDataConnection().retryForeverUsingLastTimeout();
} else {
                    if (!apnContext.getTetheredCallOn()) {
                        // Try to Re-register to the network.
                        if (DBG) log("reconnectAfterFail: activate failed, Reregistering to network");
                        mReregisterOnReconnectFailure = true;
                        mPhone.getServiceStateTracker().reRegisterNetwork(null);
                        apnContext.getDataConnection().resetRetryCount();
                        return;
                    } else {
                        if (DBG) log("Tethered mode ON, skip re-registering");
                    }
}
}

//Synthetic comment -- @@ -2412,6 +2424,14 @@
}

@Override
    protected void clearTetheredStateOnStatus() {
        if (DBG) log("clearTetheredStateOnStatus()");
        for (ApnContext apnc : mApnContexts.values()) {
            apnc.setTetheredCallOn(false);
        }
    }

    @Override
protected void log(String s) {
Log.d(LOG_TAG, "[GsmDCT] "+ s);
}







