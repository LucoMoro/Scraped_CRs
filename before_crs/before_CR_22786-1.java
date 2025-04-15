/*Fix space-in-path proguard for ADT Export.

Change-Id:I3f9bd2378f95e6f2114987d84842c0e231927629*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 1d6bad3..1d527e4 100644

//Synthetic comment -- @@ -59,6 +59,9 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//Synthetic comment -- @@ -66,6 +69,7 @@
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
//Synthetic comment -- @@ -427,29 +431,29 @@
List<String> command = new ArrayList<String>();
command.add(AdtPlugin.getOsAbsoluteProguard());

        command.add("@" + proguardConfig.getAbsolutePath()); //$NON-NLS-1$

command.add("-injars"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder(inputJar.getAbsolutePath());
for (String jarFile : jarFiles) {
sb.append(File.pathSeparatorChar);
            sb.append(jarFile);
}
        command.add(sb.toString());

command.add("-outjars"); //$NON-NLS-1$
        command.add(obfuscatedJar.getAbsolutePath());

command.add("-libraryjars"); //$NON-NLS-1$
        sb = new StringBuilder(target.getPath(IAndroidTarget.ANDROID_JAR));
IOptionalLibrary[] libraries = target.getOptionalLibraries();
if (libraries != null) {
for (IOptionalLibrary lib : libraries) {
sb.append(File.pathSeparatorChar);
                sb.append(lib.getJarPath());
}
}
        command.add(sb.toString());

if (logOutput != null) {
if (logOutput.isDirectory() == false) {
//Synthetic comment -- @@ -469,40 +473,14 @@
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

//Synthetic comment -- @@ -523,6 +501,14 @@
}
}

// launch
int execError = 1;
try {
//Synthetic comment -- @@ -555,6 +541,91 @@
}
}


/**
* Execute the Dx tool for dalvik code conversion.







