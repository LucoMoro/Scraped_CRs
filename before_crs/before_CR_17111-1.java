/*Allocation tracker content can now be sorted.

Change-Id:I9f4009e5634e0c4a2b871082c2c281f62a67ca2f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java
//Synthetic comment -- index c6d4b50..a296063 100644

//Synthetic comment -- @@ -16,15 +16,100 @@

package com.android.ddmlib;

/**
* Holds an Allocation information.
*/
public class AllocationInfo implements Comparable<AllocationInfo>, IStackTraceInfo {
private String mAllocatedClass;
private int mAllocationSize;
private short mThreadId;
private StackTraceElement[] mStackTrace;

/*
* Simple constructor.
*/
//Synthetic comment -- @@ -35,7 +120,7 @@
mThreadId = threadId;
mStackTrace = stackTrace;
}
    
/**
* Returns the name of the allocated class.
*/
//Synthetic comment -- @@ -68,4 +153,20 @@
public int compareTo(AllocationInfo otherAlloc) {
return otherAlloc.mAllocationSize - mAllocationSize;
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java
//Synthetic comment -- index e5b403b..0964226 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

/**
* Handle heap status updates.
//Synthetic comment -- @@ -557,9 +556,6 @@
totalSize, (short) threadId, steArray));
}

        // sort biggest allocations first.
        Collections.sort(list);

client.getClientData().setAllocations(list.toArray(new AllocationInfo[numEntries]));
client.update(Client.CHANGE_HEAP_ALLOCATIONS);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index 11c0e19..c39deb4 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.ddmlib.AllocationInfo;
import com.android.ddmlib.Client;
import com.android.ddmlib.AndroidDebugBridge.IClientChangeListener;
import com.android.ddmlib.ClientData.AllocationTrackingStatus;

//Synthetic comment -- @@ -46,10 +48,14 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;

/**
* Base class for our information panels.
//Synthetic comment -- @@ -79,16 +85,22 @@
private Button mEnableButton;
private Button mRequestButton;

/**
* Content Provider to display the allocations of a client.
* Expected input is a {@link Client} object, elements used in the table are of type
* {@link AllocationInfo}.
*/
    private static class AllocationContentProvider implements IStructuredContentProvider {
public Object[] getElements(Object inputElement) {
if (inputElement instanceof Client) {
AllocationInfo[] allocs = ((Client)inputElement).getClientData().getAllocations();
if (allocs != null) {
return allocs;
}
}
//Synthetic comment -- @@ -126,17 +138,9 @@
case 2:
return Short.toString(alloc.getThreadId());
case 3:
                        StackTraceElement[] traces = alloc.getStackTrace();
                        if (traces.length > 0) {
                            return traces[0].getClassName();
                        }
                        break;
case 4:
                        traces = alloc.getStackTrace();
                        if (traces.length > 0) {
                            return traces[0].getMethodName();
                        }
                        break;
}
}

//Synthetic comment -- @@ -168,6 +172,12 @@
protected Control createControl(Composite parent) {
final IPreferenceStore store = DdmUiPreferences.getStore();

// base composite for selected client with enabled thread update.
mAllocationBase = new Composite(parent, SWT.NONE);
mAllocationBase.setLayout(new FormLayout());
//Synthetic comment -- @@ -209,40 +219,91 @@
mAllocationTable.setHeaderVisible(true);
mAllocationTable.setLinesVisible(true);

        TableHelper.createTableColumn(
mAllocationTable,
"Allocation Size",
SWT.RIGHT,
"888", //$NON-NLS-1$
PREFS_ALLOC_COL_SIZE, store);

        TableHelper.createTableColumn(
mAllocationTable,
"Allocated Class",
SWT.LEFT,
"Allocated Class", //$NON-NLS-1$
PREFS_ALLOC_COL_CLASS, store);

        TableHelper.createTableColumn(
mAllocationTable,
"Thread Id",
SWT.LEFT,
"999", //$NON-NLS-1$
PREFS_ALLOC_COL_THREAD, store);

        TableHelper.createTableColumn(
mAllocationTable,
"Allocated in",
SWT.LEFT,
"utime", //$NON-NLS-1$
PREFS_ALLOC_COL_TRACE_CLASS, store);

        TableHelper.createTableColumn(
mAllocationTable,
"Allocated in",
SWT.LEFT,
"utime", //$NON-NLS-1$
PREFS_ALLOC_COL_TRACE_METHOD, store);

mAllocationViewer = new TableViewer(mAllocationTable);
mAllocationViewer.setContentProvider(new AllocationContentProvider());
//Synthetic comment -- @@ -314,6 +375,13 @@
return mAllocationBase;
}

/**
* Sets the focus to the proper control inside the panel.
*/
//Synthetic comment -- @@ -482,5 +550,27 @@
mEnableButton.setText("Start Tracking");
}
}
}








