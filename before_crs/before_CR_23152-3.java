/*Add support for addon providing their own layoutlib and/or res.

During addon parsing, the SDK Manager will detect whether the addon
has:
- data/layoutlib.jar
- data/res/
- data/fonts/

if the first one is present, then it is used during rendering.
if *both* the 2nd and 3rd ones are present, then addon resources
are used during rendering.

On the GLE side, all that's needed is adding addons to the list
of rendering targets if they have either library or resources (or
both).

Change-Id:Id16925eea2c98b9fbaaa884ac6fd8c1c1c444db2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 525ddf3..7ea34db 100644

//Synthetic comment -- @@ -1777,9 +1777,9 @@
IAndroidTarget[] targets = currentSdk.getTargets();
int match = -1;
for (int i = 0 ; i < targets.length; i++) {
                // FIXME: support add-on rendering and check based on project minSdkVersion
                if (targets[i].isPlatform()) {
                    mTargetCombo.add(targets[i].getFullName());
mTargetList.add(targets[i]);

if (mRenderingTarget != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 68372a2..7e5bc38 100644

//Synthetic comment -- @@ -579,8 +579,8 @@
// We have multiple directories - one for each combination of SDK, theme and device
// (and later, possibly other qualifiers).
// These are created -lazily-.
            String targetName = mPalette.getCurrentTarget().getFullName();
            String androidTargetNamePrefix = "Android ";
String themeNamePrefix = "Theme.";
if (targetName.startsWith(androidTargetNamePrefix)) {
targetName = targetName.substring(androidTargetNamePrefix.length());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 8d3ee7b..33d49a8 100644

//Synthetic comment -- @@ -19,8 +19,8 @@
import com.android.ddmlib.IDevice;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 5eba812..ebae458 100644

//Synthetic comment -- @@ -324,5 +324,9 @@
public int compareTo(IAndroidTarget o) {
return 0;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index 866d5b6..1c59720 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;

/**
* Represents an add-on target in the SDK.
//Synthetic comment -- @@ -72,6 +71,9 @@
private final String mVendor;
private final int mRevision;
private final String mDescription;
private String[] mSkins;
private String mDefaultSkin;
private IOptionalLibrary[] mLibraries;
//Synthetic comment -- @@ -88,10 +90,14 @@
* @param abis list of supported abis
* @param libMap A map containing the optional libraries. The map key is the fully-qualified
* library name. The value is a 2 string array with the .jar filename, and the description.
* @param basePlatform the platform the add-on is extending.
*/
AddOnTarget(String location, String name, String vendor, int revision, String description,
            String[] abis, Map<String, String[]> libMap, PlatformTarget basePlatform) {
if (location.endsWith(File.separator) == false) {
location = location + File.separator;
}
//Synthetic comment -- @@ -101,6 +107,8 @@
mVendor = vendor;
mRevision = revision;
mDescription = description;
mBasePlatform = basePlatform;

//set compatibility mode
//Synthetic comment -- @@ -161,7 +169,11 @@
}

public String getClasspathName() {
        return String.format("%1$s [%2$s]", mName, mBasePlatform.getName());
}

public String getDescription() {
//Synthetic comment -- @@ -196,6 +208,28 @@
case DOCS:
return mLocation + SdkConstants.FD_DOCS + File.separator
+ SdkConstants.FD_DOCS_REFERENCE;
case SAMPLES:
// only return the add-on samples folder if there is actually a sample (or more)
File sampleLoc = new File(mLocation, SdkConstants.FD_SAMPLES);
//Synthetic comment -- @@ -210,12 +244,16 @@
return sampleLoc.getAbsolutePath();
}
}
                // INTENDED FALL-THROUGH
default :
return mBasePlatform.getPath(pathId);
}
}

public String[] getSkins() {
return mSkins;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index 2ca7763..44c46ee 100644

//Synthetic comment -- @@ -124,6 +124,11 @@
String getClasspathName();

/**
* Returns the description of the target.
*/
String getDescription();
//Synthetic comment -- @@ -160,6 +165,11 @@
String getPath(int pathId);

/**
* Returns the available skins for this target.
*/
String[] getSkins();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 62720e7..c1ac84b 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

/**
* Represents a platform target in the SDK.
//Synthetic comment -- @@ -175,6 +174,10 @@
return mName;
}

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -210,6 +213,14 @@
return mPaths.get(pathId);
}

public String[] getSkins() {
return mSkins;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index d924d3d..43e9ac9 100644

//Synthetic comment -- @@ -554,9 +554,23 @@
}
}

String[] abiList = getAbiList(addonDir.getAbsolutePath());
AddOnTarget target = new AddOnTarget(addonDir.getAbsolutePath(), name, vendor,
                    revisionValue, description, abiList, libMap, baseTarget);

// need to parse the skins.
String[] skins = parseSkinFolder(target.getPath(IAndroidTarget.SKINS));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockAddonPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockAddonPackage.java
//Synthetic comment -- index 55ace17..1279529 100755

//Synthetic comment -- @@ -54,7 +54,11 @@
}

public String getClasspathName() {
            return null;
}

public String getDefaultSkin() {
//Synthetic comment -- @@ -62,11 +66,11 @@
}

public String getDescription() {
            return "mock addon target";
}

public String getFullName() {
            return "mock addon target";
}

public String[] getAbiList() {
//Synthetic comment -- @@ -157,5 +161,9 @@
public int compareTo(IAndroidTarget o) {
throw new UnsupportedOperationException("Implement this as needed for tests");
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformPackage.java
//Synthetic comment -- index ad1ab16..d3c1235 100755

//Synthetic comment -- @@ -86,7 +86,11 @@
}

public String getClasspathName() {
            return null;
}

public String getDefaultSkin() {
//Synthetic comment -- @@ -94,11 +98,11 @@
}

public String getDescription() {
            return "mock platform target";
}

public String getFullName() {
            return "mock platform target";
}

public String[] getAbiList() {
//Synthetic comment -- @@ -189,5 +193,9 @@
public int compareTo(IAndroidTarget o) {
throw new UnsupportedOperationException("Implement this as needed for tests");
}
}
}







