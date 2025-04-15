/*UICC support for SIMalliance Open Mobile API for Secure Element access

The SIMalliance Open Mobile API specifies the access to any supported secure
element from a mobile application. This patch adds support for the UICC.
Since the UICC is linked to the baseband processor, and not to the application
processor, access to the UICC must be supported by the baseband firmware.
For this reason, this patch extends the Android emulator's virtual modem by
some required AT commands (AT+CSIM, AT+CCHO, AT+CCHC, and AT+CGLA as defined
in 3GPP TS 27.007), and extends the RIL appropriate. For security reasons
access to the UICC is limited to the SIMalliance Open Mobile API.
This patch also adds support for using a hardware UICC inserted to a PC/SC
card reader instead of the emulator's virtual UICC.

The SIMalliance Open Mobile API was contributed under the following change IDs:Ib014d041950494cc1900a7206093f87d7b520d43,I05469de464a21f20efdc7da892f4ccfcedc4b2b8, andI30e78f50542fa8df87897806fc015f4447e02a62Change-Id:I4c15eaf4c80b20f1f1f457e831006f6581508c17Signed-off-by: Robert H. <robert.hockauf@gi-de.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..8d70955 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
//Synthetic comment -- @@ -36,9 +37,15 @@
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.CallManager;

import java.util.List;
import java.util.ArrayList;

/**
* Implementation of the ITelephony interface.
//Synthetic comment -- @@ -54,12 +61,20 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;

PhoneApp mApp;
Phone mPhone;
CallManager mCM;
MainThreadHandler mMainThreadHandler;

/**
* A request object for use with {@link MainThreadHandler}. Requesters should wait() on the
* request after sending. The main thread will notify the request when it is complete.
//Synthetic comment -- @@ -75,6 +90,23 @@
}
}

/**
* A handler that processes messages on the main thread in the phone process. Since many
* of the Phone calls are not thread safe this is needed to shuttle the requests from the
//Synthetic comment -- @@ -95,6 +127,81 @@
AsyncResult ar;

switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
request = (MainThreadRequest) msg.obj;
request.result = Boolean.valueOf(
//Synthetic comment -- @@ -730,4 +837,57 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}
}







