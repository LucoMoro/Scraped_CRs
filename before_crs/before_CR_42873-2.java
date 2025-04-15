/*avd launch: Fix paths using build target instead of minApi

This CL performs two changes:
 - Refactors the predicate to decide whether an AVD can run
   an application into a separate class.
 - Fix the AUTO launch mode to properly check compatibility using
   this new class as opposed to just checking against the project
   build target.

Change-Id:I9e0056e7fb855a18e7a14fe61af11682538dcc51*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 804d299..d9aab14 100644

//Synthetic comment -- @@ -464,7 +464,8 @@
String deviceAvd = d.getAvdName();
if (deviceAvd != null) { // physical devices return null.
AvdInfo info = avdManager.getAvd(deviceAvd, true /*validAvdOnly*/);
                    if (info != null && projectTarget.canRunOn(info.getTarget())) {
compatibleRunningAvds.put(d, info);
}
} else {
//Synthetic comment -- @@ -496,7 +497,7 @@

// we are going to take the closest AVD. ie a compatible AVD that has the API level
// closest to the project target.
                AvdInfo defaultAvd = findMatchingAvd(avdManager, projectTarget);

if (defaultAvd != null) {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -529,7 +530,7 @@
});
if (searchAgain[0]) {
// attempt to reload the AVDs and find one compatible.
                        defaultAvd = findMatchingAvd(avdManager, projectTarget);

if (defaultAvd == null) {
AdtPlugin.printErrorToConsole(project, String.format(
//Synthetic comment -- @@ -683,23 +684,25 @@

/**
* Find a matching AVD.
*/
    private AvdInfo findMatchingAvd(AvdManager avdManager, final IAndroidTarget projectTarget) {
AvdInfo[] avds = avdManager.getValidAvds();
        AvdInfo defaultAvd = null;
for (AvdInfo avd : avds) {
            if (projectTarget.canRunOn(avd.getTarget())) {
// at this point we can ignore the code name issue since
                // IAndroidTarget.canRunOn() will already have filtered the non
                // compatible AVDs.
                if (defaultAvd == null ||
avd.getTarget().getVersion().getApiLevel() <
                            defaultAvd.getTarget().getVersion().getApiLevel()) {
                    defaultAvd = avd;
}
}
}
        return defaultAvd;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AvdCompatibility.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AvdCompatibility.java
new file mode 100644
//Synthetic comment -- index 0000000..6133002

//Synthetic comment -- @@ -0,0 +1,58 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java
//Synthetic comment -- index 5638d51..10dcd01 100644

//Synthetic comment -- @@ -168,22 +168,17 @@
// get the AvdInfo
AvdInfo info = mSdk.getAvdManager().getAvd(device.getAvdName(),
true /*validAvdOnly*/);
                            if (info == null) {
                                return mWarningImage;
}
                            IAndroidTarget avdTarget = info.getTarget();
                            if (avdTarget == null) {
                                return mWarningImage;
                            }

                            // for platform targets, we only need to check the min api level
                            if (mProjectTarget.isPlatform()
                                    && avdTarget.getVersion().canRun(mMinApiVersion))
                                return mMatchImage;

                            // for add on targets, check if required libraries are available
                            return mProjectTarget.canRunOn(info.getTarget()) ?
                                    mMatchImage : mNoMatchImage;
}
}
}
//Synthetic comment -- @@ -774,23 +769,17 @@

@Override
public boolean accept(AvdInfo avd) {
            IAndroidTarget avdTarget = avd.getTarget();

if (mDevices != null) {
for (IDevice d : mDevices) {
if (avd.getName().equals(d.getAvdName())) {
return false;
}

                    if (avdTarget == null) {
                        return true;
                    }

                    if (mProjectTarget.isPlatform()) {
                        return avdTarget.getVersion().canRun(mMinApiVersion);
                    }

                    return mProjectTarget.canRunOn(avd.getTarget());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index f1c61c7..eee1542 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -394,16 +395,9 @@

@Override
public boolean accept(AvdInfo avd) {
                IAndroidTarget avdTarget = avd.getTarget();
                if (avdTarget == null) {
                    return true;
                }

                if (mProjectTarget.isPlatform()) {
                    return avdTarget.getVersion().canRun(mProjectMinApiVersion);
                }

                return mProjectTarget.canRunOn(avdTarget);
}
});
}







