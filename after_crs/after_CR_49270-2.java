/*Add test duration to XmlTestRunListener.java

Change-Id:Iebc611a252f09e116fc0c94eddb5156b2c74aa12*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java
//Synthetic comment -- index 2e48afe..27ba476 100644

//Synthetic comment -- @@ -36,9 +36,6 @@
* Writes JUnit results to an XML files in a format consistent with
* Ant's XMLJUnitResultFormatter.
* <p/>
* Creates a separate XML file per test run.
* <p/>
*/
//Synthetic comment -- @@ -211,7 +208,8 @@
serializer.startTag(ns, TESTCASE);
serializer.attribute(ns, ATTR_NAME, testId.getTestName());
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());
        long elapsedTimeMs = testResult.getEndTime() - testResult.getStartTime();
        serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTimeMs));

if (!TestStatus.PASSED.equals(testResult.getStatus())) {
String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;







