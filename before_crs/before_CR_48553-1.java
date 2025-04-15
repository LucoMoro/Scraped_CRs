/*Make ddms show Java stacks in the traditional format, for legibility. DO NOT MERGE

Also remove the need to double-click to see a stack.

Change-Id:I7fe9512a05b990c8015680c1af3ca915b0f2955f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index 23775e8..a48f73d 100644

//Synthetic comment -- @@ -77,11 +77,7 @@

private final static String PREFS_ALLOC_SASH = "allocPanel.sash"; //$NON-NLS-1$

    private static final String PREFS_STACK_COL_CLASS = "allocPanel.stack.col0"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_METHOD = "allocPanel.stack.col1"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_FILE = "allocPanel.stack.col2"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_LINE = "allocPanel.stack.col3"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_NATIVE = "allocPanel.stack.col4"; //$NON-NLS-1$

private Composite mAllocationBase;
private Table mAllocationTable;
//Synthetic comment -- @@ -387,13 +383,7 @@

// the UI below the sash
mStackTracePanel = new StackTracePanel();
        mStackTraceTable = mStackTracePanel.createPanel(mAllocationBase,
                PREFS_STACK_COL_CLASS,
                PREFS_STACK_COL_METHOD,
                PREFS_STACK_COL_FILE,
                PREFS_STACK_COL_LINE,
                PREFS_STACK_COL_NATIVE,
                store);

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
 * {@link #createPanel(Composite, String, String, String, String, String, IPreferenceStore)}.
*
*/
public final class StackTracePanel {
//Synthetic comment -- @@ -97,22 +97,10 @@

@Override
public String getColumnText(Object element, int columnIndex) {
            if (element instanceof StackTraceElement) {
                StackTraceElement traceElement = (StackTraceElement)element;
                switch (columnIndex) {
                    case 0:
                        return traceElement.getClassName();
                    case 1:
                        return traceElement.getMethodName();
                    case 2:
                        return traceElement.getFileName();
                    case 3:
                        return Integer.toString(traceElement.getLineNumber());
                    case 4:
                        return Boolean.toString(traceElement.isNativeMethod());
                }
}

return null;
}

//Synthetic comment -- @@ -166,55 +154,22 @@
* <p/>This method will set the parent {@link Composite} to use a {@link GridLayout} with
* 2 columns.
* @param parent the parent composite.
     * @param prefs_stack_col_class
     * @param prefs_stack_col_method
     * @param prefs_stack_col_file
     * @param prefs_stack_col_line
     * @param prefs_stack_col_native
* @param store
*/
    public Table createPanel(Composite parent, String prefs_stack_col_class,
            String prefs_stack_col_method, String prefs_stack_col_file, String prefs_stack_col_line,
            String prefs_stack_col_native, IPreferenceStore store) {

mStackTraceTable = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION);
        mStackTraceTable.setHeaderVisible(true);
        mStackTraceTable.setLinesVisible(true);

TableHelper.createTableColumn(
mStackTraceTable,
                "Class",
SWT.LEFT,
                "SomeLongClassName", //$NON-NLS-1$
                prefs_stack_col_class, store);

        TableHelper.createTableColumn(
                mStackTraceTable,
                "Method",
                SWT.LEFT,
                "someLongMethod", //$NON-NLS-1$
                prefs_stack_col_method, store);

        TableHelper.createTableColumn(
                mStackTraceTable,
                "File",
                SWT.LEFT,
                "android/somepackage/someotherpackage/somefile.class", //$NON-NLS-1$
                prefs_stack_col_file, store);

        TableHelper.createTableColumn(
                mStackTraceTable,
                "Line",
                SWT.RIGHT,
                "99999", //$NON-NLS-1$
                prefs_stack_col_line, store);

        TableHelper.createTableColumn(
                mStackTraceTable,
                "Native",
                SWT.LEFT,
                "Native", //$NON-NLS-1$
                prefs_stack_col_native, store);

mStackTraceViewer = new TableViewer(mStackTraceTable);
mStackTraceViewer.setContentProvider(new StackTraceContentProvider());








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ThreadPanel.java
//Synthetic comment -- index f88b4c4..81e245d 100644

//Synthetic comment -- @@ -71,11 +71,7 @@

private final static String PREFS_THREAD_SASH = "threadPanel.sash"; //$NON-NLS-1$

    private static final String PREFS_STACK_COL_CLASS = "threadPanel.stack.col0"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_METHOD = "threadPanel.stack.col1"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_FILE = "threadPanel.stack.col2"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_LINE = "threadPanel.stack.col3"; //$NON-NLS-1$
    private static final String PREFS_STACK_COL_NATIVE = "threadPanel.stack.col4"; //$NON-NLS-1$

private Display mDisplay;
private Composite mBase;
//Synthetic comment -- @@ -100,9 +96,9 @@
private Object mLock = new Object();

private static final String[] THREAD_STATUS = {
        "zombie", "running", "timed-wait", "monitor",
        "wait", "init", "start", "native", "vmwait",
        "suspended"
};

/**
//Synthetic comment -- @@ -269,21 +265,13 @@
mThreadViewer.addSelectionChangedListener(new ISelectionChangedListener() {
@Override
public void selectionChanged(SelectionChangedEvent event) {
                ThreadInfo selectedThread = getThreadSelection(event.getSelection());
                updateThreadStackTrace(selectedThread);
}
});
mThreadViewer.addDoubleClickListener(new IDoubleClickListener() {
@Override
public void doubleClick(DoubleClickEvent event) {
                ThreadInfo selectedThread = getThreadSelection(event.getSelection());
                if (selectedThread != null) {
                    Client client = (Client)mThreadViewer.getInput();

                    if (client != null) {
                        client.requestThreadStackTrace(selectedThread.getThreadId());
                    }
                }
}
});

//Synthetic comment -- @@ -301,13 +289,7 @@
mRefreshStackTraceButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                ThreadInfo selectedThread = getThreadSelection(null);
                if (selectedThread != null) {
                    Client currentClient = getCurrentClient();
                    if (currentClient != null) {
                        currentClient.requestThreadStackTrace(selectedThread.getThreadId());
                    }
                }
}
});

//Synthetic comment -- @@ -315,13 +297,7 @@
mStackTraceTimeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

mStackTracePanel = new StackTracePanel();
        mStackTraceTable = mStackTracePanel.createPanel(mStackTraceBase,
                PREFS_STACK_COL_CLASS,
                PREFS_STACK_COL_METHOD,
                PREFS_STACK_COL_FILE,
                PREFS_STACK_COL_LINE,
                PREFS_STACK_COL_NATIVE,
                store);

GridData gd;
mStackTraceTable.setLayoutData(gd = new GridData(GridData.FILL_BOTH));
//Synthetic comment -- @@ -478,6 +454,15 @@
mBase.layout();
}

/**
* Updates the stack call of the currently selected thread.
* <p/>
//Synthetic comment -- @@ -586,4 +571,3 @@
}

}








