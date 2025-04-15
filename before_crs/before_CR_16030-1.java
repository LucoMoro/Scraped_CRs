/*Fix SDK build for eclipse tests.

Broken by Change:If04c5f8b10eeaca494a155ed6c4a25bf0d9d892cChange-Id:I8f60da7d537af68df47119b636eaf03c49b51146*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index 3f40230..3484252 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;

import java.io.IOException;

/**
* Supports Eclipse JUnit execution of Android tests.
//Synthetic comment -- @@ -205,9 +206,9 @@
}

/* (non-Javadoc)
         * @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long)
*/
        public void testRunEnded(long elapsedTime) {
notifyTestRunEnded(elapsedTime);
AdtPlugin.printToConsole(mLaunchInfo.getProject(),
LaunchMessages.RemoteAdtTestRunner_RunCompleteMsg);
//Synthetic comment -- @@ -272,4 +273,4 @@
message);
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/TestCollector.java
//Synthetic comment -- index b64ede3..da4abda 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
private String mErrorMessage = null;

TestCollector() {
        mTotalTestCount = 0; 
mTestTree = new HashMap<String, TestSuiteReference>();
}

//Synthetic comment -- @@ -56,9 +56,9 @@
}

/* (non-Javadoc)
     * @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long)
*/
    public void testRunEnded(long elapsedTime) {
// ignore
}

//Synthetic comment -- @@ -104,8 +104,8 @@
}

/**
     * Sends info about the test tree to be executed (ie the suites and their enclosed tests) 
     * 
* @param notified the {@link IVisitsTestTrees} to send test data to
*/
public void sendTrees(IVisitsTestTrees notified) {
//Synthetic comment -- @@ -115,7 +115,7 @@
}

/**
     * Returns the error message that was reported when collecting test info. 
* Returns <code>null</code> if no error occurred.
*/
public String getErrorMessage() {







