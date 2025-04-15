/*Remove <merge> from the palette

The <include> tag is already removed from the palette. This changeset
does the same thing for the similar <merge> tag, since it does not
work; you cannot drag a <merge> tag somewhere into an existing layout.
(The common case of rendering views with <merge> as the root -does-
work, but that does not involve the palette.)

Change-Id:I9210e196ab66fe77b1303e756384844fde1d1c0f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 903e030..6546b0b 100644

//Synthetic comment -- @@ -48,6 +48,13 @@
public static final String VIEW_INCLUDE = "include";      //$NON-NLS-1$

/**
* The attribute name of the include tag's url naming the resource to be inserted
* <p>
* <b>NOTE</b>: The layout attribute is NOT in the Android namespace!
//Synthetic comment -- @@ -338,7 +345,7 @@
*   FrameLayout descriptor and extract its layout attributes.
*/
private ElementDescriptor createMerge(ArrayList<ElementDescriptor> knownLayouts) {
        String xml_name = "merge";  //$NON-NLS-1$

// Find View and inherit all its layout attributes
AttributeDescriptor[] viewLayoutAttribs = findViewLayoutAttributes(
//Synthetic comment -- @@ -347,7 +354,7 @@
// Create the include descriptor
ViewElementDescriptor desc = new ViewElementDescriptor(xml_name,  // xml_name
xml_name, // ui_name
                null,     // canonical class name, we don't have one
"A root tag useful for XML layouts inflated using a ViewStub.",  // tooltip
null,  // sdk_url
null,  // attributes








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index f3c62ec..30170bb 100755

//Synthetic comment -- @@ -21,6 +21,8 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -34,7 +36,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -308,9 +309,10 @@

for (ElementDescriptor desc : descriptors) {

            // Exclude the <include> tag from the View palette.
// We don't have drop support for it right now, although someday we should.
            if (LayoutDescriptors.VIEW_INCLUDE.equals(desc.getXmlName())) {
continue;
}








