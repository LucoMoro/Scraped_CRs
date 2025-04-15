/*Change the TP Data-Coding-Scheme when encoding type is UCS-2

Current: 0x0b (Class 3, UCS-2 encoding, uncompressed)
Modified: 0x08 (No meaning, UCS-2 encoding, uncompressed)

	Refer to 3GPP TS 23.038.
	Bit 4, if set to 0, indicates that bits 1 to 0 are reserved and have no message class meaning.
	So it is no matter what class is set at Bit 0 and 1.

Change-Id:Ie6ad239736baa6c7dbc25fef7c2d278fb50cb9b3*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 50dd402..56228fd 100644

//Synthetic comment -- @@ -317,8 +317,8 @@
return null;
}
// TP-Data-Coding-Scheme
            // Class 3, UCS-2 encoding, uncompressed
            bo.write(0x0b);
}

// (no TP-Validity-Period)







