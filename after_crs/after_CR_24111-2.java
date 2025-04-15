/*Add support for tracking cpu and real time.

Enhanced TraceView to support extracting cpu and real time profile
data from traces and plotting them accordingly.

In contrast, the original thread-cpu algorithm incorrectly assumed
that threads could not execute in parallel so it would produce
inaccurate representations of the global execution timeline on SMP
systems.

To use this feature, Dalvik must be restarted with appropriate
profiling options.

Collect CPU time only (default).  Global timeline must be inferred.
  adb shell setprop dalvik.vm.extra-opts -Xprofile:threadcpuclock

Collect real time only.  CPU usage information is not available.
  adb shell setprop dalvik.vm.extra-opts -Xprofile:wallclock

Collect CPU time and real time information.  Most accurate but
requires a newer VM.
  adb shell setprop dalvik.vm.extra-opts -Xprofile:dualclock

TraceView remains compatible with traces gathered on older devices
and produces identical output when analyzing traces gathered with
the thread-cpu clock.

Added a feature that displays the exclusive and inclusive time
information while hovering over a method.

Added a feature that displays the time spent in a context switch
by a simple 1 pixel underline within the body of the method call.
This makes it much easier to identify long running methods that
spend most of their time blocked on a wait.

Fixed a bug in the way that the method call highlights were
displayed which could cause them to be truncated down to a
single-pixel width depending on the pixel alignment of the
start and end of each segment.

Change-Id:Id2e6d101d9c604a0029e0920127871f88013faa0*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/Call.java b/traceview/src/com/android/traceview/Call.java
//Synthetic comment -- index 40ac244..53f16cb 100644

//Synthetic comment -- @@ -19,52 +19,30 @@
import org.eclipse.swt.graphics.Color;

class Call implements TimeLineView.Block {
    final private ThreadData mThreadData;
    final private MethodData mMethodData;
    final Call mCaller; // the caller, or null if this is the root

private String mName;
    private boolean mIsRecursive;

    long mGlobalStartTime;
    long mGlobalEndTime;

    long mThreadStartTime;
    long mThreadEndTime;

    long mInclusiveRealTime; // real time spent in this call including its children
    long mExclusiveRealTime; // real time spent in this call including its children

    long mInclusiveCpuTime; // cpu time spent in this call including its children
    long mExclusiveCpuTime; // cpu time spent in this call excluding its children

    Call(ThreadData threadData, MethodData methodData, Call caller) {
        mThreadData = threadData;
mMethodData = methodData;
mName = methodData.getProfileName();
        mCaller = caller;
}

public void updateName() {
//Synthetic comment -- @@ -87,22 +65,26 @@
return mGlobalEndTime;
}

    public long getExclusiveCpuTime() {
        return mExclusiveCpuTime;
    }

    public long getInclusiveCpuTime() {
        return mInclusiveCpuTime;
    }

    public long getExclusiveRealTime() {
        return mExclusiveRealTime;
    }

    public long getInclusiveRealTime() {
        return mInclusiveRealTime;
    }

public Color getColor() {
return mMethodData.getColor();
}

public String getName() {
return mName;
}
//Synthetic comment -- @@ -111,31 +93,71 @@
mName = name;
}

    public ThreadData getThreadData() {
        return mThreadData;
    }

    public int getThreadId() {
        return mThreadData.getId();
}

public MethodData getMethodData() {
return mMethodData;
}

    public boolean isContextSwitch() {
        return mMethodData.getId() < 0;
}

    public boolean isIgnoredBlock() {
        // Ignore the top-level call or context switches within the top-level call.
        return mCaller == null || isContextSwitch() && mCaller.mCaller == null;
}

    public TimeLineView.Block getParentBlock() {
        return mCaller;
}

public boolean isRecursive() {
        return mIsRecursive;
    }

    void setRecursive(boolean isRecursive) {
        mIsRecursive = isRecursive;
    }

    void addCpuTime(long elapsedCpuTime) {
        mExclusiveCpuTime += elapsedCpuTime;
        mInclusiveCpuTime += elapsedCpuTime;
    }

    /**
     * Record time spent in the method call.
     */
    void finish() {
        if (mCaller != null) {
            mCaller.mInclusiveCpuTime += mInclusiveCpuTime;
            mCaller.mInclusiveRealTime += mInclusiveRealTime;
        }

        mMethodData.addElapsedExclusive(mExclusiveCpuTime, mExclusiveRealTime);
        if (!mIsRecursive) {
            mMethodData.addTopExclusive(mExclusiveCpuTime, mExclusiveRealTime);
        }
        mMethodData.addElapsedInclusive(mInclusiveCpuTime, mInclusiveRealTime,
                mIsRecursive, mCaller);
    }

    public static final class TraceAction {
        public static final int ACTION_ENTER = 0;
        public static final int ACTION_EXIT = 1;

        public final int mAction;
        public final Call mCall;

        public TraceAction(int action, Call call) {
            mAction = action;
            mCall = call;
        }
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index dcf17a2..fbcd13e 100644

//Synthetic comment -- @@ -35,25 +35,37 @@
import java.util.regex.Pattern;

public class DmTraceReader extends TraceReader {
private static final int TRACE_MAGIC = 0x574f4c53;

    private static final int METHOD_TRACE_ENTER = 0x00; // method entry
    private static final int METHOD_TRACE_EXIT = 0x01; // method exit
    private static final int METHOD_TRACE_UNROLL = 0x02; // method exited by exception unrolling

    // When in dual clock mode, we report that a context switch has occurred
    // when skew between the real time and thread cpu clocks is more than this
    // many microseconds.
    private static final long MIN_CONTEXT_SWITCH_TIME_USEC = 100;

    private enum ClockSource {
        THREAD_CPU, WALL, DUAL,
    };

    private int mVersionNumber;
private boolean mRegression;
private ProfileProvider mProfileProvider;
private String mTraceFileName;
private MethodData mTopLevel;
private ArrayList<Call> mCallList;
private HashMap<String, String> mPropertiesMap;
private HashMap<Integer, MethodData> mMethodMap;
private HashMap<Integer, ThreadData> mThreadMap;
private ThreadData[] mSortedThreads;
private MethodData[] mSortedMethods;
    private long mTotalCpuTime;
    private long mTotalRealTime;
private MethodData mContextSwitch;
    private int mRecordSize;
    private ClockSource mClockSource;

// A regex for matching the thread "id name" lines in the .key file
private static final Pattern mIdNamePattern = Pattern.compile("(\\d+)\t(.*)");  //$NON-NLS-1$
//Synthetic comment -- @@ -64,14 +76,15 @@
mPropertiesMap = new HashMap<String, String>();
mMethodMap = new HashMap<Integer, MethodData>();
mThreadMap = new HashMap<Integer, ThreadData>();
        mCallList = new ArrayList<Call>();

// Create a single top-level MethodData object to hold the profile data
// for time spent in the unknown caller.
mTopLevel = new MethodData(0, "(toplevel)");
mContextSwitch = new MethodData(-1, "(context switch)");
mMethodMap.put(0, mTopLevel);
        mMethodMap.put(-1, mContextSwitch);
generateTrees();
}

void generateTrees() {
//Synthetic comment -- @@ -92,38 +105,6 @@
return mProfileProvider;
}

private MappedByteBuffer mapFile(String filename, long offset) {
MappedByteBuffer buffer = null;
try {
//Synthetic comment -- @@ -151,165 +132,279 @@
magic, TRACE_MAGIC);
throw new RuntimeException();
}

// read version
int version = buffer.getShort();
        if (version != mVersionNumber) {
            System.err.printf(
                    "Error: version number mismatch; got %d in data header but %d in options\n",
                    version, mVersionNumber);
            throw new RuntimeException();
        }
        if (version < 1 || version > 3) {
            System.err.printf(
                    "Error: unsupported trace version number %d.  "
                    + "Please use a newer version of TraceView to read this file.", version);
            throw new RuntimeException();
        }

// read offset
        int offsetToData = buffer.getShort() - 16;

// read startWhen
buffer.getLong();

        // read record size
        if (version == 1) {
            mRecordSize = 9;
        } else if (version == 2) {
            mRecordSize = 10;
        } else {
            mRecordSize = buffer.getShort();
            offsetToData -= 2;
        }

        // Skip over offsetToData bytes
        while (offsetToData-- > 0) {
buffer.get();
}
}

private void parseData(long offset) {
MappedByteBuffer buffer = mapFile(mTraceFileName, offset);
readDataFileHeader(buffer);

        ArrayList<TraceAction> trace = null;
        if (mClockSource == ClockSource.THREAD_CPU) {
            trace = new ArrayList<TraceAction>();
}

        final boolean haveThreadClock = mClockSource != ClockSource.WALL;
        final boolean haveGlobalClock = mClockSource != ClockSource.THREAD_CPU;

        // Parse all call records to obtain elapsed time information.
        ThreadData prevThreadData = null;
        for (;;) {
            int threadId;
            int methodId;
            long threadTime, globalTime;
            try {
                int recordSize = mRecordSize;

                if (mVersionNumber == 1) {
                    threadId = buffer.get();
                    recordSize -= 1;
                } else {
                    threadId = buffer.getShort();
                    recordSize -= 2;
                }

                methodId = buffer.getInt();
                recordSize -= 4;

                switch (mClockSource) {
                    case WALL:
                        threadTime = 0;
                        globalTime = buffer.getInt();
                        recordSize -= 4;
                        break;
                    case DUAL:
                        threadTime = buffer.getInt();
                        globalTime = buffer.getInt();
                        recordSize -= 8;
                        break;
                    default:
                    case THREAD_CPU:
                        threadTime = buffer.getInt();
                        globalTime = 0;
                        recordSize -= 4;
                        break;
                }

                while (recordSize-- > 0) {
                    buffer.get();
                }
            } catch (BufferUnderflowException ex) {
break;
}

            int methodAction = methodId & 0x03;
            methodId = methodId & ~0x03;
            MethodData methodData = mMethodMap.get(methodId);
            if (methodData == null) {
                String name = String.format("(0x%1$x)", methodId);  //$NON-NLS-1$
                methodData = new MethodData(methodId, name);
                mMethodMap.put(methodId, methodData);
            }

            ThreadData threadData = mThreadMap.get(threadId);
if (threadData == null) {
                String name = String.format("[%1$d]", threadId);  //$NON-NLS-1$
                threadData = new ThreadData(threadId, name, mTopLevel);
                mThreadMap.put(threadId, threadData);
}

            long elapsedGlobalTime = 0;
            if (haveGlobalClock) {
                if (!threadData.mHaveGlobalTime) {
                    threadData.mGlobalStartTime = globalTime;
                    threadData.mHaveGlobalTime = true;
                } else {
                    elapsedGlobalTime = globalTime - threadData.mGlobalEndTime;
                }
                threadData.mGlobalEndTime = globalTime;
            }

            if (haveThreadClock) {
                long elapsedThreadTime = 0;
                if (!threadData.mHaveThreadTime) {
                    threadData.mThreadStartTime = threadTime;
                    threadData.mThreadCurrentTime = threadTime;
                    threadData.mHaveThreadTime = true;
                } else {
                    elapsedThreadTime = threadTime - threadData.mThreadEndTime;
                }
                threadData.mThreadEndTime = threadTime;

                if (!haveGlobalClock) {
                    // Detect context switches whenever execution appears to switch from one
                    // thread to another.  This assumption is only valid on uniprocessor
                    // systems (which is why we now have a dual clock mode).
                    // We represent context switches in the trace by pushing a call record
                    // with MethodData mContextSwitch onto the stack of the previous
                    // thread.  We arbitrarily set the start and end time of the context
                    // switch such that the context switch occurs in the middle of the thread
                    // time and itself accounts for zero thread time.
                    if (prevThreadData != null && prevThreadData != threadData) {
                        // Begin context switch from previous thread.
                        Call switchCall = prevThreadData.enter(mContextSwitch, trace);
                        switchCall.mThreadStartTime = prevThreadData.mThreadEndTime;
                        mCallList.add(switchCall);

                        // Return from context switch to current thread.
                        Call top = threadData.top();
                        if (top.getMethodData() == mContextSwitch) {
                            threadData.exit(mContextSwitch, trace);
                            long beforeSwitch = elapsedThreadTime / 2;
                            top.mThreadStartTime += beforeSwitch;
                            top.mThreadEndTime = top.mThreadStartTime;
                        }
                    }
                    prevThreadData = threadData;
                } else {
                    // If we have a global clock, then we can detect context switches (or blocking
                    // calls or cpu suspensions or clock anomalies) by comparing global time to
                    // thread time for successive calls that occur on the same thread.
                    // As above, we represent the context switch using a special method call.
                    long sleepTime = elapsedGlobalTime - elapsedThreadTime;
                    if (sleepTime > MIN_CONTEXT_SWITCH_TIME_USEC) {
                        Call switchCall = threadData.enter(mContextSwitch, trace);
                        long beforeSwitch = elapsedThreadTime / 2;
                        long afterSwitch = elapsedThreadTime - beforeSwitch;
                        switchCall.mGlobalStartTime = globalTime - elapsedGlobalTime + beforeSwitch;
                        switchCall.mGlobalEndTime = globalTime - afterSwitch;
                        switchCall.mThreadStartTime = threadTime - afterSwitch;
                        switchCall.mThreadEndTime = switchCall.mThreadStartTime;
                        threadData.exit(mContextSwitch, trace);
                        mCallList.add(switchCall);
                    }
}

                // Add thread CPU time.
                Call top = threadData.top();
                top.addCpuTime(elapsedThreadTime);
            }

            switch (methodAction) {
                case METHOD_TRACE_ENTER: {
                    Call call = threadData.enter(methodData, trace);
                    if (haveGlobalClock) {
                        call.mGlobalStartTime = globalTime;
                    }
                    if (haveThreadClock) {
                        call.mThreadStartTime = threadTime;
                    }
                    mCallList.add(call);
                    break;
                }
                case METHOD_TRACE_EXIT:
                case METHOD_TRACE_UNROLL: {
                    Call call = threadData.exit(methodData, trace);
                    if (call != null) {
                        if (haveGlobalClock) {
                            call.mGlobalEndTime = globalTime;
                        }
                        if (haveThreadClock) {
                            call.mThreadEndTime = threadTime;
                        }
                    }
                    break;
                }
                default:
                    throw new RuntimeException("Unrecognized method action: " + methodAction);
            }
        }

        // Exit any pending open-ended calls.
        for (ThreadData threadData : mThreadMap.values()) {
            threadData.endTrace(trace);
        }

        // Recreate the global timeline from thread times, if needed.
        if (!haveGlobalClock) {
            long globalTime = 0;
            prevThreadData = null;
            for (TraceAction traceAction : trace) {
                Call call = traceAction.mCall;
                ThreadData threadData = call.getThreadData();

                if (traceAction.mAction == TraceAction.ACTION_ENTER) {
                    long threadTime = call.mThreadStartTime;
                    globalTime += call.mThreadStartTime - threadData.mThreadCurrentTime;
                    call.mGlobalStartTime = globalTime;
                    if (!threadData.mHaveGlobalTime) {
                        threadData.mHaveGlobalTime = true;
                        threadData.mGlobalStartTime = globalTime;
                    }
                    threadData.mThreadCurrentTime = threadTime;
                } else if (traceAction.mAction == TraceAction.ACTION_EXIT) {
                    long threadTime = call.mThreadEndTime;
                    globalTime += call.mThreadEndTime - threadData.mThreadCurrentTime;
                    call.mGlobalEndTime = globalTime;
                    threadData.mGlobalEndTime = globalTime;
                    threadData.mThreadCurrentTime = threadTime;
                } // else, ignore ACTION_INCOMPLETE calls, nothing to do
prevThreadData = threadData;
}
}

        // Finish updating all calls and calculate the total time spent.
        for (int i = mCallList.size() - 1; i >= 0; i--) {
            Call call = mCallList.get(i);

            // Calculate exclusive real-time by subtracting inclusive real time
            // accumulated by children from the total span.
            long realTime = call.mGlobalEndTime - call.mGlobalStartTime;
            call.mExclusiveRealTime = Math.max(realTime - call.mInclusiveRealTime, 0);
            call.mInclusiveRealTime = realTime;

            call.finish();
        }
        mTotalCpuTime = 0;
        mTotalRealTime = 0;
        for (ThreadData threadData : mThreadMap.values()) {
            Call rootCall = threadData.getRootCall();
            threadData.updateRootCallTimeBounds();
            rootCall.finish();
            mTotalCpuTime += rootCall.mInclusiveCpuTime;
            mTotalRealTime += rootCall.mInclusiveRealTime;
}

if (mRegression) {
            System.out.format("totalCpuTime %dus\n", mTotalCpuTime);
            System.out.format("totalRealTime %dus\n", mTotalRealTime);

            dumpThreadTimes();
dumpCallTimes();
}
}
//Synthetic comment -- @@ -353,7 +448,7 @@
continue;
}
if (line.equals("*end")) {
                    break;
}
}
switch (mode) {
//Synthetic comment -- @@ -372,6 +467,11 @@
break;
}
}

        if (mClockSource == null) {
            mClockSource = ClockSource.THREAD_CPU;
        }
        return offset;
}

void parseOption(String line) {
//Synthetic comment -- @@ -380,6 +480,16 @@
String key = tokens[0];
String value = tokens[1];
mPropertiesMap.put(key, value);

            if (key.equals("clock")) {
                if (value.equals("thread-cpu")) {
                    mClockSource = ClockSource.THREAD_CPU;
                } else if (value.equals("wall")) {
                    mClockSource = ClockSource.WALL;
                } else if (value.equals("dual")) {
                    mClockSource = ClockSource.DUAL;
                }
            }
}
}

//Synthetic comment -- @@ -435,38 +545,30 @@
}

private void analyzeData() {
        final TimeBase timeBase = getPreferredTimeBase();

// Sort the threads into decreasing cpu time
Collection<ThreadData> tv = mThreadMap.values();
mSortedThreads = tv.toArray(new ThreadData[tv.size()]);
Arrays.sort(mSortedThreads, new Comparator<ThreadData>() {
public int compare(ThreadData td1, ThreadData td2) {
                if (timeBase.getTime(td2) > timeBase.getTime(td1))
return 1;
                if (timeBase.getTime(td2) < timeBase.getTime(td1))
return -1;
return td2.getName().compareTo(td1.getName());
}
});

// Sort the methods into decreasing inclusive time
Collection<MethodData> mv = mMethodMap.values();
MethodData[] methods;
methods = mv.toArray(new MethodData[mv.size()]);
Arrays.sort(methods, new Comparator<MethodData>() {
public int compare(MethodData md1, MethodData md2) {
                if (timeBase.getElapsedInclusiveTime(md2) > timeBase.getElapsedInclusiveTime(md1))
return 1;
                if (timeBase.getElapsedInclusiveTime(md2) < timeBase.getElapsedInclusiveTime(md1))
return -1;
return md1.getName().compareTo(md2.getName());
}
//Synthetic comment -- @@ -475,7 +577,7 @@
// Count the number of methods with non-zero inclusive time
int nonZero = 0;
for (MethodData md : methods) {
            if (timeBase.getElapsedInclusiveTime(md) == 0)
break;
nonZero += 1;
}
//Synthetic comment -- @@ -484,7 +586,7 @@
mSortedMethods = new MethodData[nonZero];
int ii = 0;
for (MethodData md : methods) {
            if (timeBase.getElapsedInclusiveTime(md) == 0)
break;
md.setRank(ii);
mSortedMethods[ii++] = md;
//Synthetic comment -- @@ -492,7 +594,7 @@

// Let each method analyze its profile data
for (MethodData md : mSortedMethods) {
            md.analyzeData(timeBase);
}

// Update all the calls to include the method rank in
//Synthetic comment -- @@ -522,67 +624,65 @@
// entire execution of the thread.
for (ThreadData threadData : mSortedThreads) {
if (!threadData.isEmpty() && threadData.getId() != 0) {
                record = new TimeLineView.Record(threadData, threadData.getRootCall());
timeRecs.add(record);
}
}

for (Call call : mCallList) {
            record = new TimeLineView.Record(call.getThreadData(), call);
timeRecs.add(record);
}

if (mRegression) {
dumpTimeRecs(timeRecs);
System.exit(0);
}
return timeRecs;
}

    private void dumpThreadTimes() {
        System.out.print("\nThread Times\n");
        System.out.print("id  t-start    t-end  g-start    g-end     name\n");
        for (ThreadData threadData : mThreadMap.values()) {
            System.out.format("%2d %8d %8d %8d %8d  %s\n",
                    threadData.getId(),
                    threadData.mThreadStartTime, threadData.mThreadEndTime,
                    threadData.mGlobalStartTime, threadData.mGlobalEndTime,
                    threadData.getName());
        }
    }

private void dumpCallTimes() {
        System.out.print("\nCall Times\n");
        System.out.print("id  t-start    t-end  g-start    g-end    excl.    incl.  method\n");
for (Call call : mCallList) {
            System.out.format("%2d %8d %8d %8d %8d %8d %8d  %s\n",
                    call.getThreadId(), call.mThreadStartTime, call.mThreadEndTime,
                    call.mGlobalStartTime, call.mGlobalEndTime,
                    call.mExclusiveCpuTime, call.mInclusiveCpuTime,
                    call.getMethodData().getName());
}
}

private void dumpMethodStats() {
        System.out.print("\nMethod Stats\n");
        System.out.print("Excl Cpu  Incl Cpu  Excl Real Incl Real    Calls  Method\n");
for (MethodData md : mSortedMethods) {
System.out.format("%9d %9d %9s  %s\n",
                    md.getElapsedExclusiveCpuTime(), md.getElapsedInclusiveCpuTime(),
                    md.getElapsedExclusiveRealTime(), md.getElapsedInclusiveRealTime(),
md.getCalls(), md.getProfileName());
}
}

private void dumpTimeRecs(ArrayList<TimeLineView.Record> timeRecs) {
        System.out.print("\nTime Records\n");
        System.out.print("id  t-start    t-end  g-start    g-end  method\n");
for (TimeLineView.Record record : timeRecs) {
Call call = (Call) record.block;
            System.out.format("%2d %8d %8d %8d %8d  %s\n",
                    call.getThreadId(), call.mThreadStartTime, call.mThreadEndTime,
call.mGlobalStartTime, call.mGlobalEndTime,
call.getMethodData().getName());
}
//Synthetic comment -- @@ -608,12 +708,48 @@
}

@Override
    public long getTotalCpuTime() {
        return mTotalCpuTime;
    }

    @Override
    public long getTotalRealTime() {
        return mTotalRealTime;
    }

    @Override
    public boolean haveCpuTime() {
        return mClockSource != ClockSource.WALL;
    }

    @Override
    public boolean haveRealTime() {
        return mClockSource != ClockSource.THREAD_CPU;
}

@Override
public HashMap<String, String> getProperties() {
return mPropertiesMap;
}

    @Override
    public TimeBase getPreferredTimeBase() {
        if (mClockSource == ClockSource.WALL) {
            return TimeBase.REAL_TIME;
        }
        return TimeBase.CPU_TIME;
    }

    @Override
    public String getClockSource() {
        switch (mClockSource) {
            case THREAD_CPU:
                return "cpu time";
            case WALL:
                return "real time";
            case DUAL:
                return "real time, dual clock";
        }
        return null;
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/MethodData.java b/traceview/src/com/android/traceview/MethodData.java
//Synthetic comment -- index d54b72b..47f6df4 100644

//Synthetic comment -- @@ -36,9 +36,12 @@
private String mProfileName;
private String mPathname;
private int mLineNumber;
    private long mElapsedExclusiveCpuTime;
    private long mElapsedInclusiveCpuTime;
    private long mTopExclusiveCpuTime;
    private long mElapsedExclusiveRealTime;
    private long mElapsedInclusiveRealTime;
    private long mTopExclusiveRealTime;
private int[] mNumCalls = new int[2]; // index 0=normal, 1=recursive
private Color mColor;
private Color mFadedColor;
//Synthetic comment -- @@ -81,16 +84,6 @@
computeProfileName();
}

public double addWeight(int x, int y, double weight) {
if (mX == x && mY == y)
mWeight += weight;
//Synthetic comment -- @@ -115,13 +108,16 @@
computeProfileName();
}

    public void addElapsedExclusive(long cpuTime, long realTime) {
        mElapsedExclusiveCpuTime += cpuTime;
        mElapsedExclusiveRealTime += realTime;
}

    public void addElapsedInclusive(long cpuTime, long realTime,
            boolean isRecursive, Call parent) {
if (isRecursive == false) {
            mElapsedInclusiveCpuTime += cpuTime;
            mElapsedInclusiveRealTime += realTime;
mNumCalls[0] += 1;
} else {
mNumCalls[1] += 1;
//Synthetic comment -- @@ -131,27 +127,27 @@
return;

// Find the child method in the parent
        MethodData parentMethod = parent.getMethodData();
if (parent.isRecursive()) {
            parentMethod.mRecursiveChildren = updateInclusive(cpuTime, realTime,
parentMethod, this, false,
parentMethod.mRecursiveChildren);
} else {
            parentMethod.mChildren = updateInclusive(cpuTime, realTime,
parentMethod, this, false, parentMethod.mChildren);
}

// Find the parent method in the child
if (isRecursive) {
            mRecursiveParents = updateInclusive(cpuTime, realTime, this, parentMethod, true,
mRecursiveParents);
} else {
            mParents = updateInclusive(cpuTime, realTime, this, parentMethod, true,
mParents);
}
}

    private HashMap<Integer, ProfileData> updateInclusive(long cpuTime, long realTime,
MethodData contextMethod, MethodData elementMethod,
boolean elementIsParent, HashMap<Integer, ProfileData> map) {
if (map == null) {
//Synthetic comment -- @@ -159,30 +155,30 @@
} else {
ProfileData profileData = map.get(elementMethod.mId);
if (profileData != null) {
                profileData.addElapsedInclusive(cpuTime, realTime);
return map;
}
}

ProfileData elementData = new ProfileData(contextMethod,
elementMethod, elementIsParent);
        elementData.setElapsedInclusive(cpuTime, realTime);
elementData.setNumCalls(1);
map.put(elementMethod.mId, elementData);
return map;
}

    public void analyzeData(TimeBase timeBase) {
// Sort the parents and children into decreasing inclusive time
ProfileData[] sortedParents;
ProfileData[] sortedChildren;
ProfileData[] sortedRecursiveParents;
ProfileData[] sortedRecursiveChildren;

        sortedParents = sortProfileData(mParents, timeBase);
        sortedChildren = sortProfileData(mChildren, timeBase);
        sortedRecursiveParents = sortProfileData(mRecursiveParents, timeBase);
        sortedRecursiveChildren = sortProfileData(mRecursiveChildren, timeBase);

// Add "self" time to the top of the sorted children
sortedChildren = addSelf(sortedChildren);
//Synthetic comment -- @@ -215,7 +211,8 @@

// Create and return a ProfileData[] array that is a sorted copy
// of the given HashMap values.
    private ProfileData[] sortProfileData(HashMap<Integer, ProfileData> map,
            final TimeBase timeBase) {
if (map == null)
return null;

//Synthetic comment -- @@ -224,7 +221,15 @@
ProfileData[] sorted = values.toArray(new ProfileData[values.size()]);

// Sort the array by elapsed inclusive time
        Arrays.sort(sorted, new Comparator<ProfileData>() {
            public int compare(ProfileData pd1, ProfileData pd2) {
                if (timeBase.getElapsedInclusiveTime(pd2) > timeBase.getElapsedInclusiveTime(pd1))
                    return 1;
                if (timeBase.getElapsedInclusiveTime(pd2) < timeBase.getElapsedInclusiveTime(pd1))
                    return -1;
                return 0;
            }
        });
return sorted;
}

//Synthetic comment -- @@ -240,12 +245,17 @@
return pdata;
}

    public void addTopExclusive(long cpuTime, long realTime) {
        mTopExclusiveCpuTime += cpuTime;
        mTopExclusiveRealTime += realTime;
}

    public long getTopExclusiveCpuTime() {
        return mTopExclusiveCpuTime;
    }

    public long getTopExclusiveRealTime() {
        return mTopExclusiveRealTime;
}

public int getId() {
//Synthetic comment -- @@ -329,12 +339,20 @@
return getName();
}

    public long getElapsedExclusiveCpuTime() {
        return mElapsedExclusiveCpuTime;
}

    public long getElapsedExclusiveRealTime() {
        return mElapsedExclusiveRealTime;
    }

    public long getElapsedInclusiveCpuTime() {
        return mElapsedInclusiveCpuTime;
    }

    public long getElapsedInclusiveRealTime() {
        return mElapsedInclusiveRealTime;
}

public void setFadedColor(Color fadedColor) {
//Synthetic comment -- @@ -379,17 +397,31 @@
int result = md1.getName().compareTo(md2.getName());
return (mDirection == Direction.INCREASING) ? result : -result;
}
            if (mColumn == Column.BY_INCLUSIVE_CPU_TIME) {
                if (md2.getElapsedInclusiveCpuTime() > md1.getElapsedInclusiveCpuTime())
return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedInclusiveCpuTime() < md1.getElapsedInclusiveCpuTime())
return (mDirection == Direction.INCREASING) ? 1 : -1;
return md1.getName().compareTo(md2.getName());
}
            if (mColumn == Column.BY_EXCLUSIVE_CPU_TIME) {
                if (md2.getElapsedExclusiveCpuTime() > md1.getElapsedExclusiveCpuTime())
return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedExclusiveCpuTime() < md1.getElapsedExclusiveCpuTime())
                    return (mDirection == Direction.INCREASING) ? 1 : -1;
                return md1.getName().compareTo(md2.getName());
            }
            if (mColumn == Column.BY_INCLUSIVE_REAL_TIME) {
                if (md2.getElapsedInclusiveRealTime() > md1.getElapsedInclusiveRealTime())
                    return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedInclusiveRealTime() < md1.getElapsedInclusiveRealTime())
                    return (mDirection == Direction.INCREASING) ? 1 : -1;
                return md1.getName().compareTo(md2.getName());
            }
            if (mColumn == Column.BY_EXCLUSIVE_REAL_TIME) {
                if (md2.getElapsedExclusiveRealTime() > md1.getElapsedExclusiveRealTime())
                    return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedExclusiveRealTime() < md1.getElapsedExclusiveRealTime())
return (mDirection == Direction.INCREASING) ? 1 : -1;
return md1.getName().compareTo(md2.getName());
}
//Synthetic comment -- @@ -399,10 +431,25 @@
return md1.getName().compareTo(md2.getName());
return (mDirection == Direction.INCREASING) ? result : -result;
}
            if (mColumn == Column.BY_CPU_TIME_PER_CALL) {
                double time1 = md1.getElapsedInclusiveCpuTime();
time1 = time1 / md1.getTotalCalls();
                double time2 = md2.getElapsedInclusiveCpuTime();
                time2 = time2 / md2.getTotalCalls();
                double diff = time1 - time2;
                int result = 0;
                if (diff < 0)
                    result = -1;
                else if (diff > 0)
                    result = 1;
                if (result == 0)
                    return md1.getName().compareTo(md2.getName());
                return (mDirection == Direction.INCREASING) ? result : -result;
            }
            if (mColumn == Column.BY_REAL_TIME_PER_CALL) {
                double time1 = md1.getElapsedInclusiveRealTime();
                time1 = time1 / md1.getTotalCalls();
                double time2 = md2.getElapsedInclusiveRealTime();
time2 = time2 / md2.getTotalCalls();
double diff = time1 - time2;
int result = 0;
//Synthetic comment -- @@ -449,7 +496,9 @@
}

public static enum Column {
            BY_NAME, BY_EXCLUSIVE_CPU_TIME, BY_EXCLUSIVE_REAL_TIME,
            BY_INCLUSIVE_CPU_TIME, BY_INCLUSIVE_REAL_TIME, BY_CALLS,
            BY_REAL_TIME_PER_CALL, BY_CPU_TIME_PER_CALL,
};

public static enum Direction {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileData.java b/traceview/src/com/android/traceview/ProfileData.java
//Synthetic comment -- index f0c1d61..e3c47fb 100644

//Synthetic comment -- @@ -24,7 +24,8 @@
/** mContext is either the parent or child of mElement */
protected MethodData mContext;
protected boolean mElementIsParent;
    protected long mElapsedInclusiveCpuTime;
    protected long mElapsedInclusiveRealTime;
protected int mNumCalls;

public ProfileData() {
//Synthetic comment -- @@ -45,17 +46,23 @@
return mElement;
}

    public void addElapsedInclusive(long cpuTime, long realTime) {
        mElapsedInclusiveCpuTime += cpuTime;
        mElapsedInclusiveRealTime += realTime;
mNumCalls += 1;
}

    public void setElapsedInclusive(long cpuTime, long realTime) {
        mElapsedInclusiveCpuTime = cpuTime;
        mElapsedInclusiveRealTime = realTime;
}

    public long getElapsedInclusiveCpuTime() {
        return mElapsedInclusiveCpuTime;
    }

    public long getElapsedInclusiveRealTime() {
        return mElapsedInclusiveRealTime;
}

public void setNumCalls(int numCalls) {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileProvider.java b/traceview/src/com/android/traceview/ProfileProvider.java
//Synthetic comment -- index fe5c832..63a1da6 100644

//Synthetic comment -- @@ -44,26 +44,40 @@
private TraceReader mReader;
private Image mSortUp;
private Image mSortDown;
    private String mColumnNames[] = { "Name",
            "Incl Cpu Time %", "Incl Cpu Time", "Excl Cpu Time %", "Excl Cpu Time",
            "Incl Real Time %", "Incl Real Time", "Excl Real Time %", "Excl Real Time",
            "Calls+Recur\nCalls/Total", "Cpu Time/Call", "Real Time/Call" };
    private int mColumnWidths[] = { 370,
            100, 100, 100, 100,
            100, 100, 100, 100,
            100, 100, 100 };
    private int mColumnAlignments[] = { SWT.LEFT,
            SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT,
            SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT,
            SWT.CENTER, SWT.RIGHT, SWT.RIGHT };
private static final int COL_NAME = 0;
    private static final int COL_INCLUSIVE_CPU_TIME_PER = 1;
    private static final int COL_INCLUSIVE_CPU_TIME = 2;
    private static final int COL_EXCLUSIVE_CPU_TIME_PER = 3;
    private static final int COL_EXCLUSIVE_CPU_TIME = 4;
    private static final int COL_INCLUSIVE_REAL_TIME_PER = 5;
    private static final int COL_INCLUSIVE_REAL_TIME = 6;
    private static final int COL_EXCLUSIVE_REAL_TIME_PER = 7;
    private static final int COL_EXCLUSIVE_REAL_TIME = 8;
    private static final int COL_CALLS = 9;
    private static final int COL_CPU_TIME_PER_CALL = 10;
    private static final int COL_REAL_TIME_PER_CALL = 11;
    private long mTotalCpuTime;
    private long mTotalRealTime;
private Pattern mUppercase;
private int mPrevMatchIndex = -1;

public ProfileProvider(TraceReader reader) {
mRoots = reader.getMethods();
mReader = reader;
        mTotalCpuTime = reader.getTotalCpuTime();
        mTotalRealTime = reader.getTotalRealTime();
Display display = Display.getCurrent();
InputStream in = getClass().getClassLoader().getResourceAsStream(
"icons/sort_up.png");
//Synthetic comment -- @@ -126,7 +140,22 @@
}

public int[] getColumnWidths() {
        int[] widths = Arrays.copyOf(mColumnWidths, mColumnWidths.length);
        if (!mReader.haveCpuTime()) {
            widths[COL_EXCLUSIVE_CPU_TIME] = 0;
            widths[COL_EXCLUSIVE_CPU_TIME_PER] = 0;
            widths[COL_INCLUSIVE_CPU_TIME] = 0;
            widths[COL_INCLUSIVE_CPU_TIME_PER] = 0;
            widths[COL_CPU_TIME_PER_CALL] = 0;
        }
        if (!mReader.haveRealTime()) {
            widths[COL_EXCLUSIVE_REAL_TIME] = 0;
            widths[COL_EXCLUSIVE_REAL_TIME_PER] = 0;
            widths[COL_INCLUSIVE_REAL_TIME] = 0;
            widths[COL_INCLUSIVE_REAL_TIME_PER] = 0;
            widths[COL_REAL_TIME_PER_CALL] = 0;
        }
        return widths;
}

public int[] getColumnAlignments() {
//Synthetic comment -- @@ -201,31 +230,58 @@
MethodData md = (MethodData) element;
if (col == COL_NAME)
return md.getProfileName();
                if (col == COL_EXCLUSIVE_CPU_TIME) {
                    double val = md.getElapsedExclusiveCpuTime();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_EXCLUSIVE_CPU_TIME_PER) {
                    double val = md.getElapsedExclusiveCpuTime();
                    double per = val * 100.0 / mTotalCpuTime;
return String.format("%.1f%%", per);
}
                if (col == COL_INCLUSIVE_CPU_TIME) {
                    double val = md.getElapsedInclusiveCpuTime();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_CPU_TIME_PER) {
                    double val = md.getElapsedInclusiveCpuTime();
                    double per = val * 100.0 / mTotalCpuTime;
                    return String.format("%.1f%%", per);
                }
                if (col == COL_EXCLUSIVE_REAL_TIME) {
                    double val = md.getElapsedExclusiveRealTime();
                    val = traceUnits.getScaledValue(val);
                    return String.format("%.3f", val);
                }
                if (col == COL_EXCLUSIVE_REAL_TIME_PER) {
                    double val = md.getElapsedExclusiveRealTime();
                    double per = val * 100.0 / mTotalRealTime;
                    return String.format("%.1f%%", per);
                }
                if (col == COL_INCLUSIVE_REAL_TIME) {
                    double val = md.getElapsedInclusiveRealTime();
                    val = traceUnits.getScaledValue(val);
                    return String.format("%.3f", val);
                }
                if (col == COL_INCLUSIVE_REAL_TIME_PER) {
                    double val = md.getElapsedInclusiveRealTime();
                    double per = val * 100.0 / mTotalRealTime;
return String.format("%.1f%%", per);
}
if (col == COL_CALLS)
return md.getCalls();
                if (col == COL_CPU_TIME_PER_CALL) {
int numCalls = md.getTotalCalls();
                    double val = md.getElapsedInclusiveCpuTime();
                    val = val / numCalls;
                    val = traceUnits.getScaledValue(val);
                    return String.format("%.3f", val);
                }
                if (col == COL_REAL_TIME_PER_CALL) {
                    int numCalls = md.getTotalCalls();
                    double val = md.getElapsedInclusiveRealTime();
val = val / numCalls;
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
//Synthetic comment -- @@ -234,16 +290,29 @@
ProfileSelf ps = (ProfileSelf) element;
if (col == COL_NAME)
return ps.getProfileName();
                if (col == COL_INCLUSIVE_CPU_TIME) {
                    double val = ps.getElapsedInclusiveCpuTime();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_CPU_TIME_PER) {
double total;
                    double val = ps.getElapsedInclusiveCpuTime();
MethodData context = ps.getContext();
                    total = context.getElapsedInclusiveCpuTime();
                    double per = val * 100.0 / total;
                    return String.format("%.1f%%", per);
                }
                if (col == COL_INCLUSIVE_REAL_TIME) {
                    double val = ps.getElapsedInclusiveRealTime();
                    val = traceUnits.getScaledValue(val);
                    return String.format("%.3f", val);
                }
                if (col == COL_INCLUSIVE_REAL_TIME_PER) {
                    double total;
                    double val = ps.getElapsedInclusiveRealTime();
                    MethodData context = ps.getContext();
                    total = context.getElapsedInclusiveRealTime();
double per = val * 100.0 / total;
return String.format("%.1f%%", per);
}
//Synthetic comment -- @@ -252,16 +321,29 @@
ProfileData pd = (ProfileData) element;
if (col == COL_NAME)
return pd.getProfileName();
                if (col == COL_INCLUSIVE_CPU_TIME) {
                    double val = pd.getElapsedInclusiveCpuTime();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_CPU_TIME_PER) {
double total;
                    double val = pd.getElapsedInclusiveCpuTime();
MethodData context = pd.getContext();
                    total = context.getElapsedInclusiveCpuTime();
                    double per = val * 100.0 / total;
                    return String.format("%.1f%%", per);
                }
                if (col == COL_INCLUSIVE_REAL_TIME) {
                    double val = pd.getElapsedInclusiveRealTime();
                    val = traceUnits.getScaledValue(val);
                    return String.format("%.3f", val);
                }
                if (col == COL_INCLUSIVE_REAL_TIME_PER) {
                    double total;
                    double val = pd.getElapsedInclusiveRealTime();
                    MethodData context = pd.getContext();
                    total = context.getElapsedInclusiveRealTime();
double per = val * 100.0 / total;
return String.format("%.1f%%", per);
}
//Synthetic comment -- @@ -330,23 +412,38 @@
// Sort names alphabetically
sorter.setColumn(MethodData.Sorter.Column.BY_NAME);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE_CPU_TIME]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE_CPU_TIME);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE_CPU_TIME_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE_CPU_TIME);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE_CPU_TIME]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE_CPU_TIME);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE_CPU_TIME_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE_CPU_TIME);
                Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE_REAL_TIME]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE_REAL_TIME);
                Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE_REAL_TIME_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE_REAL_TIME);
                Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE_REAL_TIME]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE_REAL_TIME);
                Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE_REAL_TIME_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE_REAL_TIME);
Arrays.sort(mRoots, sorter);
} else if (name == mColumnNames[COL_CALLS]) {
sorter.setColumn(MethodData.Sorter.Column.BY_CALLS);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_CPU_TIME_PER_CALL]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_CPU_TIME_PER_CALL);
                Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_REAL_TIME_PER_CALL]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_REAL_TIME_PER_CALL);
Arrays.sort(mRoots, sorter);
}
MethodData.Sorter.Direction direction = sorter.getDirection();








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileSelf.java b/traceview/src/com/android/traceview/ProfileSelf.java
//Synthetic comment -- index 3a4f3d9..45543b2 100644

//Synthetic comment -- @@ -28,7 +28,12 @@
}

@Override
    public long getElapsedInclusiveCpuTime() {
        return mElement.getTopExclusiveCpuTime();
    }

    @Override
    public long getElapsedInclusiveRealTime() {
        return mElement.getTopExclusiveRealTime();
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileView.java b/traceview/src/com/android/traceview/ProfileView.java
//Synthetic comment -- index 506efca..8889b13 100644

//Synthetic comment -- @@ -279,7 +279,7 @@
}
if (name == "Call") {
Call call = (Call) selection.getValue();
                MethodData md = call.getMethodData();
highlightMethod(md, true);
return;
}
//Synthetic comment -- @@ -304,9 +304,11 @@
mTreeViewer.setSelection(sel, true);
Tree tree = mTreeViewer.getTree();
TreeItem[] items = tree.getSelection();
        if (items.length != 0) {
            tree.setTopItem(items[0]);
            // workaround a Mac bug by adding showItem().
            tree.showItem(items[0]);
        }
}

private void expandNode(MethodData md) {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/QtraceReader.java b/traceview/src/com/android/traceview/QtraceReader.java
deleted file mode 100644
//Synthetic comment -- index c4db4a2..0000000

//Synthetic comment -- @@ -1,45 +0,0 @@








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ThreadData.java b/traceview/src/com/android/traceview/ThreadData.java
//Synthetic comment -- index 54ea891..f71e9c2 100644

//Synthetic comment -- @@ -23,158 +23,130 @@

private int mId;
private String mName;
    private boolean mIsEmpty;

    private Call mRootCall;
private ArrayList<Call> mStack = new ArrayList<Call>();

// This is a hash of all the methods that are currently on the stack.
private HashMap<MethodData, Integer> mStackMethods = new HashMap<MethodData, Integer>();

    boolean mHaveGlobalTime;
    long mGlobalStartTime;
    long mGlobalEndTime;

    boolean mHaveThreadTime;
    long mThreadStartTime;
    long mThreadEndTime;

    long mThreadCurrentTime; // only used while parsing thread-cpu clock

ThreadData(int id, String name, MethodData topLevel) {
mId = id;
mName = String.format("[%d] %s", id, name);
mIsEmpty = true;
        mRootCall = new Call(this, topLevel, null);
        mRootCall.setName(mName);
        mStack.add(mRootCall);
}

public String getName() {
return mName;
}

    public Call getRootCall() {
        return mRootCall;
}

    /**
     * Returns true if no calls have ever been recorded for this thread.
     */
    public boolean isEmpty() {
        return mIsEmpty;
}

    Call enter(MethodData method, ArrayList<TraceAction> trace) {
        if (mIsEmpty) {
            mIsEmpty = false;
            if (trace != null) {
                trace.add(new TraceAction(TraceAction.ACTION_ENTER, mRootCall));
            }
        }

        Call caller = top();
        Call call = new Call(this, method, caller);
        mStack.add(call);

        if (trace != null) {
            trace.add(new TraceAction(TraceAction.ACTION_ENTER, call));
        }

        Integer num = mStackMethods.get(method);
if (num == null) {
num = 0;
} else if (num > 0) {
            call.setRecursive(true);
}
        mStackMethods.put(method, num + 1);

        return call;
}

    Call exit(MethodData method, ArrayList<TraceAction> trace) {
        Call call = top();
        if (call.mCaller == null) {
            return null;
}

        if (call.getMethodData() != method) {
            String error = "Method exit (" + method.getName()
                    + ") does not match current method (" + call.getMethodData().getName()
+ ")";
throw new RuntimeException(error);
}

        mStack.remove(mStack.size() - 1);

        if (trace != null) {
            trace.add(new TraceAction(TraceAction.ACTION_EXIT, call));
        }

        Integer num = mStackMethods.get(method);
if (num != null) {
            if (num == 1) {
                mStackMethods.remove(method);
} else {
                mStackMethods.put(method, num - 1);
}
}

        return call;
}

    Call top() {
        return mStack.get(mStack.size() - 1);
}

    void endTrace(ArrayList<TraceAction> trace) {
        for (int i = mStack.size() - 1; i >= 1; i--) {
            Call call = mStack.get(i);
            call.mGlobalEndTime = mGlobalEndTime;
            call.mThreadEndTime = mThreadEndTime;
            if (trace != null) {
                trace.add(new TraceAction(TraceAction.ACTION_INCOMPLETE, call));
}
}
        mStack.clear();
        mStackMethods.clear();
    }

    void updateRootCallTimeBounds() {
        if (!mIsEmpty) {
            mRootCall.mGlobalStartTime = mGlobalStartTime;
            mRootCall.mGlobalEndTime = mGlobalEndTime;
            mRootCall.mThreadStartTime = mThreadStartTime;
            mRootCall.mThreadEndTime = mThreadEndTime;
        }
}

@Override
//Synthetic comment -- @@ -186,43 +158,11 @@
return mId;
}

public long getCpuTime() {
        return mRootCall.mInclusiveCpuTime;
}

    public long getRealTime() {
        return mRootCall.mInclusiveRealTime;
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeBase.java b/traceview/src/com/android/traceview/TimeBase.java
new file mode 100644
//Synthetic comment -- index 0000000..b6b23cb

//Synthetic comment -- @@ -0,0 +1,71 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.traceview;

interface TimeBase {
    public static final TimeBase CPU_TIME = new CpuTimeBase();
    public static final TimeBase REAL_TIME = new RealTimeBase();

    public long getTime(ThreadData threadData);
    public long getElapsedInclusiveTime(MethodData methodData);
    public long getElapsedExclusiveTime(MethodData methodData);
    public long getElapsedInclusiveTime(ProfileData profileData);

    public static final class CpuTimeBase implements TimeBase {
        @Override
        public long getTime(ThreadData threadData) {
            return threadData.getCpuTime();
        }

        @Override
        public long getElapsedInclusiveTime(MethodData methodData) {
            return methodData.getElapsedInclusiveCpuTime();
        }

        @Override
        public long getElapsedExclusiveTime(MethodData methodData) {
            return methodData.getElapsedExclusiveCpuTime();
        }

        @Override
        public long getElapsedInclusiveTime(ProfileData profileData) {
            return profileData.getElapsedInclusiveCpuTime();
        }
    }

    public static final class RealTimeBase implements TimeBase {
        @Override
        public long getTime(ThreadData threadData) {
            return threadData.getRealTime();
        }

        @Override
        public long getElapsedInclusiveTime(MethodData methodData) {
            return methodData.getElapsedInclusiveRealTime();
        }

        @Override
        public long getElapsedExclusiveTime(MethodData methodData) {
            return methodData.getElapsedExclusiveRealTime();
        }

        @Override
        public long getElapsedInclusiveTime(ProfileData profileData) {
            return profileData.getElapsedInclusiveRealTime();
        }
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeLineView.java b/traceview/src/com/android/traceview/TimeLineView.java
//Synthetic comment -- index c9eb8e7..b04a411 100644

//Synthetic comment -- @@ -54,10 +54,8 @@
public class TimeLineView extends Composite implements Observer {

private HashMap<String, RowData> mRowByName;
private RowData[] mRows;
private Segment[] mSegments;
private HashMap<Integer, String> mThreadLabels;
private Timescale mTimescale;
private Surface mSurface;
//Synthetic comment -- @@ -89,19 +87,21 @@
private static final int rowYSpace = rowHeight + rowYMargin;
private static final int majorTickLength = 8;
private static final int minorTickLength = 4;
    private static final int timeLineOffsetY = 58;
private static final int tickToFontSpacing = 2;

/** start of first row */
    private static final int topMargin = 90;
private int mMouseRow = -1;
private int mNumRows;
private int mStartRow;
private int mEndRow;
private TraceUnits mUnits;
    private String mClockSource;
    private boolean mHaveCpuTime;
    private boolean mHaveRealTime;
private int mSmallFontWidth;
private int mSmallFontHeight;
private SelectionController mSelectionController;
private MethodData mHighlightMethodData;
private Call mHighlightCall;
//Synthetic comment -- @@ -118,6 +118,13 @@
public Color getColor();
public double addWeight(int x, int y, double weight);
public void clearWeight();
        public long getExclusiveCpuTime();
        public long getInclusiveCpuTime();
        public long getExclusiveRealTime();
        public long getInclusiveRealTime();
        public boolean isContextSwitch();
        public boolean isIgnoredBlock();
        public Block getParentBlock();
}

public static interface Row {
//Synthetic comment -- @@ -142,6 +149,9 @@
this.mSelectionController = selectionController;
selectionController.addObserver(this);
mUnits = reader.getTraceUnits();
        mClockSource = reader.getClockSource();
        mHaveCpuTime = reader.haveCpuTime();
        mHaveRealTime = reader.haveRealTime();
mThreadLabels = reader.getThreadLabels();

Display display = getDisplay();
//Synthetic comment -- @@ -169,11 +179,6 @@
mSmallFontWidth = gc.getFontMetrics().getAverageCharWidth();
mSmallFontHeight = gc.getFontMetrics().getHeight();

image.dispose();
gc.dispose();

//Synthetic comment -- @@ -403,6 +408,8 @@
}
});

        ArrayList<Segment> segmentList = new ArrayList<Segment>();

// The records are sorted into increasing start time,
// so the minimum start time is the start time of the first record.
double minVal = 0;
//Synthetic comment -- @@ -415,6 +422,10 @@
for (Record rec : records) {
Row row = rec.row;
Block block = rec.block;
            if (block.isIgnoredBlock()) {
                continue;
            }

String rowName = row.getName();
RowData rd = mRowByName.get(rowName);
if (rd == null) {
//Synthetic comment -- @@ -426,7 +437,6 @@
if (blockEndTime > rd.mEndTime) {
long start = Math.max(blockStartTime, rd.mEndTime);
rd.mElapsed += blockEndTime - start;
rd.mEndTime = blockEndTime;
}
if (blockEndTime > maxVal)
//Synthetic comment -- @@ -447,7 +457,7 @@
if (topStartTime < blockStartTime) {
Segment segment = new Segment(rd, top, topStartTime,
blockStartTime);
                    segmentList.add(segment);
}

// If this block starts where the previous (top) block ends,
//Synthetic comment -- @@ -457,7 +467,7 @@
rd.push(block);
} else {
// We may have to pop several frames here.
                popFrames(rd, top, blockStartTime, segmentList);
rd.push(block);
}
}
//Synthetic comment -- @@ -465,7 +475,7 @@
// Clean up the stack of each row
for (RowData rd : mRowByName.values()) {
Block top = rd.top();
            popFrames(rd, top, Integer.MAX_VALUE, segmentList);
}

mSurface.setRange(minVal, maxVal);
//Synthetic comment -- @@ -495,7 +505,7 @@

// Sort the blocks into increasing rows, and within rows into
// increasing start values.
        mSegments = segmentList.toArray(new Segment[segmentList.size()]);
Arrays.sort(mSegments, new Comparator<Segment>() {
public int compare(Segment bd1, Segment bd2) {
RowData rd1 = bd1.mRowData;
//Synthetic comment -- @@ -524,13 +534,14 @@
}
}

    private static void popFrames(RowData rd, Block top, long startTime,
            ArrayList<Segment> segmentList) {
long topEndTime = top.getEndTime();
long lastEndTime = top.getStartTime();
while (topEndTime <= startTime) {
if (topEndTime > lastEndTime) {
Segment segment = new Segment(rd, top, lastEndTime, topEndTime);
                segmentList.add(segment);
lastEndTime = topEndTime;
}
rd.pop();
//Synthetic comment -- @@ -543,7 +554,7 @@
// If we get here, then topEndTime > startTime
if (lastEndTime < startTime) {
Segment bd = new Segment(rd, top, lastEndTime, startTime);
            segmentList.add(bd);
}
}

//Synthetic comment -- @@ -648,7 +659,9 @@
private Cursor mZoomCursor;
private String mMethodName = null;
private Color mMethodColor = null;
        private String mDetails;
private int mMethodStartY;
        private int mDetailsStartY;
private int mMarkStartX;
private int mMarkEndX;

//Synthetic comment -- @@ -662,6 +675,7 @@
mZoomCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
setCursor(mZoomCursor);
mMethodStartY = mSmallFontHeight + 1;
            mDetailsStartY = mMethodStartY + mSmallFontHeight + 1;
addPaintListener(new PaintListener() {
public void paintControl(PaintEvent pe) {
draw(pe.display, pe.gc);
//Synthetic comment -- @@ -680,7 +694,7 @@
public void setMarkEnd(int x) {
mMarkEndX = x;
}

public void setMethodName(String name) {
mMethodName = name;
}
//Synthetic comment -- @@ -688,7 +702,11 @@
public void setMethodColor(Color color) {
mMethodColor = color;
}

        public void setDetails(String details) {
            mDetails = details;
        }

private void mouseMove(MouseEvent me) {
me.y = -1;
mSurface.mouseMove(me);
//Synthetic comment -- @@ -734,7 +752,10 @@

// Draw the method name and color, if needed
drawMethod(display, gcImage);

            // Draw the details, if needed
            drawDetails(display, gcImage);

// Draw the off-screen buffer to the screen
gc.drawImage(image, 0, 0);

//Synthetic comment -- @@ -771,7 +792,11 @@
// Display the maximum data value
double maxVal = mScaleInfo.getMaxVal();
info = mUnits.labelledString(maxVal);
            if (mClockSource != null) {
                info = String.format(" max %s (%s)", info, mClockSource);  //$NON-NLS-1$
            } else {
                info = String.format(" max %s ", info);  //$NON-NLS-1$
            }
Point extent = gc.stringExtent(info);
Point dim = getSize();
int x1 = dim.x - RightMargin - extent.x;
//Synthetic comment -- @@ -791,7 +816,17 @@
x1 += width + METHOD_BLOCK_MARGIN;
gc.drawString(mMethodName, x1, y1, true);
}

        private void drawDetails(Display display, GC gc) {
            if (mDetails == null) {
                return;
            }

            int x1 = LeftMargin + 2 * mSmallFontWidth + METHOD_BLOCK_MARGIN;
            int y1 = mDetailsStartY;
            gc.drawString(mDetails, x1, y1, true);
        }

private void drawTicks(Display display, GC gc) {
Point dim = getSize();
int y2 = majorTickLength + timeLineOffsetY;
//Synthetic comment -- @@ -1049,6 +1084,7 @@

String blockName = null;
Color blockColor = null;
            String blockDetails = null;

if (mDebug) {
double pixelsPerRange = mScaleInfo.getPixelsPerRange();
//Synthetic comment -- @@ -1073,8 +1109,30 @@
if (mMouseRow == strip.mRowData.mRank) {
if (mMouse.x >= strip.mX
&& mMouse.x < strip.mX + strip.mWidth) {
                        Block block = strip.mSegment.mBlock;
                        blockName = block.getName();
blockColor = strip.mColor;
                        if (mHaveCpuTime) {
                            if (mHaveRealTime) {
                                blockDetails = String.format(
                                        "excl cpu %s, incl cpu %s, "
                                        + "excl real %s, incl real %s",
                                        mUnits.labelledString(block.getExclusiveCpuTime()),
                                        mUnits.labelledString(block.getInclusiveCpuTime()),
                                        mUnits.labelledString(block.getExclusiveRealTime()),
                                        mUnits.labelledString(block.getInclusiveRealTime()));
                            } else {
                                blockDetails = String.format(
                                        "excl cpu %s, incl cpu %s",
                                        mUnits.labelledString(block.getExclusiveCpuTime()),
                                        mUnits.labelledString(block.getInclusiveCpuTime()));
                            }
                        } else {
                            blockDetails = String.format(
                                    "excl real %s, incl real %s",
                                    mUnits.labelledString(block.getExclusiveRealTime()),
                                    mUnits.labelledString(block.getInclusiveRealTime()));
                        }
}
if (mMouseSelect.x >= strip.mX
&& mMouseSelect.x < strip.mX + strip.mWidth) {
//Synthetic comment -- @@ -1125,6 +1183,7 @@
if (blockName != null) {
mTimescale.setMethodName(blockName);
mTimescale.setMethodColor(blockColor);
                mTimescale.setDetails(blockDetails);
mShowHighlightName = false;
} else if (mShowHighlightName) {
// Draw the highlighted method name
//Synthetic comment -- @@ -1136,10 +1195,12 @@
if (md != null) {
mTimescale.setMethodName(md.getProfileName());
mTimescale.setMethodColor(md.getColor());
                    mTimescale.setDetails(null);
}
} else {
mTimescale.setMethodName(null);
mTimescale.setMethodColor(null);
                mTimescale.setDetails(null);
}
mTimescale.redraw();

//Synthetic comment -- @@ -1152,7 +1213,7 @@
}

private void drawHighlights(GC gc, Point dim) {
            int height = mHighlightHeight;
if (height <= 0)
return;
for (Range range : mHighlightExclusive) {
//Synthetic comment -- @@ -1278,13 +1339,15 @@
long callEnd = -1;
RowData callRowData = null;
int prevMethodStart = -1;
            int prevMethodEnd = -1;
int prevCallStart = -1;
            int prevCallEnd = -1;
if (mHighlightCall != null) {
int callPixelStart = -1;
int callPixelEnd = -1;
                callStart = mHighlightCall.getStartTime();
                callEnd = mHighlightCall.getEndTime();
                callMethod = mHighlightCall.getMethodData();
if (callStart >= minVal)
callPixelStart = mScaleInfo.valueToPixel(callStart);
if (callEnd <= maxVal)
//Synthetic comment -- @@ -1306,7 +1369,11 @@
continue;
if (segment.mStartTime >= maxVal)
continue;

Block block = segment.mBlock;

                // Skip over blocks that were not assigned a color, including the
                // top level block and others that have zero inclusive time.
Color color = block.getColor();
if (color == null)
continue;
//Synthetic comment -- @@ -1318,6 +1385,7 @@
int pixelStart = mScaleInfo.valueToPixel(recordStart);
int pixelEnd = mScaleInfo.valueToPixel(recordEnd);
int width = pixelEnd - pixelStart;
                boolean isContextSwitch = segment.mIsContextSwitch;

RowData rd = segment.mRowData;
MethodData md = block.getMethodData();
//Synthetic comment -- @@ -1342,24 +1410,25 @@

if (mHighlightMethodData != null) {
if (mHighlightMethodData == md) {
                        if (prevMethodStart != pixelStart || prevMethodEnd != pixelEnd) {
prevMethodStart = pixelStart;
                            prevMethodEnd = pixelEnd;
int rangeWidth = width;
if (rangeWidth == 0)
rangeWidth = 1;
mHighlightExclusive.add(new Range(pixelStart
+ LeftMargin, rangeWidth, y1, color));
                            callStart = block.getStartTime();
int callPixelStart = -1;
if (callStart >= minVal)
callPixelStart = mScaleInfo.valueToPixel(callStart);
                            int callPixelEnd = -1;
                            callEnd = block.getEndTime();
                            if (callEnd <= maxVal)
                                callPixelEnd = mScaleInfo.valueToPixel(callEnd);
                            if (prevCallStart != callPixelStart || prevCallEnd != callPixelEnd) {
prevCallStart = callPixelStart;
                                prevCallEnd = callPixelEnd;
mHighlightInclusive.add(new Range(
callPixelStart + LeftMargin,
callPixelEnd + LeftMargin, y1, color));
//Synthetic comment -- @@ -1372,8 +1441,9 @@
if (segment.mStartTime >= callStart
&& segment.mEndTime <= callEnd && callMethod == md
&& callRowData == rd) {
                        if (prevMethodStart != pixelStart || prevMethodEnd != pixelEnd) {
prevMethodStart = pixelStart;
                            prevMethodEnd = pixelEnd;
int rangeWidth = width;
if (rangeWidth == 0)
rangeWidth = 1;
//Synthetic comment -- @@ -1418,7 +1488,7 @@
// how much of the region [N - 0.5, N + 0.5] is covered
// by the segment.
double weight = computeWeight(recordStart, recordEnd,
                                isContextSwitch, pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
if (weight > pix.mMaxWeight) {
pix.setFields(pixelStart, weight, segment, color,
//Synthetic comment -- @@ -1426,13 +1496,15 @@
}
} else {
int x1 = pixelStart + LeftMargin;
                        Strip strip = new Strip(
                                x1, isContextSwitch ? y1 + rowHeight - 1 : y1,
                                width, isContextSwitch ? 1 : rowHeight,
                                rd, segment, color);
mStripList.add(strip);
}
} else {
double weight = computeWeight(recordStart, recordEnd,
                            isContextSwitch, pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
if (weight > pix.mMaxWeight) {
pix.setFields(pixelStart, weight, segment, color, rd);
//Synthetic comment -- @@ -1444,7 +1516,7 @@
// Compute the weight for the next pixel
pixelStart += 1;
weight = computeWeight(recordStart, recordEnd,
                                isContextSwitch, pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
pix.setFields(pixelStart, weight, segment, color, rd);
} else if (width > 1) {
//Synthetic comment -- @@ -1455,8 +1527,10 @@
pixelStart += 1;
width -= 1;
int x1 = pixelStart + LeftMargin;
                        Strip strip = new Strip(
                                x1, isContextSwitch ? y1 + rowHeight - 1 : y1,
                                width, isContextSwitch ? 1 : rowHeight,
                                rd,segment, color);
mStripList.add(strip);
}
}
//Synthetic comment -- @@ -1483,7 +1557,11 @@
}
}

        private double computeWeight(double start, double end,
                boolean isContextSwitch, int pixel) {
            if (isContextSwitch) {
                return 0;
            }
double pixelStartFraction = mScaleInfo.valueToPixelFraction(start);
double pixelEndFraction = mScaleInfo.valueToPixelFraction(end);
double leftEndPoint = Math.max(pixelStartFraction, pixel - 0.5);
//Synthetic comment -- @@ -1811,7 +1889,7 @@
} else {
mFadeColors = true;
mShowHighlightName = true;
                mHighlightHeight = highlightHeights[mHighlightStep];
getDisplay().timerExec(HIGHLIGHT_TIMER_INTERVAL, mHighlightAnimator);
}
redraw();
//Synthetic comment -- @@ -1820,7 +1898,7 @@
private void clearHighlights() {
// System.out.printf("clearHighlights()\n");
mShowHighlightName = false;
            mHighlightHeight = 0;
mHighlightMethodData = null;
mHighlightCall = null;
mFadeColors = false;
//Synthetic comment -- @@ -1900,7 +1978,7 @@
private static final int ZOOM_TIMER_INTERVAL = 10;
private static final int HIGHLIGHT_TIMER_INTERVAL = 50;
private static final int ZOOM_STEPS = 8; // must be even
        private int mHighlightHeight = 4;
private final int[] highlightHeights = { 0, 2, 4, 5, 6, 5, 4, 2, 4, 5,
6 };
private final int HIGHLIGHT_STEPS = highlightHeights.length;
//Synthetic comment -- @@ -1989,7 +2067,12 @@
private static class Segment {
Segment(RowData rowData, Block block, long startTime, long endTime) {
mRowData = rowData;
            if (block.isContextSwitch()) {
                mBlock = block.getParentBlock();
                mIsContextSwitch = true;
            } else {
                mBlock = block;
            }
mStartTime = startTime;
mEndTime = endTime;
}
//Synthetic comment -- @@ -1998,6 +2081,7 @@
private Block mBlock;
private long mStartTime;
private long mEndTime;
        private boolean mIsContextSwitch;
}

private static class Strip {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceAction.java b/traceview/src/com/android/traceview/TraceAction.java
new file mode 100644
//Synthetic comment -- index 0000000..6717300

//Synthetic comment -- @@ -0,0 +1,31 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.traceview;

final class TraceAction {
    public static final int ACTION_ENTER = 0;
    public static final int ACTION_EXIT = 1;
    public static final int ACTION_INCOMPLETE = 2;

    public final int mAction;
    public final Call mCall;

    public TraceAction(int action, Call call) {
        mAction = action;
        mCall = call;
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceReader.java b/traceview/src/com/android/traceview/TraceReader.java
//Synthetic comment -- index e936629..fa76d27 100644

//Synthetic comment -- @@ -45,10 +45,22 @@
return null;
}

    public long getTotalCpuTime() {
return 0;
}

    public long getTotalRealTime() {
        return 0;
    }

    public boolean haveCpuTime() {
        return false;
    }

    public boolean haveRealTime() {
        return false;
    }

public HashMap<String, String> getProperties() {
return null;
}
//Synthetic comment -- @@ -56,4 +68,12 @@
public ProfileProvider getProfileProvider() {
return null;
}

    public TimeBase getPreferredTimeBase() {
        return TimeBase.CPU_TIME;
    }

    public String getClockSource() {
        return null;
    }
}







