//<Beginning of snippet n. 0>

package com.android.ide.eclipse.adt.internal.welcome;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutWindowCoordinator;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdkstats.DdmsPreferenceStore;
import com.android.sdkstats.SdkStatsService;

import org.osgi.framework.Version;

import java.io.File;
import java.util.prefs.Preferences;

/**
* ADT startup tasks (other than those performed in {@link AdtPlugin#start(org.osgi.framework.BundleContext)}
return false;
}

        String osSdkPath = AdtPrefs.getPrefs().getSdkLocation();
        if (osSdkPath == null || osSdkPath.isEmpty() || !new File(osSdkPath).isDirectory()) {
            // Try to read from the registry
            osSdkPath = getSdkPathFromRegistry();
        }

        // Fallback to previous settings if not valid
        if (osSdkPath != null && osSdkPath.length() > 0 && new File(osSdkPath).isDirectory()) {
            boolean ok = AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
                    new CheckSdkErrorHandler() {
                @Override
                public boolean handleError(
                        CheckSdkErrorHandler.Solution solution,
                        String message) {
                    return false;
                }

                @Override
                public boolean handleWarning(
                        CheckSdkErrorHandler.Solution solution,
                        String message) {
                    return true;
                }
            });
            if (ok) {
                AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
                return false;
            }
        }

return !mStore.isAdtUsed();
}

private String getSdkPathFromRegistry() {
    String sdkPath = null;
    try {
        Preferences prefs = Preferences.systemRoot().node("SOFTWARE\\Android SDK Tools");
        sdkPath = prefs.get("Path", null);
        if (sdkPath != null && !sdkPath.isEmpty() && new File(sdkPath).isDirectory()) {
            return sdkPath;
        }
    } catch (Exception e) {
        // Log error
    }
    return null;
}

private void initializeWindowCoordinator() {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {

//<End of snippet n. 0>