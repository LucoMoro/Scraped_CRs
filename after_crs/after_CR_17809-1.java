/*CTS host logging fixes.

Clean up all occurrences of logging empty error messages.

Change-Id:I9412232cd4eab9ff74932999f8ea1270c8dabe09*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/DeviceManager.java b/tools/host/src/com/android/cts/DeviceManager.java
//Synthetic comment -- index 528036e..c164ac1 100644

//Synthetic comment -- @@ -218,7 +218,7 @@
try {
Thread.sleep(100);
} catch (InterruptedException e) {
                        Log.d("polling for device sync service interrupted");
}
}
CUIOutputStream.println("Device(" + mDevice + ") connected");








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/Log.java b/tools/host/src/com/android/cts/Log.java
//Synthetic comment -- index 9ee99c8..4c6e2c8 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
if (!HostConfig.DEBUG) {
CUIOutputStream.println(ERROR_PREFIX + msg);
if (e != null) {
                CUIOutputStream.println(e.toString());
}
return;
}








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index 9c82af1..ca973a5 100644

//Synthetic comment -- @@ -1673,7 +1673,8 @@
try {
mDevice.executeShellCommand(cmd, receiver);
} catch (IOException e) {
                    Log.e(String.format("Failed to execute shell command %s on device %s", cmd,
                    		mDevice.getSerialNumber()), e);
}
}
}.start();








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestPackage.java b/tools/host/src/com/android/cts/TestPackage.java
//Synthetic comment -- index db1e449..5dda51f 100644

//Synthetic comment -- @@ -641,7 +641,7 @@
CUIOutputStream.println("Test stopped.");
mTestThread.join();
} catch (InterruptedException e) {
            Log.d("test thread interrupted");
}
}

//Synthetic comment -- @@ -753,14 +753,14 @@
try {
wait();
} catch (InterruptedException e) {
                    Log.d("interrupted while waiting for package action complete");
}
}
}
try {
Thread.sleep(HostConfig.Ints.postInstallWaitMs.value());
} catch (InterruptedException e) {
        	Log.d("interrupted while sleeping after package action complete");
}
Log.d("Leave waitPackageActionComplete()");
}







