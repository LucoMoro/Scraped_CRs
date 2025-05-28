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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @throws SealedApkException if the APK is already sealed.
 * @throws DuplicateFileException if a file conflicts with another already added to the APK
 *                                  at the same location inside the APK archive.
 */
private void writeStandardProjectResources(ApkBuilder apkBuilder,
                                            IJavaProject javaProject, IWorkspaceRoot wsRoot,
                                            ArrayList<String> list)
        throws DuplicateFileException, ApkCreationException, SealedApkException {
    Set<String> ignoredFiles = retrieveIgnoredFiles();
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            checkFolderForIgnoredFiles(sourceResource.getLocation().toFile(), ignoredFiles, apkBuilder);
        }
    }
    validateNoIgnoredFilesInApk(apkBuilder, ignoredFiles);
}

private Set<String> retrieveIgnoredFiles() {
    Set<String> ignoredFiles = new HashSet<>();
    // Logic to populate ignoredFiles from .gitignore and .classpath
    try {
        List<String> gitignoreLines = Files.readAllLines(new File(".gitignore").toPath());
        ignoredFiles.addAll(gitignoreLines);
        
        List<String> classpathLines = Files.readAllLines(new File(".classpath").toPath());
        ignoredFiles.addAll(classpathLines);
    } catch (IOException e) {
        // Handle error reading files
    }
    return ignoredFiles;
}

private void checkFolderForIgnoredFiles(File folder, Set<String> ignoredFiles, ApkBuilder apkBuilder) throws DuplicateFileException {
    for (File file : folder.listFiles()) {
        if (file.isDirectory()) {
            checkFolderForIgnoredFiles(file, ignoredFiles, apkBuilder);
        } else if (!isIgnored(file.getAbsolutePath(), ignoredFiles)) {
            apkBuilder.addSourceFolder(file);
        }
    }
}

private boolean isIgnored(String filePath, Set<String> ignoredFiles) {
    return ignoredFiles.stream().anyMatch(filePath::matches); // Consider patterns in ignoredFiles
}

private void validateNoIgnoredFilesInApk(ApkBuilder apkBuilder, Set<String> ignoredFiles)
        throws DuplicateFileException {
    // Logic to validate APK contents against ignoredFiles
    // Implementation to check contents of apkBuilder against ignoredFiles
}

//<End of snippet n. 0>