/*apps/Phone: Add a RIL interface to change the transmit power

Change-Id:Ic450bc4359b8d78c610f0828c44f3af6d3f289b9*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..3f062bd 100644

//Synthetic comment -- @@ -54,6 +54,8 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;

PhoneApp mApp;
Phone mPhone;
//Synthetic comment -- @@ -93,6 +95,7 @@
MainThreadRequest request;
Message onCompleted;
AsyncResult ar;

switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
//Synthetic comment -- @@ -157,6 +160,28 @@
}
break;

default:
Log.w(LOG_TAG, "MainThreadHandler: unexpected message code: " + msg.what);
break;
//Synthetic comment -- @@ -730,4 +755,17 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}
}







