/*ADT: Fix palette items to use full width.

Change-Id:Ieb35e80de19f60b2fd724629374ad3a91832f0e9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 30170bb..1349c23 100755

//Synthetic comment -- @@ -219,6 +219,7 @@

mRoot = new Composite(this, SWT.NONE);
GridLayoutBuilder.create(mRoot).columns(1).columnsEqual().spacing(0).noMargins();
        GridDataBuilder.create(mRoot).hGrab().hFill();

if (targetData != null) {
addGroup(mRoot, "Views", targetData.getLayoutDescriptors().getViewDescriptors());
//Synthetic comment -- @@ -304,8 +305,10 @@

Composite group = new Composite(parent, SWT.NONE);
GridLayoutBuilder.create(group).columns(1).columnsEqual().spacing(0).noMargins();
        GridDataBuilder.create(group).hFill().hGrab();

Toggle toggle = new Toggle(group, uiName);
        GridDataBuilder.create(toggle).hFill().hGrab();

for (ElementDescriptor desc : descriptors) {

//Synthetic comment -- @@ -318,7 +321,7 @@

Item item = new Item(group, this, desc);
toggle.addItem(item);
            GridDataBuilder.create(item).hFill().hGrab();
}
}








