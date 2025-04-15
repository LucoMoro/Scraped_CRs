/*Telephony: Mark SIM SMS as read after importing it

The GCF test case 8.2.2 checks if the status byte of the SMS is marked as read
after reading the SMS from SIM. The status byte is marked as read after the
SMS is imported from the SIM if it is not already marked.
Fix to check for null IccFilehandler which can happen if card is not present.

Change-Id:I4f226bc873786904527d7d822a67dfc14f05a327*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java b/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java
//Synthetic comment -- index 5fef6de..8aacc00 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.util.List;

import static android.telephony.SmsManager.STATUS_ON_ICC_FREE;

/**
* IccSmsInterfaceManager to provide an inter-process communication to
//Synthetic comment -- @@ -41,6 +43,39 @@
mContext = phone.getContext();
}

protected void enforceReceiveAndSend(String message) {
mContext.enforceCallingPermission(
"android.permission.RECEIVE_SMS", message);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java
//Synthetic comment -- index 9cd059d..0681bc2 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.util.Log;

import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.PhoneProxy;
//Synthetic comment -- @@ -69,6 +70,8 @@
synchronized (mLock) {
if (ar.exception == null) {
mSms = buildValidRawData((ArrayList<byte[]>) ar.result);
} else {
if(DBG) log("Cannot load Sms records");
if (mSms != null)
//Synthetic comment -- @@ -122,11 +125,17 @@
if (status == STATUS_ON_ICC_FREE) {
// Special case FREE: call deleteSmsOnRuim instead of
// manipulating the RUIM record
mPhone.mCM.deleteSmsOnRuim(index, response);
} else {
byte[] record = makeSmsRecordData(status, pdu);
                mPhone.getIccFileHandler().updateEFLinearFixed(
                        IccConstants.EF_SMS, index, record, null, response);
}
try {
mLock.wait();
//Synthetic comment -- @@ -179,8 +188,17 @@
"android.permission.RECEIVE_SMS",
"Reading messages from RUIM");
synchronized(mLock) {
Message response = mHandler.obtainMessage(EVENT_LOAD_DONE);
            mPhone.getIccFileHandler().loadEFLinearFixedAll(IccConstants.EF_SMS, response);

try {
mLock.wait();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 92bf390..86697d9 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.util.Log;

import com.android.internal.telephony.IccConstants;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.IntRangeManager;
//Synthetic comment -- @@ -81,7 +82,9 @@
ar = (AsyncResult)msg.obj;
synchronized (mLock) {
if (ar.exception == null) {
                            mSms  = buildValidRawData((ArrayList<byte[]>) ar.result);
} else {
if(DBG) log("Cannot load Sms records");
if (mSms != null)
//Synthetic comment -- @@ -144,10 +147,17 @@
if (status == STATUS_ON_ICC_FREE) {
// Special case FREE: call deleteSmsOnSim instead of
// manipulating the SIM record
mPhone.mCM.deleteSmsOnSim(index, response);
} else {
byte[] record = makeSmsRecordData(status, pdu);
                mPhone.getIccFileHandler().updateEFLinearFixed(
IccConstants.EF_SMS,
index, record, null, response);
}
//Synthetic comment -- @@ -204,8 +214,17 @@
"android.permission.RECEIVE_SMS",
"Reading messages from SIM");
synchronized(mLock) {
Message response = mHandler.obtainMessage(EVENT_LOAD_DONE);
            mPhone.getIccFileHandler().loadEFLinearFixedAll(IccConstants.EF_SMS, response);

try {
mLock.wait();







