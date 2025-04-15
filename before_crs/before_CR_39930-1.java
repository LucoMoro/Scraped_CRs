/*Telephony: Dynamically instantiate IccCard

Instantiate when get_sim_status request returns

Change-Id:I9c9333d23f1e0b23256731b245577d1a25721647*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 89a02d3..a5b0341 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.internal.R;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DctConstants;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -57,6 +58,7 @@
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
* {@hide}
//Synthetic comment -- @@ -159,11 +161,12 @@

// member variables
protected PhoneBase mPhone;
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

IntentFilter filter = new IntentFilter();
filter.addAction(getActionIntentReconnectAlarm());
//Synthetic comment -- @@ -454,6 +459,7 @@
mIsDisposed = true;
mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
mDataRoamingSettingObserver.unregister(mPhone.getContext());
}

protected void broadcastMessenger() {
//Synthetic comment -- @@ -576,6 +582,7 @@
protected abstract void onCleanUpConnection(boolean tearDown, int apnId, String reason);
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);

protected void onDataStallAlarm(int tag) {
loge("onDataStallAlarm: not impleted tag=" + tag);
//Synthetic comment -- @@ -686,6 +693,10 @@
onSetPolicyDataEnabled(enabled);
break;
}
default:
Log.e("DATA", "Unidentified event msg=" + msg);
break;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 740292c..b093ef7 100644

//Synthetic comment -- @@ -34,10 +34,13 @@

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cdma.CDMALTEPhone;
import com.android.internal.telephony.cdma.CdmaLteUiccFileHandler;
import com.android.internal.telephony.cdma.CdmaLteUiccRecords;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
//Synthetic comment -- @@ -84,8 +87,6 @@
protected static final int EVENT_ICC_LOCKED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;
protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 3;
    private static final int EVENT_PINPUK_DONE = 4;
    private static final int EVENT_REPOLL_STATUS_DONE = 5;
protected static final int EVENT_ICC_READY = 6;
private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 7;
private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 8;
//Synthetic comment -- @@ -122,34 +123,19 @@
return IccCardConstants.State.UNKNOWN;
}

    public IccCard(PhoneBase phone, String logTag, Boolean is3gpp, Boolean dbg) {
mLogTag = logTag;
mDbg = dbg;
        if (mDbg) log("[IccCard] Creating card type " + (is3gpp ? "3gpp" : "3gpp2"));
        mPhone = phone;
        this.is3gpp = is3gpp;
mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(mPhone.getContext(),
mPhone.mCM, mHandler, EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED, null);
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
        mCatService = CatService.getInstance(mPhone.mCM, mIccRecords,
                mPhone.mContext, mIccFileHandler, this);
mPhone.mCM.registerForOffOrNotAvailable(mHandler, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mPhone.mCM.registerForOn(mHandler, EVENT_RADIO_ON, null);
        mPhone.mCM.registerForIccStatusChanged(mHandler, EVENT_ICC_STATUS_CHANGED, null);
}

public void dispose() {
        if (mDbg) log("[IccCard] Disposing card type " + (is3gpp ? "3gpp" : "3gpp2"));
mPhone.mCM.unregisterForIccStatusChanged(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
mPhone.mCM.unregisterForOn(mHandler);
//Synthetic comment -- @@ -159,6 +145,42 @@
mIccFileHandler.dispose();
}

protected void finalize() {
if (mDbg) log("[IccCard] Finalized card type " + (is3gpp ? "3gpp" : "3gpp2"));
}
//Synthetic comment -- @@ -289,27 +311,23 @@
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
        mPhone.mCM.supplyNetworkDepersonalization(pin,
                mHandler.obtainMessage(EVENT_PINPUK_DONE, onComplete));
}

/**
//Synthetic comment -- @@ -439,21 +457,15 @@
*
*/
public String getServiceProviderName () {
        return mPhone.mIccRecords.getServiceProviderName();
}

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
//Synthetic comment -- @@ -534,11 +546,8 @@
// Call onReady Record(s) on the IccCard becomes ready (not NV)
if (oldState != IccCardConstants.State.READY && newState == IccCardConstants.State.READY &&
(is3gpp || isSubscriptionFromIccCard)) {
            if (!(mIccFileHandler instanceof CdmaLteUiccFileHandler)) {
                // CdmaLteUicc File Handler deals with both USIM and CSIM.
                // Do not lock onto one AID for now.
                mIccFileHandler.setAid(getAid());
            }
mIccRecords.onReady();
}
}
//Synthetic comment -- @@ -660,7 +669,6 @@
if (!is3gpp) {
handleCdmaSubscriptionSource();
}
                    mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
break;
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
handleCdmaSubscriptionSource();
//Synthetic comment -- @@ -681,30 +689,9 @@
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
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
//Synthetic comment -- @@ -753,10 +740,6 @@
= ar.exception;
((Message)ar.userObj).sendToTarget();
break;
                case EVENT_ICC_STATUS_CHANGED:
                    Log.d(mLogTag, "Received Event EVENT_ICC_STATUS_CHANGED");
                    mPhone.mCM.getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE));
                    break;
case EVENT_CARD_REMOVED:
onIccSwap(false);
break;
//Synthetic comment -- @@ -926,6 +909,10 @@
Log.d(mLogTag, "[IccCard] " + msg);
}

protected int getCurrentApplicationIndex() {
if (is3gpp) {
return mIccCardStatus.getGsmUmtsSubscriptionAppIndex();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 45562ca..0e5f2da 100644

//Synthetic comment -- @@ -103,11 +103,23 @@

public IccPhoneBookInterfaceManager(PhoneBase phone) {
this.phone = phone;
}

public void dispose() {
}

protected void publish() {
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
ServiceManager.addService("simphonebook", this);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index 41c9d5a..3c90647 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;

/**
* {@hide}
*/
//Synthetic comment -- @@ -33,7 +35,7 @@

protected static final boolean DBG = true;
// ***** Instance Variables
    protected boolean mDestroyed = false; // set to true once this object needs to be disposed of
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
//Synthetic comment -- @@ -79,9 +81,9 @@

// ***** Event Constants
protected static final int EVENT_SET_MSISDN_DONE = 30;
    public static final int EVENT_MWI = 0;
    public static final int EVENT_CFI = 1;
    public static final int EVENT_SPN = 2;

public static final int EVENT_GET_ICC_RECORD_DONE = 100;

//Synthetic comment -- @@ -113,7 +115,7 @@
* Call when the IccRecords object is no longer going to be used.
*/
public void dispose() {
        mDestroyed = true;
mParentCard = null;
mFh = null;
mCi = null;
//Synthetic comment -- @@ -128,12 +130,8 @@
return adnCache;
}

    public IccCard getIccCard() {
        return mParentCard;
    }

public void registerForRecordsLoaded(Handler h, int what, Object obj) {
        if (mDestroyed) {
return;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index b55240a..866628b 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.internal.telephony.gsm.UsimServiceTable;
import com.android.internal.telephony.ims.IsimRecords;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.gsm.SIMRecords;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -110,6 +111,7 @@
protected static final int EVENT_SET_NETWORK_AUTOMATIC          = 28;
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";
//Synthetic comment -- @@ -126,7 +128,8 @@
int mCallRingDelay;
public boolean mIsTheCurrentActivePhone = true;
boolean mIsVoiceCapable = true;
    public IccRecords mIccRecords;
protected AtomicReference<IccCard> mIccCard = new AtomicReference<IccCard>();
public SmsStorageMonitor mSmsStorageMonitor;
public SmsUsageMonitor mSmsUsageMonitor;
//Synthetic comment -- @@ -251,6 +254,8 @@
// Initialize device storage and outgoing SMS usage monitors for SMSDispatchers.
mSmsStorageMonitor = new SmsStorageMonitor(this);
mSmsUsageMonitor = new SmsUsageMonitor(context);
}

public void dispose() {
//Synthetic comment -- @@ -262,6 +267,7 @@
// Dispose the SMS usage and storage monitors
mSmsStorageMonitor.dispose();
mSmsUsageMonitor.dispose();
}
}

//Synthetic comment -- @@ -269,9 +275,10 @@
mSmsStorageMonitor = null;
mSmsUsageMonitor = null;
mSMS = null;
        mIccRecords = null;
mIccCard.set(null);
mDataConnectionTracker = null;
}

/**
//Synthetic comment -- @@ -309,6 +316,10 @@
}
break;

default:
throw new RuntimeException("unexpected event not handled");
}
//Synthetic comment -- @@ -319,6 +330,9 @@
return mContext;
}

/**
* Disables the DNS check (i.e., allows "0.0.0.0").
* Useful for lab testing environment.
//Synthetic comment -- @@ -667,22 +681,26 @@

@Override
public String getIccSerialNumber() {
        return mIccRecords.iccid;
}

@Override
public boolean getIccRecordsLoaded() {
        return mIccRecords.getRecordsLoaded();
}

@Override
public boolean getMessageWaitingIndicator() {
        return mIccRecords.getVoiceMessageWaiting();
}

@Override
public boolean getCallForwardingIndicator() {
        return mIccRecords.getVoiceCallForwardingFlag();
}

/**
//Synthetic comment -- @@ -1136,7 +1154,10 @@
*/
@Override
public void setVoiceMessageWaiting(int line, int countWaiting) {
        mIccRecords.setVoiceMessageWaiting(line, countWaiting);
}

/**
//Synthetic comment -- @@ -1145,7 +1166,8 @@
*/
@Override
public UsimServiceTable getUsimServiceTable() {
        return mIccRecords.getUsimServiceTable();
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
//Synthetic comment -- @@ -1158,7 +1180,7 @@
pw.println(" mCallRingDelay=" + mCallRingDelay);
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
        pw.println(" mIccRecords=" + mIccRecords);
pw.println(" mIccCard=" + mIccCard.get());
pw.println(" mSmsStorageMonitor=" + mSmsStorageMonitor);
pw.println(" mSmsUsageMonitor=" + mSmsUsageMonitor);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e4cfb23..178addd 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
//Synthetic comment -- @@ -28,12 +29,17 @@
import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
* {@hide}
*/
public abstract class ServiceStateTracker extends Handler {

protected CommandsInterface cm;

public ServiceState ss;
protected ServiceState newSS;
//Synthetic comment -- @@ -131,7 +137,7 @@
protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED  = 39;
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
protected static final int EVENT_RADIO_ON                          = 41;


protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";

//Synthetic comment -- @@ -168,7 +174,10 @@
protected static final String REGISTRATION_DENIED_GEN  = "General";
protected static final String REGISTRATION_DENIED_AUTH = "Authentication Failure";

    public ServiceStateTracker() {
}

public boolean getDesiredPowerState() {
//Synthetic comment -- @@ -295,6 +304,10 @@
}
break;

default:
log("Unhandled message with number: " + msg.what);
break;
//Synthetic comment -- @@ -305,6 +318,7 @@
protected abstract void handlePollStateResult(int what, AsyncResult ar);
protected abstract void updateSpnDisplay();
protected abstract void setPowerStateToDesired();
protected abstract void log(String s);
protected abstract void loge(String s);

//Synthetic comment -- @@ -535,4 +549,19 @@
pw.println(" mPendingRadioPowerOffAfterDataOff=" + mPendingRadioPowerOffAfterDataOff);
pw.println(" mPendingRadioPowerOffAfterDataOffTag=" + mPendingRadioPowerOffAfterDataOffTag);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java b/src/java/com/android/internal/telephony/cdma/CDMALTEPhone.java
//Synthetic comment -- index df42515..f125484 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -67,7 +68,6 @@
public CDMALTEPhone(Context context, CommandsInterface ci, PhoneNotifier notifier) {
super(context, ci, notifier, false);
m3gppSMS = new GsmSMSDispatcher(this, mSmsStorageMonitor, mSmsUsageMonitor);
        mIccRecords.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
}

@Override
//Synthetic comment -- @@ -89,10 +89,6 @@

@Override
protected void initSstIcc() {
        mIccCard.set(UiccController.getInstance(this).getIccCard());
        mIccRecords = mIccCard.get().getIccRecords();
        // CdmaLteServiceStateTracker registers with IccCard to know
        // when the card is ready. So create mIccCard before the ServiceStateTracker
mSST = new CdmaLteServiceStateTracker(this);
}

//Synthetic comment -- @@ -101,7 +97,6 @@
synchronized(PhoneProxy.lockForRadioTechnologyChange) {
super.dispose();
m3gppSMS.dispose();
            mIccRecords.unregisterForNewSms(this);
}
}

//Synthetic comment -- @@ -205,11 +200,12 @@

@Override
public boolean updateCurrentCarrierInProvider() {
        if (mIccRecords != null) {
try {
Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
ContentValues map = new ContentValues();
                String operatorNumeric = mIccRecords.getOperatorNumeric();
map.put(Telephony.Carriers.NUMERIC, operatorNumeric);
if (DBG) log("updateCurrentCarrierInProvider from UICC: numeric=" +
operatorNumeric);
//Synthetic comment -- @@ -227,7 +223,8 @@
// return IMSI from USIM as subscriber ID.
@Override
public String getSubscriberId() {
        return mIccRecords.getIMSI();
}

@Override
//Synthetic comment -- @@ -242,12 +239,14 @@

@Override
public IsimRecords getIsimRecords() {
        return mIccRecords.getIsimRecords();
}

@Override
public String getMsisdn() {
        return mIccRecords.getMsisdnNumber();
}

@Override
//Synthetic comment -- @@ -261,6 +260,26 @@
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
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.MmiCode;
//Synthetic comment -- @@ -153,10 +154,6 @@
}

protected void initSstIcc() {
        mIccCard.set(UiccController.getInstance(this).getIccCard());
        mIccRecords = mIccCard.get().getIccRecords();
        // CdmaServiceStateTracker registers with IccCard to know
        // when the Ruim card is ready. So create mIccCard before the ServiceStateTracker
mSST = new CdmaServiceStateTracker(this);
}

//Synthetic comment -- @@ -173,7 +170,6 @@
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        registerForRuimRecordEvents();
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
//Synthetic comment -- @@ -728,7 +724,10 @@
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
        mIccRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
}

public String getVoiceMailNumber() {
//Synthetic comment -- @@ -750,7 +749,8 @@
* @hide
*/
public int getVoiceMessageCount() {
        int voicemailCount =  mIccRecords.getVoiceMessageCount();
// If mRuimRecords.getVoiceMessageCount returns zero, then there is possibility
// that phone was power cycled and would have lost the voicemail count.
// So get the count from preferences.
//Synthetic comment -- @@ -1065,6 +1065,39 @@
}
}

private void processIccRecordEvents(int eventCode) {
switch (eventCode) {
case RuimRecords.EVENT_MWI:
//Synthetic comment -- @@ -1463,14 +1496,22 @@
return mEriManager.isEriFileLoaded();
}

    private void registerForRuimRecordEvents() {
        mIccRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        mIccRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
}

    private void unregisterForRuimRecordEvents() {
        mIccRecords.unregisterForRecordsEvents(this);
        mIccRecords.unregisterForRecordsLoaded(this);
}

protected void log(String s) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1088131..07e496d 100644

//Synthetic comment -- @@ -42,6 +42,8 @@
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -112,7 +114,6 @@

p.mCM.registerForAvailable (this, DctConstants.EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, DctConstants.EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        p.mIccRecords.registerForRecordsLoaded(this, DctConstants.EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataNetworkStateChanged (this, DctConstants.EVENT_DATA_STATE_CHANGED, null);
p.mCT.registerForVoiceCallEnded (this, DctConstants.EVENT_VOICE_CALL_ENDED, null);
p.mCT.registerForVoiceCallStarted (this, DctConstants.EVENT_VOICE_CALL_STARTED, null);
//Synthetic comment -- @@ -154,7 +155,8 @@
// Unregister from all events
mPhone.mCM.unregisterForAvailable(this);
mPhone.mCM.unregisterForOffOrNotAvailable(this);
        mCdmaPhone.mIccRecords.unregisterForRecordsLoaded(this);
mPhone.mCM.unregisterForDataNetworkStateChanged(this);
mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -224,11 +226,12 @@
boolean subscriptionFromNv = (mCdmaSSM.getCdmaSubscriptionSource()
== CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_NV);

boolean allowed =
(psState == ServiceState.STATE_IN_SERVICE ||
mAutoAttachOnCreation) &&
(subscriptionFromNv ||
                            mCdmaPhone.mIccRecords.getRecordsLoaded()) &&
(mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed() ||
mPhone.getState() ==PhoneConstants.State.IDLE) &&
!roaming &&
//Synthetic comment -- @@ -243,7 +246,7 @@
reason += " - psState= " + psState;
}
if (!subscriptionFromNv &&
                    !mCdmaPhone.mIccRecords.getRecordsLoaded()) {
reason += " - RUIM not loaded";
}
if (!(mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed() ||
//Synthetic comment -- @@ -1015,6 +1018,34 @@
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
            CdmaLteUiccRecords sim = (CdmaLteUiccRecords)phone.mIccRecords;
if ((sim != null) && sim.isProvisioned()) {
mMdn = sim.getMdn();
mMin = sim.getMin();
//Synthetic comment -- @@ -344,7 +344,7 @@
if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
eriText = phone.getCdmaEriText();
} else if (ss.getState() == ServiceState.STATE_POWER_OFF) {
                    eriText = phone.mIccRecords.getServiceProviderName();
if (TextUtils.isEmpty(eriText)) {
// Sets operator alpha property by retrieving from
// build-time system property
//Synthetic comment -- @@ -359,16 +359,18 @@
ss.setOperatorAlphaLong(eriText);
}

            if (phone.getIccCard().getState() == IccCardConstants.State.READY) {
// SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
// one configfured in SIM, use operator name  from CSIM record.
boolean showSpn =
                    ((CdmaLteUiccRecords)phone.mIccRecords).getCsimSpnDisplayCondition();
int iconIndex = ss.getCdmaEriIconIndex();

if (showSpn && (iconIndex == EriInfo.ROAMING_INDICATOR_OFF) &&
                    isInHomeSidNid(ss.getSystemId(), ss.getNetworkId())) {
                    ss.setOperatorAlphaLong(phone.mIccRecords.getServiceProviderName());
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 5a4af7a..8247d54 100755

//Synthetic comment -- @@ -116,12 +116,6 @@
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
//Synthetic comment -- @@ -163,11 +157,10 @@
};

public CdmaServiceStateTracker(CDMAPhone phone) {
        super();

this.phone = phone;
cr = phone.getContext().getContentResolver();
        cm = phone.mCM;
ss = new ServiceState();
newSS = new ServiceState();
cellLoc = new CdmaCellLocation();
//Synthetic comment -- @@ -204,18 +197,17 @@
Settings.System.getUriFor(Settings.System.AUTO_TIME_ZONE), true,
mAutoTimeZoneObserver);
setSignalStrengthDefaultValues();

        mNeedToRegForRuimLoaded = true;
}

public void dispose() {
// Unregister for all events.
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        phone.getIccCard().unregisterForReady(this);
cm.unregisterForCdmaOtaProvision(this);
phone.unregisterForEriFileLoaded(this);
        phone.mIccRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
cr.unregisterContentObserver(mAutoTimeObserver);
//Synthetic comment -- @@ -287,14 +279,6 @@
// TODO: Consider calling setCurrentPreferredNetworkType as we do in GsmSST.
// cm.setCurrentPreferredNetworkType();

            // The RUIM is now ready i.e if it was locked it has been
            // unlocked. At this stage, the radio is already powered on.
            if (mNeedToRegForRuimLoaded) {
                phone.mIccRecords.registerForRecordsLoaded(this,
                        EVENT_RUIM_RECORDS_LOADED, null);
                mNeedToRegForRuimLoaded = false;
            }

if (phone.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE) {
// Subscription will be read from SIM I/O
if (DBG) log("Receive EVENT_RUIM_READY");
//Synthetic comment -- @@ -413,8 +397,19 @@
mIsMinInfoReady = true;

updateOtaspState();
                    phone.getIccCard().broadcastIccStateChangedIntent(
                            IccCardConstants.INTENT_VALUE_ICC_IMSI, null);
} else {
if (DBG) {
log("GET_CDMA_SUBSCRIPTION: error parsing cdmaSubscription params num="
//Synthetic comment -- @@ -506,8 +501,6 @@
if (!isSubscriptionFromRuim) {
// NV is ready when subscription source is NV
sendMessage(obtainMessage(EVENT_NV_READY));
        } else {
            phone.getIccCard().registerForReady(this, EVENT_RUIM_READY, null);
}
}

//Synthetic comment -- @@ -1702,6 +1695,38 @@
}

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[CdmaSST] " + s);
}
//Synthetic comment -- @@ -1734,7 +1759,6 @@
pw.println(" mSavedTimeZone=" + mSavedTimeZone);
pw.println(" mSavedTime=" + mSavedTime);
pw.println(" mSavedAtTime=" + mSavedAtTime);
        pw.println(" mNeedToRegForRuimLoaded=" + mNeedToRegForRuimLoaded);
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mCurPlmn=" + mCurPlmn);
pw.println(" mMdn=" + mMdn);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimPhoneBookInterfaceManager.java
//Synthetic comment -- index 04ee2dd..e919245 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -34,7 +35,6 @@

public RuimPhoneBookInterfaceManager(CDMAPhone phone) {
super(phone);
        adnCache = phone.mIccRecords.getAdnCache();
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -61,8 +61,12 @@
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE, status);

            phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            waitForResult(status);
}

return recordSize;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e257fb6..d3e04bd 100755

//Synthetic comment -- @@ -200,7 +200,7 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed) {
loge("Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
//Synthetic comment -- @@ -318,18 +318,20 @@
// One record loaded successfully or failed, In either case
// we need to update the recordsToLoad count
recordsToLoad -= 1;
        if (DBG) log("RuimRecords:onRecordLoaded " + recordsToLoad + " requested: " + recordsRequested);

if (recordsToLoad == 0 && recordsRequested == true) {
onAllRecordsLoaded();
} else if (recordsToLoad < 0) {
            loge("RuimRecords: recordsToLoad <0, programmer error suspected");
recordsToLoad = 0;
}
}

@Override
protected void onAllRecordsLoaded() {
// Further records that can be inserted are Operator/OEM dependent

String operator = getRUIMOperatorNumeric();
//Synthetic comment -- @@ -349,13 +351,6 @@

@Override
public void onReady() {
        /* broadcast intent ICC_READY here so that we can make sure
          READY is sent before IMSI ready
        */

        mParentCard.broadcastIccStateChangedIntent(
                IccCardConstants.INTENT_VALUE_ICC_READY, null);

fetchRuimRecords();

mCi.getCDMASubscription(obtainMessage(EVENT_GET_CDMA_SUBSCRIPTION_DONE));
//Synthetic comment -- @@ -365,7 +360,7 @@
private void fetchRuimRecords() {
recordsRequested = true;

        Log.v(LOG_TAG, "RuimRecords:fetchRuimRecords " + recordsToLoad);

mCi.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;
//Synthetic comment -- @@ -374,7 +369,7 @@
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

        log("RuimRecords:fetchRuimRecords " + recordsToLoad + " requested: " + recordsRequested);
// Further records that can be inserted are Operator/OEM dependent
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 8c5368e..9b1cafc 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
//Synthetic comment -- @@ -140,11 +141,10 @@
}

mCM.setPhoneType(PhoneConstants.PHONE_TYPE_GSM);
        mIccCard.set(UiccController.getInstance(this).getIccCard());
        mIccRecords = mIccCard.get().getIccRecords();
mCT = new GsmCallTracker(this);
mSST = new GsmServiceStateTracker (this);
mSMS = new GsmSMSDispatcher(this, mSmsStorageMonitor, mSmsUsageMonitor);
mDataConnectionTracker = new GsmDataConnectionTracker (this);
if (!unitTestMode) {
mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
//Synthetic comment -- @@ -153,7 +153,6 @@
}

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        registerForSimRecordEvents();
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -797,7 +796,8 @@

public String getVoiceMailNumber() {
// Read from the SIM. If its null, try reading from the shared preference area.
        String number = mIccRecords.getVoiceMailNumber();
if (TextUtils.isEmpty(number)) {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);
//Synthetic comment -- @@ -819,8 +819,9 @@

public String getVoiceMailAlphaTag() {
String ret;

        ret = mIccRecords.getVoiceMailAlphaTag();

if (ret == null || ret.length() == 0) {
return mContext.getText(
//Synthetic comment -- @@ -853,24 +854,31 @@
}

public String getSubscriberId() {
        return mIccRecords.getIMSI();
}

public String getLine1Number() {
        return mIccRecords.getMsisdnNumber();
}

@Override
public String getMsisdn() {
        return mIccRecords.getMsisdnNumber();
}

public String getLine1AlphaTag() {
        return mIccRecords.getMsisdnAlphaTag();
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
        mIccRecords.setMsisdnNumber(alphaTag, number, onComplete);
}

public void setVoiceMailNumber(String alphaTag,
//Synthetic comment -- @@ -880,7 +888,10 @@
Message resp;
mVmNumber = voiceMailNumber;
resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
        mIccRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
}

private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
//Synthetic comment -- @@ -1248,8 +1259,9 @@

case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    mIccRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
if (onComplete != null) {
//Synthetic comment -- @@ -1322,12 +1334,41 @@
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
//Synthetic comment -- @@ -1339,11 +1380,12 @@
* @return true for success; false otherwise.
*/
boolean updateCurrentCarrierInProvider() {
        if (mIccRecords != null) {
try {
Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
ContentValues map = new ContentValues();
                map.put(Telephony.Carriers.NUMERIC, mIccRecords.getOperatorNumeric());
mContext.getContentResolver().insert(uri, map);
return true;
} catch (SQLException e) {
//Synthetic comment -- @@ -1405,16 +1447,19 @@
}

private void handleCfuQueryResult(CallForwardInfo[] infos) {
        if (infos == null || infos.length == 0) {
            // Assume the default is not active
            // Set unconditional CFF in SIM to false
            mIccRecords.setVoiceCallForwardingFlag(1, false);
        } else {
            for (int i = 0, s = infos.length; i < s; i++) {
                if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                    mIccRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
                    // should only have the one
                    break;
}
}
}
//Synthetic comment -- @@ -1473,22 +1518,31 @@
}

public boolean isCspPlmnEnabled() {
        return mIccRecords.isCspPlmnEnabled();
}

private void registerForSimRecordEvents() {
        mIccRecords.registerForNetworkSelectionModeAutomatic(
this, EVENT_SET_NETWORK_AUTOMATIC, null);
        mIccRecords.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
        mIccRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}

private void unregisterForSimRecordEvents() {
        mIccRecords.unregisterForNetworkSelectionModeAutomatic(this);
        mIccRecords.unregisterForNewSms(this);
        mIccRecords.unregisterForRecordsEvents(this);
        mIccRecords.unregisterForRecordsLoaded(this);
}

@Override
//Synthetic comment -- @@ -1505,4 +1559,8 @@
if (VDBG) pw.println(" mImeiSv=" + mImeiSv);
pw.println(" mVmNumber=" + mVmNumber);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 016513c..adce14d 100644

//Synthetic comment -- @@ -60,6 +60,8 @@
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -190,7 +192,6 @@
p.mCM.registerForAvailable (this, DctConstants.EVENT_RADIO_AVAILABLE, null);
p.mCM.registerForOffOrNotAvailable(this, DctConstants.EVENT_RADIO_OFF_OR_NOT_AVAILABLE,
null);
        p.mIccRecords.registerForRecordsLoaded(this, DctConstants.EVENT_RECORDS_LOADED, null);
p.mCM.registerForDataNetworkStateChanged (this, DctConstants.EVENT_DATA_STATE_CHANGED,
null);
p.getCallTracker().registerForVoiceCallEnded (this, DctConstants.EVENT_VOICE_CALL_ENDED,
//Synthetic comment -- @@ -235,7 +236,8 @@
//Unregister for all events
mPhone.mCM.unregisterForAvailable(this);
mPhone.mCM.unregisterForOffOrNotAvailable(this);
        mPhone.mIccRecords.unregisterForRecordsLoaded(this);
mPhone.mCM.unregisterForDataNetworkStateChanged(this);
mPhone.getCallTracker().unregisterForVoiceCallEnded(this);
mPhone.getCallTracker().unregisterForVoiceCallStarted(this);
//Synthetic comment -- @@ -637,10 +639,12 @@

int gprsState = mPhone.getServiceStateTracker().getCurrentDataConnectionState();
boolean desiredPowerState = mPhone.getServiceStateTracker().getDesiredPowerState();

boolean allowed =
(gprsState == ServiceState.STATE_IN_SERVICE || mAutoAttachOnCreation) &&
                    mPhone.mIccRecords.getRecordsLoaded() &&
(mPhone.getState() == PhoneConstants.State.IDLE ||
mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()) &&
internalDataEnabled &&
//Synthetic comment -- @@ -652,7 +656,7 @@
if (!((gprsState == ServiceState.STATE_IN_SERVICE) || mAutoAttachOnCreation)) {
reason += " - gprs= " + gprsState;
}
            if (!mPhone.mIccRecords.getRecordsLoaded()) reason += " - SIM not loaded";
if (mPhone.getState() != PhoneConstants.State.IDLE &&
!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()) {
reason += " - PhoneState= " + mPhone.getState();
//Synthetic comment -- @@ -1971,7 +1975,8 @@
log("onRadioAvailable: We're on the simulator; assuming data is connected");
}

        if (mPhone.mIccRecords.getRecordsLoaded()) {
notifyOffApnsOfAvailability(null);
}

//Synthetic comment -- @@ -2267,7 +2272,8 @@
*/
private void createAllApnList() {
mAllApns = new ArrayList<ApnSetting>();
        String operator = mPhone.mIccRecords.getOperatorNumeric();
if (operator != null) {
String selection = "numeric = '" + operator + "'";
// query only enabled apn.
//Synthetic comment -- @@ -2383,7 +2389,8 @@
}
}

        String operator = mPhone.mIccRecords.getOperatorNumeric();
int networkType = mPhone.getServiceState().getNetworkType();

if (canSetPreferApn && mPreferredApn != null &&
//Synthetic comment -- @@ -2619,6 +2626,34 @@
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
                    phone.mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

onSetComplete(ar);
//Synthetic comment -- @@ -1203,7 +1206,10 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            phone.mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

return TextUtils.replace(template, sources, destinations);
//Synthetic comment -- @@ -1228,7 +1234,10 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                phone.mIccRecords.setVoiceCallForwardingFlag(1, false);
} else {

SpannableStringBuilder tb = new SpannableStringBuilder();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 808ec2b..046d220 100644

//Synthetic comment -- @@ -125,12 +125,6 @@
long mSavedTime;
long mSavedAtTime;

    /**
     * We can't register for SIM_RECORDS_LOADED immediately because the
     * SIMRecords object may not be instantiated yet.
     */
    private boolean mNeedToRegForSimLoaded;

/** Started the recheck process after finding gprs should registered but not. */
private boolean mStartedGprsRegCheck = false;

//Synthetic comment -- @@ -193,10 +187,9 @@
};

public GsmServiceStateTracker(GSMPhone phone) {
        super();

this.phone = phone;
        cm = phone.mCM;
ss = new ServiceState();
newSS = new ServiceState();
cellLoc = new GsmCellLocation();
//Synthetic comment -- @@ -214,7 +207,6 @@
cm.setOnNITZTime(this, EVENT_NITZ_TIME, null);
cm.setOnSignalStrengthUpdate(this, EVENT_SIGNAL_STRENGTH_UPDATE, null);
cm.setOnRestrictedStateChanged(this, EVENT_RESTRICTED_STATE_CHANGED, null);
        phone.getIccCard().registerForReady(this, EVENT_SIM_READY, null);

// system setting property AIRPLANE_MODE_ON is set in Settings.
int airplaneMode = Settings.System.getInt(
//Synthetic comment -- @@ -231,7 +223,6 @@
mAutoTimeZoneObserver);

setSignalStrengthDefaultValues();
        mNeedToRegForSimLoaded = true;

// Monitor locale change
IntentFilter filter = new IntentFilter();
//Synthetic comment -- @@ -243,12 +234,13 @@
}

public void dispose() {
// Unregister for all events.
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        phone.getIccCard().unregisterForReady(this);
        phone.mIccRecords.unregisterForRecordsLoaded(this);
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -286,15 +278,6 @@
// Set the network type, in case the radio does not restore it.
cm.setCurrentPreferredNetworkType();

                // The SIM is now ready i.e if it was locked
                // it has been unlocked. At this stage, the radio is already
                // powered on.
                if (mNeedToRegForSimLoaded) {
                    phone.mIccRecords.registerForRecordsLoaded(this,
                            EVENT_SIM_RECORDS_LOADED, null);
                    mNeedToRegForSimLoaded = false;
                }

boolean skipRestoringSelection = phone.getContext().getResources().getBoolean(
com.android.internal.R.bool.skip_restoring_network_selection);

//Synthetic comment -- @@ -496,8 +479,11 @@
}

protected void updateSpnDisplay() {
        int rule = phone.mIccRecords.getDisplayRule(ss.getOperatorNumeric());
        String spn = phone.mIccRecords.getServiceProviderName();
String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN
//Synthetic comment -- @@ -1150,7 +1136,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (phone.getIccCard().getState() == IccCardConstants.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1677,6 +1663,35 @@
}

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[GsmSST] " + s);
}
//Synthetic comment -- @@ -1716,7 +1731,6 @@
pw.println(" mSavedTimeZone=" + mSavedTimeZone);
pw.println(" mSavedTime=" + mSavedTime);
pw.println(" mSavedAtTime=" + mSavedAtTime);
        pw.println(" mNeedToRegForSimLoaded=" + mNeedToRegForSimLoaded);
pw.println(" mStartedGprsRegCheck=" + mStartedGprsRegCheck);
pw.println(" mReportedGprsNoReg=" + mReportedGprsNoReg);
pw.println(" mNotification=" + mNotification);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 1ec3ea7..d5099f7 100755

//Synthetic comment -- @@ -522,7 +522,7 @@

boolean isRecordLoadResponse = false;

        if (mDestroyed) {
loge("Received message " + msg + "[" + msg.what + "] " +
" while being destroyed. Ignoring.");
return;
//Synthetic comment -- @@ -1300,12 +1300,6 @@

@Override
public void onReady() {
        /* broadcast intent SIM_READY here so that we can make sure
          READY is sent before IMSI ready
        */
        mParentCard.broadcastIccStateChangedIntent(
                IccCardConstants.INTENT_VALUE_ICC_READY, null);

fetchSimRecords();
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimPhoneBookInterfaceManager.java
//Synthetic comment -- index 35ba0d1..37f9a4f 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.IccPhoneBookInterfaceManager;

/**
//Synthetic comment -- @@ -34,7 +35,6 @@

public SimPhoneBookInterfaceManager(GSMPhone phone) {
super(phone);
        adnCache = phone.mIccRecords.getAdnCache();
//NOTE service "simphonebook" added by IccSmsInterfaceManagerProxy
}

//Synthetic comment -- @@ -61,8 +61,11 @@
AtomicBoolean status = new AtomicBoolean(false);
Message response = mBaseHandler.obtainMessage(EVENT_GET_SIZE_DONE, status);

            phone.getIccFileHandler().getEFLinearRecordSize(efid, response);
            waitForResult(status);
}

return recordSize;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index ff64486..b0a6080 100755

//Synthetic comment -- @@ -462,4 +462,8 @@
notifyPhoneStateChanged();
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 5961efd..ab7d561 100644

//Synthetic comment -- @@ -16,78 +16,151 @@

package com.android.internal.telephony.uicc;

import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.cdma.CDMALTEPhone;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;

import android.util.Log;

/* This class is responsible for keeping all knowledge about
* ICCs in the system. It is also used as API to get appropriate
* applications to pass them to phone and service trackers.
*/
public class UiccController {
private static final boolean DBG = true;
private static final String LOG_TAG = "RIL_UiccController";

private static UiccController mInstance;

private PhoneBase mCurrentPhone;
    private boolean mIsCurrentCard3gpp;
private IccCard mIccCard;

public static synchronized UiccController getInstance(PhoneBase phone) {
if (mInstance == null) {
mInstance = new UiccController(phone);
        } else {
mInstance.setNewPhone(phone);
}
return mInstance;
}

    public IccCard getIccCard() {
return mIccCard;
}

private UiccController(PhoneBase phone) {
if (DBG) log("Creating UiccController");
setNewPhone(phone);
}

    private void setNewPhone(PhoneBase phone) {
        mCurrentPhone = phone;
        if (phone instanceof GSMPhone) {
            if (DBG) log("New phone is GSMPhone");
            updateCurrentCard(IccCard.CARD_IS_3GPP);
        } else if (phone instanceof CDMALTEPhone){
            if (DBG) log("New phone type is CDMALTEPhone");
            updateCurrentCard(IccCard.CARD_IS_3GPP);
        } else if (phone instanceof CDMAPhone){
            if (DBG) log("New phone type is CDMAPhone");
            updateCurrentCard(IccCard.CARD_IS_NOT_3GPP);
        } else {
            Log.e(LOG_TAG, "Unhandled phone type. Critical error!");
        }
    }

    private void updateCurrentCard(boolean isNewCard3gpp) {
        if (mIsCurrentCard3gpp == isNewCard3gpp && mIccCard != null) {
return;
}

        if (mIccCard != null) {
mIccCard.dispose();
mIccCard = null;
}

        mIsCurrentCard3gpp = isNewCard3gpp;
        mIccCard = new IccCard(mCurrentPhone, mCurrentPhone.getPhoneName(),
                isNewCard3gpp, DBG);
}

private void log(String string) {
Log.d(LOG_TAG, string);
}
}
\ No newline at end of file







