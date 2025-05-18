//<Beginning of snippet n. 0>


import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
* An {@link Instrumentation} that runs JUnit3 and JUnit4 tests against
* test execution. Useful for quickly obtaining info on the tests to be executed by an
* instrumentation command.
* <p/>
*/
public class AndroidJUnitRunner extends Instrumentation {

private static final String ARGUMENT_ANNOTATION = "annotation";
private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";
private static final String ARGUMENT_DELAY_MSEC = "delay_msec";
private static final String LOG_TAG = "AndroidJUnitRunner";

private final Bundle mResults = new Bundle();
private Bundle mArguments;

@Override
public void onStart() {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream writer = new PrintStream(byteArrayOutputStream);
    try {
        JUnitCore testRunner = new JUnitCore();
        testRunner.addListener(new TextListener(writer));
        testRunner.addListener(new InstrumentationResultPrinter(this));
        addDelayListener(testRunner);
        
        TestRequest testRequest = buildRequest(getArguments(), writer);
        Result result = testRunner.run(testRequest.getRequest());
        
        logTestResults(result);

    } finally {
        writer.close();
        mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT,
                String.format("\n%s", byteArrayOutputStream.toString()));
        finish(Activity.RESULT_OK, mResults);
    }
}

/**
* Builds a {@link TestRequest} based on given input arguments.
* <p/>
*/
private TestRequest buildRequest(Bundle arguments, PrintStream writer) {
    String annotation = arguments.getString(ARGUMENT_ANNOTATION);
    String notAnnotation = arguments.getString(ARGUMENT_NOT_ANNOTATION);
    // Logic to create and return a TestRequest
    // Example placeholder implementation, modify as needed
    return new TestRequest(); // Adjusting to create actual instance based on annotations if necessary
}

/**
 * Sets up listener to inject {@link #ARGUMENT_DELAY_MSEC}, if specified.
 * @param testRunner
 */
private void addDelayListener(JUnitCore testRunner) {
    try {
        Object delay = getArguments().get(ARGUMENT_DELAY_MSEC);
        if (delay != null) {
            int delayMsec = Integer.parseInt(delay.toString());
            testRunner.addListener(new DelayInjector(delayMsec));
        }
    } catch (NumberFormatException e) {
        Log.e(LOG_TAG, "Invalid delay_msec parameter", e);
    }
}

private void logTestResults(Result result) {
    String resultString = String.format("Tests run: %d, Passed: %d, Failed: %d", 
            result.getRunCount(), result.getRunCount() - result.getFailureCount(), result.getFailureCount());
    Log.i(LOG_TAG, resultString);
}

@Override
public void testRunFinished(Result result) throws Exception {
    logTestResults(result);
}

//<End of snippet n. 0>