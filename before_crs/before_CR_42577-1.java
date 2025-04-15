/*Add an API for Call Barring services

Call Barring services are specified in 3GPP TS 22.088 and allows
an user to have barring of certain categories of outgoing or
incoming calls. This commit adds an API for the framework for this
purpose.

Change-Id:I5f9f2b35749377dcc430318018ac5558a25af62a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 34aa96c..4fce9bd 100644

//Synthetic comment -- @@ -942,6 +942,50 @@
Message onComplete);

/**
* getOutgoingCallerIdDisplay
* gets outgoing caller id display. The return value of
* ((AsyncResult)onComplete.obj) is an array of int, with a length of 2.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77135d4..c659692 100644

//Synthetic comment -- @@ -611,6 +611,21 @@
commandInterfaceCFAction, dialingNumber, timerSeconds, onComplete);
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
mActivePhone.getOutgoingCallerIdDisplay(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 227b406..4e02547 100755

//Synthetic comment -- @@ -782,6 +782,21 @@
Log.e(LOG_TAG, "setCallForwardingOption: not possible in CDMA");
}

public void
getOutgoingCallerIdDisplay(Message onComplete) {
Log.e(LOG_TAG, "getOutgoingCallerIdDisplay: not possible in CDMA");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b429cd2..a74f38e 100644

//Synthetic comment -- @@ -960,6 +960,21 @@
}
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
mCM.getCLIR(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index b0a6080..b4b1b7e 100755

//Synthetic comment -- @@ -316,6 +316,21 @@
int timerSeconds, Message onComplete) {
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
// FIXME: what to reply?
AsyncResult.forMessage(onComplete, null, null);







