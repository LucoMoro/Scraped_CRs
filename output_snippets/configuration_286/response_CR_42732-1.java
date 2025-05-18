//<Beginning of snippet n. 0>
public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the manifest file */
public final static String FN_ANDROID_MANIFEST_XML = "AndroidManifest.xml"; // for clarity
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
import com.android.sdklib.IAndroidTarget;

if (sample != null) {
    mValues.chosenSample = sample;
    if (!mValues.projectNameModifiedByUser) {
        mValues.projectName = sample.getName();
    }
    try {
        mIgnore = true;
        mSampleProjectName.setText(mValues.projectName);
    } catch (Exception e) {
        AdtPlugin.log(IStatus.INFO, "Error setting sample project name: %s", e.getMessage());
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
for (Entry<File, String> entry : extras.entrySet()) {
    File path = entry.getKey();
    String name = entry.getValue();
    findSamplesManifests(path, path, name, target.getVersion(), mValues.samples);
}

File topLevelSampleDir = new File(currDir, SdkConstants.FD_SAMPLES);
if (topLevelSampleDir.isDirectory()) {
    if (new File(topLevelSampleDir, SdkConstants.FN_ANDROID_MANIFEST_XML).isFile()) {
        processManifestFile(topLevelSampleDir, targetVersion, samples);
    }
    for (File f : topLevelSampleDir.listFiles()) {
        if (f.isDirectory() && new File(f, SdkConstants.FN_ANDROID_MANIFEST_XML).isFile()) {
            processManifestFile(f, targetVersion, samples);
        }
    }
}

for (File f : currDir.listFiles()) {
    if (f.isDirectory() && new File(f, SdkConstants.FN_ANDROID_MANIFEST_XML).isFile()) {
        processManifestFile(f, targetVersion, samples);
    }
}

private void processManifestFile(File dir, IAndroidTarget targetVersion, List<Pair<String, File>> samples) {
    File manifestFile = new File(dir, SdkConstants.FN_ANDROID_MANIFEST_XML);
    try {
        ManifestData data = AndroidManifestParser.parse(new FileWrapper(manifestFile));
        if (data != null) {
            boolean accept = false;
            if (targetVersion == null || data.getMinSdkVersion() <= targetVersion.getApiLevel()) {
                accept = true;
            } else {
                String s = data.getMinSdkVersionString();
                if (s != null) {
                    accept = s.equals(targetVersion.getCodename());
                }
            }

            if (accept) {
                String name = getSampleDisplayName(mValues.projectName, currDir, dir);
                samples.add(Pair.of(name, dir));
            }
        }
    } catch (Exception e) {
        AdtPlugin.log(IStatus.INFO, "NPW ignoring malformed manifest %s", manifestFile.getAbsolutePath());
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
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
File path = new File(a.getLocalOsPath(), SdkConstants.FD_SAMPLES);
if (path.isDirectory()) {
    if (new File(path, SdkConstants.FN_ANDROID_MANIFEST_XML).isFile()) {
        samples.put(path, pkg.getListDescription());
    }
}
//<End of snippet n. 3>