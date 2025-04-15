/*42661: Split API check constant warnings into a separate issue

The issue has severity warning instead of error.

Change-Id:I54232f5699fa57dd66d5f54f27c5a75a4bb6eb3b*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/MainTest.java b/lint/cli/src/test/java/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 8ba40aa..68a1ea3 100644

//Synthetic comment -- @@ -147,14 +147,6 @@
"Similarly, you can use tools:targetApi=\"11\" in an XML file to indicate that\n" +
"the element will only be inflated in an adequate context.\n" +
"\n" +
        "Lint will also flag certain constants, such as static final integers, which\n" +
        "were introduced in later versions. These will actually be copied into the\n" +
        "class files rather than being referenced, which means that the value is\n" +
        "available even when running on older devices. In some cases that's fine, and\n" +
        "in other cases it can result in a runtime crash or incorrect behavior. It\n" +
        "depends on the context, so consider the code carefully and device whether it's\n" +
        "safe and can be suppressed or whether the code needs to be guarded.\n" +
        "\n" +
"\n",

// Expected error








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ApiDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ApiDetectorTest.java
//Synthetic comment -- index a4c7e4a..012dbc9 100644

//Synthetic comment -- @@ -138,14 +138,14 @@
"              ~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 1): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 1): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
//             Note: the above error range is wrong; should be pointing to the second
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 1): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~\n" +
"7 errors, 0 warnings\n",

lintProject(
//Synthetic comment -- @@ -172,13 +172,13 @@
"              ~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 2): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 2): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 2): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~\n" +
"7 errors, 0 warnings\n",

lintProject(
//Synthetic comment -- @@ -202,13 +202,13 @@
"              ~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 4): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 4): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 4): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~\n" +
"6 errors, 0 warnings\n",

lintProject(
//Synthetic comment -- @@ -229,13 +229,13 @@
"              ~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 10): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 10): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 10): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~\n" +
"5 errors, 0 warnings\n",

lintProject(
//Synthetic comment -- @@ -366,13 +366,13 @@
"              ~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:89: Error: Field requires API level 11 (current min is 1): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:94: Error: Field requires API level 14 (current min is 1): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:97: Error: Field requires API level 11 (current min is 1): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
            "              ~~~~~~~~~~~~~~~~~~~~~~~\n" +

// Note: These annotations are within the methods, not ON the methods, so they have
// no effect (because they don't end up in the bytecode)
//Synthetic comment -- @@ -719,64 +719,64 @@

public void testJavaConstants() throws Exception {
assertEquals(""
                + "src/test/pkg/ApiSourceCheck.java:5: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "import static android.view.View.MEASURED_STATE_MASK;\n"
+ "              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:30: Error: Field requires API level 11 (current min is 1): android.widget.ZoomControls#MEASURED_STATE_MASK [NewApi]\n"
+ "        int x = MEASURED_STATE_MASK;\n"
+ "                ~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:33: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "        int y = android.view.View.MEASURED_STATE_MASK;\n"
+ "                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:36: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "        int z = View.MEASURED_STATE_MASK;\n"
+ "                ~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:37: Error: Field requires API level 14 (current min is 1): android.view.View#FIND_VIEWS_WITH_TEXT [NewApi]\n"
+ "        int find2 = View.FIND_VIEWS_WITH_TEXT; // requires API 14\n"
+ "                    ~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:40: Error: Field requires API level 12 (current min is 1): android.app.ActivityManager#MOVE_TASK_NO_USER_ACTION [NewApi]\n"
+ "        int w = ActivityManager.MOVE_TASK_NO_USER_ACTION;\n"
+ "                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:41: Error: Field requires API level 14 (current min is 1): android.widget.ZoomButton#FIND_VIEWS_WITH_CONTENT_DESCRIPTION [NewApi]\n"
+ "        int find1 = ZoomButton.FIND_VIEWS_WITH_CONTENT_DESCRIPTION; // requires\n"
+ "                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:44: Error: Field requires API level 9 (current min is 1): android.widget.ZoomControls#OVER_SCROLL_ALWAYS [NewApi]\n"
+ "        int overScroll = OVER_SCROLL_ALWAYS; // requires API 9\n"
+ "                         ~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:47: Error: Field requires API level 16 (current min is 1): android.widget.ZoomControls#IMPORTANT_FOR_ACCESSIBILITY_AUTO [NewApi]\n"
+ "        int auto = IMPORTANT_FOR_ACCESSIBILITY_AUTO; // requires API 16\n"
+ "                   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:51: Error: Field requires API level 14 (current min is 1): android.widget.ZoomButton#ROTATION_X [NewApi]\n"
                + "        Object rotationX = ZoomButton.ROTATION_X; // Requires API 14\n"
                + "                           ~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:54: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "        return (child.getMeasuredWidth() & View.MEASURED_STATE_MASK)\n"
+ "                                           ~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:55: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_HEIGHT_STATE_SHIFT [NewApi]\n"
+ "                | ((child.getMeasuredHeight() >> View.MEASURED_HEIGHT_STATE_SHIFT) & (View.MEASURED_STATE_MASK >> View.MEASURED_HEIGHT_STATE_SHIFT));\n"
+ "                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:55: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_HEIGHT_STATE_SHIFT [NewApi]\n"
+ "                | ((child.getMeasuredHeight() >> View.MEASURED_HEIGHT_STATE_SHIFT) & (View.MEASURED_STATE_MASK >> View.MEASURED_HEIGHT_STATE_SHIFT));\n"
+ "                                                                                                                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:55: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "                | ((child.getMeasuredHeight() >> View.MEASURED_HEIGHT_STATE_SHIFT) & (View.MEASURED_STATE_MASK >> View.MEASURED_HEIGHT_STATE_SHIFT));\n"
+ "                                                                                      ~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:90: Error: Field requires API level 8 (current min is 1): android.R.id#custom [NewApi]\n"
+ "        int custom = android.R.id.custom; // API 8\n"
+ "                     ~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:94: Error: Field requires API level 13 (current min is 1): android.Manifest.permission#SET_POINTER_SPEED [NewApi]\n"
+ "        String setPointerSpeed = permission.SET_POINTER_SPEED;\n"
+ "                                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:95: Error: Field requires API level 13 (current min is 1): android.Manifest.permission#SET_POINTER_SPEED [NewApi]\n"
+ "        String setPointerSpeed2 = Manifest.permission.SET_POINTER_SPEED;\n"
+ "                                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:120: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "        int y = View.MEASURED_STATE_MASK; // Not OK\n"
+ "                ~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/ApiSourceCheck.java:121: Error: Field requires API level 11 (current min is 1): android.view.View#MEASURED_STATE_MASK [NewApi]\n"
+ "        testBenignUsages(View.MEASURED_STATE_MASK); // Not OK\n"
+ "                         ~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "19 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 6598a60..7e8a460 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
//Synthetic comment -- @@ -138,16 +139,20 @@

"This check scans through all the Android API calls in the application and " +
"warns about any calls that are not available on *all* versions targeted " +
            "by this application (according to its minimum SDK attribute in the manifest).\n" +
"\n" +
            "If you really want to use this API and don't need to support older devices just " +
"set the `minSdkVersion` in your `AndroidManifest.xml` file." +
"\n" +
"If your code is *deliberately* accessing newer APIs, and you have ensured " +
            "(e.g. with conditional execution) that this code will only ever be called on a " +
"supported platform, then you can annotate your class or method with the " +
"`@TargetApi` annotation specifying the local minimum SDK to apply, such as " +
            "`@TargetApi(11)`, such that this check considers 11 rather than your manifest " +
"file's minimum SDK as the required API level.\n" +
"\n" +
"If you are deliberately setting `android:` attributes in style definitions, " +
//Synthetic comment -- @@ -155,24 +160,48 @@
"into runtime conflicts on certain devices where manufacturers have added " +
"custom attributes whose ids conflict with the new ones on later platforms.\n" +
"\n" +
            "Similarly, you can use tools:targetApi=\"11\" in an XML file to indicate that " +
            "the element will only be inflated in an adequate context.\n" +
            "\n" +
            "Lint will also flag certain constants, such as static final integers, " +
"which were introduced in later versions. These will actually be copied " +
"into the class files rather than being referenced, which means that " +
"the value is available even when running on older devices. In some " +
"cases that's fine, and in other cases it can result in a runtime " +
"crash or incorrect behavior. It depends on the context, so consider " +
            "the code carefully and device whether it's safe and can be suppressed "  +
            "or whether the code needs to be guarded.",
Category.CORRECTNESS,
6,
            Severity.ERROR,
ApiDetector.class,
            EnumSet.of(Scope.CLASS_FILE, Scope.RESOURCE_FILE, Scope.MANIFEST, Scope.JAVA_FILE))
            .addAnalysisScope(Scope.RESOURCE_FILE_SCOPE)
            .addAnalysisScope(Scope.CLASS_FILE_SCOPE)
.addAnalysisScope(Scope.JAVA_FILE_SCOPE);

/** Accessing an unsupported API */
//Synthetic comment -- @@ -207,7 +236,7 @@

protected ApiLookup mApiDatabase;
private int mMinApi = -1;
    private Set<String> mWarnedFields;

/** Constructs a new API check */
public ApiDetector() {
//Synthetic comment -- @@ -623,13 +652,14 @@
continue;
}
String fqcn = ClassContext.getFqcn(owner) + '#' + name;
                        if (mWarnedFields == null || !mWarnedFields.contains(fqcn)) {
                            String message = String.format(
                                    "Field requires API level %1$d (current min is %2$d): %3$s",
                                    api, minSdk, fqcn);
                            report(context, message, node, method, name, null,
                                    SearchHints.create(FORWARD).matchJavaSymbol());
}
}
} else if (type == AbstractInsnNode.LDC_INSN) {
LdcInsnNode node = (LdcInsnNode) instruction;
//Synthetic comment -- @@ -972,7 +1002,22 @@
context.report(UNSUPPORTED, method, node, location, message, null);
}

    // ---- Implements JavaScanner ----

@Nullable
@Override
//Synthetic comment -- @@ -1282,16 +1327,32 @@
String message = String.format(
"Field requires API level %1$d (current min is %2$d): %3$s",
api, minSdk, fqcn);
                    mContext.report(UNSUPPORTED, node, location, message, null);

                    // Record this field as already reported such that when we scan the
                    // class files later, we don't report the same error again.
                    // (This happens when a field isn't a final primitive value which
                    // gets copied into the .class file)
                    if (mWarnedFields == null) {
                        mWarnedFields = Sets.newHashSet();
}
                    mWarnedFields.add(fqcn);
}

return true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 6838a24..90ebfd8 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 139;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -64,6 +64,7 @@
issues.add(FieldGetterDetector.ISSUE);
issues.add(SdCardDetector.ISSUE);
issues.add(ApiDetector.UNSUPPORTED);
issues.add(ApiDetector.OVERRIDE);
issues.add(InvalidPackageDetector.ISSUE);
issues.add(DuplicateIdDetector.CROSS_LAYOUT);







