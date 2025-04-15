/*DevicePolicy: Fix lockNow() failing to turn off screen

A fix introduced in 93c518e4f8abd98f87cda1712b30a5a86cfa60dd
causes lockNow() to sometimes (most of the time) fail to turn
off the screen. The screen will turn off, but then immediately turn
on showing the lock screen.

This is caused by getWindowManager().lockNow() call which calls the
method to lock the screen, which the PowerManager service also
call in parallel, leading to a race condition.

The fix is to only call getWindowManager().lockNow() if the screen
is not on.

Change-Id:I235c0f34d5cc15268d569e18e025a87330deced7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/DevicePolicyManagerService.java b/services/java/com/android/server/DevicePolicyManagerService.java
//Synthetic comment -- index d8e3d59..82864b0 100644

//Synthetic comment -- @@ -1659,11 +1659,14 @@
DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
long ident = Binder.clearCallingIdentity();
try {
                // Power off the display
                mIPowerManager.goToSleepWithReason(SystemClock.uptimeMillis(),
                        WindowManagerPolicy.OFF_BECAUSE_OF_ADMIN);
                // Ensure the device is locked
                getWindowManager().lockNow();
} catch (RemoteException e) {
} finally {
Binder.restoreCallingIdentity(ident);







