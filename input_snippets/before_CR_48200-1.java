
//<Beginning of snippet n. 0>


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

ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
PrintStream writer = new PrintStream(byteArrayOutputStream);
try {
JUnitCore testRunner = new JUnitCore();

            testRunner.addListener(new TextListener(writer));
            testRunner.addListener(new InstrumentationResultPrinter(this));
            addDelayListener(testRunner);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100644


//<End of snippet n. 1>










//<Beginning of snippet n. 2>



@Override
public void testRunFinished(Result result) throws Exception {
        // TODO: implement this
}

/**

//<End of snippet n. 2>










//<Beginning of snippet n. 3>



import org.junit.runner.notification.RunListener;

/**
* A {@link RunListener} that has access to a {@link Instrumentation}. This is useful for
* test result listeners that want to dump data back to the instrumentation results.
public void sendStatus(int code, Bundle bundle) {
getInstrumentation().sendStatus(code, bundle);
}
}

//<End of snippet n. 3>








