/*EXPERIMENTAL: find OpenSSL error leaks

Change-Id:I2a1a85f9f63f87c2ca063d721386b1b5e9b48f03*/
//Synthetic comment -- diff --git a/test-runner/src/android/test/AndroidTestRunner.java b/test-runner/src/android/test/AndroidTestRunner.java
//Synthetic comment -- index 30876d0..135f2dd 100644

//Synthetic comment -- @@ -21,6 +21,9 @@
import android.os.PerformanceCollector.PerformanceResultsWriter;

import com.google.android.collect.Lists;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
//Synthetic comment -- @@ -178,6 +181,28 @@
public void runTest(TestResult testResult) {
mTestResult = testResult;

for (TestListener testListener : mTestListeners) {
mTestResult.addListener(testListener);
}







