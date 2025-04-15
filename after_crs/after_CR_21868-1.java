/*Add a network operator into the not support list of delivery report

Change-Id:I5b24e88e82cc404a0fa26a08507ec7405ff9320d*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3177d894..0a6f7e2

//Synthetic comment -- @@ -59,6 +59,7 @@
private static final List<String> NO_DELIVERY_REPORTS =
Arrays.asList(
"310410"    // AT&T Mobility
                   ,"44010"     // NTT DOCOMO
);

private TelephonyManager mTelephonyManager;







