/*Telephony: Call forwarding icon is shown after powerup

If call forwarding was enabled, then the call forwarding
icon should be shown after the phone has powered down and
powered up. Added a preference parameter to store the icon
status, in case it is not updated correctly by the network
operator.

Change-Id:I4f02ff863418c8c72a48631c8134811da9b31a3a*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3959c67..91490b5 100644

//Synthetic comment -- @@ -95,6 +95,11 @@
public static final String VM_NUMBER = "vm_number_key";
// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";
    // Key used to read/write if Call Forwarding is enabled
    public static final String CF_ENABLED = "cf_enabled_key";

    // Event constant for checking if Call Forwarding is enabled
    private static final int CHECK_CALLFORWARDING_STATUS = 75;

// Instance Variables
GsmCallTracker mCT;
//Synthetic comment -- @@ -279,7 +284,12 @@
}

public boolean getCallForwardingIndicator() {
        boolean cf = false;
        cf = mSIMRecords.getVoiceCallForwardingFlag();
        if (!cf) {
            cf = retrieveCFPref();
        }
        return cf;
}

public List<? extends MmiCode>
//Synthetic comment -- @@ -957,6 +967,20 @@
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
//Synthetic comment -- @@ -1129,6 +1153,11 @@
}
}

    private boolean retrieveCFPref() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean cf = sp.getBoolean(CF_ENABLED, false);
        return cf;
    }

private void
onNetworkInitiatedUssd(GsmMmiCode mmi) {
//Synthetic comment -- @@ -1221,13 +1250,15 @@
updateCurrentCarrierInProvider();

// Check if this is a different SIM than the previous one. If so unset the
                // voice mail number and the call forwarding flag.
String imsi = getVmSimImsi();
String imsiFromSIM = getSubscriberId();
if (imsi != null && imsiFromSIM != null && !imsiFromSIM.equals(imsi)) {
storeVoiceMailNumber(null);
                    storeCFPref(false);
setVmSimImsi(null);
}
                updateCallForwardStatus(EVENT_SIM_RECORDS_LOADED);

break;

//Synthetic comment -- @@ -1298,6 +1329,7 @@
case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
                    storeCFPref(msg.arg1 == 1);
mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
//Synthetic comment -- @@ -1351,6 +1383,13 @@
}
break;

            case CHECK_CALLFORWARDING_STATUS:
                boolean cfEnabled = retrieveCFPref();
                if (cfEnabled) {
                    notifyCallForwardingIndicator();
                }
                break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1377,6 +1416,27 @@
}

/**
     * Used to check if Call Forwarding status is present on sim card. If not, a message is
     * sent so we can check if the CF status is stored as a Shared Preference.
     */
    void updateCallForwardStatus(int caller) {
        if (caller == EVENT_SIM_RECORDS_LOADED) {
            if (LOCAL_DEBUG) {
                Log.d(LOG_TAG, "updateCallForwardStatus got sim records");
            }
            if (mSIMRecords != null && mSIMRecords.isCallForwardStatusStored()) {
                // The Sim card has the CF info
                if (LOCAL_DEBUG) {
                    Log.d(LOG_TAG, "info is present on sim");
                }
            } else {
                Message msg = obtainMessage(CHECK_CALLFORWARDING_STATUS);
                sendMessage(msg);
            }
        }
    }

    /**
* Used to track the settings upon completion of the network change.
*/
private void handleSetSelectNetwork(AsyncResult ar) {
//Synthetic comment -- @@ -1431,10 +1491,12 @@
if (infos == null || infos.length == 0) {
// Assume the default is not active
// Set unconditional CFF in SIM to false
            storeCFPref(false);
mSIMRecords.setVoiceCallForwardingFlag(1, false);
} else {
for (int i = 0, s = infos.length; i < s; i++) {
if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                    storeCFPref(infos[i].status == 1);
mSIMRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
// should only have the one
break;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fe7a5cb..41972e9 100644

//Synthetic comment -- @@ -860,6 +860,7 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
                    phone.storeCFPref(cffEnabled);
phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1178,6 +1179,7 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
            phone.storeCFPref(cffEnabled);
phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1203,6 +1205,7 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
                phone.storeCFPref(false);
phone.mSIMRecords.setVoiceCallForwardingFlag(1, false);
} else {









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 438996f..9e9cff1 100644

//Synthetic comment -- @@ -384,6 +384,14 @@
}
}

    /**
     * Check if call forward info is stored on sim
     * @return true if call forward info is stored on sim.
     */
    public boolean isCallForwardStatusStored() {
        return (mEfCfis != null) || (mEfCff != null);
    }

public boolean getVoiceCallForwardingFlag() {
return callForwardingEnabled;
}







