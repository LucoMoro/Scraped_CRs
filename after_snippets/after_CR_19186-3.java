
//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkBuilder.JarStatus;
import com.android.sdklib.build.ApkBuilder.SigningInfo;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.build.SignedJarBuilder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
* @throws SealedApkException if the APK is already sealed.
* @throws DuplicateFileException if a file conflicts with another already added to the APK
*                                   at the same location inside the APK archive.
     * @throws CoreException
*/
private void writeStandardProjectResources(ApkBuilder apkBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws DuplicateFileException, ApkCreationException, SealedApkException, CoreException {
// get the source pathes
ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                writeFolderResources(apkBuilder, javaProject, (IFolder) sourceResource);
}
}
}

    private void writeFolderResources(ApkBuilder apkBuilder, final IJavaProject javaProject,
            IFolder root) throws CoreException, ApkCreationException,
            SealedApkException, DuplicateFileException {
        final List<IPath> pathsToPackage = new ArrayList<IPath>();
        root.accept(new IResourceProxyVisitor() {
            public boolean visit(IResourceProxy proxy) throws CoreException {
                if (proxy.getType() != IResource.FILE) {
                    return true;
                }
                if (!ApkBuilder.checkFileForPackaging(proxy.getName())) {
                    return true;
                }
                IResource res = proxy.requestResource();
                if (!javaProject.isOnClasspath(res)) {
                    return true;
                }
                // Just record that we need to package this.  Packaging here throws
                // inappropriate checked exceptions.
                IPath location = res.getLocation();
                pathsToPackage.add(location);
                return true;
            }
        }, 0);
        IPath rootLocation = root.getLocation();
        for (IPath path : pathsToPackage) {
            IPath archivePath = path.makeRelativeTo(rootLocation);
            apkBuilder.addFile(path.toFile(), archivePath.toString());
        }
    }

/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths

//<End of snippet n. 0>








