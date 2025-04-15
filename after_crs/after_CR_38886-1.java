/*Junit Launch: Use non-display thread if needed.

Launching a single Junit method test directly from the Junit
results view fails because the launch was called directly from
the UI thread. This CL performs the launch in a separate thread
in such a case.

Change-Id:If8d5132b820a988bd42c9d67005b70d8a6098606*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index baa3f6b..f90bdda 100755

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ddmlib.testrunner.IRemoteAndroidTestRunner.TestSize;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunch;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
//Synthetic comment -- @@ -35,6 +35,9 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
//Synthetic comment -- @@ -42,6 +45,7 @@
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Display;

/**
* Run configuration that can execute JUnit tests on an Android platform.
//Synthetic comment -- @@ -63,9 +67,10 @@

@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
            final IProgressMonitor monitor, final IProject project,
            final AndroidLaunch androidLaunch, final AndroidLaunchConfiguration config,
            final AndroidLaunchController controller, final IFile applicationPackage,
            final ManifestData manifestData) {

String runner = getRunner(project, configuration, manifestData);
if (runner == null) {
//Synthetic comment -- @@ -76,7 +81,7 @@
return;
}
// get the target app's package
        final String targetAppPackage = getTargetPackage(manifestData, runner);
if (targetAppPackage == null) {
AdtPlugin.displayError(LaunchMessages.LaunchDialogTitle,
String.format(LaunchMessages.AndroidJUnitDelegate_NoTargetMsg_3s,
//Synthetic comment -- @@ -84,7 +89,7 @@
androidLaunch.stopLaunch();
return;
}
        final String testAppPackage = manifestData.getPackage();
AndroidJUnitLaunchInfo junitLaunchInfo = new AndroidJUnitLaunchInfo(project,
testAppPackage, runner);
junitLaunchInfo.setTestClass(getTestClass(configuration));
//Synthetic comment -- @@ -92,11 +97,26 @@
junitLaunchInfo.setTestMethod(getTestMethod(configuration));
junitLaunchInfo.setLaunch(androidLaunch);
junitLaunchInfo.setTestSize(getTestSize(configuration));
        final IAndroidLaunchAction junitLaunch = new AndroidJUnitLaunchAction(junitLaunchInfo);

        // launch on a separate thread if currently on the display thread
        if (Display.getCurrent() != null) {
            Job job = new Job("Junit Launch") {     //$NON-NLS-1$
                @Override
                protected IStatus run(IProgressMonitor m) {
                    controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                                    manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                                    junitLaunch, config, androidLaunch, monitor);
                    return Status.OK_STATUS;
                }
            };
            job.setPriority(Job.INTERACTIVE);
            job.schedule();
        } else {
            controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                    manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                    junitLaunch, config, androidLaunch, monitor);
        }
}

/**
//Synthetic comment -- @@ -226,7 +246,7 @@
return null;
}

    private String getRunnerFromConfig(ILaunchConfiguration configuration) {
return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}








