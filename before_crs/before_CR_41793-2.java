/*Use org.eclipse.platform's version for ping/sdkstats.

Currently, we use the version of the ResourcePlugin as a way to
identify the version of Eclipse. However, both Eclipse 4.2 and 3.8
have the same 3.8 version of the resources plugin. The version of
org.eclipse.platform seems like it will resolve this issue.

Change-Id:Ib9325873b9b37b3b02b25b991cf13e469cedcfd0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 10a85b9..5e69e6f 100644

//Synthetic comment -- @@ -22,13 +22,13 @@
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutWindowCoordinator;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkstats.DdmsPreferenceStore;
import com.android.sdkstats.SdkStatsService;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
//Synthetic comment -- @@ -293,7 +293,7 @@

// Report the version of Eclipse to the stat server.
// Get the version of eclipse by getting the version of one of the runtime plugins.
        Version eclipseVersion = getVersion(ResourcesPlugin.getPlugin());
String eclipseVersionString = String.format("%1$d.%2$d",  //$NON-NLS-1$
eclipseVersion.getMajor(), eclipseVersion.getMinor());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java b/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java
//Synthetic comment -- index 49518a1..8c4a4a7 100644

//Synthetic comment -- @@ -18,9 +18,11 @@

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class InstallDetails {
private static final String ADT_PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$

/**
* Returns true if the ADT plugin is available in the current platform. This is useful
//Synthetic comment -- @@ -30,4 +32,10 @@
Bundle b = Platform.getBundle(ADT_PLUGIN_ID);
return b != null;
}
}







