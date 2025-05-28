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

public class Welcome {

    private DdmsPreferenceStore mStore;

    public boolean initialize() {
        String sdkPath = AdtPrefs.getPrefs().getSdkLocation();

        // First: Check ADT prefs path
        if (sdkPath != null && sdkPath.length() > 0 && new File(sdkPath).isDirectory()) {
            if (validateSdk(sdkPath)) {
                return true;
            }
        }

        // Second: Check last SDK path
        String osSdkPath = mStore.getLastSdkPath();
        if (osSdkPath != null && osSdkPath.length() > 0 && new File(osSdkPath).isDirectory()) {
            if (validateSdk(osSdkPath)) {
                return true;
            }
        }

        // Third: Check SDK path from Windows registry
        String registrySdkPath = getSdkPathFromRegistry();
        if (registrySdkPath != null && registrySdkPath.length() > 0 && new File(registrySdkPath).isDirectory()) {
            if (validateSdk(registrySdkPath)) {
                return true;
            }
        }

        return !mStore.isAdtUsed();
    }

    private boolean validateSdk(String sdkPath) {
        boolean ok = AdtPlugin.getDefault().checkSdkLocationAndId(sdkPath,
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
            AdtPrefs.getPrefs().setSdkLocation(new File(sdkPath));
        }
        return ok;
    }

    private String getSdkPathFromRegistry() {
        String sdkPath = null;
        try {
            Preferences prefs = Preferences.systemRoot().node("HKEY_LOCAL_MACHINE\\Software\\Android SDK Tools");
            sdkPath = prefs.get("SdkPath", null);
            if (sdkPath != null && sdkPath.length() > 0 && new File(sdkPath).isDirectory()) {
                return sdkPath;
            }
        } catch (Exception e) {
            // Handle exception, log if necessary, but do not interrupt startup.
            e.printStackTrace(); // Example of logging the exception
        }
        return null;
    }

    private void initializeWindowCoordinator() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
        // Implementation continues...
        });
    }
}

//<End of snippet n. 0>