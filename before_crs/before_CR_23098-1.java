/*Add condition to check whether NFC is supported or not

In this test case it does not check that the HW has NFC functionality or not, even if the HW does not support NFC it will still run this test and will return error.

We modified the test case so that if PackageManager.FEATURE_NFC is not supported it does not run this test case.*/
//Synthetic comment -- diff --git a/tests/tests/ndef/src/android/ndef/cts/BasicNdefTest.java b/tests/tests/ndef/src/android/ndef/cts/BasicNdefTest.java
//Synthetic comment -- index 6e2ac3c..8d4d1d4 100644

//Synthetic comment -- @@ -20,9 +20,12 @@
import android.nfc.NdefRecord;
import android.nfc.FormatException;

import junit.framework.TestCase;

public class BasicNdefTest extends TestCase {
/**
* A Smart Poster containing a URL and no text.
*/
//Synthetic comment -- @@ -34,22 +37,24 @@
};

public void test_parseSmartPoster() throws FormatException {
        NdefMessage msg = new NdefMessage(SMART_POSTER_URL_NO_TEXT);
        NdefRecord[] records = msg.getRecords();

        assertEquals(1, records.length);

        assertEquals(0, records[0].getId().length);

        assertEquals(NdefRecord.TNF_WELL_KNOWN, records[0].getTnf());

        assertByteArrayEquals(NdefRecord.RTD_SMART_POSTER, records[0].getType());

        assertByteArrayEquals(new byte[] {
                (byte) 0xd1, (byte) 0x01, (byte) 0x0b, (byte) 0x55, (byte) 0x01,
                (byte) 0x67, (byte) 0x6f, (byte) 0x6f, (byte) 0x67, (byte) 0x6c,
                (byte) 0x65, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d},
                records[0].getPayload());
}

private static void assertByteArrayEquals(byte[] b1, byte[] b2) {







