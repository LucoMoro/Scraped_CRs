
//<Beginning of snippet n. 0>


import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.apache.tools.ant.util.DeweyDecimal;
import org.xml.sax.InputSource;

import java.io.File;
*
*/
public final class SetupTask extends ImportTask {
    private final static String ANT_MIN_VERSION = "1.8.0";
// main rules file
private final static String RULES_MAIN = "main_rules.xml";
// test rules file - depends on android_rules.xml
public void execute() throws BuildException {
Project antProject = getProject();

        // check the Ant version
        DeweyDecimal version = getVersion(antProject);
        DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
        if (atLeast.isGreaterThan(version)) {
            throw new BuildException(
                    "The Android Ant-based build system requires Ant " +
                    ANT_MIN_VERSION +
                    " or later. Current version is " +
                    version);
        }

// get the SDK location
File sdkDir = TaskHelper.getSdkLocation(antProject);
String sdkOsPath = sdkDir.getPath();

return libraries;
}

    /**
     * Returns the Ant version as a {@link DeweyDecimal} object.
     *
     * This is based on the implementation of
     * org.apache.tools.ant.taskdefs.condition.AntVersion.getVersion()
     *
     * @param antProject the current ant project.
     * @return the ant version.
     */
    private DeweyDecimal getVersion(Project antProject) {
        char[] versionString = antProject.getProperty("ant.version").toCharArray();
        StringBuffer sb = new StringBuffer();
        boolean foundFirstDigit = false;
        for (int i = 0; i < versionString.length; i++) {
            if (Character.isDigit(versionString[i])) {
                sb.append(versionString[i]);
                foundFirstDigit = true;
            }
            if (versionString[i] == '.' && foundFirstDigit) {
                sb.append(versionString[i]);
            }
            if (Character.isLetter(versionString[i]) && foundFirstDigit) {
                break;
            }
        }
        return new DeweyDecimal(sb.toString());
    }

}

//<End of snippet n. 0>








