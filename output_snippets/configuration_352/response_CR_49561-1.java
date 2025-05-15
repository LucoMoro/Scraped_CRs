//<Beginning of snippet n. 0>


* Writes JUnit results to an XML files in a format consistent with
* Ant's XMLJUnitResultFormatter.
* <p/>
 * Unlike Ant's formatter, this class does not report the execution time of
 * tests.
 * <p/>
* Creates a separate XML file per test run.
* <p/>
*/
long startTime = System.currentTimeMillis();

serializer.startTag(ns, TESTCASE);
serializer.attribute(ns, ATTR_NAME, getTestName(testId));
serializer.attribute(ns, ATTR_CLASSNAME, testId.getClassName());

long duration = System.currentTimeMillis() - startTime;
serializer.attribute(ns, ATTR_TIME, String.valueOf(duration / 1000.0));

if (!TestStatus.PASSED.equals(testResult.getStatus())) {
    String result = testResult.getStatus().equals(TestStatus.FAILURE) ? FAILURE : ERROR;

//<End of snippet n. 0>