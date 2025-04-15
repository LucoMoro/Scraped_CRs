/*ndk: Add support for debugging on mips target

Change-Id:I47cb9636192005d91166a6949c8b40ef88d0905f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NativeAbi.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NativeAbi.java
//Synthetic comment -- index 4bf1ff1..dcefde1 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

public enum NativeAbi {
armeabi("armeabi"),
    armeabi_v7a("armeabi-v7a"),
    mips("mips"),
x86("x86");

private final String mAbi;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java
//Synthetic comment -- index cdde5ae..6fb2f80 100644

//Synthetic comment -- @@ -28,30 +28,32 @@

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("restriction")
public class NdkHelper {
    private static final String MAKE = "make";                                      //$NON-NLS-1$
    private static final String CORE_MAKEFILE_PATH = "/build/core/build-local.mk";  //$NON-NLS-1$

/**
* Obtain the ABI's the application is compatible with.
* The ABI's are obtained by reading the result of the following command:
* make --no-print-dir -f ${NdkRoot}/build/core/build-local.mk -C <project-root> DUMP_APP_ABI
*/
    public static Collection<NativeAbi> getApplicationAbis(IProject project,
                                                                IProgressMonitor monitor) {
ICommandLauncher launcher = new CommandLauncher();
launcher.setProject(project);
String[] args = new String[] {
            "--no-print-dir",                                           //$NON-NLS-1$
            "-f",                                                       //$NON-NLS-1$
NdkManager.getNdkLocation() + CORE_MAKEFILE_PATH,
            "-C",                                                       //$NON-NLS-1$
project.getLocation().toOSString(),
            "DUMP_APP_ABI",                                             //$NON-NLS-1$
};
try {
launcher.execute(getPathToMake(), args, null, project.getLocation(), monitor);
//Synthetic comment -- @@ -65,13 +67,16 @@
launcher.waitAndRead(stdout, stderr, monitor);

String abis = stdout.toString().trim();
        Set<NativeAbi> nativeAbis = EnumSet.noneOf(NativeAbi.class);
        for (String abi: abis.split(" ")) {                             //$NON-NLS-1$
            if (abi.equals("all")) {                                    //$NON-NLS-1$
                return EnumSet.allOf(NativeAbi.class);
            }

try {
nativeAbis.add(NativeAbi.valueOf(abi));
} catch (IllegalArgumentException e) {
                AdtPlugin.printErrorToConsole(project, "Unknown Application ABI: ", abi);
}
}

//Synthetic comment -- @@ -90,12 +95,12 @@
ICommandLauncher launcher = new CommandLauncher();
launcher.setProject(project);
String[] args = new String[] {
            "--no-print-dir",                                           //$NON-NLS-1$
            "-f",                                                       //$NON-NLS-1$
NdkManager.getNdkLocation() + CORE_MAKEFILE_PATH,
            "-C",                                                       //$NON-NLS-1$
project.getLocation().toOSString(),
            "DUMP_TOOLCHAIN_PREFIX",                                    //$NON-NLS-1$
};
try {
launcher.execute(getPathToMake(), args, null, project.getLocation(), monitor);
//Synthetic comment -- @@ -120,7 +125,7 @@
*/
private static synchronized IPath getUtilitiesFolder() {
IPath ndkRoot = new Path(NdkManager.getNdkLocation());
        IPath prebuilt = ndkRoot.append("prebuilt");                      //$NON-NLS-1$
if (!prebuilt.toFile().exists() || !prebuilt.toFile().canRead()) {
return ndkRoot;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java
//Synthetic comment -- index 7070c08..36af976 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
import com.google.common.base.Joiner;

import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.debug.core.CDebugUtils;
//Synthetic comment -- @@ -62,6 +63,7 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
//Synthetic comment -- @@ -69,6 +71,8 @@

@SuppressWarnings("restriction")
public class NdkGdbLaunchDelegate extends GdbLaunchDelegate {
    private static final Joiner JOINER = Joiner.on(", ").skipNulls();

private static final String DEBUG_SOCKET = "debugsock";         //$NON-NLS-1$

@Override
//Synthetic comment -- @@ -120,7 +124,7 @@

// Get ABI's supported by the application
monitor.setTaskName(Messages.NdkGdbLaunchDelegate_Action_ObtainAppAbis);
        Collection<NativeAbi> appAbis = NdkHelper.getApplicationAbis(project, monitor);
if (appAbis.size() == 0) {
AdtPlugin.printErrorToConsole(project,
Messages.NdkGdbLaunchDelegate_LaunchError_UnableToDetectAppAbi);
//Synthetic comment -- @@ -175,6 +179,12 @@
if (compatAbi == null) {
AdtPlugin.printErrorToConsole(project,
Messages.NdkGdbLaunchDelegate_LaunchError_NoCompatibleAbi);
            AdtPlugin.printErrorToConsole(project,
                    String.format("ABI's supported by the application: %s", JOINER.join(appAbis)));
            AdtPlugin.printErrorToConsole(project,
                    String.format("ABI's supported by the device: %s, %s",      //$NON-NLS-1$
                            deviceAbi1,
                            deviceAbi2));
return false;
}

//Synthetic comment -- @@ -415,7 +425,7 @@
}

private NativeAbi getCompatibleAbi(String deviceAbi1, String deviceAbi2,
                                Collection<NativeAbi> appAbis) {
for (NativeAbi abi: appAbis) {
if (abi.toString().equals(deviceAbi1) || abi.toString().equals(deviceAbi2)) {
return abi;







