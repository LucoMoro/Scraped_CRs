/*Added Benchmarking code

Added benchmarks to the ADT plugin's builders.
Benchmarking is disabled by default and can be
Enabled by setting the BENCHMARK_FLAG  in
BuildHelper.java

Patched: Replaced tabs with spaces and removed trailing
spaces. Changed naming to be consistent.
Signed-off-by: Josiah Gaskin <josiahgaskin@google.com>

Change-Id:I2b855b627f8970f2a18a2ad1899c2b54b22f242a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5aa9647..8e6cbd6 100644

//Synthetic comment -- @@ -102,6 +102,10 @@
private final boolean mVerbose;
private final boolean mDebugMode;

    public static final boolean BENCHMARK_FLAG = false;
    public static long sStartOverallTime = 0;
    public static long sStartJavaCTime = 0;

/**
* An object able to put a marker on a resource.
*/
//Synthetic comment -- @@ -127,7 +131,7 @@
}

/**
     * Packages the resources of the project into a .ap_ file.
* @param manifestFile the manifest of the project.
* @param libProjects the list of library projects that this project depends on.
* @param resFilter an optional resource filter to be used with the -c option of aapt. If null
//Synthetic comment -- @@ -142,6 +146,15 @@
public void packageResources(IFile manifestFile, List<IProject> libProjects, String resFilter,
int versionCode, String outputFolder, String outputFilename)
throws AaptExecException, AaptResultException {
        
        // Benchmarking start
        long startPackageTime = 0;
        if (BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Starting Initial Packaging (.ap_)";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, mProject, msg);
            startPackageTime = System.nanoTime();
        }
        
// need to figure out some path before we can execute aapt;

// get the resource folder
//Synthetic comment -- @@ -185,6 +198,13 @@
outputFolder + File.separator + outputFilename, resFilter,
versionCode);
}
        
        // Benchmarking end
        if (BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Ending Initial Package (.ap_). \nTime Elapsed: "
                            + ((System.nanoTime() - startPackageTime)/Math.pow(10, 6)) + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, mProject, msg);
        }
}

/**
//Synthetic comment -- @@ -362,6 +382,8 @@
} catch (SealedApkException e) {
// this won't happen as we control when the apk is sealed.
}
        
        
}

public String[] getProjectOutputs() throws CoreException {
//Synthetic comment -- @@ -754,6 +776,14 @@
AdtPlugin.printToConsole(mProject, sb.toString());
}

        // Benchmarking start
        long startAaptTime = 0;
        if (BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Starting PostCompilation call to Aapt";
            mOutStream.println(msg);
            startAaptTime = System.nanoTime();
        }
        
// launch
int execError = 1;
try {
//Synthetic comment -- @@ -782,6 +812,12 @@
String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
throw new AaptExecException(msg, e);
}
        
        // Benchmarking end
        if (BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Ending PostCompilation call to Aapt. \nBENCHMARK ADT: Time Elapsed: " + ((System.nanoTime() - startAaptTime)/Math.pow(10, 6)) + "ms";
            mOutStream.println(msg);
        }
}

/**
//Synthetic comment -- @@ -1085,8 +1121,16 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            // If benchmarking always print the lines that
                            // correspond to benchmarking info returned by ADT
                            if(BENCHMARK_FLAG && line.startsWith("BENCHMARK:")) {
                                AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS,
                                        project, line);
                            }
                            else {
                                AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                        project, line);
                            }
} else {
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 293b340..5d61161 100644

//Synthetic comment -- @@ -52,8 +52,7 @@
* error checking and marking the files for errors/warnings.
*/
public abstract class BaseBuilder extends IncrementalProjectBuilder {
    
/** SAX Parser factory. */
private SAXParserFactory mParserFactory;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 71c38f8..0ef9b1a 100644

//Synthetic comment -- @@ -208,6 +208,20 @@
// get a project object
IProject project = getProject();

        // Benchmarking start
        long startBuildTime = 0;
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            // End JavaC Timer
            String msg = "BENCHMARK ADT: Ending Compilation \n BENCHMARK ADT: Time Elapsed: " + 
                         (System.nanoTime() - 
                          com.android.ide.eclipse.adt.internal.build.BuildHelper.sStartJavaCTime)/Math.pow(10, 6)
                          + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            msg = "BENCHMARK ADT: Starting PostCompilation";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            startBuildTime = System.nanoTime();
        }
        
// list of referenced projects. This is a mix of java projects and library projects
// and is computed below.
IProject[] allRefProjects = null;
//Synthetic comment -- @@ -621,6 +635,18 @@
markProject(AdtConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
}

        // Benchmarking end
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Ending PostCompilation. \n BENCHMARK ADT: Time Elapsed: " + ((System.nanoTime() - startBuildTime)/Math.pow(10, 6)) + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            // End Overall Timer
            msg = "BENCHMARK ADT: Done with everything! \n BENCHMARK ADT: Time Elapsed: " + 
                         (System.nanoTime() - 
                          com.android.ide.eclipse.adt.internal.build.BuildHelper.sStartOverallTime)/Math.pow(10, 6)
                          + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
        }
        
return allRefProjects;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..44fbfa9 100644

//Synthetic comment -- @@ -187,8 +187,21 @@
@Override
protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
throws CoreException {
        
// get a project object
IProject project = getProject();
        
        // Benchmarking start
        long startBuildTime = 0;
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Starting PreCompilation";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            // Start the overall timer
            com.android.ide.eclipse.adt.internal.build.BuildHelper.sStartOverallTime = System.nanoTime();
            // Start the precompiler timer
            startBuildTime = System.nanoTime();
        }
        

// For the PreCompiler, only the library projects are considered Referenced projects,
// as only those projects have an impact on what is generated by this builder.
//Synthetic comment -- @@ -507,6 +520,16 @@
mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, mDerivedProgressMonitor);
}

        // Benchmarking end
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Ending PreCompilation. \nBENCHMARK ADT: Time Elapsed: " + ((System.nanoTime() - startBuildTime)/Math.pow(10, 6)) + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            //Start JavaC Timer
            msg = "BENCHMARK ADT: Starting Compilation";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            com.android.ide.eclipse.adt.internal.build.BuildHelper.sStartJavaCTime = System.nanoTime();
        }
        
return result;
}

//Synthetic comment -- @@ -714,6 +737,14 @@
AdtPlugin.printToConsole(project, cmd_line);
}

        // Benchmarking start
        long startAaptTime = 0;
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Starting PreCompilation call to Aapt";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
            startAaptTime = System.nanoTime();
        }
        
// launch
int execError = 1;
try {
//Synthetic comment -- @@ -772,6 +803,13 @@
// This interrupts the build.
throw new AbortBuildException();
}
        
        // Benchmarking end
        if (com.android.ide.eclipse.adt.internal.build.BuildHelper.BENCHMARK_FLAG) {
            String msg = "BENCHMARK ADT: Ending PreCompilation call to Aapt. \n BENCHMARK ADT: Time Elapsed: " 
                           + ((System.nanoTime() - startAaptTime)/Math.pow(10, 6)) + "ms";
            AdtPlugin.printBuildToConsole(BuildVerbosity.ALWAYS, project, msg);
        }

// if the return code was OK, we refresh the folder that
// contains R.java to force a java recompile.







