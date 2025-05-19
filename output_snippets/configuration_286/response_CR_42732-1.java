//<Beginning of snippet n. 0>
public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the SDK Ant folder, i.e. "ant" */
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
import com.android.sdklib.IAndroidTarget;

mValues.chosenSample = sample;
if (sample != null && !mValues.projectNameModifiedByUser) {
    mValues.projectName = sample.getName();
    try {
        mIgnore = true;
        mSampleProjectName.setText(mValues.projectName);
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
for (Entry<File, String> entry : extras.entrySet()) {
    File path = entry.getKey();
    String name = entry.getValue();
    findSamplesManifests(path, path, name, target.getVersion(), mValues.samples);
}

for (File f : currDir.listFiles()) {
    if (f.isDirectory()) {
        File manifestFile = new File(f, SdkConstants.FN_ANDROID_MANIFEST_XML);
        if (manifestFile.isFile() || 
            (f.getName().equals(FD_SAMPLES) && f.listFiles().length == 1 && f.listFiles()[0].getName().equals(SdkConstants.FN_ANDROID_MANIFEST_XML))) {
            try {
                ManifestData data = AndroidManifestParser.parse(new FileWrapper(manifestFile));
                if (data != null) {
                    boolean accept = targetVersion == null || data.getMinSdkVersion() <= targetVersion.getApiLevel() || 
                                    (data.getMinSdkVersionString() != null && data.getMinSdkVersionString().equals(targetVersion.getCodename()));
                    if (accept) {
                        String name = getSampleDisplayName(extraName, rootDir, f);
                        samplesPaths.add(Pair.of(name, f));
                    }
                }
            } catch (Exception e) {
                AdtPlugin.log(IStatus.INFO, "NPW ignoring malformed manifest %s", manifestFile.getAbsolutePath()); //$NON-NLS-1$
            }
        } else if (f.getName().equals(FD_SAMPLES)) {
            AdtPlugin.log(IStatus.WARNING, "Sample directory %s does not contain a valid Android manifest", f.getAbsolutePath()); //$NON-NLS-1$
        }
    }
}

// Recurse in the project, to find embedded tests sub-projects
// We can however skip this recursion for known android sub-dirs that
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
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
        path = path.replaceAll(Pattern.quote(File.separator), " > "); //$NON-NLS-1$
    }
    if (extraName != null) {
        path = path + " [" + extraName + ']'; //$NON-NLS-1$
    }
    return path;
}

private void validatePage() {
    File topLevelSamplesPath = new File(a.getLocalOsPath(), SdkConstants.FD_SAMPLES);
    if (topLevelSamplesPath.isDirectory()) {
        samples.put(topLevelSamplesPath, pkg.getListDescription());
    }
    for (File file : topLevelSamplesPath.listFiles()) {
        if (file.isDirectory() && 
            (file.getName().equals(FD_SAMPLES) || 
            (file.listFiles().length == 1 && new File(file, SdkConstants.FN_ANDROID_MANIFEST_XML).exists()))) {
            File manifest = new File(file, SdkConstants.FN_ANDROID_MANIFEST_XML);
            if (manifest.exists()) {
                samples.put(file, pkg.getListDescription());
            }
        }
    }
}
//<End of snippet n. 3>