/*Telephony: Remove CdmaLteUicc objects

Pass IccCard object to GsmMmiCode
Create IccCardProxy
Make IccCard an interface and pass instance of IccCardProxy to
external applications (PhoneApp). IccCardProxy will use internal UiccCard
to map Icc requests to current active application on UiccCard to
maintain backwards compatibility for external applications

Change-Id:I60216887b14140bdf833a8ed579ba16cad932bdc*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CsimFileHandler.java b/src/java/com/android/internal/telephony/CsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..7006051

//Synthetic comment -- @@ -0,0 +1,72 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 648b73e..9d8c201 100644

//Synthetic comment -- @@ -16,277 +16,39 @@

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
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.sip.SipPhone;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cdma.CDMALTEPhone;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.cdma.CdmaLteUiccFileHandler;
import com.android.internal.telephony.cdma.CdmaLteUiccRecords;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.RuimFileHandler;
import com.android.internal.telephony.cdma.RuimRecords;

import com.android.internal.R;

/**
* {@hide}
*/
public class IccCard {
    protected String mLogTag;
    protected boolean mDbg;

    protected IccCardStatus mIccCardStatus = null;
    protected IccCardConstants.State mState = null;
    private final Object mStateMonitor = new Object();

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
    protected RegistrantList mRuimReadyRegistrants = new RegistrantList();

    private boolean mDesiredPinLocked;
    private boolean mDesiredFdnEnabled;
    private boolean mIccPinLocked = true; // Default to locked
    private boolean mIccFdnEnabled = false; // Default to disabled.
                                            // Will be updated when SIM_READY.

    /* Parameter is3gpp's values to be passed to constructor */
    public final static boolean CARD_IS_3GPP = true;
    public final static boolean CARD_IS_NOT_3GPP = false;

    protected static final int EVENT_ICC_LOCKED = 1;
    private static final int EVENT_GET_ICC_STATUS_DONE = 2;
    protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
    protected static final int EVENT_ICC_READY = 6;
    private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 7;
    private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 8;
    private static final int EVENT_CHANGE_ICC_PASSWORD_DONE = 9;
    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 10;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 11;
    private static final int EVENT_ICC_STATUS_CHANGED = 12;
    private static final int EVENT_CARD_REMOVED = 13;
    private static final int EVENT_CARD_ADDED = 14;
    protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 15;
    protected static final int EVENT_RADIO_ON = 16;

    public IccCardConstants.State getState() {
        if (mState == null) {
            switch(mPhone.mCM.getRadioState()) {
                /* This switch block must not return anything in
                 * IccCardConstants.State.isLocked() or IccCardConstants.State.ABSENT.
                 * If it does, handleSimStatus() may break
                 */
                case RADIO_OFF:
                case RADIO_UNAVAILABLE:
                    return IccCardConstants.State.UNKNOWN;
                default:
                    if (!is3gpp && !isSubscriptionFromIccCard) {
                        // CDMA can get subscription from NV. In that case,
                        // subscription is ready as soon as Radio is ON.
                        return IccCardConstants.State.READY;
                    }
            }
        } else {
            return mState;
        }

        return IccCardConstants.State.UNKNOWN;
    }

    public IccCard(PhoneBase phone, IccCardStatus ics, String logTag, boolean dbg) {
        mLogTag = logTag;
        mDbg = dbg;
        if (mDbg) log("Creating");
        update(phone, ics);
        mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(mPhone.getContext(),
                mPhone.mCM, mHandler, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
        mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
    }

    public void dispose() {
        if (mDbg) log("Disposing card type " + (is3gpp ? "3gpp" : "3gpp2"));
        mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
        mPhone.mCM.unregisterForOn(mHandler);
        mCatService.dispose();
        mCdmaSSM.dispose(mHandler);
        mIccRecords.dispose();
        mIccFileHandler.dispose();
    }

    public void update(PhoneBase phone, IccCardStatus ics) {
        if (phone != mPhone) {
            PhoneBase oldPhone = mPhone;
            mPhone = phone;
            log("Update");
            if (phone instanceof GSMPhone) {
                is3gpp = true;
            } else if (phone instanceof CDMALTEPhone){
                is3gpp = true;
            } else if (phone instanceof CDMAPhone){
                is3gpp = false;
            } else if (phone instanceof SipPhone){
                is3gpp = true;
            } else {
                throw new RuntimeException("Update: Unhandled phone type. Critical error!" +
                        phone.getPhoneName());
            }


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
            mCatService = CatService.getInstance(mPhone.mCM, mIccRecords, mPhone.mContext,
                    mIccFileHandler, this);
        }
        mHandler.sendMessage(mHandler.obtainMessage(EVENT_GET_ICC_STATUS_DONE, ics));
    }

    protected void finalize() {
        if (mDbg) log("[IccCard] Finalized card type " + (is3gpp ? "3gpp" : "3gpp2"));
    }

    public IccRecords getIccRecords() {
        return mIccRecords;
    }

    public IccFileHandler getIccFileHandler() {
        return mIccFileHandler;
    }

/**
* Notifies handler of any transition into IccCardConstants.State.ABSENT
*/
    public void registerForAbsent(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mAbsentRegistrants.add(r);

        if (getState() == IccCardConstants.State.ABSENT) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForAbsent(Handler h) {
        mAbsentRegistrants.remove(h);
    }

/**
* Notifies handler of any transition into IccCardConstants.State.NETWORK_LOCKED
*/
    public void registerForNetworkLocked(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        mNetworkLockedRegistrants.add(r);

        if (getState() == IccCardConstants.State.NETWORK_LOCKED) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForNetworkLocked(Handler h) {
        mNetworkLockedRegistrants.remove(h);
    }

/**
* Notifies handler of any transition into IccCardConstants.State.isPinLocked()
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

            if (getState() == IccCardConstants.State.READY) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForReady(Handler h) {
        synchronized (mStateMonitor) {
            mReadyRegistrants.remove(h);
        }
    }

    public IccCardConstants.State getRuimState() {
        if(mIccCardStatus != null) {
            return getAppState(mIccCardStatus.getCdmaSubscriptionAppIndex());
        } else {
            return IccCardConstants.State.UNKNOWN;
        }
    }

    public void registerForRuimReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);

        synchronized (mStateMonitor) {
            mRuimReadyRegistrants.add(r);

            if (getState() == IccCardConstants.State.READY &&
                    getRuimState() == IccCardConstants.State.READY ) {
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    public void unregisterForRuimReady(Handler h) {
        synchronized (mStateMonitor) {
            mRuimReadyRegistrants.remove(h);
        }
    }

/**
* Supply the ICC PIN to the ICC
//Synthetic comment -- @@ -309,25 +71,11 @@
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
        mPhone.mCM.supplyNetworkDepersonalization(pin, onComplete);
    }

/**
* Check whether ICC pin lock is enabled
//Synthetic comment -- @@ -336,9 +84,7 @@
* @return true for ICC locked enabled
*         false for ICC locked disabled
*/
    public boolean getIccLockEnabled() {
        return mIccPinLocked;
     }

/**
* Check whether ICC fdn (fixed dialing number) is enabled
//Synthetic comment -- @@ -347,9 +93,7 @@
* @return true for ICC fdn enabled
*         false for ICC fdn disabled
*/
     public boolean getIccFdnEnabled() {
        return mIccFdnEnabled;
     }

/**
* Set the ICC pin lock enabled or disabled
//Synthetic comment -- @@ -363,18 +107,7 @@
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
//Synthetic comment -- @@ -388,19 +121,7 @@
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
//Synthetic comment -- @@ -414,11 +135,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void changeIccLockPassword(String oldPassword, String newPassword,
             Message onComplete) {
         mPhone.mCM.changeIccPin(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

     }

/**
* Change the ICC password used in ICC fdn enable
//Synthetic comment -- @@ -432,12 +149,7 @@
*        ((AsyncResult)onComplete.obj).exception != null on fail
*/
public void changeIccFdnPassword(String oldPassword, String newPassword,
             Message onComplete) {
         mPhone.mCM.changeIccPin2(oldPassword, newPassword,
                 mHandler.obtainMessage(EVENT_CHANGE_ICC_PASSWORD_DONE, onComplete));

     }


/**
* Returns service provider name stored in ICC card.
//Synthetic comment -- @@ -455,490 +167,12 @@
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
        handleIccCardStatus(ics);
    }

    private void handleIccCardStatus(IccCardStatus newCardStatus) {
        boolean transitionedIntoPinLocked;
        boolean transitionedIntoAbsent;
        boolean transitionedIntoNetworkLocked;
        boolean transitionedIntoPermBlocked;
        boolean isIccCardRemoved;
        boolean isIccCardAdded;

        IccCardConstants.State oldState, newState;
        IccCardConstants.State oldRuimState = getRuimState();

        oldState = mState;
        mIccCardStatus = newCardStatus;
        newState = getIccCardState();

        synchronized (mStateMonitor) {
            mState = newState;
            updateStateProperty();
            if (oldState != IccCardConstants.State.READY &&
                    newState == IccCardConstants.State.READY) {
                mHandler.sendMessage(mHandler.obtainMessage(EVENT_ICC_READY));
                mReadyRegistrants.notifyRegistrants();
            } else if (newState.isPinLocked()) {
                mHandler.sendMessage(mHandler.obtainMessage(EVENT_ICC_LOCKED));
            }
            if (oldRuimState != IccCardConstants.State.READY &&
                    getRuimState() == IccCardConstants.State.READY) {
                mRuimReadyRegistrants.notifyRegistrants();
            }
        }

        transitionedIntoPinLocked = (
                 (oldState != IccCardConstants.State.PIN_REQUIRED &&
                     newState == IccCardConstants.State.PIN_REQUIRED)
              || (oldState != IccCardConstants.State.PUK_REQUIRED &&
                      newState == IccCardConstants.State.PUK_REQUIRED));
        transitionedIntoAbsent = (oldState != IccCardConstants.State.ABSENT &&
                newState == IccCardConstants.State.ABSENT);
        transitionedIntoNetworkLocked = (oldState != IccCardConstants.State.NETWORK_LOCKED
                && newState == IccCardConstants.State.NETWORK_LOCKED);
        transitionedIntoPermBlocked = (oldState != IccCardConstants.State.PERM_DISABLED
                && newState == IccCardConstants.State.PERM_DISABLED);
        isIccCardRemoved = (oldState != null && oldState.iccCardExist() &&
                    newState == IccCardConstants.State.ABSENT);
        isIccCardAdded = (oldState == IccCardConstants.State.ABSENT &&
                        newState != null && newState.iccCardExist());

        if (transitionedIntoPinLocked) {
            if (mDbg) log("Notify SIM pin or puk locked.");
            mPinLockedRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOCKED,
                    (newState == IccCardConstants.State.PIN_REQUIRED) ?
                            IccCardConstants.INTENT_VALUE_LOCKED_ON_PIN :
                                IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK);
        } else if (transitionedIntoAbsent) {
            if (mDbg) log("Notify SIM missing.");
            mAbsentRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT, null);
        } else if (transitionedIntoNetworkLocked) {
            if (mDbg) log("Notify SIM network locked.");
            mNetworkLockedRegistrants.notifyRegistrants();
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_LOCKED,
                    IccCardConstants.INTENT_VALUE_LOCKED_NETWORK);
        } else if (transitionedIntoPermBlocked) {
            if (mDbg) log("Notify SIM permanently disabled.");
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_ABSENT,
                    IccCardConstants.INTENT_VALUE_ABSENT_ON_PERM_DISABLED);
        }

        if (isIccCardRemoved) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_REMOVED, null));
        } else if (isIccCardAdded) {
            mHandler.sendMessage(mHandler.obtainMessage(EVENT_CARD_ADDED, null));
        }

        // Call onReady Record(s) on the IccCard becomes ready (not NV)
        if (oldState != IccCardConstants.State.READY && newState == IccCardConstants.State.READY &&
                (is3gpp || isSubscriptionFromIccCard)) {
            mIccFileHandler.setAid(getAid());
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_READY, null);
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

    public void broadcastIccStateChangedIntent(String value, String reason) {
        Intent intent = new Intent(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra(PhoneConstants.PHONE_NAME_KEY, mPhone.getPhoneName());
        intent.putExtra(IccCardConstants.INTENT_KEY_ICC_STATE, value);
        intent.putExtra(IccCardConstants.INTENT_KEY_LOCKED_REASON, reason);
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

            if (!mPhone.mIsTheCurrentActivePhone) {
                Log.e(mLogTag, "Received message " + msg + "[" + msg.what
                        + "] while being destroyed. Ignoring.");
                return;
            }

            switch (msg.what) {
                case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                    mState = null;
                    updateStateProperty();
                    broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_NOT_READY,
                            null);
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
                    if(isSubscriptionFromIccCard) {
                        mPhone.mCM.queryFacilityLock (
                                CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX,
                                obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
                        mPhone.mCM.queryFacilityLock (
                                CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX,
                                obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
                    }
                    break;
                case EVENT_ICC_LOCKED:
                    mPhone.mCM.queryFacilityLock (
                             CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX,
                             obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
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

            Log.d(mLogTag, "Received Cdma subscription source: " + newSubscriptionSource);

            boolean isNewSubFromRuim =
                (newSubscriptionSource == CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM);

            if (isNewSubFromRuim != isSubscriptionFromIccCard) {
                isSubscriptionFromIccCard = isNewSubFromRuim;
                // Parse the Stored IccCardStatus Message to set mState correctly.
                handleIccCardStatus(mIccCardStatus);
            }
        }
    }

    public IccCardConstants.State getIccCardState() {
        if(!is3gpp && !isSubscriptionFromIccCard) {
            // CDMA can get subscription from NV. In that case,
            // subscription is ready as soon as Radio is ON.
            return IccCardConstants.State.READY;
        }

        if (mIccCardStatus == null) {
            Log.e(mLogTag, "[IccCard] IccCardStatus is null");
            return IccCardConstants.State.ABSENT;
        }

        // this is common for all radio technologies
        if (!mIccCardStatus.getCardState().isCardPresent()) {
            return IccCardConstants.State.ABSENT;
        }

        RadioState currentRadioState = mPhone.mCM.getRadioState();
        // check radio technology
        if( currentRadioState == RadioState.RADIO_OFF         ||
            currentRadioState == RadioState.RADIO_UNAVAILABLE) {
            return IccCardConstants.State.NOT_READY;
        }

        if( currentRadioState == RadioState.RADIO_ON ) {
            IccCardConstants.State csimState =
                getAppState(mIccCardStatus.getCdmaSubscriptionAppIndex());
            IccCardConstants.State usimState =
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

        return IccCardConstants.State.ABSENT;
    }

    private IccCardConstants.State getAppState(int appIndex) {
        IccCardApplication app;
        if (appIndex >= 0 && appIndex < IccCardStatus.CARD_MAX_APPS) {
            app = mIccCardStatus.getApplication(appIndex);
        } else {
            Log.e(mLogTag, "[IccCard] Invalid Subscription Application index:" + appIndex);
            return IccCardConstants.State.ABSENT;
        }

        if (app == null) {
            Log.e(mLogTag, "[IccCard] Subscription Application in not present");
            return IccCardConstants.State.ABSENT;
        }

        // check if PIN required
        if (app.pin1.isPermBlocked()) {
            return IccCardConstants.State.PERM_DISABLED;
        }
        if (app.app_state.isPinRequired()) {
            return IccCardConstants.State.PIN_REQUIRED;
        }
        if (app.app_state.isPukRequired()) {
            return IccCardConstants.State.PUK_REQUIRED;
        }
        if (app.app_state.isSubscriptionPersoEnabled()) {
            return IccCardConstants.State.NETWORK_LOCKED;
        }
        if (app.app_state.isAppReady()) {
            return IccCardConstants.State.READY;
        }
        if (app.app_state.isAppNotReady()) {
            return IccCardConstants.State.NOT_READY;
        }
        return IccCardConstants.State.NOT_READY;
    }

    private IccCardConstants.State getConsolidatedState(IccCardConstants.State left,
            IccCardConstants.State right, IccCardConstants.State preferredState) {
        // Check if either is absent.
        if (right == IccCardConstants.State.ABSENT) return left;
        if (left == IccCardConstants.State.ABSENT) return right;

        // Only if both are ready, return ready
        if ((left == IccCardConstants.State.READY) && (right == IccCardConstants.State.READY)) {
            return IccCardConstants.State.READY;
        }

        // Case one is ready, but the other is not.
        if (((right == IccCardConstants.State.NOT_READY) &&
                (left == IccCardConstants.State.READY)) ||
            ((left == IccCardConstants.State.NOT_READY) &&
                    (right == IccCardConstants.State.READY))) {
            return IccCardConstants.State.NOT_READY;
        }

        // At this point, the other state is assumed to be one of locked state
        if (right == IccCardConstants.State.NOT_READY) return left;
        if (left == IccCardConstants.State.NOT_READY) return right;

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
        Log.d(mLogTag, "[IccCard] " + msg);
    }

    private void loge(String msg) {
        Log.e(mLogTag, "[IccCard] " + msg);
    }

    protected int getCurrentApplicationIndex() {
        if (is3gpp) {
            return mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
        } else {
            return mIccCardStatus.getCdmaSubscriptionAppIndex();
        }
    }

    public String getAid() {
        String aid = "";
        if (mIccCardStatus == null) {
            return aid;
        }

        int appIndex = getCurrentApplicationIndex();

        if (appIndex >= 0 && appIndex < IccCardStatus.CARD_MAX_APPS) {
            IccCardApplication app = mIccCardStatus.getApplication(appIndex);
            if (app != null) {
                aid = app.aid;
            } else {
                Log.e(mLogTag, "[IccCard] getAid: no current application index=" + appIndex);
            }
        } else {
            Log.e(mLogTag, "[IccCard] getAid: Invalid Subscription Application index=" + appIndex);
        }

        return aid;
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardApplication.java b/src/java/com/android/internal/telephony/IccCardApplicationStatus.java
similarity index 99%
rename from src/java/com/android/internal/telephony/IccCardApplication.java
rename to src/java/com/android/internal/telephony/IccCardApplicationStatus.java
//Synthetic comment -- index abb740e..f3f22ea 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
*
* {@hide}
*/
public class IccCardApplication {
public enum AppType{
APPTYPE_UNKNOWN,
APPTYPE_SIM,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
new file mode 100644
//Synthetic comment -- index 0000000..4259798

//Synthetic comment -- @@ -0,0 +1,611 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardStatus.java b/src/java/com/android/internal/telephony/IccCardStatus.java
//Synthetic comment -- index a3bdd76..b4a5e68 100644

//Synthetic comment -- @@ -57,19 +57,13 @@
}
}

    private CardState  mCardState;
    private PinState   mUniversalPinState;
    private int        mGsmUmtsSubscriptionAppIndex;
    private int        mCdmaSubscriptionAppIndex;
    private int        mImsSubscriptionAppIndex;
    private int        mNumApplications;

    private ArrayList<IccCardApplication> mApplications =
            new ArrayList<IccCardApplication>(CARD_MAX_APPS);

    public CardState getCardState() {
        return mCardState;
    }

public void setCardState(int state) {
switch(state) {
//Synthetic comment -- @@ -87,10 +81,6 @@
}
}

    public PinState getUniversalPinState() {
        return mUniversalPinState;
    }

public void setUniversalPinState(int state) {
switch(state) {
case 0:
//Synthetic comment -- @@ -116,69 +106,34 @@
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

    public int getImsSubscriptionAppIndex() {
        return mImsSubscriptionAppIndex;
    }

    public void setImsSubscriptionAppIndex(int imsSubscriptionAppIndex) {
        mImsSubscriptionAppIndex = imsSubscriptionAppIndex;
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

@Override
public String toString() {
        IccCardApplication app;

StringBuilder sb = new StringBuilder();
sb.append("IccCardState {").append(mCardState).append(",")
.append(mUniversalPinState)
        .append(",num_apps=").append(mNumApplications)
.append(",gsm_id=").append(mGsmUmtsSubscriptionAppIndex);
if (mGsmUmtsSubscriptionAppIndex >=0
&& mGsmUmtsSubscriptionAppIndex <CARD_MAX_APPS) {
            app = getApplication(mGsmUmtsSubscriptionAppIndex);
sb.append(app == null ? "null" : app);
}

sb.append(",cmda_id=").append(mCdmaSubscriptionAppIndex);
if (mCdmaSubscriptionAppIndex >=0
&& mCdmaSubscriptionAppIndex <CARD_MAX_APPS) {
            app = getApplication(mCdmaSubscriptionAppIndex);
sb.append(app == null ? "null" : app);
}

        sb.append(",ism_id=").append(mImsSubscriptionAppIndex);

sb.append("}");









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccConstants.java b/src/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 1ba6dfe..847c883 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;
//Synthetic comment -- @@ -85,6 +86,6 @@
static final String DF_GSM = "7F20";
static final String DF_CDMA = "7F25";

    //ISIM access
    static final String DF_ADFISIM = "7FFF";
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 70d8f7a..98ab17b 100644

//Synthetic comment -- @@ -91,8 +91,8 @@

// member variables
protected final CommandsInterface mCi;
    protected final IccCard mParentCard;
    protected String mAid;

static class LoadLinearFixedContext {

//Synthetic comment -- @@ -122,8 +122,8 @@
/**
* Default constructor
*/
    protected IccFileHandler(IccCard card, String aid, CommandsInterface ci) {
        mParentCard = card;
mAid = aid;
mCi = ci;
}
//Synthetic comment -- @@ -225,6 +225,24 @@
}

/**
* Load a SIM Transparent EF-IMG. Used right after loadEFImgLinearFixed to
* retrive STK's icon data.
*
//Synthetic comment -- @@ -534,6 +552,9 @@
case EF_ICCID:
case EF_PL:
return MF_SIM;
case EF_IMG:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
//Synthetic comment -- @@ -544,8 +565,5 @@
protected abstract void logd(String s);

protected abstract void loge(String s);
    protected void setAid(String aid) {
        mAid = aid;
    }

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 0e5f2da..10d7515 100644

//Synthetic comment -- @@ -291,7 +291,7 @@
private int updateEfForIccType(int efid) {
// Check if we are trying to read ADN records
if (efid == IccConstants.EF_ADN) {
            if (phone.getIccCard().isApplicationOnIcc(IccCardApplication.AppType.APPTYPE_USIM)) {
return IccConstants.EF_PBR;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index 3c90647..67bdf9e 100644

//Synthetic comment -- @@ -39,9 +39,10 @@
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
    protected IccCard mParentCard;

protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
protected RegistrantList mRecordsEventsRegistrants = new RegistrantList();
protected RegistrantList mNewSmsRegistrants = new RegistrantList();
protected RegistrantList mNetworkSelectionModeAutomaticRegistrants = new RegistrantList();
//Synthetic comment -- @@ -63,6 +64,7 @@
protected String newVoiceMailTag = null;
protected boolean isVoiceMailFixed = false;
protected int countVoiceMessages = 0;

protected int mncLength = UNINITIALIZED;
protected int mailboxIndex = 0; // 0 is no mailbox dailing number associated
//Synthetic comment -- @@ -104,11 +106,11 @@
}

// ***** Constructor
    public IccRecords(IccCard card, Context c, CommandsInterface ci) {
mContext = c;
mCi = ci;
        mFh = card.getIccFileHandler();
        mParentCard = card;
}

/**
//Synthetic comment -- @@ -116,13 +118,12 @@
*/
public void dispose() {
mDestroyed.set(true);
        mParentCard = null;
mFh = null;
mCi = null;
mContext = null;
}

    protected abstract void onRadioOffOrNotAvailable();
public abstract void onReady();

//***** Public Methods
//Synthetic comment -- @@ -146,6 +147,22 @@
recordsLoadedRegistrants.remove(h);
}

public void registerForRecordsEvents(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
mRecordsEventsRegistrants.add(r);
//Synthetic comment -- @@ -182,6 +199,15 @@
return null;
}

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 7c2f2e0..9edef4e 100755

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.util.Log;

import com.android.internal.R;
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.test.SimulatedRadioControl;
//Synthetic comment -- @@ -130,9 +131,10 @@
boolean mIsVoiceCapable = true;
protected UiccController mUiccController = null;
public AtomicReference<IccRecords> mIccRecords = new AtomicReference<IccRecords>();
    protected AtomicReference<IccCard> mIccCard = new AtomicReference<IccCard>();
public SmsStorageMonitor mSmsStorageMonitor;
public SmsUsageMonitor mSmsUsageMonitor;
public SMSDispatcher mSMS;

/**
//Synthetic comment -- @@ -254,7 +256,7 @@
// Initialize device storage and outgoing SMS usage monitors for SMSDispatchers.
mSmsStorageMonitor = new SmsStorageMonitor(this);
mSmsUsageMonitor = new SmsUsageMonitor(context);
        mUiccController = UiccController.getInstance(this);
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
}

//Synthetic comment -- @@ -276,7 +278,7 @@
mSmsUsageMonitor = null;
mSMS = null;
mIccRecords.set(null);
        mIccCard.set(null);
mDataConnectionTracker = null;
mUiccController = null;
}
//Synthetic comment -- @@ -653,9 +655,9 @@
* Retrieves the IccFileHandler of the Phone instance
*/
public IccFileHandler getIccFileHandler(){
        IccCard iccCard = mIccCard.get();
        if (iccCard == null) return null;
        return iccCard.getIccFileHandler();
}

/*
//Synthetic comment -- @@ -681,7 +683,18 @@

@Override
public IccCard getIccCard() {
        return mIccCard.get();
}

@Override
//Synthetic comment -- @@ -1186,7 +1199,7 @@
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
pw.println(" mIccRecords=" + mIccRecords.get());
        pw.println(" mIccCard=" + mIccCard.get());
pw.println(" mSmsStorageMonitor=" + mSmsStorageMonitor);
pw.println(" mSmsUsageMonitor=" + mSmsUsageMonitor);
pw.println(" mSMS=" + mSMS);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneFactory.java b/src/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 2c85dc6..2600c79 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.sip.SipPhone;
import com.android.internal.telephony.sip.SipPhoneFactory;

/**
* {@hide}
//Synthetic comment -- @@ -138,6 +139,9 @@
//reads the system properties and makes commandsinterface
sCommandsInterface = new RIL(context, networkMode, cdmaSubscription);

int phoneType = TelephonyManager.getPhoneType(networkMode);
if (phoneType == PhoneConstants.PHONE_TYPE_GSM) {
Log.i(LOG_TAG, "Creating GSMPhone");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77135d4..3387840 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
private IccSmsInterfaceManagerProxy mIccSmsInterfaceManagerProxy;
private IccPhoneBookInterfaceManagerProxy mIccPhoneBookInterfaceManagerProxy;
private PhoneSubInfoProxy mPhoneSubInfoProxy;

private boolean mResetModemOnRadioTechnologyChange = false;

//Synthetic comment -- @@ -61,7 +62,7 @@
private static final String LOG_TAG = "PHONE";

//***** Class Methods
    public PhoneProxy(Phone phone) {
mActivePhone = phone;
mResetModemOnRadioTechnologyChange = SystemProperties.getBoolean(
TelephonyProperties.PROPERTY_RESET_ON_RADIO_TECH_CHANGE, false);
//Synthetic comment -- @@ -76,6 +77,13 @@
mCommandsInterface.registerForOn(this, EVENT_RADIO_ON, null);
mCommandsInterface.registerForVoiceRadioTechChanged(
this, EVENT_VOICE_RADIO_TECH_CHANGED, null);
}

@Override
//Synthetic comment -- @@ -197,6 +205,7 @@
mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());

mCommandsInterface = ((PhoneBase)mActivePhone).mCM;

// Send an Intent to the PhoneApp that we had a radio technology change
Intent intent = new Intent(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
//Synthetic comment -- @@ -457,11 +466,11 @@
}

public boolean getIccRecordsLoaded() {
        return mActivePhone.getIccRecordsLoaded();
}

public IccCard getIccCard() {
        return mActivePhone.getIccCard();
}

public void acceptCall() throws CallStateException {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c359652..2dcf9fa 100755

//Synthetic comment -- @@ -51,6 +51,7 @@

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.IccRefreshResponse;
//Synthetic comment -- @@ -2986,24 +2987,23 @@

private Object
responseIccCardStatus(Parcel p) {
        IccCardApplication ca;

IccCardStatus status = new IccCardStatus();
status.setCardState(p.readInt());
status.setUniversalPinState(p.readInt());
        status.setGsmUmtsSubscriptionAppIndex(p.readInt());
        status.setCdmaSubscriptionAppIndex(p.readInt());
        status.setImsSubscriptionAppIndex(p.readInt());
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
//Synthetic comment -- @@ -3012,7 +3012,7 @@
ca.pin1_replaced  = p.readInt();
ca.pin1           = ca.PinStateFromRILInt(p.readInt());
ca.pin2           = ca.PinStateFromRILInt(p.readInt());
            status.addApplication(ca);
}
return status;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 1628a3d..8053777 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
//Synthetic comment -- @@ -38,7 +39,7 @@

protected CommandsInterface cm;
protected UiccController mUiccController = null;
    protected IccCard mIccCard = null;
protected IccRecords mIccRecords = null;

public ServiceState ss;
//Synthetic comment -- @@ -174,7 +175,7 @@
protected static final String REGISTRATION_DENIED_GEN  = "General";
protected static final String REGISTRATION_DENIED_AUTH = "Authentication Failure";

    public ServiceStateTracker(PhoneBase p, CommandsInterface ci) {
cm = ci;
mUiccController = UiccController.getInstance();
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCard.java b/src/java/com/android/internal/telephony/UiccCard.java
new file mode 100644
//Synthetic comment -- index 0000000..0724020

//Synthetic comment -- @@ -0,0 +1,322 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
new file mode 100644
//Synthetic comment -- index 0000000..8de1dfc7

//Synthetic comment -- @@ -0,0 +1,543 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..ae4c05e

//Synthetic comment -- @@ -0,0 +1,88 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 2b37072..4934fd7 100644

//Synthetic comment -- @@ -28,9 +28,10 @@

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;


import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -64,6 +65,7 @@

// Class members
private static IccRecords mIccRecords;

// Service members.
// Protects singleton instance lazy initialization.
//Synthetic comment -- @@ -101,9 +103,9 @@
static final String STK_DEFAULT = "Defualt Message";

/* Intentionally private for singleton */
    private CatService(CommandsInterface ci, IccRecords ir, Context context,
            IccFileHandler fh, IccCard ic) {
        if (ci == null || ir == null || context == null || fh == null
|| ic == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
//Synthetic comment -- @@ -122,9 +124,10 @@
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

mIccRecords = ir;

// Register for SIM ready event.
        ic.registerForReady(this, MSG_ID_SIM_READY, null);
mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

// Check if STK application is availalbe
//Synthetic comment -- @@ -553,24 +556,46 @@
* @param ic Icc card
* @return The only Service object in the system
*/
    public static CatService getInstance(CommandsInterface ci, IccRecords ir,
            Context context, IccFileHandler fh, IccCard ic) {
synchronized (sInstanceLock) {
if (sInstance == null) {
                if (ci == null || ir == null || context == null || fh == null
|| ic == null) {
return null;
}
HandlerThread thread = new HandlerThread("Cat Telephony service");
thread.start();
                sInstance = new CatService(ci, ir, context, fh, ic);
CatLog.d(sInstance, "NEW sInstance");
} else if ((ir != null) && (mIccRecords != ir)) {
                CatLog.d(sInstance, "Reinitialize the Service with SIMRecords");
mIccRecords = ir;

// re-Register for SIM ready event.
mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
CatLog.d(sInstance, "sr changed reinitialize and return current sInstance");
} else {
CatLog.d(sInstance, "Return current sInstance");
//Synthetic comment -- @@ -585,7 +610,7 @@
* @return The only Service object in the system
*/
public static AppInterface getInstance() {
        return getInstance(null, null, null, null, null);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index f125484..48f6359 100644

//Synthetic comment -- @@ -28,17 +28,18 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.uicc.UiccController;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -52,6 +53,12 @@
/** Secondary SMSDispatcher for 3GPP format messages. */
SMSDispatcher m3gppSMS;

/**
* Small container class used to hold information relevant to
* the carrier selection process. operatorNumeric can be ""
//Synthetic comment -- @@ -200,12 +207,11 @@

@Override
public boolean updateCurrentCarrierInProvider() {
        IccRecords r = mIccRecords.get();
        if (r != null) {
try {
Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
ContentValues map = new ContentValues();
                String operatorNumeric = r.getOperatorNumeric();
map.put(Telephony.Carriers.NUMERIC, operatorNumeric);
if (DBG) log("updateCurrentCarrierInProvider from UICC: numeric=" +
operatorNumeric);
//Synthetic comment -- @@ -223,8 +229,7 @@
// return IMSI from USIM as subscriber ID.
@Override
public String getSubscriberId() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getIMSI() : "";
}

@Override
//Synthetic comment -- @@ -239,14 +244,12 @@

@Override
public IsimRecords getIsimRecords() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getIsimRecords() : null;
}

@Override
public String getMsisdn() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getMsisdnNumber() : null;
}

@Override
//Synthetic comment -- @@ -260,23 +263,40 @@
}

@Override
    protected void registerForRuimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
return;
}
        r.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
        super.registerForRuimRecordEvents();
    }

    @Override
    protected void unregisterForRuimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
            return;
}
        r.unregisterForNewSms(this);
        super.unregisterForRuimRecordEvents();
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 227b406..a32ef93 100755

//Synthetic comment -- @@ -64,7 +64,10 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.uicc.UiccController;

//Synthetic comment -- @@ -802,6 +805,10 @@
return null;
}

/**
* Notify any interested party of a Phone state change  {@link PhoneConstants.State}
*/
//Synthetic comment -- @@ -1066,29 +1073,26 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();

        IccCard c = mIccCard.get();
        if (c != newIccCard) {
            if (c != null) {
log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForRuimRecordEvents();
                    if (mRuimPhoneBookInterfaceManager != null) {
                        mRuimPhoneBookInterfaceManager.updateIccRecords(null);
                    }
}
mIccRecords.set(null);
                mIccCard.set(null);
}
            if (newIccCard != null) {
                log("New card found");
                mIccCard.set(newIccCard);
                mIccRecords.set(newIccCard.getIccRecords());
registerForRuimRecordEvents();
                if (mRuimPhoneBookInterfaceManager != null) {
                    mRuimPhoneBookInterfaceManager.updateIccRecords(mIccRecords.get());
                }
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index c75290e..c05a3fe 100755

//Synthetic comment -- @@ -422,7 +422,8 @@
return DisconnectCause.OUT_OF_SERVICE;
} else if (phone.mCdmaSubscriptionSource ==
CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM
                           && phone.getIccCard().getState() != IccCardConstants.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode==CallFailCause.NORMAL_CLEARING) {
return DisconnectCause.NORMAL;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 51b4a4c..01344c8 100644

//Synthetic comment -- @@ -42,13 +42,13 @@
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.util.AsyncChannel;
import com.android.internal.telephony.RILConstants;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -1015,11 +1015,7 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();
        IccRecords newIccRecords = null;
        if (newIccCard != null) {
            newIccRecords = newIccCard.getIccRecords();
        }

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -1029,7 +1025,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New card found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index c19cc5e..cd0ba72 100755

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.util.Log;
import android.util.EventLog;

import com.android.internal.telephony.gsm.GsmDataConnectionTracker;
import com.android.internal.telephony.IccCardConstants;

//Synthetic comment -- @@ -66,12 +67,12 @@
handlePollStateResult(msg.what, ar);
break;
case EVENT_RUIM_RECORDS_LOADED:
            CdmaLteUiccRecords sim = (CdmaLteUiccRecords)mIccRecords;
            if ((sim != null) && sim.isProvisioned()) {
                mMdn = sim.getMdn();
                mMin = sim.getMin();
                parseSidNid(sim.getSid(), sim.getNid());
                mPrlVersion = sim.getPrlVersion();;
mIsMinInfoReady = true;
updateOtaspState();
}
//Synthetic comment -- @@ -353,12 +354,12 @@
ss.setOperatorAlphaLong(eriText);
}

            if (mIccCard != null && mIccCard.getState() == IccCardConstants.State.READY &&
mIccRecords != null) {
// SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
// one configfured in SIM, use operator name  from CSIM record.
boolean showSpn =
                    ((CdmaLteUiccRecords)mIccRecords).getCsimSpnDisplayCondition();
int iconIndex = ss.getCdmaEriIconIndex();

if (showSpn && (iconIndex == EriInfo.ROAMING_INDICATOR_OFF) &&








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java
deleted file mode 100644
//Synthetic comment -- index 93a6290..0000000

//Synthetic comment -- @@ -1,79 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import android.os.Message;

/**
 * {@hide}
 */
public final class CdmaLteUiccFileHandler extends IccFileHandler {
    static final String LOG_TAG = "CDMA";

    public CdmaLteUiccFileHandler(IccCard card, String aid, CommandsInterface ci) {
        super(card, aid, ci);
    }

    protected String getEFPath(int efid) {
        switch(efid) {
        case EF_CSIM_SPN:
        case EF_CSIM_LI:
        case EF_CSIM_MDN:
        case EF_CSIM_IMSIM:
        case EF_CSIM_CDMAHOME:
        case EF_CSIM_EPRL:
            return MF_SIM + DF_CDMA;
        case EF_AD:
            return MF_SIM + DF_GSM;
        case EF_IMPI:
        case EF_DOMAIN:
        case EF_IMPU:
            return MF_SIM + DF_ADFISIM;
        }
        return getCommonIccEFPath(efid);
    }

    @Override
    public void loadEFTransparent(int fileid, Message onLoaded) {
        if (fileid == EF_CSIM_EPRL) {
            // Entire PRL could be huge. We are only interested in
            // the first 4 bytes of the record.
            mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
                            0, 0, 4, null, null, mAid,
                            obtainMessage(EVENT_READ_BINARY_DONE,
                                          fileid, 0, onLoaded));
        } else {
            super.loadEFTransparent(fileid, onLoaded);
        }
    }


    protected void logd(String msg) {
        Log.d(LOG_TAG, "[CdmaLteUiccFileHandler] " + msg);
    }

    protected void loge(String msg) {
        Log.e(LOG_TAG, "[CdmaLteUiccFileHandler] " + msg);
    }

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java
deleted file mode 100755
//Synthetic comment -- index 97f973f..0000000

//Synthetic comment -- @@ -1,452 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.content.Context;
import android.os.AsyncResult;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccCardApplication.AppType;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.ims.IsimUiccRecords;

import java.util.ArrayList;
import java.util.Locale;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_TEST_CSIM;

/**
 * {@hide}
 */
public final class CdmaLteUiccRecords extends SIMRecords {
    // From CSIM application
    private byte[] mEFpl = null;
    private byte[] mEFli = null;
    boolean mCsimSpnDisplayCondition = false;
    private String mMdn;
    private String mMin;
    private String mPrlVersion;
    private String mHomeSystemId;
    private String mHomeNetworkId;

    private final IsimUiccRecords mIsimUiccRecords = new IsimUiccRecords();

    public CdmaLteUiccRecords(IccCard card, Context c, CommandsInterface ci) {
        super(card, c, ci);
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
            int len = ((data.length - 3) < 32) ? (data.length - 3) : 32;
            System.arraycopy(data, 3, spnData, 0, len);

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

    @Override
    protected void onRecordLoaded() {
        // One record loaded successfully or failed, In either case
        // we need to update the recordsToLoad count
        recordsToLoad -= 1;

        if (recordsToLoad == 0 && recordsRequested == true) {
            onAllRecordsLoaded();
        } else if (recordsToLoad < 0) {
            Log.e(LOG_TAG, "SIMRecords: recordsToLoad <0, programmer error suspected");
            recordsToLoad = 0;
        }
    }

    @Override
    protected void onAllRecordsLoaded() {
        setLocaleFromCsim();
        super.onAllRecordsLoaded();     // broadcasts ICC state change to "LOADED"
    }

    @Override
    protected void fetchSimRecords() {
        recordsRequested = true;

        mCi.getIMSIForApp(mParentCard.getAid(), obtainMessage(EVENT_GET_IMSI_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_AD, obtainMessage(EVENT_GET_AD_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_PL,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfPlLoaded()));
        recordsToLoad++;

        new AdnRecordLoader(mFh).loadFromEF(EF_MSISDN, EF_EXT1, 1,
                obtainMessage(EVENT_GET_MSISDN_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
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

        mFh.loadEFTransparent(EF_CSIM_EPRL,
                obtainMessage(EVENT_GET_ICC_RECORD_DONE, new EfCsimEprlLoaded()));
        recordsToLoad++;

        // load ISIM records
        recordsToLoad += mIsimUiccRecords.fetchIsimRecords(mFh, this);
    }

    private int adjstMinDigits (int digits) {
        // Per C.S0005 section 2.3.1.
        digits += 111;
        digits = (digits % 10 == 0)?(digits - 10):digits;
        digits = ((digits / 10) % 10 == 0)?(digits - 100):digits;
        digits = ((digits / 100) % 10 == 0)?(digits - 1000):digits;
        return digits;
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

    @Override
    protected void log(String s) {
        Log.d(LOG_TAG, "[CSIM] " + s);
    }

    @Override
    protected void loge(String s) {
        Log.e(LOG_TAG, "[CSIM] " + s);
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

    public String getPrlVersion() {
        return mPrlVersion;
    }

    public boolean getCsimSpnDisplayCondition() {
        return mCsimSpnDisplayCondition;
    }

    @Override
    public IsimRecords getIsimRecords() {
        return mIsimUiccRecords;
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

        if (mParentCard == null) {
            return false;
        }

        if (mParentCard.isApplicationOnIcc(AppType.APPTYPE_CSIM) &&
            ((mMdn == null) || (mMin == null))) {
            return false;
        }
        return true;
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 697ad73..03a71d8 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -29,7 +28,10 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.CommandsInterface.RadioState;

import android.app.AlarmManager;
import android.content.ContentResolver;
//Synthetic comment -- @@ -157,7 +159,7 @@
};

public CdmaServiceStateTracker(CDMAPhone phone) {
        super(phone, phone.mCM);

this.phone = phone;
cr = phone.getContext().getContentResolver();
//Synthetic comment -- @@ -206,7 +208,7 @@
cm.unregisterForVoiceNetworkStateChanged(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
        if (mIccCard != null) {mIccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -397,17 +399,15 @@
mIsMinInfoReady = true;

updateOtaspState();
                    if (mIccCard != null) {
if (DBG) {
                            log("GET_CDMA_SUBSCRIPTION broadcast Icc state changed");
}
                        mIccCard.broadcastIccStateChangedIntent(
                                IccCardConstants.INTENT_VALUE_ICC_IMSI,
                                null);
} else {
if (DBG) {
                            log("GET_CDMA_SUBSCRIPTION mIccCard is null (probably NV type device)" +
                                    " can't broadcast Icc state changed");
}
}
} else {
//Synthetic comment -- @@ -1665,24 +1665,25 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();

        if (mIccCard != newIccCard) {
            if (mIccCard != null) {
log("Removing stale icc objects.");
                mIccCard.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mIccCard = null;
}
            if (newIccCard != null) {
log("New card found");
                mIccCard = newIccCard;
                mIccRecords = mIccCard.getIccRecords();
if (isSubscriptionFromRuim) {
                    mIccCard.registerForReady(this, EVENT_RUIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index f440935..3bf9c6e 100644

//Synthetic comment -- @@ -20,17 +20,8 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccFileTypeMismatch;
import com.android.internal.telephony.IccIoResult;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneProxy;

import java.util.ArrayList;

/**
* {@hide}
//Synthetic comment -- @@ -41,8 +32,8 @@
//***** Instance Variables

//***** Constructor
    public RuimFileHandler(IccCard card, String aid, CommandsInterface ci) {
        super(card, aid, ci);
}

protected void finalize() {
//Synthetic comment -- @@ -73,6 +64,11 @@
case EF_SMS:
case EF_CST:
case EF_RUIM_SPN:
return MF_SIM + DF_CDMA;
}
return getCommonIccEFPath(efid);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index d3e04bd..095e05d 100755

//Synthetic comment -- @@ -16,8 +16,15 @@

package com.android.internal.telephony.cdma;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -31,11 +38,12 @@
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;

// can't be used since VoiceMailConstants is not public
//import com.android.internal.telephony.gsm.VoiceMailConstants;
//Synthetic comment -- @@ -43,6 +51,9 @@
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;


/**
//Synthetic comment -- @@ -61,10 +72,17 @@
private String mMin2Min1;

private String mPrlVersion;

// ***** Event Constants

    private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_IMSI_DONE = 3;
private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
private static final int EVENT_GET_ICCID_DONE = 5;
//Synthetic comment -- @@ -79,9 +97,8 @@

private static final int EVENT_RUIM_REFRESH = 31;


    public RuimRecords(IccCard card, Context c, CommandsInterface ci) {
        super(card, c, ci);

adnCache = new AdnRecordCache(mFh);

//Synthetic comment -- @@ -90,21 +107,22 @@
// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;

        mCi.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
// NOTE the EVENT_SMS_ON_RUIM is not registered
mCi.registerForIccRefresh(this, EVENT_RUIM_REFRESH, null);

// Start off by setting empty state
        onRadioOffOrNotAvailable();

}

@Override
public void dispose() {
if (DBG) log("Disposing RuimRecords " + this);
//Unregister for all events
        mCi.unregisterForOffOrNotAvailable( this);
mCi.unregisterForIccRefresh(this);
super.dispose();
}

//Synthetic comment -- @@ -113,8 +131,7 @@
if(DBG) log("RuimRecords finalized");
}

    @Override
    protected void onRadioOffOrNotAvailable() {
countVoiceMessages = 0;
mncLength = UNINITIALIZED;
iccid = null;
//Synthetic comment -- @@ -133,6 +150,11 @@
recordsRequested = false;
}

public String getMdnNumber() {
return mMyMobileNumber;
}
//Synthetic comment -- @@ -170,6 +192,15 @@
}
}

/**
* Returns the 5 or 6 digit MCC/MNC of the operator that
*  provided the RUIM card. Returns null of RUIM is not yet ready
//Synthetic comment -- @@ -192,6 +223,203 @@
return mImsi.substring(0, 3 + MccTable.smallestDigitsMccForMnc(mcc));
}

@Override
public void handleMessage(Message msg) {
AsyncResult ar;
//Synthetic comment -- @@ -207,9 +435,9 @@
}

try { switch (msg.what) {
            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;

case EVENT_GET_DEVICE_IDENTITY_DONE:
log("Event EVENT_GET_DEVICE_IDENTITY_DONE Received");
//Synthetic comment -- @@ -302,6 +530,9 @@
}
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing RUIM record", exc);
//Synthetic comment -- @@ -313,6 +544,55 @@
}
}

@Override
protected void onRecordLoaded() {
// One record loaded successfully or failed, In either case
//Synthetic comment -- @@ -343,10 +623,10 @@
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
MccTable.countryCodeForMcc(Integer.parseInt(mImsi.substring(0,3))));
}
recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
        mParentCard.broadcastIccStateChangedIntent(
                IccCardConstants.INTENT_VALUE_ICC_LOADED, null);
}

@Override
//Synthetic comment -- @@ -362,13 +642,43 @@

if (DBG) log("fetchRuimRecords " + recordsToLoad);

        mCi.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

if (DBG) log("fetchRuimRecords " + recordsToLoad + " requested: " + recordsRequested);
// Further records that can be inserted are Operator/OEM dependent
}
//Synthetic comment -- @@ -385,6 +695,29 @@
}

@Override
public void setVoiceMessageWaiting(int line, int countWaiting) {
if (line != 1) {
// only profile 1 is supported
//Synthetic comment -- @@ -411,7 +744,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentCard.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -445,6 +778,25 @@
}
}

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[RuimRecords] " + s);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b429cd2..bb9548f 100644

//Synthetic comment -- @@ -50,13 +50,13 @@
import static com.android.internal.telephony.CommandsInterface.SERVICE_CLASS_VOICE;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_BASEBAND_VERSION;

import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccRecords;
//Synthetic comment -- @@ -71,6 +71,8 @@
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -710,7 +712,8 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this);
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -729,7 +732,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this);

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -742,7 +745,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this);
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -1072,6 +1075,10 @@
mDataConnectionTracker.setDataOnRoamingEnabled(enable);
}

/**
* Removes the given MMI from the pending list and notifies
* registrants that it is complete.
//Synthetic comment -- @@ -1139,7 +1146,8 @@
GsmMmiCode mmi;
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
                                                   GSMPhone.this);
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1339,23 +1347,24 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();

        IccCard c = mIccCard.get();
        if (c != newIccCard) {
            if (c != null) {
if (LOCAL_DEBUG) log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForSimRecordEvents();
mSimPhoneBookIntManager.updateIccRecords(null);
}
mIccRecords.set(null);
                mIccCard.set(null);
}
            if (newIccCard != null) {
                if (LOCAL_DEBUG) log("New card found");
                mIccCard.set(newIccCard);
                mIccRecords.set(newIccCard.getIccRecords());
registerForSimRecordEvents();
mSimPhoneBookIntManager.updateIccRecords(mIccRecords.get());
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 83e1b0e..2572406 100644

//Synthetic comment -- @@ -376,8 +376,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getIccCard() != null &&
                        phone.getIccCard().getState() != IccCardConstants.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.mRestrictedState.isCsRestricted()) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index a1812f1..551cd54 100644

//Synthetic comment -- @@ -67,6 +67,8 @@
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -2660,11 +2662,7 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();
        IccRecords newIccRecords = null;
        if (newIccCard != null) {
            newIccRecords = newIccCard.getIccRecords();
        }

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -2674,7 +2672,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New card found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 1f7836e..73d44eb 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Context;
import com.android.internal.telephony.*;

import android.os.*;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -110,6 +111,8 @@

GSMPhone phone;
Context context;

String action;              // One of ACTION_*
String sc;                  // Service Code
//Synthetic comment -- @@ -173,7 +176,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -181,7 +184,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -196,14 +199,14 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isTwoDigitShortCode(phone.getContext(), dialString)) {
//Is a country-specific exception to short codes as defined in TS 22.030, 6.5.3.2
ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -212,10 +215,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -231,8 +234,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone) {
        GsmMmiCode ret = new GsmMmiCode(phone);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -383,12 +386,16 @@

//***** Constructor

    GsmMmiCode (GSMPhone phone) {
// The telephony unit-test cases may create GsmMmiCode's
// in secondary threads
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
}

//***** MmiCode implementation
//Synthetic comment -- @@ -764,8 +771,8 @@
} else if (pinLen < 4 || pinLen > 8 ) {
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
                    } else if (sc.equals(SC_PIN) && phone.getIccCard().getState() ==
                               IccCardConstants.State.PUK_REQUIRED ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {
//Synthetic comment -- @@ -885,9 +892,8 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    IccRecords r = phone.mIccRecords.get();
                    if (r != null) {
                        r.setVoiceCallForwardingFlag(1, cffEnabled);
}
}

//Synthetic comment -- @@ -1206,9 +1212,8 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            IccRecords r = phone.mIccRecords.get();
            if (r != null) {
                r.setVoiceCallForwardingFlag(1, cffEnabled);
}
}

//Synthetic comment -- @@ -1234,9 +1239,8 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                IccRecords r = phone.mIccRecords.get();
                if (r != null) {
                    r.setVoiceCallForwardingFlag(1, false);
}
} else {









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 42443fe..a0be5d0 100755

//Synthetic comment -- @@ -30,6 +30,10 @@
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;

import android.app.AlarmManager;
import android.app.Notification;
//Synthetic comment -- @@ -187,7 +191,7 @@
};

public GsmServiceStateTracker(GSMPhone phone) {
        super(phone, phone.mCM);

this.phone = phone;
ss = new ServiceState();
//Synthetic comment -- @@ -239,7 +243,7 @@
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        if (mIccCard != null) {mIccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
//Synthetic comment -- @@ -1087,7 +1091,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (mIccCard != null && mIccCard.getState() == IccCardConstants.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1619,23 +1623,24 @@
return;
}

        IccCard newIccCard = mUiccController.getIccCard();

        if (mIccCard != newIccCard) {
            if (mIccCard != null) {
log("Removing stale icc objects.");
                mIccCard.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
                mIccCard = null;
}
            if (newIccCard != null) {
log("New card found");
                mIccCard = newIccCard;
                mIccRecords = mIccCard.getIccRecords();
                mIccCard.registerForReady(this, EVENT_SIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index dcc9cfd..46992e6 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.PhoneBase;

/**
* {@hide}
//Synthetic comment -- @@ -36,8 +34,8 @@

//***** Constructor

    public SIMFileHandler(IccCard card, String aid, CommandsInterface ci) {
        super(card, aid, ci);
}

protected void finalize() {
//Synthetic comment -- @@ -78,20 +76,9 @@
case EF_INFO_CPHS:
case EF_CSP_CPHS:
return MF_SIM + DF_GSM;

        case EF_PBR:
            // we only support global phonebook.
            return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
}
String path = getCommonIccEFPath(efid);
if (path == null) {
            // The EFids in USIM phone book entries are decided by the card manufacturer.
            // So if we don't match any of the cases above and if its a USIM return
            // the phone book path.
            if (mParentCard != null
                    && mParentCard.isApplicationOnIcc(IccCardApplication.AppType.APPTYPE_USIM)) {
                return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
            }
Log.e(LOG_TAG, "Error: EF Path being returned in null");
}
return path;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index d5099f7..983f3c1 100755

//Synthetic comment -- @@ -31,7 +31,6 @@
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
//Synthetic comment -- @@ -43,6 +42,7 @@
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;

import java.util.ArrayList;

//Synthetic comment -- @@ -66,7 +66,6 @@

// ***** Cached SIM State; cleared on channel close

    private String imsi;
private boolean callForwardingEnabled;


//Synthetic comment -- @@ -125,9 +124,9 @@

// ***** Event Constants

    private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
    protected static final int EVENT_GET_IMSI_DONE = 3;
    protected static final int EVENT_GET_ICCID_DONE = 4;
private static final int EVENT_GET_MBI_DONE = 5;
private static final int EVENT_GET_MBDN_DONE = 6;
private static final int EVENT_GET_MWIS_DONE = 7;
//Synthetic comment -- @@ -176,8 +175,8 @@

// ***** Constructor

    public SIMRecords(IccCard card, Context c, CommandsInterface ci) {
        super(card, c, ci);

adnCache = new AdnRecordCache(mFh);

//Synthetic comment -- @@ -189,23 +188,22 @@
// recordsToLoad is set to 0 because no requests are made yet
recordsToLoad = 0;

        mCi.registerForOffOrNotAvailable(
                        this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCi.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
mCi.registerForIccRefresh(this, EVENT_SIM_REFRESH, null);

// Start off by setting empty state
        onRadioOffOrNotAvailable();

}

@Override
public void dispose() {
if (DBG) log("Disposing SIMRecords " + this);
//Unregister for all events
        mCi.unregisterForOffOrNotAvailable( this);
mCi.unregisterForIccRefresh(this);
mCi.unSetOnSmsOnSim(this);
super.dispose();
}

//Synthetic comment -- @@ -213,7 +211,7 @@
if(DBG) log("finalized");
}

    protected void onRadioOffOrNotAvailable() {
imsi = null;
msisdn = null;
voiceMailNum = null;
//Synthetic comment -- @@ -529,9 +527,9 @@
}

try { switch (msg.what) {
            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;

/* IO events */
case EVENT_GET_IMSI_DONE:
//Synthetic comment -- @@ -582,8 +580,7 @@
// finally have both the imsi and the mncLength and can parse the imsi properly
MccTable.updateMccMncConfiguration(mContext, imsi.substring(0, 3 + mncLength));
}
                mParentCard.broadcastIccStateChangedIntent(
                        IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
break;

case EVENT_GET_MBI_DONE:
//Synthetic comment -- @@ -1141,7 +1138,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentCard.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -1277,8 +1274,6 @@

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
        mParentCard.broadcastIccStateChangedIntent(
                IccCardConstants.INTENT_VALUE_ICC_LOADED, null);
}

//***** Private methods
//Synthetic comment -- @@ -1308,7 +1303,7 @@

if (DBG) log("fetchSimRecords " + recordsToLoad);

        mCi.getIMSIForApp(mParentCard.getAid(), obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimFileHandler.java b/src/java/com/android/internal/telephony/ims/IsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..a2b0c67

//Synthetic comment -- @@ -0,0 +1,59 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java b/src/java/com/android/internal/telephony/ims/IsimUiccRecords.java
//Synthetic comment -- index ee1a42d..e6d9c7c 100644

//Synthetic comment -- @@ -16,13 +16,21 @@

package com.android.internal.telephony.ims;

import android.os.AsyncResult;
import android.os.Handler;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.gsm.SimTlv;

import java.nio.charset.Charset;
import java.util.ArrayList;
//Synthetic comment -- @@ -34,12 +42,14 @@
/**
* {@hide}
*/
public final class IsimUiccRecords implements IsimRecords {
protected static final String LOG_TAG = "GSM";

private static final boolean DBG = true;
private static final boolean DUMP_RECORDS = false;   // Note: PII is logged when this is true

// ISIM EF records (see 3GPP TS 31.103)
private String mIsimImpi;               // IMS private user identity
private String mIsimDomain;             // IMS home network domain name
//Synthetic comment -- @@ -47,6 +57,75 @@

private static final int TAG_ISIM_VALUE = 0x80;     // From 3GPP TS 31.103

private class EfIsimImpiLoaded implements IccRecords.IccRecordLoaded {
public String getEfName() {
return "EF_ISIM_IMPI";
//Synthetic comment -- @@ -87,22 +166,6 @@
}

/**
     * Request the ISIM records to load.
     * @param iccFh the IccFileHandler to load the records from
     * @param h the Handler to which the response message will be sent
     * @return the number of EF record requests that were added
     */
    public int fetchIsimRecords(IccFileHandler iccFh, Handler h) {
        iccFh.loadEFTransparent(EF_IMPI, h.obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimImpiLoaded()));
        iccFh.loadEFLinearFixedAll(EF_IMPU, h.obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimImpuLoaded()));
        iccFh.loadEFTransparent(EF_DOMAIN, h.obtainMessage(
                IccRecords.EVENT_GET_ICC_RECORD_DONE, new EfIsimDomainLoaded()));
        return 3;   // number of EF record load requests
    }

    /**
* ISIM records for IMS are stored inside a Tag-Length-Value record as a UTF-8 string
* with tag value 0x80.
* @param record the byte array containing the IMS data string
//Synthetic comment -- @@ -120,11 +183,31 @@
return null;
}

    void log(String s) {
if (DBG) Log.d(LOG_TAG, "[ISIM] " + s);
}

    void loge(String s) {
if (DBG) Log.e(LOG_TAG, "[ISIM] " + s);
}

//Synthetic comment -- @@ -154,4 +237,31 @@
public String[] getIsimImpu() {
return (mIsimImpu != null) ? mIsimImpu.clone() : null;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 53a222e..05d38b5 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,13 +16,7 @@

package com.android.internal.telephony.uicc;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.sip.SipPhone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -30,6 +24,13 @@
import android.os.RegistrantList;
import android.util.Log;

/* This class is responsible for keeping all knowledge about
* ICCs in the system. It is also used as API to get appropriate
* applications to pass them to phone and service trackers.
//Synthetic comment -- @@ -38,34 +39,68 @@
private static final boolean DBG = true;
private static final String LOG_TAG = "RIL_UiccController";

private static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;

private static UiccController mInstance;

    private PhoneBase mCurrentPhone;
private CommandsInterface mCi;
    private IccCard mIccCard;
    private boolean mRegisteredWithCi = false;

private RegistrantList mIccChangedRegistrants = new RegistrantList();

    public static synchronized UiccController getInstance(PhoneBase phone) {
if (mInstance == null) {
            mInstance = new UiccController(phone);
        } else if (phone != null) {
            mInstance.setNewPhone(phone);
}
return mInstance;
}

    // This method is not synchronized as getInstance(PhoneBase) is.
    public static UiccController getInstance() {
        return getInstance(null);
}

    public synchronized IccCard getIccCard() {
        return mIccCard;
}

//Notifies when card status changes
//Synthetic comment -- @@ -97,9 +132,13 @@
}
}

    private UiccController(PhoneBase phone) {
if (DBG) log("Creating UiccController");
        setNewPhone(phone);
}

private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
//Synthetic comment -- @@ -112,55 +151,18 @@

IccCardStatus status = (IccCardStatus)ar.result;

        //Update already existing card
        if (mIccCard != null) {
            mIccCard.update(mCurrentPhone, status);
        }

        //Create new card
        if (mIccCard == null) {
            mIccCard = new IccCard(mCurrentPhone, status, mCurrentPhone.getPhoneName(), true);
}

if (DBG) log("Notifying IccChangedRegistrants");
mIccChangedRegistrants.notifyRegistrants();
}

    private void setNewPhone(PhoneBase phone) {
        if (phone == null) {
            throw new RuntimeException("Phone can't be null in UiccController");
            //return;
        }

        if (DBG) log("setNewPhone");
        // TODO: remove this
        if (phone instanceof SipPhone) {
            if (DBG) log("Got SipPhone. Ignore.");
            return;
        }

        if (mCurrentPhone != phone) {
            if (mIccCard != null) {
                // Refresh card if phone changed
                // TODO: Remove once card is simplified
                if (DBG) log("Disposing card since phone object changed");
                mIccCard.dispose();
                mIccCard = null;
            }
            sendMessage(obtainMessage(EVENT_ICC_STATUS_CHANGED));
            mCurrentPhone = phone;

            if (!mRegisteredWithCi) {
                // This needs to be done only once after we have valid phone object
                mCi = mCurrentPhone.mCM;
                mCi.registerForIccStatusChanged(this, EVENT_ICC_STATUS_CHANGED, null);
                // TODO remove this once modem correctly notifies the unsols
                mCi.registerForOn(this, EVENT_ICC_STATUS_CHANGED, null);
                mRegisteredWithCi = true;
            }
        }
    }

private void log(String string) {
Log.d(LOG_TAG, string);
}







