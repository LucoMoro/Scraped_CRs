/*Corrected buffer overflow when parsing /proc/wakelocks

The android_os_Process_parseProcLineArray in android_util_Process.cpp
writes up to buffer[endIndex]. This sometimes caused an assert to be
triggered in NewStringUTF when the output from /proc/wakelocks was
larger than 4096 bytes. The buffer was also increased in order to be
able to parse all wakelocks completely.

Change-Id:I6ebea09985c46e3625a5e13d5f85827d0c43c90c*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 5199ada..ea3f729 100644

//Synthetic comment -- @@ -830,7 +830,7 @@

private final Map<String, KernelWakelockStats> readKernelWakelockStats() {

        byte[] buffer = new byte[8192];
int len;

try {
//Synthetic comment -- @@ -878,6 +878,10 @@
endIndex < len && wlBuffer[endIndex] != '\n' && wlBuffer[endIndex] != '\0'; 
endIndex++);
endIndex++; // endIndex is an exclusive upper bound.
                if (endIndex >= (len - 1)) {
                    // Process.parseProcLine might write to wlBuffer[endIndex]
                    return m;
                }

String[] nameStringArray = mProcWakelocksName;
long[] wlData = mProcWakelocksData;







