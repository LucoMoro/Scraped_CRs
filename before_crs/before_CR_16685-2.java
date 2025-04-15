/*Telephony : Call forwarding icon is shown after powerup

If call forwarding was enabled, then the call forwarding
icon should be shown after the phone has powered down and
powered up. Added a preference parameter to store the icon
status, in case it is not updated correctly by the network
operator.

Change-Id:I4f02ff863418c8c72a48631c8134811da9b31a3a*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..37a841a 100644

//Synthetic comment -- @@ -95,6 +95,15 @@
public static final String VM_NUMBER = "vm_number_key";
// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

// Instance Variables
GsmCallTracker mCT;
//Synthetic comment -- @@ -125,6 +134,8 @@
private String mImeiSv;
private String mVmNumber;


// Constructors

//Synthetic comment -- @@ -163,6 +174,7 @@
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);

if (false) {
//Synthetic comment -- @@ -213,6 +225,7 @@
mSIMRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnUSSD(this);
mCM.unSetOnSuppServiceNotification(this);
//Synthetic comment -- @@ -283,7 +296,12 @@
}

public boolean getCallForwardingIndicator() {
        return mSIMRecords.getVoiceCallForwardingFlag();
}

public List<? extends MmiCode>
//Synthetic comment -- @@ -961,6 +979,20 @@
}
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
mCM.getCLIR(onComplete);
}
//Synthetic comment -- @@ -1133,6 +1165,11 @@
}
}


private void
onNetworkInitiatedUssd(GsmMmiCode mmi) {
//Synthetic comment -- @@ -1232,6 +1269,7 @@
storeVoiceMailNumber(null);
setVmSimImsi(null);
}

break;

//Synthetic comment -- @@ -1302,6 +1340,7 @@
case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
//Synthetic comment -- @@ -1335,6 +1374,7 @@
AsyncResult.forMessage(onComplete, ar.result, ar.exception);
onComplete.sendToTarget();
}
break;

// handle the select network completion callbacks.
//Synthetic comment -- @@ -1355,6 +1395,32 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1381,6 +1447,33 @@
}

/**
* Used to track the settings upon completion of the network change.
*/
private void handleSetSelectNetwork(AsyncResult ar) {
//Synthetic comment -- @@ -1435,10 +1528,12 @@
if (infos == null || infos.length == 0) {
// Assume the default is not active
// Set unconditional CFF in SIM to false
mSIMRecords.setVoiceCallForwardingFlag(1, false);
} else {
for (int i = 0, s = infos.length; i < s; i++) {
if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
mSIMRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
// should only have the one
break;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fe7a5cb..41972e9 100644

//Synthetic comment -- @@ -860,6 +860,7 @@
*/
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1178,6 +1179,7 @@
(info.serviceClass & serviceClassMask)
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
phone.mSIMRecords.setVoiceCallForwardingFlag(1, cffEnabled);
}

//Synthetic comment -- @@ -1203,6 +1205,7 @@
sb.append(context.getText(com.android.internal.R.string.serviceDisabled));

// Set unconditional CFF in SIM to false
phone.mSIMRecords.setVoiceCallForwardingFlag(1, false);
} else {









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..ba3e217 100644

//Synthetic comment -- @@ -384,6 +384,14 @@
}
}

public boolean getVoiceCallForwardingFlag() {
return callForwardingEnabled;
}







