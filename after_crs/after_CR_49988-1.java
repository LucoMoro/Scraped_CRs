/*Fix typos.

Change-Id:I6a9f7d8203c91f854fef64c9acd0e0b04459724b*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/profiler/SamplingProfiler.java b/dalvik/src/main/java/dalvik/system/profiler/SamplingProfiler.java
//Synthetic comment -- index 0266103..744977c 100644

//Synthetic comment -- @@ -74,7 +74,7 @@

/**
* A sampler is created every time profiling starts and cleared
     * every time profiling stops because once a {@code TimerTask} is
* canceled it cannot be reused.
*/
private Sampler sampler;
//Synthetic comment -- @@ -94,7 +94,7 @@
*  Real hprof output examples don't start the thread and trace
*  identifiers at one but seem to start at these arbitrary
*  constants. It certainly seems useful to have relatively unique
     *  identifiers when manual searching hprof output.
*/
private int nextThreadId = 200001;
private int nextStackTraceId = 300001;
//Synthetic comment -- @@ -146,9 +146,9 @@
*
* @param threadSet The thread set specifies which threads to
* sample. In a general purpose program, all threads typically
     * should be sample with a ThreadSet such as provided by {@link
     * #newThreadGroupThreadSet newThreadGroupThreadSet}. For a
     * benchmark a fixed set such as provided by {@link
* #newArrayThreadSet newArrayThreadSet} can reduce the overhead
* of profiling.
*/
//Synthetic comment -- @@ -188,7 +188,7 @@
/**
* Returns a ThreadSet for a fixed set of threads that will not
* vary at runtime. This has less overhead than a dynamically
     * calculated set, such as {@link #newThreadGroupThreadSet}, which has
* to enumerate the threads each time profiler wants to collect
* samples.
*/
//Synthetic comment -- @@ -218,7 +218,7 @@
* threads found in the specified ThreadGroup and that
* ThreadGroup's children.
*/
    public static ThreadSet newThreadGroupThreadSet(ThreadGroup threadGroup) {
return new ThreadGroupThreadSet(threadGroup);
}








