/*Update the NO_DELIVERY_REPORTS lists Add additional carriers that don’t support sms delivery support.
CTS Bug Fix : This parameter("311230" // C SPire Wireless + Celluar South) adds in CTS source code
This parameter("310600" // Cellcom) adds in CTS source code

Change-Id:I2da9296dfc8fe95633879905bd01f4316577b2d9Signed-off-by: Minheui Lee <nick2mhs1@gmail.com>*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 6c9a152..1056a13 100755

//Synthetic comment -- @@ -85,7 +85,9 @@
"30237",    // Fido
"311490",   // Virgin Mobile
"310000",   // Tracfone
                    "46003",    // China Telecom
                    "311230",   // C SPire Wireless + Celluar South
                    "310600"    // Cellcom
);

// List of network operators that doesn't support Data(binary) SMS message







