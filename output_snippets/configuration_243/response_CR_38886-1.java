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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

private final ExecutorService executorService = Executors.newSingleThreadExecutor();

@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
            IProgressMonitor monitor, IProject project, final AndroidLaunch androidLaunch,
            AndroidLaunchConfiguration config, IFile applicationPackage, ManifestData manifestData) {
    
    if (configuration == null) {
        AdtPlugin.displayError("Error", "Launch configuration is null.");
        return;
    }
    if (monitor == null) {
        AdtPlugin.displayError("Error", "Progress monitor is null.");
        return;
    }
    if (androidLaunch == null) {
        AdtPlugin.displayError("Error", "Android launch is null.");
        return;
    }
    if (applicationPackage == null) {
        AdtPlugin.displayError("Error", "Application package is null.");
        return;
    }

    executorService.submit(() -> {
        String runner = getRunner(project, configuration, manifestData);
        if (runner == null) {
            AdtPlugin.displayError("Error", "Runner is null.");
            return;
        }
        
        String targetAppPackage = getTargetPackage(manifestData, runner);
        if (targetAppPackage == null) {
            Display.getDefault().asyncExec(() -> {
                AdtPlugin.displayError(LaunchMessages.LaunchDialogTitle,
                    String.format(LaunchMessages.AndroidJUnitDelegate_NoTargetMsg_3s));
                androidLaunch.stopLaunch();
            });
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

        synchronized (this) {
            controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                junitLaunch, config, androidLaunch, monitor);
        }
    });
}

private String getRunnerFromConfig(ILaunchConfiguration configuration) throws CoreException {
    return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}

@Override
protected void finalize() throws Throwable {
    super.finalize();
    executorService.shutdown();
}

//<End of snippet n. 0>