/*Update lists of special-cased SMS carriers

Change-Id:I8d5e58339dedee24f398841811970860450566aa*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3a8d87f..7d2f2b4 100755

//Synthetic comment -- @@ -84,8 +84,31 @@
"310000",   // Tracfone
"46003",    // China Telecom
"311230",   // C SPire Wireless + Celluar South
                    "310600",   // Cellcom
                    "31000"     // Republic Wireless US
);

// List of network operators that doesn't support Data(binary) SMS message
//Synthetic comment -- @@ -99,7 +122,30 @@
"30237",    // Fido
"45008",    // KT
"45005",    // SKT Mobility
                    "45002"     // SKT Mobility
);

// List of network operators that doesn't support Maltipart SMS message







