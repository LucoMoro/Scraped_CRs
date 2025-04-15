/*Displaying urgent voicemail icon.

Per operator request, added code to propagate voice mail
message priority flag (normal or urgent) from CDMA phone
object to application level. The changes are used by the
Phone application.

Change-Id:I8d96c54bacc3be4bb7fe720532fd6d333040e509*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..94c70dc 100644

//Synthetic comment -- @@ -53,6 +53,7 @@
protected String newVoiceMailTag = null;
protected boolean isVoiceMailFixed = false;
protected int countVoiceMessages = 0;
    protected int mVoiceMessagePriority = 0;

protected int mncLength = UNINITIALIZED;
protected int mailboxIndex = 0; // 0 is no mailbox dailing number associated
//Synthetic comment -- @@ -181,6 +182,20 @@
}

/**
     * Store priority flag for voicemails.
     * @param priority SMS priority
     */
    public void setVoiceMessagePriority(int priority) {
        mVoiceMessagePriority = priority;
    }

    /** @return priority flag for voice mail message(s).
     */
    public int getVoiceMessagePriority() {
        return mVoiceMessagePriority;
    }

    /**
* Sets the SIM voice message waiting indicator records
* @param line GSM Subscriber Profile Number, one-based. Only '1' is supported
* @param countWaiting The number of messages waiting, if known. Use








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index e426e94..75a17ff 100644

//Synthetic comment -- @@ -928,6 +928,11 @@
int getVoiceMessageCount();

/**
     * Return message priority flag.
     */
    int getVoiceMessagePriority();

    /**
* Returns the alpha tag associated with the voice mail number.
* If there is no alpha tag associated or the record is not yet available,
* returns a default localized string. <p>








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1674ad6..6fa5b2b 100644

//Synthetic comment -- @@ -755,6 +755,14 @@
}

/**
     * Return default message priority for voice mail.
     * @hide
     */
     public int getVoiceMessagePriority() {
         return 0;
     }

    /**
* Returns the CDMA ERI icon index to display
*/
public int getCdmaEriIconIndex() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77f1e6c..dda5651 100644

//Synthetic comment -- @@ -492,6 +492,11 @@
return mActivePhone.getVoiceMessageCount();
}

    /** @hide */
    public int getVoiceMessagePriority() {
        return mActivePhone.getVoiceMessagePriority();
    }

public String getVoiceMailAlphaTag() {
return mActivePhone.getVoiceMailAlphaTag();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsMessageBase.java b/telephony/java/com/android/internal/telephony/SmsMessageBase.java
//Synthetic comment -- index af6c5f8..7f55589 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.internal.telephony;

import android.util.Log;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.SmsHeader;
import java.util.Arrays;

//Synthetic comment -- @@ -184,6 +185,13 @@
}

/**
     * @return default priority as an integer. It can be overriden by child classe(s).
     */
    public int getMessagePriority() {
        return BearerData.PRIORITY_NORMAL;
    }

    /**
* Returns the class of this message.
*/
public abstract MessageClass getMessageClass();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f31bf24..96104cd 100755

//Synthetic comment -- @@ -92,6 +92,7 @@

static final String VM_COUNT_CDMA = "vm_count_key_cdma";
private static final String VM_NUMBER_CDMA = "vm_number_key_cdma";
    static final String VM_VOICE_MAIL_PRIORITY_CDMA = "vm_voice_mail_priority_cdma";
private String mVmNumber = null;

static final int RESTART_ECM_TIMER = 0; // restart Ecm timer
//Synthetic comment -- @@ -756,6 +757,23 @@
return voicemailCount;
}

    /**
     * Returns priority flag for voice mail message. Carrier sends a SMS to device if there is
     * any updated voicemail info. The parser first stores these values such as voicemail count
     * in mRuimRecords/IccReords. Applications have no access to IccRecords, but it can access
     * CDMAPhone instance if it is a CDMA device. As you can see, phone app accesses voice message
     * count in the same way.
     * @hide
     */
    public int getVoiceMessagePriority() {
        int voicemailPriority = mRuimRecords.getVoiceMessagePriority();
        if (voicemailPriority == 0) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            voicemailPriority = sp.getInt(VM_VOICE_MAIL_PRIORITY_CDMA, 0);
        }
        return voicemailPriority;
    }

public String getVoiceMailAlphaTag() {
// TODO: Where can we get this value has to be clarified with QC.
String ret = "";//TODO: Remove = "", if we know where to get this value.
//Synthetic comment -- @@ -878,6 +896,11 @@
mRuimRecords.setVoiceMessageWaiting(1, mwi);
}

    /* This function stores priority flag for voicemails */
    void updateVoiceMessagePriority(int priority) {
        mRuimRecords.setVoiceMessagePriority(priority);
    }

/**
* Returns true if CDMA OTA Service Provisioning needs to be performed.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java b/telephony/java/com/android/internal/telephony/cdma/CdmaSMSDispatcher.java
//Synthetic comment -- index d6fc134..7dd80f7 100644

//Synthetic comment -- @@ -121,14 +121,17 @@
(SmsEnvelope.TELESERVICE_MWI == teleService)) {
// handling Voicemail
int voicemailCount = sms.getNumOfVoicemails();
            int voicemailPriority = sms.getMessagePriority();
Log.d(TAG, "Voicemail count=" + voicemailCount);
// Store the voicemail count in preferences.
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(
mPhone.getContext());
SharedPreferences.Editor editor = sp.edit();
editor.putInt(CDMAPhone.VM_COUNT_CDMA, voicemailCount);
            editor.putInt(CDMAPhone.VM_VOICE_MAIL_PRIORITY_CDMA, voicemailPriority);
editor.apply();
((CDMAPhone) mPhone).updateMessageWaitingIndicator(voicemailCount);
            ((CDMAPhone) mPhone).updateVoiceMessagePriority(voicemailPriority);
handled = true;
} else if (((SmsEnvelope.TELESERVICE_WMT == teleService) ||
(SmsEnvelope.TELESERVICE_WEMT == teleService)) &&








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 54cf612..9ec1aac 100644

//Synthetic comment -- @@ -440,6 +440,13 @@
}

/**
     * {@inheritDoc}
     */
    public int getMessagePriority() {
        return mBearerData != null ? mBearerData.priority : BearerData.PRIORITY_NORMAL;
    }

    /**
* Returns the status for a previously submitted message.
* For not interfering with status codes from GSM, this status code is
* shifted to the bits 31-16.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3959c67..56ea02d 100644

//Synthetic comment -- @@ -817,6 +817,13 @@
return number;
}

    /* Returns the default message priority for voice mail.
     * @hide
     */
    public int getVoiceMessagePriority() {
        return 0;
    }

private String getVmSimImsi() {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
return sp.getString(VM_SIM_IMSI, null);







