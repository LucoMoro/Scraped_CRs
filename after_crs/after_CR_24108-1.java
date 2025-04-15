/*Add a basic properties dialog.

Shows some information about the trace that can be of interest,
such as which timing mode (thread time or wall time) was used.

Change-Id:I54d136c8fe9e7ba28090547a5dad1ff8916e86db*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index 260a865..dcf17a2 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
private MethodData mTopLevel;
private ArrayList<Call> mCallList;
private ArrayList<Call> mSwitchList;
    private HashMap<String, String> mPropertiesMap;
private HashMap<Integer, MethodData> mMethodMap;
private HashMap<Integer, ThreadData> mThreadMap;
private ThreadData[] mSortedThreads;
//Synthetic comment -- @@ -60,6 +61,7 @@
public DmTraceReader(String traceFileName, boolean regression) {
mTraceFileName = traceFileName;
mRegression = regression;
        mPropertiesMap = new HashMap<String, String>();
mMethodMap = new HashMap<Integer, MethodData>();
mThreadMap = new HashMap<Integer, ThreadData>();

//Synthetic comment -- @@ -366,11 +368,21 @@
parseMethod(line);
break;
case PARSE_OPTIONS:
                parseOption(line);
break;
}
}
}

    void parseOption(String line) {
        String[] tokens = line.split("=");
        if (tokens.length == 2) {
            String key = tokens[0];
            String value = tokens[1];
            mPropertiesMap.put(key, value);
        }
    }

void parseThread(String line) {
String idStr = null;
String name = null;
//Synthetic comment -- @@ -599,4 +611,9 @@
public long getEndTime() {
return mGlobalEndTime;
}

    @Override
    public HashMap<String, String> getProperties() {
        return mPropertiesMap;
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/MainWindow.java b/traceview/src/com/android/traceview/MainWindow.java
//Synthetic comment -- index b78b4f7..cf949a6 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.sdkstats.SdkStatsService;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -54,6 +56,8 @@
super(null);
mReader = reader;
mTraceName = traceName;

        addMenuBar();
}

public void run() {
//Synthetic comment -- @@ -106,6 +110,30 @@
return sashForm1;
}

    @Override
    protected MenuManager createMenuManager() {
        MenuManager manager = super.createMenuManager();

        MenuManager viewMenu = new MenuManager("View");
        manager.add(viewMenu);

        Action showPropertiesAction = new Action("Show Properties...") {
            @Override
            public void run() {
                showProperties();
            }
        };
        viewMenu.add(showPropertiesAction);

        return manager;
    }

    private void showProperties() {
        PropertiesDialog dialog = new PropertiesDialog(getShell());
        dialog.setProperties(mReader.getProperties());
        dialog.open();
    }

/**
* Convert the old two-file format into the current concatenated one.
*








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/PropertiesDialog.java b/traceview/src/com/android/traceview/PropertiesDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..cbae0a8

//Synthetic comment -- @@ -0,0 +1,102 @@
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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.HashMap;
import java.util.Map.Entry;

public class PropertiesDialog extends Dialog {
    private HashMap<String, String> mProperties;

    public PropertiesDialog(Shell parent) {
        super(parent);

        setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
    }

    public void setProperties(HashMap<String, String> properties) {
        mProperties = properties;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        container.setLayout(gridLayout);

        TableViewer tableViewer = new TableViewer(container, SWT.HIDE_SELECTION
                | SWT.V_SCROLL | SWT.BORDER);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(true);

        TableViewerColumn propertyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        propertyColumn.getColumn().setText("Property");
        propertyColumn.setLabelProvider(new ColumnLabelProvider() {
            @SuppressWarnings("unchecked")
            public String getText(Object element) {
                Entry<String, String> entry = (Entry<String, String>) element;
                return entry.getKey();
            }
        });
        propertyColumn.getColumn().setWidth(400);

        TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        valueColumn.getColumn().setText("Value");
        valueColumn.setLabelProvider(new ColumnLabelProvider() {
            @SuppressWarnings("unchecked")
            public String getText(Object element) {
                Entry<String, String> entry = (Entry<String, String>) element;
                return entry.getValue();
            }
        });
        valueColumn.getColumn().setWidth(200);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setInput(mProperties.entrySet().toArray());

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        tableViewer.getControl().setLayoutData(gridData);

        return container;
    }
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceReader.java b/traceview/src/com/android/traceview/TraceReader.java
//Synthetic comment -- index ae75876..e936629 100644

//Synthetic comment -- @@ -49,6 +49,10 @@
return 0;
}

    public HashMap<String, String> getProperties() {
        return null;
    }

public ProfileProvider getProfileProvider() {
return null;
}







