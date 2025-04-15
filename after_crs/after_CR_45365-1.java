/*Start parsing SDK after the workbench is loaded.

This CL moves the initial parsing of the SDK to after
the workbench has been loaded. This allows early startup
code to specify the location of bundled SDK if necessary.

(cherry picked from commit a8b2efe8cda5dd75adea2dc29dfcfb11c27ef427)

Change-Id:Ie2ad8dc1088353953278b76f76ad3cbf817949c9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index e08d7ea..b11984e 100644

//Synthetic comment -- @@ -265,16 +265,6 @@

// Listen on resource file edits for updates to file inclusion
IncludeFinder.start();
}

/*
//Synthetic comment -- @@ -303,6 +293,16 @@

/** Called when the workbench has been started */
public void workbenchStarted() {
        // Parse the SDK content.
        // This is deferred in separate jobs to avoid blocking the bundle start.
        final boolean isSdkLocationValid = checkSdkLocationAndId();
        if (isSdkLocationValid) {
            // parse the SDK resources.
            // Wait 2 seconds before starting the job. This leaves some time to the
            // other bundles to initialize.
            parseSdkContent(2000 /*milliseconds*/);
        }

Display display = getDisplay();
mRed = new Color(display, 0xFF, 0x00, 0x00);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 8d8b688..aee0af3 100644

//Synthetic comment -- @@ -71,8 +71,11 @@

@Override
public void earlyStartup() {
        if (!isSdkSpecified()) {
            File bundledSdk = getBundledSdk();
            if (bundledSdk != null) {
                AdtPrefs.getPrefs().setSdkLocation(bundledSdk);
            }
}

if (isFirstTime()) {
//Synthetic comment -- @@ -88,30 +91,34 @@
AdtPlugin.getDefault().workbenchStarted();
}

    private boolean isSdkSpecified() {
String osSdkFolder = AdtPrefs.getPrefs().getOsSdkFolder();
        return (osSdkFolder != null && !osSdkFolder.isEmpty());
    }

    /**
     * Returns the path to the bundled SDK if this is part of the ADT package.
     * The ADT package has the following structure:
     *   root
     *      |--eclipse
     *      |--sdk
     * @return path to bundled SDK, null if no valid bundled SDK detected.
     */
    private File getBundledSdk() {
Location install = Platform.getInstallLocation();
if (install != null && install.getURL() != null) {
            File toolsFolder = new File(install.getURL().getFile()).getParentFile();
if (toolsFolder != null) {
                File sdkFolder = new File(toolsFolder, "sdk");
                if (sdkFolder.exists() && AdtPlugin.getDefault().checkSdkLocationAndId(
                        sdkFolder.getAbsolutePath(),
                        new SdkValidator())) {
                    return sdkFolder;
}
}
}

        return null;
}

private boolean isFirstTime() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java b/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java
//Synthetic comment -- index 71eccbf..8c4a4a7 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
public class InstallDetails {
private static final String ADT_PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$
private static final String ECLIPSE_PLATFORM_PLUGIN_ID = "org.eclipse.platform"; //$NON-NLS-1$

/**
* Returns true if the ADT plugin is available in the current platform. This is useful
//Synthetic comment -- @@ -39,10 +38,4 @@
Bundle b = Platform.getBundle(ECLIPSE_PLATFORM_PLUGIN_ID);
return b == null ? Version.emptyVersion : b.getVersion();
}
}







