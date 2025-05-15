//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Unsupported Ant version: " + version + ". Minimum required is " + ANT_MIN_VERSION + ".");
}

// display SDK Tools revision
if (sdkDir != null && sdkDir.exists()) {
    int toolsRevision = TaskHelper.getToolsRevision(sdkDir);
    if (toolsRevision != -1) {
        System.out.println("Android SDK Tools Revision " + toolsRevision);
        System.out.println("Installed at " + sdkDir.getAbsolutePath());
    } else {
        System.err.println("Error: Tools revision not found.");
    }
} else {
    System.err.println("Error: SDK directory is invalid or does not exist.");
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @param sdkFile the {@link File} for the root folder of the SDK
 * @return the tools revision or -1 if not found.
 */
static int getToolsRevision(File sdkFile) {
    Properties p = new Properties();
    File propertiesFile = new File(sdkFile, "tools/sdk_tools.properties");
    
    if (!propertiesFile.exists()) {
        System.err.println("Error: Properties file not found at " + propertiesFile.getAbsolutePath());
        return -1;
    }

    try (FileInputStream fis = new FileInputStream(propertiesFile)) {
        p.load(fis);
        
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null) {
            return Integer.parseInt(value);
        }
    } catch (FileNotFoundException e) {
        System.err.println("Error: Properties file not found.");
    } catch (IOException e) {
        System.err.println("Error reading properties file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.err.println("Error: Invalid format for tools revision.");
    }
    
    return -1; 
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>