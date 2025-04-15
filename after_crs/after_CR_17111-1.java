/*Allocation tracker content can now be sorted.

Change-Id:I9f4009e5634e0c4a2b871082c2c281f62a67ca2f*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java
//Synthetic comment -- index c6d4b50..a296063 100644

//Synthetic comment -- @@ -16,15 +16,100 @@

package com.android.ddmlib;

import java.util.Comparator;

/**
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

        private SortMode mSortMode = SortMode.SIZE;
        private boolean mDescending = true;

        public AllocationSorter() {
        }

        public void setSortMode(SortMode mode) {
            if (mSortMode == mode) {
                mDescending = !mDescending;
            } else {
                mSortMode = mode;
            }
        }

        public SortMode getSortMode() {
            return mSortMode;
        }

        public boolean isDescending() {
            return mDescending;
        }

        public int compare(AllocationInfo o1, AllocationInfo o2) {
            int diff = 0;
            switch (mSortMode) {
                case SIZE:
                    // pass, since diff is init with 0, we'll use SIZE compare below
                    // as a back up anyway.
                    break;
                case CLASS:
                    diff = o1.mAllocatedClass.compareTo(o2.mAllocatedClass);
                    break;
                case THREAD:
                    diff = o1.mThreadId - o2.mThreadId;
                    break;
                case IN_CLASS:
                    String class1 = o1.getFirstTraceClassName();
                    String class2 = o2.getFirstTraceClassName();
                    diff = compareOptionalString(class1, class2);
                    break;
                case IN_METHOD:
                    String method1 = o1.getFirstTraceMethodName();
                    String method2 = o2.getFirstTraceMethodName();
                    diff = compareOptionalString(method1, method2);
                    break;
            }

            if (diff == 0) {
                // same? compare on size
                diff = o1.mAllocationSize - o2.mAllocationSize;
            }

            if (mDescending) {
                diff = -diff;
            }

            return diff;
        }

        /** compares two strings that could be null */
        private int compareOptionalString(String str1, String str2) {
            if (str1 != null) {
                if (str2 == null) {
                    return -1;
                } else {
                    return str1.compareTo(str2);
                }
            } else {
                if (str2 == null) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    }

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

    public String getFirstTraceClassName() {
        if (mStackTrace.length > 0) {
            return mStackTrace[0].getClassName();
        }

        return null;
    }

    public String getFirstTraceMethodName() {
        if (mStackTrace.length > 0) {
            return mStackTrace[0].getMethodName();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHeap.java
//Synthetic comment -- index e5b403b..0964226 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
* Handle heap status updates.
//Synthetic comment -- @@ -557,9 +556,6 @@
totalSize, (short) threadId, steArray));
}

client.getClientData().setAllocations(list.toArray(new AllocationInfo[numEntries]));
client.update(Client.CHANGE_HEAP_ALLOCATIONS);
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index 11c0e19..c39deb4 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.ddmlib.AllocationInfo;
import com.android.ddmlib.Client;
import com.android.ddmlib.AllocationInfo.AllocationSorter;
import com.android.ddmlib.AllocationInfo.SortMode;
import com.android.ddmlib.AndroidDebugBridge.IClientChangeListener;
import com.android.ddmlib.ClientData.AllocationTrackingStatus;

//Synthetic comment -- @@ -46,10 +48,14 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.Arrays;

/**
* Base class for our information panels.
//Synthetic comment -- @@ -79,16 +85,22 @@
private Button mEnableButton;
private Button mRequestButton;

    private final AllocationSorter mSorter = new AllocationSorter();
    private TableColumn mSortColumn;
    private Image mSortUpImg;
    private Image mSortDownImg;

/**
* Content Provider to display the allocations of a client.
* Expected input is a {@link Client} object, elements used in the table are of type
* {@link AllocationInfo}.
*/
    private class AllocationContentProvider implements IStructuredContentProvider {
public Object[] getElements(Object inputElement) {
if (inputElement instanceof Client) {
AllocationInfo[] allocs = ((Client)inputElement).getClientData().getAllocations();
if (allocs != null) {
                    Arrays.sort(allocs, mSorter);
return allocs;
}
}
//Synthetic comment -- @@ -126,17 +138,9 @@
case 2:
return Short.toString(alloc.getThreadId());
case 3:
                        return alloc.getFirstTraceClassName();
case 4:
                        return alloc.getFirstTraceMethodName();
}
}

//Synthetic comment -- @@ -168,6 +172,12 @@
protected Control createControl(Composite parent) {
final IPreferenceStore store = DdmUiPreferences.getStore();

        Display display = parent.getDisplay();

        // get some images
        mSortUpImg = ImageLoader.getDdmUiLibLoader().loadImage("sort_up.png", display);
        mSortDownImg = ImageLoader.getDdmUiLibLoader().loadImage("sort_down.png", display);

// base composite for selected client with enabled thread update.
mAllocationBase = new Composite(parent, SWT.NONE);
mAllocationBase.setLayout(new FormLayout());
//Synthetic comment -- @@ -209,40 +219,91 @@
mAllocationTable.setHeaderVisible(true);
mAllocationTable.setLinesVisible(true);

        final TableColumn sizeCol = TableHelper.createTableColumn(
mAllocationTable,
"Allocation Size",
SWT.RIGHT,
"888", //$NON-NLS-1$
PREFS_ALLOC_COL_SIZE, store);
        sizeCol.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setSortColumn(sizeCol, SortMode.SIZE);
            }
        });

        final TableColumn classCol = TableHelper.createTableColumn(
mAllocationTable,
"Allocated Class",
SWT.LEFT,
"Allocated Class", //$NON-NLS-1$
PREFS_ALLOC_COL_CLASS, store);
        classCol.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setSortColumn(classCol, SortMode.CLASS);
            }
        });

        final TableColumn threadCol = TableHelper.createTableColumn(
mAllocationTable,
"Thread Id",
SWT.LEFT,
"999", //$NON-NLS-1$
PREFS_ALLOC_COL_THREAD, store);
        threadCol.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setSortColumn(threadCol, SortMode.THREAD);
            }
        });

        final TableColumn inClassCol = TableHelper.createTableColumn(
mAllocationTable,
"Allocated in",
SWT.LEFT,
"utime", //$NON-NLS-1$
PREFS_ALLOC_COL_TRACE_CLASS, store);
        inClassCol.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setSortColumn(inClassCol, SortMode.IN_CLASS);
            }
        });

        final TableColumn inMethodCol = TableHelper.createTableColumn(
mAllocationTable,
"Allocated in",
SWT.LEFT,
"utime", //$NON-NLS-1$
PREFS_ALLOC_COL_TRACE_METHOD, store);
        inMethodCol.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setSortColumn(inMethodCol, SortMode.IN_METHOD);
            }
        });

        // init the default sort colum
        switch (mSorter.getSortMode()) {
            case SIZE:
                mSortColumn = sizeCol;
                break;
            case CLASS:
                mSortColumn = classCol;
                break;
            case THREAD:
                mSortColumn = threadCol;
                break;
            case IN_CLASS:
                mSortColumn = inClassCol;
                break;
            case IN_METHOD:
                mSortColumn = inMethodCol;
                break;
        }

        mSortColumn.setImage(mSorter.isDescending() ? mSortDownImg : mSortUpImg);

mAllocationViewer = new TableViewer(mAllocationTable);
mAllocationViewer.setContentProvider(new AllocationContentProvider());
//Synthetic comment -- @@ -314,6 +375,13 @@
return mAllocationBase;
}

    @Override
    public void dispose() {
        mSortUpImg.dispose();
        mSortDownImg.dispose();
        super.dispose();
    }

/**
* Sets the focus to the proper control inside the panel.
*/
//Synthetic comment -- @@ -482,5 +550,27 @@
mEnableButton.setText("Start Tracking");
}
}

    private void setSortColumn(final TableColumn column, SortMode sortMode) {
        // set the new sort mode
        mSorter.setSortMode(sortMode);

        mAllocationTable.setRedraw(false);

        // remove image from previous sort colum
        if (mSortColumn != column) {
            mSortColumn.setImage(null);
        }

        mSortColumn = column;
        if (mSorter.isDescending()) {
            mSortColumn.setImage(mSortDownImg);
        } else {
            mSortColumn.setImage(mSortUpImg);
        }

        mAllocationTable.setRedraw(true);
        mAllocationViewer.refresh();
    }
}








