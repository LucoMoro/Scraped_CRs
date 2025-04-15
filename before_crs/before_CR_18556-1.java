/*ADT: fixes to make Proguard work under Windows.

- the proguard.bat can only pass 9 args, but we have 15+, so
  we'll use a temporary proguard config file for Windows.

- PROGUARD_HOME needs to be defined properly since the default
  proguard.bat cannot infer it properly. We need other fixes
  to that batch file so eventually we're not going to ship the
  default one and we can fix the .bat file, but properly defining
  this env var is probably a good idea anyway.

Change-Id:Ic35b6b27580ed10c831d489d2b26759cef9633ca*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 2fde101..5047e11 100644

//Synthetic comment -- @@ -57,6 +57,7 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//Synthetic comment -- @@ -64,6 +65,8 @@
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
* Helper with methods for the last 3 steps of the generation of an APK.
//Synthetic comment -- @@ -88,7 +91,8 @@
*/
public class BuildHelper {

    private static final String CONSOLE_PREFIX_DX = "Dx"; //$NON-NLS-1$

private final IProject mProject;
private final AndroidPrintStream mOutStream;
//Synthetic comment -- @@ -399,8 +403,8 @@
}

public void runProguard(File proguardConfig, File inputJar, String[] jarFiles,
            File obfuscatedJar, File logOutput) throws ProguardResultException,
            ProguardExecException {
IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);

// prepare the command line for proguard
//Synthetic comment -- @@ -449,13 +453,65 @@
command.add(new File(logOutput, "mapping.txt").getAbsolutePath()); //$NON-NLS-1$
}

        String commandArray[] = command.toArray(new String[command.size()]);

// launch
int execError = 1;
try {
// launch the command line process
            Process process = Runtime.getRuntime().exec(commandArray);

// list to store each line of stderr
ArrayList<String> results = new ArrayList<String>();







