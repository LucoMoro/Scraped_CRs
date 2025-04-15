/*ADT/Windows: figure the intial SDK Path from the registry.

On Windows, the installer sets the SDK path in the registry.
This adds code to the ADT initialization to use that.
The init process is, in that order:
- use the ADT prefs path,
- use the ddms.cfg "last sdk path",
- use the windows registry key.

Change-Id:Ib749a8e08e7d0d91402ba25ee765a37f889fb921*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 0fcf102..2fc755d 100644

//Synthetic comment -- @@ -16,10 +16,16 @@

package com.android.ide.eclipse.adt.internal.welcome;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtPlugin.CheckSdkErrorHandler;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutWindowCoordinator;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkstats.DdmsPreferenceStore;
import com.android.sdkstats.SdkStatsService;

//Synthetic comment -- @@ -38,6 +44,10 @@
import org.osgi.framework.Version;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* ADT startup tasks (other than those performed in {@link AdtPlugin#start(org.osgi.framework.BundleContext)}
//Synthetic comment -- @@ -80,34 +90,53 @@
return false;
}

        for (int i = 0; i < 2; i++) {
            String osSdkPath = null;

            if (i == 0) {
                // If we've recorded an SDK location in the .android settings, then the user
                // has run ADT before but possibly in a different workspace. We don't want to pop up
                // the welcome wizard each time if we can simply use the existing SDK install.
                osSdkPath = mStore.getLastSdkPath();
            } else if (i == 1) {
                osSdkPath = getSdkPathFromWindowsRegistry();
            }

            if (osSdkPath != null && osSdkPath.length() > 0) {
                boolean ok = new File(osSdkPath).isDirectory();

                if (!ok) {
                    osSdkPath = osSdkPath.trim();
                    ok = new File(osSdkPath).isDirectory();
}

                if (ok) {
                    // Verify that the SDK is valid
                    ok = AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
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
                        // Yes, we've seen an SDK location before and we can use it again,
                        // no need to pester the user with the welcome wizard.
                        // This also implies that the user has responded to the usage statistics
                        // question.
                        AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
                        return false;
                    }
}
}
}

//Synthetic comment -- @@ -115,6 +144,66 @@
return !mStore.isAdtUsed();
}

    private String getSdkPathFromWindowsRegistry() {
        if (SdkConstants.CURRENT_PLATFORM != SdkConstants.PLATFORM_WINDOWS) {
            return null;
        }

        final String valueName = "Path";                                               //$NON-NLS-1$
        final AtomicReference<String> result = new AtomicReference<String>();
        final Pattern regexp =
            Pattern.compile("^\\s+" + valueName + "\\s+REG_SZ\\s+(.*)$");//$NON-NLS-1$ //$NON-NLS-2$

        for (String key : new String[] {
                "HKLM\\Software\\Android SDK Tools",                                   //$NON-NLS-1$
                "HKLM\\Software\\Wow6432Node\\Android SDK Tools" }) {                  //$NON-NLS-1$

            String[] command = new String[] {
                "reg", "query", key, "/v", valueName       //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            };

            Process process;
            try {
                process = Runtime.getRuntime().exec(command);

                GrabProcessOutput.grabProcessOutput(
                        process,
                        Wait.WAIT_FOR_READERS,
                        new IProcessOutput() {
                            @Override
                            public void out(@Nullable String line) {
                                if (line != null) {
                                    Matcher m = regexp.matcher(line);
                                    if (m.matches()) {
                                        result.set(m.group(1));
                                    }
                                }
                            }

                            @Override
                            public void err(@Nullable String line) {
                                // ignore stderr
                            }
                        });
            } catch (IOException ignore) {
            } catch (InterruptedException ignore) {
            }

            String str = result.get();
            if (str != null) {
                if (new File(str).isDirectory()) {
                    return str;
                }
                str = str.trim();
                if (new File(str).isDirectory()) {
                    return str;
                }
            }
        }

        return null;
    }

private void initializeWindowCoordinator() {
final IWorkbench workbench = PlatformUI.getWorkbench();
workbench.getDisplay().asyncExec(new Runnable() {







