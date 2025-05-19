//<Beginning of snippet n. 0>

import android.test.suitebuilder.annotation.LargeTest;

import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;

public class AndroidJUnitRunner extends Instrumentation {

    private static final String ARGUMENT_ANNOTATION = "annotation";
    private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";
    private static final String ARGUMENT_DELAY_MSEC = "delay_msec";

    private static final String LOG_TAG = "AndroidJUnitRunner";

    private final Bundle mResults = new Bundle();
    private Bundle mArguments;

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        mArguments = arguments;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JUnitCore testRunner = new JUnitCore();

            testRunner.addListener(new TextListener(new PrintStream(byteArrayOutputStream)));
            testRunner.addListener(new InstrumentationResultPrinter(this));
            addDelayListener(testRunner);

            TestRequest testRequest = buildRequest(getArguments());
            Result result = testRunner.run(testRequest.getRequest());
            testRunFinished(result);

        } finally {
            mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT,
                String.format("\n%s", byteArrayOutputStream.toString()));
            finish(Activity.RESULT_OK, mResults);
        }
    }

    private TestRequest buildRequest(Bundle arguments) {
        return new TestRequest(arguments.getString(ARGUMENT_ANNOTATION), arguments.getString(ARGUMENT_NOT_ANNOTATION));
    }

    private void addDelayListener(JUnitCore testRunner) {
        try {
            Object delay = getArguments().get(ARGUMENT_DELAY_MSEC);
            if (delay != null) {
                int delayMsec = Integer.parseInt(delay.toString());
                if (delayMsec >= 0) {
                    testRunner.addListener(new DelayInjector(delayMsec));
                } else {
                    Log.e(LOG_TAG, "delay_msec cannot be negative");
                }
            }
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Invalid delay_msec parameter");
        }
    }

    @Override
    public void testRunFinished(Result result) {
        if (result != null) {
            Log.i(LOG_TAG, "Test run finished. Passed: " + result.wasSuccessful() +
                ", Failed: " + result.getFailureCount());
        } else {
            Log.e(LOG_TAG, "Result object is null, unable to report test run outcomes.");
        }
    }

//<End of snippet n. 0>