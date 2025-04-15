/*Misc fix on new project wizard.

- filter out addons that don't have extra APIs.
- display target codename when available.
- misc fix in validate re minSdk value.

(cherry picked from commit 60e722818e0343d988430246a8f8341d4a744528)

Change-Id:I9de0110b8fab94a3d1bf4ddedb14436ef05d258e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index c7c8cd8..b6aaf19 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
//Synthetic comment -- @@ -817,8 +818,14 @@
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
return String.format("API %1$d: Android %2$s", api,
                                        target.getProperty("ro.build.version.release"));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 16e89de..ed760fa 100644

//Synthetic comment -- @@ -162,11 +162,12 @@

mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
mBuildSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
mMinNameToApi = Maps.newHashMap();
List<String> labels = new ArrayList<String>(targets.length);
for (IAndroidTarget target : targets) {
            String targetLabel = target.getFullName();
labels.add(targetLabel);
mMinNameToApi.put(targetLabel, target.getVersion().getApiLevel());

//Synthetic comment -- @@ -183,7 +184,7 @@
int apiLevel = version.getApiLevel();
if (version.isPreview()) {
String codeName = version.getCodename();
                String targetLabel = "API " + apiLevel + ":" + codeName;
codeNames.add(targetLabel);
mMinNameToApi.put(targetLabel, apiLevel);
} else if (target.isPlatform()
//Synthetic comment -- @@ -300,6 +301,22 @@

}

private ControlDecoration createFieldDecoration(Control control, String description) {
ControlDecoration dec = new ControlDecoration(control, SWT.LEFT);
dec.setMarginWidth(2);
//Synthetic comment -- @@ -587,8 +604,14 @@
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"Select a minimum SDK version");
} else {
                    if (mValues.target != null && mValues.target.getVersion().getApiLevel() <
                            mValues.minSdkLevel) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"The minimum SDK version is higher than the build target version");
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 38d3bbb..080651a 100644

//Synthetic comment -- @@ -267,7 +267,7 @@
return compareTo(o.mApiLevel, o.mCodename);
}

    private int compareTo(int apiLevel, String codename) {
if (mCodename == null) {
if (codename == null) {
return mApiLevel - apiLevel;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/PkgProps.java
//Synthetic comment -- index 571a4a6..3996a51 100755

//Synthetic comment -- @@ -41,6 +41,7 @@
// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME         = "AndroidVersion.CodeName";//$NON-NLS-1$


//Synthetic comment -- @@ -79,6 +80,8 @@
// PlatformPackage

public static final String PLATFORM_VERSION         = "Platform.Version";       //$NON-NLS-1$
public static final String PLATFORM_INCLUDED_ABI    = "Platform.Included.Abi";  //$NON-NLS-1$

// ToolPackage







