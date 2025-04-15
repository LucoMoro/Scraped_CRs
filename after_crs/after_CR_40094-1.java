/*Telephony: Remove CdmaLteUicc objects

Change-Id:Ia12aaa2c64e06632e02a1c15724b4c11cebaf19d*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CsimFileHandler.java b/src/java/com/android/internal/telephony/CsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..7006051

//Synthetic comment -- @@ -0,0 +1,72 @@
/*
 * Copyright (C) 2006, 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;

/**
 * {@hide}
 * This class should be used to access files in CSIM ADF
 */
public final class CsimFileHandler extends IccFileHandler implements IccConstants {
    static final String LOG_TAG = "RIL_CsimFH";

    public CsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
    }

    protected void finalize() {
        logd("CsimFileHandler finalized");
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_SMS:
        case EF_CST:
        case EF_FDN:
        case EF_MSISDN:
        case EF_RUIM_SPN:
        case EF_CSIM_LI:
        case EF_CSIM_MDN:
        case EF_CSIM_IMSIM:
        case EF_CSIM_CDMAHOME:
        case EF_CSIM_EPRL:
            return MF_SIM + DF_ADF;
        }
        String path = getCommonIccEFPath(efid);
        if (path == null) {
            // The EFids in UICC phone book entries are decided by the card manufacturer.
            // So if we don't match any of the cases above and if its a UICC return
            // the global 3g phone book path.
            return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
        }
        return path;
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 1557304..9d8c201 100644

//Synthetic comment -- @@ -169,7 +169,7 @@
*/
public String getServiceProviderName ();
public State getIccCardState();
    public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType type);

/**
* @return true if a ICC card is present








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardApplication.java b/src/java/com/android/internal/telephony/IccCardApplicationStatus.java
similarity index 99%
rename from src/java/com/android/internal/telephony/IccCardApplication.java
rename to src/java/com/android/internal/telephony/IccCardApplicationStatus.java
//Synthetic comment -- index abb740e..f3f22ea 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
*
* {@hide}
*/
public class IccCardApplicationStatus {
public enum AppType{
APPTYPE_UNKNOWN,
APPTYPE_SIM,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index 409732a..26c0a7a 100644

//Synthetic comment -- @@ -31,8 +31,11 @@
import android.telephony.TelephonyManager;

import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.IccCardApplicationStatus.PersoSubState;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.IccCardStatus.PinState;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.uicc.UiccController;

import static com.android.internal.telephony.Phone.CDMA_SUBSCRIPTION_NV;
//Synthetic comment -- @@ -42,7 +45,8 @@
* The Phone App UI and the external world assumes that there is only one icc card,
* and one icc application available at a time. But the Uicc Controller can handle
* multiple instances of icc objects. This class implements the icc interface to expose
 * the current (based on voice radio technology) application on the uicc card, so
 * that external apps wont break.
*/

public class IccCardProxy extends Handler implements IccCard {
//Synthetic comment -- @@ -71,9 +75,9 @@
private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
private UiccController mUiccController = null;
private UiccCard mUiccCard = null;
    private UiccCardApplication mUiccApplication = null;
private IccRecords mIccRecords = null;
private CdmaSubscriptionSourceManager mCdmaSSM = null;
private boolean mRadioOn = false;
private boolean mCdmaSubscriptionFromNv = false;
private boolean mIsMultimodeCdmaPhone =
//Synthetic comment -- @@ -81,8 +85,10 @@
private boolean mQuietMode = false; // when set to true IccCardProxy will not broadcast
// ACTION_SIM_STATE_CHANGED intents
private boolean mInitialized = false;
    private State mExternalState = State.UNKNOWN;

    public IccCardProxy(Context context, CommandsInterface ci) {
        log("Creating");
this.mContext = context;
this.mCi = ci;
mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(context,
//Synthetic comment -- @@ -91,10 +97,11 @@
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
ci.registerForOn(this,EVENT_RADIO_ON, null);
ci.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_UNAVAILABLE, null);
        setExternalState(State.NOT_READY);
}

public void dispose() {
        log("Disposing");
//Cleanup icc references
mUiccController.unregisterForIccChanged(this);
mUiccController = null;
//Synthetic comment -- @@ -114,27 +121,50 @@
} else {
mCurrentAppType = UiccController.APP_FAM_3GPP2;
}
updateQuietMode();
}

    /** This function does not necessarily update mQuietMode right away
* In case of 3GPP2 subscription it needs more information (subscription source)
*/
private void updateQuietMode() {
        if (DBG) log("Updating quiet mode");
        boolean oldQuietMode = mQuietMode;
        boolean newQuietMode;
if (mCurrentAppType == UiccController.APP_FAM_3GPP) {
            newQuietMode = false;
            if (DBG) log("3GPP subscription -> QuietMode: " + newQuietMode);
} else {
//In case of 3gpp2 we need to find out if subscription used is coming from
//NV in which case we shouldn't broadcast any sim states changes if at the
//same time ro.config.multimode_cdma property set to false.
            //mInitialized = false;
            //handleCdmaSubscriptionSource();
            int newSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();
            mCdmaSubscriptionFromNv = newSubscriptionSource == CDMA_SUBSCRIPTION_NV;
            if (mCdmaSubscriptionFromNv && mIsMultimodeCdmaPhone) {
                log("Cdma multimode phone detected. Forcing IccCardProxy into 3gpp mode");
                mCurrentAppType = UiccController.APP_FAM_3GPP;
            }
            newQuietMode = mCdmaSubscriptionFromNv
                    && (mCurrentAppType == UiccController.APP_FAM_3GPP2) && !mIsMultimodeCdmaPhone;
}

        if (mQuietMode == false && newQuietMode == true) {
            // Last thing to do before switching to quiet mode is
            // broadcast ICC_READY
            log("Switching to QuietMode.");
            setExternalState(State.READY);
            mQuietMode = newQuietMode;
        } else if (mQuietMode == true && newQuietMode == false) {
            log("Switching out from QuietMode. Force broadcast of current state:" + mExternalState);
            mQuietMode = newQuietMode;
            setExternalState(mExternalState, true);
        }
        log("QuietMode is " + mQuietMode + " (app_type: " + mCurrentAppType + " nv: "
                + mCdmaSubscriptionFromNv + " multimode: " + mIsMultimodeCdmaPhone + ")");
        mInitialized = true;
        sendMessage(obtainMessage(EVENT_ICC_CHANGED));
}

public void handleMessage(Message msg) {
//Synthetic comment -- @@ -151,18 +181,17 @@
case EVENT_ICC_CHANGED:
if (mInitialized) {
updateIccAvailability();
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
//Synthetic comment -- @@ -171,81 +200,105 @@
broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
break;
case EVENT_NETWORK_LOCKED:
mNetworkLockedRegistrants.notifyRegistrants();
                setExternalState(State.NETWORK_LOCKED);
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
updateQuietMode();
break;
default:
                loge("Unhandled message with number: " + msg.what);
break;
}
}

void updateIccAvailability() {
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

    private void updateExternalState() {
        if (mUiccCard == null || mUiccCard.getCardState() == CardState.CARDSTATE_ABSENT) {
            if (mRadioOn) {
                setExternalState(State.ABSENT);
} else {
                setExternalState(State.NOT_READY);
}
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
                }
                break;
            case APPSTATE_READY:
                setExternalState(State.READY);
                break;
}
}

private void registerUiccCardEvents() {
if (mUiccCard != null) mUiccCard.registerForAbsent(this, EVENT_ICC_ABSENT, null);
        if (mUiccApplication != null) mUiccApplication.registerForReady(this, EVENT_APP_READY, null);
        if (mUiccApplication != null) mUiccApplication.registerForLocked(this, EVENT_ICC_LOCKED, null);
        if (mUiccApplication != null) mUiccApplication.registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, null);
if (mIccRecords != null) mIccRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
if (mIccRecords != null) mIccRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
}

private void unregisterUiccCardEvents() {
if (mUiccCard != null) mUiccCard.unregisterForAbsent(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForReady(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForLocked(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForNetworkLocked(this);
if (mIccRecords != null) mIccRecords.unregisterForImsiReady(this);
if (mIccRecords != null) mIccRecords.unregisterForRecordsLoaded(this);
}

/* why do external apps need to use this? */
public void broadcastIccStateChangedIntent(String value, String reason) {
if (mQuietMode) {
            log("QuietMode: NOT Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value
+ " reason " + reason);
return;
}
//Synthetic comment -- @@ -262,8 +315,8 @@
}

public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.changeIccFdnPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -273,8 +326,8 @@
}

public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.changeIccLockPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -284,24 +337,24 @@
}

private void processLockedState() {
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
                setExternalState(State.PIN_REQUIRED);
break;
            case APPSTATE_PUK:
                setExternalState(State.PUK_REQUIRED);
break;
}
}
//Synthetic comment -- @@ -310,43 +363,32 @@
return getState();
}

public boolean getIccFdnEnabled() {
        Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccFdnEnabled() : false;
return retValue;
}

public boolean getIccLockEnabled() {
/* defaults to true, if ICC is absent */
        Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccLockEnabled() : true;
return retValue;
}

public String getServiceProviderName() {
        if (mIccRecords != null) {
            return mIccRecords.getServiceProviderName();
}
return null;
}

public boolean hasIccCard() {
        if (mUiccCard != null && mUiccCard.getCardState() != CardState.CARDSTATE_ABSENT) {
return true;
}
return false;
}

    public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType type) {
Boolean retValue = mUiccCard != null ? mUiccCard.isApplicationOnIcc(type) : false;
return retValue;
}
//Synthetic comment -- @@ -417,8 +459,8 @@
}

public void setIccFdnEnabled(boolean enabled, String password, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.setIccFdnEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -428,8 +470,8 @@
}

public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.setIccLockEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -442,8 +484,8 @@
* Use invokeDepersonalization from PhoneBase class instead.
*/
public void supplyNetworkDepersonalization(String pin, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyNetworkDepersonalization(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("CommandsInterface is not set.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -453,8 +495,8 @@
}

public void supplyPin(String pin, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyPin(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -464,8 +506,8 @@
}

public void supplyPin2(String pin2, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyPin2(pin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -475,8 +517,8 @@
}

public void supplyPuk(String puk, String newPin, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyPuk(puk, newPin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -486,8 +528,8 @@
}

public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
        if (mUiccApplication != null) {
            mUiccApplication.supplyPuk2(puk2, newPin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -496,36 +538,26 @@
}
}

    private void setExternalState(State newState, boolean override) {
        if (!override && newState == mExternalState) {
            return;
}
        mExternalState = newState;
        SystemProperties.set(PROPERTY_SIM_STATE, mExternalState.toString());
        broadcastIccStateChangedIntent(mExternalState.getIntentString(),
                                       mExternalState.getReason());
    }
    private void setExternalState(State newState) {
        setExternalState(newState, false);
    }

    public State getState() {
        return mExternalState;
}

public IccFileHandler getIccFileHandler() {
        if (mUiccApplication != null) {
            return mUiccApplication.getIccFileHandler();
}
return null;
}
//Synthetic comment -- @@ -541,7 +573,11 @@
return false;
}

    private void log(String s) {
Log.d(LOG_TAG, s);
}

    private void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardStatus.java b/src/java/com/android/internal/telephony/IccCardStatus.java
//Synthetic comment -- index a3bdd76..b4a5e68 100644

//Synthetic comment -- @@ -57,19 +57,13 @@
}
}

    public CardState  mCardState;
    public PinState   mUniversalPinState;
    public int        mGsmUmtsSubscriptionAppIndex;
    public int        mCdmaSubscriptionAppIndex;
    public int        mImsSubscriptionAppIndex;

    public IccCardApplicationStatus[] mApplications;

public void setCardState(int state) {
switch(state) {
//Synthetic comment -- @@ -87,10 +81,6 @@
}
}

public void setUniversalPinState(int state) {
switch(state) {
case 0:
//Synthetic comment -- @@ -116,69 +106,34 @@
}
}

@Override
public String toString() {
        IccCardApplicationStatus app;

StringBuilder sb = new StringBuilder();
sb.append("IccCardState {").append(mCardState).append(",")
.append(mUniversalPinState)
        .append(",num_apps=").append(mApplications.length)
.append(",gsm_id=").append(mGsmUmtsSubscriptionAppIndex);
if (mGsmUmtsSubscriptionAppIndex >=0
&& mGsmUmtsSubscriptionAppIndex <CARD_MAX_APPS) {
            app = mApplications[mGsmUmtsSubscriptionAppIndex];
sb.append(app == null ? "null" : app);
}

sb.append(",cmda_id=").append(mCdmaSubscriptionAppIndex);
if (mCdmaSubscriptionAppIndex >=0
&& mCdmaSubscriptionAppIndex <CARD_MAX_APPS) {
            app = mApplications[mCdmaSubscriptionAppIndex];
sb.append(app == null ? "null" : app);
}

        sb.append(",ims_id=").append(mImsSubscriptionAppIndex);
        if (mImsSubscriptionAppIndex >=0
                && mImsSubscriptionAppIndex <CARD_MAX_APPS) {
            app = mApplications[mImsSubscriptionAppIndex];
            sb.append(app == null ? "null" : app);
        }

sb.append("}");









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccConstants.java b/src/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 1ba6dfe..847c883 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
    static final int EF_OPL = 0x6fc6;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;
//Synthetic comment -- @@ -85,6 +86,6 @@
static final String DF_GSM = "7F20";
static final String DF_CDMA = "7F25";

    //UICC access
    static final String DF_ADF = "7FFF";
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 89ec562..98ab17b 100644

//Synthetic comment -- @@ -91,8 +91,8 @@

// member variables
protected final CommandsInterface mCi;
    protected final UiccCardApplication mParentApp;
    protected final String mAid;

static class LoadLinearFixedContext {

//Synthetic comment -- @@ -122,8 +122,8 @@
/**
* Default constructor
*/
    protected IccFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        mParentApp = app;
mAid = aid;
mCi = ci;
}
//Synthetic comment -- @@ -225,6 +225,24 @@
}

/**
     * Load first @size bytes from SIM Transparent EF
     *
     * @param fileid EF id
     * @param size
     * @param onLoaded
     *
     * ((AsyncResult)(onLoaded.obj)).result is the byte[]
     *
     */
    public void loadEFTransparent(int fileid, int size, Message onLoaded) {
        Message response = obtainMessage(EVENT_READ_BINARY_DONE,
                        fileid, 0, onLoaded);

        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
                        0, 0, size, null, null, mAid, response);
    }

    /**
* Load a SIM Transparent EF-IMG. Used right after loadEFImgLinearFixed to
* retrive STK's icon data.
*
//Synthetic comment -- @@ -534,6 +552,9 @@
case EF_ICCID:
case EF_PL:
return MF_SIM;
        case EF_PBR:
            // we only support global phonebook.
            return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
case EF_IMG:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
//Synthetic comment -- @@ -544,8 +565,5 @@
protected abstract void logd(String s);

protected abstract void loge(String s);

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 0e5f2da..10d7515 100644

//Synthetic comment -- @@ -291,7 +291,7 @@
private int updateEfForIccType(int efid) {
// Check if we are trying to read ADN records
if (efid == IccConstants.EF_ADN) {
            if (phone.getIccCard().isApplicationOnIcc(IccCardApplicationStatus.AppType.APPTYPE_USIM)) {
return IccConstants.EF_PBR;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index d542658..67bdf9e 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
    protected UiccCardApplication mParentApp;

protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
protected RegistrantList mImsiReadyRegistrants = new RegistrantList();
//Synthetic comment -- @@ -106,11 +106,11 @@
}

// ***** Constructor
    public IccRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
mContext = c;
mCi = ci;
        mFh = app.getIccFileHandler();
        mParentApp = app;
}

/**
//Synthetic comment -- @@ -118,13 +118,12 @@
*/
public void dispose() {
mDestroyed.set(true);
        mParentApp = null;
mFh = null;
mCi = null;
mContext = null;
}

public abstract void onReady();

//***** Public Methods








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index f14c6b8..1457152 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.util.Log;

import com.android.internal.R;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.test.SimulatedRadioControl;
//Synthetic comment -- @@ -130,9 +131,10 @@
boolean mIsVoiceCapable = true;
protected UiccController mUiccController = null;
public AtomicReference<IccRecords> mIccRecords = new AtomicReference<IccRecords>();
public SmsStorageMonitor mSmsStorageMonitor;
public SmsUsageMonitor mSmsUsageMonitor;
    protected AtomicReference<UiccCardApplication> mUiccApplication =
            new AtomicReference<UiccCardApplication>();
public SMSDispatcher mSMS;

/**
//Synthetic comment -- @@ -254,7 +256,7 @@
// Initialize device storage and outgoing SMS usage monitors for SMSDispatchers.
mSmsStorageMonitor = new SmsStorageMonitor(this);
mSmsUsageMonitor = new SmsUsageMonitor(context);
        mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
}

//Synthetic comment -- @@ -276,7 +278,7 @@
mSmsUsageMonitor = null;
mSMS = null;
mIccRecords.set(null);
        mUiccApplication.set(null);
mDataConnectionTracker = null;
mUiccController = null;
}
//Synthetic comment -- @@ -648,9 +650,9 @@
* Retrieves the IccFileHandler of the Phone instance
*/
public IccFileHandler getIccFileHandler(){
        UiccCardApplication uiccApplication = mUiccApplication.get();
        if (uiccApplication == null) return null;
        return uiccApplication.getIccFileHandler();
}

/*
//Synthetic comment -- @@ -680,6 +682,16 @@
//throw new Exception("getIccCard Shouldn't be called from PhoneBase");
}

    /**
     * Subclasses of PhoneBase probably want to replace this with a
     * version scoped to their packages
     */
    protected AppState getCurrentUiccStateP() {
        UiccCardApplication uiccCardApplication = mUiccApplication.get();
        if (uiccCardApplication == null) return AppState.APPSTATE_UNKNOWN;
        return uiccCardApplication.getState();
    }

@Override
public String getIccSerialNumber() {
IccRecords r = mIccRecords.get();
//Synthetic comment -- @@ -1182,7 +1194,7 @@
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
pw.println(" mIccRecords=" + mIccRecords.get());
        pw.println(" mUiccApplication=" + mUiccApplication.get());
pw.println(" mSmsStorageMonitor=" + mSmsStorageMonitor);
pw.println(" mSmsUsageMonitor=" + mSmsUsageMonitor);
pw.println(" mSMS=" + mSMS);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneFactory.java b/src/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 2c85dc6..2600c79 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.sip.SipPhone;
import com.android.internal.telephony.sip.SipPhoneFactory;
import com.android.internal.telephony.uicc.UiccController;

/**
* {@hide}
//Synthetic comment -- @@ -138,6 +139,9 @@
//reads the system properties and makes commandsinterface
sCommandsInterface = new RIL(context, networkMode, cdmaSubscription);

                // Instantiate UiccController so that all other classes can just call getInstance()
                UiccController.make(context, sCommandsInterface);

int phoneType = TelephonyManager.getPhoneType(networkMode);
if (phoneType == PhoneConstants.PHONE_TYPE_GSM) {
Log.i(LOG_TAG, "Creating GSMPhone");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index a2d2806..3387840 100644

//Synthetic comment -- @@ -77,7 +77,13 @@
mCommandsInterface.registerForOn(this, EVENT_RADIO_ON, null);
mCommandsInterface.registerForVoiceRadioTechChanged(
this, EVENT_VOICE_RADIO_TECH_CHANGED, null);
        mIccCardProxy = new IccCardProxy(phone.getContext(), mCommandsInterface);
        if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_GSM) {
            // For the purpose of IccCardProxy we only care about the technology family
            mIccCardProxy.setVoiceRadioTech(ServiceState.RIL_RADIO_TECHNOLOGY_UMTS);
        } else if (phone.getPhoneType() == PhoneConstants.PHONE_TYPE_CDMA) {
            mIccCardProxy.setVoiceRadioTech(ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT);
        }
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index b14f6c8..b1c14ef 100644

//Synthetic comment -- @@ -50,6 +50,7 @@

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccCardApplicationStatus;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.IccRefreshResponse;
//Synthetic comment -- @@ -2985,24 +2986,23 @@

private Object
responseIccCardStatus(Parcel p) {
        IccCardApplicationStatus ca;

IccCardStatus status = new IccCardStatus();
status.setCardState(p.readInt());
status.setUniversalPinState(p.readInt());
        status.mGsmUmtsSubscriptionAppIndex = p.readInt();
        status.mCdmaSubscriptionAppIndex = p.readInt();
        status.mImsSubscriptionAppIndex = p.readInt();
int numApplications = p.readInt();

// limit to maximum allowed applications
if (numApplications > IccCardStatus.CARD_MAX_APPS) {
numApplications = IccCardStatus.CARD_MAX_APPS;
}
        status.mApplications = new IccCardApplicationStatus[numApplications];
for (int i = 0 ; i < numApplications ; i++) {
            ca = new IccCardApplicationStatus();
ca.app_type       = ca.AppTypeFromRILInt(p.readInt());
ca.app_state      = ca.AppStateFromRILInt(p.readInt());
ca.perso_substate = ca.PersoSubstateFromRILInt(p.readInt());
//Synthetic comment -- @@ -3011,7 +3011,7 @@
ca.pin1_replaced  = p.readInt();
ca.pin1           = ca.PinStateFromRILInt(p.readInt());
ca.pin2           = ca.PinStateFromRILInt(p.readInt());
            status.mApplications[i] = ca;
}
return status;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 2990457..1cdb693 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
//Synthetic comment -- @@ -38,7 +39,7 @@

protected CommandsInterface cm;
protected UiccController mUiccController = null;
    protected UiccCardApplication mUiccApplcation = null;
protected IccRecords mIccRecords = null;

public ServiceState ss;
//Synthetic comment -- @@ -174,7 +175,7 @@
protected static final String REGISTRATION_DENIED_GEN  = "General";
protected static final String REGISTRATION_DENIED_AUTH = "Authentication Failure";

    public ServiceStateTracker(Context c, CommandsInterface ci) {
cm = ci;
mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCard.java b/src/java/com/android/internal/telephony/UiccCard.java
//Synthetic comment -- index afc37aa..0724020 100644

//Synthetic comment -- @@ -35,14 +35,17 @@
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.IccCardApplicationStatus;
import com.android.internal.telephony.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.IccCardStatus.PinState;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cdma.CDMALTEPhone;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.RuimFileHandler;
import com.android.internal.telephony.cdma.RuimRecords;
//Synthetic comment -- @@ -55,134 +58,135 @@
* {@hide}
*/
public class UiccCard {
    protected static final String LOG_TAG = "RIL_UiccCard";
    protected static final boolean DBG = true;

    private CardState mCardState;
    private PinState mUniversalPinState;
    private int mGsmUmtsSubscriptionAppIndex;
    private int mCdmaSubscriptionAppIndex;
    private int mImsSubscriptionAppIndex;
    private UiccCardApplication[] mUiccApplications =
            new UiccCardApplication[IccCardStatus.CARD_MAX_APPS];
    private Context mContext;
    private CommandsInterface mCi;
private CatService mCatService;
    private boolean mDestroyed = false; //set to true once this card is commanded to be disposed of.

private RegistrantList mAbsentRegistrants = new RegistrantList();

private static final int EVENT_CARD_REMOVED = 13;
private static final int EVENT_CARD_ADDED = 14;

    public UiccCard(Context c, CommandsInterface ci, IccCardStatus ics) {
        if (DBG) log("Creating");
        update(c, ci, ics);
}

public void dispose() {
        if (DBG) log("Disposing card");
        if (mCatService != null) mCatService.dispose();
        for (UiccCardApplication app : mUiccApplications) {
            if (app != null) {
                app.dispose();
            }
        }
        mCatService = null;
        mUiccApplications = null;
}

    public void update(Context c, CommandsInterface ci, IccCardStatus ics) {
        if (mDestroyed) {
            loge("Updated after destroyed! Fix me!");
            return;
        }
        CardState oldState = mCardState;
        mCardState = ics.mCardState;
        mUniversalPinState = ics.mUniversalPinState;
        mGsmUmtsSubscriptionAppIndex = ics.mGsmUmtsSubscriptionAppIndex;
        mCdmaSubscriptionAppIndex = ics.mCdmaSubscriptionAppIndex;
        mImsSubscriptionAppIndex = ics.mImsSubscriptionAppIndex;
        mContext = c;
        mCi = ci;
        //update applications
        if (DBG) log(ics.mApplications.length + " applications");
        for ( int i = 0; i < mUiccApplications.length; i++) {
            if (mUiccApplications[i] == null) {
                //Create newly added Applications
                if (i < ics.mApplications.length) {
                    mUiccApplications[i] = new UiccCardApplication(this,
                            ics.mApplications[i], mContext, mCi);
                }
            } else if (i >= ics.mApplications.length) {
                //Delete removed applications
                mUiccApplications[i].dispose();
                mUiccApplications[i] = null;
            } else {
                //Update the rest
                mUiccApplications[i].update(ics.mApplications[i], mContext, mCi);
            }
}

        if (mUiccApplications.length > 0 && mUiccApplications[0] != null) {
            // Initialize or Reinitialize CatService
            mCatService = CatService.getInstance(mCi,
                                                 mContext,
                                                 this);
        } else {
            if (mCatService != null) {
                mCatService.dispose();
}
            mCatService = null;
}

        sanitizeApplicationIndexes();

        if (oldState != CardState.CARDSTATE_ABSENT && mCardState == CardState.CARDSTATE_ABSENT) {
            mAbsentRegistrants.notifyRegistrants();
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_REMOVED, null));
        } else if (oldState == CardState.CARDSTATE_ABSENT && mCardState != CardState.CARDSTATE_ABSENT) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_ADDED, null));
        }
}

protected void finalize() {
        if (DBG) log("UiccCard finalized");
}

    /**
     * This function makes sure that application indexes are valid
     * and resets invalid indexes. (This should never happen, but in case
     * RIL misbehaves we need to manage situation gracefully)
     */
    private void sanitizeApplicationIndexes() {
        mGsmUmtsSubscriptionAppIndex =
                checkIndex(mGsmUmtsSubscriptionAppIndex, AppType.APPTYPE_SIM, AppType.APPTYPE_USIM);
        mCdmaSubscriptionAppIndex =
                checkIndex(mCdmaSubscriptionAppIndex, AppType.APPTYPE_RUIM, AppType.APPTYPE_CSIM);
        mImsSubscriptionAppIndex =
                checkIndex(mImsSubscriptionAppIndex, AppType.APPTYPE_ISIM, null);
}

    private int checkIndex(int index, AppType expectedAppType, AppType altExpectedAppType) {
        if (mUiccApplications == null || index >= mUiccApplications.length) {
            loge("App index " + index + " is invalid since there are no applications");
            return -1;
        }

        if (index < 0) {
            // This is normal. (i.e. no application of this type)
            return -1;
        }

        if (mUiccApplications[index].getType() != expectedAppType &&
            mUiccApplications[index].getType() != altExpectedAppType) {
            loge("App index " + index + " is invalid since it's not " +
                    expectedAppType + " and not " + altExpectedAppType);
            return -1;
        }

        // Seems to be valid
        return index;
}

/**
//Synthetic comment -- @@ -193,7 +197,7 @@

mAbsentRegistrants.add(r);

        if (mCardState == CardState.CARDSTATE_ABSENT) {
r.notifyRegistrant();
}
}
//Synthetic comment -- @@ -202,326 +206,6 @@
mAbsentRegistrants.remove(h);
}

private void onIccSwap(boolean isAdded) {
// TODO: Here we assume the device can't handle SIM hot-swap
//      and has to reboot. We may want to add a property,
//Synthetic comment -- @@ -537,9 +221,9 @@
@Override
public void onClick(DialogInterface dialog, int which) {
if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (DBG) log("Reboot due to SIM swap");
                    PowerManager pm = (PowerManager) mContext
                            .getSystemService(Context.POWER_SERVICE);
pm.reboot("SIM is added.");
}
}
//Synthetic comment -- @@ -554,7 +238,7 @@
r.getString(R.string.sim_removed_message);
String buttonTxt = r.getString(R.string.sim_restart_button);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
.setTitle(title)
.setMessage(message)
.setPositiveButton(buttonTxt, listener)
//Synthetic comment -- @@ -563,133 +247,16 @@
dialog.show();
}

protected Handler mHandler = new Handler() {
@Override
public void handleMessage(Message msg){
            if (mDestroyed) {
                loge("Received message " + msg + "[" + msg.what
+ "] while being destroyed. Ignoring.");
return;
}

switch (msg.what) {
case EVENT_CARD_REMOVED:
onIccSwap(false);
break;
//Synthetic comment -- @@ -697,194 +264,59 @@
onIccSwap(true);
break;
default:
                    loge("Unknown Event " + msg.what);
}
}
};

    public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType type) {
        for (int i = 0 ; i < mUiccApplications.length; i++) {
            if (mUiccApplications[i] != null && mUiccApplications[i].getType() == type) {
return true;
}
}
return false;
}

    public CardState getCardState() {
        return mCardState;
    }

    public PinState getUniversalPinState() {
        return mUniversalPinState;
    }

    public UiccCardApplication getApplication(int family) {
        int index = IccCardStatus.CARD_MAX_APPS;
        switch (family) {
            case UiccController.APP_FAM_3GPP:
                index = mGsmUmtsSubscriptionAppIndex;
                break;
            case UiccController.APP_FAM_3GPP2:
                index = mCdmaSubscriptionAppIndex;
                break;
            case UiccController.APP_FAM_IMS:
                index = mImsSubscriptionAppIndex;
                break;
}
        if (index >= 0 && index < mUiccApplications.length) {
            return mUiccApplications[index];
        }
        return null;
    }

    public UiccCardApplication getApplicationIndex(int index) {
        if (index >= 0 && index < mUiccApplications.length) {
            return mUiccApplications[index];
        }
        return null;
}

private void log(String msg) {
        Log.d(LOG_TAG, msg);
}

private void loge(String msg) {
        Log.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
new file mode 100644
//Synthetic comment -- index 0000000..8de1dfc7

//Synthetic comment -- @@ -0,0 +1,543 @@
/*
 * Copyright (C) 2006, 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.IccCardApplicationStatus.PersoSubState;
import com.android.internal.telephony.IccCardStatus.PinState;
import com.android.internal.telephony.cdma.RuimFileHandler;
import com.android.internal.telephony.cdma.RuimRecords;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.ims.IsimFileHandler;
import com.android.internal.telephony.ims.IsimUiccRecords;

/**
 * {@hide}
 */
public class UiccCardApplication {
    private static final String LOG_TAG = "RIL_UiccCardApplication";
    private static final boolean DBG = true;

    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 1;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 2;

    private UiccCard mUiccCard; //parent
    private AppState      mAppState;
    private AppType       mAppType;
    private PersoSubState mPersoSubState;
    private String        mAid;
    private String        mAppLabel;
    private boolean       mPin1Replaced;
    private PinState      mPin1State;
    private PinState      mPin2State;
    private boolean       mIccFdnEnabled = false; // Default to disabled.
    private boolean mDesiredFdnEnabled;

    private CommandsInterface mCi;
    private Context mContext;
    private IccRecords mIccRecords;
    private IccFileHandler mIccFh;

    private boolean mDestroyed = false; //set to true once this App is commanded to be disposed of.

    private RegistrantList mReadyRegistrants = new RegistrantList();
    private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();

    UiccCardApplication(UiccCard uiccCard, IccCardApplicationStatus as, Context c, CommandsInterface ci) {
        if (DBG) log("Creating UiccApp: " + as);
        mUiccCard = uiccCard;
        mAppState = as.app_state;
        mAppType = as.app_type;
        mPersoSubState = as.perso_substate;
        mAid = as.aid;
        mAppLabel = as.app_label;
        mPin1Replaced = (as.pin1_replaced != 0);
        mPin1State = as.pin1;
        mPin2State = as.pin2;

        mContext = c;
        mCi = ci;

        mIccFh = createIccFileHandler(as.app_type);
        mIccRecords = createIccRecords(as.app_type, mContext, mCi);
        if (mAppState == AppState.APPSTATE_READY) {
            queryFdn();
        }
    }

    void update (IccCardApplicationStatus as, Context c, CommandsInterface ci) {
        if (mDestroyed) {
            loge("Application updated after destroyed! Fix me!");
            return;
        }

        if (DBG) log(mAppType + " update. New " + as);
        mContext = c;
        mCi = ci;
        AppType oldAppType = mAppType;
        AppState oldAppState = mAppState;
        PersoSubState oldPersoSubState = mPersoSubState;
        mAppType = as.app_type;
        mAppState = as.app_state;
        mPersoSubState = as.perso_substate;
        mAid = as.aid;
        mAppLabel = as.app_label;
        mPin1Replaced = (as.pin1_replaced != 0);
        mPin1State = as.pin1;
        mPin2State = as.pin2;

        if (mAppType != oldAppType) {
            mIccFh.dispose();
            mIccRecords.dispose();
            mIccFh = createIccFileHandler(as.app_type);
            mIccRecords = createIccRecords(as.app_type, c, ci);
        }

        if (mPersoSubState != oldPersoSubState &&
                mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
            notifyNetworkLockedRegistrantsIfNeeded(null);
        }

        if (mAppState != oldAppState) {
            if (DBG) log(oldAppType + " changed state: " + oldAppState + " -> " + mAppState);
            // If the app state turns to APPSTATE_READY, then query FDN status,
            //as it might have failed in earlier attempt.
            if (mAppState == AppState.APPSTATE_READY) {
                queryFdn();
            }
            notifyPinLockedRegistrantsIfNeeded(null);
            notifyReadyRegistrantsIfNeeded(null);
        }
    }

    synchronized void dispose() {
        if (DBG) log(mAppType + " being Disposed");
        mDestroyed = true;
        if (mIccRecords != null) { mIccRecords.dispose();}
        if (mIccFh != null) { mIccFh.dispose();}
        mIccRecords = null;
        mIccFh = null;
    }

    private IccRecords createIccRecords(AppType type, Context c, CommandsInterface ci) {
        if (type == AppType.APPTYPE_USIM || type == AppType.APPTYPE_SIM) {
            return new SIMRecords(this, c, ci);
        } else if (type == AppType.APPTYPE_RUIM || type == AppType.APPTYPE_CSIM){
            return new RuimRecords(this, c, ci);
        } else if (type == AppType.APPTYPE_ISIM) {
            return new IsimUiccRecords(this, c, ci);
        } else {
            // Unknown app type (maybe detection is still in progress)
            return null;
        }
    }

    private IccFileHandler createIccFileHandler(AppType type) {
        switch (type) {
            case APPTYPE_SIM:
                return new SIMFileHandler(this, mAid, mCi);
            case APPTYPE_RUIM:
                return new RuimFileHandler(this, mAid, mCi);
            case APPTYPE_USIM:
                return new UsimFileHandler(this, mAid, mCi);
            case APPTYPE_CSIM:
                return new CsimFileHandler(this, mAid, mCi);
            case APPTYPE_ISIM:
                return new IsimFileHandler(this, mAid, mCi);
            default:
                return null;
        }
    }

    private void queryFdn() {
        //This shouldn't change run-time. So needs to be called only once.
        int serviceClassX;

        serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                        CommandsInterface.SERVICE_CLASS_DATA +
                        CommandsInterface.SERVICE_CLASS_FAX;
        mCi.queryFacilityLock (
                CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
                mHandler.obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
    }
    /**
     * Interperate EVENT_QUERY_FACILITY_LOCK_DONE
     * @param ar is asyncResult of Query_Facility_Locked
     */
    private void onQueryFdnEnabled(AsyncResult ar) {
        if(ar.exception != null) {
            if (DBG) log("Error in querying facility lock:" + ar.exception);
            return;
        }

        int[] ints = (int[])ar.result;
        if(ints.length != 0) {
            mIccFdnEnabled = (0!=ints[0]);
            if (DBG) log("Query facility lock : "  + mIccFdnEnabled);
        } else {
            loge("Bogus facility lock response");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            AsyncResult ar;

            if (mDestroyed) {
                loge("Received message " + msg + "[" + msg.what
                        + "] while being destroyed. Ignoring.");
                return;
            }

            switch (msg.what) {
                case EVENT_QUERY_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;
                    onQueryFdnEnabled(ar);
                    break;
                case EVENT_CHANGE_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception == null) {
                        mIccFdnEnabled = mDesiredFdnEnabled;
                        if (DBG) log("EVENT_CHANGE_FACILITY_FDN_DONE: " +
                                "mIccFdnEnabled=" + mIccFdnEnabled);
                    } else {
                        loge("Error change facility fdn with exception "
                                + ar.exception);
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
                default:
                    loge("Unknown Event " + msg.what);
            }
        }
    };

    public void registerForReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mReadyRegistrants.add(r);
        notifyReadyRegistrantsIfNeeded(r);
    }

    public void unregisterForReady(Handler h) {
        mReadyRegistrants.remove(h);
    }

    /**
     * Notifies handler of any transition into State.isPinLocked()
     */
    public void registerForLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mPinLockedRegistrants.add(r);
        notifyPinLockedRegistrantsIfNeeded(r);
    }

    public void unregisterForLocked(Handler h) {
        mPinLockedRegistrants.remove(h);
    }

    /**
     * Notifies handler of any transition into State.NETWORK_LOCKED
     */
    public void registerForNetworkLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mNetworkLockedRegistrants.add(r);
        notifyNetworkLockedRegistrantsIfNeeded(r);
    }

    public void unregisterForNetworkLocked(Handler h) {
        mNetworkLockedRegistrants.remove(h);
    }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyReadyRegistrantsIfNeeded(Registrant r) {
        if (mDestroyed) {
            return;
        }
        if (mAppState == AppState.APPSTATE_READY) {
            if (mPin1State == PinState.PINSTATE_ENABLED_NOT_VERIFIED || mPin1State == PinState.PINSTATE_ENABLED_BLOCKED || mPin1State == PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
                loge("Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
                //Don't notify if application is in insane state
                return;
            }
            if (r == null) {
                if (DBG) log("Notifying registrants: READY");
                mReadyRegistrants.notifyRegistrants();
            } else {
                if (DBG) log("Notifying 1 registrant: READY");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyPinLockedRegistrantsIfNeeded(Registrant r) {
        if (mDestroyed) {
            return;
        }

        if (mAppState == AppState.APPSTATE_PIN ||
                mAppState == AppState.APPSTATE_PUK) {
            if (mPin1State == PinState.PINSTATE_ENABLED_VERIFIED || mPin1State == PinState.PINSTATE_DISABLED) {
                loge("Sanity check failed! APPSTATE is locked while PIN1 is not!!!");
                //Don't notify if application is in insane state
                return;
            }
            if (r == null) {
                if (DBG) log("Notifying registrants: LOCKED");
                mPinLockedRegistrants.notifyRegistrants();
            } else {
                if (DBG) log("Notifying 1 registrant: LOCKED");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyNetworkLockedRegistrantsIfNeeded(Registrant r) {
        if (mDestroyed) {
            return;
        }

        if (mAppState == AppState.APPSTATE_SUBSCRIPTION_PERSO &&
                mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
            if (r == null) {
                if (DBG) log("Notifying registrants: NETWORK_LOCKED");
                mNetworkLockedRegistrants.notifyRegistrants();
            } else {
                if (DBG) log("Notifying 1 registrant: NETWORK_LOCED");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public AppState getState() {
        return mAppState;
    }

    public AppType getType() {
        return mAppType;
    }

    public PersoSubState getPersoSubState() {
        return mPersoSubState;
    }

    public String getAid() {
        return mAid;
    }

    public PinState getPin1State() {
        if (mPin1Replaced) {
            return mUiccCard.getUniversalPinState();
        }
        return mPin1State;
    }

    public IccFileHandler getIccFileHandler() {
        return mIccFh;
    }

    public IccRecords getIccRecords() {
        return mIccRecords;
    }

    /**
     * Supply the ICC PIN to the ICC
     *
     * When the operation is complete, onComplete will be sent to its
     * Handler.
     *
     * onComplete.obj will be an AsyncResult
     *
     * ((AsyncResult)onComplete.obj).exception == null on success
     * ((AsyncResult)onComplete.obj).exception != null on fail
     *
     * If the supplied PIN is incorrect:
     * ((AsyncResult)onComplete.obj).exception != null
     * && ((AsyncResult)onComplete.obj).exception
     *       instanceof com.android.internal.telephony.gsm.CommandException)
     * && ((CommandException)(((AsyncResult)onComplete.obj).exception))
     *          .getCommandError() == CommandException.Error.PASSWORD_INCORRECT
     *
     *
     */
    public void supplyPin (String pin, Message onComplete) {
        mCi.supplyIccPin(pin, onComplete);
    }

    public void supplyPuk (String puk, String newPin, Message onComplete) {
        mCi.supplyIccPuk(puk, newPin, onComplete);
    }

    public void supplyPin2 (String pin2, Message onComplete) {
        mCi.supplyIccPin2(pin2, onComplete);
    }

    public void supplyPuk2 (String puk2, String newPin2, Message onComplete) {
        mCi.supplyIccPuk2(puk2, newPin2, onComplete);
    }

    public void supplyNetworkDepersonalization (String pin, Message onComplete) {
        if (DBG) log("Network Despersonalization: " + pin);
        mCi.supplyNetworkDepersonalization(pin, onComplete);
    }

    /**
     * Check whether ICC pin lock is enabled
     * This is a sync call which returns the cached pin enabled state
     *
     * @return true for ICC locked enabled
     *         false for ICC locked disabled
     */
    public boolean getIccLockEnabled() {
        // Use getPin1State to take into account pin1Replaced flag
        PinState pinState = getPin1State();
        return pinState == PinState.PINSTATE_ENABLED_NOT_VERIFIED ||
               pinState == PinState.PINSTATE_ENABLED_VERIFIED ||
               pinState == PinState.PINSTATE_ENABLED_BLOCKED ||
               pinState == PinState.PINSTATE_ENABLED_PERM_BLOCKED;
     }

    /**
     * Check whether ICC fdn (fixed dialing number) is enabled
     * This is a sync call which returns the cached pin enabled state
     *
     * @return true for ICC fdn enabled
     *         false for ICC fdn disabled
     */
     public boolean getIccFdnEnabled() {
        return mIccFdnEnabled;
     }

     /**
      * Set the ICC pin lock enabled or disabled
      * When the operation is complete, onComplete will be sent to its handler
      *
      * @param enabled "true" for locked "false" for unlocked.
      * @param password needed to change the ICC pin state, aka. Pin1
      * @param onComplete
      *        onComplete.obj will be an AsyncResult
      *        ((AsyncResult)onComplete.obj).exception == null on success
      *        ((AsyncResult)onComplete.obj).exception != null on fail
      */
     public void setIccLockEnabled (boolean enabled,
             String password, Message onComplete) {
         int serviceClassX;
         serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                 CommandsInterface.SERVICE_CLASS_DATA +
                 CommandsInterface.SERVICE_CLASS_FAX;

         mCi.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_SIM,
                 enabled, password, serviceClassX, mAid,
                 onComplete);
     }

     /**
      * Set the ICC fdn enabled or disabled
      * When the operation is complete, onComplete will be sent to its handler
      *
      * @param enabled "true" for locked "false" for unlocked.
      * @param password needed to change the ICC fdn enable, aka Pin2
      * @param onComplete
      *        onComplete.obj will be an AsyncResult
      *        ((AsyncResult)onComplete.obj).exception == null on success
      *        ((AsyncResult)onComplete.obj).exception != null on fail
      */
     public void setIccFdnEnabled (boolean enabled,
             String password, Message onComplete) {
         int serviceClassX;
         serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                 CommandsInterface.SERVICE_CLASS_DATA +
                 CommandsInterface.SERVICE_CLASS_FAX +
                 CommandsInterface.SERVICE_CLASS_SMS;

         mDesiredFdnEnabled = enabled;

         mCi.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_FD,
                 enabled, password, serviceClassX, mAid,
                 mHandler.obtainMessage(EVENT_CHANGE_FACILITY_FDN_DONE, onComplete));
     }

     /**
      * Change the ICC password used in ICC pin lock
      * When the operation is complete, onComplete will be sent to its handler
      *
      * @param oldPassword is the old password
      * @param newPassword is the new password
      * @param onComplete
      *        onComplete.obj will be an AsyncResult
      *        ((AsyncResult)onComplete.obj).exception == null on success
      *        ((AsyncResult)onComplete.obj).exception != null on fail
      */
     public void changeIccLockPassword(String oldPassword, String newPassword,
             Message onComplete) {
         if (DBG) log("Change Pin1 old: " + oldPassword + " new: " + newPassword);
         mCi.changeIccPinForApp(oldPassword, newPassword, mAid,
                 onComplete);

     }

    /**
     * Change the ICC password used in ICC fdn enable
     * When the operation is complete, onComplete will be sent to its handler
     *
     * @param oldPassword is the old password
     * @param newPassword is the new password
     * @param onComplete
     *        onComplete.obj will be an AsyncResult
     *        ((AsyncResult)onComplete.obj).exception == null on success
     *        ((AsyncResult)onComplete.obj).exception != null on fail
     */
    public void changeIccFdnPassword(String oldPassword, String newPassword,
            Message onComplete) {
        if (DBG) log("Change Pin2 old: " + oldPassword + " new: " + newPassword);
        mCi.changeIccPin2ForApp(oldPassword, newPassword, mAid,
                onComplete);
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }

    private void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..ae4c05e

//Synthetic comment -- @@ -0,0 +1,88 @@
/*
 * Copyright (C) 2006, 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;

/**
 * {@hide}
 * This class should be used to access files in USIM ADF
 */
public final class UsimFileHandler extends IccFileHandler implements IccConstants {
    static final String LOG_TAG = "RIL_UsimFH";

    public UsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
    }

    protected void finalize() {
        logd("UsimFileHandler finalized");
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_SMS:
        case EF_EXT6:
        case EF_MWIS:
        case EF_MBI:
        case EF_SPN:
        case EF_AD:
        case EF_MBDN:
        case EF_PNN:
        case EF_OPL:
        case EF_SPDI:
        case EF_SST:
        case EF_CFIS:
        case EF_MAILBOX_CPHS:
        case EF_VOICE_MAIL_INDICATOR_CPHS:
        case EF_CFF_CPHS:
        case EF_SPN_CPHS:
        case EF_SPN_SHORT_CPHS:
        case EF_FDN:
        case EF_MSISDN:
        case EF_EXT2:
        case EF_INFO_CPHS:
        case EF_CSP_CPHS:
            return MF_SIM + DF_ADF;

        case EF_PBR:
            // we only support global phonebook.
            return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
        }
        String path = getCommonIccEFPath(efid);
        if (path == null) {
            // The EFids in USIM phone book entries are decided by the card manufacturer.
            // So if we don't match any of the cases above and if its a USIM return
            // the phone book path.
            return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
        }
        return path;
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index dc6b9f0..4934fd7 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;


import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -64,6 +65,7 @@

// Class members
private static IccRecords mIccRecords;
    private static UiccCardApplication mUiccApplication;

// Service members.
// Protects singleton instance lazy initialization.
//Synthetic comment -- @@ -101,9 +103,9 @@
static final String STK_DEFAULT = "Defualt Message";

/* Intentionally private for singleton */
    private CatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir,
            Context context, IccFileHandler fh, UiccCard ic) {
        if (ci == null || ca == null || ir == null || context == null || fh == null
|| ic == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
//Synthetic comment -- @@ -122,9 +124,10 @@
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

mIccRecords = ir;
        mUiccApplication = ca;

// Register for SIM ready event.
        mUiccApplication.registerForReady(this, MSG_ID_SIM_READY, null);
mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

// Check if STK application is availalbe
//Synthetic comment -- @@ -553,24 +556,46 @@
* @param ic Icc card
* @return The only Service object in the system
*/
    public static CatService getInstance(CommandsInterface ci,
            Context context, UiccCard ic) {
        UiccCardApplication ca = null;
        IccFileHandler fh = null;
        IccRecords ir = null;
        if (ic != null) {
            /* Since Cat is not tied to any application, but rather is Uicc application
             * in itself - just get any FileHandler and IccRecords object
             */
            ca = ic.getApplicationIndex(0);
            if (ca != null) {
                fh = ca.getIccFileHandler();
                ir = ca.getIccRecords();
            }
        }
synchronized (sInstanceLock) {
if (sInstance == null) {
                if (ci == null || ca == null || ir == null || context == null || fh == null
|| ic == null) {
return null;
}
HandlerThread thread = new HandlerThread("Cat Telephony service");
thread.start();
                sInstance = new CatService(ci, ca, ir, context, fh, ic);
CatLog.d(sInstance, "NEW sInstance");
} else if ((ir != null) && (mIccRecords != ir)) {
                if (mIccRecords != null) {
                    mIccRecords.unregisterForRecordsLoaded(sInstance);
                }

                if (mUiccApplication != null) {
                    mUiccApplication.unregisterForReady(sInstance);
                }
                CatLog.d(sInstance, "Reinitialize the Service with SIMRecords and UiccCardApplication");
mIccRecords = ir;
                mUiccApplication = ca;

// re-Register for SIM ready event.
mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
                mUiccApplication.registerForReady(sInstance, MSG_ID_SIM_READY, null);
CatLog.d(sInstance, "sr changed reinitialize and return current sInstance");
} else {
CatLog.d(sInstance, "Return current sInstance");
//Synthetic comment -- @@ -585,7 +610,7 @@
* @return The only Service object in the system
*/
public static AppInterface getInstance() {
        return getInstance(null, null, null);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index f125484..48f6359 100644

//Synthetic comment -- @@ -28,17 +28,18 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.ims.IsimUiccRecords;
import com.android.internal.telephony.uicc.UiccController;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -52,6 +53,12 @@
/** Secondary SMSDispatcher for 3GPP format messages. */
SMSDispatcher m3gppSMS;

    /** CdmaLtePhone in addition to RuimRecords available from
     * PhoneBase needs access to SIMRecords and IsimUiccRecords
     */
    private SIMRecords mSimRecords;
    private IsimUiccRecords mIsimUiccRecords;

/**
* Small container class used to hold information relevant to
* the carrier selection process. operatorNumeric can be ""
//Synthetic comment -- @@ -200,12 +207,11 @@

@Override
public boolean updateCurrentCarrierInProvider() {
        if (mSimRecords != null) {
try {
Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
ContentValues map = new ContentValues();
                String operatorNumeric = mSimRecords.getOperatorNumeric();
map.put(Telephony.Carriers.NUMERIC, operatorNumeric);
if (DBG) log("updateCurrentCarrierInProvider from UICC: numeric=" +
operatorNumeric);
//Synthetic comment -- @@ -223,8 +229,7 @@
// return IMSI from USIM as subscriber ID.
@Override
public String getSubscriberId() {
        return (mSimRecords != null) ? mSimRecords.getIMSI() : "";
}

@Override
//Synthetic comment -- @@ -239,14 +244,12 @@

@Override
public IsimRecords getIsimRecords() {
        return mIsimUiccRecords;
}

@Override
public String getMsisdn() {
        return (mSimRecords != null) ? mSimRecords.getMsisdnNumber() : null;
}

@Override
//Synthetic comment -- @@ -260,23 +263,40 @@
}

@Override
    protected void onUpdateIccAvailability() {
        if (mUiccController == null ) {
return;
}

        // Update IsimRecords
        UiccCardApplication newUiccApplication = mUiccController.getUiccCardApplication(UiccController.APP_FAM_IMS);
        IsimUiccRecords newIsimUiccRecords = null;

        if (newUiccApplication != null) {
            newIsimUiccRecords = (IsimUiccRecords)newUiccApplication.getIccRecords();
}
        mIsimUiccRecords = newIsimUiccRecords;

        // Update UsimRecords
        newUiccApplication = mUiccController.getUiccCardApplication(UiccController.APP_FAM_3GPP);
        SIMRecords newSimRecords = null;
        if (newUiccApplication != null) {
            newSimRecords = (SIMRecords)newUiccApplication.getIccRecords();
        }
        if (mSimRecords != newSimRecords) {
            if (mSimRecords != null) {
                log("Removing stale SIMRecords object.");
                mSimRecords.unregisterForNewSms(this);
                mSimRecords = null;
            }
            if (newSimRecords != null) {
                log("New SIMRecords found");
                mSimRecords = newSimRecords;
                mSimRecords.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
            }
        }

        super.onUpdateIccAvailability();
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index c8afe4b..918725f 100755

//Synthetic comment -- @@ -65,7 +65,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.uicc.UiccController;

//Synthetic comment -- @@ -808,6 +810,10 @@
return null;
}

    /*package*/ AppState getCurrentUiccState() {
        return super.getCurrentUiccStateP();
    }

/**
* Notify any interested party of a Phone state change  {@link PhoneConstants.State}
*/
//Synthetic comment -- @@ -1072,29 +1078,26 @@
return;
}

        UiccCardApplication newUiccApplication = 
                mUiccController.getUiccCardApplication(UiccController.APP_FAM_3GPP2);

        UiccCardApplication app = mUiccApplication.get();
        if (app != newUiccApplication) {
            if (app != null) {
log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForRuimRecordEvents();
                    mRuimPhoneBookInterfaceManager.updateIccRecords(null);
}
mIccRecords.set(null);
                mUiccApplication.set(null);
}
            if (newUiccApplication != null) {
                log("New Uicc application found");
                mUiccApplication.set(newUiccApplication);
                mIccRecords.set(newUiccApplication.getIccRecords());
registerForRuimRecordEvents();
                mRuimPhoneBookInterfaceManager.updateIccRecords(mIccRecords.get());
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index c75290e..c05a3fe 100755

//Synthetic comment -- @@ -422,7 +422,8 @@
return DisconnectCause.OUT_OF_SERVICE;
} else if (phone.mCdmaSubscriptionSource ==
CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM
                               && (phone.getCurrentUiccState() !=
                                          IccCardApplicationStatus.AppState.APPSTATE_READY)) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index b986254..54a157c 100644

//Synthetic comment -- @@ -43,12 +43,12 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -1023,11 +1023,7 @@
return;
}

        IccRecords newIccRecords = mUiccController.getIccRecords(UiccController.APP_FAM_3GPP2);

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -1037,7 +1033,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New records found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index b173035..03e3ab6 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.util.Log;
import android.util.EventLog;

import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.gsm.GsmDataConnectionTracker;
import com.android.internal.telephony.IccCardConstants;

//Synthetic comment -- @@ -65,12 +66,12 @@
handlePollStateResult(msg.what, ar);
break;
case EVENT_RUIM_RECORDS_LOADED:
            RuimRecords ruim = (RuimRecords)mIccRecords;
            if ((ruim != null) && ruim.isProvisioned()) {
                mMdn = ruim.getMdn();
                mMin = ruim.getMin();
                parseSidNid(ruim.getSid(), ruim.getNid());
                mPrlVersion = ruim.getPrlVersion();;
mIsMinInfoReady = true;
updateOtaspState();
}
//Synthetic comment -- @@ -359,12 +360,12 @@
ss.setOperatorAlphaLong(eriText);
}

            if (mUiccApplcation != null && mUiccApplcation.getState() == AppState.APPSTATE_READY &&
mIccRecords != null) {
// SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
// one configfured in SIM, use operator name  from CSIM record.
boolean showSpn =
                    ((RuimRecords)mIccRecords).getCsimSpnDisplayCondition();
int iconIndex = ss.getCdmaEriIconIndex();

if (showSpn && (iconIndex == EriInfo.ROAMING_INDICATOR_OFF) &&








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java
deleted file mode 100644
//Synthetic comment -- index 1b34279..0000000

//Synthetic comment -- @@ -1,80 +0,0 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java
deleted file mode 100755
//Synthetic comment -- index 2f2c288..0000000

//Synthetic comment -- @@ -1,451 +0,0 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index e579336..8bdabe6 100755

//Synthetic comment -- @@ -29,7 +29,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.uicc.UiccController;

import android.app.AlarmManager;
import android.content.ContentResolver;
//Synthetic comment -- @@ -157,7 +159,7 @@
};

public CdmaServiceStateTracker(CDMAPhone phone) {
        super(phone.getContext(), phone.mCM);

this.phone = phone;
cr = phone.getContext().getContentResolver();
//Synthetic comment -- @@ -206,7 +208,7 @@
cm.unregisterForVoiceNetworkStateChanged(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
        if (mUiccApplcation != null) {mUiccApplcation.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -1698,24 +1700,25 @@
return;
}

        UiccCardApplication newUiccApplication =
                mUiccController.getUiccCardApplication(UiccController.APP_FAM_3GPP2);

        if (mUiccApplcation != newUiccApplication) {
            if (mUiccApplcation != null) {
log("Removing stale icc objects.");
                mUiccApplcation.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mUiccApplcation = null;
}
            if (newUiccApplication != null) {
log("New card found");
                mUiccApplcation = newUiccApplication;
                mIccRecords = mUiccApplcation.getIccRecords();
if (isSubscriptionFromRuim) {
                    mUiccApplcation.registerForReady(this, EVENT_RUIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 965db6d..3bf9c6e 100644

//Synthetic comment -- @@ -20,17 +20,8 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;

/**
* {@hide}
//Synthetic comment -- @@ -41,8 +32,8 @@
//***** Instance Variables

//***** Constructor
    public RuimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
}

protected void finalize() {
//Synthetic comment -- @@ -73,6 +64,11 @@
case EF_SMS:
case EF_CST:
case EF_RUIM_SPN:
        case EF_CSIM_LI:
        case EF_CSIM_MDN:
        case EF_CSIM_IMSIM:
        case EF_CSIM_CDMAHOME:
        case EF_CSIM_EPRL:
return MF_SIM + DF_CDMA;
}
return getCommonIccEFPath(efid);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 1cd0c68..b99011e 100755

//Synthetic comment -- @@ -16,8 +16,15 @@

package com.android.internal.telephony.cdma;


import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_TEST_CSIM;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -31,11 +38,12 @@
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.UiccCardApplication;

// can't be used since VoiceMailConstants is not public
//import com.android.internal.telephony.gsm.VoiceMailConstants;
//Synthetic comment -- @@ -43,6 +51,9 @@
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.IccRecords.IccRecordLoaded;
import com.android.internal.telephony.cdma.sms.UserData;


/**
//Synthetic comment -- @@ -61,10 +72,17 @@
private String mMin2Min1;

private String mPrlVersion;
    // From CSIM application
    private byte[] mEFpl = null;
    private byte[] mEFli = null;
    boolean mCsimSpnDisplayCondition = false;
    private String mMdn;
    private String mMin;
    private String mHomeSystemId;
    private String mHomeNetworkId;

// ***** Event Constants
    private static final int EVENT_APP_READY = 1;
private static final int EVENT_GET_IMSI_DONE = 3;
private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
private static final int EVENT_GET_ICCID_DONE = 5;
//Synthetic comment -- @@ -79,9 +97,8 @@

private static final int EVENT_RUIM_REFRESH = 31;

    public RuimRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        super(app, c, ci);

adnCache = new AdnRecordCache(mFh);

//Synthetic comment -- @@ -90,21 +107,22 @@
// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;

// NOTE the EVENT_SMS_ON_RUIM is not registered
mCi.registerForIccRefresh(this, EVENT_RUIM_REFRESH, null);

// Start off by setting empty state
        resetRecords();

        mParentApp.registerForReady(this, EVENT_APP_READY, null);
}

@Override
public void dispose() {
if (DBG) log("Disposing RuimRecords " + this);
//Unregister for all events
mCi.unregisterForIccRefresh(this);
        mParentApp.unregisterForReady(this);
        resetRecords();
super.dispose();
}

//Synthetic comment -- @@ -113,8 +131,7 @@
if(DBG) log("RuimRecords finalized");
}

    protected void resetRecords() {
countVoiceMessages = 0;
mncLength = UNINITIALIZED;
iccid = null;
//Synthetic comment -- @@ -133,6 +150,11 @@
recordsRequested = false;
}

    @Override
    public String getIMSI() {
        return mImsi;
    }

public String getMdnNumber() {
return mMyMobileNumber;
}
//Synthetic comment -- @@ -170,6 +192,15 @@
}
}

    private int adjstMinDigits (int digits) {
        // Per C.S0005 section 2.3.1.
        digits += 111;
        digits = (digits % 10 == 0)?(digits - 10):digits;
        digits = ((digits / 10) % 10 == 0)?(digits - 100):digits;
        digits = ((digits / 100) % 10 == 0)?(digits - 1000):digits;
        return digits;
    }

/**
* Returns the 5 or 6 digit MCC/MNC of the operator that
*  provided the RUIM card. Returns null of RUIM is not yet ready
//Synthetic comment -- @@ -192,6 +223,202 @@
return mImsi.substring(0, 3 + MccTable.smallestDigitsMccForMnc(mcc));
}

    // Refer to ETSI TS 102.221
    private class EfPlLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_PL";
        }

        public void onRecordLoaded(AsyncResult ar) {
            mEFpl = (byte[]) ar.result;
            if (DBG) log("EF_PL=" + IccUtils.bytesToHexString(mEFpl));
        }
    }

    // Refer to C.S0065 5.2.26
    private class EfCsimLiLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_LI";
        }

        public void onRecordLoaded(AsyncResult ar) {
            mEFli = (byte[]) ar.result;
            // convert csim efli data to iso 639 format
            for (int i = 0; i < mEFli.length; i+=2) {
                switch(mEFli[i+1]) {
                case 0x01: mEFli[i] = 'e'; mEFli[i+1] = 'n';break;
                case 0x02: mEFli[i] = 'f'; mEFli[i+1] = 'r';break;
                case 0x03: mEFli[i] = 'e'; mEFli[i+1] = 's';break;
                case 0x04: mEFli[i] = 'j'; mEFli[i+1] = 'a';break;
                case 0x05: mEFli[i] = 'k'; mEFli[i+1] = 'o';break;
                case 0x06: mEFli[i] = 'z'; mEFli[i+1] = 'h';break;
                case 0x07: mEFli[i] = 'h'; mEFli[i+1] = 'e';break;
                default: mEFli[i] = ' '; mEFli[i+1] = ' ';
                }
            }

            if (DBG) log("EF_LI=" + IccUtils.bytesToHexString(mEFli));
        }
    }

    // Refer to C.S0065 5.2.32
    private class EfCsimSpnLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_SPN";
        }

        public void onRecordLoaded(AsyncResult ar) {
            byte[] data = (byte[]) ar.result;
            if (DBG) log("CSIM_SPN=" +
                         IccUtils.bytesToHexString(data));

            // C.S0065 for EF_SPN decoding
            mCsimSpnDisplayCondition = ((0x01 & data[0]) != 0);

            int encoding = data[1];
            int language = data[2];
            byte[] spnData = new byte[32];
            System.arraycopy(data, 3, spnData, 0, (data.length < 32) ? data.length : 32);

            int numBytes;
            for (numBytes = 0; numBytes < spnData.length; numBytes++) {
                if ((spnData[numBytes] & 0xFF) == 0xFF) break;
            }

            if (numBytes == 0) {
                spn = "";
                return;
            }
            try {
                switch (encoding) {
                case UserData.ENCODING_OCTET:
                case UserData.ENCODING_LATIN:
                    spn = new String(spnData, 0, numBytes, "ISO-8859-1");
                    break;
                case UserData.ENCODING_IA5:
                case UserData.ENCODING_GSM_7BIT_ALPHABET:
                case UserData.ENCODING_7BIT_ASCII:
                    spn = GsmAlphabet.gsm7BitPackedToString(spnData, 0, (numBytes*8)/7);
                    break;
                case UserData.ENCODING_UNICODE_16:
                    spn =  new String(spnData, 0, numBytes, "utf-16");
                    break;
                default:
                    log("SPN encoding not supported");
                }
            } catch(Exception e) {
                log("spn decode error: " + e);
            }
            if (DBG) log("spn=" + spn);
            if (DBG) log("spnCondition=" + mCsimSpnDisplayCondition);
            SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);
        }
    }

    private class EfCsimMdnLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_MDN";
        }

        public void onRecordLoaded(AsyncResult ar) {
            byte[] data = (byte[]) ar.result;
            if (DBG) log("CSIM_MDN=" + IccUtils.bytesToHexString(data));
            int mdnDigitsNum = 0x0F & data[0];
            mMdn = IccUtils.cdmaBcdToString(data, 1, mdnDigitsNum);
            if (DBG) log("CSIM MDN=" + mMdn);
        }
    }

    private class EfCsimImsimLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_IMSIM";
        }

        public void onRecordLoaded(AsyncResult ar) {
            byte[] data = (byte[]) ar.result;
            if (DBG) log("CSIM_IMSIM=" + IccUtils.bytesToHexString(data));
            // C.S0065 section 5.2.2 for IMSI_M encoding
            // C.S0005 section 2.3.1 for MIN encoding in IMSI_M.
            boolean provisioned = ((data[7] & 0x80) == 0x80);

            if (provisioned) {
                int first3digits = ((0x03 & data[2]) << 8) + (0xFF & data[1]);
                int second3digits = (((0xFF & data[5]) << 8) | (0xFF & data[4])) >> 6;
                int digit7 = 0x0F & (data[4] >> 2);
                if (digit7 > 0x09) digit7 = 0;
                int last3digits = ((0x03 & data[4]) << 8) | (0xFF & data[3]);
                first3digits = adjstMinDigits(first3digits);
                second3digits = adjstMinDigits(second3digits);
                last3digits = adjstMinDigits(last3digits);

                StringBuilder builder = new StringBuilder();
                builder.append(String.format(Locale.US, "%03d", first3digits));
                builder.append(String.format(Locale.US, "%03d", second3digits));
                builder.append(String.format(Locale.US, "%d", digit7));
                builder.append(String.format(Locale.US, "%03d", last3digits));
                mMin = builder.toString();
                if (DBG) log("min present=" + mMin);
            } else {
                if (DBG) log("min not present");
            }
        }
    }

    private class EfCsimCdmaHomeLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_CDMAHOME";
        }

        public void onRecordLoaded(AsyncResult ar) {
            // Per C.S0065 section 5.2.8
            ArrayList<byte[]> dataList = (ArrayList<byte[]>) ar.result;
            if (DBG) log("CSIM_CDMAHOME data size=" + dataList.size());
            if (dataList.isEmpty()) {
                return;
            }
            StringBuilder sidBuf = new StringBuilder();
            StringBuilder nidBuf = new StringBuilder();

            for (byte[] data : dataList) {
                if (data.length == 5) {
                    int sid = ((data[1] & 0xFF) << 8) | (data[0] & 0xFF);
                    int nid = ((data[3] & 0xFF) << 8) | (data[2] & 0xFF);
                    sidBuf.append(sid).append(',');
                    nidBuf.append(nid).append(',');
                }
            }
            // remove trailing ","
            sidBuf.setLength(sidBuf.length()-1);
            nidBuf.setLength(nidBuf.length()-1);

            mHomeSystemId = sidBuf.toString();
            mHomeNetworkId = nidBuf.toString();
        }
    }

    private class EfCsimEprlLoaded implements IccRecordLoaded {
        public String getEfName() {
            return "EF_CSIM_EPRL";
        }
        public void onRecordLoaded(AsyncResult ar) {
            onGetCSimEprlDone(ar);
        }
    }

    private void onGetCSimEprlDone(AsyncResult ar) {
        // C.S0065 section 5.2.57 for EFeprl encoding
        // C.S0016 section 3.5.5 for PRL format.
        byte[] data = (byte[]) ar.result;
        if (DBG) log("CSIM_EPRL=" + IccUtils.bytesToHexString(data));

        // Only need the first 4 bytes of record
        if (data.length > 3) {
            int prlId = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
            mPrlVersion = Integer.toString(prlId);
        }
        if (DBG) log("CSIM PRL version=" + mPrlVersion);
    }

@Override
public void handleMessage(Message msg) {
AsyncResult ar;
//Synthetic comment -- @@ -207,9 +434,9 @@
}

try { switch (msg.what) {
            case EVENT_APP_READY:
                onReady();
                break;

case EVENT_GET_DEVICE_IDENTITY_DONE:
log("Event EVENT_GET_DEVICE_IDENTITY_DONE Received");
//Synthetic comment -- @@ -302,6 +529,9 @@
}
break;

            default:
                super.handleMessage(msg);   // IccRecords handles generic record load responses

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing RUIM record", exc);
//Synthetic comment -- @@ -313,6 +543,55 @@
}
}

    private String findBestLanguage(byte[] languages) {
        String bestMatch = null;
        String[] locales = mContext.getAssets().getLocales();

        if ((languages == null) || (locales == null)) return null;

        // Each 2-bytes consists of one language
        for (int i = 0; (i + 1) < languages.length; i += 2) {
            try {
                String lang = new String(languages, i, 2, "ISO-8859-1");
                for (int j = 0; j < locales.length; j++) {
                    if (locales[j] != null && locales[j].length() >= 2 &&
                        locales[j].substring(0, 2).equals(lang)) {
                        return lang;
                    }
                }
                if (bestMatch != null) break;
            } catch(java.io.UnsupportedEncodingException e) {
                log ("Failed to parse SIM language records");
            }
        }
        // no match found. return null
        return null;
    }

    private void setLocaleFromCsim() {
        String prefLang = null;
        // check EFli then EFpl
        prefLang = findBestLanguage(mEFli);

        if (prefLang == null) {
            prefLang = findBestLanguage(mEFpl);
        }

        if (prefLang != null) {
            // check country code from SIM
            String imsi = getIMSI();
            String country = null;
            if (imsi != null) {
                country = MccTable.countryCodeForMcc(
                                    Integer.parseInt(imsi.substring(0,3)));
            }
            log("Setting locale to " + prefLang + "_" + country);
            MccTable.setSystemLocale(mContext, prefLang, country);
        } else {
            log ("No suitable CSIM selected locale");
        }
    }

@Override
protected void onRecordLoaded() {
// One record loaded successfully or failed, In either case
//Synthetic comment -- @@ -343,6 +622,8 @@
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
MccTable.countryCodeForMcc(Integer.parseInt(mImsi.substring(0,3))));
}

        setLocaleFromCsim();
recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}
//Synthetic comment -- @@ -360,13 +641,43 @@

if (DBG) log("fetchRuimRecords " + recordsToLoad);

        mCi.getIMSIForApp(mParentApp.getAid(), obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(EF_PL,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfPlLoaded()));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_CSIM_LI,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimLiLoaded()));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_CSIM_SPN,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimSpnLoaded()));
        recordsToLoad++;

        mFh.loadEFLinearFixed(EF_CSIM_MDN, 1,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimMdnLoaded()));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_CSIM_IMSIM,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimImsimLoaded()));
        recordsToLoad++;

        mFh.loadEFLinearFixedAll(EF_CSIM_CDMAHOME,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimCdmaHomeLoaded()));
        recordsToLoad++;

        // Entire PRL could be huge. We are only interested in
        // the first 4 bytes of the record.
        mFh.loadEFTransparent(EF_CSIM_EPRL, 4,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimEprlLoaded()));
        recordsToLoad++;

if (DBG) log("fetchRuimRecords " + recordsToLoad + " requested: " + recordsRequested);
// Further records that can be inserted are Operator/OEM dependent
}
//Synthetic comment -- @@ -383,6 +694,29 @@
}

@Override
    public boolean isProvisioned() {
        // If UICC card has CSIM app, look for MDN and MIN field
        // to determine if the SIM is provisioned.  Otherwise,
        // consider the SIM is provisioned. (for case of ordinal
        // USIM only UICC.)
        // If PROPERTY_TEST_CSIM is defined, bypess provision check
        // and consider the SIM is provisioned.
        if (SystemProperties.getBoolean(PROPERTY_TEST_CSIM, false)) {
            return true;
        }

        if (mParentApp == null) {
            return false;
        }

        if (mParentApp.getType() == AppType.APPTYPE_CSIM &&
            ((mMdn == null) || (mMin == null))) {
            return false;
        }
        return true;
    }

    @Override
public void setVoiceMessageWaiting(int line, int countWaiting) {
if (line != 1) {
// only profile 1 is supported
//Synthetic comment -- @@ -409,7 +743,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentApp.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -443,6 +777,25 @@
}
}

    public String getMdn() {
        return mMdn;
    }

    public String getMin() {
        return mMin;
    }

    public String getSid() {
        return mHomeSystemId;
    }

    public String getNid() {
        return mHomeNetworkId;
    }

    public boolean getCsimSpnDisplayCondition() {
        return mCsimSpnDisplayCondition;
    }
@Override
protected void log(String s) {
Log.d(LOG_TAG, "[RuimRecords] " + s);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c0f9cc7..4d042bb 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import static com.android.internal.telephony.CommandsInterface.SERVICE_CLASS_VOICE;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_BASEBAND_VERSION;

import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
//Synthetic comment -- @@ -71,6 +72,7 @@
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -711,7 +713,8 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi =
                GsmMmiCode.newFromDialString(networkPortion, this, mUiccApplication.get());
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -730,7 +733,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, mUiccApplication.get());

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -743,7 +746,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this, mUiccApplication.get());
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -1073,6 +1076,10 @@
mDataConnectionTracker.setDataOnRoamingEnabled(enable);
}

    /*package*/ AppState getCurrentUiccState() {
        return super.getCurrentUiccStateP();
    }

/**
* Removes the given MMI from the pending list and notifies
* registrants that it is complete.
//Synthetic comment -- @@ -1141,7 +1148,7 @@
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
GSMPhone.this,
                                                   mUiccApplication.get());
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1341,23 +1348,24 @@
return;
}

        UiccCardApplication newUiccApplication = 
                mUiccController.getUiccCardApplication(UiccController.APP_FAM_3GPP);

        UiccCardApplication app = mUiccApplication.get();
        if (app != newUiccApplication) {
            if (app != null) {
if (LOCAL_DEBUG) log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForSimRecordEvents();
mSimPhoneBookIntManager.updateIccRecords(null);
}
mIccRecords.set(null);
                mUiccApplication.set(null);
}
            if (newUiccApplication != null) {
                if (LOCAL_DEBUG) log("New Uicc application found");
                mUiccApplication.set(newUiccApplication);
                mIccRecords.set(newUiccApplication.getIccRecords());
registerForSimRecordEvents();
mSimPhoneBookIntManager.updateIccRecords(mIccRecords.get());
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 9fc94a5..2572406 100644

//Synthetic comment -- @@ -376,7 +376,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getCurrentUiccState() !=
                            IccCardApplicationStatus.AppState.APPSTATE_READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.mRestrictedState.isCsRestricted()) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 80e0320..58de5f8 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -2632,11 +2633,7 @@
return;
}

        IccRecords newIccRecords = mUiccController.getIccRecords(UiccController.APP_FAM_3GPP);

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -2646,7 +2643,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New records found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index b725c69..73d44eb 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Context;
import com.android.internal.telephony.*;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;

import android.os.*;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -110,7 +111,7 @@

GSMPhone phone;
Context context;
    UiccCardApplication mUiccApplication;
IccRecords mIccRecords;

String action;              // One of ACTION_*
//Synthetic comment -- @@ -175,7 +176,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone, UiccCardApplication app) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -183,7 +184,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone, app);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -198,14 +199,14 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone, app);
ret.poundString = dialString;
} else if (isTwoDigitShortCode(phone.getContext(), dialString)) {
//Is a country-specific exception to short codes as defined in TS 22.030, 6.5.3.2
ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone, app);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -214,10 +215,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone, UiccCardApplication app) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone, app);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -233,8 +234,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone, UiccCardApplication app) {
        GsmMmiCode ret = new GsmMmiCode(phone, app);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -385,15 +386,15 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone, UiccCardApplication app) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
        mUiccApplication = app;
        if (app != null) {
            mIccRecords = app.getIccRecords();
}
}

//Synthetic comment -- @@ -771,7 +772,7 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else if (sc.equals(SC_PIN) &&
                               mUiccApplication.getState() == AppState.APPSTATE_PUK ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 13f637a..bc997aa 100644

//Synthetic comment -- @@ -31,6 +31,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.UiccController;

import android.app.AlarmManager;
import android.app.Notification;
//Synthetic comment -- @@ -188,7 +191,7 @@
};

public GsmServiceStateTracker(GSMPhone phone) {
        super(phone.getContext(), phone.mCM);

this.phone = phone;
ss = new ServiceState();
//Synthetic comment -- @@ -240,7 +243,7 @@
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        if (mUiccApplcation != null) {mUiccApplcation.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
//Synthetic comment -- @@ -1137,7 +1140,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (mUiccApplcation != null && mUiccApplcation.getState() == AppState.APPSTATE_READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1669,23 +1672,24 @@
return;
}

        UiccCardApplication newUiccApplication =
                mUiccController.getUiccCardApplication(UiccController.APP_FAM_3GPP);

        if (mUiccApplcation != newUiccApplication) {
            if (mUiccApplcation != null) {
log("Removing stale icc objects.");
                mUiccApplcation.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mUiccApplcation = null;
}
            if (newUiccApplication != null) {
log("New card found");
                mUiccApplcation = newUiccApplication;
                mIccRecords = mUiccApplcation.getIccRecords();
                mUiccApplcation.registerForReady(this, EVENT_SIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0637e8f..46992e6 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;

/**
* {@hide}
//Synthetic comment -- @@ -36,8 +34,8 @@

//***** Constructor

    public SIMFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
}

protected void finalize() {
//Synthetic comment -- @@ -78,20 +76,9 @@
case EF_INFO_CPHS:
case EF_CSP_CPHS:
return MF_SIM + DF_GSM;
}
String path = getCommonIccEFPath(efid);
if (path == null) {
Log.e(LOG_TAG, "Error: EF Path being returned in null");
}
return path;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 4a9b047..983f3c1 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.UiccCardApplication;

import java.util.ArrayList;

//Synthetic comment -- @@ -124,9 +124,9 @@

// ***** Event Constants

    private static final int EVENT_APP_READY = 1;
    private static final int EVENT_GET_IMSI_DONE = 3;
    private static final int EVENT_GET_ICCID_DONE = 4;
private static final int EVENT_GET_MBI_DONE = 5;
private static final int EVENT_GET_MBDN_DONE = 6;
private static final int EVENT_GET_MWIS_DONE = 7;
//Synthetic comment -- @@ -175,8 +175,8 @@

// ***** Constructor

    public SIMRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        super(app, c, ci);

adnCache = new AdnRecordCache(mFh);

//Synthetic comment -- @@ -188,23 +188,22 @@
// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;

mCi.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
mCi.registerForIccRefresh(this, EVENT_SIM_REFRESH, null);

// Start off by setting empty state
        resetRecords();
        mParentApp.registerForReady(this, EVENT_APP_READY, null);
}

@Override
public void dispose() {
if (DBG) log("Disposing SIMRecords " + this);
//Unregister for all events
mCi.unregisterForIccRefresh(this);
mCi.unSetOnSmsOnSim(this);
        mParentApp.unregisterForReady(this);
        resetRecords();
super.dispose();
}

//Synthetic comment -- @@ -212,7 +211,7 @@
if(DBG) log("finalized");
}

    protected void resetRecords() {
imsi = null;
msisdn = null;
voiceMailNum = null;
//Synthetic comment -- @@ -528,9 +527,9 @@
}

try { switch (msg.what) {
            case EVENT_APP_READY:
                onReady();
                break;

/* IO events */
case EVENT_GET_IMSI_DONE:
//Synthetic comment -- @@ -1139,7 +1138,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentApp.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -1304,7 +1303,7 @@

if (DBG) log("fetchSimRecords " + recordsToLoad);

        mCi.getIMSIForApp(mParentApp.getAid(), obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimFileHandler.java b/src/java/com/android/internal/telephony/ims/IsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..a2b0c67

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2006, 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony.ims;

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;

/**
 * {@hide}
 * This class should be used to access files in ISIM ADF
 */
public final class IsimFileHandler extends IccFileHandler implements IccConstants {
    static final String LOG_TAG = "RIL_IsimFH";

    public IsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
    }

    protected void finalize() {
        logd("IsimFileHandler finalized");
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_IMPI:
        case EF_IMPU:
        case EF_DOMAIN:
            return MF_SIM + DF_ADF;
        }
        String path = getCommonIccEFPath(efid);
        return path;
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java b/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java
//Synthetic comment -- index ee1a42d..e6d9c7c 100644

//Synthetic comment -- @@ -16,13 +16,21 @@

package com.android.internal.telephony.ims;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.gsm.SpnOverride;
//import com.android.internal.telephony.gsm.VoiceMailConstants;

import java.nio.charset.Charset;
import java.util.ArrayList;
//Synthetic comment -- @@ -34,12 +42,14 @@
/**
* {@hide}
*/
public final class IsimUiccRecords extends IccRecords implements IsimRecords {
protected static final String LOG_TAG = "GSM";

private static final boolean DBG = true;
private static final boolean DUMP_RECORDS = false;   // Note: PII is logged when this is true

    private static final int EVENT_APP_READY = 1;

// ISIM EF records (see 3GPP TS 31.103)
private String mIsimImpi;               // IMS private user identity
private String mIsimDomain;             // IMS home network domain name
//Synthetic comment -- @@ -47,6 +57,75 @@

private static final int TAG_ISIM_VALUE = 0x80;     // From 3GPP TS 31.103

    public IsimUiccRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        super(app, c, ci);

        recordsRequested = false;  // No load request is made till SIM ready

        // recordsToLoad is set to 0 because no requests are made yet
        recordsToLoad = 0;

        mParentApp.registerForReady(this, EVENT_APP_READY, null);
    }

    @Override
    public void dispose() {
        log("Disposing " + this);
        //Unregister for all events
        mParentApp.unregisterForReady(this);
        resetRecords();
        super.dispose();
    }

    // ***** Overridden from Handler
    public void handleMessage(Message msg) {
        if (mDestroyed.get()) {
            Log.e(LOG_TAG, "Received message " + msg +
                    "[" + msg.what + "] while being destroyed. Ignoring.");
            return;
        }

        try {
            switch (msg.what) {
                case EVENT_APP_READY:
                    onReady();
                    break;

                default:
                    super.handleMessage(msg);   // IccRecords handles generic record load responses

            }
        } catch (RuntimeException exc) {
            // I don't want these exceptions to be fatal
            Log.w(LOG_TAG, "Exception parsing SIM record", exc);
        }
    }

    protected void fetchIsimRecords() {
        recordsRequested = true;

        mFh.loadEFTransparent(EF_IMPI, obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimImpiLoaded()));
        recordsToLoad++;

        mFh.loadEFLinearFixedAll(EF_IMPU, obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimImpuLoaded()));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_DOMAIN, obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimDomainLoaded()));
        recordsToLoad++;

        log("fetchIsimRecords " + recordsToLoad);
    }

    protected void resetRecords() {
        // recordsRequested is set to false indicating that the SIM
        // read requests made so far are not valid. This is set to
        // true only when fresh set of read requests are made.
        recordsRequested = false;
    }

private class EfIsimImpiLoaded implements IccRecords.IccRecordLoaded {
public String getEfName() {
return "EF_ISIM_IMPI";
//Synthetic comment -- @@ -87,22 +166,6 @@
}

/**
* ISIM records for IMS are stored inside a Tag-Length-Value record as a UTF-8 string
* with tag value 0x80.
* @param record the byte array containing the IMS data string
//Synthetic comment -- @@ -120,11 +183,31 @@
return null;
}

    @Override
    protected void onRecordLoaded() {
        // One record loaded successfully or failed, In either case
        // we need to update the recordsToLoad count
        recordsToLoad -= 1;

        if (recordsToLoad == 0 && recordsRequested == true) {
            onAllRecordsLoaded();
        } else if (recordsToLoad < 0) {
            loge("recordsToLoad <0, programmer error suspected");
            recordsToLoad = 0;
        }
    }

    @Override
    protected void onAllRecordsLoaded() {
        recordsLoadedRegistrants.notifyRegistrants(
                new AsyncResult(null, null, null));
    }

    protected void log(String s) {
if (DBG) Log.d(LOG_TAG, "[ISIM] " + s);
}

    protected void loge(String s) {
if (DBG) Log.e(LOG_TAG, "[ISIM] " + s);
}

//Synthetic comment -- @@ -154,4 +237,31 @@
public String[] getIsimImpu() {
return (mIsimImpu != null) ? mIsimImpu.clone() : null;
}

	@Override
	public int getDisplayRule(String plmn) {
		// Not applicable to Isim
		return 0;
	}

	@Override
	public void onReady() {
        fetchIsimRecords();
	}

	@Override
	public void onRefresh(boolean fileChanged, int[] fileList) {
		// We do not handle it in Isim
	}

	@Override
	public void setVoiceMailNumber(String alphaTag, String voiceNumber,
			Message onComplete) {
		// Not applicable to Isim
	}

	@Override
	public void setVoiceMessageWaiting(int line, int countWaiting) {
		// Not applicable to Isim
	}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 81f20bc..05d38b5 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2011-2012 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,13 +16,7 @@

package com.android.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -30,6 +24,13 @@
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;

/* This class is responsible for keeping all knowledge about
* ICCs in the system. It is also used as API to get appropriate
* applications to pass them to phone and service trackers.
//Synthetic comment -- @@ -40,37 +41,68 @@

public static final int APP_FAM_3GPP =  1;
public static final int APP_FAM_3GPP2 = 2;
    public static final int APP_FAM_IMS   = 3;

private static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;

private static UiccController mInstance;

    private Context mContext;
private CommandsInterface mCi;
private UiccCard mUiccCard;

private RegistrantList mIccChangedRegistrants = new RegistrantList();

    public static synchronized UiccController make(Context c, CommandsInterface ci) {
        if (mInstance != null) {
            throw new RuntimeException("UiccController.make() should only be called once");
        }
        mInstance = new UiccController(c, ci);
        return mInstance;
    }

    public static synchronized UiccController getInstance() {
if (mInstance == null) {
            throw new RuntimeException("UiccController.getInstance can't be called before make()");
}
return mInstance;
}

public synchronized UiccCard getUiccCard() {
return mUiccCard;
}

    // Easy to use API
    public UiccCardApplication getUiccCardApplication(int family) {
        if (mUiccCard != null) {
            return mUiccCard.getApplication(family);
        }
        return null;
    }

    // Easy to use API
    public IccRecords getIccRecords(int family) {
        if (mUiccCard != null) {
            UiccCardApplication app = mUiccCard.getApplication(family);
            if (app != null) {
                return app.getIccRecords();
            }
        }
        return null;
    }

    // Easy to use API
    public IccFileHandler getIccFileHandler(int family) {
        if (mUiccCard != null) {
            UiccCardApplication app = mUiccCard.getApplication(family);
            if (app != null) {
                return app.getIccFileHandler();
            }
        }
        return null;
    }

//Notifies when card status changes
public void registerForIccChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
//Synthetic comment -- @@ -100,9 +132,13 @@
}
}

    private UiccController(Context c, CommandsInterface ci) {
if (DBG) log("Creating UiccController");
        mContext = c;
        mCi = ci;
        mCi.registerForIccStatusChanged(this, EVENT_ICC_STATUS_CHANGED, null);
        // TODO remove this once modem correctly notifies the unsols
        mCi.registerForOn(this, EVENT_ICC_STATUS_CHANGED, null);
}

private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
//Synthetic comment -- @@ -115,55 +151,18 @@

IccCardStatus status = (IccCardStatus)ar.result;

        if (mUiccCard == null) {
            //Create new card
            mUiccCard = new UiccCard(mContext, mCi, status);
        } else {
            //Update already existing card
            mUiccCard.update(mContext, mCi , status);
}

if (DBG) log("Notifying IccChangedRegistrants");
mIccChangedRegistrants.notifyRegistrants();
}

private void log(String string) {
Log.d(LOG_TAG, string);
}







