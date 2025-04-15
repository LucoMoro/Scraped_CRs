/*Add Dependency Checking to Ant Tasks

This change adds a BaseTask class that has built in mechanisms
for querying whether the task's dependencies have changed. It does
this by managing a DependencyGraph class which represents a set
of targets and their prerequisites and can test to see if an update
condition has occurred.

Change-Id:I5f3b90e234a45f16b5256e6d8a691a173352ddd5*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..32f6648 100644

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
//Synthetic comment -- @@ -253,7 +252,7 @@
public void execute() throws BuildException {
Project taskProject = getProject();

        // first do a full resource package
callAapt(null /*customPackage*/);

// if the parameters indicate generation of the R class, check if
//Synthetic comment -- @@ -289,6 +288,14 @@
final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();

if (generateRClass) {
} else if (mResourceFilter == null) {
System.out.println("Creating full resource package...");
} else {
//Synthetic comment -- @@ -427,6 +434,9 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
}

// final setup of the task








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/BaseTask.java b/anttasks/src/com/android/ant/BaseTask.java
new file mode 100644
//Synthetic comment -- index 0000000..6b69afd

//Synthetic comment -- @@ -0,0 +1,66 @@








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
new file mode 100644
//Synthetic comment -- index 0000000..1deb9da

//Synthetic comment -- @@ -0,0 +1,224 @@







