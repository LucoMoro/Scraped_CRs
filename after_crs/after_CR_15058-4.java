/*Corrected buffer overflow when parsing /proc/wakelocks

The android_os_Process_parseProcLineArray in android_util_Process.cpp
writes up to buffer[endIndex]. This sometimes caused an assert to be
triggered in NewStringUTF when the output from /proc/wakelocks was
larger than 4096 bytes. The buffer was also increased in order to be
able to parse all wakelocks completely.

Change-Id:Idf8e66d61ad979377569048f59c3eee278b146db*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index aadb576..167e45d 100644

//Synthetic comment -- @@ -844,7 +844,7 @@

private final Map<String, KernelWakelockStats> readKernelWakelockStats() {

        byte[] buffer = new byte[8192];
int len;

try {
//Synthetic comment -- @@ -891,9 +891,11 @@
for (endIndex=startIndex; 
endIndex < len && wlBuffer[endIndex] != '\n' && wlBuffer[endIndex] != '\0'; 
endIndex++);
                endIndex++; // endIndex is an exclusive upper bound.
                // Don't go over the end of the buffer, Process.parseProcLine might
                // write to wlBuffer[endIndex]
                if (endIndex >= (len - 1) ) {
                    return m;
}

String[] nameStringArray = mProcWakelocksName;







