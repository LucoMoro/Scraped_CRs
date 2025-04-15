/*To prevent the reference to null pointer

When the 'prev' obejct is null, the reference to member variable using that object should not be executed.

Change-Id:Ib01d9d4779caece75d456ef15d07c3de215c5f65*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
old mode 100644
new mode 100755
//Synthetic comment -- index 463493b..2d47e64

//Synthetic comment -- @@ -813,7 +813,7 @@
prev.resumeKeyDispatchingLocked();
}

        if (prev.app != null && prev.cpuTimeAtResume > 0
&& mService.mBatteryStatsService.isOnBattery()) {
long diff = 0;
synchronized (mService.mProcessStatsThread) {







