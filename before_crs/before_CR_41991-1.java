/*Tweak API check error messages for constructors

Instead of referring to "java.io.IOException#<init>" in the
error messages, refer to this method as
"new java.io.IOException", since the fact that constructors
get mapped to the internal name "<init>" isn't something
everybody knows.

Change-Id:I4e9aacd9cfd3acae340737d4e3094c6dbdcf52f5*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index fda22ad..8762a0c 100644

//Synthetic comment -- @@ -343,7 +343,12 @@
while (owner != null) {
int api = mApiDatabase.getCallVersion(owner, name, desc);
if (api > minSdk) {
                            String fqcn = owner.replace('/', '.') + '#' + name;
String message = String.format(
"Call requires API level %1$d (current min is %2$d): %3$s",
api, minSdk, fqcn);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java
//Synthetic comment -- index bfd7c0f..f455f84 100644

//Synthetic comment -- @@ -375,11 +375,10 @@
public void testIOException() throws Exception {
// See http://code.google.com/p/android/issues/detail?id=35190
assertEquals(
            "src/test/pkg/ApiCallTest6.java:8: Error: Call requires API level 9 (current min is 1): java.io.IOException#<init> [NewApi]\n" +
"        IOException ioException = new IOException(throwable);\n" +
"        ~~~~~~~~~~~\n" +
            "1 errors, 0 warnings\n" +
            "",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -462,14 +461,13 @@
// These errors are correctly -not- suppressed because they
// appear outside the middle inner class suppressing its own errors
// and its child's errors
            "src/test/pkg/ApiCallTest4.java:9: Error: Call requires API level 14 (current min is 1): android.widget.GridLayout#<init> [NewApi]\n" +
"        new GridLayout(null, null, 0);\n" +
"            ~~~~~~~~~~\n" +
            "src/test/pkg/ApiCallTest4.java:38: Error: Call requires API level 14 (current min is 1): android.widget.GridLayout#<init> [NewApi]\n" +
"            new GridLayout(null, null, 0);\n" +
"                ~~~~~~~~~~\n" +
            "2 errors, 0 warnings\n" +
            "",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -508,11 +506,10 @@

public void testTargetAnnotationInner() throws Exception {
assertEquals(
            "src/test/pkg/ApiTargetTest2.java:32: Error: Call requires API level 14 (current min is 3): android.widget.GridLayout#<init> [NewApi]\n" +
"                        new GridLayout(null, null, 0);\n" +
"                            ~~~~~~~~~~\n" +
            "1 errors, 0 warnings\n" +
            "",

lintProject(
"apicheck/classpath=>.classpath",
//Synthetic comment -- @@ -548,10 +545,10 @@
public void testSuper() throws Exception {
// See http://code.google.com/p/android/issues/detail?id=36384
assertEquals(
            "src/test/pkg/ApiCallTest7.java:8: Error: Call requires API level 9 (current min is 4): java.io.IOException#<init> [NewApi]\n" +
"        super(message, cause); // API 9\n" +
"        ~~~~~\n" +
            "src/test/pkg/ApiCallTest7.java:12: Error: Call requires API level 9 (current min is 4): java.io.IOException#<init> [NewApi]\n" +
"        super.toString(); throw new IOException((Throwable) null); // API 9\n" +
"                                    ~~~~~~~~~~~\n" +
"2 errors, 0 warnings\n",







