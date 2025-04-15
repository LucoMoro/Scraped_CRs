/*update the NO_DELIVERY_REPORTS list

add additional carriers that don;t support sms delivery support

Change-Id:Ib77dad1f1405fbba2033e161317df22462e9f216*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index b916328..91527f5 100644

//Synthetic comment -- @@ -58,7 +58,10 @@
// List of network operators that don't support SMS delivery report
private static final List<String> NO_DELIVERY_REPORTS =
Arrays.asList(
                    "310410",    // AT&T Mobility
                    "45005",     // SKT Mobility
                    "45002",     // SKT Mobility
                    "45008"      // KT Mobility
);

private TelephonyManager mTelephonyManager;







