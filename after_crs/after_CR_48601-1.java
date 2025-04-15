/*Prevent circular dependencies in library project dependencies

Change-Id:Iad8a9e6ad903f3e3a9806080114d97b4984eeba4*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 522e086..c80d3f8 100644

//Synthetic comment -- @@ -238,6 +238,10 @@
return true;
}

        if (issue == IssueRegistry.LINT_ERROR || issue == IssueRegistry.PARSER_ERROR) {
            return !ignoreSystemErrors();
        }

return false;
}

//Synthetic comment -- @@ -253,10 +257,14 @@
return null;
}

    protected boolean ignoreSystemErrors() {
        return true;
    }

public class TestLintClient extends Main {
private StringWriter mWriter = new StringWriter();

        public TestLintClient() {
mReporters.add(new TextReporter(this, mWriter, false));
}

//Synthetic comment -- @@ -309,7 +317,7 @@
@Nullable Location location,
@NonNull String message,
@Nullable Object data) {
            if (ignoreSystemErrors() && (issue == IssueRegistry.LINT_ERROR)) {
return;
}









//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/client/api/ProjectTest.java b/lint/cli/src/test/java/com/android/tools/lint/client/api/ProjectTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4f3c9ef

//Synthetic comment -- @@ -0,0 +1,60 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.lint.client.api;

import com.android.tools.lint.checks.AbstractCheckTest;
import com.android.tools.lint.checks.UnusedResourceDetector;
import com.android.tools.lint.detector.api.Detector;

import java.io.File;
import java.util.Arrays;

public class ProjectTest extends AbstractCheckTest {
    @Override
    protected boolean ignoreSystemErrors() {
        return false;
    }

    public void testCycle() throws Exception {
        // Ensure that a cycle in library project dependencies doesn't cause
        // infinite directory traversal
        File master = getProjectDir("MasterProject",
                // Master project
                "multiproject/main-manifest.xml=>AndroidManifest.xml",
                "multiproject/main.properties=>project.properties",
                "multiproject/MainCode.java.txt=>src/foo/main/MainCode.java"
        );
        File library = getProjectDir("LibraryProject",
                // Library project
                "multiproject/library-manifest.xml=>AndroidManifest.xml",
                "multiproject/main.properties=>project.properties", // RECURSIVE - points to self
                "multiproject/LibraryCode.java.txt=>src/foo/library/LibraryCode.java",
                "multiproject/strings.xml=>res/values/strings.xml"
        );

        assertEquals(""
                + "MasterProject/project.properties: Error: Circular library dependencies; check your project.properties files carefully [LintError]\n"
                + "1 errors, 0 warnings\n",

                checkLint(Arrays.asList(master, library)));
    }

    @Override
    protected Detector getDetector() {
        return new UnusedResourceDetector();
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/CircularDependencyException.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/CircularDependencyException.java
new file mode 100644
//Synthetic comment -- index 0000000..337eb27

//Synthetic comment -- @@ -0,0 +1,81 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.google.common.annotations.Beta;

/**
 * Exception thrown when there is a circular dependency, such as a circular dependency
 * of library mProject references
 * <p>
 * <b>NOTE: This is not a public or final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
@Beta
public class CircularDependencyException extends RuntimeException {
    @Nullable
    private Project mProject;

    @Nullable
    private Location mLocation;

    CircularDependencyException(@NonNull String message) {
        super(message);
    }

    /**
     * Returns the associated project, if any
     *
     * @return the associated project, if any
     */
    @Nullable
    public Project getProject() {
        return mProject;
    }

    /**
     * Sets the associated project, if any
     *
     * @param project the associated project, if any
     */
    public void setProject(@Nullable Project project) {
        mProject = project;
    }

    /**
     * Returns the associated location, if any
     *
     * @return the associated location, if any
     */
    @Nullable
    public Location getLocation() {
        return mLocation;
    }

    /**
     * Sets the associated location, if any
     *
     * @param location the associated location, if any
     */
    public void setLocation(@Nullable Location location) {
        mLocation = location;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 7843aa1..9afef42 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.utils.StdLogger.Level;
import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.w3c.dom.Document;
//Synthetic comment -- @@ -54,6 +55,7 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -573,6 +575,8 @@
return project;
}

    private Set<File> mProjectDirs = Sets.newHashSet();

/**
* Create a project for the given directory
* @param dir the root directory of the project
//Synthetic comment -- @@ -581,6 +585,11 @@
*/
@NonNull
protected Project createProject(@NonNull File dir, @NonNull File referenceDir) {
        if (mProjectDirs.contains(dir)) {
            throw new CircularDependencyException(
                "Circular library dependencies; check your project.properties files carefully");
        }
        mProjectDirs.add(dir);
return Project.create(this, dir, referenceDir);
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index a92a9a2..0ff3a9a 100644

//Synthetic comment -- @@ -283,7 +283,16 @@
mCanceled = false;
mScope = scope;

        Collection<Project> projects;
        try {
            projects = computeProjects(files);
        } catch (CircularDependencyException e) {
            Context context = new Context(this, e.getProject(), null, e.getLocation().getFile());
            mCurrentProject = e.getProject();
            context.report(IssueRegistry.LINT_ERROR, e.getLocation(), e.getMessage(), null);
            mCurrentProject = null;
            return;
        }
if (projects.isEmpty()) {
mClient.log(null, "No projects found for %1$s", files.toString());
return;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index c31a499..df27b2f 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.CircularDependencyException;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.SdkInfo;
//Synthetic comment -- @@ -104,7 +105,7 @@
@NonNull
public static Project create(
@NonNull LintClient client,
            @NonNull File dir,
@NonNull File referenceDir) {
return new Project(client, dir, referenceDir);
}
//Synthetic comment -- @@ -182,12 +183,18 @@
}
}

                        try {
                            Project libraryPrj = client.getProject(libraryDir, libraryReferenceDir);
                            mDirectLibraries.add(libraryPrj);
                            // By default, we don't report issues in inferred library projects.
                            // The driver will set report = true for those library explicitly
                            // requested.
                            libraryPrj.setReportIssues(false);
                        } catch (CircularDependencyException e) {
                            e.setProject(this);
                            e.setLocation(Location.create(propFile));
                            throw e;
                        }
}
} finally {
Closeables.closeQuietly(is);







