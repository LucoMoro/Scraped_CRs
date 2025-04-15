/*Telephony: Create IccCardProxy

Make IccCard an interface and pass instance of IccCardProxy to
external applications (PhoneApp). IccCardProxy will use internal UiccCard
to map Icc requests to current active application on UiccCard to
maintain backwards compatibility for external applications

Change-Id:Id81200c772177220be7cfe8a22b7c266610c71d0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index b093ef7..1557304 100644

//Synthetic comment -- @@ -16,278 +16,39 @@

package com.android.internal.telephony;

import android.os.Handler;
import android.os.Message;

import com.android.internal.telephony.IccCardConstants.State;

/**
* {@hide}
*/
public interface IccCard {
    public State getState();
    public IccRecords getIccRecords();
    public IccFileHandler getIccFileHandler();

/**
* Notifies handler of any transition into IccCardConstants.State.ABSENT
*/
    public void registerForAbsent(Handler h, int what, Object obj);
    public void unregisterForAbsent(Handler h);

/**
* Notifies handler of any transition into IccCardConstants.State.NETWORK_LOCKED
*/
    public void registerForNetworkLocked(Handler h, int what, Object obj);
    public void unregisterForNetworkLocked(Handler h);

/**
* Notifies handler of any transition into IccCardConstants.State.isPinLocked()
*/
    public void registerForLocked(Handler h, int what, Object obj);
    public void unregisterForLocked(Handler h);

    public void registerForReady(Handler h, int what, Object obj);
    public void unregisterForReady(Handler h);

/**
* Supply the ICC PIN to the ICC
//Synthetic comment -- @@ -310,25 +71,11 @@
*
*/

    public void supplyPin (String pin, Message onComplete);
    public void supplyPuk (String puk, String newPin, Message onComplete);
    public void supplyPin2 (String pin2, Message onComplete);
    public void supplyPuk2 (String puk2, String newPin2, Message onComplete);
    public void supplyNetworkDepersonalization (String pin, Message onComplete);

/**
* Check whether ICC pin lock is enabled
//Synthetic comment -- @@ -337,9 +84,7 @@
* @return true for ICC locked enabled
*         false for ICC locked disabled
*/
    public boolean getIccLockEnabled();

/**
* Check whether ICC fdn (fixed dialing number) is enabled
//Synthetic comment -- @@ -348,9 +93,7 @@
* @return true for ICC fdn enabled
*         false for ICC fdn disabled
*/
     public boolean getIccFdnEnabled();

/**
* Set the ICC pin lock enabled or disabled
//Synthetic comment -- @@ -364,18 +107,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void setIccLockEnabled (boolean enabled,
             String password, Message onComplete);

/**
* Set the ICC fdn enabled or disabled
//Synthetic comment -- @@ -389,19 +121,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void setIccFdnEnabled (boolean enabled,
             String password, Message onComplete);

/**
* Change the ICC password used in ICC pin lock
//Synthetic comment -- @@ -415,11 +135,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void changeIccLockPassword(String oldPassword, String newPassword,
             Message onComplete);

/**
* Change the ICC password used in ICC fdn enable
//Synthetic comment -- @@ -433,12 +149,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void changeIccFdnPassword(String oldPassword, String newPassword,
             Message onComplete);

/**
* Returns service provider name stored in ICC card.
//Synthetic comment -- @@ -456,490 +167,12 @@
*         yet available
*
*/
    public String getServiceProviderName ();
    public State getIccCardState();
    public boolean isApplicationOnIcc(IccCardApplication.AppType type);

/**
* @return true if a ICC card is present
*/
    public boolean hasIccCard();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
new file mode 100644
//Synthetic comment -- index 0000000..409732a

//Synthetic comment -- @@ -0,0 +1,547 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.uicc.UiccController;

import static com.android.internal.telephony.Phone.CDMA_SUBSCRIPTION_NV;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_SIM_STATE;

/*
 * The Phone App UI and the external world assumes that there is only one icc card,
 * and one icc application available at a time. But the Uicc Controller can handle
 * multiple instances of icc objects. This class implements the icc interface to expose
 * the  first application on the first icc card, so that external apps wont break.
 */

public class IccCardProxy extends Handler implements IccCard {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "RIL_IccCardProxy";

    private static final int EVENT_RADIO_OFF_OR_UNAVAILABLE = 1;
    private static final int EVENT_RADIO_ON = 2;
    private static final int EVENT_ICC_CHANGED = 3;
    private static final int EVENT_ICC_ABSENT = 4;
    private static final int EVENT_ICC_LOCKED = 5;
    private static final int EVENT_APP_READY = 6;
    private static final int EVENT_RECORDS_LOADED = 7;
    private static final int EVENT_IMSI_READY = 8;
    private static final int EVENT_NETWORK_LOCKED = 9;
    private static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 11;

    private Context mContext;
    private CommandsInterface mCi;

    private RegistrantList mReadyRegistrants = new RegistrantList();
    private RegistrantList mAbsentRegistrants = new RegistrantList();
    private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();

    private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
    private UiccController mUiccController = null;
    private UiccCard mUiccCard = null;
    private IccRecords mIccRecords = null;
    private CdmaSubscriptionSourceManager mCdmaSSM = null;
    private boolean mFirstRun = true;
    private boolean mRadioOn = false;
    private boolean mCdmaSubscriptionFromNv = false;
    private boolean mIsMultimodeCdmaPhone =
            SystemProperties.getBoolean("ro.config.multimode_cdma", false);
    private boolean mQuietMode = false; // when set to true IccCardProxy will not broadcast
                                        // ACTION_SIM_STATE_CHANGED intents
    private boolean mInitialized = false;

    public IccCardProxy(PhoneBase p, Context context, CommandsInterface ci) {
        this.mContext = context;
        this.mCi = ci;
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(context,
                ci, this, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        mUiccController = UiccController.getInstance();
        mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
        ci.registerForOn(this,EVENT_RADIO_ON, null);
        ci.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_UNAVAILABLE, null);
        SystemProperties.set(PROPERTY_SIM_STATE, "ABSENT");
    }

    public void dispose() {
        //Cleanup icc references
        mUiccController.unregisterForIccChanged(this);
        mUiccController = null;
        mCi.unregisterForOn(this);
        mCi.unregisterForOffOrNotAvailable(this);
        mCdmaSSM.dispose(this);
    }

    /*
     * The card application that the external world sees will be based on the
     * voice radio technology only!
     */
    public void setVoiceRadioTech(int radioTech) {
        if (DBG) log("Setting radio tech " + ServiceState.rilRadioTechnologyToString(radioTech));
        if (ServiceState.isGsm(radioTech)) {
            mCurrentAppType = UiccController.APP_FAM_3GPP;
        } else {
            mCurrentAppType = UiccController.APP_FAM_3GPP2;
        }
        mFirstRun = true;
        updateQuietMode();
    }

    /** This function does not necessarily updates mQuietMode right away
     * In case of 3GPP2 subscription it needs more information (subscription source)
     */
    private void updateQuietMode() {
        if (DBG) log("Updating Quiet mode");
        if (mCurrentAppType == UiccController.APP_FAM_3GPP) {
            mInitialized = true;
            mQuietMode = false;
            if (DBG) log("3GPP subscription -> QuietMode: " + mQuietMode);
            sendMessage(obtainMessage(EVENT_ICC_CHANGED));
        } else {
            //In case of 3gpp2 we need to find out if subscription used is coming from
            //NV in which case we shouldn't broadcast any sim states changes if at the
            //same time ro.config.multimode_cdma property set to false.
            mInitialized = false;
            handleCdmaSubscriptionSource();
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_RADIO_OFF_OR_UNAVAILABLE:
                mRadioOn = false;
                break;
            case EVENT_RADIO_ON:
                mRadioOn = true;
                if (!mInitialized) {
                    updateQuietMode();
                }
                break;
            case EVENT_ICC_CHANGED:
                if (mInitialized) {
                    updateIccAvailability();
                    updateStateProperty();
                }
                break;
            case EVENT_ICC_ABSENT:
                mAbsentRegistrants.notifyRegistrants();
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT, null);
                break;
            case EVENT_ICC_LOCKED:
                processLockedState();
                break;
            case EVENT_APP_READY:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_READY, null);
                break;
            case EVENT_RECORDS_LOADED:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOADED, null);
                break;
            case EVENT_IMSI_READY:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
                break;
            case EVENT_NETWORK_LOCKED:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOCKED,
                        IccCardConstants.INTENT_VALUE_LOCKED_NETWORK);
                mNetworkLockedRegistrants.notifyRegistrants();
                break;
            case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                updateQuietMode();
                break;
            default:
                Log.e(LOG_TAG, "Unhandled message with number: " + msg.what);
                break;
        }
    }

    void updateIccAvailability() {
        mUiccCard = mUiccController.getUiccCard();
        State state = State.UNKNOWN;
        IccRecords newRecords = null;
        if (mUiccCard != null) {
            state = mUiccCard.getState();
            if (DBG) log("Card State = " + state);
            newRecords = mUiccCard.getIccRecords();
        }

        if (mFirstRun) {
            if (mUiccCard == null || state == State.ABSENT) {
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT, null);
            }
            mFirstRun = false;
        }

        if (mIccRecords != newRecords) {
            if (mIccRecords != null) {
                unregisterUiccCardEvents();
                mIccRecords = null;
            }
            if (newRecords == null) {
                if (mRadioOn) {
                    broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT, null);
                } else {
                    broadcastIccStateChangedIntent(
                            IccCardConstants.INTENT_VALUE_ICC_NOT_READY, null);
                }
            } else {
                mIccRecords = newRecords;
                registerUiccCardEvents();
            }
        }
    }

    private void registerUiccCardEvents() {
        if (mUiccCard != null) mUiccCard.registerForReady(this, EVENT_APP_READY, null);
        if (mUiccCard != null) mUiccCard.registerForLocked(this, EVENT_ICC_LOCKED, null);
        if (mUiccCard != null) mUiccCard.registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, null);
        if (mUiccCard != null) mUiccCard.registerForAbsent(this, EVENT_ICC_ABSENT, null);
        if (mIccRecords != null) mIccRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
        if (mIccRecords != null) mIccRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
    }

    private void unregisterUiccCardEvents() {
        if (mUiccCard != null) mUiccCard.unregisterForReady(this);
        if (mUiccCard != null) mUiccCard.unregisterForLocked(this);
        if (mUiccCard != null) mUiccCard.unregisterForNetworkLocked(this);
        if (mUiccCard != null) mUiccCard.unregisterForAbsent(this);
        if (mIccRecords != null) mIccRecords.unregisterForImsiReady(this);
        if (mIccRecords != null) mIccRecords.unregisterForRecordsLoaded(this);
    }

    private void updateStateProperty() {
        SystemProperties.set(PROPERTY_SIM_STATE, getState().toString());
    }

    /* why do external apps need to use this? */
    public void broadcastIccStateChangedIntent(String value, String reason) {
        if (mQuietMode) {
            Log.e(LOG_TAG, "QuietMode: NOT Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value
                    + " reason " + reason);
            return;
        }

        Intent intent = new Intent(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(PhoneConstants.PHONE_NAME_KEY, "Phone");
        intent.putExtra(IccCardConstants.INTENT_KEY_ICC_STATE, value);
        intent.putExtra(IccCardConstants.INTENT_KEY_LOCKED_REASON, reason);

        if (DBG) log("Broadcasting intent ACTION_SIM_STATE_CHANGED " +  value
                + " reason " + reason);
        ActivityManagerNative.broadcastStickyIntent(intent, READ_PHONE_STATE);
    }

    public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.changeIccFdnPassword(oldPassword, newPassword, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.changeIccLockPassword(oldPassword, newPassword, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    private void processLockedState() {
        if (mUiccCard == null) {
            //Don't need to do anything if non-existent application is locked
            return;
        }
        State appState = mUiccCard.getState();
        switch (appState) {
            case PIN_REQUIRED:
                mPinLockedRegistrants.notifyRegistrants();
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOCKED,
                        IccCardConstants.INTENT_VALUE_LOCKED_ON_PIN);
                break;
            case PUK_REQUIRED:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOCKED,
                        IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK);
                break;
            case PERM_DISABLED:
                broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT,
                        IccCardConstants.INTENT_VALUE_ABSENT_ON_PERM_DISABLED);
                break;
        }
    }

    public State getIccCardState() {
        return getState();
    }

    public State getState() {
        if (mCurrentAppType == UiccController.APP_FAM_3GPP2 && mCdmaSubscriptionFromNv) {
            return State.READY;
        }
        if (mUiccCard != null) {
            mUiccCard.getState();
        }

        return State.UNKNOWN;
    }

    public boolean getIccFdnEnabled() {
        Boolean retValue = mUiccCard != null ? mUiccCard.getIccFdnEnabled() : false;
        return retValue;
    }

    public boolean getIccLockEnabled() {
        /* defaults to true, if ICC is absent */
        Boolean retValue = mUiccCard != null ? mUiccCard.getIccLockEnabled() : true;
        return retValue;
    }

    public String getServiceProviderName() {
        if (mUiccCard != null) {
            return mUiccCard.getServiceProviderName();
        }
        return null;
    }

    public boolean hasIccCard() {
        if (mUiccCard != null && mUiccCard.getState().iccCardExist()) {
            return true;
        }
        return false;
    }

    public boolean isApplicationOnIcc(IccCardApplication.AppType type) {
        Boolean retValue = mUiccCard != null ? mUiccCard.isApplicationOnIcc(type) : false;
        return retValue;
    }

    public void registerForReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mReadyRegistrants.add(r);

        if (getState() == State.READY) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForReady(Handler h) {
        mReadyRegistrants.remove(h);
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
        if (mUiccCard != null) {
            mUiccCard.setIccFdnEnabled(enabled, password, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.setIccLockEnabled(enabled, password, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    /**
     * Use invokeDepersonalization from PhoneBase class instead.
     */
    public void supplyNetworkDepersonalization(String pin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyNetworkDepersonalization(pin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("CommandsInterface is not set.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPin(String pin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPin(pin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPin2(String pin2, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPin2(pin2, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPuk(String puk, String newPin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPuk(puk, newPin, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPuk2(puk2, newPin2, onComplete);
        } else if (onComplete != null) {
            Exception e = new RuntimeException("ICC card is absent.");
            AsyncResult.forMessage(onComplete).exception = e;
            onComplete.sendToTarget();
            return;
        }
    }

    /**
     * Handles the call to get the subscription source
     *
     * @param holds the new CDMA subscription source value
     */
    private void handleCdmaSubscriptionSource() {
        int newSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();
        mCdmaSubscriptionFromNv = newSubscriptionSource == CDMA_SUBSCRIPTION_NV;
        if (mCdmaSubscriptionFromNv && mIsMultimodeCdmaPhone) {
            if (DBG) log("Cdma multimode phone detected. Forcing IccCardProxy into 3gpp mode");
            mCurrentAppType = UiccController.APP_FAM_3GPP;
        }
        boolean newQuietMode = mCdmaSubscriptionFromNv
                && (mCurrentAppType == UiccController.APP_FAM_3GPP2) && !mIsMultimodeCdmaPhone;
        if (mQuietMode == false && newQuietMode == true) {
            // Last thing to do before switching to Quiet mode is
            // broadcast ICC_READY
            if (DBG) log("Switching to QuietMode.");
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_READY, null);
        }
        mQuietMode = newQuietMode;
        if (DBG) log("QuietMode is " + mQuietMode + " (app_type: " + mCurrentAppType + " nv: "
                + mCdmaSubscriptionFromNv + " multimode: " + mIsMultimodeCdmaPhone + ")");
        mInitialized = true;
        sendMessage(obtainMessage(EVENT_ICC_CHANGED));
    }

    public IccFileHandler getIccFileHandler() {
        if (mUiccCard != null) {
            return mUiccCard.getIccFileHandler();
        }
        return null;
    }

    public IccRecords getIccRecords() {
        return mIccRecords;
    }

    public boolean getIccRecordsLoaded() {
        if (mIccRecords != null) {
            return mIccRecords.getRecordsLoaded();
        }
        return false;
    }

    protected void log(String s) {
        Log.d(LOG_TAG, s);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 70d8f7a..89ec562 100644

//Synthetic comment -- @@ -91,7 +91,7 @@

// member variables
protected final CommandsInterface mCi;
    protected final UiccCard mParentCard;
protected String mAid;

static class LoadLinearFixedContext {
//Synthetic comment -- @@ -122,7 +122,7 @@
/**
* Default constructor
*/
    protected IccFileHandler(UiccCard card, String aid, CommandsInterface ci) {
mParentCard = card;
mAid = aid;
mCi = ci;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index 3c90647..d542658 100644

//Synthetic comment -- @@ -39,9 +39,10 @@
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
    protected UiccCard mParentCard;

protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
    protected RegistrantList mImsiReadyRegistrants = new RegistrantList();
protected RegistrantList mRecordsEventsRegistrants = new RegistrantList();
protected RegistrantList mNewSmsRegistrants = new RegistrantList();
protected RegistrantList mNetworkSelectionModeAutomaticRegistrants = new RegistrantList();
//Synthetic comment -- @@ -63,6 +64,7 @@
protected String newVoiceMailTag = null;
protected boolean isVoiceMailFixed = false;
protected int countVoiceMessages = 0;
    protected String imsi;

protected int mncLength = UNINITIALIZED;
protected int mailboxIndex = 0; // 0 is no mailbox dailing number associated
//Synthetic comment -- @@ -104,7 +106,7 @@
}

// ***** Constructor
    public IccRecords(UiccCard card, Context c, CommandsInterface ci) {
mContext = c;
mCi = ci;
mFh = card.getIccFileHandler();
//Synthetic comment -- @@ -146,6 +148,22 @@
recordsLoadedRegistrants.remove(h);
}

    public void registerForImsiReady(Handler h, int what, Object obj) {
        if (mDestroyed.get()) {
            return;
        }

        Registrant r = new Registrant(h, what, obj);
        mImsiReadyRegistrants.add(r);

        if (imsi != null) {
            r.notifyRegistrant(new AsyncResult(null, null, null));
        }
    }
    public void unregisterForImsiReady(Handler h) {
        mImsiReadyRegistrants.remove(h);
    }

public void registerForRecordsEvents(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mRecordsEventsRegistrants.add(r);
//Synthetic comment -- @@ -182,6 +200,15 @@
return null;
}

    /**
     * Imsi could be set by ServiceStateTrackers in case of cdma
     * @param imsi
     */
    public void setImsi(String imsi) {
        this.imsi = imsi;
        mImsiReadyRegistrants.notifyRegistrants();
    }

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 866628b..f14c6b8 100644

//Synthetic comment -- @@ -130,7 +130,7 @@
boolean mIsVoiceCapable = true;
protected UiccController mUiccController = null;
public AtomicReference<IccRecords> mIccRecords = new AtomicReference<IccRecords>();
    protected AtomicReference<UiccCard> mUiccCard = new AtomicReference<UiccCard>();
public SmsStorageMonitor mSmsStorageMonitor;
public SmsUsageMonitor mSmsUsageMonitor;
public SMSDispatcher mSMS;
//Synthetic comment -- @@ -276,7 +276,7 @@
mSmsUsageMonitor = null;
mSMS = null;
mIccRecords.set(null);
        mUiccCard.set(null);
mDataConnectionTracker = null;
mUiccController = null;
}
//Synthetic comment -- @@ -648,9 +648,9 @@
* Retrieves the IccFileHandler of the Phone instance
*/
public IccFileHandler getIccFileHandler(){
        UiccCard uiccCard = mUiccCard.get();
        if (uiccCard == null) return null;
        return uiccCard.getIccFileHandler();
}

/*
//Synthetic comment -- @@ -676,7 +676,8 @@

@Override
public IccCard getIccCard() {
        return null;
        //throw new Exception("getIccCard Shouldn't be called from PhoneBase");
}

@Override
//Synthetic comment -- @@ -1181,7 +1182,7 @@
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
pw.println(" mIccRecords=" + mIccRecords.get());
        pw.println(" mUiccCard=" + mUiccCard.get());
pw.println(" mSmsStorageMonitor=" + mSmsStorageMonitor);
pw.println(" mSmsUsageMonitor=" + mSmsUsageMonitor);
pw.println(" mSMS=" + mSMS);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77135d4..a2d2806 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;
    private IccCardProxy mIccCardProxy;

private boolean mResetModemOnRadioTechnologyChange = false;

//Synthetic comment -- @@ -61,7 +62,7 @@
private static final String LOG_TAG = "PHONE";

//***** Class Methods
    public PhoneProxy(PhoneBase phone) {
mActivePhone = phone;
mResetModemOnRadioTechnologyChange = SystemProperties.getBoolean(
TelephonyProperties.PROPERTY_RESET_ON_RADIO_TECH_CHANGE, false);
//Synthetic comment -- @@ -76,6 +77,7 @@
mCommandsInterface.registerForOn(this, EVENT_RADIO_ON, null);
mCommandsInterface.registerForVoiceRadioTechChanged(
this, EVENT_VOICE_RADIO_TECH_CHANGED, null);
        mIccCardProxy = new IccCardProxy(phone, phone.getContext(), mCommandsInterface);
}

@Override
//Synthetic comment -- @@ -197,6 +199,7 @@
mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());

mCommandsInterface = ((PhoneBase)mActivePhone).mCM;
        mIccCardProxy.setVoiceRadioTech(newVoiceRadioTech);

// Send an Intent to the PhoneApp that we had a radio technology change
Intent intent = new Intent(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
//Synthetic comment -- @@ -457,11 +460,11 @@
}

public boolean getIccRecordsLoaded() {
        return mIccCardProxy.getIccRecordsLoaded();
}

public IccCard getIccCard() {
        return mIccCardProxy;
}

public void acceptCall() throws CallStateException {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 178addd..2990457 100644

//Synthetic comment -- @@ -38,7 +38,7 @@

protected CommandsInterface cm;
protected UiccController mUiccController = null;
    protected UiccCard mUiccCard = null;
protected IccRecords mIccRecords = null;

public ServiceState ss;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCard.java b/src/java/com/android/internal/telephony/UiccCard.java
new file mode 100644
//Synthetic comment -- index 0000000..afc37aa

//Synthetic comment -- @@ -0,0 +1,890 @@
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

import static android.Manifest.permission.READ_PHONE_STATE;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;
import android.view.WindowManager;

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cdma.CDMALTEPhone;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.cdma.CdmaLteUiccFileHandler;
import com.android.internal.telephony.cdma.CdmaLteUiccRecords;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.RuimFileHandler;
import com.android.internal.telephony.cdma.RuimRecords;

import android.os.SystemProperties;

import com.android.internal.R;

/**
 * {@hide}
 */
public class UiccCard {
    protected String mLogTag;
    protected boolean mDbg;

    protected IccCardStatus mIccCardStatus = null;
    protected State mState = null;
    protected Object mStateMonitor = new Object();

    protected boolean is3gpp = true;
    protected boolean isSubscriptionFromIccCard = true;
    protected CdmaSubscriptionSourceManager mCdmaSSM = null;
    protected PhoneBase mPhone;
    private IccRecords mIccRecords;
    private IccFileHandler mIccFileHandler;
    private CatService mCatService;

    private RegistrantList mAbsentRegistrants = new RegistrantList();
    private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    protected RegistrantList mReadyRegistrants = new RegistrantList();

    private boolean mDesiredPinLocked;
    private boolean mDesiredFdnEnabled;
    private boolean mIccPinLocked = true; // Default to locked
    private boolean mIccFdnEnabled = false; // Default to disabled.
                                            // Will be updated when SIM_READY.

    private static final int EVENT_GET_ICC_STATUS_DONE = 2;
    protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
    //private static final int EVENT_PINPUK_DONE = 4;
    //private static final int EVENT_REPOLL_STATUS_DONE = 5;
    protected static final int EVENT_ICC_READY = 6;
    private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 7;
    private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 8;
    private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;
    protected static final int EVENT_RADIO_ON = 12;
    private static final int EVENT_CARD_REMOVED = 13;
    private static final int EVENT_CARD_ADDED = 14;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 15;

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

    public UiccCard(PhoneBase phone, IccCardStatus ics, String logTag, boolean dbg) {
        mLogTag = logTag;
        log("Creating");
        mDbg = dbg;
        update(phone, ics);
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(mPhone.getContext(),
                mPhone.mCM, mHandler, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
    }

    public void dispose() {
        log("Disposing card type " + (is3gpp ? "3gpp" : "3gpp2"));
        mPhone.mCM.unregisterForIccStatusChanged(mHandler);
        mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForOn(mHandler);
        mCatService.dispose();
        mCdmaSSM.dispose(mHandler);
        mIccRecords.dispose();
        mIccFileHandler.dispose();
    }

    public void update(PhoneBase phone, IccCardStatus ics) {
        PhoneBase oldPhone = mPhone;
        mPhone = phone;
        log("Update");
        if (phone instanceof GSMPhone) {
            is3gpp = true;
        } else if (phone instanceof CDMALTEPhone){
            is3gpp = true;
        } else if (phone instanceof CDMAPhone){
            is3gpp = false;
        } else {
            loge("Update: Unhandled phone type. Critical error!" + phone.getPhoneName());
        }

        if (oldPhone != mPhone) {
            if (phone.mCM.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE
                    && phone instanceof CDMALTEPhone) {
                mIccFileHandler = new CdmaLteUiccFileHandler(this, "", mPhone.mCM);
                mIccRecords = new CdmaLteUiccRecords(this, mPhone.mContext, mPhone.mCM);
            } else {
                // Correct aid will be set later (when GET_SIM_STATUS returns)
                mIccFileHandler = is3gpp ? new SIMFileHandler(this, "", mPhone.mCM) :
                                           new RuimFileHandler(this, "", mPhone.mCM);
                mIccRecords = is3gpp ? new SIMRecords(this, mPhone.mContext, mPhone.mCM) :
                                       new RuimRecords(this, mPhone.mContext, mPhone.mCM);
            }
            mCatService = CatService.getInstance(mPhone.mCM, mIccRecords, mPhone.mContext, mIccFileHandler, this);
        }
        mHandler.sendMessage(mHandler.obtainMessage(EVENT_GET_ICC_STATUS_DONE, ics));
    }

    protected void finalize() {
        if (mDbg) log("Finalized card type " + (is3gpp ? "3gpp" : "3gpp2"));
    }

    public IccRecords getIccRecords() {
        return mIccRecords;
    }

    public IccFileHandler getIccFileHandler() {
        return mIccFileHandler;
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

    public State getSimState() {
        if(mIccCardStatus != null) {
            return getAppState(mIccCardStatus.getGsmUmtsSubscriptionAppIndex());
        } else {
            return State.UNKNOWN;
        }
    }

    public State getRuimState() {
        if(mIccCardStatus != null) {
            return getAppState(mIccCardStatus.getCdmaSubscriptionAppIndex());
        } else {
            return State.UNKNOWN;
        }
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
        mPhone.mCM.supplyIccPin(pin, onComplete);
    }

    public void supplyPuk (String puk, String newPin, Message onComplete) {
        mPhone.mCM.supplyIccPuk(puk, newPin, onComplete);
    }

    public void supplyPin2 (String pin2, Message onComplete) {
        mPhone.mCM.supplyIccPin2(pin2, onComplete);
    }

    public void supplyPuk2 (String puk2, String newPin2, Message onComplete) {
        mPhone.mCM.supplyIccPuk2(puk2, newPin2, onComplete);
    }

    public void supplyNetworkDepersonalization (String pin, Message onComplete) {
        if(mDbg) log("Network Despersonalization: " + pin);
        mPhone.mCM.supplyNetworkDepersonalization(pin, onComplete);
    }

    /**
     * Check whether ICC pin lock is enabled
     * This is a sync call which returns the cached pin enabled state
     *
     * @return true for ICC locked enabled
     *         false for ICC locked disabled
     */
    public boolean getIccLockEnabled() {
        return mIccPinLocked;
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

         mDesiredPinLocked = enabled;

         mPhone.mCM.setFacilityLock(CommandsInterface.CB_FACILITY_BA_SIM,
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

         mPhone.mCM.setFacilityLock(CommandsInterface.CB_FACILITY_BA_FD,
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
         mPhone.mCM.changeIccPin(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

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
         mPhone.mCM.changeIccPin2(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

     }


    /**
     * Returns service provider name stored in ICC card.
     * If there is no service provider name associated or the record is not
     * yet available, null will be returned <p>
     *
     * Please use this value when display Service Provider Name in idle mode <p>
     *
     * Usage of this provider name in the UI is a common carrier requirement.
     *
     * Also available via Android property "gsm.sim.operator.alpha"
     *
     * @return Service Provider Name stored in ICC card
     *         null if no service provider name associated or the record is not
     *         yet available
     *
     */
    public String getServiceProviderName () {
        return mIccRecords.getServiceProviderName();
    }

    protected void updateStateProperty() {
        mPhone.setSystemProperty(TelephonyProperties.PROPERTY_SIM_STATE, getState().toString());
    }

    private void getIccCardStatusDone(IccCardStatus ics) {
        /*if (ar.exception != null) {
            Log.e(mLogTag,"Error getting ICC status. "
                    + "RIL_REQUEST_GET_ICC_STATUS should "
                    + "never return an error", ar.exception);
            return;
        }*/
        handleIccCardStatus(ics);
    }

    private void handleIccCardStatus(IccCardStatus newCardStatus) {
        boolean transitionedIntoPinLocked;
        boolean transitionedIntoAbsent;
        boolean transitionedIntoNetworkLocked;
        boolean transitionedIntoPermBlocked;
        boolean isIccCardRemoved;
        boolean isIccCardAdded;

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
        transitionedIntoPermBlocked = (oldState != State.PERM_DISABLED
                && newState == State.PERM_DISABLED);
        isIccCardRemoved = (oldState != null &&
                        oldState.iccCardExist() && newState == State.ABSENT);
        isIccCardAdded = (oldState == State.ABSENT &&
                        newState != null && newState.iccCardExist());

        if (transitionedIntoPinLocked) {
            if (mDbg) log("Notify SIM pin or puk locked.");
            mPinLockedRegistrants.notifyRegistrants();
        } else if (transitionedIntoAbsent) {
            if (mDbg) log("Notify SIM missing.");
            mAbsentRegistrants.notifyRegistrants();
        } else if (transitionedIntoNetworkLocked) {
            if (mDbg) log("Notify SIM network locked.");
            mNetworkLockedRegistrants.notifyRegistrants();
        } else if (transitionedIntoPermBlocked) {
            if (mDbg) log("Notify SIM permanently disabled.");
            mPinLockedRegistrants.notifyRegistrants();
        }

        if (isIccCardRemoved) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_REMOVED, null));
        } else if (isIccCardAdded) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_ADDED, null));
        }

        if (oldState != State.READY && newState == State.READY) {
            mIccFileHandler.setAid(getAid());
            mReadyRegistrants.notifyRegistrants();
            mIccRecords.onReady();
        }

    }

    private void onIccSwap(boolean isAdded) {
        // TODO: Here we assume the device can't handle SIM hot-swap
        //      and has to reboot. We may want to add a property,
        //      e.g. REBOOT_ON_SIM_SWAP, to indicate if modem support
        //      hot-swap.
        DialogInterface.OnClickListener listener = null;


        // TODO: SimRecords is not reset while SIM ABSENT (only reset while
        //       Radio_off_or_not_available). Have to reset in both both
        //       added or removed situation.
        listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (mDbg) log("Reboot due to SIM swap");
                    PowerManager pm = (PowerManager) mPhone.getContext()
                    .getSystemService(Context.POWER_SERVICE);
                    pm.reboot("SIM is added.");
                }
            }

        };

        Resources r = Resources.getSystem();

        String title = (isAdded) ? r.getString(R.string.sim_added_title) :
            r.getString(R.string.sim_removed_title);
        String message = (isAdded) ? r.getString(R.string.sim_added_message) :
            r.getString(R.string.sim_removed_message);
        String buttonTxt = r.getString(R.string.sim_restart_button);

        AlertDialog dialog = new AlertDialog.Builder(mPhone.getContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonTxt, listener)
            .create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
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

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            AsyncResult ar;
            int serviceClassX;

            serviceClassX = CommandsInterface.SERVICE_CLASS_VOICE +
                            CommandsInterface.SERVICE_CLASS_DATA +
                            CommandsInterface.SERVICE_CLASS_FAX;

            if (!mPhone.mIsTheCurrentActivePhone) {
                Log.e(mLogTag, "Received message " + msg + "[" + msg.what
                        + "] while being destroyed. Ignoring.");
                return;
            }

            switch (msg.what) {
                case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                    mState = null;
                    updateStateProperty();
                    break;
                case EVENT_RADIO_ON:
                    if (!is3gpp) {
                        handleCdmaSubscriptionSource();
                    }
                    break;
                case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
                    handleCdmaSubscriptionSource();
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
                    IccCardStatus cs = (IccCardStatus)msg.obj;

                    getIccCardStatusDone(cs);
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
                case EVENT_CARD_REMOVED:
                    onIccSwap(false);
                    break;
                case EVENT_CARD_ADDED:
                    onIccSwap(true);
                    break;
                default:
                    Log.e(mLogTag, "[IccCard] Unknown Event " + msg.what);
            }
        }
    };

    private void handleCdmaSubscriptionSource() {
        if(mCdmaSSM != null)  {
            int newSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();

            if (mDbg) log("Received Cdma subscription source: " + newSubscriptionSource);

            boolean isNewSubFromRuim =
                (newSubscriptionSource == CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM);

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
            return State.ABSENT;
        }

        // this is common for all radio technologies
        if (!mIccCardStatus.getCardState().isCardPresent()) {
            return State.ABSENT;
        }

        RadioState currentRadioState = mPhone.mCM.getRadioState();
        // check radio technology
        if( currentRadioState == RadioState.RADIO_OFF         ||
            currentRadioState == RadioState.RADIO_UNAVAILABLE ) {
            return State.NOT_READY;
        }

        if( currentRadioState == RadioState.RADIO_ON ) {
            State csimState =
                getAppState(mIccCardStatus.getCdmaSubscriptionAppIndex());
            State usimState =
                getAppState(mIccCardStatus.getGsmUmtsSubscriptionAppIndex());

            if(mDbg) log("USIM=" + usimState + " CSIM=" + csimState);

            if (mPhone.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE) {
                // UICC card contains both USIM and CSIM
                // Return consolidated status
                return getConsolidatedState(csimState, usimState, csimState);
            }

            // check for CDMA radio technology
            if (!is3gpp) {
                return csimState;
            }
            return usimState;
        }

        return State.ABSENT;
    }

    private State getAppState(int appIndex) {
        IccCardApplication app;
        if (appIndex >= 0 && appIndex < IccCardStatus.CARD_MAX_APPS) {
            app = mIccCardStatus.getApplication(appIndex);
        } else {
            Log.e(mLogTag, "[IccCard] Invalid Subscription Application index:" + appIndex);
            return State.ABSENT;
        }

        if (app == null) {
            Log.e(mLogTag, "[IccCard] Subscription Application in not present");
            return State.ABSENT;
        }

        // check if PIN required
        if (app.pin1.isPermBlocked()) {
            return State.PERM_DISABLED;
        }
        if (app.app_state.isPinRequired()) {
            mIccPinLocked = true;
            return State.PIN_REQUIRED;
        }
        if (app.app_state.isPukRequired()) {
            mIccPinLocked = true;
            return State.PUK_REQUIRED;
        }
        if (app.app_state.isSubscriptionPersoEnabled()) {
            return State.NETWORK_LOCKED;
        }
        if (app.app_state.isAppReady()) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_ICC_READY));
            return State.READY;
        }
        if (app.app_state.isAppNotReady()) {
            return State.NOT_READY;
        }
        return State.NOT_READY;
    }

    private State getConsolidatedState(State left, State right, State preferredState) {
        // Check if either is absent.
        if (right == State.ABSENT) return left;
        if (left == State.ABSENT) return right;

        // Only if both are ready, return ready
        if ((left == State.READY) && (right == State.READY)) {
            return State.READY;
        }

        // Case one is ready, but the other is not.
        if (((right == State.NOT_READY) && (left == State.READY)) ||
            ((left == State.NOT_READY) && (right == State.READY))) {
            return State.NOT_READY;
        }

        // At this point, the other state is assumed to be one of locked state
        if (right == State.NOT_READY) return left;
        if (left == State.NOT_READY) return right;

        // At this point, FW currently just assumes the status will be
        // consistent across the applications...
        return preferredState;
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
        Log.d(mLogTag, "[UiccCard] " + msg);
    }

    private void loge(String msg) {
        Log.e(mLogTag, "[UiccCard] " + msg);
    }

    protected int getCurrentApplicationIndex() {
        if (is3gpp) {
            return mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
        } else {
            return mIccCardStatus.getCdmaSubscriptionAppIndex();
        }
    }

    public String getAid() {
        int appIndex = getCurrentApplicationIndex();

        IccCardApplication app;
        if (appIndex >= 0 && appIndex < IccCardStatus.CARD_MAX_APPS) {
            app = mIccCardStatus.getApplication(appIndex);
        } else {
            Log.e(mLogTag, "[UiccCard] Invalid Subscription Application index:" + appIndex);
            return "";
        }

        if (app != null) {
            return app.aid;
        }
        return "";
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 2b37072..dc6b9f0 100644

//Synthetic comment -- @@ -28,9 +28,9 @@

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;


import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -102,7 +102,7 @@

/* Intentionally private for singleton */
private CatService(CommandsInterface ci, IccRecords ir, Context context,
            IccFileHandler fh, UiccCard ic) {
if (ci == null || ir == null || context == null || fh == null
|| ic == null) {
throw new NullPointerException(
//Synthetic comment -- @@ -554,7 +554,7 @@
* @return The only Service object in the system
*/
public static CatService getInstance(CommandsInterface ci, IccRecords ir,
            Context context, IccFileHandler fh, UiccCard ic) {
synchronized (sInstanceLock) {
if (sInstance == null) {
if (ci == null || ir == null || context == null || fh == null








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f88fa9c..c8afe4b 100755

//Synthetic comment -- @@ -64,6 +64,7 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.uicc.UiccController;
//Synthetic comment -- @@ -1071,10 +1072,10 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        UiccCard c = mUiccCard.get();
        if (c != newUiccCard) {
if (c != null) {
log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
//Synthetic comment -- @@ -1084,12 +1085,12 @@
}
}
mIccRecords.set(null);
                mUiccCard.set(null);
}
            if (newUiccCard != null) {
log("New card found");
                mUiccCard.set(newUiccCard);
                mIccRecords.set(newUiccCard.getIccRecords());
registerForRuimRecordEvents();
if (mRuimPhoneBookInterfaceManager != null) {
mRuimPhoneBookInterfaceManager.updateIccRecords(mIccRecords.get());








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 07e496d..b986254 100644

//Synthetic comment -- @@ -42,11 +42,11 @@
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UiccCard;
import com.android.internal.util.AsyncChannel;
import com.android.internal.telephony.RILConstants;

//Synthetic comment -- @@ -1023,10 +1023,10 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();
IccRecords newIccRecords = null;
        if (newUiccCard != null) {
            newIccRecords = newUiccCard.getIccRecords();
}

IccRecords r = mIccRecords.get();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 43db046..b173035 100644

//Synthetic comment -- @@ -359,7 +359,7 @@
ss.setOperatorAlphaLong(eriText);
}

            if (mUiccCard != null && mUiccCard.getState() == IccCardConstants.State.READY &&
mIccRecords != null) {
// SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
// one configfured in SIM, use operator name  from CSIM record.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java
//Synthetic comment -- index 93a6290..1b34279 100644

//Synthetic comment -- @@ -19,9 +19,10 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCard;

import android.os.Message;

/**
//Synthetic comment -- @@ -30,7 +31,7 @@
public final class CdmaLteUiccFileHandler extends IccFileHandler {
static final String LOG_TAG = "CDMA";

    public CdmaLteUiccFileHandler(UiccCard card, String aid, CommandsInterface ci) {
super(card, aid, ci);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java
//Synthetic comment -- index eaa2ede..2f2c288 100755

//Synthetic comment -- @@ -24,12 +24,12 @@
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccCardApplication.AppType;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.ims.IsimRecords;
//Synthetic comment -- @@ -57,7 +57,7 @@

private final IsimUiccRecords mIsimUiccRecords = new IsimUiccRecords();

    public CdmaLteUiccRecords(UiccCard card, Context c, CommandsInterface ci) {
super(card, c, ci);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 8247d54..e579336 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -29,6 +28,7 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.CommandsInterface.RadioState;

import android.app.AlarmManager;
//Synthetic comment -- @@ -206,7 +206,7 @@
cm.unregisterForVoiceNetworkStateChanged(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
        if (mUiccCard != null) {mUiccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -397,17 +397,15 @@
mIsMinInfoReady = true;

updateOtaspState();
                    if (!isSubscriptionFromRuim && mIccRecords != null) {
if (DBG) {
                            log("GET_CDMA_SUBSCRIPTION set imsi in mIccRecords");
}
                        mIccRecords.setImsi(getImsi());
} else {
if (DBG) {
                            log("GET_CDMA_SUBSCRIPTION either mIccRecords is null  or NV type device" +
                                    " - not setting Imsi in mIccRecords");
}
}
} else {
//Synthetic comment -- @@ -1700,24 +1698,24 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        if (mUiccCard != newUiccCard) {
            if (mUiccCard != null) {
log("Removing stale icc objects.");
                mUiccCard.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mUiccCard = null;
}
            if (newUiccCard != null) {
log("New card found");
                mUiccCard = newUiccCard;
                mIccRecords = mUiccCard.getIccRecords();
if (isSubscriptionFromRuim) {
                    mUiccCard.registerForReady(this, EVENT_RUIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index f440935..965db6d 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -29,6 +28,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.UiccCard;

import java.util.ArrayList;

//Synthetic comment -- @@ -41,7 +41,7 @@
//***** Instance Variables

//***** Constructor
    public RuimFileHandler(UiccCard card, String aid, CommandsInterface ci) {
super(card, aid, ci);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index d3e04bd..1cd0c68 100755

//Synthetic comment -- @@ -32,10 +32,10 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.UiccCard;

// can't be used since VoiceMailConstants is not public
//import com.android.internal.telephony.gsm.VoiceMailConstants;
//Synthetic comment -- @@ -80,7 +80,7 @@
private static final int EVENT_RUIM_REFRESH = 31;


    public RuimRecords(UiccCard card, Context c, CommandsInterface ci) {
super(card, c, ci);

adnCache = new AdnRecordCache(mFh);
//Synthetic comment -- @@ -345,8 +345,6 @@
}
recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index fa0ef10..c0f9cc7 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccRecords;
//Synthetic comment -- @@ -71,6 +70,7 @@
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -711,7 +711,7 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this, mUiccCard.get());
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -730,7 +730,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, mUiccCard.get());

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -743,7 +743,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this, mUiccCard.get());
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -1141,7 +1141,7 @@
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
GSMPhone.this,
                                                   mUiccCard.get());
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1341,10 +1341,10 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        UiccCard c = mUiccCard.get();
        if (c != newUiccCard) {
if (c != null) {
if (LOCAL_DEBUG) log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
//Synthetic comment -- @@ -1352,12 +1352,12 @@
mSimPhoneBookIntManager.updateIccRecords(null);
}
mIccRecords.set(null);
                mUiccCard.set(null);
}
            if (newUiccCard != null) {
if (LOCAL_DEBUG) log("New card found");
                mUiccCard.set(newUiccCard);
                mIccRecords.set(newUiccCard.getIccRecords());
registerForSimRecordEvents();
mSimPhoneBookIntManager.updateIccRecords(mIccRecords.get());
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index adce14d..80e0320 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.UiccCard;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -2631,10 +2632,10 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();
IccRecords newIccRecords = null;
        if (newUiccCard != null) {
            newIccRecords = newUiccCard.getIccRecords();
}

IccRecords r = mIccRecords.get();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 2c6934c..b725c69 100644

//Synthetic comment -- @@ -110,7 +110,7 @@

GSMPhone phone;
Context context;
    UiccCard mUiccCard;
IccRecords mIccRecords;

String action;              // One of ACTION_*
//Synthetic comment -- @@ -175,7 +175,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone, UiccCard card) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -214,7 +214,7 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone, UiccCard card) {
GsmMmiCode ret;

ret = new GsmMmiCode(phone, card);
//Synthetic comment -- @@ -233,7 +233,7 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone, UiccCard card) {
GsmMmiCode ret = new GsmMmiCode(phone, card);

ret.message = ussdMessge;
//Synthetic comment -- @@ -385,13 +385,13 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone, UiccCard card) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
        mUiccCard = card;
if (card != null) {
mIccRecords = card.getIccRecords();
}
//Synthetic comment -- @@ -771,7 +771,7 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else if (sc.equals(SC_PIN) &&
                               mUiccCard.getState() == IccCardConstants.State.PUK_REQUIRED ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 046d220..13f637a 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;

import android.app.AlarmManager;
import android.app.Notification;
//Synthetic comment -- @@ -239,7 +240,7 @@
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        if (mUiccCard != null) {mUiccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
//Synthetic comment -- @@ -1136,7 +1137,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (mUiccCard.getState() == IccCardConstants.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1668,23 +1669,23 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        if (mUiccCard != newUiccCard) {
            if (mUiccCard != null) {
log("Removing stale icc objects.");
                mUiccCard.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mUiccCard = null;
}
            if (newUiccCard != null) {
log("New card found");
                mUiccCard = newUiccCard;
                mIccRecords = mUiccCard.getIccRecords();
                mUiccCard.registerForReady(this, EVENT_SIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index dcc9cfd..0637e8f 100644

//Synthetic comment -- @@ -20,11 +20,11 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.UiccCard;

/**
* {@hide}
//Synthetic comment -- @@ -36,7 +36,7 @@

//***** Constructor

    public SIMFileHandler(UiccCard card, String aid, CommandsInterface ci) {
super(card, aid, ci);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index d5099f7..4a9b047 100755

//Synthetic comment -- @@ -31,7 +31,6 @@
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
//Synthetic comment -- @@ -43,6 +42,7 @@
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.UiccCard;

import java.util.ArrayList;

//Synthetic comment -- @@ -66,7 +66,6 @@

// ***** Cached SIM State; cleared on channel close

private boolean callForwardingEnabled;


//Synthetic comment -- @@ -176,7 +175,7 @@

// ***** Constructor

    public SIMRecords(UiccCard card, Context c, CommandsInterface ci) {
super(card, c, ci);

adnCache = new AdnRecordCache(mFh);
//Synthetic comment -- @@ -582,8 +581,7 @@
// finally have both the imsi and the mncLength and can parse the imsi properly
MccTable.updateMccMncConfiguration(mContext, imsi.substring(0, 3 + mncLength));
}
                mImsiReadyRegistrants.notifyRegistrants();
break;

case EVENT_GET_MBI_DONE:
//Synthetic comment -- @@ -1277,8 +1275,6 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}

//***** Private methods








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index ab7d561..81f20bc 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.UiccCard;

import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -37,6 +38,9 @@
private static final boolean DBG = true;
private static final String LOG_TAG = "RIL_UiccController";

    public static final int APP_FAM_3GPP =  1;
    public static final int APP_FAM_3GPP2 = 2;

private static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;

//Synthetic comment -- @@ -44,7 +48,7 @@

private PhoneBase mCurrentPhone;
private CommandsInterface mCi;
    private UiccCard mUiccCard;
private boolean mRegisteredWithCi = false;

private RegistrantList mIccChangedRegistrants = new RegistrantList();
//Synthetic comment -- @@ -63,8 +67,8 @@
return getInstance(null);
}

    public synchronized UiccCard getUiccCard() {
        return mUiccCard;
}

//Notifies when card status changes
//Synthetic comment -- @@ -112,19 +116,19 @@
IccCardStatus status = (IccCardStatus)ar.result;

//Update already existing card
        if (mUiccCard != null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
            mUiccCard.update(mCurrentPhone, status);
}

//Dispose of removed card
        if (mUiccCard != null && status.getCardState() != CardState.CARDSTATE_PRESENT) {
            mUiccCard.dispose();
            mUiccCard = null;
}

//Create new card
        if (mUiccCard == null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
            mUiccCard = new UiccCard(mCurrentPhone, status, mCurrentPhone.getPhoneName(), true);
}

if (DBG) log("Notifying IccChangedRegistrants");
//Synthetic comment -- @@ -139,12 +143,12 @@

if (DBG) log("setNewPhone");
if (mCurrentPhone != phone) {
            if (mUiccCard != null) {
// Refresh card if phone changed
// TODO: Remove once card is simplified
if (DBG) log("Disposing card since phone object changed");
                mUiccCard.dispose();
                mUiccCard = null;
}
sendMessage(obtainMessage(EVENT_ICC_STATUS_CHANGED));
mCurrentPhone = phone;







