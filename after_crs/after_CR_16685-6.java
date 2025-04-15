/*Telephony: Call forwarding icon is shown after powerup

If call forwarding was enabled, then the call forwarding
icon should be shown after the phone has powered down and
powered up. Added a preference parameter to store the icon
status, in case it is not updated correctly by the network
operator.

Change-Id:I4f02ff863418c8c72a48631c8134811da9b31a3a*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index fc011c0..76d09e3 100644

//Synthetic comment -- @@ -367,4 +367,12 @@
public UsimServiceTable getUsimServiceTable() {
return null;
}

    /**
    * Check if call forward info is stored on sim
    * @return true if call forward info is stored on sim.
    */
    public boolean isCallForwardStatusStored() {
         return false;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 94f7a13..5a87841 100644

//Synthetic comment -- @@ -103,6 +103,12 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    // Event constant for notification when the SIM card is ready
    protected static final int EVENT_SIM_READY = 27;
    // Event constant for retrieving the IMSI
    protected static final int EVENT_GOT_IMSI = 28;
    // Event constant for checking if Call Forwarding is enabled
    protected static final int EVENT_CHECK_CALLFORWARDING_STATUS = 29;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 5c95e7d..bce78c9 100644

//Synthetic comment -- @@ -95,6 +95,8 @@
public static final String VM_NUMBER = "vm_number_key";
// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";
    // Key used to read/write if Call Forwarding is enabled
    public static final String CF_ENABLED = "cf_enabled_key";

// Instance Variables
GsmCallTracker mCT;
//Synthetic comment -- @@ -118,6 +120,8 @@
private String mImeiSv;
private String mVmNumber;

    private boolean mCFOnBootDone = false;
    private boolean mCFOnBootSim = false;

// Constructors

//Synthetic comment -- @@ -155,6 +159,7 @@
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
        mCM.registerForSIMReady(this, EVENT_SIM_READY, null);
mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);

if (false) {
//Synthetic comment -- @@ -206,6 +211,7 @@
mIccRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
            mCM.unregisterForSIMReady(this); //EVENT_SIM_READY
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnUSSD(this);
mCM.unSetOnSuppServiceNotification(this);
//Synthetic comment -- @@ -282,6 +288,16 @@
return mSST;
}

    @Override
    public boolean getCallForwardingIndicator() {
        boolean cf = false;
        cf = mIccRecords.getVoiceCallForwardingFlag();
        if (!cf) {
            cf = retrieveCFPref();
        }
        return cf;
    }

public List<? extends MmiCode>
getPendingMmiCodes() {
return mPendingMMIs;
//Synthetic comment -- @@ -957,6 +973,20 @@
}
}

    /**
     * This method stores the CF_ENABLED flag in preferences
     * @param enabled
     */
    public void storeCFPref(boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(CF_ENABLED, enabled);
        edit.commit();

        // Using the same method as VoiceMail to be able to track when the sim card is changed.
        setVmSimImsi(getSubscriberId());
    }

public void getOutgoingCallerIdDisplay(Message onComplete) {
mCM.getCLIR(onComplete);
}
//Synthetic comment -- @@ -1087,6 +1117,11 @@
}
}

    private boolean retrieveCFPref() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean cf = sp.getBoolean(CF_ENABLED, false);
        return cf;
    }

private void
onNetworkInitiatedUssd(GsmMmiCode mmi) {
//Synthetic comment -- @@ -1186,6 +1221,7 @@
storeVoiceMailNumber(null);
setVmSimImsi(null);
}
                onBootCallForwardStatus(EVENT_SIM_RECORDS_LOADED);

break;

//Synthetic comment -- @@ -1256,6 +1292,7 @@
case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
                    storeCFPref(msg.arg1 == 1);
mIccRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
//Synthetic comment -- @@ -1289,6 +1326,7 @@
AsyncResult.forMessage(onComplete, ar.result, ar.exception);
onComplete.sendToTarget();
}
                mCFOnBootDone = true;
break;

// handle the select network completion callbacks.
//Synthetic comment -- @@ -1309,6 +1347,32 @@
}
break;

            case EVENT_CHECK_CALLFORWARDING_STATUS:
                boolean cfEnabled = retrieveCFPref();
                if (cfEnabled) {
                    notifyCallForwardingIndicator();
                }
                break;

            case EVENT_SIM_READY:
                mCM.getIMSI(obtainMessage(EVENT_GOT_IMSI));
                break;

            case EVENT_GOT_IMSI:
                ar = (AsyncResult)msg.obj;
                if (ar.exception != null) {
                    Log.e(LOG_TAG, "Exception querying IMSI, Exception:" + ar.exception);
                    break;
                }
                String imsiNbr = getVmSimImsi();
                String subId = (String)ar.result;

                //If it is a different sim than before reset the Call Forwarding flag.
                if (imsiNbr != null && !subId.equals(imsiNbr)) {
                    storeCFPref(false);
                }
                break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1335,6 +1399,33 @@
}

/**
     * Used to check if Call Forwarding status is present on sim card. If not, a message is
     * sent so we can check if the CF status is stored as a Shared Preference.
     */
    void onBootCallForwardStatus(int caller) {
        if (!mCFOnBootDone) {
            if (caller == EVENT_SIM_RECORDS_LOADED) {
                if (LOCAL_DEBUG) {
                    Log.d(LOG_TAG, "onBootCallForwardStatus got sim records");
                }
                if (mIccRecords != null &&  mIccRecords.isCallForwardStatusStored()) {
                    //The Sim card has the CF info, so we dont need to check with the network
                    if (LOCAL_DEBUG) {
                        Log.d(LOG_TAG, "info is present on sim");
                    }
                    mCFOnBootDone = true;
                }
                mCFOnBootSim = true;
            }

            if (!mCFOnBootDone && mCFOnBootSim) {
                Message msg = obtainMessage(EVENT_CHECK_CALLFORWARDING_STATUS);
                sendMessage(msg);
            }
        }
    }

    /**
* Used to track the settings upon completion of the network change.
*/
private void handleSetSelectNetwork(AsyncResult ar) {
//Synthetic comment -- @@ -1389,10 +1480,12 @@
if (infos == null || infos.length == 0) {
// Assume the default is not active
// Set unconditional CFF in SIM to false
            storeCFPref(false);
mIccRecords.setVoiceCallForwardingFlag(1, false);
} else {
for (int i = 0, s = infos.length; i < s; i++) {
if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                    storeCFPref((infos[i].status == 1));
mIccRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
// should only have the one
break;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 16d3129..74748d1 100644

//Synthetic comment -- @@ -885,6 +885,7 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    phone.storeCFPref(cffEnabled);
phone.mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1203,6 +1204,7 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            phone.storeCFPref(cffEnabled);
phone.mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1228,6 +1230,7 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                phone.storeCFPref(false);
phone.mIccRecords.setVoiceCallForwardingFlag(1, false);
} else {









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 495b5bc..9aac7aa1 100755

//Synthetic comment -- @@ -418,6 +418,14 @@
}

/**
    * Check if call forward info is stored on sim
    * @return true if call forward info is stored on sim.
    */
    public boolean isCallForwardStatusStored() {
        return mEfCfis != null || mEfCff != null;
    }

    /**
* {@inheritDoc}
*/
@Override







