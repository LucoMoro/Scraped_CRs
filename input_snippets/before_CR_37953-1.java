
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

/**
* ADT startup tasks (other than those performed in {@link AdtPlugin#start(org.osgi.framework.BundleContext)}
return false;
}

        // If we've recorded an SDK location in the .android settings, then the user
        // has run ADT before but possibly in a different workspace. We don't want to pop up
        // the welcome wizard each time if we can simply use the existing SDK install.
        String osSdkPath = mStore.getLastSdkPath();
        if (osSdkPath != null && osSdkPath.length() > 0 && new File(osSdkPath).isDirectory()) {
            // Verify that the SDK is valid
            boolean ok = AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
                    new AdtPlugin.CheckSdkErrorHandler() {
                @Override
                public boolean handleError(
                        CheckSdkErrorHandler.Solution solution,
                        String message) {
                    return false;
}

                @Override
                public boolean handleWarning(
                        CheckSdkErrorHandler.Solution  solution,
                        String message) {
                    return true;
}
            });
            if (ok) {
                // Yes, we've seen an SDK location before and we can use it again, no need to
                // pester the user with the welcome wizard. (This also implies that the user
                // has responded to the usage statistics question.)
                AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
                return false;
}
}

return !mStore.isAdtUsed();
}

private void initializeWindowCoordinator() {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {

//<End of snippet n. 0>








