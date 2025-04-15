/*Fix time zone info in kernel is nothing after reboot

If sys_tz is not set correctly, the timestamp of
filesystem will be in-consistent with android's time,
until time zone is set from android's UI. Set kernel
timezone while initializing AlarmManagerService.

Change-Id:Ic85def954fc9388e943e3dee45767d8179825a49*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index e088417..8852cc3 100644

//Synthetic comment -- @@ -122,6 +122,14 @@
public AlarmManagerService(Context context) {
mContext = context;
mDescriptor = init();
PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);








