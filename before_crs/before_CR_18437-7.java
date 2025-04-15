/*Telephony: Add support for Uicc

Change-Id:Ic8a6e13774bcb66cf3a8a54dc673b095832e9f9e*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index a175d49..4b3d5f8 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -33,7 +33,7 @@
public final class AdnRecordCache extends Handler implements IccConstants {
//***** Instance Variables

    PhoneBase phone;
private UsimPhoneBookManager mUsimPhoneBookManager;

// Indexed by EF ID
//Synthetic comment -- @@ -56,9 +56,9 @@



    public AdnRecordCache(PhoneBase phone) {
        this.phone = phone;
        mUsimPhoneBookManager = new UsimPhoneBookManager(phone, this);
}

//***** Called from SIMRecords
//Synthetic comment -- @@ -155,7 +155,7 @@

userWriteResponse.put(efid, response);

        new AdnRecordLoader(phone).updateEF(adn, efid, extensionEF,
recordIndex, pin2,
obtainMessage(EVENT_UPDATE_ADN_DONE, efid, recordIndex, adn));
}
//Synthetic comment -- @@ -233,7 +233,7 @@

userWriteResponse.put(efid, response);

        new AdnRecordLoader(phone).updateEF(newAdn, efid, extensionEF,
index, pin2,
obtainMessage(EVENT_UPDATE_ADN_DONE, efid, index, newAdn));
}
//Synthetic comment -- @@ -296,7 +296,7 @@
return;
}

        new AdnRecordLoader(phone).loadAllFromEF(efid, extensionEf,
obtainMessage(EVENT_LOAD_ALL_ADN_LIKE_DONE, efid, 0));
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordLoader.java b/telephony/java/com/android/internal/telephony/AdnRecordLoader.java
//Synthetic comment -- index 55bdc06..ab785b3 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -20,16 +20,17 @@

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class AdnRecordLoader extends Handler {
    static String LOG_TAG;

//***** Instance Variables

    PhoneBase phone;
int ef;
int extensionEF;
int pendingExtLoads;
//Synthetic comment -- @@ -56,13 +57,11 @@

//***** Constructor

    public AdnRecordLoader(PhoneBase phone) {
// The telephony unit-test cases may create AdnRecords
// in secondary threads
        super(phone.getHandler().getLooper());

        this.phone = phone;
        LOG_TAG = phone.getPhoneName();
}

/**
//Synthetic comment -- @@ -77,7 +76,7 @@
this.recordNumber = recordNumber;
this.userResponse = response;

        phone.mIccFileHandler.loadEFLinearFixed(
ef, recordNumber,
obtainMessage(EVENT_ADN_LOAD_DONE));

//Synthetic comment -- @@ -95,7 +94,7 @@
this.extensionEF = extensionEF;
this.userResponse = response;

        phone.mIccFileHandler.loadEFLinearFixedAll(
ef,
obtainMessage(EVENT_ADN_LOAD_ALL_DONE));

//Synthetic comment -- @@ -106,7 +105,7 @@
* It will get the record size of EF record and compose hex adn array
* then write the hex array to EF record
*
     * @param adn is set with alphaTag and phone number
* @param ef EF fileid
* @param extensionEF extension EF fileid
* @param recordNumber 1-based record index
//Synthetic comment -- @@ -122,7 +121,7 @@
this.userResponse = response;
this.pin2 = pin2;

        phone.mIccFileHandler.getEFLinearRecordSize( ef,
obtainMessage(EVENT_EF_LINEAR_RECORD_SIZE_DONE, adn));
}

//Synthetic comment -- @@ -163,7 +162,7 @@
ar.exception);
}

                    phone.mIccFileHandler.updateEFLinearFixed(ef, recordNumber,
data, pin2, obtainMessage(EVENT_UPDATE_RECORD_DONE));

pendingExtLoads = 1;
//Synthetic comment -- @@ -203,7 +202,7 @@

pendingExtLoads = 1;

                        phone.mIccFileHandler.loadEFLinearFixed(
extensionEF, adn.extRecord,
obtainMessage(EVENT_EXT_RECORD_LOAD_DONE, adn));
}
//Synthetic comment -- @@ -253,7 +252,7 @@

pendingExtLoads++;

                            phone.mIccFileHandler.loadEFLinearFixed(
extensionEF, adn.extRecord,
obtainMessage(EVENT_EXT_RECORD_LOAD_DONE, adn));
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 2fbff63..8270b14 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -587,7 +587,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPin(String pin, Message result);

/**
* Supply the ICC PUK to the ICC card
//Synthetic comment -- @@ -601,7 +601,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPuk(String puk, String newPin, Message result);

/**
* Supply the ICC PIN2 to the ICC card
//Synthetic comment -- @@ -617,7 +617,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPin2(String pin2, Message result);

/**
* Supply the SIM PUK2 to the SIM card
//Synthetic comment -- @@ -633,10 +633,10 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPuk2(String puk2, String newPin2, Message result);

    void changeIccPin(String oldPin, String newPin, Message result);
    void changeIccPin2(String oldPin2, String newPin2, Message result);

void changeBarringPassword(String facility, String oldPwd, String newPwd, Message result);

//Synthetic comment -- @@ -705,7 +705,7 @@
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
    void getIMSI(Message result);

/**
*  returned message
//Synthetic comment -- @@ -1004,7 +1004,7 @@
* response.obj will be an AsyncResult
* response.obj.userObj will be a IccIoResult on success
*/
    void iccIO (int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, Message response);

/**
//Synthetic comment -- @@ -1113,7 +1113,7 @@
* @param response is callback message
*/

    void queryFacilityLock (String facility, String password, int serviceClass,
Message response);

/**
//Synthetic comment -- @@ -1123,7 +1123,7 @@
* @param serviceClass is a sum of SERVICE_CLASS_*
* @param response is callback message
*/
    void setFacilityLock (String facility, boolean lockState, String password,
int serviceClass, Message response);










//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CsimFileHandler.java b/telephony/java/com/android/internal/telephony/CsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..a30adfa

//Synthetic comment -- @@ -0,0 +1,58 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 3c2b871..06a5f66 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -28,6 +28,9 @@

import java.util.ArrayList;

/**
* {@hide}
*
//Synthetic comment -- @@ -101,6 +104,7 @@
protected static final int EVENT_RESTART_RADIO = 36;
protected static final int EVENT_SET_MASTER_DATA_ENABLE = 37;
protected static final int EVENT_RESET_DONE = 38;

/***** Constants *****/

//Synthetic comment -- @@ -187,6 +191,14 @@

/** CID of active data connection */
protected int cidActive;

/**
* Default constructor
//Synthetic comment -- @@ -194,6 +206,9 @@
protected DataConnectionTracker(PhoneBase phone) {
super();
this.phone = phone;
}

public abstract void dispose();
//Synthetic comment -- @@ -335,6 +350,10 @@
onResetDone((AsyncResult) msg.obj);
break;

default:
Log.e("DATA", "Unidentified event = " + msg.what);
break;
//Synthetic comment -- @@ -578,5 +597,29 @@
}
}


}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 182a989..f6cc9cf 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,47 +16,14 @@

package com.android.internal.telephony;

import static android.Manifest.permission.READ_PHONE_STATE;
import android.app.ActivityManagerNative;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;

/**
* {@hide}
*/
public abstract class IccCard {
    protected String mLogTag;
    protected boolean mDbg;

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
    private boolean mIccPinLocked = true; // Default to locked
    private boolean mIccFdnEnabled = false; // Default to disabled.
                                            // Will be updated when SIM_READY.


/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
static public final String INTENT_KEY_ICC_STATE = "ss";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
//Synthetic comment -- @@ -80,20 +47,6 @@
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";

    protected static final int EVENT_ICC_STATUS_CHANGED = 1;
    private static final int EVENT_GET_ICC_STATUS_DONE = 2;
    protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
    private static final int EVENT_PINPUK_DONE = 4;
    private static final int EVENT_REPOLL_STATUS_DONE = 5;
    protected static final int EVENT_ICC_READY = 6;
    private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 7;
    private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 8;
    private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;
    protected static final int EVENT_RADIO_ON = 12;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 13;

/*
UNKNOWN is a transient state, for example, after user inputs ICC pin under
PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
//Synthetic comment -- @@ -113,110 +66,30 @@
}
}

    public State getState() {
        if (mState == null) {
            switch(mPhone.mCM.getRadioState()) {
                /* This switch block must not return anything in
                 * State.isLocked() or State.ABSENT.
                 * If it does, handleSimStatus() may break
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

    public IccCard(PhoneBase phone, String logTag, Boolean dbg) {
        mPhone = phone;
        mLogTag = logTag;
        mDbg = dbg;
    }

    abstract public void dispose();

    protected void finalize() {
        if(mDbg) Log.d(mLogTag, "IccCard finalized");
    }

/**
* Notifies handler of any transition into State.ABSENT
*/
    public void registerForAbsent(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mAbsentRegistrants.add(r);

        if (getState() == State.ABSENT) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForAbsent(Handler h) {
        mAbsentRegistrants.remove(h);
    }

/**
* Notifies handler of any transition into State.NETWORK_LOCKED
*/
    public void registerForNetworkLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mNetworkLockedRegistrants.add(r);

        if (getState() == State.NETWORK_LOCKED) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForNetworkLocked(Handler h) {
        mNetworkLockedRegistrants.remove(h);
    }

/**
* Notifies handler of any transition into State.isPinLocked()
*/
    public void registerForLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mPinLockedRegistrants.add(r);

        if (getState().isPinLocked()) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForLocked(Handler h) {
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
//Synthetic comment -- @@ -239,30 +112,16 @@
*
*/

    public void supplyPin (String pin, Message onComplete) {
        mPhone.mCM.supplyIccPin(pin, mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
    }

    public void supplyPuk (String puk, String newPin, Message onComplete) {
        mPhone.mCM.supplyIccPuk(puk, newPin,
                mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
    }

    public void supplyPin2 (String pin2, Message onComplete) {
        mPhone.mCM.supplyIccPin2(pin2,
                mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
    }

    public void supplyPuk2 (String puk2, String newPin2, Message onComplete) {
        mPhone.mCM.supplyIccPuk2(puk2, newPin2,
                mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
    }

    public void supplyNetworkDepersonalization (String pin, Message onComplete) {
        if(mDbg) log("Network Despersonalization: " + pin);
        mPhone.mCM.supplyNetworkDepersonalization(pin,
                mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
    }

/**
* Check whether ICC pin lock is enabled
//Synthetic comment -- @@ -271,9 +130,7 @@
* @return true for ICC locked enabled
*         false for ICC locked disabled
*/
    public boolean getIccLockEnabled() {
        return mIccPinLocked;
     }

/**
* Check whether ICC fdn (fixed dialing number) is enabled
//Synthetic comment -- @@ -282,9 +139,7 @@
* @return true for ICC fdn enabled
*         false for ICC fdn disabled
*/
     public boolean getIccFdnEnabled() {
        return mIccFdnEnabled;
     }

/**
* Set the ICC pin lock enabled or disabled
//Synthetic comment -- @@ -297,19 +152,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
     public void setIccLockEnabled (boolean enabled,
             String password, Message onComplete) {
         int serviceClassX;
         serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                 CommandsInterface.SERVICE_CLASS_DATA +
                 CommandsInterface.SERVICE_CLASS_FAX;

         mDesiredPinLocked = enabled;

         mPhone.mCM.setFacilityLock(CommandsInterface.CB_FACILITY_BA_SIM,
                 enabled, password, serviceClassX,
                 mHandler.obtainMessage(EVENT_CHANGE_FACILITY_LOCK_DONE, onComplete));
     }

/**
* Set the ICC fdn enabled or disabled
//Synthetic comment -- @@ -322,21 +165,7 @@
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

         mPhone.mCM.setFacilityLock(CommandsInterface.CB_FACILITY_BA_FD,
                 enabled, password, serviceClassX,
                 mHandler.obtainMessage(EVENT_CHANGE_FACILITY_FDN_DONE, onComplete));
     }

/**
* Change the ICC password used in ICC pin lock
* When the operation is complete, onComplete will be sent to its handler
//Synthetic comment -- @@ -348,14 +177,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
     public void changeIccLockPassword(String oldPassword, String newPassword,
             Message onComplete) {
         if(mDbg) log("Change Pin1 old: " + oldPassword + " new: " + newPassword);
         mPhone.mCM.changeIccPin(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

     }

/**
* Change the ICC password used in ICC fdn enable
* When the operation is complete, onComplete will be sent to its handler
//Synthetic comment -- @@ -367,14 +189,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
     public void changeIccFdnPassword(String oldPassword, String newPassword,
             Message onComplete) {
         if(mDbg) log("Change Pin2 old: " + oldPassword + " new: " + newPassword);
         mPhone.mCM.changeIccPin2(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

     }


/**
* Returns service provider name stored in ICC card.
//Synthetic comment -- @@ -392,346 +207,17 @@
*         yet available
*
*/
    public abstract String getServiceProviderName();

    protected void updateStateProperty() {
        mPhone.setSystemProperty(TelephonyProperties.PROPERTY_SIM_STATE, getState().toString());
    }

    private void getIccCardStatusDone(AsyncResult ar) {
        if (ar.exception != null) {
            Log.e(mLogTag,"Error getting ICC status. "
                    + "RIL_REQUEST_GET_ICC_STATUS should "
                    + "never return an error", ar.exception);
            return;
        }
        handleIccCardStatus((IccCardStatus) ar.result);
    }

    private void handleIccCardStatus(IccCardStatus newCardStatus) {
        boolean transitionedIntoPinLocked;
        boolean transitionedIntoAbsent;
        boolean transitionedIntoNetworkLocked;

        State oldState, newState;

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

        transitionedIntoPinLocked = (
                 (oldState != State.PIN_REQUIRED && newState == State.PIN_REQUIRED)
              || (oldState != State.PUK_REQUIRED && newState == State.PUK_REQUIRED));
        transitionedIntoAbsent = (oldState != State.ABSENT && newState == State.ABSENT);
        transitionedIntoNetworkLocked = (oldState != State.NETWORK_LOCKED
                && newState == State.NETWORK_LOCKED);

        if (transitionedIntoPinLocked) {
            if(mDbg) log("Notify SIM pin or puk locked.");
            mPinLockedRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED,
                    (newState == State.PIN_REQUIRED) ?
                       INTENT_VALUE_LOCKED_ON_PIN : INTENT_VALUE_LOCKED_ON_PUK);
        } else if (transitionedIntoAbsent) {
            if(mDbg) log("Notify SIM missing.");
            mAbsentRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(INTENT_VALUE_ICC_ABSENT, null);
        } else if (transitionedIntoNetworkLocked) {
            if(mDbg) log("Notify SIM network locked.");
            mNetworkLockedRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED,
                  INTENT_VALUE_LOCKED_NETWORK);
        }
    }

    /**
     * Interperate EVENT_QUERY_FACILITY_LOCK_DONE
     * @param ar is asyncResult of Query_Facility_Locked
     */
    private void onQueryFdnEnabled(AsyncResult ar) {
        if(ar.exception != null) {
            if(mDbg) log("Error in querying facility lock:" + ar.exception);
            return;
        }

        int[] ints = (int[])ar.result;
        if(ints.length != 0) {
            mIccFdnEnabled = (0!=ints[0]);
            if(mDbg) log("Query facility lock : "  + mIccFdnEnabled);
        } else {
            Log.e(mLogTag, "[IccCard] Bogus facility lock response");
        }
    }

    /**
     * Interperate EVENT_QUERY_FACILITY_LOCK_DONE
     * @param ar is asyncResult of Query_Facility_Locked
     */
    private void onQueryFacilityLock(AsyncResult ar) {
        if(ar.exception != null) {
            if (mDbg) log("Error in querying facility lock:" + ar.exception);
            return;
        }

        int[] ints = (int[])ar.result;
        if(ints.length != 0) {
            mIccPinLocked = (0!=ints[0]);
            if(mDbg) log("Query facility lock : "  + mIccPinLocked);
        } else {
            Log.e(mLogTag, "[IccCard] Bogus facility lock response");
        }
    }

    public void broadcastIccStateChangedIntent(String value, String reason) {
        Intent intent = new Intent(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(Phone.PHONE_NAME_KEY, mPhone.getPhoneName());
        intent.putExtra(INTENT_KEY_ICC_STATE, value);
        intent.putExtra(INTENT_KEY_LOCKED_REASON, reason);
        if(mDbg) log("Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value
                + " reason " + reason);
        ActivityManagerNative.broadcastStickyIntent(intent, READ_PHONE_STATE);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            AsyncResult ar;
            int serviceClassX;

            serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                            CommandsInterface.SERVICE_CLASS_DATA +
                            CommandsInterface.SERVICE_CLASS_FAX;

            switch (msg.what) {
                case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                    mState = null;
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
                    mPhone.mCM.queryFacilityLock (
                            CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
                            obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
                    break;
                case EVENT_GET_ICC_STATUS_DONE:
                    ar = (AsyncResult)msg.obj;

                    getIccCardStatusDone(ar);
                    break;
                case EVENT_PINPUK_DONE:
                    // a PIN/PUK/PIN2/PUK2/Network Personalization
                    // request has completed. ar.userObj is the response Message
                    // Repoll before returning
                    ar = (AsyncResult)msg.obj;
                    // TODO should abstract these exceptions
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    mPhone.mCM.getIccCardStatus(
                        obtainMessage(EVENT_REPOLL_STATUS_DONE, ar.userObj));
                    break;
                case EVENT_REPOLL_STATUS_DONE:
                    // Finished repolling status after PIN operation
                    // ar.userObj is the response messaeg
                    // ar.userObj.obj is already an AsyncResult with an
                    // appropriate exception filled in if applicable

                    ar = (AsyncResult)msg.obj;
                    getIccCardStatusDone(ar);
                    ((Message)ar.userObj).sendToTarget();
                    break;
                case EVENT_QUERY_FACILITY_LOCK_DONE:
                    ar = (AsyncResult)msg.obj;
                    onQueryFacilityLock(ar);
                    break;
                case EVENT_QUERY_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;
                    onQueryFdnEnabled(ar);
                    break;
                case EVENT_CHANGE_FACILITY_LOCK_DONE:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        mIccPinLocked = mDesiredPinLocked;
                        if (mDbg) log( "EVENT_CHANGE_FACILITY_LOCK_DONE: " +
                                "mIccPinLocked= " + mIccPinLocked);
                    } else {
                        Log.e(mLogTag, "Error change facility lock with exception "
                            + ar.exception);
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
                case EVENT_CHANGE_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception == null) {
                        mIccFdnEnabled = mDesiredFdnEnabled;
                        if (mDbg) log("EVENT_CHANGE_FACILITY_FDN_DONE: " +
                                "mIccFdnEnabled=" + mIccFdnEnabled);
                    } else {
                        Log.e(mLogTag, "Error change facility fdn with exception "
                                + ar.exception);
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
                case EVENT_CHANGE_ICC_PASSWORD_DONE:
                    ar = (AsyncResult)msg.obj;
                    if(ar.exception != null) {
                        Log.e(mLogTag, "Error in change sim password with exception"
                            + ar.exception);
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
                default:
                    Log.e(mLogTag, "[IccCard] Unknown Event " + msg.what);
            }
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
        }

        // this is common for all radio technologies
        if (!mIccCardStatus.getCardState().isCardPresent()) {
            return IccCard.State.ABSENT;
        }

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

            IccCardApplication app = mIccCardStatus.getApplication(index);

            if (app == null) {
                Log.e(mLogTag, "[IccCard] Subscription Application in not present");
                return IccCard.State.ABSENT;
            }

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
                return IccCard.State.NOT_READY;
            }
            return IccCard.State.NOT_READY;
        }

        return IccCard.State.ABSENT;
    }


    public boolean isApplicationOnIcc(IccCardApplication.AppType type) {
        if (mIccCardStatus == null) return false;

        for (int i = 0 ; i < mIccCardStatus.getNumApplications(); i++) {
            IccCardApplication app = mIccCardStatus.getApplication(i);
            if (app != null && app.app_type == type) {
                return true;
            }
        }
        return false;
    }

/**
* @return true if a ICC card is present
*/
    public boolean hasIccCard() {
        if (mIccCardStatus == null) {
            return false;
        } else {
            // Returns ICC card status for both GSM and CDMA mode
            return mIccCardStatus.getCardState().isCardPresent();
        }
    }

    private void log(String msg) {
        Log.d(mLogTag, "[IccCard] " + msg);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardProxy.java b/telephony/java/com/android/internal/telephony/IccCardProxy.java
new file mode 100644
//Synthetic comment -- index 0000000..b82a711

//Synthetic comment -- @@ -0,0 +1,551 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardStatus.java b/telephony/java/com/android/internal/telephony/IccCardStatus.java
deleted file mode 100644
//Synthetic comment -- index 0e7bad7..0000000

//Synthetic comment -- @@ -1,133 +0,0 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
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

import java.util.ArrayList;

/**
 * See also RIL_CardStatus in include/telephony/ril.h
 *
 * {@hide}
 */
public class IccCardStatus {
    static final int CARD_MAX_APPS = 8;

    public enum CardState {
        CARDSTATE_ABSENT,
        CARDSTATE_PRESENT,
        CARDSTATE_ERROR;

        boolean isCardPresent() {
            return this == CARDSTATE_PRESENT;
        }
    };

    public enum PinState {
        PINSTATE_UNKNOWN,
        PINSTATE_ENABLED_NOT_VERIFIED,
        PINSTATE_ENABLED_VERIFIED,
        PINSTATE_DISABLED,
        PINSTATE_ENABLED_BLOCKED,
        PINSTATE_ENABLED_PERM_BLOCKED
    };

    private CardState  mCardState;
    private PinState   mUniversalPinState;
    private int        mGsmUmtsSubscriptionAppIndex;
    private int        mCdmaSubscriptionAppIndex;
    private int        mNumApplications;

    private ArrayList<IccCardApplication> mApplications =
            new ArrayList<IccCardApplication>(CARD_MAX_APPS);

    public CardState getCardState() {
        return mCardState;
    }

    public void setCardState(int state) {
        switch(state) {
        case 0:
            mCardState = CardState.CARDSTATE_ABSENT;
            break;
        case 1:
            mCardState = CardState.CARDSTATE_PRESENT;
            break;
        case 2:
            mCardState = CardState.CARDSTATE_ERROR;
            break;
        default:
            throw new RuntimeException("Unrecognized RIL_CardState: " + state);
        }
    }

    public void setUniversalPinState(int state) {
        switch(state) {
        case 0:
            mUniversalPinState = PinState.PINSTATE_UNKNOWN;
            break;
        case 1:
            mUniversalPinState = PinState.PINSTATE_ENABLED_NOT_VERIFIED;
            break;
        case 2:
            mUniversalPinState = PinState.PINSTATE_ENABLED_VERIFIED;
            break;
        case 3:
            mUniversalPinState = PinState.PINSTATE_DISABLED;
            break;
        case 4:
            mUniversalPinState = PinState.PINSTATE_ENABLED_BLOCKED;
            break;
        case 5:
            mUniversalPinState = PinState.PINSTATE_ENABLED_PERM_BLOCKED;
            break;
        default:
            throw new RuntimeException("Unrecognized RIL_PinState: " + state);
        }
    }

    public int getGsmUmtsSubscriptionAppIndex() {
        return mGsmUmtsSubscriptionAppIndex;
    }

    public void setGsmUmtsSubscriptionAppIndex(int gsmUmtsSubscriptionAppIndex) {
        mGsmUmtsSubscriptionAppIndex = gsmUmtsSubscriptionAppIndex;
    }

    public int getCdmaSubscriptionAppIndex() {
        return mCdmaSubscriptionAppIndex;
    }

    public void setCdmaSubscriptionAppIndex(int cdmaSubscriptionAppIndex) {
        mCdmaSubscriptionAppIndex = cdmaSubscriptionAppIndex;
    }

    public int getNumApplications() {
        return mNumApplications;
    }

    public void setNumApplications(int numApplications) {
        mNumApplications = numApplications;
    }

    public void addApplication(IccCardApplication application) {
        mApplications.add(application);
    }

    public IccCardApplication getApplication(int index) {
        return mApplications.get(index);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index b12d2d4..7ca3570 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -67,4 +67,5 @@
static final String DF_GRAPHICS = "5F50";
static final String DF_GSM = "7F20";
static final String DF_CDMA = "7F25";
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 92ddd2c..02d6c5c 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.*;
import android.util.Log;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -90,7 +91,9 @@
static protected final int EVENT_READ_ICON_DONE = 10;

// member variables
    protected PhoneBase phone;

static class LoadLinearFixedContext {

//Synthetic comment -- @@ -120,9 +123,10 @@
/**
* Default constructor
*/
    protected IccFileHandler(PhoneBase phone) {
        super();
        this.phone = phone;
}

public void dispose() {
//Synthetic comment -- @@ -145,7 +149,7 @@
= obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid, recordNum, onLoaded));

        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -164,7 +168,7 @@
onLoaded));

// TODO(): Verify when path changes are done.
        phone.mCM.iccIO(COMMAND_GET_RESPONSE, IccConstants.EF_IMG, "img",
recordNum, READ_RECORD_MODE_ABSOLUTE,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}
//Synthetic comment -- @@ -182,7 +186,7 @@
Message response
= obtainMessage(EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid, onLoaded));
        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -199,7 +203,7 @@
Message response = obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid,onLoaded));

        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -217,7 +221,7 @@
Message response = obtainMessage(EVENT_GET_BINARY_SIZE_DONE,
fileid, 0, onLoaded);

        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -236,7 +240,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
length, null, null, response);
}

//Synthetic comment -- @@ -251,7 +255,7 @@
*/
public void updateEFLinearFixed(int fileid, int recordNum, byte[] data,
String pin2, Message onComplete) {
        phone.mCM.iccIO(COMMAND_UPDATE_RECORD, fileid, getEFPath(fileid),
recordNum, READ_RECORD_MODE_ABSOLUTE, data.length,
IccUtils.bytesToHexString(data), pin2, onComplete);
}
//Synthetic comment -- @@ -262,7 +266,7 @@
* @param data must be exactly as long as the EF
*/
public void updateEFTransparent(int fileid, byte[] data, Message onComplete) {
        phone.mCM.iccIO(COMMAND_UPDATE_BINARY, fileid, getEFPath(fileid),
0, 0, data.length,
IccUtils.bytesToHexString(data), null, onComplete);
}
//Synthetic comment -- @@ -395,7 +399,7 @@
lc.results = new ArrayList<byte[]>(lc.countRecords);
}

                 phone.mCM.iccIO(COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null,
//Synthetic comment -- @@ -433,7 +437,7 @@
size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8)
+ (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);

                phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
0, 0, size, null, null,
obtainMessage(EVENT_READ_BINARY_DONE,
fileid, 0, response));
//Synthetic comment -- @@ -468,7 +472,7 @@
if (lc.recordNum > lc.countRecords) {
sendResult(response, lc.results, null);
} else {
                        phone.mCM.iccIO(COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null,








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 2f22d74..0041de6 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -268,7 +268,10 @@
private int updateEfForIccType(int efid) {
// Check if we are trying to read ADN records
if (efid == IccConstants.EF_ADN) {
            if (phone.getIccCard().isApplicationOnIcc(IccCardApplication.AppType.APPTYPE_USIM)) {
return IccConstants.EF_PBR;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
deleted file mode 100644
//Synthetic comment -- index b8d9e3c..0000000

//Synthetic comment -- @@ -1,239 +0,0 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
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

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import java.util.ArrayList;

/**
 * {@hide}
 */
public abstract class IccRecords extends Handler implements IccConstants {

    protected static final boolean DBG = true;
    // ***** Instance Variables

    protected PhoneBase phone;
    protected RegistrantList recordsLoadedRegistrants = new RegistrantList();

    protected int recordsToLoad;  // number of pending load requests

    protected AdnRecordCache adnCache;

    // ***** Cached SIM State; cleared on channel close

    protected boolean recordsRequested = false; // true if we've made requests for the sim records

    public String iccid;
    protected String msisdn = null;  // My mobile number
    protected String msisdnTag = null;
    protected String voiceMailNum = null;
    protected String voiceMailTag = null;
    protected String newVoiceMailNum = null;
    protected String newVoiceMailTag = null;
    protected boolean isVoiceMailFixed = false;
    protected int countVoiceMessages = 0;

    protected int mncLength = UNINITIALIZED;
    protected int mailboxIndex = 0; // 0 is no mailbox dailing number associated

    protected String spn;
    protected int spnDisplayCondition;

    // ***** Constants

    // Markers for mncLength
    protected static final int UNINITIALIZED = -1;
    protected static final int UNKNOWN = 0;

    // Bitmasks for SPN display rules.
    protected static final int SPN_RULE_SHOW_SPN  = 0x01;
    protected static final int SPN_RULE_SHOW_PLMN = 0x02;

    // ***** Event Constants
    protected static final int EVENT_SET_MSISDN_DONE = 30;

    // ***** Constructor

    public IccRecords(PhoneBase p) {
        this.phone = p;
    }

    protected abstract void onRadioOffOrNotAvailable();

    //***** Public Methods
    public AdnRecordCache getAdnCache() {
        return adnCache;
    }

    public void registerForRecordsLoaded(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        recordsLoadedRegistrants.add(r);

        if (recordsToLoad == 0 && recordsRequested == true) {
            r.notifyRegistrant(new AsyncResult(null, null, null));
        }
    }

    public void unregisterForRecordsLoaded(Handler h) {
        recordsLoadedRegistrants.remove(h);
    }

    public String getMsisdnNumber() {
        return msisdn;
    }

    /**
     * Set subscriber number to SIM record
     *
     * The subscriber number is stored in EF_MSISDN (TS 51.011)
     *
     * When the operation is complete, onComplete will be sent to its handler
     *
     * @param alphaTag alpha-tagging of the dailing nubmer (up to 10 characters)
     * @param number dailing nubmer (up to 20 digits)
     *        if the number starts with '+', then set to international TOA
     * @param onComplete
     *        onComplete.obj will be an AsyncResult
     *        ((AsyncResult)onComplete.obj).exception == null on success
     *        ((AsyncResult)onComplete.obj).exception != null on fail
     */
    public void setMsisdnNumber(String alphaTag, String number,
            Message onComplete) {

        msisdn = number;
        msisdnTag = alphaTag;

        if(DBG) log("Set MSISDN: " + msisdnTag +" " + msisdn);


        AdnRecord adn = new AdnRecord(msisdnTag, msisdn);

        new AdnRecordLoader(phone).updateEF(adn, EF_MSISDN, EF_EXT1, 1, null,
                obtainMessage(EVENT_SET_MSISDN_DONE, onComplete));
    }

    public String getMsisdnAlphaTag() {
        return msisdnTag;
    }

    public String getVoiceMailNumber() {
        return voiceMailNum;
    }

    /**
     * Return Service Provider Name stored in SIM (EF_SPN=0x6F46) or in RUIM (EF_RUIM_SPN=0x6F41)
     * @return null if SIM is not yet ready or no RUIM entry
     */
    public String getServiceProviderName() {
        return spn;
    }

    /**
     * Set voice mail number to SIM record
     *
     * The voice mail number can be stored either in EF_MBDN (TS 51.011) or
     * EF_MAILBOX_CPHS (CPHS 4.2)
     *
     * If EF_MBDN is available, store the voice mail number to EF_MBDN
     *
     * If EF_MAILBOX_CPHS is enabled, store the voice mail number to EF_CHPS
     *
     * So the voice mail number will be stored in both EFs if both are available
     *
     * Return error only if both EF_MBDN and EF_MAILBOX_CPHS fail.
     *
     * When the operation is complete, onComplete will be sent to its handler
     *
     * @param alphaTag alpha-tagging of the dailing nubmer (upto 10 characters)
     * @param voiceNumber dailing nubmer (upto 20 digits)
     *        if the number is start with '+', then set to international TOA
     * @param onComplete
     *        onComplete.obj will be an AsyncResult
     *        ((AsyncResult)onComplete.obj).exception == null on success
     *        ((AsyncResult)onComplete.obj).exception != null on fail
     */
    public abstract void setVoiceMailNumber(String alphaTag, String voiceNumber,
            Message onComplete);

    public String getVoiceMailAlphaTag() {
        return voiceMailTag;
    }

    /**
     * Sets the SIM voice message waiting indicator records
     * @param line GSM Subscriber Profile Number, one-based. Only '1' is supported
     * @param countWaiting The number of messages waiting, if known. Use
     *                     -1 to indicate that an unknown number of
     *                      messages are waiting
     */
    public abstract void setVoiceMessageWaiting(int line, int countWaiting);

    /** @return  true if there are messages waiting, false otherwise. */
    public boolean getVoiceMessageWaiting() {
        return countVoiceMessages != 0;
    }

    /**
     * Returns number of voice messages waiting, if available
     * If not available (eg, on an older CPHS SIM) -1 is returned if
     * getVoiceMessageWaiting() is true
     */
    public int getVoiceMessageCount() {
        return countVoiceMessages;
    }

    /**
     * Called by STK Service when REFRESH is received.
     * @param fileChanged indicates whether any files changed
     * @param fileList if non-null, a list of EF files that changed
     */
    public abstract void onRefresh(boolean fileChanged, int[] fileList);


    public boolean getRecordsLoaded() {
        if (recordsToLoad == 0 && recordsRequested == true) {
            return true;
        } else {
            return false;
        }
    }

    //***** Overridden from Handler
    public abstract void handleMessage(Message msg);

    protected abstract void onRecordLoaded();

    protected abstract void onAllRecordsLoaded();

    /**
     * Returns the SpnDisplayRule based on settings on the SIM and the
     * specified plmn (currently-registered PLMN).  See TS 22.101 Annex A
     * and TS 51.011 10.3.11 for details.
     *
     * If the SPN is not found on the SIM, the rule is always PLMN_ONLY.
     */
    protected abstract int getDisplayRule(String plmn);

    protected abstract void log(String s);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/MccTable.java b/telephony/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index b73c2f7..28a3af5 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.ActivityManagerNative;
import android.app.AlarmManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
//Synthetic comment -- @@ -28,6 +29,7 @@
import android.util.Log;

import java.util.Arrays;

/**
* The table below is built from two resources:
//Synthetic comment -- @@ -573,7 +575,7 @@
* @param phone PhoneBae to act on.
* @param mccmnc truncated imsi with just the MCC and MNC - MNC assumed to be from 4th to end
*/
    public static void updateMccMncConfiguration(PhoneBase phone, String mccmnc) {
if (!TextUtils.isEmpty(mccmnc)) {
int mcc, mnc;

//Synthetic comment -- @@ -588,9 +590,9 @@
Log.d(LOG_TAG, "updateMccMncConfiguration: mcc=" + mcc + ", mnc=" + mnc);

if (mcc != 0) {
                setTimezoneFromMccIfNeeded(phone, mcc);
                setLocaleFromMccIfNeeded(phone, mcc);
                setWifiChannelsFromMcc(phone, mcc);
}
try {
Configuration config = ActivityManagerNative.getDefault().getConfiguration();
//Synthetic comment -- @@ -608,16 +610,67 @@
}

/**
* If the timezone is not already set, set it based on the MCC of the SIM.
* @param phone PhoneBase to act on (get context from).
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setTimezoneFromMccIfNeeded(PhoneBase phone, int mcc) {
String timezone = SystemProperties.get(ServiceStateTracker.TIMEZONE_PROPERTY);
if (timezone == null || timezone.length() == 0) {
String zoneId = defaultTimeZoneForMcc(mcc);
if (zoneId != null && zoneId.length() > 0) {
                Context context = phone.getContext();
// Set time zone based on MCC
AlarmManager alarm =
(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//Synthetic comment -- @@ -632,12 +685,12 @@
* @param phone PhoneBase to act on.
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setLocaleFromMccIfNeeded(PhoneBase phone, int mcc) {
String language = MccTable.defaultLanguageForMcc(mcc);
String country = MccTable.countryCodeForMcc(mcc);

Log.d(LOG_TAG, "locale set to "+language+"_"+country);
        phone.setSystemLocale(language, country);
}

/**
//Synthetic comment -- @@ -646,10 +699,9 @@
* @param phone PhoneBase to act on (get context from).
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setWifiChannelsFromMcc(PhoneBase phone, int mcc) {
int wifiChannels = MccTable.wifiChannelsForMcc(mcc);
if (wifiChannels != 0) {
            Context context = phone.getContext();
Log.d(LOG_TAG, "WIFI_NUM_ALLOWED_CHANNELS set to " + wifiChannels);
WifiManager wM = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//persist








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index b70ecc2..9ca657c 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -99,7 +99,12 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED= 27;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";
//Synthetic comment -- @@ -109,7 +114,6 @@

/* Instance Variables */
public CommandsInterface mCM;
    protected IccFileHandler mIccFileHandler;
boolean mDnsCheckDisabled = false;
public DataConnectionTracker mDataConnection;
boolean mDoesRilSendMultipleCallRing;
//Synthetic comment -- @@ -566,7 +570,7 @@
if (l.length() >=5) {
country = l.substring(3, 5);
}
                setSystemLocale(language, country);

if (wifiChannels != 0) {
try {
//Synthetic comment -- @@ -585,61 +589,15 @@
}

/**
     * Utility code to set the system locale if it's not set already
     * @param language Two character language code desired
     * @param country Two character country code desired
     *
     *  {@hide}
     */
    public void setSystemLocale(String language, String country) {
        String l = SystemProperties.get("persist.sys.language");
        String c = SystemProperties.get("persist.sys.country");

        if (null == language) {
            return; // no match possible
        }
        language = language.toLowerCase();
        if (null == country) {
            country = "";
        }
        country = country.toUpperCase();

        if((null == l || 0 == l.length()) && (null == c || 0 == c.length())) {
            try {
                // try to find a good match
                String[] locales = mContext.getAssets().getLocales();
                final int N = locales.length;
                String bestMatch = null;
                for(int i = 0; i < N; i++) {
                    // only match full (lang + country) locales
                    if (locales[i]!=null && locales[i].length() >= 5 &&
                            locales[i].substring(0,2).equals(language)) {
                        if (locales[i].substring(3,5).equals(country)) {
                            bestMatch = locales[i];
                            break;
                        } else if (null == bestMatch) {
                            bestMatch = locales[i];
                        }
                    }
                }
                if (null != bestMatch) {
                    IActivityManager am = ActivityManagerNative.getDefault();
                    Configuration config = am.getConfiguration();
                    config.locale = new Locale(bestMatch.substring(0,2),
                                               bestMatch.substring(3,5));
                    config.userSetLocale = true;
                    am.updateConfiguration(config);
                }
            } catch (Exception e) {
                // Intentionally left blank
            }
        }
    }

    /**
* Get state
*/
    public abstract Phone.State getState();

/**
* Retrieves the IccFileHandler of the Phone instance








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 6e97a77..590b749 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -45,6 +45,7 @@
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;

private boolean mResetModemOnRadioTechnologyChange = false;
private int mVoiceTechQueryContext = 0;
//Synthetic comment -- @@ -69,6 +70,15 @@

mCi.registerForOn(this, EVENT_RADIO_ON, null);
mCi.registerForVoiceRadioTechChanged(this, EVENT_VOICE_RADIO_TECHNOLOGY_CHANGED, null);
}

@Override
//Synthetic comment -- @@ -163,6 +173,11 @@
mIccPhoneBookInterfaceManagerProxy.setmIccPhoneBookInterfaceManager(mActivePhone
.getIccPhoneBookInterfaceManager());
mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());

mCi = ((PhoneBase)mActivePhone).mCM;

//Synthetic comment -- @@ -419,7 +434,7 @@
}

public IccCard getIccCard() {
        return mActivePhone.getIccCard();
}

public void acceptCall() throws CallStateException {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 1396123..871e043 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -53,8 +53,6 @@
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.SmsResponse;
//Synthetic comment -- @@ -689,28 +687,28 @@
}

public void
    supplyIccPin(String pin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(1);
rr.mp.writeString(pin);

send(rr);
}

public void
    supplyIccPuk(String puk, String newPin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
rr.mp.writeString(puk);
rr.mp.writeString(newPin);

//Synthetic comment -- @@ -718,28 +716,28 @@
}

public void
    supplyIccPin2(String pin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(1);
rr.mp.writeString(pin);

send(rr);
}

public void
    supplyIccPuk2(String puk, String newPin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
rr.mp.writeString(puk);
rr.mp.writeString(newPin2);

//Synthetic comment -- @@ -747,14 +745,14 @@
}

public void
    changeIccPin(String oldPin, String newPin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
rr.mp.writeString(oldPin);
rr.mp.writeString(newPin);

//Synthetic comment -- @@ -762,14 +760,14 @@
}

public void
    changeIccPin2(String oldPin2, String newPin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
rr.mp.writeString(oldPin2);
rr.mp.writeString(newPin2);

//Synthetic comment -- @@ -853,12 +851,15 @@
}

public void
    getIMSI(Message result) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_GET_IMSI, result);

if (RILJ_LOGD) riljLog(rr.serialString() +
"> getIMSI:RIL_REQUEST_GET_IMSI " +
RIL_REQUEST_GET_IMSI +
" " + requestToString(rr.mRequest));

send(rr);
//Synthetic comment -- @@ -1447,13 +1448,14 @@


public void
    iccIO (int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SIM_IO, result);

rr.mp.writeInt(command);
rr.mp.writeInt(fileid);
rr.mp.writeString(path);
//Synthetic comment -- @@ -1463,7 +1465,9 @@
rr.mp.writeString(data);
rr.mp.writeString(pin2);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> iccIO: " + requestToString(rr.mRequest)
+ " 0x" + Integer.toHexString(command)
+ " 0x" + Integer.toHexString(fileid) + " "
+ " path: " + path + ","
//Synthetic comment -- @@ -1635,14 +1639,17 @@
}

public void
    queryFacilityLock (String facility, String password, int serviceClass,
Message response) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_QUERY_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

// count strings
        rr.mp.writeInt(3);

rr.mp.writeString(facility);
rr.mp.writeString(password);
//Synthetic comment -- @@ -1653,16 +1660,20 @@
}

public void
    setFacilityLock (String facility, boolean lockState, String password,
int serviceClass, Message response) {
String lockString;
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SET_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

// count strings
        rr.mp.writeInt(4);

rr.mp.writeString(facility);
lockString = (lockState)?"1":"0";
//Synthetic comment -- @@ -2845,34 +2856,42 @@

private Object
responseIccCardStatus(Parcel p) {
        IccCardApplication ca;

        IccCardStatus status = new IccCardStatus();
        status.setCardState(p.readInt());
        status.setUniversalPinState(p.readInt());
        status.setGsmUmtsSubscriptionAppIndex(p.readInt());
        status.setCdmaSubscriptionAppIndex(p.readInt());
        int numApplications = p.readInt();

        // limit to maximum allowed applications
        if (numApplications > IccCardStatus.CARD_MAX_APPS) {
            numApplications = IccCardStatus.CARD_MAX_APPS;
}
        status.setNumApplications(numApplications);

        for (int i = 0 ; i < numApplications ; i++) {
            ca = new IccCardApplication();
            ca.app_type       = ca.AppTypeFromRILInt(p.readInt());
            ca.app_state      = ca.AppStateFromRILInt(p.readInt());
            ca.perso_substate = ca.PersoSubstateFromRILInt(p.readInt());
ca.aid            = p.readString();
ca.app_label      = p.readString();
ca.pin1_replaced  = p.readInt();
            ca.pin1           = p.readInt();
            ca.pin2           = p.readInt();
            status.addApplication(ca);
}
        return status;
}

private Object








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index a7e276a..9860acd 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -125,8 +125,9 @@
protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 39;
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
protected static final int EVENT_GET_CDMA_PRL_VERSION              = 41;

protected static final int EVENT_RADIO_ON                          = 42;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccApplicationRecords.java b/telephony/java/com/android/internal/telephony/UiccApplicationRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..4433d38

//Synthetic comment -- @@ -0,0 +1,348 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCard.java b/telephony/java/com/android/internal/telephony/UiccCard.java
new file mode 100644
//Synthetic comment -- index 0000000..cb54412

//Synthetic comment -- @@ -0,0 +1,215 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCardApplication.java b/telephony/java/com/android/internal/telephony/UiccCardApplication.java
new file mode 100644
//Synthetic comment -- index 0000000..d3d0fdd

//Synthetic comment -- @@ -0,0 +1,672 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCardStatusResponse.java b/telephony/java/com/android/internal/telephony/UiccCardStatusResponse.java
new file mode 100644
//Synthetic comment -- index 0000000..77b008f

//Synthetic comment -- @@ -0,0 +1,63 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccConstants.java b/telephony/java/com/android/internal/telephony/UiccConstants.java
new file mode 100644
//Synthetic comment -- index 0000000..c8ac482

//Synthetic comment -- @@ -0,0 +1,64 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccManager.java b/telephony/java/com/android/internal/telephony/UiccManager.java
new file mode 100644
//Synthetic comment -- index 0000000..d75ff8a

//Synthetic comment -- @@ -0,0 +1,215 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccRecords.java b/telephony/java/com/android/internal/telephony/UiccRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..984b9e3

//Synthetic comment -- @@ -0,0 +1,57 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UsimFileHandler.java b/telephony/java/com/android/internal/telephony/UsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..fa4b5e9

//Synthetic comment -- @@ -0,0 +1,85 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..a48774b 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;

import android.util.Config;

//Synthetic comment -- @@ -115,7 +115,7 @@
public class CatService extends Handler implements AppInterface {

// Class members
    private static IccRecords mIccRecords;

// Service members.
private static CatService sInstance;
//Synthetic comment -- @@ -147,7 +147,7 @@
private static final int DEV_ID_NETWORK     = 0x83;

/* Intentionally private for singleton */
    private CatService(CommandsInterface ci, IccRecords ir, Context context,
IccFileHandler fh, IccCard ic) {
if (ci == null || ir == null || context == null || fh == null
|| ic == null) {
//Synthetic comment -- @@ -513,7 +513,7 @@
* @param ic Icc card
* @return The only Service object in the system
*/
    public static CatService getInstance(CommandsInterface ci, IccRecords ir,
Context context, IccFileHandler fh, IccCard ic) {
if (sInstance == null) {
if (ci == null || ir == null || context == null || fh == null








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 249950a..7a73d4b7 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -50,12 +50,17 @@
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneNotifier;
//Synthetic comment -- @@ -102,9 +107,14 @@
CdmaSMSDispatcher mSMS;
CdmaServiceStateTracker mSST;
CdmaSubscriptionSourceManager mCdmaSSM;
    RuimRecords mRuimRecords;
    RuimCard mRuimCard;
int mCdmaSubscriptionSource = CDMA_SUBSCRIPTION_NV;
ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
//Synthetic comment -- @@ -152,29 +162,29 @@
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
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);
        mCcatService = CatService.getInstance(mCM, mRuimRecords, mContext,
                mIccFileHandler, mRuimCard);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        mRuimRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//Synthetic comment -- @@ -219,7 +229,6 @@
super.dispose();

//Unregister from all former registered events
            mRuimRecords.unregisterForRecordsLoaded(this); //EVENT_RUIM_RECORDS_LOADED
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
//Synthetic comment -- @@ -236,14 +245,17 @@
mSST.dispose();
mCdmaSSM.dispose(this);
mSMS.dispose();
            mIccFileHandler.dispose(); // instance of RuimFileHandler
            mRuimRecords.dispose();
            mRuimCard.dispose();
            mRuimPhoneBookInterfaceManager.dispose();
mRuimSmsInterfaceManager.dispose();
mSubInfo.dispose();
mEriManager.dispose();
            mCcatService.dispose();
}
}

//Synthetic comment -- @@ -252,15 +264,16 @@
this.mRuimSmsInterfaceManager = null;
this.mSMS = null;
this.mSubInfo = null;
            this.mRuimRecords = null;
            this.mIccFileHandler = null;
            this.mRuimCard = null;
this.mDataConnection = null;
this.mCT = null;
this.mSST = null;
this.mEriManager = null;
            this.mCcatService = null;
this.mExitEcmRunnable = null;
}

protected void finalize() {
//Synthetic comment -- @@ -499,7 +512,7 @@
}

public boolean handlePinMmi(String dialString) {
        CdmaMmiCode mmi = CdmaMmiCode.newFromDialString(dialString, this);

if (mmi == null) {
Log.e(LOG_TAG, "Mmi is NULL!");
//Synthetic comment -- @@ -517,7 +530,7 @@
public boolean isDataConnectivityPossible() {
boolean noData = mDataConnection.getDataEnabled() &&
getDataConnectionState() == DataState.DISCONNECTED;
        return !noData && getIccCard().getState() == IccCard.State.READY &&
getServiceState().getState() == ServiceState.STATE_IN_SERVICE &&
(mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
}
//Synthetic comment -- @@ -542,13 +555,20 @@
Log.e(LOG_TAG, "setLine1Number: not possible in CDMA");
}

    public IccCard getIccCard() {
return mRuimCard;
}

public String getIccSerialNumber() {
return mRuimRecords.iccid;
}

public void setCallWaiting(boolean enable, Message onComplete) {
Log.e(LOG_TAG, "method setCallWaiting is NOT supported in CDMA!");
//Synthetic comment -- @@ -726,10 +746,21 @@
public void setVoiceMailNumber(String alphaTag,
String voiceMailNumber,
Message onComplete) {
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
mRuimRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
}

public String getVoiceMailNumber() {
//Synthetic comment -- @@ -744,7 +775,9 @@
* @hide
*/
public int getVoiceMessageCount() {
        int voicemailCount =  mRuimRecords.getVoiceMessageCount();
// If mRuimRecords.getVoiceMessageCount returns zero, then there is possibility
// that phone was power cycled and would have lost the voicemail count.
// So get the count from preferences.
//Synthetic comment -- @@ -785,8 +818,11 @@
}

public boolean getIccRecordsLoaded() {
return mRuimRecords.getRecordsLoaded();
}

public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
Log.e(LOG_TAG, "getCallForwardingOption: not possible in CDMA");
//Synthetic comment -- @@ -868,14 +904,18 @@
/*package*/ void
updateMessageWaitingIndicator(boolean mwi) {
// this also calls notifyMessageWaitingIndicator()
mRuimRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
}

/* This function is overloaded to send number of voicemails instead of sending true/false */
/*package*/ void
updateMessageWaitingIndicator(int mwi) {
mRuimRecords.setVoiceMessageWaiting(1, mwi);
}

/**
* Returns true if CDMA OTA Service Provisioning needs to be performed.
//Synthetic comment -- @@ -1027,6 +1067,15 @@
}
break;

case EVENT_EMERGENCY_CALLBACK_MODE_ENTER:{
handleEnterEmergencyCallbackMode(msg);
}
//Synthetic comment -- @@ -1102,6 +1151,44 @@
}
}

/**
* Handles the call to get the subscription source
*
//Synthetic comment -- @@ -1168,7 +1255,10 @@
* {@inheritDoc}
*/
public IccFileHandler getIccFileHandler() {
        return this.mIccFileHandler;
}

/**
//Synthetic comment -- @@ -1473,7 +1563,7 @@
getContext().getContentResolver().insert(uri, map);

// Updates MCC MNC device configuration information
                MccTable.updateMccMncConfiguration(this, operatorNumeric);

return true;
} catch (SQLException e) {
//Synthetic comment -- @@ -1482,4 +1572,14 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 93aee137..2565784 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -432,7 +432,7 @@
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY) {
return DisconnectCause.OUT_OF_SERVICE;
} else if (phone.mCdmaSubscriptionSource == RILConstants.SUBSCRIPTION_FROM_RUIM
                           && phone.getIccCard().getState() != RuimCard.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1a81a17..4370397 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -45,6 +45,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.EventLogTags;
//Synthetic comment -- @@ -116,6 +117,8 @@
// if we have no active Apn this is null
protected ApnSetting mActiveApn;

// Possibly promote to base class, the only difference is
// the INTENT_RECONNECT_ALARM action is a different string.
// Do consider technology changes if it is promoted.
//Synthetic comment -- @@ -167,7 +170,6 @@

p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        p.mRuimRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -225,9 +227,9 @@

public void dispose() {
// Unregister from all events
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
        mCdmaPhone.mRuimRecords.unregisterForRecordsLoaded(this);
phone.mCM.unregisterForDataStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -298,7 +300,7 @@
boolean roaming = phone.getServiceState().getRoaming();

if (((mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_NV) ||
                 mCdmaPhone.mRuimRecords.getRecordsLoaded()) &&
(mCdmaPhone.mSST.getCurrentCdmaDataConnectionState() ==
ServiceState.STATE_IN_SERVICE) &&
(!roaming || getDataOnRoamingEnabled()) &&
//Synthetic comment -- @@ -333,7 +335,7 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (psState == ServiceState.STATE_IN_SERVICE)
&& ((mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_NV) ||
                        mCdmaPhone.mRuimRecords.getRecordsLoaded())
&& (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
//Synthetic comment -- @@ -350,7 +352,7 @@
" PS state=" + psState +
" isSubscriptionFromRuim=" +
(mCdmaSSM.getCdmaSubscriptionSource() == RILConstants.SUBSCRIPTION_FROM_RUIM) +
                    " ruim records loaded=" + mCdmaPhone.mRuimRecords.getRecordsLoaded() +
" concurrentVoice&Data=" + mCdmaPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" dataEnabled=" + getAnyDataEnabled() +








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
//Synthetic comment -- index 8dd8c2e..bf6949c 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;

import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -54,6 +55,7 @@

CDMAPhone phone;
Context context;

String action;              // ACTION_REGISTER
String sc;                  // Service Code
//Synthetic comment -- @@ -98,7 +100,7 @@
*/

public static CdmaMmiCode
    newFromDialString(String dialString, CDMAPhone phone) {
Matcher m;
CdmaMmiCode ret = null;

//Synthetic comment -- @@ -106,7 +108,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new CdmaMmiCode(phone);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -135,10 +137,11 @@

// Constructor

    CdmaMmiCode (CDMAPhone phone) {
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
}

// MmiCode implementation
//Synthetic comment -- @@ -206,8 +209,10 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else {
                        phone.mCM.supplyIccPuk(oldPinOrPuk, newPin,
obtainMessage(EVENT_SET_COMPLETE, this));
}
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 7439d5a..7f04728 100755

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -53,6 +53,11 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.CommandsInterface.RadioState;

import java.util.Arrays;
import java.util.Calendar;
//Synthetic comment -- @@ -69,7 +74,12 @@
CdmaCellLocation cellLoc;
CdmaCellLocation newCellLoc;

     /** if time between NITZ updates is less than mNitzUpdateSpacing the update may be ignored. */
private static final int NITZ_UPDATE_SPACING_DEFAULT = 1000 * 60 * 10;
private int mNitzUpdateSpacing = SystemProperties.getInt("ro.nitz_update_spacing",
NITZ_UPDATE_SPACING_DEFAULT);
//Synthetic comment -- @@ -114,12 +124,6 @@
long mSavedTime;
long mSavedAtTime;

    /**
     * We can't register for SIM_RECORDS_LOADED immediately because the
     * SIMRecords object may not be instantiated yet.
     */
    private boolean mNeedToRegForRuimLoaded = false;

/** Wake lock used while setting time of day. */
private PowerManager.WakeLock mWakeLock;
private static final String WAKELOCK_TAG = "ServiceStateTracker";
//Synthetic comment -- @@ -186,6 +190,9 @@
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.registerForCdmaPrlChanged(this, EVENT_CDMA_PRL_VERSION_CHANGED, null);

phone.registerForEriFileLoaded(this, EVENT_ERI_FILE_LOADED, null);
cm.registerForCdmaOtaProvision(this,EVENT_OTA_PROVISION_STATUS_CHANGE, null);

//Synthetic comment -- @@ -197,24 +204,32 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME), true,
mAutoTimeObserver);
setSignalStrengthDefaultValues();

        mNeedToRegForRuimLoaded = true;
}

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
//Synthetic comment -- @@ -329,14 +344,13 @@
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
//Synthetic comment -- @@ -485,8 +499,9 @@
if (!mIsMinInfoReady) {
mIsMinInfoReady = true;
}
                    phone.getIccCard().broadcastIccStateChangedIntent(IccCard.INTENT_VALUE_ICC_IMSI,
                            null);
} else {
Log.w(LOG_TAG,"error parsing cdmaSubscription params num="
+ cdmaSubscription.length);
//Synthetic comment -- @@ -578,7 +593,7 @@
// NV is ready when subscription source is NV
sendMessage(obtainMessage(EVENT_NV_READY));
} else {
            phone.mRuimCard.registerForReady(this, EVENT_RUIM_READY, null);
}
}

//Synthetic comment -- @@ -661,7 +676,9 @@
String plmn = "";
boolean showPlmn = false;
int rule = 0;
        if (isSubscriptionFromRuim && phone.mRuimCard.getState() == IccCard.State.READY) {
// TODO RUIM SPN is not implemented, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
//Synthetic comment -- @@ -1194,6 +1211,31 @@
}
}

/**
* Returns a TimeZone object based only on parameters from the NITZ string.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimCard.java b/telephony/java/com/android/internal/telephony/cdma/RuimCard.java
deleted file mode 100644
//Synthetic comment -- index 87dc259..0000000

//Synthetic comment -- @@ -1,54 +0,0 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
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

import com.android.internal.telephony.IccCard;

/**
 * Note: this class shares common code with SimCard, consider a base class to minimize code
 * duplication.
 * {@hide}
 */
public final class RuimCard extends IccCard {

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
    public String getServiceProviderName () {
        return ((CDMAPhone)mPhone).mRuimRecords.getServiceProviderName();
    }
 }









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/telephony/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 3e2a29b..92b7cf5d 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.*;
import android.util.Log;

import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -26,6 +27,7 @@
import com.android.internal.telephony.IccIoResult;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;

import java.util.ArrayList;

//Synthetic comment -- @@ -38,8 +40,8 @@
//***** Instance Variables

//***** Constructor
    RuimFileHandler(CDMAPhone phone) {
        super(phone);
}

public void dispose() {
//Synthetic comment -- @@ -57,7 +59,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        phone.mCM.iccIO(COMMAND_GET_RESPONSE, fileid, "img", 0, 0,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index 6e12f24a..7798116 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
** Copyright 2007, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -32,7 +33,9 @@

public RuimPhoneBookInterfaceManager(CDMAPhone phone) {
super(phone);
        adnCache = phone.mRuimRecords.getAdnCache();
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -49,6 +52,14 @@
if(DBG) Log.d(LOG_TAG, "RuimPhoneBookInterfaceManager finalized");
}

public int[] getAdnRecordsSize(int efid) {
if (DBG) logd("getAdnRecordsSize: efid=" + efid);
synchronized(mLock) {
//Synthetic comment -- @@ -58,11 +69,15 @@
//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

            phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to load from the RUIM");
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 6b1e9d7..8eb3a548 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,32 +16,31 @@

package com.android.internal.telephony.cdma;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.cdma.RuimCard;
import com.android.internal.telephony.MccTable;

// can't be used since VoiceMailConstants is not public
//import com.android.internal.telephony.gsm.VoiceMailConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;


/**
* {@hide}
*/
public final class RuimRecords extends IccRecords {
static final String LOG_TAG = "CDMA";

private static final boolean DBG = true;
//Synthetic comment -- @@ -49,7 +48,6 @@

// ***** Instance Variables

    private String mImsi;
private String mMyMobileNumber;
private String mMin2Min1;

//Synthetic comment -- @@ -57,7 +55,6 @@

// ***** Event Constants

    private static final int EVENT_RUIM_READY = 1;
private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
private static final int EVENT_GET_ICCID_DONE = 5;
//Synthetic comment -- @@ -73,21 +70,21 @@
private static final int EVENT_RUIM_REFRESH = 31;


    RuimRecords(CDMAPhone p) {
        super(p);

        adnCache = new AdnRecordCache(phone);

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;


        p.mRuimCard.registerForReady(this, EVENT_RUIM_READY, null);
        p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
// NOTE the EVENT_SMS_ON_RUIM is not registered
        p.mCM.setOnIccRefresh(this, EVENT_RUIM_REFRESH, null);

// Start off by setting empty state
onRadioOffOrNotAvailable();
//Synthetic comment -- @@ -96,9 +93,8 @@

public void dispose() {
//Unregister for all events
        ((CDMAPhone) phone).mRuimCard.unregisterForReady(this);
        phone.mCM.unregisterForOffOrNotAvailable( this);
        phone.mCM.unSetOnIccRefresh(this);
}

@Override
//Synthetic comment -- @@ -142,6 +138,11 @@
Log.e(LOG_TAG, "method setVoiceMailNumber is not implemented");
}

/**
* Called by CCAT Service when REFRESH is received.
* @param fileChanged indicates whether any files changed
//Synthetic comment -- @@ -187,14 +188,14 @@

boolean isRecordLoadResponse = false;

        if (!phone.mIsTheCurrentActivePhone) {
Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}

try { switch (msg.what) {
            case EVENT_RUIM_READY:
onRuimReady();
break;

//Synthetic comment -- @@ -282,6 +283,7 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
//Synthetic comment -- @@ -299,22 +301,11 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
        ((CDMAPhone) phone).mRuimCard.broadcastIccStateChangedIntent(
                RuimCard.INTENT_VALUE_ICC_LOADED, null);
}

private void onRuimReady() {
        /* broadcast intent ICC_READY here so that we can make sure
          READY is sent before IMSI ready
        */

        ((CDMAPhone) phone).mRuimCard.broadcastIccStateChangedIntent(
                RuimCard.INTENT_VALUE_ICC_READY, null);

fetchRuimRecords();

        phone.mCM.getCDMASubscription(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_DONE));

}


//Synthetic comment -- @@ -323,7 +314,7 @@

Log.v(LOG_TAG, "RuimRecords:fetchRuimRecords " + recordsToLoad);

        phone.getIccFileHandler().loadEFTransparent(EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -353,7 +344,7 @@
}
countVoiceMessages = countWaiting;

        ((CDMAPhone) phone).notifyMessageWaitingIndicator();
}

private void handleRuimRefresh(int[] result) {
//Synthetic comment -- @@ -375,14 +366,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
if (DBG) log("handleRuimRefresh with SIM_REFRESH_RESET");
                phone.mCM.setRadioPower(false, null);
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
break;
default:
// unknown refresh operation








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 99728bd..a5ae0f6 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -62,12 +62,17 @@
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -100,14 +105,17 @@
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
GsmSMSDispatcher mSMS;
    SIMRecords mSIMRecords;
    SimCard mSimCard;
    CatService mStkService;
ArrayList <GsmMmiCode> mPendingMMIs = new ArrayList<GsmMmiCode>();
SimPhoneBookInterfaceManager mSimPhoneBookIntManager;
SimSmsInterfaceManager mSimSmsIntManager;
PhoneSubInfo mSubInfo;


Registrant mPostDialHandler;

//Synthetic comment -- @@ -141,13 +149,13 @@
mSimulatedRadioControl = (SimulatedRadioControl) ci;
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
//Synthetic comment -- @@ -155,11 +163,8 @@
mSimSmsIntManager = new SimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
}
        mStkService = CatService.getInstance(mCM, mSIMRecords, mContext,
                (SIMFileHandler)mIccFileHandler, mSimCard);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        mSIMRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -211,7 +216,6 @@

//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
            mSIMRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
//Synthetic comment -- @@ -221,32 +225,37 @@
mPendingMMIs.clear();

//Force all referenced classes to unregister their former registered events
            mStkService.dispose();
mCT.dispose();
mDataConnection.dispose();
mSST.dispose();
            mIccFileHandler.dispose(); // instance of SimFileHandler
            mSIMRecords.dispose();
            mSimCard.dispose();
            mSimPhoneBookIntManager.dispose();
mSimSmsIntManager.dispose();
mSubInfo.dispose();
}
}

public void removeReferences() {
this.mSimulatedRadioControl = null;
            this.mStkService = null;
this.mSimPhoneBookIntManager = null;
this.mSimSmsIntManager = null;
this.mSMS = null;
this.mSubInfo = null;
            this.mSIMRecords = null;
            this.mIccFileHandler = null;
            this.mSimCard = null;
this.mDataConnection = null;
this.mCT = null;
this.mSST = null;
}

protected void finalize() {
//Synthetic comment -- @@ -280,12 +289,18 @@
}

public boolean getMessageWaitingIndicator() {
return mSIMRecords.getVoiceMessageWaiting();
}

public boolean getCallForwardingIndicator() {
return mSIMRecords.getVoiceCallForwardingFlag();
}

public List<? extends MmiCode>
getPendingMmiCodes() {
//Synthetic comment -- @@ -413,6 +428,7 @@
/*package*/ void
updateMessageWaitingIndicator(boolean mwi) {
// this also calls notifyMessageWaitingIndicator()
mSIMRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
}

//Synthetic comment -- @@ -729,7 +745,7 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this);
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -748,7 +764,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this);

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -761,7 +777,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this);
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -814,7 +830,9 @@

public String getVoiceMailNumber() {
// Read from the SIM. If its null, try reading from the shared preference area.
        String number = mSIMRecords.getVoiceMailNumber();
if (TextUtils.isEmpty(number)) {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);
//Synthetic comment -- @@ -835,8 +853,9 @@
}

public String getVoiceMailAlphaTag() {
        String ret;

ret = mSIMRecords.getVoiceMailAlphaTag();

if (ret == null || ret.length() == 0) {
//Synthetic comment -- @@ -866,33 +885,48 @@
}

public String getSubscriberId() {
        return mSIMRecords.imsi;
}

public String getIccSerialNumber() {
        return mSIMRecords.iccid;
}

public String getLine1Number() {
        return mSIMRecords.getMsisdnNumber();
}

public String getLine1AlphaTag() {
        return mSIMRecords.getMsisdnAlphaTag();
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
mSIMRecords.setMsisdnNumber(alphaTag, number, onComplete);
}

public void setVoiceMailNumber(String alphaTag,
String voiceMailNumber,
Message onComplete) {

Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
mSIMRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
}

private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
//Synthetic comment -- @@ -984,11 +1018,19 @@

public boolean
getIccRecordsLoaded() {
        return mSIMRecords.getRecordsLoaded();
}

public IccCard getIccCard() {
        return mSimCard;
}

public void
//Synthetic comment -- @@ -1111,7 +1153,7 @@
// check for "default"?
boolean noData = mDataConnection.getDataEnabled() &&
getDataConnectionState() == DataState.DISCONNECTED;
        return !noData && getIccCard().getState() == SimCard.State.READY &&
getServiceState().getState() == ServiceState.STATE_IN_SERVICE &&
(mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
}
//Synthetic comment -- @@ -1183,7 +1225,8 @@
GsmMmiCode mmi;
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
                                                   GSMPhone.this);
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1267,6 +1310,10 @@
mImeiSv = (String)ar.result;
break;

case EVENT_USSD:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1302,9 +1349,15 @@

case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
if (onComplete != null) {
AsyncResult.forMessage(onComplete, ar.result, ar.exception);
//Synthetic comment -- @@ -1338,6 +1391,21 @@
}
break;

// handle the select network completion callbacks.
case EVENT_SET_NETWORK_MANUAL_COMPLETE:
case EVENT_SET_NETWORK_AUTOMATIC_COMPLETE:
//Synthetic comment -- @@ -1361,6 +1429,47 @@
}
}

/**
* Sets the "current" field in the telephony provider according to the SIM's operator
*
//Synthetic comment -- @@ -1433,6 +1542,12 @@
}

private void handleCfuQueryResult(CallForwardInfo[] infos) {
if (infos == null || infos.length == 0) {
// Assume the default is not active
// Set unconditional CFF in SIM to false
//Synthetic comment -- @@ -1473,7 +1588,10 @@
* {@inheritDoc}
*/
public IccFileHandler getIccFileHandler(){
        return this.mIccFileHandler;
}

public void activateCellBroadcastSms(int activate, Message response) {
//Synthetic comment -- @@ -1491,4 +1609,18 @@
public boolean isCspPlmnEnabled() {
return mSIMRecords.isCspPlmnEnabled();
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 7dc2504..a5b7873 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -371,7 +371,7 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getIccCard().getState() != SimCard.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.rs.isCsRestricted()) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 5f651e7..77cdbff 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -55,7 +55,11 @@
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.DataConnection.FailCause;

import java.io.IOException;
import java.util.ArrayList;
//Synthetic comment -- @@ -155,6 +159,8 @@
// for tracking retries on a secondary APN
private RetryManager mSecondaryRetryManager;

BroadcastReceiver mIntentReceiver = new BroadcastReceiver ()
{
@Override
//Synthetic comment -- @@ -205,9 +211,9 @@
GsmDataConnectionTracker(GSMPhone p) {
super(p);
mGsmPhone = p;
p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        p.mSIMRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -281,9 +287,10 @@

public void dispose() {
//Unregister for all events
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
        mGsmPhone.mSIMRecords.unregisterForRecordsLoaded(this);
phone.mCM.unregisterForDataStateChanged(this);
mGsmPhone.mCT.unregisterForVoiceCallEnded(this);
mGsmPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -348,7 +355,7 @@
public boolean isDataConnectionAsDesired() {
boolean roaming = phone.getServiceState().getRoaming();

        if (mGsmPhone.mSIMRecords.getRecordsLoaded() &&
mGsmPhone.mSST.getCurrentGprsState() == ServiceState.STATE_IN_SERVICE &&
(!roaming || getDataOnRoamingEnabled()) &&
!mIsWifiConnected &&
//Synthetic comment -- @@ -441,7 +448,8 @@

if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
                && mGsmPhone.mSIMRecords.getRecordsLoaded()
&& (mGsmPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
//Synthetic comment -- @@ -468,7 +476,7 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" gprsState=" + gprsState +
                    " sim=" + mGsmPhone.mSIMRecords.getRecordsLoaded() +
" UMTS=" + mGsmPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" isDataAllowed=" + isDataAllowed() +
//Synthetic comment -- @@ -1254,7 +1262,12 @@
*/
private void createAllApnList() {
allApns = new ArrayList<ApnSetting>();
        String operator = mGsmPhone.mSIMRecords.getSIMOperatorNumeric();

if (operator != null) {
String selection = "numeric = '" + operator + "'";
//Synthetic comment -- @@ -1333,7 +1346,12 @@
return apnList;
}

        String operator = mGsmPhone.mSIMRecords.getSIMOperatorNumeric();

if (mRequestedApnType.equals(Phone.APN_TYPE_DEFAULT)) {
if (canSetPreferApn && preferredApn != null) {
//Synthetic comment -- @@ -1525,6 +1543,7 @@
}
}

protected void log(String s) {
Log.d(LOG_TAG, "[GsmDataConnectionTracker] " + s);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fe7a5cb..d98b944 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -110,6 +110,7 @@

GSMPhone phone;
Context context;

String action;              // One of ACTION_*
String sc;                  // Service Code
//Synthetic comment -- @@ -173,7 +174,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -181,7 +182,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -196,11 +197,11 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -209,10 +210,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -228,8 +229,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone) {
        GsmMmiCode ret = new GsmMmiCode(phone);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -380,12 +381,13 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
}

//***** MmiCode implementation
//Synthetic comment -- @@ -674,10 +676,10 @@
String facility = scToBarringFacility(sc);

if (isInterrogate()) {
                    phone.mCM.queryFacilityLock(facility, password,
serviceClass, obtainMessage(EVENT_QUERY_COMPLETE, this));
} else if (isActivate() || isDeactivate()) {
                    phone.mCM.setFacilityLock(facility, isActivate(), password,
serviceClass, obtainMessage(EVENT_SET_COMPLETE, this));
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
//Synthetic comment -- @@ -739,24 +741,29 @@
} else if (pinLen < 4 || pinLen > 8 ) {
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
                    } else if (sc.equals(SC_PIN) &&
                               phone.mSimCard.getState() == SimCard.State.PUK_REQUIRED ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {
// pre-checks OK
if (sc.equals(SC_PIN)) {
                            phone.mCM.changeIccPin(oldPinOrPuk, newPin,
obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PIN2)) {
                            phone.mCM.changeIccPin2(oldPinOrPuk, newPin,
                                    obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PUK)) {
                            phone.mCM.supplyIccPuk(oldPinOrPuk, newPin,
                                    obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PUK2)) {
                            phone.mCM.supplyIccPuk2(oldPinOrPuk, newPin,
                                    obtainMessage(EVENT_SET_COMPLETE, this));
}
}
} else {
//Synthetic comment -- @@ -860,7 +867,11 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

onSetComplete(ar);
//Synthetic comment -- @@ -1178,7 +1189,11 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

return TextUtils.replace(template, sources, destinations);
//Synthetic comment -- @@ -1203,7 +1218,11 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                phone.mSIMRecords.setVoiceCallForwardingFlag(1, false);
} else {

SpannableStringBuilder tb = new SpannableStringBuilder();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 1dc46d7..8ba5be8 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -63,6 +63,11 @@
import java.util.Date;
import java.util.TimeZone;

/**
* {@hide}
*/
//Synthetic comment -- @@ -76,6 +81,10 @@
int mPreferredNetworkType;
RestrictedState rs;

private int gprsState = ServiceState.STATE_OUT_OF_SERVICE;
private int newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;

//Synthetic comment -- @@ -123,12 +132,6 @@
long mSavedTime;
long mSavedAtTime;

    /**
     * We can't register for SIM_RECORDS_LOADED immediately because the
     * SIMRecords object may not be instantiated yet.
     */
    private boolean mNeedToRegForSimLoaded;

/** Started the recheck process after finding gprs should registered but not. */
private boolean mStartedGprsRegCheck = false;

//Synthetic comment -- @@ -209,6 +212,8 @@
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);

// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
phone.getContext().getContentResolver(),
//Synthetic comment -- @@ -220,7 +225,6 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME), true,
mAutoTimeObserver);
setSignalStrengthDefaultValues();
        mNeedToRegForSimLoaded = true;

// Monitor locale change
IntentFilter filter = new IntentFilter();
//Synthetic comment -- @@ -234,13 +238,24 @@
cm.unregisterForOn(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);
        phone.mSimCard.unregisterForReady(this);

        phone.mSIMRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
}

protected void finalize() {
//Synthetic comment -- @@ -355,19 +370,19 @@
//setPowerStateToDesired();
break;

            case EVENT_RADIO_ON:
                phone.mSimCard.registerForReady(this, EVENT_SIM_READY, null);
break;

case EVENT_SIM_READY:
// The SIM is now ready i.e if it was locked
// it has been unlocked. At this stage, the radio is already
// powered on.
                if (mNeedToRegForSimLoaded) {
                    phone.mSIMRecords.registerForRecordsLoaded(this,
                            EVENT_SIM_RECORDS_LOADED, null);
                    mNeedToRegForSimLoaded = false;
                }
// restore the previous network selection.
phone.restoreSavedNetworkSelection(null);
pollState();
//Synthetic comment -- @@ -586,8 +601,12 @@
}

protected void updateSpnDisplay() {
        int rule = phone.mSIMRecords.getDisplayRule(ss.getOperatorNumeric());
        String spn = phone.mSIMRecords.getServiceProviderName();
String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN
//Synthetic comment -- @@ -1053,6 +1072,41 @@
(gprsState != ServiceState.STATE_IN_SERVICE));
}

/**
* Returns a TimeZone object based only on parameters from the NITZ string.
*/
//Synthetic comment -- @@ -1166,7 +1220,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (phone.getIccCard().getState() == IccCard.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index e8d10f9..bf52939 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -19,26 +19,24 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.Phone;

/**
* {@hide}
*/
public final class SIMFileHandler extends IccFileHandler implements IccConstants {
static final String LOG_TAG = "GSM";
    private Phone mPhone;

//***** Instance Variables

//***** Constructor

    SIMFileHandler(GSMPhone phone) {
        super(phone);
        mPhone = phone;
}

public void dispose() {
//Synthetic comment -- @@ -90,13 +88,6 @@
}
String path = getCommonIccEFPath(efid);
if (path == null) {
            // The EFids in USIM phone book entries are decided by the card manufacturer.
            // So if we don't match any of the cases above and if its a USIM return
            // the phone book path.
            IccCard card = phone.getIccCard();
            if (card != null && card.isApplicationOnIcc(IccCardApplication.AppType.APPTYPE_USIM)) {
                return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
            }
Log.e(LOG_TAG, "Error: EF Path being returned in null");
}
return path;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 7db21c0..8a22dfa 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -29,12 +29,16 @@
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;

import java.util.ArrayList;

//Synthetic comment -- @@ -42,7 +46,7 @@
/**
* {@hide}
*/
public final class SIMRecords extends IccRecords {
static final String LOG_TAG = "GSM";

private static final boolean CRASH_RIL = false;
//Synthetic comment -- @@ -58,7 +62,6 @@

// ***** Cached SIM State; cleared on channel close

    String imsi;
boolean callForwardingEnabled;


//Synthetic comment -- @@ -115,7 +118,6 @@

// ***** Event Constants

    private static final int EVENT_SIM_READY = 1;
private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_IMSI_DONE = 3;
private static final int EVENT_GET_ICCID_DONE = 4;
//Synthetic comment -- @@ -146,10 +148,10 @@

// ***** Constructor

    SIMRecords(GSMPhone p) {
        super(p);

        adnCache = new AdnRecordCache(phone);

mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();
//Synthetic comment -- @@ -160,11 +162,10 @@
recordsToLoad = 0;


        p.mSimCard.registerForReady(this, EVENT_SIM_READY, null);
        p.mCM.registerForOffOrNotAvailable(
this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        p.mCM.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
        p.mCM.setOnIccRefresh(this, EVENT_SIM_REFRESH, null);

// Start off by setting empty state
onRadioOffOrNotAvailable();
//Synthetic comment -- @@ -173,9 +174,8 @@

public void dispose() {
//Unregister for all events
        ((GSMPhone) phone).mSimCard.unregisterForReady(this);
        phone.mCM.unregisterForOffOrNotAvailable( this);
        phone.mCM.unSetOnIccRefresh(this);
}

protected void finalize() {
//Synthetic comment -- @@ -183,7 +183,7 @@
}

protected void onRadioOffOrNotAvailable() {
        imsi = null;
msisdn = null;
voiceMailNum = null;
countVoiceMessages = 0;
//Synthetic comment -- @@ -198,9 +198,9 @@

adnCache.reset();

        phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, null);
        phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, null);
        phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ISO_COUNTRY, null);

// recordsRequested is set to false indicating that the SIM
// read requests made so far are not valid. This is set to
//Synthetic comment -- @@ -213,7 +213,7 @@

/** Returns null if SIM is not yet ready */
public String getIMSI() {
        return imsi;
}

public String getMsisdnNumber() {
//Synthetic comment -- @@ -246,7 +246,7 @@

AdnRecord adn = new AdnRecord(msisdnTag, msisdn);

        new AdnRecordLoader(phone).updateEF(adn, EF_MSISDN, EF_EXT1, 1, null,
obtainMessage(EVENT_SET_MSISDN_DONE, onComplete));
}

//Synthetic comment -- @@ -298,14 +298,14 @@

if (mailboxIndex != 0 && mailboxIndex != 0xff) {

            new AdnRecordLoader(phone).updateEF(adn, EF_MBDN, EF_EXT6,
mailboxIndex, null,
obtainMessage(EVENT_SET_MBDN_DONE, onComplete));

} else if (isCphsMailboxEnabled()) {

            new AdnRecordLoader(phone).updateEF(adn, EF_MAILBOX_CPHS,
                    EF_EXT1, 1, null,
obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE, onComplete));

} else {
//Synthetic comment -- @@ -345,7 +345,7 @@

countVoiceMessages = countWaiting;

        ((GSMPhone) phone).notifyMessageWaitingIndicator();

try {
if (efMWIS != null) {
//Synthetic comment -- @@ -364,9 +364,9 @@
efMWIS[1] = (byte) countWaiting;
}

                phone.getIccFileHandler().updateEFLinearFixed(
                    EF_MWIS, 1, efMWIS, null,
                    obtainMessage (EVENT_UPDATE_DONE, EF_MWIS));
}

if (efCPHS_MWI != null) {
//Synthetic comment -- @@ -374,9 +374,9 @@
efCPHS_MWI[0] = (byte)((efCPHS_MWI[0] & 0xf0)
| (countVoiceMessages == 0 ? 0x5 : 0xa));

                phone.getIccFileHandler().updateEFTransparent(
                    EF_VOICE_MAIL_INDICATOR_CPHS, efCPHS_MWI,
                    obtainMessage (EVENT_UPDATE_DONE, EF_VOICE_MAIL_INDICATOR_CPHS));
}
} catch (ArrayIndexOutOfBoundsException ex) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -394,7 +394,7 @@

callForwardingEnabled = enable;

        ((GSMPhone) phone).notifyCallForwardingIndicator();

try {
if (mEfCfis != null) {
//Synthetic comment -- @@ -408,9 +408,9 @@
// TODO: Should really update other fields in EF_CFIS, eg,
// dialing number.  We don't read or use it right now.

                phone.getIccFileHandler().updateEFLinearFixed(
                        EF_CFIS, 1, mEfCfis, null,
                        obtainMessage (EVENT_UPDATE_DONE, EF_CFIS));
}

if (mEfCff != null) {
//Synthetic comment -- @@ -422,9 +422,9 @@
| CFF_UNCONDITIONAL_DEACTIVE);
}

                phone.getIccFileHandler().updateEFTransparent(
                        EF_CFF_CPHS, mEfCff,
                        obtainMessage (EVENT_UPDATE_DONE, EF_CFF_CPHS));
}
} catch (ArrayIndexOutOfBoundsException ex) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -451,14 +451,14 @@
/** Returns the 5 or 6 digit MCC/MNC of the operator that
*  provided the SIM card. Returns null of SIM is not yet ready
*/
    String getSIMOperatorNumeric() {
        if (imsi == null || mncLength == UNINITIALIZED || mncLength == UNKNOWN) {
return null;
}

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
        return imsi.substring(0, 3 + mncLength);
}

// ***** Overridden from Handler
//Synthetic comment -- @@ -470,14 +470,14 @@

boolean isRecordLoadResponse = false;

        if (!phone.mIsTheCurrentActivePhone) {
Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}

try { switch (msg.what) {
            case EVENT_SIM_READY:
onSimReady();
break;

//Synthetic comment -- @@ -496,22 +496,22 @@
break;
}

                imsi = (String) ar.result;

// IMSI (MCC+MNC+MSIN) is at least 6 digits, but not more
// than 15 (and usually 15).
                if (imsi != null && (imsi.length() < 6 || imsi.length() > 15)) {
                    Log.e(LOG_TAG, "invalid IMSI " + imsi);
                    imsi = null;
}

                Log.d(LOG_TAG, "IMSI: " + imsi.substring(0, 6) + "xxxxxxxxx");

if (mncLength == UNKNOWN) {
// the SIM has told us all it knows, but it didn't know the mnc length.
// guess using the mcc
try {
                        int mcc = Integer.parseInt(imsi.substring(0,3));
mncLength = MccTable.smallestDigitsMccForMnc(mcc);
} catch (NumberFormatException e) {
mncLength = UNKNOWN;
//Synthetic comment -- @@ -521,10 +521,9 @@

if (mncLength != UNKNOWN && mncLength != UNINITIALIZED) {
// finally have both the imsi and the mncLength and can parse the imsi properly
                    MccTable.updateMccMncConfiguration(phone, imsi.substring(0, 3 + mncLength));
}
                ((GSMPhone) phone).mSimCard.broadcastIccStateChangedIntent(
                        SimCard.INTENT_VALUE_ICC_IMSI, null);
break;

case EVENT_GET_MBI_DONE:
//Synthetic comment -- @@ -555,13 +554,13 @@

if (isValidMbdn) {
// Note: MBDN was not included in NUM_OF_SIM_RECORDS_LOADED
                    new AdnRecordLoader(phone).loadFromEF(EF_MBDN, EF_EXT6,
mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
} else {
// If this EF not present, try mailbox as in CPHS standard
// CPHS (CPHS4_2.WW6) is a european standard.
                    new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS,
                            EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
}

//Synthetic comment -- @@ -592,8 +591,8 @@
// FIXME right now, only load line1's CPHS voice mail entry

recordsToLoad += 1;
                        new AdnRecordLoader(phone).loadFromEF(
                                EF_MAILBOX_CPHS, EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
}
break;
//Synthetic comment -- @@ -609,8 +608,8 @@
// FIXME should use SST to decide
// FIXME right now, only load line1's CPHS voice mail entry
recordsToLoad += 1;
                    new AdnRecordLoader(phone).loadFromEF(
                            EF_MAILBOX_CPHS, EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));

break;
//Synthetic comment -- @@ -678,7 +677,7 @@
countVoiceMessages = -1;
}

                ((GSMPhone) phone).notifyMessageWaitingIndicator();
break;

case EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE:
//Synthetic comment -- @@ -707,7 +706,7 @@
countVoiceMessages = 0;
}

                    ((GSMPhone) phone).notifyMessageWaitingIndicator();
}
break;

//Synthetic comment -- @@ -759,9 +758,9 @@
}
} finally {
if (mncLength == UNKNOWN || mncLength == UNINITIALIZED) {
                        if (imsi != null) {
try {
                                int mcc = Integer.parseInt(imsi.substring(0,3));

mncLength = MccTable.smallestDigitsMccForMnc(mcc);
} catch (NumberFormatException e) {
//Synthetic comment -- @@ -775,10 +774,10 @@
Log.d(LOG_TAG, "SIMRecords: MNC length not present in EF_AD");
}
}
                    if (imsi != null && mncLength != UNKNOWN) {
// finally have both imsi and the length of the mnc and can parse
// the imsi properly
                        MccTable.updateMccMncConfiguration(phone, imsi.substring(0, 3 + mncLength));
}
}
break;
//Synthetic comment -- @@ -807,7 +806,7 @@
callForwardingEnabled =
((data[0] & CFF_LINE1_MASK) == CFF_UNCONDITIONAL_ACTIVE);

                    ((GSMPhone) phone).notifyCallForwardingIndicator();
}
break;

//Synthetic comment -- @@ -880,7 +879,7 @@
+ ar.exception + " length " + index.length);
} else {
Log.d(LOG_TAG, "READ EF_SMS RECORD index=" + index[0]);
                    phone.getIccFileHandler().loadEFLinearFixed(EF_SMS,index[0],
obtainMessage(EVENT_GET_SMS_DONE));
}
break;
//Synthetic comment -- @@ -952,8 +951,8 @@
onCphsCompleted = null;
}

                    new AdnRecordLoader(phone).
                            updateEF(adn, EF_MAILBOX_CPHS, EF_EXT1, 1, null,
obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE,
onCphsCompleted));
} else {
//Synthetic comment -- @@ -1007,7 +1006,7 @@
// Refer TS 51.011 Section 10.3.46 for the content description
callForwardingEnabled = ((data[1] & 0x01) != 0);

                ((GSMPhone) phone).notifyCallForwardingIndicator();
break;

case EVENT_GET_CSP_CPHS_DONE:
//Synthetic comment -- @@ -1039,20 +1038,20 @@

private void handleFileUpdate(int efid) {
switch(efid) {
            case EF_MBDN:
recordsToLoad++;
                new AdnRecordLoader(phone).loadFromEF(EF_MBDN, EF_EXT6,
mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
break;
            case EF_MAILBOX_CPHS:
recordsToLoad++;
                new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
            case EF_CSP_CPHS:
recordsToLoad++;
                Log.i(LOG_TAG, "[CSP] SIM Refresh for EF_CSP_CPHS");
                phone.getIccFileHandler().loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
//Synthetic comment -- @@ -1086,7 +1085,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
		if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                phone.mCM.setRadioPower(false, null);
/* Note: no need to call setRadioPower(true).  Assuming the desired
* radio power state is still ON (as tracked by ServiceStateTracker),
* ServiceStateTracker will call setRadioPower when it receives the
//Synthetic comment -- @@ -1117,7 +1116,7 @@
System.arraycopy(ba, 1, pdu, 0, n - 1);
SmsMessage message = SmsMessage.createFromPdu(pdu);

            ((GSMPhone) phone).mSMS.dispatchMessage(message);
}
}

//Synthetic comment -- @@ -1143,7 +1142,7 @@
System.arraycopy(ba, 1, pdu, 0, n - 1);
SmsMessage message = SmsMessage.createFromPdu(pdu);

                ((GSMPhone) phone).mSMS.dispatchMessage(message);

// 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
// 1 == "received by MS from network; message read"
//Synthetic comment -- @@ -1151,7 +1150,7 @@
ba[0] = 1;

if (false) { // XXX writing seems to crash RdoServD
                    phone.getIccFileHandler().updateEFLinearFixed(EF_SMS,
i, ba, null, obtainMessage(EVENT_MARK_SMS_READ_DONE, i));
}
}
//Synthetic comment -- @@ -1162,6 +1161,7 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
//Synthetic comment -- @@ -1177,12 +1177,11 @@
String operator = getSIMOperatorNumeric();

// Some fields require more than one SIM record to set

        phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, operator);

        if (imsi != null) {
            phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
                    MccTable.countryCodeForMcc(Integer.parseInt(imsi.substring(0,3))));
}
else {
Log.e("SIM", "[SIMRecords] onAllRecordsLoaded: imsi is NULL!");
//Synthetic comment -- @@ -1193,8 +1192,6 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
        ((GSMPhone) phone).mSimCard.broadcastIccStateChangedIntent(
                SimCard.INTENT_VALUE_ICC_LOADED, null);
}

//***** Private methods
//Synthetic comment -- @@ -1215,42 +1212,36 @@
}

private void onSimReady() {
        /* broadcast intent SIM_READY here so that we can make sure
          READY is sent before IMSI ready
        */
        ((GSMPhone) phone).mSimCard.broadcastIccStateChangedIntent(
                SimCard.INTENT_VALUE_ICC_READY, null);

fetchSimRecords();
}

private void fetchSimRecords() {
recordsRequested = true;
        IccFileHandler iccFh = phone.getIccFileHandler();

Log.v(LOG_TAG, "SIMRecords:fetchSimRecords " + recordsToLoad);

        phone.mCM.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

// FIXME should examine EF[MSISDN]'s capability configuration
// to determine which is the voice/data/fax line
        new AdnRecordLoader(phone).loadFromEF(EF_MSISDN, EF_EXT1, 1,
obtainMessage(EVENT_GET_MSISDN_DONE));
recordsToLoad++;

// Record number is subscriber profile
        iccFh.loadEFLinearFixed(EF_MBI, 1, obtainMessage(EVENT_GET_MBI_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_AD, obtainMessage(EVENT_GET_AD_DONE));
recordsToLoad++;

// Record number is subscriber profile
        iccFh.loadEFLinearFixed(EF_MWIS, 1, obtainMessage(EVENT_GET_MWIS_DONE));
recordsToLoad++;


//Synthetic comment -- @@ -1258,39 +1249,39 @@
// the same info as EF[MWIS]. If both exist, both are updated
// but the EF[MWIS] data is preferred
// Please note this must be loaded after EF[MWIS]
        iccFh.loadEFTransparent(
                EF_VOICE_MAIL_INDICATOR_CPHS,
obtainMessage(EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE));
recordsToLoad++;

// Same goes for Call Forward Status indicator: fetch both
// EF[CFIS] and CPHS-EF, with EF[CFIS] preferred.
        iccFh.loadEFLinearFixed(EF_CFIS, 1, obtainMessage(EVENT_GET_CFIS_DONE));
recordsToLoad++;
        iccFh.loadEFTransparent(EF_CFF_CPHS, obtainMessage(EVENT_GET_CFF_DONE));
recordsToLoad++;


getSpnFsm(true, null);

        iccFh.loadEFTransparent(EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
recordsToLoad++;

        iccFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_CSP_CPHS,obtainMessage(EVENT_GET_CSP_CPHS_DONE));
recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
            iccFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
recordsToLoad++;
}

//Synthetic comment -- @@ -1303,7 +1294,7 @@
+ "ffffffffffffffffffffffffffffff";
byte[] ba = IccUtils.hexStringToBytes(sms);

            iccFh.updateEFLinearFixed(EF_SMS, 1, ba, null,
obtainMessage(EVENT_MARK_SMS_READ_DONE, 1));
}
}
//Synthetic comment -- @@ -1390,7 +1381,7 @@
case INIT:
spn = null;

                phone.getIccFileHandler().loadEFTransparent( EF_SPN,
obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1404,11 +1395,11 @@

if (DBG) log("Load EF_SPN: " + spn
+ " spnDisplayCondition: " + spnDisplayCondition);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);

spnState = Get_Spn_Fsm_State.IDLE;
} else {
                    phone.getIccFileHandler().loadEFTransparent( EF_SPN_CPHS,
obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1426,12 +1417,12 @@
data, 0, data.length - 1 );

if (DBG) log("Load EF_SPN_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);

spnState = Get_Spn_Fsm_State.IDLE;
} else {
                    phone.getIccFileHandler().loadEFTransparent(
                            EF_SPN_SHORT_CPHS, obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

spnState = Get_Spn_Fsm_State.READ_SPN_SHORT_CPHS;
//Synthetic comment -- @@ -1444,7 +1435,7 @@
data, 0, data.length - 1);

if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);
}else {
if (DBG) log("No SPN loaded in either CHPS or 3GPP");
}
//Synthetic comment -- @@ -1550,7 +1541,6 @@
// Operator Selection menu should be disabled.
// Operator Selection Mode should be set to Automatic.
Log.i(LOG_TAG,"[CSP] Set Automatic Network Selection");
                     phone.setNetworkSelectionModeAutomatic(null);
}
return;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimCard.java b/telephony/java/com/android/internal/telephony/gsm/SimCard.java
deleted file mode 100644
//Synthetic comment -- index 174b0f4..0000000

//Synthetic comment -- @@ -1,50 +0,0 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.android.internal.telephony.gsm;

import android.util.Log;

import com.android.internal.telephony.IccCard;

/**
 * {@hide}
 */
public final class SimCard extends IccCard {

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
    public String getServiceProviderName () {
        return ((GSMPhone)mPhone).mSIMRecords.getServiceProviderName();
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index feb508a..baf2136 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
** Copyright 2007, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -32,7 +33,9 @@

public SimPhoneBookInterfaceManager(GSMPhone phone) {
super(phone);
        adnCache = phone.mSIMRecords.getAdnCache();
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -49,6 +52,14 @@
if(DBG) Log.d(LOG_TAG, "SimPhoneBookInterfaceManager finalized");
}

public int[] getAdnRecordsSize(int efid) {
if (DBG) logd("getAdnRecordsSize: efid=" + efid);
synchronized(mLock) {
//Synthetic comment -- @@ -58,11 +69,15 @@
//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

            phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                logd("interrupted while trying to load from the SIM");
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
//Synthetic comment -- index b642541..5e4b605 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneBase;

//Synthetic comment -- @@ -44,7 +45,7 @@
private static final boolean DBG = true;
private PbrFile mPbrFile;
private Boolean mIsPbrPresent;
    private PhoneBase mPhone;
private AdnRecordCache mAdnCache;
private Object mLock = new Object();
private ArrayList<AdnRecord> mPhoneBookRecords;
//Synthetic comment -- @@ -76,8 +77,8 @@
private static final int USIM_EFEMAIL_TAG = 0xCA;
private static final int USIM_EFCCP1_TAG  = 0xCB;

    public UsimPhoneBookManager(PhoneBase phone, AdnRecordCache cache) {
        mPhone = phone;
mPhoneBookRecords = new ArrayList<AdnRecord>();
mPbrFile = null;
// We assume its present, after the first read this is updated.
//Synthetic comment -- @@ -140,7 +141,7 @@
}

private void readPbrFileAndWait() {
        mPhone.getIccFileHandler().loadEFLinearFixedAll(EF_PBR, obtainMessage(EVENT_PBR_LOAD_DONE));
try {
mLock.wait();
} catch (InterruptedException e) {
//Synthetic comment -- @@ -167,7 +168,7 @@
}
}
// Read the EFEmail file.
            mPhone.getIccFileHandler().loadEFLinearFixedAll(fileIds.get(USIM_EFEMAIL_TAG),
obtainMessage(EVENT_EMAIL_LOAD_DONE));
try {
mLock.wait();
//Synthetic comment -- @@ -185,7 +186,7 @@
}

private void readIapFileAndWait(int efid) {
        mPhone.getIccFileHandler().loadEFLinearFixedAll(efid, obtainMessage(EVENT_IAP_LOAD_DONE));
try {
mLock.wait();
} catch (InterruptedException e) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index e9014ff..210333c 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -109,7 +109,7 @@
unimplemented(result);
}

    public void supplyIccPin(String pin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PIN) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -151,7 +151,7 @@
}
}

    public void supplyIccPuk(String puk, String newPin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PUK) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -193,7 +193,7 @@
}
}

    public void supplyIccPin2(String pin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PIN2) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: wrong state, state=" +
mSimFdnEnabledState);
//Synthetic comment -- @@ -234,7 +234,7 @@
}
}

    public void supplyIccPuk2(String puk2, String newPin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PUK2) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -275,7 +275,7 @@
}
}

    public void changeIccPin(String oldPin, String newPin, Message result)  {
if (oldPin != null && oldPin.equals(mPinCode)) {
mPinCode = newPin;
if (result != null) {
//Synthetic comment -- @@ -296,7 +296,7 @@
}
}

    public void changeIccPin2(String oldPin2, String newPin2, Message result)  {
if (oldPin2 != null && oldPin2.equals(mPin2Code)) {
mPin2Code = newPin2;
if (result != null) {
//Synthetic comment -- @@ -342,7 +342,7 @@
* @param serviceClass is a sum of SERVICE_CLASS_*
*/

    public void queryFacilityLock (String facility, String pin,
int serviceClass, Message result) {
if (facility != null &&
facility.equals(CommandsInterface.CB_FACILITY_BA_SIM)) {
//Synthetic comment -- @@ -377,7 +377,7 @@
* @param pin the SIM pin or "" if not required
* @param serviceClass is a sum of SERVICE_CLASS_*
*/
    public void setFacilityLock (String facility, boolean lockEnabled,
String pin, int serviceClass,
Message result) {
if (facility != null &&
//Synthetic comment -- @@ -516,7 +516,7 @@
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
    public void getIMSI(Message result) {
resultSuccess(result, "012345678901234");
}

//Synthetic comment -- @@ -1044,7 +1044,7 @@
* response.obj will be an AsyncResult
* response.obj.userObj will be a SimIoResult on success
*/
    public void iccIO (int command, int fileid, String path, int p1, int p2,
int p3, String data, String pin2, Message result) {
unimplemented(result);
}







