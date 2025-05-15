//<Beginning of snippet n. 0>


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

boolean needBreak = true;
if (s != null) {
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.ToolTip;

/**
*/
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
        Tree tree = ...; // Assume the tree is initialized elsewhere
        tree.addListener(SWT.MouseMove, event -> {
            TreeItem item = tree.getItem(new Point(event.x, event.y));
            if (item != null) {
                String description = item.getText();
                if (description != null && !description.trim().isEmpty()) {
                    ToolTip tip = new ToolTip(tree.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
                    tip.setText("Property Description");
                    tip.setMessage(description.replaceAll("\\n", " "));
                    Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                    Rectangle rect = item.getBounds(0);
                    Point pt = tree.toDisplay(rect.x, rect.y);
                    tip.setBounds(pt.x, pt.y, size.x, size.y);
                    tip.setVisible(true);
                }
            }
        });
    }

    @Override
    public void paintControl(GC gc, TreeItem item) {
        Color redGradient = new Color(Display.getCurrent(), 255, 192, 203); // Mild red
        gc.setBackground(redGradient);
        gc.fillRectangle(item.getBounds());
        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        gc.drawText(item.getText(), item.getBounds().x, item.getBounds().y);
        redGradient.dispose();
    }
    
    private String linearizeDescription(String description) {
        return description != null ? description.replaceAll("\\n", " ") : "";
    }
    
    // Additional methods for handling edge cases can be added here
}

//<End of snippet n. 1>