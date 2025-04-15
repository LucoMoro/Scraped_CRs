/*Sort palette contents

The palette has many of the Android views in a seemingly random
order. This changeset ensures that they are ordered alphabetically.

Change-Id:I675265747e7835c933fe8e2c7f99dc3c64e6bfaf*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index e79e654..9c1cacc 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
* an actual XML node attached. A non-mandatory UI node MUST have an XML node attached
* and it will cease to exist when the XML node ceases to exist.
*/
public class ElementDescriptor implements Comparable<ElementDescriptor> {
/** The XML element node name. Case sensitive. */
private String mXmlName;
/** The XML element name for the user interface, typically capitalized. */
//Synthetic comment -- @@ -345,4 +345,8 @@
return new String(c).replace("-", " ");  //$NON-NLS-1$  //$NON-NLS-2$
}

    // Implements Comparable<ElementDescriptor>:
    public int compareTo(ElementDescriptor o) {
        return mUiName.compareToIgnoreCase(o.mUiName);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 8bbbcd8..6dc49a1 100644

//Synthetic comment -- @@ -165,6 +165,10 @@
newDescriptors.add(mergeTag);
newLayouts.add(mergeTag);

        // Sort palette contents
        Collections.sort(newViews);
        Collections.sort(newLayouts);

mViewDescriptors = newViews;
mLayoutDescriptors  = newLayouts;
mRootDescriptor.setChildren(newDescriptors);







