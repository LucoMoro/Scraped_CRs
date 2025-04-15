/*Attempt to fix intermittent ADT test run failures.

Change the ADT test run steps to be more deterministic:
1) Start the JDT test run listener server side connection before the client
side (this is invokked via the launch.addProcess call)
2) Don't fork off a separate thread to run the tests. This was always
redundant and inefficient anyway, since the AndroidLaunchController already
forks off a thread to perform the launch

Bug 7976

Change-Id:If7d24998d6ebe1a9b2200717be6ba66c078017f9*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchAction.java
//Synthetic comment -- index 2bde545..b398a05 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
//Synthetic comment -- @@ -33,6 +34,7 @@
import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

/**
* A launch action that executes a instrumentation test run on an Android device.
//Synthetic comment -- @@ -53,6 +55,8 @@
/**
* Launch a instrumentation test run on given Android device. 
* Reuses JDT JUnit launch delegate so results can be communicated back to JDT JUnit UI.
* 
* @see IAndroidLaunchAction#doLaunchAction(DelayedLaunchInfo, IDevice)
*/
//Synthetic comment -- @@ -137,7 +141,7 @@
}

/**
     * Provides a VM runner implementation which starts a thread implementation of a launch process
*/
private static class VMTestRunner implements IVMRunner {

//Synthetic comment -- @@ -156,15 +160,15 @@

TestRunnerProcess runnerProcess = 
new TestRunnerProcess(config, mJUnitInfo);
            runnerProcess.start();
launch.addProcess(runnerProcess);
}
}

/**
* Launch process that executes the tests.
*/
    private static class TestRunnerProcess extends Thread implements IProcess  {

private final VMRunnerConfiguration mRunConfig;
private final AndroidJUnitLaunchInfo mJUnitInfo;
//Synthetic comment -- @@ -239,7 +243,7 @@
* @see org.eclipse.debug.core.model.ITerminate#isTerminated()
*/
public boolean isTerminated() {
            return mIsTerminated || isInterrupted();
}

/**
//Synthetic comment -- @@ -254,10 +258,18 @@
} 

/**
         * Launches a test runner that will communicate results back to JDT JUnit UI
*/
        @Override
public void run() {
mTestRunner = new RemoteAdtTestRunner();
mTestRunner.runTests(mRunConfig.getProgramArguments(), mJUnitInfo);
}







