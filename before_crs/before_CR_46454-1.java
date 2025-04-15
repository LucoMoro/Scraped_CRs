/*monitor: Display message when plat-tools is missing.

Change-Id:I1feb9abf01b635ca9e5de07aa0a269c773814640*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java
//Synthetic comment -- index 9c66205..ef8f186 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -63,6 +64,13 @@
ILogger sdkLog = NullLogger.getLogger();
SdkManager manager = SdkManager.createManager(sdkPath, sdkLog);
if (manager.getPlatformToolsVersion() == null) {
AdtUpdateDialog window = new AdtUpdateDialog(new Shell(display), sdkLog, sdkPath);
window.installPlatformTools();
}







