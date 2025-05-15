//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Insufficient Ant version. Required: " + ANT_MIN_VERSION + ", Found: " + version);
}

// display SDK Tools revision
int toolsRevision = TaskHelper.getToolsRevision(sdkDir);
if (toolsRevision != -1) {
    System.out.println("Android SDK Tools Revision " + toolsRevision);
    System.out.println("Installed at " + sdkDir.getAbsolutePath());
}

/**
 * @param antProject the current ant project.
 * @return the ant version.
 */
private DeweyDecimal getVersion(Project antProject) {
    char[] versionString = antProject.getProperty("ant.version").toCharArray();
    StringBuilder sb = new StringBuilder();
    boolean foundFirstDigit = false;
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ant;

import com.android.annotations.NonNull;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @param sdkFile the {@link File} for the root folder of the SDK
 * @return the tools revision or -1 if not found.
 */
static int getToolsRevision(File sdkFile) {
    if (sdkFile == null || !sdkFile.isDirectory()) {
        throw new IllegalArgumentException("Invalid SDK directory: " + sdkFile);
    }

    Properties p = new Properties();
    File propertiesFile = new File(sdkFile, "source.properties");

    try (FileInputStream fis = new FileInputStream(propertiesFile)) {
        p.load(fis);
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null) {
            return Integer.parseInt(value);
        } else {
            throw new BuildException("Property 'Pkg.Revision' not found in " + propertiesFile.getAbsolutePath());
        }
    } catch (IOException e) {
        // couldn't find the file? return -1 below.
        return -1;
    }
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>