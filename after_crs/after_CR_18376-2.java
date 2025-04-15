/*Separate SIM states from RADIO states

Radio state reflects the state of the modem. SIM_READY, RUIM_READY,
NV_READY are subscription states and it is possible that the new cards
have multiple subscriptions. Remove the SIM states from Radio State and
introduce a new VOICE_RADIO_TECH message to identify the exact voice
technology. SIM states will continue to be identified from the
SIM_STATUS messages.

Change-Id:Ie7f9e43bba498ea7b05dc3c2fdf2ec270ab47bbf*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/BaseCommands.java b/telephony/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 815fbfb4..7e4b859 100644

//Synthetic comment -- @@ -41,15 +41,12 @@
protected RegistrantList mAvailRegistrants = new RegistrantList();
protected RegistrantList mOffOrNotAvailRegistrants = new RegistrantList();
protected RegistrantList mNotAvailRegistrants = new RegistrantList();
protected RegistrantList mCallStateRegistrants = new RegistrantList();
protected RegistrantList mNetworkStateRegistrants = new RegistrantList();
protected RegistrantList mDataConnectionRegistrants = new RegistrantList();
    protected RegistrantList mVoiceRadioTechChangedRegistrants = new RegistrantList();
    protected RegistrantList mCdmaSubscriptionSourceChangedRegistrants = new RegistrantList();
    protected RegistrantList mCdmaPrlChangedRegistrants = new RegistrantList();
protected RegistrantList mIccStatusChangedRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOnRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOffRegistrants = new RegistrantList();
//Synthetic comment -- @@ -118,6 +115,33 @@
}
}

    public void registerForVoiceRadioTechChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mVoiceRadioTechChangedRegistrants.add(r);
    }

    public void unregisterForVoiceRadioTechChanged(Handler h) {
        mVoiceRadioTechChangedRegistrants.remove(h);
    }

    public void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCdmaSubscriptionSourceChangedRegistrants.add(r);
    }

    public void unregisterForCdmaSubscriptionSourceChanged(Handler h) {
        mCdmaSubscriptionSourceChangedRegistrants.remove(h);
    }

    public void registerForCdmaPrlChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCdmaPrlChangedRegistrants.add(r);
    }

    public void unregisterForCdmaPrlChanged(Handler h) {
        mCdmaPrlChangedRegistrants.remove(h);
    }

public void registerForOn(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);

//Synthetic comment -- @@ -189,100 +213,6 @@
}
}

public void registerForCallStateChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);

//Synthetic comment -- @@ -313,15 +243,6 @@
mDataConnectionRegistrants.remove(h);
}

public void registerForIccStatusChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mIccStatusChangedRegistrants.add(r);
//Synthetic comment -- @@ -595,8 +516,7 @@
* This function is called only by RIL.java when receiving unsolicited
* RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED
*
     * RadioState has 3 values : RADIO_OFF, RADIO_UNAVAILABLE, RADIO_ON.
*
* @param newState new RadioState decoded from RIL_UNSOL_RADIO_STATE_CHANGED
*/
//Synthetic comment -- @@ -630,30 +550,6 @@
mNotAvailRegistrants.notifyRegistrants();
}

if (mState.isOn() && !oldState.isOn()) {
Log.d(LOG_TAG,"Notifying: Radio On");
mOnRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -665,33 +561,6 @@
Log.d(LOG_TAG,"Notifying: radio off or not available");
mOffOrNotAvailRegistrants.notifyRegistrants();
}
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..2fbff63 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import android.os.Message;
import android.os.Handler;
import android.util.Log;


/**
//Synthetic comment -- @@ -29,56 +30,91 @@
enum RadioState {
RADIO_OFF,         /* Radio explictly powered off (eg CFUN=0) */
RADIO_UNAVAILABLE, /* Radio unavailable (eg, resetting or not booted) */
        RADIO_ON;          /* Radio is */

public boolean isOn() /* and available...*/ {
            return this == RADIO_ON;
}

public boolean isAvailable() {
return this != RADIO_UNAVAILABLE;
}
    }

    public enum RadioTechnologyFamily {
        RADIO_TECH_UNKNOWN,     /* Indicate that RIL is not initialized */
        RADIO_TECH_3GPP,        /* 3GPP Technologies - GSM, WCDMA, LTE */
        RADIO_TECH_3GPP2;       /* 3GPP2 Technologies - CDMA, EVDO */

        public boolean isUnknown() {
            return this == RADIO_TECH_UNKNOWN;
}

public boolean isGsm() {
            return this == RADIO_TECH_3GPP;
}

public boolean isCdma() {
            return this == RADIO_TECH_3GPP2;
        }

        public static RadioTechnologyFamily getRadioTechFamilyFromInt(int techInt) {
            RadioTechnologyFamily ret = RADIO_TECH_UNKNOWN;
            try {
                ret = values()[techInt];
            } catch (IndexOutOfBoundsException e) {
                Log.e("RIL", "Invalid radio technology family : " + techInt);
            }
            return ret;
        }
    }

    public enum RadioTechnology {
        RADIO_TECH_UNKNOWN,
        RADIO_TECH_GPRS,
        RADIO_TECH_EDGE,
        RADIO_TECH_UMTS,
        RADIO_TECH_IS95A,
        RADIO_TECH_IS95B,
        RADIO_TECH_1xRTT,
        RADIO_TECH_EVDO_0,
        RADIO_TECH_EVDO_A,
        RADIO_TECH_HSDPA,
        RADIO_TECH_HSUPA,
        RADIO_TECH_HSPA,
        RADIO_TECH_EVDO_B,
        RADIO_TECH_EHRPD,
        RADIO_TECH_LTE;

        public boolean isUnknown() {
            return this == RADIO_TECH_UNKNOWN;
        }

        public boolean isGsm() {
            return this == RADIO_TECH_GPRS || this == RADIO_TECH_EDGE || this == RADIO_TECH_UMTS
                    || this == RADIO_TECH_HSDPA || this == RADIO_TECH_HSUPA
                    || this == RADIO_TECH_HSPA || this == RADIO_TECH_LTE;
        }

        public boolean isCdma() {
            return this == RADIO_TECH_IS95A || this == RADIO_TECH_IS95B || this == RADIO_TECH_1xRTT
                    || this == RADIO_TECH_EVDO_0 || this == RADIO_TECH_EVDO_A
                    || this == RADIO_TECH_EVDO_B || this == RADIO_TECH_EHRPD;
        }

        public boolean isEvdo() {
            return this == RADIO_TECH_EVDO_0 || this == RADIO_TECH_EVDO_A
                    || this == RADIO_TECH_EVDO_B;
        }

        public static RadioTechnology getRadioTechFromInt(int techInt) {
            RadioTechnology rt = RADIO_TECH_UNKNOWN;
            try {
                rt = values()[techInt];
            } catch (IndexOutOfBoundsException e) {
                Log.e("RIL", "Invalid radio technology : " + techInt);
            }
            return rt;
}
}

//Synthetic comment -- @@ -154,6 +190,10 @@

RadioState getRadioState();

    void getVoiceRadioTechnology(Message result);
    void getCdmaSubscriptionSource(Message result);
    void getCdmaPrlVersion(Message result);

/**
* Fires on any RadioState transition
* Always fires immediately as well
//Synthetic comment -- @@ -165,6 +205,15 @@
void registerForRadioStateChanged(Handler h, int what, Object obj);
void unregisterForRadioStateChanged(Handler h);

    void registerForVoiceRadioTechChanged(Handler h, int what, Object obj);
    void unregisterForVoiceRadioTechChanged(Handler h);

    void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj);
    void unregisterForCdmaSubscriptionSourceChanged(Handler h);

    void registerForCdmaPrlChanged(Handler h, int what, Object obj);
    void unregisterForCdmaPrlChanged(Handler h);

/**
* Fires on any transition into RadioState.isOn()
* Fires immediately if currently in that state
//Synthetic comment -- @@ -201,19 +250,6 @@
void registerForOffOrNotAvailable(Handler h, int what, Object obj);
void unregisterForOffOrNotAvailable(Handler h);

void registerForCallStateChanged(Handler h, int what, Object obj);
void unregisterForCallStateChanged(Handler h);
void registerForNetworkStateChanged(Handler h, int what, Object obj);
//Synthetic comment -- @@ -221,13 +257,6 @@
void registerForDataStateChanged(Handler h, int what, Object obj);
void unregisterForDataStateChanged(Handler h);

/** InCall voice privacy notifications */
void registerForInCallVoicePrivacyOn(Handler h, int what, Object obj);
void unregisterForInCallVoicePrivacyOn(Handler h);
//Synthetic comment -- @@ -235,13 +264,10 @@
void unregisterForInCallVoicePrivacyOff(Handler h);

/**
     * Fires on any change in ICC status
*/
    void registerForIccStatusChanged(Handler h, int what, Object obj);
    void unregisterForIccStatusChanged(Handler h);

/**
* unlike the register* methods, there's only one new SMS handler








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 3b9e6cc..3c2b871 100644

//Synthetic comment -- @@ -93,7 +93,7 @@
protected static final int EVENT_START_RECOVERY = 28;
protected static final int EVENT_APN_CHANGED = 29;
protected static final int EVENT_CDMA_DATA_DETACHED = 30;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 31;
protected static final int EVENT_PS_RESTRICT_ENABLED = 32;
protected static final int EVENT_PS_RESTRICT_DISABLED = 33;
public static final int EVENT_CLEAN_UP_CONNECTION = 34;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 90f9e8c..83c3221 100644

//Synthetic comment -- @@ -38,10 +38,15 @@

private IccCardStatus mIccCardStatus = null;
protected State mState = null;
    protected Object mStateMonitor = new Object();

    protected boolean is3gpp = true;
    protected int mCdmaSubscriptionSource = RILConstants.SUBSCRIPTION_FROM_RUIM;
protected PhoneBase mPhone;
private RegistrantList mAbsentRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    protected RegistrantList mReadyRegistrants = new RegistrantList();

private boolean mDesiredPinLocked;
private boolean mDesiredFdnEnabled;
//Synthetic comment -- @@ -73,7 +78,7 @@
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";

    protected static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
private static final int EVENT_PINPUK_DONE = 4;
//Synthetic comment -- @@ -84,9 +89,12 @@
private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;
    protected static final int EVENT_RADIO_ON = 12;
    private static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE = 13;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 14;

/*
      UNKNOWN is a transient state, for example, after user inputs ICC pin under
PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
turns to READY
*/
//Synthetic comment -- @@ -113,25 +121,16 @@
*/
case RADIO_OFF:
case RADIO_UNAVAILABLE:
return State.UNKNOWN;
                default:
                    if(!is3gpp && (mCdmaSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_NV)) {
                        return State.READY;
                    }
}
} else {
return mState;
}

return State.UNKNOWN;
}

//Synthetic comment -- @@ -198,6 +197,23 @@
mPinLockedRegistrants.remove(h);
}

    public void registerForReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mReadyRegistrants.add(r);

            if (getState() == State.READY) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForReady(Handler h) {
        synchronized (mStateMonitor) {
            mReadyRegistrants.remove(h);
        }
    }

/**
* Supply the ICC PIN to the ICC
//Synthetic comment -- @@ -399,7 +415,13 @@
oldState = mState;
mIccCardStatus = newCardStatus;
newState = getIccCardState();

        synchronized (mStateMonitor) {
            mState = newState;
            if (oldState != State.READY && newState == State.READY) {
                mReadyRegistrants.notifyRegistrants();
            }
        }

updateStateProperty();

//Synthetic comment -- @@ -493,9 +515,19 @@
updateStateProperty();
broadcastIccStateChangedIntent(INTENT_VALUE_ICC_NOT_READY, null);
break;
                case EVENT_RADIO_ON:
                    mPhone.mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
                    break;
                case EVENT_ICC_STATUS_CHANGED:
                    if (mPhone.mCM.getRadioState() == RadioState.RADIO_ON) {
                        Log.d(mLogTag, "Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                        mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE, msg.obj));
                    } else {
                        Log.d(mLogTag, "Received EVENT_ICC_STATUS_CHANGED while radio is not ON. Ignoring");
                    }
                    break;
                case EVENT_ICC_READY:
mPhone.mCM.queryFacilityLock (
CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX,
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
//Synthetic comment -- @@ -503,12 +535,6 @@
CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
break;
case EVENT_GET_ICC_STATUS_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -582,6 +608,22 @@
= ar.exception;
((Message)ar.userObj).sendToTarget();
break;
                case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE:
                    ar = (AsyncResult)msg.obj;
                    if((ar.exception == null) && (ar.result != null)) {
                        int newCdmaSubscriptionSource = ((int[]) ar.result)[0];
                        Log.d(mLogTag, "Received Cdma subscription source: " +
                                newCdmaSubscriptionSource);
                        if (newCdmaSubscriptionSource != mCdmaSubscriptionSource) {
                            mCdmaSubscriptionSource = newCdmaSubscriptionSource;
                            // Parse the Stored IccCardStatus Message to set mState correctly.
                            handleIccCardStatus(mIccCardStatus);
                        }
                    }
                    break;
                case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                    mPhone.mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
                    break;
default:
Log.e(mLogTag, "[IccCard] Unknown Event " + msg.what);
}
//Synthetic comment -- @@ -589,6 +631,10 @@
};

public State getIccCardState() {
        if(!is3gpp && (mCdmaSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_NV)) {
            return State.READY;
        }

if (mIccCardStatus == null) {
Log.e(mLogTag, "[IccCard] IccCardStatus is null");
return IccCard.State.ABSENT;
//Synthetic comment -- @@ -602,27 +648,18 @@
RadioState currentRadioState = mPhone.mCM.getRadioState();
// check radio technology
if( currentRadioState == RadioState.RADIO_OFF         ||
            currentRadioState == RadioState.RADIO_UNAVAILABLE) {
return IccCard.State.NOT_READY;
}

        if( currentRadioState == RadioState.RADIO_ON ) {

int index;

// check for CDMA radio technology
            if (!is3gpp) {
index = mIccCardStatus.getCdmaSubscriptionAppIndex();
            } else {
index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
}

//Synthetic comment -- @@ -635,15 +672,18 @@

// check if PIN required
if (app.app_state.isPinRequired()) {
                mIccPinLocked = true;
return IccCard.State.PIN_REQUIRED;
}
if (app.app_state.isPukRequired()) {
                mIccPinLocked = true;
return IccCard.State.PUK_REQUIRED;
}
if (app.app_state.isSubscriptionPersoEnabled()) {
return IccCard.State.NETWORK_LOCKED;
}
if (app.app_state.isAppReady()) {
                mHandler.sendMessage(mHandler.obtainMessage(EVENT_ICC_READY));
return IccCard.State.READY;
}
if (app.app_state.isAppNotReady()) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..0381bd1 100644

//Synthetic comment -- @@ -99,6 +99,8 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    protected static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE   = 27;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED= 28;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 803b736..8a14618 100644

//Synthetic comment -- @@ -135,15 +135,19 @@
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
        case RILConstants.NETWORK_MODE_CDMA_AND_LTE_EVDO:
return Phone.PHONE_TYPE_CDMA;

case RILConstants.NETWORK_MODE_WCDMA_PREF:
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
        case RILConstants.NETWORK_MODE_GSM_WCDMA_LTE:
return Phone.PHONE_TYPE_GSM;

case RILConstants.NETWORK_MODE_GLOBAL:
        case RILConstants.NETWORK_MODE_GLOBAL_LTE:
        case RILConstants.NETWORK_MODE_LTE_ONLY:
return Phone.PHONE_TYPE_CDMA;
default:
return Phone.PHONE_TYPE_GSM;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 5e7dcb0..6e97a77 100644

//Synthetic comment -- @@ -20,21 +20,19 @@
import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface.RadioTechnologyFamily;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.test.SimulatedRadioControl;

import java.util.List;
//Synthetic comment -- @@ -43,15 +41,18 @@
public final static Object lockForRadioTechnologyChange = new Object();

private Phone mActivePhone;
    private CommandsInterface mCi;
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;

private boolean mResetModemOnRadioTechnologyChange = false;
    private int mVoiceTechQueryContext = 0;

    private static final int EVENT_VOICE_RADIO_TECHNOLOGY_CHANGED = 1;
    private static final int EVENT_RADIO_ON = 2;
    private static final int EVENT_REQUEST_VOICE_RADIO_TECH_DONE = 3;

private static final String LOG_TAG = "PHONE";

//***** Class Methods
//Synthetic comment -- @@ -64,84 +65,42 @@
mIccPhoneBookInterfaceManagerProxy = new IccPhoneBookInterfaceManagerProxy(
phone.getIccPhoneBookInterfaceManager());
mPhoneSubInfoProxy = new PhoneSubInfoProxy(phone.getPhoneSubInfo());
        mCi = ((PhoneBase)mActivePhone).mCM;

        mCi.registerForOn(this, EVENT_RADIO_ON, null);
        mCi.registerForVoiceRadioTechChanged(this, EVENT_VOICE_RADIO_TECHNOLOGY_CHANGED, null);
}

@Override
public void handleMessage(Message msg) {
switch(msg.what) {

        case EVENT_RADIO_ON:
        case EVENT_VOICE_RADIO_TECHNOLOGY_CHANGED:
            /* Proactively query voice radio technologies */
            mVoiceTechQueryContext++;
            mCi.getVoiceRadioTechnology(this.obtainMessage(
                            EVENT_REQUEST_VOICE_RADIO_TECH_DONE, mVoiceTechQueryContext));
break;

        case EVENT_REQUEST_VOICE_RADIO_TECH_DONE:
                AsyncResult ar = (AsyncResult) msg.obj;

                if ((Integer)ar.userObj != mVoiceTechQueryContext) return;

                if (ar.exception == null) {
                    RadioTechnologyFamily newVoiceTech =
                            RadioTechnologyFamily.getRadioTechFamilyFromInt(((int[]) ar.result)[0]);
                    updatePhoneObject(newVoiceTech);
                } else {
                    loge("Voice Radio Technology query failed!");
                }
            break;

default:
Log.e(LOG_TAG,"Error! This handler was not registered for this message type. Message: "
+ msg.what);
            break;
}
super.handleMessage(msg);
}
//Synthetic comment -- @@ -162,6 +121,102 @@
Log.e(LOG_TAG, "[PhoneProxy] " + msg);
}

    private void updatePhoneObject(RadioTechnologyFamily newVoiceRadioTech) {

        if (mActivePhone != null
                && ((newVoiceRadioTech.isCdma() && mActivePhone.getPhoneType() == PHONE_TYPE_CDMA))
                || ((newVoiceRadioTech.isGsm() && mActivePhone.getPhoneType() == PHONE_TYPE_GSM))) {
            // Nothing changed. Keep phone as it is.
            Log.v(LOG_TAG, "Ignoring voice radio technology changed message. newVoiceRadioTech = "
                    + newVoiceRadioTech + "Active Phone = " + mActivePhone.getPhoneName());
            return;
        }

        if (newVoiceRadioTech.isUnknown()) {
            // We need some voice phone object to be active always, so never
            // delete the phone without anything to replace it with!
            Log.i(LOG_TAG,
                    "Ignoring voice radio technology changed message. newVoiceRadioTech = Unknown."
                            + "Active Phone = " + mActivePhone.getPhoneName());
            return;
        }

        boolean oldPowerState = false; // old power state to off
        if (mResetModemOnRadioTechnologyChange) {
            if (mCi.getRadioState().isOn()) {
                oldPowerState = true;
                logd("Setting Radio Power to Off");
                mCi.setRadioPower(false, null);
            }
        }

        deleteAndCreatePhone(newVoiceRadioTech);

        if (mResetModemOnRadioTechnologyChange) { // restore power state
            logd("Resetting Radio");
            mCi.setRadioPower(oldPowerState, null);
        }

        // Set the new interfaces in the proxy's
        mIccSmsInterfaceManagerProxy.setmIccSmsInterfaceManager(
                mActivePhone.getIccSmsInterfaceManager());
        mIccPhoneBookInterfaceManagerProxy.setmIccPhoneBookInterfaceManager(mActivePhone
                .getIccPhoneBookInterfaceManager());
        mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());

        mCi = ((PhoneBase)mActivePhone).mCM;

        // Send an Intent to the PhoneApp that we had a radio technology change
        Intent intent = new Intent(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(Phone.PHONE_NAME_KEY, mActivePhone.getPhoneName());
        ActivityManagerNative.broadcastStickyIntent(intent, null);

    }

    private void deleteAndCreatePhone(RadioTechnologyFamily newVoiceRadioTech) {

        String mOutgoingPhoneName = "Unknown";

        if (mActivePhone != null) {
            mOutgoingPhoneName = ((PhoneBase) mActivePhone).getPhoneName();
        }

        Log.i(LOG_TAG, "Switching Voice Phone : " + mOutgoingPhoneName + " >>> "
                + (newVoiceRadioTech.isGsm() ? "GSM" : "CDMA"));

        if (mActivePhone != null) {
            Log.v(LOG_TAG, "Disposing old phone..");
            if (mActivePhone instanceof GSMPhone) {
                ((GSMPhone) mActivePhone).dispose();
            } else if (mActivePhone instanceof CDMAPhone) {
                ((CDMAPhone) mActivePhone).dispose();
            }
        }

        Phone oldPhone = mActivePhone;

        // Give the garbage collector a hint to start the garbage collection
        // asap NOTE this has been disabled since radio technology change could
        // happen during e.g. a multimedia playing and could slow the system.
        // Tests needs to be done to see the effects of the GC call here when
        // system is busy.
        // System.gc();

        if (newVoiceRadioTech.isCdma()) {
            mActivePhone = PhoneFactory.getCdmaPhone();
            if (null != oldPhone) {
                ((GSMPhone) oldPhone).removeReferences();
            }
        } else if (newVoiceRadioTech.isGsm()) {
            mActivePhone = PhoneFactory.getGsmPhone();
            if (null != oldPhone) {
                ((CDMAPhone) oldPhone).removeReferences();
            }
        }

        oldPhone = null;
    }

public ServiceState getServiceState() {
return mActivePhone.getServiceState();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index a9a4be2..1396123 100644

//Synthetic comment -- @@ -592,14 +592,18 @@
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
            case RILConstants.NETWORK_MODE_GSM_WCDMA_LTE:
mPhoneType = RILConstants.GSM_PHONE;
break;
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
            case RILConstants.NETWORK_MODE_CDMA_AND_LTE_EVDO:
mPhoneType = RILConstants.CDMA_PHONE;
break;
case RILConstants.NETWORK_MODE_GLOBAL:
            case RILConstants.NETWORK_MODE_GLOBAL_LTE:
            case RILConstants.NETWORK_MODE_LTE_ONLY:
mPhoneType = RILConstants.CDMA_PHONE;
break;
default:
//Synthetic comment -- @@ -633,6 +637,33 @@

//***** CommandsInterface implementation

    public void getVoiceRadioTechnology(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_VOICE_RADIO_TECH, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    public void getCdmaSubscriptionSource(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    public void getCdmaPrlVersion(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_PRL_VERSION, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

@Override public void
setOnNITZTime(Handler h, int what, Object obj) {
super.setOnNITZTime(h, what, obj);
//Synthetic comment -- @@ -1968,14 +1999,7 @@
switch(stateInt) {
case 0: state = RadioState.RADIO_OFF; break;
case 1: state = RadioState.RADIO_UNAVAILABLE; break;
            case 2: state = RadioState.RADIO_ON; break;

default:
throw new RuntimeException(
//Synthetic comment -- @@ -2211,6 +2235,9 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
            case RIL_REQUEST_VOICE_RADIO_TECH: ret = responseInts(p); break;
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret = responseInts(p); break;
            case RIL_REQUEST_CDMA_PRL_VERSION: ret = responseString(p); break;
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2352,6 +2379,9 @@
case RIL_UNSOL_OEM_HOOK_RAW: ret = responseRaw(p); break;
case RIL_UNSOL_RINGBACK_TONE: ret = responseInts(p); break;
case RIL_UNSOL_RESEND_INCALL_MUTE: ret = responseVoid(p); break;
            case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseVoid(p); break;
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: ret =  responseVoid(p); break;
            case RIL_UNSOL_CDMA_PRL_CHANGED: ret =  responseVoid(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2369,7 +2399,25 @@
if (RILJ_LOGD) unsljLogMore(response, newState.toString());

switchToRadioState(newState);
                break;
            case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                mVoiceRadioTechChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, null, null));
                break;
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                mCdmaSubscriptionSourceChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, null, null));
                break;
            case RIL_UNSOL_CDMA_PRL_CHANGED:
                if (RILJ_LOGD) unsljLog(response);

                mCdmaPrlChangedRegistrants
                    .notifyRegistrants(new AsyncResult(null, null, null));
                break;
case RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED:
if (RILJ_LOGD) unsljLog(response);

//Synthetic comment -- @@ -3264,6 +3312,9 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
            case RIL_REQUEST_VOICE_RADIO_TECH: return "RIL_REQUEST_VOICE_RADIO_TECH";
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
            case RIL_REQUEST_CDMA_PRL_VERSION: return "RIL_REQUEST_CDMA_PRL_VERSION";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3308,7 +3359,10 @@
case RIL_UNSOL_OEM_HOOK_RAW: return "UNSOL_OEM_HOOK_RAW";
case RIL_UNSOL_RINGBACK_TONE: return "UNSOL_RINGBACK_TONG";
case RIL_UNSOL_RESEND_INCALL_MUTE: return "UNSOL_RESEND_INCALL_MUTE";
            case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: return "UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED";
            case RIL_UNSOL_CDMA_PRL_CHANGED: return "UNSOL_CDMA_PRL_CHANGED";
            case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            default: return "<unknown response>";
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 71a80e0..dd0d229 100644

//Synthetic comment -- @@ -66,6 +66,10 @@
int NETWORK_MODE_EVDO_NO_CDMA   = 6; /* EvDo only */
int NETWORK_MODE_GLOBAL         = 7; /* GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL)
AVAILABLE Application Settings menu*/
    int NETWORK_MODE_CDMA_AND_LTE_EVDO  = 8;  /* CDMA + LTE/EvDo auto */
    int NETWORK_MODE_GSM_WCDMA_LTE      = 9;  /* GSM/WCDMA/LTE auto */
    int NETWORK_MODE_GLOBAL_LTE         = 10; /* CDMA/EvDo/GSM/WCDMA/LTE auto */
    int NETWORK_MODE_LTE_ONLY           = 11; /* LTE only */
int PREFERRED_NETWORK_MODE      = NETWORK_MODE_WCDMA_PREF;

/* CDMA subscription source. See ril.h RIL_REQUEST_CDMA_SET_SUBSCRIPTION */
//Synthetic comment -- @@ -236,6 +240,9 @@
int RIL_REQUEST_SET_SMSC_ADDRESS = 101;
int RIL_REQUEST_REPORT_SMS_MEMORY_STATUS = 102;
int RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING = 103;
    int RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE = 104;
    int RIL_REQUEST_CDMA_PRL_VERSION = 105;
    int RIL_REQUEST_VOICE_RADIO_TECH = 106;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -268,4 +275,7 @@
int RIL_UNSOL_OEM_HOOK_RAW = 1028;
int RIL_UNSOL_RINGBACK_TONE = 1029;
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
    int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
    int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
    int RIL_UNSOL_VOICE_RADIO_TECH_CHANGED = 1034;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..6146578 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
protected static final int DATA_ACCESS_HSUPA = 10;
protected static final int DATA_ACCESS_HSPA = 11;
protected static final int DATA_ACCESS_CDMA_EvDo_B = 12;
    protected static final int DATA_ACCESS_LTE = 13;
    protected static final int DATA_ACCESS_EHRPD = 14;

protected CommandsInterface cm;

//Synthetic comment -- @@ -120,6 +122,12 @@
protected static final int EVENT_ERI_FILE_LOADED                   = 36;
protected static final int EVENT_OTA_PROVISION_STATUS_CHANGE       = 37;
protected static final int EVENT_SET_RADIO_POWER_OFF               = 38;
    protected static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE      = 39;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 40;
    protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 41;
    protected static final int EVENT_GET_CDMA_PRL_VERSION              = 42;

    protected static final int EVENT_RADIO_ON                          = 43;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
old mode 100755
new mode 100644
//Synthetic comment -- index 1c81a07..1ecce12

//Synthetic comment -- @@ -103,6 +103,7 @@
CdmaServiceStateTracker mSST;
RuimRecords mRuimRecords;
RuimCard mRuimCard;
    int mCdmaSubscriptionSource = CDMA_SUBSCRIPTION_NV;
ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
//Synthetic comment -- @@ -150,13 +151,13 @@
super(notifier, context, ci, unitTestMode);

mCM.setPhoneType(Phone.PHONE_TYPE_CDMA);
        mRuimCard = new RuimCard(this);
mCT = new CdmaCallTracker(this);
mSST = new CdmaServiceStateTracker (this);
mSMS = new CdmaSMSDispatcher(this);
mIccFileHandler = new RuimFileHandler(this);
mRuimRecords = new RuimRecords(this);
mDataConnection = new CdmaDataConnectionTracker (this);
mRuimPhoneBookInterfaceManager = new RuimPhoneBookInterfaceManager(this);
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
//Synthetic comment -- @@ -170,9 +171,10 @@
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

        mCM.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOG_TAG);
//Synthetic comment -- @@ -220,9 +222,9 @@
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);
            mCM.unregisterForCdmaSubscriptionSourceChanged(this);
removeCallbacks(mExitEcmRunnable);

mPendingMmis.clear();
//Synthetic comment -- @@ -889,7 +891,8 @@
|| cdmaMin.substring(0,6).equals(UNACTIVATED_MIN2_VALUE))
|| SystemProperties.getBoolean("test_cdma_setup", false);
}
        if (DBG) Log.d(LOG_TAG, "needsOtaServiceProvisioning: ret=" + needsProvisioning +
                                " cdmaMin=" + cdmaMin);
return needsProvisioning;
}

//Synthetic comment -- @@ -1044,6 +1047,13 @@

case EVENT_RADIO_ON:{
Log.d(LOG_TAG, "Event EVENT_RADIO_ON Received");
                mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
            }
            break;

            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:{
                Log.d(LOG_TAG, "EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED");
                mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
}
break;

//Synthetic comment -- @@ -1057,6 +1067,21 @@
}
break;

            case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE:
                ar = (AsyncResult) msg.obj;
                int newSubscriptionSource = ((int[]) ar.result)[0];

                if (newSubscriptionSource != mCdmaSubscriptionSource) {
                    Log.v(LOG_TAG, "Subscription Source Changed : " + mCdmaSubscriptionSource
                            + " >> " + newSubscriptionSource);
                    mCdmaSubscriptionSource = newSubscriptionSource;
                    if (newSubscriptionSource == CDMA_SUBSCRIPTION_NV) {
                        // NV is ready when subscription source is NV
                        sendMessage(obtainMessage(EVENT_NV_READY));
                    }
                }
                break;

case EVENT_NV_READY:{
Log.d(LOG_TAG, "Event EVENT_NV_READY Received");
//Inform the Service State Tracker








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
old mode 100755
new mode 100644
//Synthetic comment -- index fbe455e..93e27e7

//Synthetic comment -- @@ -430,8 +430,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.mCdmaSubscriptionSource == Phone.CDMA_SUBSCRIPTION_RUIM_SIM
                           && phone.getIccCard().getState() != RuimCard.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..f987dcd 100644

//Synthetic comment -- @@ -166,7 +166,6 @@
p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mRuimRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -175,6 +174,8 @@
p.mSST.registerForRoamingOn(this, EVENT_ROAMING_ON, null);
p.mSST.registerForRoamingOff(this, EVENT_ROAMING_OFF, null);
p.mCM.registerForCdmaOtaProvision(this, EVENT_CDMA_OTA_PROVISION, null);
        p.mSST.registerForCdmaSubscriptionSourceChanged(this,
                EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);

IntentFilter filter = new IntentFilter();
filter.addAction(INTENT_RECONNECT_ALARM);
//Synthetic comment -- @@ -225,7 +226,6 @@
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
mCdmaPhone.mRuimRecords.unregisterForRecordsLoaded(this);
phone.mCM.unregisterForDataStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -233,6 +233,7 @@
mCdmaPhone.mSST.unregisterForCdmaDataConnectionDetached(this);
mCdmaPhone.mSST.unregisterForRoamingOn(this);
mCdmaPhone.mSST.unregisterForRoamingOff(this);
        mCdmaPhone.mSST.unregisterForCdmaSubscriptionSourceChanged(this);
phone.mCM.unregisterForCdmaOtaProvision(this);

phone.getContext().unregisterReceiver(this.mIntentReceiver);
//Synthetic comment -- @@ -294,7 +295,7 @@
public boolean isDataConnectionAsDesired() {
boolean roaming = phone.getServiceState().getRoaming();

        if (((!mCdmaPhone.mSST.isSubscriptionFromRuim) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded()) &&
(mCdmaPhone.mSST.getCurrentCdmaDataConnectionState() ==
ServiceState.STATE_IN_SERVICE) &&
//Synthetic comment -- @@ -329,7 +330,7 @@

if ((state == State.IDLE || state == State.SCANNING)
&& (psState == ServiceState.STATE_IN_SERVICE)
                && ((!mCdmaPhone.mSST.isSubscriptionFromRuim) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded())
&& (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
//Synthetic comment -- @@ -345,8 +346,8 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" PS state=" + psState +
                    " isSubscriptionFromRuim=" + mCdmaPhone.mSST.isSubscriptionFromRuim +
                    " ruim records loaded=" + mCdmaPhone.mRuimRecords.getRecordsLoaded() +
" concurrentVoice&Data=" + mCdmaPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" dataEnabled=" + getAnyDataEnabled() +
//Synthetic comment -- @@ -998,8 +999,10 @@
onRecordsLoaded();
break;

            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                if(!mCdmaPhone.mSST.isSubscriptionFromRuim) {
                    onNVReady();
                }
break;

case EVENT_CDMA_DATA_DETACHED:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index d2a4bd8..cf3ead3 100755

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.ServiceState;
//Synthetic comment -- @@ -47,9 +48,11 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.CommandsInterface.RadioState;

import java.util.Arrays;
import java.util.Calendar;
//Synthetic comment -- @@ -96,6 +99,7 @@
private RegistrantList cdmaDataConnectionAttachedRegistrants = new RegistrantList();
private RegistrantList cdmaDataConnectionDetachedRegistrants = new RegistrantList();
private RegistrantList cdmaForSubscriptionInfoReadyRegistrants = new RegistrantList();
    private RegistrantList cdmaSubscriptionSourceChangedRegistrants = new RegistrantList();

/**
* Sometimes we get the NITZ time before we know what country we
//Synthetic comment -- @@ -136,7 +140,8 @@
private boolean mIsMinInfoReady = false;

private boolean isEriTextLoaded = false;
    boolean isSubscriptionFromRuim = false;
    private boolean mSubscriptionSourceUnknown = true;

private boolean mPendingRadioPowerOffAfterDataOff = false;

//Synthetic comment -- @@ -171,16 +176,14 @@
(PowerManager)phone.getContext().getSystemService(Context.POWER_SERVICE);
mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);

cm.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, null);

cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED_CDMA, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
        cm.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        cm.registerForCdmaPrlChanged(this, EVENT_CDMA_PRL_VERSION_CHANGED, null);

phone.registerForEriFileLoaded(this, EVENT_ERI_FILE_LOADED, null);
cm.registerForCdmaOtaProvision(this,EVENT_OTA_PROVISION_STATUS_CHANGE, null);

//Synthetic comment -- @@ -198,17 +201,18 @@

public void dispose() {
// Unregister for all events.
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);
        phone.mRuimCard.unregisterForReady(this);

cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
phone.mRuimRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
        cm.unregisterForCdmaSubscriptionSourceChanged(this);
        cm.unregisterForCdmaPrlChanged(this);
}

@Override
//Synthetic comment -- @@ -286,48 +290,127 @@
cdmaForSubscriptionInfoReadyRegistrants.remove(h);
}

    /**
     * Registration point for NV ready
     * @param h handler to notify
     * @param what what code of message when delivered
     * @param obj placed in Message.obj
     */
    public void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        cdmaSubscriptionSourceChangedRegistrants.add(r);
    }

    public void unregisterForCdmaSubscriptionSourceChanged(Handler h) {
        cdmaSubscriptionSourceChangedRegistrants.remove(h);
    }

    /**
     * Save current source of cdma subscription
     * @param source - 1 for NV, 0 for RUIM
     */
    private void saveCdmaSubscriptionSource(int source) {
        Log.d(LOG_TAG, "Storing cdma subscription source: " + source);
        Secure.putInt(phone.getContext().getContentResolver(),
                Secure.CDMA_SUBSCRIPTION_MODE,
                source );
    }

    private void getSubscriptionInfoAndStartPollingThreads() {
        cm.getCDMASubscription(obtainMessage(EVENT_POLL_STATE_CDMA_SUBSCRIPTION));
        cm.getCdmaPrlVersion(obtainMessage(EVENT_GET_CDMA_PRL_VERSION));

        // Get Registration Information
        pollState();

        // Signal strength polling stops when radio is off.
        queueNextSignalStrengthPoll();
    }

@Override
public void handleMessage (Message msg) {
AsyncResult ar;
int[] ints;
String[] strings;

        if (!phone.mIsTheCurrentActivePhone) {
            Log.e(LOG_TAG, "Received message " + msg +
                    "[" + msg.what + "] while being destroyed. Ignoring.");
            return;
        }

switch (msg.what) {

        case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
            cm.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
            break;

        case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE:
            log("Received EVENT_GET_CDMA_SUBSCRIPTION_SOURCE: ");
            ar = (AsyncResult) msg.obj;

            if (ar.exception == null) {
                int newSubscriptionSource = ((int[]) ar.result)[0]; // 0 - RUIM, 1 - NV

                // Detect a subscription mode change and process it
                if (mSubscriptionSourceUnknown || (isSubscriptionFromRuim !=
                    (newSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM))) {
                    mSubscriptionSourceUnknown = false;
                    Log.v(LOG_TAG, "Subscription Source Changed : "
                            + (isSubscriptionFromRuim ? "0" : "1") + " >> "
                            + newSubscriptionSource);
                    isSubscriptionFromRuim =
                        (newSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM);
                    saveCdmaSubscriptionSource(newSubscriptionSource);
                    if (!isSubscriptionFromRuim) {
                        // NV is ready when subscription source is NV
                        sendMessage(obtainMessage(EVENT_NV_READY));
                    } else {
                        phone.mRuimCard.registerForReady(this, EVENT_RUIM_READY, null);
                    }
                    cdmaSubscriptionSourceChangedRegistrants.notifyRegistrants();
                }
            } else {
                Log.w(LOG_TAG, "Error in parsing CDMA_SUBSCRIPTION_SOURCE:" +ar.exception);
            }
break;

case EVENT_RUIM_READY:
// The RUIM is now ready i.e if it was locked it has been
// unlocked. At this stage, the radio is already powered on.
if (mNeedToRegForRuimLoaded) {
phone.mRuimRecords.registerForRecordsLoaded(this,
EVENT_RUIM_RECORDS_LOADED, null);
mNeedToRegForRuimLoaded = false;
}
if (DBG) log("Receive EVENT_RUIM_READY and Send Request getCDMASubscription.");
            getSubscriptionInfoAndStartPollingThreads();
break;

case EVENT_NV_READY:
// For Non-RUIM phones, the subscription information is stored in
// Non Volatile. Here when Non-Volatile is ready, we can poll the CDMA
// subscription info.
            getSubscriptionInfoAndStartPollingThreads();
            break;

        case EVENT_CDMA_PRL_VERSION_CHANGED :
            cm.getCdmaPrlVersion(obtainMessage(EVENT_GET_CDMA_PRL_VERSION));
            break;

        case EVENT_GET_CDMA_PRL_VERSION :
            ar = (AsyncResult) msg.obj;
            if (ar.exception == null) {
                mPrlVersion = (String) ar.result;
            }
break;

case EVENT_RADIO_STATE_CHANGED:
            if(cm.getRadioState() == RadioState.RADIO_ON) {
                cm.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
            } else {
                mSubscriptionSourceUnknown = true;
            }
// This will do nothing in the 'radio not available' case.
setPowerStateToDesired();
pollState();
//Synthetic comment -- @@ -341,7 +424,7 @@
// This callback is called when signal strength is polled
// all by itself.

            if (!(cm.getRadioState().isOn())) {
// Polling will continue when radio turns back on.
return;
}
//Synthetic comment -- @@ -410,7 +493,7 @@

if (ar.exception == null) {
String cdmaSubscription[] = (String[])ar.result;
                if (cdmaSubscription != null && cdmaSubscription.length >= 4) {
mMdn = cdmaSubscription[0];
if (cdmaSubscription[1] != null) {
String[] sid = cdmaSubscription[1].split(",");
//Synthetic comment -- @@ -611,7 +694,7 @@
String plmn = "";
boolean showPlmn = false;
int rule = 0;
        if (isSubscriptionFromRuim && phone.mRuimCard.getState() == IccCard.State.READY) {
// TODO RUIM SPN is not implemented, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
//Synthetic comment -- @@ -793,7 +876,7 @@
String opNames[] = (String[])ar.result;

if (opNames != null && opNames.length >= 3) {
                    if (!isSubscriptionFromRuim) {
// In CDMA in case on NV, the ss.mOperatorAlphaLong is set later with the
// ERI text, so here it is ignored what is coming from the modem.
newSS.setOperatorName(null, opNames[1], opNames[2]);
//Synthetic comment -- @@ -917,18 +1000,6 @@
pollStateDone();
break;

default:
// Issue all poll-related commands at once, then count
// down the responses which are allowed to arrive
//Synthetic comment -- @@ -1081,7 +1152,7 @@
}

if (hasChanged) {
            if (!isSubscriptionFromRuim) {
String eriText;
// Now the CDMAPhone sees the new ServiceState so it can get the new ERI text
if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
//Synthetic comment -- @@ -1197,7 +1268,7 @@
*/
private void
queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimCard.java b/telephony/java/com/android/internal/telephony/cdma/RuimCard.java
//Synthetic comment -- index 734badd..0817d26 100644

//Synthetic comment -- @@ -27,18 +27,22 @@

RuimCard(CDMAPhone phone) {
super(phone, "CDMA", true);
        is3gpp = false;
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
        mPhone.mCM.registerForIccStatusChanged(mHandler, EVENT_ICC_STATUS_CHANGED, null);
        mPhone.mCM.registerForCdmaSubscriptionSourceChanged(mHandler,
                EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
updateStateProperty();
}

@Override
public void dispose() {
//Unregister for all events
        mPhone.mCM.unregisterForOn(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForIccStatusChanged(mHandler);
        mPhone.mCM.unregisterForCdmaSubscriptionSourceChanged(mHandler);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..d7d15fc 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
recordsToLoad = 0;


        p.mRuimCard.registerForReady(this, EVENT_RUIM_READY, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
// NOTE the EVENT_SMS_ON_RUIM is not registered
p.mCM.setOnIccRefresh(this, EVENT_RUIM_REFRESH, null);
//Synthetic comment -- @@ -96,7 +96,7 @@

public void dispose() {
//Unregister for all events
        ((CDMAPhone) phone).mRuimCard.unregisterForReady(this);
phone.mCM.unregisterForOffOrNotAvailable( this);
phone.mCM.unSetOnIccRefresh(this);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..99728bd 100644

//Synthetic comment -- @@ -142,13 +142,14 @@
}

mCM.setPhoneType(Phone.PHONE_TYPE_GSM);
        mSimCard = new SimCard(this);
mCT = new GsmCallTracker(this);
mSST = new GsmServiceStateTracker (this);
mSMS = new GsmSMSDispatcher(this);
mIccFileHandler = new SIMFileHandler(this);
mSIMRecords = new SIMRecords(this);
mDataConnection = new GsmDataConnectionTracker (this);

if (!unitTestMode) {
mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
mSimSmsIntManager = new SimSmsInterfaceManager(this);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..1dc46d7 100644

//Synthetic comment -- @@ -201,13 +201,13 @@
mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);

cm.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        cm.registerForOn(this, EVENT_RADIO_ON, null);
cm.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, null);

cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);

// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
//Synthetic comment -- @@ -231,9 +231,10 @@
public void dispose() {
// Unregister for all events.
cm.unregisterForAvailable(this);
        cm.unregisterForOn(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);
        phone.mSimCard.unregisterForReady(this);

phone.mSIMRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
//Synthetic comment -- @@ -343,12 +344,21 @@
String[] strings;
Message message;

        if (!phone.mIsTheCurrentActivePhone) {
            Log.e(LOG_TAG, "Received message " + msg +
                    "[" + msg.what + "] while being destroyed. Ignoring.");
            return;
        }
switch (msg.what) {
case EVENT_RADIO_AVAILABLE:
//this is unnecessary
//setPowerStateToDesired();
break;

            case EVENT_RADIO_ON:
                phone.mSimCard.registerForReady(this, EVENT_SIM_READY, null);
                break;

case EVENT_SIM_READY:
// The SIM is now ready i.e if it was locked
// it has been unlocked. At this stage, the radio is already
//Synthetic comment -- @@ -380,7 +390,7 @@
// This callback is called when signal strength is polled
// all by itself

                if (!(cm.getRadioState().isOn())) {
// Polling will continue when radio turns back on and not CDMA
return;
}
//Synthetic comment -- @@ -777,20 +787,6 @@
pollStateDone();
break;

default:
// Issue all poll-related commands at once
// then count down the responses, which
//Synthetic comment -- @@ -842,6 +838,9 @@
case DATA_ACCESS_HSPA:
ret = "HSPA";
break;
            case DATA_ACCESS_LTE:
                ret = "LTE";
                break;
default:
Log.e(LOG_TAG, "Wrong network type: " + Integer.toString(type));
break;
//Synthetic comment -- @@ -1091,7 +1090,7 @@
}

private void queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..66aa7ce 100644

//Synthetic comment -- @@ -160,7 +160,7 @@
recordsToLoad = 0;


        p.mSimCard.registerForReady(this, EVENT_SIM_READY, null);
p.mCM.registerForOffOrNotAvailable(
this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
//Synthetic comment -- @@ -173,7 +173,7 @@

public void dispose() {
//Unregister for all events
        ((GSMPhone) phone).mSimCard.unregisterForReady(this);
phone.mCM.unregisterForOffOrNotAvailable( this);
phone.mCM.unSetOnIccRefresh(this);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimCard.java b/telephony/java/com/android/internal/telephony/gsm/SimCard.java
//Synthetic comment -- index 835cb29..174b0f4 100644

//Synthetic comment -- @@ -28,18 +28,18 @@
SimCard(GSMPhone phone) {
super(phone, "GSM", true);

        mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForIccStatusChanged(mHandler, EVENT_ICC_STATUS_CHANGED, null);
updateStateProperty();
}

@Override
public void dispose() {
//Unregister for all events
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForOn(mHandler);
        mPhone.mCM.unregisterForIccStatusChanged(mHandler);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index a120f52..e9014ff 100644

//Synthetic comment -- @@ -122,9 +122,9 @@

if (pin != null && pin.equals(mPinCode)) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin: success!");
mPinUnlockAttempts = 0;
mSimLockedState = SimLockState.NONE;
            mIccStatusChangedRegistrants.notifyRegistrants();

if (result != null) {
AsyncResult.forMessage(result, null, null);
//Synthetic comment -- @@ -164,9 +164,9 @@

if (puk != null && puk.equals(SIM_PUK_CODE)) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: success!");
mSimLockedState = SimLockState.NONE;
mPukUnlockAttempts = 0;
            mIccStatusChangedRegistrants.notifyRegistrants();

if (result != null) {
AsyncResult.forMessage(result, null, null);
//Synthetic comment -- @@ -446,11 +446,11 @@
*      The ar.result List is sorted by DriverCall.index
*/
public void getCurrentCalls (Message result) {
        if ((mState == RadioState.RADIO_ON) && !isSimLocked()) {
//Log.i("GSM", "[SimCmds] getCurrentCalls");
resultSuccess(result, simulatedCallState.getDriverCalls());
} else {
            //Log.i("GSM", "[SimCmds] getCurrentCalls: RADIO_OFF or SIM not ready!");
resultFail(result,
new CommandException(
CommandException.Error.RADIO_NOT_AVAILABLE));
//Synthetic comment -- @@ -1024,14 +1024,7 @@

public void setRadioPower(boolean on, Message result) {
if(on) {
            setRadioState(RadioState.RADIO_ON);
} else {
setRadioState(RadioState.RADIO_OFF);
}
//Synthetic comment -- @@ -1474,4 +1467,16 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}

    public void getVoiceRadioTechnology(Message response) {
        unimplemented(response);
    }

    public void getCdmaSubscriptionSource(Message response) {
        unimplemented(response);
    }

    public void getCdmaPrlVersion(Message response) {
        unimplemented(response);
    }
}







