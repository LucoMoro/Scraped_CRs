/*fix for supporting 3 digits MNC code

Default Android MNC value has a 2 digit but it should be supported a 3 digit
MNC in India. (should be supported both 2 and 3 digits MNC)

Change-Id:I69373d196b29bccd06653841f24cbfe3886834fbSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
old mode 100644
new mode 100755
//Synthetic comment -- index b14896a..6e161d3

//Synthetic comment -- @@ -453,6 +453,35 @@
return null;
}

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
return imsi.substring(0, 3 + mncLength);







