/*Telephony: Fix MT SMS with invalid TOA field causing crash issue

Throw FormatException when type of number is incorrect and handle it

Change-Id:I305ea1c9e4fee29c84caefec7d7564d46228851d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSmsAddress.java b/src/java/com/android/internal/telephony/gsm/GsmSmsAddress.java
//Synthetic comment -- index c163803..fa188f3 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.internal.telephony.gsm;

import android.telephony.PhoneNumberUtils;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsAddress;

//Synthetic comment -- @@ -35,9 +35,10 @@
* @param offset the offset of the Address-Length byte
* @param length the length in bytes rounded up, e.g. "2 +
*        (addressLength + 1) / 2"
*/

    public GsmSmsAddress(byte[] data, int offset, int length) {
origBytes = new byte[length];
System.arraycopy(data, offset, origBytes, 0, length);

//Synthetic comment -- @@ -49,7 +50,8 @@

// TOA must have its high bit set
if ((toa & 0x80) != 0x80) {
            throw new RuntimeException("Invalid TOA - high bit must be set");
}

if (isAlphanumeric()) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 1ed1478..76a4b7f 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static com.android.internal.telephony.SmsConstants.MessageClass;
import static com.android.internal.telephony.SmsConstants.ENCODING_UNKNOWN;
//Synthetic comment -- @@ -560,7 +561,12 @@
int addressLength = pdu[cur] & 0xff;
int lengthBytes = 2 + (addressLength + 1) / 2;

            ret = new GsmSmsAddress(pdu, cur, lengthBytes);

cur += lengthBytes;








