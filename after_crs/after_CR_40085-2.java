/*Telephony: Use IccUtils from uicc packge

Change-Id:I3b718b9aea1f21c7906c8243b4ca0db6af495a08*/




//Synthetic comment -- diff --git a/tests/src/com/android/cellbroadcastreceiver/DialogSmsDisplayTests.java b/tests/src/com/android/cellbroadcastreceiver/DialogSmsDisplayTests.java
//Synthetic comment -- index 095ce06..b2150f1 100644

//Synthetic comment -- @@ -27,8 +27,8 @@

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmSmsCbMessage;
import com.android.internal.telephony.uicc.IccUtils;

/**
* Various instrumentation tests for CellBroadcastReceiver.








//Synthetic comment -- diff --git a/tests/src/com/android/cellbroadcastreceiver/tests/SendCdmaCmasMessages.java b/tests/src/com/android/cellbroadcastreceiver/tests/SendCdmaCmasMessages.java
//Synthetic comment -- index 17f76cb..43649d9 100644

//Synthetic comment -- @@ -27,11 +27,11 @@
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.BitwiseOutputStream;

import java.util.Arrays;








//Synthetic comment -- diff --git a/tests/src/com/android/cellbroadcastreceiver/tests/SendTestMessages.java b/tests/src/com/android/cellbroadcastreceiver/tests/SendTestMessages.java
//Synthetic comment -- index a8496ff..e634b23 100644

//Synthetic comment -- @@ -25,8 +25,8 @@

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmSmsCbMessage;
import com.android.internal.telephony.uicc.IccUtils;

import java.io.UnsupportedEncodingException;








