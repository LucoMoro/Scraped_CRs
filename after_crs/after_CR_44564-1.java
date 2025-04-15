/*WIP DO NOT MERGE. TESTING.
Telephony: Make IccCardProxy follow 2 apps

If any app is pin or puk locked broadcast locked intent
so that user can verify its pin

This also works around an issue where some rils
show second app as unknown if apps are puk-locked

Change-Id:Ic638c74050bbcf01bf4e907fea23c8ba31120703*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..7f541f3 100644

//Synthetic comment -- @@ -84,9 +84,13 @@

private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
private UiccController mUiccController = null;
    // Index 0 is unused. Index 1 is 3GPP. Index 2 is 3GPP2
    // This is done in such a way so that we can use mCurrentAppType as index an into these arrays
    private UiccCard[] mUiccCard = new UiccCard[UiccController.APP_FAM_3GPP2 + 1];
    private UiccCardApplication[] mUiccApplication =
            new UiccCardApplication[UiccController.APP_FAM_3GPP2 + 1];
    private IccRecords[] mIccRecords = new IccRecords[UiccController.APP_FAM_3GPP2 + 1];

private CdmaSubscriptionSourceManager mCdmaSSM = null;
private boolean mRadioOn = false;
private boolean mCdmaSubscriptionFromNv = false;
//Synthetic comment -- @@ -185,6 +189,16 @@
}

public void handleMessage(Message msg) {
        int appType = -1;
        if (msg.obj instanceof AsyncResult) {
            AsyncResult ar = (AsyncResult)msg.obj;
            if (ar != null && ar.userObj instanceof Integer) {
                appType = (Integer)ar.userObj;
                log("appType is " + appType);
            }
        }
        log ("handleMessage msg.what=" + msg.what + " appType=" + appType + " mCurrentAppType=" +
                mCurrentAppType);
switch (msg.what) {
case EVENT_RADIO_OFF_OR_UNAVAILABLE:
mRadioOn = false;
//Synthetic comment -- @@ -201,24 +215,33 @@
}
break;
case EVENT_ICC_ABSENT:
                if (appType == mCurrentAppType) {
                    mAbsentRegistrants.notifyRegistrants();
                }
                updateExternalState();
break;
case EVENT_ICC_LOCKED:
                mPinLockedRegistrants.notifyRegistrants();
                updateExternalState();
break;
case EVENT_APP_READY:
                updateExternalState();
break;
case EVENT_RECORDS_LOADED:
                if (appType == mCurrentAppType) {
                    broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOADED, null);
                }
break;
case EVENT_IMSI_READY:
                if (appType == mCurrentAppType) {
                    broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
                }
break;
case EVENT_NETWORK_LOCKED:
                if (appType == mCurrentAppType) {
                    mNetworkLockedRegistrants.notifyRegistrants();
                    setExternalState(State.NETWORK_LOCKED);
                }
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
updateQuietMode();
//Synthetic comment -- @@ -231,33 +254,37 @@

private void updateIccAvailability() {
synchronized (mLock) {
            for (int i = UiccController.APP_FAM_3GPP; i <= UiccController.APP_FAM_3GPP2; i++) {
                UiccCard newCard = mUiccController.getUiccCard();
                CardState state = CardState.CARDSTATE_ABSENT;
                UiccCardApplication newApp = null;
                IccRecords newRecords = null;
                if (newCard != null) {
                    state = newCard.getCardState();
                    newApp = newCard.getApplication(i);
                    if (newApp != null) {
                        newRecords = newApp.getIccRecords();
                    }
                }

                if (mIccRecords[i] != newRecords ||
                        mUiccApplication[i] != newApp ||
                        mUiccCard[i] != newCard) {
                    if (DBG) log("Icc changed. Reregestering.");
                    unregisterUiccCardEvents(i);
                    mUiccCard[i] = newCard;
                    mUiccApplication[i] = newApp;
                    mIccRecords[i] = newRecords;
                    registerUiccCardEvents(i);
}
}
            // Once we have all uicc apps queried we can update external state
updateExternalState();
}
}

private void updateExternalState() {
        if (mUiccCard[mCurrentAppType] == null || mUiccCard[mCurrentAppType].getCardState() == CardState.CARDSTATE_ABSENT) {
if (mRadioOn) {
setExternalState(State.ABSENT);
} else {
//Synthetic comment -- @@ -266,25 +293,35 @@
return;
}

        if (mUiccCard[mCurrentAppType].getCardState() == CardState.CARDSTATE_ERROR ||
                mUiccApplication[mCurrentAppType] == null) {
setExternalState(State.UNKNOWN);
return;
}

        // If any of the app is pin/puk/permanently blocked - display that info
        if (mUiccApplication[UiccController.APP_FAM_3GPP].getState() == AppState.APPSTATE_PIN ||
                mUiccApplication[UiccController.APP_FAM_3GPP2].getState() == AppState.APPSTATE_PIN) {
            setExternalState(State.PIN_REQUIRED);
            return;
        } else if (mUiccApplication[UiccController.APP_FAM_3GPP].getState() == AppState.APPSTATE_PUK ||
                mUiccApplication[UiccController.APP_FAM_3GPP2].getState() == AppState.APPSTATE_PUK) {
            if (mUiccApplication[UiccController.APP_FAM_3GPP].getPin1State() == PinState.PINSTATE_ENABLED_PERM_BLOCKED ||
                    mUiccApplication[UiccController.APP_FAM_3GPP2].getPin1State() == PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
                setExternalState(State.PERM_DISABLED);
                return;
            } else {
                setExternalState(State.PUK_REQUIRED);
                return;
            }
        }
        switch (mUiccApplication[mCurrentAppType].getState()) {
case APPSTATE_UNKNOWN:
case APPSTATE_DETECTED:
setExternalState(State.UNKNOWN);
break;
case APPSTATE_SUBSCRIPTION_PERSO:
                if (mUiccApplication[mCurrentAppType].getPersoSubState() == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
setExternalState(State.NETWORK_LOCKED);
} else {
setExternalState(State.UNKNOWN);
//Synthetic comment -- @@ -296,26 +333,28 @@
}
}

    private void registerUiccCardEvents(int appFamily) {
        int i = appFamily; //index
        if (mUiccCard[i] != null) mUiccCard[i].registerForAbsent(this, EVENT_ICC_ABSENT, i);
        if (mUiccApplication[i] != null) {
            mUiccApplication[i].registerForReady(this, EVENT_APP_READY, i);
            mUiccApplication[i].registerForLocked(this, EVENT_ICC_LOCKED, i);
            mUiccApplication[i].registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, i);
}
        if (mIccRecords[i] != null) {
            mIccRecords[i].registerForImsiReady(this, EVENT_IMSI_READY, i);
            mIccRecords[i].registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, i);
}
}

    private void unregisterUiccCardEvents(int appFamily) {
        int i = appFamily; //index
        if (mUiccCard[i] != null) mUiccCard[i].unregisterForAbsent(this);
        if (mUiccApplication[i] != null) mUiccApplication[i].unregisterForReady(this);
        if (mUiccApplication[i] != null) mUiccApplication[i].unregisterForLocked(this);
        if (mUiccApplication[i] != null) mUiccApplication[i].unregisterForNetworkLocked(this);
        if (mIccRecords[i] != null) mIccRecords[i].unregisterForImsiReady(this);
        if (mIccRecords[i] != null) mIccRecords[i].unregisterForRecordsLoaded(this);
}

private void broadcastIccStateChangedIntent(String value, String reason) {
//Synthetic comment -- @@ -338,19 +377,19 @@
}
}

    private void processLockedState(int appType) {
synchronized (mLock) {
            if (mUiccApplication[appType] == null) {
//Don't need to do anything if non-existent application is locked
return;
}
            PinState pin1State = mUiccApplication[appType].getPin1State();
if (pin1State == PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
setExternalState(State.PERM_DISABLED);
return;
}

            AppState appState = mUiccApplication[appType].getState();
switch (appState) {
case APPSTATE_PIN:
mPinLockedRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -363,6 +402,12 @@
}
}

    private void processAppReady(int appType) {
        if (appType == mCurrentAppType) {
            setExternalState(State.READY);
        }
    }

private void setExternalState(State newState, boolean override) {
synchronized (mLock) {
if (!override && newState == mExternalState) {
//Synthetic comment -- @@ -381,8 +426,8 @@

public boolean getIccRecordsLoaded() {
synchronized (mLock) {
            if (mIccRecords[mCurrentAppType] != null) {
                return mIccRecords[mCurrentAppType].getRecordsLoaded();
}
return false;
}
//Synthetic comment -- @@ -426,7 +471,7 @@
@Override
public IccRecords getIccRecords() {
synchronized (mLock) {
            return mIccRecords[mCurrentAppType];
}
}

//Synthetic comment -- @@ -434,7 +479,7 @@
public IccFileHandler getIccFileHandler() {
synchronized (mLock) {
if (mUiccApplication != null) {
                return mUiccApplication[mCurrentAppType].getIccFileHandler();
}
return null;
}
//Synthetic comment -- @@ -512,8 +557,16 @@
@Override
public void supplyPin(String pin, Message onComplete) {
synchronized (mLock) {
            // If current app is not locked (or doesn't exist) while another
            // app is locked - try to unlock another app
            UiccCardApplication app = mUiccApplication[mCurrentAppType];
            if ((app == null || !app.getState().isPinRequired()) &&
                    mUiccApplication[3 - mCurrentAppType] != null &&
                    mUiccApplication[3 - mCurrentAppType].getState().isPinRequired()) {
                app = mUiccApplication[3 - mCurrentAppType];
            }
            if (app != null) {
                app.supplyPin(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -526,8 +579,16 @@
@Override
public void supplyPuk(String puk, String newPin, Message onComplete) {
synchronized (mLock) {
            // If current app is not locked (or doesn't exist) while another
            // app is locked - try to unlock another app
            UiccCardApplication app = mUiccApplication[mCurrentAppType];
            if ((app == null || !app.getState().isPukRequired()) &&
                    mUiccApplication[3 - mCurrentAppType] != null &&
                    mUiccApplication[3 - mCurrentAppType].getState().isPukRequired()) {
                app = mUiccApplication[3 - mCurrentAppType];
            }
            if (app != null) {
                app.supplyPuk(puk, newPin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -540,8 +601,8 @@
@Override
public void supplyPin2(String pin2, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].supplyPin2(pin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -554,8 +615,8 @@
@Override
public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].supplyPuk2(puk2, newPin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -568,8 +629,8 @@
@Override
public void supplyNetworkDepersonalization(String pin, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].supplyNetworkDepersonalization(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("CommandsInterface is not set.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -583,7 +644,8 @@
public boolean getIccLockEnabled() {
synchronized (mLock) {
/* defaults to true, if ICC is absent */
            Boolean retValue = mUiccApplication[mCurrentAppType] != null ?
                    mUiccApplication[mCurrentAppType].getIccLockEnabled() : true;
return retValue;
}
}
//Synthetic comment -- @@ -591,7 +653,8 @@
@Override
public boolean getIccFdnEnabled() {
synchronized (mLock) {
            Boolean retValue = mUiccApplication[mCurrentAppType] != null ?
                    mUiccApplication[mCurrentAppType].getIccFdnEnabled() : false;
return retValue;
}
}
//Synthetic comment -- @@ -599,8 +662,8 @@
@Override
public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].setIccLockEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -613,8 +676,8 @@
@Override
public void setIccFdnEnabled(boolean enabled, String password, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].setIccFdnEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -627,8 +690,9 @@
@Override
public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].changeIccLockPassword(oldPassword,
                        newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -641,8 +705,9 @@
@Override
public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication[mCurrentAppType] != null) {
                mUiccApplication[mCurrentAppType].changeIccFdnPassword(oldPassword,
                        newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -655,8 +720,8 @@
@Override
public String getServiceProviderName() {
synchronized (mLock) {
            if (mIccRecords[mCurrentAppType] != null) {
                return mIccRecords[mCurrentAppType].getServiceProviderName();
}
return null;
}
//Synthetic comment -- @@ -665,7 +730,8 @@
@Override
public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType type) {
synchronized (mLock) {
            Boolean retValue = mUiccCard[mCurrentAppType] != null ?
                    mUiccCard[mCurrentAppType].isApplicationOnIcc(type) : false;
return retValue;
}
}
//Synthetic comment -- @@ -673,7 +739,7 @@
@Override
public boolean hasIccCard() {
synchronized (mLock) {
            if (mUiccCard[mCurrentAppType] != null && mUiccCard[mCurrentAppType].getCardState() != CardState.CARDSTATE_ABSENT) {
return true;
}
return false;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..eff4217 100644

//Synthetic comment -- @@ -214,7 +214,7 @@
int[] ints = (int[])ar.result;
if(ints.length != 0) {
mIccFdnEnabled = (0!=ints[0]);
                if (DBG) log("Query facility lock fd : "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
//Synthetic comment -- @@ -256,7 +256,7 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                if (DBG) log("Query facility lock sc: "  + ints[0]);

mIccLockEnabled = (ints[0] != 0);








