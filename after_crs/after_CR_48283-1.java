/*Make ddms show Java stacks in the traditional format, for legibility.

Also remove the need to double-click to see a stack.

Change-Id:I076179b09350d1c85614c7325e2faa4848b4e8d8*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index 23775e8..a48f73d 100644

//Synthetic comment -- @@ -77,11 +77,7 @@

private final static String PREFS_ALLOC_SASH = "allocPanel.sash"; //$NON-NLS-1$

    private static final String PREFS_STACK_COLUMN = "allocPanel.stack.col0"; //$NON-NLS-1$

private Composite mAllocationBase;
private Table mAllocationTable;
//Synthetic comment -- @@ -387,13 +383,7 @@

// the UI below the sash
mStackTracePanel = new StackTracePanel();
        mStackTraceTable = mStackTracePanel.createPanel(mAllocationBase, PREFS_STACK_COLUMN, store);

// now setup the sash.
// form layout data
//Synthetic comment -- @@ -659,4 +649,3 @@
}

}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/StackTracePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/StackTracePanel.java
//Synthetic comment -- index 336a5a3..b00120b 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
* <p/>This is not a panel in the regular sense. Instead this is just an object around the creation
* and management of a Stack Trace display.
* <p/>UI creation is done through
 * {@link #createPanel(Composite, String, IPreferenceStore)}.
*
*/
public final class StackTracePanel {
//Synthetic comment -- @@ -97,22 +97,10 @@

@Override
public String getColumnText(Object element, int columnIndex) {
            if (element instanceof StackTraceElement && columnIndex == 0) {
                StackTraceElement traceElement = (StackTraceElement) element;
                return "  at " + traceElement.toString();
}
return null;
}

//Synthetic comment -- @@ -166,55 +154,22 @@
* <p/>This method will set the parent {@link Composite} to use a {@link GridLayout} with
* 2 columns.
* @param parent the parent composite.
     * @param prefs_stack_column
* @param store
*/
    public Table createPanel(Composite parent, String prefs_stack_column,
            IPreferenceStore store) {

mStackTraceTable = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION);
        mStackTraceTable.setHeaderVisible(false);
        mStackTraceTable.setLinesVisible(false);

TableHelper.createTableColumn(
mStackTraceTable,
                "Info",
SWT.LEFT,
                "SomeLongClassName.method(android/somepackage/someotherpackage/somefile.java:99999)", //$NON-NLS-1$
                prefs_stack_column, store);

mStackTraceViewer = new TableViewer(mStackTraceTable);
mStackTraceViewer.setContentProvider(new StackTraceContentProvider());








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java
//Synthetic comment -- index f88b4c4..81e245d 100644

//Synthetic comment -- @@ -71,11 +71,7 @@

private final static String PREFS_THREAD_SASH = "threadPanel.sash"; //$NON-NLS-1$

    private static final String PREFS_STACK_COLUMN = "threadPanel.stack.col0"; //$NON-NLS-1$

private Display mDisplay;
private Composite mBase;
//Synthetic comment -- @@ -100,9 +96,9 @@
private Object mLock = new Object();

private static final String[] THREAD_STATUS = {
        "Zombie", "Runnable", "TimedWait", "Monitor",
        "Wait", "Initializing", "Starting", "Native", "VmWait",
        "Suspended"
};

/**
//Synthetic comment -- @@ -269,21 +265,13 @@
mThreadViewer.addSelectionChangedListener(new ISelectionChangedListener() {
@Override
public void selectionChanged(SelectionChangedEvent event) {
                requestThreadStackTrace(getThreadSelection(event.getSelection()));
}
});
mThreadViewer.addDoubleClickListener(new IDoubleClickListener() {
@Override
public void doubleClick(DoubleClickEvent event) {
                requestThreadStackTrace(getThreadSelection(event.getSelection()));
}
});

//Synthetic comment -- @@ -301,13 +289,7 @@
mRefreshStackTraceButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                requestThreadStackTrace(getThreadSelection(null));
}
});

//Synthetic comment -- @@ -315,13 +297,7 @@
mStackTraceTimeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

mStackTracePanel = new StackTracePanel();
        mStackTraceTable = mStackTracePanel.createPanel(mStackTraceBase, PREFS_STACK_COLUMN, store);

GridData gd;
mStackTraceTable.setLayoutData(gd = new GridData(GridData.FILL_BOTH));
//Synthetic comment -- @@ -478,6 +454,15 @@
mBase.layout();
}

    private void requestThreadStackTrace(ThreadInfo selectedThread) {
        if (selectedThread != null) {
            Client client = (Client) mThreadViewer.getInput();
            if (client != null) {
                client.requestThreadStackTrace(selectedThread.getThreadId());
            }
        }
    }

/**
* Updates the stack call of the currently selected thread.
* <p/>
//Synthetic comment -- @@ -586,4 +571,3 @@
}

}







