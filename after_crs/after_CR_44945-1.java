/*Telephony: Add ME Depersonalization support.

Change-Id:Iae52f87a875a22b4d0465b9d93f1e0b8ca6bbfa8*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..a2461db 100644

//Synthetic comment -- @@ -687,7 +687,7 @@

void changeBarringPassword(String facility, String oldPwd, String newPwd, Message result);

    void supplyDepersonalization(String netpin, int type, Message result);

/**
*  returned message








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index f1ac581..d4e306f 100644

//Synthetic comment -- @@ -58,10 +58,10 @@
public void unregisterForAbsent(Handler h);

/**
     * Notifies handler of any transition into State.PERSO_LOCKED
*/
    public void registerForPersoLocked(Handler h, int what, Object obj);
    public void unregisterForPersoLocked(Handler h);

/**
* Notifies handler of any transition into IccCardConstants.State.isPinLocked()
//Synthetic comment -- @@ -107,7 +107,7 @@
/**
* Supply Network depersonalization code to the RIL
*/
    public void supplyDepersonalization (String pin, int type, Message onComplete);

/**
* Check whether ICC pin lock is enabled








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..c577fef 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
private static final int EVENT_APP_READY = 6;
private static final int EVENT_RECORDS_LOADED = 7;
private static final int EVENT_IMSI_READY = 8;
    private static final int EVENT_PERSO_LOCKED = 9;
private static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 11;

private final Object mLock = new Object();
//Synthetic comment -- @@ -80,7 +80,7 @@

private RegistrantList mAbsentRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mPersoLockedRegistrants = new RegistrantList();

private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
private UiccController mUiccController = null;
//Synthetic comment -- @@ -96,6 +96,7 @@
// ACTION_SIM_STATE_CHANGED intents
private boolean mInitialized = false;
private State mExternalState = State.UNKNOWN;
    private PersoSubState mPersoSubState = PersoSubState.PERSOSUBSTATE_UNKNOWN;

public IccCardProxy(Context context, CommandsInterface ci) {
log("Creating");
//Synthetic comment -- @@ -216,9 +217,10 @@
case EVENT_IMSI_READY:
broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
break;
            case EVENT_PERSO_LOCKED:
                mPersoSubState = mUiccApplication.getPersoSubState();
                mPersoLockedRegistrants.notifyRegistrants((AsyncResult)msg.obj);
                setExternalState(State.PERSO_LOCKED);
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
updateQuietMode();
//Synthetic comment -- @@ -284,8 +286,9 @@
setExternalState(State.PUK_REQUIRED);
break;
case APPSTATE_SUBSCRIPTION_PERSO:
                if (mUiccApplication.isPersoLocked()) {
                    mPersoSubState = mUiccApplication.getPersoSubState();
                    setExternalState(State.PERSO_LOCKED);
} else {
setExternalState(State.UNKNOWN);
}
//Synthetic comment -- @@ -301,7 +304,7 @@
if (mUiccApplication != null) {
mUiccApplication.registerForReady(this, EVENT_APP_READY, null);
mUiccApplication.registerForLocked(this, EVENT_ICC_LOCKED, null);
            mUiccApplication.registerForPersoLocked(this, EVENT_PERSO_LOCKED, null);
}
if (mIccRecords != null) {
mIccRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
//Synthetic comment -- @@ -313,7 +316,7 @@
if (mUiccCard != null) mUiccCard.unregisterForAbsent(this);
if (mUiccApplication != null) mUiccApplication.unregisterForReady(this);
if (mUiccApplication != null) mUiccApplication.unregisterForLocked(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForPersoLocked(this);
if (mIccRecords != null) mIccRecords.unregisterForImsiReady(this);
if (mIccRecords != null) mIccRecords.unregisterForRecordsLoaded(this);
}
//Synthetic comment -- @@ -393,7 +396,7 @@
case ABSENT: return IccCardConstants.INTENT_VALUE_ICC_ABSENT;
case PIN_REQUIRED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
case PUK_REQUIRED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
            case PERSO_LOCKED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
case READY: return IccCardConstants.INTENT_VALUE_ICC_READY;
case NOT_READY: return IccCardConstants.INTENT_VALUE_ICC_NOT_READY;
case PERM_DISABLED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
//Synthetic comment -- @@ -409,7 +412,7 @@
switch (state) {
case PIN_REQUIRED: return IccCardConstants.INTENT_VALUE_LOCKED_ON_PIN;
case PUK_REQUIRED: return IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK;
            case PERSO_LOCKED: return IccCardConstants.INTENT_VALUE_LOCKED_PERSO;
case PERM_DISABLED: return IccCardConstants.INTENT_VALUE_ABSENT_ON_PERM_DISABLED;
default: return null;
}
//Synthetic comment -- @@ -464,26 +467,19 @@
}

/**
     * Notifies handler of any transition into State.PERSO_LOCKED
*/
    public void registerForPersoLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mPersoLockedRegistrants.add(r);

        if (getState() == State.PERSO_LOCKED) {
            r.notifyRegistrant(new AsyncResult(null, mPersoSubState.ordinal(), null));
}
}

    public void unregisterForPersoLocked(Handler h) {
        mPersoLockedRegistrants.remove(h);
}

/**
//Synthetic comment -- @@ -565,17 +561,17 @@
}
}

    /**
     * Use invokeDepersonalization from PhoneBase class instead.
     */
    public void supplyDepersonalization(String pin, int type, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyDepersonalization(pin, type, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("CommandsInterface is not set.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 6131792..c3cdf4a 100755

//Synthetic comment -- @@ -825,12 +825,13 @@
}

public void
    supplyDepersonalization(String netpin, int type, Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_DEPERSONALIZATION_CODE, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) +
                        " Type:" + type);

        rr.mp.writeInt(type);
rr.mp.writeString(netpin);

send(rr);
//Synthetic comment -- @@ -2219,7 +2220,7 @@
case RIL_REQUEST_ENTER_SIM_PUK2: ret =  responseInts(p); break;
case RIL_REQUEST_CHANGE_SIM_PIN: ret =  responseInts(p); break;
case RIL_REQUEST_CHANGE_SIM_PIN2: ret =  responseInts(p); break;
            case RIL_REQUEST_ENTER_DEPERSONALIZATION_CODE: ret =  responseInts(p); break;
case RIL_REQUEST_GET_CURRENT_CALLS: ret =  responseCallList(p); break;
case RIL_REQUEST_DIAL: ret =  responseVoid(p); break;
case RIL_REQUEST_GET_IMSI: ret =  responseString(p); break;
//Synthetic comment -- @@ -3469,7 +3470,7 @@
case RIL_REQUEST_ENTER_SIM_PUK2: return "ENTER_SIM_PUK2";
case RIL_REQUEST_CHANGE_SIM_PIN: return "CHANGE_SIM_PIN";
case RIL_REQUEST_CHANGE_SIM_PIN2: return "CHANGE_SIM_PIN2";
            case RIL_REQUEST_ENTER_DEPERSONALIZATION_CODE: return "ENTER_DEPERSONALIZATION_CODE";
case RIL_REQUEST_GET_CURRENT_CALLS: return "GET_CURRENT_CALLS";
case RIL_REQUEST_DIAL: return "DIAL";
case RIL_REQUEST_GET_IMSI: return "GET_IMSI";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..f589275 100644

//Synthetic comment -- @@ -71,7 +71,7 @@

private RegistrantList mReadyRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mPersoLockedRegistrants = new RegistrantList();

UiccCardApplication(UiccCard uiccCard,
IccCardApplicationStatus as,
//Synthetic comment -- @@ -128,10 +128,10 @@
mIccRecords = createIccRecords(as.app_type, c, ci);
}

        if (mPersoSubState != oldPersoSubState &&
                isPersoLocked()) {
            notifyPersoLockedRegistrantsIfNeeded(null);
        }

if (mAppState != oldAppState) {
if (DBG) log(oldAppType + " changed state: " + oldAppState + " -> " + mAppState);
//Synthetic comment -- @@ -373,19 +373,19 @@
}

/**
     * Notifies handler of any transition into State.PERSO_LOCKED
*/
    public void registerForPersoLocked(Handler h, int what, Object obj) {
synchronized (mLock) {
Registrant r = new Registrant (h, what, obj);
            mPersoLockedRegistrants.add(r);
            notifyPersoLockedRegistrantsIfNeeded(r);
}
}

    public void unregisterForPersoLocked(Handler h) {
synchronized (mLock) {
            mPersoLockedRegistrants.remove(h);
}
}

//Synthetic comment -- @@ -449,19 +449,20 @@
*
* @param r Registrant to be notified. If null - all registrants will be notified
*/
     private synchronized void notifyPersoLockedRegistrantsIfNeeded(Registrant r) {
if (mDestroyed) {
return;
}

if (mAppState == AppState.APPSTATE_SUBSCRIPTION_PERSO &&
                isPersoLocked()) {
            AsyncResult ar = new AsyncResult(null, mPersoSubState.ordinal(), null);
if (r == null) {
                log("Notifying registrants: PERSO_LOCKED");
                mPersoLockedRegistrants.notifyRegistrants(ar);
} else {
                log("Notifying 1 registrant: PERSO_LOCKED");
                r.notifyRegistrant(ar);
}
}
}
//Synthetic comment -- @@ -511,6 +512,16 @@
}
}

    public boolean isPersoLocked() {
        switch (mPersoSubState) {
            case PERSOSUBSTATE_UNKNOWN:
            case PERSOSUBSTATE_IN_PROGRESS:
            case PERSOSUBSTATE_READY:
                return false;
            default:
                return true;
        }
    }
/**
* Supply the ICC PIN to the ICC
*
//Synthetic comment -- @@ -555,10 +566,10 @@
}
}

    public void supplyDepersonalization (String pin, int type, Message onComplete) {
synchronized (mLock) {
            log("Network Despersonalization: pin = " + pin + " , type = " + type);
            mCi.supplyDepersonalization(pin, type, onComplete);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipCommandInterface.java b/src/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index 99f4e0f..32df3d8 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
String newPwd, Message result) {
}

    public void supplyDepersonalization(String netpin, int type,  Message result) {
}

public void getCurrentCalls(Message result) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 4f61509..fbe32fb 100644

//Synthetic comment -- @@ -428,7 +428,7 @@
unimplemented(result);
}

    public void supplyDepersonalization(String netpin, int type, Message result)  {
unimplemented(result);
}









//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java
//Synthetic comment -- index ea6836d..272326c 100644

//Synthetic comment -- @@ -214,7 +214,7 @@
}

@Override
    public void supplyDepersonalization(String pin, int type, Message onComplete) {
}

@Override







