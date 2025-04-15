/*Update ViewInfo in the layoutlib API.

- support for View and LayoutParams
- support for default property value map.

Change-Id:I70028710b1f76329a8bd501428fbd68a14fafa1e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index cd927ec..26e960e 100755

//Synthetic comment -- @@ -155,7 +155,7 @@
return "";
}

        String name = gre.callGetDisplayName(canvasViewInfo.getUiViewKey());

if (name == null) {
// The name is typically a fully-qualified class name. Let's make it a tad shorter.
//Synthetic comment -- @@ -192,7 +192,7 @@
LayoutEditor layoutEditor = canvas.getLayoutEditor();
for (CanvasSelection cs : selection) {
CanvasViewInfo vi = cs.getViewInfo();
            UiViewElementNode key = vi.getUiViewKey();
Node node = key.getXmlNode();
String t = layoutEditor.getXmlText(node);
if (t != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index bff1606..2da2467 100755

//Synthetic comment -- @@ -59,7 +59,7 @@
private final Rectangle mAbsRect;
private final Rectangle mSelectionRect;
private final String mName;
    private final UiViewElementNode mUiViewKey;
private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

//Synthetic comment -- @@ -85,13 +85,13 @@
mParent = parent;
mName = viewInfo.getClassName();

        // The ViewInfo#getViewKey() method returns a key which depends on the
        // IXmlPullParser used to parse the layout files. In this case, the parser is
        // guaranteed to be an UiElementPullParser, which creates keys that are of type
        // UiViewElementNode.
// We'll simply crash if the type is not right, as this is not supposed to happen
// and nothing could work if there's a type mismatch.
        mUiViewKey  = (UiViewElementNode) viewInfo.getViewKey();

int x = viewInfo.getLeft();
int y = viewInfo.getTop();
//Synthetic comment -- @@ -110,7 +110,7 @@
// Only use children which have a ViewKey of the correct type.
// We can't interact with those when they have a null key or
// an incompatible type.
                if (child.getViewKey() instanceof UiViewElementNode) {
mChildren.add(new CanvasViewInfo(child, this, x, y));
}
}
//Synthetic comment -- @@ -153,12 +153,12 @@
}

/**
     * Returns the view key. Could be null, although unlikely.
* @return An {@link UiViewElementNode} that uniquely identifies the object in the XML model.
     * @see ViewInfo#getViewKey()
*/
    public UiViewElementNode getUiViewKey() {
        return mUiViewKey;
}

/**
//Synthetic comment -- @@ -211,7 +211,7 @@
// ---- Implementation of IPropertySource

public Object getEditableValue() {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
return ((IPropertySource) uiView).getEditableValue();
}
//Synthetic comment -- @@ -219,7 +219,7 @@
}

public IPropertyDescriptor[] getPropertyDescriptors() {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
return ((IPropertySource) uiView).getPropertyDescriptors();
}
//Synthetic comment -- @@ -227,7 +227,7 @@
}

public Object getPropertyValue(Object id) {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
return ((IPropertySource) uiView).getPropertyValue(id);
}
//Synthetic comment -- @@ -235,7 +235,7 @@
}

public boolean isPropertySet(Object id) {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
return ((IPropertySource) uiView).isPropertySet(id);
}
//Synthetic comment -- @@ -243,14 +243,14 @@
}

public void resetPropertyValue(Object id) {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
((IPropertySource) uiView).resetPropertyValue(id);
}
}

public void setPropertyValue(Object id, Object value) {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
((IPropertySource) uiView).setPropertyValue(id, value);
}
//Synthetic comment -- @@ -263,7 +263,7 @@
* @return The XML node corresponding to this info object, or null
*/
public Node getXmlNode() {
        UiViewElementNode uiView = getUiViewKey();
if (uiView != null) {
return uiView.getXmlNode();
}
//Synthetic comment -- @@ -281,8 +281,8 @@
// The root element is the one whose GRAND parent
// is null (because the parent will be a -document-
// node).
        return mUiViewKey == null || mUiViewKey.getUiParent() == null ||
            mUiViewKey.getUiParent().getUiParent() == null;
}

/**
//Synthetic comment -- @@ -295,7 +295,7 @@
*/
public boolean isInvisibleParent() {
if (mAbsRect.width < SELECTION_MIN_SIZE || mAbsRect.height < SELECTION_MIN_SIZE) {
            return mUiViewKey != null && mUiViewKey.getDescriptor().hasChildren();
}

return false;
//Synthetic comment -- @@ -331,7 +331,7 @@
*/
/* package */ SimpleElement toSimpleElement() {

        UiViewElementNode uiNode = getUiViewKey();

String fqcn = SimpleXmlTransfer.getFqcn(uiNode.getDescriptor());
String parentFqcn = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 00b2518..680c12e 100644

//Synthetic comment -- @@ -184,7 +184,7 @@
CanvasViewInfo vi = cs.getViewInfo();
// You can't delete the root element
if (vi != null && !vi.isRoot()) {
                        UiViewElementNode ui = vi.getUiViewKey();
if (ui != null) {
ui.deleteXmlNode();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e313f00..0374fce 100755

//Synthetic comment -- @@ -620,7 +620,7 @@
// O(n^2) here, but both the selection size and especially the
// invisibleParents size are expected to be small
if (invisibleParents.contains(viewInfo)) {
                        UiViewElementNode node = viewInfo.getUiViewKey();
if (node != null) {
if (result == null) {
result = new HashSet<UiElementNode>();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 0a267d6..198e507 100755

//Synthetic comment -- @@ -129,7 +129,7 @@
tv.setComparer(new IElementComparer() {
public int hashCode(Object element) {
if (element instanceof CanvasViewInfo) {
                    UiViewElementNode key = ((CanvasViewInfo) element).getUiViewKey();
if (key != null) {
return key.hashCode();
}
//Synthetic comment -- @@ -142,8 +142,8 @@

public boolean equals(Object a, Object b) {
if (a instanceof CanvasViewInfo && b instanceof CanvasViewInfo) {
                    UiViewElementNode keyA = ((CanvasViewInfo) a).getUiViewKey();
                    UiViewElementNode keyB = ((CanvasViewInfo) b).getUiViewKey();
if (keyA != null) {
return keyA.equals(keyB);
}
//Synthetic comment -- @@ -350,7 +350,7 @@
*/
public Image getImage(Object element) {
if (element instanceof CanvasViewInfo) {
                element = ((CanvasViewInfo) element).getUiViewKey();
}

if (element instanceof UiElementNode) {
//Synthetic comment -- @@ -376,7 +376,7 @@
*/
public String getText(Object element) {
if (element instanceof CanvasViewInfo) {
                element = ((CanvasViewInfo) element).getUiViewKey();
}

if (element instanceof UiElementNode) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 3db65f1..3b85a3a 100644

//Synthetic comment -- @@ -555,7 +555,7 @@

// Check if the selected object still exists
ViewHierarchy viewHierarchy = mCanvas.getViewHierarchy();
            Object key = s.getViewInfo().getUiViewKey();
CanvasViewInfo vi = viewHierarchy.findViewInfoKey(key, lastValidViewInfoRoot);

// Remove the previous selection -- if the selected object still exists
//Synthetic comment -- @@ -625,7 +625,7 @@
for (Iterator<CanvasSelection> it = selection.iterator(); it.hasNext(); ) {
CanvasSelection cs = it.next();
CanvasViewInfo vi = cs.getViewInfo();
            UiViewElementNode key = vi == null ? null : vi.getUiViewKey();
Node node = key == null ? null : key.getXmlNode();
if (node == null) {
// Missing ViewInfo or view key or XML, discard this.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index e7c718f..c737fc4 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
return;
}

        UiViewElementNode key = vi.getUiViewKey();

if (key != null) {
mCanvas.getNodeFactory().create(vi);
//Synthetic comment -- @@ -187,7 +187,7 @@
if (vi.isInvisibleParent()) {
mInvisibleParents.add(vi);
} else if (invisibleNodes != null) {
            UiViewElementNode key = vi.getUiViewKey();

if (key != null && invisibleNodes.contains(key)) {
vi.setExploded(true);
//Synthetic comment -- @@ -461,7 +461,7 @@
if (canvasViewInfo == null) {
return null;
}
        if (canvasViewInfo.getUiViewKey() == viewKey) {
return canvasViewInfo;
}

//Synthetic comment -- @@ -545,7 +545,7 @@

Set<UiElementNode> nodes = new HashSet<UiElementNode>(mInvisibleParents.size());
for (CanvasViewInfo info : mInvisibleParents) {
            UiViewElementNode node = info.getUiViewKey();
if (node != null) {
nodes.add(node);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java
//Synthetic comment -- index f4ebc23..825a0e4 100755

//Synthetic comment -- @@ -41,7 +41,7 @@
* {@link CanvasViewInfo}. The bounds of the node are set to the canvas view bounds.
*/
public NodeProxy create(CanvasViewInfo canvasViewInfo) {
        return create(canvasViewInfo.getUiViewKey(), canvasViewInfo.getAbsRect());
}

/**








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 8e0fe73..85c7365 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.layoutlib.api.SceneResult.LayoutStatus;

import java.awt.image.BufferedImage;

/**
* An object allowing interaction with an Android layout.
//Synthetic comment -- @@ -81,6 +82,17 @@
return null;
}

/**
* Re-renders the layout as-is.
* In case of success, this should be followed by calls to {@link #getRootView()} and








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ViewInfo.java
//Synthetic comment -- index d991cc8..57be46f 100644

//Synthetic comment -- @@ -18,35 +18,48 @@

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* Layout information for a specific view object
*/
public class ViewInfo {

    protected final Object mKey;
    protected final String mName;
    protected final int mLeft;
    protected final int mRight;
    protected final int mTop;
    protected final int mBottom;
    protected List<ViewInfo> mChildren = Collections.emptyList();

    public ViewInfo(String name, Object key, int left, int top, int right, int bottom) {
mName = name;
        mKey = key;
mLeft = left;
mRight = right;
mTop = top;
mBottom = bottom;
}

/**
* Sets the list of children {@link ViewInfo}.
*/
public void setChildren(List<ViewInfo> children) {
        mChildren = Collections.unmodifiableList(children);
}

/**
//Synthetic comment -- @@ -57,12 +70,12 @@
}

/**
     * Returns the key associated with the node. Can be null.
*
* @see IXmlPullParser#getViewKey()
*/
    public Object getViewKey() {
        return mKey;
}

/**
//Synthetic comment -- @@ -101,11 +114,12 @@
}

/**
     * Returns a map of default values for some properties. The map key is the property name,
     * as found in the XML.
*/
    public Map<String, String> getDefaultPropertyValues() {
        return null;
}

/**
//Synthetic comment -- @@ -113,7 +127,7 @@
* to query the object properties that are not in the XML and not in the map returned
* by {@link #getDefaultPropertyValues()}.
*/
    public Object getViewObject() {
        return null;
}
}







