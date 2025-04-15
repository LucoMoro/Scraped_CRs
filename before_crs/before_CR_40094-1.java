/*Telephony: Remove CdmaLteUicc objects

Change-Id:Ia12aaa2c64e06632e02a1c15724b4c11cebaf19d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CsimFileHandler.java b/src/java/com/android/internal/telephony/CsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..7006051

//Synthetic comment -- @@ -0,0 +1,72 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 1557304..9d8c201 100644

//Synthetic comment -- @@ -169,7 +169,7 @@
*/
public String getServiceProviderName ();
public State getIccCardState();
    public boolean isApplicationOnIcc(IccCardApplication.AppType type);

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
public class IccCardApplication {
public enum AppType{
APPTYPE_UNKNOWN,
APPTYPE_SIM,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index 409732a..26c0a7a 100644

//Synthetic comment -- @@ -31,8 +31,11 @@
import android.telephony.TelephonyManager;

import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.gsm.SIMRecords;
import com.android.internal.telephony.uicc.UiccController;

import static com.android.internal.telephony.Phone.CDMA_SUBSCRIPTION_NV;
//Synthetic comment -- @@ -42,7 +45,8 @@
* The Phone App UI and the external world assumes that there is only one icc card,
* and one icc application available at a time. But the Uicc Controller can handle
* multiple instances of icc objects. This class implements the icc interface to expose
 * the  first application on the first icc card, so that external apps wont break.
*/

public class IccCardProxy extends Handler implements IccCard {
//Synthetic comment -- @@ -71,9 +75,9 @@
private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
private UiccController mUiccController = null;
private UiccCard mUiccCard = null;
private IccRecords mIccRecords = null;
private CdmaSubscriptionSourceManager mCdmaSSM = null;
    private boolean mFirstRun = true;
private boolean mRadioOn = false;
private boolean mCdmaSubscriptionFromNv = false;
private boolean mIsMultimodeCdmaPhone =
//Synthetic comment -- @@ -81,8 +85,10 @@
private boolean mQuietMode = false; // when set to true IccCardProxy will not broadcast
// ACTION_SIM_STATE_CHANGED intents
private boolean mInitialized = false;

    public IccCardProxy(PhoneBase p, Context context, CommandsInterface ci) {
this.mContext = context;
this.mCi = ci;
mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(context,
//Synthetic comment -- @@ -91,10 +97,11 @@
mUiccController.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
ci.registerForOn(this,EVENT_RADIO_ON, null);
ci.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_UNAVAILABLE, null);
        SystemProperties.set(PROPERTY_SIM_STATE, "ABSENT");
}

public void dispose() {
//Cleanup icc references
mUiccController.unregisterForIccChanged(this);
mUiccController = null;
//Synthetic comment -- @@ -114,27 +121,50 @@
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
//Synthetic comment -- @@ -151,18 +181,17 @@
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
//Synthetic comment -- @@ -171,81 +200,105 @@
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
//Synthetic comment -- @@ -262,8 +315,8 @@
}

public void changeIccFdnPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.changeIccFdnPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -273,8 +326,8 @@
}

public void changeIccLockPassword(String oldPassword, String newPassword, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.changeIccLockPassword(oldPassword, newPassword, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -284,24 +337,24 @@
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
//Synthetic comment -- @@ -310,43 +363,32 @@
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
//Synthetic comment -- @@ -417,8 +459,8 @@
}

public void setIccFdnEnabled(boolean enabled, String password, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.setIccFdnEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -428,8 +470,8 @@
}

public void setIccLockEnabled(boolean enabled, String password, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.setIccLockEnabled(enabled, password, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -442,8 +484,8 @@
* Use invokeDepersonalization from PhoneBase class instead.
*/
public void supplyNetworkDepersonalization(String pin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyNetworkDepersonalization(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("CommandsInterface is not set.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -453,8 +495,8 @@
}

public void supplyPin(String pin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPin(pin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -464,8 +506,8 @@
}

public void supplyPin2(String pin2, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPin2(pin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -475,8 +517,8 @@
}

public void supplyPuk(String puk, String newPin, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPuk(puk, newPin, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -486,8 +528,8 @@
}

public void supplyPuk2(String puk2, String newPin2, Message onComplete) {
        if (mUiccCard != null) {
            mUiccCard.supplyPuk2(puk2, newPin2, onComplete);
} else if (onComplete != null) {
Exception e = new RuntimeException("ICC card is absent.");
AsyncResult.forMessage(onComplete).exception = e;
//Synthetic comment -- @@ -496,36 +538,26 @@
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
//Synthetic comment -- @@ -541,7 +573,11 @@
return false;
}

    protected void log(String s) {
Log.d(LOG_TAG, s);
}
}








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
//Synthetic comment -- index 89ec562..98ab17b 100644

//Synthetic comment -- @@ -91,8 +91,8 @@

// member variables
protected final CommandsInterface mCi;
    protected final UiccCard mParentCard;
    protected String mAid;

static class LoadLinearFixedContext {

//Synthetic comment -- @@ -122,8 +122,8 @@
/**
* Default constructor
*/
    protected IccFileHandler(UiccCard card, String aid, CommandsInterface ci) {
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
//Synthetic comment -- index d542658..67bdf9e 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
protected Context mContext;
protected CommandsInterface mCi;
protected IccFileHandler mFh;
    protected UiccCard mParentCard;

protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
protected RegistrantList mImsiReadyRegistrants = new RegistrantList();
//Synthetic comment -- @@ -106,11 +106,11 @@
}

// ***** Constructor
    public IccRecords(UiccCard card, Context c, CommandsInterface ci) {
mContext = c;
mCi = ci;
        mFh = card.getIccFileHandler();
        mParentCard = card;
}

/**
//Synthetic comment -- @@ -118,13 +118,12 @@
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index f14c6b8..1457152 100644

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
    protected AtomicReference<UiccCard> mUiccCard = new AtomicReference<UiccCard>();
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
        mUiccCard.set(null);
mDataConnectionTracker = null;
mUiccController = null;
}
//Synthetic comment -- @@ -648,9 +650,9 @@
* Retrieves the IccFileHandler of the Phone instance
*/
public IccFileHandler getIccFileHandler(){
        UiccCard uiccCard = mUiccCard.get();
        if (uiccCard == null) return null;
        return uiccCard.getIccFileHandler();
}

/*
//Synthetic comment -- @@ -680,6 +682,16 @@
//throw new Exception("getIccCard Shouldn't be called from PhoneBase");
}

@Override
public String getIccSerialNumber() {
IccRecords r = mIccRecords.get();
//Synthetic comment -- @@ -1182,7 +1194,7 @@
pw.println(" mIsTheCurrentActivePhone=" + mIsTheCurrentActivePhone);
pw.println(" mIsVoiceCapable=" + mIsVoiceCapable);
pw.println(" mIccRecords=" + mIccRecords.get());
        pw.println(" mUiccCard=" + mUiccCard.get());
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
//Synthetic comment -- index a2d2806..3387840 100644

//Synthetic comment -- @@ -77,7 +77,13 @@
mCommandsInterface.registerForOn(this, EVENT_RADIO_ON, null);
mCommandsInterface.registerForVoiceRadioTechChanged(
this, EVENT_VOICE_RADIO_TECH_CHANGED, null);
        mIccCardProxy = new IccCardProxy(phone, phone.getContext(), mCommandsInterface);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index b14f6c8..b1c14ef 100644

//Synthetic comment -- @@ -50,6 +50,7 @@

import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.IccRefreshResponse;
//Synthetic comment -- @@ -2985,24 +2986,23 @@

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
//Synthetic comment -- @@ -3011,7 +3011,7 @@
ca.pin1_replaced  = p.readInt();
ca.pin1           = ca.PinStateFromRILInt(p.readInt());
ca.pin2           = ca.PinStateFromRILInt(p.readInt());
            status.addApplication(ca);
}
return status;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 2990457..1cdb693 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
//Synthetic comment -- @@ -38,7 +39,7 @@

protected CommandsInterface cm;
protected UiccController mUiccController = null;
    protected UiccCard mUiccCard = null;
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
//Synthetic comment -- index afc37aa..0724020 100644

//Synthetic comment -- @@ -35,14 +35,17 @@
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
//Synthetic comment -- @@ -55,134 +58,135 @@
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
//Synthetic comment -- @@ -193,7 +197,7 @@

mAbsentRegistrants.add(r);

        if (getState() == State.ABSENT) {
r.notifyRegistrant();
}
}
//Synthetic comment -- @@ -202,326 +206,6 @@
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
//Synthetic comment -- @@ -537,9 +221,9 @@
@Override
public void onClick(DialogInterface dialog, int which) {
if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (mDbg) log("Reboot due to SIM swap");
                    PowerManager pm = (PowerManager) mPhone.getContext()
                    .getSystemService(Context.POWER_SERVICE);
pm.reboot("SIM is added.");
}
}
//Synthetic comment -- @@ -554,7 +238,7 @@
r.getString(R.string.sim_removed_message);
String buttonTxt = r.getString(R.string.sim_restart_button);

        AlertDialog dialog = new AlertDialog.Builder(mPhone.getContext())
.setTitle(title)
.setMessage(message)
.setPositiveButton(buttonTxt, listener)
//Synthetic comment -- @@ -563,133 +247,16 @@
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
//Synthetic comment -- @@ -697,194 +264,59 @@
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
new file mode 100644
//Synthetic comment -- index 0000000..8de1dfc7

//Synthetic comment -- @@ -0,0 +1,543 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..ae4c05e

//Synthetic comment -- @@ -0,0 +1,88 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index dc6b9f0..4934fd7 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;


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
            IccFileHandler fh, UiccCard ic) {
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
            Context context, IccFileHandler fh, UiccCard ic) {
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
//Synthetic comment -- index c8afe4b..918725f 100755

//Synthetic comment -- @@ -65,7 +65,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.uicc.UiccController;

//Synthetic comment -- @@ -808,6 +810,10 @@
return null;
}

/**
* Notify any interested party of a Phone state change  {@link PhoneConstants.State}
*/
//Synthetic comment -- @@ -1072,29 +1078,26 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        UiccCard c = mUiccCard.get();
        if (c != newUiccCard) {
            if (c != null) {
log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForRuimRecordEvents();
                    if (mRuimPhoneBookInterfaceManager != null) {
                        mRuimPhoneBookInterfaceManager.updateIccRecords(null);
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
//Synthetic comment -- index b986254..54a157c 100644

//Synthetic comment -- @@ -43,12 +43,12 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UiccCard;
import com.android.internal.util.AsyncChannel;
import com.android.internal.telephony.RILConstants;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -1023,11 +1023,7 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();
        IccRecords newIccRecords = null;
        if (newUiccCard != null) {
            newIccRecords = newUiccCard.getIccRecords();
        }

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -1037,7 +1033,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New card found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index b173035..03e3ab6 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.util.Log;
import android.util.EventLog;

import com.android.internal.telephony.gsm.GsmDataConnectionTracker;
import com.android.internal.telephony.IccCardConstants;

//Synthetic comment -- @@ -65,12 +66,12 @@
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
//Synthetic comment -- @@ -359,12 +360,12 @@
ss.setOperatorAlphaLong(eriText);
}

            if (mUiccCard != null && mUiccCard.getState() == IccCardConstants.State.READY &&
mIccRecords != null) {
// SIM is found on the device. If ERI roaming is OFF, and SID/NID matches
// one configfured in SIM, use operator name  from CSIM record.
boolean showSpn =
                    ((CdmaLteUiccRecords)mIccRecords).getCsimSpnDisplayCondition();
int iconIndex = ss.getCdmaEriIconIndex();

if (showSpn && (iconIndex == EriInfo.ROAMING_INDICATOR_OFF) &&








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccFileHandler.java
deleted file mode 100644
//Synthetic comment -- index 1b34279..0000000

//Synthetic comment -- @@ -1,80 +0,0 @@
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
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.UiccCard;

import android.os.Message;

/**
 * {@hide}
 */
public final class CdmaLteUiccFileHandler extends IccFileHandler {
    static final String LOG_TAG = "CDMA";

    public CdmaLteUiccFileHandler(UiccCard card, String aid, CommandsInterface ci) {
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
//Synthetic comment -- index 2f2c288..0000000

//Synthetic comment -- @@ -1,451 +0,0 @@
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
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.UiccCard;
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

    public CdmaLteUiccRecords(UiccCard card, Context c, CommandsInterface ci) {
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
//Synthetic comment -- index e579336..8bdabe6 100755

//Synthetic comment -- @@ -29,7 +29,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;
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
        if (mUiccCard != null) {mUiccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -1698,24 +1700,25 @@
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
//Synthetic comment -- index 965db6d..3bf9c6e 100644

//Synthetic comment -- @@ -20,17 +20,8 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccFileTypeMismatch;
import com.android.internal.telephony.IccIoResult;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.UiccCard;

import java.util.ArrayList;

/**
* {@hide}
//Synthetic comment -- @@ -41,8 +32,8 @@
//***** Instance Variables

//***** Constructor
    public RuimFileHandler(UiccCard card, String aid, CommandsInterface ci) {
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
//Synthetic comment -- index 1cd0c68..b99011e 100755

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
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.UiccCard;

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


    public RuimRecords(UiccCard card, Context c, CommandsInterface ci) {
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
//Synthetic comment -- @@ -192,6 +223,202 @@
return mImsi.substring(0, 3 + MccTable.smallestDigitsMccForMnc(mcc));
}

@Override
public void handleMessage(Message msg) {
AsyncResult ar;
//Synthetic comment -- @@ -207,9 +434,9 @@
}

try { switch (msg.what) {
            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;

case EVENT_GET_DEVICE_IDENTITY_DONE:
log("Event EVENT_GET_DEVICE_IDENTITY_DONE Received");
//Synthetic comment -- @@ -302,6 +529,9 @@
}
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing RUIM record", exc);
//Synthetic comment -- @@ -313,6 +543,55 @@
}
}

@Override
protected void onRecordLoaded() {
// One record loaded successfully or failed, In either case
//Synthetic comment -- @@ -343,6 +622,8 @@
SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
MccTable.countryCodeForMcc(Integer.parseInt(mImsi.substring(0,3))));
}
recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
}
//Synthetic comment -- @@ -360,13 +641,43 @@

if (DBG) log("fetchRuimRecords " + recordsToLoad);

        mCi.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;

if (DBG) log("fetchRuimRecords " + recordsToLoad + " requested: " + recordsRequested);
// Further records that can be inserted are Operator/OEM dependent
}
//Synthetic comment -- @@ -383,6 +694,29 @@
}

@Override
public void setVoiceMessageWaiting(int line, int countWaiting) {
if (line != 1) {
// only profile 1 is supported
//Synthetic comment -- @@ -409,7 +743,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentCard.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -443,6 +777,25 @@
}
}

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[RuimRecords] " + s);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c0f9cc7..4d042bb 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import static com.android.internal.telephony.CommandsInterface.SERVICE_CLASS_VOICE;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_BASEBAND_VERSION;

import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
//Synthetic comment -- @@ -71,6 +72,7 @@
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -711,7 +713,8 @@

// Only look at the Network portion for mmi
String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this, mUiccCard.get());
if (LOCAL_DEBUG) Log.d(LOG_TAG,
"dialing w/ mmi '" + mmi + "'...");

//Synthetic comment -- @@ -730,7 +733,7 @@
}

public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, mUiccCard.get());

if (mmi != null && mmi.isPinCommand()) {
mPendingMMIs.add(mmi);
//Synthetic comment -- @@ -743,7 +746,7 @@
}

public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this, mUiccCard.get());
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.sendUssd(ussdMessge);
//Synthetic comment -- @@ -1073,6 +1076,10 @@
mDataConnectionTracker.setDataOnRoamingEnabled(enable);
}

/**
* Removes the given MMI from the pending list and notifies
* registrants that it is complete.
//Synthetic comment -- @@ -1141,7 +1148,7 @@
mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
isUssdRequest,
GSMPhone.this,
                                                   mUiccCard.get());
onNetworkInitiatedUssd(mmi);
}
}
//Synthetic comment -- @@ -1341,23 +1348,24 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();

        UiccCard c = mUiccCard.get();
        if (c != newUiccCard) {
            if (c != null) {
if (LOCAL_DEBUG) log("Removing stale icc objects.");
if (mIccRecords.get() != null) {
unregisterForSimRecordEvents();
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 9fc94a5..2572406 100644

//Synthetic comment -- @@ -376,7 +376,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getIccCard().getState() != IccCardConstants.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.mRestrictedState.isCsRestricted()) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 80e0320..58de5f8 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.UiccCard;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
//Synthetic comment -- @@ -2632,11 +2633,7 @@
return;
}

        UiccCard newUiccCard = mUiccController.getUiccCard();
        IccRecords newIccRecords = null;
        if (newUiccCard != null) {
            newIccRecords = newUiccCard.getIccRecords();
        }

IccRecords r = mIccRecords.get();
if (r != newIccRecords) {
//Synthetic comment -- @@ -2646,7 +2643,7 @@
mIccRecords.set(null);
}
if (newIccRecords != null) {
                log("New card found");
mIccRecords.set(newIccRecords);
newIccRecords.registerForRecordsLoaded(
this, DctConstants.EVENT_RECORDS_LOADED, null);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index b725c69..73d44eb 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Context;
import com.android.internal.telephony.*;

import android.os.*;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -110,7 +111,7 @@

GSMPhone phone;
Context context;
    UiccCard mUiccCard;
IccRecords mIccRecords;

String action;              // One of ACTION_*
//Synthetic comment -- @@ -175,7 +176,7 @@
*/

static GsmMmiCode
    newFromDialString(String dialString, GSMPhone phone, UiccCard card) {
Matcher m;
GsmMmiCode ret = null;

//Synthetic comment -- @@ -183,7 +184,7 @@

// Is this formatted like a standard supplementary service code?
if (m.matches()) {
            ret = new GsmMmiCode(phone, card);
ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
//Synthetic comment -- @@ -198,14 +199,14 @@
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet
// (up to the maximum defined in 3GPP TS 24.080 [10]), followed by #SEND".

            ret = new GsmMmiCode(phone, card);
ret.poundString = dialString;
} else if (isTwoDigitShortCode(phone.getContext(), dialString)) {
//Is a country-specific exception to short codes as defined in TS 22.030, 6.5.3.2
ret = null;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
            ret = new GsmMmiCode(phone, card);
ret.dialingNumber = dialString;
}

//Synthetic comment -- @@ -214,10 +215,10 @@

static GsmMmiCode
newNetworkInitiatedUssd (String ussdMessage,
                                boolean isUssdRequest, GSMPhone phone, UiccCard card) {
GsmMmiCode ret;

        ret = new GsmMmiCode(phone, card);

ret.message = ussdMessage;
ret.isUssdRequest = isUssdRequest;
//Synthetic comment -- @@ -233,8 +234,8 @@
return ret;
}

    static GsmMmiCode newFromUssdUserInput(String ussdMessge, GSMPhone phone, UiccCard card) {
        GsmMmiCode ret = new GsmMmiCode(phone, card);

ret.message = ussdMessge;
ret.state = State.PENDING;
//Synthetic comment -- @@ -385,15 +386,15 @@

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
}

//Synthetic comment -- @@ -771,7 +772,7 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else if (sc.equals(SC_PIN) &&
                               mUiccCard.getState() == IccCardConstants.State.PUK_REQUIRED ) {
// Sim is puk-locked
handlePasswordError(com.android.internal.R.string.needPuk);
} else {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 13f637a..bc997aa 100644

//Synthetic comment -- @@ -31,6 +31,9 @@
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UiccCard;

import android.app.AlarmManager;
import android.app.Notification;
//Synthetic comment -- @@ -188,7 +191,7 @@
};

public GsmServiceStateTracker(GSMPhone phone) {
        super(phone, phone.mCM);

this.phone = phone;
ss = new ServiceState();
//Synthetic comment -- @@ -240,7 +243,7 @@
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
        if (mUiccCard != null) {mUiccCard.unregisterForReady(this);}
if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
//Synthetic comment -- @@ -1137,7 +1140,7 @@
((state & RILConstants.RIL_RESTRICTED_STATE_CS_EMERGENCY) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//ignore the normal call and data restricted state before SIM READY
            if (mUiccCard.getState() == IccCardConstants.State.READY) {
newRs.setCsNormalRestricted(
((state & RILConstants.RIL_RESTRICTED_STATE_CS_NORMAL) != 0) ||
((state & RILConstants.RIL_RESTRICTED_STATE_CS_ALL) != 0) );
//Synthetic comment -- @@ -1669,23 +1672,24 @@
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
//Synthetic comment -- index 0637e8f..46992e6 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
import android.util.Log;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.UiccCard;

/**
* {@hide}
//Synthetic comment -- @@ -36,8 +34,8 @@

//***** Constructor

    public SIMFileHandler(UiccCard card, String aid, CommandsInterface ci) {
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
//Synthetic comment -- index 4a9b047..983f3c1 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.UiccCard;

import java.util.ArrayList;

//Synthetic comment -- @@ -124,9 +124,9 @@

// ***** Event Constants

    private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
    protected static final int EVENT_GET_IMSI_DONE = 3;
    protected static final int EVENT_GET_ICCID_DONE = 4;
private static final int EVENT_GET_MBI_DONE = 5;
private static final int EVENT_GET_MBDN_DONE = 6;
private static final int EVENT_GET_MWIS_DONE = 7;
//Synthetic comment -- @@ -175,8 +175,8 @@

// ***** Constructor

    public SIMRecords(UiccCard card, Context c, CommandsInterface ci) {
        super(card, c, ci);

adnCache = new AdnRecordCache(mFh);

//Synthetic comment -- @@ -188,23 +188,22 @@
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

//Synthetic comment -- @@ -212,7 +211,7 @@
if(DBG) log("finalized");
}

    protected void onRadioOffOrNotAvailable() {
imsi = null;
msisdn = null;
voiceMailNum = null;
//Synthetic comment -- @@ -528,9 +527,9 @@
}

try { switch (msg.what) {
            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;

/* IO events */
case EVENT_GET_IMSI_DONE:
//Synthetic comment -- @@ -1139,7 +1138,7 @@
}

if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentCard.getAid())) {
// This is for different app. Ignore.
return;
}
//Synthetic comment -- @@ -1304,7 +1303,7 @@

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
//Synthetic comment -- index 81f20bc..05d38b5 100644

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
import com.android.internal.telephony.UiccCard;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -30,6 +24,13 @@
import android.os.RegistrantList;
import android.util.Log;

/* This class is responsible for keeping all knowledge about
* ICCs in the system. It is also used as API to get appropriate
* applications to pass them to phone and service trackers.
//Synthetic comment -- @@ -40,37 +41,68 @@

public static final int APP_FAM_3GPP =  1;
public static final int APP_FAM_3GPP2 = 2;

private static final int EVENT_ICC_STATUS_CHANGED = 1;
private static final int EVENT_GET_ICC_STATUS_DONE = 2;

private static UiccController mInstance;

    private PhoneBase mCurrentPhone;
private CommandsInterface mCi;
private UiccCard mUiccCard;
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

public synchronized UiccCard getUiccCard() {
return mUiccCard;
}

//Notifies when card status changes
public void registerForIccChanged(Handler h, int what, Object obj) {
Registrant r = new Registrant (h, what, obj);
//Synthetic comment -- @@ -100,9 +132,13 @@
}
}

    private UiccController(PhoneBase phone) {
if (DBG) log("Creating UiccController");
        setNewPhone(phone);
}

private synchronized void onGetIccCardStatusDone(AsyncResult ar) {
//Synthetic comment -- @@ -115,55 +151,18 @@

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
mIccChangedRegistrants.notifyRegistrants();
}

    private void setNewPhone(PhoneBase phone) {
        if (phone == null) {
            throw new RuntimeException("Phone can't be null in UiccController");
            //return;
        }

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







