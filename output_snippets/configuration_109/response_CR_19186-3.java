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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

private void writeStandardProjectResources(ApkBuilder apkBuilder,
IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list,
List<String> ignorePatterns) throws DuplicateFileException, ApkCreationException, SealedApkException {
    ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);
    
    for (IPath sourcePath : sourceFolders) {
        IResource sourceResource = wsRoot.findMember(sourcePath);
        if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
            File folder = sourceResource.getLocation().toFile();
            for (File file : folder.listFiles()) {
                if (!shouldIgnore(file, ignorePatterns) && isSourceFile(file)) {
                    apkBuilder.addSourceFolder(file);
                } else {
                    System.out.println("Skipping file/folder due to ignore settings: " + file.getPath());
                }
            }
        }
    }
}

private boolean shouldIgnore(File file, List<String> ignorePatterns) {
    for (String pattern : ignorePatterns) {
        if (file.getPath().contains(pattern)) {
            return true;
        }
    }
    return false;
}

private boolean isSourceFile(File file) {
    return file.isFile() && !file.getName().startsWith(".") && !file.getName().endsWith(".class");
}

//<End of snippet n. 0>
