//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Ant version must be at least " + ANT_MIN_VERSION + " but found " + version);
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
    String versionProp = antProject.getProperty("ant.version");
    if (versionProp == null) {
        throw new BuildException("Failed to retrieve the Ant version property.");
    }
    char[] versionString = versionProp.toCharArray();
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
    Properties p = new Properties();
    File toolsFile = new File(sdkFile, "tools/ant/build.properties");
    if (!toolsFile.exists()) {
        throw new BuildException("SDK Tools directory does not exist: " + toolsFile.getAbsolutePath());
    }
    try (FileInputStream fis = new FileInputStream(toolsFile)) {
        p.load(fis);
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null) {
            return Integer.parseInt(value);
        }
    } catch (IOException e) {
        throw new BuildException("Error loading properties from SDK Tools file: " + toolsFile.getAbsolutePath(), e);
    }
    return -1;
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>