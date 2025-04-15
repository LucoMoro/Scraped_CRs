/*ADT: differentiate icons in from editors.

In ElementDescriptor, provide 2 icon methods:
- getIcon() returns a generic icon whatever the descriptor.
  This is used by the XML content assist.
- getFormIcon() return an icon specific to the descriptor.
  This is used by the form editor in the block/detail tree.

The icons are the same auto-generated stuff than before.
A changeset with specific 'pretty' icons would be more than welcome.

Change-Id:Ib0ff8298101462a7dd7e8e92f671eb0171e46c48*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 97ed5a6..4d90c9f 100644

//Synthetic comment -- @@ -459,7 +459,7 @@

StringBuilder sb = new StringBuilder();

        Image icon = elementDescriptor.getIcon();
if (icon != null) {
sb.append("<form><li style=\"image\" value=\"" +        //$NON-NLS-1$
IMAGE_KEY + "\">");                             //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index 793ea14..3d4c3f8 100644

//Synthetic comment -- @@ -235,6 +235,39 @@
}

/**
* Returns an optional ImageDescriptor for the element.
* <p/>
* By default this tries to return an image based on the XML name of the element.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 4323075..2b82cd2 100755

//Synthetic comment -- @@ -483,7 +483,7 @@
}
}
if (img == null) {
                        img = desc.getIcon();
}
if (img != null) {
if (node.hasError()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java
//Synthetic comment -- index 2386a8c..3de4579 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
section.setDescription("The content of the Android Manifest is made up of three sections. You can also edit the XML directly.");

Composite table = createTableLayout(toolkit, 2 /* numColumns */);
        
StringBuffer buf = new StringBuffer();
buf.append(String.format("<form><li style=\"image\" value=\"app_img\"><a href=\"page:%1$s\">", //$NON-NLS-1$
ApplicationPage.PAGE_ID));
//Synthetic comment -- @@ -81,12 +81,12 @@

mFormText = createFormText(table, toolkit, true, buf.toString(),
false /* setupLayoutData */);
        
AndroidManifestDescriptors manifestDescriptor = editor.getManifestDescriptors();

Image androidLogo = AdtPlugin.getAndroidLogo();
mFormText.setImage("android_img", androidLogo); //$NON-NLS-1$
        
if (manifestDescriptor != null) {
mFormText.setImage("app_img", getIcon(manifestDescriptor.getApplicationElement())); //$NON-NLS-1$
mFormText.setImage("perm_img", getIcon(manifestDescriptor.getPermissionElement())); //$NON-NLS-1$
//Synthetic comment -- @@ -98,7 +98,7 @@
}
mFormText.addHyperlinkListener(editor.createHyperlinkListener());
}
    
/**
* Update the UI with information from the new descriptors.
* <p/>At this point, this only refreshes the icons.
//Synthetic comment -- @@ -114,12 +114,8 @@
mFormText.setImage("inst_img", getIcon(manifestDescriptor.getInstrumentationElement())); //$NON-NLS-1$
}
}
    
private Image getIcon(ElementDescriptor desc) {
        if (desc != null && desc.getIcon() != null) {
            return desc.getIcon();
        }
        
        return AdtPlugin.getAndroidLogo();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java
//Synthetic comment -- index 593d657..1adeae6 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
FormText text = SectionHelper.createFormText(masterTable, toolkit,
true /* isHtml */, tooltip, true /* setupLayoutData */);
text.addHyperlinkListener(mTree.getEditor().createHyperlinkListener());
                    Image icon = elem_desc.getIcon();
if (icon != null) {
text.setImage(DescriptorsUtils.IMAGE_KEY, icon);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeLabelProvider.java
//Synthetic comment -- index 46b0a6b..190b4b4 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
* UiModelTreeLabelProvider is a trivial implementation of {@link ILabelProvider}
* where elements are expected to derive from {@link UiElementNode} or
* from {@link ElementDescriptor}.
 * 
* It is used by both the master tree viewer and by the list in the Add... selection dialog.
*/
public class UiModelTreeLabelProvider implements ILabelProvider {
//Synthetic comment -- @@ -42,26 +42,27 @@
*/
public Image getImage(Object element) {
ElementDescriptor desc = null;
if (element instanceof ElementDescriptor) {
            Image img = ((ElementDescriptor) element).getIcon();
            if (img != null) {
                return img;
            }
} else if (element instanceof UiElementNode) {
            UiElementNode node = (UiElementNode) element;
desc = node.getDescriptor();
            if (desc != null) {
                Image img = desc.getIcon();
                if (img != null) {
                    if (node.hasError()) {
                        //TODO: cache image
                        return new ErrorImageComposite(img).createImage();
                    } else {
                        return img;
                    }
}
}
}
return AdtPlugin.getAndroidLogo();
}








