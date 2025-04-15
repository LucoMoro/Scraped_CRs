/*Separate detector metadata from issue

This changeset makes a change to how lint maps issues to detectors.

Until now, each issue would name a detector class, as well as the
issue scope required by that detector, to analyze this issue (as well
as incremental analysis scopes). If an IDE wants to replace certain
detectors (for example, to more directly use extra data available to
make the rule more widely applicable or accurate, such as type
resolution) this meant replacing the issue instance itself, which
doesn't work well when other code references an issue by
instance. It's also hacky. Now, with these concerns separate, a tool
can replace the implementation instance of an issue while continuing
to report the same issue.

Since I was making an incompatible change to the Issue class, I took
the opportunity to make several other changes I've been meaning to get
to:

- An issue can now specify more than a single "More Info" URL

- There's a new "Brief" description, typically 4-8 words, which
  captures the essence of the bug. This is used in headers such as the
  Preferences dialog etc, in the HTML report summary instead of ids,
  etc.

- There's a new OutputFormat enum. Until now, the explanation
  attribute of an issue had some markup (* for bold, ` for code, etc),
  and there were multiple methods to get the explanation:

   * getExplanation() -- returned the raw markup text
   * getExplanationAsSimpleText() -- generates plain text version
   * getExplanationAsHtml() -- generates HTML markup (using <b> for
      bold etc)

  This was specific to the explanation, and in particular the
  description could not contain markup for code identifiers.

  Instead, there's now a single getExplanation(OutputFormat) method,
  and similarly getDescription now takes an OutputFormat, as does the
  new getBriefDescription() method. This also allowed many issues to
  start using code markers and bold markers in the issue summary.  The
  HTML report for example takes advantage of this.

Change-Id:I3571b8e104695db3e3d994077bd0bcb492d474d2*/
//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/HtmlReporter.java b/lint/cli/src/main/java/com/android/tools/lint/HtmlReporter.java
//Synthetic comment -- index 83cd76d..6e44af6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.SdkConstants.DOT_JPG;
import static com.android.SdkConstants.DOT_PNG;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.tools.lint.checks.BuiltinIssueRegistry;
//Synthetic comment -- @@ -169,6 +170,8 @@
// Explain this issue
mWriter.write("<div class=\"id\"><a href=\"#\" title=\"Return to top\">");                     //$NON-NLS-1$
mWriter.write(issue.getId());
mWriter.write("</a><div class=\"issueSeparator\"></div>\n"); //$NON-NLS-1$
mWriter.write("</div>\n");                               //$NON-NLS-1$

//Synthetic comment -- @@ -360,7 +363,7 @@

mWriter.write("<div class=\"summary\">\n");              //$NON-NLS-1$
mWriter.write("Explanation: ");
        String description = issue.getDescription();
mWriter.write(description);
if (!description.isEmpty()
&& Character.isLetter(description.charAt(description.length() - 1))) {
//Synthetic comment -- @@ -368,18 +371,31 @@
}
mWriter.write("</div>\n");                               //$NON-NLS-1$
mWriter.write("<div class=\"explanation\">\n");          //$NON-NLS-1$
        String explanationHtml = issue.getExplanationAsHtml();
mWriter.write(explanationHtml);
mWriter.write("\n</div>\n");                             //$NON-NLS-1$;
        if (issue.getMoreInfo() != null) {
            mWriter.write("<br/>");                                  //$NON-NLS-1$
mWriter.write("<div class=\"moreinfo\">");           //$NON-NLS-1$
mWriter.write("More info: ");
            mWriter.write("<a href=\"");                         //$NON-NLS-1$
            mWriter.write(issue.getMoreInfo());
            mWriter.write("\">");                                //$NON-NLS-1$
            mWriter.write(issue.getMoreInfo());
            mWriter.write("</a></div>\n");                       //$NON-NLS-1$
}

mWriter.write("<br/>");                                  //$NON-NLS-1$
//Synthetic comment -- @@ -560,6 +576,8 @@
mWriter.write(issue.getId());
mWriter.write("\">");                                    //$NON-NLS-1$
mWriter.write(issue.getId());
mWriter.write("</a>\n");                                 //$NON-NLS-1$

mWriter.write("</td></tr>\n");








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/LombokParser.java b/lint/cli/src/main/java/com/android/tools/lint/LombokParser.java
//Synthetic comment -- index bfa9d02..f699b59 100644

//Synthetic comment -- @@ -148,7 +148,6 @@
return Location.create(mFile, null /*contents*/, pos.getStart(), pos.getEnd());
}


@Override
public void setClientData(@Nullable Object clientData) {
mClientData = clientData;








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Main.java b/lint/cli/src/main/java/com/android/tools/lint/Main.java
//Synthetic comment -- index c585803..a1a97a1 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.SdkConstants.DOT_XML;
import static com.android.tools.lint.client.api.IssueRegistry.LINT_ERROR;
import static com.android.tools.lint.client.api.IssueRegistry.PARSER_ERROR;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -820,7 +821,7 @@
}

private static void listIssue(PrintStream out, Issue issue) {
        out.print(wrapArg("\"" + issue.getId() + "\": " + issue.getDescription()));
}

private static void showIssues(IssueRegistry registry) {
//Synthetic comment -- @@ -867,7 +868,7 @@
System.out.print('-');
}
System.out.println();
        System.out.println(wrap("Summary: " + issue.getDescription()));
System.out.println("Priority: " + issue.getPriority() + " / 10");
System.out.println("Severity: " + issue.getDefaultSeverity().getDescription());
System.out.println("Category: " + issue.getCategory().getFullName());
//Synthetic comment -- @@ -878,12 +879,14 @@
issue.getId()));
}

        if (issue.getExplanation() != null) {
            System.out.println();
            System.out.println(wrap(issue.getExplanationAsSimpleText()));
        }
        if (issue.getMoreInfo() != null) {
            System.out.println("More information: " + issue.getMoreInfo());
}
}









//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/XmlReporter.java b/lint/cli/src/main/java/com/android/tools/lint/XmlReporter.java
//Synthetic comment -- index c0dca75..18b27b2 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.tools.lint;

import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Position;
import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.BufferedWriter;
//Synthetic comment -- @@ -55,7 +58,8 @@
@Override
public void write(int errorCount, int warningCount, List<Warning> issues) throws IOException {
mWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");      //$NON-NLS-1$
        mWriter.write("<issues format=\"3\"");                              //$NON-NLS-1$
String revision = mClient.getRevision();
if (revision != null) {
mWriter.write(String.format(" by=\"lint %1$s\"", revision));    //$NON-NLS-1$
//Synthetic comment -- @@ -77,10 +81,17 @@
issue.getCategory().getFullName());
writeAttribute(mWriter, 2, "priority",                                //$NON-NLS-1$
Integer.toString(issue.getPriority()));
                writeAttribute(mWriter, 2, "summary", issue.getDescription());        //$NON-NLS-1$
                writeAttribute(mWriter, 2, "explanation", issue.getExplanation());    //$NON-NLS-1$
                if (issue.getMoreInfo() != null) {
                    writeAttribute(mWriter, 2, "url", issue.getMoreInfo());           //$NON-NLS-1$
}
if (warning.errorLine != null && !warning.errorLine.isEmpty()) {
String line = warning.errorLine;








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/MainTest.java b/lint/cli/src/test/java/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 68a1ea3..5c5ccb9 100644

//Synthetic comment -- @@ -159,6 +159,37 @@
});
}

public void testNonexistentLibrary() throws Exception {
checkDriver(
"",








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/XmlReporterTest.java b/lint/cli/src/test/java/com/android/tools/lint/XmlReporterTest.java
//Synthetic comment -- index 9ada70f..ed5b0cc 100644

//Synthetic comment -- @@ -82,7 +82,7 @@
String report = Files.toString(file, Charsets.UTF_8);
assertEquals(
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<issues format=\"3\" by=\"lint unittest\">\n" +
"\n" +
"    <issue\n" +
"        id=\"UsesMinSdkAttributes\"\n" +
//Synthetic comment -- @@ -93,6 +93,7 @@
"        summary=\"Checks that the minimum SDK and target SDK attributes are defined\"\n" +
"        explanation=\"The manifest should contain a `&lt;uses-sdk>` element which defines the minimum API Level required for the application to run, as well as the target version (the highest API level you have tested the version for.)\"\n" +
"        url=\"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html\"\n" +
"        errorLine1=\"    &lt;uses-sdk android:minSdkVersion=&quot;8&quot; />\"\n" +
"        errorLine2=\"    ^\">\n" +
"        <location\n" +
//Synthetic comment -- @@ -183,7 +184,7 @@
String report = Files.toString(file, Charsets.UTF_8);
assertEquals(
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<issues format=\"3\" by=\"lint unittest\">\n" +
"\n" +
"    <issue\n" +
"        id=\"UsesMinSdkAttributes\"\n" +
//Synthetic comment -- @@ -194,6 +195,7 @@
"        summary=\"Checks that the minimum SDK and target SDK attributes are defined\"\n" +
"        explanation=\"The manifest should contain a `&lt;uses-sdk>` element which defines the minimum API Level required for the application to run, as well as the target version (the highest API level you have tested the version for.)\"\n" +
"        url=\"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html\"\n" +
"        errorLine1=\"    &lt;uses-sdk android:minSdkVersion=&quot;8&quot; />\"\n" +
"        errorLine2=\"    ^\">\n" +
"        <location\n" +








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index bffe93e..b8b229c 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
// issues are properly registered
List<Issue> candidates = new BuiltinIssueRegistry().getIssues();
for (Issue issue : candidates) {
            if (issue.getDetectorClass() == detectorClass) {
issues.add(issue);
}
}
//Synthetic comment -- @@ -235,7 +235,7 @@

protected boolean isEnabled(Issue issue) {
Class<? extends Detector> detectorClass = getDetectorInstance().getClass();
        if (issue.getDetectorClass() == detectorClass) {
return true;
}









//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ButtonDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ButtonDetectorTest.java
//Synthetic comment -- index 989f33c..21153aa 100644

//Synthetic comment -- @@ -242,7 +242,7 @@
}

public void testBack() throws Exception {
        sTestIssue = ButtonDetector.BACKBUTTON;
assertEquals(
"res/layout/buttonbar.xml:183: Warning: Back buttons are not standard on Android; see design guide's navigation section [BackButton]\n" +
"    <Button\n" +








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index 0b8b141..7d81700 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.annotations.VisibleForTesting;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -53,6 +54,8 @@
protected IssueRegistry() {
}

/**
* Issue reported by lint (not a specific detector) when it cannot even
* parse an XML file prior to analysis
//Synthetic comment -- @@ -60,14 +63,14 @@
@NonNull
public static final Issue PARSER_ERROR = Issue.create(
"ParserError", //$NON-NLS-1$
"Finds files that contain fatal parser errors",
"Lint will ignore any files that contain fatal parsing errors. These may contain " +
"other errors, or contain code which affects issues in other files.",
Category.CORRECTNESS,
10,
Severity.ERROR,
            Detector.class,
            Scope.RESOURCE_FILE_SCOPE);

/**
* Issue reported by lint for various other issues which prevents lint from
//Synthetic comment -- @@ -76,6 +79,7 @@
@NonNull
public static final Issue LINT_ERROR = Issue.create(
"LintError", //$NON-NLS-1$
"Issues related to running lint itself, such as failure to read files, etc",
"This issue type represents a problem running lint itself. Examples include " +
"failure to find bytecode for source files (which means certain detectors " +
//Synthetic comment -- @@ -87,8 +91,22 @@
Category.LINT,
10,
Severity.ERROR,
            Detector.class,
            Scope.RESOURCE_FILE_SCOPE);

/**
* Returns the list of issues that can be found by all known detectors.
//Synthetic comment -- @@ -127,7 +145,7 @@
list = new ArrayList<Issue>(initialSize);
for (Issue issue : issues) {
// Determine if the scope matches
                    if (issue.isAdequate(scope)) {
list.add(issue);
}
}
//Synthetic comment -- @@ -169,15 +187,16 @@
new HashMap<Class<? extends Detector>, EnumSet<Scope>>();

for (Issue issue : issues) {
            Class<? extends Detector> detectorClass = issue.getDetectorClass();
            EnumSet<Scope> issueScope = issue.getScope();
if (!detectorClasses.contains(detectorClass)) {
// Determine if the issue is enabled
if (!configuration.isEnabled(issue)) {
continue;
}

                assert issue.isAdequate(scope); // Ensured by getIssuesForScope above

detectorClass = client.replaceDetector(detectorClass);









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 4022c7f..eb54b6c 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import com.android.resources.ResourceFolderType;
import com.android.sdklib.IAndroidTarget;
import com.android.tools.lint.client.api.LintListener.EventType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -500,7 +499,7 @@
Multimap<Class<? extends Detector>, Issue> issueMap =
ArrayListMultimap.create(issues.size(), 3);
for (Issue issue : issues) {
            issueMap.put(issue.getDetectorClass(), issue);
}

Map<Class<? extends Detector>, EnumSet<Scope>> detectorToScope =
//Synthetic comment -- @@ -532,7 +531,7 @@
add = true; // Include detector if any of its issues are enabled

EnumSet<Scope> s = detectorToScope.get(detectorClass);
                    EnumSet<Scope> issueScope = issue.getScope();
if (s == null) {
detectorToScope.put(detectorClass, issueScope);
} else if (!s.containsAll(issueScope)) {
//Synthetic comment -- @@ -872,10 +871,8 @@
if (mCanceled) {
mClient.report(
projectContext,
                // Must provide an issue since API guarantees that the issue parameter
                // is valid
                Issue.create("Lint", "", "", Category.PERFORMANCE, 0, Severity.INFORMATIONAL, //$NON-NLS-1$
                        Detector.class, EnumSet.noneOf(Scope.class)),
Severity.INFORMATIONAL,
null /*range*/,
"Lint canceled by user", null);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Implementation.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Implementation.java
new file mode 100644
//Synthetic comment -- index 0000000..d2ffee4

//Synthetic comment -- @@ -0,0 +1,187 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index bf46619..6462420 100644

//Synthetic comment -- @@ -17,14 +17,11 @@
package com.android.tools.lint.detector.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.IssueRegistry;
import com.google.common.annotations.Beta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;


//Synthetic comment -- @@ -45,65 +42,71 @@
private static final String HTTP_PREFIX = "http://"; //$NON-NLS-1$

private final String mId;
private final String mDescription;
private final String mExplanation;
private final Category mCategory;
private final int mPriority;
private final Severity mSeverity;
    private String mMoreInfoUrl;
private boolean mEnabledByDefault = true;
    private final EnumSet<Scope> mScope;
    private List<EnumSet<Scope>> mAnalysisScopes;
    private final Class<? extends Detector> mClass;

// Use factory methods
private Issue(
@NonNull String id,
@NonNull String description,
@NonNull String explanation,
@NonNull Category category,
int priority,
@NonNull Severity severity,
            @NonNull Class<? extends Detector> detectorClass,
            @NonNull EnumSet<Scope> scope) {
        super();
mId = id;
mDescription = description;
mExplanation = explanation;
mCategory = category;
mPriority = priority;
mSeverity = severity;
        mClass = detectorClass;
        mScope = scope;
}

/**
     * Creates a new issue
*
* @param id the fixed id of the issue
     * @param description the quick summary of the issue (one line)
* @param explanation a full explanation of the issue, with suggestions for
*            how to fix it
* @param category the associated category, if any
* @param priority the priority, a number from 1 to 10 with 10 being most
*            important/severe
* @param severity the default severity of the issue
     * @param detectorClass the class of the detector to find this issue
     * @param scope the scope of files required to analyze this issue
* @return a new {@link Issue}
*/
@NonNull
public static Issue create(
@NonNull String id,
@NonNull String description,
@NonNull String explanation,
@NonNull Category category,
int priority,
@NonNull Severity severity,
            @NonNull Class<? extends Detector> detectorClass,
            @NonNull EnumSet<Scope> scope) {
        return new Issue(id, description, explanation, category, priority, severity,
                detectorClass, scope);
}

/**
//Synthetic comment -- @@ -119,13 +122,41 @@
}

/**
     * Briefly (one line) describes the kinds of checks performed by this rule
*
     * @return a quick summary of the issue, never null, never empty
*/
@NonNull
    public String getDescription() {
        return mDescription;
}

/**
//Synthetic comment -- @@ -133,41 +164,21 @@
* "Buttons must define contentDescriptions". Preferably the explanation
* should also contain a description of how the problem should be solved.
* Additional info can be provided via {@link #getMoreInfo()}.
     * <p>
     * Note that the text may contain some simple markup, such as *'s around sentences
     * for bold text, and back quotes (`) for code fragments. You can obtain
     * the text without this markup by calling {@link #getExplanationAsSimpleText()},
     * and you can obtain the text as annotated HTML by calling
     * {@link #getExplanationAsHtml()}.
*
* @return an explanation of the issue, never null, never empty
*/
@NonNull
    public String getExplanation() {
        return mExplanation;
    }

    /**
     * Like {@link #getExplanation()}, but returns the text as properly escaped
     * and marked up HTML, where http URLs are linked, where words with asterisks
     * such as *this* are shown in bold, etc.
     *
     * @return the explanation of the issue, never null
     */
    @NonNull
    public String getExplanationAsHtml() {
        return convertMarkup(mExplanation, true /* html */);
    }

    /**
     * Like {@link #getExplanation()}, but returns the text as simple text without
     * markup tokens such as `this` for code fragments etc.
     *
     * @return the explanation of the issue, never null
     */
    @NonNull
    public String getExplanationAsSimpleText() {
        return convertMarkup(mExplanation, false /* not html = text */);
}

/**
//Synthetic comment -- @@ -215,9 +226,41 @@
*
* @return a link to more information, or null
*/
    @Nullable
    public String getMoreInfo() {
        return mMoreInfoUrl;
}

/**
//Synthetic comment -- @@ -231,14 +274,24 @@
}

/**
     * Returns the scope required to analyze the code to detect this issue.
     * This is determined by the detectors which reports the issue.
*
     * @return the required scope
*/
@NonNull
    public EnumSet<Scope> getScope() {
        return mScope;
}

/**
//Synthetic comment -- @@ -256,18 +309,6 @@
}

/**
     * Sets a more info URL string
     *
     * @param moreInfoUrl url string
     * @return this, for constructor chaining
     */
    @NonNull
    public Issue setMoreInfo(@NonNull String moreInfoUrl) {
        mMoreInfoUrl = moreInfoUrl;
        return this;
    }

    /**
* Sets whether this issue is enabled by default.
*
* @param enabledByDefault whether the issue should be enabled by default
//Synthetic comment -- @@ -279,150 +320,6 @@
return this;
}

    /**
     * Returns the sets of scopes required to analyze this issue, or null if all
     * scopes named by {@link Issue#getScope()} are necessary. Note that only
     * <b>one</b> match out of this collection is required, not all, and that
     * the scope set returned by {@link #getScope()} does not have to be returned
     * by this method, but is always implied to be included.
     * <p>
     * The scopes returned by {@link Issue#getScope()} list all the various
     * scopes that are <b>affected</b> by this issue, meaning the detector
     * should consider it. Frequently, the detector must analyze all these
     * scopes in order to properly decide whether an issue is found. For
     * example, the unused resource detector needs to consider both the XML
     * resource files and the Java source files in order to decide if a resource
     * is unused. If it analyzes just the Java files for example, it might
     * incorrectly conclude that a resource is unused because it did not
     * discover a resource reference in an XML file.
     * <p>
     * However, there are other issues where the issue can occur in a variety of
     * files, but the detector can consider each in isolation. For example, the
     * API checker is affected by both XML files and Java class files (detecting
     * both layout constructor references in XML layout files as well as code
     * references in .class files). It doesn't have to analyze both; it is
     * capable of incrementally analyzing just an XML file, or just a class
     * file, without considering the other.
     * <p>
     * The required scope list provides a list of scope sets that can be used to
     * analyze this issue. For each scope set, all the scopes must be matched by
     * the incremental analysis, but any one of the scope sets can be analyzed
     * in isolation.
     * <p>
     * The required scope list is not required to include the full scope set
     * returned by {@link #getScope()}; that set is always assumed to be
     * included.
     * <p>
     * NOTE: You would normally call {@link #isAdequate(EnumSet)} rather
     * than calling this method directly.
     *
     * @return a list of required scopes, or null.
     */
    @Nullable
    public Collection<EnumSet<Scope>> getAnalysisScopes() {
        return mAnalysisScopes;
    }

    /**
     * Sets the collection of scopes that are allowed to be analyzed independently.
     * See the {@link #getAnalysisScopes()} method for a full explanation.
     * Note that you usually want to just call {@link #addAnalysisScope(EnumSet)}
     * instead of constructing a list up front and passing it in here. This
     * method exists primarily such that commonly used share sets of analysis
     * scopes can be reused and set directly.
     *
     * @param required the collection of scopes
     * @return this, for constructor chaining
     */
    @NonNull
    public Issue setAnalysisScopes(@Nullable List<EnumSet<Scope>> required) {
        mAnalysisScopes = required;

        return this;
    }

    /**
     * Returns true if the given scope is adequate for analyzing this issue.
     * This looks through the analysis scopes (see
     * {@link #addAnalysisScope(EnumSet)}) and if the scope passed in fully
     * covers at least one of them, or if it covers the scope of the issue
     * itself (see {@link #getScope()}, which should be a superset of all the
     * analysis scopes) returns true.
     * <p>
     * The scope set returned by {@link Issue#getScope()} lists all the various
     * scopes that are <b>affected</b> by this issue, meaning the detector
     * should consider it. Frequently, the detector must analyze all these
     * scopes in order to properly decide whether an issue is found. For
     * example, the unused resource detector needs to consider both the XML
     * resource files and the Java source files in order to decide if a resource
     * is unused. If it analyzes just the Java files for example, it might
     * incorrectly conclude that a resource is unused because it did not
     * discover a resource reference in an XML file.
     * <p>
     * However, there are other issues where the issue can occur in a variety of
     * files, but the detector can consider each in isolation. For example, the
     * API checker is affected by both XML files and Java class files (detecting
     * both layout constructor references in XML layout files as well as code
     * references in .class files). It doesn't have to analyze both; it is
     * capable of incrementally analyzing just an XML file, or just a class
     * file, without considering the other.
     * <p>
     * An issue can register additional scope sets that can are adequate
     * for analyzing the issue, by calling {@link #addAnalysisScope(EnumSet)}.
     * This method returns true if the given scope matches one or more analysis
     * scope, or the overall scope.
     *
     * @param scope the scope available for analysis
     * @return true if this issue can be analyzed with the given available scope
     */
    public boolean isAdequate(@NonNull EnumSet<Scope> scope) {
        if (scope.containsAll(mScope)) {
            return true;
        }

        if (mAnalysisScopes != null) {
            for (EnumSet<Scope> analysisScope : mAnalysisScopes) {
                if (mScope.containsAll(analysisScope)) {
                    return true;
                }
            }
        }

        if (this == IssueRegistry.LINT_ERROR || this == IssueRegistry.PARSER_ERROR) {
            return true;
        }

        return false;
    }

    /**
     * Adds a scope set that can be analyzed independently to uncover this issue.
     * See the {@link #getAnalysisScopes()} method for a full explanation.
     * Note that the {@link #getScope()} does not have to be added here; it is
     * always considered an analysis scope.
     *
     * @param scope the additional scope which can analyze this issue independently
     * @return this, for constructor chaining
     */
    public Issue addAnalysisScope(@Nullable EnumSet<Scope> scope) {
        if (mAnalysisScopes == null) {
            mAnalysisScopes = new ArrayList<EnumSet<Scope>>(2);
        }
        mAnalysisScopes.add(scope);

        return this;
    }

    /**
     * Returns the class of the detector to use to find this issue
     *
     * @return the class of the detector to use to find this issue
     */
    @NonNull
    public Class<? extends Detector> getDetectorClass() {
        return mClass;
    }

@Override
public String toString() {
return mId;
//Synthetic comment -- @@ -549,4 +446,19 @@
}
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java
//Synthetic comment -- index 0e33f1b..8e1739c 100644

//Synthetic comment -- @@ -149,4 +149,14 @@
public static final EnumSet<Scope> MANIFEST_SCOPE = EnumSet.of(MANIFEST);
/** Scope-set used for detectors which correspond to some other context */
public static final EnumSet<Scope> OTHER_SCOPE = EnumSet.of(OTHER);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AccessibilityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AccessibilityDetector.java
//Synthetic comment -- index 4d069cc..fd3fad8 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -52,6 +53,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ContentDescription", //$NON-NLS-1$
"Ensures that image widgets provide a contentDescription",
"Non-textual widgets like ImageViews and ImageButtons should use the " +
"`contentDescription` attribute to specify a textual description of " +
//Synthetic comment -- @@ -71,8 +73,9 @@
Category.A11Y,
3,
Severity.WARNING,
            AccessibilityDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link AccessibilityDetector} */
public AccessibilityDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AlwaysShowActionDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AlwaysShowActionDetector.java
//Synthetic comment -- index 2f3fc20..64194b0 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -39,7 +40,6 @@
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import lombok.ast.AstVisitor;
//Synthetic comment -- @@ -58,7 +58,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"AlwaysShowAction", //$NON-NLS-1$
            "Checks for uses of showAsAction=\"always\" and suggests showAsAction=\"ifRoom\" " +
"instead",

"Using `showAsAction=\"always\"` in menu XML, or `MenuItem.SHOW_AS_ACTION_ALWAYS` in " +
//Synthetic comment -- @@ -77,9 +78,10 @@
Category.USABILITY,
3,
Severity.WARNING,
            AlwaysShowActionDetector.class,
            EnumSet.of(Scope.RESOURCE_FILE, Scope.JAVA_FILE)).setMoreInfo(
                    "http://developer.android.com/design/patterns/actionbar.html"); //$NON-NLS-1$

/** List of showAsAction attributes appearing in the current menu XML file */
private List<Attr> mFileAttributes;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AnnotationDetector.java
//Synthetic comment -- index eca9256..65b9ade 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -61,6 +62,7 @@
/** Placing SuppressLint on a local variable doesn't work for class-file based checks */
public static final Issue ISSUE = Issue.create(
"LocalSuppress", //$NON-NLS-1$
"Looks for @SuppressLint annotations in locations where it doesn't work for class based checks",

"The `@SuppressAnnotation` is used to suppress Lint warnings in Java files. However, " +
//Synthetic comment -- @@ -74,8 +76,9 @@
Category.CORRECTNESS,
3,
Severity.ERROR,
            AnnotationDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Constructs a new {@link AnnotationDetector} check */
public AnnotationDetector() {
//Synthetic comment -- @@ -162,7 +165,7 @@
// Special-case the ApiDetector issue, since it does both source file analysis
// only on field references, and class file analysis on the rest, so we allow
// annotations outside of methods only on fields
            if (issue != null && !issue.getScope().contains(Scope.JAVA_FILE)
|| issue == ApiDetector.UNSUPPORTED) {
// Ensure that this isn't a field
Node parent = node.getParent();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 18dd953..f9f2047 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.DefaultPosition;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -135,25 +136,23 @@
private static final boolean AOSP_BUILD = System.getenv("ANDROID_BUILD_TOP") != null; //$NON-NLS-1$

/** Accessing an unsupported API */
    public static final Issue UNSUPPORTED = Issue.create("NewApi", //$NON-NLS-1$
"Finds API accesses to APIs that are not supported in all targeted API versions",

"This check scans through all the Android API calls in the application and " +
"warns about any calls that are not available on *all* versions targeted " +
            "by this application (according to its minimum SDK attribute in the manifest).\n"
            +
"\n" +
            "If you really want to use this API and don't need to support older devices just "
            +
"set the `minSdkVersion` in your `AndroidManifest.xml` file." +
"\n" +
"If your code is *deliberately* accessing newer APIs, and you have ensured " +
            "(e.g. with conditional execution) that this code will only ever be called on a "
            +
"supported platform, then you can annotate your class or method with the " +
"`@TargetApi` annotation specifying the local minimum SDK to apply, such as " +
            "`@TargetApi(11)`, such that this check considers 11 rather than your manifest "
            +
"file's minimum SDK as the required API level.\n" +
"\n" +
"If you are deliberately setting `android:` attributes in style definitions, " +
//Synthetic comment -- @@ -161,19 +160,21 @@
"into runtime conflicts on certain devices where manufacturers have added " +
"custom attributes whose ids conflict with the new ones on later platforms.\n" +
"\n" +
            "Similarly, you can use tools:targetApi=\"11\" in an XML file to indicate that "
            +
"the element will only be inflated in an adequate context.",
Category.CORRECTNESS,
6,
Severity.ERROR,
            ApiDetector.class,
            EnumSet.of(Scope.CLASS_FILE, Scope.RESOURCE_FILE, Scope.MANIFEST))
            .addAnalysisScope(Scope.RESOURCE_FILE_SCOPE)
            .addAnalysisScope(Scope.CLASS_FILE_SCOPE);

/** Accessing an inlined API on older platforms */
    public static final Issue INLINED = Issue.create("InlinedApi", //$NON-NLS-1$
"Finds inlined fields that may or may not work on older platforms",

"This check scans through all the Android API field references in the application " +
//Synthetic comment -- @@ -186,27 +187,26 @@
"the code carefully and device whether it's safe and can be suppressed " +
"or whether the code needs tbe guarded.\n" +
"\n" +
            "If you really want to use this API and don't need to support older devices just "
            +
"set the `minSdkVersion` in your `AndroidManifest.xml` file." +
"\n" +
"If your code is *deliberately* accessing newer APIs, and you have ensured " +
            "(e.g. with conditional execution) that this code will only ever be called on a "
            +
"supported platform, then you can annotate your class or method with the " +
"`@TargetApi` annotation specifying the local minimum SDK to apply, such as " +
            "`@TargetApi(11)`, such that this check considers 11 rather than your manifest "
            +
"file's minimum SDK as the required API level.\n",
Category.CORRECTNESS,
6,
Severity.WARNING,
            ApiDetector.class,
            EnumSet.of(Scope.JAVA_FILE))
            .addAnalysisScope(Scope.JAVA_FILE_SCOPE);

/** Accessing an unsupported API */
    public static final Issue OVERRIDE = Issue.create("Override", //$NON-NLS-1$
"Finds method declarations that will accidentally override methods in later versions",

"Suppose you are building against Android API 8, and you've subclassed Activity. " +
//Synthetic comment -- @@ -228,8 +228,9 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            ApiDetector.class,
            Scope.CLASS_FILE_SCOPE);

private static final String TARGET_API_VMSIG = '/' + TARGET_API + ';';
private static final String SWITCH_TABLE_PREFIX = "$SWITCH_TABLE$";  //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 8bbcffb..549288f 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -61,6 +62,7 @@
/** Are there differences in how many array elements are declared? */
public static final Issue INCONSISTENT = Issue.create(
"InconsistentArrays", //$NON-NLS-1$
"Checks for inconsistencies in the number of elements in arrays",
"When an array is translated in a different locale, it should normally have " +
"the same number of elements as the original array. When adding or removing " +
//Synthetic comment -- @@ -77,8 +79,9 @@
Category.CORRECTNESS,
7,
Severity.WARNING,
            ArraySizeDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

private Multimap<File, Pair<String, Integer>> mFileToArrayCount;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 9c22c55..0db6037 100644

//Synthetic comment -- @@ -86,7 +86,7 @@
issues.add(MergeRootFrameLayoutDetector.ISSUE);
issues.add(NestedScrollingWidgetDetector.ISSUE);
issues.add(ChildCountDetector.SCROLLVIEW_ISSUE);
        issues.add(ChildCountDetector.ADAPTERVIEW_ISSUE);
issues.add(UseCompoundDrawableDetector.ISSUE);
issues.add(UselessViewDetector.USELESS_PARENT);
issues.add(UselessViewDetector.USELESS_LEAF);
//Synthetic comment -- @@ -103,7 +103,7 @@
issues.add(MissingClassDetector.INSTANTIATABLE);
issues.add(MissingClassDetector.INNERCLASS);
issues.add(MissingIdDetector.ISSUE);
        issues.add(WrongCaseDetector.WRONGCASE);
issues.add(HandlerDetector.ISSUE);
issues.add(FragmentDetector.ISSUE);
issues.add(TranslationDetector.EXTRA);
//Synthetic comment -- @@ -111,10 +111,10 @@
issues.add(HardcodedValuesDetector.ISSUE);
issues.add(Utf8Detector.ISSUE);
issues.add(DosLineEndingDetector.ISSUE);
        issues.add(CommentDetector.EASTEREGG);
        issues.add(CommentDetector.STOPSHIP);
        issues.add(ProguardDetector.WRONGKEEP);
        issues.add(ProguardDetector.SPLITCONFIG);
issues.add(PxUsageDetector.PX_ISSUE);
issues.add(PxUsageDetector.DP_ISSUE);
issues.add(PxUsageDetector.IN_MM_ISSUE);
//Synthetic comment -- @@ -166,7 +166,7 @@
issues.add(TypographyDetector.OTHER);
issues.add(ButtonDetector.ORDER);
issues.add(ButtonDetector.CASE);
        issues.add(ButtonDetector.BACKBUTTON);
issues.add(ButtonDetector.STYLE);
issues.add(DetectMissingPrefix.MISSING_NAMESPACE);
issues.add(OverdrawDetector.ISSUE);
//Synthetic comment -- @@ -178,15 +178,15 @@
issues.add(WrongImportDetector.ISSUE);
issues.add(WrongLocationDetector.ISSUE);
issues.add(ViewConstructorDetector.ISSUE);
        issues.add(NamespaceDetector.CUSTOMVIEW);
issues.add(NamespaceDetector.UNUSED);
issues.add(NamespaceDetector.TYPO);
issues.add(AlwaysShowActionDetector.ISSUE);
issues.add(TitleDetector.ISSUE);
issues.add(ColorUsageDetector.ISSUE);
issues.add(JavaPerformanceDetector.PAINT_ALLOC);
        issues.add(JavaPerformanceDetector.USE_VALUEOF);
        issues.add(JavaPerformanceDetector.USE_SPARSEARRAY);
issues.add(WakelockDetector.ISSUE);
issues.add(CleanupDetector.RECYCLE_RESOURCE);
issues.add(CleanupDetector.COMMIT_FRAGMENT);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index e756e03..39343f2 100644

//Synthetic comment -- @@ -43,6 +43,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -93,9 +94,14 @@
private static final String ANDROID_CANCEL_RESOURCE =
ANDROID_STRING_PREFIX + "cancel"; //$NON-NLS-1$

/** The main issue discovered by this detector */
public static final Issue ORDER = Issue.create(
"ButtonOrder", //$NON-NLS-1$
"Ensures the dismissive action of a dialog is on the left and affirmative on " +
"the right",

//Synthetic comment -- @@ -114,14 +120,14 @@
Category.USABILITY,
8,
Severity.WARNING,
            ButtonDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo(
                "http://developer.android.com/design/building-blocks/dialogs.html"); //$NON-NLS-1$

/** The main issue discovered by this detector */
public static final Issue STYLE = Issue.create(
"ButtonStyle", //$NON-NLS-1$
"Ensures that buttons in button bars are borderless",

"Button bars typically use a borderless style for the buttons. Set the " +
//Synthetic comment -- @@ -132,14 +138,14 @@
Category.USABILITY,
5,
Severity.WARNING,
            ButtonDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo(
                "http://developer.android.com/design/building-blocks/buttons.html"); //$NON-NLS-1$

/** The main issue discovered by this detector */
    public static final Issue BACKBUTTON = Issue.create(
"BackButton", //$NON-NLS-1$
"Looks for Back buttons, which are not common on the Android platform.",
// TODO: Look for ">" as label suffixes as well

//Synthetic comment -- @@ -157,15 +163,15 @@
Category.USABILITY,
6,
Severity.WARNING,
            ButtonDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
.setEnabledByDefault(false)
            .setMoreInfo(
                "http://developer.android.com/design/patterns/pure-android.html"); //$NON-NLS-1$

/** The main issue discovered by this detector */
public static final Issue CASE = Issue.create(
"ButtonCase", //$NON-NLS-1$
"Ensures that Cancel/OK dialog buttons use the canonical capitalization",

"The standard capitalization for OK/Cancel dialogs is \"OK\" and \"Cancel\". " +
//Synthetic comment -- @@ -175,8 +181,7 @@
Category.USABILITY,
2,
Severity.WARNING,
            ButtonDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Set of resource names whose value was either OK or Cancel */
private Set<String> mApplicableResources;
//Synthetic comment -- @@ -343,8 +348,8 @@
} else {
assert BACK_LABEL.equalsIgnoreCase(label) : label + ':' + context.file;
Location location = context.getLocation(element);
                        if (context.isEnabled(BACKBUTTON)) {
                            context.report(BACKBUTTON, element, location,
"Back buttons are not standard on Android; see design guide's " +
"navigation section", null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ChildCountDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ChildCountDetector.java
//Synthetic comment -- index bc1e30d..9b10d8c 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -45,30 +46,35 @@
*/
public class ChildCountDetector extends LayoutDetector {

/** The main issue discovered by this detector */
public static final Issue SCROLLVIEW_ISSUE = Issue.create(
"ScrollViewCount", //$NON-NLS-1$
"Checks that ScrollViews have exactly one child widget",
"ScrollViews can only have one child widget. If you want more children, wrap them " +
"in a container layout.",
Category.CORRECTNESS,
8,
Severity.WARNING,
            ChildCountDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** The main issue discovered by this detector */
    public static final Issue ADAPTERVIEW_ISSUE = Issue.create(
"AdapterViewChildren", //$NON-NLS-1$
"Checks that AdapterViews do not define their children in XML",
"AdapterViews such as ListViews must be configured with data from Java code, " +
"such as a ListAdapter.",
Category.CORRECTNESS,
10,
Severity.WARNING,
            ChildCountDetector.class,
            Scope.RESOURCE_FILE_SCOPE).setMoreInfo(
                "http://developer.android.com/reference/android/widget/AdapterView.html"); //$NON-NLS-1$

/** Constructs a new {@link ChildCountDetector} */
public ChildCountDetector() {
//Synthetic comment -- @@ -104,7 +110,7 @@
} else {
// Adapter view
if (childCount > 0 && getAccurateChildCount(element) > 0) {
                context.report(ADAPTERVIEW_ISSUE, element,
context.getLocation(element),
"A list/grid should have no children declared in XML", null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CleanupDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CleanupDetector.java
//Synthetic comment -- index 1541d23..1639a18 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -50,10 +51,16 @@
* for missing {@code commit} calls on FragmentTransactions, etc.
*/
public class CleanupDetector extends Detector implements ClassScanner {
/** Problems with missing recycle calls */
public static final Issue RECYCLE_RESOURCE = Issue.create(
"Recycle", //$NON-NLS-1$
        "Looks for missing recycle() calls on resources",

"Many resources, such as TypedArrays, VelocityTrackers, etc., " +
"should be recycled (with a `recycle()` call) after use. This lint check looks " +
//Synthetic comment -- @@ -62,21 +69,20 @@
Category.PERFORMANCE,
7,
Severity.WARNING,
        CleanupDetector.class,
        Scope.CLASS_FILE_SCOPE);

/** Problems with missing commit calls. */
public static final Issue COMMIT_FRAGMENT = Issue.create(
"CommitTransaction", //$NON-NLS-1$
            "Looks for missing commit() calls on FragmentTransactions",

"After creating a `FragmentTransaction`, you typically need to commit it as well",

Category.CORRECTNESS,
7,
Severity.WARNING,
            CleanupDetector.class,
            Scope.CLASS_FILE_SCOPE);

// Target method names
private static final String RECYCLE = "recycle";                                  //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index e7ffcb6..85be257 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -44,6 +45,7 @@
/** Attempting to set a resource id as a color */
public static final Issue ISSUE = Issue.create(
"ResourceAsColor", //$NON-NLS-1$
"Looks for calls to setColor where a resource id is passed instead of a " +
"resolved color",

//Synthetic comment -- @@ -54,8 +56,9 @@
Category.CORRECTNESS,
7,
Severity.ERROR,
            ColorUsageDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Constructs a new {@link ColorUsageDetector} check */
public ColorUsageDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CommentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CommentDetector.java
//Synthetic comment -- index 6f07e77..8e52c7d 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -42,9 +43,14 @@
public class CommentDetector extends Detector implements Detector.JavaScanner {
private static final String STOPSHIP_COMMENT = "STOPSHIP"; //$NON-NLS-1$

/** Looks for hidden code */
    public static final Issue EASTEREGG = Issue.create(
"EasterEgg", //$NON-NLS-1$
"Looks for hidden easter eggs",
"An \"easter egg\" is code deliberately hidden in the code, both from potential " +
"users and even from other developers. This lint check looks for code which " +
//Synthetic comment -- @@ -52,13 +58,14 @@
Category.SECURITY,
6,
Severity.WARNING,
            CommentDetector.class,
            Scope.JAVA_FILE_SCOPE).setEnabledByDefault(false);

/** Looks for special comment markers intended to stop shipping the code */
    public static final Issue STOPSHIP = Issue.create(
"StopShip", //$NON-NLS-1$
            "Looks for comment markers of the form \"STOPSHIP\" which indicates that code " +
"should not be released yet",

"Using the comment `// STOPSHIP` can be used to flag code that is incomplete but " +
//Synthetic comment -- @@ -67,8 +74,8 @@
Category.CORRECTNESS,
10,
Severity.WARNING,
            CommentDetector.class,
            Scope.JAVA_FILE_SCOPE).setEnabledByDefault(false);

private static final String ESCAPE_STRING = "\\u002a\\u002f"; //$NON-NLS-1$

//Synthetic comment -- @@ -174,7 +181,7 @@
0, ESCAPE_STRING.length())) {
Location location = Location.create(context.file, source,
offset + i - 1, offset + i - 1 + ESCAPE_STRING.length());
                        context.report(EASTEREGG, location,
"Code might be hidden here; found unicode escape sequence " +
"which is interpreted as comment end, compiled code follows",
null);
//Synthetic comment -- @@ -187,7 +194,7 @@
// TODO: Only flag this issue in release mode??
Location location = Location.create(context.file, source,
offset + i - 1, offset + i - 1 + STOPSHIP_COMMENT.length());
                context.report(STOPSHIP, location,
"STOPSHIP comment found; points to code which must be fixed prior " +
"to release",
null);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CutPasteDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CutPasteDetector.java
//Synthetic comment -- index 84fb6b6..d50d64e 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -56,7 +57,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"CutPasteId", //$NON-NLS-1$
            "Looks for code cut & paste mistakes in findViewById() calls",

"This lint check looks for cases where you have cut & pasted calls to " +
"`findViewById` but have forgotten to update the R.id field. It's possible " +
//Synthetic comment -- @@ -68,8 +70,9 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            CutPasteDetector.class,
            Scope.JAVA_FILE_SCOPE);

private Node mLastMethod;
private Map<String, MethodInvocation> mIds;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DeprecationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DeprecationDetector.java
//Synthetic comment -- index 6475a7a..8a4ad27 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -53,14 +54,16 @@
/** Usage of deprecated views or attributes */
public static final Issue ISSUE = Issue.create(
"Deprecated", //$NON-NLS-1$
"Looks for usages of deprecated layouts, attributes, and so on.",
"Deprecated views, attributes and so on are deprecated because there " +
"is a better way to do something. Do it that new way. You've been warned.",
Category.CORRECTNESS,
2,
Severity.WARNING,
            DeprecationDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link DeprecationDetector} */
public DeprecationDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DetectMissingPrefix.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DetectMissingPrefix.java
//Synthetic comment -- index 2b24732..55f9587 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -63,6 +64,7 @@
/** Attributes missing the android: prefix */
public static final Issue MISSING_NAMESPACE = Issue.create(
"MissingPrefix", //$NON-NLS-1$
"Detect XML attributes not using the Android namespace",
"Most Android views have attributes in the Android namespace. When referencing " +
"these attributes you *must* include the namespace prefix, or your attribute will " +
//Synthetic comment -- @@ -74,10 +76,10 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            DetectMissingPrefix.class,
            EnumSet.of(Scope.MANIFEST, Scope.RESOURCE_FILE))
            .addAnalysisScope(Scope.MANIFEST_SCOPE)
            .addAnalysisScope(Scope.RESOURCE_FILE_SCOPE);

private static final Set<String> NO_PREFIX_ATTRS = new HashSet<String>();
static {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DosLineEndingDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DosLineEndingDetector.java
//Synthetic comment -- index 1a2a720..8c565e0 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -35,6 +36,7 @@
/** Detects mangled DOS line ending documents */
public static final Issue ISSUE = Issue.create(
"MangledCRLF", //$NON-NLS-1$
"Checks that files with DOS line endings are consistent",

"On Windows, line endings are typically recorded as carriage return plus " +
//Synthetic comment -- @@ -48,9 +50,10 @@
Category.CORRECTNESS,
2,
Severity.ERROR,
            DosLineEndingDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo("https://bugs.eclipse.org/bugs/show_bug.cgi?id=375421"); //$NON-NLS-1$

/** Constructs a new {@link DosLineEndingDetector} */
public DosLineEndingDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index de3e4d2..a8c9735 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -72,21 +73,26 @@
private Multimap<File, Multimap<String, Occurrence>> mLocations;
private List<Occurrence> mErrors;

/** The main issue discovered by this detector */
public static final Issue WITHIN_LAYOUT = Issue.create(
"DuplicateIds", //$NON-NLS-1$
"Checks for duplicate ids within a single layout",
"Within a layout, id's should be unique since otherwise `findViewById()` can " +
"return an unexpected view.",
Category.CORRECTNESS,
7,
Severity.WARNING,
            DuplicateIdDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** The main issue discovered by this detector */
public static final Issue CROSS_LAYOUT = Issue.create(
"DuplicateIncludedIds", //$NON-NLS-1$
"Checks for duplicate ids across layouts that are combined with include tags",
"It's okay for two independent layouts to use the same ids. However, if " +
"layouts are combined with include tags, then the id's need to be unique " +
//Synthetic comment -- @@ -95,8 +101,7 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            DuplicateIdDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Constructs a duplicate id check */
public DuplicateIdDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateResourceDetector.java
//Synthetic comment -- index 004303c..3aa7947 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Location.Handle;
//Synthetic comment -- @@ -59,6 +60,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"DuplicateDefinition", //$NON-NLS-1$
"Discovers duplicate definitions of resources",

"You can define a resource multiple times in different resource folders; that's how " +
//Synthetic comment -- @@ -70,8 +72,10 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            DuplicateResourceDetector.class,
            Scope.ALL_RESOURCES_SCOPE).addAnalysisScope(Scope.RESOURCE_FILE_SCOPE);

private static final String PRODUCT = "product";   //$NON-NLS-1$
private Map<ResourceType, Set<String>> mTypeMap;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ExtraTextDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ExtraTextDetector.java
//Synthetic comment -- index e0781db..1f98b1d 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.DefaultPosition;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Position;
//Synthetic comment -- @@ -44,7 +45,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ExtraText", //$NON-NLS-1$
            "Looks for extraneous text in layout files",
"Layout resource files should only contain elements and attributes. Any XML " +
"text content found in the file is likely accidental (and potentially " +
"dangerous if the text resembles XML and the developer believes the text " +
//Synthetic comment -- @@ -52,8 +55,10 @@
Category.CORRECTNESS,
3,
Severity.WARNING,
            ExtraTextDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new detector */
public ExtraTextDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index 04841ab..99e79aa 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -52,6 +53,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"FieldGetter", //$NON-NLS-1$
"Suggests replacing uses of getters with direct field access within a class",

"Accessing a field within the class that defines a getter for that field is " +
//Synthetic comment -- @@ -59,18 +61,20 @@
"nothing other than return the field, you might want to just reference the " +
"local field directly instead.\n" +
"\n" +
            "NOTE: As of Android 2.3 (Gingerbread), this optimization is performed " +
"automatically by Dalvik, so there is no need to change your code; this is " +
"only relevant if you are targeting older versions of Android.",

Category.PERFORMANCE,
4,
Severity.WARNING,
            FieldGetterDetector.class,
            Scope.CLASS_FILE_SCOPE).
// This is a micro-optimization: not enabled by default
            setEnabledByDefault(false).setMoreInfo(
           "http://developer.android.com/guide/practices/design/performance.html#internal_get_set"); //$NON-NLS-1$
private ArrayList<Entry> mPendingCalls;

/** Constructs a new {@link FieldGetterDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index f6ebcd6..43d7e80 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -53,7 +54,8 @@
/** Are fragment subclasses instantiatable? */
public static final Issue ISSUE = Issue.create(
"ValidFragment", //$NON-NLS-1$
        "Ensures that Fragment subclasses can be instantiated",

"From the Fragment documentation:\n" +
"*Every* fragment must have an empty constructor, so it can be instantiated when " +
//Synthetic comment -- @@ -66,9 +68,11 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
        FragmentDetector.class,
        Scope.CLASS_FILE_SCOPE).setMoreInfo(
        "http://developer.android.com/reference/android/app/Fragment.html#Fragment()"); //$NON-NLS-1$


/** Constructs a new {@link FragmentDetector} */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/GridLayoutDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/GridLayoutDetector.java
//Synthetic comment -- index c348502..c95b0b6 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -46,6 +47,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"GridLayout", //$NON-NLS-1$
"Checks for potential GridLayout errors like declaring rows and columns outside " +
"the declared grid dimensions",
"Declaring a layout_row or layout_column that falls outside the declared size " +
//Synthetic comment -- @@ -53,8 +55,9 @@
Category.CORRECTNESS,
4,
Severity.FATAL,
            GridLayoutDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link GridLayoutDetector} check */
public GridLayoutDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index cfe8f0b..ad029b3 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -38,20 +39,22 @@

/** Potentially leaking handlers */
public static final Issue ISSUE = Issue.create(
        "HandlerLeak", //$NON-NLS-1$
        "Ensures that Handler classes do not hold on to a reference to an outer class",

        "In Android, Handler classes should be static or leaks might occur. " +
        "Messages enqueued on the application thread's MessageQueue also retain their " +
        "target Handler. If the Handler is an inner class, its outer class will be " +
        "retained as well. To avoid leaking the outer class, declare the Handler as a " +
        "static nested class with a WeakReference to its outer class.",

        Category.PERFORMANCE,
        4,
        Severity.WARNING,
        HandlerDetector.class,
        Scope.CLASS_FILE_SCOPE);

/** Constructs a new {@link HandlerDetector} */
public HandlerDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index fd678ca..76a242d 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -44,7 +45,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"HardcodedDebugMode", //$NON-NLS-1$
            "Checks for hardcoded values of android:debuggable in the manifest",

"It's best to leave out the `android:debuggable` attribute from the manifest. " +
"If you do, then the tools will automatically insert `android:debuggable=true` when " +
//Synthetic comment -- @@ -58,8 +60,9 @@
Category.SECURITY,
5,
Severity.WARNING,
            HardcodedDebugModeDetector.class,
            Scope.MANIFEST_SCOPE);

/** Constructs a new {@link HardcodedDebugModeDetector} check */
public HardcodedDebugModeDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedValuesDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedValuesDetector.java
//Synthetic comment -- index 11cc19d..e16f4b8 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -47,7 +48,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"HardcodedText", //$NON-NLS-1$
"Looks for hardcoded text attributes which should be converted to resource lookup",
"Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
"\n" +
"* When creating configuration variations (for example for landscape or portrait)" +
//Synthetic comment -- @@ -62,8 +65,9 @@
Category.I18N,
5,
Severity.WARNING,
            HardcodedValuesDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

// TODO: Add additional issues here, such as hardcoded colors, hardcoded sizes, etc









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 7f26c96..f08f160 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -143,9 +144,18 @@
private static final EnumSet<Scope> ICON_TYPE_SCOPE = EnumSet.of(Scope.ALL_RESOURCE_FILES,
Scope.JAVA_FILE, Scope.MANIFEST);

/** Wrong icon size according to published conventions */
public static final Issue ICON_EXPECTED_SIZE = Issue.create(
"IconExpectedSize", //$NON-NLS-1$
"Ensures that launcher icons, notification icons etc have the correct size",
"There are predefined sizes (for each density) for launcher icons. You " +
"should follow these conventions to make sure your icons fit in with the " +
//Synthetic comment -- @@ -153,16 +163,16 @@
Category.ICONS,
5,
Severity.WARNING,
            IconDetector.class,
            ICON_TYPE_SCOPE)
// Still some potential false positives:
.setEnabledByDefault(false)
            .setMoreInfo(
            "http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

/** Inconsistent dip size across densities */
public static final Issue ICON_DIP_SIZE = Issue.create(
"IconDipSize", //$NON-NLS-1$
"Ensures that icons across densities provide roughly the same density-independent size",
"Checks the all icons which are provided in multiple densities, all compute to " +
"roughly the same density-independent pixel (`dip`) size. This catches errors where " +
//Synthetic comment -- @@ -171,12 +181,12 @@
Category.ICONS,
5,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Images in res/drawable folder */
public static final Issue ICON_LOCATION = Issue.create(
"IconLocation", //$NON-NLS-1$
"Ensures that images are not defined in the density-independent drawable folder",
"The res/drawable folder is intended for density-independent graphics such as " +
"shapes defined in XML. For bitmaps, move it to `drawable-mdpi` and consider " +
//Synthetic comment -- @@ -186,13 +196,13 @@
Category.ICONS,
5,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE).setMoreInfo(
"http://developer.android.com/guide/practices/screens_support.html"); //$NON-NLS-1$

/** Missing density versions of image */
public static final Issue ICON_DENSITIES = Issue.create(
"IconDensities", //$NON-NLS-1$
"Ensures that icons provide custom versions for all supported densities",
"Icons will look best if a custom version is provided for each of the " +
"major screen density classes (low, medium, high, extra high). " +
//Synthetic comment -- @@ -207,13 +217,13 @@
Category.ICONS,
4,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE).setMoreInfo(
"http://developer.android.com/guide/practices/screens_support.html"); //$NON-NLS-1$

/** Missing density folders */
public static final Issue ICON_MISSING_FOLDER = Issue.create(
"IconMissingDensityFolder", //$NON-NLS-1$
"Ensures that all the density folders are present",
"Icons will look best if a custom version is provided for each of the " +
"major screen density classes (low, medium, high, extra high). " +
//Synthetic comment -- @@ -227,26 +237,26 @@
Category.ICONS,
3,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE).setMoreInfo(
"http://developer.android.com/guide/practices/screens_support.html"); //$NON-NLS-1$

/** Using .gif bitmaps */
public static final Issue GIF_USAGE = Issue.create(
"GifUsage", //$NON-NLS-1$
"Checks for images using the GIF file format which is discouraged",
"The `.gif` file format is discouraged. Consider using `.png` (preferred) " +
"or `.jpg` (acceptable) instead.",
Category.ICONS,
5,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/resources/drawable-resource.html#Bitmap"); //$NON-NLS-1$

/** Duplicated icons across different names */
public static final Issue DUPLICATES_NAMES = Issue.create(
"IconDuplicates", //$NON-NLS-1$
"Finds duplicated icons under different names",
"If an icon is repeated under different names, you can consolidate and just " +
"use one of the icons and delete the others to make your application smaller. " +
//Synthetic comment -- @@ -255,12 +265,12 @@
Category.ICONS,
3,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Duplicated contents across configurations for a given name */
public static final Issue DUPLICATES_CONFIGURATIONS = Issue.create(
"IconDuplicatesConfig", //$NON-NLS-1$
"Finds icons that have identical bitmaps across various configuration parameters",
"If an icon is provided under different configuration parameters such as " +
"`drawable-hdpi` or `-v11`, they should typically be different. This detector " +
//Synthetic comment -- @@ -269,13 +279,13 @@
Category.ICONS,
5,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Icons appearing in both -nodpi and a -Ndpi folder */
public static final Issue ICON_NODPI = Issue.create(
"IconNoDpi", //$NON-NLS-1$
            "Finds icons that appear in both a -nodpi folder and a dpi folder",
"Bitmaps that appear in `drawable-nodpi` folders will not be scaled by the " +
"Android framework. If a drawable resource of the same name appears *both* in " +
"a `-nodpi` folder as well as a dpi folder such as `drawable-hdpi`, then " +
//Synthetic comment -- @@ -284,39 +294,39 @@
Category.ICONS,
7,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Icons appearing as both drawable xml files and bitmaps */
public static final Issue ICON_XML_AND_PNG = Issue.create(
"IconXmlAndPng", //$NON-NLS-1$
            "Finds icons that appear both as a drawable .xml file and as bitmaps",
            "If a drawable resource appears as an .xml file in the drawable/ folder, " +
"it's usually not intentional for it to also appear as a bitmap using the " +
"same name; generally you expect the drawable XML file to define states " +
"and each state has a corresponding drawable bitmap.",
Category.ICONS,
7,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Wrong filename according to the format */
public static final Issue ICON_EXTENSION = Issue.create(
"IconExtension", //$NON-NLS-1$
"Checks that the icon file extension matches the actual image format in the file",

            "Ensures that icons have the correct file extension (e.g. a .png file is " +
            "really in the PNG format and not for example a GIF file named .png.)",
Category.ICONS,
3,
Severity.WARNING,
            IconDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Wrong filename according to the format */
public static final Issue ICON_COLORS = Issue.create(
"IconColors", //$NON-NLS-1$
"Checks that icons follow the recommended visual style",

"Notification icons and Action Bar icons should only white and shades of gray. " +
//Synthetic comment -- @@ -329,13 +339,13 @@
Category.ICONS,
6,
Severity.WARNING,
            IconDetector.class,
            ICON_TYPE_SCOPE).setMoreInfo(
"http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

/** Wrong launcher icon shape */
public static final Issue ICON_LAUNCHER_SHAPE = Issue.create(
"IconLauncherShape", //$NON-NLS-1$
"Checks that launcher icons follow the recommended visual style",

"According to the Android Design Guide " +
//Synthetic comment -- @@ -349,8 +359,7 @@
Category.ICONS,
6,
Severity.WARNING,
            IconDetector.class,
            ICON_TYPE_SCOPE).setMoreInfo(
"http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

/** Constructs a new {@link IconDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index 0608de1..bad3ec6 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.SdkInfo;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -60,9 +61,14 @@
*/
public class InefficientWeightDetector extends LayoutDetector {

/** Can a weight be replaced with 0dp instead for better performance? */
public static final Issue INEFFICIENT_WEIGHT = Issue.create(
"InefficientWeight", //$NON-NLS-1$
"Looks for inefficient weight declarations in LinearLayouts",
"When only a single widget in a LinearLayout defines a weight, it is more " +
"efficient to assign a width/height of `0dp` to it since it will absorb all " +
//Synthetic comment -- @@ -71,12 +77,12 @@
Category.PERFORMANCE,
3,
Severity.WARNING,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Are weights nested? */
public static final Issue NESTED_WEIGHTS = Issue.create(
"NestedWeights", //$NON-NLS-1$
"Looks for nested layout weights, which are costly",
"Layout weights require a widget to be measured twice. When a LinearLayout with " +
"non-zero weights is nested inside another LinearLayout with non-zero weights, " +
//Synthetic comment -- @@ -84,12 +90,12 @@
Category.PERFORMANCE,
3,
Severity.WARNING,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Should a LinearLayout set android:baselineAligned? */
public static final Issue BASELINE_WEIGHTS = Issue.create(
"DisableBaselineAlignment", //$NON-NLS-1$
"Looks for LinearLayouts which should set android:baselineAligned=false",
"When a LinearLayout is used to distribute the space proportionally between " +
"nested layouts, the baseline alignment property should be turned off to " +
//Synthetic comment -- @@ -97,12 +103,12 @@
Category.PERFORMANCE,
3,
Severity.WARNING,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Using 0dp on the wrong dimension */
public static final Issue WRONG_0DP = Issue.create(
"Suspicious0dp", //$NON-NLS-1$
"Looks for 0dp as the width in a vertical LinearLayout or as the height in a " +
"horizontal",

//Synthetic comment -- @@ -116,12 +122,12 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Missing explicit orientation */
public static final Issue ORIENTATION = Issue.create(
"Orientation", //$NON-NLS-1$
"Checks that LinearLayouts with multiple children set the orientation",

"The default orientation of a LinearLayout is horizontal. It's pretty easy to "
//Synthetic comment -- @@ -134,8 +140,7 @@
Category.CORRECTNESS,
2,
Severity.ERROR,
            InefficientWeightDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/**
* Map from element to whether that element has a non-zero linear layout








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InvalidPackageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InvalidPackageDetector.java
//Synthetic comment -- index 048f6ae..072ec05 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -39,7 +40,6 @@
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

//Synthetic comment -- @@ -50,7 +50,9 @@
*/
public class InvalidPackageDetector extends Detector implements Detector.ClassScanner {
/** Accessing an invalid package */
    public static final Issue ISSUE = Issue.create("InvalidPackage", //$NON-NLS-1$
"Finds API accesses to APIs that are not supported in Android",

"This check scans through libraries looking for calls to APIs that are not included " +
//Synthetic comment -- @@ -70,8 +72,9 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            InvalidPackageDetector.class,
            EnumSet.of(Scope.JAVA_LIBRARIES));

private static final String JAVA_PKG_PREFIX = "java/";    //$NON-NLS-1$
private static final String JAVAX_PKG_PREFIX = "javax/";  //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index b161699..0dac7dc 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -60,9 +61,15 @@
* drawing operations and using HashMap instead of SparseArray.
*/
public class JavaPerformanceDetector extends Detector implements Detector.JavaScanner {
/** Allocating objects during a paint method */
public static final Issue PAINT_ALLOC = Issue.create(
"DrawAllocation", //$NON-NLS-1$
"Looks for memory allocations within drawing code",

"You should avoid allocating objects during a drawing or layout operation. These " +
//Synthetic comment -- @@ -78,12 +85,12 @@
Category.PERFORMANCE,
9,
Severity.WARNING,
            JavaPerformanceDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Using HashMaps where SparseArray would be better */
    public static final Issue USE_SPARSEARRAY = Issue.create(
"UseSparseArrays", //$NON-NLS-1$
"Looks for opportunities to replace HashMaps with the more efficient SparseArray",

"For maps where the keys are of type integer, it's typically more efficient to " +
//Synthetic comment -- @@ -101,13 +108,13 @@
Category.PERFORMANCE,
4,
Severity.WARNING,
            JavaPerformanceDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Using {@code new Integer()} instead of the more efficient {@code Integer.valueOf} */
    public static final Issue USE_VALUEOF = Issue.create(
"UseValueOf", //$NON-NLS-1$
            "Looks for usages of \"new\" for wrapper classes which should use \"valueOf\" instead",

"You should not call the constructor for wrapper classes directly, such as" +
"`new Integer(42)`. Instead, call the `valueOf` factory method, such as " +
//Synthetic comment -- @@ -117,8 +124,7 @@
Category.PERFORMANCE,
4,
Severity.WARNING,
            JavaPerformanceDetector.class,
            Scope.JAVA_FILE_SCOPE);

static final String ON_MEASURE = "onMeasure";                           //$NON-NLS-1$
static final String ON_DRAW = "onDraw";                                 //$NON-NLS-1$
//Synthetic comment -- @@ -179,8 +185,8 @@
mContext = context;

mCheckAllocations = context.isEnabled(PAINT_ALLOC);
            mCheckMaps = context.isEnabled(USE_SPARSEARRAY);
            mCheckValueOf = context.isEnabled(USE_VALUEOF);
}

@Override
//Synthetic comment -- @@ -220,7 +226,7 @@
&& node.astTypeReference().astParts().size() == 1
&& node.astArguments().size() == 1) {
String argument = node.astArguments().first().toString();
                    mContext.report(USE_VALUEOF, node, mContext.getLocation(node),
String.format("Use %1$s.valueOf(%2$s) instead", typeName, argument),
null);
}
//Synthetic comment -- @@ -492,19 +498,19 @@
if (first.getTypeName().equals(INTEGER)) {
String valueType = types.last().getTypeName();
if (valueType.equals(INTEGER)) {
                        mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseIntArray(...) instead for better performance",
null);
} else if (valueType.equals(BOOLEAN)) {
                        mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseBooleanArray(...) instead for better performance",
null);
} else if (valueType.equals(LONG) && mContext.getProject().getMinSdk() >= 17) {
                        mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseLongArray(...) instead for better performance",
null);
} else {
                        mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
String.format(
"Use new SparseArray<%1$s>(...) instead for better performance",
valueType),
//Synthetic comment -- @@ -521,15 +527,15 @@
TypeReference first = types.first();
String valueType = first.getTypeName();
if (valueType.equals(INTEGER)) {
                    mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseIntArray(...) instead for better performance",
null);
} else if (valueType.equals(BOOLEAN)) {
                    mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseBooleanArray(...) instead for better performance",
null);
} else if (valueType.equals(LONG) && mContext.getProject().getMinSdk() >= 17) {
                    mContext.report(USE_SPARSEARRAY, node, mContext.getLocation(node),
"Use new SparseLongArray(...) instead for better performance",
null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LabelForDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LabelForDetector.java
//Synthetic comment -- index 283e244..5503728 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -57,7 +58,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"LabelFor", //$NON-NLS-1$
            "Ensures that text fields are marked with a labelFor attribute",
"Text fields should be labelled with a `labelFor` attribute, " +
"provided your `minSdkVersion` is at least 17.\n" +
"\n" +
//Synthetic comment -- @@ -66,8 +69,9 @@
Category.A11Y,
2,
Severity.WARNING,
            LabelForDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

private Set<String> mLabels;
private List<Element> mTextFields;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LocaleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LocaleDetector.java
//Synthetic comment -- index 3bde211..3005cb9 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -56,7 +57,8 @@
/** Calling risky convenience methods */
public static final Issue STRING_LOCALE = Issue.create(
"DefaultLocale", //$NON-NLS-1$
            "Finds calls to locale-ambiguous String manipulation methods",

"Calling `String#toLowerCase()` or `#toUpperCase()` *without specifying an " +
"explicit locale* is a common source of bugs. The reason for that is that those " +
//Synthetic comment -- @@ -72,14 +74,17 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            LocaleDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.CLASS_FILE)).setMoreInfo(
             "http://developer.android.com/reference/java/util/Locale.html#default_locale"); //$NON-NLS-1$

/** Constructing SimpleDateFormat without an explicit locale */
public static final Issue DATE_FORMAT = Issue.create(
"SimpleDateFormat", //$NON-NLS-1$
            "Using SimpleDateFormat directly without an explicit locale",

"Almost all callers should use `getDateInstance()`, `getDateTimeInstance()`, or " +
"`getTimeInstance()` to get a ready-made instance of SimpleDateFormat suitable " +
//Synthetic comment -- @@ -95,9 +100,11 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            LocaleDetector.class,
            Scope.CLASS_FILE_SCOPE).setMoreInfo(
             "http://developer.android.com/reference/java/text/SimpleDateFormat.html"); //$NON-NLS-1$

static final String DATE_FORMAT_OWNER = "java/text/SimpleDateFormat"; //$NON-NLS-1$
private static final String STRING_OWNER = "java/lang/String";                //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index 83fac97..046ad56 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -63,11 +64,16 @@
* wrong order.
*/
public class ManifestOrderDetector extends Detector implements Detector.XmlScanner {

/** Wrong order of elements in the manifest */
public static final Issue ORDER = Issue.create(
"ManifestOrder", //$NON-NLS-1$
            "Checks for manifest problems like <uses-sdk> after the <application> tag",
"The <application> tag should appear after the elements which declare " +
"which version you need, which features you need, which libraries you " +
"need, and so on. In the past there have been subtle bugs (such as " +
//Synthetic comment -- @@ -77,12 +83,12 @@
Category.CORRECTNESS,
5,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE);

/** Missing a {@code <uses-sdk>} element */
public static final Issue USES_SDK = Issue.create(
"UsesMinSdkAttributes", //$NON-NLS-1$
"Checks that the minimum SDK and target SDK attributes are defined",

"The manifest should contain a `<uses-sdk>` element which defines the " +
//Synthetic comment -- @@ -93,13 +99,13 @@
Category.CORRECTNESS,
9,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Using a targetSdkVersion that isn't recent */
public static final Issue TARGET_NEWER = Issue.create(
"OldTargetApi", //$NON-NLS-1$
"Checks that the manifest specifies a targetSdkVersion that is recent",

"When your application runs on a version of Android that is more recent than your " +
//Synthetic comment -- @@ -117,14 +123,14 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/reference/android/os/Build.VERSION_CODES.html"); //$NON-NLS-1$

/** Using multiple {@code <uses-sdk>} elements */
public static final Issue MULTIPLE_USES_SDK = Issue.create(
"MultipleUsesSdk", //$NON-NLS-1$
            "Checks that the <uses-sdk> element appears at most once",

"The `<uses-sdk>` element should appear just once; the tools will *not* merge the " +
"contents of all the elements so if you split up the attributes across multiple " +
//Synthetic comment -- @@ -134,13 +140,13 @@
Category.CORRECTNESS,
6,
Severity.FATAL,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */
public static final Issue WRONG_PARENT = Issue.create(
"WrongManifestParent", //$NON-NLS-1$
"Checks that various manifest elements are declared in the right place",

"The `<uses-library>` element should be defined as a direct child of the " +
//Synthetic comment -- @@ -152,13 +158,13 @@
Category.CORRECTNESS,
6,
Severity.FATAL,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/manifest-intro.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */
public static final Issue DUPLICATE_ACTIVITY = Issue.create(
"DuplicateActivity", //$NON-NLS-1$
"Checks that an activity is registered only once in the manifest",

"An activity should only be registered once in the manifest. If it is " +
//Synthetic comment -- @@ -169,12 +175,12 @@
Category.CORRECTNESS,
5,
Severity.ERROR,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE);

/** Not explicitly defining allowBackup */
public static final Issue ALLOW_BACKUP = Issue.create(
"AllowBackup", //$NON-NLS-1$
"Ensure that allowBackup is explicitly set in the application's manifest",

"The allowBackup attribute determines if an application's data can be backed up " +
//Synthetic comment -- @@ -202,13 +208,13 @@
Category.SECURITY,
3,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/reference/android/R.attr.html#allowBackup");

/** Conflicting permission names */
public static final Issue UNIQUE_PERMISSION = Issue.create(
"UniquePermission", //$NON-NLS-1$
"Checks that permission names are unique",

"The unqualified names or your permissions must be unique. The reason for this " +
//Synthetic comment -- @@ -223,12 +229,12 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE);

/** Using a resource for attributes that do not allow it */
public static final Issue SET_VERSION = Issue.create(
"MissingVersion", //$NON-NLS-1$
"Checks that the application name and version are set",

"You should define the version information for your application.\n" +
//Synthetic comment -- @@ -241,13 +247,13 @@
Category.CORRECTNESS,
2,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/tools/publishing/versioning.html#appversioning");

/** Using a resource for attributes that do not allow it */
public static final Issue ILLEGAL_REFERENCE = Issue.create(
"IllegalResourceRef", //$NON-NLS-1$
"Checks for resource references where only literals are allowed",

"For the `versionCode` attribute, you have to specify an actual integer " +
//Synthetic comment -- @@ -258,8 +264,7 @@
Category.CORRECTNESS,
8,
Severity.WARNING,
            ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE);

/** Constructs a new {@link ManifestOrderDetector} check */
public ManifestOrderDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestTypoDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestTypoDetector.java
//Synthetic comment -- index 74b3d81..147e0e9 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -71,6 +72,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ManifestTypo", //$NON-NLS-1$
"Checks for manifest typos",

"This check looks through the manifest, and if it finds any tags " +
//Synthetic comment -- @@ -78,8 +80,9 @@
Category.CORRECTNESS,
5,
Severity.WARNING,
            ManifestTypoDetector.class,
            Scope.MANIFEST_SCOPE);

private static final Set<String> sValidTags;
static {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index 8709852..5c010dc 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -41,21 +42,24 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"FloatMath", //$NON-NLS-1$
            "Suggests replacing android.util.FloatMath calls with java.lang.Math",

            "In older versions of Android, using android.util.FloatMath was recommended " +
"for performance reasons when operating on floats. However, on modern hardware " +
"doubles are just as fast as float (though they take more memory), and in " +
            "recent versions of Android, FloatMath is actually slower than using java.lang.Math " +
            "due to the way the JIT optimizes java.lang.Math. Therefore, you should use " +
            "Math instead of FloatMath if you are only targeting Froyo and above.",

Category.PERFORMANCE,
3,
Severity.WARNING,
            MathDetector.class,
            Scope.CLASS_FILE_SCOPE).setMoreInfo(
               "http://developer.android.com/guide/practices/design/performance.html#avoidfloat"); //$NON-NLS-1$

/** Constructs a new {@link MathDetector} check */
public MathDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index 1b76c03..baee367 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -86,7 +87,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"MergeRootFrame", //$NON-NLS-1$
            "Checks whether a root <FrameLayout> can be replaced with a <merge> tag",
"If a `<FrameLayout>` is the root of a layout and does not provide background " +
"or padding etc, it can often be replaced with a `<merge>` tag which is slightly " +
"more efficient. Note that this depends on context, so make sure you understand " +
//Synthetic comment -- @@ -94,8 +97,10 @@
Category.PERFORMANCE,
4,
Severity.WARNING,
            MergeRootFrameLayoutDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.JAVA_FILE)).setMoreInfo(
"http://android-developers.blogspot.com/2009/03/android-layout-tricks-3-optimize-by.html"); //$NON-NLS-1$

/** Constructs a new {@link MergeRootFrameLayoutDetector} */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 8002c40..90a983b 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -73,54 +74,61 @@
public class MissingClassDetector extends LayoutDetector implements ClassScanner {
/** Manifest-referenced classes missing from the project or libraries */
public static final Issue MISSING = Issue.create(
        "MissingRegistered", //$NON-NLS-1$
        "Ensures that classes referenced in the manifest are present in the project or libraries",

        "If a class is referenced in the manifest, it must also exist in the project (or in one " +
        "of the libraries included by the project. This check helps uncover typos in " +
        "registration names, or attempts to rename or move classes without updating the " +
        "manifest file properly.",

        Category.CORRECTNESS,
        8,
        Severity.ERROR,
        MissingClassDetector.class,
        EnumSet.of(Scope.MANIFEST, Scope.CLASS_FILE, Scope.JAVA_LIBRARIES, Scope.RESOURCE_FILE))
        .setMoreInfo("http://developer.android.com/guide/topics/manifest/manifest-intro.html"); //$NON-NLS-1$

/** Are activity, service, receiver etc subclasses instantiatable? */
public static final Issue INSTANTIATABLE = Issue.create(
        "Instantiatable", //$NON-NLS-1$
        "Ensures that classes registered in the manifest file are instantiatable",

        "Activities, services, broadcast receivers etc. registered in the manifest file " +
        "must be \"instantiatable\" by the system, which means that the class must be " +
        "public, it must have an empty public constructor, and if it's an inner class, " +
        "it must be a static inner class.",

        Category.CORRECTNESS,
        6,
        Severity.WARNING,
        MissingClassDetector.class,
        Scope.CLASS_FILE_SCOPE);

/** Is the right character used for inner class separators? */
public static final Issue INNERCLASS = Issue.create(
        "InnerclassSeparator", //$NON-NLS-1$
        "Ensures that inner classes are referenced using '$' instead of '.' in class names",

        "When you reference an inner class in a manifest file, you must use '$' instead of '.' " +
        "as the separator character, i.e. Outer$Inner instead of Outer.Inner.\n" +
        "\n" +
        "(If you get this warning for a class which is not actually an inner class, it's " +
        "because you are using uppercase characters in your package name, which is not " +
        "conventional.)",

        Category.CORRECTNESS,
        3,
        Severity.WARNING,
        MissingClassDetector.class,
        Scope.MANIFEST_SCOPE);

private Map<String, Location.Handle> mReferencedClasses;
private Set<String> mCustomViews;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingIdDetector.java
//Synthetic comment -- index 1b79600..fac6789 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -42,7 +43,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"MissingId", //$NON-NLS-1$
            "Ensures that XML tags like <fragment> specify an id or tag attribute",

"If you do not specify an android:id or an android:tag attribute on a " +
"<fragment> element, then if the activity is restarted (for example for " +
//Synthetic comment -- @@ -60,9 +62,10 @@
Category.CORRECTNESS,
5,
Severity.WARNING,
            MissingIdDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo("http://developer.android.com/guide/components/fragments.html"); //$NON-NLS-1$

/** Constructs a new {@link MissingIdDetector} */
public MissingIdDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NamespaceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NamespaceDetector.java
//Synthetic comment -- index 0b6ab02..ab46545 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -45,9 +46,15 @@
* Checks for various issues related to XML namespaces
*/
public class NamespaceDetector extends LayoutDetector {
/** Typos in the namespace */
public static final Issue TYPO = Issue.create(
"NamespaceTypo", //$NON-NLS-1$
"Looks for misspellings in namespace declarations",

"Accidental misspellings in namespace declarations can lead to some very " +
//Synthetic comment -- @@ -56,12 +63,12 @@
Category.CORRECTNESS,
8,
Severity.WARNING,
            NamespaceDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Unused namespace declarations */
public static final Issue UNUSED = Issue.create(
"UnusedNamespace", //$NON-NLS-1$
"Finds unused namespaces in XML documents",

"Unused namespace declarations take up space and require processing that is not " +
//Synthetic comment -- @@ -70,12 +77,12 @@
Category.PERFORMANCE,
1,
Severity.WARNING,
            NamespaceDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Using custom namespace attributes in a library project */
    public static final Issue CUSTOMVIEW = Issue.create(
"LibraryCustomView", //$NON-NLS-1$
"Flags custom attributes in libraries, which must use the res-auto-namespace instead",

"When using a custom view with custom attributes in a library project, the layout " +
//Synthetic comment -- @@ -86,8 +93,7 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            NamespaceDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Prefix relevant for custom namespaces */
private static final String XMLNS_ANDROID = "xmlns:android";                    //$NON-NLS-1$
//Synthetic comment -- @@ -96,7 +102,7 @@
private Map<String, Attr> mUnusedNamespaces;
private boolean mCheckUnused;

  /** Constructs a new {@link NamespaceDetector} */
public NamespaceDetector() {
}

//Synthetic comment -- @@ -184,7 +190,8 @@
}

if (haveCustomNamespace) {
          boolean checkCustomAttrs = context.isEnabled(CUSTOMVIEW) && context.getProject().isLibrary();
mCheckUnused = context.isEnabled(UNUSED);

if (checkCustomAttrs) {
//Synthetic comment -- @@ -211,7 +218,7 @@
String uri = attribute.getValue();
if (uri != null && !uri.isEmpty() && uri.startsWith(URI_PREFIX)
&& !uri.equals(ANDROID_URI)) {
                    context.report(CUSTOMVIEW, attribute, context.getLocation(attribute),
"When using a custom namespace attribute in a library project, " +
"use the namespace \"" + AUTO_URI + "\" instead.", null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java
//Synthetic comment -- index 4650a8f..c09be48 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -48,6 +49,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"NestedScrolling", //$NON-NLS-1$
"Checks whether a scrolling widget has any nested scrolling widgets within",
// TODO: Better description!
"A scrolling widget such as a `ScrollView` should not contain any nested " +
//Synthetic comment -- @@ -55,8 +57,9 @@
Category.CORRECTNESS,
7,
Severity.WARNING,
            NestedScrollingWidgetDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link NestedScrollingWidgetDetector} */
public NestedScrollingWidgetDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
//Synthetic comment -- index e970572..8487706 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -41,6 +42,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"UnlocalizedSms", //$NON-NLS-1$
"Looks for code sending text messages to unlocalized phone numbers",

"SMS destination numbers must start with a country code or the application code " +
//Synthetic comment -- @@ -50,8 +52,9 @@
Category.CORRECTNESS,
5,
Severity.WARNING,
            NonInternationalizedSmsDetector.class,
            Scope.JAVA_FILE_SCOPE);


/** Constructs a new {@link NonInternationalizedSmsDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index 559a7ad..e0e40cb 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import com.android.tools.lint.client.api.SdkInfo;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -101,7 +102,9 @@
/** Usage of deprecated views or attributes */
public static final Issue ISSUE = Issue.create(
"ObsoleteLayoutParam", //$NON-NLS-1$
"Looks for layout params that are not valid for the given parent layout",
"The given layout_param is not defined for the given layout, meaning it has no " +
"effect. This usually happens when you change the parent layout or move view " +
"code around without updating the layout params. This will cause useless " +
//Synthetic comment -- @@ -110,8 +113,9 @@
Category.PERFORMANCE,
6,
Severity.WARNING,
            ObsoleteLayoutParamsDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/**
* Set of layout parameter names that are considered valid no matter what so








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 2a07a86..549db6f 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -45,7 +46,6 @@
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -59,7 +59,8 @@
/** Missing onClick handlers */
public static final Issue ISSUE = Issue.create(
"OnClick", //$NON-NLS-1$
            "Ensures that onClick attribute values refer to real methods",

"The `onClick` attribute value should be the name of a method in this View's context " +
"to invoke when the view is clicked. This name must correspond to a public method " +
//Synthetic comment -- @@ -70,8 +71,9 @@
Category.CORRECTNESS,
10,
Severity.ERROR,
            OnClickDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.CLASS_FILE));

private Map<String, Location.Handle> mNames;
private Map<String, List<String>> mSimilar;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index aedc355..c657f30 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -91,7 +92,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"Overdraw", //$NON-NLS-1$
"Looks for overdraw issues (where a view is painted only to be fully painted over)",
"If you set a background drawable on a root view, then you should use a " +
"custom theme where the theme background is null. Otherwise, the theme background " +
"will be painted first, only to have your custom background completely cover it; " +
//Synthetic comment -- @@ -115,8 +118,9 @@
Category.PERFORMANCE,
3,
Severity.WARNING,
            OverdrawDetector.class,
            EnumSet.of(Scope.MANIFEST, Scope.JAVA_FILE, Scope.ALL_RESOURCE_FILES));

/** Mapping from FQN activity names to theme names registered in the manifest */
private Map<String, String> mActivityToTheme;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverrideDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverrideDetector.java
//Synthetic comment -- index 15e2245..2c93419 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -54,6 +55,7 @@
/** Accidental overrides */
public static final Issue ISSUE = Issue.create(
"DalvikOverride", //$NON-NLS-1$
"Looks for methods treated as overrides by Dalvik",

"The Android virtual machine will treat a package private method in one " +
//Synthetic comment -- @@ -70,8 +72,9 @@
Category.CORRECTNESS,
7,
Severity.ERROR,
            OverrideDetector.class,
            EnumSet.of(Scope.ALL_CLASS_FILES));

/** map from owner class name to JVM signatures for its package private methods  */
private final Map<String, Set<String>> mPackagePrivateMethods = Maps.newHashMap();
//Synthetic comment -- @@ -171,7 +174,7 @@
}

if (mErrors != null) {
                context.requestRepeat(this, ISSUE.getScope());
}
} else {
assert context.getPhase() == 2;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index e8887a5..fea7cd9 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -40,6 +41,7 @@
/** Packaged private key files */
public static final Issue ISSUE = Issue.create(
"PackagedPrivateKey", //$NON-NLS-1$
"Looks for packaged private key files",

"In general, you should not package private key files inside your app.",
//Synthetic comment -- @@ -47,8 +49,7 @@
Category.SECURITY,
8,
Severity.WARNING,
            PrivateKeyDetector.class,
            Scope.OTHER_SCOPE);

/** Constructs a new {@link PrivateKeyDetector} check */
public PrivateKeyDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateResourceDetector.java
//Synthetic comment -- index d8a4ce0b..6fa7955 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -36,7 +37,9 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"PrivateResource", //$NON-NLS-1$
"Looks for references to private resources",
"Private resources should not be referenced; the may not be present everywhere, and " +
"even where they are they may disappear without notice.\n" +
"\n" +
//Synthetic comment -- @@ -45,8 +48,9 @@
Category.CORRECTNESS,
3,
Severity.FATAL,
            PrivateResourceDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new detector */
public PrivateResourceDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ProguardDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ProguardDetector.java
//Synthetic comment -- index 7762659..a68b7ca 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -30,16 +31,19 @@
import com.android.tools.lint.detector.api.Speed;

import java.io.File;
import java.util.EnumSet;

/**
* Check which looks for errors in Proguard files.
*/
public class ProguardDetector extends Detector {

/** The main issue discovered by this detector */
    public static final Issue WRONGKEEP = Issue.create(
"Proguard", //$NON-NLS-1$
"Looks for problems in proguard config files",
"Using `-keepclasseswithmembernames` in a proguard config file is not " +
"correct; it can cause some symbols to be renamed which should not be.\n" +
//Synthetic comment -- @@ -51,14 +55,15 @@
Category.CORRECTNESS,
8,
Severity.FATAL,
            ProguardDetector.class,
            EnumSet.of(Scope.PROGUARD_FILE)).setMoreInfo(
            "http://http://code.google.com/p/android/issues/detail?id=16384"); //$NON-NLS-1$

/** Finds ProGuard files that contain non-project specific configuration
* locally and suggests replacing it with an include path */
    public static final Issue SPLITCONFIG = Issue.create(
"ProguardSplit", //$NON-NLS-1$
"Checks for old proguard.cfg files that contain generic Android rules",

"Earlier versions of the Android tools bundled a single `proguard.cfg` file " +
//Synthetic comment -- @@ -88,26 +93,25 @@
Category.CORRECTNESS,
3,
Severity.WARNING,
            ProguardDetector.class,
            EnumSet.of(Scope.PROGUARD_FILE));

@Override
public void run(@NonNull Context context) {
String contents = context.getContents();
if (contents != null) {
            if (context.isEnabled(WRONGKEEP)) {
int index = contents.indexOf(
// Old pattern:
"-keepclasseswithmembernames class * {\n" + //$NON-NLS-1$
"    public <init>(android.");              //$NON-NLS-1$
if (index != -1) {
                    context.report(WRONGKEEP,
Location.create(context.file, contents, index, index),
"Obsolete ProGuard file; use -keepclasseswithmembers instead of " +
"-keepclasseswithmembernames", null);
}
}
            if (context.isEnabled(SPLITCONFIG)) {
int index = contents.indexOf("-keep public class * extends android.app.Activity");
if (index != -1) {
// Only complain if project.properties actually references this file;
//Synthetic comment -- @@ -136,7 +140,7 @@
}
}
if (properties.contains(PROGUARD_CONFIG)) {
                        context.report(SPLITCONFIG,
Location.create(context.file, contents, index, index),
String.format(
"Local ProGuard configuration contains general Android " +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index ed9447e..291193e 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.annotations.Nullable;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -52,10 +53,15 @@
* Also look for non-"sp" text sizes.
*/
public class PxUsageDetector extends LayoutDetector {
/** Using px instead of dp */
public static final Issue PX_ISSUE = Issue.create(
"PxUsage", //$NON-NLS-1$
            "Looks for use of the \"px\" dimension",
// This description is from the below screen support document
"For performance reasons and to keep the code simpler, the Android system uses pixels " +
"as the standard unit for expressing dimension or coordinate values. That means that " +
//Synthetic comment -- @@ -69,14 +75,15 @@
Category.CORRECTNESS,
2,
Severity.WARNING,
            PxUsageDetector.class,
            Scope.RESOURCE_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/guide/practices/screens_support.html#screen-independence"); //$NON-NLS-1$

/** Using mm/in instead of dp */
public static final Issue IN_MM_ISSUE = Issue.create(
"InOrMmUsage", //$NON-NLS-1$
            "Looks for use of the \"mm\" or \"in\" dimensions",

"Avoid using `mm` (millimeters) or `in` (inches) as the unit for dimensions.\n" +
"\n" +
//Synthetic comment -- @@ -87,13 +94,13 @@
Category.CORRECTNESS,
4,
Severity.WARNING,
            PxUsageDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Using sp instead of dp */
public static final Issue DP_ISSUE = Issue.create(
"SpUsage", //$NON-NLS-1$
            "Looks for uses of \"dp\" instead of \"sp\" dimensions for text sizes",

"When setting text sizes, you should normally use `sp`, or \"scale-independent " +
"pixels\". This is like the `dp` unit, but it is also scaled " +
//Synthetic comment -- @@ -109,13 +116,14 @@
Category.CORRECTNESS,
3,
Severity.WARNING,
            PxUsageDetector.class,
            Scope.RESOURCE_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/training/multiscreen/screendensities.html"); //$NON-NLS-1$

/** Using text sizes that are too small */
public static final Issue SMALL_SP_ISSUE = Issue.create(
"SmallSp", //$NON-NLS-1$
"Looks for text sizes that are too small",

"Avoid using sizes smaller than 12sp.",
//Synthetic comment -- @@ -123,8 +131,7 @@
Category.USABILITY,
4,
Severity.WARNING,
            PxUsageDetector.class,
            Scope.RESOURCE_FILE_SCOPE);


/** Constructs a new {@link PxUsageDetector} */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 5b5a321..63dd2c6 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -58,21 +59,25 @@
public class RegistrationDetector extends LayoutDetector implements ClassScanner {
/** Unregistered activities and services */
public static final Issue ISSUE = Issue.create(
        "Registered", //$NON-NLS-1$
        "Ensures that Activities, Services and Content Providers are registered in the manifest",

        "Activities, services and content providers should be registered in the " +
        "`AndroidManifest.xml` file using `<activity>`, `<service>` and `<provider>` tags.\n" +
        "\n" +
        "If your activity is simply a parent class intended to be subclassed by other " +
        "\"real\" activities, make it an abstract class.",

        Category.CORRECTNESS,
        6,
        Severity.WARNING,
        RegistrationDetector.class,
        EnumSet.of(Scope.MANIFEST, Scope.CLASS_FILE)).setMoreInfo(
        "http://developer.android.com/guide/topics/manifest/manifest-intro.html"); //$NON-NLS-1$

protected Multimap<String, String> mManifestRegistrations;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index 861c1be..75b9f02 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -85,9 +86,10 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"RequiredSize", //$NON-NLS-1$
            "Ensures that the layout_width and layout_height are specified for all views",

            "All views must specify an explicit layout_width and layout_height attribute. " +
"There is a runtime check for this, so if you fail to specify a size, an exception " +
"is thrown at runtime.\n" +
"\n" +
//Synthetic comment -- @@ -96,8 +98,9 @@
Category.CORRECTNESS,
4,
Severity.ERROR,
            RequiredAttributeDetector.class,
            EnumSet.of(Scope.JAVA_FILE, Scope.ALL_RESOURCE_FILES));

/** Map from each style name to parent style */
@Nullable private Map<String, String> mStyleParents;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ScrollViewChildDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ScrollViewChildDetector.java
//Synthetic comment -- index 81bb522..5e1fda9 100644

//Synthetic comment -- @@ -26,6 +26,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -49,7 +50,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ScrollViewSize", //$NON-NLS-1$
            "Checks that ScrollViews use wrap_content in scrolling dimension",
// TODO add a better explanation here!
"ScrollView children must set their `layout_width` or `layout_height` attributes " +
"to `wrap_content` rather than `fill_parent` or `match_parent` in the scrolling " +
//Synthetic comment -- @@ -57,8 +59,9 @@
Category.CORRECTNESS,
7,
Severity.WARNING,
            ScrollViewChildDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link ScrollViewChildDetector} */
public ScrollViewChildDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SdCardDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SdCardDetector.java
//Synthetic comment -- index c876b96..784ec02 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -43,7 +44,8 @@
/** Hardcoded /sdcard/ references */
public static final Issue ISSUE = Issue.create(
"SdCardPath", //$NON-NLS-1$
            "Looks for hardcoded references to /sdcard",

"Your code should not reference the `/sdcard` path directly; instead use " +
"`Environment.getExternalStorageDirectory().getPath()`.\n" +
//Synthetic comment -- @@ -55,8 +57,10 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            SdCardDetector.class,
            Scope.JAVA_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/data/data-storage.html#filesExternal"); //$NON-NLS-1$

/** Constructs a new {@link SdCardDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecureRandomDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecureRandomDetector.java
//Synthetic comment -- index f1d349b..33af4a0 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -49,18 +50,20 @@
public class SecureRandomDetector extends Detector implements ClassScanner {
/** Unregistered activities and services */
public static final Issue ISSUE = Issue.create(
        "SecureRandom", //$NON-NLS-1$
        "Looks for suspicious usage of the SecureRandom class",

        "Specifying a fixed seed will cause the instance to return a predictable sequence " +
        "of numbers. This may be useful for testing but it is not appropriate for secure use.",

        Category.PERFORMANCE,
        9,
        Severity.WARNING,
        SecureRandomDetector.class,
        Scope.CLASS_FILE_SCOPE).
        setMoreInfo("http://developer.android.com/reference/java/security/SecureRandom.html");

private static final String SET_SEED = "setSeed"; //$NON-NLS-1$
private static final String OWNER_SECURE_RANDOM = "java/security/SecureRandom"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 6dd651d..b1ba6c9 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -73,9 +74,18 @@
public class SecurityDetector extends Detector implements Detector.XmlScanner,
Detector.JavaScanner {

/** Exported services */
public static final Issue EXPORTED_SERVICE = Issue.create(
"ExportedService", //$NON-NLS-1$
"Checks for exported services that do not require permissions",
"Exported services (services which either set `exported=true` or contain " +
"an intent-filter and do not specify `exported=false`) should define a " +
//Synthetic comment -- @@ -84,12 +94,12 @@
Category.SECURITY,
5,
Severity.WARNING,
            SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Exported content providers */
public static final Issue EXPORTED_PROVIDER = Issue.create(
"ExportedContentProvider", //$NON-NLS-1$
"Checks for exported content providers that do not require permissions",
"Content providers are exported by default and any application on the " +
"system can potentially use them to read and write data. If the content " +
//Synthetic comment -- @@ -99,12 +109,12 @@
Category.SECURITY,
5,
Severity.WARNING,
            SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Exported receivers */
public static final Issue EXPORTED_RECEIVER = Issue.create(
"ExportedReceiver", //$NON-NLS-1$
"Checks for exported receivers that do not require permissions",
"Exported receivers (receivers which either set `exported=true` or contain " +
"an intent-filter and do not specify `exported=false`) should define a " +
//Synthetic comment -- @@ -113,27 +123,27 @@
Category.SECURITY,
5,
Severity.WARNING,
            SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Content provides which grant all URIs access */
public static final Issue OPEN_PROVIDER = Issue.create(
"GrantAllUris", //$NON-NLS-1$
            "Checks for <grant-uri-permission> elements where everything is shared",
"The `<grant-uri-permission>` element allows specific paths to be shared. " +
"This detector checks for a path URL of just '/' (everything), which is " +
"probably not what you want; you should limit access to a subset.",
Category.SECURITY,
7,
Severity.WARNING,
            SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Using the world-writable flag */
public static final Issue WORLD_WRITEABLE = Issue.create(
"WorldWriteableFiles", //$NON-NLS-1$
            "Checks for openFileOutput() and getSharedPreferences() calls passing " +
            "MODE_WORLD_WRITEABLE",
"There are cases where it is appropriate for an application to write " +
"world writeable files, but these should be reviewed carefully to " +
"ensure that they contain no private data, and that if the file is " +
//Synthetic comment -- @@ -142,15 +152,15 @@
Category.SECURITY,
4,
Severity.WARNING,
            SecurityDetector.class,
            Scope.JAVA_FILE_SCOPE);


/** Using the world-readable flag */
public static final Issue WORLD_READABLE = Issue.create(
"WorldReadableFiles", //$NON-NLS-1$
            "Checks for openFileOutput() and getSharedPreferences() calls passing " +
            "MODE_WORLD_READABLE",
"There are cases where it is appropriate for an application to write " +
"world readable files, but these should be reviewed carefully to " +
"ensure that they contain no private data that is leaked to other " +
//Synthetic comment -- @@ -158,8 +168,7 @@
Category.SECURITY,
4,
Severity.WARNING,
            SecurityDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Constructs a new {@link SecurityDetector} check */
public SecurityDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java
//Synthetic comment -- index 4b4ba01..646f2ce 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -37,6 +38,7 @@
public class SetJavaScriptEnabledDetector extends Detector implements Detector.JavaScanner {
/** Invocations of setJavaScriptEnabled */
public static final Issue ISSUE = Issue.create("SetJavaScriptEnabled", //$NON-NLS-1$
"Looks for invocations of android.webkit.WebSettings.setJavaScriptEnabled",

"Your code should not invoke `setJavaScriptEnabled` if you are not sure that " +
//Synthetic comment -- @@ -45,8 +47,10 @@
Category.SECURITY,
6,
Severity.WARNING,
            SetJavaScriptEnabledDetector.class,
            Scope.JAVA_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/guide/practices/security.html"); //$NON-NLS-1$

/** Constructs a new {@link SetJavaScriptEnabledDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 76887a5..c265142 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -52,7 +53,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"CommitPrefEdits", //$NON-NLS-1$
            "Looks for code editing a SharedPreference but forgetting to call commit() on it",

"After calling `edit()` on a `SharedPreference`, you must call `commit()` " +
"or `apply()` on the editor to save the results.",
//Synthetic comment -- @@ -60,8 +62,9 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            SharedPrefsDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Constructs a new {@link SharedPrefsDetector} check */
public SharedPrefsDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StateListDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StateListDetector.java
//Synthetic comment -- index c0dea30..013be69 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -48,15 +49,17 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"StateListReachable", //$NON-NLS-1$
            "Looks for unreachable states in a <selector>",
"In a selector, only the last child in the state list should omit a " +
"state qualifier. If not, all subsequent items in the list will be ignored " +
"since the given item will match all.",
Category.CORRECTNESS,
5,
Severity.WARNING,
            StateListDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

private static final String STATE_PREFIX = "state_"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index dd97482..480e61b 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -87,9 +88,20 @@
* TODO: Handle Resources.getQuantityString as well
*/
public class StringFormatDetector extends ResourceXmlDetector implements Detector.JavaScanner {
/** Whether formatting strings are invalid */
public static final Issue INVALID = Issue.create(
"StringFormatInvalid", //$NON-NLS-1$
"Checks that format strings are valid",

"If a string contains a '%' character, then the string may be a formatting string " +
//Synthetic comment -- @@ -112,12 +124,12 @@
Category.MESSAGES,
9,
Severity.ERROR,
            StringFormatDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Whether formatting argument types are consistent across translations */
public static final Issue ARG_COUNT = Issue.create(
"StringFormatCount", //$NON-NLS-1$
"Ensures that all format strings are used and that the same number is defined "
+ "across translations",

//Synthetic comment -- @@ -128,14 +140,14 @@
Category.MESSAGES,
5,
Severity.WARNING,
            StringFormatDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Whether the string format supplied in a call to String.format matches the format string */
public static final Issue ARG_TYPES = Issue.create(
"StringFormatMatches", //$NON-NLS-1$
            "Ensures that the format used in <string> definitions is compatible with the "
                + "String.format call",

"This lint check ensures the following:\n" +
"(1) If there are multiple translations of the format string, then all translations " +
//Synthetic comment -- @@ -146,8 +158,7 @@
Category.MESSAGES,
9,
Severity.ERROR,
            StringFormatDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.JAVA_FILE));

/**
* Map from a format string name to a list of declaration file and actual








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StyleCycleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StyleCycleDetector.java
//Synthetic comment -- index 8ce103b..4c316bd 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -44,14 +45,17 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"StyleCycle", //$NON-NLS-1$
"Looks for cycles in style definitions",
"There should be no cycles in style definitions as this can lead to runtime " +
"exceptions.",
Category.CORRECTNESS,
8,
Severity.FATAL,
            StyleCycleDetector.class,
            Scope.RESOURCE_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/ui/themes.html#Inheritance"); //$NON-NLS-1$

/** Constructs a new {@link StyleCycleDetector} */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SystemPermissionsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SystemPermissionsDetector.java
//Synthetic comment -- index 21fb6c0..2d3a711 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -48,6 +49,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ProtectedPermissions", //$NON-NLS-1$
"Looks for permissions that are only granted to system apps",

"Permissions with the protection level signature or signatureOrSystem are only " +
//Synthetic comment -- @@ -57,8 +59,9 @@
Category.CORRECTNESS,
5,
Severity.ERROR,
            SystemPermissionsDetector.class,
            EnumSet.of(Scope.MANIFEST));

// List of permissions have the protection levels signature or systemOrSignature.
// This list must be sorted alphabetically.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextFieldDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextFieldDetector.java
//Synthetic comment -- index a059f5c..55cb407 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -52,7 +53,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"TextFields", //$NON-NLS-1$
            "Looks for text fields missing inputType or hint settings",

"Providing an `inputType` attribute on a text field improves usability " +
"because depending on the data to be input, optimized keyboards can be shown " +
//Synthetic comment -- @@ -71,8 +73,9 @@
Category.USABILITY,
5,
Severity.WARNING,
            TextFieldDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link TextFieldDetector} */
public TextFieldDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextViewDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextViewDetector.java
//Synthetic comment -- index 3e519cf..4e4d7f3 100644

//Synthetic comment -- @@ -50,6 +50,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -69,9 +70,15 @@
* Checks for cases where a TextView should probably be an EditText instead
*/
public class TextViewDetector extends LayoutDetector {
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"TextViewEdits", //$NON-NLS-1$
"Looks for TextViews being used for input",

"Using a `<TextView>` to input text is generally an error, you should be " +
//Synthetic comment -- @@ -88,12 +95,12 @@
Category.CORRECTNESS,
7,
Severity.WARNING,
            TextViewDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Text could be selectable */
public static final Issue SELECTABLE = Issue.create(
"SelectableText", //$NON-NLS-1$
"Looks for TextViews which should probably allow their text to be selected",

"If a `<TextView>` is used to display data, the user might want to copy that " +
//Synthetic comment -- @@ -107,8 +114,7 @@
Category.USABILITY,
7,
Severity.WARNING,
            TextViewDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link TextViewDetector} */
public TextViewDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TitleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TitleDetector.java
//Synthetic comment -- index 5fc5340..6bbbda6 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -46,6 +47,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"MenuTitle", //$NON-NLS-1$
"Ensures that all menu items supply a title",

"From the action bar documentation:\n" +
//Synthetic comment -- @@ -64,8 +66,10 @@
Category.USABILITY,
5,
Severity.WARNING,
            TitleDetector.class,
            Scope.RESOURCE_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/ui/actionbar.html"); //$NON-NLS-1$

/** Constructs a new {@link TitleDetector} */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index 9bc7af5..f650b23 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -44,7 +45,8 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ShowToast", //$NON-NLS-1$
            "Looks for code creating a Toast but forgetting to call show() on it",

"`Toast.makeText()` creates a `Toast` but does *not* show it. You must call " +
"`show()` on the resulting object to actually make the `Toast` appear.",
//Synthetic comment -- @@ -52,8 +54,9 @@
Category.CORRECTNESS,
6,
Severity.WARNING,
            ToastDetector.class,
            Scope.JAVA_FILE_SCOPE);


/** Constructs a new {@link ToastDetector} check */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TooManyViewsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TooManyViewsDetector.java
//Synthetic comment -- index 94a9611..d0f2daa 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -34,9 +35,15 @@
* Checks whether a root FrameLayout can be replaced with a {@code <merge>} tag.
*/
public class TooManyViewsDetector extends LayoutDetector {
/** Issue of having too many views in a single layout */
public static final Issue TOO_MANY = Issue.create(
"TooManyViews", //$NON-NLS-1$
"Checks whether a layout has too many views",
"Using too many views in a single layout is bad for " +
"performance. Consider using compound drawables or other tricks for " +
//Synthetic comment -- @@ -46,12 +53,12 @@
Category.PERFORMANCE,
1,
Severity.WARNING,
            TooManyViewsDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Issue of having too deep hierarchies in layouts */
public static final Issue TOO_DEEP = Issue.create(
"TooDeepLayout", //$NON-NLS-1$
"Checks whether a layout hierarchy is too deep",
"Layouts with too much nesting is bad for performance. " +
"Consider using a flatter layout (such as `RelativeLayout` or `GridLayout`)." +
//Synthetic comment -- @@ -60,8 +67,7 @@
Category.PERFORMANCE,
1,
Severity.WARNING,
            TooManyViewsDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

private static final int MAX_VIEW_COUNT;
private static final int MAX_DEPTH;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 0f892b1..d33ea88 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -70,9 +71,14 @@
private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[a-z]{2}$"); //$NON-NLS-1$
private static final Pattern REGION_PATTERN = Pattern.compile("^r([A-Z]{2})$"); //$NON-NLS-1$

/** Are all translations complete? */
public static final Issue MISSING = Issue.create(
"MissingTranslation", //$NON-NLS-1$
"Checks for incomplete translations where not all strings are translated",
"If an application has more than one locale, then all the strings declared in " +
"one language should also be translated in all other languages.\n" +
//Synthetic comment -- @@ -95,12 +101,12 @@
Category.MESSAGES,
8,
Severity.FATAL,
            TranslationDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Are there extra translations that are "unused" (appear only in specific languages) ? */
public static final Issue EXTRA = Issue.create(
"ExtraTranslation", //$NON-NLS-1$
"Checks for translations that appear to be unused (no default language string)",
"If a string appears in a specific language translation file, but there is " +
"no corresponding string in the default locale, then this string is probably " +
//Synthetic comment -- @@ -112,8 +118,7 @@
Category.MESSAGES,
6,
Severity.FATAL,
            TranslationDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

private Set<String> mNames;
private Set<String> mTranslatedArrays;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoDetector.java
//Synthetic comment -- index b9b889f..20c1420 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -88,6 +89,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"Typos", //$NON-NLS-1$
"Looks for typos in messages",

"This check looks through the string definitions, and if it finds any words " +
//Synthetic comment -- @@ -95,8 +97,9 @@
Category.MESSAGES,
7,
Severity.WARNING,
            TypoDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new detector */
public TypoDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index ae5f24c..90b1f65 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -47,9 +48,15 @@
* Checks for various typographical issues in string definitions.
*/
public class TypographyDetector extends ResourceXmlDetector {
/** Replace hyphens with dashes? */
public static final Issue DASHES = Issue.create(
"TypographyDashes", //$NON-NLS-1$
"Looks for usages of hyphens which can be replaced by n dash and m dash characters",
"The \"n dash\" (\u2013, &#8211;) and the \"m dash\" (\u2014, &#8212;) " +
"characters are used for ranges (n dash) and breaks (m dash). Using these " +
//Synthetic comment -- @@ -58,13 +65,13 @@
Category.TYPOGRAPHY,
5,
Severity.WARNING,
            TypographyDetector.class,
            Scope.RESOURCE_FILE_SCOPE).
            setMoreInfo("http://en.wikipedia.org/wiki/Dash"); //$NON-NLS-1$

/** Replace dumb quotes with smart quotes? */
public static final Issue QUOTES = Issue.create(
"TypographyQuotes", //$NON-NLS-1$
"Looks for straight quotes which can be replaced by curvy quotes",
"Straight single quotes and double quotes, when used as a pair, can be replaced " +
"by \"curvy quotes\" (or directional quotes). This can make the text more " +
//Synthetic comment -- @@ -77,9 +84,8 @@
Category.TYPOGRAPHY,
5,
Severity.WARNING,
            TypographyDetector.class,
            Scope.RESOURCE_FILE_SCOPE).
            setMoreInfo("http://en.wikipedia.org/wiki/Quotation_mark"). //$NON-NLS-1$
// This feature is apparently controversial: recent apps have started using
// straight quotes to avoid inconsistencies. Disabled by default for now.
setEnabledByDefault(false);
//Synthetic comment -- @@ -87,6 +93,7 @@
/** Replace fraction strings with fraction characters? */
public static final Issue FRACTIONS = Issue.create(
"TypographyFractions", //$NON-NLS-1$
"Looks for fraction strings which can be replaced with a fraction character",
"You can replace certain strings, such as 1/2, and 1/4, with dedicated " +
"characters for these, such as \u00BD (&#189;) and \00BC (&#188;). " +
//Synthetic comment -- @@ -94,26 +101,26 @@
Category.TYPOGRAPHY,
5,
Severity.WARNING,
            TypographyDetector.class,
            Scope.RESOURCE_FILE_SCOPE).
            setMoreInfo("http://en.wikipedia.org/wiki/Number_Forms"); //$NON-NLS-1$

/** Replace ... with the ellipsis character? */
public static final Issue ELLIPSIS = Issue.create(
"TypographyEllipsis", //$NON-NLS-1$
"Looks for ellipsis strings (...) which can be replaced with an ellipsis character",
"You can replace the string \"...\" with a dedicated ellipsis character, " +
"ellipsis character (\u2026, &#8230;). This can help make the text more readable.",
Category.TYPOGRAPHY,
5,
Severity.WARNING,
            TypographyDetector.class,
            Scope.RESOURCE_FILE_SCOPE).
            setMoreInfo("http://en.wikipedia.org/wiki/Ellipsis"); //$NON-NLS-1$

/** The main issue discovered by this detector */
public static final Issue OTHER = Issue.create(
"TypographyOther", //$NON-NLS-1$
"Looks for miscellaneous typographical problems like replacing (c) with \u00A9",
"This check looks for miscellaneous typographical problems and offers replacement " +
"sequences that will make the text easier to read and your application more " +
//Synthetic comment -- @@ -121,8 +128,7 @@
Category.TYPOGRAPHY,
3,
Severity.WARNING,
            TypographyDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

private static final String GRAVE_QUOTE_MESSAGE =
"Avoid quoting with grave accents; use apostrophes or better yet directional quotes instead";








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 80ab682..0c1e03e 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -91,18 +92,25 @@
*/
public class UnusedResourceDetector extends ResourceXmlDetector implements Detector.JavaScanner {

/** Unused resources (other than ids). */
    public static final Issue ISSUE = Issue.create("UnusedResources", //$NON-NLS-1$
"Looks for unused resources",
"Unused resources make applications larger and slow down builds.",
Category.PERFORMANCE,
3,
Severity.WARNING,
            UnusedResourceDetector.class,
            EnumSet.of(Scope.MANIFEST, Scope.ALL_RESOURCE_FILES, Scope.ALL_JAVA_FILES));

/** Unused id's */
    public static final Issue ISSUE_IDS = Issue.create("UnusedIds", //$NON-NLS-1$
"Looks for unused id's",
"This resource id definition appears not to be needed since it is not referenced " +
"from anywhere. Having id definitions, even if unused, is not necessarily a bad " +
//Synthetic comment -- @@ -111,8 +119,7 @@
Category.PERFORMANCE,
1,
Severity.WARNING,
            UnusedResourceDetector.class,
            EnumSet.of(Scope.MANIFEST, Scope.ALL_RESOURCE_FILES, Scope.ALL_JAVA_FILES))
.setEnabledByDefault(false);

private Set<String> mDeclarations;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UseCompoundDrawableDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UseCompoundDrawableDetector.java
//Synthetic comment -- index db5de4d..5b15bf2 100644

//Synthetic comment -- @@ -26,6 +26,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -48,7 +49,10 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"UseCompoundDrawables", //$NON-NLS-1$
            "Checks whether the current node can be replaced by a TextView using compound drawables.",
"A `LinearLayout` which contains an `ImageView` and a `TextView` can be more " +
"efficiently handled as a compound drawable (a single TextView, using the " +
"`drawableTop`, `drawableLeft`, `drawableRight` and/or `drawableBottom` attributes " +
//Synthetic comment -- @@ -61,8 +65,9 @@
Category.PERFORMANCE,
6,
Severity.WARNING,
            UseCompoundDrawableDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link UseCompoundDrawableDetector} */
public UseCompoundDrawableDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UselessViewDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UselessViewDetector.java
//Synthetic comment -- index 867c3c4..1616500 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -55,9 +56,15 @@
* Checks whether the current node can be removed without affecting the layout.
*/
public class UselessViewDetector extends LayoutDetector {
/** Issue of including a parent that has no value on its own */
public static final Issue USELESS_PARENT = Issue.create(
"UselessParent", //$NON-NLS-1$
"Checks whether a parent layout can be removed.",
"A layout with children that has no siblings, is not a scrollview or " +
"a root layout, and does not have a background, can be removed and have " +
//Synthetic comment -- @@ -66,20 +73,19 @@
Category.PERFORMANCE,
2,
Severity.WARNING,
            UselessViewDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Issue of including a leaf that isn't shown */
public static final Issue USELESS_LEAF = Issue.create(
"UselessLeaf", //$NON-NLS-1$
"Checks whether a leaf layout can be removed.",
"A layout that has no children or no background can often be removed (since it " +
"is invisible) for a flatter and more efficient layout hierarchy.",
Category.PERFORMANCE,
2,
Severity.WARNING,
            UselessViewDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link UselessViewDetector} */
public UselessViewDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/Utf8Detector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/Utf8Detector.java
//Synthetic comment -- index 2e23483..1be9813 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -38,6 +39,7 @@
/** Detects non-utf8 encodings */
public static final Issue ISSUE = Issue.create(
"EnforceUTF8", //$NON-NLS-1$
"Checks that all XML resource files are using UTF-8 as the file encoding",
"XML supports encoding in a wide variety of character sets. However, not all " +
"tools handle the XML encoding attribute correctly, and nearly all Android " +
//Synthetic comment -- @@ -46,8 +48,9 @@
Category.I18N,
2,
Severity.WARNING,
            Utf8Detector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** See http://www.w3.org/TR/REC-xml/#NT-EncodingDecl */
private static final Pattern ENCODING_PATTERN =








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 4bffdd7..b8ccec1 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -49,6 +50,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"ViewConstructor", //$NON-NLS-1$
"Checks that custom views define the expected constructors",

"Some layout tools (such as the Android layout editor for Eclipse) needs to " +
//Synthetic comment -- @@ -65,8 +67,9 @@
Category.USABILITY,
3,
Severity.WARNING,
            ViewConstructorDetector.class,
            Scope.CLASS_FILE_SCOPE);

/** Constructs a new {@link ViewConstructorDetector} check */
public ViewConstructorDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTagDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTagDetector.java
//Synthetic comment -- index 46e24cc..77770d3 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -52,9 +53,10 @@
/** Using setTag and leaking memory */
public static final Issue ISSUE = Issue.create(
"ViewTag", //$NON-NLS-1$
            "Finds potential leaks when using View.setTag",

            "Prior to Android 4.0, the implementation of View.setTag(int, Object) would " +
"store the objects in a static map, where the values were strongly referenced. " +
"This means that if the object contains any references pointing back to the " +
"context, the context (which points to pretty much everything else) will leak. " +
//Synthetic comment -- @@ -65,8 +67,9 @@
Category.PERFORMANCE,
6,
Severity.WARNING,
            ViewTagDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.CLASS_FILE));

/** Constructs a new {@link ViewTagDetector} */
public ViewTagDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTypeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTypeDetector.java
//Synthetic comment -- index 436fae4..6ed60fa 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -60,15 +61,18 @@
/** Detector for finding inconsistent usage of views and casts */
public class ViewTypeDetector extends ResourceXmlDetector implements Detector.JavaScanner {
/** Mismatched view types */
    public static final Issue ISSUE = Issue.create("WrongViewCast", //$NON-NLS-1$
"Looks for incorrect casts to views that according to the XML are of a different type",
"Keeps track of the view types associated with ids and if it finds a usage of " +
"the id in the Java code it ensures that it is treated as the same type.",
Category.CORRECTNESS,
9,
Severity.ERROR,
            ViewTypeDetector.class,
            EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.ALL_JAVA_FILES));

private final Map<String, Object> mIdToViewTag = new HashMap<String, Object>(50);









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index 47410eb..5e1c41c 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -52,26 +53,28 @@

/** Problems using wakelocks */
public static final Issue ISSUE = Issue.create(
        "Wakelock", //$NON-NLS-1$
        "Looks for problems with wakelock usage",

        "Failing to release a wakelock properly can keep the Android device in " +
        "a high power mode, which reduces battery life. There are several causes " +
        "of this, such as releasing the wake lock in `onDestroy()` instead of in " +
        "`onPause()`, failing to call `release()` in all possible code paths after " +
        "an `acquire()`, and so on.\n" +
        "\n" +
        "NOTE: If you are using the lock just to keep the screen on, you should " +
        "strongly consider using `FLAG_KEEP_SCREEN_ON` instead. This window flag " +
        "will be correctly managed by the platform as the user moves between " +
        "applications and doesn't require a special permission. See " +
        "http://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_KEEP_SCREEN_ON.",

        Category.PERFORMANCE,
        9,
        Severity.WARNING,
        WakelockDetector.class,
        Scope.CLASS_FILE_SCOPE);

private static final String WAKELOCK_OWNER = "android/os/PowerManager$WakeLock"; //$NON-NLS-1$
private static final String RELEASE_METHOD = "release"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongCallDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongCallDetector.java
//Synthetic comment -- index 174f915..3b10d55 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -46,7 +47,9 @@
/** Calling the wrong method */
public static final Issue ISSUE = Issue.create(
"WrongCall", //$NON-NLS-1$
            "Finds cases where the wrong call is made, such as calling onMeasure instead of measure",

"Custom views typically need to call `measure()` on their children, not `onMeasure`. " +
"Ditto for onDraw, onLayout, etc.",
//Synthetic comment -- @@ -54,8 +57,9 @@
Category.CORRECTNESS,
6,
Severity.ERROR,
            WrongCallDetector.class,
            Scope.CLASS_FILE_SCOPE);

/** Constructs a new {@link WrongCallDetector} */
public WrongCallDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongCaseDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongCaseDetector.java
//Synthetic comment -- index d15af9a..0c2a045 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -37,21 +38,22 @@
*/
public class WrongCaseDetector extends LayoutDetector {
/** Using the wrong case for layout tags */
    public static final Issue WRONGCASE = Issue.create(
"WrongCase", //$NON-NLS-1$
"Ensures that the correct case is used for special layout tags such as <fragment>",

            ""
            + "Most layout tags, such as <Button>, refer to actual view classes and are therefore "
            + "capitalized. However, there are exceptions such as <fragment> and <include>. This "
            + "lint check looks for incorrect capitalizations.",

Category.CORRECTNESS,
8,
Severity.WARNING,
            WrongCaseDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo("http://developer.android.com/guide/components/fragments.html"); //$NON-NLS-1$

/** Constructs a new {@link WrongCaseDetector} */
public WrongCaseDetector() {
//Synthetic comment -- @@ -77,7 +79,7 @@
public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
String tag = element.getTagName();
String correct = Character.toLowerCase(tag.charAt(0)) + tag.substring(1);
        context.report(WRONGCASE, element, context.getLocation(element),
String.format("Invalid tag <%1$s>; should be <%2$s>", tag, correct), null);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index e0e2d0a..4ae14aa 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.tools.lint.client.api.IDomParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
//Synthetic comment -- @@ -89,6 +90,7 @@
/** Reference to an unknown id */
public static final Issue UNKNOWN_ID = Issue.create(
"UnknownId", //$NON-NLS-1$
"Checks for id references in RelativeLayouts that are not defined elsewhere",
"The `@+id/` syntax refers to an existing id, or creates a new one if it has " +
"not already been defined elsewhere. However, this means that if you have a " +
//Synthetic comment -- @@ -99,12 +101,14 @@
Category.CORRECTNESS,
8,
Severity.FATAL,
            WrongIdDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Reference to an id that is not in the current layout */
public static final Issue UNKNOWN_ID_LAYOUT = Issue.create(
"UnknownIdInLayout", //$NON-NLS-1$
"Makes sure that @+id references refer to views in the same layout",

"The `@+id/` syntax refers to an existing id, or creates a new one if it has " +
//Synthetic comment -- @@ -120,8 +124,9 @@
Category.CORRECTNESS,
5,
Severity.WARNING,
            WrongIdDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a duplicate id check */
public WrongIdDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongImportDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongImportDetector.java
//Synthetic comment -- index 64f3ce1..bfaa865 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -48,7 +49,9 @@
*/
public class WrongImportDetector extends Detector implements Detector.JavaScanner {
/** Is android.R being imported? */
    public static final Issue ISSUE = Issue.create("SuspiciousImport", //$NON-NLS-1$
"Checks for 'import android.R' statements, which are usually accidental",
"Importing `android.R` is usually not intentional; it sometimes happens when " +
"you use an IDE and ask it to automatically add imports at a time when your " +
//Synthetic comment -- @@ -60,8 +63,9 @@
Category.CORRECTNESS,
9,
Severity.WARNING,
            WrongImportDetector.class,
            Scope.JAVA_FILE_SCOPE);

/** Constructs a new {@link WrongImportDetector} check */
public WrongImportDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongLocationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongLocationDetector.java
//Synthetic comment -- index 37ffbeb..35b9058 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -35,7 +36,7 @@
/** Main issue investigated by this detector */
public static final Issue ISSUE = Issue.create(
"WrongFolder", //$NON-NLS-1$

"Finds resource files that are placed in the wrong folders",

"Resource files are sometimes placed in the wrong folder, and it can lead to " +
//Synthetic comment -- @@ -45,8 +46,9 @@
Category.CORRECTNESS,
8,
Severity.ERROR,
            WrongLocationDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

/** Constructs a new {@link WrongLocationDetector} check */
public WrongLocationDetector() {







