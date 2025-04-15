/*EXPERIMENTAL: find OpenSSL error leaks

Change-Id:I2a1a85f9f63f87c2ca063d721386b1b5e9b48f03*/




//Synthetic comment -- diff --git a/test-runner/src/android/test/AndroidTestRunner.java b/test-runner/src/android/test/AndroidTestRunner.java
//Synthetic comment -- index 30876d0..8c78758 100644

//Synthetic comment -- @@ -21,6 +21,10 @@
import android.os.PerformanceCollector.PerformanceResultsWriter;

import com.google.android.collect.Lists;

import org.apache.harmony.xnet.provider.jsse.NativeCrypto;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
//Synthetic comment -- @@ -178,6 +182,28 @@
public void runTest(TestResult testResult) {
mTestResult = testResult;

        mTestResult.addListener(new TestListener() {

            @Override
            public void startTest(Test test) {
            }

            @Override
            public void endTest(Test test) {
                if (NativeCrypto.ERR_peek_last_error() != 0) {
                    mTestResult.addError(test, new Exception("Lingering OpenSSL errors"));
                }
            }

            @Override
            public void addFailure(Test test, AssertionFailedError t) {
            }

            @Override
            public void addError(Test test, Throwable t) {
            }
        });

for (TestListener testListener : mTestListeners) {
mTestResult.addListener(testListener);
}







