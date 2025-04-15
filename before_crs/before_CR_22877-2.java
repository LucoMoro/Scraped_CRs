/*Fragment Rendering Support

This changeset adds designtime-previewing of fragments, where
a layout which contains fragments will show the contents of
the fragments inline.

Initially, the fragments are empty, but you can right click on them to
bring up a context menu where you can choose which layout to show at
designtime. This is persisted across IDE sessions, just like the
ListView render preview.

In addition to the generic layout chooser, all layout references found
in the associated Fragment class (usually what you want) are listed
directly in the menu.

Change-Id:Ib7f8caae568eff94a57fd50b8e054f5fa52f3da6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 064094e..f6877f1 100644

//Synthetic comment -- @@ -178,7 +178,13 @@
// Set the text of the mock view to the simplified name of the custom class
Method m = view.getClass().getMethod("setText",
new Class<?>[] { CharSequence.class });
            m.invoke(view, getShortClassName(className));

// Call MockView.setGravity(Gravity.CENTER) to get the text centered in
// MockViews.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 8ec3111..ca06477 100644

//Synthetic comment -- @@ -20,11 +20,15 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -348,6 +352,13 @@
Node xmlNode = uiNode.getXmlNode();

if (xmlNode != null) {
Node attribute = xmlNode.getAttributes().getNamedItemNS(namespace, localName);
if (attribute != null) {
String value = attribute.getNodeValue();
//Synthetic comment -- @@ -379,7 +390,19 @@

public String getName() {
if (mParsingState == START_TAG || mParsingState == END_TAG) {
            return getCurrentNode().getDescriptor().getXmlLocalName();
}

return null;
//Synthetic comment -- @@ -478,18 +501,18 @@
}

/** {@link DimensionEntry} complex unit: Value is raw pixels. */
    public static final int COMPLEX_UNIT_PX = 0;
/** {@link DimensionEntry} complex unit: Value is Device Independent
*  Pixels. */
    public static final int COMPLEX_UNIT_DIP = 1;
/** {@link DimensionEntry} complex unit: Value is a scaled pixel. */
    public static final int COMPLEX_UNIT_SP = 2;
/** {@link DimensionEntry} complex unit: Value is in points. */
    public static final int COMPLEX_UNIT_PT = 3;
/** {@link DimensionEntry} complex unit: Value is in inches. */
    public static final int COMPLEX_UNIT_IN = 4;
/** {@link DimensionEntry} complex unit: Value is in millimeters. */
    public static final int COMPLEX_UNIT_MM = 5;

private final static DimensionEntry[] sDimensions = new DimensionEntry[] {
new DimensionEntry("px", COMPLEX_UNIT_PX),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 2a70dd0..286591f 100755

//Synthetic comment -- @@ -448,9 +448,9 @@
}

/**
     * Returns the layout url attribute value for the closest surrounding include element
     * parent, or null if this {@link CanvasViewInfo} is not rendered as part of an
     * include tag.
*
* @return the layout url attribute value for the surrounding include tag, or null if
*         not applicable
//Synthetic comment -- @@ -460,14 +460,21 @@
while (curr != null) {
if (curr.mUiViewNode != null) {
Node node = curr.mUiViewNode.getXmlNode();
                if (node != null && node.getNamespaceURI() == null
                        && node.getNodeType() == Node.ELEMENT_NODE
                        && LayoutDescriptors.VIEW_INCLUDE.equals(node.getNodeName())) {
                    // Note: the layout attribute is NOT in the Android namespace
                    Element element = (Element) node;
                    String url = element.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
                    if (url.length() > 0) {
                        return url;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index f188e90..893849b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -210,7 +211,7 @@
}
}

        insertListPreviewType(endId);
insertVisualRefactorings(endId);
}

//Synthetic comment -- @@ -235,8 +236,8 @@
mMenuManager.insertBefore(endId, new Separator());
}

    /** "Preview List Content" pull-right menu */
    private void insertListPreviewType(String endId) {

List<SelectionItem> selection = mCanvas.getSelectionManager().getSelections();
if (selection.size() == 0) {
//Synthetic comment -- @@ -249,6 +250,10 @@
mMenuManager.insertBefore(endId, new Separator());
mMenuManager.insertBefore(endId, new ListViewTypeMenu(mCanvas));
return;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
new file mode 100644
//Synthetic comment -- index 0000000..bb519fb

//Synthetic comment -- @@ -0,0 +1,310 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index e814b17..29811dc 100644

//Synthetic comment -- @@ -1428,7 +1428,7 @@
float xdpi = mConfigComposite.getXDpi();
float ydpi = mConfigComposite.getYDpi();

        ILayoutPullParser modelParser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, project);
ILayoutPullParser topParser = modelParser;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 2bfa841..7e1f88e 100644

//Synthetic comment -- @@ -56,6 +56,8 @@
public static final String KEY_LV_HEADER = "listheader";    //$NON-NLS-1$
/** The property key, included in comments, which references a list footer layout */
public static final String KEY_LV_FOOTER = "listfooter";    //$NON-NLS-1$

/** The metadata class is a singleton for now since it has no state of its own */
private static final LayoutMetadata sInstance = new LayoutMetadata();
//Synthetic comment -- @@ -84,8 +86,10 @@
public String getProperty(IDocument document, Node node, String name) {
IStructuredModel model = null;
try {
            IModelManager modelManager = StructuredModelManager.getModelManager();
            model = modelManager.getExistingModelForRead(document);

Node comment = findComment(node);
if (comment != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index e522957..5476c60 100644

//Synthetic comment -- @@ -132,8 +132,8 @@
}

/**
     * Action which brings up the "Create new XML File" wizard, pre-selected with the
     * animation category
*/
private class PickLayoutAction extends Action {
private final String mType;







