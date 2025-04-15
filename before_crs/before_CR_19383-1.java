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

/**
* A GPS Network-initiated Handler class used by LocationManager.
*
//Synthetic comment -- @@ -291,58 +293,32 @@
*/
static String decodeGSMPackedString(byte[] input)
{
    	final char CHAR_CR = 0x0D;
    	int nStridx = 0;
    	int nPckidx = 0;
    	int num_bytes = input.length;
    	int cPrev = 0;
    	int cCurr = 0;
    	byte nShift;
    	byte nextChar;
    	byte[] stringBuf = new byte[input.length * 2]; 
    	String result = "";
    	
    	while(nPckidx < num_bytes)
    	{
    		nShift = (byte) (nStridx & 0x07);
    		cCurr = input[nPckidx++];
    		if (cCurr < 0) cCurr += 256;

    		/* A 7-bit character can be split at the most between two bytes of packed
    		 ** data.
    		 */
    		nextChar = (byte) (( (cCurr << nShift) | (cPrev >> (8-nShift)) ) & 0x7F);
    		stringBuf[nStridx++] = nextChar;

    		/* Special case where the whole of the next 7-bit character fits inside
    		 ** the current byte of packed data.
    		 */
    		if(nShift == 6)
    		{
    			/* If the next 7-bit character is a CR (0x0D) and it is the last
    			 ** character, then it indicates a padding character. Drop it.
    			 */
    			if (nPckidx == num_bytes || (cCurr >> 1) == CHAR_CR)
    			{
    				break;
    			}
    			
    			nextChar = (byte) (cCurr >> 1); 
    			stringBuf[nStridx++] = nextChar;
    		}

    		cPrev = cCurr;
    	}
    	
    	try{
    		result = new String(stringBuf, 0, nStridx, "US-ASCII");
    	}
    	catch (UnsupportedEncodingException e)
    	{
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return result;
}

static String decodeUTF8String(byte[] input)







