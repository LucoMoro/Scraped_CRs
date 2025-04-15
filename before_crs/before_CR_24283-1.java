/*Motorola Mobility Inc. CTS Patch for CTS 2.1
Signed-off-by: Ji Xiao <ejx002@gmail.com>

Change-Id:I6b804077fb3bd11e6a099f00009d6c14a51a6a2d*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ListeningPortsTest.java b/tests/tests/net/src/android/net/cts/ListeningPortsTest.java
//Synthetic comment -- index b6e6efb..9657093 100644

//Synthetic comment -- @@ -44,6 +44,8 @@
EXCEPTION_PATTERNS.add("00000000:15B3"); // 0.0.0.0:5555   - emulator port
EXCEPTION_PATTERNS.add("0F02000A:15B3"); // 10.0.2.15:5555 - net forwarding for emulator
EXCEPTION_PATTERNS.add("[0-9A-F]{6}7F:[0-9A-F]{4}"); // IPv4 Loopback

// IPv6 exceptions
EXCEPTION_PATTERNS.add("[0]{31}1:[0-9A-F]{4}"); // IPv6 Loopback







