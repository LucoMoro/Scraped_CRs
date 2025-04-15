/*Property sheet: Fix tooltip and selected property painting

This CL contains the following fixes for the property sheet:

(1) Add a custom paint to the selection background. In Eclipse 3.5 and
    3.6, selected items in the property sheet are unreadable because
    they are painted white on white (well at least they are on
    Macs). This seems to be fixed in Eclipse 3.7M3. In any case, this
    changeset adds a custom paint listener which adds a mild red
    gradient below the selection to make the property name readable.

(2) Override the builtin property sheet's display of the property
    description such that it can take the property description and
    "linearize" it (remove newlines and make it into a single line
    display item).

(3) Hide the tooltip (no longer necessary since the full tooltip text
    is displayed in the statusbar)

Change-Id:Ib9c338759f79afa51f07deacb3962129e321c16b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 09691b8..17e15ad 100644

//Synthetic comment -- @@ -38,8 +38,8 @@
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -416,7 +416,7 @@
needBreak = true;
} else if (s != null) {
if (needBreak && s.trim().length() > 0) {
                    sb.append('\r');
}
sb.append(s);
needBreak = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
//Synthetic comment -- index dadba56..2e98dab 100755

//Synthetic comment -- @@ -16,9 +16,15 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -28,6 +34,8 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;

/**
//Synthetic comment -- @@ -45,7 +53,7 @@
* @since GLE2
*/
public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {


public PropertySheetPage() {
super();
//Synthetic comment -- @@ -55,7 +63,80 @@
public void createControl(Composite parent) {
super.createControl(parent);

        setupTooltip();
}

/**
//Synthetic comment -- @@ -128,7 +209,10 @@
label.addListener(SWT.MouseDown, this);
Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
Rectangle rect = item.getBounds(0);
                            Point pt = tree.toDisplay(rect.x, rect.y);
tip.setBounds(pt.x, pt.y, size.x, size.y);
tip.setVisible(true);
}







