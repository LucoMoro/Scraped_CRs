/*Telephony: Mark SIM SMS as read after importing it

The GCF test case 8.2.2 checks if the status byte of the SMS is marked as read
after reading the SMS from SIM. The status byte is marked as read after the
SMS is imported from the SIM if it is not already marked.
Fix to check for null IccFilehandler which can happen if card is not present.

Extract common functions from Sim/RuimSmsInterfaceManager to their parent
class IccSmsInterfaceManager.

Change-Id:I4f226bc873786904527d7d822a67dfc14f05a327*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java b/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java
//Synthetic comment -- index b911b10..7907bb4 100644

//Synthetic comment -- @@ -18,31 +18,124 @@

import android.app.PendingIntent;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.util.Log;

import com.android.internal.telephony.uicc.IccConstants;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.util.HexDump;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.telephony.SmsManager.STATUS_ON_ICC_FREE;
import static android.telephony.SmsManager.STATUS_ON_ICC_READ;
import static android.telephony.SmsManager.STATUS_ON_ICC_UNREAD;

/**
* IccSmsInterfaceManager to provide an inter-process communication to
* access Sms in Icc.
*/
public abstract class IccSmsInterfaceManager extends ISms.Stub {
    static final String LOG_TAG = "RIL_IccSms";
    static final boolean DBG = true;

    protected final Object mLock = new Object();
    protected boolean mSuccess;
    private List<SmsRawData> mSms;

    private static final int EVENT_LOAD_DONE = 1;
    private static final int EVENT_UPDATE_DONE = 2;
    protected static final int EVENT_SET_BROADCAST_ACTIVATION_DONE = 3;
    protected static final int EVENT_SET_BROADCAST_CONFIG_DONE = 4;

protected PhoneBase mPhone;
protected Context mContext;
protected SMSDispatcher mDispatcher;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AsyncResult ar;

            switch (msg.what) {
                case EVENT_UPDATE_DONE:
                    ar = (AsyncResult) msg.obj;
                    synchronized (mLock) {
                        mSuccess = (ar.exception == null);
                        mLock.notifyAll();
                    }
                    break;
                case EVENT_LOAD_DONE:
                    ar = (AsyncResult)msg.obj;
                    synchronized (mLock) {
                        if (ar.exception == null) {
                            mSms  = (List<SmsRawData>)
                                    buildValidRawData((ArrayList<byte[]>) ar.result);
                            //Mark SMS as read after importing it from card.
                            markMessagesAsRead((ArrayList<byte[]>) ar.result);
                        } else {
                            if(DBG) log("Cannot load Sms records");
                            if (mSms != null)
                                mSms.clear();
                        }
                        mLock.notifyAll();
                    }
                    break;
                case EVENT_SET_BROADCAST_ACTIVATION_DONE:
                case EVENT_SET_BROADCAST_CONFIG_DONE:
                    ar = (AsyncResult) msg.obj;
                    synchronized (mLock) {
                        mSuccess = (ar.exception == null);
                        mLock.notifyAll();
                    }
                    break;
            }
        }
    };

protected IccSmsInterfaceManager(PhoneBase phone){
mPhone = phone;
mContext = phone.getContext();
}

    protected void markMessagesAsRead(ArrayList<byte[]> messages) {
        if (messages == null) {
            return;
        }

        //IccFileHandler can be null, if icc card is absent.
        IccFileHandler fh = mPhone.getIccFileHandler();
        if (fh == null) {
            //shouldn't really happen, as messages are marked as read, only
            //after importing it from icc.
            if (Rlog.isLoggable("SMS", Log.DEBUG)) {
                log("markMessagesAsRead - aborting, no icc card present.");
            }
            return;
        }

        int count = messages.size();

        for (int i = 0; i < count; i++) {
             byte[] ba = messages.get(i);
             if (ba[0] == STATUS_ON_ICC_UNREAD) {
                 int n = ba.length;
                 byte[] nba = new byte[n - 1];
                 System.arraycopy(ba, 1, nba, 0, n - 1);
                 byte[] record = makeSmsRecordData(STATUS_ON_ICC_READ, nba);
                 fh.updateEFLinearFixed(IccConstants.EF_SMS, i + 1, record, null, null);
                 if (Rlog.isLoggable("SMS", Log.DEBUG)) {
                     log("SMS " + (i + 1) + " marked as read");
                 }
             }
        }
    }

protected void enforceReceiveAndSend(String message) {
mContext.enforceCallingPermission(
"android.permission.RECEIVE_SMS", message);
//Synthetic comment -- @@ -51,6 +144,119 @@
}

/**
     * Update the specified message on the Icc.
     *
     * @param index record index of message to update
     * @param status new message status (STATUS_ON_ICC_READ,
     *                  STATUS_ON_ICC_UNREAD, STATUS_ON_ICC_SENT,
     *                  STATUS_ON_ICC_UNSENT, STATUS_ON_ICC_FREE)
     * @param pdu the raw PDU to store
     * @return success or not
     *
     */
    public boolean
    updateMessageOnIccEf(int index, int status, byte[] pdu) {
        if (DBG) log("updateMessageOnIccEf: index=" + index +
                " status=" + status + " ==> " +
                "("+ Arrays.toString(pdu) + ")");
        enforceReceiveAndSend("Updating message on Icc");
        synchronized(mLock) {
            mSuccess = false;
            Message response = mHandler.obtainMessage(EVENT_UPDATE_DONE);

            if (status == STATUS_ON_ICC_FREE) {
                // RIL_REQUEST_DELETE_SMS_ON_SIM vs RIL_REQUEST_CDMA_DELETE_SMS_ON_RUIM
                // Special case FREE: call deleteSmsOnSim/Ruim instead of
                // manipulating the record
                // Will eventually fail if icc card is not present.
                deleteSms(index, response);
            } else {
                //IccFilehandler can be null if ICC card is not present.
                IccFileHandler fh = mPhone.getIccFileHandler();
                if (fh == null) {
                    response.recycle();
                    return mSuccess; /* is false */
                }
                byte[] record = makeSmsRecordData(status, pdu);
                fh.updateEFLinearFixed(
                        IccConstants.EF_SMS,
                        index, record, null, response);
            }
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                log("interrupted while trying to update by index");
            }
        }
        return mSuccess;
    }

    /**
     * Copy a raw SMS PDU to the Icc.
     *
     * @param pdu the raw PDU to store
     * @param status message status (STATUS_ON_ICC_READ, STATUS_ON_ICC_UNREAD,
     *               STATUS_ON_ICC_SENT, STATUS_ON_ICC_UNSENT)
     * @return success or not
     *
     */
    public boolean copyMessageToIccEf(int status, byte[] pdu, byte[] smsc) {
        //NOTE smsc not used in RUIM
        if (DBG) log("copyMessageToIccEf: status=" + status + " ==> " +
                "pdu=("+ Arrays.toString(pdu) +
                "), smsm=(" + Arrays.toString(smsc) +")");
        enforceReceiveAndSend("Copying message to Icc");
        synchronized(mLock) {
            mSuccess = false;
            Message response = mHandler.obtainMessage(EVENT_UPDATE_DONE);

            //RIL_REQUEST_WRITE_SMS_TO_SIM vs RIL_REQUEST_CDMA_WRITE_SMS_TO_RUIM
            writeSms(status, smsc, pdu, response);

            try {
                mLock.wait();
            } catch (InterruptedException e) {
                log("interrupted while trying to update by index");
            }
        }
        return mSuccess;
    }

    /**
     * Retrieves all messages currently stored on Icc.
     *
     * @return list of SmsRawData of all sms on Icc
     */
    public List<SmsRawData> getAllMessagesFromIccEf() {
        if (DBG) log("getAllMessagesFromEF");

        mContext.enforceCallingPermission(
                "android.permission.RECEIVE_SMS",
                "Reading messages from Icc");
        synchronized(mLock) {

            IccFileHandler fh = mPhone.getIccFileHandler();
            if (fh == null) {
                Rlog.e(LOG_TAG, "Cannot load Sms records. No icc card?");
                if (mSms != null) {
                    mSms.clear();
                    return mSms;
                }
            }

            Message response = mHandler.obtainMessage(EVENT_LOAD_DONE);
            fh.loadEFLinearFixedAll(IccConstants.EF_SMS, response);

            try {
                mLock.wait();
            } catch (InterruptedException e) {
                log("interrupted while trying to load from the Icc");
            }
        }
        return mSms;
    }

    /**
* Send a data based SMS to a specific application port.
*
* @param destAddr the address to send the message to
//Synthetic comment -- @@ -222,6 +428,10 @@
return data;
}

    protected abstract void deleteSms(int index, Message response);

    protected abstract void writeSms(int status, byte[] pdu, byte[] smsc, Message response);

protected abstract void log(String msg);

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/cdma/RuimSmsInterfaceManager.java
//Synthetic comment -- index 75f2644..ff7d2ef 100644

//Synthetic comment -- @@ -18,24 +18,13 @@
package com.android.internal.telephony.cdma;

import android.content.Context;
import android.os.Message;
import android.telephony.Rlog;

import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.uicc.IccUtils;

/**
* RuimSmsInterfaceManager to provide an inter-process communication to
* access Sms in Ruim.
//Synthetic comment -- @@ -44,43 +33,6 @@
static final String LOG_TAG = "CDMA";
static final boolean DBG = true;

public RuimSmsInterfaceManager(CDMAPhone phone, SMSDispatcher dispatcher) {
super(phone);
mDispatcher = dispatcher;
//Synthetic comment -- @@ -98,97 +50,14 @@
if(DBG) Rlog.d(LOG_TAG, "RuimSmsInterfaceManager finalized");
}

    protected void deleteSms(int index, Message response) {
        mPhone.mCM.deleteSmsOnRuim(index, response);
}

    protected void writeSms(int status, byte[] pdu, byte[] smsc, Message response) {
//NOTE smsc not used in RUIM
        mPhone.mCM.writeSmsToRuim(status, IccUtils.bytesToHexString(pdu),
                response);
}

public boolean enableCellBroadcast(int messageIdentifier) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/src/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index 4e51bfd..003a5d3 100644

//Synthetic comment -- @@ -17,28 +17,16 @@
package com.android.internal.telephony.gsm;

import android.content.Context;
import android.os.Binder;
import android.os.Message;
import android.telephony.Rlog;

import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.IntRangeManager;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.uicc.IccUtils;

import java.util.ArrayList;

/**
* SimSmsInterfaceManager to provide an inter-process communication to
//Synthetic comment -- @@ -48,60 +36,12 @@
static final String LOG_TAG = "GSM";
static final boolean DBG = true;

private CellBroadcastRangeManager mCellBroadcastRangeManager =
new CellBroadcastRangeManager();

private static final int SMS_CB_CODE_SCHEME_MIN = 0;
private static final int SMS_CB_CODE_SCHEME_MAX = 255;

public SimSmsInterfaceManager(GSMPhone phone, SMSDispatcher dispatcher) {
super(phone);
mDispatcher = dispatcher;
//Synthetic comment -- @@ -120,100 +60,13 @@
if(DBG) Rlog.d(LOG_TAG, "SimSmsInterfaceManager finalized");
}

    protected void deleteSms(int index, Message response) {
        mPhone.mCM.deleteSmsOnSim(index, response);
}

    protected void writeSms(int status, byte[] pdu, byte[] smsc, Message response) {
        mPhone.mCM.writeSmsToSim(status, IccUtils.bytesToHexString(smsc),
                IccUtils.bytesToHexString(pdu), response);
}

public boolean enableCellBroadcast(int messageIdentifier) {







