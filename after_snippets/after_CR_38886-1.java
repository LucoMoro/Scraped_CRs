
//<Beginning of snippet n. 0>


package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ddmlib.testrunner.IRemoteAndroidTestRunner.TestSize;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunch;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Display;

/**
* Run configuration that can execute JUnit tests on an Android platform.

@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
            final IProgressMonitor monitor, final IProject project,
            final AndroidLaunch androidLaunch, final AndroidLaunchConfiguration config,
            final AndroidLaunchController controller, final IFile applicationPackage,
            final ManifestData manifestData) {

String runner = getRunner(project, configuration, manifestData);
if (runner == null) {
return;
}
// get the target app's package
        final String targetAppPackage = getTargetPackage(manifestData, runner);
if (targetAppPackage == null) {
AdtPlugin.displayError(LaunchMessages.LaunchDialogTitle,
String.format(LaunchMessages.AndroidJUnitDelegate_NoTargetMsg_3s,
androidLaunch.stopLaunch();
return;
}
        final String testAppPackage = manifestData.getPackage();
AndroidJUnitLaunchInfo junitLaunchInfo = new AndroidJUnitLaunchInfo(project,
testAppPackage, runner);
junitLaunchInfo.setTestClass(getTestClass(configuration));
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
return null;
}

    private String getRunnerFromConfig(ILaunchConfiguration configuration) {
return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}


//<End of snippet n. 0>








