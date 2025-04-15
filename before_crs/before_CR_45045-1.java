/*Telephony: Consider EVDO ecio zero value as valid.

When mEvdoEcio receives 0 getEvdoEcio returns -1.

Ecio valid values are positive integers, to fix this
consider EVDO ecio zero value as valid.

Change-Id:Ice8415150e7502c7f39e1a4135d35ea2bec1eb1fCRs-Fixed: 402159*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SignalStrength.java b/telephony/java/android/telephony/SignalStrength.java
//Synthetic comment -- index 92b889b..4671310 100755

//Synthetic comment -- @@ -279,7 +279,7 @@
mCdmaEcio = (mCdmaEcio > 0) ? -mCdmaEcio : -160;

mEvdoDbm = (mEvdoDbm > 0) ? -mEvdoDbm : -120;
        mEvdoEcio = (mEvdoEcio > 0) ? -mEvdoEcio : -1;
mEvdoSnr = ((mEvdoSnr > 0) && (mEvdoSnr <= 8)) ? mEvdoSnr : -1;

// TS 36.214 Physical Layer Section 5.1.3, TS 36.331 RRC







