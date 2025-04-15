/*Add support for code coverage.

Change-Id:Id8ff6f93f3f31e79e9383a3848cda46599702fd6*/
//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 21112df..90a3752 100644

//Synthetic comment -- @@ -24,15 +24,20 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
* An {@link Instrumentation} that runs JUnit3 and JUnit4 tests against
//Synthetic comment -- @@ -104,6 +109,15 @@
* test execution. Useful for quickly obtaining info on the tests to be executed by an
* instrumentation command.
* <p/>
*/
public class AndroidJUnitRunner extends Instrumentation {

//Synthetic comment -- @@ -114,10 +128,11 @@
private static final String ARGUMENT_ANNOTATION = "annotation";
private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";
private static final String ARGUMENT_DELAY_MSEC = "delay_msec";

private static final String LOG_TAG = "AndroidJUnitRunner";

    private final Bundle mResults = new Bundle();
private Bundle mArguments;

@Override
//Synthetic comment -- @@ -171,12 +186,15 @@

ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
PrintStream writer = new PrintStream(byteArrayOutputStream);
try {
JUnitCore testRunner = new JUnitCore();

            testRunner.addListener(new TextListener(writer));
            testRunner.addListener(new InstrumentationResultPrinter(this));
            addDelayListener(testRunner);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -191,15 +209,53 @@
t.printStackTrace(writer);

} finally {
writer.close();
            mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT,
String.format("\n%s",
byteArrayOutputStream.toString()));
            finish(Activity.RESULT_OK, mResults);
}

}

/**
* Builds a {@link TestRequest} based on given input arguments.
* <p/>
//Synthetic comment -- @@ -267,20 +323,4 @@
testRequestBuilder.addTestClass(testClassName);
}
}

    /**
     * Sets up listener to inject {@link #ARGUMENT_DELAY_MSEC}, if specified.
     * @param testRunner
     */
    private void addDelayListener(JUnitCore testRunner) {
        try {
            Object delay = getArguments().get(ARGUMENT_DELAY_MSEC);  // Accept either string or int
            if (delay != null) {
                int delayMsec = Integer.parseInt(delay.toString());
                testRunner.addListener(new DelayInjector(delayMsec));
            }
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Invalid delay_msec parameter", e);
        }
    }
}








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/CoverageListener.java b/androidtestlib/src/com/android/test/runner/listener/CoverageListener.java
new file mode 100644
//Synthetic comment -- index 0000000..4b36ea3

//Synthetic comment -- @@ -0,0 +1,105 @@








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/InstrumentationResultPrinter.java b/androidtestlib/src/com/android/test/runner/listener/InstrumentationResultPrinter.java
//Synthetic comment -- index cc38829..df392e2 100644

//Synthetic comment -- @@ -105,7 +105,6 @@

@Override
public void testRunFinished(Result result) throws Exception {
        // TODO: implement this
}

/**








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/InstrumentationRunListener.java b/androidtestlib/src/com/android/test/runner/listener/InstrumentationRunListener.java
//Synthetic comment -- index f189cf4..e9370af 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

import org.junit.runner.notification.RunListener;

/**
* A {@link RunListener} that has access to a {@link Instrumentation}. This is useful for
* test result listeners that want to dump data back to the instrumentation results.
//Synthetic comment -- @@ -42,4 +44,15 @@
public void sendStatus(int code, Bundle bundle) {
getInstrumentation().sendStatus(code, bundle);
}
}







