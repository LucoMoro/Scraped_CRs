/*Telephony: Add uicc debug info to DebugService

Change-Id:I03be52f97ea84808d608c48b4e6ffa2454e2a67c*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DebugService.java b/src/java/com/android/internal/telephony/DebugService.java
//Synthetic comment -- index 82543ae..deba3eb 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import android.telephony.Rlog;

import java.io.FileDescriptor;
import java.io.PrintWriter;

//Synthetic comment -- @@ -99,6 +102,20 @@
}
pw.flush();
pw.println("++++++++++++++++++++++++++++++++");
log("dump: -");
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af81e04..c3256df 100644

//Synthetic comment -- @@ -3872,7 +3872,7 @@
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("RIL:");
pw.println(" mSocket=" + mSocket);
pw.println(" mSenderThread=" + mSenderThread);
pw.println(" mSender=" + mSender);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccCardProxy.java b/src/java/com/android/internal/telephony/uicc/IccCardProxy.java
//Synthetic comment -- index 72da49c..f1fae9d 100644

//Synthetic comment -- @@ -46,6 +46,9 @@
import com.android.internal.telephony.uicc.IccCardStatus.PinState;
import com.android.internal.telephony.uicc.UiccController;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_SIM_STATE;

/**
//Synthetic comment -- @@ -699,4 +702,34 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccRecords.java b/src/java/com/android/internal/telephony/uicc/IccRecords.java
//Synthetic comment -- index a690e93..2f326aa 100644

//Synthetic comment -- @@ -25,6 +25,8 @@

import com.android.internal.telephony.CommandsInterface;

import java.util.concurrent.atomic.AtomicBoolean;

/**
//Synthetic comment -- @@ -442,4 +444,49 @@
public UsimServiceTable getUsimServiceTable() {
return null;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IsimUiccRecords.java b/src/java/com/android/internal/telephony/uicc/IsimUiccRecords.java
//Synthetic comment -- index 212cb0c..2330aa8a 100644

//Synthetic comment -- @@ -26,8 +26,11 @@
import com.android.internal.telephony.gsm.SimTlv;
//import com.android.internal.telephony.gsm.VoiceMailConstants;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.android.internal.telephony.uicc.IccConstants.EF_DOMAIN;
import static com.android.internal.telephony.uicc.IccConstants.EF_IMPI;
//Synthetic comment -- @@ -263,4 +266,15 @@
protected void loge(String s) {
if (DBG) Rlog.e(LOG_TAG, "[ISIM] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/RuimRecords.java b/src/java/com/android/internal/telephony/uicc/RuimRecords.java
//Synthetic comment -- index 4c8cf06..c7d1070 100755

//Synthetic comment -- @@ -23,7 +23,10 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_TEST_CSIM;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Context;
import android.os.AsyncResult;
//Synthetic comment -- @@ -797,4 +800,23 @@
protected void loge(String s) {
Rlog.e(LOG_TAG, "[RuimRecords] " + s);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/SIMRecords.java b/src/java/com/android/internal/telephony/uicc/SIMRecords.java
//Synthetic comment -- index 6f226b8..66eaf6a 100755

//Synthetic comment -- @@ -37,7 +37,10 @@
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.gsm.SmsMessage;

import java.util.ArrayList;


/**
//Synthetic comment -- @@ -1673,4 +1676,26 @@

log("[CSP] Value Added Service Group (0xC0), not found!");
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccCard.java b/src/java/com/android/internal/telephony/uicc/UiccCard.java
//Synthetic comment -- index 8da07b8..bc211ed 100644

//Synthetic comment -- @@ -49,6 +49,9 @@

import com.android.internal.R;

/**
* {@hide}
*/
//Synthetic comment -- @@ -350,4 +353,49 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java b/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java
//Synthetic comment -- index 2a349f7..bde5925 100644

//Synthetic comment -- @@ -30,6 +30,9 @@
import com.android.internal.telephony.uicc.IccCardApplicationStatus.PersoSubState;
import com.android.internal.telephony.uicc.IccCardStatus.PinState;

/**
* {@hide}
*/
//Synthetic comment -- @@ -691,4 +694,38 @@
private void loge(String msg) {
Rlog.e(LOG_TAG, msg);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index d486743..b46f180 100644

//Synthetic comment -- @@ -26,6 +26,9 @@

import com.android.internal.telephony.CommandsInterface;

/**
* This class is responsible for keeping all knowledge about
* Universal Integrated Circuit Card (UICC), also know as SIM's,
//Synthetic comment -- @@ -218,4 +221,21 @@
private void log(String string) {
Rlog.d(LOG_TAG, string);
}
}







