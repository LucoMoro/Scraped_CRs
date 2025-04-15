/*fix for supporting 3 digits MNC codeDefault Android MNC value has a 2 digit but it should be supported a 3 digitMNC in India. (should be supported both 2 and 3 digits MNC)Change-Id: I69373d196b29bccd06653841f24cbfe3886834fbSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>

Change-Id:I0f0191e1c6446029c5f7d2957778fa743ee39e9a*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
old mode 100644
new mode 100755
//Synthetic comment -- index b14896a..00f3419

//Synthetic comment -- @@ -141,6 +141,27 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;

// ***** Constructor

SIMRecords(GSMPhone p) {
//Synthetic comment -- @@ -498,6 +519,16 @@

Log.d(LOG_TAG, "IMSI: " + imsi.substring(0, 6) + "xxxxxxx");

if (mncLength == UNKNOWN) {
// the SIM has told us all it knows, but it didn't know the mnc length.
// guess using the mcc
//Synthetic comment -- @@ -742,6 +773,16 @@
mncLength = UNKNOWN;
}
} finally {
if (mncLength == UNKNOWN || mncLength == UNINITIALIZED) {
if (imsi != null) {
try {







