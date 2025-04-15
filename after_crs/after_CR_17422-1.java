/*telephony: Voice mail notification related issues

Fixed the following issues in voicemail

1.Voice mail notification was not seen in CDMA NV mode
until a phone reboot happened.
Separated voice mail notify from SIM/RUIM

2.If SIM does not support file id EF_MWIS & EF_CPHS_MWIS
the voice mail count was lost after phone reset.
Added support to store voice mail count
in phone memory if SIM write fails

Change-Id:I82878c88ffd77c29e98c57a22d123fb106f6722b*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..9669751 100644

//Synthetic comment -- @@ -52,7 +52,6 @@
protected String newVoiceMailNum = null;
protected String newVoiceMailTag = null;
protected boolean isVoiceMailFixed = false;

protected int mncLength = UNINITIALIZED;
protected int mailboxIndex = 0; // 0 is no mailbox dailing number associated
//Synthetic comment -- @@ -187,21 +186,7 @@
*                     -1 to indicate that an unknown number of
*                      messages are waiting
*/
    public abstract void setVoiceMessageWaiting(int line, int countWaiting, Message onComplete);

/**
* Called by STK Service when REFRESH is received.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..b8aa62e 100644

//Synthetic comment -- @@ -106,6 +106,11 @@
// Key used to read/write "disable DNS server check" pref (used for testing)
public static final String DNS_SERVER_CHECK_DISABLED_KEY = "dns_server_check_disabled_key";

    // Key used for storing voice mail count
    public static final String VM_COUNT = "vm_count_key";
    // Key used to read/write the ID for storing the voice mail
    public static final String VM_ID = "vm_id_key";

/* Instance Variables */
public CommandsInterface mCM;
protected IccFileHandler mIccFileHandler;
//Synthetic comment -- @@ -115,7 +120,7 @@
int mCallRingContinueToken = 0;
int mCallRingDelay;
public boolean mIsTheCurrentActivePhone = true;
    private int mVmCount = 0;
/**
* Set a system property, unless we're in unit test mode
*/
//Synthetic comment -- @@ -746,8 +751,21 @@
public abstract int getPhoneType();

/** @hide */
    /** @return number of voicemails */
    public int getVoiceMessageCount() {
        return mVmCount;
    }

    /** @return true if there are messages waiting, false otherwise. */
    public boolean getMessageWaitingIndicator() {
        return mVmCount != 0;
    }

    /** sets the voice mail count of the phone and notifies listeners. */
    public void setVoiceMessageCount(int countWaiting) {
        mVmCount = countWaiting;
        // notify listeners of voice mail
        notifyMessageWaitingIndicator();
}

/**








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ca526a5..f4bfe5d 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.content.Intent;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
//Synthetic comment -- @@ -35,6 +36,7 @@
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.provider.Telephony.Sms.Intents;
import android.provider.Settings;
//Synthetic comment -- @@ -116,6 +118,8 @@
/** Radio is ON */
static final protected int EVENT_RADIO_ON = 12;

    static final protected int EVENT_UPDATE_ICC_MWI = 18;

protected Phone mPhone;
protected Context mContext;
protected ContentResolver mResolver;
//Synthetic comment -- @@ -400,6 +404,16 @@
obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
}
break;

        case EVENT_UPDATE_ICC_MWI:
            ar = (AsyncResult) msg.obj;
            if ( ar == null)
                break;
            if (ar.exception != null) {
                Log.v(TAG, " MWI update on card failed " + ar.exception );
                storeVoiceMailCount();
            }
            break;
}
}

//Synthetic comment -- @@ -993,6 +1007,22 @@
acknowledgeLastIncomingSms(success, rc, null);
}
}
};

    protected void storeVoiceMailCount() {
        // Store the voice mail count in persistent memory.
        String imsi = mPhone.getSubscriberId();
        int mwi = mPhone.getVoiceMessageCount();

        Log.d(TAG, " Storing Voice Mail Count = " + mwi
                    + " for imsi = " + imsi
                    + " in preferences.");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(((PhoneBase)mPhone).VM_COUNT, mwi);
        editor.putString(((PhoneBase)mPhone).VM_ID, imsi);
        editor.commit();
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 7aecf5b..fa91d0e 100755

//Synthetic comment -- @@ -90,7 +90,6 @@
// Default Emergency Callback Mode exit timer
private static final int DEFAULT_ECM_EXIT_TIMER_VALUE = 300000;

private static final String VM_NUMBER_CDMA = "vm_number_key_cdma";
private String mVmNumber = null;

//Synthetic comment -- @@ -209,7 +208,7 @@
updateCurrentCarrierInProvider(operatorNumeric);

// Notify voicemails.
        updateVoiceMail();
}

public void dispose() {
//Synthetic comment -- @@ -364,11 +363,6 @@
return mSST.mSignalStrength;
}

public List<? extends MmiCode>
getPendingMmiCodes() {
return mPendingMmis;
//Synthetic comment -- @@ -737,19 +731,9 @@
return number;
}

    // pending voice mail count updated after phone creation
    private void updateVoiceMail() {
        setVoiceMessageCount(getStoredVoiceMessageCount());
}

public String getVoiceMailAlphaTag() {
//Synthetic comment -- @@ -862,18 +846,6 @@
if (DBG) Log.d(LOG_TAG, "sendEmergencyCallbackModeChange");
}

/**
* Returns true if CDMA OTA Service Provisioning needs to be performed.
*/
//Synthetic comment -- @@ -1456,4 +1428,11 @@
}
return false;
}

    /** gets the voice mail count from preferences */
    private int getStoredVoiceMessageCount() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        return (sp.getInt(VM_COUNT, 0));
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java b/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java
//Synthetic comment -- index ed93aea..22beca9 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import android.telephony.SmsManager;
import android.telephony.SmsMessage.MessageClass;

import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.SmsHeader;
//Synthetic comment -- @@ -125,15 +126,7 @@
if ((SmsEnvelope.TELESERVICE_VMN == teleService) ||
(SmsEnvelope.TELESERVICE_MWI == teleService)) {
// handling Voicemail
            updateMessageWaitingIndicator(sms.getNumOfVoicemails());
handled = true;
} else if (((SmsEnvelope.TELESERVICE_WMT == teleService) ||
(SmsEnvelope.TELESERVICE_WEMT == teleService)) &&
//Synthetic comment -- @@ -503,4 +496,19 @@
return CommandsInterface.CDMA_SMS_FAIL_CAUSE_ENCODING_PROBLEM;
}
}

    /* package */void updateMessageWaitingIndicator(int mwi) {
        // range check
        if (mwi < 0) {
            mwi = -1;
        } else if (mwi > 0xff) {
            // C.S0015-B v2, 4.5.12
            // range: 0-99
            mwi = 0xff;
        }
        // update voice mail count in phone and notify listeners
        ((PhoneBase) mPhone).setVoiceMessageCount(mwi);
        // store voice mail count in preferences
        storeVoiceMailCount();
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..016035b 100644

//Synthetic comment -- @@ -108,7 +108,6 @@

@Override
protected void onRadioOffOrNotAvailable() {
mncLength = UNINITIALIZED;
iccid = null;

//Synthetic comment -- @@ -331,23 +330,9 @@
}

@Override
    public void setVoiceMessageWaiting(int line, int countWaiting, Message onComplete) {
        //Will be used in future to store voice mail count in UIM
        Log.d(LOG_TAG, "RuimRecords:setVoiceMessageWaiting - NOP for CDMA");
}

private void handleRuimRefresh(int[] result) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 153a591..802ac4b 100644

//Synthetic comment -- @@ -278,8 +278,17 @@
return mSST.mSignalStrength;
}

    // pending voice mail count updated after phone creation
    private void updateVoiceMail() {
        if (mSIMRecords == null) {
            return;
        }
        // get voice mail count from SIM
        int countVoiceMessages = mSIMRecords.getVoiceMessageCount();
        if (countVoiceMessages == 0) {
            countVoiceMessages = getStoredVoiceMessageCount();
        }
        setVoiceMessageCount(countVoiceMessages);
}

public boolean getCallForwardingIndicator() {
//Synthetic comment -- @@ -409,17 +418,6 @@
mNotifier.notifyDataConnectionFailed(this, reason);
}

public void
notifyCallForwardingIndicator() {
mNotifier.notifyCallForwardingChanged(this);
//Synthetic comment -- @@ -1236,8 +1234,8 @@
storeVoiceMailNumber(null);
setVmSimImsi(null);
}
                updateVoiceMail();
                break;

case EVENT_GET_BASEBAND_VERSION_DONE:
ar = (AsyncResult)msg.obj;
//Synthetic comment -- @@ -1494,4 +1492,24 @@
public boolean isCspPlmnEnabled() {
return mSIMRecords.isCspPlmnEnabled();
}

    /** gets the voice mail count from preferences */
    private int getStoredVoiceMessageCount() {
        int countVoiceMessages = 0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String imsi = sp.getString(VM_ID, null);
        String currentImsi = getSubscriberId();

        Log.d(LOG_TAG, "Voicemail count retrieval for Imsi = " + imsi +
                " current Imsi = " + currentImsi );

        if ((imsi != null) && (currentImsi != null)
                && (currentImsi.equals(imsi))) {
            // get voice mail count from preferences
            countVoiceMessages = sp.getInt(VM_COUNT, 0);
            Log.d(LOG_TAG, "Voice Mail Count from preference = " + countVoiceMessages );
        }
        return countVoiceMessages;
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 5540bb2..09e0ee6 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
//Synthetic comment -- @@ -103,13 +104,13 @@

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
            updateMessageWaitingIndicator(sms.getNumOfVoicemails());
handled |= sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator set SMS shouldStore=" + !handled);
}
} else if (sms.isMWIClearMessage()) {
            updateMessageWaitingIndicator(0);
handled |= sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator clear SMS shouldStore=" + !handled);
//Synthetic comment -- @@ -384,4 +385,26 @@
return CommandsInterface.GSM_SMS_FAIL_CAUSE_UNSPECIFIED_ERROR;
}
}

    /* package */void updateMessageWaitingIndicator(int mwi) {
        Message onComplete;
        // range check
        if (mwi < 0) {
            mwi = -1;
        } else if (mwi > 0xff) {
            // TS 23.040 9.2.3.24.2
            // "The value 255 shall be taken to mean 255 or greater"
            mwi = 0xff;
        }
        // update voice mail count in GsmPhone
        ((PhoneBase) mPhone).setVoiceMessageCount(mwi);
        // store voice mail count in SIM/preferences
        if (mGsmPhone.mSIMRecords != null) {
            onComplete = obtainMessage(EVENT_UPDATE_ICC_MWI);
            mGsmPhone.mSIMRecords.setVoiceMessageWaiting(1, mwi, onComplete);
        } else {
            Log.d(TAG, "SIM Records not found, MWI not updated");
        }
    }

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..e1d58cd 100644

//Synthetic comment -- @@ -144,6 +144,8 @@
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;

    private static final int EVENT_SET_MWIS_DONE = 36;
    private static final int EVENT_SET_CPHS_MWIS_DONE = 37;
// ***** Constructor

SIMRecords(GSMPhone p) {
//Synthetic comment -- @@ -186,7 +188,6 @@
imsi = null;
msisdn = null;
voiceMailNum = null;
mncLength = UNINITIALIZED;
iccid = null;
// -1 means no EF_SPN found; treat accordingly.
//Synthetic comment -- @@ -328,32 +329,19 @@
*                      messages are waiting
*/
public void
    setVoiceMessageWaiting(int line, int countWaiting, Message onComplete) {
if (line != 1) {
// only profile 1 is supported
return;
}

try {
if (efMWIS != null) {
// TS 51.011 10.3.45

// lsb of byte 0 is 'voicemail' status
efMWIS[0] = (byte)((efMWIS[0] & 0xfe)
                                    | (countWaiting == 0 ? 0 : 1));

// byte 1 is the number of voice messages waiting
if (countWaiting < 0) {
//Synthetic comment -- @@ -366,18 +354,28 @@

phone.getIccFileHandler().updateEFLinearFixed(
EF_MWIS, 1, efMWIS, null,
                    obtainMessage (EVENT_SET_MWIS_DONE, EF_MWIS, 0, onComplete ));
}

if (efCPHS_MWI != null) {
// Refer CPHS4_2.WW6 B4.2.3
efCPHS_MWI[0] = (byte)((efCPHS_MWI[0] & 0xf0)
                            | (countWaiting == 0 ? 0x5 : 0xa));

phone.getIccFileHandler().updateEFTransparent(
                        EF_VOICE_MAIL_INDICATOR_CPHS,
                        efCPHS_MWI,
                        obtainMessage(EVENT_SET_CPHS_MWIS_DONE, EF_VOICE_MAIL_INDICATOR_CPHS, 0,
                                onComplete));
}

            if ((efMWIS == null) && (efCPHS_MWI == null)) {
                AsyncResult.forMessage((onComplete)).exception =
                    new IccVmNotSupportedException(
                        "SIM does not support EF_MWIS & EF_CPHS_MWIS");
                onComplete.sendToTarget();
            }

} catch (ArrayIndexOutOfBoundsException ex) {
Log.w(LOG_TAG,
"Error saving voice mail state to SIM. Probably malformed SIM record", ex);
//Synthetic comment -- @@ -646,64 +644,57 @@
case EVENT_GET_MWIS_DONE:
isRecordLoadResponse = true;

                ar = (AsyncResult) msg.obj;
                data = (byte[]) ar.result;

                Log.d(LOG_TAG, "EF_MWIS : " + IccUtils.bytesToHexString(data));

if (ar.exception != null) {
                    Log.d(LOG_TAG, "EVENT_GET_MWIS_DONE exception = "
                            + ar.exception);
break;
}

if ((data[0] & 0xff) == 0xff) {
Log.d(LOG_TAG, "SIMRecords: Uninitialized record MWIS");
break;
}

                efMWIS = data;
                break;

            case EVENT_SET_MWIS_DONE:
            case EVENT_SET_CPHS_MWIS_DONE: {
                ar = (AsyncResult) msg.obj;
                Message onComplete = (Message) ar.userObj;
                if (onComplete == null) {
                    break;
}
                if (ar.exception != null) {
                    AsyncResult.forMessage((onComplete)).exception =
                        new IccVmNotSupportedException(
                            "SIM update failed for EF_MWIS/EF_CPHS_MWIS");
                } else {
                    AsyncResult.forMessage((onComplete)).exception = null;
                }
                onComplete.sendToTarget();
                break;
            }

case EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE:
isRecordLoadResponse = true;
                ar = (AsyncResult) msg.obj;
                data = (byte[]) ar.result;

                Log.d(LOG_TAG, "EF_CPHS_MWI: " + IccUtils.bytesToHexString(data));

if (ar.exception != null) {
                    Log.d(LOG_TAG, "EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE exception = "
                            + ar.exception);
break;
}
efCPHS_MWI = data;
                break;

case EVENT_GET_ICCID_DONE:
isRecordLoadResponse = true;
//Synthetic comment -- @@ -1552,4 +1543,36 @@

Log.w(LOG_TAG, "[CSP] Value Added Service Group (0xC0), not found!");
}

    public int getVoiceMessageCount() {
        boolean voiceMailWaiting = false;
        int countVoiceMessages = 0;

        if (efMWIS != null) {
            // Use this data if the EF[MWIS] exists and
            // has been loaded
            // Refer TS 51.011 Section 10.3.45 for the content description
            voiceMailWaiting = ((efMWIS[0] & 0x01) != 0);
            countVoiceMessages = efMWIS[1] & 0xff;

            if (voiceMailWaiting && countVoiceMessages == 0) {
                // Unknown count = -1
                countVoiceMessages = -1;
            }
            Log.d(LOG_TAG, " VoiceMessageCount from SIM MWIS = " + countVoiceMessages);
        } else if (efCPHS_MWI != null) {
            // use voice mail count from CPHS
            int indicator = (int) (efCPHS_MWI[0] & 0xf);

            // Refer CPHS4_2.WW6 B4.2.3
            if (indicator == 0xA) {
                // Unknown count = -1
                countVoiceMessages = -1;
            } else if (indicator == 0x5) {
                countVoiceMessages = 0;
            }
            Log.d(LOG_TAG, " VoiceMessageCount from SIM CPHS = " + countVoiceMessages);
        }
        return countVoiceMessages;
    }
}







