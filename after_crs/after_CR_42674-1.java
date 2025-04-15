/*Collapse layout properties by default

This changeset collapses the layout property category by default in
the property sheet.

If you for example select a button in a RelativeLayout, the entire
property sheet would be filled with the various layout parameters in a
RelativeLayout, which meant you'd need to collapse or scroll
immediately anyway.

Even in other layouts, you typically only see properties such as width
and height there, which can usually be better manipulated in the
layout actions bar. This just makes the property sheet more useful
since it shows the other common attributes closer to the top.

Change-Id:I27d27f0453ac1b6897b80b0001e56e8690198bbb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertySheetPage.java
//Synthetic comment -- index 5893c2c..58fddc0 100644

//Synthetic comment -- @@ -106,6 +106,7 @@
});
mPropertyTable.setDefaultCollapsedNames(Arrays.asList(
"Deprecated",
                "Layout Parameters",
"Layout Parameters|Margins"));

createActions();







