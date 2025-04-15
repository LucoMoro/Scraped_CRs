/*Improve java.lang.Thread documentation.

Change-Id:Id722186291bd9be94b64616397a979f07766c34f*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Thread.java b/luni/src/main/java/java/lang/Thread.java
//Synthetic comment -- index 210b90c..4e6d204 100644

//Synthetic comment -- @@ -41,33 +41,23 @@

/**
* A {@code Thread} is a concurrent unit of execution. It has its own call stack
 * for methods being invoked, their arguments and local variables. Each application
 * has at least one thread running when it is started, the main thread, in the main
 * {@link ThreadGroup}. The runtime keeps its own threads in the system thread
 * group.
 *
 * <p>Threads interact and synchronize by the use of shared objects and the monitors
 * associated with these objects.
 *
 * <p>There are two ways to execute code in a new thread.
 * You can either subclass {@code Thread} and overriding its {@link #run()} method,
 * or construct a new {@code Thread} and pass a {@link Runnable} to the constructor.
 * In either case, the {@link #start()} method must be called to actually execute
* the new {@code Thread}.
*
 * <p>Each {@code Thread} has an integer priority that affect how the thread is
 * scheduled by the OS. A new thread inherits the priority of its parent.
 * A thread's priority can be set using the {@link #setPriority(int)} method.
*/
public class Thread implements Runnable {
private static final int NANOS_PER_MILLI = 1000000;
//Synthetic comment -- @@ -117,16 +107,23 @@

/**
* The maximum priority value allowed for a thread.
     * This corresponds to (but does not have the same value as)
     * {@code android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY}.
*/
public static final int MAX_PRIORITY = 10;

/**
* The minimum priority value allowed for a thread.
     * This corresponds to (but does not have the same value as)
     * {@code android.os.Process.THREAD_PRIORITY_LOWEST}.
*/
public static final int MIN_PRIORITY = 1;

/**
     * The normal (default) priority value assigned to the main thread.
     * This corresponds to (but does not have the same value as)
     * {@code android.os.Process.THREAD_PRIORITY_DEFAULT}.

*/
public static final int NORM_PRIORITY = 5;

//Synthetic comment -- @@ -567,8 +564,6 @@

/**
* Returns the name of the Thread.
*/
public final String getName() {
return name;
//Synthetic comment -- @@ -576,9 +571,6 @@

/**
* Returns the priority of the Thread.
*/
public final int getPriority() {
return priority;
//Synthetic comment -- @@ -710,14 +702,10 @@
}

/**
     * Tests whether this is a daemon thread.
     * A daemon thread only runs as long as there are non-daemon threads running.
     * When the last non-daemon thread ends, the runtime will exit. This is not
     * normally relevant to applications with a UI.
*/
public final boolean isDaemon() {
return daemon;
//Synthetic comment -- @@ -868,16 +856,15 @@
}

/**
     * Marks this thread as a daemon thread.
     * A daemon thread only runs as long as there are non-daemon threads running.
     * When the last non-daemon thread ends, the runtime will exit. This is not
     * normally relevant to applications with a UI.
     * @throws IllegalThreadStateException - if this thread has already started.
*/
public final void setDaemon(boolean isDaemon) {
if (hasBeenStarted) {
            throw new IllegalThreadStateException("Thread already started");
}

if (vmThread == null) {
//Synthetic comment -- @@ -955,21 +942,16 @@
}

/**
     * Sets the priority of this thread. If the requested priority is greater than the
     * parent thread group's {@link java.lang.ThreadGroup#getMaxPriority}, the group's maximum
     * priority will be used instead.
*
     * @throws IllegalArgumentException - if the new priority is greater than {@link #MAX_PRIORITY}
     *     or less than {@link #MIN_PRIORITY}
*/
public final void setPriority(int priority) {
if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("Priority out of range: " + priority);
}

if (priority > group.getMaxPriority()) {







