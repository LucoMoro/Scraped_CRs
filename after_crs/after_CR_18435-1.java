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
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -495,15 +494,9 @@
public void executeDx(IJavaProject javaProject, String[] inputPaths, String osOutFilePath)
throws CoreException, DexException {

// get the dex wrapper
        Sdk sdk = Sdk.getCurrent();
        DexWrapper wrapper = sdk.getDexWrapper();

if (wrapper == null) {
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 765dfbb..c388545 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -83,8 +82,6 @@

private final IAndroidTarget mTarget;

/**
* mAttributeValues is a map { key => list [ values ] }.
* The key for the map is "(element-xml-name,attribute-namespace:attribute-xml-local-name)".
//Synthetic comment -- @@ -115,10 +112,6 @@
mTarget = androidTarget;
}

/**
* Creates an AndroidTargetData object.
* @param platformLibraries
//Synthetic comment -- @@ -155,10 +148,6 @@
setOptionalLibraries(platformLibraries, optionalLibraries);
}

public IResourceRepository getSystemResources() {
return mSystemResourceRepository;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index fb19fc8..9c273fb 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
//Synthetic comment -- @@ -94,23 +93,10 @@
try {
SubMonitor progress = SubMonitor.convert(monitor,
String.format("Parsing SDK %1$s", mAndroidTarget.getName()),
                    13);

AndroidTargetData targetData = new AndroidTargetData(mAndroidTarget);

// parse the rest of the data.

AndroidJarLoader classLoader =








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 61d35c2..dcffb68 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
//Synthetic comment -- @@ -113,6 +114,7 @@
}

private final SdkManager mManager;
    private final DexWrapper mDexWrapper;
private final AvdManager mAvdManager;

/** Map associating an {@link IAndroidTarget} to an {@link AndroidTargetData} */
//Synthetic comment -- @@ -228,13 +230,25 @@
// get an SdkManager object for the location
SdkManager manager = SdkManager.createManager(sdkLocation, log);
if (manager != null) {
                // load DX.
                DexWrapper dexWrapper = new DexWrapper();
                String dexLocation =
                        sdkLocation + File.separator +
                        SdkConstants.OS_SDK_PLATFORM_TOOLS_LIB_FOLDER + SdkConstants.FN_DX_JAR;
                IStatus res = dexWrapper.loadDex(dexLocation);
                if (res != Status.OK_STATUS) {
                    log.error(null, res.getMessage());
                    dexWrapper = null;
                }

                // create the AVD Manager
AvdManager avdManager = null;
try {
avdManager = new AvdManager(manager, log);
} catch (AndroidLocationException e) {
log.error(e, "Error parsing the AVDs");
}
                sCurrentSdk = new Sdk(manager, dexWrapper, avdManager);
return sCurrentSdk;
} else {
StringBuilder sb = new StringBuilder("Error Loading the SDK:\n");
//Synthetic comment -- @@ -538,6 +552,14 @@
}

/**
     * Retursn a {@link DexWrapper} object to be used to execute dx commands. If dx.jar was not
     * loaded properly, then this will return <code>null</code>.
     */
    public DexWrapper getDexWrapper() {
        return mDexWrapper;
    }

    /**
* Returns the {@link AvdManager}. If the AvdManager failed to parse the AVD folder, this could
* be <code>null</code>.
*/
//Synthetic comment -- @@ -599,8 +621,9 @@
}
}

    private Sdk(SdkManager manager, DexWrapper dexWrapper, AvdManager avdManager) {
mManager = manager;
        mDexWrapper = dexWrapper;
mAvdManager = avdManager;

// listen to projects closing








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index 1016ba8..dc75c61 100644

//Synthetic comment -- @@ -67,9 +67,13 @@
public final static int AAPT                = 20;
/** OS Path to the target's version of the aidl tool. */
public final static int AIDL                = 21;
    /** OS Path to the target's version of the dx too.<br>
     * This is deprecated as dx is now in the platform tools and not in the platform. */
    @Deprecated
public final static int DX                  = 22;
    /** OS Path to the target's version of the dx.jar file.<br>
     * This is deprecated as dx.jar is now in the platform tools and not in the platform.. */
    @Deprecated
public final static int DX_JAR              = 23;
/** OS Path to the "ant" folder which contains the ant build rules (ver 2 and above) */
public final static int ANT                 = 24;







