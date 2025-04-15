/*ADT: differentiate icons in from editors.

In ElementDescriptor, provide 2 icon methods:
- getIcon() returns a generic icon whatever the descriptor.
  This is used by the XML content assist.
- getFormIcon() return an icon specific to the descriptor.
  This is used by the form editor in the block/detail tree.

The icons are the same auto-generated stuff than before.
A changeset with specific 'pretty' icons would be more than welcome.

Change-Id:Ib0ff8298101462a7dd7e8e92f671eb0171e46c48*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index 1e238ae..0309dc2 100644

//Synthetic comment -- @@ -441,7 +441,7 @@
String tooltip = null;
if (choice instanceof ElementDescriptor) {
keyword = ((ElementDescriptor)choice).getXmlName();
                icon    = ((ElementDescriptor)choice).getIcon();
tooltip = DescriptorsUtils.formatTooltip(((ElementDescriptor)choice).getTooltip());
} else if (choice instanceof TextValueDescriptor) {
continue; // Value nodes are not part of the completion choices
//Synthetic comment -- @@ -449,7 +449,7 @@
continue; // not real attribute descriptors
} else if (choice instanceof AttributeDescriptor) {
keyword = ((AttributeDescriptor)choice).getXmlLocalName();
                icon    = ((AttributeDescriptor)choice).getIcon();
if (choice instanceof TextAttributeDescriptor) {
tooltip = ((TextAttributeDescriptor) choice).getTooltip();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 4ceacfa..c4f5ef6 100644

//Synthetic comment -- @@ -89,6 +89,9 @@
@SuppressWarnings("restriction") // Uses XML model, which has no non-restricted replacement yet
public abstract class AndroidXmlEditor extends FormEditor implements IResourceChangeListener {

/** Preference name for the current page of this file */
private static final String PREF_CURRENT_PAGE = "_current_page"; //$NON-NLS-1$

//Synthetic comment -- @@ -641,7 +644,7 @@
mTextPageIndex = index;
setPageText(index, mTextEditor.getTitle());
setPageImage(index,
                    IconFactory.getInstance().getIcon("editor_page_source")); //$NON-NLS-1$

if (AdtPlugin.DEBUG_XML_FILE_INIT) {
AdtPlugin.log(IStatus.ERROR, "Found document class: %1$s, file=%2$s",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/AttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/AttributeDescriptor.java
//Synthetic comment -- index b46f261..1cf11e4 100644

//Synthetic comment -- @@ -96,10 +96,12 @@

/**
* Returns an optional icon for the attribute.
*
* @return An icon for this element or null.
*/
    public Image getIcon() {
return IconFactory.getInstance().getIcon(ATTRIBUTE_ICON_FILENAME);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 97ed5a6..95edfb3 100644

//Synthetic comment -- @@ -459,7 +459,7 @@

StringBuilder sb = new StringBuilder();

        Image icon = elementDescriptor.getIcon();
if (icon != null) {
sb.append("<form><li style=\"image\" value=\"" +        //$NON-NLS-1$
IMAGE_KEY + "\">");                             //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index 793ea14..8ab25cb 100644

//Synthetic comment -- @@ -226,15 +226,55 @@
}

/**
     * Returns an optional icon for the element.
*
* @return An icon for this element or null.
*/
    public Image getIcon() {
return IconFactory.getInstance().getIcon(ELEMENT_ICON_FILENAME);
}

/**
* Returns an optional ImageDescriptor for the element.
* <p/>
* By default this tries to return an image based on the XML name of the element.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java
//Synthetic comment -- index 41b07b5..b45a197 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
* @return An icon for this element or null.
*/
@Override
    public Image getIcon() {
IconFactory factory = IconFactory.getInstance();
String name = mXmlName;
if (name.indexOf('.') != -1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 4323075..b08e616 100755

//Synthetic comment -- @@ -483,7 +483,7 @@
}
}
if (img == null) {
                        img = desc.getIcon();
}
if (img != null) {
if (node.hasError()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 9e090fd..fcf87d4 100755

//Synthetic comment -- @@ -552,7 +552,7 @@
break;
}
case ICON_ONLY: {
                item = new ImageControl(parent, SWT.None, desc.getIcon());
item.setToolTipText(desc.getUiName());
break;
}
//Synthetic comment -- @@ -586,7 +586,7 @@
mMouseIn = false;

setText(desc.getUiName());
            setImage(desc.getIcon());
setToolTipText(desc.getTooltip());
addMouseTrackListener(this);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/PaletteMetadataDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/PaletteMetadataDescriptor.java
//Synthetic comment -- index 287c5a8..3380a38 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
}

@Override
    public Image getIcon() {
if (mIconName != null) {
IconFactory factory = IconFactory.getInstance();
Image icon = factory.getIcon(mIconName);
//Synthetic comment -- @@ -70,7 +70,7 @@
}
}

        return super.getIcon();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index ac9d83d..d19c1e6 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
/**
* Multi-page form editor for AndroidManifest.xml.
*/
public final class ManifestEditor extends AndroidXmlEditor {

public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".manifest.ManifestEditor"; //$NON-NLS-1$
//Synthetic comment -- @@ -197,7 +198,6 @@
return null;
}

    @SuppressWarnings("restriction")
private void onDescriptorsChanged() {
IStructuredModel model = getModelForRead();
if (model != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java
//Synthetic comment -- index 2386a8c..f821375 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.pages;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -44,7 +46,7 @@
section.setDescription("The content of the Android Manifest is made up of three sections. You can also edit the XML directly.");

Composite table = createTableLayout(toolkit, 2 /* numColumns */);
        
StringBuffer buf = new StringBuffer();
buf.append(String.format("<form><li style=\"image\" value=\"app_img\"><a href=\"page:%1$s\">", //$NON-NLS-1$
ApplicationPage.PAGE_ID));
//Synthetic comment -- @@ -67,7 +69,7 @@
buf.append(": Instrumentation defined.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append(String.format("<li style=\"image\" value=\"android_img\"><a href=\"page:%1$s\">", //$NON-NLS-1$
ManifestEditor.TEXT_EDITOR_ID));
buf.append("XML Source");
buf.append("</a>"); //$NON-NLS-1$
//Synthetic comment -- @@ -81,12 +83,13 @@

mFormText = createFormText(table, toolkit, true, buf.toString(),
false /* setupLayoutData */);
        
AndroidManifestDescriptors manifestDescriptor = editor.getManifestDescriptors();

Image androidLogo = AdtPlugin.getAndroidLogo();
mFormText.setImage("android_img", androidLogo); //$NON-NLS-1$
        
if (manifestDescriptor != null) {
mFormText.setImage("app_img", getIcon(manifestDescriptor.getApplicationElement())); //$NON-NLS-1$
mFormText.setImage("perm_img", getIcon(manifestDescriptor.getPermissionElement())); //$NON-NLS-1$
//Synthetic comment -- @@ -98,7 +101,7 @@
}
mFormText.addHyperlinkListener(editor.createHyperlinkListener());
}
    
/**
* Update the UI with information from the new descriptors.
* <p/>At this point, this only refreshes the icons.
//Synthetic comment -- @@ -114,12 +117,8 @@
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
//Synthetic comment -- index 593d657..2eca501 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
FormText text = SectionHelper.createFormText(masterTable, toolkit,
true /* isHtml */, tooltip, true /* setupLayoutData */);
text.addHyperlinkListener(mTree.getEditor().createHyperlinkListener());
                    Image icon = elem_desc.getIcon();
if (icon != null) {
text.setImage(DescriptorsUtils.IMAGE_KEY, icon);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeLabelProvider.java
//Synthetic comment -- index 46b0a6b..451a6eb 100644

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








