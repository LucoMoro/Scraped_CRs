/*monitor: ping back on startup

Just like DDMS, send a ping during startup.

Change-Id:Id4f1f3cf2daec772f6566faaeb75ad7f8fe0a239*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java
//Synthetic comment -- index 27ba6d7..a095a31 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.monitor.SdkToolsLocator.SdkInstallStatus;
import com.android.prefs.AndroidLocation;
import com.android.sdkstats.SdkStatsService;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
//Synthetic comment -- @@ -32,6 +33,10 @@
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MonitorApplication implements IApplication {
private static final String SDK_PATH_ENVVAR = "com.android.sdk.path";
//Synthetic comment -- @@ -41,10 +46,12 @@
public Object start(IApplicationContext context) throws Exception {
Display display = PlatformUI.createDisplay();

        // set workspace location
Location instanceLoc = Platform.getInstanceLocation();
IPath workspacePath = new Path(AndroidLocation.getFolder()).append(MONITOR_WORKSPACE_PATH);
instanceLoc.set(workspacePath.toFile().toURI().toURL(), true);

        // figure out path to SDK
String sdkPath = findSdkPath(display);
if (!isValidSdkLocation(sdkPath)) {
// exit with return code -1
//Synthetic comment -- @@ -52,6 +59,14 @@
}
MonitorPlugin.getDefault().setSdkPath(sdkPath);

        // ping back to servers
        // if this is the first time using ddms or adt, open up the stats service
        // opt out dialog, and request user for permissions.
        SdkStatsService stats = new SdkStatsService();
        stats.checkUserPermissionForPing(new Shell(display));
        ping(stats, new Path(sdkPath).append("tools").toOSString());

        // open up RCP
try {
int returnCode = PlatformUI.createAndRunWorkbench(display,
new MonitorWorkbenchAdvisor());
//Synthetic comment -- @@ -142,4 +157,37 @@
return null;
}
}

    private static void ping(SdkStatsService stats, String toolsLocation) {
        Properties p = new Properties();
        try{
            File sourceProp;
            if (toolsLocation != null && toolsLocation.length() > 0) {
                sourceProp = new File(toolsLocation, "source.properties"); //$NON-NLS-1$
            } else {
                sourceProp = new File("source.properties"); //$NON-NLS-1$
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(sourceProp);
                p.load(fis);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            String revision = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
            if (revision != null && revision.length() > 0) {
                stats.ping("ddms", revision);  //$NON-NLS-1$
            }
        } catch (FileNotFoundException e) {
            // couldn't find the file? don't ping.
        } catch (IOException e) {
            // couldn't find the file? don't ping.
        }
    }
}







