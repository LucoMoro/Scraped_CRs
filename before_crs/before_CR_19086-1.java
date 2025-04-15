/*Wi-Fi state is incorrect when enabling Wi-Fi

mLastInfo and mLastState need to be cleared when Wi-Fi is
disabled. Without this fix the shown state might be wrong
when enabling Wi-Fi again. This is clearly visible in the
scenario when the user connects to an AP and then disables
Wi-Fi and then walk out of range. When the user enables
Wi-Fi again it looks like the AP is still connected.

Change-Id:Idc0fe5da248acd50c1b05ac9b93c168db0d701cd*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiSettings.java b/src/com/android/settings/wifi/WifiSettings.java
//Synthetic comment -- index d389cae..75d0667 100644

//Synthetic comment -- @@ -465,6 +465,8 @@
} else {
mScanner.pause();
mAccessPoints.removeAll();
}
}








