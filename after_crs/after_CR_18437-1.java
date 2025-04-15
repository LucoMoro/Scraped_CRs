/*Telephony: Add support for Uicc

Change-Id:Ic8a6e13774bcb66cf3a8a54dc673b095832e9f9e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index a175d49..db5f4da 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
public final class AdnRecordCache extends Handler implements IccConstants {
//***** Instance Variables

    private IccFileHandler mFh;
private UsimPhoneBookManager mUsimPhoneBookManager;

// Indexed by EF ID
//Synthetic comment -- @@ -56,9 +56,9 @@



    public AdnRecordCache(IccFileHandler fh) {
        mFh = fh;
        mUsimPhoneBookManager = new UsimPhoneBookManager(mFh, this);
}

//***** Called from SIMRecords
//Synthetic comment -- @@ -155,7 +155,7 @@

userWriteResponse.put(efid, response);

        new AdnRecordLoader(mFh).updateEF(adn, efid, extensionEF,
recordIndex, pin2,
obtainMessage(EVENT_UPDATE_ADN_DONE, efid, recordIndex, adn));
}
//Synthetic comment -- @@ -233,7 +233,7 @@

userWriteResponse.put(efid, response);

        new AdnRecordLoader(mFh).updateEF(newAdn, efid, extensionEF,
index, pin2,
obtainMessage(EVENT_UPDATE_ADN_DONE, efid, index, newAdn));
}
//Synthetic comment -- @@ -296,7 +296,7 @@
return;
}

        new AdnRecordLoader(mFh).loadAllFromEF(efid, extensionEf,
obtainMessage(EVENT_LOAD_ALL_ADN_LIKE_DONE, efid, 0));
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordLoader.java b/telephony/java/com/android/internal/telephony/AdnRecordLoader.java
//Synthetic comment -- index 55bdc06..1b083c6 100644

//Synthetic comment -- @@ -20,16 +20,17 @@

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class AdnRecordLoader extends Handler {
    final static String LOG_TAG = "RIL_AdnRecordLoader";

//***** Instance Variables

    private IccFileHandler mFh;
int ef;
int extensionEF;
int pendingExtLoads;
//Synthetic comment -- @@ -56,13 +57,11 @@

//***** Constructor

    public AdnRecordLoader(IccFileHandler fh) {
// The telephony unit-test cases may create AdnRecords
// in secondary threads
        super(Looper.getMainLooper());
        mFh = fh;
}

/**
//Synthetic comment -- @@ -77,7 +76,7 @@
this.recordNumber = recordNumber;
this.userResponse = response;

        mFh.loadEFLinearFixed(
ef, recordNumber,
obtainMessage(EVENT_ADN_LOAD_DONE));

//Synthetic comment -- @@ -95,7 +94,7 @@
this.extensionEF = extensionEF;
this.userResponse = response;

        mFh.loadEFLinearFixedAll(
ef,
obtainMessage(EVENT_ADN_LOAD_ALL_DONE));

//Synthetic comment -- @@ -106,7 +105,7 @@
* It will get the record size of EF record and compose hex adn array
* then write the hex array to EF record
*
     * @param adn is set with alphaTag and phoneNubmer
* @param ef EF fileid
* @param extensionEF extension EF fileid
* @param recordNumber 1-based record index
//Synthetic comment -- @@ -122,7 +121,7 @@
this.userResponse = response;
this.pin2 = pin2;

        mFh.getEFLinearRecordSize( ef,
obtainMessage(EVENT_EF_LINEAR_RECORD_SIZE_DONE, adn));
}

//Synthetic comment -- @@ -163,7 +162,7 @@
ar.exception);
}

                    mFh.updateEFLinearFixed(ef, recordNumber,
data, pin2, obtainMessage(EVENT_UPDATE_RECORD_DONE));

pendingExtLoads = 1;
//Synthetic comment -- @@ -203,7 +202,7 @@

pendingExtLoads = 1;

                        mFh.loadEFLinearFixed(
extensionEF, adn.extRecord,
obtainMessage(EVENT_EXT_RECORD_LOAD_DONE, adn));
}
//Synthetic comment -- @@ -253,7 +252,7 @@

pendingExtLoads++;

                            mFh.loadEFLinearFixed(
extensionEF, adn.extRecord,
obtainMessage(EVENT_EXT_RECORD_LOAD_DONE, adn));
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 2fbff63..a80256d 100644

//Synthetic comment -- @@ -587,7 +587,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPin(int slot, String aid, String pin, Message result);

/**
* Supply the ICC PUK to the ICC card
//Synthetic comment -- @@ -601,7 +601,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPuk(int slot, String aid, String puk, String newPin, Message result);

/**
* Supply the ICC PIN2 to the ICC card
//Synthetic comment -- @@ -617,7 +617,7 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPin2(int slot, String aid, String pin2, Message result);

/**
* Supply the SIM PUK2 to the SIM card
//Synthetic comment -- @@ -633,10 +633,10 @@
* ar.exception and ar.result are null on success
*/

    void supplyIccPuk2(int slot, String aid, String puk2, String newPin2, Message result);

    void changeIccPin(int slot, String aid, String oldPin, String newPin, Message result);
    void changeIccPin2(int slot, String aid, String oldPin2, String newPin2, Message result);

void changeBarringPassword(String facility, String oldPwd, String newPwd, Message result);

//Synthetic comment -- @@ -705,7 +705,7 @@
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
    void getIMSI(int slot, String aid, Message result);

/**
*  returned message
//Synthetic comment -- @@ -1004,7 +1004,7 @@
* response.obj will be an AsyncResult
* response.obj.userObj will be a IccIoResult on success
*/
    void iccIO (int slot, String aid, int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, Message response);

/**
//Synthetic comment -- @@ -1113,7 +1113,7 @@
* @param response is callback message
*/

    void queryFacilityLock (int slot, String aid, String facility, String password, int serviceClass,
Message response);

/**
//Synthetic comment -- @@ -1123,7 +1123,7 @@
* @param serviceClass is a sum of SERVICE_CLASS_*
* @param response is callback message
*/
    void setFacilityLock (int slot, String aid, String facility, boolean lockState, String password,
int serviceClass, Message response);










//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CsimFileHandler.java b/telephony/java/com/android/internal/telephony/CsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..3396fc5

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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

    public CsimFileHandler(UiccCardApplication app, int slotId, String aid, CommandsInterface ci) {
        super(app, slotId, aid, ci);
    }

    protected void finalize() {
        Log.d(LOG_TAG, "CsimFileHandler finalized");
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_SMS:
        case EF_CST:
        case EF_RUIM_SPN:
            return MF_SIM + ADF;
        }
        return getCommonIccEFPath(efid);
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Log.e(LOG_TAG, msg);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 3c2b871..2e6ca49 100644

//Synthetic comment -- @@ -28,6 +28,9 @@

import java.util.ArrayList;

import com.android.internal.telephony.UiccManager.AppFamily;
import com.android.internal.telephony.gsm.SIMRecords;

/**
* {@hide}
*
//Synthetic comment -- @@ -101,6 +104,7 @@
protected static final int EVENT_RESTART_RADIO = 36;
protected static final int EVENT_SET_MASTER_DATA_ENABLE = 37;
protected static final int EVENT_RESET_DONE = 38;
    protected static final int EVENT_ICC_CHANGED = 39;

/***** Constants *****/

//Synthetic comment -- @@ -187,6 +191,14 @@

/** CID of active data connection */
protected int cidActive;
    
    /** Should be overridden in child classes */
    protected static final AppFamily mAppFamily = AppFamily.APP_FAM_3GPP;
    
    protected UiccManager mUiccManager = null;
    private UiccCardApplication mUiccApplication = null;
    private UiccCard mUiccCard = null;
    protected UiccApplicationRecords mUiccAppRecords = null;

/**
* Default constructor
//Synthetic comment -- @@ -194,6 +206,9 @@
protected DataConnectionTracker(PhoneBase phone) {
super();
this.phone = phone;
        mUiccManager = UiccManager.getInstance();
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);

}

public abstract void dispose();
//Synthetic comment -- @@ -335,6 +350,10 @@
onResetDone((AsyncResult) msg.obj);
break;

            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                break;

default:
Log.e("DATA", "Unidentified event = " + msg.what);
break;
//Synthetic comment -- @@ -578,5 +597,29 @@
}
}

    void updateIccAvailability() {

        UiccCardApplication newApplication = mUiccManager
                .getCurrentApplication(mAppFamily);

        if (mUiccApplication != newApplication) {
            if (mUiccApplication != null) {
                Log.d(LOG_TAG, "Removing stale Application");
                if (mUiccAppRecords != null) {
                    mUiccAppRecords.unregisterForRecordsLoaded(this);
                    mUiccAppRecords = null;
                }
                mUiccApplication = null;
                mUiccCard = null;
            }
            if (newApplication != null) {
                Log.d(LOG_TAG, "New application found " + mAppFamily);
                mUiccApplication = newApplication;
                mUiccCard = newApplication.getCard();
                mUiccAppRecords = newApplication.getApplicationRecords();
                mUiccAppRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
            }
        }
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 83c3221..48ea1e6 100644

//Synthetic comment -- @@ -16,45 +16,14 @@

package com.android.internal.telephony;

import android.os.Handler;
import android.os.Message;

/**
* {@hide}
*/

public interface IccCard {
/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
static public final String INTENT_KEY_ICC_STATE = "ss";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
//Synthetic comment -- @@ -78,21 +47,6 @@
/* NETWORK means ICC is locked on NETWORK PERSONALIZATION */
static public final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";

/*
UNKNOWN is a transient state, for example, after user inputs ICC pin under
PIN_REQUIRED state, the query for ICC status returns UNKNOWN before it
//Synthetic comment -- @@ -112,108 +66,30 @@
}
}

    public State getState();

    public void dispose();

/**
* Notifies handler of any transition into State.ABSENT
*/
    public void registerForAbsent(Handler h, int what, Object obj);

    public void unregisterForAbsent(Handler h);

/**
* Notifies handler of any transition into State.NETWORK_LOCKED
*/
    public void registerForNetworkLocked(Handler h, int what, Object obj);

    public void unregisterForNetworkLocked(Handler h);

/**
* Notifies handler of any transition into State.isPinLocked()
*/
    public void registerForLocked(Handler h, int what, Object obj);

    public void unregisterForLocked(Handler h);

/**
* Supply the ICC PIN to the ICC
//Synthetic comment -- @@ -236,30 +112,16 @@
*
*/

    public void supplyPin(String pin, Message onComplete);

    public void supplyPuk(String puk, String newPin, Message onComplete);

    public void supplyPin2(String pin2, Message onComplete);

    public void supplyPuk2(String puk2, String newPin2, Message onComplete);

    public void supplyNetworkDepersonalization(String pin, Message onComplete);


/**
* Check whether ICC pin lock is enabled
//Synthetic comment -- @@ -268,9 +130,7 @@
* @return true for ICC locked enabled
*         false for ICC locked disabled
*/
    public boolean getIccLockEnabled();

/**
* Check whether ICC fdn (fixed dialing number) is enabled
//Synthetic comment -- @@ -279,9 +139,7 @@
* @return true for ICC fdn enabled
*         false for ICC fdn disabled
*/
    public boolean getIccFdnEnabled();

/**
* Set the ICC pin lock enabled or disabled
//Synthetic comment -- @@ -294,19 +152,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
    public void setIccLockEnabled(boolean enabled, String password, Message onComplete);

/**
* Set the ICC fdn enabled or disabled
//Synthetic comment -- @@ -319,21 +165,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
    public void setIccFdnEnabled(boolean enabled, String password, Message onComplete);
/**
* Change the ICC password used in ICC pin lock
* When the operation is complete, onComplete will be sent to its handler
//Synthetic comment -- @@ -345,14 +177,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
    public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete);
/**
* Change the ICC password used in ICC fdn enable
* When the operation is complete, onComplete will be sent to its handler
//Synthetic comment -- @@ -364,14 +189,7 @@
*        ((AsyncResult)onComplete.obj).exception == null on success
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
    public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete);

/**
* Returns service provider name stored in ICC card.
//Synthetic comment -- @@ -389,338 +207,17 @@
*         yet available
*
*/
    public String getServiceProviderName();

    public void broadcastIccStateChangedIntent(String value, String reason);

    public State getIccCardState();

    public boolean isApplicationOnIcc(UiccConstants.AppType type);

/**
* @return true if a ICC card is present
*/

    public boolean hasIccCard();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardProxy.java b/telephony/java/com/android/internal/telephony/IccCardProxy.java
new file mode 100644
//Synthetic comment -- index 0000000..08256d6

//Synthetic comment -- @@ -0,0 +1,477 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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

import static android.Manifest.permission.READ_PHONE_STATE;
import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface.RadioTechnologyFamily;
import com.android.internal.telephony.UiccConstants.AppType;
import com.android.internal.telephony.UiccConstants.AppState;
import com.android.internal.telephony.UiccConstants.CardState;
import com.android.internal.telephony.UiccConstants.PersoSubState;
import com.android.internal.telephony.UiccManager.AppFamily;

/*
 * The Phone App UI and the external world assumes that there is only one icc card,
 * and one icc application available at a time. But the Uicc Manager can handle
 * multiple instances of icc objects. This class implements the icc interface to expose
 * the  first application on the first icc card, so that external apps wont break.
 */

public class IccCardProxy extends Handler implements IccCard {

    private static final String LOG_TAG = "RIL_IccCardProxy";

    private static final int EVENT_ICC_CHANGED = 1;
    private static final int EVENT_ICC_ABSENT = 2;
    private static final int EVENT_ICC_LOCKED = 3;
    private static final int EVENT_APP_READY = 4;
    private static final int EVENT_NETWORK_LOCKED = 5;
    private static final int EVENT_RECORDS_LOADED = 6;
    private static final int EVENT_IMSI_READY = 7;
    private static final int EVENT_PERSO_SUBSTATE_CHANGED = 8;

    private Context mContext;
    private CommandsInterface cm;

    private RegistrantList mAbsentRegistrants = new RegistrantList();
    private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();

    private AppFamily mCurrentAppType = AppFamily.APP_FAM_3GPP; //default to 3gpp?
    private UiccManager mUiccManager = null;
    private UiccCard mUiccCard = null;
    private UiccCardApplication mApplication = null;
    private UiccApplicationRecords mAppRecords = null;

    public IccCardProxy(Context mContext, CommandsInterface cm) {
        super();
        this.mContext = mContext;
        this.cm = cm;

        mUiccManager = UiccManager.getInstance(mContext, cm);
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);

        //Force update
        sendMessage(obtainMessage(EVENT_ICC_CHANGED));
    }

    public void dispose() {
        //Cleanup icc references
        mUiccManager.unregisterForIccChanged(this);
        mUiccManager = null;
    }

    /*
     * The card application that the external world sees will be based on the
     * voice radio technology only!
     */
    public void setVoiceRadioTech(RadioTechnologyFamily mVoiceRadioFamily) {
        Log.d(LOG_TAG, "Setting radio tech " + mVoiceRadioFamily);
        if (mVoiceRadioFamily == RadioTechnologyFamily.RADIO_TECH_3GPP2)
            mCurrentAppType = AppFamily.APP_FAM_3GPP2;
        else
            mCurrentAppType = AppFamily.APP_FAM_3GPP;

        sendMessage(obtainMessage(EVENT_ICC_CHANGED));
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                updateStateProperty();
                break;
            case EVENT_ICC_ABSENT:
                mAbsentRegistrants.notifyRegistrants();
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_ABSENT, null);
                break;
            case EVENT_ICC_LOCKED:
                processLockedState();
                break;
            case EVENT_NETWORK_LOCKED:
                mNetworkLockedRegistrants.notifyRegistrants();
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED, INTENT_VALUE_LOCKED_NETWORK);
                break;
            case EVENT_APP_READY:
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_READY, null);
                break;
            case EVENT_RECORDS_LOADED:
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOADED, null);
                break;
            case EVENT_IMSI_READY:
                broadcastIccStateChangedIntent(IccCard.INTENT_VALUE_ICC_IMSI, null);
                break;
            case EVENT_PERSO_SUBSTATE_CHANGED:
                if (mApplication != null) {
                    broadcastPersoSubState(mApplication.getPersonalizationState());
                }
                break;
            default:
                Log.e(LOG_TAG, "Unhandled message with number: " + msg.what);
                break;
        }
    }

    void updateIccAvailability() {

        UiccCardApplication newApplication = mUiccManager.getCurrentApplication(mCurrentAppType);

        if (mApplication != newApplication) {
            if (mApplication != null) {
                mApplication.unregisterForUnavailable(this);
                unregisterUiccCardEvents();
                mApplication = null;
                mUiccCard = null;
                mAppRecords = null;
            }
            if (newApplication == null) {
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_NOT_READY, null);
            } else {
                mApplication = newApplication;
                mUiccCard = newApplication.getCard();
                mAppRecords = newApplication.getApplicationRecords();
                registerUiccCardEvents();
            }
        }
    }

    private void unregisterUiccCardEvents() {
        mApplication.unregisterForReady(this);
        mApplication.unregisterForLocked(this);
        mApplication.unregisterForNetworkLocked(this);
        mApplication.unregisterForPersoSubstate(this);
        mUiccCard.unregisterForAbsent(this);
        mAppRecords.unregisterForImsiReady(this);
        mAppRecords.unregisterForRecordsLoaded(this);
    }

    private void registerUiccCardEvents() {
        mApplication.registerForReady(this, EVENT_APP_READY, null);
        mApplication.registerForLocked(this, EVENT_ICC_LOCKED, null);
        mApplication.registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, null);
        mApplication.registerForPersoSubstate(this, EVENT_PERSO_SUBSTATE_CHANGED, null);
        mUiccCard.registerForAbsent(this, EVENT_ICC_ABSENT, null);
        mAppRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
        mAppRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
    }

    private void updateStateProperty() {
        if (mUiccCard == null) {
            SystemProperties.set(TelephonyProperties.PROPERTY_SIM_STATE, CardState.ABSENT.toString());
        } else {
            SystemProperties.set(TelephonyProperties.PROPERTY_SIM_STATE, mUiccCard.getCardState().toString());
        }
    }

    /* why do external apps need to use this? */
    public void broadcastIccStateChangedIntent(String value, String reason) {
        Intent intent = new Intent(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(INTENT_KEY_ICC_STATE, value);
        intent.putExtra(INTENT_KEY_LOCKED_REASON, reason);
        Log.e(LOG_TAG, "Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value
                + " reason " + reason);
        ActivityManagerNative.broadcastStickyIntent(intent, READ_PHONE_STATE);
    }

    public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mApplication != null) {
            mApplication.changeIccFdnPassword(oldPassword, newPassword, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mApplication != null) {
            mApplication.changeIccLockPassword(oldPassword, newPassword, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    private void processLockedState() {
        if (mApplication == null) {
            //Don't need to do anything if non-existent application is locked
            return;
        }
        AppState appState = mApplication.getState();
        switch (mApplication.getState()) {
            case APPSTATE_PIN:
                mPinLockedRegistrants.notifyRegistrants();
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED, INTENT_VALUE_LOCKED_ON_PIN);
                break;
            case APPSTATE_PUK:
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED, INTENT_VALUE_LOCKED_ON_PUK);
                break;
        }
    }

    public State getIccCardState() {
        /*
         * TODO: What is difference between getState() and
         * getIccCardState()? No one seems to be using getIccCardState();
         */
        return getState();
    }

    public State getState() {
        State retState = State.UNKNOWN;
        CardState cardState = CardState.ABSENT;
        AppState appState = AppState.APPSTATE_UNKNOWN;
        PersoSubState persoState = PersoSubState.PERSOSUBSTATE_UNKNOWN;

        if (mUiccCard != null && mApplication != null) {
            appState = mApplication.getState();
            cardState = mUiccCard.getCardState();
            persoState = mApplication.getPersonalizationState();
        }

        switch (cardState) {
            case ABSENT:
                retState = State.ABSENT;
                break;
            case ERROR:
                retState = State.NOT_READY;
                break;
            case PRESENT:
                switch (appState) {
                    case APPSTATE_UNKNOWN:
                        retState = State.UNKNOWN;
                        break;
                    case APPSTATE_READY:
                        retState = State.READY;
                        break;
                    case APPSTATE_PIN:
                        retState = State.PIN_REQUIRED;
                        break;
                    case APPSTATE_PUK:
                        retState = State.PUK_REQUIRED;
                        break;
                    case APPSTATE_SUBSCRIPTION_PERSO:
                        switch (persoState) {
                            case PERSOSUBSTATE_UNKNOWN:
                            case PERSOSUBSTATE_IN_PROGRESS:
                                retState = State.UNKNOWN;
                                break;
                            case PERSOSUBSTATE_READY:
                                //This should never happen
                                retState = State.UNKNOWN;
                                break;
                            case PERSOSUBSTATE_SIM_NETWORK:
                                retState = State.NETWORK_LOCKED;
                                break;
                        }
                        break;
                    case APPSTATE_DETECTED:
                        retState = State.UNKNOWN;
                        break;
                }
        }
        return retState;
    }

    public boolean getIccFdnEnabled() {
        Boolean retValue = mApplication != null ? mApplication.getIccFdnEnabled() : false;
        return retValue;
    }

    public boolean getIccLockEnabled() {
        /* defaults to true, if ICC is absent */
        Boolean retValue = mApplication != null ? mApplication.getIccLockEnabled() : true;
        return retValue;
    }

    public String getServiceProviderName() {
        String retValue = mAppRecords != null ? mAppRecords.getServiceProviderName() : "";
        return retValue;
    }

    public boolean hasIccCard() {
        if (mUiccCard != null && mUiccCard.getCardState() == CardState.PRESENT) {
            return true;
        }
        return false;
    }

    public boolean isApplicationOnIcc(AppType type) {
        Boolean retValue = mUiccCard != null ? mUiccCard.isApplicationOnIcc(type) : false;
        return retValue;
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

    public void setIccFdnEnabled(boolean enabled, String password, Message onComplete) {
        if (mApplication != null) {
            mApplication.setIccFdnEnabled(enabled, password, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
        if (mApplication != null) {
            mApplication.setIccLockEnabled(enabled, password, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyNetworkDepersonalization(String pin, Message onComplete) {
        if (mApplication != null) {
            mApplication.supplyNetworkDepersonalization(pin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPin(String pin, Message onComplete) {
        if (mApplication != null) {
            mApplication.supplyPin(pin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPin2(String pin2, Message onComplete) {
        if (mApplication != null) {
            mApplication.supplyPin2(pin2, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPuk(String puk, String newPin, Message onComplete) {
        if (mApplication != null) {
            mApplication.supplyPuk(puk, newPin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
        if (mApplication != null) {
            mApplication.supplyPuk(puk2, newPin2, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    private void broadcastPersoSubState(PersoSubState state) {
        switch (state) {
            case PERSOSUBSTATE_UNKNOWN:
            case PERSOSUBSTATE_IN_PROGRESS:
            case PERSOSUBSTATE_READY:
                return;
            case PERSOSUBSTATE_SIM_NETWORK:
                Log.e(LOG_TAG, "Notify SIM network locked.");
                broadcastIccStateChangedIntent(INTENT_VALUE_ICC_LOCKED,
                        INTENT_VALUE_LOCKED_NETWORK);
                break;
            default:
                Log.e(LOG_TAG, "This Personalization substate is not handled: " + state);
                break;
        }
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardStatus.java b/telephony/java/com/android/internal/telephony/IccCardStatus.java
deleted file mode 100644
//Synthetic comment -- index 0e7bad7..0000000

//Synthetic comment -- @@ -1,133 +0,0 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index b12d2d4..1d52cde 100644

//Synthetic comment -- @@ -67,4 +67,5 @@
static final String DF_GRAPHICS = "5F50";
static final String DF_GSM = "7F20";
static final String DF_CDMA = "7F25";
    static final String ADF = "7FFF";
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 92ddd2c..1ebc971 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.*;
import android.util.Log;
import com.android.internal.telephony.UiccCardApplication;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -90,7 +91,10 @@
static protected final int EVENT_READ_ICON_DONE = 10;

// member variables
    protected CommandsInterface mCi;
    protected UiccCardApplication mApplication;
    protected int mSlotId;
    protected String mAid;

static class LoadLinearFixedContext {

//Synthetic comment -- @@ -120,9 +124,11 @@
/**
* Default constructor
*/
    protected IccFileHandler(UiccCardApplication app, int slotId, String aid, CommandsInterface ci) {
        mApplication = app;
        mSlotId = slotId;
        mAid = aid;
        mCi = ci;
}

public void dispose() {
//Synthetic comment -- @@ -145,7 +151,7 @@
= obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid, recordNum, onLoaded));

        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -164,7 +170,7 @@
onLoaded));

// TODO(): Verify when path changes are done.
        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, IccConstants.EF_IMG, "img",
recordNum, READ_RECORD_MODE_ABSOLUTE,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}
//Synthetic comment -- @@ -182,7 +188,7 @@
Message response
= obtainMessage(EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid, onLoaded));
        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -199,7 +205,7 @@
Message response = obtainMessage(EVENT_GET_RECORD_SIZE_DONE,
new LoadLinearFixedContext(fileid,onLoaded));

        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -217,7 +223,7 @@
Message response = obtainMessage(EVENT_GET_BINARY_SIZE_DONE,
fileid, 0, onLoaded);

        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, fileid, getEFPath(fileid),
0, 0, GET_RESPONSE_EF_SIZE_BYTES, null, null, response);
}

//Synthetic comment -- @@ -236,7 +242,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIO(mSlotId, mAid, COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
length, null, null, response);
}

//Synthetic comment -- @@ -251,7 +257,7 @@
*/
public void updateEFLinearFixed(int fileid, int recordNum, byte[] data,
String pin2, Message onComplete) {
        mCi.iccIO(mSlotId, mAid, COMMAND_UPDATE_RECORD, fileid, getEFPath(fileid),
recordNum, READ_RECORD_MODE_ABSOLUTE, data.length,
IccUtils.bytesToHexString(data), pin2, onComplete);
}
//Synthetic comment -- @@ -262,7 +268,7 @@
* @param data must be exactly as long as the EF
*/
public void updateEFTransparent(int fileid, byte[] data, Message onComplete) {
        mCi.iccIO(mSlotId, mAid, COMMAND_UPDATE_BINARY, fileid, getEFPath(fileid),
0, 0, data.length,
IccUtils.bytesToHexString(data), null, onComplete);
}
//Synthetic comment -- @@ -395,7 +401,7 @@
lc.results = new ArrayList<byte[]>(lc.countRecords);
}

                 mCi.iccIO(mSlotId, mAid, COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null,
//Synthetic comment -- @@ -433,7 +439,7 @@
size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8)
+ (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);

                mCi.iccIO(mSlotId, mAid, COMMAND_READ_BINARY, fileid, getEFPath(fileid),
0, 0, size, null, null,
obtainMessage(EVENT_READ_BINARY_DONE,
fileid, 0, response));
//Synthetic comment -- @@ -468,7 +474,7 @@
if (lc.recordNum > lc.countRecords) {
sendResult(response, lc.results, null);
} else {
                        mCi.iccIO(mSlotId, mAid, COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
lc.recordNum,
READ_RECORD_MODE_ABSOLUTE,
lc.recordSize, null, null,








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 2f22d74..da0fb84 100644

//Synthetic comment -- @@ -268,7 +268,10 @@
private int updateEfForIccType(int efid) {
// Check if we are trying to read ADN records
if (efid == IccConstants.EF_ADN) {
            //getUiccCard() can return null, if there is no icc present.
            if (phone.getUiccCard() != null
                    && phone.getUiccCard().isApplicationOnIcc(
                            UiccConstants.AppType.APPTYPE_USIM)) {
return IccConstants.EF_PBR;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
deleted file mode 100644
//Synthetic comment -- index b8d9e3c..0000000

//Synthetic comment -- @@ -1,239 +0,0 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/MccTable.java b/telephony/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index b73c2f7..3cf7aa7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.ActivityManagerNative;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
//Synthetic comment -- @@ -28,6 +29,7 @@
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

/**
* The table below is built from two resources:
//Synthetic comment -- @@ -573,7 +575,7 @@
* @param phone PhoneBae to act on.
* @param mccmnc truncated imsi with just the MCC and MNC - MNC assumed to be from 4th to end
*/
    public static void updateMccMncConfiguration(Context context, String mccmnc) {
if (!TextUtils.isEmpty(mccmnc)) {
int mcc, mnc;

//Synthetic comment -- @@ -588,9 +590,9 @@
Log.d(LOG_TAG, "updateMccMncConfiguration: mcc=" + mcc + ", mnc=" + mnc);

if (mcc != 0) {
                setTimezoneFromMccIfNeeded(context, mcc);
                setLocaleFromMccIfNeeded(context, mcc);
                setWifiChannelsFromMcc(context, mcc);
}
try {
Configuration config = ActivityManagerNative.getDefault().getConfiguration();
//Synthetic comment -- @@ -608,16 +610,67 @@
}

/**
     * Utility code to set the system locale if it's not set already
     * @param langauge Two character language code desired
     * @param country Two character country code desired
     *
     *  {@hide}
     */
    public static void setSystemLocale(Context context, String language, String country) {
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
                String[] locales = context.getAssets().getLocales();
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
* If the timezone is not already set, set it based on the MCC of the SIM.
* @param phone PhoneBase to act on (get context from).
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setTimezoneFromMccIfNeeded(Context context, int mcc) {
String timezone = SystemProperties.get(ServiceStateTracker.TIMEZONE_PROPERTY);
if (timezone == null || timezone.length() == 0) {
String zoneId = defaultTimeZoneForMcc(mcc);
if (zoneId != null && zoneId.length() > 0) {
// Set time zone based on MCC
AlarmManager alarm =
(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//Synthetic comment -- @@ -632,12 +685,12 @@
* @param phone PhoneBase to act on.
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setLocaleFromMccIfNeeded(Context context, int mcc) {
String language = MccTable.defaultLanguageForMcc(mcc);
String country = MccTable.countryCodeForMcc(mcc);

Log.d(LOG_TAG, "locale set to "+language+"_"+country);
        setSystemLocale(context, language, country);
}

/**
//Synthetic comment -- @@ -646,10 +699,9 @@
* @param phone PhoneBase to act on (get context from).
* @param mcc Mobile Country Code of the SIM or SIM-like entity (build prop on CDMA)
*/
    private static void setWifiChannelsFromMcc(Context context, int mcc) {
int wifiChannels = MccTable.wifiChannelsForMcc(mcc);
if (wifiChannels != 0) {
Log.d(LOG_TAG, "WIFI_NUM_ALLOWED_CHANNELS set to " + wifiChannels);
WifiManager wM = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//persist








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 0381bd1..28c5661 100644

//Synthetic comment -- @@ -99,8 +99,13 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    protected static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE    = 27;
protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED= 28;
    //other
    protected static final int EVENT_ICC_CHANGED                    = 29;
    protected static final int EVENT_SET_NETWORK_AUTOMATIC          = 30;
    protected static final int EVENT_NEW_ICC_SMS                    = 31;
    protected static final int EVENT_ICC_RECORD_EVENTS              = 32;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";
//Synthetic comment -- @@ -110,7 +115,6 @@

/* Instance Variables */
public CommandsInterface mCM;
boolean mDnsCheckDisabled = false;
public DataConnectionTracker mDataConnection;
boolean mDoesRilSendMultipleCallRing;
//Synthetic comment -- @@ -638,9 +642,10 @@
}

/**
     * Returns the ICC card interface for this phone, or null
     * if not applicable to underlying technology.
*/
    public abstract UiccCard getUiccCard();

/**
* Retrieves the IccFileHandler of the Phone instance








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 8a14618..06424d3 100644

//Synthetic comment -- @@ -57,6 +57,9 @@
* instances
*/
public static void makeDefaultPhone(Context context) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {};
synchronized(Phone.class) {
if (!sMadeDefaults) {
sLooper = Looper.myLooper();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 6e97a77..99d9b9c 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;
    private IccCardProxy mIccProxy;

private boolean mResetModemOnRadioTechnologyChange = false;
private int mVoiceTechQueryContext = 0;
//Synthetic comment -- @@ -69,6 +70,15 @@

mCi.registerForOn(this, EVENT_RADIO_ON, null);
mCi.registerForVoiceRadioTechChanged(this, EVENT_VOICE_RADIO_TECHNOLOGY_CHANGED, null);
        
        mIccProxy = new IccCardProxy(phone.getContext(), mCi);
        mIccProxy.setVoiceRadioTech(
                phone.getPhoneType() == Phone.PHONE_TYPE_CDMA ?
                        RadioTechnologyFamily.RADIO_TECH_3GPP2
                        : RadioTechnologyFamily.RADIO_TECH_3GPP);

        UiccManager.getInstance(phone.getContext(), mCi);

}

@Override
//Synthetic comment -- @@ -163,6 +173,11 @@
mIccPhoneBookInterfaceManagerProxy.setmIccPhoneBookInterfaceManager(mActivePhone
.getIccPhoneBookInterfaceManager());
mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());
        mIccProxy.setVoiceRadioTech(
                mActivePhone.getPhoneType() == Phone.PHONE_TYPE_CDMA ?
                        RadioTechnologyFamily.RADIO_TECH_3GPP2
                        : RadioTechnologyFamily.RADIO_TECH_3GPP);


mCi = ((PhoneBase)mActivePhone).mCM;

//Synthetic comment -- @@ -419,7 +434,7 @@
}

public IccCard getIccCard() {
        return mIccProxy;
}

public void acceptCall() throws CallStateException {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 1396123..0db5cb6 100644

//Synthetic comment -- @@ -53,8 +53,6 @@
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.SmsResponse;
//Synthetic comment -- @@ -689,28 +687,30 @@
}

public void
    supplyIccPin(int slot, String aid, String pin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(pin);

send(rr);
}

public void
    supplyIccPuk(int slot, String aid, String puk, String newPin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(puk);
rr.mp.writeString(newPin);

//Synthetic comment -- @@ -718,28 +718,30 @@
}

public void
    supplyIccPin2(int slot, String aid, String pin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(pin);

send(rr);
}

public void
    supplyIccPuk2(int slot, String aid, String puk, String newPin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(puk);
rr.mp.writeString(newPin2);

//Synthetic comment -- @@ -747,14 +749,15 @@
}

public void
    changeIccPin(int slot, String aid, String oldPin, String newPin, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(oldPin);
rr.mp.writeString(newPin);

//Synthetic comment -- @@ -762,14 +765,15 @@
}

public void
    changeIccPin2(int slot, String aid, String oldPin2, String newPin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN2, result);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeString(oldPin2);
rr.mp.writeString(newPin2);

//Synthetic comment -- @@ -853,12 +857,17 @@
}

public void
    getIMSI(int slot, String aid, Message result) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_GET_IMSI, result);

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);

if (RILJ_LOGD) riljLog(rr.serialString() +
"> getIMSI:RIL_REQUEST_GET_IMSI " +
RIL_REQUEST_GET_IMSI +
                              " slot: " + slot +
                              " aid: " + aid +
" " + requestToString(rr.mRequest));

send(rr);
//Synthetic comment -- @@ -1447,13 +1456,15 @@


public void
    iccIO (int slot, String aid, int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, Message result) {
//Note: This RIL request has not been renamed to ICC,
//       but this request is also valid for SIM and RUIM
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SIM_IO, result);

        rr.mp.writeInt(slot);
        rr.mp.writeString(aid);
rr.mp.writeInt(command);
rr.mp.writeInt(fileid);
rr.mp.writeString(path);
//Synthetic comment -- @@ -1463,7 +1474,10 @@
rr.mp.writeString(data);
rr.mp.writeString(pin2);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> iccIO: "
                + " slot: " + slot
                + " aid: " + aid + " "
                + requestToString(rr.mRequest)
+ " 0x" + Integer.toHexString(command)
+ " 0x" + Integer.toHexString(fileid) + " "
+ " path: " + path + ","
//Synthetic comment -- @@ -1635,14 +1649,18 @@
}

public void
    queryFacilityLock (int slot, String aid, String facility, String password, int serviceClass,
Message response) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_QUERY_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                + " slot: " + slot + " aid: " + aid + " facility: " + facility);

// count strings
        rr.mp.writeInt(5);

        rr.mp.writeString(Integer.toString(slot));
        rr.mp.writeString(aid);

rr.mp.writeString(facility);
rr.mp.writeString(password);
//Synthetic comment -- @@ -1653,16 +1671,21 @@
}

public void
    setFacilityLock (int slot, String aid, String facility, boolean lockState, String password,
int serviceClass, Message response) {
String lockString;
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SET_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                + " slot: " + slot + " aid: " + aid + " facility: " + facility
                + " lockstate: " + lockState);

// count strings
        rr.mp.writeInt(6);

        rr.mp.writeString(Integer.toString(slot));
        rr.mp.writeString(aid);

rr.mp.writeString(facility);
lockString = (lockState)?"1":"0";
//Synthetic comment -- @@ -2845,34 +2868,46 @@

private Object
responseIccCardStatus(Parcel p) {
        UiccCardStatusResponse r = new UiccCardStatusResponse();
        int num_cards = p.readInt();
        r.cards = new UiccCardStatusResponse.CardStatus[num_cards];
        for (int i = 0; i < num_cards; i ++) {
            r.cards[i] = r.new CardStatus();
            UiccCardStatusResponse.CardStatus cs = r.cards[i];
            cs.card_state = UiccConstants.CardState.values()[p.readInt()];
            cs.universal_pin_state = UiccConstants.PinState.values()[p.readInt()];
            int num_current_3gpp_indexes = p.readInt();
            cs.subscription_3gpp_app_index = new int[num_current_3gpp_indexes];
            for (int j = 0; j < num_current_3gpp_indexes; j++) {
                cs.subscription_3gpp_app_index[j] = p.readInt();
            }
            int num_current_3gpp2_indexes = p.readInt();
            cs.subscription_3gpp2_app_index = new int[num_current_3gpp2_indexes];
            for (int j = 0; j < num_current_3gpp2_indexes; j++) {
                cs.subscription_3gpp2_app_index[j] = p.readInt();
            }
            int num_applications = p.readInt();

            if (num_applications > UiccConstants.RIL_CARD_MAX_APPS) {
                num_applications = UiccConstants.RIL_CARD_MAX_APPS;
}

            cs.applications = new UiccCardStatusResponse.CardStatus.AppStatus[num_applications];
            for (int j = 0 ; j < num_applications ; j++) {
                UiccCardStatusResponse.CardStatus.AppStatus ca;
                ca = r.cards[i].new AppStatus();
                ca.app_type       = UiccConstants.AppType.values()[p.readInt()];
                ca.app_state      = UiccConstants.AppState.values()[p.readInt()];
                ca.perso_substate = UiccConstants.PersoSubState.values()[p.readInt()];
                ca.aid            = p.readString();
                ca.app_label      = p.readString();
                ca.pin1_replaced  = p.readInt();
                ca.pin1           = UiccConstants.PinState.values()[p.readInt()];
                ca.pin2           = UiccConstants.PinState.values()[p.readInt()];
                r.cards[i].applications[j] = ca;
            }
}
        return r;
}

private Object








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 6146578..50b9629 100644

//Synthetic comment -- @@ -128,6 +128,8 @@
protected static final int EVENT_GET_CDMA_PRL_VERSION              = 42;

protected static final int EVENT_RADIO_ON                          = 43;
    protected static final int EVENT_ICC_CHANGED                       = 44;
    protected static final int EVENT_ICC_RECORD_EVENTS                 = 45;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccApplicationRecords.java b/telephony/java/com/android/internal/telephony/UiccApplicationRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..c393c7f

//Synthetic comment -- @@ -0,0 +1,355 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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
import com.android.internal.telephony.UiccConstants.AppType;

/** This class will handle application specific records.
 * It will be subclassed by RuimRecords, UsimRecords etc.
 * Every user of this class will be registered for Unavailable with every
 * object it gets reference to. It is the user's responsibility to unregister
 * and remove reference to object, once UNAVAILABLE callback is received.
 */
public abstract class UiccApplicationRecords extends Handler{
    protected boolean mDestroyed = false; //set to true once this object is commanded to be disposed of.
    // Reference to UiccRecords to be able to access files
    // outside if this application's DF
    protected UiccRecords mUiccRecords;
    protected Context mContext;
    protected CommandsInterface mCi;
    protected UiccCardApplication mParentApp;
    protected IccFileHandler mFh;
    protected UiccCard mParentCard;

    public static final int EVENT_MWI = 0;
    public static final int EVENT_CFI = 1;
    public static final int EVENT_SPN = 2;
    public static final int EVENT_EONS = 3;

    // Internal events
    protected static final int EVENT_APP_READY = 1;

    private RegistrantList mUnavailableRegistrants = new RegistrantList();
    protected RegistrantList mRecordsEventsRegistrants = new RegistrantList();
    protected RegistrantList mNewSmsRegistrants = new RegistrantList();
    protected RegistrantList mNetworkSelectionModeAutomaticRegistrants = new RegistrantList();
    protected RegistrantList mImsiReadyRegistrants = new RegistrantList();

    public UiccApplicationRecords(UiccCardApplication parent, Context c, CommandsInterface ci, UiccRecords ur) {
        mContext = c;
        mCi = ci;
        mUiccRecords = ur;
        mParentApp = parent;
        mFh = mParentApp.getIccFileHandler();
        mParentCard = mParentApp.getCard();
        mParentApp.registerForReady(this, EVENT_APP_READY, null);
    }

    public synchronized void dispose() {
        mDestroyed = true;
        mParentCard = null;
        mFh = null;
        mParentApp.unregisterForReady(this);
        mParentApp = null;
        mUiccRecords = null;
        mUnavailableRegistrants.notifyRegistrants();
        //TODO: Do we need to to anything here? Probably - once we have code here
    }

    public synchronized void registerForUnavailable(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant (h, what, obj);
        mUnavailableRegistrants.add(r);
    }
    public synchronized void unregisterForUnavailable(Handler h) {
        mUnavailableRegistrants.remove(h);
    }

    public UiccCardApplication getCardApplication() {
        return mParentApp;
    }

    public synchronized void registerForRecordsEvents(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mRecordsEventsRegistrants.add(r);
    }
    public synchronized void unregisterForRecordsEvents(Handler h) {
        mRecordsEventsRegistrants.remove(h);
    }

    public synchronized void registerForNewSms(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mNewSmsRegistrants.add(r);
    }
    public synchronized void unregisterForNewSms(Handler h) {
        mNewSmsRegistrants.remove(h);
    }

    public synchronized void registerForNetworkSelectionModeAutomatic(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mNetworkSelectionModeAutomaticRegistrants.add(r);
    }
    public synchronized void unregisterForNetworkSelectionModeAutomatic(Handler h) {
        mNetworkSelectionModeAutomaticRegistrants.remove(h);
    }

    public synchronized void registerForImsiReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mImsiReadyRegistrants.add(r);
        if (mImsi != null && mImsi.length() > 0) {
            r.notifyRegistrant();
        }
    }
    public synchronized void unregisterForImsiReady(Handler h) {
        mImsiReadyRegistrants.remove(h);
    }

    // IccRecords
    protected static final boolean DBG = true;
    // ***** Instance Variables

    protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
    protected RegistrantList mIccRefreshRegistrants = new RegistrantList();

    protected int recordsToLoad;  // number of pending load requests

    protected AdnRecordCache adnCache;

    // ***** Cached SIM State; cleared on channel close

    protected boolean recordsRequested = false; // true if we've made requests for the sim records

    public String iccid;
    protected String msisdn = null;  // My mobile number
    protected String msisdnTag = null;
    protected String mImsi = null;
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

    //public IccRecords(PhoneBase p) {
    //    this.phone = p;
    //}

    protected abstract void onRadioOffOrNotAvailable();

    //***** Public Methods
    public AdnRecordCache getAdnCache() {
        return adnCache;
    }

    public synchronized void registerForRecordsLoaded(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant(h, what, obj);
        recordsLoadedRegistrants.add(r);

        if (recordsToLoad == 0 && recordsRequested == true) {
            r.notifyRegistrant(new AsyncResult(null, null, null));
        }
    }
    public synchronized void unregisterForRecordsLoaded(Handler h) {
        recordsLoadedRegistrants.remove(h);
    }

    /** Register for IccRefresh */
    public void registerForIccRefreshReset(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        mIccRefreshRegistrants.add(r);
    }

    public void unregisterForIccRefreshReset(Handler h) {
        mIccRefreshRegistrants.remove(h);
    }

    public void onIccRefreshReset() {
        mIccRefreshRegistrants.notifyRegistrants();
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

        new AdnRecordLoader(mFh).updateEF(adn, IccConstants.EF_MSISDN, IccConstants.EF_EXT1, 1, null,
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCard.java b/telephony/java/com/android/internal/telephony/UiccCard.java
new file mode 100644
//Synthetic comment -- index 0000000..77d19cd

//Synthetic comment -- @@ -0,0 +1,221 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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


import com.android.internal.telephony.UiccConstants.CardState;
import com.android.internal.telephony.UiccConstants.PinState;
import com.android.internal.telephony.UiccConstants.AppType;
import com.android.internal.telephony.cat.CatService;
import android.content.Context;
import android.os.Handler;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.util.Log;

/** Every user of this class will be registered for Unavailable with every
 * object it gets reference to. It is the user's responsibility to unregister
 * and remove reference to object, once UNAVAILABLE callback is received.
 */
public class UiccCard extends Handler{
    private String mLogTag = "RIL_UiccCard";

    private UiccManager mUiccManager; //parent
    private UiccCardApplication[] mUiccApplications;
    private UiccRecords mUiccRecords;
    private int mSlotId;
    private CardState mCardState;
    private PinState mUniversalPinState;
    private int[] mSubscription3gppAppIndex;     /* value < RIL_CARD_MAX_APPS */
    private int[] mSubscription3gpp2AppIndex;    /* value < RIL_CARD_MAX_APPS */
    private RegistrantList mUnavailableRegistrants = new RegistrantList();
    private RegistrantList mAbsentRegistrants = new RegistrantList();
    private boolean mDestroyed = false; //set to true once this card is commanded to be disposed of.
    private Context mContext;
    private CommandsInterface mCi;
    private CatService mCatService;


    UiccCard(UiccManager uiccManager, int slotId, UiccCardStatusResponse.CardStatus ics, Context c, CommandsInterface ci) {
        mUiccManager = uiccManager;
        mSlotId = slotId;
        mCardState = ics.card_state;
        mUniversalPinState = ics.universal_pin_state;
        mSubscription3gppAppIndex = ics.subscription_3gpp_app_index;
        mSubscription3gpp2AppIndex = ics.subscription_3gpp2_app_index;
        mUiccRecords = new UiccRecords(this);
        mUiccApplications = new UiccCardApplication[UiccConstants.RIL_CARD_MAX_APPS];
        mContext = c;
        mCi = ci;

        Log.d(mLogTag, "Creating " + ics.applications.length + " applications");
        for (int i = 0; i < ics.applications.length; i++) {
            mUiccApplications[i] = new UiccCardApplication(this, ics.applications[i], mUiccRecords, mContext, mCi);
        }

        if (mUiccApplications.length > 0 && mUiccApplications[0] != null) {
            mCatService = CatService.getInstance(mCi, mUiccApplications[0].getApplicationRecords(), mContext,
                                                 mUiccApplications[0].getIccFileHandler(), null);
        }
    }

    public void update(UiccCardStatusResponse.CardStatus ics, Context c, CommandsInterface ci) {
        if (mDestroyed) {
            Log.e(mLogTag, "Updated after destroyed! Fix me!");
            return;
        }
        //TODO: Fusion - If state changed - notify registrants
        mCardState = ics.card_state;
        mUniversalPinState = ics.universal_pin_state;
        mSubscription3gppAppIndex = ics.subscription_3gpp_app_index;
        mSubscription3gpp2AppIndex = ics.subscription_3gpp2_app_index;
        mContext = c;
        mCi = ci;
        //update applications
        for ( int i = 0; i < mUiccApplications.length; i++) {
            if (mUiccApplications[i] == null) {
                //Create newly added Applications
                if (i < ics.applications.length) {
                    mUiccApplications[i] = new UiccCardApplication(this,
                            ics.applications[i], mUiccRecords, mContext, mCi);
                }
            } else if (i >= ics.applications.length) {
                //Delete removed applications
                mUiccApplications[i].dispose();
                mUiccApplications[i] = null;
            } else {
                //Update the rest
                mUiccApplications[i].update(ics.applications[i], mUiccRecords, mContext, mCi);
            }
        }
    }

    public synchronized void dispose() {
        mDestroyed = true;

        mUiccRecords.dispose();
        mUiccRecords = null;
        if (mCatService != null) {
            mCatService.dispose();
        }
        mCatService = null;
        for (UiccCardApplication app: mUiccApplications) {
            if (app != null) {
                app.dispose();
            }
        }
        mUiccApplications = null;
        mUnavailableRegistrants.notifyRegistrants();
    }

    public UiccManager getUiccManager() {
        return mUiccManager;
    }
    public int[] getSubscription3gppAppIndex() {
        return mSubscription3gppAppIndex;
    }

    public int[] getSubscription3gpp2AppIndex() {
        return mSubscription3gpp2AppIndex;
    }

    /* Returns number of applications on this card */
    public int getNumApplications() {
        int count = 0;
        for (UiccCardApplication a : mUiccApplications) {
            if (a != null) {
                count++;
            }
        }
        return count;
    }

    public int getSlotId() {
        return mSlotId;
    }

    public synchronized UiccCardApplication getUiccCardApplication(int appIndex) {
        if (mDestroyed) {
            return null;
        }

        if (appIndex < mUiccApplications.length) {
            return mUiccApplications[appIndex];
        } else {
            return null;
        }
    }

    public CardState getCardState() {
        return mCardState;
    }

    public synchronized UiccRecords getRecords() {
        return mUiccRecords;
    }

    public PinState getUniversalPinState() {
        return mUniversalPinState;
    }
    public synchronized void registerForUnavailable(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant (h, what, obj);
        mUnavailableRegistrants.add(r);
    }
    public synchronized void unregisterForUnavailable(Handler h) {
        mUnavailableRegistrants.remove(h);
    }

    protected void finalize() {
        Log.d(mLogTag, "UiccCard finalized");
    }

    /**
     * Notifies handler of any transition into State.ABSENT
     */
    public void registerForAbsent(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mAbsentRegistrants.add(r);

        if (getCardState() == CardState.ABSENT) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForAbsent(Handler h) {
        mAbsentRegistrants.remove(h);
    }

    public boolean isApplicationOnIcc(AppType type) {
        for (UiccCardApplication a : mUiccApplications) {
            if (a != null && a.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private void logd(String msg) {
        Log.d(mLogTag, msg);
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCardApplication.java b/telephony/java/com/android/internal/telephony/UiccCardApplication.java
new file mode 100644
//Synthetic comment -- index 0000000..17007c1

//Synthetic comment -- @@ -0,0 +1,667 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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

import com.android.internal.telephony.UiccConstants.AppState;
import com.android.internal.telephony.UiccConstants.AppType;
import com.android.internal.telephony.UiccConstants.PersoSubState;
import com.android.internal.telephony.UiccConstants.PinState;
import com.android.internal.telephony.cdma.RuimFileHandler;
import com.android.internal.telephony.cdma.RuimRecords;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
/** This class will handle PIN, PUK, etc
 * Every user of this class will be registered for Unavailable with every
 * object it gets reference to. It is the user's responsibility to unregister
 * and remove reference to object, once UNAVAILABLE callback is received.
 */
public class UiccCardApplication {
    private static final int EVENT_PIN1PUK1_DONE = 1;
    private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 2;
    private static final int EVENT_CHANGE_PIN1_DONE = 3;
    private static final int EVENT_CHANGE_PIN2_DONE = 4;
    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 5;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 6;
    private static final int EVENT_PIN2PUK2_DONE = 7;

    private String mLogTag = "RIL_UiccCardApplication";
    protected boolean mDbg;

    private UiccCard mUiccCard; //parent
    private int mSlotId; //Icc slot number of the Icc this app resides on
    private AppState      mAppState;
    private AppType       mAppType;
    private PersoSubState mPersoSubState;
    private String        mAid;
    private String        mAppLabel;
    private boolean       mPin1Replaced;
    private PinState      mPin1State;
    private PinState      mPin2State;
    private boolean mDesiredFdnEnabled;
    private boolean mIccFdnEnabled = false; // Default to disabled.
                                            // Will be updated when SIM_READY.
    private UiccApplicationRecords mUiccApplicationRecords;
    private IccFileHandler mIccFh;

    private boolean mDestroyed = false; //set to true once this App is commanded to be disposed of.

    private CommandsInterface mCi;
    private Context mContext;

    private RegistrantList mReadyRegistrants = new RegistrantList();
    private RegistrantList mUnavailableRegistrants = new RegistrantList();
    private RegistrantList mLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    private RegistrantList mPersoSubstateRegistrants = new RegistrantList();

    UiccCardApplication(UiccCard uiccCard, UiccCardStatusResponse.CardStatus.AppStatus as, UiccRecords ur, Context c, CommandsInterface ci) {
        Log.d(mLogTag, "Creating UiccApp: " + as);
        mSlotId = uiccCard.getSlotId();
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

        mIccFh = createUiccFileHandler(as.app_type);
        mUiccApplicationRecords = createUiccApplicationRecords(as.app_type, ur, mContext, mCi);
        queryFdnEnabledIfReady();
    }

    void update (UiccCardStatusResponse.CardStatus.AppStatus as, UiccRecords ur, Context c, CommandsInterface ci) {
        if (mDestroyed) {
            Log.e(mLogTag, "Application updated after destroyed! Fix me!");
            return;
        }
        Log.d(mLogTag, mAppType + " update. New " + as);
        mContext = c;
        mCi = ci;

        if (as.app_type != mAppType) {
            mUiccApplicationRecords.dispose();
            mUiccApplicationRecords = createUiccApplicationRecords(as.app_type, ur, c, ci);
            mAppType = as.app_type;
        }

        if (mPersoSubState != as.perso_substate) {
            mPersoSubState = as.perso_substate;
            notifyNetworkLockedRegistrants();
            notifyPersoSubstateRegistrants();
        }

        mAid = as.aid;
        mAppLabel = as.app_label;
        mPin1Replaced = (as.pin1_replaced != 0);
        mPin1State = as.pin1;
        mPin2State = as.pin2;
        if (mAppState != as.app_state) {
            Log.d(mLogTag, mAppType + " changed state: " + mAppState + " -> " + as.app_state);
            mAppState = as.app_state;
            queryFdnEnabledIfReady();
            notifyLockedRegistrants();
            notifyReadyRegistrants();
        }
    }

    synchronized void dispose() {
        mDestroyed = true;
        mUiccApplicationRecords.dispose();
        mUiccApplicationRecords = null;
        mIccFh = null;
        notifyUnavailableRegistrants();
    }

    private UiccApplicationRecords createUiccApplicationRecords(AppType type, UiccRecords ur, Context c, CommandsInterface ci) {
        if (type == AppType.APPTYPE_USIM || type == AppType.APPTYPE_SIM) {
            return new SIMRecords(this, ur, c, ci);
        } else {
            return new RuimRecords(this, ur, c, ci);
        }
    }

    private IccFileHandler createUiccFileHandler(AppType type) {
        switch (type) {
            case APPTYPE_SIM:
                return new SIMFileHandler(this, mSlotId, mAid, mCi);
            case APPTYPE_RUIM:
                return new RuimFileHandler(this, mSlotId, mAid, mCi);
            case APPTYPE_USIM:
                return new UsimFileHandler(this, mSlotId, mAid, mCi);
            case APPTYPE_CSIM:
                return new CsimFileHandler(this, mSlotId, mAid, mCi);
            default:
                return null;
        }
    }

    public AppType getType() {
        return mAppType;
    }

    public synchronized UiccApplicationRecords getApplicationRecords() {
        return mUiccApplicationRecords;
    }

    public synchronized IccFileHandler getIccFileHandler() {
        return mIccFh;
    }

    public synchronized UiccCard getCard() {
        return mUiccCard;
    }

    public AppState getState()
    {
        return mAppState;
    }

    public PersoSubState getPersonalizationState() {
        return mPersoSubState;
    }

    public PinState getPin1State() {
        if (mPin1Replaced) {
            return mUiccCard.getUniversalPinState();
        } else {
            return mPin1State;
        }
    }

    public PinState getPin2State() {
        return mPin2State;
    }

    public String getAid() {
        return mAid;
    }

    public String getAppLabel() {
        return mAppLabel;
    }

    private synchronized void notifyAllRegistrants() {
        notifyUnavailableRegistrants();
        notifyLockedRegistrants();
        notifyReadyRegistrants();
        notifyNetworkLockedRegistrants();
        notifyPersoSubstateRegistrants();
    }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyUnavailableRegistrants(Registrant r) {
        if (mDestroyed) {
            if (r == null) {
                mUnavailableRegistrants.notifyRegistrants();
            } else {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
            return;
        }
    }

    private synchronized void notifyUnavailableRegistrants() {
        notifyUnavailableRegistrants(null);
    }


    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyLockedRegistrants(Registrant r) {
        if (mDestroyed) {
            return;
        }

        if (mAppState == AppState.APPSTATE_PIN ||
            mAppState == AppState.APPSTATE_PUK ||
            mAppState == AppState.APPSTATE_SUBSCRIPTION_PERSO) {
            if (mPin1State == PinState.PINSTATE_ENABLED_VERIFIED || mPin1State == PinState.PINSTATE_DISABLED) {
                Log.e(mLogTag, "Sanity check failed! APPSTATE is locked while PIN1 is not!!!");
                //Don't notify if application is in insane state
                return;
            }
            if (r == null) {
                Log.d(mLogTag, "Notifying registrants: LOCKED");
                mLockedRegistrants.notifyRegistrants();
            } else {
                Log.d(mLogTag, "Notifying 1 registrant: LOCKED");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    private synchronized void notifyPersoSubstateRegistrants() {
        notifyPersoSubstateRegistrants(null);
    }

    /** Notifies specified registrant.
    *
    * @param r Registrant to be notified. If null - all registrants will be notified
    */
   private synchronized void notifyPersoSubstateRegistrants(Registrant r) {
       if (mDestroyed) {
           return;
       }

       if (r == null) {
           mPersoSubstateRegistrants.notifyRegistrants();
       } else {
           r.notifyRegistrant(new AsyncResult(null, null, null));
       }
   }

   private synchronized void notifyLockedRegistrants() {
       notifyLockedRegistrants(null);
   }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyReadyRegistrants(Registrant r) {
        if (mDestroyed) {
            return;
        }
        if (mAppState == AppState.APPSTATE_READY) {
            if (mPin1State == PinState.PINSTATE_ENABLED_NOT_VERIFIED || mPin1State == PinState.PINSTATE_ENABLED_BLOCKED || mPin1State == PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
                Log.e(mLogTag, "Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
                //Don't notify if application is in insane state
                return;
            }
            if (r == null) {
                Log.d(mLogTag, "Notifying registrants: READY");
                mReadyRegistrants.notifyRegistrants();
            } else {
                Log.d(mLogTag, "Notifying 1 registrant: READY");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    private synchronized void notifyReadyRegistrants() {
        notifyReadyRegistrants(null);
    }

    /** Notifies specified registrant.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
     */
    private synchronized void notifyNetworkLockedRegistrants(Registrant r) {
        if (mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
            if (r == null) {
                mNetworkLockedRegistrants.notifyRegistrants();
            } else {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
            return;
        }
    }

    private synchronized void notifyNetworkLockedRegistrants() {
        notifyNetworkLockedRegistrants(null);
    }

    public synchronized void registerForReady(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant (h, what, obj);
        mReadyRegistrants.add(r);

        notifyReadyRegistrants(r);
    }
    public synchronized void unregisterForReady(Handler h) {
        mReadyRegistrants.remove(h);
    }

    public synchronized void registerForUnavailable(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant (h, what, obj);
        mUnavailableRegistrants.add(r);
    }
    public synchronized void unregisterForUnavailable(Handler h) {
        mUnavailableRegistrants.remove(h);
    }

    protected void finalize() {
        if(mDbg) Log.d(mLogTag, mAppType + " Finalized");
    }

    /**
     * Notifies handler of any transition into State.NETWORK_LOCKED
     */
    public void registerForNetworkLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mNetworkLockedRegistrants.add(r);
        notifyNetworkLockedRegistrants(r);
    }

    public void unregisterForNetworkLocked(Handler h) {
        mNetworkLockedRegistrants.remove(h);
    }

    /**
     * Notifies handler of any changes to PersoSubstate
     */
    public void registerForPersoSubstate(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mPersoSubstateRegistrants.add(r);
    }

    public void unregisterForPersoSubstate(Handler h) {
        mPersoSubstateRegistrants.remove(h);
    }

    /**
     * Notifies handler of any transition into State.isPinLocked()
     */
    public void registerForLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mLockedRegistrants.add(r);
        notifyLockedRegistrants(r);
    }

    public void unregisterForLocked(Handler h) {
        mLockedRegistrants.remove(h);
    }


    /**
     * Supply the ICC PIN to the ICC
     *
     * When the operation is complete, onComplete will be sent to it's
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
        mCi.supplyIccPin(mSlotId, mAid, pin, mHandler.obtainMessage(EVENT_PIN1PUK1_DONE, onComplete));
    }

    public void supplyPuk (String puk, String newPin, Message onComplete) {
        mCi.supplyIccPuk(mSlotId, mAid, puk, newPin,
                mHandler.obtainMessage(EVENT_PIN1PUK1_DONE, onComplete));
    }

    public void supplyPin2 (String pin2, Message onComplete) {
        mCi.supplyIccPin2(mSlotId, mAid, pin2,
                mHandler.obtainMessage(EVENT_PIN2PUK2_DONE, onComplete));
    }

    public void supplyPuk2 (String puk2, String newPin2, Message onComplete) {
        mCi.supplyIccPuk2(mSlotId, mAid, puk2, newPin2,
                mHandler.obtainMessage(EVENT_PIN2PUK2_DONE, onComplete));
    }

    public void supplyNetworkDepersonalization (String pin, Message onComplete) {
        if(mDbg) log("Network Despersonalization: " + pin);
        mCi.supplyNetworkDepersonalization(pin,
                mHandler.obtainMessage(EVENT_PIN1PUK1_DONE, onComplete));
    }

    /**
     * Check whether ICC pin lock is enabled
     * This is a sync call which returns the cached pin enabled state
     *
     * @return true for ICC locked enabled
     *         false for ICC locked disabled
     */
    public boolean getIccLockEnabled() {
        return mPin1State == PinState.PINSTATE_ENABLED_NOT_VERIFIED ||
               mPin1State == PinState.PINSTATE_ENABLED_VERIFIED ||
               mPin1State == PinState.PINSTATE_ENABLED_BLOCKED ||
               mPin1State == PinState.PINSTATE_ENABLED_PERM_BLOCKED;
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

        mCi.setFacilityLock(mSlotId, mAid, CommandsInterface.CB_FACILITY_BA_SIM,
                enabled, password, serviceClassX,
                mHandler.obtainMessage(EVENT_CHANGE_FACILITY_LOCK_DONE, onComplete));
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

        mCi.setFacilityLock(mSlotId, mAid, CommandsInterface.CB_FACILITY_BA_FD,
                enabled, password, serviceClassX,
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
        if(mDbg) log("Change Pin1 old: " + oldPassword + " new: " + newPassword);
        mCi.changeIccPin(mSlotId, mAid, oldPassword, newPassword,
                mHandler.obtainMessage(EVENT_CHANGE_PIN1_DONE, onComplete));

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
        if(mDbg) log("Change Pin2 old: " + oldPassword + " new: " + newPassword);
        mCi.changeIccPin2(mSlotId, mAid, oldPassword, newPassword,
                mHandler.obtainMessage(EVENT_CHANGE_PIN2_DONE, onComplete));

    }
    
    private void queryFdnEnabledIfReady() {
        if (mAppState == AppState.APPSTATE_READY) {
            queryFdnEnabled();
        }
    }

    private void queryFdnEnabled() {
        //This shouldn't change run-time. So needs to be called only once.
        int serviceClassX;

        serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                        CommandsInterface.SERVICE_CLASS_DATA +
                        CommandsInterface.SERVICE_CLASS_FAX;
        mCi.queryFacilityLock (mSlotId, mAid,
                CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
                mHandler.obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
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

    protected Handler mHandler = new Handler() {
        private void sendResultToTarget(Message m, Throwable e) {
            AsyncResult.forMessage(m).exception = e;
            m.sendToTarget();
        }

        @Override
        public void handleMessage(Message msg){
            AsyncResult ar;

            switch (msg.what) {
                case EVENT_PIN1PUK1_DONE:
                case EVENT_PIN2PUK2_DONE:
                    // a PIN/PUK/PIN2/PUK2/Network Personalization
                    // request has completed. ar.userObj is the response Message
                    ar = (AsyncResult)msg.obj;
                    sendResultToTarget((Message)ar.userObj, ar.exception);
                    break;
                case EVENT_QUERY_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;
                    onQueryFdnEnabled(ar);
                    break;
                case EVENT_CHANGE_FACILITY_LOCK_DONE:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        if (mDbg) log( "EVENT_CHANGE_FACILITY_LOCK_DONE ");
                    } else {
                        Log.e(mLogTag, "Error change facility sim lock with exception "
                            + ar.exception);
                    }
                    sendResultToTarget((Message)ar.userObj, ar.exception);
                    break;
                case EVENT_CHANGE_FACILITY_FDN_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception == null) {
                        mIccFdnEnabled = mDesiredFdnEnabled;
                        if (mDbg) log("EVENT_CHANGE_FACILITY_FDN_DONE ");
                    } else {
                        Log.e(mLogTag, "Error change facility fdn with exception "
                                + ar.exception);
                    }
                    sendResultToTarget((Message)ar.userObj, ar.exception);
                    break;
                case EVENT_CHANGE_PIN1_DONE:
                case EVENT_CHANGE_PIN2_DONE:
                    ar = (AsyncResult)msg.obj;
                    if(ar.exception != null) {
                        Log.e(mLogTag, "Error in change icc app password with exception"
                            + ar.exception);
                    }
                    sendResultToTarget((Message)ar.userObj, ar.exception);
                    break;
                default:
                    Log.e(mLogTag, "[IccCard] Unknown Event " + msg.what);
            }
        }
    };

    private void log(String msg) {
        Log.d(mLogTag, "[IccCard] " + msg);
    }

 }
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccCardStatusResponse.java b/telephony/java/com/android/internal/telephony/UiccCardStatusResponse.java
new file mode 100644
//Synthetic comment -- index 0000000..83d40bb

//Synthetic comment -- @@ -0,0 +1,61 @@
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

import com.android.internal.telephony.UiccConstants.AppType;
import com.android.internal.telephony.UiccConstants.AppState;
import com.android.internal.telephony.UiccConstants.CardState;
import com.android.internal.telephony.UiccConstants.PersoSubState;
import com.android.internal.telephony.UiccConstants.PinState;

/* An object of this class will be passed as the response to the RIL_REQUEST_GET_SIM_STATUS
 */
public class UiccCardStatusResponse {

    class CardStatus {
        class AppStatus {
            public AppType        app_type;
            public AppState       app_state;
            // applicable only if app_state == RIL_APPSTATE_SUBSCRIPTION_PERSO
            public PersoSubState  perso_substate;
            // null terminated string, e.g., from 0xA0, 0x00 -> 0x41, 0x30, 0x30, 0x30 */
            public String         aid;
            // null terminated string
            public String         app_label;
            // applicable to USIM and CSIM
            public int            pin1_replaced;
            public PinState       pin1;
            public PinState       pin2;
            public String toString() {
                return "AppStatus: " + app_type + " " + app_state + " " + perso_substate +
                       " aid:" + aid + " app_label:" + app_label + " pin1_replaced: " +
                       pin1_replaced + " pin1:" + pin1 + " pin2:" + pin2;
            }
        }
        CardState     card_state;
        PinState      universal_pin_state;
        int[]         subscription_3gpp_app_index;     /* value < RIL_CARD_MAX_APPS */
        int[]         subscription_3gpp2_app_index;    /* value < RIL_CARD_MAX_APPS */
        AppStatus[]   applications;
    }

    CardStatus[] cards;

    UiccCardStatusResponse() {

    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccConstants.java b/telephony/java/com/android/internal/telephony/UiccConstants.java
new file mode 100644
//Synthetic comment -- index 0000000..32aaefa

//Synthetic comment -- @@ -0,0 +1,64 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2009, Code Aurora Forum. All rights reserved.
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

public class UiccConstants {
    public enum AppState{
        APPSTATE_UNKNOWN,
        APPSTATE_DETECTED,
        APPSTATE_PIN,
        APPSTATE_PUK,
        APPSTATE_SUBSCRIPTION_PERSO,
        APPSTATE_READY;
    }

    public enum AppType{
        APPTYPE_UNKNOWN,
        APPTYPE_SIM,
        APPTYPE_USIM,
        APPTYPE_RUIM,
        APPTYPE_CSIM
    }


    public enum CardState {
        ABSENT,
        PRESENT,
        ERROR;
    }

    public enum PersoSubState{
        PERSOSUBSTATE_UNKNOWN,
        PERSOSUBSTATE_IN_PROGRESS,
        PERSOSUBSTATE_READY,
        PERSOSUBSTATE_SIM_NETWORK;
    }

    public enum PinState {
        PINSTATE_UNKNOWN,
        PINSTATE_ENABLED_NOT_VERIFIED,
        PINSTATE_ENABLED_VERIFIED,
        PINSTATE_DISABLED,
        PINSTATE_ENABLED_BLOCKED,
        PINSTATE_ENABLED_PERM_BLOCKED
    }

    static final int RIL_MAX_CARDS = 2;
    static final int RIL_CARD_MAX_APPS = 8;

}
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccManager.java b/telephony/java/com/android/internal/telephony/UiccManager.java
new file mode 100644
//Synthetic comment -- index 0000000..32a4d99

//Synthetic comment -- @@ -0,0 +1,228 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.android.internal.telephony.UiccConstants.CardState;
import com.android.internal.telephony.cat.CatService;

/* This class will be responsible for keeping all knowledge about
 * ICCs in the system. It will also be used as API to get appropriate
 * applications to pass them to phone and service trackers.
 */
public class UiccManager extends Handler{
    public enum AppFamily {
        APP_FAM_3GPP,
        APP_FAM_3GPP2;
    }

    private static UiccManager mInstance;

    private static final int EVENT_RADIO_ON = 1;
    private static final int EVENT_ICC_STATUS_CHANGED = 2;
    private static final int EVENT_GET_ICC_STATUS_DONE = 3;
    private static final int EVENT_RADIO_OFF_OR_UNAVAILABLE = 4;

    private String mLogTag = "RIL_UiccManager";
    CommandsInterface mCi;
    Context mContext;
    UiccCard[] mUiccCards;

    private RegistrantList mIccChangedRegistrants = new RegistrantList();
    private CatService mCatService;

    public static UiccManager getInstance(Context c, CommandsInterface ci) {
        if (mInstance == null) {
            mInstance = new UiccManager(c, ci);
        } else {
            mInstance.mCi = ci;
            mInstance.mContext = c;
        }
        return mInstance;
    }

    public static UiccManager getInstance() {
        if (mInstance == null) {
            return null;
        } else {
            return mInstance;
        }
    }

    private UiccManager(Context c, CommandsInterface ci) {
        Log.e(mLogTag, "Creating");

        mUiccCards = new UiccCard[UiccConstants.RIL_MAX_CARDS];

        mContext = c;
        mCi = ci;
        mCi.registerForOn(this,EVENT_RADIO_ON, null);
        mCi.registerForIccStatusChanged(this, EVENT_ICC_STATUS_CHANGED, null);
        mCi.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_UNAVAILABLE, null);

        mCatService = CatService.getInstance(mCi, null, mContext, null, null);

    }

    @Override
    public void handleMessage (Message msg) {
        AsyncResult ar;

        switch (msg.what) {
            case EVENT_RADIO_ON:
            case EVENT_ICC_STATUS_CHANGED:
                Log.d(mLogTag, "Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                mCi.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE, msg.obj));
                break;
            case EVENT_GET_ICC_STATUS_DONE:
                ar = (AsyncResult)msg.obj;

                onGetIccCardStatusDone(ar);

                //If UiccManager was provided with a callback when icc status update
                //was triggered - now is the time to call it.
                if (ar.userObj != null && ar.userObj instanceof AsyncResult) {
                    AsyncResult internalAr = (AsyncResult)ar.userObj;
                    if (internalAr.userObj != null &&
                            internalAr.userObj instanceof Message) {
                        Message onComplete = (Message)internalAr.userObj;
                        if (onComplete != null) {
                            onComplete.sendToTarget();
                        }
                    }
                } else if (ar.userObj != null && ar.userObj instanceof Message) {
                    Message onComplete = (Message)ar.userObj;
                    onComplete.sendToTarget();
                }
                break;
            case EVENT_RADIO_OFF_OR_UNAVAILABLE:
                disposeCards();
                break;
            default:
                Log.e(mLogTag, " Unknown Event " + msg.what);
        }

    }

    private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
        if (ar.exception != null) {
            Log.e(mLogTag,"Error getting ICC status. "
                    + "RIL_REQUEST_GET_ICC_STATUS should "
                    + "never return an error", ar.exception);
            return;
        }

        UiccCardStatusResponse status = (UiccCardStatusResponse)ar.result;

        boolean cardsChanged = false;

        for (int i = 0; i < UiccConstants.RIL_MAX_CARDS; i++) {
            //Update already existing cards
            if (mUiccCards[i] != null && i < status.cards.length) {
                if (mUiccCards[i].getCardState() != status.cards[i].card_state) {
                    cardsChanged = true;
                }
                mUiccCards[i].update(status.cards[i], mContext, mCi);
            }

            //Dispose of removed cards
            if (mUiccCards[i] != null && i >= status.cards.length) {
                mUiccCards[i].dispose();
                mUiccCards[i] = null;
                cardsChanged = true;
            }

            //Create added cards
            if (mUiccCards[i] == null && i < status.cards.length) {
                mUiccCards[i] = new UiccCard(this, i, status.cards[i], mContext, mCi);
                cardsChanged = true;
            }
        }

        if (cardsChanged) {
            mIccChangedRegistrants.notifyRegistrants();
        }
    }

    private synchronized void disposeCards() {
        for (int i = mUiccCards.length - 1; i >= 0; i--) {
            if (mUiccCards[i] != null) {
                mUiccCards[i].dispose();
                mUiccCards[i] = null;
            }
        }
    }

    public void triggerIccStatusUpdate(Object onComplete) {
        sendMessage(obtainMessage(EVENT_ICC_STATUS_CHANGED, onComplete));
    }

    public synchronized UiccCard[] getIccCards() {
        ArrayList<UiccCard> cards = new ArrayList<UiccCard>();
        for (UiccCard c: mUiccCards) {
            if (c != null && c.getCardState() == CardState.PRESENT) {
                cards.add(c);
            }
        }
        return (UiccCard[])cards.toArray();
    }

    /* Return First subscription of selected family */
    public synchronized UiccCardApplication getCurrentApplication(AppFamily family) {
        for (UiccCard c: mUiccCards) {
            if (c == null || c.getCardState() != CardState.PRESENT) continue;
            int[] subscriptions;
            if (family == AppFamily.APP_FAM_3GPP) {
                subscriptions = c.getSubscription3gppAppIndex();
            } else {
                subscriptions = c.getSubscription3gpp2AppIndex();
            }
            if (subscriptions != null && subscriptions.length > 0) {
                //return First current subscription
                UiccCardApplication app = c.getUiccCardApplication(subscriptions[0]);
                return app;
            } else {
                //No subscriptions found
                return null;
            }
        }
        //No Cards found
        return null;
    }

    //Notifies when any of the cards' STATE changes (or card gets added or removed)
    public void registerForIccChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        synchronized (mIccChangedRegistrants) {
            mIccChangedRegistrants.add(r);
        }
    }
    public void unregisterForIccChanged(Handler h) {
        synchronized (mIccChangedRegistrants) {
            mIccChangedRegistrants.remove(h);
        }
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UiccRecords.java b/telephony/java/com/android/internal/telephony/UiccRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..2350f69

//Synthetic comment -- @@ -0,0 +1,60 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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
import android.os.Registrant;
import android.os.RegistrantList;

/** This class will know how to access outside of
 * application DFs. (i.e. phonebook, etc.)
 * Every user of this class will be registered for Unavailable with every
 * object it gets reference to. It is the user's responsibility to unregister
 * and remove reference to object, once UNAVAILABLE callback is received.
 */
public class UiccRecords extends Handler implements IccConstants{
    UiccCard mUiccCard; //parent
    IccFileHandler mIccFh;

    private boolean mDestroyed = false; //set to true once this object is commanded to be disposed of.
    private RegistrantList mUnavailableRegistrants = new RegistrantList();

    UiccRecords(UiccCard uc) {
        mUiccCard = uc;
        //iccFh = new IccFileHandler(null); - IccFileHandler should be modified not to be abstract class
    }

    synchronized void dispose() {
        mDestroyed = true;
        mUnavailableRegistrants.notifyRegistrants();
        //TODO: Add code here
    }

    public synchronized void registerForUnavailable(Handler h, int what, Object obj) {
        if (mDestroyed) {
            return;
        }

        Registrant r = new Registrant (h, what, obj);
        mUnavailableRegistrants.add(r);
    }
    public synchronized void unregisterForUnavailable(Handler h) {
        mUnavailableRegistrants.remove(h);
    }

}
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UsimFileHandler.java b/telephony/java/com/android/internal/telephony/UsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..b18bd1d

//Synthetic comment -- @@ -0,0 +1,86 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
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

    public UsimFileHandler(UiccCardApplication app, int slotId, String aid, CommandsInterface ci) {
        super(app, slotId, aid, ci);
    }

    protected void finalize() {
        Log.d(LOG_TAG, "UsimFileHandler finalized");
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_SMS:
            return MF_SIM + DF_TELECOM;

        case EF_EXT6:
        case EF_MWIS:
        case EF_MBI:
        case EF_SPN:
        case EF_AD:
        case EF_MBDN:
        case EF_PNN:
        case EF_SPDI:
        case EF_SST:
        case EF_CFIS:
        case EF_MAILBOX_CPHS:
        case EF_VOICE_MAIL_INDICATOR_CPHS:
        case EF_CFF_CPHS:
        case EF_SPN_CPHS:
        case EF_SPN_SHORT_CPHS:
        case EF_INFO_CPHS:
            return MF_SIM + ADF;

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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..e281a18 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccApplicationRecords;

import android.util.Config;

//Synthetic comment -- @@ -115,7 +115,7 @@
public class CatService extends Handler implements AppInterface {

// Class members
    private static UiccApplicationRecords mIccRecords;

// Service members.
private static CatService sInstance;
//Synthetic comment -- @@ -147,7 +147,7 @@
private static final int DEV_ID_NETWORK     = 0x83;

/* Intentionally private for singleton */
    private CatService(CommandsInterface ci, UiccApplicationRecords ir, Context context,
IccFileHandler fh, IccCard ic) {
if (ci == null || ir == null || context == null || fh == null
|| ic == null) {
//Synthetic comment -- @@ -513,7 +513,7 @@
* @param ic Icc card
* @return The only Service object in the system
*/
    public static CatService getInstance(CommandsInterface ci, UiccApplicationRecords ir,
Context context, IccFileHandler fh, IccCard ic) {
if (sInstance == null) {
if (ci == null || ir == null || context == null || fh == null








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index a8d05552..84d8825 100644

//Synthetic comment -- @@ -50,12 +50,17 @@
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.UiccManager.AppFamily;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccConstants;
import com.android.internal.telephony.UiccManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneNotifier;
//Synthetic comment -- @@ -101,9 +106,14 @@
CdmaCallTracker mCT;
CdmaSMSDispatcher mSMS;
CdmaServiceStateTracker mSST;

    /* icc stuff */
    UiccManager mUiccManager = null;
    UiccCardApplication m3gpp2Application = null;
    UiccCard mRuimCard = null;
    RuimRecords mRuimRecords = null;
int mCdmaSubscriptionSource = CDMA_SUBSCRIPTION_NV;

ArrayList <CdmaMmiCode> mPendingMmis = new ArrayList<CdmaMmiCode>();
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
//Synthetic comment -- @@ -151,27 +161,27 @@
super(notifier, context, ci, unitTestMode);

mCM.setPhoneType(Phone.PHONE_TYPE_CDMA);
mCT = new CdmaCallTracker(this);
mSST = new CdmaServiceStateTracker (this);
mSMS = new CdmaSMSDispatcher(this);

mDataConnection = new CdmaDataConnectionTracker (this);
mRuimPhoneBookInterfaceManager = new RuimPhoneBookInterfaceManager(this);
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);

mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);
        mCM.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);

        mUiccManager = UiccManager.getInstance(getContext(), mCM);
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);

mCM.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);

//Synthetic comment -- @@ -218,7 +228,6 @@
super.dispose();

//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
//Synthetic comment -- @@ -234,14 +243,18 @@
mDataConnection.dispose();
mSST.dispose();
mSMS.dispose();

            //TODO -  fusion
            //mRuimPhoneBookInterfaceManager.dispose();
mRuimSmsInterfaceManager.dispose();
mSubInfo.dispose();
mEriManager.dispose();

            //cleanup icc stuff
            mUiccManager.unregisterForIccChanged(this);
            if(mRuimRecords != null) {
                unregisterForRuimRecordEvents();
            }
}
}

//Synthetic comment -- @@ -250,15 +263,16 @@
this.mRuimSmsInterfaceManager = null;
this.mSMS = null;
this.mSubInfo = null;
this.mDataConnection = null;
this.mCT = null;
this.mSST = null;
this.mEriManager = null;
this.mExitEcmRunnable = null;

            m3gpp2Application = null;
            mUiccManager = null;
            mRuimRecords = null;
            mRuimCard = null;
}

protected void finalize() {
//Synthetic comment -- @@ -497,7 +511,7 @@
}

public boolean handlePinMmi(String dialString) {
        CdmaMmiCode mmi = CdmaMmiCode.newFromDialString(dialString, this, m3gpp2Application);

if (mmi == null) {
Log.e(LOG_TAG, "Mmi is NULL!");
//Synthetic comment -- @@ -515,7 +529,7 @@
public boolean isDataConnectivityPossible() {
boolean noData = mDataConnection.getDataEnabled() &&
getDataConnectionState() == DataState.DISCONNECTED;
        return !noData && m3gpp2Application != null && m3gpp2Application.getState() == UiccConstants.AppState.APPSTATE_READY &&
getServiceState().getState() == ServiceState.STATE_IN_SERVICE &&
(mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
}
//Synthetic comment -- @@ -540,13 +554,20 @@
Log.e(LOG_TAG, "setLine1Number: not possible in CDMA");
}

    public UiccCard getUiccCard() {
return mRuimCard;
}

    public IccCard getIccCard() {
        throw new RuntimeException("getIccCard function in phone object should never be called. Use PhoneProxy instead.");
    }

public String getIccSerialNumber() {
        if (mRuimRecords != null) {
return mRuimRecords.iccid;
}
        return null;
    }

public void setCallWaiting(boolean enable, Message onComplete) {
Log.e(LOG_TAG, "method setCallWaiting is NOT supported in CDMA!");
//Synthetic comment -- @@ -724,10 +745,21 @@
public void setVoiceMailNumber(String alphaTag,
String voiceMailNumber,
Message onComplete) {

        if (mRuimRecords != null) {
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
mRuimRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
            return;
        }

        if (onComplete != null) {
            Exception e = new RuntimeException("Ruim is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
}

public String getVoiceMailNumber() {
//Synthetic comment -- @@ -742,7 +774,9 @@
* @hide
*/
public int getVoiceMessageCount() {
        int voicemailCount =  0;
        if (mRuimRecords != null)
            voicemailCount = mRuimRecords.getVoiceMessageCount();
// If mRuimRecords.getVoiceMessageCount returns zero, then there is possibility
// that phone was power cycled and would have lost the voicemail count.
// So get the count from preferences.
//Synthetic comment -- @@ -783,8 +817,11 @@
}

public boolean getIccRecordsLoaded() {
        if (mRuimRecords != null) {
return mRuimRecords.getRecordsLoaded();
}
        return false;
    }

public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
Log.e(LOG_TAG, "getCallForwardingOption: not possible in CDMA");
//Synthetic comment -- @@ -866,14 +903,18 @@
/*package*/ void
updateMessageWaitingIndicator(boolean mwi) {
// this also calls notifyMessageWaitingIndicator()
        if (mRuimRecords != null) {
mRuimRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
}
    }

/* This function is overloaded to send number of voicemails instead of sending true/false */
/*package*/ void
updateMessageWaitingIndicator(int mwi) {
        if (mRuimRecords != null) {
mRuimRecords.setVoiceMessageWaiting(1, mwi);
}
    }

/**
* Returns true if CDMA OTA Service Provisioning needs to be performed.
//Synthetic comment -- @@ -1025,6 +1066,15 @@
}
break;

            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                break;

            case EVENT_ICC_RECORD_EVENTS:
                ar = (AsyncResult)msg.obj;
                processIccRecordEvents((Integer)ar.result);
                break;

case EVENT_EMERGENCY_CALLBACK_MODE_ENTER:{
handleEnterEmergencyCallbackMode(msg);
}
//Synthetic comment -- @@ -1069,10 +1119,9 @@

case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE:
ar = (AsyncResult) msg.obj;
if (ar.exception == null) {
int newSubscriptionSource = ((int[]) ar.result)[0];
    
if (newSubscriptionSource != mCdmaSubscriptionSource) {
Log.v(LOG_TAG, "Subscription Source Changed : " + mCdmaSubscriptionSource
+ " >> " + newSubscriptionSource);
//Synthetic comment -- @@ -1088,6 +1137,7 @@
// trigger a change in Phone objects and this object will be destroyed.
Log.w(LOG_TAG, "Unable to get CDMA Subscription Source " + ar.exception);
}

break;

case EVENT_NV_READY:{
//Synthetic comment -- @@ -1123,6 +1173,44 @@
}
}

    private void processIccRecordEvents(int eventCode) {
        switch (eventCode) {
            case RuimRecords.EVENT_MWI:
                notifyMessageWaitingIndicator();
                break;
        }
    }

    void updateIccAvailability() {
        if (mUiccManager == null ) {
            return;
        }

        UiccCardApplication new3gpp2Application = mUiccManager
                .getCurrentApplication(AppFamily.APP_FAM_3GPP2);

        if (m3gpp2Application != new3gpp2Application) {
            if (m3gpp2Application != null) {
                Log.d(LOG_TAG, "Removing stale 3gpp Application.");
                if (mRuimRecords != null) {
                    unregisterForRuimRecordEvents();
                    mRuimRecords = null;
                    mRuimPhoneBookInterfaceManager.updateRuimRecords(null);
                }
                m3gpp2Application = null;
                mRuimCard = null;
            }
            if (new3gpp2Application != null) {
                Log.d(LOG_TAG, "New 3gpp application found");
                m3gpp2Application = new3gpp2Application;
                mRuimCard = new3gpp2Application.getCard();
                mRuimRecords = (RuimRecords) m3gpp2Application.getApplicationRecords();
                mRuimPhoneBookInterfaceManager.updateRuimRecords(mRuimRecords);
                registerForRuimRecordEvents();
            }
        }
    }

/**
* Retrieves the PhoneSubInfo of the CDMAPhone
*/
//Synthetic comment -- @@ -1174,7 +1262,10 @@
* {@inheritDoc}
*/
public IccFileHandler getIccFileHandler() {
        if (m3gpp2Application != null) {
            return m3gpp2Application.getIccFileHandler();
        }
        return null;
}

/**
//Synthetic comment -- @@ -1479,7 +1570,7 @@
getContext().getContentResolver().insert(uri, map);

// Updates MCC MNC device configuration information
                MccTable.updateMccMncConfiguration(this.getContext(), operatorNumeric);

return true;
} catch (SQLException e) {
//Synthetic comment -- @@ -1488,4 +1579,14 @@
}
return false;
}

    private void registerForRuimRecordEvents() {
        mRuimRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        mRuimRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
    }

    private void unregisterForRuimRecordEvents() {
        mRuimRecords.unregisterForRecordsEvents(this);
        mRuimRecords.unregisterForRecordsLoaded(this);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 93e27e7..e5038ff 100644

//Synthetic comment -- @@ -431,7 +431,7 @@
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY) {
return DisconnectCause.OUT_OF_SERVICE;
} else if (phone.mCdmaSubscriptionSource == Phone.CDMA_SUBSCRIPTION_RUIM_SIM
                           && phone.getIccCard().getState() != IccCard.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index f987dcd..ccae26b 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.UiccManager.AppFamily;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.EventLogTags;
//Synthetic comment -- @@ -114,6 +115,8 @@
// if we have no active Apn this is null
protected ApnSetting mActiveApn;

    protected static final AppFamily mAppFamily = AppFamily.APP_FAM_3GPP2;

// Possibly promote to base class, the only difference is
// the INTENT_RECONNECT_ALARM action is a different string.
// Do consider technology changes if it is promoted.
//Synthetic comment -- @@ -165,7 +168,6 @@

p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -223,9 +225,9 @@

public void dispose() {
// Unregister from all events
        mUiccManager.unregisterForIccChanged(this);
phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
phone.mCM.unregisterForDataStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -296,7 +298,7 @@
boolean roaming = phone.getServiceState().getRoaming();

if (((!mCdmaPhone.mSST.isSubscriptionFromRuim) ||
                (mUiccAppRecords != null && mUiccAppRecords.getRecordsLoaded())) &&
(mCdmaPhone.mSST.getCurrentCdmaDataConnectionState() ==
ServiceState.STATE_IN_SERVICE) &&
(!roaming || getDataOnRoamingEnabled()) &&
//Synthetic comment -- @@ -331,7 +333,7 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (psState == ServiceState.STATE_IN_SERVICE)
&& ((!mCdmaPhone.mSST.isSubscriptionFromRuim) ||
                        (mUiccAppRecords != null && mUiccAppRecords.getRecordsLoaded()))
&& (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
//Synthetic comment -- @@ -346,8 +348,8 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" PS state=" + psState +
                    " isSubscriptionFromRuim=" + (mCdmaPhone.mSST.isSubscriptionFromRuim) +
                    " ruim records loaded=" + ((mUiccAppRecords != null) ? mUiccAppRecords.getRecordsLoaded() : null) +
" concurrentVoice&Data=" + mCdmaPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" dataEnabled=" + getAnyDataEnabled() +








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
//Synthetic comment -- index 8dd8c2e..1132291 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.UiccCardApplication;

import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -54,6 +55,7 @@

CDMAPhone phone;
Context context;
    UiccCardApplication mUiccApp;

String action;              // ACTION_REGISTER
String sc;                  // Service Code
//Synthetic comment -- @@ -98,7 +100,7 @@
*/

public static CdmaMmiCode
    newFromDialString(String dialString, CDMAPhone phone, UiccCardApplication app) {
Matcher m;
CdmaMmiCode ret = null;

//Synthetic comment -- @@ -106,7 +108,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new CdmaMmiCode(phone, app);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -135,10 +137,11 @@

// Constructor

    CdmaMmiCode (CDMAPhone phone, UiccCardApplication app) {
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
        mUiccApp = app;
}

// MmiCode implementation
//Synthetic comment -- @@ -206,9 +209,11 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else {
                        if (mUiccApp != null) {
                            mUiccApp.supplyPuk(oldPinOrPuk, newPin,
obtainMessage(EVENT_SET_COMPLETE, this));
}
                    }
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index cf3ead3..d95ecf1 100755

//Synthetic comment -- @@ -53,6 +53,11 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UiccConstants.AppState;
import com.android.internal.telephony.UiccManager.AppFamily;

import java.util.Arrays;
import java.util.Calendar;
//Synthetic comment -- @@ -69,7 +74,12 @@
CdmaCellLocation cellLoc;
CdmaCellLocation newCellLoc;

    /* icc stuff */
    UiccManager mUiccManager = null;
    UiccCardApplication m3gpp2Application = null;
    RuimRecords mRuimRecords = null;

     /** if time between NTIZ updates is less than mNitzUpdateSpacing the update may be ignored. */
private static final int NITZ_UPDATE_SPACING_DEFAULT = 1000 * 60 * 10;
private int mNitzUpdateSpacing = SystemProperties.getInt("ro.nitz_update_spacing",
NITZ_UPDATE_SPACING_DEFAULT);
//Synthetic comment -- @@ -115,12 +125,6 @@
long mSavedTime;
long mSavedAtTime;

/** Wake lock used while setting time of day. */
private PowerManager.WakeLock mWakeLock;
private static final String WAKELOCK_TAG = "ServiceStateTracker";
//Synthetic comment -- @@ -184,6 +188,9 @@
cm.registerForCdmaSubscriptionSourceChanged(this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
cm.registerForCdmaPrlChanged(this, EVENT_CDMA_PRL_VERSION_CHANGED, null);

        mUiccManager = UiccManager.getInstance(phone.getContext(), this.cm);
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);

phone.registerForEriFileLoaded(this, EVENT_ERI_FILE_LOADED, null);
cm.registerForCdmaOtaProvision(this,EVENT_OTA_PROVISION_STATUS_CHANGE, null);

//Synthetic comment -- @@ -195,24 +202,32 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME), true,
mAutoTimeObserver);
setSignalStrengthDefaultValues();
}

public void dispose() {
// Unregister for all events.
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);

cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);
cm.unregisterForCdmaSubscriptionSourceChanged(this);
cm.unregisterForCdmaPrlChanged(this);

        //cleanup icc stuff
        mUiccManager.unregisterForIccChanged(this);
        if (m3gpp2Application != null) {
            m3gpp2Application.unregisterForReady(this);
        }
        if(mRuimRecords != null) {
            mRuimRecords.unregisterForRecordsLoaded(this);
        }
        m3gpp2Application = null;
        mUiccManager = null;
        mRuimRecords = null;
}

@Override
//Synthetic comment -- @@ -345,6 +360,10 @@
cm.getCdmaSubscriptionSource(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_SOURCE));
break;

        case EVENT_ICC_CHANGED:
            updateIccAvailability();
            break;

case EVENT_GET_CDMA_SUBSCRIPTION_SOURCE:
log("Received EVENT_GET_CDMA_SUBSCRIPTION_SOURCE: ");
ar = (AsyncResult) msg.obj;
//Synthetic comment -- @@ -365,8 +384,6 @@
if (!isSubscriptionFromRuim) {
// NV is ready when subscription source is NV
sendMessage(obtainMessage(EVENT_NV_READY));
}
cdmaSubscriptionSourceChangedRegistrants.notifyRegistrants();
}
//Synthetic comment -- @@ -378,11 +395,6 @@
case EVENT_RUIM_READY:
// The RUIM is now ready i.e if it was locked it has been
// unlocked. At this stage, the radio is already powered on.
if (DBG) log("Receive EVENT_RUIM_READY and Send Request getCDMASubscription.");
getSubscriptionInfoAndStartPollingThreads();
break;
//Synthetic comment -- @@ -530,8 +542,9 @@
if (!mIsMinInfoReady) {
mIsMinInfoReady = true;
}
                    if (mRuimRecords != null) {
                        mRuimRecords.setImsi(getImsi());
                    }
} else {
Log.w(LOG_TAG,"error parsing cdmaSubscription params num="
+ cdmaSubscription.length);
//Synthetic comment -- @@ -694,7 +707,9 @@
String plmn = "";
boolean showPlmn = false;
int rule = 0;
        if (isSubscriptionFromRuim
                && m3gpp2Application != null
                && m3gpp2Application.getState() == AppState.APPSTATE_READY) {
// TODO RUIM SPN is not implemented, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
//Synthetic comment -- @@ -1227,6 +1242,31 @@
}
}

    void updateIccAvailability() {

        UiccCardApplication new3gpp2Application = mUiccManager
                .getCurrentApplication(AppFamily.APP_FAM_3GPP2);

        if (m3gpp2Application != new3gpp2Application) {
            if (m3gpp2Application != null) {
                log("Removing stale 3gpp Application.");
                m3gpp2Application.unregisterForReady(this);
                if (mRuimRecords != null) {
                    mRuimRecords.unregisterForRecordsLoaded(this);
                    mRuimRecords = null;
                }
                m3gpp2Application = null;
            }
            if (new3gpp2Application != null) {
                log("New 3gpp2 application found");
                m3gpp2Application = new3gpp2Application;
                m3gpp2Application.registerForReady(this, EVENT_RUIM_READY, null);
                mRuimRecords = (RuimRecords) m3gpp2Application.getApplicationRecords();
                mRuimRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
            }
        }
    }

/**
* Returns a TimeZone object based only on parameters from the NITZ string.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimCard.java b/telephony/java/com/android/internal/telephony/cdma/RuimCard.java
deleted file mode 100644
//Synthetic comment -- index 0817d26..0000000

//Synthetic comment -- @@ -1,53 +0,0 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/telephony/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 3e2a29b..0dce1b5 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.*;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -26,6 +27,7 @@
import com.android.internal.telephony.IccIoResult;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.UiccCardApplication;

import java.util.ArrayList;

//Synthetic comment -- @@ -38,8 +40,8 @@
//***** Instance Variables

//***** Constructor
    public RuimFileHandler(UiccCardApplication app,int slotId, String aid, CommandsInterface ci) {
        super(app, slotId, aid, ci);
}

public void dispose() {
//Synthetic comment -- @@ -57,7 +59,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIO(mSlotId, mAid, COMMAND_GET_RESPONSE, fileid, "img", 0, 0,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index 6e12f24a..de7c752 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -32,7 +33,9 @@

public RuimPhoneBookInterfaceManager(CDMAPhone phone) {
super(phone);
        if (phone.mRuimRecords != null) {
            adnCache = phone.mRuimRecords.getAdnCache();
        }
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -49,6 +52,14 @@
if(DBG) Log.d(LOG_TAG, "RuimPhoneBookInterfaceManager finalized");
}

    public void updateRuimRecords(RuimRecords ruimRecords) {
        if (ruimRecords != null) {
            adnCache = ruimRecords.getAdnCache();
        } else {
            adnCache = null;
        }
    }

public int[] getAdnRecordsSize(int efid) {
if (DBG) logd("getAdnRecordsSize: efid=" + efid);
synchronized(mLock) {
//Synthetic comment -- @@ -58,11 +69,15 @@
//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

            IccFileHandler fh = phone.getIccFileHandler();
            //IccFileHandler can be null if there is no icc card present.
            if (fh != null) {
                fh.getEFLinearRecordSize(efid, response);
                try {
                    mLock.wait();
                } catch (InterruptedException e) {
                    logd("interrupted while trying to load from the RUIM");
                }
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 6b1e9d7..2e8045e 100644

//Synthetic comment -- @@ -16,32 +16,31 @@

package com.android.internal.telephony.cdma;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.UiccApplicationRecords;
import com.android.internal.telephony.UiccRecords;
import com.android.internal.telephony.UiccConstants.AppType;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.MccTable;

// can't be used since VoiceMailConstants is not public
//import com.android.internal.telephony.gsm.VoiceMailConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccUtils;


/**
* {@hide}
*/
public final class RuimRecords extends UiccApplicationRecords {
static final String LOG_TAG = "CDMA";

private static final boolean DBG = true;
//Synthetic comment -- @@ -49,7 +48,6 @@

// ***** Instance Variables

private String mMyMobileNumber;
private String mMin2Min1;

//Synthetic comment -- @@ -57,7 +55,6 @@

// ***** Event Constants

private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
private static final int EVENT_GET_ICCID_DONE = 5;
//Synthetic comment -- @@ -73,21 +70,21 @@
private static final int EVENT_RUIM_REFRESH = 31;


    public RuimRecords(UiccCardApplication parent, UiccRecords ur, Context c, CommandsInterface ci) {
        super(parent, c, ci, ur);

        adnCache = new AdnRecordCache(mFh);

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;

        //TODO: This probably is not required anymore - this whole object will be
        //destroyed once this event is received by UiccManager
        mCi.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
// NOTE the EVENT_SMS_ON_RUIM is not registered
        mCi.setOnIccRefresh(this, EVENT_RUIM_REFRESH, null);

// Start off by setting empty state
onRadioOffOrNotAvailable();
//Synthetic comment -- @@ -96,9 +93,8 @@

public void dispose() {
//Unregister for all events
        mCi.unregisterForOffOrNotAvailable( this);
        mCi.unSetOnIccRefresh(this);
}

@Override
//Synthetic comment -- @@ -142,6 +138,11 @@
Log.e(LOG_TAG, "method setVoiceMailNumber is not implemented");
}

    public void setImsi(String imsi) {
        mImsi = imsi;
        mImsiReadyRegistrants.notifyRegistrants();
    }

/**
* Called by CCAT Service when REFRESH is received.
* @param fileChanged indicates whether any files changed
//Synthetic comment -- @@ -187,14 +188,14 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed) {
Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}

try { switch (msg.what) {
            case EVENT_APP_READY:
onRuimReady();
break;

//Synthetic comment -- @@ -282,6 +283,7 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;
        Log.v(LOG_TAG, "RuimRecords:onRecordLoaded " + recordsToLoad + " requested: " + recordsRequested);

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
//Synthetic comment -- @@ -299,22 +301,11 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}

private void onRuimReady() {
fetchRuimRecords();
        mCi.getCDMASubscription(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_DONE));
}


//Synthetic comment -- @@ -323,7 +314,7 @@

Log.v(LOG_TAG, "RuimRecords:fetchRuimRecords " + recordsToLoad);

        mFh.loadEFTransparent(IccConstants.EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -353,7 +344,7 @@
}
countVoiceMessages = countWaiting;

        mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
}

private void handleRuimRefresh(int[] result) {
//Synthetic comment -- @@ -375,14 +366,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
if (DBG) log("handleRuimRefresh with SIM_REFRESH_RESET");
                onIccRefreshReset();
break;
default:
// unknown refresh operation








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 99728bd..8484380 100644

//Synthetic comment -- @@ -62,12 +62,17 @@
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccManager.AppFamily;
import com.android.internal.telephony.UiccConstants;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -100,14 +105,17 @@
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
GsmSMSDispatcher mSMS;

ArrayList <GsmMmiCode> mPendingMMIs = new ArrayList<GsmMmiCode>();
SimPhoneBookInterfaceManager mSimPhoneBookIntManager;
SimSmsInterfaceManager mSimSmsIntManager;
PhoneSubInfo mSubInfo;

    /* icc stuff */
    UiccManager mUiccManager = null;
    UiccCardApplication m3gppApplication = null;
    UiccCard mSimCard = null;
    SIMRecords mSIMRecords = null;

Registrant mPostDialHandler;

//Synthetic comment -- @@ -141,13 +149,13 @@
mSimulatedRadioControl = (SimulatedRadioControl) ci;
}

        mUiccManager = UiccManager.getInstance(context, mCM);
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);

mCM.setPhoneType(Phone.PHONE_TYPE_GSM);
mCT = new GsmCallTracker(this);
mSST = new GsmServiceStateTracker (this);
mSMS = new GsmSMSDispatcher(this);
mDataConnection = new GsmDataConnectionTracker (this);

if (!unitTestMode) {
//Synthetic comment -- @@ -155,11 +163,8 @@
mSimSmsIntManager = new SimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
}

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -211,7 +216,6 @@

//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
//Synthetic comment -- @@ -221,32 +225,37 @@
mPendingMMIs.clear();

//Force all referenced classes to unregister their former registered events
mCT.dispose();
mDataConnection.dispose();
mSST.dispose();

            //TODO - fusion
            //mSimPhoneBookIntManager.dispose();
mSimSmsIntManager.dispose();
mSubInfo.dispose();

            //cleanup icc stuff
            mUiccManager.unregisterForIccChanged(this);
            if(mSIMRecords != null) {
                unregisterForSimRecordEvents();
            }
}
}

public void removeReferences() {
this.mSimulatedRadioControl = null;
this.mSimPhoneBookIntManager = null;
this.mSimSmsIntManager = null;
this.mSMS = null;
this.mSubInfo = null;
this.mDataConnection = null;
this.mCT = null;
this.mSST = null;

            this.m3gppApplication = null;
            this.mUiccManager = null;
            this.mSimCard = null;
            this.mSIMRecords = null;
}

protected void finalize() {
//Synthetic comment -- @@ -280,12 +289,18 @@
}

public boolean getMessageWaitingIndicator() {
        if (mSIMRecords != null) {
return mSIMRecords.getVoiceMessageWaiting();
}
        return false;
    }

public boolean getCallForwardingIndicator() {
        if (mSIMRecords != null) {
return mSIMRecords.getVoiceCallForwardingFlag();
}
        return false;
    }

public List<? extends MmiCode>
getPendingMmiCodes() {
//Synthetic comment -- @@ -413,6 +428,7 @@
/*package*/ void
updateMessageWaitingIndicator(boolean mwi) {
// this also calls notifyMessageWaitingIndicator()
        if (mSIMRecords != null)
mSIMRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
}

//Synthetic comment -- @@ -729,7 +745,7 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this, m3gppApplication);
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -748,7 +764,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, m3gppApplication);

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -761,7 +777,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this, m3gppApplication);
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -814,7 +830,9 @@

public String getVoiceMailNumber() {
// Read from the SIM. If its null, try reading from the shared preference area.
        String number = null;
        if (mSIMRecords != null)
            number = mSIMRecords.getVoiceMailNumber();
if (TextUtils.isEmpty(number)) {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);
//Synthetic comment -- @@ -835,8 +853,9 @@
}

public String getVoiceMailAlphaTag() {
        String ret = null;

        if (mSIMRecords != null)
ret = mSIMRecords.getVoiceMailAlphaTag();

if (ret == null || ret.length() == 0) {
//Synthetic comment -- @@ -866,33 +885,48 @@
}

public String getSubscriberId() {
        return mSIMRecords != null ? mSIMRecords.getIMSI() : null;
}

public String getIccSerialNumber() {
        return mSIMRecords != null ? mSIMRecords.iccid : null;
}

public String getLine1Number() {
        return mSIMRecords != null ? mSIMRecords.getMsisdnNumber() : null;
}

public String getLine1AlphaTag() {
        return mSIMRecords != null ? mSIMRecords.getMsisdnAlphaTag() : null;
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
        if (mSIMRecords != null) {
mSIMRecords.setMsisdnNumber(alphaTag, number, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("Sim is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
        }
}

public void setVoiceMailNumber(String alphaTag,
String voiceMailNumber,
Message onComplete) {

        if (mSIMRecords != null) {
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
mSIMRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
            return;
        }

        if (onComplete != null) {
            Exception e = new RuntimeException("Sim is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
        }
}

private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
//Synthetic comment -- @@ -984,11 +1018,19 @@

public boolean
getIccRecordsLoaded() {
        if (mSIMRecords != null) {
            return mSIMRecords.getRecordsLoaded();
        }
        return false;
    }


    public UiccCard getUiccCard() {
        return mSimCard;
}

public IccCard getIccCard() {
        throw new RuntimeException("getIccCard function in phone object should never be called. Use PhoneProxy instead.");
}

public void
//Synthetic comment -- @@ -1111,7 +1153,7 @@
// check for "default"?
boolean noData = mDataConnection.getDataEnabled() &&
getDataConnectionState() == DataState.DISCONNECTED;
        return !noData && m3gppApplication != null && m3gppApplication.getState() == UiccConstants.AppState.APPSTATE_READY &&
getServiceState().getState() == ServiceState.STATE_IN_SERVICE &&
(mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
}
//Synthetic comment -- @@ -1183,7 +1225,8 @@
GsmMmiCode mmi;
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
                                                   GSMPhone.this,
                                                   m3gppApplication);
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1267,6 +1310,10 @@
mImeiSv = (String)ar.result;
break;

            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                break;

case EVENT_USSD:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1302,9 +1349,15 @@

case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
                if (ar.exception == null && mSIMRecords != null) {
mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
                if (mSIMRecords == null) {
                if (ar.exception == null) {
                        Log.w(LOG_TAG, "setVoiceCallForwardingFlag() aborted. icc absent?");
                        ar.exception = new RuntimeException("Sim card is absent.");
                    }
                }
onComplete = (Message) ar.userObj;
if (onComplete != null) {
AsyncResult.forMessage(onComplete, ar.result, ar.exception);
//Synthetic comment -- @@ -1338,6 +1391,21 @@
}
break;

            case EVENT_NEW_ICC_SMS:
                ar = (AsyncResult)msg.obj;
                mSMS.dispatchMessage((SmsMessage)ar.result);
                break;

            case EVENT_SET_NETWORK_AUTOMATIC:
                ar = (AsyncResult)msg.obj;
                setNetworkSelectionModeAutomatic((Message)ar.result);
                break;

            case EVENT_ICC_RECORD_EVENTS:
                ar = (AsyncResult)msg.obj;
                processIccRecordEvents((Integer)ar.result);
                break;

// handle the select network completion callbacks.
case EVENT_SET_NETWORK_MANUAL_COMPLETE:
case EVENT_SET_NETWORK_AUTOMATIC_COMPLETE:
//Synthetic comment -- @@ -1361,6 +1429,47 @@
}
}

    private void processIccRecordEvents(int eventCode) {
        switch (eventCode) {
            case SIMRecords.EVENT_CFI:
                notifyCallForwardingIndicator();
                break;
            case SIMRecords.EVENT_MWI:
                notifyMessageWaitingIndicator();
                break;
        }
    }

    void updateIccAvailability() {
        if (mUiccManager == null ) {
            return;
        }

        UiccCardApplication new3gppApplication = mUiccManager
                .getCurrentApplication(AppFamily.APP_FAM_3GPP);

        if (m3gppApplication != new3gppApplication) {
            if (m3gppApplication != null) {
                Log.d(LOG_TAG, "Removing stale 3gpp Application.");
                if (mSIMRecords != null) {
                    unregisterForSimRecordEvents();
                    mSIMRecords = null;
                    mSimPhoneBookIntManager.updateSimRecords(null);
                }
                m3gppApplication = null;
                mSimCard = null;
            }
            if (new3gppApplication != null) {
                Log.d(LOG_TAG, "New 3gpp application found");
                m3gppApplication = new3gppApplication;
                mSimCard = new3gppApplication.getCard();
                mSIMRecords = (SIMRecords) m3gppApplication.getApplicationRecords();
                registerForSimRecordEvents();
                mSimPhoneBookIntManager.updateSimRecords(mSIMRecords);
            }
        }
    }

/**
* Sets the "current" field in the telephony provider according to the SIM's operator
*
//Synthetic comment -- @@ -1433,6 +1542,12 @@
}

private void handleCfuQueryResult(CallForwardInfo[] infos) {

        if (mSIMRecords == null) {
            Log.w(LOG_TAG, "handleCfuQueryResult() called when mSIMRecords is null.");
            return; // will eventually fail anyway.
        }

if (infos == null || infos.length == 0) {
// Assume the default is not active
// Set unconditional CFF in SIM to false
//Synthetic comment -- @@ -1473,7 +1588,10 @@
* {@inheritDoc}
*/
public IccFileHandler getIccFileHandler(){
        if (m3gppApplication != null) {
            return m3gppApplication.getIccFileHandler();
        }
        return null;
}

public void activateCellBroadcastSms(int activate, Message response) {
//Synthetic comment -- @@ -1491,4 +1609,18 @@
public boolean isCspPlmnEnabled() {
return mSIMRecords.isCspPlmnEnabled();
}

    private void registerForSimRecordEvents() {
        mSIMRecords.registerForNetworkSelectionModeAutomatic(this, EVENT_SET_NETWORK_AUTOMATIC, null);
        mSIMRecords.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
        mSIMRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        mSIMRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
    }

    private void unregisterForSimRecordEvents() {
        mSIMRecords.unregisterForNetworkSelectionModeAutomatic(this);
        mSIMRecords.unregisterForNewSms(this);
        mSIMRecords.unregisterForRecordsEvents(this);
        mSIMRecords.unregisterForRecordsLoaded(this);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 7dc2504..d07587d 100644

//Synthetic comment -- @@ -371,7 +371,7 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (!phone.getIccRecordsLoaded()) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.rs.isCsRestricted()) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 5f651e7..0572a4b 100644

//Synthetic comment -- @@ -55,7 +55,11 @@
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccManager;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.UiccManager.AppFamily;

import java.io.IOException;
import java.util.ArrayList;
//Synthetic comment -- @@ -154,6 +158,8 @@
private RetryManager mDefaultRetryManager;
// for tracking retries on a secondary APN
private RetryManager mSecondaryRetryManager;
    
    protected static final AppFamily mAppFamily = AppFamily.APP_FAM_3GPP;

BroadcastReceiver mIntentReceiver = new BroadcastReceiver ()
{
//Synthetic comment -- @@ -205,9 +211,9 @@
GsmDataConnectionTracker(GSMPhone p) {
super(p);
mGsmPhone = p;

p.mCM.registerForAvailable (this, EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.registerForDataStateChanged (this, EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -281,9 +287,10 @@

public void dispose() {
//Unregister for all events
        mUiccManager.unregisterForIccChanged(this);

phone.mCM.unregisterForAvailable(this);
phone.mCM.unregisterForOffOrNotAvailable(this);
phone.mCM.unregisterForDataStateChanged(this);
mGsmPhone.mCT.unregisterForVoiceCallEnded(this);
mGsmPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -348,7 +355,7 @@
public boolean isDataConnectionAsDesired() {
boolean roaming = phone.getServiceState().getRoaming();

        if (mUiccAppRecords != null && mUiccAppRecords.getRecordsLoaded() &&
mGsmPhone.mSST.getCurrentGprsState() == ServiceState.STATE_IN_SERVICE &&
(!roaming || getDataOnRoamingEnabled()) &&
!mIsWifiConnected &&
//Synthetic comment -- @@ -441,7 +448,8 @@

if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
                && mUiccAppRecords != null
                && mUiccAppRecords.getRecordsLoaded()
&& (mGsmPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
//Synthetic comment -- @@ -468,7 +476,7 @@
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" gprsState=" + gprsState +
                    " sim=" + ((mUiccAppRecords != null) ? mUiccAppRecords.getRecordsLoaded() : null) +
" UMTS=" + mGsmPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +
" isDataAllowed=" + isDataAllowed() +
//Synthetic comment -- @@ -1254,7 +1262,12 @@
*/
private void createAllApnList() {
allApns = new ArrayList<ApnSetting>();
        String operator;
        if (mUiccAppRecords != null && mUiccAppRecords instanceof SIMRecords) {
            operator = ((SIMRecords)mUiccAppRecords).getSIMOperatorNumeric();
        } else {
            operator = "";
        }

if (operator != null) {
String selection = "numeric = '" + operator + "'";
//Synthetic comment -- @@ -1333,7 +1346,12 @@
return apnList;
}

        String operator;
        if (mUiccAppRecords != null && mUiccAppRecords instanceof SIMRecords) {
            operator = ((SIMRecords)mUiccAppRecords).getSIMOperatorNumeric();
        } else {
            operator = "";
        }

if (mRequestedApnType.equals(Phone.APN_TYPE_DEFAULT)) {
if (canSetPreferApn && preferredApn != null) {
//Synthetic comment -- @@ -1517,7 +1535,7 @@
trySetupData(Phone.REASON_PS_RESTRICT_ENABLED);
}
break;
                
default:
// handle the message in the super class DataConnectionTracker
super.handleMessage(msg);
//Synthetic comment -- @@ -1525,6 +1543,7 @@
}
}


protected void log(String s) {
Log.d(LOG_TAG, "[GsmDataConnectionTracker] " + s);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fe7a5cb..96a7d40 100644

//Synthetic comment -- @@ -110,6 +110,7 @@

GSMPhone phone;
Context context;
    UiccCardApplication mUiccApp;

String action;              // One of ACTION_*
String sc;                  // Service Code
//Synthetic comment -- @@ -173,7 +174,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone, UiccCardApplication app) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -181,7 +182,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone, app);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -196,11 +197,11 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone, app);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone, app);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -209,10 +210,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone, UiccCardApplication app) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone, app);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -228,8 +229,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone, UiccCardApplication app) {
        GsmMmiCode ret = new GsmMmiCode(phone, app);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -380,12 +381,13 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone, UiccCardApplication app) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
        mUiccApp = app;
}

//***** MmiCode implementation
//Synthetic comment -- @@ -674,10 +676,10 @@
String facility = scToBarringFacility(sc);

if (isInterrogate()) {
                    phone.mCM.queryFacilityLock(0, "", facility, password,
serviceClass, obtainMessage(EVENT_QUERY_COMPLETE, this));
} else if (isActivate() || isDeactivate()) {
                    phone.mCM.setFacilityLock(0, "", facility, isActivate(), password,
serviceClass, obtainMessage(EVENT_SET_COMPLETE, this));
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
//Synthetic comment -- @@ -739,24 +741,29 @@
} else if (pinLen < 4 || pinLen > 8 ) {
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
                    } else if (sc.equals(SC_PIN)
                            && phone.m3gppApplication != null
                            && phone.m3gppApplication.getState() == UiccConstants.AppState.APPSTATE_PUK) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {
// pre-checks OK
if (sc.equals(SC_PIN)) {
                            if (mUiccApp != null)
                                mUiccApp.changeIccLockPassword(oldPinOrPuk, newPin,
obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PIN2)) {
                            if (mUiccApp != null)
                                mUiccApp.changeIccFdnPassword(oldPinOrPuk, newPin,
                                        obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PUK)) {
                            if (mUiccApp != null)
                                mUiccApp.supplyPuk(oldPinOrPuk, newPin,
                                        obtainMessage(EVENT_SET_COMPLETE, this));
} else if (sc.equals(SC_PUK2)) {
                            if (mUiccApp != null)
                                mUiccApp.supplyPuk2(oldPinOrPuk, newPin,
                                        obtainMessage(EVENT_SET_COMPLETE, this));
}
}
} else {
//Synthetic comment -- @@ -860,7 +867,11 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    if (phone.mSIMRecords != null) {
                        phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
                    } else {
                        Log.w(LOG_TAG, "setVoiceCallForwardingFlag aborted. sim records is null.");
                    }
}

onSetComplete(ar);
//Synthetic comment -- @@ -1178,7 +1189,11 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            if (phone.mSIMRecords != null) {
                phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
            } else {
                Log.w(LOG_TAG, "setVoiceCallForwardingFlag aborted. sim records is null.");
            }
}

return TextUtils.replace(template, sources, destinations);
//Synthetic comment -- @@ -1203,7 +1218,11 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                if (phone.mSIMRecords != null) {
                    phone.mSIMRecords.setVoiceCallForwardingFlag(1, false);
                } else {
                    Log.w(LOG_TAG, "setVoiceCallForwardingFlag aborted. sim records is null.");
                }
} else {

SpannableStringBuilder tb = new SpannableStringBuilder();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 1dc46d7..efc0152 100644

//Synthetic comment -- @@ -63,6 +63,11 @@
import java.util.Date;
import java.util.TimeZone;

import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccManager;
import com.android.internal.telephony.UiccConstants.AppState;
import com.android.internal.telephony.UiccManager.AppFamily;

/**
* {@hide}
*/
//Synthetic comment -- @@ -76,6 +81,10 @@
int mPreferredNetworkType;
RestrictedState rs;

    /* icc stuff */
    UiccManager mUiccManager = null;
    UiccCardApplication m3gppApplication = null;
    SIMRecords mSIMRecords = null;
private int gprsState = ServiceState.STATE_OUT_OF_SERVICE;
private int newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;

//Synthetic comment -- @@ -123,12 +132,6 @@
long mSavedTime;
long mSavedAtTime;

/** Started the recheck process after finding gprs should registered but not. */
private boolean mStartedGprsRegCheck = false;

//Synthetic comment -- @@ -209,6 +212,8 @@
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);

        mUiccManager = UiccManager.getInstance(phone.getContext(), this.cm);
        mUiccManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
phone.getContext().getContentResolver(),
//Synthetic comment -- @@ -220,7 +225,6 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME), true,
mAutoTimeObserver);
setSignalStrengthDefaultValues();

// Monitor locale change
IntentFilter filter = new IntentFilter();
//Synthetic comment -- @@ -234,13 +238,24 @@
cm.unregisterForOn(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForNetworkStateChanged(this);

cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(this.mAutoTimeObserver);

        //cleanup icc stuff
        mUiccManager.unregisterForIccChanged(this);
        if (m3gppApplication != null) {
            m3gppApplication.unregisterForReady(this);
    }
        if(mSIMRecords != null) {
            mSIMRecords.unregisterForRecordsEvents(this);
            mSIMRecords.unregisterForRecordsLoaded(this);
        }
        m3gppApplication = null;
        mUiccManager = null;
        mSIMRecords = null;
}

protected void finalize() {
//Synthetic comment -- @@ -355,19 +370,19 @@
//setPowerStateToDesired();
break;

            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                break;

            case EVENT_ICC_RECORD_EVENTS:
                ar = (AsyncResult)msg.obj;
                processIccRecordEvents((Integer)ar.result);
break;

case EVENT_SIM_READY:
// The SIM is now ready i.e if it was locked
// it has been unlocked. At this stage, the radio is already
// powered on.
// restore the previous network selection.
phone.restoreSavedNetworkSelection(null);
pollState();
//Synthetic comment -- @@ -586,8 +601,12 @@
}

protected void updateSpnDisplay() {
        if (mSIMRecords == null) {
            Log.e(LOG_TAG, "mSIMRecords null while updateSpnDisplay was called.");
            return;
        }
        int rule = mSIMRecords.getDisplayRule(ss.getOperatorNumeric());
        String spn = mSIMRecords.getServiceProviderName();
String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN
//Synthetic comment -- @@ -1053,6 +1072,41 @@
(gprsState != ServiceState.STATE_IN_SERVICE));
}

    private void processIccRecordEvents(int eventCode) {
        switch (eventCode) {
            case SIMRecords.EVENT_SPN:
                updateSpnDisplay();
                break;
        }
    }

    void updateIccAvailability() {

        UiccCardApplication new3gppApplication = mUiccManager
                .getCurrentApplication(AppFamily.APP_FAM_3GPP);

        if (m3gppApplication != new3gppApplication) {
            if (m3gppApplication != null) {
                log("Removing stale 3gpp Application.");
                m3gppApplication.unregisterForReady(this);
                if (mSIMRecords != null) {
                    mSIMRecords.unregisterForRecordsEvents(this);
                    mSIMRecords.unregisterForRecordsLoaded(this);
                    mSIMRecords = null;
                }
                m3gppApplication = null;
            }
            if (new3gppApplication != null) {
                log("New 3gpp application found");
                m3gppApplication = new3gppApplication;
                m3gppApplication.registerForReady(this, EVENT_SIM_READY, null);
                mSIMRecords = (SIMRecords) m3gppApplication.getApplicationRecords();
                mSIMRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
                mSIMRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
            }
        }
    }

/**
* Returns a TimeZone object based only on parameters from the NITZ string.
*/
//Synthetic comment -- @@ -1166,7 +1220,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (m3gppApplication != null && m3gppApplication.getState() == AppState.APPSTATE_READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index e8d10f9..06f267a 100644

//Synthetic comment -- @@ -19,26 +19,24 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccConstants.AppType;

/**
* {@hide}
*/
public final class SIMFileHandler extends IccFileHandler implements IccConstants {
static final String LOG_TAG = "GSM";

//***** Instance Variables

//***** Constructor

    public SIMFileHandler(UiccCardApplication app, int slotId, String aid, CommandsInterface ci) {
        super(app, slotId, aid, ci);
}

public void dispose() {
//Synthetic comment -- @@ -90,13 +88,6 @@
}
String path = getCommonIccEFPath(efid);
if (path == null) {
Log.e(LOG_TAG, "Error: EF Path being returned in null");
}
return path;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 7db21c0..17c7275 100644

//Synthetic comment -- @@ -29,12 +29,16 @@
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.UiccApplicationRecords;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.UiccRecords;
import com.android.internal.telephony.UiccConstants.AppType;

import java.util.ArrayList;

//Synthetic comment -- @@ -42,7 +46,7 @@
/**
* {@hide}
*/
public final class SIMRecords extends UiccApplicationRecords {
static final String LOG_TAG = "GSM";

private static final boolean CRASH_RIL = false;
//Synthetic comment -- @@ -58,7 +62,6 @@

// ***** Cached SIM State; cleared on channel close

boolean callForwardingEnabled;


//Synthetic comment -- @@ -115,7 +118,6 @@

// ***** Event Constants

private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_IMSI_DONE = 3;
private static final int EVENT_GET_ICCID_DONE = 4;
//Synthetic comment -- @@ -146,10 +148,10 @@

// ***** Constructor

    public SIMRecords(UiccCardApplication parent, UiccRecords ur, Context c, CommandsInterface ci) {
        super(parent, c, ci, ur);

        adnCache = new AdnRecordCache(mFh);

mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();
//Synthetic comment -- @@ -160,11 +162,10 @@
recordsToLoad = 0;


        mCi.registerForOffOrNotAvailable(
this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mCi.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
        mCi.setOnIccRefresh(this, EVENT_SIM_REFRESH, null);

// Start off by setting empty state
onRadioOffOrNotAvailable();
//Synthetic comment -- @@ -173,9 +174,8 @@

public void dispose() {
//Unregister for all events
        mCi.unregisterForOffOrNotAvailable( this);
        mCi.unSetOnIccRefresh(this);
}

protected void finalize() {
//Synthetic comment -- @@ -183,7 +183,7 @@
}

protected void onRadioOffOrNotAvailable() {
        mImsi = null;
msisdn = null;
voiceMailNum = null;
countVoiceMessages = 0;
//Synthetic comment -- @@ -198,9 +198,9 @@

adnCache.reset();

        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, null);
        SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, null);
        SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY, null);

// recordsRequested is set to false indicating that the SIM
// read requests made so far are not valid. This is set to
//Synthetic comment -- @@ -213,7 +213,7 @@

/** Returns null if SIM is not yet ready */
public String getIMSI() {
        return mImsi;
}

public String getMsisdnNumber() {
//Synthetic comment -- @@ -246,7 +246,7 @@

AdnRecord adn = new AdnRecord(msisdnTag, msisdn);

        new AdnRecordLoader(mFh).updateEF(adn, IccConstants.EF_MSISDN, IccConstants.EF_EXT1, 1, null,
obtainMessage(EVENT_SET_MSISDN_DONE, onComplete));
}

//Synthetic comment -- @@ -298,14 +298,14 @@

if (mailboxIndex != 0 && mailboxIndex != 0xff) {

            new AdnRecordLoader(mFh).updateEF(adn, IccConstants.EF_MBDN, IccConstants.EF_EXT6,
mailboxIndex, null,
obtainMessage(EVENT_SET_MBDN_DONE, onComplete));

} else if (isCphsMailboxEnabled()) {

            new AdnRecordLoader(mFh).updateEF(adn, IccConstants.EF_MAILBOX_CPHS,
                    IccConstants.EF_EXT1, 1, null,
obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE, onComplete));

} else {
//Synthetic comment -- @@ -345,7 +345,7 @@

countVoiceMessages = countWaiting;

        mRecordsEventsRegistrants.notifyResult(EVENT_MWI);

try {
if (efMWIS != null) {
//Synthetic comment -- @@ -364,9 +364,9 @@
efMWIS[1] = (byte) countWaiting;
}

                mFh.updateEFLinearFixed(
                    IccConstants.EF_MWIS, 1, efMWIS, null,
                    obtainMessage (EVENT_UPDATE_DONE, IccConstants.EF_MWIS));
}

if (efCPHS_MWI != null) {
//Synthetic comment -- @@ -374,9 +374,9 @@
efCPHS_MWI[0] = (byte)((efCPHS_MWI[0] & 0xf0)
| (countVoiceMessages == 0 ? 0x5 : 0xa));

                mFh.updateEFTransparent(
                    IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS, efCPHS_MWI,
                    obtainMessage (EVENT_UPDATE_DONE, IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS));
}
} catch (ArrayIndexOutOfBoundsException ex) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -394,7 +394,7 @@

callForwardingEnabled = enable;

        mRecordsEventsRegistrants.notifyResult(EVENT_CFI);

try {
if (mEfCfis != null) {
//Synthetic comment -- @@ -408,9 +408,9 @@
// TODO: Should really update other fields in EF_CFIS, eg,
// dialing number.  We don't read or use it right now.

                mFh.updateEFLinearFixed(
                        IccConstants.EF_CFIS, 1, mEfCfis, null,
                        obtainMessage (EVENT_UPDATE_DONE, IccConstants.EF_CFIS));
}

if (mEfCff != null) {
//Synthetic comment -- @@ -422,9 +422,9 @@
| CFF_UNCONDITIONAL_DEACTIVE);
}

                mFh.updateEFTransparent(
                        IccConstants.EF_CFF_CPHS, mEfCff,
                        obtainMessage (EVENT_UPDATE_DONE, IccConstants.EF_CFF_CPHS));
}
} catch (ArrayIndexOutOfBoundsException ex) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -451,14 +451,14 @@
/** Returns the 5 or 6 digit MCC/MNC of the operator that
*  provided the SIM card. Returns null of SIM is not yet ready
*/
    public String getSIMOperatorNumeric() {
        if (mImsi == null || mncLength == UNINITIALIZED || mncLength == UNKNOWN) {
return null;
}

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
        return mImsi.substring(0, 3 + mncLength);
}

// ***** Overridden from Handler
//Synthetic comment -- @@ -470,14 +470,14 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed) {
Log.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}

try { switch (msg.what) {
            case EVENT_APP_READY:
onSimReady();
break;

//Synthetic comment -- @@ -496,22 +496,22 @@
break;
}

                mImsi = (String) ar.result;

// IMSI (MCC+MNC+MSIN) is at least 6 digits, but not more
// than 15 (and usually 15).
                if (mImsi != null && (mImsi.length() < 6 || mImsi.length() > 15)) {
                    Log.e(LOG_TAG, "invalid IMSI " + mImsi);
                    mImsi = null;
}

                Log.d(LOG_TAG, "IMSI: " + mImsi.substring(0, 6) + "xxxxxxxxx");

if (mncLength == UNKNOWN) {
// the SIM has told us all it knows, but it didn't know the mnc length.
// guess using the mcc
try {
                        int mcc = Integer.parseInt(mImsi.substring(0,3));
mncLength = MccTable.smallestDigitsMccForMnc(mcc);
} catch (NumberFormatException e) {
mncLength = UNKNOWN;
//Synthetic comment -- @@ -521,10 +521,9 @@

if (mncLength != UNKNOWN && mncLength != UNINITIALIZED) {
// finally have both the imsi and the mncLength and can parse the imsi properly
                    MccTable.updateMccMncConfiguration(mContext, mImsi.substring(0, 3 + mncLength));
}
                mImsiReadyRegistrants.notifyRegistrants();
break;

case EVENT_GET_MBI_DONE:
//Synthetic comment -- @@ -555,13 +554,13 @@

if (isValidMbdn) {
// Note: MBDN was not included in NUM_OF_SIM_RECORDS_LOADED
                    new AdnRecordLoader(mFh).loadFromEF(IccConstants.EF_MBDN, IccConstants.EF_EXT6,
mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
} else {
// If this EF not present, try mailbox as in CPHS standard
// CPHS (CPHS4_2.WW6) is a european standard.
                    new AdnRecordLoader(mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS,
                            IccConstants.EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
}

//Synthetic comment -- @@ -592,8 +591,8 @@
// FIXME right now, only load line1's CPHS voice mail entry

recordsToLoad += 1;
                        new AdnRecordLoader(mFh).loadFromEF(
                                IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
}
break;
//Synthetic comment -- @@ -609,8 +608,8 @@
// FIXME should use SST to decide
// FIXME right now, only load line1's CPHS voice mail entry
recordsToLoad += 1;
                    new AdnRecordLoader(mFh).loadFromEF(
                            IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1,
obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));

break;
//Synthetic comment -- @@ -678,7 +677,7 @@
countVoiceMessages = -1;
}

                mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
break;

case EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE:
//Synthetic comment -- @@ -707,7 +706,7 @@
countVoiceMessages = 0;
}

                    mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
}
break;

//Synthetic comment -- @@ -759,9 +758,9 @@
}
} finally {
if (mncLength == UNKNOWN || mncLength == UNINITIALIZED) {
                        if (mImsi != null) {
try {
                                int mcc = Integer.parseInt(mImsi.substring(0,3));

mncLength = MccTable.smallestDigitsMccForMnc(mcc);
} catch (NumberFormatException e) {
//Synthetic comment -- @@ -775,10 +774,10 @@
Log.d(LOG_TAG, "SIMRecords: MNC length not present in EF_AD");
}
}
                    if (mImsi != null && mncLength != UNKNOWN) {
// finally have both imsi and the length of the mnc and can parse
// the imsi properly
                        MccTable.updateMccMncConfiguration(mContext, mImsi.substring(0, 3 + mncLength));
}
}
break;
//Synthetic comment -- @@ -807,7 +806,7 @@
callForwardingEnabled =
((data[0] & CFF_LINE1_MASK) == CFF_UNCONDITIONAL_ACTIVE);

                    mRecordsEventsRegistrants.notifyResult(EVENT_CFI);
}
break;

//Synthetic comment -- @@ -880,7 +879,7 @@
+ ar.exception + " length " + index.length);
} else {
Log.d(LOG_TAG, "READ EF_SMS RECORD index=" + index[0]);
                    mFh.loadEFLinearFixed(IccConstants.EF_SMS,index[0],
obtainMessage(EVENT_GET_SMS_DONE));
}
break;
//Synthetic comment -- @@ -952,8 +951,8 @@
onCphsCompleted = null;
}

                    new AdnRecordLoader(mFh).
                            updateEF(adn, IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1, 1, null,
obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE,
onCphsCompleted));
} else {
//Synthetic comment -- @@ -1007,7 +1006,7 @@
// Refer TS 51.011 Section 10.3.46 for the content description
callForwardingEnabled = ((data[1] & 0x01) != 0);

                mRecordsEventsRegistrants.notifyResult(EVENT_CFI);
break;

case EVENT_GET_CSP_CPHS_DONE:
//Synthetic comment -- @@ -1039,20 +1038,20 @@

private void handleFileUpdate(int efid) {
switch(efid) {
            case IccConstants.EF_MBDN:
recordsToLoad++;
                new AdnRecordLoader(mFh).loadFromEF(IccConstants.EF_MBDN, IccConstants.EF_EXT6,
mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
break;
            case IccConstants.EF_MAILBOX_CPHS:
recordsToLoad++;
                new AdnRecordLoader(mFh).loadFromEF(IccConstants.EF_MAILBOX_CPHS, IccConstants.EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
            case IccConstants.EF_CSP_CPHS:
recordsToLoad++;
                Log.i(LOG_TAG,"CSP: SIM Refresh called for EF_CSP_CPHS");
                mFh.loadEFTransparent(IccConstants.EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
//Synthetic comment -- @@ -1086,7 +1085,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
		if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                mCi.setRadioPower(false, null);
/* Note: no need to call setRadioPower(true).  Assuming the desired
* radio power state is still ON (as tracked by ServiceStateTracker),
* ServiceStateTracker will call setRadioPower when it receives the
//Synthetic comment -- @@ -1117,7 +1116,7 @@
System.arraycopy(ba, 1, pdu, 0, n - 1);
SmsMessage message = SmsMessage.createFromPdu(pdu);

            mNewSmsRegistrants.notifyResult(message);
}
}

//Synthetic comment -- @@ -1143,7 +1142,7 @@
System.arraycopy(ba, 1, pdu, 0, n - 1);
SmsMessage message = SmsMessage.createFromPdu(pdu);

                mNewSmsRegistrants.notifyResult(message);

// 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
// 1 == "received by MS from network; message read"
//Synthetic comment -- @@ -1151,7 +1150,7 @@
ba[0] = 1;

if (false) { // XXX writing seems to crash RdoServD
                    mFh.updateEFLinearFixed(IccConstants.EF_SMS,
i, ba, null, obtainMessage(EVENT_MARK_SMS_READ_DONE, i));
}
}
//Synthetic comment -- @@ -1162,6 +1161,7 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;
        Log.v(LOG_TAG, "SIMRecords:onRecordLoaded " + recordsToLoad + " requested: " + recordsRequested);

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
//Synthetic comment -- @@ -1177,12 +1177,11 @@
String operator = getSIMOperatorNumeric();

// Some fields require more than one SIM record to set
        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, operator);

        if (mImsi != null) {
            SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
                    MccTable.countryCodeForMcc(Integer.parseInt(mImsi.substring(0,3))));
}
else {
Log.e("SIM", "[SIMRecords] onAllRecordsLoaded: imsi is NULL!");
//Synthetic comment -- @@ -1193,8 +1192,6 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}

//***** Private methods
//Synthetic comment -- @@ -1215,42 +1212,36 @@
}

private void onSimReady() {
fetchSimRecords();
}

private void fetchSimRecords() {
recordsRequested = true;
        //IccFileHandler iccFh = mFh;

Log.v(LOG_TAG, "SIMRecords:fetchSimRecords " + recordsToLoad);

        mCi.getIMSI(mParentApp.getCard().getSlotId(), mParentApp.getAid(),obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(IccConstants.EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

// FIXME should examine EF[MSISDN]'s capability configuration
// to determine which is the voice/data/fax line
        new AdnRecordLoader(mFh).loadFromEF(IccConstants.EF_MSISDN, IccConstants.EF_EXT1, 1,
obtainMessage(EVENT_GET_MSISDN_DONE));
recordsToLoad++;

// Record number is subscriber profile
        mFh.loadEFLinearFixed(IccConstants.EF_MBI, 1, obtainMessage(EVENT_GET_MBI_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(IccConstants.EF_AD, obtainMessage(EVENT_GET_AD_DONE));
recordsToLoad++;

// Record number is subscriber profile
        mFh.loadEFLinearFixed(IccConstants.EF_MWIS, 1, obtainMessage(EVENT_GET_MWIS_DONE));
recordsToLoad++;


//Synthetic comment -- @@ -1258,39 +1249,39 @@
// the same info as EF[MWIS]. If both exist, both are updated
// but the EF[MWIS] data is preferred
// Please note this must be loaded after EF[MWIS]
        mFh.loadEFTransparent(
                IccConstants.EF_VOICE_MAIL_INDICATOR_CPHS,
obtainMessage(EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE));
recordsToLoad++;

// Same goes for Call Forward Status indicator: fetch both
// EF[CFIS] and CPHS-EF, with EF[CFIS] preferred.
        mFh.loadEFLinearFixed(IccConstants.EF_CFIS, 1, obtainMessage(EVENT_GET_CFIS_DONE));
recordsToLoad++;
        mFh.loadEFTransparent(IccConstants.EF_CFF_CPHS, obtainMessage(EVENT_GET_CFF_DONE));
recordsToLoad++;


getSpnFsm(true, null);

        mFh.loadEFTransparent(IccConstants.EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
recordsToLoad++;

        mFh.loadEFLinearFixed(IccConstants.EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(IccConstants.EF_SST, obtainMessage(EVENT_GET_SST_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(IccConstants.EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(IccConstants.EF_CSP_CPHS,obtainMessage(EVENT_GET_CSP_CPHS_DONE));
recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
            mFh.loadEFLinearFixedAll(IccConstants.EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
recordsToLoad++;
}

//Synthetic comment -- @@ -1303,7 +1294,7 @@
+ "ffffffffffffffffffffffffffffff";
byte[] ba = IccUtils.hexStringToBytes(sms);

            mFh.updateEFLinearFixed(IccConstants.EF_SMS, 1, ba, null,
obtainMessage(EVENT_MARK_SMS_READ_DONE, 1));
}
}
//Synthetic comment -- @@ -1390,7 +1381,7 @@
case INIT:
spn = null;

                mFh.loadEFTransparent( IccConstants.EF_SPN,
obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1404,11 +1395,11 @@

if (DBG) log("Load EF_SPN: " + spn
+ " spnDisplayCondition: " + spnDisplayCondition);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);

spnState = Get_Spn_Fsm_State.IDLE;
} else {
                    mFh.loadEFTransparent( IccConstants.EF_SPN_CPHS,
obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1426,12 +1417,12 @@
data, 0, data.length - 1 );

if (DBG) log("Load EF_SPN_CPHS: " + spn);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);

spnState = Get_Spn_Fsm_State.IDLE;
} else {
                    mFh.loadEFTransparent(
                            IccConstants.EF_SPN_SHORT_CPHS, obtainMessage(EVENT_GET_SPN_DONE));
recordsToLoad++;

spnState = Get_Spn_Fsm_State.READ_SPN_SHORT_CPHS;
//Synthetic comment -- @@ -1444,7 +1435,7 @@
data, 0, data.length - 1);

if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);
}else {
if (DBG) log("No SPN loaded in either CHPS or 3GPP");
}
//Synthetic comment -- @@ -1550,7 +1541,6 @@
// Operator Selection menu should be disabled.
// Operator Selection Mode should be set to Automatic.
Log.i(LOG_TAG,"[CSP] Set Automatic Network Selection");
}
return;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimCard.java b/telephony/java/com/android/internal/telephony/gsm/SimCard.java
deleted file mode 100644
//Synthetic comment -- index 174b0f4..0000000

//Synthetic comment -- @@ -1,50 +0,0 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index feb508a..a2268e5 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -32,7 +33,9 @@

public SimPhoneBookInterfaceManager(GSMPhone phone) {
super(phone);
        if (phone.mSIMRecords != null) {
            adnCache = phone.mSIMRecords.getAdnCache();
        }
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -49,6 +52,14 @@
if(DBG) Log.d(LOG_TAG, "SimPhoneBookInterfaceManager finalized");
}

    public void updateSimRecords(SIMRecords simRecords) {
        if (simRecords != null) {
            adnCache = simRecords.getAdnCache();
        } else {
            adnCache = null;
        }
    }

public int[] getAdnRecordsSize(int efid) {
if (DBG) logd("getAdnRecordsSize: efid=" + efid);
synchronized(mLock) {
//Synthetic comment -- @@ -58,11 +69,15 @@
//Using mBaseHandler, no difference in EVENT_GET_SIZE_DONE handling
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE);

            IccFileHandler fh = phone.getIccFileHandler();
            //IccFileHandler can be null if there is no icc card present.
            if (fh != null) {
                fh.getEFLinearRecordSize(efid, response);
                try {
                    mLock.wait();
                } catch (InterruptedException e) {
                    logd("interrupted while trying to load from the SIM");
                }
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
//Synthetic comment -- index b642541..72b81b5 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneBase;

//Synthetic comment -- @@ -44,7 +45,7 @@
private static final boolean DBG = true;
private PbrFile mPbrFile;
private Boolean mIsPbrPresent;
    private IccFileHandler mFh;
private AdnRecordCache mAdnCache;
private Object mLock = new Object();
private ArrayList<AdnRecord> mPhoneBookRecords;
//Synthetic comment -- @@ -76,8 +77,8 @@
private static final int USIM_EFEMAIL_TAG = 0xCA;
private static final int USIM_EFCCP1_TAG  = 0xCB;

    public UsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache) {
        mFh = fh;
mPhoneBookRecords = new ArrayList<AdnRecord>();
mPbrFile = null;
// We assume its present, after the first read this is updated.
//Synthetic comment -- @@ -140,7 +141,7 @@
}

private void readPbrFileAndWait() {
        mFh.loadEFLinearFixedAll(EF_PBR, obtainMessage(EVENT_PBR_LOAD_DONE));
try {
mLock.wait();
} catch (InterruptedException e) {
//Synthetic comment -- @@ -167,7 +168,7 @@
}
}
// Read the EFEmail file.
            mFh.loadEFLinearFixedAll(fileIds.get(USIM_EFEMAIL_TAG),
obtainMessage(EVENT_EMAIL_LOAD_DONE));
try {
mLock.wait();
//Synthetic comment -- @@ -185,7 +186,7 @@
}

private void readIapFileAndWait(int efid) {
        mFh.loadEFLinearFixedAll(efid, obtainMessage(EVENT_IAP_LOAD_DONE));
try {
mLock.wait();
} catch (InterruptedException e) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index e9014ff..7b6202e 100644

//Synthetic comment -- @@ -109,7 +109,7 @@
unimplemented(result);
}

    public void supplyIccPin(int slot, String aid, String pin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PIN) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -151,7 +151,7 @@
}
}

    public void supplyIccPuk(int slot, String aid, String puk, String newPin, Message result)  {
if (mSimLockedState != SimLockState.REQUIRE_PUK) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -193,7 +193,7 @@
}
}

    public void supplyIccPin2(int slot, String aid, String pin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PIN2) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPin2: wrong state, state=" +
mSimFdnEnabledState);
//Synthetic comment -- @@ -234,7 +234,7 @@
}
}

    public void supplyIccPuk2(int slot, String aid, String puk2, String newPin2, Message result)  {
if (mSimFdnEnabledState != SimFdnState.REQUIRE_PUK2) {
Log.i(LOG_TAG, "[SimCmd] supplyIccPuk2: wrong state, state=" +
mSimLockedState);
//Synthetic comment -- @@ -275,7 +275,7 @@
}
}

    public void changeIccPin(int slot, String aid, String oldPin, String newPin, Message result)  {
if (oldPin != null && oldPin.equals(mPinCode)) {
mPinCode = newPin;
if (result != null) {
//Synthetic comment -- @@ -296,7 +296,7 @@
}
}

    public void changeIccPin2(int slot, String aid, String oldPin2, String newPin2, Message result)  {
if (oldPin2 != null && oldPin2.equals(mPin2Code)) {
mPin2Code = newPin2;
if (result != null) {
//Synthetic comment -- @@ -342,7 +342,7 @@
* @param serviceClass is a sum of SERVICE_CLASS_*
*/

    public void queryFacilityLock (int slot, String aid, String facility, String pin,
int serviceClass, Message result) {
if (facility != null &&
facility.equals(CommandsInterface.CB_FACILITY_BA_SIM)) {
//Synthetic comment -- @@ -377,7 +377,7 @@
* @param pin the SIM pin or "" if not required
* @param serviceClass is a sum of SERVICE_CLASS_*
*/
    public void setFacilityLock (int slot, String aid, String facility, boolean lockEnabled,
String pin, int serviceClass,
Message result) {
if (facility != null &&
//Synthetic comment -- @@ -516,7 +516,7 @@
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
    public void getIMSI(int slot, String aid, Message result) {
resultSuccess(result, "012345678901234");
}

//Synthetic comment -- @@ -1044,7 +1044,7 @@
* response.obj will be an AsyncResult
* response.obj.userObj will be a SimIoResult on success
*/
    public void iccIO (int slot, String aid, int command, int fileid, String path, int p1, int p2,
int p3, String data, String pin2, Message result) {
unimplemented(result);
}







