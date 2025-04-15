/*Don't Run KnownFailure Tests in CTS

Bug 2814482

Don't run tests marked with the KnownFailure annotation, because
they pass/fail randomly across devices making the CTS reports noisy.

The modification in CollectAllTests will throw out known failure
tests for the android.core.* tests. Entries for these known failures
will no longer appear in the testcase repository of xml files.

The change in DescirptionGenerator throws out the known failures in
the CTS tests like android.net.cts.*. These will no longer show up
as well.

You can still use the runtest utility to run known failures, because
those do not consult the XML files to decide what test cases should
be executed. If you need to exclude KnownFailures for runtest, then
you can add a predicate in InstrumentationCtsTestRunner at the bottom
of the file.

Change-Id:Iab3433b2ab57c04f04b85d5cd95310de805482af*/
//Synthetic comment -- diff --git a/tools/utils/CollectAllTests.java b/tools/utils/CollectAllTests.java
//Synthetic comment -- index 560fdd0..73e1a43 100644

//Synthetic comment -- @@ -265,33 +265,38 @@
public TestResult doRun(Test test) {
return super.doRun(test);
}
            
            
            
};
        
runner.setPrinter(new ResultPrinter(System.out) {
@Override
protected void printFooter(TestResult result) {
}
            
@Override
protected void printHeader(long runTime) {
}
});
runner.doRun(TESTSUITE);
}
    
private String getKnownFailure(final Class<? extends TestCase> testClass,
final String testName) {
return getAnnotation(testClass, testName, KNOWN_FAILURE);
}
    
private boolean isBrokenTest(final Class<? extends TestCase> testClass,
final String testName)  {
return getAnnotation(testClass, testName, BROKEN_TEST) != null;
}
    
private String getAnnotation(final Class<? extends TestCase> testClass,
final String testName, final String annotationName) {
try {
//Synthetic comment -- @@ -329,12 +334,14 @@
String testClassName = test.getClass().getName();
String testName = test.getName();
String knownFailure = getKnownFailure(test.getClass(), testName);
        
        if (isBrokenTest(test.getClass(), testName)) {
System.out.println("ignoring broken test: " + test);
return;
}
            

if (!testName.startsWith("test")) {
try {








//Synthetic comment -- diff --git a/tools/utils/DescriptionGenerator.java b/tools/utils/DescriptionGenerator.java
//Synthetic comment -- index 7edc38f..22cc622 100644

//Synthetic comment -- @@ -80,7 +80,6 @@

static final String ATTRIBUTE_NAME = "name";
static final String ATTRIBUTE_HOST_CONTROLLER = "HostController";
    static final String ATTRIBUTE_KNOWN_FAILURE = "KnownFailure";

static final String XML_OUTPUT_PATH = "./description.xml";

//Synthetic comment -- @@ -404,7 +403,7 @@
elem.getParentNode().removeChild(elem);
} else {
for (TestMethod caze : cases) {
                    if (caze.mIsBroken) {
continue;
}
Node caseNode = elem.appendChild(mDoc.createElement(TAG_TEST));
//Synthetic comment -- @@ -414,10 +413,6 @@
setAttribute(caseNode, ATTRIBUTE_HOST_CONTROLLER, caze.mController);
}

                    if (caze.mKnownFailure != null) {
                        setAttribute(caseNode, ATTRIBUTE_KNOWN_FAILURE, caze.mKnownFailure);
                    }

if (caze.mDescription != null && !caze.mDescription.equals("")) {
caseNode.appendChild(mDoc.createElement(TAG_DESCRIPTION))
.setTextContent(caze.mDescription);







