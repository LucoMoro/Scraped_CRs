//<Beginning of snippet n. 0>


package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ddmlib.testrunner.IRemoteAndroidTestRunner.TestSize;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunch;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Display;

/**
* Run configuration that can execute JUnit tests on an Android platform.
*/
@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
        IProgressMonitor monitor, IProject project, final AndroidLaunch androidLaunch,
        AndroidLaunchConfiguration config, AndroidLaunchController controller,
        IFile applicationPackage, ManifestData manifestData) {

    String runner = getRunner(project, configuration, manifestData);
    if (runner == null) {
        return;
    }
    
    // get the target app's package
    String targetAppPackage = getTargetPackage(manifestData, runner);
    if (targetAppPackage == null) {
        AdtPlugin.displayError(LaunchMessages.LaunchDialogTitle,
            String.format(LaunchMessages.AndroidJUnitDelegate_NoTargetMsg_3s));
        androidLaunch.stopLaunch();
        return;
    }

    String testAppPackage = manifestData.getPackage();
    AndroidJUnitLaunchInfo junitLaunchInfo = new AndroidJUnitLaunchInfo(project,
        testAppPackage, runner);
    junitLaunchInfo.setTestClass(getTestClass(configuration));
    junitLaunchInfo.setTestMethod(getTestMethod(configuration));
    junitLaunchInfo.setLaunch(androidLaunch);
    junitLaunchInfo.setTestSize(getTestSize(configuration));
    IAndroidLaunchAction junitLaunch = new AndroidJUnitLaunchAction(junitLaunchInfo);
    
    // Execute on a separate thread if on the UI thread
    if (Display.getCurrent() != null) {
        new Thread(() -> {
            try {
                controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                    manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                    junitLaunch, config, androidLaunch, monitor);
            } catch (Exception e) {
                AdtPlugin.log(e);
            } finally {
                Display.asyncExec(() -> {
                    // Update UI if necessary
                });
            }
        }).start();
    } else {
        try {
            controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                junitLaunch, config, androidLaunch, monitor);
        } catch (Exception e) {
            AdtPlugin.log(e);
        }
    }
}

/**
* return null;
*/

private String getRunnerFromConfig(ILaunchConfiguration configuration) throws CoreException {
    return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}

//<End of snippet n. 0>