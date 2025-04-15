/*Corrected buffer overflow when parsing /proc/wakelocks

The android_os_Process_parseProcLineArray in android_util_Process.cpp
needs one extra byte to terminate strings. This was already handled correctly
in android_os_Process_readProcFile, but was not handled in java when
parsing /proc/wakelocks in readKernelWakelockStats in BatteryStatsImpl.java.
This caused an assert in NewStringUTF when the output from /proc/wakelocks
was larger than 4096 bytes. The buffer was also increased in order to be
able to parse all wakelocks completely.

Change-Id:I22d2feeac0b75005819ea4fd56ea7d10f47e0fc4*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 5199ada..8cc029e 100644

//Synthetic comment -- @@ -830,12 +830,13 @@

private final Map<String, KernelWakelockStats> readKernelWakelockStats() {

        byte[] buffer = new byte[4096];
int len;
        
try {
FileInputStream is = new FileInputStream("/proc/wakelocks");
            len = is.read(buffer);
is.close();

if (len > 0) {
//Synthetic comment -- @@ -847,6 +848,12 @@
}
}
}
} catch (java.io.FileNotFoundException e) {
return null;
} catch (java.io.IOException e) {







