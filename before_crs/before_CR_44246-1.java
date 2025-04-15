/*Add NativeCrypto error checking to test runs

Some errors were going unnoticed after a test run because they were
pushed to the OpenSSL error stack and not checked after a test run. This
adds a check at the end of each test to see whether the OpenSSL error
state was cleared properly so we can catch these errors early.

Change-Id:Ic1444ecbd05121b2b2db9a51c1dbcbd7bb14a9f8*/
//Synthetic comment -- diff --git a/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java b/tests/core/runner/src/android/test/InstrumentationCtsTestRunner.java
//Synthetic comment -- index cc7d7b0..52d3b6d 100644

//Synthetic comment -- @@ -19,9 +19,12 @@
import com.android.internal.util.Predicate;
import com.android.internal.util.Predicates;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.SideEffect;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
//Synthetic comment -- @@ -47,6 +50,8 @@
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

/**
* This test runner extends the default InstrumentationTestRunner. It overrides
//Synthetic comment -- @@ -114,6 +119,8 @@
protected AndroidTestRunner getAndroidTestRunner() {
AndroidTestRunner runner = super.getAndroidTestRunner();

runner.addTestListener(new TestListener() {
/**
* The last test class we executed code from.
//Synthetic comment -- @@ -228,6 +235,42 @@
return runner;
}

// http://code.google.com/p/vogar/source/browse/trunk/src/vogar/target/TestEnvironment.java
static class TestEnvironment {
private Locale mDefaultLocale;







