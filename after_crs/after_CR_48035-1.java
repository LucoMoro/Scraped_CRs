/*Code cleanup

Ran some static analysis on the lint code and fixed
various issues - some typos, made some fields final, made
some methods static, sorted modifier order etc.

Change-Id:Ibdcdec5745f040eb7b0880cf6999c0f0ea7f7e6f*/




//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/HtmlReporter.java b/lint/cli/src/main/java/com/android/tools/lint/HtmlReporter.java
//Synthetic comment -- index 19f1028..83cd76d 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
*/
@Beta
public class HtmlReporter extends Reporter {
    private static final boolean USE_HOLO_STYLE = true;
private static final String CSS = USE_HOLO_STYLE
? "hololike.css" : "default.css"; //$NON-NLS-1$ //$NON-NLS-2$

//Synthetic comment -- @@ -132,7 +132,7 @@
mWriter.write("<br/><br/>");                                  //$NON-NLS-1$

Issue previousIssue = null;
        if (!issues.isEmpty()) {
List<List<Warning>> related = new ArrayList<List<Warning>>();
List<Warning> currentList = null;
for (Warning warning : issues) {
//Synthetic comment -- @@ -229,7 +229,7 @@
int otherLocations = 0;
while (l != null) {
String message = l.getMessage();
                            if (message != null && !message.isEmpty()) {
Position start = l.getStart();
int line = start != null ? start.getLine() : -1;
String path = mClient.getDisplayPath(warning.project, l.getFile());
//Synthetic comment -- @@ -244,7 +244,7 @@
String name = l.getFile().getName();
if (!(endsWith(name, DOT_PNG) || endsWith(name, DOT_JPG))) {
String s = mClient.readFile(l.getFile());
                                    if (s != null && !s.isEmpty()) {
mWriter.write("<pre class=\"errorlines\">\n");   //$NON-NLS-1$
int offset = start != null ? start.getOffset() : -1;
appendCodeBlock(s, line, offset);
//Synthetic comment -- @@ -362,7 +362,7 @@
mWriter.write("Explanation: ");
String description = issue.getDescription();
mWriter.write(description);
        if (!description.isEmpty()
&& Character.isLetter(description.charAt(description.length() - 1))) {
mWriter.write('.');
}
//Synthetic comment -- @@ -617,7 +617,7 @@
}
location = location.getSecondary();
}
                if (!urls.isEmpty()) {
// Sort in order
Collections.sort(urls, new Comparator<String>() {
@Override








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java b/lint/cli/src/main/java/com/android/tools/lint/LintCliXmlParser.java
//Synthetic comment -- index c06178a..3e1408a 100644

//Synthetic comment -- @@ -70,8 +70,9 @@
return null;
}

    @NonNull
@Override
    public Location getLocation(@NonNull XmlContext context, @NonNull Node node) {
OffsetPosition pos = (OffsetPosition) getPosition(node, -1, -1);
if (pos != null) {
return Location.create(context.file, pos, (OffsetPosition) pos.getEnd());
//Synthetic comment -- @@ -80,8 +81,9 @@
return Location.create(context.file);
}

    @NonNull
@Override
    public Location getLocation(@NonNull XmlContext context, @NonNull Node node,
int start, int end) {
OffsetPosition pos = (OffsetPosition) getPosition(node, start, end);
if (pos != null) {
//Synthetic comment -- @@ -91,13 +93,15 @@
return Location.create(context.file);
}

    @NonNull
@Override
    public Handle createLocationHandle(@NonNull XmlContext context, @NonNull Node node) {
return new LocationHandle(context.file, node);
}

    @NonNull
@Override
    protected OffsetPosition createPosition(int line, int column, int offset) {
return new OffsetPosition(line, column, offset);
}

//Synthetic comment -- @@ -119,7 +123,7 @@
* Linked position: for a begin offset this will point to the end
* offset, and for an end offset this will be null
*/
        private PositionXmlParser.Position mEnd;

/**
* Creates a new {@link OffsetPosition}
//Synthetic comment -- @@ -150,19 +154,19 @@
}

@Override
        public PositionXmlParser.Position getEnd() {
return mEnd;
}

@Override
        public void setEnd(@NonNull PositionXmlParser.Position end) {
mEnd = end;
}

@Override
public String toString() {
return "OffsetPosition [line=" + mLine + ", column=" + mColumn + ", offset="
                    + mOffset + ", end=" + mEnd + ']';
}
}

//Synthetic comment -- @@ -172,8 +176,8 @@

/* Handle for creating DOM positions cheaply and returning full fledged locations later */
private class LocationHandle implements Handle {
        private final File mFile;
        private final Node mNode;
private Object mClientData;

public LocationHandle(File file, Node node) {
//Synthetic comment -- @@ -181,8 +185,9 @@
mNode = node;
}

        @NonNull
@Override
        public Location resolve() {
OffsetPosition pos = (OffsetPosition) getPosition(mNode);
if (pos != null) {
return Location.create(mFile, pos, (OffsetPosition) pos.getEnd());








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/LombokParser.java b/lint/cli/src/main/java/com/android/tools/lint/LombokParser.java
//Synthetic comment -- index bc841e2..26845a9 100644

//Synthetic comment -- @@ -47,7 +47,7 @@

// Don't analyze files containing errors
List<ParseProblem> problems = source.getProblems();
            if (problems != null && !problems.isEmpty()) {
context.getDriver().setHasParserErrors(true);

/* Silently ignore the errors. There are still some bugs in Lombok/Parboiled
//Synthetic comment -- @@ -97,17 +97,19 @@
}
}

    @NonNull
@Override
    public Location getLocation(
@NonNull JavaContext context,
            @NonNull Node node) {
        Position position = node.getPosition();
return Location.create(context.file, context.getContents(),
position.getStart(), position.getEnd());
}

    @NonNull
@Override
    public Handle createLocationHandle(@NonNull JavaContext context, @NonNull Node node) {
return new LocationHandle(context.file, node);
}

//Synthetic comment -- @@ -116,9 +118,9 @@
}

/* Handle for creating positions cheaply and returning full fledged locations later */
    private static class LocationHandle implements Handle {
        private final File mFile;
        private final Node mNode;
private Object mClientData;

public LocationHandle(File file, Node node) {
//Synthetic comment -- @@ -126,8 +128,9 @@
mNode = node;
}

        @NonNull
@Override
        public Location resolve() {
Position pos = mNode.getPosition();
return Location.create(mFile, null /*contents*/, pos.getStart(), pos.getEnd());
}








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Main.java b/lint/cli/src/main/java/com/android/tools/lint/Main.java
//Synthetic comment -- index d2f9439..085a38c 100644

//Synthetic comment -- @@ -109,9 +109,9 @@
private static final int ERRNO_HELP = 4;
private static final int ERRNO_INVALIDARGS = 5;

    protected final List<Warning> mWarnings = new ArrayList<Warning>();
    protected final Set<String> mSuppress = new HashSet<String>();
    protected final Set<String> mEnabled = new HashSet<String>();
/** If non-null, only run the specified checks (possibly modified by enable/disables) */
protected Set<String> mCheck = null;
protected boolean mHasErrors;
//Synthetic comment -- @@ -120,7 +120,7 @@
protected int mErrorCount;
protected int mWarningCount;
protected boolean mShowLines = true;
    protected final List<Reporter> mReporters = Lists.newArrayList();
protected boolean mQuiet;
protected boolean mWarnAll;
protected boolean mNoWarnings;
//Synthetic comment -- @@ -533,7 +533,7 @@
}
}

        if (files.isEmpty()) {
System.err.println("No files to analyze.");
System.exit(ERRNO_INVALIDARGS);
} else if (files.size() > 1
//Synthetic comment -- @@ -614,7 +614,7 @@
* @param filename The filename given as a command-line argument.
* @return A File matching filename, either absolute or relative to lint.workdir if defined.
*/
    private static File getInArgumentPath(String filename) {
File file = new File(filename);

if (!file.isAbsolute()) {
//Synthetic comment -- @@ -642,7 +642,7 @@
* @param filename The filename given as a command-line argument.
* @return A File matching filename, either absolute or relative to lint.workdir if defined.
*/
    private static File getOutArgumentPath(String filename) {
File file = new File(filename);

if (!file.isAbsolute()) {
//Synthetic comment -- @@ -670,20 +670,20 @@
* @return A new File corresponding to {@link #PROP_WORK_DIR} or null.
*/
@Nullable
    private static File getLintWorkDir() {
// First check the Java properties (e.g. set using "java -jar ... -Dname=value")
String path = System.getProperty(PROP_WORK_DIR);
        if (path == null || path.isEmpty()) {
// If not found, check environment variables.
path = System.getenv(PROP_WORK_DIR);
}
        if (path != null && !path.isEmpty()) {
return new File(path);
}
return null;
}

    private static void printHelpTopicSuppress() {
System.out.println(wrap(getSuppressHelp()));
}

//Synthetic comment -- @@ -771,7 +771,7 @@
properties.load(input);

String revision = properties.getProperty("Pkg.Revision"); //$NON-NLS-1$
                if (revision != null && !revision.isEmpty()) {
return revision;
}
} catch (IOException e) {
//Synthetic comment -- @@ -784,7 +784,7 @@
return null;
}

    private static void displayValidIds(IssueRegistry registry, PrintStream out) {
List<Category> categories = registry.getCategories();
out.println("Valid issue categories:");
for (Category category : categories) {
//Synthetic comment -- @@ -798,11 +798,11 @@
}
}

    private static void listIssue(PrintStream out, Issue issue) {
out.print(wrapArg("\"" + issue.getId() + "\": " + issue.getDescription()));
}

    private static void showIssues(IssueRegistry registry) {
List<Issue> issues = registry.getIssues();
List<Issue> sorted = new ArrayList<Issue>(issues);
Collections.sort(sorted, new Comparator<Issue>() {
//Synthetic comment -- @@ -840,7 +840,7 @@
}
}

    private static void describeIssue(Issue issue) {
System.out.println(issue.getId());
for (int i = 0; i < issue.getId().length(); i++) {
System.out.print('-');
//Synthetic comment -- @@ -956,7 +956,7 @@
argWidth = Math.max(argWidth, arg.length());
}
argWidth += 2;
        StringBuilder sb = new StringBuilder(20);
for (int i = 0; i < argWidth; i++) {
sb.append(' ');
}
//Synthetic comment -- @@ -966,7 +966,7 @@
for (int i = 0; i < args.length; i += 2) {
String arg = args[i];
String description = args[i + 1];
            if (arg.isEmpty()) {
out.println(description);
} else {
out.print(wrap(String.format(formatString, arg, description),
//Synthetic comment -- @@ -1006,7 +1006,7 @@
}

/** File content cache */
    private final Map<File, String> mFileContents = new HashMap<File, String>(100);

/** Read the contents of the given file, possibly cached */
private String getContents(File file) {
//Synthetic comment -- @@ -1085,7 +1085,7 @@
}
}
}
                            StringBuilder sb = new StringBuilder(100);
sb.append(warning.errorLine);
sb.append('\n');
for (int i = 0; i < column; i++) {
//Synthetic comment -- @@ -1099,7 +1099,7 @@
int endColumn = endPosition.getColumn();
if (endLine == line && endColumn > column) {
for (int i = column; i < endColumn; i++) {
                                        sb.append('~');
}
displayCaret = false;
}
//Synthetic comment -- @@ -1150,8 +1150,9 @@
return index;
}

    @NonNull
@Override
    public String readFile(@NonNull File file) {
try {
return LintUtils.getEncodedString(this, file);
} catch (IOException e) {
//Synthetic comment -- @@ -1222,8 +1223,9 @@
super(Main.this, null /*project*/, null /*parent*/, lintFile);
}

        @NonNull
@Override
        public Severity getSeverity(@NonNull Issue issue) {
Severity severity = computeSeverity(issue);

if (mAllErrors && severity != Severity.IGNORE) {
//Synthetic comment -- @@ -1237,8 +1239,9 @@
return severity;
}

        @NonNull
@Override
        protected Severity getDefaultSeverity(@NonNull Issue issue) {
if (mWarnAll) {
return issue.getDefaultSeverity();
}
//Synthetic comment -- @@ -1277,7 +1280,7 @@
}
}

    private static class ProgressPrinter implements LintListener {
@Override
public void update(
@NonNull LintDriver lint,
//Synthetic comment -- @@ -1315,6 +1318,9 @@
case COMPLETED:
System.out.println();
break;
                case STARTING:
                    // Ignored for now
                    break;
}
}
}
//Synthetic comment -- @@ -1383,7 +1389,7 @@
chop++;
}
path = path.substring(chop);
            if (path.isEmpty()) {
path = file.getName();
}
} else if (mFullPath) {








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/MultiProjectHtmlReporter.java b/lint/cli/src/main/java/com/android/tools/lint/MultiProjectHtmlReporter.java
//Synthetic comment -- index d039edc..6035c29 100644

//Synthetic comment -- @@ -215,10 +215,10 @@
}

private static class ProjectEntry implements Comparable<ProjectEntry> {
        public final int errorCount;
        public final int warningCount;
        public final String fileName;
        public final String path;


public ProjectEntry(String fileName, int errorCount, int warningCount, String path) {








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Reporter.java b/lint/cli/src/main/java/com/android/tools/lint/Reporter.java
//Synthetic comment -- index fc3aaae..3086a7c 100644

//Synthetic comment -- @@ -50,8 +50,8 @@
protected boolean mBundleResources;
protected Map<String, String> mUrlMap;
protected File mResources;
    protected final Map<File, String> mResourceUrl = new HashMap<File, String>();
    protected final Map<String, File> mNameToFile = new HashMap<String, File>();

/**
* Write the given warnings into the report








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/TextReporter.java b/lint/cli/src/main/java/com/android/tools/lint/TextReporter.java
//Synthetic comment -- index 4f2c8b4..8e9f776 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
boolean abbreviate = mClient.getDriver().isAbbreviating();

StringBuilder output = new StringBuilder(issues.size() * 200);
        if (issues.isEmpty()) {
mWriter.write('\n');
mWriter.write("No issues found.");
mWriter.write('\n');
//Synthetic comment -- @@ -95,7 +95,7 @@

output.append('\n');

                if (warning.errorLine != null && !warning.errorLine.isEmpty()) {
output.append(warning.errorLine);
}

//Synthetic comment -- @@ -103,7 +103,7 @@
Location location = warning.location.getSecondary();
while (location != null) {
if (location.getMessage() != null
                                && !location.getMessage().isEmpty()) {
output.append("    "); //$NON-NLS-1$
String path = mClient.getDisplayPath(warning.project,
location.getFile());
//Synthetic comment -- @@ -119,7 +119,7 @@
}

if (location.getMessage() != null
                                    && !location.getMessage().isEmpty()) {
output.append(':');
output.append(' ');
output.append(location.getMessage());
//Synthetic comment -- @@ -133,12 +133,12 @@

if (!abbreviate) {
location = warning.location.getSecondary();
                        StringBuilder sb = new StringBuilder(100);
sb.append("Also affects: ");
int begin = sb.length();
while (location != null) {
if (location.getMessage() == null
                                    || !location.getMessage().isEmpty()) {
if (sb.length() > begin) {
sb.append(", ");
}








//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/XmlReporter.java b/lint/cli/src/main/java/com/android/tools/lint/XmlReporter.java
//Synthetic comment -- index 04ac1d9..c0dca75 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
}
mWriter.write(">\n");                                               //$NON-NLS-1$

        if (!issues.isEmpty()) {
for (Warning warning : issues) {
mWriter.write('\n');
indent(mWriter, 1);








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AnnotationDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AnnotationDetectorTest.java
//Synthetic comment -- index becca80..7da1444 100644

//Synthetic comment -- @@ -25,19 +25,19 @@
public class AnnotationDetectorTest extends AbstractCheckTest {
public void test() throws Exception {
assertEquals(
            "src/test/pkg/WrongAnnotation.java:9: Error: The @SuppressLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"    public static void foobar(View view, @SuppressLint(\"NewApi\") int foo) { // Invalid: class-file check\n" +
"                                         ~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:10: Error: The @SuppressLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @SuppressLint(\"NewApi\") // Invalid\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:12: Error: The @SuppressLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @SuppressLint({\"SdCardPath\", \"NewApi\"}) // Invalid: class-file based check on local variable\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:14: Error: The @SuppressLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @android.annotation.SuppressLint({\"SdCardPath\", \"NewApi\"}) // Invalid (FQN)\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/WrongAnnotation.java:28: Error: The @SuppressLint annotation cannot be used on a local variable with the lint check 'NewApi': move out to the surrounding method [LocalSuppress]\n" +
"        @SuppressLint(\"NewApi\")\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~\n" +
"5 errors, 0 warnings\n",








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java
//Synthetic comment -- index 46e494e..f875fa1 100644

//Synthetic comment -- @@ -27,7 +27,7 @@

public void test() throws Exception {
assertEquals(
                "src/test/pkg/NonInternationalizedSmsDetectorTest.java:18: Warning: To make sure the SMS can be sent by all users, please start the SMS number with a + and a country code or restrict the code invocation to people in the country you are targeting. [UnlocalizedSms]\n" +
"  sms.sendMultipartTextMessage(\"001234567890\", null, null, null, null);\n" +
"                               ~~~~~~~~~~~~~~\n" +
"0 errors, 1 warnings\n" +








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/TranslationDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/TranslationDetectorTest.java
//Synthetic comment -- index 7a9830b..f13cff1 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
}

public void testTranslation() throws Exception {
        TranslationDetector.sCompleteRegions = false;
assertEquals(
// Sample files from the Home app
"res/values/strings.xml:20: Error: \"show_all_apps\" is not translated in nl-rNL [MissingTranslation]\n" +
//Synthetic comment -- @@ -66,7 +66,7 @@
}

public void testTranslationWithCompleteRegions() throws Exception {
        TranslationDetector.sCompleteRegions = true;
assertEquals(
// Sample files from the Home app
"res/values/strings.xml:19: Error: \"home_title\" is not translated in es-rUS [MissingTranslation]\n" +
//Synthetic comment -- @@ -112,7 +112,7 @@
}

public void testTranslatedArrays() throws Exception {
        TranslationDetector.sCompleteRegions = true;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -122,7 +122,7 @@
}

public void testTranslationSuppresss() throws Exception {
        TranslationDetector.sCompleteRegions = false;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -166,7 +166,7 @@
}

public void testNonTranslatable1() throws Exception {
        TranslationDetector.sCompleteRegions = true;
assertEquals(
"res/values-nb/nontranslatable.xml:3: Error: The resource string \"dummy\" has been marked as translatable=\"false\" [ExtraTranslation]\n" +
"    <string name=\"dummy\">Ignore Me</string>\n" +
//Synthetic comment -- @@ -179,7 +179,7 @@
}

public void testNonTranslatable2() throws Exception {
        TranslationDetector.sCompleteRegions = true;
assertEquals(
"res/values-nb/nontranslatable.xml:3: Error: Non-translatable resources should only be defined in the base values/ folder [ExtraTranslation]\n" +
"    <string name=\"dummy\" translatable=\"false\">Ignore Me</string>\n" +
//Synthetic comment -- @@ -191,7 +191,7 @@
}

public void testSpecifiedLanguageOk() throws Exception {
        TranslationDetector.sCompleteRegions = false;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -201,7 +201,7 @@
}

public void testSpecifiedLanguage() throws Exception {
        TranslationDetector.sCompleteRegions = false;
assertEquals(
"No warnings.",









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/AsmVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/AsmVisitor.java
//Synthetic comment -- index 81e2934..e8a4bb5 100644

//Synthetic comment -- @@ -62,14 +62,13 @@
* there isn't a max-constant there, so update this along with ASM library
* updates.
*/
    private static final int TYPE_COUNT = AbstractInsnNode.LINE + 1;
private final Map<String, List<ClassScanner>> mMethodNameToChecks =
new HashMap<String, List<ClassScanner>>();
private final Map<String, List<ClassScanner>> mMethodOwnerToChecks =
new HashMap<String, List<ClassScanner>>();
private final List<Detector> mFullClassChecks = new ArrayList<Detector>();

private final List<? extends Detector> mAllDetectors;
private List<ClassScanner>[] mNodeTypeDetectors;

//Synthetic comment -- @@ -78,7 +77,6 @@
// but it makes client code tricky and ugly.
@SuppressWarnings("unchecked")
AsmVisitor(@NonNull LintClient client, @NonNull List<? extends Detector> classDetectors) {
mAllDetectors = classDetectors;

// TODO: Check appliesTo() for files, and find a quick way to enable/disable
//Synthetic comment -- @@ -117,11 +115,10 @@
int[] types = scanner.getApplicableAsmNodeTypes();
if (types != null) {
checkFullClass = false;
                for (int type : types) {
if (type < 0 || type >= TYPE_COUNT) {
// Can't support this node type: looks like ASM wasn't updated correctly.
                        client.log(null, "Out of range node type %1$d from detector %2$s",
type, scanner);
continue;
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/Configuration.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/Configuration.java
//Synthetic comment -- index ad1c7e1..d233be7 100644

//Synthetic comment -- @@ -111,7 +111,7 @@

/**
* Marks the beginning of a "bulk" editing operation with repeated calls to
     * {@link #setSeverity} or {@link #ignore}. After all the values have been
* set, the client <b>must</b> call {@link #finishBulkEditing()}. This
* allows configurations to avoid doing expensive I/O (such as writing out a
* config XML file) for each and every editing operation when they are








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java
//Synthetic comment -- index 5a8a973..db82556 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
private static final String TAG_IGNORE = "ignore"; //$NON-NLS-1$

private final Configuration mParent;
    private final Project mProject;
private final File mConfigFile;
private boolean mBulkEditing;

//Synthetic comment -- @@ -237,7 +237,7 @@
Node node = issues.item(i);
Element element = (Element) node;
String id = element.getAttribute(ATTR_ID);
                if (id.isEmpty()) {
formatError("Invalid lint config file: Missing required issue id attribute");
continue;
}
//Synthetic comment -- @@ -269,7 +269,7 @@
if (child.getNodeType() == Node.ELEMENT_NODE) {
Element ignore = (Element) child;
String path = ignore.getAttribute(ATTR_PATH);
                            if (path.isEmpty()) {
formatError("Missing required %1$s attribute under %2$s",
ATTR_PATH, id);
} else {
//Synthetic comment -- @@ -305,7 +305,7 @@
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +     //$NON-NLS-1$
"<lint>\n");                                         //$NON-NLS-1$

            if (!mSuppressed.isEmpty() || !mSeverity.isEmpty()) {
// Process the maps in a stable sorted order such that if the
// files are checked into version control with the project,
// there are no random diffs just because hashing algorithms
//Synthetic comment -- @@ -331,7 +331,7 @@
}

List<String> paths = mSuppressed.get(id);
                    if (paths != null && !paths.isEmpty()) {
writer.write('>');
writer.write('\n');
// The paths are already kept in sorted order when they are modified
//Synthetic comment -- @@ -358,7 +358,7 @@
// Move file into place: move current version to lint.xml~ (removing the old ~ file
// if it exists), then move the new version to lint.xml.
File oldFile = new File(mConfigFile.getParentFile(),
                    mConfigFile.getName() + '~'); //$NON-NLS-1$
if (oldFile.exists()) {
oldFile.delete();
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index d6f0579..dcfed69 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
@NonNull
public static final Issue LINT_ERROR = Issue.create(
"LintError", //$NON-NLS-1$
            "Issues related to running lint itself, such as failure to read files, etc",
"This issue type represents a problem running lint itself. Examples include " +
"failure to find bytecode for source files (which means certain detectors " +
"could not be run), parsing errors in lint configuration files, etc." +
//Synthetic comment -- @@ -234,8 +234,8 @@
* @return true if the given string is a valid category
*/
public final boolean isCategoryName(@NonNull String name) {
        for (Category category : getCategories()) {
            if (category.getName().equals(name) || category.getFullName().equals(name)) {
return true;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/JavaVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/JavaVisitor.java
//Synthetic comment -- index b1d8832..b74693a 100644

//Synthetic comment -- @@ -138,7 +138,7 @@
new ArrayList<VisitingDetector>();
private final List<VisitingDetector> mAllDetectors;
private final List<VisitingDetector> mFullTreeDetectors;
    private final Map<Class<? extends Node>, List<VisitingDetector>> mNodeTypeDetectors =
new HashMap<Class<? extends Node>, List<VisitingDetector>>();
private final IJavaParser mParser;

//Synthetic comment -- @@ -181,8 +181,8 @@

if (detector.appliesToResourceRefs()) {
mResourceFieldDetectors.add(v);
            } else if ((names == null || names.isEmpty())
                    && (nodeTypes == null || nodeTypes.isEmpty())) {
mFullTreeDetectors.add(v);
}
}
//Synthetic comment -- @@ -214,10 +214,10 @@
}
}

            if (!mMethodDetectors.isEmpty() || !mResourceFieldDetectors.isEmpty()) {
AstVisitor visitor = new DelegatingJavaVisitor(context);
compilationUnit.accept(visitor);
            } else if (!mNodeTypeDetectors.isEmpty()) {
AstVisitor visitor = new DispatchVisitor();
compilationUnit.accept(visitor);
}
//Synthetic comment -- @@ -1113,8 +1113,8 @@
public DelegatingJavaVisitor(JavaContext context) {
mContext = context;

            mVisitMethods = !mMethodDetectors.isEmpty();
            mVisitResources = !mResourceFieldDetectors.isEmpty();
}

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 461e64f..7843aa1 100644

//Synthetic comment -- @@ -294,14 +294,14 @@
* @return A new File corresponding to {@link LintClient#PROP_BIN_DIR} or null.
*/
@Nullable
    private static File getLintBinDir() {
// First check the Java properties (e.g. set using "java -jar ... -Dname=value")
String path = System.getProperty(PROP_BIN_DIR);
        if (path == null || path.isEmpty()) {
// If not found, check environment variables.
path = System.getenv(PROP_BIN_DIR);
}
        if (path != null && !path.isEmpty()) {
return new File(path);
}
return null;
//Synthetic comment -- @@ -472,7 +472,7 @@
}
}

            if (classes.isEmpty()) {
File folder = new File(projectDir, CLASS_FOLDER);
if (folder.exists()) {
classes.add(folder);
//Synthetic comment -- @@ -485,7 +485,7 @@

// If it's maven, also correct the source path, "src" works but
// it's in a more specific subfolder
                        if (sources.isEmpty()) {
File src = new File(projectDir,
"src" + File.separator     //$NON-NLS-1$
+ "main" + File.separator  //$NON-NLS-1$
//Synthetic comment -- @@ -512,7 +512,7 @@
}

// Fallback, in case there is no Eclipse project metadata here
            if (sources.isEmpty()) {
File src = new File(projectDir, SRC_FOLDER);
if (src.exists()) {
sources.add(src);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 463562b..a92a9a2 100644

//Synthetic comment -- @@ -78,6 +78,7 @@
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -120,8 +121,8 @@
private static final String SUPPRESS_LINT_VMSIG = '/' + SUPPRESS_LINT + ';';

private final LintClient mClient;
    private final IssueRegistry mRegistry;
private volatile boolean mCanceled;
private EnumSet<Scope> mScope;
private List<? extends Detector> mApplicableDetectors;
private Map<Scope, List<Detector>> mScopeDetectors;
//Synthetic comment -- @@ -283,7 +284,7 @@
mScope = scope;

Collection<Project> projects = computeProjects(files);
        if (projects.isEmpty()) {
mClient.log(null, "No projects found for %1$s", files.toString());
return;
}
//Synthetic comment -- @@ -332,7 +333,7 @@
// The set of available detectors varies between projects
computeDetectors(project);

            if (mApplicableDetectors.isEmpty()) {
// No detectors enabled in this project: skip it
continue;
}
//Synthetic comment -- @@ -382,7 +383,7 @@
// those that apply for the configuration.
computeRepeatingDetectors(mRepeatingDetectors, project);

                if (mApplicableDetectors.isEmpty()) {
// No detectors enabled in this project: skip it
continue;
}
//Synthetic comment -- @@ -415,7 +416,7 @@
Map<Class<? extends Detector>, EnumSet<Scope>> detectorToScope =
new HashMap<Class<? extends Detector>, EnumSet<Scope>>();
Map<Scope, List<Detector>> scopeToDetectors =
                new EnumMap<Scope, List<Detector>>(Scope.class);

List<Detector> detectorList = new ArrayList<Detector>();
// Compute the list of detectors (narrowed down from mRepeatingDetectors),
//Synthetic comment -- @@ -426,7 +427,7 @@
for (Detector detector : detectors) {
Class<? extends Detector> detectorClass = detector.getClass();
Collection<Issue> detectorIssues = issueMap.get(detectorClass);
            if (detectorIssues != null) {
boolean add = false;
for (Issue issue : detectorIssues) {
// The reason we have to check whether the detector is enabled
//Synthetic comment -- @@ -480,7 +481,7 @@
mCurrentVisitor = null;

Configuration configuration = project.getConfiguration();
        mScopeDetectors = new EnumMap<Scope, List<Detector>>(Scope.class);
mApplicableDetectors = mRegistry.createDetectors(mClient, configuration,
mScope, mScopeDetectors);

//Synthetic comment -- @@ -815,7 +816,7 @@
if (mScope.contains(Scope.ALL_RESOURCE_FILES) || mScope.contains(Scope.RESOURCE_FILE)) {
List<Detector> checks = union(mScopeDetectors.get(Scope.RESOURCE_FILE),
mScopeDetectors.get(Scope.ALL_RESOURCE_FILES));
            if (checks != null && !checks.isEmpty()) {
List<ResourceXmlDetector> xmlDetectors =
new ArrayList<ResourceXmlDetector>(checks.size());
for (Detector detector : checks) {
//Synthetic comment -- @@ -823,13 +824,13 @@
xmlDetectors.add((ResourceXmlDetector) detector);
}
}
                if (!xmlDetectors.isEmpty()) {
List<File> files = project.getSubset();
if (files != null) {
checkIndividualResources(project, main, xmlDetectors, files);
} else {
File res = project.getResourceFolder();
                        if (res != null && !xmlDetectors.isEmpty()) {
checkResFolder(project, main, res, xmlDetectors);
}
}
//Synthetic comment -- @@ -844,7 +845,7 @@
if (mScope.contains(Scope.JAVA_FILE) || mScope.contains(Scope.ALL_JAVA_FILES)) {
List<Detector> checks = union(mScopeDetectors.get(Scope.JAVA_FILE),
mScopeDetectors.get(Scope.ALL_JAVA_FILES));
            if (checks != null && !checks.isEmpty()) {
List<File> files = project.getSubset();
if (files != null) {
checkIndividualJavaFiles(project, main, checks, files);
//Synthetic comment -- @@ -1026,7 +1027,7 @@

List<File> libraries = project.getJavaLibraries();
List<ClassEntry> libraryEntries;
        if (!libraries.isEmpty()) {
libraryEntries = new ArrayList<ClassEntry>(64);
findClasses(libraryEntries, libraries);
Collections.sort(libraryEntries);
//Synthetic comment -- @@ -1036,7 +1037,7 @@

List<File> classFolders = project.getJavaClassFolders();
List<ClassEntry> classEntries;
        if (classFolders.isEmpty()) {
String message = String.format("No .class files were found in project \"%1$s\", "
+ "so none of the classfile based checks could be run. "
+ "Does the project need to be built first?", project.getName());
//Synthetic comment -- @@ -1101,7 +1102,7 @@
}
}

            if (!entries.isEmpty()) {
Collections.sort(entries);
// No superclass info available on individual lint runs, unless
// the client can provide it
//Synthetic comment -- @@ -1123,7 +1124,7 @@
Project project, Project main) {
if (mScope.contains(scope)) {
List<Detector> classDetectors = mScopeDetectors.get(scope);
            if (classDetectors != null && !classDetectors.isEmpty() && !entries.isEmpty()) {
AsmVisitor visitor = new AsmVisitor(mClient, classDetectors);

String sourceContents = null;
//Synthetic comment -- @@ -1333,7 +1334,7 @@
}
}

    private static void addClassFiles(@NonNull File dir, @NonNull List<File> classFiles) {
// Process the resource folder
File[] files = dir.listFiles();
if (files != null && files.length > 0) {
//Synthetic comment -- @@ -1359,14 +1360,14 @@
return;
}

        assert !checks.isEmpty();

// Gather all Java source files in a single pass; more efficient.
List<File> sources = new ArrayList<File>(100);
for (File folder : sourceFolders) {
gatherJavaFiles(folder, sources);
}
        if (!sources.isEmpty()) {
JavaVisitor visitor = new JavaVisitor(javaParser, checks);
for (File file : sources) {
JavaContext context = new JavaContext(this, project, main, file);
//Synthetic comment -- @@ -1405,7 +1406,7 @@
}
}

    private static void gatherJavaFiles(@NonNull File dir, @NonNull List<File> result) {
File[] files = dir.listFiles();
if (files != null) {
for (File file : files) {
//Synthetic comment -- @@ -1443,7 +1444,7 @@
return mCurrentVisitor;
}

            if (applicableChecks.isEmpty()) {
mCurrentVisitor = null;
return null;
}
//Synthetic comment -- @@ -1472,9 +1473,8 @@
// same time

Arrays.sort(resourceDirs);
for (File dir : resourceDirs) {
            ResourceFolderType type = ResourceFolderType.getFolderType(dir.getName());
if (type != null) {
checkResourceFolder(project, main, dir, type, checks);
}
//Synthetic comment -- @@ -1566,7 +1566,7 @@
*/
public void removeLintListener(@NonNull LintListener listener) {
mListeners.remove(listener);
        if (mListeners.isEmpty()) {
mListeners = null;
}
}
//Synthetic comment -- @@ -1574,8 +1574,7 @@
/** Notifies listeners, if any, that the given event has occurred */
private void fireEvent(@NonNull LintListener.EventType type, @Nullable Context context) {
if (mListeners != null) {
            for (LintListener listener : mListeners) {
listener.update(this, type, context);
}
}
//Synthetic comment -- @@ -1583,8 +1582,7 @@

/**
* Wrapper around the lint client. This sits in the middle between a
     * detector calling for example {@link LintClient#report} and
* the actual embedding tool, and performs filtering etc such that detectors
* and lint clients don't have to make sure they check for ignored issues or
* filtered out warnings.
//Synthetic comment -- @@ -1669,8 +1667,9 @@
return mDelegate.getJavaClassFolders(project);
}

        @NonNull
@Override
        public List<File> getJavaLibraries(@NonNull Project project) {
return mDelegate.getJavaLibraries(project);
}

//Synthetic comment -- @@ -1900,7 +1899,7 @@
return false;
}

    private static boolean isSuppressed(@Nullable Issue issue, List<AnnotationNode> annotations) {
for (AnnotationNode annotation : annotations) {
String desc = annotation.desc;









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintListener.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintListener.java
//Synthetic comment -- index 2247a6d..8195d36 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
@Beta
public interface LintListener {
/** The various types of events provided to lint listeners */
    enum EventType {
/** A lint check is about to begin */
STARTING,

//Synthetic comment -- @@ -51,7 +51,7 @@

/** The lint check is done */
COMPLETED,
    }

/**
* Notifies listeners that the event of the given type has occurred.
//Synthetic comment -- @@ -65,6 +65,6 @@
* @param type the type of event that occurred
* @param context the context providing additional information
*/
    void update(@NonNull LintDriver driver, @NonNull EventType type,
@Nullable Context context);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/XmlVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/XmlVisitor.java
//Synthetic comment -- index 816c028..2e64118 100644

//Synthetic comment -- @@ -109,9 +109,9 @@
}
}

            if ((attributes == null || (attributes.isEmpty()
&& attributes != XmlScanner.ALL))
                  && (elements == null || (elements.isEmpty()
&& elements != XmlScanner.ALL))) {
mDocumentDetectors.add(xmlDetector);
}
//Synthetic comment -- @@ -145,8 +145,8 @@
check.visitDocument(context, context.document);
}

            if (!mElementToCheck.isEmpty() || !mAttributeToCheck.isEmpty()
                    || !mAllAttributeDetectors.isEmpty() || !mAllElementDetectors.isEmpty()) {
visitElement(context, context.document.getDocumentElement());
}

//Synthetic comment -- @@ -165,19 +165,17 @@
List<Detector.XmlScanner> elementChecks = mElementToCheck.get(element.getTagName());
if (elementChecks != null) {
assert elementChecks instanceof RandomAccess;
            for (XmlScanner check : elementChecks) {
check.visitElement(context, element);
}
}
        if (!mAllElementDetectors.isEmpty()) {
            for (XmlScanner check : mAllElementDetectors) {
check.visitElement(context, element);
}
}

        if (!mAttributeToCheck.isEmpty() || !mAllAttributeDetectors.isEmpty()) {
NamedNodeMap attributes = element.getAttributes();
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
//Synthetic comment -- @@ -187,14 +185,12 @@
}
List<Detector.XmlScanner> list = mAttributeToCheck.get(name);
if (list != null) {
                    for (XmlScanner check : list) {
check.visitAttribute(context, attribute);
}
}
                if (!mAllAttributeDetectors.isEmpty()) {
                    for (XmlScanner check : mAllAttributeDetectors) {
check.visitAttribute(context, attribute);
}
}
//Synthetic comment -- @@ -212,14 +208,12 @@

// Post hooks
if (elementChecks != null) {
            for (XmlScanner check : elementChecks) {
check.visitElementAfter(context, element);
}
}
        if (!mAllElementDetectors.isEmpty()) {
            for (XmlScanner check : mAllElementDetectors) {
check.visitElementAfter(context, element);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Category.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Category.java
//Synthetic comment -- index ba8e5b5..c267420 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
@Beta
public final class Category implements Comparable<Category> {
private final String mName;
private final int mPriority;
private final Category mParent;

//Synthetic comment -- @@ -38,17 +37,14 @@
*
* @param parent the name of a parent category, or null
* @param name the name of the category
* @param priority a sorting priority, with higher being more important
*/
private Category(
@Nullable Category parent,
@NonNull String name,
int priority) {
mParent = parent;
mName = name;
mPriority = priority;
}

//Synthetic comment -- @@ -61,7 +57,7 @@
*/
@NonNull
public static Category create(@NonNull String name, int priority) {
        return new Category(null, name, priority);
}

/**
//Synthetic comment -- @@ -69,17 +65,12 @@
*
* @param parent the name of a parent category, or null
* @param name the name of the category
* @param priority a sorting priority, with higher being more important
* @return a new category
*/
@NonNull
    public static Category create(@Nullable Category parent, @NonNull String name, int priority) {
        return new Category(parent, name, priority);
}

/**
//Synthetic comment -- @@ -101,15 +92,6 @@
}

/**
* Returns a full name for this category. For a top level category, this is just
* the {@link #getName()} value, but for nested categories it will include the parent
* names as well.
//Synthetic comment -- @@ -137,34 +119,34 @@
}

/** Issues related to running lint itself */
    public static final Category LINT = create("Lint", 110);

/** Issues related to correctness */
    public static final Category CORRECTNESS = create("Correctness", 100);

/** Issues related to security */
    public static final Category SECURITY = create("Security", 90);

/** Issues related to performance */
    public static final Category PERFORMANCE = create("Performance", 80);

/** Issues related to usability */
    public static final Category USABILITY = create("Usability", 70);

/** Issues related to accessibility */
    public static final Category A11Y = create("Accessibility", 60);

/** Issues related to internationalization */
    public static final Category I18N = create("Internationalization", 50);

// Sub categories

/** Issues related to icons */
    public static final Category ICONS = create(USABILITY, "Icons", 73);

/** Issues related to typography */
    public static final Category TYPOGRAPHY = create(USABILITY, "Typography", 76);

/** Issues related to messages/strings */
    public static final Category MESSAGES = create(CORRECTNESS, "Messages", 95);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 2b3ce34..800e969 100644

//Synthetic comment -- @@ -51,9 +51,9 @@
public class ClassContext extends Context {
private final File mBinDir;
/** The class file DOM root node */
    private final ClassNode mClassNode;
/** The class file byte data */
    private final byte[] mBytes;
/** The source file, if known/found */
private File mSourceFile;
/** The contents of the source file, if source file is known/found */
//Synthetic comment -- @@ -444,7 +444,7 @@
if (node.methods != null && !node.methods.isEmpty()) {
MethodNode firstMethod = getFirstRealMethod(node);
if (firstMethod != null) {
                return findLineNumber(firstMethod);
}
}

//Synthetic comment -- @@ -498,7 +498,7 @@
}
}

            if (!classNode.methods.isEmpty()) {
return (MethodNode) classNode.methods.get(0);
}
}
//Synthetic comment -- @@ -605,7 +605,7 @@
* @return a user-readable string
*/
public static String createSignature(String owner, String name, String desc) {
        StringBuilder sb = new StringBuilder(100);

if (desc != null) {
Type returnType = Type.getReturnType(desc);
//Synthetic comment -- @@ -659,7 +659,7 @@
@NonNull
public static String getInternalName(@NonNull String fqcn) {
String[] parts = fqcn.split("\\."); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder(fqcn.length());
String prev = null;
for (String part : parts) {
if (prev != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Context.java
//Synthetic comment -- index 40c9d1f..a379dd0 100644

//Synthetic comment -- @@ -333,7 +333,7 @@
}

/** Pattern for version qualifiers */
    private static final Pattern VERSION_PATTERN = Pattern.compile("^v(\\d+)$"); //$NON-NLS-1$

private static File sCachedFolder = null;
private static int sCachedFolderVersion = -1;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index e2c5907..443746d 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;

import lombok.ast.Node;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
//Synthetic comment -- @@ -115,7 +116,7 @@
* @return the list of applicable node types (AST node classes), or null
*/
@Nullable
        List<Class<? extends Node>> getApplicableNodeTypes();

/**
* Return the list of method names this detector is interested in, or
//Synthetic comment -- @@ -129,7 +130,7 @@
* This makes it easy to write detectors that focus on some fixed calls.
* For example, the StringFormatDetector uses this mechanism to look for
* "format" calls, and when found it looks around (using the AST's
         * {@link Node#getParent()} method) to see if it's called on
* a String class instance, and if so do its normal processing. Note
* that since it doesn't need to do any other AST processing, that
* detector does not actually supply a visitor.
//Synthetic comment -- @@ -190,7 +191,7 @@
void visitResourceReference(
@NonNull JavaContext context,
@Nullable AstVisitor visitor,
                @NonNull Node node,
@NonNull String type,
@NonNull String name,
boolean isFramework);
//Synthetic comment -- @@ -367,7 +368,7 @@
* invoked on all elements or all attributes
*/
@NonNull
        List<String> ALL = new ArrayList<String>(0); // NOT Collections.EMPTY!
// We want to distinguish this from just an *empty* list returned by the caller!
}

//Synthetic comment -- @@ -477,7 +478,7 @@
public void visitDocument(@NonNull XmlContext context, @NonNull Document document) {
// This method must be overridden if your detector does
// not return something from getApplicableElements or
        // getApplicableAttributes
assert false;
}

//Synthetic comment -- @@ -524,7 +525,7 @@
}

@Nullable @SuppressWarnings("javadoc")
    public List<Class<? extends Node>> getApplicableNodeTypes() {
return null;
}

//Synthetic comment -- @@ -540,7 +541,7 @@

@SuppressWarnings("javadoc")
public void visitResourceReference(@NonNull JavaContext context, @Nullable AstVisitor visitor,
            @NonNull Node node, @NonNull String type, @NonNull String name,
boolean isFramework) {
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index 70d3cf7..e5856b9 100644

//Synthetic comment -- @@ -521,7 +521,7 @@
return sb.toString();
}

    private static void appendEscapedText(StringBuilder sb, String text, boolean html,
int start, int end) {
if (html) {
for (int i = start; i < end; i++) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 17ea37e..9dec569 100644

//Synthetic comment -- @@ -58,6 +58,10 @@
*/
@Beta
public class LintUtils {
    // Utility class, do not instantiate
    private LintUtils() {
    }

/**
* Format a list of strings, and cut of the list at {@code maxItems} if the
* number of items are greater.
//Synthetic comment -- @@ -335,7 +339,7 @@
*
* @param path the path variable to split, which can use both : and ; as
*            path separators.
     * @return the individual path components as an Iterable of strings
*/
public static Iterable<String> splitPath(String path) {
if (path.indexOf(';') != -1) {
//Synthetic comment -- @@ -447,7 +451,7 @@
return PositionXmlParser.getXmlString(bytes);
}

        return getEncodedString(bytes);
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index 183e7c1..e255ede 100644

//Synthetic comment -- @@ -172,7 +172,7 @@
@Override
public String toString() {
return "Location [file=" + mFile + ", start=" + mStart + ", end=" + mEnd + ", message="
                + mMessage + ']';
}

/**
//Synthetic comment -- @@ -256,7 +256,7 @@
}
prev = c;
}
        return create(file);
}

/**
//Synthetic comment -- @@ -298,7 +298,7 @@
while (currentLine < line) {
offset = contents.indexOf('\n', offset);
if (offset == -1) {
                return create(file);
}
currentLine++;
offset++;
//Synthetic comment -- @@ -306,13 +306,12 @@

if (line == currentLine) {
if (patternStart != null) {
SearchDirection direction = SearchDirection.NEAREST;
if (hints != null) {
direction = hints.mDirection;
}

                int index;
if (direction == SearchDirection.BACKWARD) {
index = findPreviousMatch(contents, offset, patternStart, hints);
line = adjustLine(contents, line, offset, index);
//Synthetic comment -- @@ -379,7 +378,7 @@
return new Location(file, position, position);
}

        return create(file);
}

private static int findPreviousMatch(@NonNull String contents, int offset, String pattern,
//Synthetic comment -- @@ -526,7 +525,7 @@
* actual locations later (if needed). This makes it possible to for example
* delay looking up line numbers, for locations that are offset based.
*/
    public interface Handle {
/**
* Compute a full location for the given handle
*
//Synthetic comment -- @@ -542,7 +541,7 @@
*
* @param clientData the data to store with this location
*/
        void setClientData(@Nullable Object clientData);

/**
* Returns the client data associated with this location - an optional field
//Synthetic comment -- @@ -552,15 +551,15 @@
* @return the data associated with this location
*/
@Nullable
        Object getClientData();
}

/** A default {@link Handle} implementation for simple file offsets */
public static class DefaultLocationHandle implements Handle {
        private final File mFile;
        private final String mContents;
        private final int mStartOffset;
        private final int mEndOffset;
private Object mClientData;

/**
//Synthetic comment -- @@ -580,7 +579,7 @@
@Override
@NonNull
public Location resolve() {
            return create(mFile, mContents, mStartOffset, mEndOffset);
}

@Override
//Synthetic comment -- @@ -632,7 +631,7 @@
* {@code patternStart} is non null)
*/
@NonNull
        private final SearchDirection mDirection;

/** Whether the matched pattern should be a whole word */
private boolean mWholeWord;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index b2ac0d1..c31a499 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
for (int i = 1; i < 1000; i++) {
String key = String.format(ANDROID_LIBRARY_REFERENCE_FORMAT, i);
String library = properties.getProperty(key);
                        if (library == null || library.isEmpty()) {
// No holes in the numbering sequence is allowed
break;
}
//Synthetic comment -- @@ -171,13 +171,13 @@
// the reference dir as well
libraryReferenceDir = libraryReferenceDir.getCanonicalFile();
if (!libraryDir.getPath().startsWith(referenceDir.getPath())) {
                                File file = libraryReferenceDir;
                                while (file != null && !file.getPath().isEmpty()) {
                                    if (libraryDir.getPath().startsWith(file.getPath())) {
                                        libraryReferenceDir = file;
break;
}
                                    file = file.getParentFile();
}
}
}
//Synthetic comment -- @@ -206,7 +206,7 @@

@Override
public String toString() {
        return "Project [dir=" + mDir + ']';
}

@Override
//Synthetic comment -- @@ -570,7 +570,7 @@
@NonNull
public List<Project> getAllLibraries() {
if (mAllLibraries == null) {
            if (mDirectLibraries.isEmpty()) {
return mDirectLibraries;
}

//Synthetic comment -- @@ -776,7 +776,7 @@
}
}

        if (sources.isEmpty()) {
mClient.log(null,
"Warning: Could not find sources or generated sources for project %1$s",
getName());
//Synthetic comment -- @@ -800,7 +800,7 @@
}
}

        if (classDirs.isEmpty()) {
mClient.log(null,
"No bytecode found: Has the project been built? (%1$s)", getName());
}
//Synthetic comment -- @@ -886,7 +886,7 @@
private static int sCurrentVersion;

/** In an AOSP build environment, identify the currently built image version, if available */
    private static int findCurrentAospVersion() {
if (sCurrentVersion < 1) {
File apiDir = new File(getAospTop(), "frameworks/base/api" //$NON-NLS-1$
.replace('/', File.separatorChar));








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Severity.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Severity.java
//Synthetic comment -- index cde61bd..f74e6b5 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
@NonNull
private final String mDisplay;

    Severity(@NonNull String display) {
mDisplay = display;
}

//Synthetic comment -- @@ -71,7 +71,8 @@
*
* @return a description of the severity
*/
    @NonNull
    public String getDescription() {
return mDisplay;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Speed.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Speed.java
//Synthetic comment -- index 8c20a19..c68dab0 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
/** The detector might take a long time to run */
SLOW("Slow");

    private final String mDisplayName;

Speed(@NonNull String displayName) {
mDisplayName = displayName;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AccessibilityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AccessibilityDetector.java
//Synthetic comment -- index 3572e89..4d069cc 100644

//Synthetic comment -- @@ -78,8 +78,9 @@
public AccessibilityDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -120,7 +121,7 @@
} else {
Attr attributeNode = element.getAttributeNodeNS(ANDROID_URI, ATTR_CONTENT_DESCRIPTION);
String attribute = attributeNode.getValue();
            if (attribute.isEmpty() || attribute.equals("TODO")) { //$NON-NLS-1$
context.report(ISSUE, attributeNode, context.getLocation(attributeNode),
"[Accessibility] Empty contentDescription attribute on image", null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AlwaysShowActionDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AlwaysShowActionDetector.java
//Synthetic comment -- index 1406ada..34a3f10 100644

//Synthetic comment -- @@ -88,7 +88,7 @@
/** List of locations of MenuItem.SHOW_AS_ACTION_ALWAYS references in Java code */
private List<Location> mAlwaysFields;
/** True if references to MenuItem.SHOW_AS_ACTION_IF_ROOM were found */
    private boolean mHasIfRoomRefs;

/** Constructs a new {@link AlwaysShowActionDetector} */
public AlwaysShowActionDetector() {
//Synthetic comment -- @@ -99,8 +99,9 @@
return folderType == ResourceFolderType.MENU;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -121,7 +122,7 @@
return;
}
if (mFileAttributes != null) {
            assert context instanceof XmlContext; // mFileAttributes is only set in XML files

List<Attr> always = new ArrayList<Attr>();
List<Attr> ifRoom = new ArrayList<Attr>();
//Synthetic comment -- @@ -145,11 +146,11 @@
}
}

            if (!always.isEmpty() && mFileAttributes.size() > 1) {
// Complain if you're using more than one "always", or if you're
// using "always" and aren't using "ifRoom" at all (and provided you
// have more than a single item)
                if (always.size() > 2 || ifRoom.isEmpty()) {
XmlContext xmlContext = (XmlContext) context;
Location location = null;
for (int i = always.size() - 1; i >= 0; i--) {
//Synthetic comment -- @@ -196,7 +197,7 @@

@Override
public
    List<Class<? extends Node>> getApplicableNodeTypes() {
return Collections.<Class<? extends Node>>singletonList(Select.class);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/AnnotationDetector.java
//Synthetic comment -- index 9982f18..2d2a2ef 100644

//Synthetic comment -- @@ -84,8 +84,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -93,7 +94,7 @@

@Override
public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(Annotation.class);
}

@Override
//Synthetic comment -- @@ -176,8 +177,8 @@
// This issue doesn't have AST access: annotations are not
// available for local variables or parameters
mContext.report(ISSUE,mContext.getLocation(node), String.format(
                    "The @SuppressLint annotation cannot be used on a local " +
                    "variable with the lint check '%1$s': move out to the " +
"surrounding method", id),
null);
return false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiClass.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiClass.java
//Synthetic comment -- index 365de80..f8564f2 100644

//Synthetic comment -- @@ -77,7 +77,7 @@
// The field can come from this class or from a super class or an interface
// The value can never be lower than this introduction of this class.
// When looking at super classes and interfaces, it can never be lower than when the
        // super class or interface was added as a super class or interface to this class.
// Look at all the values and take the lowest.
// For instance:
// This class A is introduced in 5 with super class B.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index aff9705..d2a7844 100644

//Synthetic comment -- @@ -151,8 +151,9 @@
public ApiDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

//Synthetic comment -- @@ -190,7 +191,7 @@

String value = attribute.getValue();

        String prefix;
if (value.startsWith(ANDROID_PREFIX)) {
prefix = ANDROID_PREFIX;
} else if (value.startsWith(ANDROID_THEME_PREFIX)) {
//Synthetic comment -- @@ -198,7 +199,6 @@
} else {
return;
}

// Convert @android:type/foo into android/R$type and "foo"
int index = value.indexOf('/', prefix.length());
//Synthetic comment -- @@ -318,7 +318,7 @@

@SuppressWarnings("rawtypes") // ASM API
@Override
    public void checkClass(@NonNull final ClassContext context, @NonNull ClassNode classNode) {
if (mApiDatabase == null) {
return;
}
//Synthetic comment -- @@ -575,7 +575,7 @@
}
}

    private static void checkSimpleDateFormat(ClassContext context, MethodNode method,
MethodInsnNode node, int minSdk) {
if (minSdk >= 9) {
// Already OK
//Synthetic comment -- @@ -612,7 +612,7 @@
}

@SuppressWarnings("rawtypes") // ASM API
    private static boolean methodDefinedLocally(ClassNode classNode, String name, String desc) {
List methodList = classNode.methods;
for (Object m : methodList) {
MethodNode method = (MethodNode) m;
//Synthetic comment -- @@ -625,8 +625,9 @@
}

@SuppressWarnings("rawtypes") // ASM API
    private static void checkSwitchBlock(ClassContext context, ClassNode classNode,
            FieldInsnNode field, MethodNode method, String name, String owner, int api,
            int minSdk) {
// Switch statements on enums are tricky. The compiler will generate a method
// which returns an array of the enum constants, indexed by their ordinal() values.
// However, we only want to complain if the code is actually referencing one of
//Synthetic comment -- @@ -688,7 +689,7 @@
if (next == null) {
return;
}
        int ordinal;
switch (next.getOpcode()) {
case Opcodes.ICONST_0: ordinal = 0; break;
case Opcodes.ICONST_1: ordinal = 1; break;
//Synthetic comment -- @@ -753,7 +754,7 @@
* methods (for anonymous inner classes) or outer classes (for inner classes)
* of the given class.
*/
    private static int getClassMinSdk(ClassContext context, ClassNode classNode) {
int classMinSdk = getLocalMinSdk(classNode.invisibleAnnotations);
if (classMinSdk != -1) {
return classMinSdk;
//Synthetic comment -- @@ -832,7 +833,7 @@
* @param element the element to look at, including parents
* @return the API level to use for this element, or -1
*/
    private static int getLocalMinSdk(@NonNull Element element) {
while (element != null) {
String targetApi = element.getAttributeNS(TOOLS_URI, ATTR_TARGET_API);
if (targetApi != null && !targetApi.isEmpty()) {
//Synthetic comment -- @@ -863,7 +864,7 @@
return -1;
}

    private static void report(final ClassContext context, String message, AbstractInsnNode node,
MethodNode method, String patternStart, String patternEnd, SearchHints hints) {
int lineNumber = node != null ? ClassContext.findLineNumber(node) : -1;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index 17578b8..a056822 100644

//Synthetic comment -- @@ -90,7 +90,6 @@
private byte[] mData;
private int[] mIndices;
private int mClassCount;
private String[] mJavaPackages;

private static WeakReference<ApiLookup> sInstance =
//Synthetic comment -- @@ -155,7 +154,7 @@
* @return a (possibly shared) instance of the API database, or null
*         if its data can't be found
*/
    private static ApiLookup get(LintClient client, File xmlFile) {
if (!xmlFile.exists()) {
client.log(null, "The API database file %1$s does not exist", xmlFile);
return null;
//Synthetic comment -- @@ -233,7 +232,7 @@
/**
* Database format:
* <pre>
     * 1. A file header, which is the exact contents of {@link #FILE_HEADER} encoded
*     as ASCII characters. The purpose of the header is to identify what the file
*     is for, for anyone attempting to open the file.
* 2. A file version number. If the binary file does not match the reader's expected
//Synthetic comment -- @@ -295,7 +294,7 @@
}

mClassCount = buffer.getInt();
            int methodCount = buffer.getInt();

int javaPackageCount = buffer.getInt();
// Read in the Java packages
//Synthetic comment -- @@ -308,7 +307,7 @@
}

// Read in the class table indices;
            int count = mClassCount + methodCount;
int[] offsets = new int[count];

// Another idea: I can just store the DELTAS in the file (and add them up
//Synthetic comment -- @@ -415,7 +414,7 @@
}

// Only include classes that have one or more members requiring version 2 or higher:
            if (!members.isEmpty()) {
classes.add(className);
memberMap.put(apiClass, members);
memberCount += members.size();
//Synthetic comment -- @@ -583,7 +582,7 @@
// For debugging only
private String dumpEntry(int offset) {
if (DEBUG_SEARCH) {
            StringBuilder sb = new StringBuilder(200);
for (int i = offset; i < mData.length; i++) {
if (mData[i] == 0) {
break;
//Synthetic comment -- @@ -800,7 +799,7 @@
return false;
}

    private static int comparePackage(String s1, String s2, int max) {
for (int i = 0; i < max; i++) {
if (i == s1.length()) {
return -1;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiParser.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiParser.java
//Synthetic comment -- index 765f0d4..b3c2f2a 100644

//Synthetic comment -- @@ -29,15 +29,15 @@
*/
public class ApiParser extends DefaultHandler {

    private static final String NODE_API = "api";
    private static final String NODE_CLASS = "class";
    private static final String NODE_FIELD = "field";
    private static final String NODE_METHOD = "method";
    private static final String NODE_EXTENDS = "extends";
    private static final String NODE_IMPLEMENTS = "implements";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_SINCE = "since";

private final Map<String, ApiClass> mClasses = new HashMap<String, ApiClass>();

//Synthetic comment -- @@ -54,7 +54,7 @@
public void startElement(String uri, String localName, String qName, Attributes attributes)
throws SAXException {

        if (localName == null || localName.isEmpty()) {
localName = qName;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 4f63913..8bbcffb 100644

//Synthetic comment -- @@ -180,9 +180,9 @@
// Make sure we still have a conflict, in case one or more of the
// elements were marked with tools:ignore
int count = -1;
LintDriver driver = context.getDriver();
boolean foundConflict = false;
                    Location curr;
for (curr = location; curr != null; curr = curr.getSecondary()) {
Object clientData = curr.getClientData();
if (clientData instanceof Node) {
//Synthetic comment -- @@ -226,7 +226,7 @@
int phase = context.getPhase();

Attr attribute = element.getAttributeNode(ATTR_NAME);
        if (attribute == null || attribute.getValue().isEmpty()) {
if (phase != 1) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 39a5ae3..71866c3 100644

//Synthetic comment -- @@ -216,8 +216,9 @@
public BuiltinIssueRegistry() {
}

    @NonNull
@Override
    public List<Issue> getIssues() {
return sIssues;
}

//Synthetic comment -- @@ -251,7 +252,7 @@
}

String lintClassPath = System.getenv("ANDROID_LINT_JARS"); //$NON-NLS-1$
        if (lintClassPath != null && !lintClassPath.isEmpty()) {
String[] paths = lintClassPath.split(File.pathSeparator);
for (String path : paths) {
File jarFile = new File(path);
//Synthetic comment -- @@ -328,6 +329,10 @@
// the primary purpose right now is to allow for example the HTML report
// to give a hint to the user that some fixes don't require manual work

        return getIssuesWithFixes().contains(issue);
    }

    private static Set<Issue> getIssuesWithFixes() {
if (sAdtFixes == null) {
sAdtFixes = new HashSet<Issue>(25);
sAdtFixes.add(InefficientWeightDetector.INEFFICIENT_WEIGHT);
//Synthetic comment -- @@ -356,7 +361,7 @@
sAdtFixes.add(DosLineEndingDetector.ISSUE);
}

        return sAdtFixes;
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index d7aa4d4..e756e03 100644

//Synthetic comment -- @@ -198,8 +198,9 @@
public ButtonDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -223,7 +224,7 @@
}
}

    private static String stripLabel(String text) {
text = text.trim();
if (text.length() > 2
&& (text.charAt(0) == '"' || text.charAt(0) == '\'')
//Synthetic comment -- @@ -361,7 +362,7 @@
}
}

    private static boolean parentDefinesSelectableItem(Element element) {
String background = element.getAttributeNS(ANDROID_URI, ATTR_BACKGROUND);
if (VALUE_SELECTABLE_ITEM_BACKGROUND.equals(background)) {
return true;
//Synthetic comment -- @@ -390,7 +391,7 @@
* TODO: Add in patterns for other languages. We can use the
* @android:string/ok and @android:string/cancel localizations to look
* up the canonical ones. */
    private static boolean isEnglishResource(XmlContext context) {
String folder = context.file.getParentFile().getName();
if (folder.indexOf('-') != -1) {
String[] qualifiers = folder.split("-"); //$NON-NLS-1$
//Synthetic comment -- @@ -445,7 +446,7 @@
Node child = childNodes.item(i);
if (child.getNodeType() == Node.TEXT_NODE) {
String text = stripLabel(child.getNodeValue());
                    if (!text.isEmpty()) {
mKeyToLabel.put(itemName, text);
break;
}
//Synthetic comment -- @@ -548,7 +549,7 @@
* Sort a list of label buttons into the expected order (Cancel on the left,
* OK on the right
*/
    private static void sortButtons(List<String> labelList) {
for (int i = 0, n = labelList.size(); i < n; i++) {
String label = labelList.get(i);
if (label.equalsIgnoreCase(CANCEL_LABEL) && i > 0) {
//Synthetic comment -- @@ -569,8 +570,8 @@
}

/** Creates a display string for a list of button labels, such as "Cancel | OK" */
    private static String describeButtons(List<String> labelList) {
        StringBuilder sb = new StringBuilder(80);
for (String label : labelList) {
if (sb.length() > 0) {
sb.append(" | "); //$NON-NLS-1$
//Synthetic comment -- @@ -639,7 +640,7 @@
return isWrongPosition(element, false /*isCancel*/);
}

    private static boolean isInButtonBar(Element element) {
assert element.getTagName().equals(BUTTON) : element.getTagName();
Node parentNode = element.getParentNode();
if (parentNode.getNodeType() != Node.ELEMENT_NODE) {
//Synthetic comment -- @@ -682,7 +683,7 @@
}

/** Is the given button in the wrong position? */
    private static boolean isWrongPosition(Element element, boolean isCancel) {
Node parentNode = element.getParentNode();
if (parentNode.getNodeType() != Node.ELEMENT_NODE) {
return false;
//Synthetic comment -- @@ -769,7 +770,7 @@
}

/** Is the given target id the id of a {@code <Button>} within this RelativeLayout? */
    private static boolean isButtonId(Element parent, String targetId) {
for (Element child : LintUtils.getChildren(parent)) {
String id = child.getAttributeNS(ANDROID_URI, ATTR_ID);
if (LintUtils.idReferencesMatch(id, targetId)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ChildCountDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ChildCountDetector.java
//Synthetic comment -- index d4aa1f0..bc1e30d 100644

//Synthetic comment -- @@ -74,8 +74,9 @@
public ChildCountDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index c659ab1..622af71 100644

//Synthetic comment -- @@ -66,8 +66,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CommentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CommentDetector.java
//Synthetic comment -- index 0e5d8d0..6f07e77 100644

//Synthetic comment -- @@ -86,8 +86,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}

//Synthetic comment -- @@ -164,7 +165,7 @@
int start,
int end) {
char prev = 0;
        char c;
for (int i = start; i < end - 2; i++, prev = c) {
c = source.charAt(i);
if (prev == '\\') {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ControlFlowGraph.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ControlFlowGraph.java
//Synthetic comment -- index 5f5b2fe..cbafe28 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
*/
@NonNull
public String toString(boolean includeAdjacent) {
            StringBuilder sb = new StringBuilder(100);

sb.append(getId(instruction));
sb.append(':');
//Synthetic comment -- @@ -164,7 +164,7 @@
//sb.append('L' + l.getLabel().info + ":");
sb.append("LABEL");
} else if (instruction instanceof LineNumberNode) {
                sb.append("LINENUMBER ").append(((LineNumberNode)instruction).line);
} else if (instruction instanceof FrameNode) {
sb.append("FRAME");
} else {
//Synthetic comment -- @@ -172,14 +172,14 @@
// AbstractVisitor isn't available unless debug/util is included,
boolean printed = false;
try {
                    Class<?> cls = Class.forName("org.objectweb.asm.util"); //$NON-NLS-1$
                    Field field = cls.getField("OPCODES");
String[] OPCODES = (String[]) field.get(null);
printed = true;
if (opcode > 0 && opcode <= OPCODES.length) {
sb.append(OPCODES[opcode]);
if (instruction.getType() == AbstractInsnNode.METHOD_INSN) {
                            sb.append('(').append(((MethodInsnNode)instruction).name).append(')');
}
}
} catch (Throwable t) {
//Synthetic comment -- @@ -187,7 +187,7 @@
}
if (!printed) {
if (instruction.getType() == AbstractInsnNode.METHOD_INSN) {
                        sb.append('(').append(((MethodInsnNode)instruction).name).append(')');
} else {
sb.append(instruction.toString());
}
//Synthetic comment -- @@ -197,17 +197,17 @@
if (includeAdjacent) {
if (successors != null && !successors.isEmpty()) {
sb.append(" Next:");
                    for (Node successor : successors) {
sb.append(' ');
                        sb.append(successor.toString(false));
}
}

if (exceptions != null && !exceptions.isEmpty()) {
sb.append(" Exceptions:");
                    for (Node exception : exceptions) {
sb.append(' ');
                        sb.append(exception.toString(false));
}
}
sb.append('\n');
//Synthetic comment -- @@ -278,9 +278,9 @@
*/
@NonNull
public String toString(@Nullable Node start) {
        StringBuilder sb = new StringBuilder(400);

        AbstractInsnNode curr;
if (start != null) {
curr = start.instruction;
} else {
//Synthetic comment -- @@ -295,9 +295,9 @@
}

while (curr != null) {
            Node node = mNodeMap.get(curr);
            if (node != null) {
                sb.append(node.toString(true));
}
curr = curr.getNext();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CutPasteDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CutPasteDetector.java
//Synthetic comment -- index 193ab8f..84fb6b6 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
"CutPasteId", //$NON-NLS-1$
            "Looks for code cut & paste mistakes in findViewById() calls",

"This lint check looks for cases where you have cut & pasted calls to " +
"`findViewById` but have forgotten to update the R.id field. It's possible " +
//Synthetic comment -- @@ -183,8 +183,8 @@
}

private static class ReachableVisitor extends ForwardingAstVisitor {
        @NonNull private final MethodInvocation mFrom;
        @NonNull private final MethodInvocation mTo;
private boolean mReachable;
private boolean mSeenEnd;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DeprecationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DeprecationDetector.java
//Synthetic comment -- index 86dcef4..6475a7a 100644

//Synthetic comment -- @@ -66,8 +66,9 @@
public DeprecationDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DetectMissingPrefix.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DetectMissingPrefix.java
//Synthetic comment -- index 10f2f56..2b24732 100644

//Synthetic comment -- @@ -103,8 +103,9 @@
|| folderType == INTERPOLATOR;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -116,7 +117,7 @@
@Override
public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
String uri = attribute.getNamespaceURI();
        if (uri == null || uri.isEmpty()) {
String name = attribute.getName();
if (name == null) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DosLineEndingDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DosLineEndingDetector.java
//Synthetic comment -- index c2e735c..1a2a720 100644

//Synthetic comment -- @@ -56,8 +56,9 @@
public DosLineEndingDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index 48e8661..de3e4d2 100644

//Synthetic comment -- @@ -108,8 +108,9 @@
return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.MENU;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -152,7 +153,7 @@
public void afterCheckProject(@NonNull Context context) {
if (context.getPhase() == 1) {
// Look for duplicates
            if (!mIncludes.isEmpty()) {
// Traverse all the include chains and ensure that there are no duplicates
// across.
if (context.isEnabled(CROSS_LAYOUT)
//Synthetic comment -- @@ -226,11 +227,11 @@
assert context.getPhase() == 2;

Collection<Multimap<String, Occurrence>> maps = mLocations.get(context.file);
                if (maps != null && !maps.isEmpty()) {
for (Multimap<String, Occurrence> map : maps) {
                        if (!maps.isEmpty()) {
Collection<Occurrence> occurrences = map.get(layout);
                            if (occurrences != null && !occurrences.isEmpty()) {
for (Occurrence occurrence : occurrences) {
Location location = context.getLocation(element);
location.setClientData(element);
//Synthetic comment -- @@ -274,11 +275,11 @@
}
} else {
Collection<Multimap<String, Occurrence>> maps = mLocations.get(context.file);
            if (maps != null && !maps.isEmpty()) {
for (Multimap<String, Occurrence> map : maps) {
                    if (!maps.isEmpty()) {
Collection<Occurrence> occurrences = map.get(id);
                        if (occurrences != null && !occurrences.isEmpty()) {
for (Occurrence occurrence : occurrences) {
if (context.getDriver().isSuppressed(CROSS_LAYOUT, attribute)) {
return;
//Synthetic comment -- @@ -297,7 +298,7 @@
}

/** Find the first id attribute with the given value below the given node */
    private static Attr findIdAttribute(Node node, String targetValue) {
if (node.getNodeType() == Node.ELEMENT_NODE) {
Attr attribute = ((Element) node).getAttributeNodeNS(ANDROID_URI, ATTR_ID);
if (attribute != null && attribute.getValue().equals(targetValue)) {
//Synthetic comment -- @@ -319,10 +320,10 @@

/** Include Graph Node */
private static class Layout {
        private final File mFile;
        private final Set<String> mIds;
private List<Layout> mIncludes;
private List<Layout> mIncludedBy;

Layout(File file, Set<String> ids) {
mFile = file;
//Synthetic comment -- @@ -354,7 +355,7 @@
}

boolean isIncluded() {
            return mIncludedBy != null && !mIncludedBy.isEmpty();
}

File getFile() {
//Synthetic comment -- @@ -390,7 +391,7 @@
}
for (File file : mFileToIds.keySet()) {
Set<String> ids = mFileToIds.get(file);
                if (ids != null && !ids.isEmpty()) {
if (!mFileToLayout.containsKey(file)) {
mFileToLayout.put(file, new Layout(file, ids));
}
//Synthetic comment -- @@ -411,7 +412,7 @@
List<String> includedLayouts = mIncludes.get(file);
for (String name : includedLayouts) {
Collection<Layout> layouts = nameToLayout.get(name);
                    if (layouts != null && !layouts.isEmpty()) {
if (layouts.size() == 1) {
from.include(layouts.iterator().next());
} else {
//Synthetic comment -- @@ -609,7 +610,7 @@

Set<String> layoutIds = layout.getIds();
if (layoutIds != null && layoutIds.contains(id)) {
                StringBuilder path = new StringBuilder(80);

if (!stack.isEmpty()) {
Iterator<Layout> iterator = stack.descendingIterator();
//Synthetic comment -- @@ -622,7 +623,7 @@
path.append(" defines ");
path.append(id);

                assert occurrences.get(layout) == null : id + ',' + layout;
occurrences.put(layout, new Occurrence(layout.getFile(), null, path.toString()));
}

//Synthetic comment -- @@ -641,11 +642,11 @@
}

private static class Occurrence implements Comparable<Occurrence> {
        public final File file;
        public final String includePath;
public Occurrence next;
public Location location;
public String message;

public Occurrence(File file, String message, String includePath) {
this.file = file;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/DuplicateResourceDetector.java
//Synthetic comment -- index fce4633..004303c 100644

//Synthetic comment -- @@ -82,8 +82,9 @@
public DuplicateResourceDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ExtraTextDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ExtraTextDetector.java
//Synthetic comment -- index f76b5cc..e0781db 100644

//Synthetic comment -- @@ -69,8 +69,9 @@
|| folderType == ResourceFolderType.COLOR;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index a74b8ec..04841ab 100644

//Synthetic comment -- @@ -77,8 +77,9 @@
public FieldGetterDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -153,7 +154,7 @@
}

Map<String, String> getters = checkMethods(context.getClassNode(), names);
            if (!getters.isEmpty()) {
for (String getter : getters.keySet()) {
for (Entry entry : mPendingCalls) {
String name = entry.name;
//Synthetic comment -- @@ -193,7 +194,7 @@
}

// Validate that these getter methods are really just simple field getters
    // like these int and String getters:
// public int getFoo();
//   Code:
//    0:   aload_0








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 954872d..932d307 100644

//Synthetic comment -- @@ -75,8 +75,9 @@
public FragmentDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/GridLayoutDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/GridLayoutDetector.java
//Synthetic comment -- index 34cc089..c348502 100644

//Synthetic comment -- @@ -60,8 +60,9 @@
public GridLayoutDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -74,7 +75,7 @@

private static int getInt(Element element, String attribute, int defaultValue) {
String valueString = element.getAttributeNS(ANDROID_URI, attribute);
        if (valueString != null && !valueString.isEmpty()) {
try {
return Integer.decode(valueString);
} catch (NumberFormatException nufe) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 7d5a5e5..cfe8f0b 100644

//Synthetic comment -- @@ -57,8 +57,9 @@
public HandlerDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index 3bd913c..fd678ca 100644

//Synthetic comment -- @@ -65,8 +65,9 @@
public HardcodedDebugModeDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedValuesDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/HardcodedValuesDetector.java
//Synthetic comment -- index 74522ba..11cc19d 100644

//Synthetic comment -- @@ -71,8 +71,9 @@
public HardcodedValuesDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -99,7 +100,7 @@
@Override
public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
String value = attribute.getValue();
        if (!value.isEmpty() && (value.charAt(0) != '@' && value.charAt(0) != '?')) {
// Make sure this is really one of the android: attributes
if (!ANDROID_URI.equals(attribute.getNamespaceURI())) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 011c291..4d8b3a4 100644

//Synthetic comment -- @@ -357,8 +357,9 @@
public IconDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

//Synthetic comment -- @@ -443,7 +444,7 @@
checkDuplicates(context, pixelSizes, fileSizes);
}

                if (checkFolders && !folderToNames.isEmpty()) {
checkDensities(context, res, folderToNames, nonDpiFolderNames);
}
}
//Synthetic comment -- @@ -459,7 +460,7 @@
// This method looks for duplicates in the assets. This uses two pieces of information
// (file sizes and image dimensions) to quickly reject candidates, such that it only
// needs to check actual file contents on a small subset of the available files.
    private static void checkDuplicates(Context context, Map<File, Dimension> pixelSizes,
Map<File, Long> fileSizes) {
Map<Long, Set<File>> sameSizes = new HashMap<Long, Set<File>>();
Map<Long, File> seenSizes = new HashMap<Long, File>(fileSizes.size());
//Synthetic comment -- @@ -479,7 +480,7 @@
}
}

        if (sameSizes.isEmpty()) {
return;
}

//Synthetic comment -- @@ -509,8 +510,8 @@

// Files that we have no dimensions for must be compared against everything
Collection<Set<File>> sets = sameDimensions.values();
            if (!noSize.isEmpty()) {
                if (!sets.isEmpty()) {
for (Set<File> set : sets) {
set.addAll(noSize);
}
//Synthetic comment -- @@ -588,7 +589,7 @@
}
}

                if (!equal.isEmpty()) {
Map<File, Set<File>> partitions = new HashMap<File, Set<File>>();
List<Set<File>> sameSets = new ArrayList<Set<File>>();
for (Map.Entry<File, File> entry : equal.entrySet()) {
//Synthetic comment -- @@ -614,7 +615,7 @@
// for stable output.
List<List<File>> lists = new ArrayList<List<File>>();
for (Set<File> same : sameSets) {
                        assert !same.isEmpty();
ArrayList<File> sorted = new ArrayList<File>(same);
Collections.sort(sorted);
lists.add(sorted);
//Synthetic comment -- @@ -643,7 +644,7 @@
}

if (sameNames) {
                            StringBuilder sb = new StringBuilder(sameFiles.size() * 16);
for (File file : sameFiles) {
if (sb.length() > 0) {
sb.append(", "); //$NON-NLS-1$
//Synthetic comment -- @@ -655,7 +656,7 @@
lastName, sb.toString());
context.report(DUPLICATES_CONFIGURATIONS, location, message, null);
} else {
                            StringBuilder sb = new StringBuilder(sameFiles.size() * 16);
for (File file : sameFiles) {
if (sb.length() > 0) {
sb.append(", "); //$NON-NLS-1$
//Synthetic comment -- @@ -677,7 +678,7 @@
// This method checks the given map from resource file to pixel dimensions for each
// such image and makes sure that the normalized dip sizes across all the densities
// are mostly the same.
    private static void checkDipSizes(Context context, Map<File, Dimension> pixelSizes) {
// Partition up the files such that I can look at a series by name. This
// creates a map from filename (such as foo.png) to a list of files
// providing that icon in various folders: drawable-mdpi/foo.png, drawable-hdpi/foo.png
//Synthetic comment -- @@ -797,7 +798,7 @@

if (widthStdDev > meanWidth / 10 || heightStdDev > meanHeight) {
Location location = null;
                    StringBuilder sb = new StringBuilder(100);

// Sort entries by decreasing dip size
List<Map.Entry<File, Dimension>> entries =
//Synthetic comment -- @@ -846,7 +847,7 @@
}
}

    private static void checkDensities(Context context, File res,
Map<File, Set<String>> folderToNames,
Map<File, Set<String>> nonDpiFolderNames) {
// TODO: Is there a way to look at the manifest and figure out whether
//Synthetic comment -- @@ -870,7 +871,7 @@
missing.add(density);
}
}
            if (!missing.isEmpty()) {
context.report(
ICON_MISSING_FOLDER,
Location.create(res),
//Synthetic comment -- @@ -888,7 +889,7 @@
noDpiNames.addAll(entry.getValue());
}
}
            if (!noDpiNames.isEmpty()) {
// Make sure that none of the nodpi names appear in a non-nodpi folder
Set<String> inBoth = new HashSet<String>();
List<File> files = new ArrayList<File>();
//Synthetic comment -- @@ -905,7 +906,7 @@
}
}

                if (!inBoth.isEmpty()) {
List<String> list = new ArrayList<String>(inBoth);
Collections.sort(list);

//Synthetic comment -- @@ -1021,7 +1022,7 @@
Set<String> names = entry.getValue();
if (names.size() != allNames.size()) {
List<String> delta = new ArrayList<String>(nameDifferences(allNames,  names));
                    if (delta.isEmpty()) {
continue;
}
Collections.sort(delta);
//Synthetic comment -- @@ -1035,7 +1036,7 @@
defined.add(e.getKey().getName());
}
}
                        if (!defined.isEmpty()) {
foundIn = String.format(" (found in %1$s)",
LintUtils.formatList(defined,
context.getDriver().isAbbreviating() ? 5 : -1));
//Synthetic comment -- @@ -1060,7 +1061,7 @@
* Sets.difference(a, b) because we want to make the comparisons <b>without
* file extensions</b> and return the result <b>with</b>..
*/
    private static Set<String> nameDifferences(Set<String> a, Set<String> b) {
Set<String> names1 = new HashSet<String>(a.size());
for (String s : a) {
names1.add(LintUtils.getBaseName(s));
//Synthetic comment -- @@ -1072,7 +1073,7 @@

names1.removeAll(names2);

        if (!names1.isEmpty()) {
// Map filenames back to original filenames with extensions
Set<String> result = new HashSet<String>(names1.size());
for (String s : a) {
//Synthetic comment -- @@ -1097,7 +1098,7 @@
* Sets.intersection(a, b) because we want to make the comparisons <b>without
* file extensions</b> and return the result <b>with</b>.
*/
    private static Set<String> nameIntersection(Set<String> a, Set<String> b) {
Set<String> names1 = new HashSet<String>(a.size());
for (String s : a) {
names1.add(LintUtils.getBaseName(s));
//Synthetic comment -- @@ -1109,7 +1110,7 @@

names1.retainAll(names2);

        if (!names1.isEmpty()) {
// Map filenames back to original filenames with extensions
Set<String> result = new HashSet<String>(names1.size());
for (String s : a) {
//Synthetic comment -- @@ -1404,7 +1405,7 @@
return null;
}

    private static void checkExtension(Context context, File file) {
try {
ImageInputStream input = ImageIO.createImageInputStream(file);
if (input != null) {
//Synthetic comment -- @@ -1527,7 +1528,7 @@
* case if it specifies -v11+, or if the minimum SDK version declared in the
* manifest is at least 11.
*/
    private static boolean isAndroid30(Context context, int folderVersion) {
return folderVersion >= 11 || context.getMainProject().getMinSdk() >= 11;
}

//Synthetic comment -- @@ -1536,7 +1537,7 @@
* case if it specifies -v9 or -v10, or if the minimum SDK version declared in the
* manifest is 9 or 10 (and it does not specify some higher version like -v11
*/
    private static boolean isAndroid23(Context context, int folderVersion) {
if (isAndroid30(context, folderVersion)) {
return false;
}
//Synthetic comment -- @@ -1550,7 +1551,7 @@
return minSdk == 9 || minSdk == 10;
}

    private static float getMdpiScalingFactor(String folderName) {
// Can't do startsWith(DRAWABLE_MDPI) because the folder could
// be something like "drawable-sw600dp-mdpi".
if (folderName.contains("-mdpi")) {            //$NON-NLS-1$
//Synthetic comment -- @@ -1566,7 +1567,7 @@
}
}

    private static void checkSize(Context context, String folderName, File file,
int mdpiWidth, int mdpiHeight, boolean exactMatch) {
String fileName = file.getName();
// Only scan .png files (except 9-patch png's) and jpg files
//Synthetic comment -- @@ -1575,8 +1576,8 @@
return;
}

        int width;
        int height;
// Use 3:4:6:8 scaling ratio to look up the other expected sizes
if (folderName.startsWith(DRAWABLE_MDPI)) {
width = mdpiWidth;
//Synthetic comment -- @@ -1622,7 +1623,7 @@
}
}

    private static Dimension getSize(File file) {
try {
ImageInputStream input = ImageIO.createImageInputStream(file);
if (input != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index 39009c6..48e9dc5 100644

//Synthetic comment -- @@ -119,14 +119,15 @@
* Map from element to whether that element has a non-zero linear layout
* weight or has an ancestor which does
*/
    private final Map<Node, Boolean> mInsideWeight = new IdentityHashMap<Node, Boolean>();

/** Constructs a new {@link InefficientWeightDetector} */
public InefficientWeightDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -173,7 +174,7 @@
&& !VALUE_VERTICAL.equals(element.getAttributeNS(ANDROID_URI, ATTR_ORIENTATION))
&& !element.hasAttributeNS(ANDROID_URI, ATTR_BASELINE_ALIGNED)) {
// See if all the children are layouts
            boolean allChildrenAreLayouts = !children.isEmpty();
SdkInfo sdkInfo = context.getClient().getSdkInfo(context.getProject());
for (Element child : children) {
String tagName = child.getTagName();
//Synthetic comment -- @@ -222,7 +223,8 @@
}
}

    private static void checkWrong0Dp(XmlContext context, Element element,
                                      List<Element> children) {
boolean isVertical = false;
String orientation = element.getAttributeNS(ANDROID_URI, ATTR_ORIENTATION);
if (VALUE_VERTICAL.equals(orientation)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InvalidPackageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/InvalidPackageDetector.java
//Synthetic comment -- index 460995d..048f6ae 100644

//Synthetic comment -- @@ -90,14 +90,15 @@
* user has added libraries in this package namespace (such as the
* null annotations jars) we don't flag these.
*/
    private final Set<String> mJavaxLibraryClasses = Sets.newHashSetWithExpectedSize(64);

/** Constructs a new package check */
public InvalidPackageDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

//Synthetic comment -- @@ -110,7 +111,7 @@

@SuppressWarnings("rawtypes") // ASM API
@Override
    public void checkClass(@NonNull final ClassContext context, @NonNull ClassNode classNode) {
if (!context.isFromClassLibrary() || shouldSkip(context.file)) {
return;
}
//Synthetic comment -- @@ -245,7 +246,7 @@
}
}

    private static Object getPackageName(String owner) {
String pkg = owner;
int index = pkg.lastIndexOf('/');
if (index != -1) {
//Synthetic comment -- @@ -255,7 +256,7 @@
return ClassContext.getFqcn(pkg);
}

    private static boolean shouldSkip(File file) {
// No need to do work on this library, which is included in pretty much all new ADT
// projects
if (file.getPath().endsWith("android-support-v4.jar")) { //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index 6ee9b1b..61dec0d 100644

//Synthetic comment -- @@ -145,8 +145,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -168,11 +169,11 @@

private static class PerformanceVisitor extends ForwardingAstVisitor {
private final JavaContext mContext;
        private final boolean mCheckMaps;
        private final boolean mCheckAllocations;
        private final boolean mCheckValueOf;
/** Whether allocations should be "flagged" in the current method */
private boolean mFlagAllocations;

public PerformanceVisitor(JavaContext context) {
mContext = context;
//Synthetic comment -- @@ -307,7 +308,7 @@
*    }
* </pre>
*/
        private static boolean isLazilyInitialized(Node node) {
Node curr = node.getParent();
while (curr != null) {
if (curr instanceof MethodDeclaration) {
//Synthetic comment -- @@ -324,14 +325,14 @@
List<String> assignments = new ArrayList<String>();
AssignmentTracker visitor = new AssignmentTracker(assignments);
ifNode.astStatement().accept(visitor);
                    if (!assignments.isEmpty()) {
List<String> references = new ArrayList<String>();
addReferencedVariables(references, ifNode.astCondition());
                        if (!references.isEmpty()) {
SetView<String> intersection = Sets.intersection(
new HashSet<String>(assignments),
new HashSet<String>(references));
                            return !intersection.isEmpty();
}
}
return false;
//Synthetic comment -- @@ -368,7 +369,7 @@
* Returns whether the given method declaration represents a method
* where allocating objects is not allowed for performance reasons
*/
        private static boolean isBlockedAllocationMethod(MethodDeclaration node) {
return isOnDrawMethod(node) || isOnMeasureMethod(node) || isOnLayoutMethod(node)
|| isLayoutMethod(node);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LabelForDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LabelForDetector.java
//Synthetic comment -- index dea05bd..283e244 100644

//Synthetic comment -- @@ -76,8 +76,9 @@
public LabelForDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LocaleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/LocaleDetector.java
//Synthetic comment -- index e28603b..3bde211 100644

//Synthetic comment -- @@ -106,8 +106,9 @@
public LocaleDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -204,7 +205,7 @@
}
}

    private static class StringValue extends SourceValue {
private final String mString;

StringValue(int size, String string) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index e0cd15b..9762c07 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
"Checks that the <uses-sdk> element appears at most once",

"The `<uses-sdk>` element should appear just once; the tools will *not* merge the " +
            "contents of all the elements so if you split up the attributes across multiple " +
"elements, only one of them will take effect. To fix this, just merge all the " +
"attributes from the various elements into a single <uses-sdk> element.",

//Synthetic comment -- @@ -236,7 +236,7 @@
private int mSeenUsesSdk;

/** Activities we've encountered */
    private final Set<String> mActivities = new HashSet<String>();

/** Permission basenames */
private Map<String, String> mPermissionNames;
//Synthetic comment -- @@ -244,8 +244,9 @@
/** Package declared in the manifest */
private String mPackage;

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index 000b139..8709852 100644

//Synthetic comment -- @@ -61,8 +61,9 @@
public MathDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 3be7815..ada2fbf 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
"Ensures that classes registered in the manifest file are instantiatable",

"Activities, services, broadcast receivers etc. registered in the manifest file " +
        "must be \"instantiatable\" by the system, which means that the class must be " +
"public, it must have an empty public constructor, and if it's an inner class, " +
"it must be a static inner class.",

//Synthetic comment -- @@ -120,8 +120,9 @@
public MissingClassDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/MissingIdDetector.java
//Synthetic comment -- index 174261c..1b79600 100644

//Synthetic comment -- @@ -68,8 +68,9 @@
public MissingIdDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NamespaceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NamespaceDetector.java
//Synthetic comment -- index c834a65..0b6ab02 100644

//Synthetic comment -- @@ -95,14 +95,14 @@

private Map<String, Attr> mUnusedNamespaces;
private boolean mCheckUnused;

  /** Constructs a new {@link NamespaceDetector} */
public NamespaceDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -184,15 +184,15 @@
}

if (haveCustomNamespace) {
          boolean checkCustomAttrs = context.isEnabled(CUSTOMVIEW) && context.getProject().isLibrary();
mCheckUnused = context.isEnabled(UNUSED);

            if (checkCustomAttrs) {
checkCustomNamespace(context, root);
}
checkElement(context, root);

            if (mCheckUnused && !mUnusedNamespaces.isEmpty()) {
for (Map.Entry<String, Attr> entry : mUnusedNamespaces.entrySet()) {
String prefix = entry.getKey();
Attr attribute = entry.getValue();
//Synthetic comment -- @@ -203,13 +203,13 @@
}
}

    private static void checkCustomNamespace(XmlContext context, Element element) {
NamedNodeMap attributes = element.getAttributes();
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
if (attribute.getName().startsWith(XMLNS_PREFIX)) {
String uri = attribute.getValue();
                if (uri != null && !uri.isEmpty() && uri.startsWith(URI_PREFIX)
&& !uri.equals(ANDROID_URI)) {
context.report(CUSTOMVIEW, attribute, context.getLocation(attribute),
"When using a custom namespace attribute in a library project, " +
//Synthetic comment -- @@ -222,7 +222,7 @@
private void checkElement(XmlContext context, Node node) {
if (node.getNodeType() == Node.ELEMENT_NODE) {
if (mCheckUnused) {
                NamedNodeMap attributes = node.getAttributes();
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
String prefix = attribute.getPrefix();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java
//Synthetic comment -- index f32b1f4..4650a8f 100644

//Synthetic comment -- @@ -68,8 +68,9 @@
mVisitingVerticalScroll = 0;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -140,7 +141,7 @@
}
}

    private static boolean isVerticalScroll(Element element) {
String view = element.getTagName();
if (view.equals(GALLERY) || view.equals(HORIZONTAL_SCROLL_VIEW)) {
return false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
//Synthetic comment -- index 86191bf..3ee7e82 100644

//Synthetic comment -- @@ -92,7 +92,7 @@

if (!number.startsWith("+")) {  //$NON-NLS-1$
context.report(ISSUE, context.getLocation(destinationAddress),
                       "To make sure the SMS can be sent by all users, please start the SMS number " +
"with a + and a country code or restrict the code invocation to people in the country " +
"you are targeting.",
null);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index 24eb7ba..559a7ad 100644

//Synthetic comment -- @@ -211,15 +211,16 @@
* pair is a pair of an attribute name to be checked, and the file that
* attribute is referenced in.
*/
    private final List<Pair<String, Location.Handle>> mPending =
new ArrayList<Pair<String,Location.Handle>>();

/** Constructs a new {@link ObsoleteLayoutParamsDetector} */
public ObsoleteLayoutParamsDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -373,7 +374,7 @@
}
}

                StringBuilder sb = new StringBuilder(40);
for (Pair<File, String> include : includes) {
if (sb.length() > 0) {
sb.append(", "); //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 5b2f3f7..2a07a86 100644

//Synthetic comment -- @@ -81,14 +81,15 @@
public OnClickDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
public void afterCheckProject(@NonNull Context context) {
        if (mNames != null && !mNames.isEmpty() && mHaveBytecode) {
List<String> names = new ArrayList<String>(mNames.keySet());
Collections.sort(names);
LintDriver driver = context.getDriver();
//Synthetic comment -- @@ -109,8 +110,7 @@
List<String> similar = mSimilar != null ? mSimilar.get(name) : null;
if (similar != null) {
Collections.sort(similar);
                  message += String.format(" (did you mean %1$s ?)", Joiner.on(", ").join(similar));
}
context.report(ISSUE, location, message, null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index 4994271..e73a4b5 100644

//Synthetic comment -- @@ -163,8 +163,9 @@
return LintUtils.isXmlFile(file) || LintUtils.endsWith(file.getName(), DOT_JAVA);
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -360,7 +361,7 @@
}
}

    private static String getDrawableResource(File drawableFile) {
String resource = drawableFile.getName();
if (endsWith(resource, DOT_XML)) {
resource = resource.substring(0, resource.length() - DOT_XML.length());
//Synthetic comment -- @@ -370,7 +371,7 @@

private void scanBitmap(Context context, Element element) {
String tileMode = element.getAttributeNS(ANDROID_URI, ATTR_TILE_MODE);
        if (!(tileMode.equals(VALUE_DISABLED) || tileMode.isEmpty())) {
if (mValidDrawables != null) {
String resource = getDrawableResource(context.file);
mValidDrawables.remove(resource);
//Synthetic comment -- @@ -385,7 +386,7 @@
}
if (name.startsWith(".")) {  //$NON-NLS-1$
String pkg = context.getProject().getPackage();
            if (pkg != null && !pkg.isEmpty()) {
name = pkg + name;
}
}
//Synthetic comment -- @@ -396,7 +397,7 @@
mActivities.add(name);

String theme = element.getAttributeNS(ANDROID_URI, ATTR_THEME);
        if (theme != null && !theme.isEmpty()) {
if (mActivityToTheme == null) {
mActivityToTheme = new HashMap<String, String>();
}
//Synthetic comment -- @@ -413,7 +414,7 @@
parent = "";
}

        if (parent.isEmpty()) {
int index = styleName.lastIndexOf('.');
if (index != -1) {
parent = styleName.substring(0, index);
//Synthetic comment -- @@ -435,7 +436,7 @@
if (textNode.getNodeType() == Node.TEXT_NODE) {
String text = textNode.getNodeValue();
String trim = text.trim();
                            if (!trim.isEmpty()) {
if (trim.equals(NULL_RESOURCE)
|| trim.equals(TRANSPARENT_COLOR)
|| mValidDrawables != null
//Synthetic comment -- @@ -459,7 +460,6 @@
mBlankThemes = new ArrayList<String>();
}
mBlankThemes.add(resource);
}
}

//Synthetic comment -- @@ -499,7 +499,7 @@
CompilationUnit compilationUnit = (CompilationUnit) node.getParent();
packageName = compilationUnit.astPackageDeclaration().getPackageName();
}
                mClassFqn = (!packageName.isEmpty() ? (packageName + '.') : "") + name;

return false;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverrideDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverrideDetector.java
//Synthetic comment -- index 7e6d440..15e2245 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
EnumSet.of(Scope.ALL_CLASS_FILES));

/** map from owner class name to JVM signatures for its package private methods  */
    private final Map<String, Set<String>> mPackagePrivateMethods = Maps.newHashMap();

/** Map from owner to signature to super class being overridden */
private Map<String, Map<String, String>> mErrors;
//Synthetic comment -- @@ -90,8 +90,9 @@
public OverrideDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index ba7c29f..b1e848b 100644

//Synthetic comment -- @@ -54,9 +54,9 @@
public PrivateKeyDetector() {
}

    private static boolean isPrivateKeyFile(File file) {
if (!file.isFile() ||
            (!LintUtils.endsWith(file.getPath(), "pem") &&  //NON-NLS-1$
!LintUtils.endsWith(file.getPath(), "key"))) { //NON-NLS-1$
return false;
}
//Synthetic comment -- @@ -64,7 +64,7 @@
try {
String firstLine = Files.readFirstLine(file, Charsets.US_ASCII);
return firstLine != null &&
                firstLine.startsWith("---") &&     //NON-NLS-1$
firstLine.contains("PRIVATE KEY"); //NON-NLS-1$
} catch (IOException ex) {
// Don't care
//Synthetic comment -- @@ -73,7 +73,7 @@
return false;
}

    private static void checkFolder(Context context, File dir) {
if (dir.isDirectory()) {
File[] files = dir.listFiles();
if (files != null) {
//Synthetic comment -- @@ -118,8 +118,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateResourceDetector.java
//Synthetic comment -- index c1e405b..d8a4ce0 100644

//Synthetic comment -- @@ -52,8 +52,9 @@
public PrivateResourceDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ProguardDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ProguardDetector.java
//Synthetic comment -- index 5e57849..7762659 100644

//Synthetic comment -- @@ -156,8 +156,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index dbe74c1..ed9447e 100644

//Synthetic comment -- @@ -131,8 +131,9 @@
public PxUsageDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -234,7 +235,7 @@
}
}

    private static void checkStyleItem(XmlContext context, Element item, Node textNode) {
String text = textNode.getNodeValue();
for (int j = text.length() - 1; j > 0; j--) {
char c = text.charAt(j);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RecycleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RecycleDetector.java
//Synthetic comment -- index cad354c..fac98b0 100644

//Synthetic comment -- @@ -264,9 +264,9 @@
* also consider the resource recycled.
*/
private static class RecycleTracker extends Interpreter {
        private static final Value INSTANCE = BasicValue.INT_VALUE; // Only identity matters, not value
        private static final Value RECYCLED = BasicValue.FLOAT_VALUE;
        private static final Value UNKNOWN = BasicValue.UNINITIALIZED_VALUE;

private final ClassContext mContext;
private final MethodNode mMethod;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index e87505e..56d07e7 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
"Ensures that Activities, Services and Content Providers are registered in the manifest",

"Activities, services and content providers should be registered in the " +
        "`AndroidManifest.xml` file using `<activity>`, `<service>` and `<provider>` tags.\n" +
"\n" +
"If your activity is simply a parent class intended to be subclassed by other " +
"\"real\" activities, make it an abstract class.",
//Synthetic comment -- @@ -80,8 +80,9 @@
public RegistrationDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -231,7 +232,7 @@
};

/** Looks up the corresponding framework class a given manifest tag's class should extend */
    private static String tagToClass(String tag) {
for (int i = 0, n = sTags.length; i < n; i++) {
if (sTags[i].equals(tag)) {
return sClasses[i];
//Synthetic comment -- @@ -242,7 +243,7 @@
}

/** Looks up the manifest tag a given framework class should be registered with */
    private static String classToTag(String className) {
for (int i = 0, n = sClasses.length; i < n; i++) {
if (sClasses[i].equals(className)) {
return sTags[i];








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index e31d8a5..861c1be 100644

//Synthetic comment -- @@ -100,29 +100,29 @@
EnumSet.of(Scope.JAVA_FILE, Scope.ALL_RESOURCE_FILES));

/** Map from each style name to parent style */
    @Nullable private Map<String, String> mStyleParents;

/** Set of style names where the style sets the layout width */
    @Nullable private Set<String> mWidthStyles;

/** Set of style names where the style sets the layout height */
    @Nullable private Set<String> mHeightStyles;

/** Set of layout names for layouts that are included by an {@code <include>} tag
* where the width is set on the include */
    @Nullable private Set<String> mIncludedWidths;

/** Set of layout names for layouts that are included by an {@code <include>} tag
* where the height is set on the include */
    @Nullable private Set<String> mIncludedHeights;

/** Set of layout names for layouts that are included by an {@code <include>} tag
* where the width is <b>not</b> set on the include */
    @Nullable private Set<String> mNotIncludedWidths;

/** Set of layout names for layouts that are included by an {@code <include>} tag
* where the height is <b>not</b> set on the include */
    @Nullable private Set<String> mNotIncludedHeights;

/** Whether the width was set in a theme definition */
private boolean mSetWidthInTheme;
//Synthetic comment -- @@ -134,8 +134,9 @@
public RequiredAttributeDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -178,7 +179,7 @@
return isSizeStyle(stripStylePrefix(style), sizeStyles, 0);
}

    private static boolean isFrameworkSizeStyle(String style) {
// The styles Widget.TextView.ListSeparator (and several theme variations, such as
// Widget.Holo.TextView.ListSeparator, Widget.Holo.Light.TextView.ListSeparator, etc)
// define layout_width and layout_height.
//Synthetic comment -- @@ -610,7 +611,6 @@
// it has the same net effect
recordIncludeWidth(layout, true);
recordIncludeHeight(layout, true);
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ScrollViewChildDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ScrollViewChildDetector.java
//Synthetic comment -- index c4db539..81bb522 100644

//Synthetic comment -- @@ -64,8 +64,9 @@
public ScrollViewChildDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SdCardDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SdCardDetector.java
//Synthetic comment -- index 3a155e3..c876b96 100644

//Synthetic comment -- @@ -68,8 +68,9 @@
return true;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecureRandomDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecureRandomDetector.java
//Synthetic comment -- index 4900fea..f1d349b 100644

//Synthetic comment -- @@ -134,7 +134,7 @@
}
}

    private static void checkValidSetSeed(ClassContext context, MethodInsnNode call) {
assert call.name.equals(SET_SEED);

// Make sure the argument passed is not a literal








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 57f4af4..bc8d47b 100644

//Synthetic comment -- @@ -92,7 +92,7 @@
"ExportedContentProvider", //$NON-NLS-1$
"Checks for exported content providers that do not require permissions",
"Content providers are exported by default and any application on the " +
            "system can potentially use them to read and write data. If the content " +
"provider provides access to sensitive data, it should be protected by " +
"specifying `export=false` in the manifest or by protecting it with a " +
"permission that can be granted to other applications.",
//Synthetic comment -- @@ -179,8 +179,9 @@
public SecurityDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}
@Override
//Synthetic comment -- @@ -217,10 +218,10 @@
}
}

    private static boolean getExported(Element element) {
// Used to check whether an activity, service or broadcast receiver is exported.
String exportValue = element.getAttributeNS(ANDROID_URI, ATTR_EXPORTED);
        if (exportValue != null && !exportValue.isEmpty()) {
return Boolean.valueOf(exportValue);
} else {
for (Element child : LintUtils.getChildren(element)) {
//Synthetic comment -- @@ -233,24 +234,24 @@
return false;
}

    private static boolean isUnprotectedByPermission(Element element) {
// Used to check whether an activity, service or broadcast receiver are
// protected by a permission.
String permission = element.getAttributeNS(ANDROID_URI, ATTR_PERMISSION);
        if (permission == null || permission.isEmpty()) {
Node parent = element.getParentNode();
if (parent.getNodeType() == Node.ELEMENT_NODE
&& parent.getNodeName().equals(TAG_APPLICATION)) {
Element application = (Element) parent;
permission = application.getAttributeNS(ANDROID_URI, ATTR_PERMISSION);
                return permission == null || permission.isEmpty();
}
}

return false;
}

    private static boolean isLauncher(Element element) {
// Checks whether an element is a launcher activity.
for (Element child : LintUtils.getChildren(element)) {
if (child.getTagName().equals(TAG_INTENT_FILTER)) {
//Synthetic comment -- @@ -266,7 +267,7 @@
return false;
}

    private static void checkActivity(XmlContext context, Element element) {
// Do not flag launch activities. Even if not explicitly exported, it's
// safe to assume that those activities should be exported.
if (getExported(element) && isUnprotectedByPermission(element) && !isLauncher(element)) {
//Synthetic comment -- @@ -276,7 +277,7 @@
}
}

    private static boolean isStandardReceiver(Element element) {
// Checks whether a broadcast receiver receives a standard Android action
for (Element child : LintUtils.getChildren(element)) {
if (child.getTagName().equals(TAG_INTENT_FILTER)) {
//Synthetic comment -- @@ -291,7 +292,7 @@
return false;
}

    private static void checkReceiver(XmlContext context, Element element) {
if (getExported(element) && isUnprotectedByPermission(element) &&
!isStandardReceiver(element)) {
// No declared permission for this exported receiver: complain
//Synthetic comment -- @@ -300,7 +301,7 @@
}
}

    private static void checkService(XmlContext context, Element element) {
if (getExported(element) && isUnprotectedByPermission(element)) {
// No declared permission for this exported service: complain
context.report(EXPORTED_SERVICE, element, context.getLocation(element),
//Synthetic comment -- @@ -308,7 +309,7 @@
}
}

    private static void checkGrantPermission(XmlContext context, Element element) {
Attr path = element.getAttributeNodeNS(ANDROID_URI, ATTR_PATH);
Attr prefix = element.getAttributeNodeNS(ANDROID_URI, ATTR_PATH_PREFIX);
Attr pattern = element.getAttributeNodeNS(ANDROID_URI, ATTR_PATH_PATTERN);
//Synthetic comment -- @@ -326,11 +327,11 @@
}
}

    private static void checkProvider(XmlContext context, Element element) {
String exportValue = element.getAttributeNS(ANDROID_URI, ATTR_EXPORTED);
// Content providers are exported by default
boolean exported = true;
        if (exportValue != null && !exportValue.isEmpty()) {
exported = Boolean.valueOf(exportValue);
}

//Synthetic comment -- @@ -339,11 +340,11 @@
// of the permissions. We'll accept the permission, readPermission, or writePermission
// attributes on the provider element, or a path-permission element.
String permission = element.getAttributeNS(ANDROID_URI, ATTR_READ_PERMISSION);
            if (permission == null || permission.isEmpty()) {
permission = element.getAttributeNS(ANDROID_URI, ATTR_WRITE_PERMISSION);
                if (permission == null || permission.isEmpty()) {
permission = element.getAttributeNS(ANDROID_URI, ATTR_PERMISSION);
                    if (permission == null || permission.isEmpty()) {
// No permission attributes? Check for path-permission.

// TODO: Add a Lint check to ensure the path-permission is good, similar to








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java
//Synthetic comment -- index 361d4f9..4b4ba01 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
public static final Issue ISSUE = Issue.create("SetJavaScriptEnabled", //$NON-NLS-1$
"Looks for invocations of android.webkit.WebSettings.setJavaScriptEnabled",

            "Your code should not invoke `setJavaScriptEnabled` if you are not sure that " +
"your app really requires JavaScript support.",

Category.SECURITY,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 1f432f7..76887a5 100644

//Synthetic comment -- @@ -187,14 +187,15 @@
return null;
}

    private static class CommitFinder extends ForwardingAstVisitor {
        /** The target edit call */
        private final MethodInvocation mTarget;
        /** whether it allows the commit call to be seen before the target node */
        private final boolean mAllowCommitBeforeTarget;
/** Whether we've found one of the commit/cancel methods */
private boolean mFound;
/** Whether we've seen the target edit node yet */
private boolean mSeenTarget;

private CommitFinder(MethodInvocation target, boolean allowCommitBeforeTarget) {
mTarget = target;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StateListDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StateListDetector.java
//Synthetic comment -- index 6161e04..c0dea30 100644

//Synthetic comment -- @@ -69,8 +69,9 @@
return folderType == ResourceFolderType.DRAWABLE;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -103,7 +104,7 @@
stateNames.add(name + '=' + attribute.getValue());
} else {
String namespaceUri = attribute.getNamespaceURI();
                        if (namespaceUri != null && !namespaceUri.isEmpty() &&
!ANDROID_URI.equals(namespaceUri)) {
// There is a custom attribute on this item.
// This could be a state, see








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index 670f095..e8780a1 100644

//Synthetic comment -- @@ -159,7 +159,7 @@
* Map of strings that contain percents that aren't formatting strings; these
* should not be passed to String.format.
*/
    private final Map<String, Handle> mNotFormatStrings = new HashMap<String, Handle>();

/**
* Set of strings that have an unknown format such as date formatting; we should not
//Synthetic comment -- @@ -255,7 +255,7 @@

// Also make sure this String isn't an unformatted String
String formatted = element.getAttribute("formatted"); //$NON-NLS-1$
                if (!formatted.isEmpty() && !Boolean.parseBoolean(formatted)) {
if (!mNotFormatStrings.containsKey(name)) {
Handle handle = context.parser.createLocationHandle(context, element);
handle.setClientData(element);
//Synthetic comment -- @@ -350,7 +350,7 @@
}
}

    private static void checkTypes(Context context, Formatter formatter, boolean checkValid,
boolean checkTypes, String name, List<Pair<Handle, String>> list) {
Map<Integer, String> types = new HashMap<Integer, String>();
Map<Integer, Handle> typeDefinition = new HashMap<Integer, Handle>();
//Synthetic comment -- @@ -511,7 +511,7 @@
* others may work (e.g. float versus integer) but are probably not
* intentional.
*/
    private static boolean isIncompatible(char conversion1, char conversion2) {
int class1 = getConversionClass(conversion1);
int class2 = getConversionClass(conversion2);
return class1 != class2
//Synthetic comment -- @@ -570,8 +570,8 @@
return CONVERSION_CLASS_UNKNOWN;
}

    private static Location refineLocation(Context context, Location location,
            String formatString, int substringStart, int substringEnd) {
Position startLocation = location.getStart();
Position endLocation = location.getStart();
if (startLocation != null && endLocation != null) {
//Synthetic comment -- @@ -597,7 +597,7 @@
* Check that the number of arguments in the format string is consistent
* across translations, and that all arguments are used
*/
    private static void checkArity(Context context, String name, List<Pair<Handle, String>> list) {
// Check to make sure that the argument counts and types are consistent
int prevCount = -1;
for (Pair<Handle, String> pair : list) {
//Synthetic comment -- @@ -899,7 +899,7 @@
MethodInvocation call) {

StrictListAccessor<Expression, MethodInvocation> args = call.astArguments();
        if (args.isEmpty()) {
return;
}

//Synthetic comment -- @@ -1089,7 +1089,7 @@
/** Map from variable name to corresponding type */
private final Map<String, Class<?>> mTypes = new HashMap<String, Class<?>>();
/** The AST node for the String.format we're interested in */
        private final lombok.ast.Node mTargetNode;
private boolean mDone;
/**
* Result: the name of the string resource being passed to the








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StyleCycleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/StyleCycleDetector.java
//Synthetic comment -- index 61290e1..8ce103b 100644

//Synthetic comment -- @@ -63,8 +63,9 @@
return folderType == ResourceFolderType.VALUES;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SystemPermissionsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SystemPermissionsDetector.java
//Synthetic comment -- index 905a7b4..21fb6c0 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
//Synthetic comment -- @@ -154,8 +155,9 @@
public SystemPermissionsDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -168,7 +170,7 @@

@Override
public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_USES_PERMISSION);
}

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextFieldDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextFieldDetector.java
//Synthetic comment -- index 9eb4474..a059f5c 100644

//Synthetic comment -- @@ -78,8 +78,9 @@
public TextFieldDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -204,7 +205,7 @@
}
}

    private static void reportMismatch(XmlContext context, Attr idNode, Attr inputTypeNode,
String message) {
Location location;
if (inputTypeNode != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextViewDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TextViewDetector.java
//Synthetic comment -- index dd98709..3e519cf 100644

//Synthetic comment -- @@ -114,8 +114,9 @@
public TextViewDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TitleDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TitleDetector.java
//Synthetic comment -- index 1d1d024..5fc5340 100644

//Synthetic comment -- @@ -77,8 +77,9 @@
return folderType == ResourceFolderType.MENU;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index e8f8d6e..01804af 100644

//Synthetic comment -- @@ -114,11 +114,11 @@
}
}

    private static class ShowFinder extends ForwardingAstVisitor {
        /** The target makeText call */
        private final MethodInvocation mTarget;
/** Whether we've found the show method */
private boolean mFound;
/** Whether we've seen the target makeText node yet */
private boolean mSeenTarget;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TooManyViewsDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TooManyViewsDetector.java
//Synthetic comment -- index b4095c8..94a9611 100644

//Synthetic comment -- @@ -73,14 +73,16 @@
if (countValue != null) {
try {
maxViewCount = Integer.parseInt(countValue);
            } catch (NumberFormatException e) {
                // pass: set to default below
}
}
String depthValue = System.getenv("ANDROID_LINT_MAX_DEPTH"); //$NON-NLS-1$
if (depthValue != null) {
try {
maxDepth = Integer.parseInt(depthValue);
            } catch (NumberFormatException e) {
                // pass: set to default below
}
}
if (maxViewCount == 0) {
//Synthetic comment -- @@ -102,8 +104,9 @@
public TooManyViewsDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 6b63ffc..5ca6046 100644

//Synthetic comment -- @@ -64,7 +64,7 @@
*/
public class TranslationDetector extends ResourceXmlDetector {
@VisibleForTesting
    static boolean sCompleteRegions =
System.getenv("ANDROID_LINT_COMPLETE_REGIONS") != null; //$NON-NLS-1$

private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[a-z]{2}$"); //$NON-NLS-1$
//Synthetic comment -- @@ -333,7 +333,7 @@

// Do we need to resolve fallback strings for regions that only define a subset
// of the strings in the language and fall back on the main language for the rest?
        if (!sCompleteRegions) {
for (String l : languageToStrings.keySet()) {
if (l.indexOf('-') != -1) {
// Yes, we have regions. Merge all base language string names into each region.
//Synthetic comment -- @@ -372,7 +372,7 @@
if (stringCount != strings.size()) {
if (reportMissing) {
Set<String> difference = Sets.difference(defaultStrings, strings);
                    if (!difference.isEmpty()) {
if (mMissingLocations == null) {
mMissingLocations = new HashMap<String, Location>();
}
//Synthetic comment -- @@ -396,7 +396,7 @@

if (reportExtra) {
Set<String> difference = Sets.difference(strings, defaultStrings);
                    if (!difference.isEmpty()) {
if (mExtraLocations == null) {
mExtraLocations = new HashMap<String, Location>();
}
//Synthetic comment -- @@ -493,7 +493,7 @@
}

assert context.getPhase() == 1;
        if (attribute == null || attribute.getValue().isEmpty()) {
context.report(MISSING, element, context.getLocation(element),
"Missing name attribute in <string> declaration", null);
} else {
//Synthetic comment -- @@ -554,7 +554,7 @@
}
}

    private static boolean allItemsAreReferences(Element element) {
assert element.getTagName().equals(TAG_STRING_ARRAY);
NodeList childNodes = element.getChildNodes();
for (int i = 0, n = childNodes.getLength(); i < n; i++) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoDetector.java
//Synthetic comment -- index d0cf366..b9b889f 100644

//Synthetic comment -- @@ -79,11 +79,11 @@
* </ul>
*/
public class TypoDetector extends ResourceXmlDetector {
    @Nullable private TypoLookup mLookup;
    @Nullable private String mLastLanguage;
    @Nullable private String mLastRegion;
    @Nullable private String mLanguage;
    @Nullable private String mRegion;

/** The main issue discovered by this detector */
public static final Issue ISSUE = Issue.create(
//Synthetic comment -- @@ -164,8 +164,9 @@
}
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.NORMAL;
}

//Synthetic comment -- @@ -313,7 +314,7 @@
}

/** Report the typo found at the given offset and suggest the given replacements */
    private static void reportTypo(XmlContext context, Node node, String text, int begin,
List<String> replacements) {
if (replacements.size() < 2) {
return;
//Synthetic comment -- @@ -326,7 +327,7 @@
String message;

boolean isCapitalized = Character.isUpperCase(word.charAt(0));
        StringBuilder sb = new StringBuilder(40);
for (int i = 1, n = replacements.size(); i < n; i++) {
String replacement = replacements.get(i);
if (first == null) {
//Synthetic comment -- @@ -337,8 +338,8 @@
}
sb.append('"');
if (isCapitalized) {
                sb.append(Character.toUpperCase(replacement.charAt(0)));
                sb.append(replacement.substring(1));
} else {
sb.append(replacement);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoLookup.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypoLookup.java
//Synthetic comment -- index 2dcd6c3..7d9d8f2 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
private int[] mIndices;
private int mWordCount;

    private static final WeakHashMap<String, TypoLookup> sInstanceMap =
new WeakHashMap<String, TypoLookup>();

/**
//Synthetic comment -- @@ -166,7 +166,7 @@
File binaryData = new File(cacheDir, name
// Incorporate version number in the filename to avoid upgrade filename
// conflicts on Windows (such as issue #26663)
                + '-' + BINARY_FORMAT_VERSION + ".bin"); //$NON-NLS-1$

if (DEBUG_FORCE_REGENERATE_BINARY) {
System.err.println("\nTemporarily regenerating binary data unconditionally \nfrom "
//Synthetic comment -- @@ -764,19 +764,19 @@
// contain these. None of the currently included dictionaries do. However, it does
// help us properly deal with punctuation and spacing characters.

    static boolean isUpperCase(byte b) {
return Character.isUpperCase((char) b);
}

    static byte toLowerCase(byte b) {
return (byte) Character.toLowerCase((char) b);
}

    static boolean isSpace(byte b) {
return Character.isWhitespace((char) b);
}

    static boolean isLetter(byte b) {
// Assume that multi byte characters represent letters in other languages.
// Obviously, it could be unusual punctuation etc but letters are more likely
// in this context.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index 66d6889..209b3d7 100644

//Synthetic comment -- @@ -197,8 +197,9 @@
return folderType == ResourceFolderType.VALUES;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -457,7 +458,7 @@
} else if (message.equals(SINGLE_QUOTE_MESSAGE)) {
int offset = text.indexOf('\'');
if (offset != -1) {
                int endOffset = text.indexOf('\'', offset + 1);           //$NON-NLS-1$
if (endOffset != -1) {
edits.add(new ReplaceEdit(offset, 1, "\u2018"));     //$NON-NLS-1$
edits.add(new ReplaceEdit(endOffset, 1, "\u2019"));  //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 5a3f174..436f420 100644

//Synthetic comment -- @@ -215,7 +215,7 @@
unused.removeAll(styles);

// Remove id's if the user has disabled reporting issue ids
            if (!unused.isEmpty() && !context.isEnabled(ISSUE_IDS)) {
// Remove all R.id references
List<String> ids = new ArrayList<String>();
for (String resource : unused) {
//Synthetic comment -- @@ -226,7 +226,7 @@
unused.removeAll(ids);
}

            if (!unused.isEmpty() && !context.getDriver().hasParserErrors()) {
mUnused = new HashMap<String, Location>(unused.size());
for (String resource : unused) {
mUnused.put(resource, null);
//Synthetic comment -- @@ -241,7 +241,7 @@

// Report any resources that we (for some reason) could not find a declaration
// location for
            if (!mUnused.isEmpty()) {
// Fill in locations for files that we didn't encounter in other ways
for (Map.Entry<String, Location> entry : mUnused.entrySet()) {
String resource = entry.getKey();
//Synthetic comment -- @@ -480,8 +480,9 @@
}
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.SLOW;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UseCompoundDrawableDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UseCompoundDrawableDetector.java
//Synthetic comment -- index 289806f..db5de4d 100644

//Synthetic comment -- @@ -68,8 +68,9 @@
public UseCompoundDrawableDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UselessViewDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UselessViewDetector.java
//Synthetic comment -- index c75cb93..867c3c4 100644

//Synthetic comment -- @@ -85,8 +85,9 @@
public UselessViewDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -140,7 +141,7 @@
}

// This is the old UselessLayoutCheck from layoutopt
    private static void checkUselessMiddleLayout(XmlContext context, Element element) {
// Conditions:
// - The node has children
// - The node does not have siblings
//Synthetic comment -- @@ -209,7 +210,7 @@
}

// This is the old UselessView check from layoutopt
    private static void checkUselessLeaf(XmlContext context, Element element) {
assert LintUtils.getChildCount(element) == 0;

// Conditions:








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/Utf8Detector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/Utf8Detector.java
//Synthetic comment -- index 6f9a94c..2e23483 100644

//Synthetic comment -- @@ -57,8 +57,9 @@
public Utf8Detector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -71,7 +72,8 @@

// AAPT: The prologue must be in the first line
int lineEnd = 0;
        int max = xml.length();
        for (; lineEnd < max; lineEnd++) {
char c = xml.charAt(lineEnd);
if (c == '\n' || c == '\r') {
break;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 1c276ed..0eba024 100644

//Synthetic comment -- @@ -72,8 +72,9 @@
public ViewConstructorDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -113,7 +114,7 @@
return false;
}

    private static void checkConstructors(ClassContext context, ClassNode classNode) {
// Look through constructors
@SuppressWarnings("rawtypes")
List methods = classNode.methods;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTagDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTagDetector.java
//Synthetic comment -- index 3590f83..46e24cc 100644

//Synthetic comment -- @@ -72,8 +72,9 @@
public ViewTagDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -153,7 +154,7 @@
internalName = className.replace('.', '/');
}
assert internalName != null;
                        parent = driver.getSuperClass(internalName);
}
className = parent;
internalName = null;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTypeDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ViewTypeDetector.java
//Synthetic comment -- index 46d806e..bad5d50 100644

//Synthetic comment -- @@ -70,10 +70,11 @@
ViewTypeDetector.class,
EnumSet.of(Scope.ALL_RESOURCE_FILES, Scope.ALL_JAVA_FILES));

    private final Map<String, Object> mIdToViewTag = new HashMap<String, Object>(50);

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

//Synthetic comment -- @@ -173,7 +174,7 @@
}

/** Check if the view and cast type are compatible */
    private static void checkCompatible(JavaContext context, String castType, String layoutType,
List<String> layoutTypes, Cast node) {
assert layoutType == null || layoutTypes == null; // Should only specify one or the other
boolean compatible = true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index a8ce2f2..1994877 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
* Make sure you add the asm-debug or asm-util jars to the runtime classpath
* as well since the opcode integer to string mapping display routine looks for
* it via reflection. */
    private static final boolean DEBUG = false;

/** Constructs a new {@link WakelockDetector} */
public WakelockDetector() {
//Synthetic comment -- @@ -185,7 +185,7 @@
// Requires util package
//ClassNode clazz = classNode;
//clazz.accept(new TraceClassVisitor(new PrintWriter(System.out)));
                System.out.println(graph.toString(graph.getNode(acquire)));
}

int status = dfs(graph.getNode(acquire));
//Synthetic comment -- @@ -205,10 +205,10 @@
}
}

    private static final int SEEN_TARGET = 1;
    private static final int SEEN_BRANCH = 2;
    private static final int SEEN_EXCEPTION = 4;
    private static final int SEEN_RETURN = 8;

/** TODO RENAME */
private static class MyGraph extends ControlFlowGraph {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index e8a1bbf..e0e2d0a 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
public class WrongIdDetector extends LayoutDetector {

/** Ids bound to widgets in any of the layout files */
    private final Set<String> mGlobalIds = new HashSet<String>(100);

/** Ids bound to widgets in the current layout file */
private Set<String> mFileIds;
//Synthetic comment -- @@ -132,8 +132,9 @@
return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.VALUES;
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -219,7 +220,7 @@
if (suggestions.size() > 1) {
suggestionMessage = String.format(" Did you mean one of {%2$s} ?",
id, Joiner.on(", ").join(suggestions));
                    } else if (!suggestions.isEmpty()) {
suggestionMessage = String.format(" Did you mean %2$s ?",
id, suggestions.get(0));
} else {
//Synthetic comment -- @@ -251,7 +252,7 @@
}
}

    private static void report(Context context, Issue issue, Handle handle, String message) {
Location location = handle.resolve();
Object clientData = handle.getClientData();
if (clientData instanceof Node) {
//Synthetic comment -- @@ -275,7 +276,7 @@
String type = element.getAttribute(ATTR_TYPE);
if (VALUE_ID.equals(type)) {
String name = element.getAttribute(ATTR_NAME);
                if (!name.isEmpty()) {
if (mDeclaredIds == null) {
mDeclaredIds = Sets.newHashSet();
}
//Synthetic comment -- @@ -311,13 +312,13 @@
return definedLocally;
}

    private static List<String> getSpellingSuggestions(String id, Collection<String> ids) {
int maxDistance = id.length() >= 4 ? 2 : 1;

// Look for typos and try to match with custom views and android views
Multimap<Integer, String> matches = ArrayListMultimap.create(2, 10);
int count = 0;
        if (!ids.isEmpty()) {
for (String matchWith : ids) {
matchWith = stripIdPrefix(matchWith);
if (Math.abs(id.length() - matchWith.length()) > maxDistance) {
//Synthetic comment -- @@ -339,9 +340,9 @@
}

for (int i = 0; i < maxDistance; i++) {
            Collection<String> strings = matches.get(i);
            if (strings != null && !strings.isEmpty()) {
                List<String> suggestions = new ArrayList<String>(strings);
Collections.sort(suggestions);
return suggestions;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongImportDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongImportDetector.java
//Synthetic comment -- index bc3c45e..64f3ce1 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.ImportDeclaration;
import lombok.ast.Node;

/**
* Checks for "import android.R", which seems to be a common source of confusion
//Synthetic comment -- @@ -66,16 +67,17 @@
public WrongImportDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements Detector.JavaScanner ----

@Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>> singletonList(
ImportDeclaration.class);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongLocationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/WrongLocationDetector.java
//Synthetic comment -- index 09011cc..37ffbeb 100644

//Synthetic comment -- @@ -52,8 +52,9 @@
public WrongLocationDetector() {
}

    @NonNull
@Override
    public Speed getSpeed() {
return Speed.FAST;
}








