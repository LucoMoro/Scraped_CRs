/*Fix PhoneApp crash while decoding operatorNumeric to find mcc

Handle StringIndexOutOfBoundsException when mcc is found using
substring operation.

Change-Id:I9e89a791e6efb3f9daacc33ac3aef05d9218af41*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 56cf53a..808ec2b 100644

//Synthetic comment -- @@ -864,8 +864,9 @@
mNitzUpdatedTime = false;
} else {
String iso = "";
                String mcc = "";
try{
                    mcc = operatorNumeric.substring(0, 3);
iso = MccTable.countryCodeForMcc(Integer.parseInt(mcc));
} catch ( NumberFormatException ex){
loge("pollStateDone: countryCodeForMcc error" + ex);







