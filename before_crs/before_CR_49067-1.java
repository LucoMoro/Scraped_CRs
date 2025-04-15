/*Changes to ddmlib to support the builder library.

Change-Id:I15bdbce58af6995c7bfbe610ee919610338469bf*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index fad86dd..c787a12 100644

//Synthetic comment -- @@ -152,6 +152,22 @@
}

/**
* Initializes the <code>ddm</code> library.
* <p/>This must be called once <b>before</b> any call to
* {@link #createBridge(String, boolean)}.
//Synthetic comment -- @@ -172,7 +188,7 @@
* values were changed from the default values.
* <p/>When the application quits, {@link #terminate()} should be called.
* @param clientSupport Indicates whether the library should enable the monitoring and
     * interaction with applications running on the devices.
* @see AndroidDebugBridge#createBridge(String, boolean)
* @see DdmPreferences
*/








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java b/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java
//Synthetic comment -- index c29ff04..d7496d0 100644

//Synthetic comment -- @@ -69,6 +69,8 @@
/** the XML namespace */
private static final String ns = null;

private File mReportDir = new File(System.getProperty("java.io.tmpdir"));

private String mReportPath = "";
//Synthetic comment -- @@ -82,6 +84,18 @@
mReportDir = file;
}

@Override
public void testRunStarted(String runName, int numTests) {
mRunResult = new TestRunResult(runName);
//Synthetic comment -- @@ -175,27 +189,46 @@
}

/**
     * Creates the output stream to use for test results. Exposed for mocking.
*/
    OutputStream createOutputResultStream(File reportDir) throws IOException {
File reportFile = File.createTempFile(TEST_RESULT_FILE_PREFIX, TEST_RESULT_FILE_SUFFIX,
reportDir);
Log.i(LOG_TAG, String.format("Created xml report file at %s",
reportFile.getAbsolutePath()));
mReportPath = reportFile.getAbsolutePath();
return new BufferedOutputStream(new FileOutputStream(reportFile));
}

void printTestResults(KXmlSerializer serializer, String timestamp, long elapsedTime)
throws IOException {
serializer.startTag(ns, TESTSUITE);
        serializer.attribute(ns, ATTR_NAME, mRunResult.getName());
serializer.attribute(ns, ATTR_TESTS, Integer.toString(mRunResult.getNumTests()));
serializer.attribute(ns, ATTR_FAILURES, Integer.toString(mRunResult.getNumFailedTests()));
serializer.attribute(ns, ATTR_ERRORS, Integer.toString(mRunResult.getNumErrorTests()));
serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTime));
serializer.attribute(ns, TIMESTAMP, timestamp);
        serializer.attribute(ns, HOSTNAME, "localhost");
serializer.startTag(ns, PROPERTIES);
serializer.endTag(ns, PROPERTIES);

//Synthetic comment -- @@ -207,11 +240,15 @@
serializer.endTag(ns, TESTSUITE);
}

void print(KXmlSerializer serializer, TestIdentifier testId, TestResult testResult)
throws IOException {

serializer.startTag(ns, TESTCASE);
        serializer.attribute(ns, ATTR_NAME, testId.getTestName());
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());
serializer.attribute(ns, ATTR_TIME, "0");








