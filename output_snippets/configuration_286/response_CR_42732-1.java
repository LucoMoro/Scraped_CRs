//<Beginning of snippet n. 0>


public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the SDK Ant folder, i.e. "ant" */

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


*/
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
findSamplesManifests(path, path, name,
                     target.getVersion(),
                     mValues.samples);
}
}


for (File f : currDir.listFiles()) {
if (f.isDirectory()) {
    // Check for "sample" subdirectory
    File[] subDirs = f.listFiles(File::isDirectory);
    boolean hasSingleSampleDir = subDirs != null && subDirs.length == 1 && "sample".equals(subDirs[0].getName());

    // Assume this is a sample if it contains an android manifest or is a single "sample" directory.
    File manifestFile = new File(f, SdkConstants.FN_ANDROID_MANIFEST_XML);
    if (manifestFile.isFile() || hasSingleSampleDir) {
        try {
            ManifestData data = AndroidManifestParser.parse(new FileWrapper(manifestFile));
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
    samples.put(path, pkg.getListDescription());
}
}

//<End of snippet n. 3>