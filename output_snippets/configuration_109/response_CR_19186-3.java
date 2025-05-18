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
import java.util.regex.Pattern;

/**
 * @throws SealedApkException if the APK is already sealed.
 * @throws DuplicateFileException if a file conflicts with another already added to the APK
 *                                  at the same location inside the APK archive.
 */
private void writeStandardProjectResources(ApkBuilder apkBuilder,
                                            IJavaProject javaProject, IWorkspaceRoot wsRoot,
                                            ArrayList<String> list)
        throws DuplicateFileException, ApkCreationException, SealedApkException {
    Set<Pattern> ignoredFiles = retrieveIgnoredFiles();
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            checkFolderForIgnoredFiles(sourceResource.getLocation().toFile(), ignoredFiles, apkBuilder);
        }
    }
    validateNoIgnoredFilesInApk(apkBuilder, ignoredFiles);
}

private Set<Pattern> retrieveIgnoredFiles() {
    Set<Pattern> ignoredFiles = new HashSet<>();
    try {
        List<String> gitignoreLines = Files.readAllLines(new File(".gitignore").toPath());
        for (String line : gitignoreLines) {
            ignoredFiles.add(Pattern.compile(wildcardToRegex(line.trim())));
        }

        List<String> classpathLines = Files.readAllLines(new File(".classpath").toPath());
        for (String line : classpathLines) {
            ignoredFiles.add(Pattern.compile(wildcardToRegex(line.trim())));
        }
    } catch (IOException e) {
        // Handle error reading files
        e.printStackTrace(); // Improved error logging
    }
    return ignoredFiles;
}

private String wildcardToRegex(String pattern) {
    return pattern.replace(".", "\\.")
                  .replace("*", ".*?")
                  .replace("?", ".?");
}

private void checkFolderForIgnoredFiles(File folder, Set<Pattern> ignoredFiles, ApkBuilder apkBuilder) throws DuplicateFileException {
    for (File file : folder.listFiles()) {
        if (file.isDirectory()) {
            checkFolderForIgnoredFiles(file, ignoredFiles, apkBuilder);
        } else if (!isIgnored(file.getAbsolutePath(), ignoredFiles)) {
            apkBuilder.addSourceFolder(file);
        }
    }
}

private boolean isIgnored(String filePath, Set<Pattern> ignoredFiles) {
    return ignoredFiles.stream().anyMatch(pattern -> pattern.matcher(filePath).matches());
}

private void validateNoIgnoredFilesInApk(ApkBuilder apkBuilder, Set<Pattern> ignoredFiles)
        throws DuplicateFileException {
    // Logic to validate APK contents against ignoredFiles
    // Assume apkBuilder has a method to retrieve its contents
    for (String apkFile : apkBuilder.getContents()) {
        if (isIgnored(apkFile, ignoredFiles)) {
            throw new DuplicateFileException("Ignored file found in APK: " + apkFile);
        }
    }
}

//<End of snippet n. 0>