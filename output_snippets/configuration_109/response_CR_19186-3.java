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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @throws SealedApkException if the APK is already sealed.
 * @throws DuplicateFileException if a file conflicts with another already added to the APK
 *                               at the same location inside the APK archive.
 */
private void writeStandardProjectResources(ApkBuilder apkBuilder,
                                            IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
        throws DuplicateFileException, ApkCreationException, SealedApkException {

    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);
    List<Pattern> exclusionPatterns = getExclusionPatternsFromClasspath();

    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            File srcFolder = sourceResource.getLocation().toFile();
            if (!isExcluded(srcFolder, exclusionPatterns)) {
                try {
                    addFilesFromFolder(apkBuilder, srcFolder, exclusionPatterns);
                } catch (DuplicateFileException e) {
                    logError(e, srcFolder);
                }
            } else {
                logExclusion(srcFolder);
            }
        }
    }
}

private void addFilesFromFolder(ApkBuilder apkBuilder, File folder, List<Pattern> exclusionPatterns) throws DuplicateFileException {
    File[] files = folder.listFiles();
    if (files != null) {
        for (File file : files) {
            if (!isExcluded(file, exclusionPatterns)) {
                if (file.isDirectory()) {
                    addFilesFromFolder(apkBuilder, file, exclusionPatterns);
                } else {
                    apkBuilder.addSourceFile(file);
                }
            } else {
                logExclusion(file);
            }
        }
    }
}

private List<Pattern> getExclusionPatternsFromClasspath() {
    List<Pattern> patterns = new ArrayList<>();
    // Logic to load patterns from .gitignore and .classpath with wildcard support
    try {
        loadExclusionPatternsFromFile(".gitignore", patterns);
        loadExclusionPatternsFromFile(".classpath", patterns);
    } catch (IOException e) {
        logError(e, null);
    }
    return patterns;
}

private void loadExclusionPatternsFromFile(String filename, List<Pattern> patterns) throws IOException {
    File file = new File(filename);
    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String regexPattern = convertToRegex(line);
                    patterns.add(Pattern.compile(regexPattern));
                }
            }
        }
    }
}

private String convertToRegex(String pattern) {
    return pattern.replace(".", "\\.")
                  .replace("*", ".*")
                  .replace("?", ".") + "$";
}

private boolean isExcluded(File file, List<Pattern> exclusionPatterns) {
    for (Pattern pattern : exclusionPatterns) {
        if (pattern.matcher(file.getPath()).matches()) {
            return true;
        }
    }
    return false;
}

private void logError(Exception e, File file) {
    String context = (file != null) ? "File: " + file.getPath() : "General error";
    System.err.println("Error processing: " + context + ". " + e.getMessage());
}

private void logExclusion(File file) {
    System.out.println("Excluded: " + file.getPath());
}

//<End of snippet n. 0>