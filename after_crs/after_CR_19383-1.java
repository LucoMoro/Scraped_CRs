/*Fixed GSM encoded network initiated position request

GSM encoded network initiated position requests were previously
not correctly decoded. The GSM encoded string was decoded as
ASCII but not all characters are encoded in the same way for
GSM and ASCII. These characters was not displayed.

Besides that, when the number of characters was evenly divided
by 8 the last character was lost. This is also corrected.

Change-Id:Ic70b7c28bbba1f2936a9cc99e78f8002cc5c8761*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsNetInitiatedHandler.java b/location/java/com/android/internal/location/GpsNetInitiatedHandler.java
//Synthetic comment -- index a5466d1..4767777 100755

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.os.RemoteException;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;

/**
* A GPS Network-initiated Handler class used by LocationManager.
*
//Synthetic comment -- @@ -291,58 +293,32 @@
*/
static String decodeGSMPackedString(byte[] input)
{
        final char PADDING_CHAR = 0x00;
        int lengthBytes = input.length;
        int lengthSeptets = (lengthBytes * 8) / 7;
        String decoded;

        /* Special case where the last 7 bits in the last byte could hold a valid
         * 7-bit character or a padding character. Drop the last 7-bit character
         * if it is a padding character.
         */
        if (lengthBytes % 7 == 0) {
            if (lengthBytes > 0) {
                if ((input[lengthBytes - 1] >> 1) == PADDING_CHAR) {
                    lengthSeptets = lengthSeptets - 1;
                }
            }
        }

        decoded = GsmAlphabet.gsm7BitPackedToString(input, 0, lengthSeptets);

        // Return "" if decoding of GSM packed string fails
        if (null == decoded) {
            Log.e(TAG, "Decoding of GSM packed string failed");
            decoded = "";
        }

        return decoded;
}

static String decodeUTF8String(byte[] input)







