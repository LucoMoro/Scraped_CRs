/*The original timeout (4 mins) is too rigorous in live network.

Change-Id:I3699a0f3bc7d0b62d7e9235efb9132d4a1a7d58e*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3177d894..15326c0 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
private Intent mDeliveryIntent;
private boolean mDeliveryReportSupported;

    private static final int TIME_OUT = 1000 * 60 * 5;

@Override
protected void setUp() throws Exception {







