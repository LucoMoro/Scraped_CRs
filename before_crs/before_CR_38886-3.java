/*Junit Launch: Use non-display thread if needed.

Launching a single Junit method test directly from the Junit
results view fails because the launch was called directly from
the UI thread. This CL performs the launch in a separate thread
in such a case.

Fixeshttp://code.google.com/p/android/issues/detail?id=34170Change-Id:If8d5132b820a988bd42c9d67005b70d8a6098606*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index baa3f6b..55d3762 100755

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ddmlib.testrunner.IRemoteAndroidTestRunner.TestSize;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunch;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
//Synthetic comment -- @@ -35,6 +35,9 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
//Synthetic comment -- @@ -42,6 +45,7 @@
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

/**
* Run configuration that can execute JUnit tests on an Android platform.
//Synthetic comment -- @@ -63,9 +67,10 @@

@Override
protected void doLaunch(final ILaunchConfiguration configuration, final String mode,
            IProgressMonitor monitor, IProject project, final AndroidLaunch androidLaunch,
            AndroidLaunchConfiguration config, AndroidLaunchController controller,
            IFile applicationPackage, ManifestData manifestData) {

String runner = getRunner(project, configuration, manifestData);
if (runner == null) {
//Synthetic comment -- @@ -76,7 +81,7 @@
return;
}
// get the target app's package
        String targetAppPackage = getTargetPackage(manifestData, runner);
if (targetAppPackage == null) {
AdtPlugin.displayError(LaunchMessages.LaunchDialogTitle,
String.format(LaunchMessages.AndroidJUnitDelegate_NoTargetMsg_3s,
//Synthetic comment -- @@ -84,7 +89,7 @@
androidLaunch.stopLaunch();
return;
}
        String testAppPackage = manifestData.getPackage();
AndroidJUnitLaunchInfo junitLaunchInfo = new AndroidJUnitLaunchInfo(project,
testAppPackage, runner);
junitLaunchInfo.setTestClass(getTestClass(configuration));
//Synthetic comment -- @@ -92,11 +97,27 @@
junitLaunchInfo.setTestMethod(getTestMethod(configuration));
junitLaunchInfo.setLaunch(androidLaunch);
junitLaunchInfo.setTestSize(getTestSize(configuration));
        IAndroidLaunchAction junitLaunch = new AndroidJUnitLaunchAction(junitLaunchInfo);

        controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
                junitLaunch, config, androidLaunch, monitor);
}

/**
//Synthetic comment -- @@ -226,7 +247,7 @@
return null;
}

    private String getRunnerFromConfig(ILaunchConfiguration configuration) throws CoreException {
return getStringLaunchAttribute(ATTR_INSTR_NAME, configuration);
}








