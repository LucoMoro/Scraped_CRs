/*Telephony: Add support for sending DTMF codes.

Implementations added to PhoneInterfaceManager:
- TelephonyManager sendDtmf, startDtmf, stopDtmf interface implementations forward actual work to CallManager.

Change-Id:Id30bce303c5b45f5ef7cdebe1246ab6006075407*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index ab6011c..2b40d60 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -57,6 +57,9 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;

/** The singleton instance. */
private static PhoneInterfaceManager sInstance;
//Synthetic comment -- @@ -163,6 +166,39 @@
}
break;

default:
Log.w(LOG_TAG, "MainThreadHandler: unexpected message code: " + msg.what);
break;
//Synthetic comment -- @@ -794,4 +830,59 @@
public int getLteOnCdmaMode() {
return mPhone.getLteOnCdmaMode();
}
}







