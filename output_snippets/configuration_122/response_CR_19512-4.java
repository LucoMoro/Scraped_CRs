//<Beginning of snippet n. 0>


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

private boolean needBreak = true; // Declare 'needBreak' properly in context

private void appendDescription(String s, StringBuilder sb) {
    if (s != null) {
        if (needBreak && s.trim().length() > 0) {
            sb.append('\r');
        }
        sb.append(removeNewlines(s));
        needBreak = false;
    }
}

private String removeNewlines(String description) {
    return description.replaceAll("\\r?\\n", " ");
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
import org.eclipse.swt.widgets.ToolTip;

public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {

    public PropertySheetPage() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        setupTooltip();
    }

    private void setupTooltip() {
        Tree tree = getTree();
        tree.addListener(SWT.MouseMove, event -> {
            TreeItem item = tree.getItem(new Point(event.x, event.y));
            if (item != null) {
                String tooltipText = item.getText(); // Assuming the first text is the description
                if (tooltipText != null) {
                    String description = removeNewlines(tooltipText); 
                    ToolTip tip = new ToolTip(tree.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
                    tip.setText("Property Description");
                    tip.setMessage(description);
                    Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                    Rectangle rect = item.getBounds(0);
                    Point pt = tree.toDisplay(rect.x, rect.y);
                    tip.setBounds(pt.x, pt.y, size.x, size.y);
                    tip.setVisible(true);
                }
            }
        });
    }

    private String removeNewlines(String description) {
        return description.replaceAll("\\r?\\n", " ");
    }
}

//<End of snippet n. 1>