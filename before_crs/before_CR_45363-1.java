/*Start parsing SDK after the workbench is loaded.

This CL moves the initial parsing of the SDK to after
the workbench has been loaded. This allows early startup
code to specify the location of bundled SDK if necessary.

Change-Id:I21d98531dc6ddee0a615cbfc78c5aa470a6770ef*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index e08d7ea..60e2771 100644

//Synthetic comment -- @@ -265,16 +265,6 @@

// Listen on resource file edits for updates to file inclusion
IncludeFinder.start();

        // Parse the SDK content.
        // This is deferred in separate jobs to avoid blocking the bundle start.
        final boolean isSdkLocationValid = checkSdkLocationAndId();
        if (isSdkLocationValid) {
            // parse the SDK resources.
            // Wait 2 seconds before starting the job. This leaves some time to the
            // other bundles to initialize.
            parseSdkContent(2000 /*milliseconds*/);
        }
}

/*
//Synthetic comment -- @@ -303,6 +293,16 @@

/** Called when the workbench has been started */
public void workbenchStarted() {
Display display = getDisplay();
mRed = new Color(display, 0xFF, 0x00, 0x00);

//Synthetic comment -- @@ -1367,7 +1367,7 @@
/**
* Parses the SDK resources.
*/
    private void parseSdkContent(long delay) {
// Perform the update in a thread (here an Eclipse runtime job)
// since this should never block the caller (especially the start method)
Job job = new Job(Messages.AdtPlugin_Android_SDK_Content_Loader) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 8d8b688..aee0af3 100644

//Synthetic comment -- @@ -71,8 +71,11 @@

@Override
public void earlyStartup() {
        if (InstallDetails.isAndroidIdePackage()) {
            useBundledSdk();
}

if (isFirstTime()) {
//Synthetic comment -- @@ -88,30 +91,34 @@
AdtPlugin.getDefault().workbenchStarted();
}

    private void useBundledSdk() {
String osSdkFolder = AdtPrefs.getPrefs().getOsSdkFolder();

        // sdk path is already set
        if (osSdkFolder != null && osSdkFolder.length() > 0) {
            return;
        }

        // The Android IDE bundle is structured as follows:
        // root
        //    |--eclipse
        //    |--sdk
        // So use the SDK folder that is
Location install = Platform.getInstallLocation();
if (install != null && install.getURL() != null) {
            String toolsFolder = new File(install.getURL().getFile()).getParent();
if (toolsFolder != null) {
                String osSdkPath = toolsFolder + File.separator + "sdk";
                if (AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
                                new SdkValidator())) {
                    AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
}
}
}
}

private boolean isFirstTime() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java b/eclipse/plugins/com.android.ide.eclipse.base/src/com/android/ide/eclipse/base/InstallDetails.java
//Synthetic comment -- index 71eccbf..8c4a4a7 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
public class InstallDetails {
private static final String ADT_PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$
private static final String ECLIPSE_PLATFORM_PLUGIN_ID = "org.eclipse.platform"; //$NON-NLS-1$
    private static final String ADT_PRODUCT_PLUGIN_ID = "com.android.ide.eclipse.adt.package"; //$NON-NLS-1$

/**
* Returns true if the ADT plugin is available in the current platform. This is useful
//Synthetic comment -- @@ -39,10 +38,4 @@
Bundle b = Platform.getBundle(ECLIPSE_PLATFORM_PLUGIN_ID);
return b == null ? Version.emptyVersion : b.getVersion();
}

    /** Returns true if this is the "Eclipse for Android Developers" product. */
    public static boolean isAndroidIdePackage() {
        Bundle b = Platform.getBundle(ADT_PRODUCT_PLUGIN_ID);
        return b != null;
    }
}







