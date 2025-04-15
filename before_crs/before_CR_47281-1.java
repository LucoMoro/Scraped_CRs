/*API check: Only check calls and field accesses, not declarations

It appears Dalvik is very forgiving and doesn't try to preload classes
until actually needed, so there is no need to flag variable and field
declarations, and in fact, patterns used for supporting new and old
versions sometimes declares these methods and only conditionally end
up actually accessing methods and fields, so only check method and
field accesses.

Change-Id:I9018a64845b5b29744926e3d0be371801bbdd167*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 7129b9a..3a0b93b 100644

//Synthetic comment -- @@ -74,6 +74,15 @@
* by this application (according to its minimum API requirement in the manifest).
*/
public class ApiDetector extends ResourceXmlDetector implements Detector.ClassScanner {
private static final boolean AOSP_BUILD = System.getenv("ANDROID_BUILD_TOP") != null; //$NON-NLS-1$

/** Accessing an unsupported API */
//Synthetic comment -- @@ -177,6 +186,13 @@
int minSdk = getMinSdk(context);
if (api > minSdk && api > context.getFolderVersion()
&& api > getLocalMinSdk(attribute.getOwnerElement())) {
Location location = context.getLocation(attribute);
String message = String.format(
"%1$s requires API level %2$d (current min is %3$d)",
//Synthetic comment -- @@ -303,47 +319,49 @@

InsnList nodes = method.instructions;

            // Check types in parameter list and types of local variables
            List localVariables = method.localVariables;
            if (localVariables != null) {
                for (Object v : localVariables) {
                    LocalVariableNode var = (LocalVariableNode) v;
                    String desc = var.desc;
                    if (desc.charAt(0) == 'L') {
                        // "Lpackage/Class;" => "package/Bar"
                        String className = desc.substring(1, desc.length() - 1);
                        int api = mApiDatabase.getClassVersion(className);
                        if (api > minSdk) {
                            String fqcn = ClassContext.getFqcn(className);
                            String message = String.format(
                                "Class requires API level %1$d (current min is %2$d): %3$s",
                                api, minSdk, fqcn);
                            report(context, message, var.start, method,
                                    className.substring(className.lastIndexOf('/') + 1), null,
                                    SearchHints.create(NEAREST).matchJavaSymbol());
}
}
}
            }

            // Check return type
            // The parameter types are already handled as local variables so we can skip
            // right to the return type.
            // Check types in parameter list
            String signature = method.desc;
            if (signature != null) {
                int args = signature.indexOf(')');
                if (args != -1 && signature.charAt(args + 1) == 'L') {
                    String type = signature.substring(args + 2, signature.length() - 1);
                    int api = mApiDatabase.getClassVersion(type);
                    if (api > minSdk) {
                        String fqcn = ClassContext.getFqcn(type);
                        String message = String.format(
                            "Class requires API level %1$d (current min is %2$d): %3$s",
                            api, minSdk, fqcn);
                        AbstractInsnNode first = nodes.size() > 0 ? nodes.get(0) : null;
                        report(context, message, first, method, method.name, null,
                                SearchHints.create(BACKWARD).matchJavaSymbol());
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java
//Synthetic comment -- index 9823f23..b5e243b 100644

//Synthetic comment -- @@ -124,15 +124,9 @@

public void testApi1() throws Exception {
assertEquals(
            "src/foo/bar/ApiCallTest.java:18: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMLocator [NewApi]\n" +
            " public void method(Chronometer chronometer, DOMLocator locator) {\n" +
            "                                             ~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:20: Error: Call requires API level 11 (current min is 1): android.app.Activity#getActionBar [NewApi]\n" +
"  getActionBar(); // API 11\n" +
"  ~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:23: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMError [NewApi]\n" +
            "  DOMError error = null; // API 8\n" +
            "  ~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:24: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMErrorHandler [NewApi]\n" +
"  Class<?> clz = DOMErrorHandler.class; // API 8\n" +
"                 ~~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -145,22 +139,14 @@
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 1): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
"                         ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:38: Error: Class requires API level 14 (current min is 1): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 1): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 1): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
"                              ~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:45: Error: Class requires API level 14 (current min is 1): android.widget.GridLayout [NewApi]\n" +
            " GridLayout getGridLayout() { // API 14\n" +
            "            ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:49: Error: Class requires API level 14 (current min is 1): android.app.ApplicationErrorReport [NewApi]\n" +
            " private ApplicationErrorReport getReport() {\n" +
            "                                ~~~~~~~~~\n" +
            "12 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -172,15 +158,9 @@

public void testApi2() throws Exception {
assertEquals(
            "src/foo/bar/ApiCallTest.java:18: Error: Class requires API level 8 (current min is 2): org.w3c.dom.DOMLocator [NewApi]\n" +
            " public void method(Chronometer chronometer, DOMLocator locator) {\n" +
            "                                             ~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:20: Error: Call requires API level 11 (current min is 2): android.app.Activity#getActionBar [NewApi]\n" +
"  getActionBar(); // API 11\n" +
"  ~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:23: Error: Class requires API level 8 (current min is 2): org.w3c.dom.DOMError [NewApi]\n" +
            "  DOMError error = null; // API 8\n" +
            "  ~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:24: Error: Class requires API level 8 (current min is 2): org.w3c.dom.DOMErrorHandler [NewApi]\n" +
"  Class<?> clz = DOMErrorHandler.class; // API 8\n" +
"                 ~~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -193,22 +173,13 @@
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 2): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
"                         ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:38: Error: Class requires API level 14 (current min is 2): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 2): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 2): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
"                              ~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:45: Error: Class requires API level 14 (current min is 2): android.widget.GridLayout [NewApi]\n" +
            " GridLayout getGridLayout() { // API 14\n" +
            "            ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:49: Error: Class requires API level 14 (current min is 2): android.app.ApplicationErrorReport [NewApi]\n" +
            " private ApplicationErrorReport getReport() {\n" +
            "                                ~~~~~~~~~\n" +
            "12 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -220,15 +191,9 @@

public void testApi4() throws Exception {
assertEquals(
            "src/foo/bar/ApiCallTest.java:18: Error: Class requires API level 8 (current min is 4): org.w3c.dom.DOMLocator [NewApi]\n" +
            " public void method(Chronometer chronometer, DOMLocator locator) {\n" +
            "                                             ~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:20: Error: Call requires API level 11 (current min is 4): android.app.Activity#getActionBar [NewApi]\n" +
"  getActionBar(); // API 11\n" +
"  ~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:23: Error: Class requires API level 8 (current min is 4): org.w3c.dom.DOMError [NewApi]\n" +
            "  DOMError error = null; // API 8\n" +
            "  ~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:24: Error: Class requires API level 8 (current min is 4): org.w3c.dom.DOMErrorHandler [NewApi]\n" +
"  Class<?> clz = DOMErrorHandler.class; // API 8\n" +
"                 ~~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -238,22 +203,13 @@
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 4): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
"                         ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:38: Error: Class requires API level 14 (current min is 4): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 4): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 4): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
"                              ~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:45: Error: Class requires API level 14 (current min is 4): android.widget.GridLayout [NewApi]\n" +
            " GridLayout getGridLayout() { // API 14\n" +
            "            ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:49: Error: Class requires API level 14 (current min is 4): android.app.ApplicationErrorReport [NewApi]\n" +
            " private ApplicationErrorReport getReport() {\n" +
            "                                ~~~~~~~~~\n" +
            "11 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -274,22 +230,13 @@
"src/foo/bar/ApiCallTest.java:33: Error: Field requires API level 11 (current min is 10): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
"                         ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:38: Error: Class requires API level 14 (current min is 10): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:38: Error: Field requires API level 14 (current min is 10): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
"src/foo/bar/ApiCallTest.java:41: Error: Field requires API level 11 (current min is 10): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"  Mode mode = PorterDuff.Mode.OVERLAY; // API 11\n" +
"                              ~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:45: Error: Class requires API level 14 (current min is 10): android.widget.GridLayout [NewApi]\n" +
            " GridLayout getGridLayout() { // API 14\n" +
            "            ~~~~~~~~~~~~~\n" +
            "src/foo/bar/ApiCallTest.java:49: Error: Class requires API level 14 (current min is 10): android.app.ApplicationErrorReport [NewApi]\n" +
            " private ApplicationErrorReport getReport() {\n" +
            "                                ~~~~~~~~~\n" +
            "8 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -404,15 +351,10 @@
// These errors are correctly -not- suppressed because they
// appear in method3 (line 74-98) which is annotated with a
// @SuppressLint annotation specifying only an unrelated issue id
            "src/foo/bar/SuppressTest1.java:74: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMLocator [NewApi]\n" +
            " public void method3(Chronometer chronometer, DOMLocator locator) {\n" +
            "                                              ~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:76: Error: Call requires API level 11 (current min is 1): android.app.Activity#getActionBar [NewApi]\n" +
"  getActionBar(); // API 11\n" +
"  ~~~~~~~~~~~~\n" +
            "src/foo/bar/SuppressTest1.java:79: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMError [NewApi]\n" +
            "  DOMError error = null; // API 8\n" +
            "  ~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:80: Error: Class requires API level 8 (current min is 1): org.w3c.dom.DOMErrorHandler [NewApi]\n" +
"  Class<?> clz = DOMErrorHandler.class; // API 8\n" +
"                 ~~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -425,9 +367,6 @@
"src/foo/bar/SuppressTest1.java:89: Error: Field requires API level 11 (current min is 1): dalvik.bytecode.OpcodeInfo#MAXIMUM_VALUE [NewApi]\n" +
"  int field = OpcodeInfo.MAXIMUM_VALUE; // API 11\n" +
"                         ~~~~~~~~~~~~~\n" +
            "src/foo/bar/SuppressTest1.java:94: Error: Class requires API level 14 (current min is 1): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest1.java:94: Error: Field requires API level 14 (current min is 1): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = getReport().batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -439,16 +378,10 @@
// no effect (because they don't end up in the bytecode)


            "src/foo/bar/SuppressTest4.java:16: Error: Class requires API level 14 (current min is 1): android.app.ApplicationErrorReport [NewApi]\n" +
            "  ApplicationErrorReport report = null;\n" +
            "  ~~~~~~~~~~~~~~~~~~~~~~\n" +
            "src/foo/bar/SuppressTest4.java:19: Error: Class requires API level 14 (current min is 1): android.app.ApplicationErrorReport.BatteryInfo [NewApi]\n" +
            "  BatteryInfo batteryInfo = report.batteryInfo;\n" +
            "  ~~~~~~~~~~~\n" +
"src/foo/bar/SuppressTest4.java:19: Error: Field requires API level 14 (current min is 1): android.app.ApplicationErrorReport#batteryInfo [NewApi]\n" +
"  BatteryInfo batteryInfo = report.batteryInfo;\n" +
"              ~~~~~~~~~~~\n" +
            "13 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -578,13 +511,10 @@
"src/test/pkg/TestEnum.java:37: Error: Enum value requires API level 11 (current min is 4): android.graphics.PorterDuff.Mode#OVERLAY [NewApi]\n" +
"            case OVERLAY: {\n" +
"                 ~~~~~~~\n" +
            "src/test/pkg/TestEnum.java:58: Error: Class requires API level 11 (current min is 4): android.renderscript.Element.DataType [NewApi]\n" +
            "    public static void test4(final android.renderscript.Element.DataType type) {\n" +
            "                                                                ~~~~~~~~\n" +
"src/test/pkg/TestEnum.java:61: Error: Enum for switch requires API level 11 (current min is 4): android.renderscript.Element.DataType [NewApi]\n" +
"        switch (type) {\n" +
"        ^\n" +
            "4 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -623,10 +553,7 @@
public void testInnerClassPositions() throws Exception {
// See http://code.google.com/p/android/issues/detail?id=38113
assertEquals(
            "src/test/pkg/ApiCallTest8.java:8: Error: Class requires API level 8 (current min is 4): android.text.style.LeadingMarginSpan.LeadingMarginSpan2 [NewApi]\n" +
            "        LeadingMarginSpan.LeadingMarginSpan2 span = null;        \n" +
            "                          ~~~~~~~~~~~~~~~~~~\n" +
            "1 errors, 0 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",







