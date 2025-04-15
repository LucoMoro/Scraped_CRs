/*Fix for wiping out phone data due to security intrusion.

Changing the passed value to wipeDataLocked from 0 to 1. This will
enable the automatic formatting / wiping of emails, media files and
SD card when intrusion is detected. This happens in case users provide
the wrong password after a number of attempts.

Change-Id:I8ff4671ffa8149632298d0a87fdd0de6767ea2cd*/




//Synthetic comment -- diff --git a/services/java/com/android/server/DevicePolicyManagerService.java b/services/java/com/android/server/DevicePolicyManagerService.java
//Synthetic comment -- index 1538003..111c671 100644

//Synthetic comment -- @@ -958,7 +958,7 @@
saveSettingsLocked();
int max = getMaximumFailedPasswordsForWipe(null);
if (max > 0 && mFailedPasswordAttempts >= max) {
                    wipeDataLocked(1);
}
sendAdminCommandLocked(DeviceAdminReceiver.ACTION_PASSWORD_FAILED,
DeviceAdminInfo.USES_POLICY_WATCH_LOGIN);







