/*Sync sdk=>tools/base: [ddmlib] "Add test duration to XmlTestRunListener.java"

Merges platforms/sdk.git 57c517981cce7c25ef6b805fb36986fdd89e6c46
from ddmlib from sdk to tools/base.

Change-Id:I8642fdabd3001c09fe855f6e136d3868ff14936e*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java b/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java
//Synthetic comment -- index 3d160a6..b503fbf 100644

//Synthetic comment -- @@ -37,9 +37,6 @@
* Writes JUnit results to an XML files in a format consistent with
* Ant's XMLJUnitResultFormatter.
* <p/>
 * Unlike Ant's formatter, this class does not report the execution time of
 * tests.
 * <p/>
* Creates a separate XML file per test run.
* <p/>
*/
//Synthetic comment -- @@ -262,7 +259,8 @@
serializer.startTag(ns, TESTCASE);
serializer.attribute(ns, ATTR_NAME, getTestName(testId));
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());
        serializer.attribute(ns, ATTR_TIME, "0");

if (!TestStatus.PASSED.equals(testResult.getStatus())) {
String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;







