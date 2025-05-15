//<Beginning of snippet n. 0>
long startTime = System.currentTimeMillis();

try {
    serializer.startTag(ns, TESTCASE);
    serializer.attribute(ns, ATTR_NAME, getTestName(testId));
    serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());

    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    String formattedDuration = String.format("%.3f", duration / 1000.0);
    serializer.attribute(ns, ATTR_TIME, formattedDuration);
    
    if (!TestStatus.PASSED.equals(testResult.getStatus())) {
        String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;
    }
} catch (Exception e) {
    // Implement error handling logic here to manage timing failures without affecting test output
}
//<End of snippet n. 0>