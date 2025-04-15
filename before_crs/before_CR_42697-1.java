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
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.utils.NullLogger;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -98,6 +101,7 @@
private Label mPreferredAvdLabel;

private IAndroidTarget mProjectTarget;

private boolean mSupportMultiDeviceLaunch;
private Button mAllDevicesTargetButton;
//Synthetic comment -- @@ -365,8 +369,31 @@
}

mPreferredAvdSelector.setManager(avdManager);
        mPreferredAvdSelector.setFilter(mProjectTarget);
mPreferredAvdSelector.refresh(false);
}

/* (non-Javadoc)
//Synthetic comment -- @@ -429,6 +456,11 @@
// update the AVD list
if (project != null) {
mProjectTarget = Sdk.getCurrent().getTarget(project);
}

updateAvdList(avdManager);







