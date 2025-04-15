/*Telephony: Get proper EF path for EF_ADN

Both EF_ADN and EF_CSIM_LI are referring to same constant value 0x6F3A.
So cannot derive different paths for them using exisitng logic
hence added work around to derive path for EF_ADN.

Change-Id:Ib718da462dcb285c832abdf35cc234ac5905b114CRs-Fixed: 325699*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 4c271f9..b0b9a92 100644

//Synthetic comment -- @@ -51,6 +51,13 @@

@Override
protected String getEFPath(int efid) {
switch(efid) {
case EF_SMS:
case EF_CST:







