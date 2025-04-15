/*Filter input in the alloc tracker panel.

Change-Id:Ib8f591022228d3ec2a8d50c0dc3d2caadca86e81*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AllocationInfo.java
//Synthetic comment -- index a296063..ecf55ab 100644

//Synthetic comment -- @@ -169,4 +169,27 @@

return null;
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AllocationPanel.java
//Synthetic comment -- index c39deb4..d9492c3 100644

//Synthetic comment -- @@ -35,6 +35,8 @@
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -46,6 +48,7 @@
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -54,7 +57,9 @@
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.Arrays;

/**
//Synthetic comment -- @@ -84,11 +89,13 @@
private Table mStackTraceTable;
private Button mEnableButton;
private Button mRequestButton;

private final AllocationSorter mSorter = new AllocationSorter();
private TableColumn mSortColumn;
private Image mSortUpImg;
private Image mSortDownImg;

/**
* Content Provider to display the allocations of a client.
//Synthetic comment -- @@ -100,6 +107,9 @@
if (inputElement instanceof Client) {
AllocationInfo[] allocs = ((Client)inputElement).getClientData().getAllocations();
if (allocs != null) {
Arrays.sort(allocs, mSorter);
return allocs;
}
//Synthetic comment -- @@ -184,7 +194,7 @@

// table above the sash
Composite topParent = new Composite(mAllocationBase, SWT.NONE);
        topParent.setLayout(new GridLayout(2, false));

mEnableButton = new Button(topParent, SWT.PUSH);
mEnableButton.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -212,10 +222,36 @@

setUpButtons(false /* enabled */, AllocationTrackingStatus.OFF);

        mAllocationTable = new Table(topParent, SWT.MULTI | SWT.FULL_SELECTION);
GridData gridData;
mAllocationTable.setLayoutData(gridData = new GridData(GridData.FILL_BOTH));
        gridData.horizontalSpan = 2;
mAllocationTable.setHeaderVisible(true);
mAllocationTable.setLinesVisible(true);

//Synthetic comment -- @@ -572,5 +608,22 @@
mAllocationTable.setRedraw(true);
mAllocationViewer.refresh();
}
}








