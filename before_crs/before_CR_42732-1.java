/*NPW Sample Wizard: list extras with a "sample" directory.

Some extras have just a single "sample" directory
instead of having a samples/<sample_name> directory.
Support this and list them as valid samples.

Change-Id:I25bc59db0b9853d98385cca98f70bf261558fd8b*/
//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 26db7aa..6f47fa9 100644

//Synthetic comment -- @@ -247,6 +247,15 @@
public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the SDK Ant folder, i.e. "ant" */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/SampleSelectionPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/SampleSelectionPage.java
//Synthetic comment -- index ec701fa..1972470 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -150,6 +151,10 @@
mValues.chosenSample = sample;
if (sample != null && !mValues.projectNameModifiedByUser) {
mValues.projectName = sample.getName();
try {
mIgnore = true;
mSampleProjectName.setText(mValues.projectName);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/SdkSelectionPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/SdkSelectionPage.java
//Synthetic comment -- index 9ce5060..6cafcf0 100644

//Synthetic comment -- @@ -222,9 +222,17 @@
for (Entry<File, String> entry : extras.entrySet()) {
File path = entry.getKey();
String name = entry.getValue();
                    findSamplesManifests(path, path, name,
                                         target.getVersion(),
                                         mValues.samples);
}
}

//Synthetic comment -- @@ -280,40 +288,7 @@

for (File f : currDir.listFiles()) {
if (f.isDirectory()) {
                // Assume this is a sample if it contains an android manifest.
                File manifestFile = new File(f, SdkConstants.FN_ANDROID_MANIFEST_XML);
                if (manifestFile.isFile()) {
                    try {
                        ManifestData data =
                            AndroidManifestParser.parse(new FileWrapper(manifestFile));
                        if (data != null) {
                            boolean accept = false;
                            if (targetVersion == null) {
                                accept = true;
                            } else if (targetVersion != null) {
                                int i = data.getMinSdkVersion();
                                if (i != ManifestData.MIN_SDK_CODENAME) {
                                   accept = i <= targetVersion.getApiLevel();
                                } else {
                                    String s = data.getMinSdkVersionString();
                                    if (s != null) {
                                        accept = s.equals(targetVersion.getCodename());
                                    }
                                }
                            }

                            if (accept) {
                                String name = getSampleDisplayName(extraName, rootDir, f);
                                samplesPaths.add(Pair.of(name, f));
                            }
                        }
                    } catch (Exception e) {
                        // Ignore. Don't use a sample which manifest doesn't parse correctly.
                        AdtPlugin.log(IStatus.INFO,
                                "NPW ignoring malformed manifest %s",   //$NON-NLS-1$
                                manifestFile.getAbsolutePath());
                    }
                }

// Recurse in the project, to find embedded tests sub-projects
// We can however skip this recursion for known android sub-dirs that
//Synthetic comment -- @@ -328,27 +303,98 @@
}
}

/**
* Compute the sample name compared to its root directory.
*/
private String getSampleDisplayName(String extraName, File rootDir, File sampleDir) {
        String path = sampleDir.getPath();
        int n = rootDir.getPath().length();
        if (path.length() > n) {
            path = path.substring(n);
            if (path.charAt(0) == File.separatorChar) {
                path = path.substring(1);
}
            if (path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1);
}
            path = path.replaceAll(Pattern.quote(File.separator), " > ");   //$NON-NLS-1$
}
if (extraName != null) {
            path = path + " [" + extraName + ']';                            //$NON-NLS-1$
}

        return path;
}

private void validatePage() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 5986387..0bca185 100644

//Synthetic comment -- @@ -381,6 +381,14 @@
File path = new File(a.getLocalOsPath(), SdkConstants.FD_SAMPLES);
if (path.isDirectory()) {
samples.put(path, pkg.getListDescription());
}
}
}







