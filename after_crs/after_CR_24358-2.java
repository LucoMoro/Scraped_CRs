/*Integrate unknown attributes in the normal UiElementNode workflow.

Somehow this should have been the default.
There's a few cases where this will allow us to simplify the code
and that will be for another CL.

Also fixeshttp://code.google.com/p/android/issues/detail?id=17762Change-Id:Ieccd36f5f4042f414311f09339ed18fc73d7b122*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index f28c681..38ab6d8 100644

//Synthetic comment -- @@ -435,7 +435,7 @@
}

UiAttributeNode currAttrNode = null;
                for (UiAttributeNode attrNode : currentUiNode.getAllUiAttributes()) {
if (attrNode.getDescriptor().getXmlLocalName().equals(attrName)) {
currAttrNode = attrNode;
break;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 07593cf..e8e3c9a 100644

//Synthetic comment -- @@ -225,7 +225,7 @@
UiElementNode node = getCurrentNode();

if (node != null) {
            Collection<UiAttributeNode> attributes = node.getAllUiAttributes();
int count = attributes.size();

return count + (mZeroAttributeIsPadding ? 1 : 0);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index c37ffe8..aef29b8 100755

//Synthetic comment -- @@ -462,7 +462,7 @@

SimpleElement e = new SimpleElement(fqcn, parentFqcn, bounds, parentBounds);

        for (UiAttributeNode attr : uiNode.getAllUiAttributes()) {
String value = attr.getCurrentValue();
if (value != null && value.length() > 0) {
AttributeDescriptor attrDesc = attr.getDescriptor();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index 38a5e6b..6a50689 100644

//Synthetic comment -- @@ -316,7 +316,7 @@

for (UiElementNode ui_node : nodeList) {
if (ui_node.getDescriptor().getXmlName().equals(nodeType)) {
                for (UiAttributeNode attr : ui_node.getAllUiAttributes()) {
if (attr.getDescriptor().getXmlLocalName().equals(
AndroidManifestDescriptors.ANDROID_NAME_ATTR)) {
if (attr.getCurrentValue().equals(className)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java
//Synthetic comment -- index 5e7ca30..c2e4f0f 100644

//Synthetic comment -- @@ -244,7 +244,7 @@
@Override
public boolean isDirty() {
if (mUiElementNode != null && !super.isDirty()) {
            for (UiAttributeNode ui_attr : mUiElementNode.getAllUiAttributes()) {
if (ui_attr.isDirty()) {
markDirty();
break;
//Synthetic comment -- @@ -269,7 +269,7 @@
if (mUiElementNode != null) {
mEditor.wrapEditXmlModel(new Runnable() {
public void run() {
                    for (UiAttributeNode ui_attr : mUiElementNode.getAllUiAttributes()) {
ui_attr.commit();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 6506b58..64f9885 100644

//Synthetic comment -- @@ -119,7 +119,7 @@
/** A read-only view of the UI children node collection. */
private List<UiElementNode> mReadOnlyUiChildren;
/** A read-only view of the UI attributes collection. */
    private Collection<UiAttributeNode> mCachedAllUiAttributes;
/** A map of hidden attribute descriptors. Key is the XML name. */
private Map<String, AttributeDescriptor> mCachedHiddenAttributes;
/** An optional list of {@link IUiUpdateListener}. Most element nodes will not have any
//Synthetic comment -- @@ -181,7 +181,7 @@
*/
private void clearAttributes() {
mUiAttributes = null;
        mCachedAllUiAttributes = null;
mCachedHiddenAttributes = null;
mUnknownUiAttributes = new HashSet<UiAttributeNode>();
}
//Synthetic comment -- @@ -598,17 +598,27 @@
}

/**
     * Returns a collection containing all the known attributes as well as
     * all the unknown ui attributes.
     *
* @return A read-only version of the attributes collection.
*/
    public Collection<UiAttributeNode> getAllUiAttributes() {
        if (mCachedAllUiAttributes == null) {

            List<UiAttributeNode> allValues =
                new ArrayList<UiAttributeNode>(getInternalUiAttributes().values());
            allValues.addAll(mUnknownUiAttributes);

            mCachedAllUiAttributes = Collections.unmodifiableCollection(allValues);
}
        return mCachedAllUiAttributes;
}

/**
     * Returns all the unknown ui attributes, that is those we found defined in the
     * actual XML but that we don't have descriptors for.
     *
* @return A read-only version of the unknown attributes collection.
*/
public Collection<UiAttributeNode> getUnknownUiAttributes() {
//Synthetic comment -- @@ -637,8 +647,7 @@
}

// get the error value from the attributes.
        for (UiAttributeNode attribute : getAllUiAttributes()) {
if (attribute.hasError()) {
return true;
}
//Synthetic comment -- @@ -880,11 +889,7 @@
* This is called by the UI when the embedding part needs to be committed.
*/
public void commit() {
        for (UiAttributeNode uiAttr : getAllUiAttributes()) {
uiAttr.commit();
}
}
//Synthetic comment -- @@ -896,13 +901,7 @@
* loaded from the model.
*/
public boolean isDirty() {
        for (UiAttributeNode uiAttr : getAllUiAttributes()) {
if (uiAttr.isDirty()) {
return true;
}
//Synthetic comment -- @@ -1368,7 +1367,7 @@
}

// Clone the current list of unknown attributes. We'll then remove from this list when
        // we find attributes which are still unknown. What will be left are the old unknown
// attributes that have been deleted in the current XML attribute list.
@SuppressWarnings("unchecked")
HashSet<UiAttributeNode> deleted = (HashSet<UiAttributeNode>) mUnknownUiAttributes.clone();
//Synthetic comment -- @@ -1418,6 +1417,7 @@
// Remove from the internal list unknown attributes that have been deleted from the xml
for (UiAttributeNode a : deleted) {
mUnknownUiAttributes.remove(a);
                mCachedAllUiAttributes = null;
}
}
}
//Synthetic comment -- @@ -1435,6 +1435,7 @@
UiAttributeNode uiAttr = desc.createUiNode(this);
uiAttr.setDirty(true);
mUnknownUiAttributes.add(uiAttr);
        mCachedAllUiAttributes = null;
return uiAttr;
}

//Synthetic comment -- @@ -1539,10 +1540,7 @@
*/
public boolean commitDirtyAttributesToXml() {
boolean result = false;
        for (UiAttributeNode uiAttr : getAllUiAttributes()) {
if (uiAttr.isDirty()) {
result |= commitAttributeToXml(uiAttr, uiAttr.getCurrentValue());
uiAttr.setDirty(false);
//Synthetic comment -- @@ -1668,21 +1666,21 @@

// Try with all internal attributes
UiAttributeNode uiAttr = setInternalAttrValue(
                getAllUiAttributes(), attrXmlName, attrNsUri, value, override);
if (uiAttr != null) {
return uiAttr;
}

if (uiAttr == null) {
// Failed to find the attribute. For non-android attributes that is mostly expected,
            // in which case we just create a new custom one. As a side effect, we'll find the
            // attribute descriptor via getAllUiAttributes().
            addUnknownAttribute(attrXmlName, attrXmlName, attrNsUri);

            // We've created the attribute, but not actually set the value on it, so let's do it.
            // Try with the updated internal attributes.
            uiAttr = setInternalAttrValue(
                    getAllUiAttributes(), attrXmlName, attrNsUri, value, override);
}

return uiAttr;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index dce8160..f2b9a55 100644

//Synthetic comment -- @@ -755,7 +755,7 @@
name = name.substring(pos + 1);
}

            for (UiAttributeNode attrNode : currentUiNode.getAllUiAttributes()) {
if (attrNode.getDescriptor().getXmlLocalName().equals(name)) {
AttributeDescriptor desc = attrNode.getDescriptor();
if (desc instanceof ReferenceAttributeDescriptor) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java
//Synthetic comment -- index ccf4e83..a5149ae 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
assertSame(mManifestDesc, ui.getDescriptor());
assertNull(ui.getUiParent());
assertEquals(0, ui.getUiChildren().size());
        assertEquals(0, ui.getAllUiAttributes().size());
}

/**
//Synthetic comment -- @@ -139,14 +139,14 @@
ui.loadFromXmlNode(root);
assertEquals("manifest", ui.getDescriptor().getXmlName());
assertEquals(1, ui.getUiChildren().size());
        assertEquals(0, ui.getAllUiAttributes().size());

// get /manifest/application
Iterator<UiElementNode> ui_child_it = ui.getUiChildren().iterator();
UiElementNode application = ui_child_it.next();
assertEquals("application", application.getDescriptor().getXmlName());
assertEquals(0, application.getUiChildren().size());
        assertEquals(0, application.getAllUiAttributes().size());
}


//Synthetic comment -- @@ -161,21 +161,21 @@
ui.loadFromXmlNode(root);
assertEquals("manifest", ui.getDescriptor().getXmlName());
assertEquals(2, ui.getUiChildren().size());
        assertEquals(0, ui.getAllUiAttributes().size());

// get /manifest/application
Iterator<UiElementNode> ui_child_it = ui.getUiChildren().iterator();
UiElementNode application = ui_child_it.next();
assertEquals("application", application.getDescriptor().getXmlName());
assertEquals(0, application.getUiChildren().size());
        assertEquals(0, application.getAllUiAttributes().size());
assertEquals(0, application.getUiSiblingIndex());

// get /manifest/permission
UiElementNode first_permission = ui_child_it.next();
assertEquals("permission", first_permission.getDescriptor().getXmlName());
assertEquals(0, first_permission.getUiChildren().size());
        assertEquals(0, first_permission.getAllUiAttributes().size());
assertEquals(1, first_permission.getUiSiblingIndex());
}

//Synthetic comment -- @@ -206,58 +206,58 @@
ui.loadFromXmlNode(root);
assertEquals("manifest", ui.getDescriptor().getXmlName());
assertEquals(3, ui.getUiChildren().size());
        assertEquals(0, ui.getAllUiAttributes().size());

// get /manifest/application
Iterator<UiElementNode> ui_child_it = ui.getUiChildren().iterator();
UiElementNode application = ui_child_it.next();
assertEquals("application", application.getDescriptor().getXmlName());
assertEquals(4, application.getUiChildren().size());
        assertEquals(0, application.getAllUiAttributes().size());

// get /manifest/application/activity #1
Iterator<UiElementNode> app_child_it = application.getUiChildren().iterator();
UiElementNode first_activity = app_child_it.next();
assertEquals("activity", first_activity.getDescriptor().getXmlName());
assertEquals(0, first_activity.getUiChildren().size());
        assertEquals(0, first_activity.getAllUiAttributes().size());

// get /manifest/application/activity #2
UiElementNode second_activity = app_child_it.next();
assertEquals("activity", second_activity.getDescriptor().getXmlName());
assertEquals(1, second_activity.getUiChildren().size());
        assertEquals(0, second_activity.getAllUiAttributes().size());

// get /manifest/application/activity #2/intent-filter #1
Iterator<UiElementNode> activity_child_it = second_activity.getUiChildren().iterator();
UiElementNode intent_filter = activity_child_it.next();
assertEquals("intent-filter", intent_filter.getDescriptor().getXmlName());
assertEquals(0, intent_filter.getUiChildren().size());
        assertEquals(0, intent_filter.getAllUiAttributes().size());

// get /manifest/application/provider #1
UiElementNode first_provider = app_child_it.next();
assertEquals("provider", first_provider.getDescriptor().getXmlName());
assertEquals(0, first_provider.getUiChildren().size());
        assertEquals(0, first_provider.getAllUiAttributes().size());

// get /manifest/application/provider #2
UiElementNode second_provider = app_child_it.next();
assertEquals("provider", second_provider.getDescriptor().getXmlName());
assertEquals(0, second_provider.getUiChildren().size());
        assertEquals(0, second_provider.getAllUiAttributes().size());

// get /manifest/permission #1
UiElementNode first_permission = ui_child_it.next();
assertEquals("permission", first_permission.getDescriptor().getXmlName());
assertEquals(0, first_permission.getUiChildren().size());
        assertEquals(0, first_permission.getAllUiAttributes().size());

// get /manifest/permission #1
UiElementNode second_permission = ui_child_it.next();
assertEquals("permission", second_permission.getDescriptor().getXmlName());
assertEquals(0, second_permission.getUiChildren().size());
        assertEquals(0, second_permission.getAllUiAttributes().size());
}









