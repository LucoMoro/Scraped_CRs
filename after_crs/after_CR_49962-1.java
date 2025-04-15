/*Add LintRequest object

This allows us to pass additional information to lint jobs
over time without changing the LintDriver analyze signature.
It also lets detectors get to the original LintClient,
which is useful for IDE-specific lint checks.

Change-Id:I6985ab6daf0b1668b8e5b00db288be52a37ade20*/




//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Main.java b/lint/cli/src/main/java/com/android/tools/lint/Main.java
//Synthetic comment -- index c53469e..c585803 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.client.api.LintListener;
import com.android.tools.lint.client.api.LintRequest;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -613,7 +614,7 @@
mDriver.addLintListener(new ProgressPrinter());
}

        mDriver.analyze(new LintRequest(this, files));

Collections.sort(mWarnings);

//Synthetic comment -- @@ -680,7 +681,6 @@
return file;
}

/**
* Returns the File corresponding to the system property or the environment variable
* for {@link #PROP_WORK_DIR}.








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 264924b..bffe93e 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.client.api.LintRequest;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -281,7 +282,7 @@
public String analyze(List<File> files) throws Exception {
mDriver = new LintDriver(new CustomIssueRegistry(), this);
configureDriver(mDriver);
            mDriver.analyze(new LintRequest(this, files).setScope(getLintScope(files)));

Collections.sort(mWarnings);









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index c21e283..10040a1 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
private static final String SUPPRESS_LINT_VMSIG = '/' + SUPPRESS_LINT + ';';

private final LintClient mClient;
    private LintRequest mRequest;
private final IssueRegistry mRegistry;
private volatile boolean mCanceled;
private EnumSet<Scope> mScope;
//Synthetic comment -- @@ -163,7 +164,14 @@
}

/**
     * Returns the lint client requesting the lint check. This may not be the same
     * instance as the one passed in to this driver; lint uses a wrapper which performs
     * additional validation to ensure that for example badly behaved detectors which report
     * issues that have been disabled will get muted without the real lint client getting
     * notified. Thus, this {@link LintClient} is suitable for use by detectors to look
     * up a client to for example get location handles from, but tool handling code should
     * never try to cast this client back to their original lint client. For the original
     * lint client, use {@link LintRequest} instead.
*
* @return the client, never null
*/
//Synthetic comment -- @@ -173,6 +181,17 @@
}

/**
     * Returns the current request, which points to the original files to be checked,
     * the original scope, the original {@link LintClient}, as well as the release mode.
     *
     * @return the request
     */
    @NonNull
    public LintRequest getRequest() {
        return mRequest;
    }

    /**
* Records a property for later retrieval by {@link #getProperty(Object)}
*
* @param key the key to associate the value with
//Synthetic comment -- @@ -311,14 +330,40 @@
* @param files the files and directories to be analyzed
* @param scope the scope of the analysis; detectors with a wider scope will
*            not be run. If null, the scope will be inferred from the files.
     * @deprecated use {@link #analyze(LintRequest) instead}
*/
    @Deprecated
public void analyze(@NonNull List<File> files, @Nullable EnumSet<Scope> scope) {
        analyze(new LintRequest(mClient, files).setScope(scope));
    }

    /**
     * Analyze the given files (which can point to Android projects or directories
     * containing Android projects). Issues found are reported to the associated
     * {@link LintClient}.
     * <p>
     * Note that the {@link LintDriver} is not multi thread safe or re-entrant;
     * if you want to run potentially overlapping lint jobs, create a separate driver
     * for each job.
     *
     * @param request the files and directories to be analyzed
     */
    public void analyze(@NonNull LintRequest request) {
        try {
            mRequest = request;
            analyze();
        } finally {
            mRequest = null;
        }
    }

    /** Runs the driver to analyze the requested files */
    private void analyze() {
        mCanceled = false;
        mScope = mRequest.getScope();
Collection<Project> projects;
try {
            projects = computeProjects(mRequest.getFiles());
} catch (CircularDependencyException e) {
mCurrentProject = e.getProject();
if (mCurrentProject != null) {
//Synthetic comment -- @@ -330,7 +375,7 @@
return;
}
if (projects.isEmpty()) {
            mClient.log(null, "No projects found for %1$s", mRequest.getFiles().toString());
return;
}
if (mCanceled) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintRequest.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintRequest.java
new file mode 100644
//Synthetic comment -- index 0000000..4b09d88

//Synthetic comment -- @@ -0,0 +1,135 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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
import com.android.tools.lint.detector.api.Scope;
import com.google.common.annotations.Beta;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

/**
 * Information about a request to run lint
 * <p>
 * <b>NOTE: This is not a public or final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
@Beta
public class LintRequest {
    @NonNull
    private final LintClient mClient;

    @NonNull
    private final List<File> mFiles;

    @Nullable
    private EnumSet<Scope> mScope;

    @Nullable
    private Boolean mReleaseMode;

    /**
     * Creates a new {@linkplain LintRequest}, to be passed to a {@link LintDriver}
     *
     * @param client the tool wrapping the analyzer, such as an IDE or a CLI
     * @param files the set of files to check with lint. This can reference Android projects,
     *          or directories containing Android projects, or individual XML or Java files
     *          (typically for incremental IDE analysis).
     *
     * @return the set of files to check, should not be empty
     *
     */
    public LintRequest(@NonNull LintClient client, @NonNull List<File> files) {
        mClient = client;
        mFiles = files;
    }

    /**
     * Returns the lint client requesting the lint check
     *
     * @return the client, never null
     */
    @NonNull
    public LintClient getClient() {
        return mClient;
    }

    /**
     * Returns the set of files to check with lint. This can reference Android projects,
     * or directories containing Android projects, or individual XML or Java files
     * (typically for incremental IDE analysis).
     *
     * @return the set of files to check, should not be empty
     */
    @NonNull
    public List<File> getFiles() {
        return mFiles;
    }

    /**
     * Sets the scope to use; lint checks which require a wider scope set
     * will be ignored
     *
     * @return the scope to use, or null to use the default
     */
    @Nullable
    public EnumSet<Scope> getScope() {
        return mScope;
    }

    /**
     * Sets the scope to use; lint checks which require a wider scope set
     * will be ignored
     *
     * @param scope the scope
     * @return this, for constructor chaining
     */
    @NonNull
    public LintRequest setScope(@Nullable EnumSet<Scope> scope) {
        mScope = scope;
        return this;
    }

    /**
     * Returns {@code true} if lint is invoked as part of a release mode build,
     * {@code false}  if it is part of a debug mode build, and {@code null} if
     * the release mode is not known
     *
     * @return true if this lint is running in release mode, null if not known
     */
    @Nullable
    public Boolean isReleaseMode() {
        return mReleaseMode;
    }

    /**
     * Sets the release mode. Use {@code true} if lint is invoked as part of a
     * release mode build, {@code false} if it is part of a debug mode build,
     * and {@code null} if the release mode is not known
     *
     * @param releaseMode true if this lint is running in release mode, null if not known
     * @return this, for constructor chaining
     */
    @NonNull
    public LintRequest setReleaseMode(@Nullable Boolean releaseMode) {
        mReleaseMode = releaseMode;
        return this;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java
//Synthetic comment -- index a379dd0..42c97e7 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -75,15 +74,6 @@
/** The contents of the file */
private String mContents;

/** Map of properties to share results between detectors */
private Map<String, Object> mProperties;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 90ebfd8..72f0381 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -339,7 +340,7 @@

private static Set<Issue> getIssuesWithFixes() {
if (sAdtFixes == null) {
            sAdtFixes = Sets.newHashSetWithExpectedSize(25);
sAdtFixes.add(InefficientWeightDetector.INEFFICIENT_WEIGHT);
sAdtFixes.add(AccessibilityDetector.ISSUE);
sAdtFixes.add(InefficientWeightDetector.BASELINE_WEIGHTS);
//Synthetic comment -- @@ -359,6 +360,7 @@
sAdtFixes.add(TypographyDetector.QUOTES);
sAdtFixes.add(UseCompoundDrawableDetector.ISSUE);
sAdtFixes.add(ApiDetector.UNSUPPORTED);
            sAdtFixes.add(ApiDetector.INLINED);
sAdtFixes.add(TypoDetector.ISSUE);
sAdtFixes.add(ManifestOrderDetector.ALLOW_BACKUP);
sAdtFixes.add(MissingIdDetector.ISSUE);







