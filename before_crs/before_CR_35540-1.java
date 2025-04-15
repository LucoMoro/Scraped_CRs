/*monitor: ping back on startup

Just like DDMS, send a ping during startup.

Change-Id:Id4f1f3cf2daec772f6566faaeb75ad7f8fe0a239*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java
//Synthetic comment -- index 27ba6d7..a095a31 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.monitor.SdkToolsLocator.SdkInstallStatus;
import com.android.prefs.AndroidLocation;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
//Synthetic comment -- @@ -32,6 +33,10 @@
import org.eclipse.ui.PlatformUI;

import java.io.File;

public class MonitorApplication implements IApplication {
private static final String SDK_PATH_ENVVAR = "com.android.sdk.path";
//Synthetic comment -- @@ -41,10 +46,12 @@
public Object start(IApplicationContext context) throws Exception {
Display display = PlatformUI.createDisplay();

Location instanceLoc = Platform.getInstanceLocation();
IPath workspacePath = new Path(AndroidLocation.getFolder()).append(MONITOR_WORKSPACE_PATH);
instanceLoc.set(workspacePath.toFile().toURI().toURL(), true);

String sdkPath = findSdkPath(display);
if (!isValidSdkLocation(sdkPath)) {
// exit with return code -1
//Synthetic comment -- @@ -52,6 +59,14 @@
}
MonitorPlugin.getDefault().setSdkPath(sdkPath);

try {
int returnCode = PlatformUI.createAndRunWorkbench(display,
new MonitorWorkbenchAdvisor());
//Synthetic comment -- @@ -142,4 +157,37 @@
return null;
}
}
}







