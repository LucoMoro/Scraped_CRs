/*Respect Eclipse classpath when adding files to APK

Iterating through the source folder may include files that the user has asked to be ignored.*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 088c9ef..be15850 100644

//Synthetic comment -- @@ -26,22 +26,24 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.build.ApkBuilder.JarStatus;
import com.android.sdklib.build.ApkBuilder.SigningInfo;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
//Synthetic comment -- @@ -752,10 +754,11 @@
* @throws SealedApkException if the APK is already sealed.
* @throws DuplicateFileException if a file conflicts with another already added to the APK
*                                   at the same location inside the APK archive.
*/
private void writeStandardProjectResources(ApkBuilder apkBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws DuplicateFileException, ApkCreationException, SealedApkException {
// get the source pathes
ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

//Synthetic comment -- @@ -763,12 +766,41 @@
for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                // get a File from the IResource
                apkBuilder.addSourceFolder(sourceResource.getLocation().toFile());
}
}
}

/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths







