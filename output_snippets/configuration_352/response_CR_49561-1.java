//<Beginning of snippet n. 0>
long startTime = System.currentTimeMillis();
serializer.startTag(ns, TESTCASE);
serializer.attribute(ns, ATTR_NAME, getTestName(testId));
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());

TestResult testResult = executeTest(testId);

long endTime = System.currentTimeMillis();
long duration = endTime - startTime;
serializer.attribute(ns, ATTR_TIME, String.valueOf(duration));

switch (testResult.getStatus()) {
    case PASSED:
        serializer.attribute(ns, ATTR_RESULT, TestStatus.PASSED.toString());
        break;
    case FAILURE:
        serializer.attribute(ns, ATTR_RESULT, TestStatus.FAILURE.toString());
        break;
    default:
        serializer.attribute(ns, ATTR_RESULT, TestStatus.ERROR.toString());
        break;
}
//<End of snippet n. 0>