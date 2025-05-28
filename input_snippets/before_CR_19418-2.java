
//<Beginning of snippet n. 0>


import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.xml.sax.InputSource;

import java.io.File;
*
*/
public final class SetupTask extends ImportTask {
// main rules file
private final static String RULES_MAIN = "main_rules.xml";
// test rules file - depends on android_rules.xml
public void execute() throws BuildException {
Project antProject = getProject();

// get the SDK location
File sdkDir = TaskHelper.getSdkLocation(antProject);
String sdkOsPath = sdkDir.getPath();

return libraries;
}
}

//<End of snippet n. 0>








