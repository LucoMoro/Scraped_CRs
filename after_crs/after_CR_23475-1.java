/*Telephony: Mark SIM SMS as read after importing it.

The GCF testcase 8.2.2 checks if the status byte of the SMS
is marked as read after reading the SMS from SIM. The status
byte is marked as read after the SMS is imported from the SIM
if it is not already marked

Change-Id:Ida0036760d2248b797134ec90c870f065ec52501*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java b/telephony/java/com/android/internal/telephony/gsm/SimSmsInterfaceManager.java
//Synthetic comment -- index e55596b..d1e21e3 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import java.util.Set;

import static android.telephony.SmsManager.STATUS_ON_ICC_FREE;
import static android.telephony.SmsManager.STATUS_ON_ICC_READ;
import static android.telephony.SmsManager.STATUS_ON_ICC_UNREAD;

/**
* SimSmsInterfaceManager to provide an inter-process communication to
//Synthetic comment -- @@ -78,6 +80,8 @@
synchronized (mLock) {
if (ar.exception == null) {
mSms  = buildValidRawData((ArrayList<byte[]>) ar.result);
                            //Mark SMS as read after importing it from SIM.
                            markMessagesAsRead((ArrayList<byte[]>) ar.result);
} else {
if(DBG) log("Cannot load Sms records");
if (mSms != null)
//Synthetic comment -- @@ -98,6 +102,26 @@
}
};

    private void markMessagesAsRead(ArrayList<byte[]> messages) {
        if (messages == null) {
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
                ((SIMFileHandler)mPhone.getIccFileHandler()).updateEFLinearFixed(
                        IccConstants.EF_SMS, i + 1, record, null, null);
                log("SMS " + (i + 1) + " marked as read");
            }
        }
    }

public SimSmsInterfaceManager(GSMPhone phone, SMSDispatcher dispatcher) {
super(phone);
mDispatcher = dispatcher;







