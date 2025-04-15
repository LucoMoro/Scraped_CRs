/*fixes buffer overflow for data

Change-Id:Ia1fc89b2fe0fc84ca540fef2783b95f8f9952d6fSigned-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java b/src/java/com/android/internal/telephony/cdma/CdmaLteUiccRecords.java
//Synthetic comment -- index eaa2ede..97f973f 100755

//Synthetic comment -- @@ -116,7 +116,8 @@
int encoding = data[1];
int language = data[2];
byte[] spnData = new byte[32];
            System.arraycopy(data, 3, spnData, 0, (data.length < 32) ? data.length : 32);

int numBytes;
for (numBytes = 0; numBytes < spnData.length; numBytes++) {







