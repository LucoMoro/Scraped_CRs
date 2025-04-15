/*Select AVD to run an app based on minSdkVersion

The list of AVD's capable of running a particular application
should be determined based on the application's minSdkLevel
rather than the current build target.

Change-Id:Id6428652ebe0ceb8e1a81e90e2146f1aab44c975*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index 22e5efa..855a7fa 100644

//Synthetic comment -- @@ -18,17 +18,20 @@

import com.android.ddmuilib.ImageLoader;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.internal.widgets.AvdSelector.IAvdFilter;
import com.android.utils.NullLogger;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -98,6 +101,7 @@
private Label mPreferredAvdLabel;

private IAndroidTarget mProjectTarget;
    private AndroidVersion mProjectMinApiVersion;

private boolean mSupportMultiDeviceLaunch;
private Button mAllDevicesTargetButton;
//Synthetic comment -- @@ -365,8 +369,31 @@
}

mPreferredAvdSelector.setManager(avdManager);
mPreferredAvdSelector.refresh(false);

        mPreferredAvdSelector.setFilter(new IAvdFilter() {
            @Override
            public void prepare() {
            }

            @Override
            public void cleanup() {
            }

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

/* (non-Javadoc)
//Synthetic comment -- @@ -429,6 +456,11 @@
// update the AVD list
if (project != null) {
mProjectTarget = Sdk.getCurrent().getTarget(project);

            ManifestInfo mi = ManifestInfo.get(project);
            final int minApiLevel = mi.getMinSdkVersion();
            final String minApiCodeName = mi.getMinSdkCodeName();
            mProjectMinApiVersion = new AndroidVersion(minApiLevel, minApiCodeName);
}

updateAvdList(avdManager);







