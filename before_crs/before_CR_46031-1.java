/*Make Runtime.availableProcessors report configured rather than on-line processors.

Also improve some of the Runtime documentation a little.

Change-Id:I49d28aceeb0da47a94378e1052a70704600d3417*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index a2debfd9..e42bbf2 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
import java.util.List;
import java.util.StringTokenizer;
import libcore.io.Libcore;
import static libcore.io.OsConstants._SC_NPROCESSORS_ONLN;

/**
* Allows Java applications to interface with the environment in which they are
//Synthetic comment -- @@ -90,7 +90,7 @@
/**
* Prevent this class from being instantiated.
*/
    private Runtime(){
String pathList = System.getProperty("java.library.path", ".");
String pathSep = System.getProperty("path.separator", ":");
String fileSep = System.getProperty("file.separator", "/");
//Synthetic comment -- @@ -242,14 +242,12 @@
}

/**
     * Causes the VM to stop running and the program to exit. If
     * {@link #runFinalizersOnExit(boolean)} has been previously invoked with a
* {@code true} argument, then all objects will be properly
* garbage-collected and finalized first.
     *
     * @param code
     *            the return code. By convention, non-zero return codes indicate
     *            abnormal terminations.
*/
public void exit(int code) {
// Make sure we don't try this several times
//Synthetic comment -- @@ -290,10 +288,7 @@
}

/**
     * Returns the amount of free memory resources which are available to the
     * running program.
     *
     * @return the approximate amount of free memory, measured in bytes.
*/
public native long freeMemory();

//Synthetic comment -- @@ -305,9 +300,7 @@
public native void gc();

/**
     * Returns the single {@code Runtime} instance.
     *
     * @return the {@code Runtime} object for the current application.
*/
public static Runtime getRuntime() {
return mRuntime;
//Synthetic comment -- @@ -329,7 +322,7 @@
}

/*
     * Loads and links a library without security checks.
*/
void load(String pathName, ClassLoader loader) {
if (pathName == null) {
//Synthetic comment -- @@ -356,7 +349,7 @@
}

/*
     * Loads and links a library without security checks.
*/
void loadLibrary(String libraryName, ClassLoader loader) {
if (loader != null) {
//Synthetic comment -- @@ -428,29 +421,19 @@

/**
* Returns the total amount of memory which is available to the running
     * program.
     *
     * @return the total amount of memory, measured in bytes.
*/
public native long totalMemory();

/**
* Switches the output of debug information for instructions on or off.
* On Android, this method does nothing.
     *
     * @param enable
     *            {@code true} to switch tracing on, {@code false} to switch it
     *            off.
*/
public void traceInstructions(boolean enable) {
}

/**
* Switches the output of debug information for methods on or off.
     *
     * @param enable
     *            {@code true} to switch tracing on, {@code false} to switch it
     *            off.
*/
public void traceMethodCalls(boolean enable) {
if (enable != tracingMethods) {
//Synthetic comment -- @@ -584,15 +567,10 @@
}

/**
     * Causes the VM to stop running, and the program to exit.
     * Neither shutdown hooks nor finalizers are run before.
     *
     * @param code
     *            the return code. By convention, non-zero return codes indicate
     *            abnormal terminations.
     * @see #addShutdownHook(Thread)
     * @see #removeShutdownHook(Thread)
     * @see #runFinalizersOnExit(boolean)
*/
public void halt(int code) {
// Get out of here...
//Synthetic comment -- @@ -600,10 +578,15 @@
}

/**
     * Returns the number of processors available to the VM, at least 1.
*/
public int availableProcessors() {
        return (int) Libcore.os.sysconf(_SC_NPROCESSORS_ONLN);
}

/**







