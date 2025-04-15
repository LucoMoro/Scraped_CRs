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
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -613,7 +614,7 @@
mDriver.addLintListener(new ProgressPrinter());
}

        mDriver.analyze(files, null /* scope */);

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
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -281,7 +282,7 @@
public String analyze(List<File> files) throws Exception {
mDriver = new LintDriver(new CustomIssueRegistry(), this);
configureDriver(mDriver);
            mDriver.analyze(files, getLintScope(files));

Collections.sort(mWarnings);









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index c21e283..10040a1 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
private static final String SUPPRESS_LINT_VMSIG = '/' + SUPPRESS_LINT + ';';

private final LintClient mClient;
private final IssueRegistry mRegistry;
private volatile boolean mCanceled;
private EnumSet<Scope> mScope;
//Synthetic comment -- @@ -163,7 +164,14 @@
}

/**
     * Returns the lint client requesting the lint check
*
* @return the client, never null
*/
//Synthetic comment -- @@ -173,6 +181,17 @@
}

/**
* Records a property for later retrieval by {@link #getProperty(Object)}
*
* @param key the key to associate the value with
//Synthetic comment -- @@ -311,14 +330,40 @@
* @param files the files and directories to be analyzed
* @param scope the scope of the analysis; detectors with a wider scope will
*            not be run. If null, the scope will be inferred from the files.
*/
public void analyze(@NonNull List<File> files, @Nullable EnumSet<Scope> scope) {
        mCanceled = false;
        mScope = scope;

Collection<Project> projects;
try {
            projects = computeProjects(files);
} catch (CircularDependencyException e) {
mCurrentProject = e.getProject();
if (mCurrentProject != null) {
//Synthetic comment -- @@ -330,7 +375,7 @@
return;
}
if (projects.isEmpty()) {
            mClient.log(null, "No projects found for %1$s", files.toString());
return;
}
if (mCanceled) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintRequest.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintRequest.java
new file mode 100644
//Synthetic comment -- index 0000000..4b09d88

//Synthetic comment -- @@ -0,0 +1,135 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java
//Synthetic comment -- index a379dd0..42c97e7 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -75,15 +74,6 @@
/** The contents of the file */
private String mContents;

    /**
     * Whether the lint job has been canceled.
     * <p>
     * Slow-running detectors should check this flag via
     * {@link AtomicBoolean#get()} and abort if canceled
     */
    @NonNull
    public final AtomicBoolean canceled = new AtomicBoolean();

/** Map of properties to share results between detectors */
private Map<String, Object> mProperties;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 90ebfd8..72f0381 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.google.common.annotations.Beta;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -339,7 +340,7 @@

private static Set<Issue> getIssuesWithFixes() {
if (sAdtFixes == null) {
            sAdtFixes = new HashSet<Issue>(25);
sAdtFixes.add(InefficientWeightDetector.INEFFICIENT_WEIGHT);
sAdtFixes.add(AccessibilityDetector.ISSUE);
sAdtFixes.add(InefficientWeightDetector.BASELINE_WEIGHTS);
//Synthetic comment -- @@ -359,6 +360,7 @@
sAdtFixes.add(TypographyDetector.QUOTES);
sAdtFixes.add(UseCompoundDrawableDetector.ISSUE);
sAdtFixes.add(ApiDetector.UNSUPPORTED);
sAdtFixes.add(TypoDetector.ISSUE);
sAdtFixes.add(ManifestOrderDetector.ALLOW_BACKUP);
sAdtFixes.add(MissingIdDetector.ISSUE);







