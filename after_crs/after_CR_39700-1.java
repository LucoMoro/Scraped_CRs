/*Improve handling of library projects in lint

This changeset improves the way lint handles library projects.

Until now, running lint on project "master" would also look up any
library projects referenced by the master project, and analyze the
library projects as well. This is necessary in order to correctly
compute unused resources for example, since it's possible for a
resource to be defined in one project and referenced in another.

However, while this behavior is desirable for users who partition
their code up into library projects, it has some serious problems for
users who are using a third party library project:

- Their lint output can be swamped with errors from the library which
  they have no control over.

- If for example the library provides translations into 60 languages,
  lint will use these 60 languages as the set of languages targeted by
  the application, and complain about all strings in the master
  project which are not translated into all the languages.

This changeset makes a key change to how library projects are
handled:

   (1) If you run lint on all projects (including the library projects),
       then there is no change from before.

   (2) If you run lint and specify just a project, then lint will
       continue to analyze the project as well as all its libraries,
       but will only report problems on the user-specified project.

The way this is done is by a new "report errors" attribute stored with
each project. All projects that are explicitly referenced on the
command line (or selected in the Eclipse UI), and all projects that
are found recursively if you specify a top level directory, all these
projects have their "report errors" flag set. Any remaining projects,
which would be those found through library project references, these
have their report errors flag cleared. And whenever lint is processing
errors, it will filter out errors for projects that are not reporting
errors.

This addresses issuehttp://code.google.com/p/android/issues/detail?id=33847as well as a number of other requests (in StackOverflow and elsewhere)
around the ability to filter errors in library projects.

Change-Id:I9d084f598c678ecf79cfe70d8ea7a84844333acc*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 08a5c19..69a44f3 100644

//Synthetic comment -- @@ -126,6 +126,7 @@
private List<Detector> mRepeatingDetectors;
private EnumSet<Scope> mRepeatScope;
private Project[] mCurrentProjects;
    private Project mCurrentProject;
private boolean mAbbreviating = true;

/**
//Synthetic comment -- @@ -228,6 +229,19 @@
}

/**
     * Returns the projects being analyzed
     *
     * @return the projects being analyzed
     */
    @NonNull
    public List<Project> getProjects() {
        if (mCurrentProjects != null) {
            return Arrays.asList(mCurrentProjects);
        }
        return Collections.emptyList();
    }

    /**
* Analyze the given file (which can point to an Android project). Issues found
* are reported to the associated {@link LintClient}.
*
//Synthetic comment -- @@ -604,6 +618,15 @@
roots.removeAll(project.getAllLibraries());
}

        // Report issues for all projects that are explicitly referenced. We need to
        // do this here, since the project initialization will mark all library
        // projects as no-report projects by default.
        for (Project project : allProjects) {
            // Report issues for all projects explicitly listed or found via a directory
            // traversal -- including library projects.
            project.setReportIssues(true);
        }

if (LintUtils.assertionsEnabled()) {
// Make sure that all the project directories are unique. This ensures
// that we didn't accidentally end up with different project instances
//Synthetic comment -- @@ -661,6 +684,8 @@
allProjects.addAll(allLibraries);
mCurrentProjects = allProjects.toArray(new Project[allProjects.size()]);

        mCurrentProject = project;

for (Detector check : mApplicableDetectors) {
check.beforeCheckProject(projectContext);
if (mCanceled) {
//Synthetic comment -- @@ -668,6 +693,7 @@
}
}

        assert mCurrentProject == project;
runFileDetectors(project, project);

if (!Scope.checkSingleFile(mScope)) {
//Synthetic comment -- @@ -675,6 +701,7 @@
for (Project library : libraries) {
Context libraryContext = new Context(this, library, project, projectDir);
fireEvent(EventType.SCANNING_LIBRARY_PROJECT, libraryContext);
                mCurrentProject = library;

for (Detector check : mApplicableDetectors) {
check.beforeCheckLibraryProject(libraryContext);
//Synthetic comment -- @@ -682,12 +709,15 @@
return;
}
}
                assert mCurrentProject == library;

runFileDetectors(library, project);
if (mCanceled) {
return;
}

                assert mCurrentProject == library;

for (Detector check : mApplicableDetectors) {
check.afterCheckLibraryProject(libraryContext);
if (mCanceled) {
//Synthetic comment -- @@ -697,6 +727,8 @@
}
}

        mCurrentProject = project;

for (Detector check : mApplicableDetectors) {
check.afterCheckProject(projectContext);
if (mCanceled) {
//Synthetic comment -- @@ -1487,6 +1519,11 @@
@Nullable Location location,
@NonNull String message,
@Nullable Object data) {
            assert mCurrentProject != null;
            if (!mCurrentProject.getReportIssues()) {
                return;
            }

Configuration configuration = context.getConfiguration();
if (!configuration.isEnabled(issue)) {
if (issue != IssueRegistry.PARSER_ERROR && issue != IssueRegistry.LINT_ERROR) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index c05a1cd..7629034 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
private List<File> mJavaLibraries;
private List<Project> mDirectLibraries;
private List<Project> mAllLibraries;
    private boolean mReportIssues = true;

/**
* Creates a new {@link Project} for the given directory.
//Synthetic comment -- @@ -159,7 +160,12 @@
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
//Synthetic comment -- @@ -573,6 +579,34 @@
return mName;
}

    /**
     * Sets whether lint should report issues in this project. See
     * {@link #getReportIssues()} for a full description of what that means.
     *
     * @param reportIssues whether lint should report issues in this project
     */
    public void setReportIssues(boolean reportIssues) {
        mReportIssues = reportIssues;
    }

    /**
     * Returns whether lint should report issues in this project.
     * <p>
     * If a user specifies a project and its library projects for analysis, then
     * those library projects are all "included", and all errors found in all
     * the projects are reported. But if the user is only running lint on the
     * main project, we shouldn't report errors in any of the library projects.
     * We still need to <b>consider</b> them for certain types of checks, such
     * as determining whether resources found in the main project are unused, so
     * the detectors must still get a chance to look at these projects. The
     * {@code #getReportIssues()} attribute is used for this purpose.
     *
     * @return whether lint should report issues in this project
     */
    public boolean getReportIssues() {
        return mReportIssues;
    }

// ---------------------------------------------------------------------------
// Support for running lint on the AOSP source tree itself









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index a60e206..f89fb81 100644

//Synthetic comment -- @@ -143,13 +143,19 @@

// Convention seen in various projects
mIgnoreFile = context.file.getName().startsWith("donottranslate"); //$NON-NLS-1$

        if (!context.getProject().getReportIssues()) {
            mIgnoreFile = true;
        }
}

@Override
public void afterCheckFile(@NonNull Context context) {
if (context.getPhase() == 1) {
// Store this layout's set of ids for full project analysis in afterCheckProject
            if (context.getProject().getReportIssues()) {
                mFileToNames.put(context.file, mNames);
            }

mNames = null;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index a593f1f..bcbdd85 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -99,6 +100,7 @@
Severity.WARNING,
UnusedResourceDetector.class,
EnumSet.of(Scope.MANIFEST, Scope.ALL_RESOURCE_FILES, Scope.ALL_JAVA_FILES));

/** Unused id's */
public static final Issue ISSUE_IDS = Issue.create("UnusedIds", //$NON-NLS-1$
"Looks for unused id's",
//Synthetic comment -- @@ -175,14 +177,18 @@
if (xmlContext.getDriver().isSuppressed(ISSUE, root)) {
//  Also remove it from consideration such that even the
// presence of this field in the R file is ignored.
                                    mUnused.remove(resource);
return;
}
}
}

                        if (!context.getProject().getReportIssues()) {
                            // If this is a library project not being analyzed, ignore it
                            mUnused.remove(resource);
                            return;
                        }

recordLocation(resource, Location.create(file));
}
}
//Synthetic comment -- @@ -289,12 +295,34 @@
List<String> sorted = new ArrayList<String>(mUnused.keySet());
Collections.sort(sorted);

                Boolean skippedLibraries = null;

for (String resource : sorted) {
Location location = mUnused.get(resource);
if (location != null) {
// We were prepending locations, but we want to prefer the base folders
location = Location.reverse(location);
}

                    if (location == null) {
                        if (skippedLibraries == null) {
                            skippedLibraries = false;
                            for (Project project : context.getDriver().getProjects()) {
                                if (!project.getReportIssues()) {
                                    skippedLibraries = true;
                                    break;
                                }
                            }
                        }
                        if (skippedLibraries) {
                            // Skip this resource if we don't have a location, and one or
                            // more library projects were skipped; the resource was very
                            // probably defined in that library project and only encountered
                            // in the main project's java R file
                            continue;
                        }
                    }

String message = String.format("The resource %1$s appears to be unused",
resource);
Issue issue = getIssue(resource);
//Synthetic comment -- @@ -364,6 +392,10 @@
mUnused.remove(resource);
return;
}
                            if (!context.getProject().getReportIssues()) {
                                mUnused.remove(resource);
                                return;
                            }
recordLocation(resource, context.getLocation(nameAttribute));
}
}
//Synthetic comment -- @@ -422,6 +454,10 @@
mUnused.remove(resource);
return;
}
                if (!context.getProject().getReportIssues()) {
                    mUnused.remove(resource);
                    return;
                }
recordLocation(resource, context.getLocation(attribute));
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 9c0904f..d05a203 100644

//Synthetic comment -- @@ -77,13 +77,14 @@

protected String lintFiles(String... relativePaths) throws Exception {
List<File> files = new ArrayList<File>();
        File targetDir = getTargetDir();
for (String relativePath : relativePaths) {
            File file = getTestfile(targetDir, relativePath);
assertNotNull(file);
files.add(file);
}

        addManifestFile(targetDir);

return checkLint(files);
}
//Synthetic comment -- @@ -107,7 +108,16 @@
mOutput.append("No warnings.");
}

        String result = mOutput.toString();

        // The output typically contains a few directory/filenames.
        // On Windows we need to change the separators to the unix-style
        // forward slash to make the test as OS-agnostic as possible.
        if (File.separatorChar != '/') {
            result = result.replace(File.separatorChar, '/');
        }

        return result;
}

/**
//Synthetic comment -- @@ -116,28 +126,30 @@
*   separators to the unix-style forward slash.
*/
protected String lintProject(String... relativePaths) throws Exception {
        File projectDir = getProjectDir(null, relativePaths);
        return checkLint(Collections.singletonList(projectDir));
    }

    /** Creates a project directory structure from the given files */
    protected File getProjectDir(String name, String ...relativePaths) throws Exception {
assertFalse("getTargetDir must be overridden to make a unique directory",
getTargetDir().equals(getTempDir()));

File projectDir = getTargetDir();
        if (name != null) {
            projectDir = new File(projectDir, name);
        }
        assertTrue(projectDir.getPath(), projectDir.mkdirs());

List<File> files = new ArrayList<File>();
for (String relativePath : relativePaths) {
            File file = getTestfile(projectDir, relativePath);
assertNotNull(file);
files.add(file);
}

addManifestFile(projectDir);
        return projectDir;
}

private void addManifestFile(File projectDir) throws IOException {
//Synthetic comment -- @@ -185,7 +197,11 @@

private File makeTestFile(String name, String relative,
final InputStream contents) throws IOException {
        return makeTestFile(getTargetDir(), name, relative, contents);
    }

    private File makeTestFile(File dir, String name, String relative,
            final InputStream contents) throws IOException {
if (relative != null) {
dir = new File(dir, relative);
if (!dir.exists()) {
//Synthetic comment -- @@ -210,7 +226,7 @@
return tempFile;
}

    private File getTestfile(File targetDir, String relativePath) throws IOException {
// Support replacing filenames and paths with a => syntax, e.g.
//   dir/file.txt=>dir2/dir3/file2.java
// will read dir/file.txt from the test data and write it into the target
//Synthetic comment -- @@ -236,7 +252,7 @@
relative = targetPath.substring(0, index);
}

        return makeTestFile(targetDir, name, relative, stream);
}

protected boolean isEnabled(Issue issue) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index 6c08149..d33088f 100644

//Synthetic comment -- @@ -121,5 +121,4 @@
"res/drawable/states.xml=>res/drawable-hdpi/f.xml",
"res/drawable/states.xml=>res/drawable-xhdpi/f.xml"));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/RegistrationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/RegistrationDetectorTest.java
//Synthetic comment -- index 75d784f..2369fad 100644

//Synthetic comment -- @@ -85,4 +85,25 @@
"bytecode/TestReceiver$1.class.data=>bin/classes/test/pkg/TestReceiver$1.class"
));
}

    public void testLibraryProjects() throws Exception {
        // If a library project provides additional activities, it is not an error to
        // not register all of those here
        assertEquals(
            "No warnings.",

            lintProject(
                // Master project
                "multiproject/main-manifest.xml=>AndroidManifest.xml",
                "multiproject/main.properties=>project.properties",

                // Library project
                "multiproject/library-manifest.xml=>../LibraryProject/AndroidManifest.xml",
                "multiproject/library.properties=>../LibraryProject/project.properties",

                "bytecode/.classpath=>../LibraryProject/.classpath",
                "bytecode/OnClickActivity.java.txt=>../LibraryProject/src/test/pkg/OnClickActivity.java",
                "bytecode/OnClickActivity.class.data=>../LibraryProject/bin/classes/test/pkg/OnClickActivity.class"
                ));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/TranslationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/TranslationDetectorTest.java
//Synthetic comment -- index 6201bdb..6f1c2e6 100644

//Synthetic comment -- @@ -114,4 +114,27 @@
"res/values/strings3.xml=>res/values/strings.xml",
"res/values-fr/strings.xml=>res/values-fr/strings.xml"));
}

    public void testLibraryProjects() throws Exception {
        // If a library project provides additional locales, that should not force
        // the main project to include all those translations
        assertEquals(
            "No warnings.",

             lintProject(
                 // Master project
                 "multiproject/main-manifest.xml=>AndroidManifest.xml",
                 "multiproject/main.properties=>project.properties",
                 "res/values/strings2.xml",

                 // Library project
                 "multiproject/library-manifest.xml=>../LibraryProject/AndroidManifest.xml",
                 "multiproject/library.properties=>../LibraryProject/project.properties",

                 "res/values/strings.xml=>../LibraryProject/res/values/strings.xml",
                 "res/values-cs/strings.xml=>../LibraryProject/res/values-cs/strings.xml",
                 "res/values-cs/strings.xml=>../LibraryProject/res/values-de/strings.xml",
                 "res/values-cs/strings.xml=>../LibraryProject/res/values-nl/strings.xml"
             ));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/UnusedResourceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/UnusedResourceDetectorTest.java
//Synthetic comment -- index d2af622..bf32e2b 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("javadoc")
public class UnusedResourceDetectorTest extends AbstractCheckTest {
private boolean mEnableIds = false;
//Synthetic comment -- @@ -101,12 +104,9 @@
"AndroidManifest.xml"));
}

    public void testMultiProjectIgnoreLibraries() throws Exception {
assertEquals(
           "No warnings.",

lintProject(
// Master project
//Synthetic comment -- @@ -122,6 +122,29 @@
));
}

    public void testMultiProject() throws Exception {
        File master = getProjectDir("MasterProject",
                // Master project
                "multiproject/main-manifest.xml=>AndroidManifest.xml",
                "multiproject/main.properties=>project.properties",
                "multiproject/MainCode.java.txt=>src/foo/main/MainCode.java"
        );
        File library = getProjectDir("LibraryProject",
                // Library project
                "multiproject/library-manifest.xml=>AndroidManifest.xml",
                "multiproject/library.properties=>project.properties",
                "multiproject/LibraryCode.java.txt=>src/foo/library/LibraryCode.java",
                "multiproject/strings.xml=>res/values/strings.xml"
        );
        assertEquals(
           // string1 is defined and used in the library project
           // string2 is defined in the library project and used in the master project
           // string3 is defined in the library project and not used anywhere
           "strings.xml:7: Warning: The resource R.string.string3 appears to be unused",

           checkLint(Arrays.asList(master, library)));
    }

public void testFqcnReference() throws Exception {
assertEquals(
"No warnings.",
//Synthetic comment -- @@ -151,6 +174,4 @@
"res/values/plurals.xml",
"AndroidManifest.xml"));
}
}







