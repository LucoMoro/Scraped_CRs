/*Custom sort property sheet categories

The property sheet is hardcoded to sort property categories
alphabetically.

Unfortunately, this means that for most widgets, the first properties
listed in the property sheet is "Deprecated" !

This changeset adds a custom sorter to the propertysheet, and sorts
the deprecated category to the bottom. It also sorts the "Misc"
category second to last, which means that you'll usually see the type
of the widget (e.g. "Button" or "EditText") at the top.

Change-Id:Ib2c8ec81cae82650a8a41e533e3803a1f6582fa7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/TextAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/TextAttributeDescriptor.java
//Synthetic comment -- index d1038b5..2408954 100644

//Synthetic comment -- @@ -39,6 +39,8 @@
* This is the "default" kind of attribute. If in doubt, use this.
*/
public class TextAttributeDescriptor extends AttributeDescriptor implements IPropertyDescriptor {
    public static final String DEPRECATED_CATEGORY = "Deprecated";

private String mUiName;
private String mTooltip;

//Synthetic comment -- @@ -102,7 +104,7 @@

public String getCategory() {
if (isDeprecated()) {
            return DEPRECATED_CATEGORY;
}

ElementDescriptor parent = getParent();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
//Synthetic comment -- index daf9655..b67e606 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor.DEPRECATED_CATEGORY;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
//Synthetic comment -- @@ -36,6 +38,7 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetSorter;

/**
* A customized property sheet page for the graphical layout editor v2.
//Synthetic comment -- @@ -52,9 +55,30 @@
* @since GLE2
*/
public class PropertySheetPage extends org.eclipse.ui.views.properties.PropertySheetPage {
    private static final String MISC_CATEGORY = "Misc";

public PropertySheetPage() {
super();

        setSorter(new PropertySheetSorter() {
            @Override
            public int compareCategories(String categoryA, String categoryB) {
                // Sort the "Deprecated" category to the bottom, and the "Misc"
                // category second to last.
                if (categoryA.equals(DEPRECATED_CATEGORY)) {
                    return 1;
                } else if (categoryB.equals(DEPRECATED_CATEGORY)) {
                    return -1;
                }
                if (categoryA.equals(MISC_CATEGORY)) {
                    return 1;
                } else if (categoryB.equals(MISC_CATEGORY)) {
                    return -1;
                }

                return super.compareCategories(categoryA, categoryB);
            }
        });
}

@Override







