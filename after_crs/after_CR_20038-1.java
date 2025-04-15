/*Phone: Basic PhoneApp changes for DSDS.

Basic phone app changes to support dual sim dual standby(DSDS).

Change-Id:Icffc8c0b167d19087374926233e6a121b0bdab94*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index 80c6f57..aabfd17 100644

//Synthetic comment -- @@ -50,6 +50,9 @@
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import android.provider.Settings;
import java.util.ArrayList;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
//Synthetic comment -- @@ -104,6 +107,7 @@
private static final int EVENT_TTY_PREFERRED_MODE_CHANGED = 14;
private static final int EVENT_TTY_MODE_GET = 15;
private static final int EVENT_TTY_MODE_SET = 16;
    private static final int EVENT_TECHNOLOGY_CHANGED = 17;

// The MMI codes are also used by the InCallScreen.
public static final int MMI_INITIATE = 51;
//Synthetic comment -- @@ -153,9 +157,6 @@
boolean mShowBluetoothIndication = false;
static int mDockState = Intent.EXTRA_DOCK_STATE_UNDOCKED;

// The InCallScreen instance (or null if the InCallScreen hasn't been
// created yet.)
private InCallScreen mInCallScreen;
//Synthetic comment -- @@ -208,21 +209,29 @@
/** boolean indicating restoring mute state on InCallScreen.onResume() */
private boolean mShouldRestoreMuteOnInCallResume;

    /* Array of SinglePhone Objects to store each phoneproxy and associated objects */
    private static ArrayList<SinglePhone> mSinglePhones = new ArrayList<SinglePhone> ();

    // Internal PhoneApp CDMA Call state tracker
    CdmaPhoneCallState cdmaPhoneCallState = null;

// Following are the CDMA OTA information Objects used during OTA Call.
// cdmaOtaProvisionData object store static OTA information that needs
// to be maintained even during Slider open/close scenarios.
// cdmaOtaConfigData object stores configuration info to control visiblity
// of each OTA Screens.
// cdmaOtaScreenState object store OTA Screen State information.
    public OtaUtils.CdmaOtaProvisionData cdmaOtaProvisionData = null;
    public OtaUtils.CdmaOtaConfigData cdmaOtaConfigData = null;
    public OtaUtils.CdmaOtaScreenState cdmaOtaScreenState = null;
    public OtaUtils.CdmaOtaInCallScreenUiState cdmaOtaInCallScreenUiState = null;

// TTY feature enabled on this platform
private boolean mTtyEnabled;
// Current TTY operating mode selected by user
private int mPreferredTtyMode = Phone.TTY_MODE_OFF;
    private int mPhoneType;
    private int defaultSubscription = 0;

/**
* Set the restore mute state flag. Used when we are setting the mute state
//Synthetic comment -- @@ -241,10 +250,25 @@
return mShouldRestoreMuteOnInCallResume;
}

    /*package*/void checkPhoneType() {
        SinglePhone singlePhone;
        for (int i = 0; i < TelephonyManager.getPhoneCount(); i++) {
            //check for both phone states, if there is any change re-register for the events.
            singlePhone = getSinglePhone(i);
            Log.d(LOG_TAG,"old phone type:"+singlePhone.mPhoneType+ ", New Phone type:"+singlePhone.mPhone.getPhoneType());
            if (singlePhone.mPhoneType != singlePhone.mPhone.getPhoneType()) {
                Log.d(LOG_TAG,"handleMessage: radio Technology has changed (" + singlePhone.mPhone.getPhoneName() + ")");
                initForNewRadioTechnology(i);
                singlePhone.mPhoneType = singlePhone.mPhone.getPhoneType();
            }
        }
    }

Handler mHandler = new Handler() {
@Override
public void handleMessage(Message msg) {
Phone.State phoneState;
            checkPhoneType();
switch (msg.what) {

// TODO: This event should be handled by the lock screen, just
//Synthetic comment -- @@ -364,6 +388,7 @@
mInCallScreen.requestUpdateTouchUi();
}
}
                    break;

case EVENT_TTY_PREFERRED_MODE_CHANGED:
// TTY mode is only applied if a headset is connected
//Synthetic comment -- @@ -383,6 +408,10 @@
case EVENT_TTY_MODE_SET:
handleSetTTYModeResponse(msg);
break;

                case EVENT_TECHNOLOGY_CHANGED:
                    // Nothing to do here. already handled by checkPhoneType above
                    break;
}
}
};
//Synthetic comment -- @@ -396,18 +425,30 @@
if (VDBG) Log.v(LOG_TAG, "onCreate()...");

ContentResolver resolver = getContentResolver();
        if (TelephonyManager.isDsdsEnabled()) {
            Log.v(LOG_TAG, "PhoneApp onCreate() DSDS Enabled!!!!");
        }

if (phone == null) {
// Initialize the telephony framework
PhoneFactory.makeDefaultPhones(this);
            
mCM = CallManager.getInstance();
            // Create SinglePhone which hold phone proxy and its corresponding memebers.
            for(int i = 0; i < TelephonyManager.getPhoneCount(); i++) {
                mSinglePhones.add(new SinglePhone(i));
                updatePhoneAppCdmaVariables(i) ;
                mCM.registerPhone(mSinglePhones.get(i).mPhone);
            }

            // Get the default subscription from the system property
            defaultSubscription = getDefaultSubscription();

            // Set Default PhoneApp variables
            setDefaultPhone(defaultSubscription);

            
            mPhoneType = phone.getPhoneType();
NotificationMgr.init(this);

phoneMgr = new PhoneInterfaceManager(this, phone);
//Synthetic comment -- @@ -422,12 +463,6 @@

int phoneType = phone.getPhoneType();

if (BluetoothAdapter.getDefaultAdapter() != null) {
mBtHandsfree = new BluetoothHandsfree(this, mCM);
startService(new Intent(this, BluetoothHeadsetService.class));
//Synthetic comment -- @@ -498,6 +533,7 @@
intentFilter.addAction(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
intentFilter.addAction(TelephonyIntents.ACTION_SERVICE_STATE_CHANGED);
intentFilter.addAction(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED);
            intentFilter.addAction(TelephonyIntents.ACTION_DEFAULT_SUBSCRIPTION_CHANGED);
if (mTtyEnabled) {
intentFilter.addAction(TtyIntent.TTY_PREFERRED_MODE_CHANGE_ACTION);
}
//Synthetic comment -- @@ -528,15 +564,6 @@
PhoneUtils.setAudioMode(mCM);
}

// XXX pre-load the SimProvider so that it's ready
resolver.getType(Uri.parse("content://icc/adn"));

//Synthetic comment -- @@ -563,11 +590,41 @@
android.provider.Settings.System.HEARING_AID,
0);
AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setParameter(CallIndependentServices.HAC_KEY, hac != 0 ?
                                      CallIndependentServices.HAC_VAL_ON :
                                      CallIndependentServices.HAC_VAL_OFF);
}
        Log.v(LOG_TAG,"onCreate done...");
    }

    // updates cdma variables of PhoneApp
    public void updatePhoneAppCdmaVariables(int subscription) {
        Log.v(LOG_TAG,"updatePhoneAppCdmaVariables" + subscription);
        SinglePhone singlePhone = getSinglePhone(subscription);

        if ((singlePhone != null) &&(singlePhone.mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA)) {
            cdmaPhoneCallState = singlePhone.mCdmaPhoneCallState;
            cdmaOtaProvisionData = singlePhone.mCdmaOtaProvisionData;
            cdmaOtaConfigData = singlePhone.mCdmaOtaConfigData;
            cdmaOtaScreenState = singlePhone.mCdmaOtaScreenState;
            cdmaOtaInCallScreenUiState = singlePhone.mCdmaOtaInCallScreenUiState;
        }
    }

    // update PhoneApp variables
    private void updatePhoneApp(int subscription) {
       SinglePhone singlePhone = getSinglePhone(subscription);

       if (singlePhone == null) return;

       if (singlePhone.mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
           // Create an instance of CdmaPhoneCallState and initialize it to IDLE
           singlePhone.initializeCdmaVariables();
           updatePhoneAppCdmaVariables(subscription);
       } else {
           singlePhone.clearCdmaVariables();
       }
    }

@Override
public void onConfigurationChanged(Configuration newConfig) {
//Synthetic comment -- @@ -616,8 +673,10 @@
* This intent can only be used from within the Phone app, since the
* InCallScreen is not exported from our AndroidManifest.
*/
    /* package */ static Intent createInCallIntent(int subscription) {
        Log.d(LOG_TAG, "createInCallIntent subscription:");
Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.putExtra("Subscription", subscription);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//Synthetic comment -- @@ -631,7 +690,8 @@
* comes up.
*/
/* package */ static Intent createInCallIntent(boolean showDialpad) {
        Log.d(LOG_TAG, "createInCallIntent showdialpad");
        Intent intent = createInCallIntent(getDefaultSubscription());
intent.putExtra(InCallScreen.SHOW_DIALPAD_EXTRA, showDialpad);
return intent;
}
//Synthetic comment -- @@ -645,13 +705,18 @@
*/
private void displayCallScreen() {
if (VDBG) Log.d(LOG_TAG, "displayCallScreen()...");
        startActivity(createInCallIntent(getPhoneInCall().getSubscription()));
Profiler.callScreenRequested();
}

boolean isSimPinEnabled() {
return mIsSimPinEnabled;
}
    
    boolean isSimPinEnabled(int subscription) {
        SinglePhone singlePhone = getSinglePhone(subscription);
        return singlePhone.mIsSimPinEnabled;
    }

boolean authenticateAgainstCachedSimPin(String pin) {
return (mCachedSimPin != null && mCachedSimPin.equals(pin));
//Synthetic comment -- @@ -665,6 +730,10 @@
mInCallScreen = inCallScreen;
}

    InCallScreen getInCallScreen() {
        return mInCallScreen;
    }

/**
* @return true if the in-call UI is running as the foreground
* activity.  (In other words, from the perspective of the
//Synthetic comment -- @@ -690,7 +759,7 @@
* For OTA Call, it call InCallScreen api to handle OTA Call End scenario
* to display OTA Call End screen.
*/
    void dismissCallScreen(Phone phone) {
if (mInCallScreen != null) {
if ((phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) &&
(mInCallScreen.isOtaCallInActiveState()
//Synthetic comment -- @@ -1219,8 +1288,8 @@
}
}

    /* package */ Phone.State getPhoneState(int subscription) {
        return getSinglePhone(subscription).mLastPhoneState;
}

/**
//Synthetic comment -- @@ -1237,35 +1306,24 @@

private void onMMIComplete(AsyncResult r) {
if (VDBG) Log.d(LOG_TAG, "onMMIComplete()...");
        Phone localPhone = null;
MmiCode mmiCode = (MmiCode) r.result;
        if (r.userObj != null ) {
            localPhone = (Phone)r.userObj;
        }
        PhoneUtils.displayMMIComplete(localPhone, getInstance(), mmiCode, null, null);
}

    private void initForNewRadioTechnology(int subscription) {
if (DBG) Log.d(LOG_TAG, "initForNewRadioTechnology...");
        SinglePhone singlePhone = getSinglePhone(subscription);

        Phone phone = singlePhone.mPhone;
        updatePhoneApp(subscription);
        if (phone.getPhoneType() != Phone.PHONE_TYPE_CDMA) {
clearOtaState();
}
	    clearInCallScreenMode();

ringer.updateRingerContextAfterRadioTechnologyChange(this.phone);
notifier.updateCallNotifierRegistrationsAfterRadioTechnologyChange();
//Synthetic comment -- @@ -1389,10 +1447,20 @@
@Override
public void onReceive(Context context, Intent intent) {
String action = intent.getAction();
            Log.v(LOG_TAG,"Action intent recieved:"+action);
            //gets the subscription information ( "0" or "1")
            int subscription = intent.getIntExtra("phone_subscription", getDefaultSubscription());
if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                // When airplane mode is selected/deselected from settings
                // AirplaneModeEnabler sets the value of extra "state" to
                // true if airplane mode is enabled and false if it is
                // disabled and broadcasts the intent. setRadioPower uses
                // true if airplane mode is disabled and false if enabled.
                boolean enabled = intent.getBooleanExtra("state",false);
                for (int i = 0; i < TelephonyManager.getPhoneCount(); i++) {
                    getPhone(i).setRadioPower(!enabled);
                }

} else if (action.equals(BluetoothHeadset.ACTION_STATE_CHANGED)) {
mBluetoothHeadsetState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE,
BluetoothHeadset.STATE_ERROR);
//Synthetic comment -- @@ -1450,8 +1518,10 @@
Log.d(LOG_TAG, "Radio technology switched. Now " + newPhone + " is active.");
initForNewRadioTechnology();
} else if (action.equals(TelephonyIntents.ACTION_SERVICE_STATE_CHANGED)) {
                Phone phone = getPhone(subscription);
                handleServiceStateChanged(intent, phone);
} else if (action.equals(TelephonyIntents.ACTION_EMERGENCY_CALLBACK_MODE_CHANGED)) {
                Phone phone = getPhone(subscription);
if (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
Log.d(LOG_TAG, "Emergency Callback Mode arrived in PhoneApp.");
// Start Emergency Callback Mode service
//Synthetic comment -- @@ -1480,6 +1550,10 @@
if(ringerMode == AudioManager.RINGER_MODE_SILENT) {
notifier.silenceRinger();
}
            } else if (action.equals(TelephonyIntents.ACTION_DEFAULT_SUBSCRIPTION_CHANGED)) {
                Log.d(LOG_TAG, "Default subscription changed, subscription: " + subscription);
                defaultSubscription = subscription;
                setDefaultPhone(subscription);
}
}
}
//Synthetic comment -- @@ -1526,7 +1600,7 @@
}
}

    private void handleServiceStateChanged(Intent intent, Phone phone) {
/**
* This used to handle updating EriTextWidgetProvider this routine
* and and listening for ACTION_SERVICE_STATE_CHANGED intents could
//Synthetic comment -- @@ -1664,4 +1738,124 @@
// System process is dead.
}
}

    // gets the SinglePhone corresponding to a subscription
    private SinglePhone getSinglePhone(int subscription) {
        try {
            return mSinglePhones.get(subscription);
        } catch (IndexOutOfBoundsException e) {
            Log.e(LOG_TAG,"subscripton Index out of bounds "+e);
            return null;
        }
    }

    // gets the array of PhoneProxys
    Phone[] getPhones() {
        int numPhones = TelephonyManager.getPhoneCount();
        Phone[] phones = new Phone[numPhones];

        for (int i = 0; i < mSinglePhones.size(); i++) {
            phones[i] = getSinglePhone(i).mPhone;
        }
        return phones;
    }

    // gets the Default Phone
    static Phone getDefaultPhone() {
        PhoneApp app = PhoneApp.getInstance();
        return app.getPhone(getDefaultSubscription());
    }

    // gets the Phone correspoding to a subscription
    static Phone getPhone(int subscription) {
        PhoneApp app = PhoneApp.getInstance();
        SinglePhone singlePhone= app.getSinglePhone(subscription);
        if (singlePhone != null) {
            return singlePhone.mPhone;
        } else {
            Log.w(LOG_TAG, "singlePhone object is null returning default phone");
            return app.phone;
        }
    }

    boolean isSimPukLocked(int subscription) {
        return getSinglePhone(subscription).mIsSimPukLocked;
    }

    /**
     * Gets the active phone that has call in progress.
     */
    public Phone getPhoneInCall() {
        Phone phone = null;
        boolean isInCall = false;
        //TODO DSDA, Extend to handle if both phones are in call.
        for (int i = 0; i < TelephonyManager.getPhoneCount(); i++) {
            phone = getSinglePhone(i).mPhone;
            if ((phone != null) && (phone.isInCall())) {
                isInCall = true;
                break;
            }
        }
        if (isInCall) {
            return phone;
        } else {
            Log.w(LOG_TAG, "No phone is in active call state returning default phone");
            return this.phone;
        }
    }

    /**
      * Get the subscription that has service
      */
    public int getVoiceSubscriptionInService() {
        int voiceSub = getVoiceSubscription();
        int sub = voiceSub;
        for (int i = 0; i < TelephonyManager.getPhoneCount(); i++) {
            Phone phone = getPhone(i);
            int ss = phone.getServiceState().getState();
            if ((ss == ServiceState.STATE_IN_SERVICE)
                    || (ss == ServiceState.STATE_EMERGENCY_ONLY)) {
                sub = i;
                if (sub == voiceSub) break;
            }
        }
        return sub;
    }

    CdmaPhoneCallState getCdmaPhoneCallState (int subscription) {
        SinglePhone singlePhone = getSinglePhone(subscription);
        if (singlePhone == null) {
            return null;
        }
        return singlePhone.mCdmaPhoneCallState;
    }

    //Sets the default phoneApp variables
    void setDefaultPhone(int subscription){
        //When default phone dynamically changes need to handle
        SinglePhone singlePhone = getSinglePhone(subscription);
        phone = singlePhone.mPhone;
        mLastPhoneState = singlePhone.mLastPhoneState;
        updatePhoneAppCdmaVariables(subscription);
        defaultSubscription = subscription;
    }
    /* Gets the default subscription */
    public static int getDefaultSubscription() {
        return PhoneFactory.getDefaultSubscription();
    }

    /* Gets User preferred Voice subscription setting*/
    public static int getVoiceSubscription() {
        return PhoneFactory.getVoiceSubscription();
    }

    /* Gets User preferred Data subscription setting*/
    public static int getDataSubscription() {
        return PhoneFactory.getDataSubscription();
    }

    /* Gets User preferred SMS subscription setting*/
    public static int getSMSSubscription() {
        return PhoneFactory.getSMSSubscription();
    }
}








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..2c7811a 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.CommandException;

import java.util.List;
import java.util.ArrayList;
//Synthetic comment -- @@ -47,6 +49,8 @@
private static final String LOG_TAG = "PhoneInterfaceManager";
private static final boolean DBG = (PhoneApp.DBG_LEVEL >= 2);

    private static final String SUBSCRIPTION = "Subscription";

// Message codes used with mMainThreadHandler
private static final int CMD_HANDLE_PIN_MMI = 1;
private static final int CMD_HANDLE_NEIGHBORING_CELL = 2;
//Synthetic comment -- @@ -65,13 +69,16 @@
* request after sending. The main thread will notify the request when it is complete.
*/
private static final class MainThreadRequest {
        /** The first argument to use for the request */
        public Object arg1;
        /** The second argument to use for the request */
        public Object arg2;
/** The result of the request that is run on the main thread */
public Object result;

        public MainThreadRequest(Object arg1, Object arg2) {
            this.arg1 = arg1;
            this.arg2 = arg2;
}
}

//Synthetic comment -- @@ -97,8 +104,11 @@
switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
request = (MainThreadRequest) msg.obj;
                    int sub = (Integer) request.arg2;
                    Phone phone = PhoneApp.getPhone(sub);
                    Log.i(LOG_TAG,"CMD_HANDLE_PIN_MMI: sub :" + phone.getSubscription());
request.result = Boolean.valueOf(
                            phone.handlePinMmi((String) request.arg1));
// Wake up the requesting thread
synchronized (request) {
request.notifyAll();
//Synthetic comment -- @@ -138,14 +148,15 @@
case CMD_END_CALL:
request = (MainThreadRequest) msg.obj;
boolean hungUp = false;
                    phone = PhoneApp.getInstance().getPhoneInCall();
                    int phoneType = phone.getPhoneType();
if (phoneType == Phone.PHONE_TYPE_CDMA) {
// CDMA: If the user presses the Power button we treat it as
// ending the complete call session
                        hungUp = PhoneUtils.hangupRingingAndActive(phone);
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
                        // GSM: End all calls except waiting
                        hungUp = PhoneUtils.hangupAllCalls(phone);
} else {
throw new IllegalStateException("Unexpected phone type: " + phoneType);
}
//Synthetic comment -- @@ -169,12 +180,12 @@
* waits for the request to complete, and returns the result.
* @see sendRequestAsync
*/
    private Object sendRequest(int command, Object arg1, Object arg2) {
if (Looper.myLooper() == mMainThreadHandler.getLooper()) {
throw new RuntimeException("This method will deadlock if called from the main thread.");
}

        MainThreadRequest request = new MainThreadRequest(arg1, arg2);
Message msg = mMainThreadHandler.obtainMessage(command, request);
msg.sendToTarget();

//Synthetic comment -- @@ -215,12 +226,23 @@
ServiceManager.addService("phone", this);
}

    // returns phone associated with the subscription.
    // getPhone(0) returns default phone in single standby mode.
    private Phone getPhone(int subscription) {
        return PhoneApp.getPhone(subscription);
    }

//
// Implementation of the ITelephony interface.
//

public void dial(String number) {
if (DBG) log("dial: " + number);
        dialOnSubscription(number, getPreferredVoiceSubscription());
    }

    public void dialOnSubscription(String number, int subscription) {
        if (DBG) log("dial: " + number);
// No permission check needed here: This is just a wrapper around the
// ACTION_DIAL intent, which is available to any app since it puts up
// the UI before it does anything.
//Synthetic comment -- @@ -231,16 +253,22 @@
}

// PENDING: should we just silently fail if phone is offhook or ringing?
        Phone.State state = mCM.getState();
if (state != Phone.State.OFFHOOK && state != Phone.State.RINGING) {
Intent  intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(SUBSCRIPTION, subscription);
mApp.startActivity(intent);
}
}

public void call(String number) {
if (DBG) log("call: " + number);
        callOnSubscription(number, getPreferredVoiceSubscription());
    }

    public void callOnSubscription(String number, int subscription) {
        if (DBG) log("call: " + number);

// This is just a wrapper around the ACTION_CALL intent, but we still
// need to do a permission check since we're calling startActivity()
//Synthetic comment -- @@ -253,6 +281,7 @@
}

Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        intent.putExtra(SUBSCRIPTION, subscription);
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
intent.setClassName(mApp, PhoneApp.getCallScreenClassName());
mApp.startActivity(intent);
//Synthetic comment -- @@ -270,7 +299,7 @@
if (specifyInitialDialpadState) {
intent = PhoneApp.createInCallIntent(initialDialpadState);
} else {
                intent = PhoneApp.createInCallIntent(getPreferredVoiceSubscription());
}
mApp.startActivity(intent);
} finally {
//Synthetic comment -- @@ -296,12 +325,25 @@
* @return true is a call was ended
*/
public boolean endCall() {
        return endCallOnSubscription(getDefaultSubscription());
    }

    /**
     * End a call based on the call state of the subscription
     * @return true is a call was ended
     */
    public boolean endCallOnSubscription(int subscription) {
enforceCallPermission();
        return (Boolean) sendRequest(CMD_END_CALL, null, null);
}

public void answerRingingCall() {
if (DBG) log("answerRingingCall...");
        answerRingingCallOnSubscription(getDefaultSubscription());
    }

    public void answerRingingCallOnSubscription(int subscription) {
        if (DBG) log("answerRingingCall...");
// TODO: there should eventually be a separate "ANSWER_PHONE" permission,
// but that can probably wait till the big TelephonyManager API overhaul.
// For now, protect this call with the MODIFY_PHONE_STATE permission.
//Synthetic comment -- @@ -323,10 +365,10 @@
* return value, so let's just return void for now.
*/
private void answerRingingCallInternal() {
        final boolean hasRingingCall = mCM.hasActiveRingingCall();
if (hasRingingCall) {
            final boolean hasActiveCall = mCM.hasActiveFgCall();
            final boolean hasHoldingCall = mCM.hasActiveBgCall();
if (hasActiveCall && hasHoldingCall) {
// Both lines are in use!
// TODO: provide a flag to let the caller specify what
//Synthetic comment -- @@ -362,7 +404,7 @@
* @see silenceRinger
*/
private void silenceRingerInternal() {
        if ((mCM.getState() == Phone.State.RINGING)
&& mApp.notifier.isRinging()) {
// Ringer is actually playing, so silence it.
if (DBG) log("silenceRingerInternal: silencing...");
//Synthetic comment -- @@ -371,25 +413,42 @@
}

public boolean isOffhook() {
        return (mCM.getState() == Phone.State.OFFHOOK);
}

public boolean isRinging() {
        return (mCM.getState() == Phone.State.RINGING);
}

public boolean isIdle() {
        return isIdleOnSubscription(getDefaultSubscription());
    }

    public boolean isIdleOnSubscription(int subscription) {
        return (getPhone(subscription).getState() == Phone.State.IDLE);
}

public boolean isSimPinEnabled() {
        return isSimPinEnabledOnSubscription(getDefaultSubscription());
    }

    public boolean isSimPinEnabledOnSubscription(int subscription) {
enforceReadPermission();
        return (PhoneApp.getInstance().isSimPinEnabled(subscription));
    }

    public boolean isSimPukLockedOnSubscription(int subscription) {
        enforceReadPermission();
        return (PhoneApp.getInstance().isSimPukLocked(subscription));
}

public boolean supplyPin(String pin) {
        return supplyPinOnSubscription(pin, getDefaultSubscription());
    }

    public boolean supplyPinOnSubscription(String pin, int subscription) {
enforceModifyPermission();
        final CheckSimPin checkSimPin = new CheckSimPin(getPhone(subscription).getIccCard());
checkSimPin.start();
return checkSimPin.checkPin(pin);
}
//Synthetic comment -- @@ -468,75 +527,110 @@
}

public void updateServiceLocation() {
        updateServiceLocationOnSubscription(getDefaultSubscription());
    }

    public void updateServiceLocationOnSubscription(int subscription) {
// No permission check needed here: this call is harmless, and it's
// needed for the ServiceState.requestStateUpdate() call (which is
// already intentionally exposed to 3rd parties.)
        getPhone(subscription).updateServiceLocation();
}

public boolean isRadioOn() {
        return isRadioOnOnSubscription(getDefaultSubscription());
    }

    public boolean isRadioOnOnSubscription(int subscription) {
        return getPhone(subscription).getServiceState().getState() != ServiceState.STATE_POWER_OFF;
}

public void toggleRadioOnOff() {
        toggleRadioOnOffOnSubscription(getDefaultSubscription());
}

    public void toggleRadioOnOffOnSubscription(int subscription) {
enforceModifyPermission();
        getPhone(subscription).setRadioPower(!isRadioOn());
    }

    public boolean setRadio(boolean turnOn) {
        return setRadioOnSubscription(turnOn, getDefaultSubscription());
    }

    public boolean setRadioOnSubscription(boolean turnOn, int subscription) {
        enforceModifyPermission();
        if ((getPhone(subscription).getServiceState().getState() != ServiceState.STATE_POWER_OFF) != turnOn) {
            toggleRadioOnOffOnSubscription(subscription);
}
return true;
}

public boolean enableDataConnectivity() {
enforceModifyPermission();
        return getPhone(PhoneApp.getDataSubscription()).enableDataConnectivity();
}

public int enableApnType(String type) {
enforceModifyPermission();
        return getPhone(PhoneApp.getDataSubscription()).enableApnType(type);
}

public int disableApnType(String type) {
enforceModifyPermission();
        return getPhone(PhoneApp.getDataSubscription()).disableApnType(type);
}

public boolean disableDataConnectivity() {
enforceModifyPermission();
        return getPhone(PhoneApp.getDataSubscription()).disableDataConnectivity();
}

public boolean isDataConnectivityPossible() {
        return getPhone(PhoneApp.getDataSubscription()).isDataConnectivityPossible();
}

public boolean handlePinMmi(String dialString) {
        return handlePinMmiOnSubscription(dialString, getDefaultSubscription());
    }

    public boolean handlePinMmiOnSubscription(String dialString, int subscription) {
enforceModifyPermission();
        return (Boolean) sendRequest(CMD_HANDLE_PIN_MMI, dialString, subscription);
}

public void cancelMissedCallsNotification() {
        cancelMissedCallsNotificationOnSubscription(getDefaultSubscription());
    }

    public void cancelMissedCallsNotificationOnSubscription(int subscription) {
enforceModifyPermission();
NotificationMgr.getDefault().cancelMissedCallNotification();
}

public int getCallState() {
        return getCallStateOnSubscription(getDefaultSubscription());
    }

    public int getCallStateOnSubscription(int subscription) {
        return DefaultPhoneNotifier.convertCallState(getPhone(subscription).getState());
}

public int getDataState() {
        return DefaultPhoneNotifier.convertDataState(
                getPhone(PhoneApp.getDataSubscription()).getDataConnectionState());
}

public int getDataActivity() {
        return DefaultPhoneNotifier.convertDataActivityState(
                getPhone(PhoneApp.getDataSubscription()).getDataActivityState());
}

public Bundle getCellLocation() {
        return getCellLocationOnSubscription(getDefaultSubscription());
    }

    public Bundle getCellLocationOnSubscription(int subscription) {
try {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.ACCESS_FINE_LOCATION, null);
//Synthetic comment -- @@ -548,24 +642,37 @@
android.Manifest.permission.ACCESS_COARSE_LOCATION, null);
}
Bundle data = new Bundle();
        getPhone(subscription).getCellLocation().fillInNotifierBundle(data);
return data;
}

public void enableLocationUpdates() {
        enableLocationUpdatesOnSubscription(getDefaultSubscription());
    }

    public void enableLocationUpdatesOnSubscription(int subscription) {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.CONTROL_LOCATION_UPDATES, null);
        getPhone(subscription).enableLocationUpdates();
}

public void disableLocationUpdates() {
        disableLocationUpdatesOnSubscription(getDefaultSubscription());
    }

    public void disableLocationUpdatesOnSubscription(int subscription) {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.CONTROL_LOCATION_UPDATES, null);
        getPhone(subscription).disableLocationUpdates();
}

@SuppressWarnings("unchecked")
public List<NeighboringCellInfo> getNeighboringCellInfo() {
        return getNeighboringCellInfoOnSubscription(getDefaultSubscription());
    }

    @SuppressWarnings("unchecked")
    public List<NeighboringCellInfo> getNeighboringCellInfoOnSubscription(int subscription) {
try {
mApp.enforceCallingOrSelfPermission(
android.Manifest.permission.ACCESS_FINE_LOCATION, null);
//Synthetic comment -- @@ -582,7 +689,7 @@

try {
cells = (ArrayList<NeighboringCellInfo>) sendRequest(
                    CMD_HANDLE_NEIGHBORING_CELL, null, null);
} catch (RuntimeException e) {
Log.e(LOG_TAG, "getNeighboringCellInfo " + e);
}
//Synthetic comment -- @@ -638,14 +745,22 @@
}

public int getActivePhoneType() {
        return getActivePhoneTypeOnSubscription(getDefaultSubscription());
    }

    public int getActivePhoneTypeOnSubscription(int subscription) {
        return getPhone(subscription).getPhoneType();
}

/**
* Returns the CDMA ERI icon index to display
*/
public int getCdmaEriIconIndex() {
        return getCdmaEriIconIndexOnSubscription(getDefaultSubscription());
    }

    public int getCdmaEriIconIndexOnSubscription(int subscription) {
        return getPhone(subscription).getCdmaEriIconIndex();
}

/**
//Synthetic comment -- @@ -654,26 +769,38 @@
* 1 - FLASHING
*/
public int getCdmaEriIconMode() {
        return getCdmaEriIconModeOnSubscription(getDefaultSubscription());
    }

    public int getCdmaEriIconModeOnSubscription(int subscription) {
        return getPhone(subscription).getCdmaEriIconMode();
}

/**
* Returns the CDMA ERI text,
*/
public String getCdmaEriText() {
        return getCdmaEriTextOnSubscription(getDefaultSubscription());
    }

    public String getCdmaEriTextOnSubscription(int subscription) {
        return getPhone(subscription).getCdmaEriText();
}

/**
* Returns true if CDMA provisioning needs to run.
*/
public boolean getCdmaNeedsProvisioning() {
        return getCdmaNeedsProvisioningOnSubscription(getDefaultSubscription());
    }

    public boolean getCdmaNeedsProvisioningOnSubscription(int subscription) {
        if (getActivePhoneTypeOnSubscription(subscription) == Phone.PHONE_TYPE_GSM) {
return false;
}

boolean needsProvisioning = false;
        String cdmaMin = getPhone(subscription).getCdmaMin();
try {
needsProvisioning = OtaUtils.needsActivation(cdmaMin);
} catch (IllegalArgumentException e) {
//Synthetic comment -- @@ -687,14 +814,28 @@
* Returns the unread count of voicemails
*/
public int getVoiceMessageCount() {
        return getVoiceMessageCountOnSubscription(getDefaultSubscription());
    }

    /**
     * Returns the unread count of voicemails for a subscription
     */
    public int getVoiceMessageCountOnSubscription(int subscription) {
        return getPhone(subscription).getVoiceMessageCount();
}

/**
* Returns the network type
*/
public int getNetworkType() {
        return getNetworkTypeOnSubscription(getDefaultSubscription());
    }

    /**
     * Returns the network type for a subscription
     */
    public int getNetworkTypeOnSubscription(int subscription) {
        int radiotech = getPhone(subscription).getServiceState().getRadioTechnology();
switch(radiotech) {
case ServiceState.RADIO_TECHNOLOGY_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
//Synthetic comment -- @@ -728,6 +869,37 @@
* @return true if a ICC card is present
*/
public boolean hasIccCard() {
        return hasIccCardOnSubscription(getDefaultSubscription());
    }

    /**
     * @return true if a ICC card is present for a subscription
     */
    public boolean hasIccCardOnSubscription(int subscription) {
        return getPhone(subscription).getIccCard().hasIccCard();
    }

    /**
     * {@hide}
     * Returns Default subscription, 0 in the case of single standby.
     */
    public int getDefaultSubscription() {
        return PhoneApp.getDefaultSubscription();
    }

    /**
     * {@hide}
     * Returns Preferred Voice subscription.
     */
    public int getPreferredVoiceSubscription() {
        return PhoneApp.getVoiceSubscription();
    }

    /**
     * {@hide}
     * Returns Preferred Data subscription.
     */
    public int getPreferredDataSubscription() {
        return PhoneApp.getDataSubscription();
}
}








//Synthetic comment -- diff --git a/src/com/android/phone/SinglePhone.java b/src/com/android/phone/SinglePhone.java
new file mode 100644
//Synthetic comment -- index 0000000..b14b739

//Synthetic comment -- @@ -0,0 +1,244 @@
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

package com.android.phone;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncResult;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.OtaUtils.CdmaOtaScreenState;



public class SinglePhone {

/* package */ static final String LOG_TAG = "SinglePhone";

    /**
     * SinglePhone -wide debug level:
     *   0 - no debug logging
     *   1 - normal debug logging if ro.debuggable is set (which is true in
     *       "eng" and "userdebug" builds but not "user" builds)
     *   2 - ultra-verbose debug logging
     *
     * Most individual classes in the phone app have a local DBG constant,
     * typically set to
     *   (SinglePhone.DBG_LEVEL >= 1) && (SystemProperties.getInt("ro.debuggable", 0) == 1)
     * or else
     *   (SinglePhone.DBG_LEVEL >= 2)
     * depending on the desired verbosity.
     */
    /* package */ static final int DBG_LEVEL = 1;

    private static final boolean DBG =
            (SinglePhone.DBG_LEVEL >= 1) && (SystemProperties.getInt("ro.debuggable", 0) == 1);
    private static final boolean VDBG = (SinglePhone.DBG_LEVEL >= 2);

     // Message codes; see mHandler below.
    private static final int EVENT_SIM_STATE_CHANGED = 8;
    private static final int EVENT_UNSOL_CDMA_INFO_RECORD = 12;
    private static final int EVENT_DOCK_STATE_CHANGED = 13;
    private static final int EVENT_TECHNOLOGY_CHANGED = 17;

    // The MMI codes are also used by the InCallScreen.
    public static final int MMI_COMPLETE = 52;
    public static final int MMI_CANCEL = 53;


    public Phone mPhone;
    public int mPhoneType;

    //public SinglePhoneHandler mHandler; //handler for SinglePhone
    public boolean mIsSimPinEnabled;
    public String mCachedSimPin;
    public boolean mIsSimPukLocked;
    // Last phone state seen by updatePhoneState()
    public Phone.State mLastPhoneState = Phone.State.IDLE;


    // Internal SinglePhone cdma Call state tracker
    public CdmaPhoneCallState mCdmaPhoneCallState = null;

    // Following are the CDMA OTA information Objects used during OTA Call.
    // cdmaOtaProvisionData object store static OTA information that needs
    // to be maintained even during Slider open/close scenarios.
    // cdmaOtaConfigData object stores configuration info to control visiblity
    // of each OTA Screens.
    // cdmaOtaScreenState object store OTA Screen State information.
    public OtaUtils.CdmaOtaProvisionData mCdmaOtaProvisionData = null;
    public OtaUtils.CdmaOtaConfigData mCdmaOtaConfigData = null;
    public OtaUtils.CdmaOtaScreenState mCdmaOtaScreenState = null;
    public OtaUtils.CdmaOtaInCallScreenUiState mCdmaOtaInCallScreenUiState = null;


    SinglePhone(int subscription) {
        if (VDBG) Log.d(LOG_TAG, "Single Phone constructor: "+ subscription);
        // Get the phone
        mPhone = PhoneFactory.getPhone(subscription);
        mPhoneType = mPhone.getPhoneType();
        //mHandler = new SinglePhoneHandler(mPhone);
        //PhoneApp app = PhoneApp.getInstance();

        // register for MMI/USSD
        //mPhone.registerForMmiComplete(mHandler, MMI_COMPLETE, null);

        // register connection tracking to PhoneUtils
        //PhoneUtils.initializeConnectionHandler(mPhone);
        // TODO: Register for Cdma Information Records
        // phone.registerCdmaInformationRecord(mHandler, EVENT_UNSOL_CDMA_INFO_RECORD, null);

        boolean phoneIsCdma = (mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA);

        if (mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
            initializeCdmaVariables();
        }

    }

    public void initializeCdmaVariables() {

        if (mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
            // Create an instance of CdmaPhoneCallState and initialize it to IDLE
            mCdmaPhoneCallState = new CdmaPhoneCallState();
            mCdmaPhoneCallState.CdmaPhoneCallStateInit();

            if (mCdmaOtaProvisionData == null) {
                mCdmaOtaProvisionData = new OtaUtils.CdmaOtaProvisionData();
            }
            if (mCdmaOtaConfigData == null ) {
                mCdmaOtaConfigData = new OtaUtils.CdmaOtaConfigData();
            }
            if (mCdmaOtaScreenState == null ) {
                mCdmaOtaScreenState = new OtaUtils.CdmaOtaScreenState();
            }
            if (mCdmaOtaInCallScreenUiState == null) {
                mCdmaOtaInCallScreenUiState = new OtaUtils.CdmaOtaInCallScreenUiState();
            }
        }
    }

    public void clearCdmaVariables() {

        if (mPhone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
            mCdmaPhoneCallState = null;
            mCdmaOtaProvisionData = null;
            mCdmaOtaConfigData = null;
            mCdmaOtaScreenState = null;
            mCdmaOtaInCallScreenUiState = null;
        }
    }

    /*public class SinglePhoneHandler extends Handler {
        Phone mPhone;

        SinglePhoneHandler(Phone phone) {
            mPhone = phone;
        }
        @Override
        public void handleMessage(Message msg) {
            PhoneApp app = PhoneApp.getInstance();
            app.checkPhoneType();
            switch (msg.what) {

                case MMI_COMPLETE:
                    onMMIComplete((AsyncResult) msg.obj);
                    break;

                case MMI_CANCEL:
                    PhoneUtils.cancelMmiCode(mPhone);
                    break;

                case EVENT_SIM_STATE_CHANGED:
                    // Marks the event where the SIM goes into ready state.
                    // Right now, this is only used for the PUK-unlocking
                    // process.
                    if (msg.obj.equals(IccCard.INTENT_VALUE_ICC_SUBSCRIPTION_READY)) {
                        // when the right event is triggered and there
                        // are UI objects in the foreground, we close
                        // them to display the lock panel.
                        Activity pukEntryActivity;
                        ProgressDialog pukEntryProgressDialog;
                        pukEntryActivity = app.getPUKEntryActivity();
                        pukEntryProgressDialog = app.getPUKEntryProgressDialog();
                        if (pukEntryActivity != null) {
                            pukEntryActivity.finish();
                            pukEntryActivity = null;
                        }
                        if (pukEntryProgressDialog != null) {
                            pukEntryProgressDialog.dismiss();
                            pukEntryProgressDialog = null;
                        }
                    }
                    break;

                case EVENT_UNSOL_CDMA_INFO_RECORD:
                    //TODO: handle message here;
                    break;

                case EVENT_TECHNOLOGY_CHANGED:
                    // Nothing to do here. already handled by checkPhoneType above
                    break;

                case EVENT_DOCK_STATE_CHANGED:
                    // If the phone is docked/undocked during a call, and no wired or BT headset
                    // is connected: turn on/off the speaker accordingly.
                    boolean inDockMode = false;
                    if (app.mDockState == Intent.EXTRA_DOCK_STATE_DESK ||
                            app.mDockState == Intent.EXTRA_DOCK_STATE_CAR) {
                        inDockMode = true;
                    }
                    if (VDBG) Log.d(LOG_TAG, "received EVENT_DOCK_STATE_CHANGED. Phone inDock = "
                            + inDockMode);

                    Phone.State phoneState = mPhone.getState();
                    BluetoothHandsfree btHandsfree;
                    btHandsfree = app.getBluetoothHandsfree();
                    if (phoneState == Phone.State.OFFHOOK &&
                            !app.isHeadsetPlugged() &&
                            !(btHandsfree != null && btHandsfree.isAudioOn())) {
                        PhoneUtils.turnOnSpeaker(app.getAppContext(), inDockMode, true);

                        InCallScreen inCallScreen;
                        inCallScreen = app.getInCallScreen();
                        if (inCallScreen != null) {
                            inCallScreen.requestUpdateTouchUi();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
     }
*/
 /*    private void onMMIComplete(AsyncResult r) {
        if (VDBG) Log.d(LOG_TAG, "onMMIComplete()...");
        MmiCode mmiCode = (MmiCode) r.result;
        PhoneUtils.displayMMIComplete(mPhone, PhoneApp.getInstance(), mmiCode, null, null);
    }*/
};







