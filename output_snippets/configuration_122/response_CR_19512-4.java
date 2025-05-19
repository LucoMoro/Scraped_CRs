//<Beginning of snippet n. 0>


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

needBreak = true;
} else if (s != null) {
if (needBreak && s.trim().length() > 0) {
                    sb.append('\r');
}
sb.append(s);
needBreak = false;

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
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;

/**
 */
public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {
    private ToolTip tip;

    public PropertySheetPage() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        setupTooltip();
        registerCustomPaintListener();
    }

    private void setupTooltip() {
        tip = new ToolTip(Display.getDefault().getActiveShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
        tip.setText("Tooltip Title");
        tip.setMessage("Tooltip Content");

        Display.getDefault().addFilter(SWT.MouseEnter, e -> {
            TreeItem item = (TreeItem) e.item;
            if (item != null) {
                displayTooltip((Tree) e.widget, item);
            }
        });
        
        Display.getDefault().addFilter(SWT.MouseExit, e -> {
            TreeItem item = (TreeItem) e.item;
            if (item != null && !item.getBounds(0).contains(e.x, e.y)) {
                tip.setVisible(false);
            }
        });
    }

    private void displayTooltip(Tree tree, TreeItem item) {
        item.addListener(SWT.MouseMove, e -> {
            Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Rectangle rect = item.getBounds(0);
            Point pt = tree.toDisplay(rect.x, rect.y);
            tip.setBounds(pt.x, pt.y, size.x, size.y);
            tip.setVisible(true);
        });
    }

    private String linearizeDescription(String description) {
        return description != null ? description.replaceAll("\\r?\\n", " ") : "";
    }

    private void registerCustomPaintListener() {
        this.addPaintListener(e -> {
            TreeItem item = (TreeItem) e.item;
            if (item.getItemCount() > 0 && item.isSelected()) {
                e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_RED));
                e.gc.fillGradientRectangle(0, 0, e.width, e.height, true);
            } else {
                e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                e.gc.fillRectangle(0, 0, e.width, e.height);
            }
        });
    }
}

//<End of snippet n. 1>