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
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IPath;

private void writeStandardProjectResources(ApkBuilder apkBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws DuplicateFileException, ApkCreationException, SealedApkException {
    
    // Load ignore patterns from a configuration file (e.g. .gitignore or user-defined)
    List<String> ignorePatterns = loadIgnorePatterns();
    
    // Check the classpath for exclusions
    List<IPath> classpathExclusions = readClassPathExclusions(javaProject);

    // Get the source paths
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);
    
    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            File folder = sourceResource.getLocation().toFile();
            // Check for ignore patterns and classpath exclusions
            if (!isExcluded(folder, ignorePatterns) && !classpathExclusions.contains(sourcePath)) {
                apkBuilder.addSourceFolder(folder);
            }
        }
    }
}

private boolean isExcluded(File file, List<String> ignorePatterns) {
    // Implement logic to check if file matches any of the ignore patterns
    for (String pattern : ignorePatterns) {
        // Simple matching check (could be more elaborate based on requirements)
        if (file.getName().matches(pattern)) {
            return true;
        }
    }
    return false;
}

private List<IPath> readClassPathExclusions(IJavaProject javaProject) {
    // Implement logic to read the project's .classpath file and extract exclusion paths
    List<IPath> exclusions = new ArrayList<>();
    // Example implementation will go here
    return exclusions;
}

//<End of snippet n. 0>