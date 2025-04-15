/*Refactor most of the ApkBuilder into a separate helper class.

The Eclipse incremental builder will call the helper, as will
the multi-apk export code (that is not an Eclipse builder).

Change-Id:Ibac9aff4557232a3fa0033336c5d4d4327ae3883*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
new file mode 100644
//Synthetic comment -- index 0000000..3b1c413

//Synthetic comment -- @@ -0,0 +1,466 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilder.java
//Synthetic comment -- index 769fa44..c3983b0 100644

//Synthetic comment -- @@ -18,23 +18,12 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ApkInstallManager;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.jarutils.DebugKeyProvider;
import com.android.jarutils.JavaResourceFilter;
import com.android.jarutils.SignedJarBuilder;
import com.android.jarutils.DebugKeyProvider.IKeyGenOutput;
import com.android.jarutils.DebugKeyProvider.KeytoolException;
import com.android.jarutils.SignedJarBuilder.IZipEntryFilter;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.AndroidXPathFactory;
//Synthetic comment -- @@ -47,49 +36,32 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;

public class ApkBuilder extends BaseBuilder {

public static final String ID = "com.android.ide.eclipse.adt.ApkBuilder"; //$NON-NLS-1$

private static final String PROPERTY_CONVERT_TO_DEX = "convertToDex"; //$NON-NLS-1$
private static final String PROPERTY_PACKAGE_RESOURCES = "packageResources"; //$NON-NLS-1$
private static final String PROPERTY_BUILD_APK = "buildApk"; //$NON-NLS-1$

    private static final String DX_PREFIX = "Dx"; //$NON-NLS-1$

    final static String GDBSERVER_NAME = "gdbserver"; //$NON-NLS-1$

/**
* Dex conversion flag. This is set to true if one of the changed/added/removed
* file is a .class file. Upon visiting all the delta resource, if this
//Synthetic comment -- @@ -109,8 +81,8 @@
*/
private boolean mBuildFinalPackage = false;

    private PrintStream mOutStream = null;
    private PrintStream mErrStream = null;

/**
* Basic Resource Delta Visitor class to check if a referenced project had a change in its
//Synthetic comment -- @@ -164,7 +136,7 @@
if (type == IResource.FILE) {
// check if the file is a valid file that would be
// included during the final packaging.
                            if (checkFileForPackaging((IFile)resource)) {
mMakeFinalPackage = true;
}

//Synthetic comment -- @@ -173,7 +145,7 @@
// if this is a folder, we check if this is a valid folder as well.
// If this is a folder that needs to be ignored, we must return false,
// so that we ignore its content.
                            return checkFolderForPackaging((IFolder)resource);
}
}
}
//Synthetic comment -- @@ -194,53 +166,6 @@
}
}

    /**
     * Custom {@link IZipEntryFilter} to filter out everything that is not a standard java
     * resources, and also record whether the zip file contains native libraries.
     * <p/>Used in {@link SignedJarBuilder#writeZip(java.io.InputStream, IZipEntryFilter)} when
     * we only want the java resources from external jars.
     */
    private final static class JavaAndNativeResourceFilter extends JavaResourceFilter {
        private final List<String> mNativeLibs = new ArrayList<String>();
        private boolean mNativeLibInteference = false;

        @Override
        public boolean checkEntry(String name) {
            boolean value = super.checkEntry(name);

            // only do additional checks if the file passes the default checks.
            if (value) {
                if (name.endsWith(".so")) {
                    mNativeLibs.add(name);

                    // only .so located in lib/ will interfer with the installation
                    if (name.startsWith("lib/")) {
                        mNativeLibInteference = true;
                    }
                } else if (name.endsWith(".jnilib")) {
                    mNativeLibs.add(name);
                }
            }

            return value;
        }

        List<String> getNativeLibs() {
            return mNativeLibs;
        }

        boolean getNativeLibInterefence() {
            return mNativeLibInteference;
        }

        void clear() {
            mNativeLibs.clear();
            mNativeLibInteference = false;
        }
    }

    private final JavaAndNativeResourceFilter mResourceFilter = new JavaAndNativeResourceFilter();

public ApkBuilder() {
super();
}
//Synthetic comment -- @@ -454,9 +379,18 @@
ic.refreshLocal(IResource.DEPTH_ONE, monitor);
}

// we need to test all three, as we may need to make the final package
// but not the intermediary ones.
if (mPackageResources || mConvertToDex || mBuildFinalPackage) {
// resource to the AndroidManifest.xml file
IFile manifestFile = project.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);

//Synthetic comment -- @@ -493,64 +427,23 @@
removeMarkersFromContainer(project, AndroidConstants.MARKER_AAPT_PACKAGE);

// need to figure out some path before we can execute aapt;

                    // get the resource folder
                    IFolder resFolder = project.getFolder(AndroidConstants.WS_RESOURCES);

                    // and the assets folder
                    IFolder assetsFolder = project.getFolder(AndroidConstants.WS_ASSETS);

                    // we need to make sure this one exists.
                    if (assetsFolder.exists() == false) {
                        assetsFolder = null;
}

                    IPath resLocation = resFolder.getLocation();
                    IPath manifestLocation = manifestFile.getLocation();

                    if (resLocation != null && manifestLocation != null) {
                        // list of res folder (main project + maybe libraries)
                        ArrayList<String> osResPaths = new ArrayList<String>();
                        osResPaths.add(resLocation.toOSString()); //main project

                        // libraries?
                        if (libProjects != null) {
                            for (IProject lib : libProjects) {
                                IFolder libResFolder = lib.getFolder(SdkConstants.FD_RES);
                                if (libResFolder.exists()) {
                                    osResPaths.add(libResFolder.getLocation().toOSString());
                                }
                            }
                        }

                        String osManifestPath = manifestLocation.toOSString();

                        String osAssetsPath = null;
                        if (assetsFolder != null) {
                            osAssetsPath = assetsFolder.getLocation().toOSString();
                        }

                        // build the default resource package
                        if (executeAapt(project, osManifestPath, osResPaths,
                                osAssetsPath,
                                osBinPath + File.separator + AndroidConstants.FN_RESOURCES_AP_,
                                null /*configFilter*/) == false) {
                            // aapt failed. Whatever files that needed to be marked
                            // have already been marked. We just return.
                            return allRefProjects;
                        }

                        // build has been done. reset the state of the builder
                        mPackageResources = false;

                        // and store it
                        saveProjectBooleanProperty(PROPERTY_PACKAGE_RESOURCES, mPackageResources);
                    }
}

// then we check if we need to package the .class into classes.dex
if (mConvertToDex) {
                    if (executeDx(javaProject, osBinPath, osBinPath + File.separator +
AndroidConstants.FN_CLASSES_DEX, referencedJavaProjects) == false) {
// dx failed, we return
return allRefProjects;
//Synthetic comment -- @@ -584,7 +477,7 @@

String classesDexPath = osBinPath + File.separator +
AndroidConstants.FN_CLASSES_DEX;
                if (finalPackage(osBinPath + File.separator + AndroidConstants.FN_RESOURCES_AP_,
classesDexPath, osFinalPackagePath, javaProject, libProjects,
referencedJavaProjects, debuggable) == false) {
return allRefProjects;
//Synthetic comment -- @@ -645,639 +538,6 @@
}

/**
     * Executes aapt. If any error happen, files or the project will be marked.
     * @param project The Project
     * @param osManifestPath The path to the manifest file
     * @param osResPath The path to the res folder
     * @param osAssetsPath The path to the assets folder. This can be null.
     * @param osOutFilePath The path to the temporary resource file to create.
     * @param configFilter The configuration filter for the resources to include
     * (used with -c option, for example "port,en,fr" to include portrait, English and French
     * resources.)
     * @return true if success, false otherwise.
     */
    private boolean executeAapt(IProject project, String osManifestPath,
            List<String> osResPaths, String osAssetsPath, String osOutFilePath,
            String configFilter) {
        IAndroidTarget target = Sdk.getCurrent().getTarget(project);

        // Create the command line.
        ArrayList<String> commandArray = new ArrayList<String>();
        commandArray.add(target.getPath(IAndroidTarget.AAPT));
        commandArray.add("package"); //$NON-NLS-1$
        commandArray.add("-f");//$NON-NLS-1$
        if (AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE) {
            commandArray.add("-v"); //$NON-NLS-1$
        }

        // if more than one res, this means there's a library (or more) and we need
        // to activate the auto-add-overlay
        if (osResPaths.size() > 1) {
            commandArray.add("--auto-add-overlay"); //$NON-NLS-1$
        }

        if (configFilter != null) {
            commandArray.add("-c"); //$NON-NLS-1$
            commandArray.add(configFilter);
        }

        commandArray.add("-M"); //$NON-NLS-1$
        commandArray.add(osManifestPath);

        for (String path : osResPaths) {
            commandArray.add("-S"); //$NON-NLS-1$
            commandArray.add(path);
        }

        if (osAssetsPath != null) {
            commandArray.add("-A"); //$NON-NLS-1$
            commandArray.add(osAssetsPath);
        }

        commandArray.add("-I"); //$NON-NLS-1$
        commandArray.add(target.getPath(IAndroidTarget.ANDROID_JAR));

        commandArray.add("-F"); //$NON-NLS-1$
        commandArray.add(osOutFilePath);

        String command[] = commandArray.toArray(
                new String[commandArray.size()]);

        if (AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE) {
            StringBuilder sb = new StringBuilder();
            for (String c : command) {
                sb.append(c);
                sb.append(' ');
            }
            AdtPlugin.printToConsole(project, sb.toString());
        }

        // launch
        int execError = 1;
        try {
            // launch the command line process
            Process process = Runtime.getRuntime().exec(command);

            // list to store each line of stderr
            ArrayList<String> results = new ArrayList<String>();

            // get the output and return code from the process
            execError = grabProcessOutput(process, results);

            // attempt to parse the error output
            boolean parsingError = parseAaptOutput(results, project);

            // if we couldn't parse the output we display it in the console.
            if (parsingError) {
                if (execError != 0) {
                    AdtPlugin.printErrorToConsole(project, results.toArray());
                } else {
                    AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project,
                            results.toArray());
                }
            }

            // We need to abort if the exec failed.
            if (execError != 0) {
                // if the exec failed, and we couldn't parse the error output (and therefore
                // not all files that should have been marked, were marked), we put a generic
                // marker on the project and abort.
                if (parsingError) {
                    markProject(AndroidConstants.MARKER_PACKAGING, Messages.Unparsed_AAPT_Errors,
                            IMarker.SEVERITY_ERROR);
                }

                // abort if exec failed.
                return false;
            }
        } catch (IOException e1) {
            String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        } catch (InterruptedException e) {
            String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        }

        return true;
    }

    /**
     * Execute the Dx tool for dalvik code conversion.
     * @param javaProject The java project
     * @param osBinPath the path to the output folder of the project
     * @param osOutFilePath the path of the dex file to create.
     * @param referencedJavaProjects the list of referenced projects for this project.
     *
     * @throws CoreException
     */
    private boolean executeDx(IJavaProject javaProject, String osBinPath, String osOutFilePath,
            IJavaProject[] referencedJavaProjects) throws CoreException {
        IAndroidTarget target = Sdk.getCurrent().getTarget(javaProject.getProject());
        AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
        if (targetData == null) {
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    Messages.ApkBuilder_UnableBuild_Dex_Not_loaded));
        }

        // get the dex wrapper
        DexWrapper wrapper = targetData.getDexWrapper();

        if (wrapper == null) {
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    Messages.ApkBuilder_UnableBuild_Dex_Not_loaded));
        }

        // make sure dx use the proper output streams.
        // first make sure we actually have the streams available.
        if (mOutStream == null) {
            IProject project = getProject();
            mOutStream = AdtPlugin.getOutPrintStream(project, DX_PREFIX);
            mErrStream = AdtPlugin.getErrPrintStream(project, DX_PREFIX);
        }

        try {
            // get the list of libraries to include with the source code
            String[] libraries = getExternalJars();

            // get the list of referenced projects output to add
            String[] projectOutputs = getProjectOutputs(referencedJavaProjects);

            String[] fileNames = new String[1 + projectOutputs.length + libraries.length];

            // first this project output
            fileNames[0] = osBinPath;

            // then other project output
            System.arraycopy(projectOutputs, 0, fileNames, 1, projectOutputs.length);

            // then external jars.
            System.arraycopy(libraries, 0, fileNames, 1 + projectOutputs.length, libraries.length);

            int res = wrapper.run(osOutFilePath, fileNames,
                    AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE,
                    mOutStream, mErrStream);

            if (res != 0) {
                // output error message and marker the project.
                String message = String.format(Messages.Dalvik_Error_d,
                        res);
                AdtPlugin.printErrorToConsole(getProject(), message);
                markProject(AndroidConstants.MARKER_PACKAGING, message, IMarker.SEVERITY_ERROR);
                return false;
            }
        } catch (Throwable ex) {
            String message = ex.getMessage();
            if (message == null) {
                message = ex.getClass().getCanonicalName();
            }
            message = String.format(Messages.Dalvik_Error_s, message);
            AdtPlugin.printErrorToConsole(getProject(), message);
            markProject(AndroidConstants.MARKER_PACKAGING, message, IMarker.SEVERITY_ERROR);
            if ((ex instanceof NoClassDefFoundError)
                    || (ex instanceof NoSuchMethodError)) {
                AdtPlugin.printErrorToConsole(getProject(), Messages.Incompatible_VM_Warning,
                        Messages.Requires_1_5_Error);
            }
            return false;
        }

        return true;
    }

    /**
     * Makes the final package. Package the dex files, the temporary resource file into the final
     * package file.
     * @param intermediateApk The path to the temporary resource file.
     * @param dex The path to the dex file.
     * @param output The path to the final package file to create.
     * @param javaProject the java project being compiled
     * @param libProjects an optional list of library projects (can be null)
     * @param referencedJavaProjects referenced projects.
     * @param debuggable whether the project manifest has debuggable==true. If true, any gdbserver
     * executables will be packaged with the native libraries.
     * @return true if success, false otherwise.
     */
    private boolean finalPackage(String intermediateApk, String dex, String output,
            final IJavaProject javaProject, IProject[] libProjects,
            IJavaProject[] referencedJavaProjects, boolean debuggable) {

        FileOutputStream fos = null;
        try {
            IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
            String osKeyPath = store.getString(AdtPrefs.PREFS_CUSTOM_DEBUG_KEYSTORE);
            if (osKeyPath == null || new File(osKeyPath).exists() == false) {
                osKeyPath = DebugKeyProvider.getDefaultKeyStoreOsPath();
                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                        Messages.ApkBuilder_Using_Default_Key);
            } else {
                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                        String.format(Messages.ApkBuilder_Using_s_To_Sign, osKeyPath));
            }

            // TODO: get the store type from somewhere else.
            DebugKeyProvider provider = new DebugKeyProvider(osKeyPath, null /* storeType */,
                    new IKeyGenOutput() {
                        public void err(String message) {
                            AdtPlugin.printErrorToConsole(javaProject.getProject(),
                                    Messages.ApkBuilder_Signing_Key_Creation_s + message);
                        }

                        public void out(String message) {
                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                    javaProject.getProject(),
                                    Messages.ApkBuilder_Signing_Key_Creation_s + message);
                        }
            });
            PrivateKey key = provider.getDebugKey();
            X509Certificate certificate = (X509Certificate)provider.getCertificate();

            if (key == null) {
                String msg = String.format(Messages.Final_Archive_Error_s,
                        Messages.ApkBuilder_Unable_To_Gey_Key);
                AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
                markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
                return false;
            }

            // compare the certificate expiration date
            if (certificate != null && certificate.getNotAfter().compareTo(new Date()) < 0) {
                // TODO, regenerate a new one.
                String msg = String.format(Messages.Final_Archive_Error_s,
                    String.format(Messages.ApkBuilder_Certificate_Expired_on_s,
                            DateFormat.getInstance().format(certificate.getNotAfter())));
                AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
                markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
                return false;
            }

            // create the jar builder.
            fos = new FileOutputStream(output);
            SignedJarBuilder builder = new SignedJarBuilder(fos, key, certificate);

            // add the intermediate file containing the compiled resources.
            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                    String.format(Messages.ApkBuilder_Packaging_s, intermediateApk));
            FileInputStream fis = new FileInputStream(intermediateApk);
            try {
                builder.writeZip(fis, null /* filter */);
            } finally {
                fis.close();
            }

            // Now we add the new file to the zip archive for the classes.dex file.
            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                    String.format(Messages.ApkBuilder_Packaging_s,
                            AndroidConstants.FN_CLASSES_DEX));
            File entryFile = new File(dex);
            builder.writeFile(entryFile, AndroidConstants.FN_CLASSES_DEX);

            // Now we write the standard resources from the project and the referenced projects.
            writeStandardResources(builder, javaProject, referencedJavaProjects);

            // Now we write the standard resources from the external libraries
            for (String libraryOsPath : getExternalJars()) {
                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                        String.format(Messages.ApkBuilder_Packaging_s, libraryOsPath));
                try {
                    fis = new FileInputStream(libraryOsPath);
                    mResourceFilter.clear();
                    builder.writeZip(fis, mResourceFilter);

                    // check if we found native libraries in the external library. This
                    // constitutes an error or warning depending on if they are in lib/
                    List<String> nativeLibs = mResourceFilter.getNativeLibs();
                    boolean nativeInterference = mResourceFilter.getNativeLibInterefence();
                    if (nativeLibs.size() > 0) {
                        String libName = new File(libraryOsPath).getName();
                        String msg = String.format("Native libraries detected in '%1$s'. See console for more information.",
                                libName);


                        markProject(AndroidConstants.MARKER_PACKAGING, msg,
                                nativeInterference ||
                                        AdtPrefs.getPrefs().getBuildForceErrorOnNativeLibInJar() ?
                                        IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);

                        ArrayList<String> consoleMsgs = new ArrayList<String>();
                        consoleMsgs.add(String.format(
                                "The library '%1$s' contains native libraries that will not run on the device.",
                                libName));
                        if (nativeInterference) {
                            consoleMsgs.add("Additionally some of those libraries will interfer with the installation of the application because of their location in lib/");
                            consoleMsgs.add("lib/ is reserved for NDK libraries.");
                        }
                        consoleMsgs.add("The following libraries were found:");
                        for (String lib : nativeLibs) {
                            consoleMsgs.add(" - " + lib);
                        }
                        AdtPlugin.printErrorToConsole(javaProject.getProject(),
                                consoleMsgs.toArray());

                        return false;
                    }
                } finally {
                    fis.close();
                }
            }

            // now write the native libraries.
            // First look if the lib folder is there.
            IResource libFolder = javaProject.getProject().findMember(SdkConstants.FD_NATIVE_LIBS);
            if (libFolder != null && libFolder.exists() &&
                    libFolder.getType() == IResource.FOLDER) {
                // look inside and put .so in lib/* by keeping the relative folder path.
                writeNativeLibraries((IFolder) libFolder, builder, debuggable);
            }

            // write the native libraries for the library projects.
            if (libProjects != null) {
                for (IProject lib : libProjects) {
                    libFolder = lib.findMember(SdkConstants.FD_NATIVE_LIBS);
                    if (libFolder != null && libFolder.exists() &&
                            libFolder.getType() == IResource.FOLDER) {
                        // look inside and put .so in lib/* by keeping the relative folder path.
                        writeNativeLibraries((IFolder) libFolder, builder, debuggable);
                    }
                }
            }

            // close the jar file and write the manifest and sign it.
            builder.close();
        } catch (GeneralSecurityException e1) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e1.getMessage());
            AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        } catch (IOException e1) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e1.getMessage());
            AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        } catch (KeytoolException e) {
            String eMessage = e.getMessage();

            // mark the project with the standard message
            String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);

            // output more info in the console
            AdtPlugin.printErrorToConsole(javaProject.getProject(),
                    msg,
                    String.format(Messages.ApkBuilder_JAVA_HOME_is_s, e.getJavaHome()),
                    Messages.ApkBuilder_Update_or_Execute_manually_s,
                    e.getCommandLine());
        } catch (AndroidLocationException e) {
            String eMessage = e.getMessage();

            // mark the project with the standard message
            String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);

            // and also output it in the console
            AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
        } catch (CoreException e) {
            // mark project and return
            String msg = String.format(Messages.Final_Archive_Error_s, e.getMessage());
            AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        } catch (Exception e) {
            // try to catch other exception to actually display an error. This will be useful
            // if we get an NPE or something so that we can at least notify the user that something
            // went wrong (otherwise the build appears to succeed but the zip archive is not closed
            // and therefore invalid.
            String msg = e.getMessage();
            if (msg == null) {
                msg = e.getClass().getCanonicalName();
            }

            msg = String.format("Unknown error: %1$s", msg);
            AdtPlugin.printErrorToConsole(javaProject.getProject(), msg);
            markProject(AndroidConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // pass.
                }
            }
        }

        return true;
    }

    /**
     * Writes native libraries into a {@link SignedJarBuilder}.
     * <p/>The native libraries must be located in a given main folder. Under this folder, it is
     * expected that the libraries are under a sub-folder that represents the ABI of the library.
     *
     * The path in the archive is based on the ABI folder name, and located under a main
     * folder called "lib".
     *
     * This method also packages any "gdbserver" executable it finds in the ABI folders, if
     * <var>debuggable</var> is set to true.
     *
     * @param rootFolder The folder containing the native libraries.
     * @param jarBuilder the {@link SignedJarBuilder} used to create the archive.
     * @param debuggable whether the application is debuggable. If <code>true</code> then gdbserver
     * executables will be packaged as well.
     * @throws CoreException
     * @throws IOException
     */
    private void writeNativeLibraries(IFolder rootFolder, SignedJarBuilder jarBuilder,
            boolean debuggable)
            throws CoreException, IOException {
        // the native files must be under a single sub-folder under the main root folder.
        // the sub-folder represents the abi for the native libs
        IResource[] abis = rootFolder.members();
        for (IResource abi : abis) {
            if (abi.getType() == IResource.FOLDER) { // ignore non folders.
                IResource[] libs = ((IFolder)abi).members();

                for (IResource lib : libs) {
                    if (lib.getType() == IResource.FILE) { // ignore non files.
                        IPath path = lib.getFullPath();

                        // check the extension.
                        String ext = path.getFileExtension();
                        if (AndroidConstants.EXT_NATIVE_LIB.equalsIgnoreCase(ext) ||
                                (debuggable && GDBSERVER_NAME.equals(lib.getName()))) {
                            // compute the path inside the archive.
                            IPath apkPath = new Path(SdkConstants.FD_APK_NATIVE_LIBS);
                            apkPath = apkPath.append(abi.getName()).append(lib.getName());

                            // writes the file in the apk.
                            jarBuilder.writeFile(lib.getLocation().toFile(), apkPath.toString());
                        }
                    }
                }
            }
        }
    }

    /**
     * Writes the standard resources of a project and its referenced projects
     * into a {@link SignedJarBuilder}.
     * Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
     * @param javaProject the javaProject object.
     * @param referencedJavaProjects the java projects that this project references.
     * @throws IOException
     * @throws CoreException
     */
    private void writeStandardResources(SignedJarBuilder jarBuilder, IJavaProject javaProject,
            IJavaProject[] referencedJavaProjects) throws IOException, CoreException {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsRoot = ws.getRoot();

        // create a list of path already put into the archive, in order to detect conflict
        ArrayList<String> list = new ArrayList<String>();

        writeStandardProjectResources(jarBuilder, javaProject, wsRoot, list);

        for (IJavaProject referencedJavaProject : referencedJavaProjects) {
            // only include output from non android referenced project
            // (This is to handle the case of reference Android projects in the context of
            // instrumentation projects that need to reference the projects to be tested).
            if (referencedJavaProject.getProject().hasNature(
                    AndroidConstants.NATURE_DEFAULT) == false) {
                writeStandardProjectResources(jarBuilder, referencedJavaProject, wsRoot, list);
            }
        }
    }

    /**
     * Writes the standard resources of a {@link IJavaProject} into a {@link SignedJarBuilder}.
     * Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
     * @param javaProject the javaProject object.
     * @param wsRoot the {@link IWorkspaceRoot}.
     * @param list a list of files already added to the archive, to detect conflicts.
     * @throws IOException
     */
    private void writeStandardProjectResources(SignedJarBuilder jarBuilder,
            IJavaProject javaProject, IWorkspaceRoot wsRoot, ArrayList<String> list)
            throws IOException {
        // get the source pathes
        ArrayList<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(javaProject);

        // loop on them and then recursively go through the content looking for matching files.
        for (IPath sourcePath : sourceFolders) {
            IResource sourceResource = wsRoot.findMember(sourcePath);
            if (sourceResource != null && sourceResource.getType() == IResource.FOLDER) {
                writeStandardSourceFolderResources(jarBuilder, sourcePath, (IFolder)sourceResource,
                        list);
            }
        }
    }

    /**
     * Recursively writes the standard resources of a source folder into a {@link SignedJarBuilder}.
     * Standard resources are non java/aidl files placed in the java package folders.
     * @param jarBuilder the {@link SignedJarBuilder}.
     * @param sourceFolder the {@link IPath} of the source folder.
     * @param currentFolder The current folder we're recursively processing.
     * @param list a list of files already added to the archive, to detect conflicts.
     * @throws IOException
     */
    private void writeStandardSourceFolderResources(SignedJarBuilder jarBuilder, IPath sourceFolder,
            IFolder currentFolder, ArrayList<String> list) throws IOException {
        try {
            IResource[] members = currentFolder.members();

            for (IResource member : members) {
                int type = member.getType();
                if (type == IResource.FILE && member.exists()) {
                    if (checkFileForPackaging((IFile)member)) {
                        // this files must be added to the archive.
                        IPath fullPath = member.getFullPath();

                        // We need to create its path inside the archive.
                        // This path is relative to the source folder.
                        IPath relativePath = fullPath.removeFirstSegments(
                                sourceFolder.segmentCount());
                        String zipPath = relativePath.toString();

                        // lets check it's not already in the list of path added to the archive
                        if (list.indexOf(zipPath) != -1) {
                            AdtPlugin.printErrorToConsole(getProject(),
                                    String.format(
                                            Messages.ApkBuilder_s_Conflict_with_file_s,
                                            fullPath, zipPath));
                        } else {
                            // get the File object
                            File entryFile = member.getLocation().toFile();

                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, getProject(),
                                    String.format(
                                            Messages.ApkBuilder_Packaging_s_into_s,
                                            fullPath, zipPath));

                            // write it in the zip archive
                            jarBuilder.writeFile(entryFile, zipPath);

                            // and add it to the list of entries
                            list.add(zipPath);
                        }
                    }
                } else if (type == IResource.FOLDER) {
                    if (checkFolderForPackaging((IFolder)member)) {
                        writeStandardSourceFolderResources(jarBuilder, sourceFolder,
                                (IFolder)member, list);
                    }
                }
            }
        } catch (CoreException e) {
            // if we can't get the members of the folder, we just don't do anything.
        }
    }

    /**
     * Returns the list of the output folders for the specified {@link IJavaProject} objects, if
     * they are Android projects.
     *
     * @param referencedJavaProjects the java projects.
     * @return an array, always. Can be empty.
     * @throws CoreException
     */
    private String[] getProjectOutputs(IJavaProject[] referencedJavaProjects) throws CoreException {
        ArrayList<String> list = new ArrayList<String>();

        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsRoot = ws.getRoot();

        for (IJavaProject javaProject : referencedJavaProjects) {
            // only include output from non android referenced project
            // (This is to handle the case of reference Android projects in the context of
            // instrumentation projects that need to reference the projects to be tested).
            if (javaProject.getProject().hasNature(AndroidConstants.NATURE_DEFAULT) == false) {
                // get the output folder
                IPath path = null;
                try {
                    path = javaProject.getOutputLocation();
                } catch (JavaModelException e) {
                    continue;
                }

                IResource outputResource = wsRoot.findMember(path);
                if (outputResource != null && outputResource.getType() == IResource.FOLDER) {
                    String outputOsPath = outputResource.getLocation().toOSString();

                    list.add(outputOsPath);
                }
            }
        }

        return list.toArray(new String[list.size()]);
    }

    /**
* Returns an array of {@link IJavaProject} matching the provided {@link IProject} objects.
* @param projects the IProject objects.
* @return an array, always. Can be empty.
//Synthetic comment -- @@ -1296,28 +556,6 @@
return list.toArray(new IJavaProject[list.size()]);
}

    /**
     * Checks a {@link IFile} to make sure it should be packaged as standard resources.
     * @param file the IFile representing the file.
     * @return true if the file should be packaged as standard java resources.
     */
    static boolean checkFileForPackaging(IFile file) {
        String name = file.getName();

        String ext = file.getFileExtension();
        return JavaResourceFilter.checkFileForPackaging(name, ext);
    }

    /**
     * Checks whether an {@link IFolder} and its content is valid for packaging into the .apk as
     * standard Java resource.
     * @param folder the {@link IFolder} to check.
     */
    static boolean checkFolderForPackaging(IFolder folder) {
        String name = folder.getName();
        return JavaResourceFilter.checkFolderForPackaging(name);
    }

@Override
protected void abortOnBadSetup(IJavaProject javaProject) throws CoreException {
super.abortOnBadSetup(javaProject);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..fa5993e

//Synthetic comment -- @@ -0,0 +1,911 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkDeltaVisitor.java
//Synthetic comment -- index b01ab39..643fee5 100644

//Synthetic comment -- @@ -237,7 +237,7 @@
// inside the native library folder. Test if the changed resource is a .so file.
if (type == IResource.FILE &&
(AndroidConstants.EXT_NATIVE_LIB.equalsIgnoreCase(path.getFileExtension())
                            || ApkBuilder.GDBSERVER_NAME.equals(resource.getName()))) {
mMakeFinalPackage = true;
return false; // return false for file.
}
//Synthetic comment -- @@ -260,9 +260,9 @@
// Also excluded are aidl files, and package.html files
if (type == IResource.FOLDER) {
// always visit the subfolders, unless the folder is not to be included
                            return ApkBuilder.checkFolderForPackaging((IFolder)resource);
} else if (type == IResource.FILE) {
                            if (ApkBuilder.checkFileForPackaging((IFile)resource)) {
mMakeFinalPackage = true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java
//Synthetic comment -- index 7bf000f..7211ac4 100644

//Synthetic comment -- @@ -33,27 +33,20 @@
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
//Synthetic comment -- @@ -65,101 +58,6 @@
*/
abstract class BaseBuilder extends IncrementalProjectBuilder {

    // TODO: rename the pattern to something that makes sense + javadoc comments.

    /**
     * Single line aapt warning for skipping files.<br>
     * "  (skipping hidden file '&lt;file path&gt;'"
     */
    private final static Pattern sPattern0Line1 = Pattern.compile(
            "^\\s+\\(skipping hidden file\\s'(.*)'\\)$"); //$NON-NLS-1$

    /**
     * First line of dual line aapt error.<br>
     * "ERROR at line &lt;line&gt;: &lt;error&gt;"<br>
     * " (Occurred while parsing &lt;path&gt;)"
     */
    private final static Pattern sPattern1Line1 = Pattern.compile(
            "^ERROR\\s+at\\s+line\\s+(\\d+):\\s+(.*)$"); //$NON-NLS-1$
    /**
     * Second line of dual line aapt error.<br>
     * "ERROR at line &lt;line&gt;: &lt;error&gt;"<br>
     * " (Occurred while parsing &lt;path&gt;)"<br>
     * @see #sPattern1Line1
     */
    private final static Pattern sPattern1Line2 = Pattern.compile(
            "^\\s+\\(Occurred while parsing\\s+(.*)\\)$");  //$NON-NLS-1$
    /**
     * First line of dual line aapt error.<br>
     * "ERROR: &lt;error&gt;"<br>
     * "Defined at file &lt;path&gt; line &lt;line&gt;"
     */
    private final static Pattern sPattern2Line1 = Pattern.compile(
            "^ERROR:\\s+(.+)$"); //$NON-NLS-1$
    /**
     * Second line of dual line aapt error.<br>
     * "ERROR: &lt;error&gt;"<br>
     * "Defined at file &lt;path&gt; line &lt;line&gt;"<br>
     * @see #sPattern2Line1
     */
    private final static Pattern sPattern2Line2 = Pattern.compile(
            "Defined\\s+at\\s+file\\s+(.+)\\s+line\\s+(\\d+)"); //$NON-NLS-1$
    /**
     * Single line aapt error<br>
     * "&lt;path&gt; line &lt;line&gt;: &lt;error&gt;"
     */
    private final static Pattern sPattern3Line1 = Pattern.compile(
            "^(.+)\\sline\\s(\\d+):\\s(.+)$"); //$NON-NLS-1$
    /**
     * First line of dual line aapt error.<br>
     * "ERROR parsing XML file &lt;path&gt;"<br>
     * "&lt;error&gt; at line &lt;line&gt;"
     */
    private final static Pattern sPattern4Line1 = Pattern.compile(
            "^Error\\s+parsing\\s+XML\\s+file\\s(.+)$"); //$NON-NLS-1$
    /**
     * Second line of dual line aapt error.<br>
     * "ERROR parsing XML file &lt;path&gt;"<br>
     * "&lt;error&gt; at line &lt;line&gt;"<br>
     * @see #sPattern4Line1
     */
    private final static Pattern sPattern4Line2 = Pattern.compile(
            "^(.+)\\s+at\\s+line\\s+(\\d+)$"); //$NON-NLS-1$

    /**
     * Single line aapt warning<br>
     * "&lt;path&gt;:&lt;line&gt;: &lt;error&gt;"
     */
    private final static Pattern sPattern5Line1 = Pattern.compile(
            "^(.+?):(\\d+):\\s+WARNING:(.+)$"); //$NON-NLS-1$

    /**
     * Single line aapt error<br>
     * "&lt;path&gt;:&lt;line&gt;: &lt;error&gt;"
     */
    private final static Pattern sPattern6Line1 = Pattern.compile(
            "^(.+?):(\\d+):\\s+(.+)$"); //$NON-NLS-1$

    /**
     * 4 line aapt error<br>
     * "ERROR: 9-path image &lt;path&gt; malformed"<br>
     * Line 2 and 3 are taken as-is while line 4 is ignored (it repeats with<br>
     * 'ERROR: failure processing &lt;path&gt;)
     */
    private final static Pattern sPattern7Line1 = Pattern.compile(
            "^ERROR:\\s+9-patch\\s+image\\s+(.+)\\s+malformed\\.$"); //$NON-NLS-1$

    private final static Pattern sPattern8Line1 = Pattern.compile(
            "^(invalid resource directory name): (.*)$"); //$NON-NLS-1$

    /**
     * 2 line aapt error<br>
     * "ERROR: Invalid configuration: foo"<br>
     * "                              ^^^"<br>
     * There's no need to parse the 2nd line.
     */
    private final static Pattern sPattern9Line1 = Pattern.compile(
            "^Invalid configuration: (.+)$"); //$NON-NLS-1$

/** SAX Parser factory. */
private SAXParserFactory mParserFactory;
//Synthetic comment -- @@ -352,6 +250,19 @@
* @throws InterruptedException
*/
protected final int grabProcessOutput(final Process process,
final ArrayList<String> results)
throws InterruptedException {
// Due to the limited buffer size on windows for the standard io (stderr, stdout), we
//Synthetic comment -- @@ -388,8 +299,6 @@
InputStreamReader is = new InputStreamReader(process.getInputStream());
BufferedReader outReader = new BufferedReader(is);

                IProject project = getProject();

try {
while (true) {
String line = outReader.readLine();
//Synthetic comment -- @@ -412,218 +321,6 @@
}

/**
     * Parse the output of aapt and mark the incorrect file with error markers
     *
     * @param results the output of aapt
     * @param project the project containing the file to mark
     * @return true if the parsing failed, false if success.
     */
    protected final boolean parseAaptOutput(ArrayList<String> results,
            IProject project) {
        // nothing to parse? just return false;
        if (results.size() == 0) {
            return false;
        }

        // get the root of the project so that we can make IFile from full
        // file path
        String osRoot = project.getLocation().toOSString();

        Matcher m;

        for (int i = 0; i < results.size(); i++) {
            String p = results.get(i);

            m = sPattern0Line1.matcher(p);
            if (m.matches()) {
                // we ignore those (as this is an ignore message from aapt)
                continue;
            }

            m = sPattern1Line1.matcher(p);
            if (m.matches()) {
                String lineStr = m.group(1);
                String msg = m.group(2);

                // get the matcher for the next line.
                m = getNextLineMatcher(results, ++i, sPattern1Line2);
                if (m == null) {
                    return true;
                }

                String location = m.group(1);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }
                continue;
            }

            // this needs to be tested before Pattern2 since they both start with 'ERROR:'
            m = sPattern7Line1.matcher(p);
            if (m.matches()) {
                String location = m.group(1);
                String msg = p; // default msg is the line in case we don't find anything else

                if (++i < results.size()) {
                    msg = results.get(i).trim();
                    if (++i < results.size()) {
                        msg = msg + " - " + results.get(i).trim(); //$NON-NLS-1$

                        // skip the next line
                        i++;
                    }
                }

                // display the error
                if (checkAndMark(location, null, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m =  sPattern2Line1.matcher(p);
            if (m.matches()) {
                // get the msg
                String msg = m.group(1);

                // get the matcher for the next line.
                m = getNextLineMatcher(results, ++i, sPattern2Line2);
                if (m == null) {
                    return true;
                }

                String location = m.group(1);
                String lineStr = m.group(2);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }
                continue;
            }

            m = sPattern3Line1.matcher(p);
            if (m.matches()) {
                String location = m.group(1);
                String lineStr = m.group(2);
                String msg = m.group(3);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m = sPattern4Line1.matcher(p);
            if (m.matches()) {
                // get the filename.
                String location = m.group(1);

                // get the matcher for the next line.
                m = getNextLineMatcher(results, ++i, sPattern4Line2);
                if (m == null) {
                    return true;
                }

                String msg = m.group(1);
                String lineStr = m.group(2);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m = sPattern5Line1.matcher(p);
            if (m.matches()) {
                String location = m.group(1);
                String lineStr = m.group(2);
                String msg = m.group(3);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_WARNING) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m = sPattern6Line1.matcher(p);
            if (m.matches()) {
                String location = m.group(1);
                String lineStr = m.group(2);
                String msg = m.group(3);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m = sPattern8Line1.matcher(p);
            if (m.matches()) {
                String location = m.group(2);
                String msg = m.group(1);

                // check the values and attempt to mark the file.
                if (checkAndMark(location, null, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            m = sPattern9Line1.matcher(p);
            if (m.matches()) {
                String badConfig = m.group(1);
                String msg = String.format("APK Configuration filter '%1$s' is invalid", badConfig);

                // skip the next line
                i++;

                // check the values and attempt to mark the file.
                if (checkAndMark(null /*location*/, null, msg, osRoot, project,
                        AndroidConstants.MARKER_AAPT_PACKAGE, IMarker.SEVERITY_ERROR) == false) {
                    return true;
                }

                // success, go to the next line
                continue;
            }

            // invalid line format, flag as error, and bail
            return true;
        }

        return false;
    }



    /**
* Saves a String property into the persistent storage of the project.
* @param propertyName the name of the property. The id of the plugin is added to this string.
* @param value the value to save
//Synthetic comment -- @@ -688,195 +385,6 @@
return ProjectHelper.loadResourceProperty(project, propertyName);
}

    /**
     * Check if the parameters gotten from the error output are valid, and mark
     * the file with an AAPT marker.
     * @param location the full OS path of the error file. If null, the project is marked
     * @param lineStr
     * @param message
     * @param root The root directory of the project, in OS specific format.
     * @param project
     * @param markerId The marker id to put.
     * @param severity The severity of the marker to put (IMarker.SEVERITY_*)
     * @return true if the parameters were valid and the file was marked successfully.
     *
     * @see IMarker
     */
    private final  boolean checkAndMark(String location, String lineStr,
            String message, String root, IProject project, String markerId, int severity) {
        // check this is in fact a file
        if (location != null) {
            File f = new File(location);
            if (f.exists() == false) {
                return false;
            }
        }

        // get the line number
        int line = -1; // default value for error with no line.

        if (lineStr != null) {
            try {
                line = Integer.parseInt(lineStr);
            } catch (NumberFormatException e) {
                // looks like the string we extracted wasn't a valid
                // file number. Parsing failed and we return true
                return false;
            }
        }

        // add the marker
        IResource f2 = project;
        if (location != null) {
            f2 = getResourceFromFullPath(location, root, project);
            if (f2 == null) {
                return false;
            }
        }

        // check if there's a similar marker already, since aapt is launched twice
        boolean markerAlreadyExists = false;
        try {
            IMarker[] markers = f2.findMarkers(markerId, true, IResource.DEPTH_ZERO);

            for (IMarker marker : markers) {
                int tmpLine = marker.getAttribute(IMarker.LINE_NUMBER, -1);
                if (tmpLine != line) {
                    break;
                }

                int tmpSeverity = marker.getAttribute(IMarker.SEVERITY, -1);
                if (tmpSeverity != severity) {
                    break;
                }

                String tmpMsg = marker.getAttribute(IMarker.MESSAGE, null);
                if (tmpMsg == null || tmpMsg.equals(message) == false) {
                    break;
                }

                // if we're here, all the marker attributes are equals, we found it
                // and exit
                markerAlreadyExists = true;
                break;
            }

        } catch (CoreException e) {
            // if we couldn't get the markers, then we just mark the file again
            // (since markerAlreadyExists is initialized to false, we do nothing)
        }

        if (markerAlreadyExists == false) {
            BaseProjectHelper.markResource(f2, markerId, message, line, severity);
        }

        return true;
    }

    /**
     * Returns a matching matcher for the next line
     * @param lines The array of lines
     * @param nextIndex The index of the next line
     * @param pattern The pattern to match
     * @return null if error or no match, the matcher otherwise.
     */
    private final Matcher getNextLineMatcher(ArrayList<String> lines,
            int nextIndex, Pattern pattern) {
        // unless we can't, because we reached the last line
        if (nextIndex == lines.size()) {
            // we expected a 2nd line, so we flag as error
            // and we bail
            return null;
        }

        Matcher m = pattern.matcher(lines.get(nextIndex));
        if (m.matches()) {
           return m;
        }

        return null;
    }

    private IResource getResourceFromFullPath(String filename, String root,
            IProject project) {
        if (filename.startsWith(root)) {
            String file = filename.substring(root.length());

            // get the resource
            IResource r = project.findMember(file);

            // if the resource is valid, we add the marker
            if (r.exists()) {
                return r;
            }
        }

        return null;
    }

    /**
     * Returns an array of external jar files used by the project.
     * @return an array of OS-specific absolute file paths
     */
    protected final String[] getExternalJars() {
        // get the current project
        IProject project = getProject();

        // get a java project from it
        IJavaProject javaProject = JavaCore.create(project);

        IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

        ArrayList<String> oslibraryList = new ArrayList<String>();
        IClasspathEntry[] classpaths = javaProject.readRawClasspath();
        if (classpaths != null) {
            for (IClasspathEntry e : classpaths) {
                if (e.getEntryKind() == IClasspathEntry.CPE_LIBRARY ||
                        e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                    // if this is a classpath variable reference, we resolve it.
                    if (e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                        e = JavaCore.getResolvedClasspathEntry(e);
                    }

                    // get the IPath
                    IPath path = e.getPath();

                    // check the name ends with .jar
                    if (AndroidConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
                        boolean local = false;
                        IResource resource = wsRoot.findMember(path);
                        if (resource != null && resource.exists() &&
                                resource.getType() == IResource.FILE) {
                            local = true;
                            oslibraryList.add(resource.getLocation().toOSString());
                        }

                        if (local == false) {
                            // if the jar path doesn't match a workspace resource,
                            // then we get an OSString and check if this links to a valid file.
                            String osFullPath = path.toOSString();

                            File f = new File(osFullPath);
                            if (f.exists()) {
                                oslibraryList.add(osFullPath);
                            } else {
                                String message = String.format( Messages.Couldnt_Locate_s_Error,
                                        path);
                                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                        project, message);

                                // Also put a warning marker on the project
                                markProject(AndroidConstants.MARKER_PACKAGING, message,
                                        IMarker.SEVERITY_WARNING);
                            }
                        }
                    }
                }
            }
        }

        return oslibraryList.toArray(new String[oslibraryList.size()]);
    }

/**
* Aborts the build if the SDK/project setups are broken. This does not








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index a65b33a..388bffa 100644

//Synthetic comment -- @@ -719,7 +719,7 @@
execError = grabProcessOutput(process, results);

// attempt to parse the error output
            boolean parsingError = parseAaptOutput(results, project);

// if we couldn't parse the output we display it in the console.
if (parsingError) {







