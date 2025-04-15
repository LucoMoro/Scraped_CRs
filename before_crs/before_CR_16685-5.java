/*Telephony: Call forwarding icon is shown after powerup

If call forwarding was enabled, then the call forwarding
icon should be shown after the phone has powered down and
powered up. Added a preference parameter to store the icon
status, in case it is not updated correctly by the network
operator.

Change-Id:I4f02ff863418c8c72a48631c8134811da9b31a3a*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3959c67..8f314fc 100644

//Synthetic comment -- @@ -95,6 +95,11 @@
public static final String VM_NUMBER = "vm_number_key";
// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

// Instance Variables
GsmCallTracker mCT;
//Synthetic comment -- @@ -279,7 +284,12 @@
}

public boolean getCallForwardingIndicator() {
        return mSIMRecords.getVoiceCallForwardingFlag();
}

public List<? extends MmiCode>
//Synthetic comment -- @@ -1129,8 +1139,48 @@
}
}


    private void
onNetworkInitiatedUssd(GsmMmiCode mmi) {
mMmiCompleteRegistrants.notifyRegistrants(
new AsyncResult(null, mmi, null));
//Synthetic comment -- @@ -1221,13 +1271,15 @@
updateCurrentCarrierInProvider();

// Check if this is a different SIM than the previous one. If so unset the
                // voice mail number.
String imsi = getVmSimImsi();
String imsiFromSIM = getSubscriberId();
if (imsi != null && imsiFromSIM != null && !imsiFromSIM.equals(imsi)) {
storeVoiceMailNumber(null);
setVmSimImsi(null);
}

break;

//Synthetic comment -- @@ -1298,6 +1350,7 @@
case EVENT_SET_CALL_FORWARD_DONE:
ar = (AsyncResult)msg.obj;
if (ar.exception == null) {
mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
}
onComplete = (Message) ar.userObj;
//Synthetic comment -- @@ -1351,6 +1404,13 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1431,10 +1491,12 @@
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
//Synthetic comment -- index fe7a5cb..42093ae 100644

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
//Synthetic comment -- index 438996f..9e9cff1 100644

//Synthetic comment -- @@ -384,6 +384,14 @@
}
}

public boolean getVoiceCallForwardingFlag() {
return callForwardingEnabled;
}







