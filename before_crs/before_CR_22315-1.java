/*Load mnc/mcc configuration value from SIM (KDDI-CDMA)

Now support to load mnc/mcc value from IMSI on KDDI-CDMA.

Change-Id:I6f1d137ecf2e3130ca8f1a782dccec13b51f9933*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
old mode 100644
new mode 100755
//Synthetic comment -- index 87b0c60..3429099

//Synthetic comment -- @@ -16,10 +16,13 @@

package com.android.internal.telephony.cdma;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
//Synthetic comment -- @@ -59,6 +62,7 @@

private static final int EVENT_RUIM_READY = 1;
private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
private static final int EVENT_GET_ICCID_DONE = 5;
private static final int EVENT_GET_CDMA_SUBSCRIPTION_DONE = 10;
//Synthetic comment -- @@ -114,6 +118,9 @@

adnCache.reset();

// recordsRequested is set to false indicating that the SIM
// read requests made so far are not valid. This is set to
// true only when fresh set of read requests are made.
//Synthetic comment -- @@ -201,6 +208,33 @@
break;

/* IO events */

case EVENT_GET_CDMA_SUBSCRIPTION_DONE:
ar = (AsyncResult)msg.obj;
//Synthetic comment -- @@ -291,6 +325,13 @@

// Further records that can be inserted are Operator/OEM dependent

recordsLoadedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
((CDMAPhone) phone).mRuimCard.broadcastIccStateChangedIntent(
//Synthetic comment -- @@ -317,6 +358,9 @@

Log.v(LOG_TAG, "RuimRecords:fetchRuimRecords " + recordsToLoad);

phone.getIccFileHandler().loadEFTransparent(EF_ICCID,
obtainMessage(EVENT_GET_ICCID_DONE));
recordsToLoad++;







