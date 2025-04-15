/*Improve java.lang.Thread documentation.

Change-Id:Id722186291bd9be94b64616397a979f07766c34f*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Thread.java b/luni/src/main/java/java/lang/Thread.java
//Synthetic comment -- index 210b90c..318bfaa 100644

//Synthetic comment -- @@ -41,33 +41,20 @@

/**
* A {@code Thread} is a concurrent unit of execution. It has its own call stack
 * for methods being invoked, their arguments and local variables. Each virtual
 * machine instance has at least one main {@code Thread} running when it is
 * started; typically, there are several others for housekeeping. The
 * application might decide to launch additional {@code Thread}s for specific
 * purposes.
 * <p>
 * {@code Thread}s in the same VM interact and synchronize by the use of shared
 * objects and monitors associated with these objects. Synchronized methods and
 * part of the API in {@link Object} also allow {@code Thread}s to cooperate.
 * <p>
 * There are basically two main ways of having a {@code Thread} execute
 * application code. One is providing a new class that extends {@code Thread}
 * and overriding its {@link #run()} method. The other is providing a new
 * {@code Thread} instance with a {@link Runnable} object during its creation.
 * In both cases, the {@link #start()} method must be called to actually execute
* the new {@code Thread}.
 * <p>
 * Each {@code Thread} has an integer priority that basically determines the
 * amount of CPU time the {@code Thread} gets. It can be set using the
 * {@link #setPriority(int)} method. A {@code Thread} can also be made a daemon,
 * which makes it run in the background. The latter also affects VM termination
 * behavior: the VM does not terminate automatically as long as there are
 * non-daemon threads running.
*
 * @see java.lang.Object
 * @see java.lang.ThreadGroup
 *
*/
public class Thread implements Runnable {
private static final int NANOS_PER_MILLI = 1000000;
//Synthetic comment -- @@ -117,16 +104,23 @@

/**
* The maximum priority value allowed for a thread.
*/
public static final int MAX_PRIORITY = 10;

/**
* The minimum priority value allowed for a thread.
*/
public static final int MIN_PRIORITY = 1;

/**
     * The normal (default) priority value assigned to threads.
*/
public static final int NORM_PRIORITY = 5;

//Synthetic comment -- @@ -477,13 +471,12 @@
}

/**
     * Destroys the receiver without any monitor cleanup.
     *
* @deprecated Not implemented.
*/
@Deprecated
public void destroy() {
        throw new NoSuchMethodError("Thread.destroy()"); // TODO Externalize???
}

/**
//Synthetic comment -- @@ -567,8 +560,6 @@

/**
* Returns the name of the Thread.
     *
     * @return the Thread's name
*/
public final String getName() {
return name;
//Synthetic comment -- @@ -576,9 +567,6 @@

/**
* Returns the priority of the Thread.
     *
     * @return the Thread's priority
     * @see Thread#setPriority
*/
public final int getPriority() {
return priority;
//Synthetic comment -- @@ -710,14 +698,10 @@
}

/**
     * Returns a <code>boolean</code> indicating whether the receiver is a
     * daemon Thread (<code>true</code>) or not (<code>false</code>) A
     * daemon Thread only runs as long as there are non-daemon Threads running.
     * When the last non-daemon Thread ends, the whole program ends no matter if
     * it had daemon Threads still running or not.
     *
     * @return a <code>boolean</code> indicating whether the Thread is a daemon
     * @see Thread#setDaemon
*/
public final boolean isDaemon() {
return daemon;
//Synthetic comment -- @@ -836,9 +820,7 @@

/**
* Throws {@code UnsupportedOperationException}.
     *
     * @see Thread#suspend()
     * @deprecated Used with deprecated method {@link Thread#suspend}
*/
@Deprecated
public final void resume() {
//Synthetic comment -- @@ -868,23 +850,25 @@
}

/**
     * Set if the receiver is a daemon Thread or not. This can only be done
     * before the Thread starts running.
     *
     * @param isDaemon
     *            indicates whether the Thread should be daemon or not
     * @see Thread#isDaemon
*/
public final void setDaemon(boolean isDaemon) {
        if (hasBeenStarted) {
            throw new IllegalThreadStateException("Thread already started."); // TODO Externalize?
        }

if (vmThread == null) {
daemon = isDaemon;
}
}

/**
* Sets the default uncaught exception handler. This handler is invoked in
* case any Thread dies due to an unhandled exception.
//Synthetic comment -- @@ -955,21 +939,16 @@
}

/**
     * Sets the priority of the Thread. Note that the final priority set may not
     * be the parameter that was passed - it will depend on the receiver's
     * ThreadGroup. The priority cannot be set to be higher than the receiver's
     * ThreadGroup's maxPriority().
*
     * @param priority
     *            new priority for the Thread
     * @throws IllegalArgumentException
     *             if the new priority is greater than Thread.MAX_PRIORITY or
     *             less than Thread.MIN_PRIORITY
     * @see Thread#getPriority
*/
public final void setPriority(int priority) {
if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("Priority out of range"); // TODO Externalize?
}

if (priority > group.getMaxPriority()) {
//Synthetic comment -- @@ -1036,14 +1015,11 @@
* the receiver will be called by the receiver Thread itself (and not the
* Thread calling <code>start()</code>).
*
     * @throws IllegalThreadStateException if the Thread has been started before
     *
* @see Thread#run
*/
public synchronized void start() {
        if (hasBeenStarted) {
            throw new IllegalThreadStateException("Thread already started."); // TODO Externalize?
        }

hasBeenStarted = true;

//Synthetic comment -- @@ -1065,9 +1041,6 @@

/**
* Throws {@code UnsupportedOperationException}.
     *
     * @throws NullPointerException if <code>throwable()</code> is
     *         <code>null</code>
* @deprecated because stopping a thread in this manner is unsafe and can
* leave your application and the VM in an unpredictable state.
*/
//Synthetic comment -- @@ -1078,8 +1051,6 @@

/**
* Throws {@code UnsupportedOperationException}.
     *
     * @see Thread#resume()
* @deprecated May cause deadlocks.
*/
@Deprecated







