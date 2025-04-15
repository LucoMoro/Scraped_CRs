/*Prevent circular dependencies in library project dependencies

Change-Id:Ia43c2436e01032ace7e2b13e59d60d10f71e7004*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index f57fb9c..4f427a2 100644

//Synthetic comment -- @@ -215,6 +215,10 @@
return true;
}

return false;
}

//Synthetic comment -- @@ -230,10 +234,14 @@
return null;
}

public class TestLintClient extends Main {
private StringWriter mWriter = new StringWriter();

        TestLintClient() {
mReporters.add(new TextReporter(this, mWriter, false));
}

//Synthetic comment -- @@ -279,9 +287,14 @@
}

@Override
        public void report(Context context, Issue issue, Severity severity, Location location,
                String message, Object data) {
            if (issue == IssueRegistry.LINT_ERROR) {
return;
}









//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/client/api/ProjectTest.java b/lint/cli/src/test/java/com/android/tools/lint/client/api/ProjectTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4f3c9ef

//Synthetic comment -- @@ -0,0 +1,60 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/CircularDependencyException.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/CircularDependencyException.java
new file mode 100644
//Synthetic comment -- index 0000000..337eb27

//Synthetic comment -- @@ -0,0 +1,81 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 7843aa1..9afef42 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.utils.StdLogger.Level;
import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import org.w3c.dom.Document;
//Synthetic comment -- @@ -54,6 +55,7 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -573,6 +575,8 @@
return project;
}

/**
* Create a project for the given directory
* @param dir the root directory of the project
//Synthetic comment -- @@ -581,6 +585,11 @@
*/
@NonNull
protected Project createProject(@NonNull File dir, @NonNull File referenceDir) {
return Project.create(this, dir, referenceDir);
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index a92a9a2..0ff3a9a 100644

//Synthetic comment -- @@ -283,7 +283,16 @@
mCanceled = false;
mScope = scope;

        Collection<Project> projects = computeProjects(files);
if (projects.isEmpty()) {
mClient.log(null, "No projects found for %1$s", files.toString());
return;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index c31a499..df27b2f 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.SdkInfo;
//Synthetic comment -- @@ -104,7 +105,7 @@
@NonNull
public static Project create(
@NonNull LintClient client,
            @NonNull  File dir,
@NonNull File referenceDir) {
return new Project(client, dir, referenceDir);
}
//Synthetic comment -- @@ -182,12 +183,18 @@
}
}

                        Project libraryPrj = client.getProject(libraryDir, libraryReferenceDir);
                        mDirectLibraries.add(libraryPrj);
                        // By default, we don't report issues in inferred library projects.
                        // The driver will set report = true for those library explicitly
                        // requested.
                        libraryPrj.setReportIssues(false);
}
} finally {
Closeables.closeQuietly(is);







