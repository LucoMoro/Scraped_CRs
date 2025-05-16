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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.swt.widgets.ToolTip;

public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {
    private ToolTip tip;

    public PropertySheetPage() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        setupTooltip();
    }

    private void setupTooltip() {
        tip = new ToolTip(Display.getCurrent().getActiveShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
        tip.setVisible(false);
    }

    public void showToolTip(Tree tree, TreeItem item, String description) {
        if (description != null && !description.isEmpty()) {
            tip.setText("Description");
            tip.setMessage(linearizeDescription(description));
            Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Rectangle rect = item.getBounds(0);
            Point pt = tree.toDisplay(rect.x, rect.y);
            tip.setBounds(pt.x, pt.y, size.x, size.y);
            tip.setVisible(true);
        }
    }

    private String linearizeDescription(String description) {
        return description.replaceAll("\\r?\\n", " ");
    }

    @Override
    public void dispose() {
        if (tip != null && !tip.isDisposed()) {
            tip.dispose();
        }
        super.dispose();
    }
}

//<End of snippet n. 1>