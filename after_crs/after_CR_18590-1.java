/*ADT: fixes to make Proguard work under Windows.

- the proguard.bat can only pass 9 args, but we have 15+, so
  we'll use a temporary proguard config file for Windows.

- PROGUARD_HOME needs to be defined properly since the default
  proguard.bat cannot infer it properly. We need other fixes
  to that batch file so eventually we're not going to ship the
  default one and we can fix the .bat file, but properly defining
  this env var is probably a good idea anyway.

Change-Id:Id007d5504a5e05d1c4b5a0728e96e0d677978ef4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 2fde101..5047e11 100644

//Synthetic comment -- @@ -57,6 +57,7 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//Synthetic comment -- @@ -64,6 +65,8 @@
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* Helper with methods for the last 3 steps of the generation of an APK.
//Synthetic comment -- @@ -88,7 +91,8 @@
*/
public class BuildHelper {

    private static final String CONSOLE_PREFIX_DX = "Dx";   //$NON-NLS-1$
    private final static String TEMP_PREFIX = "android_";   //$NON-NLS-1$

private final IProject mProject;
private final AndroidPrintStream mOutStream;
//Synthetic comment -- @@ -399,8 +403,8 @@
}

public void runProguard(File proguardConfig, File inputJar, String[] jarFiles,
                            File obfuscatedJar, File logOutput)
            throws ProguardResultException, ProguardExecException, IOException {
IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);

// prepare the command line for proguard
//Synthetic comment -- @@ -449,13 +453,65 @@
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







