/*ADT: Ability to run proguard as part of the builders.

SDK Bug: 3126476

This adds a global prefs that enable users to have the builders
run Proguard on projects which are configured for it. Having a
global pref is probably not optimal but is a good way to get
started. Ideally it should be a per project setting or ever
better a launch configuration option, but this is a bigger scope
we can implement later.

There are also a few fixes to make Proguard work under Windows:
- the proguard.bat can only pass 9 args, but we have 15+, so
  we'll use a temporary proguard config file for Windows.
- PROGUARD_HOME needs to be defined properly since the default
  proguard.bat cannot infer it properly. We need other fixes
  to that batch file so eventually we're not going to ship the
  default one and we can fix the .bat file, but properly defining
  this env var is probably a good idea anyway.

Change-Id:Ic18c6318eeb0310137fde13554664a746fb847e7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index e582a91..60242d9 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -55,8 +57,12 @@
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//Synthetic comment -- @@ -67,6 +73,8 @@
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
* Helper with methods for the last 3 steps of the generation of an APK.
//Synthetic comment -- @@ -453,7 +461,7 @@
command.add(new File(logOutput, "seeds.txt").getAbsolutePath());   //$NON-NLS-1$

command.add("-printusage");                                        //$NON-NLS-1$
            command.add(new File(logOutput, "unused.txt").getAbsolutePath());  //$NON-NLS-1$

command.add("-printmapping");                                      //$NON-NLS-1$
command.add(new File(logOutput, "mapping.txt").getAbsolutePath()); //$NON-NLS-1$
//Synthetic comment -- @@ -599,6 +607,146 @@
}

/**
     * Executes proguard if project is configured for it.
     *
     * @param project The project on which to run proguard.
     * @return Returns an array for all the compiled code for the project, similar to
     *  {@link #getCompiledCodePaths(boolean, ResourceMarker)}.
     * @throws CoreException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ProguardResultException
     * @throws ProguardExecException
     */
    public String[] executeProguard(IProject project, ResourceMarker resMarker)
                throws CoreException, IOException, FileNotFoundException,
                ProguardResultException, ProguardExecException {

        // Check if the prefs indicate to use proguard.
        boolean runProguard = AdtPrefs.getPrefs().getBuildUseProguard();

        File proguardConfigFile = null;

        if (runProguard) {
            // Check if the project has the proguard config property defined.
            ProjectState state = Sdk.getProjectState(project);
            String proguardConfig = state.getProperties().getProperty(
                    ProjectProperties.PROPERTY_PROGUARD_CONFIG);

            // Check the config file actually exists.
            if (proguardConfig == null || proguardConfig.length() == 0) {
                runProguard = false;
            } else {
                proguardConfigFile = new File(proguardConfig);
                if (proguardConfigFile.isAbsolute() == false) {
                    proguardConfigFile = new File(project.getLocation().toFile(), proguardConfig);
                }
                runProguard = proguardConfigFile.isFile();

                if (!runProguard) {
                    // There's a config file property but the file does not exist.
                    // Issue a warning in the log to let the user know it's not going to be
                    // proguarded. The warning is not on the output console though, to avoid
                    // spamming at each rebuild.
                    AdtPlugin.log(IStatus.WARNING,
                            "Proguard is being ignored: Project %1$s defines %2$s=%3$s but file %4$s does not exist.",   //$NON-NLS-1$
                            project.getName(),
                            ProjectProperties.PROPERTY_PROGUARD_CONFIG,
                            proguardConfig,
                            proguardConfigFile.getAbsoluteFile());
                }
            }
        }

        if (runProguard) {
            // the output of the main project (and any java-only project dependency)
            String[] projectOutputs = getProjectOutputs();

            // create a jar from the output of these projects
            File inputJar = File.createTempFile(TEMP_PREFIX, AndroidConstants.DOT_JAR);
            // TODO FIXME this may leave a lot of temp files around on a long session.
            // Should have a better way to clean up e.g. before each build.
            inputJar.deleteOnExit();

            JarOutputStream jos = new JarOutputStream(new FileOutputStream(inputJar));
            for (String po : projectOutputs) {
                File root = new File(po);
                if (root.exists()) {
                    addFileToJar(jos, root, root);
                }
            }
            jos.close();

            // get the other jar files
            String[] jarFiles = getCompiledCodePaths(
                    false /*includeProjectOutputs*/,
                    resMarker);

            // destination file for proguard
            File obfuscatedJar = File.createTempFile(TEMP_PREFIX, AndroidConstants.DOT_JAR);
            obfuscatedJar.deleteOnExit();

            // run proguard
            runProguard(proguardConfigFile, inputJar, jarFiles, obfuscatedJar,
                    new File(project.getLocation().toFile(), SdkConstants.FD_PROGUARD));

            // dx input is proguard's output
            return new String[] { obfuscatedJar.getAbsolutePath() };
        } else {
            // no proguard, simply get all the compiled code path: project output(s) +
            // jar file(s)
            return getCompiledCodePaths(
                    true /*includeProjectOutputs*/,
                    resMarker);
        }
    }

    /**
     * Adds a file to a jar file.
     * The <var>rootDirectory</var> dictates the path of the file inside the jar file. It must be
     * a parent of <var>file</var>.
     * @param jar the jar to add the file to
     * @param file the file to add
     * @param rootDirectory the rootDirectory.
     * @throws IOException
     */
    private static void addFileToJar(JarOutputStream jar, File file, File rootDirectory)
            throws IOException {
        if (file.isDirectory()) {
            for (File child: file.listFiles()) {
                addFileToJar(jar, child, rootDirectory);
            }

        } else if (file.isFile()) {
            // check the extension
            String name = file.getName();
            if (name.toLowerCase().endsWith(AndroidConstants.DOT_CLASS) == false) {
                return;
            }

            String rootPath = rootDirectory.getAbsolutePath();
            String path = file.getAbsolutePath();
            path = path.substring(rootPath.length()).replace("\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }

            JarEntry entry = new JarEntry(path);
            entry.setTime(file.lastModified());
            jar.putNextEntry(entry);

            // put the content of the file.
            byte[] buffer = new byte[1024];
            int count;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((count = bis.read(buffer)) != -1) {
                jar.write(buffer, 0, count);
            }
            jar.closeEntry();
        }
    }

    /**
* Executes aapt. If any error happen, files or the project will be marked.
* @param osManifestPath The path to the manifest file
* @param osResPath The path to the res folder








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 4fe237f..d3b8326 100644

//Synthetic comment -- @@ -487,8 +487,7 @@
// then we check if we need to package the .class into classes.dex
if (mConvertToDex) {
try {
                        String[] dxInputPaths = helper.executeProguard(project, mResourceMarker);

helper.executeDx(javaProject, dxInputPaths, osBinPath + File.separator +
SdkConstants.FN_APK_CLASSES_DEX);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index de5847b..e8122bd 100644

//Synthetic comment -- @@ -34,6 +34,8 @@

public final static String PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR = AdtPlugin.PLUGIN_ID + ".forceErrorNativeLibInJar"; //$NON-NLS-1$

    public final static String PREFS_BUILD_USE_PROGUARD = AdtPlugin.PLUGIN_ID + ".useProguard"; //$NON-NLS-1$

public final static String PREFS_BUILD_VERBOSITY = AdtPlugin.PLUGIN_ID + ".buildVerbosity"; //$NON-NLS-1$

public final static String PREFS_DEFAULT_DEBUG_KEYSTORE = AdtPlugin.PLUGIN_ID + ".defaultDebugKeyStore"; //$NON-NLS-1$
//Synthetic comment -- @@ -60,9 +62,10 @@
/** Verbosity of the build */
private BuildVerbosity mBuildVerbosity = BuildVerbosity.NORMAL;

    private boolean mBuildForceResResfresh = true;
private boolean mBuildForceErrorOnNativeLibInJar = true;
private boolean mFormatXml = false;
    private boolean mBuildUseProguard = false;
private float mMonitorDensity = 0.f;

public static enum BuildVerbosity {
//Synthetic comment -- @@ -139,6 +142,10 @@
mBuildForceErrorOnNativeLibInJar = mStore.getBoolean(PREFS_BUILD_RES_AUTO_REFRESH);
}

        if (property == null || PREFS_BUILD_USE_PROGUARD.equals(property)) {
            mBuildUseProguard = mStore.getBoolean(PREFS_BUILD_USE_PROGUARD);
        }

if (property == null || PREFS_MONITOR_DENSITY.equals(property)) {
mMonitorDensity = mStore.getFloat(PREFS_MONITOR_DENSITY);
}
//Synthetic comment -- @@ -172,6 +179,10 @@
return mBuildForceErrorOnNativeLibInJar;
}

    public boolean getBuildUseProguard() {
        return mBuildUseProguard;
    }

public float getMonitorDensity() {
return mMonitorDensity;
}
//Synthetic comment -- @@ -190,6 +201,7 @@

store.setDefault(PREFS_BUILD_RES_AUTO_REFRESH, true);
store.setDefault(PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR, true);
        store.setDefault(PREFS_BUILD_USE_PROGUARD, false);

store.setDefault(PREFS_BUILD_VERBOSITY, BuildVerbosity.ALWAYS.name());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/BuildPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/BuildPreferencePage.java
//Synthetic comment -- index 2c538bd..59d2881 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
//Synthetic comment -- @@ -62,6 +63,12 @@
"Force error when external jars contain native libraries",
getFieldEditorParent()));

        addField(new BooleanFieldEditor(AdtPrefs.PREFS_BUILD_USE_PROGUARD,
                "Use Proguard for projects which define " +
                ProjectProperties.PROPERTY_PROGUARD_CONFIG +
                " in their properties",
                getFieldEditorParent()));

RadioGroupFieldEditor rgfe = new RadioGroupFieldEditor(
AdtPrefs.PREFS_BUILD_VERBOSITY,
Messages.BuildPreferencePage_Build_Output, 1, new String[][] {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 17fc1e2..d6da31f 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.xml.AndroidManifest;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -52,16 +51,11 @@
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
* Export helper to export release version of APKs.
//Synthetic comment -- @@ -141,59 +135,7 @@
File dexFile = File.createTempFile(TEMP_PREFIX, AndroidConstants.DOT_DEX);
dexFile.deleteOnExit();

            String[] dxInput = helper.executeProguard(project, null /*resMarker*/);

IJavaProject javaProject = JavaCore.create(project);
IProject[] javaProjects = ProjectHelper.getReferencedProjects(project);
//Synthetic comment -- @@ -327,49 +269,4 @@
project.getName()));
}
}
}







