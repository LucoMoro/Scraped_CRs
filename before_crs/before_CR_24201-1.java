/*Update NO_DELIVERY_REPORTS list for SMS

Add "USCC" to the NO_DELIVERY_REPORTS list

Change-Id:I5c67d281632281e1ddad11a746203be3ca84a813*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 26aa9ef..bd2cbbf 100644

//Synthetic comment -- @@ -78,7 +78,8 @@
"44074",    // KDDI
"44075",    // KDDI
"44076",    // KDDI
                    "311870"    // Boost Mobile
);

// List of network operators that doesn't support Data(binary) SMS message







