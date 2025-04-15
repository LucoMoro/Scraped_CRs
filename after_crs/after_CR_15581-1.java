/*Speculative fix for intermittent CTS UI test failures.

UI related tests fail if the keyguard is currently displayed.
Previously, the CTS harness used a separate util app which would disable the
keyguard upon receiving a broadcast.

However, if the util app's process is killed by the system, the keyguard is
immediately displayed. Its not known for sure, but this could explain the
intermittent UI test failures.

This commit adds logic to disable the keyguard in the CTS test runner itself.

Bug 2342725.

Change-Id:I6fbc89fb895f56527d367508f455f5cbaa1a6f46*/




//Synthetic comment -- diff --git a/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java b/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java
//Synthetic comment -- index 5ac7251..4ca1cc2 100644

//Synthetic comment -- @@ -16,27 +16,31 @@

package android.test;

import com.android.internal.util.Predicate;
import com.android.internal.util.Predicates;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.SideEffect;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.HasAnnotation;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

/**
* This test runner extends the default InstrumentationTestRunner. It overrides
* the {@code onCreate(Bundle)} method and sets the system properties necessary
//Synthetic comment -- @@ -52,37 +56,50 @@

/**
* Convenience definition of our log tag.
     */
private static final String TAG = "InstrumentationCtsTestRunner";

/**
* True if (and only if) we are running in single-test mode (as opposed to
* batch mode).
*/
private boolean singleTest = false;

@Override
public void onCreate(Bundle arguments) {
// We might want to move this to /sdcard, if is is mounted/writable.
File cacheDir = getTargetContext().getCacheDir();

        // Set some properties that the core tests absolutely need.
System.setProperty("user.language", "en");
System.setProperty("user.region", "US");

System.setProperty("java.home", cacheDir.getAbsolutePath());
System.setProperty("user.home", cacheDir.getAbsolutePath());
System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
System.setProperty("javax.net.ssl.trustStore",
"/etc/security/cacerts.bks");

TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

if (arguments != null) {
String classArg = arguments.getString(ARGUMENT_TEST_CLASS);
            singleTest = classArg != null && classArg.contains("#");
}

        // attempt to disable keyguard,  if current test has permission to do so
        // TODO: move this to a better place, such as InstrumentationTestRunner ?
        if (getContext().checkCallingOrSelfPermission(android.Manifest.permission.DISABLE_KEYGUARD)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Disabling keyguard");
            KeyguardManager keyguardManager =
                (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.newKeyguardLock("cts").disableKeyguard();
        } else {
            Log.i(TAG, "Test lacks permission to disable keyguard. " +
                    "UI based tests may fail if keyguard is up");
        }

super.onCreate(arguments);
}

//Synthetic comment -- @@ -92,36 +109,36 @@

runner.addTestListener(new TestListener() {
/**
             * The last test class we executed code from.
*/
private Class<?> lastClass;

/**
* The minimum time we expect a test to take.
*/
private static final int MINIMUM_TIME = 100;

/**
* The start time of our current test in System.currentTimeMillis().
*/
private long startTime;

public void startTest(Test test) {
if (test.getClass() != lastClass) {
lastClass = test.getClass();
printMemory(test.getClass());
}

Thread.currentThread().setContextClassLoader(
test.getClass().getClassLoader());

startTime = System.currentTimeMillis();
}

public void endTest(Test test) {
if (test instanceof TestCase) {
cleanup((TestCase)test);

/*
* Make sure all tests take at least MINIMUM_TIME to
* complete. If they don't, we wait a bit. The Cupcake
//Synthetic comment -- @@ -129,7 +146,7 @@
* short time, which causes headache for the CTS.
*/
long timeTaken = System.currentTimeMillis() - startTime;

if (timeTaken < MINIMUM_TIME) {
try {
Thread.sleep(MINIMUM_TIME - timeTaken);
//Synthetic comment -- @@ -139,15 +156,15 @@
}
}
}

public void addError(Test test, Throwable t) {
// This space intentionally left blank.
}

public void addFailure(Test test, AssertionFailedError t) {
// This space intentionally left blank.
}

/**
* Dumps some memory info.
*/
//Synthetic comment -- @@ -157,7 +174,7 @@
long total = runtime.totalMemory();
long free = runtime.freeMemory();
long used = total - free;

Log.d(TAG, "Total memory  : " + total);
Log.d(TAG, "Used memory   : " + used);
Log.d(TAG, "Free memory   : " + free);
//Synthetic comment -- @@ -173,7 +190,7 @@
*/
private void cleanup(TestCase test) {
Class<?> clazz = test.getClass();

while (clazz != TestCase.class) {
Field[] fields = clazz.getDeclaredFields();
for (int i = 0; i < fields.length; i++) {
//Synthetic comment -- @@ -188,15 +205,15 @@
}
}
}

clazz = clazz.getSuperclass();
}
}

});

return runner;
    }

@Override
List<Predicate<TestMethod>> getBuilderRequirements() {







