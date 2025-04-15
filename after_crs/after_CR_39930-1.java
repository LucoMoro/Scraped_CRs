/*Telephony: Dynamically instantiate IccCard

Instantiate when get_sim_status request returns

Change-Id:I9c9333d23f1e0b23256731b245577d1a25721647*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 89a02d3..a5b0341 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.internal.R;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -57,6 +58,7 @@
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
* {@hide}
//Synthetic comment -- @@ -159,11 +161,12 @@

// member variables
protected PhoneBase mPhone;
    protected UiccController mUiccController;
    protected AtomicReference<IccRecords> mIccRecords = new AtomicReference<IccRecords>();
protected DctConstants.Activity mActivity = DctConstants.Activity.NONE;
protected DctConstants.State mState = DctConstants.State.IDLE;
protected Handler mDataConnectionTracker = null;

protected long mTxPkts;
protected long mRxPkts;
protected int mNetStatPollPeriod;
//Synthetic comment -- @@ -412,6 +415,8 @@
super();
if (DBG) log("DCT.constructor");
mPhone = phone;
        mUiccController = UiccController.getInstance();
        mUiccController.registerForIccChanged(this, DctConstants.EVENT_ICC_CHANGED, null);

IntentFilter filter = new IntentFilter();
filter.addAction(getActionIntentReconnectAlarm());
//Synthetic comment -- @@ -454,6 +459,7 @@
mIsDisposed = true;
mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
mDataRoamingSettingObserver.unregister(mPhone.getContext());
        mUiccController.unregisterForIccChanged(this);
}

protected void broadcastMessenger() {
//Synthetic comment -- @@ -576,6 +582,7 @@
protected abstract void onCleanUpConnection(boolean tearDown, int apnId, String reason);
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);
    protected abstract void onUpdateIcc();

protected void onDataStallAlarm(int tag) {
loge("onDataStallAlarm: not impleted tag=" + tag);
//Synthetic comment -- @@ -686,6 +693,10 @@
onSetPolicyDataEnabled(enabled);
break;
}
            case DctConstants.EVENT_ICC_CHANGED:
                onUpdateIcc();
                break;

default:
Log.e("DATA", "Unidentified event msg=" + msg);
break;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 740292c..b093ef7 100644

//Synthetic comment -- @@ -34,10 +34,13 @@

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
//Synthetic comment -- @@ -84,8 +87,6 @@
protected static final int EVENT_ICC_LOCKED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
protected static final int EVENT_ICC_READY = 6;
private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 7;
private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 8;
//Synthetic comment -- @@ -122,34 +123,19 @@
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
mPhone.mCM.unregisterForIccStatusChanged(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
mPhone.mCM.unregisterForOn(mHandler);
//Synthetic comment -- @@ -159,6 +145,42 @@
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
//Synthetic comment -- @@ -289,27 +311,23 @@
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
//Synthetic comment -- @@ -439,21 +457,15 @@
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
//Synthetic comment -- @@ -534,11 +546,8 @@
// Call onReady Record(s) on the IccCard becomes ready (not NV)
if (oldState != IccCardConstants.State.READY && newState == IccCardConstants.State.READY &&
(is3gpp || isSubscriptionFromIccCard)) {
            mIccFileHandler.setAid(getAid());
            broadcastIccStateChangedIntent(IccCardConstants.INTENT_VALUE_ICC_READY, null);
mIccRecords.onReady();
}
}
//Synthetic comment -- @@ -660,7 +669,6 @@
if (!is3gpp) {
handleCdmaSubscriptionSource();
}
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
handleCdmaSubscriptionSource();
//Synthetic comment -- @@ -681,30 +689,9 @@
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
break;
case EVENT_GET_ICC_STATUS_DONE:
                    IccCardStatus cs = (IccCardStatus)msg.obj;

                    getIccCardStatusDone(cs);
break;
case EVENT_QUERY_FACILITY_LOCK_DONE:
ar = (AsyncResult)msg.obj;
//Synthetic comment -- @@ -753,10 +740,6 @@
= ar.exception;
((Message)ar.userObj).sendToTarget();
break;
case EVENT_CARD_REMOVED:
onIccSwap(false);
break;
//Synthetic comment -- @@ -926,6 +909,10 @@
Log.d(mLogTag, "[IccCard] " + msg);
}

    private void loge(String msg) {
        Log.e(mLogTag, "[IccCard] " + msg);
    }

protected int getCurrentApplicationIndex() {
if (is3gpp) {
return mIccCardStatus.getGsmUmtsSubscriptionAppIndex();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 45562ca..0e5f2da 100644

//Synthetic comment -- @@ -103,11 +103,23 @@

public IccPhoneBookInterfaceManager(PhoneBase phone) {
this.phone = phone;
        IccRecords r = phone.mIccRecords.get();
        if (r != null) {
            adnCache = r.getAdnCache();
        }
}

public void dispose() {
}

    public void updateIccRecords(IccRecords iccRecords) {
        if (iccRecords != null) {
            adnCache = iccRecords.getAdnCache();
        } else {
            adnCache = null;
        }
    }

protected void publish() {
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
ServiceManager.addService("simphonebook", this);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index 41c9d5a..3c90647 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;

import java.util.concurrent.atomic.AtomicBoolean;

/**
* {@hide}
*/
//Synthetic comment -- @@ -33,7 +35,7 @@

protected static final boolean DBG = true;
// ***** Instance Variables
    protected AtomicBoolean mDestroyed = new AtomicBoolean(false);
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
//Synthetic comment -- @@ -79,9 +81,9 @@

// ***** Event Constants
protected static final int EVENT_SET_MSISDN_DONE = 30;
    public static final int EVENT_MWI = 0; // Message Waiting indication
    public static final int EVENT_CFI = 1; // Call Forwarding indication
    public static final int EVENT_SPN = 2; // Service Provider Name

public static final int EVENT_GET_ICC_RECORD_DONE = 100;

//Synthetic comment -- @@ -113,7 +115,7 @@
* Call when the IccRecords object is no longer going to be used.
*/
public void dispose() {
        mDestroyed.set(true);
mParentCard = null;
mFh = null;
mCi = null;
//Synthetic comment -- @@ -128,12 +130,8 @@
return adnCache;
}

public void registerForRecordsLoaded(Handler h, int what, Object obj) {
        if (mDestroyed.get()) {
return;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index b55240a..866628b 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.gsm.SIMRecords;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -110,6 +111,7 @@
protected static final int EVENT_SET_NETWORK_AUTOMATIC          = 28;
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;
    protected static final int EVENT_ICC_CHANGED                    = 31;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";
//Synthetic comment -- @@ -126,7 +128,8 @@
int mCallRingDelay;
public boolean mIsTheCurrentActivePhone = true;
boolean mIsVoiceCapable = true;
    protected UiccController mUiccController = null;
    public AtomicReference<IccRecords> mIccRecords = new AtomicReference<IccRecords>();
protected AtomicReference<IccCard> mIccCard = new AtomicReference<IccCard>();
public SmsStorageMonitor mSmsStorageMonitor;
public SmsUsageMonitor mSmsUsageMonitor;
//Synthetic comment -- @@ -251,6 +254,8 @@
// Initialize device storage and outgoing SMS usage monitors for SMSDispatchers.
mSmsStorageMonitor = new SmsStorageMonitor(this);
mSmsUsageMonitor = new SmsUsageMonitor(context);
        mUiccController = UiccController.getInstance(this);
        mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
}

public void dispose() {
//Synthetic comment -- @@ -262,6 +267,7 @@
// Dispose the SMS usage and storage monitors
mSmsStorageMonitor.dispose();
mSmsUsageMonitor.dispose();
            mUiccController.unregisterForIccChanged(this);
}
}

//Synthetic comment -- @@ -269,9 +275,10 @@
mSmsStorageMonitor = null;
mSmsUsageMonitor = null;
mSMS = null;
        mIccRecords.set(null);
mIccCard.set(null);
mDataConnectionTracker = null;
        mUiccController = null;
}

/**
//Synthetic comment -- @@ -309,6 +316,10 @@
}
break;

            case EVENT_ICC_CHANGED:
                onUpdateIccAvailability();
                break;

default:
throw new RuntimeException("unexpected event not handled");
}
//Synthetic comment -- @@ -319,6 +330,9 @@
return mContext;
}

    // Will be called when icc changed
    protected abstract void onUpdateIccAvailability();

/**
* Disables the DNS check (i.e., allows "0.0.0.0").
* Useful for lab testing environment.
//Synthetic comment -- @@ -667,22 +681,26 @@

@Override
public String getIccSerialNumber() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.iccid : "";
}

@Override
public boolean getIccRecordsLoaded() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getRecordsLoaded() : false;
}

@Override
public boolean getMessageWaitingIndicator() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getVoiceMessageWaiting() : false;
}

@Override
public boolean getCallForwardingIndicator() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getVoiceCallForwardingFlag() : false;
}

/**
//Synthetic comment -- @@ -1136,7 +1154,10 @@
*/
@Override
public void setVoiceMessageWaiting(int line, int countWaiting) {
        IccRecords r = mIccRecords.get();
        if (r != null) {
            r.setVoiceMessageWaiting(line, countWaiting);
        }
}

/**
//Synthetic comment -- @@ -1145,7 +1166,8 @@
*/
@Override
public UsimServiceTable getUsimServiceTable() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getUsimServiceTable() : null;
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
//Synthetic comment -- @@ -1158,7 +1180,7 @@
pw.println(" mCallRingDelay=" + mCallRingDelay);
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
        pw.println(" mIccRecords=" + mIccRecords.get());
pw.println(" mIccCard=" + mIccCard.get());
pw.println(" mSmsStorageMonitor=" + mSmsStorageMonitor);
pw.println(" mSmsUsageMonitor=" + mSmsUsageMonitor);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e4cfb23..178addd 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
//Synthetic comment -- @@ -28,12 +29,17 @@
import java.io.FileDescriptor;
import java.io.PrintWriter;

import com.android.internal.telephony.uicc.UiccController;

/**
* {@hide}
*/
public abstract class ServiceStateTracker extends Handler {

protected CommandsInterface cm;
    protected UiccController mUiccController = null;
    protected IccCard mIccCard = null;
    protected IccRecords mIccRecords = null;

public ServiceState ss;
protected ServiceState newSS;
//Synthetic comment -- @@ -131,7 +137,7 @@
protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 39;
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
protected static final int EVENT_RADIO_ON                          = 41;
    protected static final int EVENT_ICC_CHANGED                       = 42;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";

//Synthetic comment -- @@ -168,7 +174,10 @@
protected static final String REGISTRATION_DENIED_GEN  = "General";
protected static final String REGISTRATION_DENIED_AUTH = "Authentication Failure";

    public ServiceStateTracker(PhoneBase p, CommandsInterface ci) {
        cm = ci;
        mUiccController = UiccController.getInstance();
        mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
}

public boolean getDesiredPowerState() {
//Synthetic comment -- @@ -295,6 +304,10 @@
}
break;

            case EVENT_ICC_CHANGED:
                onUpdateIccAvailability();
                break;

default:
log("Unhandled message with number: " + msg.what);
break;
//Synthetic comment -- @@ -305,6 +318,7 @@
protected abstract void handlePollStateResult(int what, AsyncResult ar);
protected abstract void updateSpnDisplay();
protected abstract void setPowerStateToDesired();
    protected abstract void onUpdateIccAvailability();
protected abstract void log(String s);
protected abstract void loge(String s);

//Synthetic comment -- @@ -535,4 +549,19 @@
pw.println(" mPendingRadioPowerOffAfterDataOff=" + mPendingRadioPowerOffAfterDataOff);
pw.println(" mPendingRadioPowerOffAfterDataOffTag=" + mPendingRadioPowerOffAfterDataOffTag);
}

    /**
     * Verifies the current thread is the same as the thread originally
     * used in the initialization of this instance. Throws RuntimeException
     * if not.
     *
     * @exception RuntimeException if the current thread is not
     * the thread that originally obtained this PhoneBase instance.
     */
    protected void checkCorrectThread() {
        if (Thread.currentThread() != getLooper().getThread()) {
            throw new RuntimeException(
                    "ServiceStateTracker must be used from within one thread");
        }
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index df42515..f125484 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -67,7 +68,6 @@
public CDMALTEPhone(Context context, CommandsInterface ci, PhoneNotifier notifier) {
super(context, ci, notifier, false);
m3gppSMS = new GsmSMSDispatcher(this, mSmsStorageMonitor, mSmsUsageMonitor);
}

@Override
//Synthetic comment -- @@ -89,10 +89,6 @@

@Override
protected void initSstIcc() {
mSST = new CdmaLteServiceStateTracker(this);
}

//Synthetic comment -- @@ -101,7 +97,6 @@
synchronized(PhoneProxy.lockForRadioTechnologyChange) {
super.dispose();
m3gppSMS.dispose();
}
}

//Synthetic comment -- @@ -205,11 +200,12 @@

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
//Synthetic comment -- @@ -227,7 +223,8 @@
// return IMSI from USIM as subscriber ID.
@Override
public String getSubscriberId() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getIMSI() : "";
}

@Override
//Synthetic comment -- @@ -242,12 +239,14 @@

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
//Synthetic comment -- @@ -261,6 +260,26 @@
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
protected void log(String s) {
Log.d(LOG_TAG, "[CDMALTEPhone] " + s);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 26ef9dc..f88fa9c 100755

//Synthetic comment -- @@ -50,6 +50,7 @@
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.MmiCode;
//Synthetic comment -- @@ -153,10 +154,6 @@
}

protected void initSstIcc() {
mSST = new CdmaServiceStateTracker(this);
}

//Synthetic comment -- @@ -173,7 +170,6 @@
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
//Synthetic comment -- @@ -728,7 +724,10 @@
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
        IccRecords r = mIccRecords.get();
        if (r != null) {
            r.setVoiceMailNumber(alphaTag, mVmNumber, resp);
        }
}

public String getVoiceMailNumber() {
//Synthetic comment -- @@ -750,7 +749,8 @@
* @hide
*/
public int getVoiceMessageCount() {
        IccRecords r = mIccRecords.get();
        int voicemailCount =  (r != null) ? r.getVoiceMessageCount() : 0;
// If mRuimRecords.getVoiceMessageCount returns zero, then there is possibility
// that phone was power cycled and would have lost the voicemail count.
// So get the count from preferences.
//Synthetic comment -- @@ -1065,6 +1065,39 @@
}
}

    @Override
    protected void onUpdateIccAvailability() {
        if (mUiccController == null ) {
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

private void processIccRecordEvents(int eventCode) {
switch (eventCode) {
case RuimRecords.EVENT_MWI:
//Synthetic comment -- @@ -1463,14 +1496,22 @@
return mEriManager.isEriFileLoaded();
}

    protected void registerForRuimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
            return;
        }
        r.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        r.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
}

    protected void unregisterForRuimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
            return;
        }
        r.unregisterForRecordsEvents(this);
        r.unregisterForRecordsLoaded(this);
}

protected void log(String s) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1088131..07e496d 100644

//Synthetic comment -- @@ -42,6 +42,8 @@
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -112,7 +114,6 @@

p.mCM.registerForAvailable (this, DctConstants.EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, DctConstants.EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
p.mCM.registerForDataNetworkStateChanged (this, DctConstants.EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, DctConstants.EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, DctConstants.EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -154,7 +155,8 @@
// Unregister from all events
mPhone.mCM.unregisterForAvailable(this);
mPhone.mCM.unregisterForOffOrNotAvailable(this);
        IccRecords r = mIccRecords.get();
        if (r != null) { r.unregisterForRecordsLoaded(this);}
mPhone.mCM.unregisterForDataNetworkStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -224,11 +226,12 @@
boolean subscriptionFromNv = (mCdmaSSM.getCdmaSubscriptionSource()
== CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_NV);

        IccRecords r = mIccRecords.get();
boolean allowed =
(psState == ServiceState.STATE_IN_SERVICE ||
mAutoAttachOnCreation) &&
(subscriptionFromNv ||
                            (r != null && r.getRecordsLoaded())) &&
(mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed() ||
mPhone.getState() ==PhoneConstants.State.IDLE) &&
!roaming &&
//Synthetic comment -- @@ -243,7 +246,7 @@
reason += " - psState= " + psState;
}
if (!subscriptionFromNv &&
                    !(r != null && r.getRecordsLoaded())) {
reason += " - RUIM not loaded";
}
if (!(mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed() ||
//Synthetic comment -- @@ -1015,6 +1018,34 @@
}

@Override
    protected void onUpdateIcc() {
        if (mUiccController == null ) {
            return;
        }

        IccCard newIccCard = mUiccController.getIccCard();
        IccRecords newIccRecords = null;
        if (newIccCard != null) {
            newIccRecords = newIccCard.getIccRecords();
        }

        IccRecords r = mIccRecords.get();
        if (r != newIccRecords) {
            if (r != null) {
                log("Removing stale icc objects.");
                r.unregisterForRecordsLoaded(this);
                mIccRecords.set(null);
            }
            if (newIccRecords != null) {
                log("New card found");
                mIccRecords.set(newIccRecords);
                newIccRecords.registerForRecordsLoaded(
                        this, DctConstants.EVENT_RECORDS_LOADED, null);
            }
        }
    }

    @Override
public boolean isDisconnected() {
return ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.FAILED));
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 0c5c342..43db046 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
handlePollStateResult(msg.what, ar);
break;
case EVENT_RUIM_RECORDS_LOADED:
            CdmaLteUiccRecords sim = (CdmaLteUiccRecords)mIccRecords;
if ((sim != null) && sim.isProvisioned()) {
mMdn = sim.getMdn();
mMin = sim.getMin();
//Synthetic comment -- @@ -344,7 +344,7 @@
if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
eriText = phone.getCdmaEriText();
} else if (ss.getState() == ServiceState.STATE_POWER_OFF) {
                    eriText = (mIccRecords != null) ? mIccRecords.getServiceProviderName() : null;
if (TextUtils.isEmpty(eriText)) {
// Sets operator alpha property by retrieving from
// build-time system property
//Synthetic comment -- @@ -359,16 +359,18 @@
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
                    isInHomeSidNid(ss.getSystemId(), ss.getNetworkId()) &&
                    mIccRecords != null) {
                    ss.setOperatorAlphaLong(mIccRecords.getServiceProviderName());
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 5a4af7a..8247d54 100755

//Synthetic comment -- @@ -116,12 +116,6 @@
long mSavedTime;
long mSavedAtTime;

/** Wake lock used while setting time of day. */
private PowerManager.WakeLock mWakeLock;
private static final String WAKELOCK_TAG = "ServiceStateTracker";
//Synthetic comment -- @@ -163,11 +157,10 @@
};

public CdmaServiceStateTracker(CDMAPhone phone) {
        super(phone, phone.mCM);

this.phone = phone;
cr = phone.getContext().getContentResolver();
ss = new ServiceState();
newSS = new ServiceState();
cellLoc = new CdmaCellLocation();
//Synthetic comment -- @@ -204,18 +197,17 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME_ZONE), true,
mAutoTimeZoneObserver);
setSignalStrengthDefaultValues();
}

public void dispose() {
        checkCorrectThread();
// Unregister for all events.
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
        if (mIccCard != null) {mIccCard.unregisterForReady(this);}
        if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(mAutoTimeObserver);
//Synthetic comment -- @@ -287,14 +279,6 @@
// TODO: Consider calling setCurrentPreferredNetworkType as we do in GsmSST.
// cm.setCurrentPreferredNetworkType();

if (phone.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE) {
// Subscription will be read from SIM I/O
if (DBG) log("Receive EVENT_RUIM_READY");
//Synthetic comment -- @@ -413,8 +397,19 @@
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
if (DBG) {
log("GET_CDMA_SUBSCRIPTION: error parsing cdmaSubscription params num="
//Synthetic comment -- @@ -506,8 +501,6 @@
if (!isSubscriptionFromRuim) {
// NV is ready when subscription source is NV
sendMessage(obtainMessage(EVENT_NV_READY));
}
}

//Synthetic comment -- @@ -1702,6 +1695,38 @@
}

@Override
    protected void onUpdateIccAvailability() {
        if (mUiccController == null ) {
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
                }
            }
        }
    }

    @Override
protected void log(String s) {
Log.d(LOG_TAG, "[CdmaSST] " + s);
}
//Synthetic comment -- @@ -1734,7 +1759,6 @@
pw.println(" mSavedTimeZone=" + mSavedTimeZone);
pw.println(" mSavedTime=" + mSavedTime);
pw.println(" mSavedAtTime=" + mSavedAtTime);
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mCurPlmn=" + mCurPlmn);
pw.println(" mMdn=" + mMdn);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index 04ee2dd..e919245 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -34,7 +35,6 @@

public RuimPhoneBookInterfaceManager(CDMAPhone phone) {
super(phone);
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -61,8 +61,12 @@
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE, status);

            IccFileHandler fh = phone.getIccFileHandler();
            //IccFileHandler can be null if there is no icc card present.
            if (fh != null) {
                fh.getEFLinearRecordSize(efid, response);
                waitForResult(status);
            }
}

return recordSize;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e257fb6..d3e04bd 100755

//Synthetic comment -- @@ -200,7 +200,7 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed.get()) {
loge("Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
//Synthetic comment -- @@ -318,18 +318,20 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;
        if (DBG) log("onRecordLoaded " + recordsToLoad + " requested: " + recordsRequested);

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
} else if (recordsToLoad < 0) {
            loge("recordsToLoad <0, programmer error suspected");
recordsToLoad = 0;
}
}

@Override
protected void onAllRecordsLoaded() {
        if (DBG) log("record load complete");

// Further records that can be inserted are Operator/OEM dependent

String operator = getRUIMOperatorNumeric();
//Synthetic comment -- @@ -349,13 +351,6 @@

@Override
public void onReady() {
fetchRuimRecords();

mCi.getCDMASubscription(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_DONE));
//Synthetic comment -- @@ -365,7 +360,7 @@
private void fetchRuimRecords() {
recordsRequested = true;

        if (DBG) log("fetchRuimRecords " + recordsToLoad);

mCi.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;
//Synthetic comment -- @@ -374,7 +369,7 @@
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

        if (DBG) log("fetchRuimRecords " + recordsToLoad + " requested: " + recordsRequested);
// Further records that can be inserted are Operator/OEM dependent
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 8c5368e..9b1cafc 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
//Synthetic comment -- @@ -140,11 +141,10 @@
}

mCM.setPhoneType(PhoneConstants.PHONE_TYPE_GSM);
mCT = new GsmCallTracker(this);
mSST = new GsmServiceStateTracker (this);
mSMS = new GsmSMSDispatcher(this, mSmsStorageMonitor, mSmsUsageMonitor);

mDataConnectionTracker = new GsmDataConnectionTracker (this);
if (!unitTestMode) {
mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
//Synthetic comment -- @@ -153,7 +153,6 @@
}

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -797,7 +796,8 @@

public String getVoiceMailNumber() {
// Read from the SIM. If its null, try reading from the shared preference area.
        IccRecords r = mIccRecords.get();
        String number = (r != null) ? r.getVoiceMailNumber() : "";
if (TextUtils.isEmpty(number)) {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);
//Synthetic comment -- @@ -819,8 +819,9 @@

public String getVoiceMailAlphaTag() {
String ret;
        IccRecords r = mIccRecords.get();

        ret = (r != null) ? r.getVoiceMailAlphaTag() : "";

if (ret == null || ret.length() == 0) {
return mContext.getText(
//Synthetic comment -- @@ -853,24 +854,31 @@
}

public String getSubscriberId() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getIMSI() : "";
}

public String getLine1Number() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getMsisdnNumber() : "";
}

@Override
public String getMsisdn() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getMsisdnNumber() : "";
}

public String getLine1AlphaTag() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.getMsisdnAlphaTag() : "";
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
        IccRecords r = mIccRecords.get();
        if (r != null) {
            r.setMsisdnNumber(alphaTag, number, onComplete);
        }
}

public void setVoiceMailNumber(String alphaTag,
//Synthetic comment -- @@ -880,7 +888,10 @@
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
        IccRecords r = mIccRecords.get();
        if (r != null) {
            r.setVoiceMailNumber(alphaTag, mVmNumber, resp);
        }
}

private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
//Synthetic comment -- @@ -1248,8 +1259,9 @@

case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
                IccRecords r = mIccRecords.get();
                if (ar.exception == null && r != null) {
                    r.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
if (onComplete != null) {
//Synthetic comment -- @@ -1322,12 +1334,41 @@
}
}

    @Override
    protected void onUpdateIccAvailability() {
        if (mUiccController == null ) {
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
        }
    }

private void processIccRecordEvents(int eventCode) {
switch (eventCode) {
            case IccRecords.EVENT_CFI:
notifyCallForwardingIndicator();
break;
            case IccRecords.EVENT_MWI:
notifyMessageWaitingIndicator();
break;
}
//Synthetic comment -- @@ -1339,11 +1380,12 @@
* @return true for success; false otherwise.
*/
boolean updateCurrentCarrierInProvider() {
        IccRecords r = mIccRecords.get();
        if (r != null) {
try {
Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
ContentValues map = new ContentValues();
                map.put(Telephony.Carriers.NUMERIC, r.getOperatorNumeric());
mContext.getContentResolver().insert(uri, map);
return true;
} catch (SQLException e) {
//Synthetic comment -- @@ -1405,16 +1447,19 @@
}

private void handleCfuQueryResult(CallForwardInfo[] infos) {
        IccRecords r = mIccRecords.get();
        if (r != null) {
            if (infos == null || infos.length == 0) {
                // Assume the default is not active
                // Set unconditional CFF in SIM to false
                r.setVoiceCallForwardingFlag(1, false);
            } else {
                for (int i = 0, s = infos.length; i < s; i++) {
                    if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                        r.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
                        // should only have the one
                        break;
                    }
}
}
}
//Synthetic comment -- @@ -1473,22 +1518,31 @@
}

public boolean isCspPlmnEnabled() {
        IccRecords r = mIccRecords.get();
        return (r != null) ? r.isCspPlmnEnabled() : false;
}

private void registerForSimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
            return;
        }
        r.registerForNetworkSelectionModeAutomatic(
this, EVENT_SET_NETWORK_AUTOMATIC, null);
        r.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
        r.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        r.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}

private void unregisterForSimRecordEvents() {
        IccRecords r = mIccRecords.get();
        if (r == null) {
            return;
        }
        r.unregisterForNetworkSelectionModeAutomatic(this);
        r.unregisterForNewSms(this);
        r.unregisterForRecordsEvents(this);
        r.unregisterForRecordsLoaded(this);
}

@Override
//Synthetic comment -- @@ -1505,4 +1559,8 @@
if (VDBG) pw.println(" mImeiSv=" + mImeiSv);
pw.println(" mVmNumber=" + mVmNumber);
}

    protected void log(String s) {
        Log.d(LOG_TAG, "[GSMPhone] " + s);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 016513c..adce14d 100644

//Synthetic comment -- @@ -60,6 +60,8 @@
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -190,7 +192,6 @@
p.mCM.registerForAvailable (this, DctConstants.EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, DctConstants.EVENT_RADIO_OFF_OR_NOT_AVAILABLE,
null);
p.mCM.registerForDataNetworkStateChanged (this, DctConstants.EVENT_DATA_STATE_CHANGED,
null);
p.getCallTracker().registerForVoiceCallEnded (this, DctConstants.EVENT_VOICE_CALL_ENDED,
//Synthetic comment -- @@ -235,7 +236,8 @@
//Unregister for all events
mPhone.mCM.unregisterForAvailable(this);
mPhone.mCM.unregisterForOffOrNotAvailable(this);
        IccRecords r = mIccRecords.get();
        if (r != null) { r.unregisterForRecordsLoaded(this);}
mPhone.mCM.unregisterForDataNetworkStateChanged(this);
mPhone.getCallTracker().unregisterForVoiceCallEnded(this);
mPhone.getCallTracker().unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -637,10 +639,12 @@

int gprsState = mPhone.getServiceStateTracker().getCurrentDataConnectionState();
boolean desiredPowerState = mPhone.getServiceStateTracker().getDesiredPowerState();
        IccRecords r = mIccRecords.get();
        boolean recordsLoaded = (r != null) ? r.getRecordsLoaded() : false;

boolean allowed =
(gprsState == ServiceState.STATE_IN_SERVICE || mAutoAttachOnCreation) &&
                    recordsLoaded &&
(mPhone.getState() == PhoneConstants.State.IDLE ||
mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()) &&
internalDataEnabled &&
//Synthetic comment -- @@ -652,7 +656,7 @@
if (!((gprsState == ServiceState.STATE_IN_SERVICE) || mAutoAttachOnCreation)) {
reason += " - gprs= " + gprsState;
}
            if (!recordsLoaded) reason += " - SIM not loaded";
if (mPhone.getState() != PhoneConstants.State.IDLE &&
!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()) {
reason += " - PhoneState= " + mPhone.getState();
//Synthetic comment -- @@ -1971,7 +1975,8 @@
log("onRadioAvailable: We're on the simulator; assuming data is connected");
}

        IccRecords r = mIccRecords.get();
        if (r != null && r.getRecordsLoaded()) {
notifyOffApnsOfAvailability(null);
}

//Synthetic comment -- @@ -2267,7 +2272,8 @@
*/
private void createAllApnList() {
mAllApns = new ArrayList<ApnSetting>();
        IccRecords r = mIccRecords.get();
        String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
String selection = "numeric = '" + operator + "'";
// query only enabled apn.
//Synthetic comment -- @@ -2383,7 +2389,8 @@
}
}

        IccRecords r = mIccRecords.get();
        String operator = (r != null) ? r.getOperatorNumeric() : "";
int networkType = mPhone.getServiceState().getNetworkType();

if (canSetPreferApn && mPreferredApn != null &&
//Synthetic comment -- @@ -2619,6 +2626,34 @@
}

@Override
    protected void onUpdateIcc() {
        if (mUiccController == null ) {
            return;
        }

        IccCard newIccCard = mUiccController.getIccCard();
        IccRecords newIccRecords = null;
        if (newIccCard != null) {
            newIccRecords = newIccCard.getIccRecords();
        }

        IccRecords r = mIccRecords.get();
        if (r != newIccRecords) {
            if (r != null) {
                log("Removing stale icc objects.");
                r.unregisterForRecordsLoaded(this);
                mIccRecords.set(null);
            }
            if (newIccRecords != null) {
                log("New card found");
                mIccRecords.set(newIccRecords);
                newIccRecords.registerForRecordsLoaded(
                        this, DctConstants.EVENT_RECORDS_LOADED, null);
            }
        }
    }

    @Override
protected void log(String s) {
Log.d(LOG_TAG, "[GsmDCT] "+ s);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 15e6a22..1f7836e 100644

//Synthetic comment -- @@ -885,7 +885,10 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    IccRecords r = phone.mIccRecords.get();
                    if (r != null) {
                        r.setVoiceCallForwardingFlag(1, cffEnabled);
                    }
}

onSetComplete(ar);
//Synthetic comment -- @@ -1203,7 +1206,10 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            IccRecords r = phone.mIccRecords.get();
            if (r != null) {
                r.setVoiceCallForwardingFlag(1, cffEnabled);
            }
}

return TextUtils.replace(template, sources, destinations);
//Synthetic comment -- @@ -1228,7 +1234,10 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                IccRecords r = phone.mIccRecords.get();
                if (r != null) {
                    r.setVoiceCallForwardingFlag(1, false);
                }
} else {

SpannableStringBuilder tb = new SpannableStringBuilder();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 808ec2b..046d220 100644

//Synthetic comment -- @@ -125,12 +125,6 @@
long mSavedTime;
long mSavedAtTime;

/** Started the recheck process after finding gprs should registered but not. */
private boolean mStartedGprsRegCheck = false;

//Synthetic comment -- @@ -193,10 +187,9 @@
};

public GsmServiceStateTracker(GSMPhone phone) {
        super(phone, phone.mCM);

this.phone = phone;
ss = new ServiceState();
newSS = new ServiceState();
cellLoc = new GsmCellLocation();
//Synthetic comment -- @@ -214,7 +207,6 @@
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);

// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
//Synthetic comment -- @@ -231,7 +223,6 @@
mAutoTimeZoneObserver);

setSignalStrengthDefaultValues();

// Monitor locale change
IntentFilter filter = new IntentFilter();
//Synthetic comment -- @@ -243,12 +234,13 @@
}

public void dispose() {
        checkCorrectThread();
// Unregister for all events.
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        if (mIccCard != null) {mIccCard.unregisterForReady(this);}
        if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -286,15 +278,6 @@
// Set the network type, in case the radio does not restore it.
cm.setCurrentPreferredNetworkType();

boolean skipRestoringSelection = phone.getContext().getResources().getBoolean(
com.android.internal.R.bool.skip_restoring_network_selection);

//Synthetic comment -- @@ -496,8 +479,11 @@
}

protected void updateSpnDisplay() {
        if (mIccRecords == null) {
            return;
        }
        int rule = mIccRecords.getDisplayRule(ss.getOperatorNumeric());
        String spn = mIccRecords.getServiceProviderName();
String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN
//Synthetic comment -- @@ -1150,7 +1136,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (mIccCard.getState() == IccCardConstants.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1677,6 +1663,35 @@
}

@Override
    protected void onUpdateIccAvailability() {
        if (mUiccController == null ) {
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
            }
        }
    }
    @Override
protected void log(String s) {
Log.d(LOG_TAG, "[GsmSST] " + s);
}
//Synthetic comment -- @@ -1716,7 +1731,6 @@
pw.println(" mSavedTimeZone=" + mSavedTimeZone);
pw.println(" mSavedTime=" + mSavedTime);
pw.println(" mSavedAtTime=" + mSavedAtTime);
pw.println(" mStartedGprsRegCheck=" + mStartedGprsRegCheck);
pw.println(" mReportedGprsNoReg=" + mReportedGprsNoReg);
pw.println(" mNotification=" + mNotification);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 1ec3ea7..d5099f7 100755

//Synthetic comment -- @@ -522,7 +522,7 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed.get()) {
loge("Received message " + msg + "[" + msg.what + "] " +
" while being destroyed. Ignoring.");
return;
//Synthetic comment -- @@ -1300,12 +1300,6 @@

@Override
public void onReady() {
fetchSimRecords();
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index 35ba0d1..37f9a4f 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -34,7 +35,6 @@

public SimPhoneBookInterfaceManager(GSMPhone phone) {
super(phone);
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -61,8 +61,11 @@
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE, status);

            IccFileHandler fh = phone.getIccFileHandler();
            if (fh != null) {
                fh.getEFLinearRecordSize(efid, response);
                waitForResult(status);
            }
}

return recordSize;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index ff64486..b0a6080 100755

//Synthetic comment -- @@ -462,4 +462,8 @@
notifyPhoneStateChanged();
}
}

    @Override
    protected void onUpdateIccAvailability() {
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 5961efd..ab7d561 100644

//Synthetic comment -- @@ -16,78 +16,151 @@

package com.android.internal.telephony.uicc;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.PhoneBase;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

/* This class is responsible for keeping all knowledge about
* ICCs in the system. It is also used as API to get appropriate
* applications to pass them to phone and service trackers.
*/
public class UiccController extends Handler {
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
    public void registerForIccChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mIccChangedRegistrants.add(r);
        //Notify registrant right after registering, so that it will get the latest ICC status,
        //otherwise which may not happen until there is an actual change in ICC status.
        r.notifyRegistrant();
    }
    public void unregisterForIccChanged(Handler h) {
        mIccChangedRegistrants.remove(h);
    }

    @Override
    public void handleMessage (Message msg) {
        switch (msg.what) {
            case EVENT_ICC_STATUS_CHANGED:
                if (DBG) log("Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                mCi.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
                break;
            case EVENT_GET_ICC_STATUS_DONE:
                if (DBG) log("Received EVENT_GET_ICC_STATUS_DONE");
                AsyncResult ar = (AsyncResult)msg.obj;
                onGetIccCardStatusDone(ar);
                break;
            default:
                Log.e(LOG_TAG, " Unknown Event " + msg.what);
        }
    }

private UiccController(PhoneBase phone) {
if (DBG) log("Creating UiccController");
setNewPhone(phone);
}

    private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
        if (ar.exception != null) {
            Log.e(LOG_TAG,"Error getting ICC status. "
                    + "RIL_REQUEST_GET_ICC_STATUS should "
                    + "never return an error", ar.exception);
return;
}

        IccCardStatus status = (IccCardStatus)ar.result;

        //Update already existing card
        if (mIccCard != null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
            mIccCard.update(mCurrentPhone, status);
        }

        //Dispose of removed card
        if (mIccCard != null && status.getCardState() != CardState.CARDSTATE_PRESENT) {
mIccCard.dispose();
mIccCard = null;
}

        //Create new card
        if (mIccCard == null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
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
\ No newline at end of file
}







