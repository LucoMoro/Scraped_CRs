
//<Beginning of snippet n. 0>


public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
    /**
     * Name of an extra's sample folder.
     * Ideally extras should have one {@link #FD_SAMPLES} folder containing
     * one or more sub-folders (one per sample). However some older extras
     * might contain a single "sample" folder with directly the samples files
     * in it. When possible we should encourage extras' owners to move to the
     * multi-samples format.
     */
    public final static String FD_SAMPLE = "sample";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the SDK Ant folder, i.e. "ant" */

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.SdkConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
import com.android.sdklib.IAndroidTarget;
mValues.chosenSample = sample;
if (sample != null && !mValues.projectNameModifiedByUser) {
mValues.projectName = sample.getName();
            if (SdkConstants.FD_SAMPLE.equals(mValues.projectName) &&
                    sample.getParentFile() != null) {
                mValues.projectName = sample.getParentFile().getName() + '_' + mValues.projectName;
            }
try {
mIgnore = true;
mSampleProjectName.setText(mValues.projectName);

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


for (Entry<File, String> entry : extras.entrySet()) {
File path = entry.getKey();
String name = entry.getValue();

                    // Case where the sample is at the root of the directory and not
                    // in a per-sample sub-directory.
                    if (path.getName().equals(SdkConstants.FD_SAMPLE)) {
                        findSampleManifestInDir(
                                path, path, name, target.getVersion(), mValues.samples);
                    }

                    // Scan sub-directories
                    findSamplesManifests(
                            path, path, name, target.getVersion(), mValues.samples);
}
}


for (File f : currDir.listFiles()) {
if (f.isDirectory()) {
                findSampleManifestInDir(f, rootDir, extraName, targetVersion, samplesPaths);

// Recurse in the project, to find embedded tests sub-projects
// We can however skip this recursion for known android sub-dirs that
}
}

    private void findSampleManifestInDir(
            File sampleDir,
            File rootDir,
            String extraName,
            AndroidVersion targetVersion,
            List<Pair<String, File>> samplesPaths) {
        // Assume this is a sample if it contains an android manifest.
        File manifestFile = new File(sampleDir, SdkConstants.FN_ANDROID_MANIFEST_XML);
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
                        String name = getSampleDisplayName(extraName, rootDir, sampleDir);
                        samplesPaths.add(Pair.of(name, sampleDir));
                    }
                }
            } catch (Exception e) {
                // Ignore. Don't use a sample which manifest doesn't parse correctly.
                AdtPlugin.log(IStatus.INFO,
                        "NPW ignoring malformed manifest %s",   //$NON-NLS-1$
                        manifestFile.getAbsolutePath());
            }
        }
    }

/**
* Compute the sample name compared to its root directory.
*/
private String getSampleDisplayName(String extraName, File rootDir, File sampleDir) {
        String name = null;
        if (!rootDir.equals(sampleDir)) {
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
                name = path.replaceAll(Pattern.quote(File.separator), " > ");   //$NON-NLS-1$
}
        }
        if (name == null &&
                rootDir.equals(sampleDir) &&
                sampleDir.getName().equals(SdkConstants.FD_SAMPLE) &&
                extraName != null) {
            // This is an old-style extra with one single sample directory. Just use the
            // extra's name as the same name.
            return extraName;
        }
        if (name == null) {
            // Otherwise try to use the sample's directory name as the sample name.
            while (sampleDir != null &&
                   (name == null ||
                    SdkConstants.FD_SAMPLE.equals(name) ||
                    SdkConstants.FD_SAMPLES.equals(name))) {
                name = sampleDir.getName();
                sampleDir = sampleDir.getParentFile();
}
        }
        if (name == null) {
            if (extraName != null) {
                // In the unlikely case nothing worked and we have an extra name, use that.
                return extraName;
            } else {
                name = "Sample"; // fallback name... should not happen.         //$NON-NLS-1$
            }
}
if (extraName != null) {
            name = name + " [" + extraName + ']';                               //$NON-NLS-1$
}

        return name;
}

private void validatePage() {

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


File path = new File(a.getLocalOsPath(), SdkConstants.FD_SAMPLES);
if (path.isDirectory()) {
samples.put(path, pkg.getListDescription());
                    continue;
                }
                // Some old-style extras simply have a single "sample" directory.
                // Accept it if it contains an AndroidManifest.xml.
                path = new File(a.getLocalOsPath(), SdkConstants.FD_SAMPLE);
                if (path.isDirectory() &&
                        new File(path, SdkConstants.FN_ANDROID_MANIFEST_XML).isFile()) {
                    samples.put(path, pkg.getListDescription());
}
}
}

//<End of snippet n. 3>








