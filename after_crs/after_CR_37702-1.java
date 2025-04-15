/*Telephony: Fix MT SMS with invalid TOA field causing crash issue

Throw FormatException when type of number is incorrect and handle it

Change-Id:I448d4f2ec236eec0b9ddb0bcc2e8bc44b2042e73*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSmsAddress.java b/telephony/java/com/android/internal/telephony/gsm/GsmSmsAddress.java
//Synthetic comment -- index c163803..9505b7e 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.gsm;

import android.telephony.PhoneNumberUtils;
import java.text.ParseException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsAddress;

//Synthetic comment -- @@ -35,9 +35,10 @@
* @param offset the offset of the Address-Length byte
* @param length the length in bytes rounded up, e.g. "2 +
*        (addressLength + 1) / 2"
     * @throws ParseException
*/

    public GsmSmsAddress(byte[] data, int offset, int length) throws ParseException {
origBytes = new byte[length];
System.arraycopy(data, offset, origBytes, 0, length);

//Synthetic comment -- @@ -49,7 +50,7 @@

// TOA must have its high bit set
if ((toa & 0x80) != 0x80) {
            throw new ParseException("Invalid TOA - high bit must be set. toa = " + toa, 7);
}

if (isAlphanumeric()) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index da60584..5b75b43 100644

//Synthetic comment -- @@ -28,6 +28,7 @@

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import static android.telephony.SmsMessage.ENCODING_16BIT;
import static android.telephony.SmsMessage.ENCODING_7BIT;
//Synthetic comment -- @@ -557,7 +558,12 @@
int addressLength = pdu[cur] & 0xff;
int lengthBytes = 2 + (addressLength + 1) / 2;

            try {
                ret = new GsmSmsAddress(pdu, cur, lengthBytes);
            } catch (ParseException e) {
                Log.e(LOG_TAG, e.getMessage());
                ret = null;
            }

cur += lengthBytes;








