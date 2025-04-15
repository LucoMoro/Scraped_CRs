/*Revert "Don't warn about missing SDK when bundled"

This reverts commit df23dac52a3af6e7e4dd485072a125ab3794d9ac.*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index af6f20a..e08d7ea 100644

//Synthetic comment -- @@ -46,7 +46,6 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.internal.ui.EclipseUiHelper;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.io.StreamException;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -267,16 +266,14 @@
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

//Synthetic comment -- @@ -1369,10 +1366,8 @@

/**
* Parses the SDK resources.
*/
    private void parseSdkContent(long delay) {
// Perform the update in a thread (here an Eclipse runtime job)
// since this should never block the caller (especially the start method)
Job job = new Job(Messages.AdtPlugin_Android_SDK_Content_Loader) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index 206c4f5..8d8b688 100644

//Synthetic comment -- @@ -109,10 +109,6 @@
if (AdtPlugin.getDefault().checkSdkLocationAndId(osSdkPath,
new SdkValidator())) {
AdtPrefs.getPrefs().setSdkLocation(new File(osSdkPath));
}
}
}







