/*attempt to fix build

Change-Id:Ifddcbb2d6341d3fc51d99055919a413efe932c3f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/runtime/RemoteAdtTestRunner.java
//Synthetic comment -- index 3f40230..0ce03cc 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;

import java.io.IOException;

/**
* Supports Eclipse JUnit execution of Android tests.
//Synthetic comment -- @@ -207,7 +208,7 @@
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
//Synthetic comment -- index b64ede3..77664b0 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
/* (non-Javadoc)
* @see com.android.ddmlib.testrunner.ITestRunListener#testRunEnded(long)
*/
    public void testRunEnded(long elapsedTime) {
// ignore
}








