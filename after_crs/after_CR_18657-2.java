/*Adding support for SMS email sender and subject

3GPP 23.040 9.2.3.24.11 specifies how email addresses are supported
in SMS messages. The previous solution is moved down to the CDMA
classes and the new code is implemented for GSM.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsMessageBase.java b/telephony/java/com/android/internal/telephony/SmsMessageBase.java
//Synthetic comment -- index af6c5f8..73ca6f8 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import java.util.Arrays;

import static android.telephony.SmsMessage.MessageClass;

/**
* Base class declaring the specific methods and members for SmsMessage.
//Synthetic comment -- @@ -355,13 +354,11 @@
return indexOnIcc;
}

    /**
     * Parses the message body, filtering out any potential email address
     * information
     */
    abstract protected void parseMessageBody();

/**
* Try to parse this message as an email gateway message
//Synthetic comment -- @@ -373,21 +370,7 @@
* TP-OA/TP-DA field contains a generic gateway address and the to/from
* address is added at the beginning as shown above." (which is supported here)
* - Multiple addreses separated by commas, no spaces, Subject field delimited
     * by '()' or '##' and '#' Section 9.2.3.24.11
*/
    abstract protected void extractEmailAddressFromMessageBody();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 54cf612..c3cafb0 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.Parcel;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.text.format.Time;
import android.util.Config;
import android.util.Log;
//Synthetic comment -- @@ -440,6 +441,35 @@
}

/**
     * {@inheritDoc}
     */
    protected void parseMessageBody() {
        // originatingAddress could be null if this message is from a status
        // report.
        if (originatingAddress != null && originatingAddress.couldBeEmailGateway()) {
            extractEmailAddressFromMessageBody();
        }
    }

    /**
     * {@inheritDoc}
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

    /**
* Returns the status for a previously submitted message.
* For not interfering with status codes from GSM, this status code is
* shifted to the bits 31-16.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f4c5e6c..5511487 100644

//Synthetic comment -- @@ -927,6 +927,53 @@
return replyPathPresent;
}

    /** {@inheritDoc} */
    protected void parseMessageBody() {
        if (getProtocolIdentifier() == 0x32) {
            // This is an "email sms", extract sender address from message body
            isEmail = true;
            extractEmailAddressFromMessageBody();
        }
    }

    /** {@inheritDoc} */
    protected void extractEmailAddressFromMessageBody() {
        int firstDoubleHashIndex = messageBody.indexOf("##");
        int firstSingleHashIndex = messageBody.indexOf('#',
                firstDoubleHashIndex + 2);
        int firstLeftParenthesisIndex = messageBody.indexOf('(');
        int firstRigthParenthesisIndex = messageBody.indexOf(')',
                firstLeftParenthesisIndex + 1);

        if (firstDoubleHashIndex >= 0
                && firstDoubleHashIndex < firstSingleHashIndex
                && (firstLeftParenthesisIndex < 0
                        || firstRigthParenthesisIndex < 0
                        || firstDoubleHashIndex < firstLeftParenthesisIndex)) {
            // Using format from##subject#body
            emailFrom = messageBody.substring(0, firstDoubleHashIndex);
            pseudoSubject = messageBody.substring(firstDoubleHashIndex + 2,
                    firstSingleHashIndex);
            emailBody = messageBody.substring(firstSingleHashIndex + 1);
        } else if (firstLeftParenthesisIndex >= 0
                && firstLeftParenthesisIndex < firstRigthParenthesisIndex) {
            // Using format from(subject)body
            emailFrom = messageBody.substring(0, firstLeftParenthesisIndex);
            pseudoSubject = messageBody.substring(firstLeftParenthesisIndex + 1,
                    firstRigthParenthesisIndex);
            emailBody = messageBody.substring(firstRigthParenthesisIndex + 1);
        } else {
            // Using format from[space]body
            String[] parts = messageBody.split(" ", 1);
            if (parts.length < 2) {
                emailBody = parts[0];
            } else {
                emailFrom = parts[0];
                emailBody = parts[1];
            }
        }
    }

/**
* TS 27.005 3.1, <pdu> definition "In the case of SMS: 3GPP TS 24.011 [6]
* SC address followed by 3GPP TS 23.040 [3] TPDU in hexadecimal format:







