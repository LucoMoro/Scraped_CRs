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
                    if (AvdCompatibility.canRun(info, projectTarget, minApiVersion)
                            == AvdCompatibility.Compatibility.YES) {
compatibleRunningAvds.put(d, info);
}
} else {
//Synthetic comment -- @@ -496,7 +497,7 @@

// we are going to take the closest AVD. ie a compatible AVD that has the API level
// closest to the project target.
                AvdInfo defaultAvd = findMatchingAvd(avdManager, projectTarget, minApiVersion);

if (defaultAvd != null) {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -529,7 +530,7 @@
});
if (searchAgain[0]) {
// attempt to reload the AVDs and find one compatible.
                        defaultAvd = findMatchingAvd(avdManager, projectTarget, minApiVersion);

if (defaultAvd == null) {
AdtPlugin.printErrorToConsole(project, String.format(
//Synthetic comment -- @@ -683,23 +684,25 @@

/**
* Find a matching AVD.
     * @param minApiVersion
*/
    private AvdInfo findMatchingAvd(AvdManager avdManager, final IAndroidTarget projectTarget,
            AndroidVersion minApiVersion) {
AvdInfo[] avds = avdManager.getValidAvds();
        AvdInfo bestAvd = null;
for (AvdInfo avd : avds) {
            if (AvdCompatibility.canRun(avd, projectTarget, minApiVersion)
                    == AvdCompatibility.Compatibility.YES) {
// at this point we can ignore the code name issue since
                // AvdCompatibility.canRun() will already have filtered out the non compatible AVDs.
                if (bestAvd == null ||
avd.getTarget().getVersion().getApiLevel() <
                            bestAvd.getTarget().getVersion().getApiLevel()) {
                    bestAvd = avd;
}
}
}
        return bestAvd;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AvdCompatibility.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AvdCompatibility.java
new file mode 100644
//Synthetic comment -- index 0000000..6133002

//Synthetic comment -- @@ -0,0 +1,58 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.launch;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.avd.AvdInfo;

public class AvdCompatibility {
    public enum Compatibility {
        YES,
        NO,
        UNKNOWN,
    };

    /**
     * Returns whether the specified AVD can run the given project that is built against
     * a particular SDK and has the specified minApiLevel.
     * @param avd AVD to check compatibility for
     * @param projectTarget project build target
     * @param minApiVersion project min api level
     * @return whether the given AVD can run the given application
     */
    public static Compatibility canRun(AvdInfo avd, IAndroidTarget projectTarget,
            AndroidVersion minApiVersion) {
        if (avd == null) {
            return Compatibility.UNKNOWN;
        }

        IAndroidTarget avdTarget = avd.getTarget();
        if (avdTarget == null) {
            return Compatibility.UNKNOWN;
        }

        // for platform targets, we only need to check the min api version
        if (projectTarget.isPlatform()) {
            return avdTarget.getVersion().canRun(minApiVersion) ?
                    Compatibility.YES : Compatibility.NO;
        }

        // for add-on targets, delegate to the add on target to check for compatibility
        return projectTarget.canRunOn(avdTarget) ? Compatibility.YES : Compatibility.NO;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java
//Synthetic comment -- index 5638d51..10dcd01 100644

//Synthetic comment -- @@ -168,22 +168,17 @@
// get the AvdInfo
AvdInfo info = mSdk.getAvdManager().getAvd(device.getAvdName(),
true /*validAvdOnly*/);
                            AvdCompatibility.Compatibility c =
                                    AvdCompatibility.canRun(info, mProjectTarget,
                                            mMinApiVersion);
                            switch (c) {
                                case YES:
                                    return mMatchImage;
                                case NO:
                                    return mNoMatchImage;
                                case UNKNOWN:
                                    return mWarningImage;
}
}
}
}
//Synthetic comment -- @@ -774,23 +769,17 @@

@Override
public boolean accept(AvdInfo avd) {
if (mDevices != null) {
for (IDevice d : mDevices) {
                    // do not accept running avd's
if (avd.getName().equals(d.getAvdName())) {
return false;
}

                    // only accept avd's that can actually run the project
                    AvdCompatibility.Compatibility c =
                            AvdCompatibility.canRun(avd, mProjectTarget, mMinApiVersion);
                    return (c == AvdCompatibility.Compatibility.NO) ? false : true;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index f1c61c7..eee1542 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.launch.AvdCompatibility.Compatibility;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -394,16 +395,9 @@

@Override
public boolean accept(AvdInfo avd) {
                AvdCompatibility.Compatibility c =
                        AvdCompatibility.canRun(avd, mProjectTarget, mProjectMinApiVersion);
                return (c == Compatibility.NO) ? false : true;
}
});
}







