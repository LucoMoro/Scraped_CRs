//<Beginning of snippet n. 0>

long startTime = System.currentTimeMillis();
serializer.startTag(ns, TESTCASE);
serializer.attribute(ns, ATTR_NAME, getTestName(testId));
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());

// Execute the test here (not shown in the snippet)
// Assuming the test is executed, and result is retrieved

long endTime = System.currentTimeMillis();
long duration = endTime - startTime;
serializer.attribute(ns, ATTR_TIME, String.format("%.3f", duration / 1000.0));

if (!TestStatus.PASSED.equals(testResult.getStatus())) {
    String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;

//<End of snippet n. 0>