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

    public boolean start() {
        // Check ADT prefs path
        String adtPrefsPath = AdtPrefs.getPrefs().getSdkLocation().getAbsolutePath();
        if (adtPrefsPath != null && new File(adtPrefsPath).isDirectory()) {
            AdtPrefs.getPrefs().setSdkLocation(new File(adtPrefsPath));
            return false;
        }

        // Last SDK path check
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

        // Registry lookup if both previous checks failed
        String registrySdkPath = getSdkPathFromRegistry();
        if (registrySdkPath != null && new File(registrySdkPath).isDirectory()) {
            AdtPrefs.getPrefs().setSdkLocation(new File(registrySdkPath));
            return false;
        }

        return !mStore.isAdtUsed();
    }

    private String getSdkPathFromRegistry() {
        try {
            Preferences prefs = Preferences.systemRoot().node("Software/MySDK");
            return prefs.get("SDKPath", null);
        } catch (Exception e) {
            // Handle registry access errors gracefully
            return null;
        }
    }

    private void initializeWindowCoordinator() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                // Initialization logic here
            }
        });
    }
}
//<End of snippet n. 0>