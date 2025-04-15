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
    private UiccCard mUiccCard = null;
    private UiccCardApplication mUiccApplication = null;
    private IccRecords mIccRecords = null;
private CdmaSubscriptionSourceManager mCdmaSSM = null;
private boolean mRadioOn = false;
private boolean mCdmaSubscriptionFromNv = false;
//Synthetic comment -- @@ -185,6 +189,16 @@
}

public void handleMessage(Message msg) {
switch (msg.what) {
case EVENT_RADIO_OFF_OR_UNAVAILABLE:
mRadioOn = false;
//Synthetic comment -- @@ -201,24 +215,33 @@
}
break;
case EVENT_ICC_ABSENT:
                mAbsentRegistrants.notifyRegistrants();
                setExternalState(State.ABSENT);
break;
case EVENT_ICC_LOCKED:
                processLockedState();
break;
case EVENT_APP_READY:
                setExternalState(State.READY);
break;
case EVENT_RECORDS_LOADED:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOADED, null);
break;
case EVENT_IMSI_READY:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
break;
case EVENT_NETWORK_LOCKED:
                mNetworkLockedRegistrants.notifyRegistrants();
                setExternalState(State.NETWORK_LOCKED);
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
updateQuietMode();
//Synthetic comment -- @@ -231,33 +254,37 @@

private void updateIccAvailability() {
synchronized (mLock) {
            UiccCard newCard = mUiccController.getUiccCard();
            CardState state = CardState.CARDSTATE_ABSENT;
            UiccCardApplication newApp = null;
            IccRecords newRecords = null;
            if (newCard != null) {
                state = newCard.getCardState();
                newApp = newCard.getApplication(mCurrentAppType);
                if (newApp != null) {
                    newRecords = newApp.getIccRecords();
}
}

            if (mIccRecords != newRecords || mUiccApplication != newApp || mUiccCard != newCard) {
                if (DBG) log("Icc changed. Reregestering.");
                unregisterUiccCardEvents();
                mUiccCard = newCard;
                mUiccApplication = newApp;
                mIccRecords = newRecords;
                registerUiccCardEvents();
            }

updateExternalState();
}
}

private void updateExternalState() {
        if (mUiccCard == null || mUiccCard.getCardState() == CardState.CARDSTATE_ABSENT) {
if (mRadioOn) {
setExternalState(State.ABSENT);
} else {
//Synthetic comment -- @@ -266,25 +293,35 @@
return;
}

        if (mUiccCard.getCardState() == CardState.CARDSTATE_ERROR ||
                mUiccApplication == null) {
setExternalState(State.UNKNOWN);
return;
}

        switch (mUiccApplication.getState()) {
case APPSTATE_UNKNOWN:
case APPSTATE_DETECTED:
setExternalState(State.UNKNOWN);
break;
            case APPSTATE_PIN:
                setExternalState(State.PIN_REQUIRED);
                break;
            case APPSTATE_PUK:
                setExternalState(State.PUK_REQUIRED);
                break;
case APPSTATE_SUBSCRIPTION_PERSO:
                if (mUiccApplication.getPersoSubState() == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
setExternalState(State.NETWORK_LOCKED);
} else {
setExternalState(State.UNKNOWN);
//Synthetic comment -- @@ -296,26 +333,28 @@
}
}

    private void registerUiccCardEvents() {
        if (mUiccCard != null) mUiccCard.registerForAbsent(this, EVENT_ICC_ABSENT, null);
        if (mUiccApplication != null) {
            mUiccApplication.registerForReady(this, EVENT_APP_READY, null);
            mUiccApplication.registerForLocked(this, EVENT_ICC_LOCKED, null);
            mUiccApplication.registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, null);
}
        if (mIccRecords != null) {
            mIccRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
            mIccRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
}
}

    private void unregisterUiccCardEvents() {
        if (mUiccCard != null) mUiccCard.unregisterForAbsent(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForReady(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForLocked(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForNetworkLocked(this);
        if (mIccRecords != null) mIccRecords.unregisterForImsiReady(this);
        if (mIccRecords != null) mIccRecords.unregisterForRecordsLoaded(this);
}

private void broadcastIccStateChangedIntent(String value, String reason) {
//Synthetic comment -- @@ -338,19 +377,19 @@
}
}

    private void processLockedState() {
synchronized (mLock) {
            if (mUiccApplication == null) {
//Don't need to do anything if non-existent application is locked
return;
}
            PinState pin1State = mUiccApplication.getPin1State();
if (pin1State == PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
setExternalState(State.PERM_DISABLED);
return;
}

            AppState appState = mUiccApplication.getState();
switch (appState) {
case APPSTATE_PIN:
mPinLockedRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -363,6 +402,12 @@
}
}

private void setExternalState(State newState, boolean override) {
synchronized (mLock) {
if (!override && newState == mExternalState) {
//Synthetic comment -- @@ -381,8 +426,8 @@

public boolean getIccRecordsLoaded() {
synchronized (mLock) {
            if (mIccRecords != null) {
                return mIccRecords.getRecordsLoaded();
}
return false;
}
//Synthetic comment -- @@ -426,7 +471,7 @@
@Override
public IccRecords getIccRecords() {
synchronized (mLock) {
            return mIccRecords;
}
}

//Synthetic comment -- @@ -434,7 +479,7 @@
public IccFileHandler getIccFileHandler() {
synchronized (mLock) {
if (mUiccApplication != null) {
                return mUiccApplication.getIccFileHandler();
}
return null;
}
//Synthetic comment -- @@ -512,8 +557,16 @@
@Override
public void supplyPin(String pin, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.supplyPin(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -526,8 +579,16 @@
@Override
public void supplyPuk(String puk, String newPin, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.supplyPuk(puk, newPin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -540,8 +601,8 @@
@Override
public void supplyPin2(String pin2, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.supplyPin2(pin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -554,8 +615,8 @@
@Override
public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.supplyPuk2(puk2, newPin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -568,8 +629,8 @@
@Override
public void supplyNetworkDepersonalization(String pin, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.supplyNetworkDepersonalization(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("CommandsInterface is not set.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -583,7 +644,8 @@
public boolean getIccLockEnabled() {
synchronized (mLock) {
/* defaults to true, if ICC is absent */
            Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccLockEnabled() : true;
return retValue;
}
}
//Synthetic comment -- @@ -591,7 +653,8 @@
@Override
public boolean getIccFdnEnabled() {
synchronized (mLock) {
            Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccFdnEnabled() : false;
return retValue;
}
}
//Synthetic comment -- @@ -599,8 +662,8 @@
@Override
public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.setIccLockEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -613,8 +676,8 @@
@Override
public void setIccFdnEnabled(boolean enabled, String password, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.setIccFdnEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -627,8 +690,9 @@
@Override
public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.changeIccLockPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -641,8 +705,9 @@
@Override
public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
synchronized (mLock) {
            if (mUiccApplication != null) {
                mUiccApplication.changeIccFdnPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -655,8 +720,8 @@
@Override
public String getServiceProviderName() {
synchronized (mLock) {
            if (mIccRecords != null) {
                return mIccRecords.getServiceProviderName();
}
return null;
}
//Synthetic comment -- @@ -665,7 +730,8 @@
@Override
public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType type) {
synchronized (mLock) {
            Boolean retValue = mUiccCard != null ? mUiccCard.isApplicationOnIcc(type) : false;
return retValue;
}
}
//Synthetic comment -- @@ -673,7 +739,7 @@
@Override
public boolean hasIccCard() {
synchronized (mLock) {
            if (mUiccCard != null && mUiccCard.getCardState() != CardState.CARDSTATE_ABSENT) {
return true;
}
return false;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..eff4217 100644

//Synthetic comment -- @@ -214,7 +214,7 @@
int[] ints = (int[])ar.result;
if(ints.length != 0) {
mIccFdnEnabled = (0!=ints[0]);
                if (DBG) log("Query facility lock : "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
//Synthetic comment -- @@ -256,7 +256,7 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                if (DBG) log("Query facility lock : "  + ints[0]);

mIccLockEnabled = (ints[0] != 0);








