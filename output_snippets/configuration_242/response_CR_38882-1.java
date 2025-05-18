//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Ant version is insufficient. Required: " + ANT_MIN_VERSION + ", Found: " + version);
}

// display SDK Tools revision
if (sdkDir != null && sdkDir.isDirectory()) {
    int toolsRevision = TaskHelper.getToolsRevision(sdkDir);
    if (toolsRevision != -1) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Android SDK Tools Revision " + toolsRevision);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Installed at " + sdkDir.getAbsolutePath());
    } else {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "SDK Tools revision not found.");
    }
} else {
    throw new BuildException("SDK directory is not specified or not a valid directory.");
}

/**
 * @param antProject the current ant project.
 * @return the ant version.
 */
private DeweyDecimal getVersion(Project antProject) {
    String versionString = antProject.getProperty("ant.version");
    if (versionString == null) {
        throw new BuildException("Property 'ant.version' not found in the project properties.");
    }
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param sdkFile the {@link File} for the root folder of the SDK
 * @return the tools revision or -1 if not found.
 */
static int getToolsRevision(File sdkFile) {
    Properties p = new Properties();
    File toolsFile = new File(sdkFile, "tools/package.xml"); // Adjusted path for demo purpose
    try (FileInputStream fis = new FileInputStream(toolsFile)) {
        p.load(fis);
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null) {
            return Integer.parseInt(value);
        }
    } catch (FileNotFoundException e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "SDK Tools file not found: " + toolsFile.getAbsolutePath(), e);
    } catch (IOException e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error reading SDK Tools file: " + toolsFile.getAbsolutePath(), e);
    }

    return -1;
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>