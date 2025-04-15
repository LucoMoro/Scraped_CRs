/*43194: Fix NPE when preferred avd is null

Change-Id:If80929f0cf90bbc15f8bb1b065450058f1fb2e96*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 31d4d0f..4281f19 100644

//Synthetic comment -- @@ -377,6 +377,9 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
IAndroidTarget preferredAvdTarget = preferredAvd.getTarget();
if (preferredAvdTarget != null
&& !preferredAvdTarget.getVersion().canRun(minApiVersion)) {







