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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
            IProgressMonitor monitor, IProject project, final AndroidLaunch androidLaunch,
            AndroidLaunchConfiguration config, AndroidLaunchController controller,
            IFile applicationPackage, ManifestData manifestData) {

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    executorService.submit(() -> {
        try {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            String runner = getRunner(project, configuration, manifestData);
            if (runner == null) {
                return;
            }

            String targetAppPackage = getTargetPackage(manifestData, runner);
            if (targetAppPackage == null) {
                // Ensure UI updates are executed on the UI thread
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

            controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                    manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                    junitLaunch, config, androidLaunch, monitor);
        } catch (Exception e) {
            // Enhanced error handling for better user feedback
            AdtPlugin.logError("Error during JUnit launch execution: " + e.getMessage(), e);
        }
    });

    executorService.shutdown(); // Adjusted to properly handle executor lifecycle
}

// Ensure UI thread checks to be conducted before executing UI related operations or in the method
private void executeOnUiThread(Runnable task) {
    if (PlatformUI.isWorkbenchRunning() && Display.getCurrent() != null) {
        task.run();  // Execute directly as we are already on UI thread
    } else {
        Display.getDefault().asyncExec(task); // Schedule for UI thread
    }
}

private String getRunnerFromConfig(ILaunchConfiguration configuration) throws CoreException {
    return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}

//<End of snippet n. 0>