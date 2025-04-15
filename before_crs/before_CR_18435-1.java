/*DexWrapper is now loaded by the SDK instead of the platforms.

Since tools_r8, dx.jar is now located in platform-tools. instead
of platforms/android-<API>/tools/lib/.

There is therefore no need for all the platforms to load their
own dx.jar through reflection into DexWrapper. The SDK now does it
for all, and the builders query the SDK for the wrapper.

Change-Id:Ia79ccaf95237f2b25b43f26241e6335acc7050a3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 6462e03..2fde101 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -495,15 +494,9 @@
public void executeDx(IJavaProject javaProject, String[] inputPaths, String osOutFilePath)
throws CoreException, DexException {

        IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);
        AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
        if (targetData == null) {
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    Messages.ApkBuilder_UnableBuild_Dex_Not_loaded));
        }

// get the dex wrapper
        DexWrapper wrapper = targetData.getDexWrapper();

if (wrapper == null) {
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 765dfbb..c388545 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -83,8 +82,6 @@

private final IAndroidTarget mTarget;

    private DexWrapper mDexWrapper;

/**
* mAttributeValues is a map { key => list [ values ] }.
* The key for the map is "(element-xml-name,attribute-namespace:attribute-xml-local-name)".
//Synthetic comment -- @@ -115,10 +112,6 @@
mTarget = androidTarget;
}

    void setDexWrapper(DexWrapper wrapper) {
        mDexWrapper = wrapper;
    }

/**
* Creates an AndroidTargetData object.
* @param platformLibraries
//Synthetic comment -- @@ -155,10 +148,6 @@
setOptionalLibraries(platformLibraries, optionalLibraries);
}

    public DexWrapper getDexWrapper() {
        return mDexWrapper;
    }

public IResourceRepository getSystemResources() {
return mSystemResourceRepository;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index fb19fc8..9c273fb 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
//Synthetic comment -- @@ -94,23 +93,10 @@
try {
SubMonitor progress = SubMonitor.convert(monitor,
String.format("Parsing SDK %1$s", mAndroidTarget.getName()),
                    14);

AndroidTargetData targetData = new AndroidTargetData(mAndroidTarget);

            // load DX.
            DexWrapper dexWrapper = new DexWrapper();
            IStatus res = dexWrapper.loadDex(mAndroidTarget.getPath(IAndroidTarget.DX_JAR));
            if (res != Status.OK_STATUS) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                        String.format("dx.jar loading failed for target '%1$s'",
                                mAndroidTarget.getFullName()));
            }

            // we have loaded dx.
            targetData.setDexWrapper(dexWrapper);
            progress.worked(1);

// parse the rest of the data.

AndroidJarLoader classLoader =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 61d35c2..dcffb68 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
//Synthetic comment -- @@ -113,6 +114,7 @@
}

private final SdkManager mManager;
private final AvdManager mAvdManager;

/** Map associating an {@link IAndroidTarget} to an {@link AndroidTargetData} */
//Synthetic comment -- @@ -228,13 +230,25 @@
// get an SdkManager object for the location
SdkManager manager = SdkManager.createManager(sdkLocation, log);
if (manager != null) {
AvdManager avdManager = null;
try {
avdManager = new AvdManager(manager, log);
} catch (AndroidLocationException e) {
log.error(e, "Error parsing the AVDs");
}
                sCurrentSdk = new Sdk(manager, avdManager);
return sCurrentSdk;
} else {
StringBuilder sb = new StringBuilder("Error Loading the SDK:\n");
//Synthetic comment -- @@ -538,6 +552,14 @@
}

/**
* Returns the {@link AvdManager}. If the AvdManager failed to parse the AVD folder, this could
* be <code>null</code>.
*/
//Synthetic comment -- @@ -599,8 +621,9 @@
}
}

    private Sdk(SdkManager manager, AvdManager avdManager) {
mManager = manager;
mAvdManager = avdManager;

// listen to projects closing








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index 1016ba8..dc75c61 100644

//Synthetic comment -- @@ -67,9 +67,13 @@
public final static int AAPT                = 20;
/** OS Path to the target's version of the aidl tool. */
public final static int AIDL                = 21;
    /** OS Path to the target's version of the dx too. */
public final static int DX                  = 22;
    /** OS Path to the target's version of the dx.jar file. */
public final static int DX_JAR              = 23;
/** OS Path to the "ant" folder which contains the ant build rules (ver 2 and above) */
public final static int ANT                 = 24;







