//<Beginning of snippet n. 0>


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
import org.eclipse.core.runtime.IPath;

import java.io.File;
import java.util.ArrayList;

/**
 * @throws SealedApkException if the APK is already sealed.
 * @throws DuplicateFileException if a file conflicts with another already added to the APK
 *                                   at the same location inside the APK archive.
 */
private void writeStandardProjectResources(ApkBuilder apkBuilder,
                                            IJavaProject javaProject, IWorkspaceRoot wsRoot, 
                                            ArrayList<String> ignoredFiles) 
            throws DuplicateFileException, ApkCreationException, SealedApkException {
    // get the source paths
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            File sourceFolder = sourceResource.getLocation().toFile();
            if (!isIgnored(sourceFolder, ignoredFiles)) {
                apkBuilder.addSourceFolder(sourceFolder);
            }
        }
    }
}

private boolean isIgnored(File file, ArrayList<String> ignoredFiles) {
    String filePath = file.getAbsolutePath();
    for (String ignored : ignoredFiles) {
        if (filePath.contains(ignored)) {
            return true;
        }
    }
    return false;
}

//<End of snippet n. 0>