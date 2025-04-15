/*ADT/Windows: figure the intial SDK Path from the registry.

On Windows, the installer sets the SDK path in the registry.
This adds code to the ADT initialization to use that.
The init process is, in that order:
- use the ADT prefs path,
- use the ddms.cfg "last sdk path",
- use the windows registry key.

Change-Id:Ib749a8e08e7d0d91402ba25ee765a37f889fb921*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 0fcf102..5f1f29e 100644

//Synthetic comment -- @@ -16,10 +16,16 @@

package com.android.ide.eclipse.adt.internal.welcome;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutWindowCoordinator;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdkstats.DdmsPreferenceStore;
import com.android.sdkstats.SdkStatsService;

//Synthetic comment -- @@ -38,6 +44,10 @@
import org.osgi.framework.Version;

import java.io.File;

/**
* ADT startup tasks (other than those performed in {@link AdtPlugin#start(org.osgi.framework.BundleContext)}
//Synthetic comment -- @@ -80,34 +90,53 @@
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

//Synthetic comment -- @@ -115,6 +144,64 @@
return !mStore.isAdtUsed();
}

private void initializeWindowCoordinator() {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {







