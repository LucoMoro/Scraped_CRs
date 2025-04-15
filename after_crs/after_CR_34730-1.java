/*statistics from some cpus don't fit

Cpu statistics from time_in_state does not fit
in the data structures in ProcessState.java. The
time_in_state file eventually becomes larger than
the buffer of 256 bytes witch cause missing data
in the result or a NoMoreElementsException when
parsing the data.
The numger of cpu speeds can be larger than the
MAX_SPEEDS limit.

Solution
Make sure that the whole file gets read and increase
the MAX_SPEEDS that the system can handle.

Change-Id:I6dfc127b2934988006649cce321d58750a946017*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ProcessStats.java b/core/java/com/android/internal/os/ProcessStats.java
//Synthetic comment -- index e0e9a29..9490437 100644

//Synthetic comment -- @@ -154,7 +154,7 @@

private boolean mFirst = true;

    private byte[] mBuffer = new byte[4096];

/**
* The time in microseconds that the CPU has been running at each speed.
//Synthetic comment -- @@ -556,7 +556,7 @@
private long[] getCpuSpeedTimes(long[] out) {
long[] tempTimes = out;
long[] tempSpeeds = mCpuSpeeds;
        final int MAX_SPEEDS = 60;
if (out == null) {
tempTimes = new long[MAX_SPEEDS]; // Hopefully no more than that
tempSpeeds = new long[MAX_SPEEDS];







