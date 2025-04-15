/*Add support for delay_msec arg.

Change-Id:I1c4d9f4ab65751052ee154f32f0745fb9d670363*/
//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java b/androidtestlib/src/com/android/test/runner/AndroidJUnitRunner.java
//Synthetic comment -- index 557e293..21112df 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.android.test.runner.listener.InstrumentationResultPrinter;

import org.junit.internal.TextListener;
//Synthetic comment -- @@ -112,6 +113,7 @@
private static final String ARGUMENT_LOG_ONLY = "log";
private static final String ARGUMENT_ANNOTATION = "annotation";
private static final String ARGUMENT_NOT_ANNOTATION = "notAnnotation";

private static final String LOG_TAG = "AndroidJUnitRunner";

//Synthetic comment -- @@ -174,6 +176,7 @@

testRunner.addListener(new TextListener(writer));
testRunner.addListener(new InstrumentationResultPrinter(this));

TestRequest testRequest = buildRequest(getArguments(), writer);
Result result = testRunner.run(testRequest.getRequest());
//Synthetic comment -- @@ -264,4 +267,20 @@
testRequestBuilder.addTestClass(testClassName);
}
}
}








//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/listener/DelayInjector.java b/androidtestlib/src/com/android/test/runner/listener/DelayInjector.java
new file mode 100644
//Synthetic comment -- index 0000000..b092029

//Synthetic comment -- @@ -0,0 +1,56 @@







