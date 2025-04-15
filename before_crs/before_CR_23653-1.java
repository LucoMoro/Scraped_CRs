/*Fix excludes in the change layout/widget refactoring dialogs

Change-Id:Ib1564f7528d36182071427b795f9e15da64ffc7a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutWizard.java
//Synthetic comment -- index 04da01d..ac920eb 100644

//Synthetic comment -- @@ -18,7 +18,9 @@

import static com.android.ide.common.layout.LayoutConstants.FQCN_RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
//Synthetic comment -- @@ -108,6 +110,8 @@
// RelativeLayout at the root.
Set<String> exclude = new HashSet<String>();
exclude.add(VIEW_INCLUDE);
boolean oldIsRelativeLayout = mOldType.equals(FQCN_RELATIVE_LAYOUT);
if (oldIsRelativeLayout) {
exclude.add(mOldType);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewWizard.java
//Synthetic comment -- index 1372006..f235cf6 100644

//Synthetic comment -- @@ -16,8 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CustomViewFinder;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.ViewMetadataRepository;
//Synthetic comment -- @@ -152,7 +155,9 @@
targetData.getLayoutDescriptors().getViewDescriptors();
for (ViewElementDescriptor d : descriptors) {
String className = d.getFullClassName();
                            if (className.equals(LayoutDescriptors.VIEW_INCLUDE)) {
continue;
}
combo.add(d.getUiName());







