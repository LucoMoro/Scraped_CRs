/*Don't warn about missing SDK when bundled

Change-Id:I88d76329ab5f3afa4fda338c170a78bef1939165*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index e08d7ea..af6f20a 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.internal.ui.EclipseUiHelper;
import com.android.ide.eclipse.base.InstallDetails;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.io.StreamException;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -266,14 +267,16 @@
// Listen on resource file edits for updates to file inclusion
IncludeFinder.start();

        if (!InstallDetails.isAndroidIdePackage()) {
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
}

//Synthetic comment -- @@ -1366,8 +1369,10 @@

/**
* Parses the SDK resources.
     *
     * @param delay the delay to wait before starting the parsing job
*/
    public void parseSdkContent(long delay) {
// Perform the update in a thread (here an Eclipse runtime job)
// since this should never block the caller (especially the start method)
Job job = new Job(Messages.AdtPlugin_Android_SDK_Content_Loader) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 8d8b688..206c4f5 100644

//Synthetic comment -- @@ -109,6 +109,10 @@
if (AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
new SdkValidator())) {
AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
                    // parse the SDK resources.
                    // Wait 2 seconds before starting the job. This leaves some time to the
                    // other bundles to initialize.
                    AdtPlugin.getDefault().parseSdkContent(2000 /*milliseconds*/);
}
}
}







