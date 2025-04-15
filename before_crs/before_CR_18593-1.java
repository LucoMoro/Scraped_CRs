/*ADT: Exclude <include> tag from the GLE palette.

Change-Id:Ia318556bc88a823fb6eae9048634c824524f3b90*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 5972df6..d49aeb9 100644

//Synthetic comment -- @@ -40,6 +40,13 @@
*/
public final class LayoutDescriptors implements IDescriptorProvider {

// Public attributes names, attributes descriptors and elements descriptors
public static final String ID_ATTR = "id"; //$NON-NLS-1$

//Synthetic comment -- @@ -272,7 +279,7 @@
*   View descriptor and extract its layout attributes.
*/
private void insertInclude(ArrayList<ElementDescriptor> knownViews) {
        String xml_name = "include";  //$NON-NLS-1$

// Create the include custom attributes
ArrayList<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index a1f0b10..4d62502 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -241,6 +242,13 @@
Toggle toggle = new Toggle(group, uiName);

for (ElementDescriptor desc : descriptors) {
Item item = new Item(group, desc);
toggle.addItem(item);
GridData gd = new GridData();







