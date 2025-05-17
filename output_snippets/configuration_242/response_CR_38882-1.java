//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Ant version must be at least " + ANT_MIN_VERSION);
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
    if (antProject == null) {
        throw new BuildException("Project cannot be null.");
    }
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
 * @return the tools revision or throws a BuildException if not found.
 */
static int getToolsRevision(File sdkFile) {
    if (sdkFile == null || !sdkFile.exists() || !sdkFile.isDirectory()) {
        throw new BuildException("SDK file must exist and be a directory.");
    }
    
    File toolsFile = new File(sdkFile, "tools.properties");
    if (!toolsFile.exists() || !toolsFile.isFile()) {
        throw new BuildException("tools.properties file not found at: " + toolsFile.getAbsolutePath());
    }
    
    Properties p = new Properties();
    try (FileInputStream fis = new FileInputStream(toolsFile)) {
        p.load(fis);
        
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        } else {
            throw new BuildException("Pkg.Revision not found in tools.properties.");
        }
    } catch (IOException e) {
        throw new BuildException("Error loading properties file: " + e.getMessage());
    } catch (NumberFormatException e) {
        throw new BuildException("Invalid number format for Pkg.Revision: " + e.getMessage());
    }
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>