/*More CTS Test Runner Fixes

Changing the initial status of tests now to fail, and
making InstrumentationCtsTestRunner explicitly report
when it has omitted a test due to profile or feature
requirements only.

More background on why:

Initially, the starting test status was set to PASS.
This seemed like a bug, because tests that did not
get a result back from the test runner would automatically
pass.

I noticed this when adding the profile/feature annotations,
so I changed the test status to NOT_EXECUTED. However,
I discovered that batch test runner would rerun any tests
that were not executed, so any tests that were discarded
by the annotations would be rerun infinitely..

So then...I changed the test status to start off as OMITTED,
so that discarded tests which don't produce any output would
get flagged as OMITTED and not cause infinite retrying.

However, this uncovered a whole class of tests that were
being omitted due to test errors (previously marked PASS even
though they were failing). This is because the tests would
fail to execute as a batch and then CTS as part of its algorithm
would run them individually again. However, due to an error
of being unable to load the test class, they would be marked
as PASS (before the OMITTED change), because there would be
output from the runner...

By setting the default status to FAIL, there won't be any tests
that get away as PASS due to getting no output from the runner.
Unfortunately, this means any tests that aren't executed or
thrown out by annotations, will be flagged. There seems to be
a couple of tests marked with @Suppress that will need to be
changed to @BrokenTest. There is probably a more involved fix
to fix these things, but its probably not worth it at this time.

The default status also cannot be ERROR, because CTS sees both
errors and failures as the same.

Change-Id:If1d561ee0e8c4919287469238d29c72c1ba0475c*/




//Synthetic comment -- diff --git a/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java b/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java
//Synthetic comment -- index f2098ea..fbd89ad 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.annotation.cts.Profile;
import android.annotation.cts.RequiredFeatures;
import android.annotation.cts.SupportedProfiles;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
//Synthetic comment -- @@ -68,6 +69,10 @@

private static final String ARGUMENT_PROFILE = "profile";

    private static final String REPORT_VALUE_ID = "InstrumentationCtsTestRunner";

    private static final int REPORT_VALUE_RESULT_OMITTED = -3;

/** Profile of the device being tested or null to run all tests regardless of profile. */
private Profile mProfile;

//Synthetic comment -- @@ -256,9 +261,42 @@
return builderRequirements;
}

    /**
     * Send back an indication that a test was omitted. InstrumentationTestRunner won't run omitted
     * tests, but CTS needs to know that the test was omitted. Otherwise, it will attempt to rerun
     * the test thinking that ADB must have crashed or something.
     */
    private void sendOmittedStatus(TestMethod t) {
        Bundle bundle = new Bundle();
        bundle.putString(Instrumentation.REPORT_KEY_IDENTIFIER, REPORT_VALUE_ID);
        bundle.putInt(InstrumentationTestRunner.REPORT_KEY_NUM_TOTAL, 1);
        bundle.putInt(InstrumentationTestRunner.REPORT_KEY_NUM_CURRENT, 1);
        bundle.putString(InstrumentationTestRunner.REPORT_KEY_NAME_CLASS,
                t.getEnclosingClassname());
        bundle.putString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST,
                t.getName());

        // First status message causes CTS to print out the test name like "Class#test..."
        sendStatus(InstrumentationTestRunner.REPORT_VALUE_RESULT_START, bundle);

        // Second status message causes CTS to complete the line like "Class#test...(omitted)"
        sendStatus(REPORT_VALUE_RESULT_OMITTED, bundle);
    }

private Predicate<TestMethod> getProfilePredicate(final Profile specifiedProfile) {
return new Predicate<TestMethod>() {
public boolean apply(TestMethod t) {
                if (isValidTest(t)) {
                    // InstrumentationTestRunner will run the test and send back results.
                    return true;
                } else {
                    // InstrumentationTestRunner WON'T run the test, so send back omitted status.
                    sendOmittedStatus(t);
                    return false;
                }
            }

            private boolean isValidTest(TestMethod t) {
Set<Profile> profiles = new HashSet<Profile>();
add(profiles, t.getAnnotation(SupportedProfiles.class));
add(profiles, t.getEnclosingClass().getAnnotation(SupportedProfiles.class));
//Synthetic comment -- @@ -290,6 +328,17 @@
private Predicate<TestMethod> getFeaturePredicate() {
return new Predicate<TestMethod>() {
public boolean apply(TestMethod t) {
                if (isValidTest(t)) {
                    // InstrumentationTestRunner will run the test and send back results.
                    return true;
                } else {
                    // InstrumentationTestRunner WON'T run the test, so send back omitted status.
                    sendOmittedStatus(t);
                    return false;
                }
            }

            private boolean isValidTest(TestMethod t) {
Set<String> features = new HashSet<String>();
add(features, t.getAnnotation(RequiredFeatures.class));
add(features, t.getEnclosingClass().getAnnotation(RequiredFeatures.class));








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index 49e32d4..7b314af 100644

//Synthetic comment -- @@ -1212,6 +1212,7 @@
public final static int STATUS_PASS = 0;
public final static int STATUS_FAIL = -1;
public final static int STATUS_ERROR = -2;
        public final static int STATUS_OMITTED = -3;

private ArrayList<String> mResultLines;

//Synthetic comment -- @@ -1230,7 +1231,7 @@
mResultLines = new ArrayList<String>();
mStackTrace = null;
mFailedMsg = null;
            mResultCode = CtsTestResult.CODE_FAIL;
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -1426,6 +1427,10 @@
case STATUS_ERROR:
mResultCode = CtsTestResult.CODE_FAIL;
break;

            case STATUS_OMITTED:
                mResultCode = CtsTestResult.CODE_OMITTED;
                break;
}
}

//Synthetic comment -- @@ -1491,6 +1496,10 @@
case STATUS_PASS:
mResultCode = CtsTestResult.CODE_PASS;
break;

                case STATUS_OMITTED:
                    mResultCode = CtsTestResult.CODE_OMITTED;
                    break;
}
resultLines.removeAll(resultLines);
}
//Synthetic comment -- @@ -1542,6 +1551,10 @@
mTest.setResult(new CtsTestResult(
CtsTestResult.CODE_FAIL, mFailedMsg, mStackTrace));
break;

                case STATUS_OMITTED:
                    mTest.setResult(new CtsTestResult(CtsTestResult.CODE_OMITTED));
                    break;
}
}
// report status even if no matching test was found








//Synthetic comment -- diff --git a/tools/utils/CollectAllTests.java b/tools/utils/CollectAllTests.java
//Synthetic comment -- index 73e1a43..7628ba2 100644

//Synthetic comment -- @@ -297,6 +297,11 @@
return getAnnotation(testClass, testName, BROKEN_TEST) != null;
}

    private boolean isSuppressed(final Class<? extends TestCase> testClass,
            final String testName)  {
        return getAnnotation(testClass, testName, SUPPRESSED_TEST) != null;
    }

private String getAnnotation(final Class<? extends TestCase> testClass,
final String testName, final String annotationName) {
try {
//Synthetic comment -- @@ -341,6 +346,9 @@
} else if (isBrokenTest(test.getClass(), testName)) {
System.out.println("ignoring broken test: " + test);
return;
        } else if (isSuppressed(test.getClass(), testName)) {
            System.out.println("ignoring suppressed test: " + test);
            return;
}

if (!testName.startsWith("test")) {
//Synthetic comment -- @@ -359,7 +367,7 @@
testCases.put(testClassName, testClass);
}

        testClass.mCases.add(new TestMethod(testName, "", "", knownFailure, false, false));

try {
test.getClass().getConstructor(new Class<?>[0]);








//Synthetic comment -- diff --git a/tools/utils/DescriptionGenerator.java b/tools/utils/DescriptionGenerator.java
//Synthetic comment -- index 22cc622..99be6dc 100644

//Synthetic comment -- @@ -65,6 +65,7 @@
static final String HOST_CONTROLLER = "dalvik.annotation.HostController";
static final String KNOWN_FAILURE = "dalvik.annotation.KnownFailure";
static final String BROKEN_TEST = "dalvik.annotation.BrokenTest";
    static final String SUPPRESSED_TEST = "android.test.suitebuilder.annotation.Suppress";

static final String JUNIT_TEST_CASE_CLASS_NAME = "junit.framework.testcase";
static final String TAG_PACKAGE = "TestPackage";
//Synthetic comment -- @@ -403,7 +404,7 @@
elem.getParentNode().removeChild(elem);
} else {
for (TestMethod caze : cases) {
                    if (caze.mIsBroken || caze.mIsSuppressed || caze.mKnownFailure != null) {
continue;
}
Node caseNode = elem.appendChild(mDoc.createElement(TAG_TEST));
//Synthetic comment -- @@ -524,6 +525,7 @@
String controller = "";
String knownFailure = null;
boolean isBroken = false;
                boolean isSuppressed = false;
for (AnnotationDesc cAnnot : annotations) {

AnnotationTypeDoc atype = cAnnot.annotationType();
//Synthetic comment -- @@ -533,12 +535,14 @@
knownFailure = getAnnotationDescription(cAnnot);
} else if (atype.toString().equals(BROKEN_TEST)) {
isBroken = true;
                    } else if (atype.toString().equals(SUPPRESSED_TEST)) {
                        isSuppressed = true;
}
}

if (name.startsWith("test")) {
cases.add(new TestMethod(name, method.commentText(), controller, knownFailure,
                            isBroken, isSuppressed));
}
}

//Synthetic comment -- @@ -596,6 +600,7 @@
String mController;
String mKnownFailure;
boolean mIsBroken;
        boolean mIsSuppressed;

/**
* Construct an test case object.
//Synthetic comment -- @@ -605,12 +610,13 @@
* @param knownFailure The reason of known failure.
*/
TestMethod(String name, String description, String controller, String knownFailure,
                boolean isBroken, boolean isSuppressed) {
mName = name;
mDescription = description;
mController = controller;
mKnownFailure = knownFailure;
mIsBroken = isBroken;
            mIsSuppressed = isSuppressed;
}
}
}







