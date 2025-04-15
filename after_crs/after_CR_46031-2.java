/*Make Runtime.availableProcessors report configured rather than on-line processors.

Also improve some of the Runtime documentation a little.

Change-Id:I49d28aceeb0da47a94378e1052a70704600d3417*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index a2debfd9..c4c12db 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
import java.util.List;
import java.util.StringTokenizer;
import libcore.io.Libcore;
import static libcore.io.OsConstants._SC_NPROCESSORS_CONF;

/**
* Allows Java applications to interface with the environment in which they are
//Synthetic comment -- @@ -90,7 +90,7 @@
/**
* Prevent this class from being instantiated.
*/
    private Runtime() {
String pathList = System.getProperty("java.library.path", ".");
String pathSep = System.getProperty("path.separator", ":");
String fileSep = System.getProperty("file.separator", "/");
//Synthetic comment -- @@ -242,14 +242,12 @@
}

/**
     * Causes the VM to stop running and the program to exit.
     * If {@link #runFinalizersOnExit(boolean)} has been previously invoked with a
* {@code true} argument, then all objects will be properly
* garbage-collected and finalized first.
     * Use 0 to signal success to the calling process and 1 to signal failure.
     * This method is unlikely to be useful to an Android application.
*/
public void exit(int code) {
// Make sure we don't try this several times
//Synthetic comment -- @@ -290,10 +288,7 @@
}

/**
     * Returns the amount of free memory available to the running program, in bytes.
*/
public native long freeMemory();

//Synthetic comment -- @@ -305,9 +300,7 @@
public native void gc();

/**
     * Returns the single {@code Runtime} instance for the current application.
*/
public static Runtime getRuntime() {
return mRuntime;
//Synthetic comment -- @@ -329,7 +322,7 @@
}

/*
     * Loads and links the given library without security checks.
*/
void load(String pathName, ClassLoader loader) {
if (pathName == null) {
//Synthetic comment -- @@ -356,7 +349,7 @@
}

/*
     * Searches for a library, then loads and links it without security checks.
*/
void loadLibrary(String libraryName, ClassLoader loader) {
if (loader != null) {
//Synthetic comment -- @@ -428,29 +421,19 @@

/**
* Returns the total amount of memory which is available to the running
     * program, in bytes.
*/
public native long totalMemory();

/**
* Switches the output of debug information for instructions on or off.
* On Android, this method does nothing.
*/
public void traceInstructions(boolean enable) {
}

/**
* Switches the output of debug information for methods on or off.
*/
public void traceMethodCalls(boolean enable) {
if (enable != tracingMethods) {
//Synthetic comment -- @@ -584,15 +567,10 @@
}

/**
     * Causes the VM to stop running, and the program to exit with the given return code.
     * Use 0 to signal success to the calling process and 1 to signal failure.
     * Neither shutdown hooks nor finalizers are run before exiting.
     * This method is unlikely to be useful to an Android application.
*/
public void halt(int code) {
// Get out of here...
//Synthetic comment -- @@ -600,10 +578,15 @@
}

/**
     * Returns the number of processor cores available to the VM, at least 1.
     * Traditionally this returned the number currently online,
     * but many mobile devices are able to take unused cores offline to
     * save power, so releases newer than Android 4.2 (Jelly Bean) return the maximum number of
     * cores that could be made available if there were no power or heat
     * constraints.
*/
public int availableProcessors() {
        return (int) Libcore.os.sysconf(_SC_NPROCESSORS_CONF);
}

/**







