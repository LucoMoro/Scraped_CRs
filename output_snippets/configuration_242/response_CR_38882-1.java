//<Beginning of snippet n. 0>
Project antProject = getProject();

// check the Ant version
DeweyDecimal version = getVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
    throw new BuildException("Ant version is less than required: " + ANT_MIN_VERSION);
}

// Validate sdkDir
if (sdkDir == null || !sdkDir.isDirectory() || !sdkDir.canRead()) {
    throw new BuildException("Invalid SDK directory: " + (sdkDir == null ? "null" : sdkDir.getAbsolutePath()));
}

// display SDK Tools revision
int toolsRevision = TaskHelper.getToolsRevision(sdkDir);
if (toolsRevision == -1) {
    logger.warning("Tools revision not found for SDK directory: " + sdkDir.getAbsolutePath());
} else {
    logger.info("Android SDK Tools Revision " + toolsRevision);
    logger.info("Installed at " + sdkDir.getAbsolutePath());
}

/**
 * @param antProject the current ant project.
 * @return the ant version.
 */
private DeweyDecimal getVersion(Project antProject) {
    String antVersion = antProject.getProperty("ant.version");
    if (antVersion == null || antVersion.isEmpty()) {
        throw new BuildException("Invalid 'ant.version' property: it cannot be null or empty.");
    }
    return new DeweyDecimal(antVersion);
}
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
import java.util.logging.Logger;

private static final Logger logger = Logger.getLogger("SDKToolsLogger");

/**
 * @param sdkFile the {@link File} for the root folder of the SDK
 * @return the tools revision or -1 if not found.
 */
static int getToolsRevision(File sdkFile) {
    Properties p = new Properties();
    try (FileInputStream input = new FileInputStream(new File(sdkFile, "source.properties"))) {
        p.load(input);
        String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
        if (value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        } else {
            logger.warning("Pkg.Revision is null or empty in: " + sdkFile.getAbsolutePath());
        }
    } catch (FileNotFoundException e) {
        throw new BuildException("Could not find source.properties file at path: " + sdkFile.getAbsolutePath(), e);
    } catch (IOException e) {
        throw new BuildException("IOException while loading properties from: " + sdkFile.getAbsolutePath(), e);
    } catch (NumberFormatException e) {
        logger.warning("Invalid number format for tools revision in: " + sdkFile.getAbsolutePath());
    }
    return -1;
}

static String checkSinglePath(String attribute, Path path) {
//<End of snippet n. 1>