/*ndk: use custom ndk gdb configuration tab.

This patch removes CDT's debugger tab and uses a custom debug
tab which provides only the options relevant to ndk-gdb.

Change-Id:Ia5273334bc39e5f5908d2d6d0f7702fe4f0655ce*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkDebuggerConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkDebuggerConfigTab.java
deleted file mode 100644
//Synthetic comment -- index e45800d..0000000

//Synthetic comment -- @@ -1,63 +0,0 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ndk.internal.launch;

import com.android.ide.eclipse.ndk.internal.NdkVariables;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.IGDBLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.internal.ui.launching.RemoteApplicationCDebuggerTab;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("restriction")
public class NdkDebuggerConfigTab extends RemoteApplicationCDebuggerTab {
    public static final String DEFAULT_GDB_PORT = "5039";
    public static final String DEFAULT_GDB = getVar(NdkVariables.NDK_GDB);
    public static final String DEFAULT_PROGRAM =
            String.format("%1$s/obj/local/%2$s/app_process",
                    getVar(NdkVariables.NDK_PROJECT),
                    getVar(NdkVariables.NDK_COMPAT_ABI));
    public static final String DEFAULT_SOLIB_PATH =
            getVar(NdkVariables.NDK_PROJECT) + "/obj/local/armeabi/";


    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        super.setDefaults(config);

        config.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME, DEFAULT_GDB);
        config.setAttribute(IGDBLaunchConfigurationConstants.ATTR_GDB_INIT, ""); //$NON-NLS-1$
        config.setAttribute(IGDBLaunchConfigurationConstants.ATTR_PORT, DEFAULT_GDB_PORT);
        config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN, false);

        config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,
                IGDBLaunchConfigurationConstants.DEBUGGER_MODE_REMOTE_ATTACH);
        config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
                DEFAULT_PROGRAM);

        List<String> solibPaths = new ArrayList<String>(2);
        solibPaths.add(DEFAULT_SOLIB_PATH);
        config.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUGGER_SOLIB_PATH, solibPaths);
    }

    private static String getVar(String varName) {
        return "${" + varName + "}";
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkDebuggerTab.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkDebuggerTab.java
new file mode 100644
//Synthetic comment -- index 0000000..eff6275

//Synthetic comment -- @@ -0,0 +1,332 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchConfigTabGroups.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchConfigTabGroups.java
//Synthetic comment -- index 6bc6bec..de7912d 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
new NdkMainLaunchConfigTab(),
                new NdkDebuggerConfigTab(),
new SourceLookupTab(),
new CommonTab()
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java
//Synthetic comment -- index 5ac5859..7070c08 100644

//Synthetic comment -- @@ -294,7 +294,7 @@
// setup port forwarding between local port & remote (device) unix domain socket
monitor.setTaskName(Messages.NdkGdbLaunchDelegate_Action_SettingUpPortForward);
String localport = config.getAttribute(IGDBLaunchConfigurationConstants.ATTR_PORT,
                NdkDebuggerConfigTab.DEFAULT_GDB_PORT);
try {
device.createForward(Integer.parseInt(localport),
String.format("%s/%s", appDir, DEBUG_SOCKET), //$NON-NLS-1$
//Synthetic comment -- @@ -340,22 +340,19 @@
manager.addVariables(ndkVars);

// fix path to gdb
        String userGdbPath = wcopy.getAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
                NdkDebuggerConfigTab.DEFAULT_GDB);
wcopy.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
elaborateExpression(manager, userGdbPath));

        // fix program name
        String userProgramPath = wcopy.getAttribute(
                ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
                NdkDebuggerConfigTab.DEFAULT_PROGRAM);
wcopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
                elaborateExpression(manager, userProgramPath));

// fix solib paths
List<String> solibPaths = wcopy.getAttribute(
                IGDBLaunchConfigurationConstants.ATTR_DEBUGGER_SOLIB_PATH,
                Collections.singletonList(NdkDebuggerConfigTab.DEFAULT_SOLIB_PATH));
List<String> fixedSolibPaths = new ArrayList<String>(solibPaths.size());
for (String u : solibPaths) {
fixedSolibPaths.add(elaborateExpression(manager, u));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkLaunchConstants.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkLaunchConstants.java
new file mode 100644
//Synthetic comment -- index 0000000..b75e58a

//Synthetic comment -- @@ -0,0 +1,44 @@







