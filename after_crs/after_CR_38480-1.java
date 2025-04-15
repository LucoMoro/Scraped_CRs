/*Update the NO_DELIVERY_REPORTS list to add USCC carrier that doesn’t support sms delivery report

Change-Id:I8f9d96712f55668ca4d0a134f2642f61354668ad*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 1056a13..2257111 100755

//Synthetic comment -- @@ -79,6 +79,8 @@
"44076",    // KDDI
"311870",   // Boost Mobile
"311220",   // USCC
                    "311225",   // USCC LTE
                    "311580",   // USCC LTE
"302720",   // Rogers
"30272",    // Rogers
"302370",   // Fido







