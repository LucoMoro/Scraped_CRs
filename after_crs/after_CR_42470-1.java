/*Policy change for wipe data when wrong password entered too many times

This changes the policy to also include the external storage when
the user fails to input the correct password, for the set number
of attempts.
We have requirements from operators that require external storage
to be included during these conditions.

Change-Id:Ie1e5c639d656f125eb1aff52827c8a03f2511d77*/




//Synthetic comment -- diff --git a/services/java/com/android/server/DevicePolicyManagerService.java b/services/java/com/android/server/DevicePolicyManagerService.java
//Synthetic comment -- index ea19d6e..b5198de 100644

//Synthetic comment -- @@ -1811,7 +1811,7 @@
saveSettingsLocked();
int max = getMaximumFailedPasswordsForWipe(null);
if (max > 0 && mFailedPasswordAttempts >= max) {
                    wipeDataLocked(1);
}
sendAdminCommandLocked(DeviceAdminReceiver.ACTION_PASSWORD_FAILED,
DeviceAdminInfo.USES_POLICY_WATCH_LOGIN);







