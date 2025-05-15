//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Ant version is less than required: " + ANT_MIN_VERSION);
}

// display SDK Tools revision
if (sdkDir != null) {
    int toolsRevision = TaskHelper.getToolsRevision(sdkDir);
    if (toolsRevision != -1) {
        System.out.println("Android SDK Tools Revision " + toolsRevision);
        System.out.println("Installed at " + sdkDir.getAbsolutePath());
    }
} else {
    throw new BuildException("SDK directory is invalid or not specified.");
}

/* @param antProject the current ant project.
 * @return the ant version.
 */
private DeweyDecimal getVersion(Project antProject) {
    String versionString = antProject.getProperty("ant.version");
    if (versionString == null) {
        throw new BuildException("Ant version property not found.");
    }
    char[] versionChars = versionString.toCharArray();
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

/* @param sdkFile the {@link File} for the root folder of the SDK
 * @return the tools revision or -1 if not found.
 */
static int getToolsRevision(File sdkFile) {
    Properties p = new Properties();
    try (FileInputStream stream = new FileInputStream(new File(sdkFile, "tools/source.properties"))) {
        p.load(stream);
        String value = p.getProperty("Pkg.Revision");
        if (value != null) {
            return Integer.parseInt(value);
        }
    } catch (FileNotFoundException e) {
        // Log the missing file scenario
        System.err.println("Source properties file not found in SDK tools folder: " + e.getMessage());
    } catch (IOException e) {
        // Log the IO exception while loading properties
        System.err.println("Error reading source properties file: " + e.getMessage());
    } catch (NumberFormatException e) {
        // Log the parsing error for number format
        System.err.println("Error parsing tools revision: " + e.getMessage());
    }
    return -1;
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>