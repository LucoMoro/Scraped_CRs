/*Fix ADT build: update ManifestMerger constructors.

Change-Id:Ibd7a91a584644e71a8c40a812e3971d9dbe75cfa*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index cbce3c9..18459d2 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.StreamException;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
//Synthetic comment -- @@ -830,7 +831,9 @@
} else {
final ArrayList<String> errors = new ArrayList<String>();

            // TODO change MergerLog.wrapSdkLog by a custom IMergerLog that will create
            // and maintain error markers.
            ManifestMerger merger = new ManifestMerger(MergerLog.wrapSdkLog(new ISdkLog() {

@Override
public void warning(String warningFormat, Object... args) {
//Synthetic comment -- @@ -846,7 +849,7 @@
public void error(Throwable t, String errorFormat, Object... args) {
errors.add(String.format(errorFormat, args));
}
            }));

File[] libManifests = new File[libProjects.size()];
int libIndex = 0;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 760084d..f22a742 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.resources.ResourceFolderType;
import com.android.sdklib.SdkConstants;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -748,7 +749,9 @@

/** Merges the given manifest fragment into the given manifest file */
private boolean mergeManifest(Document currentManifest, Document fragment) {
        // TODO change MergerLog.wrapSdkLog by a custom IMergerLog that will create
        // and maintain error markers.
        ManifestMerger merger = new ManifestMerger(MergerLog.wrapSdkLog(AdtPlugin.getDefault()));
return currentManifest != null && fragment != null
&& merger.process(currentManifest, fragment);
}







