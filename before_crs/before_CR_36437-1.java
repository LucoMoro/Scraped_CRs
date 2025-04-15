/*Clear lint markers as part of a clean build

Change-Id:I6b3c1c7416a4babfa44b83741f697ad3deed4435*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index c213005..c4e9cae 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -641,6 +642,9 @@
removeMarkersFromContainer(project, AdtConstants.MARKER_AIDL);
removeMarkersFromContainer(project, AdtConstants.MARKER_RENDERSCRIPT);
removeMarkersFromContainer(project, AdtConstants.MARKER_ANDROID);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 9424bf0..68b3407 100644

//Synthetic comment -- @@ -266,8 +266,12 @@
return null;
}

    /** Clears any lint markers from the given resource (project, folder or file) */
    static void clearMarkers(IResource resource) {
clearMarkers(Collections.singletonList(resource));
}








