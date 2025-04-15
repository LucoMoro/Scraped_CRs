/*More CTS Test Runner Fixes

Changing the initial status of tests now to error, and
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

By setting the default status to ERROR, there won't be any tests
that get away as PASS due to getting no output from the runner.
Unfortunately, this means any tests that aren't executed or
thrown out by annotations, will be flagged. There seems to be
a couple of tests marked with @Suppress that will need to be
changed to @BrokenTest. There is probably a more involved fix
to fix these things, but its probably not worth it at this time.

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








//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java b/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java
//Synthetic comment -- index f60638a..4c62ab1 100644

//Synthetic comment -- @@ -18,11 +18,6 @@

import dalvik.annotation.BrokenTest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//Synthetic comment -- @@ -40,9 +35,12 @@
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class DatabaseCursorTest extends AndroidTestCase implements PerformanceTestCase {
private static final String sString1 = "this is a test";
private static final String sString2 = "and yet another test";
//Synthetic comment -- @@ -371,7 +369,7 @@
}

//@Large
    @BrokenTest("Previously suppressed...")
public void testLoadingThreadDelayRegisterData() throws Exception {
mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data INT);");









//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/NinePatchTest.java b/tests/tests/graphics/src/android/graphics/cts/NinePatchTest.java
//Synthetic comment -- index d69dd4f..fd5cbf6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -34,7 +35,6 @@
import android.graphics.RectF;
import android.graphics.Region;
import android.test.AndroidTestCase;

@TestTargetClass(NinePatch.class)
public class NinePatchTest extends AndroidTestCase {
//Synthetic comment -- @@ -174,7 +174,7 @@
method = "hasAlpha",
args = {}
)
    @BrokenTest(value="Suppressed for current release")
public void testHasAlpha() {
assertFalse(mNinePatch.hasAlpha());
assertEquals(mNinePatch.hasAlpha(), mBitmap.hasAlpha());








//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoActivityRelatedPermissionTest.java
//Synthetic comment -- index 39da4b6..05b582b 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
*/
@UiThreadTest
@MediumTest
@BrokenTest("This test passes, but crashes the UI thread later on. See issues 1909470, 1910487")
public void testSystemAlertWindow() {
final int[] types = new int[] {








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index 49e32d4..9134b9a 100644

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
            mResultCode = CtsTestResult.CODE_ERROR;
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







