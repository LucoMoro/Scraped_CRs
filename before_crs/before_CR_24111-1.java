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
    
    // Values for bits within the mFlags field.
    private static final int METHOD_ACTION_MASK = 0x3;
    private static final int IS_RECURSIVE = 0x10;

    private int mThreadId;
    private int mFlags;
    MethodData mMethodData;
    
    /** 0-based thread-local start time */
    long mThreadStartTime;
    
    /**  global start time */
    long mGlobalStartTime;

    /** global end time */
    long mGlobalEndTime;
    
private String mName;

    /**
     * This constructor is used for the root of a Call tree. The name is
     * the name of the corresponding thread. 
     */
    Call(String name, MethodData methodData) {
        mName = name;
        mMethodData = methodData;
    }

    Call() {
    }
    
    Call(int threadId, MethodData methodData, long time, int methodAction) {
        mThreadId = threadId;
mMethodData = methodData;
        mThreadStartTime = time;
        mFlags = methodAction & METHOD_ACTION_MASK;
mName = methodData.getProfileName();
    }
    
    public void set(int threadId, MethodData methodData, long time, int methodAction) {
        mThreadId = threadId;
        mMethodData = methodData;
        mThreadStartTime = time;
        mFlags = methodAction & METHOD_ACTION_MASK;
        mName = methodData.getProfileName();
}

public void updateName() {
//Synthetic comment -- @@ -87,22 +65,26 @@
return mGlobalEndTime;
}

public Color getColor() {
return mMethodData.getColor();
}

    public void addExclusiveTime(long elapsed) {
        mMethodData.addElapsedExclusive(elapsed);
        if ((mFlags & IS_RECURSIVE) == 0) {
            mMethodData.addTopExclusive(elapsed);
        }
    }

    public void addInclusiveTime(long elapsed, Call parent) {
        boolean isRecursive = (mFlags & IS_RECURSIVE) != 0;
        mMethodData.addElapsedInclusive(elapsed, isRecursive, parent);
    }

public String getName() {
return mName;
}
//Synthetic comment -- @@ -111,31 +93,71 @@
mName = name;
}

    int getThreadId() {
        return mThreadId;
}

public MethodData getMethodData() {
return mMethodData;
}

    int getMethodAction() {
        return mFlags & METHOD_ACTION_MASK;
}

    public void dump() {
        System.out.printf("%s [%d, %d]\n", mName, mGlobalStartTime, mGlobalEndTime);
}

    public void setRecursive(boolean isRecursive) {
        if (isRecursive) {
            mFlags |= IS_RECURSIVE;
        } else {
            mFlags &= ~IS_RECURSIVE;
        }
}

public boolean isRecursive() {
        return (mFlags & IS_RECURSIVE) != 0;
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index dcf17a2..075485d 100644

//Synthetic comment -- @@ -35,25 +35,37 @@
import java.util.regex.Pattern;

public class DmTraceReader extends TraceReader {

    private int mVersionNumber = 0;
    private boolean mDebug = false;
private static final int TRACE_MAGIC = 0x574f4c53;
private boolean mRegression;
private ProfileProvider mProfileProvider;
private String mTraceFileName;
private MethodData mTopLevel;
private ArrayList<Call> mCallList;
    private ArrayList<Call> mSwitchList;
private HashMap<String, String> mPropertiesMap;
private HashMap<Integer, MethodData> mMethodMap;
private HashMap<Integer, ThreadData> mThreadMap;
private ThreadData[] mSortedThreads;
private MethodData[] mSortedMethods;
    private long mGlobalEndTime;
private MethodData mContextSwitch;
    private int mOffsetToData;
    private byte[] mBytes = new byte[8];

// A regex for matching the thread "id name" lines in the .key file
private static final Pattern mIdNamePattern = Pattern.compile("(\\d+)\t(.*)");  //$NON-NLS-1$
//Synthetic comment -- @@ -64,14 +76,15 @@
mPropertiesMap = new HashMap<String, String>();
mMethodMap = new HashMap<Integer, MethodData>();
mThreadMap = new HashMap<Integer, ThreadData>();

// Create a single top-level MethodData object to hold the profile data
// for time spent in the unknown caller.
mTopLevel = new MethodData(0, "(toplevel)");
mContextSwitch = new MethodData(-1, "(context switch)");
mMethodMap.put(0, mTopLevel);
generateTrees();
        // dumpTrees();
}

void generateTrees() {
//Synthetic comment -- @@ -92,38 +105,6 @@
return mProfileProvider;
}

    Call readCall(MappedByteBuffer buffer, Call call) {
        int threadId;
        int methodId;
        long time;
        
        try {
            if (mVersionNumber == 1)
                threadId = buffer.get();
            else
                threadId = buffer.getShort();
            methodId = buffer.getInt();
            time = buffer.getInt();
        } catch (BufferUnderflowException ex) {
            return null;
        }
        
        int methodAction = methodId & 0x03;
        methodId = methodId & ~0x03;
        MethodData methodData = mMethodMap.get(methodId);
        if (methodData == null) {
            String name = String.format("(0x%1$x)", methodId);  //$NON-NLS-1$
            methodData = new MethodData(methodId, name);
        }
        
        if (call != null) {
            call.set(threadId, methodData, time, methodAction);
        } else {
            call = new Call(threadId, methodData, time, methodAction);
        }
        return call;
    }
    
private MappedByteBuffer mapFile(String filename, long offset) {
MappedByteBuffer buffer = null;
try {
//Synthetic comment -- @@ -151,165 +132,273 @@
magic, TRACE_MAGIC);
throw new RuntimeException();
}
// read version
int version = buffer.getShort();
        
// read offset
        mOffsetToData = buffer.getShort() - 16;
        
// read startWhen
buffer.getLong();
        
        // Skip over "mOffsetToData" bytes
        for (int ii = 0; ii < mOffsetToData; ii++) {
buffer.get();
}
        
        // Save this position so that we can re-read the data later
        buffer.mark();
}

private void parseData(long offset) {
MappedByteBuffer buffer = mapFile(mTraceFileName, offset);
readDataFileHeader(buffer);
        parseDataPass1(buffer);
        
        buffer.reset();
        parseDataPass2(buffer);
    }
    
    private void parseDataPass1(MappedByteBuffer buffer) {
        mSwitchList = new ArrayList<Call>();

        // Read the first call so that we can set "prevThreadData"
        Call call = new Call();
        call = readCall(buffer, call);
        if (call == null)
            return;
        long callTime = call.mThreadStartTime;
        long prevCallTime = 0;
        ThreadData threadData = mThreadMap.get(call.getThreadId());
        if (threadData == null) {
            String name = String.format("[%1$d]", call.getThreadId());  //$NON-NLS-1$
            threadData = new ThreadData(call.getThreadId(), name, mTopLevel);
            mThreadMap.put(call.getThreadId(), threadData);
}
        ThreadData prevThreadData = threadData;
        while (true) {
            // If a context switch occurred, then insert a placeholder "call"
            // record so that we can do something reasonable with the global
            // timestamps.
            if (prevThreadData != threadData) {
                Call switchEnter = new Call(prevThreadData.getId(),
                        mContextSwitch, prevCallTime, 0);
                prevThreadData.setLastContextSwitch(switchEnter);
                mSwitchList.add(switchEnter);
                Call contextSwitch = threadData.getLastContextSwitch();
                if (contextSwitch != null) {
                    long prevStartTime = contextSwitch.mThreadStartTime;
                    long elapsed = callTime - prevStartTime;
                    long beforeSwitch = elapsed / 2;
                    long afterSwitch = elapsed - beforeSwitch;
                    long exitTime = callTime - afterSwitch;
                    contextSwitch.mThreadStartTime = prevStartTime + beforeSwitch;
                    Call switchExit = new Call(threadData.getId(),
                            mContextSwitch, exitTime, 1);
                    
                    mSwitchList.add(switchExit);
                }
                prevThreadData = threadData;
            }

            // Read the next call
            call = readCall(buffer, call);
            if (call == null) {
break;
}
            prevCallTime = callTime;
            callTime = call.mThreadStartTime;

            threadData = mThreadMap.get(call.getThreadId());
if (threadData == null) {
                String name = String.format("[%d]", call.getThreadId());
                threadData = new ThreadData(call.getThreadId(), name, mTopLevel);
                mThreadMap.put(call.getThreadId(), threadData);
}
        }
    }

    void parseDataPass2(MappedByteBuffer buffer) {
        mCallList = new ArrayList<Call>();

        // Read the first call so that we can set "prevThreadData"
        Call call = readCall(buffer, null);
        long callTime = call.mThreadStartTime;
        long prevCallTime = callTime;
        ThreadData threadData = mThreadMap.get(call.getThreadId());
        ThreadData prevThreadData = threadData;
        threadData.setGlobalStartTime(0);
        
        int nthContextSwitch = 0;

        // Assign a global timestamp to each event.
        long globalTime = 0;
        while (true) {
            long elapsed = callTime - prevCallTime;
            if (threadData != prevThreadData) {
                // Get the next context switch.  This one is entered
                // by the previous thread.
                Call contextSwitch = mSwitchList.get(nthContextSwitch++);
                mCallList.add(contextSwitch);
                elapsed = contextSwitch.mThreadStartTime - prevCallTime;
                globalTime += elapsed;
                elapsed = 0;
                contextSwitch.mGlobalStartTime = globalTime;
                prevThreadData.handleCall(contextSwitch, globalTime);
                
                if (!threadData.isEmpty()) {
                    // This context switch is exited by the current thread.
                    contextSwitch = mSwitchList.get(nthContextSwitch++);
                    mCallList.add(contextSwitch);
                    contextSwitch.mGlobalStartTime = globalTime;
                    elapsed = callTime - contextSwitch.mThreadStartTime;
                    threadData.handleCall(contextSwitch, globalTime);
}

                // If the thread's global start time has not been set yet,
                // then set it.
                if (threadData.getGlobalStartTime() == -1)
                    threadData.setGlobalStartTime(globalTime);
prevThreadData = threadData;
}

            globalTime += elapsed;
            call.mGlobalStartTime = globalTime;
            
            threadData.handleCall(call, globalTime);
            mCallList.add(call);
            
            // Read the next call
            call = readCall(buffer, null);
            if (call == null) {
                break;
            }
            prevCallTime = callTime;
            callTime = call.mThreadStartTime;
            threadData = mThreadMap.get(call.getThreadId());
}

        // Allow each thread to do any cleanup of the call stack.
        // Also add the elapsed time for each thread to the toplevel
        // method's inclusive time.
        for (int id : mThreadMap.keySet()) {
            threadData = mThreadMap.get(id);
            long endTime = threadData.endTrace();
            if (endTime > 0)
                mTopLevel.addElapsedInclusive(endTime, false, null);
}

        mGlobalEndTime = globalTime;
        
if (mRegression) {
dumpCallTimes();
}
}
//Synthetic comment -- @@ -353,7 +442,7 @@
continue;
}
if (line.equals("*end")) {
                    return offset;
}
}
switch (mode) {
//Synthetic comment -- @@ -372,6 +461,11 @@
break;
}
}
}

void parseOption(String line) {
//Synthetic comment -- @@ -380,6 +474,16 @@
String key = tokens[0];
String value = tokens[1];
mPropertiesMap.put(key, value);
}
}

//Synthetic comment -- @@ -435,38 +539,30 @@
}

private void analyzeData() {
// Sort the threads into decreasing cpu time
Collection<ThreadData> tv = mThreadMap.values();
mSortedThreads = tv.toArray(new ThreadData[tv.size()]);
Arrays.sort(mSortedThreads, new Comparator<ThreadData>() {
public int compare(ThreadData td1, ThreadData td2) {
                if (td2.getCpuTime() > td1.getCpuTime())
return 1;
                if (td2.getCpuTime() < td1.getCpuTime())
return -1;
return td2.getName().compareTo(td1.getName());
}
});

        // Analyze the call tree so that we can label the "worst" children.
        // Also set all the root pointers in each node in the call tree.
        long sum = 0;
        for (ThreadData t : mSortedThreads) {
            if (t.isEmpty() == false) {
                Call root = t.getCalltreeRoot();
                root.mGlobalStartTime = t.getGlobalStartTime();
            }
        }

// Sort the methods into decreasing inclusive time
Collection<MethodData> mv = mMethodMap.values();
MethodData[] methods;
methods = mv.toArray(new MethodData[mv.size()]);
Arrays.sort(methods, new Comparator<MethodData>() {
public int compare(MethodData md1, MethodData md2) {
                if (md2.getElapsedInclusive() > md1.getElapsedInclusive())
return 1;
                if (md2.getElapsedInclusive() < md1.getElapsedInclusive())
return -1;
return md1.getName().compareTo(md2.getName());
}
//Synthetic comment -- @@ -475,7 +571,7 @@
// Count the number of methods with non-zero inclusive time
int nonZero = 0;
for (MethodData md : methods) {
            if (md.getElapsedInclusive() == 0)
break;
nonZero += 1;
}
//Synthetic comment -- @@ -484,7 +580,7 @@
mSortedMethods = new MethodData[nonZero];
int ii = 0;
for (MethodData md : methods) {
            if (md.getElapsedInclusive() == 0)
break;
md.setRank(ii);
mSortedMethods[ii++] = md;
//Synthetic comment -- @@ -492,7 +588,7 @@

// Let each method analyze its profile data
for (MethodData md : mSortedMethods) {
            md.analyzeData();
}

// Update all the calls to include the method rank in
//Synthetic comment -- @@ -522,67 +618,65 @@
// entire execution of the thread.
for (ThreadData threadData : mSortedThreads) {
if (!threadData.isEmpty() && threadData.getId() != 0) {
                Call call = new Call(threadData.getId(), mTopLevel,
                        threadData.getGlobalStartTime(), 0);
                call.mGlobalStartTime = threadData.getGlobalStartTime();
                call.mGlobalEndTime = threadData.getGlobalEndTime();
                record = new TimeLineView.Record(threadData, call);
timeRecs.add(record);
}
}

for (Call call : mCallList) {
            if (call.getMethodAction() != 0 || call.getThreadId() == 0)
                continue;
            ThreadData threadData = mThreadMap.get(call.getThreadId());
            record = new TimeLineView.Record(threadData, call);
timeRecs.add(record);
}
        
if (mRegression) {
dumpTimeRecs(timeRecs);
System.exit(0);
}
return timeRecs;
}
        
private void dumpCallTimes() {
        String action;
        
        System.out.format("id thread  global start,end   method\n");
for (Call call : mCallList) {
            if (call.getMethodAction() == 0) {
                action = "+";
            } else {
                action = " ";
            }
            long callTime = call.mThreadStartTime;
            System.out.format("%2d %6d %8d %8d %s %s\n",
                    call.getThreadId(), callTime, call.mGlobalStartTime,
                    call.mGlobalEndTime, action, call.getMethodData().getName());
//            if (call.getMethodAction() == 0 && call.getGlobalEndTime() < call.getGlobalStartTime()) {
//                System.out.printf("endtime %d < startTime %d\n",
//                        call.getGlobalEndTime(), call.getGlobalStartTime());
//            }
}
}

private void dumpMethodStats() {
        System.out.format("\nExclusive Inclusive     Calls  Method\n");
for (MethodData md : mSortedMethods) {
System.out.format("%9d %9d %9s  %s\n",
                    md.getElapsedExclusive(), md.getElapsedInclusive(),
md.getCalls(), md.getProfileName());
}
}

private void dumpTimeRecs(ArrayList<TimeLineView.Record> timeRecs) {
        System.out.format("\nid thread  global start,end  method\n");
for (TimeLineView.Record record : timeRecs) {
Call call = (Call) record.block;
            long callTime = call.mThreadStartTime;
            System.out.format("%2d %6d %8d %8d  %s\n",
                    call.getThreadId(), callTime,
call.mGlobalStartTime, call.mGlobalEndTime,
call.getMethodData().getName());
}
//Synthetic comment -- @@ -608,12 +702,48 @@
}

@Override
    public long getEndTime() {
        return mGlobalEndTime;
}

@Override
public HashMap<String, String> getProperties() {
return mPropertiesMap;
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/MethodData.java b/traceview/src/com/android/traceview/MethodData.java
//Synthetic comment -- index d54b72b..47f6df4 100644

//Synthetic comment -- @@ -36,9 +36,12 @@
private String mProfileName;
private String mPathname;
private int mLineNumber;
    private long mElapsedExclusive;
    private long mElapsedInclusive;
    private long mTopExclusive;
private int[] mNumCalls = new int[2]; // index 0=normal, 1=recursive
private Color mColor;
private Color mFadedColor;
//Synthetic comment -- @@ -81,16 +84,6 @@
computeProfileName();
}

    private Comparator<ProfileData> mByElapsedInclusive = new Comparator<ProfileData>() {
        public int compare(ProfileData pd1, ProfileData pd2) {
            if (pd2.getElapsedInclusive() > pd1.getElapsedInclusive())
                return 1;
            if (pd2.getElapsedInclusive() < pd1.getElapsedInclusive())
                return -1;
            return 0;
        }
    };

public double addWeight(int x, int y, double weight) {
if (mX == x && mY == y)
mWeight += weight;
//Synthetic comment -- @@ -115,13 +108,16 @@
computeProfileName();
}

    public void addElapsedExclusive(long time) {
        mElapsedExclusive += time;
}

    public void addElapsedInclusive(long time, boolean isRecursive, Call parent) {
if (isRecursive == false) {
            mElapsedInclusive += time;
mNumCalls[0] += 1;
} else {
mNumCalls[1] += 1;
//Synthetic comment -- @@ -131,27 +127,27 @@
return;

// Find the child method in the parent
        MethodData parentMethod = parent.mMethodData;
if (parent.isRecursive()) {
            parentMethod.mRecursiveChildren = updateInclusive(time,
parentMethod, this, false,
parentMethod.mRecursiveChildren);
} else {
            parentMethod.mChildren = updateInclusive(time,
parentMethod, this, false, parentMethod.mChildren);
}

// Find the parent method in the child
if (isRecursive) {
            mRecursiveParents = updateInclusive(time, this, parentMethod, true,
mRecursiveParents);
} else {
            mParents = updateInclusive(time, this, parentMethod, true,
mParents);
}
}

    private HashMap<Integer, ProfileData> updateInclusive(long time,
MethodData contextMethod, MethodData elementMethod,
boolean elementIsParent, HashMap<Integer, ProfileData> map) {
if (map == null) {
//Synthetic comment -- @@ -159,30 +155,30 @@
} else {
ProfileData profileData = map.get(elementMethod.mId);
if (profileData != null) {
                profileData.addElapsedInclusive(time);
return map;
}
}

ProfileData elementData = new ProfileData(contextMethod,
elementMethod, elementIsParent);
        elementData.setElapsedInclusive(time);
elementData.setNumCalls(1);
map.put(elementMethod.mId, elementData);
return map;
}

    public void analyzeData() {
// Sort the parents and children into decreasing inclusive time
ProfileData[] sortedParents;
ProfileData[] sortedChildren;
ProfileData[] sortedRecursiveParents;
ProfileData[] sortedRecursiveChildren;

        sortedParents = sortProfileData(mParents);
        sortedChildren = sortProfileData(mChildren);
        sortedRecursiveParents = sortProfileData(mRecursiveParents);
        sortedRecursiveChildren = sortProfileData(mRecursiveChildren);

// Add "self" time to the top of the sorted children
sortedChildren = addSelf(sortedChildren);
//Synthetic comment -- @@ -215,7 +211,8 @@

// Create and return a ProfileData[] array that is a sorted copy
// of the given HashMap values.
    private ProfileData[] sortProfileData(HashMap<Integer, ProfileData> map) {
if (map == null)
return null;

//Synthetic comment -- @@ -224,7 +221,15 @@
ProfileData[] sorted = values.toArray(new ProfileData[values.size()]);

// Sort the array by elapsed inclusive time
        Arrays.sort(sorted, mByElapsedInclusive);
return sorted;
}

//Synthetic comment -- @@ -240,12 +245,17 @@
return pdata;
}

    public void addTopExclusive(long time) {
        mTopExclusive += time;
}

    public long getTopExclusive() {
        return mTopExclusive;
}

public int getId() {
//Synthetic comment -- @@ -329,12 +339,20 @@
return getName();
}

    public long getElapsedExclusive() {
        return mElapsedExclusive;
}

    public long getElapsedInclusive() {
        return mElapsedInclusive;
}

public void setFadedColor(Color fadedColor) {
//Synthetic comment -- @@ -379,17 +397,31 @@
int result = md1.getName().compareTo(md2.getName());
return (mDirection == Direction.INCREASING) ? result : -result;
}
            if (mColumn == Column.BY_INCLUSIVE) {
                if (md2.getElapsedInclusive() > md1.getElapsedInclusive())
return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedInclusive() < md1.getElapsedInclusive())
return (mDirection == Direction.INCREASING) ? 1 : -1;
return md1.getName().compareTo(md2.getName());
}
            if (mColumn == Column.BY_EXCLUSIVE) {
                if (md2.getElapsedExclusive() > md1.getElapsedExclusive())
return (mDirection == Direction.INCREASING) ? -1 : 1;
                if (md2.getElapsedExclusive() < md1.getElapsedExclusive())
return (mDirection == Direction.INCREASING) ? 1 : -1;
return md1.getName().compareTo(md2.getName());
}
//Synthetic comment -- @@ -399,10 +431,25 @@
return md1.getName().compareTo(md2.getName());
return (mDirection == Direction.INCREASING) ? result : -result;
}
            if (mColumn == Column.BY_TIME_PER_CALL) {
                double time1 = md1.getElapsedInclusive();
time1 = time1 / md1.getTotalCalls();
                double time2 = md2.getElapsedInclusive();
time2 = time2 / md2.getTotalCalls();
double diff = time1 - time2;
int result = 0;
//Synthetic comment -- @@ -449,7 +496,9 @@
}

public static enum Column {
            BY_NAME, BY_EXCLUSIVE, BY_INCLUSIVE, BY_CALLS, BY_TIME_PER_CALL
};

public static enum Direction {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileData.java b/traceview/src/com/android/traceview/ProfileData.java
//Synthetic comment -- index f0c1d61..e3c47fb 100644

//Synthetic comment -- @@ -24,7 +24,8 @@
/** mContext is either the parent or child of mElement */
protected MethodData mContext;
protected boolean mElementIsParent;
    protected long mElapsedInclusive;
protected int mNumCalls;

public ProfileData() {
//Synthetic comment -- @@ -45,17 +46,23 @@
return mElement;
}

    public void addElapsedInclusive(long elapsedInclusive) {
        mElapsedInclusive += elapsedInclusive;
mNumCalls += 1;
}

    public void setElapsedInclusive(long elapsedInclusive) {
        mElapsedInclusive = elapsedInclusive;
}

    public long getElapsedInclusive() {
        return mElapsedInclusive;
}

public void setNumCalls(int numCalls) {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileProvider.java b/traceview/src/com/android/traceview/ProfileProvider.java
//Synthetic comment -- index fe5c832..63a1da6 100644

//Synthetic comment -- @@ -44,26 +44,40 @@
private TraceReader mReader;
private Image mSortUp;
private Image mSortDown;
    private String mColumnNames[] = { "Name", "Incl %", "Inclusive", "Excl %",
            "Exclusive", "Calls+Recur\nCalls/Total", "Time/Call" };
    private int mColumnWidths[] = { 370, 70, 70, 70, 70, 90, 70 };
    private int mColumnAlignments[] = { SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
            SWT.RIGHT, SWT.RIGHT, SWT.CENTER, SWT.RIGHT };
private static final int COL_NAME = 0;
    private static final int COL_INCLUSIVE_PER = 1;
    private static final int COL_INCLUSIVE = 2;
    private static final int COL_EXCLUSIVE_PER = 3;
    private static final int COL_EXCLUSIVE = 4;
    private static final int COL_CALLS = 5;
    private static final int COL_TIME_PER_CALL = 6;
    private long mTotalTime;
private Pattern mUppercase;
private int mPrevMatchIndex = -1;

public ProfileProvider(TraceReader reader) {
mRoots = reader.getMethods();
mReader = reader;
        mTotalTime = reader.getEndTime();
Display display = Display.getCurrent();
InputStream in = getClass().getClassLoader().getResourceAsStream(
"icons/sort_up.png");
//Synthetic comment -- @@ -126,7 +140,22 @@
}

public int[] getColumnWidths() {
        return mColumnWidths;
}

public int[] getColumnAlignments() {
//Synthetic comment -- @@ -201,31 +230,58 @@
MethodData md = (MethodData) element;
if (col == COL_NAME)
return md.getProfileName();
                if (col == COL_EXCLUSIVE) {
                    double val = md.getElapsedExclusive();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_EXCLUSIVE_PER) {
                    double val = md.getElapsedExclusive();
                    double per = val * 100.0 / mTotalTime;
return String.format("%.1f%%", per);
}
                if (col == COL_INCLUSIVE) {
                    double val = md.getElapsedInclusive();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_PER) {
                    double val = md.getElapsedInclusive();
                    double per = val * 100.0 / mTotalTime;
return String.format("%.1f%%", per);
}
if (col == COL_CALLS)
return md.getCalls();
                if (col == COL_TIME_PER_CALL) {
int numCalls = md.getTotalCalls();
                    double val = md.getElapsedInclusive();
val = val / numCalls;
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
//Synthetic comment -- @@ -234,16 +290,29 @@
ProfileSelf ps = (ProfileSelf) element;
if (col == COL_NAME)
return ps.getProfileName();
                if (col == COL_INCLUSIVE) {
                    double val = ps.getElapsedInclusive();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_PER) {
double total;
                    double val = ps.getElapsedInclusive();
MethodData context = ps.getContext();
                    total = context.getElapsedInclusive();
double per = val * 100.0 / total;
return String.format("%.1f%%", per);
}
//Synthetic comment -- @@ -252,16 +321,29 @@
ProfileData pd = (ProfileData) element;
if (col == COL_NAME)
return pd.getProfileName();
                if (col == COL_INCLUSIVE) {
                    double val = pd.getElapsedInclusive();
val = traceUnits.getScaledValue(val);
return String.format("%.3f", val);
}
                if (col == COL_INCLUSIVE_PER) {
double total;
                    double val = pd.getElapsedInclusive();
MethodData context = pd.getContext();
                    total = context.getElapsedInclusive();
double per = val * 100.0 / total;
return String.format("%.1f%%", per);
}
//Synthetic comment -- @@ -330,23 +412,38 @@
// Sort names alphabetically
sorter.setColumn(MethodData.Sorter.Column.BY_NAME);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_EXCLUSIVE_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_EXCLUSIVE);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_INCLUSIVE_PER]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_INCLUSIVE);
Arrays.sort(mRoots, sorter);
} else if (name == mColumnNames[COL_CALLS]) {
sorter.setColumn(MethodData.Sorter.Column.BY_CALLS);
Arrays.sort(mRoots, sorter);
            } else if (name == mColumnNames[COL_TIME_PER_CALL]) {
                sorter.setColumn(MethodData.Sorter.Column.BY_TIME_PER_CALL);
Arrays.sort(mRoots, sorter);
}
MethodData.Sorter.Direction direction = sorter.getDirection();








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileSelf.java b/traceview/src/com/android/traceview/ProfileSelf.java
//Synthetic comment -- index 3a4f3d9..45543b2 100644

//Synthetic comment -- @@ -28,7 +28,12 @@
}

@Override
    public long getElapsedInclusive() {
        return mElement.getTopExclusive();
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileView.java b/traceview/src/com/android/traceview/ProfileView.java
//Synthetic comment -- index 506efca..8889b13 100644

//Synthetic comment -- @@ -279,7 +279,7 @@
}
if (name == "Call") {
Call call = (Call) selection.getValue();
                MethodData md = call.mMethodData;
highlightMethod(md, true);
return;
}
//Synthetic comment -- @@ -304,9 +304,11 @@
mTreeViewer.setSelection(sel, true);
Tree tree = mTreeViewer.getTree();
TreeItem[] items = tree.getSelection();
        tree.setTopItem(items[0]);
        // workaround a Mac bug by adding showItem().
        tree.showItem(items[0]);
}

private void expandNode(MethodData md) {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/QtraceReader.java b/traceview/src/com/android/traceview/QtraceReader.java
deleted file mode 100644
//Synthetic comment -- index c4db4a2..0000000

//Synthetic comment -- @@ -1,45 +0,0 @@
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

import java.util.ArrayList;
import java.util.HashMap;

public class QtraceReader extends TraceReader {
    QtraceReader(String traceName) {
    }

    @Override
    public MethodData[] getMethods() {
        return null;
    }

    @Override
    public HashMap<Integer, String> getThreadLabels() {
        return null;
    }

    @Override
    public ArrayList<TimeLineView.Record> getThreadTimeRecords() {
        return null;
    }

    @Override
    public ProfileProvider getProfileProvider() {
        return null;
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ThreadData.java b/traceview/src/com/android/traceview/ThreadData.java
//Synthetic comment -- index 54ea891..f71e9c2 100644

//Synthetic comment -- @@ -23,158 +23,130 @@

private int mId;
private String mName;
    private long mGlobalStartTime = -1;
    private long mGlobalEndTime = -1;
    private long mLastEventTime;
    private long mCpuTime;
    private Call mRoot;
    private Call mCurrent;
    private Call mLastContextSwitch;
private ArrayList<Call> mStack = new ArrayList<Call>();
    
// This is a hash of all the methods that are currently on the stack.
private HashMap<MethodData, Integer> mStackMethods = new HashMap<MethodData, Integer>();
    
    // True if no calls have ever been added to this thread
    private boolean mIsEmpty;

ThreadData(int id, String name, MethodData topLevel) {
mId = id;
mName = String.format("[%d] %s", id, name);
        mRoot = new Call(mName, topLevel);
        mCurrent = mRoot;
mIsEmpty = true;
    }

    public boolean isEmpty() {
        return mIsEmpty;
}

public String getName() {
return mName;
}

    public Call getCalltreeRoot() {
        return mRoot;
}

    void handleCall(Call call, long globalTime) {
        mIsEmpty = false;
        long currentTime = call.mThreadStartTime;
        if (currentTime < mLastEventTime) {
            System.err
            .printf(
                    "ThreadData: '%1$s' call time (%2$d) is less than previous time (%3$d) for thread '%4$s'\n",
                    call.getName(), currentTime, mLastEventTime, mName);
            System.exit(1);
        }
        long elapsed = currentTime - mLastEventTime;
        mCpuTime += elapsed;
        if (call.getMethodAction() == 0) {
            // This is a method entry.
            enter(call, elapsed);
        } else {
            // This is a method exit.
            exit(call, elapsed, globalTime);
        }
        mLastEventTime = currentTime;
        mGlobalEndTime = globalTime;
}

    private void enter(Call c, long elapsed) {
        Call caller = mCurrent;
        push(c);
        
        // Check the stack for a matching method to determine if this call
        // is recursive.
        MethodData md = c.mMethodData;
        Integer num = mStackMethods.get(md);
if (num == null) {
num = 0;
} else if (num > 0) {
            c.setRecursive(true);
}
        num += 1;
        mStackMethods.put(md, num);
        mCurrent = c;

        // Add the elapsed time to the caller's exclusive time
        caller.addExclusiveTime(elapsed);
}

    private void exit(Call c, long elapsed, long globalTime) {
        mCurrent.mGlobalEndTime = globalTime;
        Call top = pop();
        if (top == null) {
            return;
}

        if (mCurrent.mMethodData != c.mMethodData) {
            String error = "Method exit (" + c.getName()
                    + ") does not match current method (" + mCurrent.getName()
+ ")";
throw new RuntimeException(error);
        } else {
            long duration = c.mThreadStartTime - mCurrent.mThreadStartTime;
            Call caller = top();
            mCurrent.addExclusiveTime(elapsed);
            mCurrent.addInclusiveTime(duration, caller);
            if (caller == null) {
                caller = mRoot;
            }
            mCurrent = caller;
}
    }

    public void push(Call c) {
        mStack.add(c);
    }

    public Call pop() {
        ArrayList<Call> stack = mStack;
        if (stack.size() == 0)
            return null;
        Call top = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        
        // Decrement the count on the method in the hash table and remove
        // the entry when it goes to zero.
        MethodData md = top.mMethodData;
        Integer num = mStackMethods.get(md);
if (num != null) {
            num -= 1;
            if (num <= 0) {
                mStackMethods.remove(md);
} else {
                mStackMethods.put(md, num);
}
}
        return top;
}

    public Call top() {
        ArrayList<Call> stack = mStack;
        if (stack.size() == 0)
            return null;
        return stack.get(stack.size() - 1);
}

    public long endTrace() {
        // If we have calls on the stack when the trace ends, then clean up
        // the stack and compute the inclusive time of the methods by pretending
        // that we are exiting from their methods now.
        while (mCurrent != mRoot) {
            long duration = mLastEventTime - mCurrent.mThreadStartTime;
            pop();
            Call caller = top();
            mCurrent.addInclusiveTime(duration, caller);
            mCurrent.mGlobalEndTime = mGlobalEndTime;
            if (caller == null) {
                caller = mRoot;
}
            mCurrent = caller;
}
        return mLastEventTime;
}

@Override
//Synthetic comment -- @@ -186,43 +158,11 @@
return mId;
}

    public void setCpuTime(long cpuTime) {
        mCpuTime = cpuTime;
    }

public long getCpuTime() {
        return mCpuTime;
}

    public void setGlobalStartTime(long globalStartTime) {
        mGlobalStartTime = globalStartTime;
    }

    public long getGlobalStartTime() {
        return mGlobalStartTime;
    }

    public void setLastEventTime(long lastEventTime) {
        mLastEventTime = lastEventTime;
    }

    public long getLastEventTime() {
        return mLastEventTime;
    }

    public void setGlobalEndTime(long globalEndTime) {
        mGlobalEndTime = globalEndTime;
    }

    public long getGlobalEndTime() {
        return mGlobalEndTime;
    }

    public void setLastContextSwitch(Call lastContextSwitch) {
        mLastContextSwitch = lastContextSwitch;
    }

    public Call getLastContextSwitch() {
        return mLastContextSwitch;
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeBase.java b/traceview/src/com/android/traceview/TimeBase.java
new file mode 100644
//Synthetic comment -- index 0000000..b6b23cb

//Synthetic comment -- @@ -0,0 +1,71 @@








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeLineView.java b/traceview/src/com/android/traceview/TimeLineView.java
//Synthetic comment -- index c9eb8e7..b04a411 100644

//Synthetic comment -- @@ -54,10 +54,8 @@
public class TimeLineView extends Composite implements Observer {

private HashMap<String, RowData> mRowByName;
    private double mTotalElapsed;
private RowData[] mRows;
private Segment[] mSegments;
    private ArrayList<Segment> mSegmentList = new ArrayList<Segment>();
private HashMap<Integer, String> mThreadLabels;
private Timescale mTimescale;
private Surface mSurface;
//Synthetic comment -- @@ -89,19 +87,21 @@
private static final int rowYSpace = rowHeight + rowYMargin;
private static final int majorTickLength = 8;
private static final int minorTickLength = 4;
    private static final int timeLineOffsetY = 38;
private static final int tickToFontSpacing = 2;

/** start of first row */
    private static final int topMargin = 70;
private int mMouseRow = -1;
private int mNumRows;
private int mStartRow;
private int mEndRow;
private TraceUnits mUnits;
private int mSmallFontWidth;
private int mSmallFontHeight;
    private int mMediumFontWidth;
private SelectionController mSelectionController;
private MethodData mHighlightMethodData;
private Call mHighlightCall;
//Synthetic comment -- @@ -118,6 +118,13 @@
public Color getColor();
public double addWeight(int x, int y, double weight);
public void clearWeight();
}

public static interface Row {
//Synthetic comment -- @@ -142,6 +149,9 @@
this.mSelectionController = selectionController;
selectionController.addObserver(this);
mUnits = reader.getTraceUnits();
mThreadLabels = reader.getThreadLabels();

Display display = getDisplay();
//Synthetic comment -- @@ -169,11 +179,6 @@
mSmallFontWidth = gc.getFontMetrics().getAverageCharWidth();
mSmallFontHeight = gc.getFontMetrics().getHeight();

        if (mSetFonts) {
            gc.setFont(mFontRegistry.get("medium"));  //$NON-NLS-1$
        }
        mMediumFontWidth = gc.getFontMetrics().getAverageCharWidth();

image.dispose();
gc.dispose();

//Synthetic comment -- @@ -403,6 +408,8 @@
}
});

// The records are sorted into increasing start time,
// so the minimum start time is the start time of the first record.
double minVal = 0;
//Synthetic comment -- @@ -415,6 +422,10 @@
for (Record rec : records) {
Row row = rec.row;
Block block = rec.block;
String rowName = row.getName();
RowData rd = mRowByName.get(rowName);
if (rd == null) {
//Synthetic comment -- @@ -426,7 +437,6 @@
if (blockEndTime > rd.mEndTime) {
long start = Math.max(blockStartTime, rd.mEndTime);
rd.mElapsed += blockEndTime - start;
                mTotalElapsed += blockEndTime - start;
rd.mEndTime = blockEndTime;
}
if (blockEndTime > maxVal)
//Synthetic comment -- @@ -447,7 +457,7 @@
if (topStartTime < blockStartTime) {
Segment segment = new Segment(rd, top, topStartTime,
blockStartTime);
                    mSegmentList.add(segment);
}

// If this block starts where the previous (top) block ends,
//Synthetic comment -- @@ -457,7 +467,7 @@
rd.push(block);
} else {
// We may have to pop several frames here.
                popFrames(rd, top, blockStartTime);
rd.push(block);
}
}
//Synthetic comment -- @@ -465,7 +475,7 @@
// Clean up the stack of each row
for (RowData rd : mRowByName.values()) {
Block top = rd.top();
            popFrames(rd, top, Integer.MAX_VALUE);
}

mSurface.setRange(minVal, maxVal);
//Synthetic comment -- @@ -495,7 +505,7 @@

// Sort the blocks into increasing rows, and within rows into
// increasing start values.
        mSegments = mSegmentList.toArray(new Segment[mSegmentList.size()]);
Arrays.sort(mSegments, new Comparator<Segment>() {
public int compare(Segment bd1, Segment bd2) {
RowData rd1 = bd1.mRowData;
//Synthetic comment -- @@ -524,13 +534,14 @@
}
}

    private void popFrames(RowData rd, Block top, long startTime) {
long topEndTime = top.getEndTime();
long lastEndTime = top.getStartTime();
while (topEndTime <= startTime) {
if (topEndTime > lastEndTime) {
Segment segment = new Segment(rd, top, lastEndTime, topEndTime);
                mSegmentList.add(segment);
lastEndTime = topEndTime;
}
rd.pop();
//Synthetic comment -- @@ -543,7 +554,7 @@
// If we get here, then topEndTime > startTime
if (lastEndTime < startTime) {
Segment bd = new Segment(rd, top, lastEndTime, startTime);
            mSegmentList.add(bd);
}
}

//Synthetic comment -- @@ -648,7 +659,9 @@
private Cursor mZoomCursor;
private String mMethodName = null;
private Color mMethodColor = null;
private int mMethodStartY;
private int mMarkStartX;
private int mMarkEndX;

//Synthetic comment -- @@ -662,6 +675,7 @@
mZoomCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
setCursor(mZoomCursor);
mMethodStartY = mSmallFontHeight + 1;
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
        
private void mouseMove(MouseEvent me) {
me.y = -1;
mSurface.mouseMove(me);
//Synthetic comment -- @@ -734,7 +752,10 @@

// Draw the method name and color, if needed
drawMethod(display, gcImage);
            
// Draw the off-screen buffer to the screen
gc.drawImage(image, 0, 0);

//Synthetic comment -- @@ -771,7 +792,11 @@
// Display the maximum data value
double maxVal = mScaleInfo.getMaxVal();
info = mUnits.labelledString(maxVal);
            info = String.format(" max %s ", info);  //$NON-NLS-1$
Point extent = gc.stringExtent(info);
Point dim = getSize();
int x1 = dim.x - RightMargin - extent.x;
//Synthetic comment -- @@ -791,7 +816,17 @@
x1 += width + METHOD_BLOCK_MARGIN;
gc.drawString(mMethodName, x1, y1, true);
}
        
private void drawTicks(Display display, GC gc) {
Point dim = getSize();
int y2 = majorTickLength + timeLineOffsetY;
//Synthetic comment -- @@ -1049,6 +1084,7 @@

String blockName = null;
Color blockColor = null;

if (mDebug) {
double pixelsPerRange = mScaleInfo.getPixelsPerRange();
//Synthetic comment -- @@ -1073,8 +1109,30 @@
if (mMouseRow == strip.mRowData.mRank) {
if (mMouse.x >= strip.mX
&& mMouse.x < strip.mX + strip.mWidth) {
                        blockName = strip.mSegment.mBlock.getName();
blockColor = strip.mColor;
}
if (mMouseSelect.x >= strip.mX
&& mMouseSelect.x < strip.mX + strip.mWidth) {
//Synthetic comment -- @@ -1125,6 +1183,7 @@
if (blockName != null) {
mTimescale.setMethodName(blockName);
mTimescale.setMethodColor(blockColor);
mShowHighlightName = false;
} else if (mShowHighlightName) {
// Draw the highlighted method name
//Synthetic comment -- @@ -1136,10 +1195,12 @@
if (md != null) {
mTimescale.setMethodName(md.getProfileName());
mTimescale.setMethodColor(md.getColor());
}
} else {
mTimescale.setMethodName(null);
mTimescale.setMethodColor(null);
}
mTimescale.redraw();

//Synthetic comment -- @@ -1152,7 +1213,7 @@
}

private void drawHighlights(GC gc, Point dim) {
            int height = highlightHeight;
if (height <= 0)
return;
for (Range range : mHighlightExclusive) {
//Synthetic comment -- @@ -1278,13 +1339,15 @@
long callEnd = -1;
RowData callRowData = null;
int prevMethodStart = -1;
int prevCallStart = -1;
if (mHighlightCall != null) {
int callPixelStart = -1;
int callPixelEnd = -1;
                callStart = mHighlightCall.mGlobalStartTime;
                callEnd = mHighlightCall.mGlobalEndTime;
                callMethod = mHighlightCall.mMethodData;
if (callStart >= minVal)
callPixelStart = mScaleInfo.valueToPixel(callStart);
if (callEnd <= maxVal)
//Synthetic comment -- @@ -1306,7 +1369,11 @@
continue;
if (segment.mStartTime >= maxVal)
continue;
Block block = segment.mBlock;
Color color = block.getColor();
if (color == null)
continue;
//Synthetic comment -- @@ -1318,6 +1385,7 @@
int pixelStart = mScaleInfo.valueToPixel(recordStart);
int pixelEnd = mScaleInfo.valueToPixel(recordEnd);
int width = pixelEnd - pixelStart;

RowData rd = segment.mRowData;
MethodData md = block.getMethodData();
//Synthetic comment -- @@ -1342,24 +1410,25 @@

if (mHighlightMethodData != null) {
if (mHighlightMethodData == md) {
                        if (prevMethodStart != pixelStart) {
prevMethodStart = pixelStart;
int rangeWidth = width;
if (rangeWidth == 0)
rangeWidth = 1;
mHighlightExclusive.add(new Range(pixelStart
+ LeftMargin, rangeWidth, y1, color));
                            Call call = (Call) block;
                            callStart = call.mGlobalStartTime;
int callPixelStart = -1;
if (callStart >= minVal)
callPixelStart = mScaleInfo.valueToPixel(callStart);
                            if (prevCallStart != callPixelStart) {
prevCallStart = callPixelStart;
                                int callPixelEnd = -1;
                                callEnd = call.mGlobalEndTime;
                                if (callEnd <= maxVal)
                                    callPixelEnd = mScaleInfo.valueToPixel(callEnd);
mHighlightInclusive.add(new Range(
callPixelStart + LeftMargin,
callPixelEnd + LeftMargin, y1, color));
//Synthetic comment -- @@ -1372,8 +1441,9 @@
if (segment.mStartTime >= callStart
&& segment.mEndTime <= callEnd && callMethod == md
&& callRowData == rd) {
                        if (prevMethodStart != pixelStart) {
prevMethodStart = pixelStart;
int rangeWidth = width;
if (rangeWidth == 0)
rangeWidth = 1;
//Synthetic comment -- @@ -1418,7 +1488,7 @@
// how much of the region [N - 0.5, N + 0.5] is covered
// by the segment.
double weight = computeWeight(recordStart, recordEnd,
                                pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
if (weight > pix.mMaxWeight) {
pix.setFields(pixelStart, weight, segment, color,
//Synthetic comment -- @@ -1426,13 +1496,15 @@
}
} else {
int x1 = pixelStart + LeftMargin;
                        Strip strip = new Strip(x1, y1, width, rowHeight, rd,
                                segment, color);
mStripList.add(strip);
}
} else {
double weight = computeWeight(recordStart, recordEnd,
                            pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
if (weight > pix.mMaxWeight) {
pix.setFields(pixelStart, weight, segment, color, rd);
//Synthetic comment -- @@ -1444,7 +1516,7 @@
// Compute the weight for the next pixel
pixelStart += 1;
weight = computeWeight(recordStart, recordEnd,
                                pixelStart);
weight = block.addWeight(pixelStart, rd.mRank, weight);
pix.setFields(pixelStart, weight, segment, color, rd);
} else if (width > 1) {
//Synthetic comment -- @@ -1455,8 +1527,10 @@
pixelStart += 1;
width -= 1;
int x1 = pixelStart + LeftMargin;
                        Strip strip = new Strip(x1, y1, width, rowHeight, rd,
                                segment, color);
mStripList.add(strip);
}
}
//Synthetic comment -- @@ -1483,7 +1557,11 @@
}
}

        private double computeWeight(double start, double end, int pixel) {
double pixelStartFraction = mScaleInfo.valueToPixelFraction(start);
double pixelEndFraction = mScaleInfo.valueToPixelFraction(end);
double leftEndPoint = Math.max(pixelStartFraction, pixel - 0.5);
//Synthetic comment -- @@ -1811,7 +1889,7 @@
} else {
mFadeColors = true;
mShowHighlightName = true;
                highlightHeight = highlightHeights[mHighlightStep];
getDisplay().timerExec(HIGHLIGHT_TIMER_INTERVAL, mHighlightAnimator);
}
redraw();
//Synthetic comment -- @@ -1820,7 +1898,7 @@
private void clearHighlights() {
// System.out.printf("clearHighlights()\n");
mShowHighlightName = false;
            highlightHeight = 0;
mHighlightMethodData = null;
mHighlightCall = null;
mFadeColors = false;
//Synthetic comment -- @@ -1900,7 +1978,7 @@
private static final int ZOOM_TIMER_INTERVAL = 10;
private static final int HIGHLIGHT_TIMER_INTERVAL = 50;
private static final int ZOOM_STEPS = 8; // must be even
        private int highlightHeight = 4;
private final int[] highlightHeights = { 0, 2, 4, 5, 6, 5, 4, 2, 4, 5,
6 };
private final int HIGHLIGHT_STEPS = highlightHeights.length;
//Synthetic comment -- @@ -1989,7 +2067,12 @@
private static class Segment {
Segment(RowData rowData, Block block, long startTime, long endTime) {
mRowData = rowData;
            mBlock = block;
mStartTime = startTime;
mEndTime = endTime;
}
//Synthetic comment -- @@ -1998,6 +2081,7 @@
private Block mBlock;
private long mStartTime;
private long mEndTime;
}

private static class Strip {








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceAction.java b/traceview/src/com/android/traceview/TraceAction.java
new file mode 100644
//Synthetic comment -- index 0000000..6717300

//Synthetic comment -- @@ -0,0 +1,31 @@








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceReader.java b/traceview/src/com/android/traceview/TraceReader.java
//Synthetic comment -- index e936629..fa76d27 100644

//Synthetic comment -- @@ -45,10 +45,22 @@
return null;
}

    public long getEndTime() {
return 0;
}

public HashMap<String, String> getProperties() {
return null;
}
//Synthetic comment -- @@ -56,4 +68,12 @@
public ProfileProvider getProfileProvider() {
return null;
}
}







