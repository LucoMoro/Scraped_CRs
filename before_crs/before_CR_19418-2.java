/*Add Ant version check in setup task.

Change-Id:Id2237ae2fd64a1ccae5b1a1957099c218cdaf9a5*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 4f14d62..4d1bac6 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import org.apache.tools.ant.taskdefs.ImportTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.xml.sax.InputSource;

import java.io.File;
//Synthetic comment -- @@ -64,6 +65,7 @@
*
*/
public final class SetupTask extends ImportTask {
// main rules file
private final static String RULES_MAIN = "main_rules.xml";
// test rules file - depends on android_rules.xml
//Synthetic comment -- @@ -77,6 +79,17 @@
public void execute() throws BuildException {
Project antProject = getProject();

// get the SDK location
File sdkDir = TaskHelper.getSdkLocation(antProject);
String sdkOsPath = sdkDir.getPath();
//Synthetic comment -- @@ -548,4 +561,33 @@

return libraries;
}
}







