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
    protected RegistrantList mSIMReadyRegistrants = new RegistrantList();
    protected RegistrantList mSIMLockedRegistrants = new RegistrantList();
    protected RegistrantList mRUIMReadyRegistrants = new RegistrantList();
    protected RegistrantList mRUIMLockedRegistrants = new RegistrantList();
    protected RegistrantList mNVReadyRegistrants = new RegistrantList();
protected RegistrantList mCallStateRegistrants = new RegistrantList();
protected RegistrantList mNetworkStateRegistrants = new RegistrantList();
protected RegistrantList mDataConnectionRegistrants = new RegistrantList();
    protected RegistrantList mRadioTechnologyChangedRegistrants = new RegistrantList();
protected RegistrantList mIccStatusChangedRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOnRegistrants = new RegistrantList();
protected RegistrantList mVoicePrivacyOffRegistrants = new RegistrantList();
//Synthetic comment -- @@ -118,6 +115,33 @@
}
}

public void registerForOn(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);

//Synthetic comment -- @@ -189,100 +213,6 @@
}
}


    /** Any transition into SIM_READY */
    public void registerForSIMReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mSIMReadyRegistrants.add(r);

            if (mState.isSIMReady()) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForSIMReady(Handler h) {
        synchronized (mStateMonitor) {
            mSIMReadyRegistrants.remove(h);
        }
    }

    /** Any transition into RUIM_READY */
    public void registerForRUIMReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mRUIMReadyRegistrants.add(r);

            if (mState.isRUIMReady()) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForRUIMReady(Handler h) {
        synchronized(mStateMonitor) {
            mRUIMReadyRegistrants.remove(h);
        }
    }

    /** Any transition into NV_READY */
    public void registerForNVReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mNVReadyRegistrants.add(r);

            if (mState.isNVReady()) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForNVReady(Handler h) {
        synchronized (mStateMonitor) {
            mNVReadyRegistrants.remove(h);
        }
    }

    public void registerForSIMLockedOrAbsent(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mSIMLockedRegistrants.add(r);

            if (mState == RadioState.SIM_LOCKED_OR_ABSENT) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForSIMLockedOrAbsent(Handler h) {
        synchronized (mStateMonitor) {
            mSIMLockedRegistrants.remove(h);
        }
    }

    public void registerForRUIMLockedOrAbsent(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mRUIMLockedRegistrants.add(r);

            if (mState == RadioState.RUIM_LOCKED_OR_ABSENT) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForRUIMLockedOrAbsent(Handler h) {
        synchronized (mStateMonitor) {
            mRUIMLockedRegistrants.remove(h);
        }
    }

public void registerForCallStateChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);

//Synthetic comment -- @@ -313,15 +243,6 @@
mDataConnectionRegistrants.remove(h);
}

    public void registerForRadioTechnologyChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mRadioTechnologyChangedRegistrants.add(r);
    }

    public void unregisterForRadioTechnologyChanged(Handler h) {
        mRadioTechnologyChangedRegistrants.remove(h);
    }

public void registerForIccStatusChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mIccStatusChangedRegistrants.add(r);
//Synthetic comment -- @@ -595,8 +516,7 @@
* This function is called only by RIL.java when receiving unsolicited
* RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED
*
     * RadioState has 5 values : RADIO_OFF, RADIO_UNAVAILABLE, SIM_NOT_READY,
     * SIM_LOCKED_OR_ABSENT, and SIM_READY.
*
* @param newState new RadioState decoded from RIL_UNSOL_RADIO_STATE_CHANGED
*/
//Synthetic comment -- @@ -630,30 +550,6 @@
mNotAvailRegistrants.notifyRegistrants();
}

            if (mState.isSIMReady() && !oldState.isSIMReady()) {
                Log.d(LOG_TAG,"Notifying: SIM ready");
                mSIMReadyRegistrants.notifyRegistrants();
            }

            if (mState == RadioState.SIM_LOCKED_OR_ABSENT) {
                Log.d(LOG_TAG,"Notifying: SIM locked or absent");
                mSIMLockedRegistrants.notifyRegistrants();
            }

            if (mState.isRUIMReady() && !oldState.isRUIMReady()) {
                Log.d(LOG_TAG,"Notifying: RUIM ready");
                mRUIMReadyRegistrants.notifyRegistrants();
            }

            if (mState == RadioState.RUIM_LOCKED_OR_ABSENT) {
                Log.d(LOG_TAG,"Notifying: RUIM locked or absent");
                mRUIMLockedRegistrants.notifyRegistrants();
            }
            if (mState.isNVReady() && !oldState.isNVReady()) {
                Log.d(LOG_TAG,"Notifying: NV ready");
                mNVReadyRegistrants.notifyRegistrants();
            }

if (mState.isOn() && !oldState.isOn()) {
Log.d(LOG_TAG,"Notifying: Radio On");
mOnRegistrants.notifyRegistrants();
//Synthetic comment -- @@ -665,33 +561,6 @@
Log.d(LOG_TAG,"Notifying: radio off or not available");
mOffOrNotAvailRegistrants.notifyRegistrants();
}

            /* Radio Technology Change events
             * NOTE: isGsm and isCdma have no common states in RADIO_OFF or RADIO_UNAVAILABLE; the
             *   current phone is determined by mPhoneType
             * NOTE: at startup no phone have been created and the RIL determines the mPhoneType
             *   looking based on the networkMode set by the PhoneFactory in the constructor
             */

            if (mState.isGsm() && oldState.isCdma()) {
                Log.d(LOG_TAG,"Notifying: radio technology change CDMA to GSM");
                mRadioTechnologyChangedRegistrants.notifyRegistrants();
            }

            if (mState.isGsm() && !oldState.isOn() && (mPhoneType == Phone.PHONE_TYPE_CDMA)) {
                Log.d(LOG_TAG,"Notifying: radio technology change CDMA OFF to GSM");
                mRadioTechnologyChangedRegistrants.notifyRegistrants();
            }

            if (mState.isCdma() && oldState.isGsm()) {
                Log.d(LOG_TAG,"Notifying: radio technology change GSM to CDMA");
                mRadioTechnologyChangedRegistrants.notifyRegistrants();
            }

            if (mState.isCdma() && !oldState.isOn() && (mPhoneType == Phone.PHONE_TYPE_GSM)) {
                Log.d(LOG_TAG,"Notifying: radio technology change GSM OFF to CDMA");
                mRadioTechnologyChangedRegistrants.notifyRegistrants();
            }
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..2fbff63 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import android.os.Message;
import android.os.Handler;


/**
//Synthetic comment -- @@ -29,56 +30,91 @@
enum RadioState {
RADIO_OFF,         /* Radio explictly powered off (eg CFUN=0) */
RADIO_UNAVAILABLE, /* Radio unavailable (eg, resetting or not booted) */
        SIM_NOT_READY,     /* Radio is on, but the SIM interface is not ready */
        SIM_LOCKED_OR_ABSENT,  /* SIM PIN locked, PUK required, network
                               personalization, or SIM absent */
        SIM_READY,         /* Radio is on and SIM interface is available */
        RUIM_NOT_READY,    /* Radio is on, but the RUIM interface is not ready */
        RUIM_READY,        /* Radio is on and the RUIM interface is available */
        RUIM_LOCKED_OR_ABSENT, /* RUIM PIN locked, PUK required, network
                                  personalization locked, or RUIM absent */
        NV_NOT_READY,      /* Radio is on, but the NV interface is not available */
        NV_READY;          /* Radio is on and the NV interface is available */

public boolean isOn() /* and available...*/ {
            return this == SIM_NOT_READY
                    || this == SIM_LOCKED_OR_ABSENT
                    || this == SIM_READY
                    || this == RUIM_NOT_READY
                    || this == RUIM_READY
                    || this == RUIM_LOCKED_OR_ABSENT
                    || this == NV_NOT_READY
                    || this == NV_READY;
}

public boolean isAvailable() {
return this != RADIO_UNAVAILABLE;
}

        public boolean isSIMReady() {
            return this == SIM_READY;
        }

        public boolean isRUIMReady() {
            return this == RUIM_READY;
        }

        public boolean isNVReady() {
            return this == NV_READY;
}

public boolean isGsm() {
            return this == SIM_NOT_READY
                    || this == SIM_LOCKED_OR_ABSENT
                    || this == SIM_READY;
}

public boolean isCdma() {
            return this ==  RUIM_NOT_READY
                    || this == RUIM_READY
                    || this == RUIM_LOCKED_OR_ABSENT
                    || this == NV_NOT_READY
                    || this == NV_READY;
}
}

//Synthetic comment -- @@ -154,6 +190,10 @@

RadioState getRadioState();

/**
* Fires on any RadioState transition
* Always fires immediately as well
//Synthetic comment -- @@ -165,6 +205,15 @@
void registerForRadioStateChanged(Handler h, int what, Object obj);
void unregisterForRadioStateChanged(Handler h);

/**
* Fires on any transition into RadioState.isOn()
* Fires immediately if currently in that state
//Synthetic comment -- @@ -201,19 +250,6 @@
void registerForOffOrNotAvailable(Handler h, int what, Object obj);
void unregisterForOffOrNotAvailable(Handler h);

    /**
     * Fires on any transition into SIM_READY
     * Fires immediately if if currently in that state
     * In general, actions should be idempotent. State may change
     * before event is received.
     */
    void registerForSIMReady(Handler h, int what, Object obj);
    void unregisterForSIMReady(Handler h);

    /** Any transition into SIM_LOCKED_OR_ABSENT */
    void registerForSIMLockedOrAbsent(Handler h, int what, Object obj);
    void unregisterForSIMLockedOrAbsent(Handler h);

void registerForCallStateChanged(Handler h, int what, Object obj);
void unregisterForCallStateChanged(Handler h);
void registerForNetworkStateChanged(Handler h, int what, Object obj);
//Synthetic comment -- @@ -221,13 +257,6 @@
void registerForDataStateChanged(Handler h, int what, Object obj);
void unregisterForDataStateChanged(Handler h);

    void registerForRadioTechnologyChanged(Handler h, int what, Object obj);
    void unregisterForRadioTechnologyChanged(Handler h);
    void registerForNVReady(Handler h, int what, Object obj);
    void unregisterForNVReady(Handler h);
    void registerForRUIMLockedOrAbsent(Handler h, int what, Object obj);
    void unregisterForRUIMLockedOrAbsent(Handler h);

/** InCall voice privacy notifications */
void registerForInCallVoicePrivacyOn(Handler h, int what, Object obj);
void unregisterForInCallVoicePrivacyOn(Handler h);
//Synthetic comment -- @@ -235,13 +264,10 @@
void unregisterForInCallVoicePrivacyOff(Handler h);

/**
     * Fires on any transition into RUIM_READY
     * Fires immediately if if currently in that state
     * In general, actions should be idempotent. State may change
     * before event is received.
*/
    void registerForRUIMReady(Handler h, int what, Object obj);
    void unregisterForRUIMReady(Handler h);

/**
* unlike the register* methods, there's only one new SMS handler








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 3b9e6cc..3c2b871 100644

//Synthetic comment -- @@ -93,7 +93,7 @@
protected static final int EVENT_START_RECOVERY = 28;
protected static final int EVENT_APN_CHANGED = 29;
protected static final int EVENT_CDMA_DATA_DETACHED = 30;
    protected static final int EVENT_NV_READY = 31;
protected static final int EVENT_PS_RESTRICT_ENABLED = 32;
protected static final int EVENT_PS_RESTRICT_DISABLED = 33;
public static final int EVENT_CLEAN_UP_CONNECTION = 34;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 90f9e8c..83c3221 100644

//Synthetic comment -- @@ -38,10 +38,15 @@

private IccCardStatus mIccCardStatus = null;
protected State mState = null;
protected PhoneBase mPhone;
private RegistrantList mAbsentRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
private RegistrantList mNetworkLockedRegistrants = new RegistrantList();

private boolean mDesiredPinLocked;
private boolean mDesiredFdnEnabled;
//Synthetic comment -- @@ -73,7 +78,7 @@
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";

    protected static final int EVENT_ICC_LOCKED_OR_ABSENT = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
private static final int EVENT_PINPUK_DONE = 4;
//Synthetic comment -- @@ -84,9 +89,12 @@
private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;

/*
      UNKNOWN is a transient state, for example, after uesr inputs ICC pin under
PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
turns to READY
*/
//Synthetic comment -- @@ -113,25 +121,16 @@
*/
case RADIO_OFF:
case RADIO_UNAVAILABLE:
                case SIM_NOT_READY:
                case RUIM_NOT_READY:
return State.UNKNOWN;
                case SIM_LOCKED_OR_ABSENT:
                case RUIM_LOCKED_OR_ABSENT:
                    //this should be transient-only
                    return State.UNKNOWN;
                case SIM_READY:
                case RUIM_READY:
                case NV_READY:
                    return State.READY;
                case NV_NOT_READY:
                    return State.ABSENT;
}
} else {
return mState;
}

        Log.e(mLogTag, "IccCard.getState(): case should never be reached");
return State.UNKNOWN;
}

//Synthetic comment -- @@ -198,6 +197,23 @@
mPinLockedRegistrants.remove(h);
}


/**
* Supply the ICC PIN to the ICC
//Synthetic comment -- @@ -399,7 +415,13 @@
oldState = mState;
mIccCardStatus = newCardStatus;
newState = getIccCardState();
        mState = newState;

updateStateProperty();

//Synthetic comment -- @@ -493,9 +515,19 @@
updateStateProperty();
broadcastIccStateChangedIntent(INTENT_VALUE_ICC_NOT_READY, null);
break;
                case EVENT_ICC_READY:
                    //TODO: put facility read in SIM_READY now, maybe in REG_NW
mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
mPhone.mCM.queryFacilityLock (
CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX,
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
//Synthetic comment -- @@ -503,12 +535,6 @@
CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
break;
                case EVENT_ICC_LOCKED_OR_ABSENT:
                    mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
                    mPhone.mCM.queryFacilityLock (
                            CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX,
                            obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
                    break;
case EVENT_GET_ICC_STATUS_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -582,6 +608,22 @@
= ar.exception;
((Message)ar.userObj).sendToTarget();
break;
default:
Log.e(mLogTag, "[IccCard] Unknown Event " + msg.what);
}
//Synthetic comment -- @@ -589,6 +631,10 @@
};

public State getIccCardState() {
if (mIccCardStatus == null) {
Log.e(mLogTag, "[IccCard] IccCardStatus is null");
return IccCard.State.ABSENT;
//Synthetic comment -- @@ -602,27 +648,18 @@
RadioState currentRadioState = mPhone.mCM.getRadioState();
// check radio technology
if( currentRadioState == RadioState.RADIO_OFF         ||
            currentRadioState == RadioState.RADIO_UNAVAILABLE ||
            currentRadioState == RadioState.SIM_NOT_READY     ||
            currentRadioState == RadioState.RUIM_NOT_READY    ||
            currentRadioState == RadioState.NV_NOT_READY      ||
            currentRadioState == RadioState.NV_READY) {
return IccCard.State.NOT_READY;
}

        if( currentRadioState == RadioState.SIM_LOCKED_OR_ABSENT  ||
            currentRadioState == RadioState.SIM_READY             ||
            currentRadioState == RadioState.RUIM_LOCKED_OR_ABSENT ||
            currentRadioState == RadioState.RUIM_READY) {

int index;

// check for CDMA radio technology
            if (currentRadioState == RadioState.RUIM_LOCKED_OR_ABSENT ||
                currentRadioState == RadioState.RUIM_READY) {
index = mIccCardStatus.getCdmaSubscriptionAppIndex();
            }
            else {
index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
}

//Synthetic comment -- @@ -635,15 +672,18 @@

// check if PIN required
if (app.app_state.isPinRequired()) {
return IccCard.State.PIN_REQUIRED;
}
if (app.app_state.isPukRequired()) {
return IccCard.State.PUK_REQUIRED;
}
if (app.app_state.isSubscriptionPersoEnabled()) {
return IccCard.State.NETWORK_LOCKED;
}
if (app.app_state.isAppReady()) {
return IccCard.State.READY;
}
if (app.app_state.isAppNotReady()) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..0381bd1 100644

//Synthetic comment -- @@ -99,6 +99,8 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 803b736..8a14618 100644

//Synthetic comment -- @@ -135,15 +135,19 @@
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
return Phone.PHONE_TYPE_CDMA;

case RILConstants.NETWORK_MODE_WCDMA_PREF:
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
return Phone.PHONE_TYPE_GSM;

case RILConstants.NETWORK_MODE_GLOBAL:
return Phone.PHONE_TYPE_CDMA;
default:
return Phone.PHONE_TYPE_GSM;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 5e7dcb0..6e97a77 100644

//Synthetic comment -- @@ -20,21 +20,19 @@
import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.gsm.GsmDataConnection;
import com.android.internal.telephony.test.SimulatedRadioControl;

import java.util.List;
//Synthetic comment -- @@ -43,15 +41,18 @@
public final static Object lockForRadioTechnologyChange = new Object();

private Phone mActivePhone;
    private String mOutgoingPhone;
    private CommandsInterface mCommandsInterface;
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;

private boolean mResetModemOnRadioTechnologyChange = false;

    private static final int EVENT_RADIO_TECHNOLOGY_CHANGED = 1;
private static final String LOG_TAG = "PHONE";

//***** Class Methods
//Synthetic comment -- @@ -64,84 +65,42 @@
mIccPhoneBookInterfaceManagerProxy = new IccPhoneBookInterfaceManagerProxy(
phone.getIccPhoneBookInterfaceManager());
mPhoneSubInfoProxy = new PhoneSubInfoProxy(phone.getPhoneSubInfo());
        mCommandsInterface = ((PhoneBase)mActivePhone).mCM;
        mCommandsInterface.registerForRadioTechnologyChanged(
                this, EVENT_RADIO_TECHNOLOGY_CHANGED, null);
}

@Override
public void handleMessage(Message msg) {
switch(msg.what) {
        case EVENT_RADIO_TECHNOLOGY_CHANGED:
            //switch Phone from CDMA to GSM or vice versa
            mOutgoingPhone = ((PhoneBase)mActivePhone).getPhoneName();
            logd("Switching phone from " + mOutgoingPhone + "Phone to " +
                    (mOutgoingPhone.equals("GSM") ? "CDMAPhone" : "GSMPhone") );
            boolean oldPowerState = false; // old power state to off
            if (mResetModemOnRadioTechnologyChange) {
                if (mCommandsInterface.getRadioState().isOn()) {
                    oldPowerState = true;
                    logd("Setting Radio Power to Off");
                    mCommandsInterface.setRadioPower(false, null);
                }
            }

            if(mOutgoingPhone.equals("GSM")) {
                logd("Make a new CDMAPhone and destroy the old GSMPhone.");

                ((GSMPhone)mActivePhone).dispose();
                Phone oldPhone = mActivePhone;

                //Give the garbage collector a hint to start the garbage collection asap
                // NOTE this has been disabled since radio technology change could happen during
                //   e.g. a multimedia playing and could slow the system. Tests needs to be done
                //   to see the effects of the GC call here when system is busy.
                //System.gc();

                mActivePhone = PhoneFactory.getCdmaPhone();
                ((GSMPhone)oldPhone).removeReferences();
                oldPhone = null;
            } else {
                logd("Make a new GSMPhone and destroy the old CDMAPhone.");

                ((CDMAPhone)mActivePhone).dispose();
                //mActivePhone = null;
                Phone oldPhone = mActivePhone;

                // Give the GC a hint to start the garbage collection asap
                // NOTE this has been disabled since radio technology change could happen during
                //   e.g. a multimedia playing and could slow the system. Tests needs to be done
                //   to see the effects of the GC call here when system is busy.
                //System.gc();

                mActivePhone = PhoneFactory.getGsmPhone();
                ((CDMAPhone)oldPhone).removeReferences();
                oldPhone = null;
            }

            if (mResetModemOnRadioTechnologyChange) {
                logd("Resetting Radio");
                mCommandsInterface.setRadioPower(oldPowerState, null);
            }

            //Set the new interfaces in the proxy's
            mIccSmsInterfaceManagerProxy.setmIccSmsInterfaceManager(
                    mActivePhone.getIccSmsInterfaceManager());
            mIccPhoneBookInterfaceManagerProxy.setmIccPhoneBookInterfaceManager(
                    mActivePhone.getIccPhoneBookInterfaceManager());
            mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());
            mCommandsInterface = ((PhoneBase)mActivePhone).mCM;

            //Send an Intent to the PhoneApp that we had a radio technology change
            Intent intent = new Intent(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            intent.putExtra(Phone.PHONE_NAME_KEY, mActivePhone.getPhoneName());
            ActivityManagerNative.broadcastStickyIntent(intent, null);
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


public ServiceState getServiceState() {
return mActivePhone.getServiceState();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index a9a4be2..1396123 100644

//Synthetic comment -- @@ -592,14 +592,18 @@
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
mPhoneType = RILConstants.GSM_PHONE;
break;
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
mPhoneType = RILConstants.CDMA_PHONE;
break;
case RILConstants.NETWORK_MODE_GLOBAL:
mPhoneType = RILConstants.CDMA_PHONE;
break;
default:
//Synthetic comment -- @@ -633,6 +637,33 @@

//***** CommandsInterface implementation

@Override public void
setOnNITZTime(Handler h, int what, Object obj) {
super.setOnNITZTime(h, what, obj);
//Synthetic comment -- @@ -1968,14 +1999,7 @@
switch(stateInt) {
case 0: state = RadioState.RADIO_OFF; break;
case 1: state = RadioState.RADIO_UNAVAILABLE; break;
            case 2: state = RadioState.SIM_NOT_READY; break;
            case 3: state = RadioState.SIM_LOCKED_OR_ABSENT; break;
            case 4: state = RadioState.SIM_READY; break;
            case 5: state = RadioState.RUIM_NOT_READY; break;
            case 6: state = RadioState.RUIM_READY; break;
            case 7: state = RadioState.RUIM_LOCKED_OR_ABSENT; break;
            case 8: state = RadioState.NV_NOT_READY; break;
            case 9: state = RadioState.NV_READY; break;

default:
throw new RuntimeException(
//Synthetic comment -- @@ -2211,6 +2235,9 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
default:
throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
//break;
//Synthetic comment -- @@ -2352,6 +2379,9 @@
case RIL_UNSOL_OEM_HOOK_RAW: ret = responseRaw(p); break;
case RIL_UNSOL_RINGBACK_TONE: ret = responseInts(p); break;
case RIL_UNSOL_RESEND_INCALL_MUTE: ret = responseVoid(p); break;

default:
throw new RuntimeException("Unrecognized unsol response: " + response);
//Synthetic comment -- @@ -2369,7 +2399,25 @@
if (RILJ_LOGD) unsljLogMore(response, newState.toString());

switchToRadioState(newState);
            break;
case RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED:
if (RILJ_LOGD) unsljLog(response);

//Synthetic comment -- @@ -3264,6 +3312,9 @@
case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
default: return "<unknown request>";
}
}
//Synthetic comment -- @@ -3308,7 +3359,10 @@
case RIL_UNSOL_OEM_HOOK_RAW: return "UNSOL_OEM_HOOK_RAW";
case RIL_UNSOL_RINGBACK_TONE: return "UNSOL_RINGBACK_TONG";
case RIL_UNSOL_RESEND_INCALL_MUTE: return "UNSOL_RESEND_INCALL_MUTE";
            default: return "<unknown reponse>";
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 71a80e0..dd0d229 100644

//Synthetic comment -- @@ -66,6 +66,10 @@
int NETWORK_MODE_EVDO_NO_CDMA   = 6; /* EvDo only */
int NETWORK_MODE_GLOBAL         = 7; /* GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL)
AVAILABLE Application Settings menu*/
int PREFERRED_NETWORK_MODE      = NETWORK_MODE_WCDMA_PREF;

/* CDMA subscription source. See ril.h RIL_REQUEST_CDMA_SET_SUBSCRIPTION */
//Synthetic comment -- @@ -236,6 +240,9 @@
int RIL_REQUEST_SET_SMSC_ADDRESS = 101;
int RIL_REQUEST_REPORT_SMS_MEMORY_STATUS = 102;
int RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING = 103;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -268,4 +275,7 @@
int RIL_UNSOL_OEM_HOOK_RAW = 1028;
int RIL_UNSOL_RINGBACK_TONE = 1029;
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..6146578 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
protected static final int DATA_ACCESS_HSUPA = 10;
protected static final int DATA_ACCESS_HSPA = 11;
protected static final int DATA_ACCESS_CDMA_EvDo_B = 12;

protected CommandsInterface cm;

//Synthetic comment -- @@ -120,6 +122,12 @@
protected static final int EVENT_ERI_FILE_LOADED                   = 36;
protected static final int EVENT_OTA_PROVISION_STATUS_CHANGE       = 37;
protected static final int EVENT_SET_RADIO_POWER_OFF               = 38;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
old mode 100755
new mode 100644
//Synthetic comment -- index 1c81a07..1ecce12

//Synthetic comment -- @@ -103,6 +103,7 @@
CdmaServiceStateTracker mSST;
RuimRecords mRuimRecords;
RuimCard mRuimCard;
ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
//Synthetic comment -- @@ -150,13 +151,13 @@
super(notifier, context, ci, unitTestMode);

mCM.setPhoneType(Phone.PHONE_TYPE_CDMA);
mCT = new CdmaCallTracker(this);
mSST = new CdmaServiceStateTracker (this);
mSMS = new CdmaSMSDispatcher(this);
mIccFileHandler = new RuimFileHandler(this);
mRuimRecords = new RuimRecords(this);
mDataConnection = new CdmaDataConnectionTracker (this);
        mRuimCard = new RuimCard(this);
mRuimPhoneBookInterfaceManager = new RuimPhoneBookInterfaceManager(this);
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
//Synthetic comment -- @@ -170,9 +171,10 @@
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
        mCM.registerForNVReady(this, EVENT_NV_READY, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOG_TAG);
//Synthetic comment -- @@ -220,9 +222,9 @@
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
            mCM.unregisterForNVReady(this); //EVENT_NV_READY
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);
removeCallbacks(mExitEcmRunnable);

mPendingMmis.clear();
//Synthetic comment -- @@ -889,7 +891,8 @@
|| cdmaMin.substring(0,6).equals(UNACTIVATED_MIN2_VALUE))
|| SystemProperties.getBoolean("test_cdma_setup", false);
}
        if (DBG) Log.d(LOG_TAG, "needsOtaServiceProvisioning: ret=" + needsProvisioning);
return needsProvisioning;
}

//Synthetic comment -- @@ -1044,6 +1047,13 @@

case EVENT_RADIO_ON:{
Log.d(LOG_TAG, "Event EVENT_RADIO_ON Received");
}
break;

//Synthetic comment -- @@ -1057,6 +1067,21 @@
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
                } else if (phone.mCM.getRadioState() != CommandsInterface.RadioState.NV_READY
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
        p.mCM.registerForNVReady(this, EVENT_NV_READY, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -175,6 +174,8 @@
p.mSST.registerForRoamingOn(this, EVENT_ROAMING_ON, null);
p.mSST.registerForRoamingOff(this, EVENT_ROAMING_OFF, null);
p.mCM.registerForCdmaOtaProvision(this, EVENT_CDMA_OTA_PROVISION, null);

IntentFilter filter = new IntentFilter();
filter.addAction(INTENT_RECONNECT_ALARM);
//Synthetic comment -- @@ -225,7 +226,6 @@
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
mCdmaPhone.mRuimRecords.unregisterForRecordsLoaded(this);
        phone.mCM.unregisterForNVReady(this);
phone.mCM.unregisterForDataStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -233,6 +233,7 @@
mCdmaPhone.mSST.unregisterForCdmaDataConnectionDetached(this);
mCdmaPhone.mSST.unregisterForRoamingOn(this);
mCdmaPhone.mSST.unregisterForRoamingOff(this);
phone.mCM.unregisterForCdmaOtaProvision(this);

phone.getContext().unregisterReceiver(this.mIntentReceiver);
//Synthetic comment -- @@ -294,7 +295,7 @@
public boolean isDataConnectionAsDesired() {
boolean roaming = phone.getServiceState().getRoaming();

        if (((phone.mCM.getRadioState() == CommandsInterface.RadioState.NV_READY) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded()) &&
(mCdmaPhone.mSST.getCurrentCdmaDataConnectionState() ==
ServiceState.STATE_IN_SERVICE) &&
//Synthetic comment -- @@ -329,7 +330,7 @@

if ((state == State.IDLE || state == State.SCANNING)
&& (psState == ServiceState.STATE_IN_SERVICE)
                && ((phone.mCM.getRadioState() == CommandsInterface.RadioState.NV_READY) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded())
&& (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
//Synthetic comment -- @@ -345,8 +346,8 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" PS state=" + psState +
                    " radio state=" + phone.mCM.getRadioState() +
                    " ruim=" + mCdmaPhone.mRuimRecords.getRecordsLoaded() +
" concurrentVoice&Data=" + mCdmaPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" dataEnabled=" + getAnyDataEnabled() +
//Synthetic comment -- @@ -998,8 +999,10 @@
onRecordsLoaded();
break;

            case EVENT_NV_READY:
                onNVReady();
break;

case EVENT_CDMA_DATA_DETACHED:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index d2a4bd8..cf3ead3 100755

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.ServiceState;
//Synthetic comment -- @@ -47,9 +48,11 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;

import java.util.Arrays;
import java.util.Calendar;
//Synthetic comment -- @@ -96,6 +99,7 @@
private RegistrantList cdmaDataConnectionAttachedRegistrants = new RegistrantList();
private RegistrantList cdmaDataConnectionDetachedRegistrants = new RegistrantList();
private RegistrantList cdmaForSubscriptionInfoReadyRegistrants = new RegistrantList();

/**
* Sometimes we get the NITZ time before we know what country we
//Synthetic comment -- @@ -136,7 +140,8 @@
private boolean mIsMinInfoReady = false;

private boolean isEriTextLoaded = false;
    private boolean isSubscriptionFromRuim = false;

private boolean mPendingRadioPowerOffAfterDataOff = false;

//Synthetic comment -- @@ -171,16 +176,14 @@
(PowerManager)phone.getContext().getSystemService(Context.POWER_SERVICE);
mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);

        cm.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
cm.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, null);

cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED_CDMA, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);

        cm.registerForRUIMReady(this, EVENT_RUIM_READY, null);

        cm.registerForNVReady(this, EVENT_NV_READY, null);
phone.registerForEriFileLoaded(this, EVENT_ERI_FILE_LOADED, null);
cm.registerForCdmaOtaProvision(this,EVENT_OTA_PROVISION_STATUS_CHANGE, null);

//Synthetic comment -- @@ -198,17 +201,18 @@

public void dispose() {
// Unregister for all events.
        cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);
        cm.unregisterForRUIMReady(this);
        cm.unregisterForNVReady(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
phone.mRuimRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
}

@Override
//Synthetic comment -- @@ -286,48 +290,127 @@
cdmaForSubscriptionInfoReadyRegistrants.remove(h);
}

@Override
public void handleMessage (Message msg) {
AsyncResult ar;
int[] ints;
String[] strings;

switch (msg.what) {
        case EVENT_RADIO_AVAILABLE:
break;

case EVENT_RUIM_READY:
// The RUIM is now ready i.e if it was locked it has been
// unlocked. At this stage, the radio is already powered on.
            isSubscriptionFromRuim = true;
if (mNeedToRegForRuimLoaded) {
phone.mRuimRecords.registerForRecordsLoaded(this,
EVENT_RUIM_RECORDS_LOADED, null);
mNeedToRegForRuimLoaded = false;
}

            cm.getCDMASubscription(obtainMessage(EVENT_POLL_STATE_CDMA_SUBSCRIPTION));
if (DBG) log("Receive EVENT_RUIM_READY and Send Request getCDMASubscription.");

            // Restore the previous network selection.
            pollState();

            // Signal strength polling stops when radio is off.
            queueNextSignalStrengthPoll();
break;

case EVENT_NV_READY:
            isSubscriptionFromRuim = false;
// For Non-RUIM phones, the subscription information is stored in
// Non Volatile. Here when Non-Volatile is ready, we can poll the CDMA
// subscription info.
            cm.getCDMASubscription( obtainMessage(EVENT_POLL_STATE_CDMA_SUBSCRIPTION));
            pollState();
            // Signal strength polling stops when radio is off.
            queueNextSignalStrengthPoll();
break;

case EVENT_RADIO_STATE_CHANGED:
// This will do nothing in the 'radio not available' case.
setPowerStateToDesired();
pollState();
//Synthetic comment -- @@ -341,7 +424,7 @@
// This callback is called when signal strength is polled
// all by itself.

            if (!(cm.getRadioState().isOn()) || (cm.getRadioState().isGsm())) {
// Polling will continue when radio turns back on.
return;
}
//Synthetic comment -- @@ -410,7 +493,7 @@

if (ar.exception == null) {
String cdmaSubscription[] = (String[])ar.result;
                if (cdmaSubscription != null && cdmaSubscription.length >= 5) {
mMdn = cdmaSubscription[0];
if (cdmaSubscription[1] != null) {
String[] sid = cdmaSubscription[1].split(",");
//Synthetic comment -- @@ -611,7 +694,7 @@
String plmn = "";
boolean showPlmn = false;
int rule = 0;
        if (cm.getRadioState().isRUIMReady()) {
// TODO RUIM SPN is not implemented, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
//Synthetic comment -- @@ -793,7 +876,7 @@
String opNames[] = (String[])ar.result;

if (opNames != null && opNames.length >= 3) {
                    if (cm.getRadioState().isNVReady()) {
// In CDMA in case on NV, the ss.mOperatorAlphaLong is set later with the
// ERI text, so here it is ignored what is coming from the modem.
newSS.setOperatorName(null, opNames[1], opNames[2]);
//Synthetic comment -- @@ -917,18 +1000,6 @@
pollStateDone();
break;

        case SIM_NOT_READY:
        case SIM_LOCKED_OR_ABSENT:
        case SIM_READY:
            log("Radio Technology Change ongoing, setting SS to off");
            newSS.setStateOff();
            newCellLoc.setStateInvalid();
            setSignalStrengthDefaultValues();
            mGotCountryCode = false;

            // NOTE: pollStateDone() is not needed in this case
            break;

default:
// Issue all poll-related commands at once, then count
// down the responses which are allowed to arrive
//Synthetic comment -- @@ -1081,7 +1152,7 @@
}

if (hasChanged) {
            if (cm.getRadioState().isNVReady()) {
String eriText;
// Now the CDMAPhone sees the new ServiceState so it can get the new ERI text
if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
//Synthetic comment -- @@ -1197,7 +1268,7 @@
*/
private void
queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength || (cm.getRadioState().isGsm())) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimCard.java b/telephony/java/com/android/internal/telephony/cdma/RuimCard.java
//Synthetic comment -- index 734badd..0817d26 100644

//Synthetic comment -- @@ -27,18 +27,22 @@

RuimCard(CDMAPhone phone) {
super(phone, "CDMA", true);
        mPhone.mCM.registerForRUIMLockedOrAbsent(mHandler, EVENT_ICC_LOCKED_OR_ABSENT, null);
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForRUIMReady(mHandler, EVENT_ICC_READY, null);
updateStateProperty();
}

@Override
public void dispose() {
//Unregister for all events
        mPhone.mCM.unregisterForRUIMLockedOrAbsent(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForRUIMReady(mHandler);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..d7d15fc 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
recordsToLoad = 0;


        p.mCM.registerForRUIMReady(this, EVENT_RUIM_READY, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
// NOTE the EVENT_SMS_ON_RUIM is not registered
p.mCM.setOnIccRefresh(this, EVENT_RUIM_REFRESH, null);
//Synthetic comment -- @@ -96,7 +96,7 @@

public void dispose() {
//Unregister for all events
        phone.mCM.unregisterForRUIMReady(this);
phone.mCM.unregisterForOffOrNotAvailable( this);
phone.mCM.unSetOnIccRefresh(this);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..99728bd 100644

//Synthetic comment -- @@ -142,13 +142,14 @@
}

mCM.setPhoneType(Phone.PHONE_TYPE_GSM);
mCT = new GsmCallTracker(this);
mSST = new GsmServiceStateTracker (this);
mSMS = new GsmSMSDispatcher(this);
mIccFileHandler = new SIMFileHandler(this);
mSIMRecords = new SIMRecords(this);
mDataConnection = new GsmDataConnectionTracker (this);
        mSimCard = new SimCard(this);
if (!unitTestMode) {
mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
mSimSmsIntManager = new SimSmsInterfaceManager(this);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..1dc46d7 100644

//Synthetic comment -- @@ -201,13 +201,13 @@
mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);

cm.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
cm.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, null);

cm.registerForNetworkStateChanged(this, EVENT_NETWORK_STATE_CHANGED, null);
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);
        cm.registerForSIMReady(this, EVENT_SIM_READY, null);

// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
//Synthetic comment -- @@ -231,9 +231,10 @@
public void dispose() {
// Unregister for all events.
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);
        cm.unregisterForSIMReady(this);

phone.mSIMRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
//Synthetic comment -- @@ -343,12 +344,21 @@
String[] strings;
Message message;

switch (msg.what) {
case EVENT_RADIO_AVAILABLE:
//this is unnecessary
//setPowerStateToDesired();
break;

case EVENT_SIM_READY:
// The SIM is now ready i.e if it was locked
// it has been unlocked. At this stage, the radio is already
//Synthetic comment -- @@ -380,7 +390,7 @@
// This callback is called when signal strength is polled
// all by itself

                if (!(cm.getRadioState().isOn()) || (cm.getRadioState().isCdma())) {
// Polling will continue when radio turns back on and not CDMA
return;
}
//Synthetic comment -- @@ -777,20 +787,6 @@
pollStateDone();
break;

            case RUIM_NOT_READY:
            case RUIM_READY:
            case RUIM_LOCKED_OR_ABSENT:
            case NV_NOT_READY:
            case NV_READY:
                Log.d(LOG_TAG, "Radio Technology Change ongoing, setting SS to off");
                newSS.setStateOff();
                newCellLoc.setStateInvalid();
                setSignalStrengthDefaultValues();
                mGotCountryCode = false;

                //NOTE: pollStateDone() is not needed in this case
                break;

default:
// Issue all poll-related commands at once
// then count down the responses, which
//Synthetic comment -- @@ -842,6 +838,9 @@
case DATA_ACCESS_HSPA:
ret = "HSPA";
break;
default:
Log.e(LOG_TAG, "Wrong network type: " + Integer.toString(type));
break;
//Synthetic comment -- @@ -1091,7 +1090,7 @@
}

private void queueNextSignalStrengthPoll() {
        if (dontPollSignalStrength || (cm.getRadioState().isCdma())) {
// The radio is telling us about signal strength changes
// we don't have to ask it
return;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..66aa7ce 100644

//Synthetic comment -- @@ -160,7 +160,7 @@
recordsToLoad = 0;


        p.mCM.registerForSIMReady(this, EVENT_SIM_READY, null);
p.mCM.registerForOffOrNotAvailable(
this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
//Synthetic comment -- @@ -173,7 +173,7 @@

public void dispose() {
//Unregister for all events
        phone.mCM.unregisterForSIMReady(this);
phone.mCM.unregisterForOffOrNotAvailable( this);
phone.mCM.unSetOnIccRefresh(this);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimCard.java b/telephony/java/com/android/internal/telephony/gsm/SimCard.java
//Synthetic comment -- index 835cb29..174b0f4 100644

//Synthetic comment -- @@ -28,18 +28,18 @@
SimCard(GSMPhone phone) {
super(phone, "GSM", true);

        mPhone.mCM.registerForSIMLockedOrAbsent(mHandler, EVENT_ICC_LOCKED_OR_ABSENT, null);
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForSIMReady(mHandler, EVENT_ICC_READY, null);
updateStateProperty();
}

@Override
public void dispose() {
//Unregister for all events
        mPhone.mCM.unregisterForSIMLockedOrAbsent(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForSIMReady(mHandler);
}

@Override








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index a120f52..fdef66e 100644

//Synthetic comment -- @@ -122,9 +122,9 @@

if (pin != null && pin.equals(mPinCode)) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin: success!");
            setRadioState(RadioState.SIM_READY);
mPinUnlockAttempts = 0;
mSimLockedState = SimLockState.NONE;

if (result != null) {
AsyncResult.forMessage(result, null, null);
//Synthetic comment -- @@ -164,9 +164,9 @@

if (puk != null && puk.equals(SIM_PUK_CODE)) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: success!");
            setRadioState(RadioState.SIM_READY);
mSimLockedState = SimLockState.NONE;
mPukUnlockAttempts = 0;

if (result != null) {
AsyncResult.forMessage(result, null, null);
//Synthetic comment -- @@ -446,11 +446,11 @@
*      The ar.result List is sorted by DriverCall.index
*/
public void getCurrentCalls (Message result) {
        if (mState == RadioState.SIM_READY) {
//Log.i("GSM", "[SimCmds] getCurrentCalls");
resultSuccess(result, simulatedCallState.getDriverCalls());
} else {
            //Log.i("GSM", "[SimCmds] getCurrentCalls: SIM not ready!");
resultFail(result,
new CommandException(
CommandException.Error.RADIO_NOT_AVAILABLE));
//Synthetic comment -- @@ -1024,14 +1024,7 @@

public void setRadioPower(boolean on, Message result) {
if(on) {
            if (isSimLocked()) {
                Log.i("SIM", "[SimCmd] setRadioPower: SIM locked! state=" +
                        mSimLockedState);
                setRadioState(RadioState.SIM_LOCKED_OR_ABSENT);
            }
            else {
                setRadioState(RadioState.SIM_READY);
            }
} else {
setRadioState(RadioState.RADIO_OFF);
}
//Synthetic comment -- @@ -1474,4 +1467,20 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}
}







