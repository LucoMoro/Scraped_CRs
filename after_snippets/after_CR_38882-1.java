
//<Beginning of snippet n. 0>


Project antProject = getProject();

// check the Ant version
        DeweyDecimal version = getAntVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
throw new BuildException(
}

// display SDK Tools revision
        DeweyDecimal toolsRevison = TaskHelper.getToolsRevision(sdkDir);
        if (toolsRevison != null) {
System.out.println("Android SDK Tools Revision " + toolsRevison);
System.out.println("Installed at " + sdkDir.getAbsolutePath());
}
* @param antProject the current ant project.
* @return the ant version.
*/
    private DeweyDecimal getAntVersion(Project antProject) {
char[] versionString = antProject.getProperty("ant.version").toCharArray();
StringBuilder sb = new StringBuilder();
boolean foundFirstDigit = false;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


package com.android.ant;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.DeweyDecimal;

import java.io.File;
import java.io.FileInputStream;
* @param sdkFile the {@link File} for the root folder of the SDK
* @return the tools revision or -1 if not found.
*/
    @Nullable
    static DeweyDecimal getToolsRevision(File sdkFile) {
Properties p = new Properties();
try{
// tools folder must exist, or this custom task wouldn't run!

String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (value != null) {
                return new DeweyDecimal(value);
}
} catch (FileNotFoundException e) {
// couldn't find the file? return -1 below.
// couldn't find the file? return -1 below.
}

        return null;
}

static String checkSinglePath(String attribute, Path path) {

//<End of snippet n. 1>








