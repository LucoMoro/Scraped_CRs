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

public class WelcomeTasks {
    private DdmsPreferenceStore mStore;

    public boolean performStartupTasks() {
        String osSdkPath = mStore.getLastSdkPath();
        if (osSdkPath != null && osSdkPath.length() > 0 && new File(osSdkPath).isDirectory()) {
            boolean ok = AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
                    new CheckSdkErrorHandler() {
                @Override
                public boolean handleError(CheckSdkErrorHandler.Solution solution, String message) {
                    return false;
                }

                @Override
                public boolean handleWarning(CheckSdkErrorHandler.Solution solution, String message) {
                    return true;
                }
            });
            if (ok) {
                AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
                return false;
            }
        }

        String registrySdkPath = retrieveSdkPathFromRegistry();
        if (registrySdkPath != null && registrySdkPath.length() > 0 && new File(registrySdkPath).isDirectory()) {
            boolean ok = AdtPlugin.getDefault().checkSdkLocationAndId(registrySdkPath,
                    new CheckSdkErrorHandler() {
                @Override
                public boolean handleError(CheckSdkErrorHandler.Solution solution, String message) {
                    return false;
                }

                @Override
                public boolean handleWarning(CheckSdkErrorHandler.Solution solution, String message) {
                    return true;
                }
            });
            if (ok) {
                AdtPrefs.getPrefs().setSdkLocation(new File(registrySdkPath));
                return false;
            }
        }

        return !mStore.isAdtUsed();
    }

    private String retrieveSdkPathFromRegistry() {
        try {
            Preferences prefs = Preferences.userRoot().node("Software\\Android\\SDK"); 
            String sdkPath = prefs.get("Path", null);
            return sdkPath;
        } catch (Exception e) {
            return null;
        }
    }

    private void initializeWindowCoordinator() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                // Implementation for window coordinator initialization
            }
        });
    }
}

//<End of snippet n. 0>