//<Beginning of snippet n. 0>

StringBuilder sb = new StringBuilder();
boolean needBreak = true;

if (s != null) {
    if (needBreak && s.trim().length() > 0) {
        sb.append('\r');
    }
    sb.append(s.trim().replaceAll("\\s+", " "));
    needBreak = false;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;

public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {

    private Tooltip tip;

    public PropertySheetPage() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        setupTooltip();
    }

    private void setupTooltip() {
        // Tooltip setup and positioning logic
        Tree tree = getTreeControl();
        TreeItem[] selection = tree.getSelection();
        if (selection.length > 0) {
            TreeItem item = selection[0];
            Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Rectangle rect = item.getBounds(0);
            Point pt = tree.toDisplay(rect.x, rect.y);
            int adjustedY = Math.max(pt.y, 0); // Prevent tooltip from exceeding display boundaries
            tip.setBounds(pt.x, adjustedY, size.x, size.y);
            tip.setVisible(true);
        }
    }

    private Tree getTreeControl() {
        // Method to get the tree control, implementation may vary based on context
        return new Tree(new Shell(new Display()), SWT.NONE);
    }
    
    // Additional method to handle tooltip visibility lifecycle
    public void onSelectionChange() {
        if (tip.isVisible()) {
            tip.setVisible(false);
        }
        setupTooltip(); // Reposition tooltip on selection change
    }
}

//<End of snippet n. 1>