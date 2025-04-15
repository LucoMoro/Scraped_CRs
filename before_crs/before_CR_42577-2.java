/*Add an API for Call Barring services

Call Barring services are specified in 3GPP TS 22.088 and allows
an user to have barring of certain categories of outgoing or
incoming calls. This commit adds an API for the framework for this
purpose.

Change-Id:I5f9f2b35749377dcc430318018ac5558a25af62a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index fbce476..0cb432d 100644

//Synthetic comment -- @@ -952,6 +952,50 @@
Message onComplete);

/**
* getOutgoingCallerIdDisplay
* gets outgoing caller id display. The return value of
* ((AsyncResult)onComplete.obj) is an array of int, with a length of 2.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 300529b..c4b0956 100644

//Synthetic comment -- @@ -631,6 +631,21 @@
commandInterfaceCFAction, dialingNumber, timerSeconds, onComplete);
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
mActivePhone.getOutgoingCallerIdDisplay(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1465a17..a967c47 100755

//Synthetic comment -- @@ -783,6 +783,21 @@
Rlog.e(LOG_TAG, "setCallForwardingOption: not possible in CDMA");
}

public void
getOutgoingCallerIdDisplay(Message onComplete) {
Rlog.e(LOG_TAG, "getOutgoingCallerIdDisplay: not possible in CDMA");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b930348..c238eed 100644

//Synthetic comment -- @@ -959,6 +959,21 @@
}
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
mCM.getCLIR(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index 041ac79..e1c2675 100755

//Synthetic comment -- @@ -325,6 +325,21 @@
int timerSeconds, Message onComplete) {
}

public void getOutgoingCallerIdDisplay(Message onComplete) {
// FIXME: what to reply?
AsyncResult.forMessage(onComplete, null, null);







