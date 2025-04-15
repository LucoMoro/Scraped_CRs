/*Support for sms email sender and subject

3GPP 23.040 3.8 specifies how email addresses are
supported in sms messages.

The old solution is moved down to CDMA classes and the new
code is implemented for GSM.

Change-Id:Ib796db068cc27c7e79347423bbf7eeb07e6bb780*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SmsMessageBase.java b/src/java/com/android/internal/telephony/SmsMessageBase.java
//Synthetic comment -- index 22d8cd8..efdfd45 100644

//Synthetic comment -- @@ -312,13 +312,11 @@
return indexOnIcc;
}

    protected void parseMessageBody() {
        // originatingAddress could be null if this message is from a status
        // report.
        if (originatingAddress != null && originatingAddress.couldBeEmailGateway()) {
            extractEmailAddressFromMessageBody();
        }
    }

/**
* Try to parse this message as an email gateway message
//Synthetic comment -- @@ -330,21 +328,8 @@
* TP-OA/TP-DA field contains a generic gateway address and the to/from
* address is added at the beginning as shown above." (which is supported here)
* - Multiple addresses separated by commas, no spaces, Subject field delimited
     * by '()' or '##' and '#' Section 9.2.3.24.11 (which are NOT supported here)
*/
    protected void extractEmailAddressFromMessageBody() {

        /* Some carriers may use " /" delimiter as below
         *
         * 1. [x@y][ ]/[subject][ ]/[body]
         * -or-
         * 2. [x@y][ ]/[body]
         */
         String[] parts = messageBody.split("( /)|( )", 2);
         if (parts.length < 2) return;
         emailFrom = parts[0];
         emailBody = parts[1];
         isEmail = Telephony.Mms.isEmailAddress(emailFrom);
    }

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/SmsMessage.java b/src/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 6254a50..714912a 100644

//Synthetic comment -- @@ -414,6 +414,34 @@
}

/**
* Returns the status for a previously submitted message.
* For not interfering with status codes from GSM, this status code is
* shifted to the bits 31-16.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 1ed1478..93a0da9 100644

//Synthetic comment -- @@ -882,6 +882,50 @@
return replyPathPresent;
}

/**
* TS 27.005 3.1, &lt;pdu&gt; definition "In the case of SMS: 3GPP TS 24.011 [6]
* SC address followed by 3GPP TS 23.040 [3] TPDU in hexadecimal format:







