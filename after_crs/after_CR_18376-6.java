/*Separate SIM states from RADIO states

Radio state reflects the state of the modem. SIM_READY, RUIM_READY,
NV_READY are subscription states and it is possible that the new cards
have multiple subscriptions. Remove the SIM states from Radio State and
introduce a new VOICE_RADIO_TECH message to identify the exact voice
technology. SIM states will continue to be identified from the
SIM_STATUS messages.

Change-Id:Ie7f9e43bba498ea7b05dc3c2fdf2ec270ab47bbf*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/BaseCommands.java b/telephony/java/com/android/internal/telephony/BaseCommands.java
//Synthetic comment -- index 474e8b4..7e4b859 100644

//Synthetic comment -- @@ -41,15 +41,10 @@
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
//Synthetic comment -- @@ -120,6 +115,15 @@
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
//Synthetic comment -- @@ -209,100 +213,6 @@
}
}

public void registerForCallStateChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);

//Synthetic comment -- @@ -333,15 +243,6 @@
mDataConnectionRegistrants.remove(h);
}

public void registerForIccStatusChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mIccStatusChangedRegistrants.add(r);
//Synthetic comment -- @@ -615,8 +516,7 @@
* This function is called only by RIL.java when receiving unsolicited
* RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED
*
     * RadioState has 3 values : RADIO_OFF, RADIO_UNAVAILABLE, RADIO_ON.
*
* @param newState new RadioState decoded from RIL_UNSOL_RADIO_STATE_CHANGED
*/
//Synthetic comment -- @@ -650,30 +550,6 @@
mNotAvailRegistrants.notifyRegistrants();
}

if (mState.isOn() && !oldState.isOn()) {
Log.d(LOG_TAG,"Notifying: Radio On");
mOnRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -685,33 +561,6 @@
Log.d(LOG_TAG,"Notifying: radio off or not available");
mOffOrNotAvailRegistrants.notifyRegistrants();
}
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 1981b43..adc96f6 100644

//Synthetic comment -- @@ -30,56 +30,91 @@
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

//Synthetic comment -- @@ -156,6 +191,9 @@
RadioState getRadioState();

void getCdmaSubscriptionSource(Message result);
    void getVoiceRadioTechnology(Message result);
    void getCdmaSubscriptionSource(Message result);
    void getCdmaPrlVersion(Message result);

/**
* Fires on any RadioState transition
//Synthetic comment -- @@ -168,6 +206,9 @@
void registerForRadioStateChanged(Handler h, int what, Object obj);
void unregisterForRadioStateChanged(Handler h);

    void registerForVoiceRadioTechChanged(Handler h, int what, Object obj);
    void unregisterForVoiceRadioTechChanged(Handler h);

void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj);
void unregisterForCdmaSubscriptionSourceChanged(Handler h);

//Synthetic comment -- @@ -210,19 +251,6 @@
void registerForOffOrNotAvailable(Handler h, int what, Object obj);
void unregisterForOffOrNotAvailable(Handler h);

void registerForCallStateChanged(Handler h, int what, Object obj);
void unregisterForCallStateChanged(Handler h);
void registerForNetworkStateChanged(Handler h, int what, Object obj);
//Synthetic comment -- @@ -230,13 +258,6 @@
void registerForDataStateChanged(Handler h, int what, Object obj);
void unregisterForDataStateChanged(Handler h);

/** InCall voice privacy notifications */
void registerForInCallVoicePrivacyOn(Handler h, int what, Object obj);
void unregisterForInCallVoicePrivacyOn(Handler h);
//Synthetic comment -- @@ -244,13 +265,10 @@
void unregisterForInCallVoicePrivacyOff(Handler h);

/**
     * Fires on any change in ICC status
*/
    void registerForIccStatusChanged(Handler h, int what, Object obj);
    void unregisterForIccStatusChanged(Handler h);

/**
* unlike the register* methods, there's only one new SMS handler








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 265bf7e..503f38f 100644

//Synthetic comment -- @@ -93,7 +93,7 @@
protected static final int EVENT_START_RECOVERY = 28;
protected static final int EVENT_APN_CHANGED = 29;
protected static final int EVENT_CDMA_DATA_DETACHED = 30;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 31;
protected static final int EVENT_PS_RESTRICT_ENABLED = 32;
protected static final int EVENT_PS_RESTRICT_DISABLED = 33;
public static final int EVENT_CLEAN_UP_CONNECTION = 34;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index e270ce9..0ac39e28 100644

//Synthetic comment -- @@ -28,6 +28,7 @@

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;

/**
* {@hide}
//Synthetic comment -- @@ -38,10 +39,16 @@

private IccCardStatus mIccCardStatus = null;
protected State mState = null;
    protected Object mStateMonitor = new Object();

    protected boolean is3gpp = true;
    protected boolean isSubscriptionFromIccCard = true;
    protected CdmaSubscriptionSourceManager mCdmaSSM = null;
protected PhoneBase mPhone;
private RegistrantList mAbsentRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    protected RegistrantList mReadyRegistrants = new RegistrantList();

private boolean mDesiredPinLocked;
private boolean mDesiredFdnEnabled;
//Synthetic comment -- @@ -73,7 +80,7 @@
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";

    protected static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
private static final int EVENT_PINPUK_DONE = 4;
//Synthetic comment -- @@ -84,9 +91,11 @@
private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;
    protected static final int EVENT_RADIO_ON = 12;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 13;

/*
      UNKNOWN is a transient state, for example, after user inputs ICC pin under
PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
turns to READY
*/
//Synthetic comment -- @@ -113,25 +122,18 @@
*/
case RADIO_OFF:
case RADIO_UNAVAILABLE:
return State.UNKNOWN;
                default:
                    if (!is3gpp && !isSubscriptionFromIccCard) {
                        // CDMA can get subscription from NV. In that case,
                        // subscription is ready as soon as Radio is ON.
                        return State.READY;
                    }
}
} else {
return mState;
}

return State.UNKNOWN;
}

//Synthetic comment -- @@ -198,6 +200,23 @@
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
//Synthetic comment -- @@ -399,7 +418,13 @@
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

//Synthetic comment -- @@ -499,9 +524,24 @@
updateStateProperty();
broadcastIccStateChangedIntent(INTENT_VALUE_ICC_NOT_READY, null);
break;
                case EVENT_RADIO_ON:
                    if (!is3gpp) {
                        handleCdmaSubscriptionSource();
                    }
mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
                    break;
                case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                    handleCdmaSubscriptionSource();
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
//Synthetic comment -- @@ -509,12 +549,6 @@
CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
break;
case EVENT_GET_ICC_STATUS_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -594,7 +628,30 @@
}
};

    private void handleCdmaSubscriptionSource() {
        if(mCdmaSSM != null)  {
            int newSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();

            Log.d(mLogTag, "Received Cdma subscription source: " + newSubscriptionSource);

            boolean isNewSubFromRuim =
                (newSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM);

            if (isNewSubFromRuim != isSubscriptionFromIccCard) {
                isSubscriptionFromIccCard = isNewSubFromRuim;
                // Parse the Stored IccCardStatus Message to set mState correctly.
                handleIccCardStatus(mIccCardStatus);
            }
        }
    }

public State getIccCardState() {
        if(!is3gpp && !isSubscriptionFromIccCard) {
            // CDMA can get subscription from NV. In that case,
            // subscription is ready as soon as Radio is ON.
            return State.READY;
        }

if (mIccCardStatus == null) {
Log.e(mLogTag, "[IccCard] IccCardStatus is null");
return IccCard.State.ABSENT;
//Synthetic comment -- @@ -608,27 +665,18 @@
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

//Synthetic comment -- @@ -647,15 +695,18 @@

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
//Synthetic comment -- index 1674ad6..f5e6d56 100644

//Synthetic comment -- @@ -99,6 +99,7 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED= 27;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 2e391cb..d75e589 100644

//Synthetic comment -- @@ -137,15 +137,19 @@
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
//Synthetic comment -- index 77f1e6c..f71443a 100644

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
//Synthetic comment -- index 9a6c95c..0a0ba6b 100644

//Synthetic comment -- @@ -598,14 +598,18 @@
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
//Synthetic comment -- @@ -639,6 +643,15 @@

//***** CommandsInterface implementation

    public void getVoiceRadioTechnology(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_VOICE_RADIO_TECH, result);

        if (RILJ_LOGD)
            riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

public void getCdmaSubscriptionSource(Message result) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, result);

//Synthetic comment -- @@ -648,6 +661,15 @@
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
//Synthetic comment -- @@ -1983,14 +2005,7 @@
switch(stateInt) {
case 0: state = RadioState.RADIO_OFF; break;
case 1: state = RadioState.RADIO_UNAVAILABLE; break;
            case 2: state = RadioState.RADIO_ON; break;

default:
throw new RuntimeException(
//Synthetic comment -- @@ -2233,6 +2248,9 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret = responseInts(p); break;
            case RIL_REQUEST_VOICE_RADIO_TECH: ret = responseInts(p); break;
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret = responseInts(p); break;
            case RIL_REQUEST_CDMA_PRL_VERSION: ret = responseString(p); break;
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2374,6 +2392,7 @@
case RIL_UNSOL_OEM_HOOK_RAW: ret = responseRaw(p); break;
case RIL_UNSOL_RINGBACK_TONE: ret = responseInts(p); break;
case RIL_UNSOL_RESEND_INCALL_MUTE: ret = responseVoid(p); break;
            case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: ret =  responseVoid(p); break;
case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: ret =  responseVoid(p); break;
case RIL_UNSOL_CDMA_PRL_CHANGED: ret =  responseVoid(p); break;

//Synthetic comment -- @@ -2400,6 +2419,17 @@
int [] ssource = (int[])ret;
mCdmaSubscriptionSourceChangedRegistrants
.notifyRegistrants(new AsyncResult(null, ssource, null));
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
//Synthetic comment -- @@ -3304,6 +3334,9 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
            case RIL_REQUEST_VOICE_RADIO_TECH: return "RIL_REQUEST_VOICE_RADIO_TECH";
            case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
            case RIL_REQUEST_CDMA_PRL_VERSION: return "RIL_REQUEST_CDMA_PRL_VERSION";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3350,6 +3383,7 @@
case RIL_UNSOL_RESEND_INCALL_MUTE: return "UNSOL_RESEND_INCALL_MUTE";
case RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED: return "UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED";
case RIL_UNSOL_CDMA_PRL_CHANGED: return "UNSOL_CDMA_PRL_CHANGED";
            case RIL_UNSOL_VOICE_RADIO_TECH_CHANGED: return "UNSOL_VOICE_RADIO_TECH_CHANGED";
default: return "<unknown response>";
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index cc71b48..62fdaff 100644

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
//Synthetic comment -- @@ -238,6 +242,8 @@
int RIL_REQUEST_REPORT_SMS_MEMORY_STATUS = 102;
int RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING = 103;
int RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE = 104;
    int RIL_REQUEST_CDMA_PRL_VERSION = 105;
    int RIL_REQUEST_VOICE_RADIO_TECH = 106;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -272,4 +278,5 @@
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
    int RIL_UNSOL_VOICE_RADIO_TECH_CHANGED = 1033;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index d63896c..5c9612e 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
protected static final int DATA_ACCESS_HSUPA = 10;
protected static final int DATA_ACCESS_HSPA = 11;
protected static final int DATA_ACCESS_CDMA_EvDo_B = 12;
    protected static final int DATA_ACCESS_EHRPD = 13;
    protected static final int DATA_ACCESS_LTE = 14;

protected CommandsInterface cm;

//Synthetic comment -- @@ -122,6 +124,8 @@
protected static final int EVENT_SET_RADIO_POWER_OFF               = 38;
protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 39;
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
    protected static final int EVENT_GET_CDMA_PRL_VERSION              = 41;
    protected static final int EVENT_RADIO_ON                          = 42;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f31bf24..e5f51a5f 100644

//Synthetic comment -- @@ -101,8 +101,10 @@
CdmaCallTracker mCT;
CdmaSMSDispatcher mSMS;
CdmaServiceStateTracker mSST;
    CdmaSubscriptionSourceManager mCdmaSSM;
RuimRecords mRuimRecords;
RuimCard mRuimCard;
    int mCdmaSubscriptionSource = CDMA_SUBSCRIPTION_NV;
ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
//Synthetic comment -- @@ -150,13 +152,15 @@
super(notifier, context, ci, unitTestMode);

mCM.setPhoneType(Phone.PHONE_TYPE_CDMA);
        mRuimCard = new RuimCard(this);
mCT = new CdmaCallTracker(this);
mSST = new CdmaServiceStateTracker (this);
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(context, ci, this,
                                        EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
mSMS = new CdmaSMSDispatcher(this);
mIccFileHandler = new RuimFileHandler(this);
mRuimRecords = new RuimRecords(this);
mDataConnection = new CdmaDataConnectionTracker (this);
mRuimPhoneBookInterfaceManager = new RuimPhoneBookInterfaceManager(this);
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this, mSMS);
mSubInfo = new PhoneSubInfo(this);
//Synthetic comment -- @@ -170,7 +174,6 @@
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

PowerManager pm
//Synthetic comment -- @@ -220,9 +223,9 @@
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);
            mCM.unregisterForCdmaSubscriptionSourceChanged(this);
removeCallbacks(mExitEcmRunnable);

mPendingMmis.clear();
//Synthetic comment -- @@ -231,6 +234,7 @@
mCT.dispose();
mDataConnection.dispose();
mSST.dispose();
            mCdmaSSM.dispose(this);
mSMS.dispose();
mIccFileHandler.dispose(); // instance of RuimFileHandler
mRuimRecords.dispose();
//Synthetic comment -- @@ -894,7 +898,8 @@
|| cdmaMin.substring(0,6).equals(UNACTIVATED_MIN2_VALUE))
|| SystemProperties.getBoolean("test_cdma_setup", false);
}
        if (DBG) Log.d(LOG_TAG, "needsOtaServiceProvisioning: ret=" + needsProvisioning +
                                " cdmaMin=" + cdmaMin);
return needsProvisioning;
}

//Synthetic comment -- @@ -1049,6 +1054,13 @@

case EVENT_RADIO_ON:{
Log.d(LOG_TAG, "Event EVENT_RADIO_ON Received");
                handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
            }
            break;

            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:{
                Log.d(LOG_TAG, "EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED");
                handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
}
break;

//Synthetic comment -- @@ -1096,6 +1108,21 @@
}

/**
     * Handles the call to get the subscription source
     *
     * @param holds the new CDMA subscription source value
     */
    private void handleCdmaSubscriptionSource(int newSubscriptionSource) {
        if (newSubscriptionSource != mCdmaSubscriptionSource) {
             mCdmaSubscriptionSource = newSubscriptionSource;
             if (newSubscriptionSource == CDMA_SUBSCRIPTION_NV) {
                 // NV is ready when subscription source is NV
                 sendMessage(obtainMessage(EVENT_NV_READY));
             }
        }
    }

    /**
* Retrieves the PhoneSubInfo of the CDMAPhone
*/
public PhoneSubInfo getPhoneSubInfo() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index fbe455e..93aee137 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.RILConstants;

/**
* {@hide}
//Synthetic comment -- @@ -430,8 +431,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.mCdmaSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM
                           && phone.getIccCard().getState() != RuimCard.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..1a81a17 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.RILConstants;

import java.util.ArrayList;

//Synthetic comment -- @@ -62,6 +63,7 @@
protected final String LOG_TAG = "CDMA";

private CDMAPhone mCdmaPhone;
    private CdmaSubscriptionSourceManager mCdmaSSM;

// Indicates baseband will not auto-attach
private boolean noAutoAttach = false;
//Synthetic comment -- @@ -166,7 +168,6 @@
p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mRuimRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -175,6 +176,8 @@
p.mSST.registerForRoamingOn(this, EVENT_ROAMING_ON, null);
p.mSST.registerForRoamingOff(this, EVENT_ROAMING_OFF, null);
p.mCM.registerForCdmaOtaProvision(this, EVENT_CDMA_OTA_PROVISION, null);
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance (p.getContext(), p.mCM, this,
                                              EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);

IntentFilter filter = new IntentFilter();
filter.addAction(INTENT_RECONNECT_ALARM);
//Synthetic comment -- @@ -225,7 +228,6 @@
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
mCdmaPhone.mRuimRecords.unregisterForRecordsLoaded(this);
phone.mCM.unregisterForDataStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -233,6 +235,7 @@
mCdmaPhone.mSST.unregisterForCdmaDataConnectionDetached(this);
mCdmaPhone.mSST.unregisterForRoamingOn(this);
mCdmaPhone.mSST.unregisterForRoamingOff(this);
        mCdmaSSM.dispose(this);
phone.mCM.unregisterForCdmaOtaProvision(this);

phone.getContext().unregisterReceiver(this.mIntentReceiver);
//Synthetic comment -- @@ -294,7 +297,7 @@
public boolean isDataConnectionAsDesired() {
boolean roaming = phone.getServiceState().getRoaming();

        if (((mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_NV) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded()) &&
(mCdmaPhone.mSST.getCurrentCdmaDataConnectionState() ==
ServiceState.STATE_IN_SERVICE) &&
//Synthetic comment -- @@ -329,7 +332,7 @@

if ((state == State.IDLE || state == State.SCANNING)
&& (psState == ServiceState.STATE_IN_SERVICE)
                && ((mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_NV) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded())
&& (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
//Synthetic comment -- @@ -345,8 +348,9 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" PS state=" + psState +
                    " isSubscriptionFromRuim=" +
                    (mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_RUIM) +
                    " ruim records loaded=" + mCdmaPhone.mRuimRecords.getRecordsLoaded() +
" concurrentVoice&Data=" + mCdmaPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" dataEnabled=" + getAnyDataEnabled() +
//Synthetic comment -- @@ -998,8 +1002,10 @@
onRecordsLoaded();
break;

            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                if(mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_NV) {
                    onNVReady();
                }
break;

case EVENT_CDMA_DATA_DETACHED:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 7e0b42f..829a5e1 100755

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
//Synthetic comment -- @@ -136,7 +139,8 @@
private boolean mIsMinInfoReady = false;

private boolean isEriTextLoaded = false;
    boolean isSubscriptionFromRuim = false;
    private CdmaSubscriptionSourceManager mCdmaSSM;

private boolean mPendingRadioPowerOffAfterDataOff = false;

//Synthetic comment -- @@ -167,20 +171,21 @@
newCellLoc = new CdmaCellLocation();
mSignalStrength = new SignalStrength();

        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(phone.getContext(), cm, this,
                                                EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        isSubscriptionFromRuim = (mCdmaSSM.getCdmaSubscriptionSource() ==
                                      RILConstants.SUBSCRIPTION_FROM_RUIM);
PowerManager powerManager =
(PowerManager)phone.getContext().getSystemService(Context.POWER_SERVICE);
mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);

cm.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, null);

cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED_CDMA, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
        cm.registerForCdmaPrlChanged(this, EVENT_CDMA_PRL_VERSION_CHANGED, null);

phone.registerForEriFileLoaded(this, EVENT_ERI_FILE_LOADED, null);
cm.registerForCdmaOtaProvision(this,EVENT_OTA_PROVISION_STATUS_CHANGE, null);

//Synthetic comment -- @@ -198,17 +203,18 @@

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
        mCdmaSSM.dispose(this);
        cm.unregisterForCdmaPrlChanged(this);
}

@Override
//Synthetic comment -- @@ -286,48 +292,80 @@
cdmaForSubscriptionInfoReadyRegistrants.remove(h);
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
            handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
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
                handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());

                // Signal strength polling stops when radio is off.
                queueNextSignalStrengthPoll();
            }
// This will do nothing in the 'radio not available' case.
setPowerStateToDesired();
pollState();
//Synthetic comment -- @@ -341,7 +379,7 @@
// This callback is called when signal strength is polled
// all by itself.

            if (!(cm.getRadioState().isOn())) {
// Polling will continue when radio turns back on.
return;
}
//Synthetic comment -- @@ -410,7 +448,7 @@

if (ar.exception == null) {
String cdmaSubscription[] = (String[])ar.result;
                if (cdmaSubscription != null && cdmaSubscription.length >= 4) {
mMdn = cdmaSubscription[0];
if (cdmaSubscription[1] != null) {
String[] sid = cdmaSubscription[1].split(",");
//Synthetic comment -- @@ -549,6 +587,18 @@

//***** Private Instance Methods

    private void handleCdmaSubscriptionSource(int newSubscriptionSource) {
        Log.v(LOG_TAG, "Subscription Source : " + newSubscriptionSource);
        isSubscriptionFromRuim = (newSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM);
        saveCdmaSubscriptionSource(newSubscriptionSource);
        if (!isSubscriptionFromRuim) {
            // NV is ready when subscription source is NV
            sendMessage(obtainMessage(EVENT_NV_READY));
        } else {
            phone.mRuimCard.registerForReady(this, EVENT_RUIM_READY, null);
        }
    }

@Override
protected void setPowerStateToDesired() {
// If we want it on and it's off, turn it on
//Synthetic comment -- @@ -628,7 +678,7 @@
String plmn = "";
boolean showPlmn = false;
int rule = 0;
        if (isSubscriptionFromRuim && phone.mRuimCard.getState() == IccCard.State.READY) {
// TODO RUIM SPN is not implemented, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
//Synthetic comment -- @@ -810,7 +860,7 @@
String opNames[] = (String[])ar.result;

if (opNames != null && opNames.length >= 3) {
                    if (!isSubscriptionFromRuim) {
// In CDMA in case on NV, the ss.mOperatorAlphaLong is set later with the
// ERI text, so here it is ignored what is coming from the modem.
newSS.setOperatorName(null, opNames[1], opNames[2]);
//Synthetic comment -- @@ -934,18 +984,6 @@
pollStateDone();
break;

default:
// Issue all poll-related commands at once, then count
// down the responses which are allowed to arrive
//Synthetic comment -- @@ -1098,7 +1136,7 @@
}

if (hasChanged) {
            if (!isSubscriptionFromRuim) {
String eriText;
// Now the CDMAPhone sees the new ServiceState so it can get the new ERI text
if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
//Synthetic comment -- @@ -1214,7 +1252,7 @@
*/
private void
queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaSubscriptionSourceManager.java b/telephony/java/com/android/internal/telephony/cdma/CdmaSubscriptionSourceManager.java
new file mode 100644
//Synthetic comment -- index 0000000..c8d57cb

//Synthetic comment -- @@ -0,0 +1,170 @@
/*
 * Copyright (c) 2010 The Android Open Source Project
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

package com.android.internal.telephony.cdma;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.RILConstants;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.provider.Settings;
import android.util.Log;

/**
 * Class that handles the CDMA subscription source changed events from RIL
 */
public class CdmaSubscriptionSourceManager extends Handler {
    static final String LOG_TAG = "CDMA";
    private static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1;
    private static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE     = 2;
    private static final int EVENT_RADIO_ON                         = 3;

    // ***** Instance Variables
    private static CdmaSubscriptionSourceManager sInstance;
    private CommandsInterface mCM;
    private Context mContext;
    private RegistrantList mCdmaSubscriptionSourceChangedRegistrants = new RegistrantList();
    private int mRef = 0;

    // Type of CDMA subscription source
    private int mCdmaSubscriptionSource = RILConstants.SUBSCRIPTION_FROM_NV;

    // Constructor
    private CdmaSubscriptionSourceManager(Context context, CommandsInterface ci) {
        mContext = context;
        mCM = ci;
        mCM.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        mCM.registerForOn(this, EVENT_RADIO_ON, null);
        getDefaultCdmaSubscriptionSource();
    }

    /**
     * This function creates a single instance of this class
     *
     * @return object of type CdmaSubscriptionSourceManager
     */
    public synchronized static CdmaSubscriptionSourceManager getInstance(Context context,
           CommandsInterface ci, Handler h, int what, Object obj) {
        if (null == sInstance) {
            sInstance = new CdmaSubscriptionSourceManager(context, ci);
        }
        sInstance.mRef++;
        sInstance.registerForCdmaSubscriptionSourceChanged(h, what, obj);
        return sInstance;
    }

    /**
     * Unregisters for the registered event with RIL
     */
    public void dispose(Handler h) {
        mCdmaSubscriptionSourceChangedRegistrants.remove(h);
        mRef--;
        if (mRef <= 0) {
            mCM.unregisterForCdmaSubscriptionSourceChanged(this);
            mCM.unregisterForOn(this);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.os.Handler#handleMessage(android.os.Message)
     */
    @Override
    public void handleMessage(Message msg) {
        AsyncResult ar;
        switch (msg.what) {
            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED: {
                Log.d(LOG_TAG, "EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED");
                mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
            }
            break;
            case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE: {
                ar = (AsyncResult)msg.obj;
                handleGetCdmaSubscriptionSource(ar);
            }
            break;
            case EVENT_RADIO_ON: {
                mCM.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
            }
            break;
            default:
                super.handleMessage(msg);
        }
    }

    /**
     * Returns the current CDMA subscription source value
     * @return CDMA subscription source value
     */
    public int getCdmaSubscriptionSource() {
        return mCdmaSubscriptionSource;
    }

    /**
     * Gets the default CDMA subscription source
     *
     * @param cr
     * @return
     */
    private int getDefaultCdmaSubscriptionSource() {
        // Get the default value from the Settings
        mCdmaSubscriptionSource = Settings.Secure.getInt(mContext.getContentResolver(),
                Settings.Secure.CDMA_SUBSCRIPTION_MODE, RILConstants.PREFERRED_CDMA_SUBSCRIPTION);
        return mCdmaSubscriptionSource;
    }

    /**
     * Allows clients to register for CDMA subscription source changed event
     */
    private void registerForCdmaSubscriptionSourceChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mCdmaSubscriptionSourceChangedRegistrants.add(r);
    }

    /**
     * Handles the call to get the subscription source
     *
     * @param ar AsyncResult object that contains the result of get CDMA
     *            subscription source call
     */
    private void handleGetCdmaSubscriptionSource(AsyncResult ar) {
        if ((ar.exception == null) && (ar.result != null)) {
            int newSubscriptionSource = ((int[]) ar.result)[0];

            if (newSubscriptionSource != mCdmaSubscriptionSource) {
                Log.v(LOG_TAG, "Subscription Source Changed : " + mCdmaSubscriptionSource + " >> "
                        + newSubscriptionSource);
                mCdmaSubscriptionSource = newSubscriptionSource;

                // Notify registrants of the new CDMA subscription source
                mCdmaSubscriptionSourceChangedRegistrants.notifyRegistrants(new AsyncResult(null,
                        null, null));
            }
        } else {
            // GET_CDMA_SUBSCRIPTION is returning Failure. Probably
            // because modem created GSM Phone. If modem created
            // GSMPhone, then PhoneProxy will trigger a change in
            // Phone objects and this object will be destroyed.
            Log.w(LOG_TAG, "Unable to get CDMA Subscription Source, Exception: " + ar.exception
                    + ", result: " + ar.result);
        }
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimCard.java b/telephony/java/com/android/internal/telephony/cdma/RuimCard.java
//Synthetic comment -- index 734badd..87dc259 100644

//Synthetic comment -- @@ -27,18 +27,23 @@

RuimCard(CDMAPhone phone) {
super(phone, "CDMA", true);
        is3gpp = false;
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
        mPhone.mCM.registerForIccStatusChanged(mHandler, EVENT_ICC_STATUS_CHANGED, null);
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(mPhone.getContext(),
                       mPhone.mCM, mHandler, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
updateStateProperty();
}

@Override
public void dispose() {
        mCdmaSSM.dispose(mHandler);

//Unregister for all events
        mPhone.mCM.unregisterForOn(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForIccStatusChanged(mHandler);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..6b1e9d7 100644

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
//Synthetic comment -- @@ -187,6 +187,12 @@

boolean isRecordLoadResponse = false;

        if (!phone.mIsTheCurrentActivePhone) {
            Log.e(LOG_TAG, "Received message " + msg +
                    "[" + msg.what + "] while being destroyed. Ignoring.");
            return;
        }

try { switch (msg.what) {
case EVENT_RUIM_READY:
onRuimReady();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3959c67..de69ede 100644

//Synthetic comment -- @@ -138,13 +138,14 @@
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
mSimSmsIntManager = new SimSmsInterfaceManager(this, mSMS);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6ddb312..4fa45c7 100644

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
//Synthetic comment -- @@ -386,7 +396,7 @@
// This callback is called when signal strength is polled
// all by itself

                if (!(cm.getRadioState().isOn())) {
// Polling will continue when radio turns back on and not CDMA
return;
}
//Synthetic comment -- @@ -790,20 +800,6 @@
pollStateDone();
break;

default:
// Issue all poll-related commands at once
// then count down the responses, which
//Synthetic comment -- @@ -855,6 +851,9 @@
case DATA_ACCESS_HSPA:
ret = "HSPA";
break;
            case DATA_ACCESS_LTE:
                ret = "LTE";
                break;
default:
Log.e(LOG_TAG, "Wrong network type: " + Integer.toString(type));
break;
//Synthetic comment -- @@ -1104,7 +1103,7 @@
}

private void queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 3b133da..c9dc9b4 100755

//Synthetic comment -- @@ -179,7 +179,7 @@
recordsToLoad = 0;


        p.mSimCard.registerForReady(this, EVENT_SIM_READY, null);
p.mCM.registerForOffOrNotAvailable(
this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
//Synthetic comment -- @@ -192,7 +192,7 @@

public void dispose() {
//Unregister for all events
        ((GSMPhone) phone).mSimCard.unregisterForReady(this);
phone.mCM.unregisterForOffOrNotAvailable( this);
phone.mCM.unSetOnIccRefresh(this);
}
//Synthetic comment -- @@ -489,6 +489,12 @@

boolean isRecordLoadResponse = false;

        if (!phone.mIsTheCurrentActivePhone) {
            Log.e(LOG_TAG, "Received message " + msg +
                    "[" + msg.what + "] while being destroyed. Ignoring.");
            return;
        }

try { switch (msg.what) {
case EVENT_SIM_READY:
onSimReady();








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
//Synthetic comment -- index 806674e..1bd4136 100644

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
//Synthetic comment -- @@ -1023,14 +1023,7 @@

public void setRadioPower(boolean on, Message result) {
if(on) {
            setRadioState(RadioState.RADIO_ON);
} else {
setRadioState(RadioState.RADIO_OFF);
}
//Synthetic comment -- @@ -1477,4 +1470,16 @@
public void getCdmaSubscriptionSource(Message response) {
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







