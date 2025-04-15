/*Add support to Ant for dependency checking

This change adds dependency checking to the Ant generation of
R.java. If no resources have been changed or added since the
last generation of R.java then that call to aapt will be skipped.

Change-Id:I262b94111316496fac54da8030a6d474dc30b584*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index 4110e8d..d0b96d3 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Path;

//Synthetic comment -- @@ -51,7 +50,7 @@
* <tr><td></td><td></td><td></td></tr>
* </table>
*/
public final class AaptExecLoopTask extends Task {

/**
* Class representing a &lt;nocompress&gt; node in the main task XML.
//Synthetic comment -- @@ -259,8 +258,10 @@
// more R classes need to be created for libraries.
if (mRFolder != null && new File(mRFolder).isDirectory()) {
libPkgProp = taskProject.getProperty(AntConstants.PROP_PROJECT_LIBS_PKG);
            // Replace ";" with ":" since that's what aapt expects
            libPkgProp = libPkgProp.replace(';', ':');
}
// Call aapt. If there are libraries, we'll pass a non-null string of libs.
callAapt(libPkgProp);
//Synthetic comment -- @@ -278,6 +279,18 @@
final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();

if (generateRClass) {
} else if (mResourceFilter == null) {
System.out.println("Creating full resource package...");
} else {
//Synthetic comment -- @@ -416,6 +429,8 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
}

// final setup of the task








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/BaseTask.java b/anttasks/src/com/android/ant/BaseTask.java
new file mode 100644
//Synthetic comment -- index 0000000..2126d3f

//Synthetic comment -- @@ -0,0 +1,70 @@








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
new file mode 100644
//Synthetic comment -- index 0000000..461197d

//Synthetic comment -- @@ -0,0 +1,253 @@







