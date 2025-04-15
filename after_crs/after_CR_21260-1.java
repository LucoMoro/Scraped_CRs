/*New library project mechanism.

- When libraries build, create a jar file containing
  all the classes (except the R classes) and the java
  resources. This will be used by the main project.

Change-Id:Ib909efbb20e30c6eeb1619a4bf3c70eeeb5e2a5d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 545d0f9..b609dbc 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.sdklib.build.ApkBuilder.SigningInfo;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.IArchiveBuilder;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
//Synthetic comment -- @@ -126,6 +127,11 @@
mVerbose = verbose;
}

    public static void writeResources(IArchiveBuilder builder, IJavaProject javaProject)
            throws DuplicateFileException, ApkCreationException, SealedApkException, CoreException {
        writeStandardResources(builder, javaProject, null);
    }

/**
* Packages the resources of the projet into a .ap_ file.
* @param manifestFile the manifest of the project.
//Synthetic comment -- @@ -721,7 +727,7 @@
* Writes the standard resources of a project and its referenced projects
* into a {@link SignedJarBuilder}.
* Standard resources are non java/aidl files placed in the java package folders.
     * @param builder the archive builder.
* @param javaProject the javaProject object.
* @param referencedJavaProjects the java projects that this project references.
* @throws ApkCreationException if an error occurred
//Synthetic comment -- @@ -730,25 +736,24 @@
*                                   at the same location inside the APK archive.
* @throws CoreException
*/
    private static void writeStandardResources(IArchiveBuilder builder, IJavaProject javaProject,
List<IJavaProject> referencedJavaProjects)
throws DuplicateFileException, ApkCreationException, SealedApkException,
CoreException  {
IWorkspace ws = ResourcesPlugin.getWorkspace();
IWorkspaceRoot wsRoot = ws.getRoot();

        writeStandardProjectResources(builder, javaProject, wsRoot);

        if (referencedJavaProjects != null) {
            for (IJavaProject referencedJavaProject : referencedJavaProjects) {
                // only include output from non android referenced project
                // (This is to handle the case of reference Android projects in the context of
                // instrumentation projects that need to reference the projects to be tested).
                if (referencedJavaProject.getProject().hasNature(
                        AndroidConstants.NATURE_DEFAULT) == false) {
                    writeStandardProjectResources(builder, referencedJavaProject, wsRoot);
                }
}
}
}
//Synthetic comment -- @@ -759,15 +764,14 @@
* @param jarBuilder the {@link ApkBuilder}.
* @param javaProject the javaProject object.
* @param wsRoot the {@link IWorkspaceRoot}.
* @throws ApkCreationException if an error occurred
* @throws SealedApkException if the APK is already sealed.
* @throws DuplicateFileException if a file conflicts with another already added to the APK
*                                   at the same location inside the APK archive.
* @throws CoreException
*/
    private static void writeStandardProjectResources(IArchiveBuilder builder,
            IJavaProject javaProject, IWorkspaceRoot wsRoot)
throws DuplicateFileException, ApkCreationException, SealedApkException, CoreException {
// get the source pathes
List<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);
//Synthetic comment -- @@ -776,14 +780,14 @@
for (IPath sourcePath : sourceFolders) {
IResource sourceResource = wsRoot.findMember(sourcePath);
if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                writeFolderResources(builder, javaProject, (IFolder) sourceResource);
}
}
}

    private static void writeFolderResources(IArchiveBuilder builder,
            final IJavaProject javaProject, IFolder root) throws CoreException,
            ApkCreationException, SealedApkException, DuplicateFileException {
final List<IPath> pathsToPackage = new ArrayList<IPath>();
root.accept(new IResourceProxyVisitor() {
public boolean visit(IResourceProxy proxy) throws CoreException {
//Synthetic comment -- @@ -809,7 +813,7 @@
IPath rootLocation = root.getLocation();
for (IPath path : pathsToPackage) {
IPath archivePath = path.makeRelativeTo(rootLocation);
            builder.addFile(path.toFile(), archivePath.toString());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 1d3d13b..4574088 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.IArchiveBuilder;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IContainer;
//Synthetic comment -- @@ -58,9 +60,18 @@
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class PostCompilerBuilder extends BaseBuilder {

//Synthetic comment -- @@ -215,11 +226,14 @@
try {
// get the project info
ProjectState projectState = Sdk.getProjectState(project);

            // this can happen if the project has no default.properties.
            if (projectState == null) {
return null;
}

            boolean isLibrary = projectState.isLibrary();

// get the libraries
List<IProject> libProjects = projectState.getFullLibraryProjects();

//Synthetic comment -- @@ -244,6 +258,22 @@
// separator
IFolder outputFolder = BaseProjectHelper.getOutputFolder(project);

            // if the project is a library project, then create a jar from the compiled class
            if (isLibrary) {
                // In case it's not a library we check this after recording the result
                // of the resource delta visitor.
                if (outputFolder == null) {
                    // mark project and exit
                    markProject(AndroidConstants.MARKER_PACKAGING, Messages.Failed_To_Get_Output,
                            IMarker.SEVERITY_ERROR);
                    return allRefProjects;
                }

                writeLibraryPackage(project, outputFolder);

                return allRefProjects;
            }

// now we need to get the classpath list
List<IPath> sourceList = BaseProjectHelper.getSourceClasspaths(javaProject);

//Synthetic comment -- @@ -254,6 +284,7 @@
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Start_Full_Apk_Build);

                // Full build: we do all the steps.
mPackageResources = true;
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -264,6 +295,7 @@
// go through the resources and see if something changed.
IResourceDelta delta = getDelta(project);
if (delta == null) {
                    // no delta? Same as full build: we do all the steps.
mPackageResources = true;
mConvertToDex = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -624,6 +656,126 @@
return allRefProjects;
}

    private static class JarBuilder implements IArchiveBuilder {

        private static Pattern R_PATTERN = Pattern.compile("R(\\$.*)?\\.class"); //$NON-NLS-1$

        private final byte[] buffer = new byte[1024];
        private final JarOutputStream mOutputStream;

        JarBuilder(JarOutputStream outputStream) {
            mOutputStream = outputStream;
        }

        public void addFile(IFile file, IFolder rootFolder) throws ApkCreationException {
            // we only package class file from the output folder
            if (AndroidConstants.EXT_CLASS.equals(file.getFileExtension()) == false) {
                return;
            }

            // we don't package any R[$*] classes.
            String name = file.getName();
            if (R_PATTERN.matcher(name).matches()) {
                return;
            }

            IPath path = file.getFullPath().makeRelativeTo(rootFolder.getFullPath());
            try {
                addFile(file.getContents(), file.getLocalTimeStamp(), path.toString());
            } catch (ApkCreationException e) {
                throw e;
            } catch (Exception e) {
                throw new ApkCreationException(e, "Failed to add %s", file);
            }
        }

        public void addFile(File file, String archivePath) throws ApkCreationException,
                SealedApkException, DuplicateFileException {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                long lastModified = file.lastModified();
                addFile(inputStream, lastModified, archivePath);
            } catch (ApkCreationException e) {
                throw e;
            } catch (Exception e) {
                throw new ApkCreationException(e, "Failed to add %s", file);
            }
        }

        private void addFile(InputStream content, long lastModified, String archivePath)
                throws IOException, ApkCreationException {
            // create the jar entry
            JarEntry entry = new JarEntry(archivePath);
            entry.setTime(lastModified);

            try {
                // add the entry to the jar archive
                mOutputStream.putNextEntry(entry);

                // read the content of the entry from the input stream, and write it into the archive.
                int count;
                while ((count = content.read(buffer)) != -1) {
                    mOutputStream.write(buffer, 0, count);
                }
            } finally {
                try {
                    if (content != null) {
                        content.close();
                    }
                } catch (Exception e) {
                    throw new ApkCreationException(e, "Failed to close stream");
                }
            }
        }
    }

    private void writeLibraryPackage(IProject project, IFolder outputFolder) {
        IFile jarIFile = outputFolder.getFile(project.getName().toLowerCase() + ".jar");

        JarOutputStream jos = null;
        try {
            Manifest manifest = new Manifest();
            Attributes mainAttributes = manifest.getMainAttributes();
            mainAttributes.put(Attributes.Name.CLASS_PATH, "Android ADT");
            mainAttributes.putValue("Created-By", "1.0 (Android)");
            jos = new JarOutputStream(
                    new FileOutputStream(jarIFile.getLocation().toFile()), manifest);

            JarBuilder jarBuilder = new JarBuilder(jos);

            // write the class files
            writeClassFilesIntoJar(jarBuilder, outputFolder, outputFolder);

            // now write the standard Java resources
            BuildHelper.writeResources(jarBuilder, JavaCore.create(project));

        } catch (Exception e) {
            // do something.
            e.printStackTrace();
        } finally {
            if (jos != null) {
                try {
                    jos.close();
                } catch (IOException e) {
                    // pass
                }
            }
        }
    }

    private void writeClassFilesIntoJar(JarBuilder builder, IFolder folder, IFolder rootFolder)
            throws CoreException, IOException, ApkCreationException {
        IResource[] members = folder.members();
        for (IResource member : members) {
            if (member.getType() == IResource.FOLDER) {
                writeClassFilesIntoJar(builder, (IFolder) member, rootFolder);
            } else if (member.getType() == IResource.FILE) {
                IFile file = (IFile) member;
                builder.addFile(file, rootFolder);
            }
        }
    }

@Override
protected void startupOnInitialize() {
super.startupOnInitialize();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index fd3a07d..c101109 100644

//Synthetic comment -- @@ -490,7 +490,8 @@
// generate resources.
boolean compiledTheResources = mMustCompileResources;
if (mMustCompileResources) {
                handleResources(project, javaPackage, projectTarget, manifestFile, libProjects,
                        projectState.isLibrary());
saveProjectBooleanProperty(PROPERTY_COMPILE_RESOURCES , false);
}

//Synthetic comment -- @@ -570,11 +571,13 @@
* @param projectTarget the target of the main project
* @param manifest the {@link IFile} representing the project manifest
* @param libProjects the library dependencies
     * @param isLibrary if the project is a library project
* @throws CoreException
* @throws AbortBuildException
*/
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
            IFile manifest, List<IProject> libProjects, boolean isLibrary)
            throws CoreException, AbortBuildException {
// get the resource folder
IFolder resFolder = project.getFolder(AndroidConstants.WS_RESOURCES);

//Synthetic comment -- @@ -624,7 +627,7 @@
}

execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, null /* custom java package */, isLibrary);

final int count = libOutputFolders.size();
if (count > 0) {
//Synthetic comment -- @@ -632,7 +635,7 @@
IFolder libFolder = libOutputFolders.get(i);
String libJavaPackage = libJavaPackages.get(i);
execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                            libFolder, libResFolders, libJavaPackage, isLibrary);
}
}
}
//Synthetic comment -- @@ -653,11 +656,13 @@
* @param libResFolders the list of res folders for the library.
* @param customJavaPackage an optional javapackage to replace the main project java package.
* can be null.
     * @param isLibrary if the project is a library project
* @throws AbortBuildException
*/
private void execAapt(IProject project, IAndroidTarget projectTarget, String osOutputPath,
String osResPath, String osManifestPath, IFolder packageFolder,
            ArrayList<IFolder> libResFolders, String customJavaPackage, boolean isLibrary)
            throws AbortBuildException {
// We actually need to delete the manifest.java as it may become empty and
// in this case aapt doesn't generate an empty one, but instead doesn't
// touch it.
//Synthetic comment -- @@ -673,6 +678,10 @@
array.add("-v"); //$NON-NLS-1$
}

        if (isLibrary) {
            array.add("--non-constant-id"); //$NON-NLS-1$
        }

if (libResFolders.size() > 0) {
array.add("--auto-add-overlay"); //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index c066dff..3438f4b 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
* - Native libraries from the project or its library.
*
*/
public final class ApkBuilder implements IArchiveBuilder {

private final static Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
Pattern.CASE_INSENSITIVE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/IArchiveBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/IArchiveBuilder.java
new file mode 100644
//Synthetic comment -- index 0000000..e2230e9

//Synthetic comment -- @@ -0,0 +1,35 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.build;

import java.io.File;

public interface IArchiveBuilder {

    /**
     * Adds a file to the archive at a given path
     * @param file the file to add
     * @param archivePath the path of the file inside the APK archive.
     * @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
     * @throws DuplicateFileException if a file conflicts with another already added to the APK
     *                                   at the same location inside the APK archive.
     */
    void addFile(File file, String archivePath) throws ApkCreationException,
            SealedApkException, DuplicateFileException;

}







