/*Add support for code coverage.

Change-Id:Id8ff6f93f3f31e79e9383a3848cda46599702fd6*/




//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 21112df..90a3752 100644

//Synthetic comment -- @@ -24,15 +24,20 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.android.test.runner.listener.CoverageListener;
import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;
import com.android.test.runner.listener.InstrumentationRunListener;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
* An {@link Instrumentation} that runs JUnit3 and JUnit4 tests against
//Synthetic comment -- @@ -104,6 +109,15 @@
* test execution. Useful for quickly obtaining info on the tests to be executed by an
* instrumentation command.
* <p/>
 * <b>To generate EMMA code coverage:</b>
 * -e coverage true
 * Note: this requires an emma instrumented build. By default, the code coverage results file
 * will be saved in a /data/<app>/coverage.ec file, unless overridden by coverageFile flag (see
 * below)
 * <p/>
 * <b> To specify EMMA code coverage results file path:</b>
 * -e coverageFile /sdcard/myFile.ec
 * <p/>
*/
public class AndroidJUnitRunner extends Instrumentation {

//Synthetic comment -- @@ -114,10 +128,11 @@
private static final String ARGUMENT_ANNOTATION = "annotation";
private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";
private static final String ARGUMENT_DELAY_MSEC = "delay_msec";
    private static final String ARGUMENT_COVERAGE = "coverage";
    private static final String ARGUMENT_COVERAGE_PATH = "coverageFile";

private static final String LOG_TAG = "AndroidJUnitRunner";

private Bundle mArguments;

@Override
//Synthetic comment -- @@ -171,12 +186,15 @@

ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
PrintStream writer = new PrintStream(byteArrayOutputStream);
        List<RunListener> listeners = new ArrayList<RunListener>();

try {
JUnitCore testRunner = new JUnitCore();

            addListener(listeners, testRunner, new TextListener(writer));
            addListener(listeners, testRunner, new InstrumentationResultPrinter(this));
            addDelayListener(listeners, testRunner);
            addCoverageListener(listeners, testRunner);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -191,15 +209,53 @@
t.printStackTrace(writer);

} finally {
            Bundle results = new Bundle();
            reportRunEnded(listeners, writer, results);
writer.close();
            results.putString(Instrumentation.REPORT_KEY_STREAMRESULT,
String.format("\n%s",
byteArrayOutputStream.toString()));
            finish(Activity.RESULT_OK, results);
}

}

    private void addListener(List<RunListener> list, JUnitCore testRunner, RunListener listener) {
        list.add(listener);
        testRunner.addListener(listener);
    }

    private void addCoverageListener(List<RunListener> list, JUnitCore testRunner) {
        if (getBooleanArgument(ARGUMENT_COVERAGE)) {
            String coverageFilePath = getArguments().getString(ARGUMENT_COVERAGE_PATH);
            addListener(list, testRunner, new CoverageListener(this, coverageFilePath));
        }
    }

    /**
     * Sets up listener to inject {@link #ARGUMENT_DELAY_MSEC}, if specified.
     * @param testRunner
     */
    private void addDelayListener(List<RunListener> list, JUnitCore testRunner) {
        try {
            Object delay = getArguments().get(ARGUMENT_DELAY_MSEC);  // Accept either string or int
            if (delay != null) {
                int delayMsec = Integer.parseInt(delay.toString());
                addListener(list, testRunner, new DelayInjector(delayMsec));
            }
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Invalid delay_msec parameter", e);
        }
    }

    private void reportRunEnded(List<RunListener> listeners, PrintStream writer, Bundle results) {
        for (RunListener listener : listeners) {
            if (listener instanceof InstrumentationRunListener) {
                ((InstrumentationRunListener)listener).instrumentationRunFinished(writer, results);
            }
        }
    }

/**
* Builds a {@link TestRequest} based on given input arguments.
* <p/>
//Synthetic comment -- @@ -267,20 +323,4 @@
testRequestBuilder.addTestClass(testClassName);
}
}
}








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/CoverageListener.java b/androidtestlib/src/com/android/test/runner/listener/CoverageListener.java
new file mode 100644
//Synthetic comment -- index 0000000..4b36ea3

//Synthetic comment -- @@ -0,0 +1,105 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.test.runner.listener;

import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A test {@link RunListener} that generates EMMA code coverage.
 */
public class CoverageListener extends InstrumentationRunListener {

    private String mCoverageFilePath;

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the path to the generated code coverage file.
     */
    private static final String REPORT_KEY_COVERAGE_PATH = "coverageFilePath";
    // Default file name for code coverage
    private static final String DEFAULT_COVERAGE_FILE_NAME = "coverage.ec";

    private static final String LOG_TAG = null;

    /**
     * Creates a {@link CoverageListener).
     *
     * @param instr the {@link Instrumentation} that the test is running under
     * @param customCoverageFilePath an optional user specified path for the coverage file
     *         If null, file will be generated in test app's file directory.
     */
    public CoverageListener(Instrumentation instr, String customCoverageFilePath) {
        super(instr);
        mCoverageFilePath = customCoverageFilePath;
        if (mCoverageFilePath == null) {
            mCoverageFilePath = instr.getTargetContext().getFilesDir().getAbsolutePath() +
                    File.separator + DEFAULT_COVERAGE_FILE_NAME;
        }
    }

    @Override
    public void instrumentationRunFinished(PrintStream writer, Bundle results) {
        generateCoverageReport(writer, results);
    }

    private void generateCoverageReport(PrintStream writer, Bundle results) {
        // use reflection to call emma dump coverage method, to avoid
        // always statically compiling against emma jar
        java.io.File coverageFile = new java.io.File(mCoverageFilePath);
        try {
            Class<?> emmaRTClass = Class.forName("com.vladium.emma.rt.RT");
            Method dumpCoverageMethod = emmaRTClass.getMethod("dumpCoverageData",
                    coverageFile.getClass(), boolean.class, boolean.class);

            dumpCoverageMethod.invoke(null, coverageFile, false, false);

            // output path to generated coverage file so it can be parsed by a test harness if
            // needed
            results.putString(REPORT_KEY_COVERAGE_PATH, mCoverageFilePath);
            // also output a more user friendly msg
            writer.format("\nGenerated code coverage data to %s",mCoverageFilePath);
        } catch (ClassNotFoundException e) {
            reportEmmaError(writer, "Is emma jar on classpath?", e);
        } catch (SecurityException e) {
            reportEmmaError(writer, e);
        } catch (NoSuchMethodException e) {
            reportEmmaError(writer, e);
        } catch (IllegalArgumentException e) {
            reportEmmaError(writer, e);
        } catch (IllegalAccessException e) {
            reportEmmaError(writer, e);
        } catch (InvocationTargetException e) {
            reportEmmaError(writer, e);
        }
    }

    private void reportEmmaError(PrintStream writer, Exception e) {
        reportEmmaError(writer, "", e);
    }

    private void reportEmmaError(PrintStream writer, String hint, Exception e) {
        String msg = "Failed to generate emma coverage. " + hint;
        Log.e(LOG_TAG, msg, e);
        writer.format("\nError: %s", msg);
    }
}








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/InstrumentationResultPrinter.java b/androidtestlib/src/com/android/test/runner/listener/InstrumentationResultPrinter.java
//Synthetic comment -- index cc38829..df392e2 100644

//Synthetic comment -- @@ -105,7 +105,6 @@

@Override
public void testRunFinished(Result result) throws Exception {
}

/**








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/InstrumentationRunListener.java b/androidtestlib/src/com/android/test/runner/listener/InstrumentationRunListener.java
//Synthetic comment -- index f189cf4..e9370af 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

import org.junit.runner.notification.RunListener;

import java.io.PrintStream;

/**
* A {@link RunListener} that has access to a {@link Instrumentation}. This is useful for
* test result listeners that want to dump data back to the instrumentation results.
//Synthetic comment -- @@ -42,4 +44,15 @@
public void sendStatus(int code, Bundle bundle) {
getInstrumentation().sendStatus(code, bundle);
}

    /**
     * Optional callback subclasses can implement. Will be called when instrumentation run
     * completes.
     *
     * @param streamResult the {@link PrintStream} to instrumentation out.
     * @param resultBundle the instrumentation result bundle. Can be used to inject key-value pairs
     * into the instrumentation output when run in -r/raw mode
     */
    public void instrumentationRunFinished(PrintStream streamResult, Bundle resultBundle) {
    }
}







