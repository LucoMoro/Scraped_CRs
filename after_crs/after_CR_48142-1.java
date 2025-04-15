/*Add support for delay_msec arg.

Change-Id:I1c4d9f4ab65751052ee154f32f0745fb9d670363*/




//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 557e293..21112df 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.android.test.runner.listener.DelayInjector;
import com.android.test.runner.listener.InstrumentationResultPrinter;

import org.junit.internal.TextListener;
//Synthetic comment -- @@ -112,6 +113,7 @@
private static final String ARGUMENT_LOG_ONLY = "log";
private static final String ARGUMENT_ANNOTATION = "annotation";
private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";
    private static final String ARGUMENT_DELAY_MSEC = "delay_msec";

private static final String LOG_TAG = "AndroidJUnitRunner";

//Synthetic comment -- @@ -174,6 +176,7 @@

testRunner.addListener(new TextListener(writer));
testRunner.addListener(new InstrumentationResultPrinter(this));
            addDelayListener(testRunner);

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -264,4 +267,20 @@
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








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/DelayInjector.java b/androidtestlib/src/com/android/test/runner/listener/DelayInjector.java
new file mode 100644
//Synthetic comment -- index 0000000..b092029

//Synthetic comment -- @@ -0,0 +1,56 @@
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

import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * A {@link RunListener} that injects a given delay between tests.
 */
public class DelayInjector extends RunListener {

    private final int mDelayMsec;

    /**
     * @param delayMsec
     */
    public DelayInjector(int delayMsec) {
        mDelayMsec = delayMsec;
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        // delay before first test
        delay();
    }

    @Override
    public void testFinished(Description description) throws Exception {
        // delay after every test
        delay();
    }

    private void delay() {
        try {
            Thread.sleep(mDelayMsec);
        } catch (InterruptedException e) {
            Log.e("DelayInjector", "interrupted", e);
        }
    }
}







