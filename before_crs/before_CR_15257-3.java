/*Detect Unwhitelisted Root Processes

Bug 2738144

Add a test that checks for root processes currently running on the
phone. It uses a large whitelist since a lot of system processes
run as root...this test will probably raise a lof of waiver
questions since these processes often correspond to hardware
drivers.

Change-Id:I28ee6cf563c9e7073836e3534703c33c8925458f*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/RootProcessTest.java b/tests/tests/os/src/android/os/cts/RootProcessTest.java
new file mode 100644
//Synthetic comment -- index 0000000..50fd979

//Synthetic comment -- @@ -0,0 +1,214 @@







