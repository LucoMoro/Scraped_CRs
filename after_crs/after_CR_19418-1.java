/*Add Ant version check in setup task.

Change-Id:Id2237ae2fd64a1ccae5b1a1957099c218cdaf9a5*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 4f14d62..809825b 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.apache.tools.ant.util.DeweyDecimal;
import org.xml.sax.InputSource;

import java.io.File;
//Synthetic comment -- @@ -64,6 +65,7 @@
*
*/
public final class SetupTask extends ImportTask {
    private final static String ANT_MIN_VERSION = "1.8.0";
// main rules file
private final static String RULES_MAIN = "main_rules.xml";
// test rules file - depends on android_rules.xml
//Synthetic comment -- @@ -77,6 +79,17 @@
public void execute() throws BuildException {
Project antProject = getProject();

        // checks the Ant version
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
//Synthetic comment -- @@ -548,4 +561,32 @@

return libraries;
}

    /**
     * Returns the Ant version as a {@link DeweyDecimal} object.
     *
     * This is based on the implementation of AntVersion.getVersion()
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







