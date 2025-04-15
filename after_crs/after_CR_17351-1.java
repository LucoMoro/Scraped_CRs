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
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;
//Synthetic comment -- @@ -138,6 +139,7 @@
SmsMessage msg = new SmsMessage();
SmsEnvelope env = new SmsEnvelope();
CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subaddr = new CdmaSmsSubaddress();
byte[] data;
byte count;
int countInt;
//Synthetic comment -- @@ -180,15 +182,24 @@

addr.origBytes = data;

        subaddr.type = p.readInt(); // p_cur->sSubAddress.subaddressType
        subaddr.odd = p.readByte();     // p_cur->sSubAddress.odd
        count = p.readByte();           // p_cur->sSubAddress.number_of_digits

        if (count < 0) {
            count = 0;
}

        // p_cur->sSubAddress.digits[digitCount] :

        data = new byte[count];

        for (int index = 0; index < count; ++index) {
            data[index] = p.readByte();
        }

        subaddr.origBytes = data;

/* currently not supported by the modem-lib:
env.bearerReply
env.replySeqNo
//Synthetic comment -- @@ -210,6 +221,7 @@

// link the the filled objects to the SMS
env.origAddress = addr;
        env.origSubaddress = subaddr;
msg.originatingAddress = addr;
msg.mEnvelope = env;

//Synthetic comment -- @@ -818,6 +830,8 @@
output.write(mEnvelope.teleService);
output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);
        output.write(mEnvelope.origSubaddress.origBytes, 0,
                mEnvelope.origSubaddress.origBytes.length);

return output.toByteArray();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/sms/CdmaSmsSubaddress.java b/telephony/java/com/android/internal/telephony/cdma/sms/CdmaSmsSubaddress.java
new file mode 100644
//Synthetic comment -- index 0000000..f9cebf5

//Synthetic comment -- @@ -0,0 +1,27 @@
/*
 * Copyright (C) 2010 The Android Open Source Project. All rights reserved.
 * Copyright (C) 2010 Code Aurora Forum. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony.cdma.sms;

public class CdmaSmsSubaddress {
    public int type;

    public byte odd;

    public byte[] origBytes;
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/sms/SmsEnvelope.java b/telephony/java/com/android/internal/telephony/cdma/sms/SmsEnvelope.java
//Synthetic comment -- index 0dcacc1..4f00163 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.internal.telephony.cdma.sms;


import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;

public final class SmsEnvelope{
/**
* Message Types
//Synthetic comment -- @@ -74,17 +76,23 @@

/**
* The origination address identifies the originator of the SMS message.
     * (See 3GPP2 C.S0015-B, v2, 3.4.3.3)
*/
public CdmaSmsAddress origAddress;

/**
* The destination address identifies the target of the SMS message.
     * (See 3GPP2 C.S0015-B, v2, 3.4.3.3)
*/
public CdmaSmsAddress destAddress;

/**
     * The origination subaddress identifies the originator of the SMS message.
     * (See 3GPP2 C.S0015-B, v2, 3.4.3.4)
     */
    public CdmaSmsSubaddress origSubaddress;

    /**
* The 6-bit bearer reply parameter is used to request the return of a
* SMS Acknowledge Message.
* (See 3GPP2 C.S0015-B, v2, 3.4.3.5)







