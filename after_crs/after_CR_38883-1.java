/*Fix parsing SDK Tools in ant tasks.

(cherry picked from commit ac7efd7e9f6f9a9fd76afaa774f26ad76602c25a)

Change-Id:I261269ac5aa931c767007ea6a6e7a75be4301ced*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/CheckEnvTask.java b/anttasks/src/com/android/ant/CheckEnvTask.java
//Synthetic comment -- index d6b6cc4..e8e1a89 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
Project antProject = getProject();

// check the Ant version
        DeweyDecimal version = getAntVersion(antProject);
DeweyDecimal atLeast = new DeweyDecimal(ANT_MIN_VERSION);
if (atLeast.isGreaterThan(version)) {
throw new BuildException(
//Synthetic comment -- @@ -67,8 +67,8 @@
}

// display SDK Tools revision
        DeweyDecimal toolsRevison = TaskHelper.getToolsRevision(sdkDir);
        if (toolsRevison != null) {
System.out.println("Android SDK Tools Revision " + toolsRevison);
System.out.println("Installed at " + sdkDir.getAbsolutePath());
}
//Synthetic comment -- @@ -83,7 +83,7 @@
* @param antProject the current ant project.
* @return the ant version.
*/
    private DeweyDecimal getAntVersion(Project antProject) {
char[] versionString = antProject.getProperty("ant.version").toCharArray();
StringBuilder sb = new StringBuilder();
boolean foundFirstDigit = false;








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/TaskHelper.java b/anttasks/src/com/android/ant/TaskHelper.java
//Synthetic comment -- index 3a372a1..35de033 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ant;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
//Synthetic comment -- @@ -25,6 +26,7 @@
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.DeweyDecimal;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -68,7 +70,8 @@
* @param sdkFile the {@link File} for the root folder of the SDK
* @return the tools revision or -1 if not found.
*/
    @Nullable
    static DeweyDecimal getToolsRevision(File sdkFile) {
Properties p = new Properties();
try{
// tools folder must exist, or this custom task wouldn't run!
//Synthetic comment -- @@ -90,7 +93,7 @@

String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (value != null) {
                return new DeweyDecimal(value);
}
} catch (FileNotFoundException e) {
// couldn't find the file? return -1 below.
//Synthetic comment -- @@ -98,7 +101,7 @@
// couldn't find the file? return -1 below.
}

        return null;
}

static String checkSinglePath(String attribute, Path path) {







