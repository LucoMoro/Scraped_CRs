/*jUnit XML file report execution time in seconds, not ms.

Change-Id:I4d2375564e0f48ba39ebff7f0340ed56260d0d2c*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java b/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java
//Synthetic comment -- index b503fbf..18ce841 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
serializer.attribute(ns, ATTR_TESTS, Integer.toString(mRunResult.getNumTests()));
serializer.attribute(ns, ATTR_FAILURES, Integer.toString(mRunResult.getNumFailedTests()));
serializer.attribute(ns, ATTR_ERRORS, Integer.toString(mRunResult.getNumErrorTests()));
        serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTime));
serializer.attribute(ns, TIMESTAMP, timestamp);
serializer.attribute(ns, HOSTNAME, mHostName);

//Synthetic comment -- @@ -260,7 +260,7 @@
serializer.attribute(ns, ATTR_NAME, getTestName(testId));
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());
long elapsedTimeMs = testResult.getEndTime() - testResult.getStartTime();
        serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTimeMs));

if (!TestStatus.PASSED.equals(testResult.getStatus())) {
String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;







