/*Add alloc number in the alloc tracker.

This lets the user sort the allocation in the order they
happened (or reverse)

Change-Id:I85ca3b190f3a5d63828d78882ee833e5523c2154*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java
//Synthetic comment -- index ecf55ab..90bd7d4 100644

//Synthetic comment -- @@ -22,13 +22,14 @@
* Holds an Allocation information.
*/
public class AllocationInfo implements IStackTraceInfo {
    private String mAllocatedClass;
    private int mAllocationSize;
    private short mThreadId;
    private StackTraceElement[] mStackTrace;

public static enum SortMode {
        SIZE, CLASS, THREAD, IN_CLASS, IN_METHOD;
}

public final static class AllocationSorter implements Comparator<AllocationInfo> {
//Synthetic comment -- @@ -58,6 +59,9 @@
public int compare(AllocationInfo o1, AllocationInfo o2) {
int diff = 0;
switch (mSortMode) {
case SIZE:
// pass, since diff is init with 0, we'll use SIZE compare below
// as a back up anyway.
//Synthetic comment -- @@ -113,8 +117,9 @@
/*
* Simple constructor.
*/
    AllocationInfo(String allocatedClass, int allocationSize,
short threadId, StackTraceElement[] stackTrace) {
mAllocatedClass = allocatedClass;
mAllocationSize = allocationSize;
mThreadId = threadId;
//Synthetic comment -- @@ -122,6 +127,14 @@
}

/**
* Returns the name of the allocated class.
*/
public String getAllocatedClass() {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java
//Synthetic comment -- index 0964226..1761b79 100644

//Synthetic comment -- @@ -512,6 +512,7 @@
data.position(messageHdrLen);

ArrayList<AllocationInfo> list = new ArrayList<AllocationInfo>(numEntries);
for (int i = 0; i < numEntries; i++) {
int totalSize;
int threadId, classNameIndex, stackDepth;
//Synthetic comment -- @@ -552,7 +553,7 @@
data.get();
}

            list.add(new AllocationInfo(classNames[classNameIndex],
totalSize, (short) threadId, steArray));
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index d9492c3..e28b37e 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
*/
public class AllocationPanel extends TablePanel {

private final static String PREFS_ALLOC_COL_SIZE = "allocPanel.Col0"; //$NON-NLS-1$
private final static String PREFS_ALLOC_COL_CLASS = "allocPanel.Col1"; //$NON-NLS-1$
private final static String PREFS_ALLOC_COL_THREAD = "allocPanel.Col2"; //$NON-NLS-1$
//Synthetic comment -- @@ -142,14 +143,16 @@
AllocationInfo alloc = (AllocationInfo)element;
switch (columnIndex) {
case 0:
                        return Integer.toString(alloc.getSize());
case 1:
                        return alloc.getAllocatedClass();
case 2:
                        return Short.toString(alloc.getThreadId());
case 3:
                        return alloc.getFirstTraceClassName();
case 4:
return alloc.getFirstTraceMethodName();
}
}
//Synthetic comment -- @@ -255,6 +258,19 @@
mAllocationTable.setHeaderVisible(true);
mAllocationTable.setLinesVisible(true);

final TableColumn sizeCol = TableHelper.createTableColumn(
mAllocationTable,
"Allocation Size",







