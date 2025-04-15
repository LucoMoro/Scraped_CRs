/*Remove obsolete stuff.

Change-Id:Ib3a4f7c0c479b4c8c7f2e27d47ba756969d4f0b4*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3b2afc8..c4e71b3 100644

//Synthetic comment -- @@ -24,17 +24,16 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkManager;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectCreator;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
//Synthetic comment -- @@ -45,8 +44,8 @@
import com.android.sdkuilib.internal.repository.SdkUpdaterNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.AvdManagerWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.utils.ILogger;
import com.android.utils.Pair;
//Synthetic comment -- @@ -276,9 +275,6 @@
} else if (SdkCommandLine.OBJECT_LIB_PROJECT.equals(directObject)) {
createProject(true /*library*/);

}
} else if (SdkCommandLine.VERB_UPDATE.equals(verb)) {
if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
//Synthetic comment -- @@ -1307,50 +1303,6 @@
}



/**
* Prompts the user to setup a hardware config for a Platform-based AVD.








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index a487881..68b1943 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
public static final String OBJECT_TEST_PROJECT   = "test-project";              //$NON-NLS-1$
public static final String OBJECT_LIB_PROJECT    = "lib-project";               //$NON-NLS-1$
public static final String OBJECT_ADB            = "adb";                       //$NON-NLS-1$

public static final String ARG_ALIAS        = "alias";                          //$NON-NLS-1$
public static final String ARG_ACTIVITY     = "activity";                       //$NON-NLS-1$
//Synthetic comment -- @@ -156,9 +155,6 @@

{ VERB_UPDATE, OBJECT_SDK,
"Updates the SDK by suggesting new platforms to install if available." },
};

public SdkCommandLine(ILogger logger) {
//Synthetic comment -- @@ -419,23 +415,6 @@
VERB_UPDATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,                //$NON-NLS-1$
"Target ID to set for the project.", null);

}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/MakeIdentity.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/MakeIdentity.java
deleted file mode 100644
//Synthetic comment -- index 955a81c..0000000

//Synthetic comment -- @@ -1,105 +0,0 @@







