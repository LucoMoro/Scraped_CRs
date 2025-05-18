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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Welcome {

    private DdmsPreferenceStore mStore;
    private static final Logger LOGGER = Logger.getLogger(Welcome.class.getName());

    public boolean initialize() {
        String sdkPath = AdtPrefs.getPrefs().getSdkLocation();

        // First: Check ADT prefs path
        if (isValidSdkPath(sdkPath)) {
            if (validateSdk(sdkPath)) {
                return true;
            }
        }

        // Second: Check last SDK path
        String osSdkPath = mStore.getLastSdkPath();
        if (isValidSdkPath(osSdkPath)) {
            if (validateSdk(osSdkPath)) {
                return true;
            }
        }

        // Third: Check SDK path from Windows registry
        String registrySdkPath = getSdkPathFromRegistry();
        if (isValidSdkPath(registrySdkPath)) {
            if (validateSdk(registrySdkPath)) {
                return true;
            }
        }

        return !mStore.isAdtUsed();
    }

    private boolean isValidSdkPath(String sdkPath) {
        return sdkPath != null && sdkPath.length() > 0 && new File(sdkPath).isDirectory();
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
            if (isValidSdkPath(sdkPath)) {
                return sdkPath;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error accessing SDK path from registry", e);
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