/*Add subaddress to SMS fingerprint calculation.

As per SMS specification in 3GPP2 C.S0015-B, section 4.3.1.6, if the
Subaddress is included in a CDMA SMS message, it needs to be used for
duplicate detection. Subaddress, which is an optional field was omitted
while computing the SMS fingerprint. Hence it was never being used in
duplicate detection if it was included in the SMS. Add subaddress to the
SMS fingerprint.

Change-Id:Iad9e89887a17caba59033ab8f8d94b63b33cb4bb*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
old mode 100755
new mode 100644
//Synthetic comment -- index b50502c..f869dbd

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;
//Synthetic comment -- @@ -138,6 +139,7 @@
SmsMessage msg = new SmsMessage();
SmsEnvelope env = new SmsEnvelope();
CdmaSmsAddress addr = new CdmaSmsAddress();
byte[] data;
byte count;
int countInt;
//Synthetic comment -- @@ -180,15 +182,24 @@

addr.origBytes = data;

        // ignore subaddress
        p.readInt(); //p_cur->sSubAddress.subaddressType
        p.readInt(); //p_cur->sSubAddress.odd
        count = p.readByte(); //p_cur->sSubAddress.number_of_digits
        //p_cur->sSubAddress.digits[digitCount] :
        for (int index=0; index < count; index++) {
            p.readByte();
}

/* currently not supported by the modem-lib:
env.bearerReply
env.replySeqNo
//Synthetic comment -- @@ -210,6 +221,7 @@

// link the the filled objects to the SMS
env.origAddress = addr;
msg.originatingAddress = addr;
msg.mEnvelope = env;

//Synthetic comment -- @@ -818,6 +830,8 @@
output.write(mEnvelope.teleService);
output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);

return output.toByteArray();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/sms/CdmaSmsSubaddress.java b/telephony/java/com/android/internal/telephony/cdma/sms/CdmaSmsSubaddress.java
new file mode 100644
//Synthetic comment -- index 0000000..f9cebf5

//Synthetic comment -- @@ -0,0 +1,27 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/sms/SmsEnvelope.java b/telephony/java/com/android/internal/telephony/cdma/sms/SmsEnvelope.java
//Synthetic comment -- index 0dcacc1..4f00163 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.internal.telephony.cdma.sms;


public final class SmsEnvelope{
/**
* Message Types
//Synthetic comment -- @@ -74,17 +76,23 @@

/**
* The origination address identifies the originator of the SMS message.
     * (See 3GPP2 C.S0015-B, v2, 3.4.3.4)
*/
public CdmaSmsAddress origAddress;

/**
* The destination address identifies the target of the SMS message.
     * (See 3GPP2 C.S0015-B, v2, 3.4.3.4)
*/
public CdmaSmsAddress destAddress;

/**
* The 6-bit bearer reply parameter is used to request the return of a
* SMS Acknowledge Message.
* (See 3GPP2 C.S0015-B, v2, 3.4.3.5)







