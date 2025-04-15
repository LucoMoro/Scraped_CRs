/*Phone(DSDS): Support for Pin/Puk in MultiSim scenarios

Extending PIN/PUK for multisim scenarios.

TODO: Fix the compile errors
TODO: Depends on NON DSDS

Change-Id:I69987ef8ccfd902ecd743047fb2a8d96c52ba4f2*/




//Synthetic comment -- diff --git a/src/com/android/phone/FdnSetting.java b/src/com/android/phone/FdnSetting.java
//Synthetic comment -- index 04c070e..c7161a2 100644

//Synthetic comment -- @@ -294,8 +294,6 @@
a.show();
} else {
// set the correct error message depending upon the state.
if (mPinChangeState == PIN_CHANGE_PUK) {
if (mPhone.getIccCard().getIccPuk2Blocked()) {
Log.d(LOG_TAG,"PUK2 Blocked while changing PIN2.Options"
//Synthetic comment -- @@ -309,7 +307,7 @@
} else {
displayMessage(R.string.badPin2);
}

// Reset the state depending upon or knowledge of the PUK state.
if (!mIsPuk2Locked) {
displayMessage(R.string.badPin2);
//Synthetic comment -- @@ -434,8 +432,7 @@
* Reflect the updated FDN state in the UI.
*/
private void updateEnableFDN() {
        if (mPhone.getIccCard().getIccFdnAvailable()) {
if (mPhone.getIccCard().getIccFdnEnabled()) {
mButtonEnableFDN.setTitle(R.string.enable_fdn_ok);
mButtonEnableFDN.setSummary(R.string.fdn_enabled);
//Synthetic comment -- @@ -445,7 +442,7 @@
mButtonEnableFDN.setSummary(R.string.fdn_disabled);
mButtonEnableFDN.setDialogTitle(R.string.enable_fdn);
}
        } else {
// Disable FDN Settings since FDN service is unavailable.
mButtonEnableFDN.setEnabled(false);
mButtonChangePin2.setEnabled(false);
//Synthetic comment -- @@ -453,7 +450,6 @@
mButtonChangePin2.setSummary(R.string.fdn_unavailable);
displayMessage(R.string.fdn_unavailable);
}
}

@Override
//Synthetic comment -- @@ -510,8 +506,7 @@
super.onResume();
mPhone = PhoneApp.getInstance().getPhone(mSubscription);
updateEnableFDN();
        checkPin2StatusAndUpdateFdnScreen();
}

/**
//Synthetic comment -- @@ -542,8 +537,6 @@
Log.d(LOG_TAG, "FdnSetting: " + msg);
}

private void checkPin2StatusAndUpdateFdnScreen() {
if (mPhone.getIccCard().getIccPuk2Blocked()) {
Log.d(LOG_TAG,"PUK2 is Blocked.Disabling Enable FDN,Change PIN2");
//Synthetic comment -- @@ -558,6 +551,5 @@
resetPinChangeState();
}
}
}









//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index c133023..11e6c0c 100644

//Synthetic comment -- @@ -190,6 +190,7 @@
boolean mShowBluetoothIndication = false;
static int mDockState = Intent.EXTRA_DOCK_STATE_UNDOCKED;
static boolean sVoiceCapable = true;
    public boolean mIsSimPukLocked;

// Internal PhoneApp Call state tracker
CdmaPhoneCallState cdmaPhoneCallState;
//Synthetic comment -- @@ -1680,6 +1681,14 @@
// been attempted.
mHandler.sendMessage(mHandler.obtainMessage(EVENT_SIM_STATE_CHANGED,
intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE)));
                String reason = intent.getStringExtra(IccCardConstants.INTENT_KEY_LOCKED_REASON);
                if (IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK.equals(reason)) {
                    Log.d(LOG_TAG, "Setting mIsSimPukLocked:true");
                    mIsSimPukLocked = true;
                } else {
                    Log.d(LOG_TAG, "Setting mIsSimPukLocked:false");
                    mIsSimPukLocked = false;
                }
} else if (action.equals(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED)) {
String newPhone = intent.getStringExtra(PhoneConstants.PHONE_NAME_KEY);
Log.d(LOG_TAG, "Radio technology switched. Now " + newPhone + " is active.");
//Synthetic comment -- @@ -2054,6 +2063,10 @@
return DEFAULT_SUBSCRIPTION;
}

    boolean isSimPukLocked(int subscription) {
        return mIsSimPukLocked;
    }

public int getVoiceSubscriptionInService() {
return DEFAULT_SUBSCRIPTION;
}








//Synthetic comment -- diff --git a/src/com/android/phone/SpecialCharSequenceMgr.java b/src/com/android/phone/SpecialCharSequenceMgr.java
//Synthetic comment -- index bb942f0..bcd5f21 100644

//Synthetic comment -- @@ -204,7 +204,32 @@
if ((input.startsWith("**04") || input.startsWith("**05"))
&& input.endsWith("#")) {
PhoneApp app = PhoneApp.getInstance();
            Phone phone = null;
            boolean isPukRequired = false;
            if (input.startsWith("**05")) {
                // Called when user tries to unblock PIN by entering the MMI code
                // through emergency dialer app. Send the request on the right
                // sub which is in PUK_REQUIRED state. Use the default subscription
                // when none of the subscriptions are PUK-Locked. This may be
                // a change PIN request.
                int numPhones = MSimTelephonyManager.getDefault().getPhoneCount();
                for (int i = 0; i < numPhones; i++) {
                    if (app.isSimPukLocked(i)) {
                        phone = app.getPhone(i);
                        log("Sending PUK on subscription :" + phone.getSubscription());
                        break;
                    }
                }
                if (phone == null) {
                    log("No Subscription is PUK-Locked..Using default phone");
                    phone = app.phone;
                }
            } else {
                // Change Pin request (**04). Use voice phone.
                int voiceSub = app.getVoiceSubscription();
                phone = app.getPhone(voiceSub);
            }
            boolean isMMIHandled = phone.handlePinMmi(input);

// if the PUK code is recognized then indicate to the
// phone app that an attempt to unPUK the device was








//Synthetic comment -- diff --git a/src/com/android/phone/msim/MSimPhoneApp.java b/src/com/android/phone/msim/MSimPhoneApp.java
//Synthetic comment -- index 697ce28..b444d6b 100755

//Synthetic comment -- @@ -50,6 +50,7 @@
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
//Synthetic comment -- @@ -540,6 +541,22 @@
getPhone(i).setRadioPower(!enabled);
}

            } else if ((action.equals(TelephonyIntents.ACTION_SIM_STATE_CHANGED)) &&
                    (mPUKEntryActivity != null)) {
                // if an attempt to un-PUK-lock the device was made, while we're
                // receiving this state change notification, notify the handler.
                // NOTE: This is ONLY triggered if an attempt to un-PUK-lock has
                // been attempted.
                mHandler.sendMessage(mHandler.obtainMessage(EVENT_SIM_STATE_CHANGED,
                        intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE)));
                String reason = intent.getStringExtra(IccCardConstants.INTENT_KEY_LOCKED_REASON);
                if (IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK.equals(reason)) {
                    Log.d(LOG_TAG, "Setting mIsSimPukLocked:true on sub :" + subscription);
                    getMSPhone(subscription).mIsSimPukLocked = true;
                } else {
                    Log.d(LOG_TAG, "Setting mIsSimPukLocked:false on sub :" + subscription);
                    getMSPhone(subscription).mIsSimPukLocked = false;
                }
} else if (action.equals(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED)) {
String newPhone = intent.getStringExtra(PhoneConstants.PHONE_NAME_KEY);
Log.d(LOG_TAG, "Radio technology switched. Now " + newPhone + " is active.");
//Synthetic comment -- @@ -654,6 +671,10 @@
}
}

    boolean isSimPukLocked(int subscription) {
        return getMSPhone(subscription).mIsSimPukLocked;
    }

/**
* Get the subscription that has service
* Following are the conditions applicable when deciding the subscription for dial








//Synthetic comment -- diff --git a/src/com/android/phone/msim/MSimPhoneInterfaceManager.java b/src/com/android/phone/msim/MSimPhoneInterfaceManager.java
//Synthetic comment -- index 81dbe9c..4873d17 100644

//Synthetic comment -- @@ -120,7 +120,6 @@
switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
request = (MainThreadRequest) msg.obj;
sub = (Integer) request.argument2;
Phone phone = PhoneApp.getPhone(sub);
Log.i(LOG_TAG,"CMD_HANDLE_PIN_MMI: sub :" + phone.getSubscription());
//Synthetic comment -- @@ -165,7 +164,6 @@
case CMD_END_CALL:
request = (MainThreadRequest) msg.obj;
boolean hungUp = false;
sub = (Integer) request.argument;
log("Ending call on subscription =" + sub);
phone = mApp.getPhone(sub);
//Synthetic comment -- @@ -190,7 +188,6 @@

case CMD_SET_DATA_SUBSCRIPTION:
request = (MainThreadRequest) msg.obj;
int subscription = (Integer) request.argument;
onCompleted = obtainMessage(EVENT_SET_DATA_SUBSCRIPTION_DONE, request);
SubscriptionManager subManager = SubscriptionManager.getInstance();
//Synthetic comment -- @@ -492,7 +489,17 @@
return ((MSimPhoneApp)mApp).isSimPinEnabled(subscription);
}

    public boolean isSimPukLocked(int subscription) {
        enforceReadPermission();
        return ((MSimPhoneApp)mApp).isSimPukLocked(subscription);
    }

public boolean supplyPin(String pin, int subscription) {
        return (supplyPinReportResult(pin, subscription) == PhoneContants.PIN_RESULT_SUCCESS)
            ? true : false;
    }

    public int supplyPinReportResult(String pin, int subscription) {
enforceModifyPermission();
final UnlockSim checkSimPin = new UnlockSim(getPhone(subscription).getIccCard());
checkSimPin.start();
//Synthetic comment -- @@ -500,6 +507,11 @@
}

public boolean supplyPuk(String puk, String pin, int subscription) {
        return (supplyPukReportResult(puk, pin, subscription) == PhoneContants.PIN_RESULT_SUCCESS)
            ? true : false;
    }

    public int supplyPukReportResult(String puk, String pin, int subscription) {
enforceModifyPermission();
final UnlockSim checkSimPuk = new UnlockSim(getPhone(subscription).getIccCard());
checkSimPuk.start();
//Synthetic comment -- @@ -515,7 +527,7 @@
private final IccCard mSimCard;

private boolean mDone = false;
        private int mResult = PhoneContants.PIN_RESULT_SUCCESS;

// For replies from SimCard interface
private Handler mHandler;
//Synthetic comment -- @@ -539,7 +551,17 @@
case SUPPLY_PIN_COMPLETE:
Log.d(LOG_TAG, "SUPPLY_PIN_COMPLETE");
synchronized (UnlockSim.this) {
                                    if (ar.exception != null) {
                                        if (ar.exception instanceof CommandException &&
                                                ((CommandException)(ar.exception)).getCommandError()
                                                == CommandException.Error.PASSWORD_INCORRECT) {
                                            mResult = PhoneContants.PIN_PASSWORD_INCORRECT;
                                        } else {
                                            mResult = PhoneContants.PIN_GENERAL_FAILURE;
                                        }
                                    } else {
                                        mResult = PhoneContants.PIN_RESULT_SUCCESS;
                                    }
mDone = true;
UnlockSim.this.notifyAll();
}
//Synthetic comment -- @@ -559,7 +581,7 @@
*
* If PUK is not null, unlock SIM card with PUK and set PIN code
*/
        synchronized int unlockSim(String puk, String pin) {

while (mHandler == null) {
try {
//Synthetic comment -- @@ -742,6 +764,10 @@
return (List <NeighboringCellInfo>) cells;
}

    // Gets the retry count during PIN1/PUK1 verification.
    public int getIccPin1RetryCount(int subscription) {
        return getPhone(subscription).getIccCard().getIccPin1RetryCount();
    }

public List<CellInfo> getAllCellInfo() {
try {







