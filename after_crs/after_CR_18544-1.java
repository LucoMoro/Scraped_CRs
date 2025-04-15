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
//Synthetic comment -- index 2fde101..a304e15 100644

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
//Synthetic comment -- @@ -55,8 +57,13 @@
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
import java.io.PrintStream;
//Synthetic comment -- @@ -64,6 +71,10 @@
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
* Helper with methods for the last 3 steps of the generation of an APK.
//Synthetic comment -- @@ -90,6 +101,8 @@

private static final String CONSOLE_PREFIX_DX = "Dx"; //$NON-NLS-1$

    private final static String TEMP_PREFIX = "android_";  //$NON-NLS-1$

private final IProject mProject;
private final AndroidPrintStream mOutStream;
private final AndroidPrintStream mErrStream;
//Synthetic comment -- @@ -399,8 +412,8 @@
}

public void runProguard(File proguardConfig, File inputJar, String[] jarFiles,
            File obfuscatedJar, File logOutput)
                throws ProguardResultException, ProguardExecException, IOException {
IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);

// prepare the command line for proguard
//Synthetic comment -- @@ -449,13 +462,65 @@
command.add(new File(logOutput, "mapping.txt").getAbsolutePath()); //$NON-NLS-1$
}

        String commandArray[];

        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS) {
            // On Windows, proguard.bat can only pass %1...%9 to the java -jar proguard.jar
            // call, but we have at least 15 arguments here so some get dropped silently
            // and quoting is a big issue. So instead we'll work around that by writing
            // all the arguments to a temporary config file.

            commandArray = new String[3];

            // Arg 0 is the proguard.bat path and arg 1 is the user config file
            commandArray[0] = command.get(0);
            commandArray[1] = command.get(1);

            // Write all the other arguments to a config file
            File argsFile = File.createTempFile(TEMP_PREFIX, ".pro");           //$NON-NLS-1$
            // TODO FIXME this may leave a lot of temp files around on a long session.
            // Should have a better way to clean up e.g. before each build.
            argsFile.deleteOnExit();

            FileWriter fw = new FileWriter(argsFile);

            for (int i = 2; i < command.size(); i++) {
                String s = command.get(i);
                fw.write(s);
                fw.write(s.startsWith("-") ? ' ' : '\n');                       //$NON-NLS-1$
            }

            fw.close();

            commandArray[2] = "@" + argsFile.getAbsolutePath();                 //$NON-NLS-1$
        } else {
            // For Mac & Linux, use a regular command string array.

            commandArray = command.toArray(new String[command.size()]);
        }

        // Define PROGUARD_HOME to point to $SDK/tools/proguard if it's not yet defined.
        // The Mac/Linux proguard.sh can infer it correctly but not the proguard.bat one.
        String[] envp = null;
        Map<String, String> envMap = new TreeMap<String, String>(System.getenv());
        if (!envMap.containsKey("PROGUARD_HOME")) {                                    //$NON-NLS-1$
            envMap.put("PROGUARD_HOME",    Sdk.getCurrent().getSdkLocation() +         //$NON-NLS-1$
                                            SdkConstants.FD_TOOLS + File.separator +
                                            SdkConstants.FD_PROGUARD);
            envp = new String[envMap.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : envMap.entrySet()) {
                envp[i++] = String.format("%1$s=%2$s",                                 //$NON-NLS-1$
                                          entry.getKey(),
                                          entry.getValue());
            }
        }

// launch
int execError = 1;
try {
// launch the command line process
            Process process = Runtime.getRuntime().exec(commandArray, envp);

// list to store each line of stderr
ArrayList<String> results = new ArrayList<String>();
//Synthetic comment -- @@ -535,6 +600,144 @@
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
            if (proguardConfig != null && proguardConfig.length() > 0) {
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ExecResultException.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ExecResultException.java
//Synthetic comment -- index 37ab581..c87edd1 100644

//Synthetic comment -- @@ -44,4 +44,20 @@
public int getErrorCode() {
return mErrorCode;
}

    /**
     * Generates a message with the exception name, the error code and the captured output.
     */
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(": Error code ").append(mErrorCode);
        if (mOutput != null) {
            for (String s : mOutput) {
                sb.append("\n").append(s);
            }
        }

        return sb.toString();
    }
}








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
//Synthetic comment -- index 11210e3..2567416 100644

//Synthetic comment -- @@ -34,6 +34,8 @@

public final static String PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR = AdtPlugin.PLUGIN_ID + ".forceErrorNativeLibInJar"; //$NON-NLS-1$

    public final static String PREFS_BUILD_USE_PROGUARD = AdtPlugin.PLUGIN_ID + ".useProguard"; //$NON-NLS-1$

public final static String PREFS_BUILD_VERBOSITY = AdtPlugin.PLUGIN_ID + ".buildVerbosity"; //$NON-NLS-1$

public final static String PREFS_DEFAULT_DEBUG_KEYSTORE = AdtPlugin.PLUGIN_ID + ".defaultDebugKeyStore"; //$NON-NLS-1$
//Synthetic comment -- @@ -58,8 +60,9 @@
/** Verbosity of the build */
private BuildVerbosity mBuildVerbosity = BuildVerbosity.NORMAL;

    private boolean mBuildForceResResfresh = true;
private boolean mBuildForceErrorOnNativeLibInJar = true;
    private boolean mBuildUseProguard = false;
private float mMonitorDensity = 0.f;

public static enum BuildVerbosity {
//Synthetic comment -- @@ -136,6 +139,10 @@
mBuildForceErrorOnNativeLibInJar = mStore.getBoolean(PREFS_BUILD_RES_AUTO_REFRESH);
}

        if (property == null || PREFS_BUILD_USE_PROGUARD.equals(property)) {
            mBuildUseProguard = mStore.getBoolean(PREFS_BUILD_USE_PROGUARD);
        }

if (property == null || PREFS_MONITOR_DENSITY.equals(property)) {
mMonitorDensity = mStore.getFloat(PREFS_MONITOR_DENSITY);
}
//Synthetic comment -- @@ -161,6 +168,10 @@
return mBuildForceErrorOnNativeLibInJar;
}

    public boolean getBuildUseProguard() {
        return mBuildUseProguard;
    }

public float getMonitorDensity() {
return mMonitorDensity;
}
//Synthetic comment -- @@ -179,6 +190,7 @@

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
//Synthetic comment -- index cdfe3b3..abf6536 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.ProguardExecException;
import com.android.ide.eclipse.adt.internal.build.ProguardResultException;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -49,6 +51,7 @@
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -135,59 +138,7 @@
File dexFile = File.createTempFile(TEMP_PREFIX, AndroidConstants.DOT_DEX);
dexFile.deleteOnExit();

            String[] dxInput = helper.executeProguard(project, null /*resMarker*/);

IJavaProject javaProject = JavaCore.create(project);
IProject[] javaProjects = ProjectHelper.getReferencedProjects(project);
//Synthetic comment -- @@ -289,49 +240,4 @@
project.getName()));
}
}
}







