/*Telephony: Display of Opertor name.

Operator name was displayed in junk charaters on CDMA network
due to wrong encoding scheme.Corrected the same.

Change-Id:I8d18a90deab7fb14935d265b885cbf405fcc7d8d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e8cd8f3..0f31264 100755

//Synthetic comment -- @@ -297,9 +297,11 @@
break;
case UserData.ENCODING_IA5:
case UserData.ENCODING_GSM_7BIT_ALPHABET:
                case UserData.ENCODING_7BIT_ASCII:
spn = GsmAlphabet.gsm7BitPackedToString(spnData, 0, (numBytes*8)/7);
break;
case UserData.ENCODING_UNICODE_16:
spn =  new String(spnData, 0, numBytes, "utf-16");
break;







