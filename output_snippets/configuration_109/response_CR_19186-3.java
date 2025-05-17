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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

private static final Logger LOGGER = Logger.getLogger("ResourceLogger");
private final ReentrantLock lock = new ReentrantLock();

/**
 * @throws SealedApkException if the APK is already sealed.
 * @throws DuplicateFileException if a file conflicts with another already added to the APK
 *                                   at the same location inside the APK archive.
 */
private void writeStandardProjectResources(ApkBuilder apkBuilder,
        IJavaProject javaProject, IWorkspaceRoot wsRoot, List<String> ignoreList)
        throws DuplicateFileException, ApkCreationException, SealedApkException {
    
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            if (!isIgnored(sourceResource.getLocation().toOSString(), ignoreList)) {
                try {
                    lock.lock();
                    apkBuilder.addSourceFolder(sourceResource.getLocation().toFile());
                } catch (Exception e) {
                    LOGGER.severe("Error adding source folder: " + sourceResource.getLocation().toOSString() + " - " + e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}

private boolean isIgnored(String resourcePath, List<String> ignoreList) {
    for (String ignore : ignoreList) {
        if (resourcePath.contains(ignore)) {
            return true;
        }
    }
    return false;
}

/**
 * Returns an array of external jar files used by the project.
 * @return an array of OS-specific absolute file paths

//<End of snippet n. 0>