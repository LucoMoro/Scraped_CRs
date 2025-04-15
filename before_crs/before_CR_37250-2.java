/*Fix nullness annotations

Eclipse 4.2 includes analysis support for @Nullable and @NonNull
annotations.  However, it requires these annotations to be *repeated*
on every single method implementing or overriding a superclass or
interface method (!).

This changeset basically applies the quickfixes to inline these
annotations.  It also changes the retention of our nullness
annotations from source to class, since without this Eclipse believes
that a @NonNull annotation downstream is a redefinition of a @Nullable
annotation.

Finally, the null analysis revealed a dozen or so places where the
nullness annotation was either wrong, or some null checking on
parameters or return values needed to be done.

Change-Id:I43b4e56e2d025a8a4c92a8873f55c13cdbc4c1cb*/
//Synthetic comment -- diff --git a/common/src/com/android/annotations/NonNull.java b/common/src/com/android/annotations/NonNull.java
//Synthetic comment -- index e306a31..973ebb6 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
* This is a marker annotation and it has no specific attributes.
*/
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD,PARAMETER,LOCAL_VARIABLE,FIELD})
public @interface NonNull {
}








//Synthetic comment -- diff --git a/common/src/com/android/annotations/NonNullByDefault.java b/common/src/com/android/annotations/NonNullByDefault.java
//Synthetic comment -- index 23903d1..3db891c 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
* This is a marker annotation and it has no specific attributes.
*/
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({PACKAGE, TYPE})
public @interface NonNullByDefault {
}








//Synthetic comment -- diff --git a/common/src/com/android/annotations/Nullable.java b/common/src/com/android/annotations/Nullable.java
//Synthetic comment -- index 376c1f6..d9c3861 100755

//Synthetic comment -- @@ -43,7 +43,7 @@
* This is a marker annotation and it has no specific attributes.
*/
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD, PARAMETER, LOCAL_VARIABLE, FIELD})
public @interface Nullable {
}








//Synthetic comment -- diff --git a/common/src/com/android/util/PositionXmlParser.java b/common/src/com/android/util/PositionXmlParser.java
//Synthetic comment -- index 052348d..6eee96f 100644

//Synthetic comment -- @@ -677,7 +677,7 @@
}

@Override
        public void setEnd(Position end) {
mEnd = end;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 57406d5..d7adbae 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_X;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_Y;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -45,7 +47,7 @@
public class AbsoluteLayoutRule extends BaseLayoutRule {

@Override
    public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(2);
infos.add("AbsoluteLayout is deprecated.");
infos.add("Use other layouts instead.");
//Synthetic comment -- @@ -56,8 +58,8 @@
// The AbsoluteLayout accepts any drag'n'drop anywhere on its surface.

@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView,
            final IDragElement[] elements) {

if (elements.length == 0) {
return null;
//Synthetic comment -- @@ -65,7 +67,8 @@

DropFeedback df = new DropFeedback(null, new IFeedbackPainter() {
@Override
            public void paint(IGraphics gc, INode node, DropFeedback feedback) {
// Paint callback for the AbsoluteLayout.
// This is called by the canvas when a draw is needed.
drawFeedback(gc, node, elements, feedback);
//Synthetic comment -- @@ -128,8 +131,8 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
// Update the data used by the DropFeedback.paintCallback above.
feedback.userData = p;
feedback.requestPaint = true;
//Synthetic comment -- @@ -138,13 +141,14 @@
}

@Override
    public void onDropLeave(INode targetNode, IDragElement[] elements, DropFeedback feedback) {
// Nothing to do.
}

@Override
    public void onDropped(final INode targetNode, final IDragElement[] elements,
            final DropFeedback feedback, final Point p) {

final Rect b = targetNode.getBounds();
if (!b.isValid()) {
//Synthetic comment -- @@ -158,7 +162,7 @@

targetNode.editXml("Add elements to AbsoluteLayout", new INodeHandler() {
@Override
            public void handle(INode node) {
boolean first = true;
Point offset = null;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AdapterViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AdapterViewRule.java
//Synthetic comment -- index 5b23e34..28f5fc9 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -27,12 +29,14 @@
/** Rule for AdapterView subclasses that don't have more specific rules */
public class AdapterViewRule extends BaseLayoutRule {
@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView, IDragElement[] elements) {
// You are not allowed to insert children into AdapterViews; you must
// use the dedicated addView methods etc dynamically
DropFeedback dropFeedback = new DropFeedback(null,  new IFeedbackPainter() {
@Override
            public void paint(IGraphics gc, INode node, DropFeedback feedback) {
Rect b = node.getBounds();
if (b.isValid()) {
gc.useStyle(DrawingStyle.DROP_RECIPIENT);
//Synthetic comment -- @@ -50,8 +54,8 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
feedback.invalidTarget = true;
return feedback;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 581788b..6f76b51 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
//Synthetic comment -- @@ -52,7 +51,10 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
//Synthetic comment -- @@ -114,11 +116,13 @@

IMenuCallback actionCallback = new IMenuCallback() {
@Override
            public void action(RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
parentNode.editXml("Change Margins", new INodeHandler() {
@Override
                    public void handle(INode n) {
String uri = ANDROID_URI;
String all = first.getStringAttr(uri, ATTR_LAYOUT_MARGIN);
String left = first.getStringAttr(uri, ATTR_LAYOUT_MARGIN_LEFT);
//Synthetic comment -- @@ -154,8 +158,8 @@
final INode first = targets.get(0);
ChoiceProvider provider = new ChoiceProvider() {
@Override
                public void addChoices(List<String> titles, List<URL> iconUrls,
                        List<String> ids) {
IAttributeInfo info = first.getAttributeInfo(ANDROID_URI, attributeName);
if (info != null) {
// Generate list of possible gravity value constants
//Synthetic comment -- @@ -180,8 +184,10 @@
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);

final List<? extends INode> targets = children == null || children.size() == 0 ?
//Synthetic comment -- @@ -192,8 +198,11 @@
// Shared action callback
IMenuCallback actionCallback = new IMenuCallback() {
@Override
            public void action(RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
final String actionId = action.getId();
final String undoLabel;
if (actionId.equals(ACTION_FILL_WIDTH)) {
//Synthetic comment -- @@ -205,7 +214,7 @@
}
parentNode.editXml(undoLabel, new INodeHandler() {
@Override
                    public void handle(INode n) {
String attribute = actionId.equals(ACTION_FILL_WIDTH)
? ATTR_LAYOUT_WIDTH : ATTR_LAYOUT_HEIGHT;
String value;
//Synthetic comment -- @@ -244,7 +253,8 @@
* Derived layouts should override this behavior if not appropriate.
*/
@Override
    public void onPaste(INode targetNode, Object targetView, IDragElement[] elements) {
DropFeedback feedback = onDropEnter(targetNode, targetView, elements);
if (feedback != null) {
Point p = targetNode.getBounds().getTopLeft();
//Synthetic comment -- @@ -576,7 +586,7 @@
targetNode.editXml("Insert Elements", new INodeHandler() {

@Override
            public void handle(INode node) {
// Now write the new elements.
int insertPos = initialInsertPos;
for (IDragElement element : elements) {
//Synthetic comment -- @@ -606,9 +616,9 @@
}

@Override
    public DropFeedback onResizeBegin(INode child, INode parent,
            SegmentType horizontalEdge, SegmentType verticalEdge,
            Object childView, Object parentView) {
ResizeState state = createResizeState(parent, parentView, child);
state.horizontalEdgeType = horizontalEdge;
state.verticalEdgeType = verticalEdge;
//Synthetic comment -- @@ -618,7 +628,8 @@
Map<INode, Rect> sizes = mRulesEngine.measureChildren(parent,
new IClientRulesEngine.AttributeFilter() {
@Override
                    public String getAttribute(INode node, String namespace, String localName) {
// Change attributes to wrap_content
if (ATTR_LAYOUT_WIDTH.equals(localName)
&& SdkConstants.NS_RESOURCES.equals(namespace)) {
//Synthetic comment -- @@ -638,7 +649,8 @@

return new DropFeedback(state, new IFeedbackPainter() {
@Override
            public void paint(IGraphics gc, INode node, DropFeedback feedback) {
ResizeState resizeState = (ResizeState) feedback.userData;
if (resizeState != null && resizeState.bounds != null) {
paintResizeFeedback(gc, node, resizeState);
//Synthetic comment -- @@ -737,8 +749,8 @@
}

@Override
    public void onResizeUpdate(DropFeedback feedback, INode child, INode parent,
            Rect newBounds, int modifierMask) {
ResizeState state = (ResizeState) feedback.userData;
state.bounds = newBounds;
state.modifierMask = modifierMask;
//Synthetic comment -- @@ -799,14 +811,14 @@
}

@Override
    public void onResizeEnd(DropFeedback feedback, INode child, final INode parent,
            final Rect newBounds) {
final Rect oldBounds = child.getBounds();
if (oldBounds.w != newBounds.w || oldBounds.h != newBounds.h) {
final ResizeState state = (ResizeState) feedback.userData;
child.editXml("Resize", new INodeHandler() {
@Override
                public void handle(INode n) {
setNewSizeBounds(state, n, parent, oldBounds, newBounds,
state.horizontalEdgeType, state.verticalEdgeType);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 507bed5..4cc5f5e 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
new HashMap<String, Map<String, Prop>>();

@Override
    public boolean onInitialize(String fqcn, IClientRulesEngine engine) {
this.mRulesEngine = engine;

// This base rule can handle any class so we don't need to filter on
//Synthetic comment -- @@ -124,7 +124,8 @@
* - List of all other simple toggle attributes.
*/
@Override
    public void addContextMenuActions(List<RuleAction> actions, final INode selectedNode) {
String width = null;
String currentWidth = selectedNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_WIDTH);

//Synthetic comment -- @@ -155,9 +156,9 @@
final IMenuCallback onChange = new IMenuCallback() {
@Override
public void action(
                    final RuleAction action,
                    final List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
String fullActionId = action.getId();
boolean isProp = fullActionId.startsWith(PROP_PREFIX);
final String actionId = isProp ?
//Synthetic comment -- @@ -425,14 +426,14 @@
onChange /*callback*/, null /*icon*/, 50,
true /*supportsMultipleNodes*/, new ActionProvider() {
@Override
            public List<RuleAction> getNestedActions(INode node) {
List<RuleAction> propertyActionTypes = new ArrayList<RuleAction>();
propertyActionTypes.add(RuleAction.createChoices(
"recent", "Recent", //$NON-NLS-1$
onChange /*callback*/, null /*icon*/, 10,
true /*supportsMultipleNodes*/, new ActionProvider() {
@Override
                            public List<RuleAction> getNestedActions(INode n) {
List<RuleAction> propertyActions = new ArrayList<RuleAction>();
addRecentPropertyActions(propertyActions, n, onChange);
return propertyActions;
//Synthetic comment -- @@ -449,7 +450,7 @@
onChange /*callback*/, null /*icon*/, 60,
true /*supportsMultipleNodes*/, new ActionProvider() {
@Override
                            public List<RuleAction> getNestedActions(INode n) {
List<RuleAction> propertyActions = new ArrayList<RuleAction>();
addPropertyActions(propertyActions, n, onChange, null, true);
return propertyActions;
//Synthetic comment -- @@ -463,7 +464,7 @@
onChange /*callback*/, null /*icon*/, 80,
true /*supportsMultipleNodes*/, new ActionProvider() {
@Override
                            public List<RuleAction> getNestedActions(INode n) {
List<RuleAction> propertyActions = new ArrayList<RuleAction>();
addPropertyActions(propertyActions, n, onChange, null, false);
return propertyActions;
//Synthetic comment -- @@ -527,7 +528,7 @@
onChange /*callback*/, null /*icon*/, sortPriority++,
true /*supportsMultipleNodes*/, new ActionProvider() {
@Override
                        public List<RuleAction> getNestedActions(INode n) {
List<RuleAction> propertyActions = new ArrayList<RuleAction>();
addPropertyActions(propertyActions, n, onChange, definedBy, false);
return propertyActions;
//Synthetic comment -- @@ -744,7 +745,8 @@
*/
private static ChoiceProvider BOOLEAN_CHOICE_PROVIDER = new ChoiceProvider() {
@Override
        public void addChoices(List<String> titles, List<URL> iconUrls, List<String> ids) {
titles.add("True");
ids.add(TRUE_ID);

//Synthetic comment -- @@ -772,7 +774,8 @@
}

@Override
        public void addChoices(List<String> titles, List<URL> iconUrls, List<String> ids) {
for (Entry<String, String> entry : mProperty.getChoices().entrySet()) {
ids.add(entry.getKey());
titles.add(entry.getValue());
//Synthetic comment -- @@ -888,7 +891,8 @@
* an indication of where to paste.
*/
@Override
    public void onPaste(INode targetNode, Object targetView, IDragElement[] elements) {
//
INode parent = targetNode.getParent();
if (parent != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/CalendarViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/CalendarViewRule.java
//Synthetic comment -- index c580d8a..c509b95 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -30,7 +31,8 @@
public class CalendarViewRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

// CalendarViews need a lot of space, and the wrapping doesn't seem to work








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java
//Synthetic comment -- index e7a129b..86855ae 100644

//Synthetic comment -- @@ -16,13 +16,14 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EDIT_TEXT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -33,7 +34,8 @@
public class DialerFilterRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

// A DialerFilter requires a couple of nested EditTexts with fixed ids:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/EditTextRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/EditTextRule.java
//Synthetic comment -- index 875756b..dc60086 100644

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_EMS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.REQUEST_FOCUS;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -35,7 +37,8 @@
public class EditTextRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (parent != null) {
//Synthetic comment -- @@ -57,7 +60,8 @@
* Adds a "Request Focus" menu item.
*/
@Override
    public void addContextMenuActions(List<RuleAction> actions, final INode selectedNode) {
super.addContextMenuActions(actions, selectedNode);

final boolean hasFocus = hasFocus(selectedNode);
//Synthetic comment -- @@ -65,11 +69,14 @@

IMenuCallback onChange = new IMenuCallback() {
@Override
            public void action(RuleAction menuAction, List<? extends INode> selectedNodes,
                    String valueId, Boolean newValue) {
selectedNode.editXml(label, new INodeHandler() {
@Override
                    public void handle(INode node) {
INode focus = findFocus(findRoot(node));
if (focus != null && focus.getParent() != null) {
focus.getParent().removeChild(focus);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FragmentRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FragmentRule.java
//Synthetic comment -- index fdef082..533795d 100644

//Synthetic comment -- @@ -15,9 +15,10 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -27,7 +28,8 @@
*/
public class FragmentRule extends BaseViewRule {
@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
// When dropping a fragment tag, ask the user which layout to include.
if (insertType == InsertType.CREATE) { // NOT InsertType.CREATE_PREVIEW
String fqcn = mRulesEngine.displayFragmentSourceInput();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index 8a93fef..bbe4f1d 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -50,15 +52,16 @@
// The FrameLayout accepts any drag'n'drop anywhere on its surface.

@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView,
            final IDragElement[] elements) {
if (elements.length == 0) {
return null;
}

return new DropFeedback(null, new IFeedbackPainter() {
@Override
            public void paint(IGraphics gc, INode node, DropFeedback feedback) {
drawFeedback(gc, node, elements, feedback);
}
});
//Synthetic comment -- @@ -113,21 +116,22 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
feedback.userData = p;
feedback.requestPaint = true;
return feedback;
}

@Override
    public void onDropLeave(INode targetNode, IDragElement[] elements, DropFeedback feedback) {
// ignore
}

@Override
    public void onDropped(final INode targetNode, final IDragElement[] elements,
            final DropFeedback feedback, final Point p) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;
//Synthetic comment -- @@ -141,7 +145,7 @@
targetNode.editXml("Add elements to FrameLayout", new INodeHandler() {

@Override
            public void handle(INode node) {

// Now write the new elements.
for (IDragElement element : elements) {
//Synthetic comment -- @@ -159,8 +163,10 @@
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);
actions.add(RuleAction.createSeparator(25));
actions.add(createMarginAction(parentNode, children));
//Synthetic comment -- @@ -170,7 +176,8 @@
}

@Override
    public void onChildInserted(INode node, INode parent, InsertType insertType) {
// Look at the fill preferences and fill embedded layouts etc
String fqcn = node.getFqcn();
IViewMetadata metadata = mRulesEngine.getMetadata(fqcn);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index c51d229..a737251 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
//Synthetic comment -- @@ -30,7 +29,10 @@
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -148,8 +150,10 @@
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);

String namespace = getNamespace(parentNode);
//Synthetic comment -- @@ -174,11 +178,14 @@

IMenuCallback actionCallback = new IMenuCallback() {
@Override
            public void action(final RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
parentNode.editXml("Add/Remove Row/Column", new INodeHandler() {
@Override
                    public void handle(INode n) {
String id = action.getId();
if (id.equals(ACTION_SHOW_STRUCTURE)) {
sShowStructure = !sShowStructure;
//Synthetic comment -- @@ -268,15 +275,16 @@
}

@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView, IDragElement[] elements) {
GridDropHandler userData = new GridDropHandler(this, targetNode, targetView);
IFeedbackPainter painter = GridLayoutPainter.createDropFeedbackPainter(this, elements);
return new DropFeedback(userData, painter);
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
feedback.requestPaint = true;

GridDropHandler handler = (GridDropHandler) feedback.userData;
//Synthetic comment -- @@ -286,8 +294,8 @@
}

@Override
    public void onDropped(final INode targetNode, final IDragElement[] elements,
            DropFeedback feedback, Point p) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;
//Synthetic comment -- @@ -319,7 +327,8 @@
}

@Override
    public void onChildInserted(INode node, INode parent, InsertType insertType) {
if (insertType == InsertType.MOVE_WITHIN) {
// Don't adjust widths/heights/weights when just moving within a single layout
return;
//Synthetic comment -- @@ -386,7 +395,7 @@
}

@Override
    public void onRemovingChildren(List<INode> deleted, INode parent) {
super.onRemovingChildren(deleted, parent);

// Attempt to clean up spacer objects for any newly-empty rows or columns
//Synthetic comment -- @@ -521,8 +530,8 @@
}

@Override
    public void paintSelectionFeedback(IGraphics graphics, INode parentNode,
            List<? extends INode> childNodes, Object view) {
super.paintSelectionFeedback(graphics, parentNode, childNodes, view);

if (sShowStructure) {
//Synthetic comment -- @@ -569,7 +578,10 @@
* approach #3 above.
*/
@Override
    public void onPaste(INode targetNode, Object targetView, IDragElement[] elements) {
DropFeedback feedback = onDropEnter(targetNode, targetView, elements);
if (feedback != null) {
Rect b = targetNode.getBounds();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java
//Synthetic comment -- index bc3de5e..7eb3474 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -30,7 +31,8 @@
public class GridViewRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, getFillParentValueName());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index b2ea435..e7be263 100644

//Synthetic comment -- @@ -16,13 +16,15 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -39,7 +41,8 @@
public class HorizontalScrollViewRule extends FrameLayoutRule {

@Override
    public void onChildInserted(INode child, INode parent, InsertType insertType) {
super.onChildInserted(child, parent, insertType);

// The child of the ScrollView should fill in both directions
//Synthetic comment -- @@ -49,7 +52,8 @@
}

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {
//Synthetic comment -- @@ -62,8 +66,8 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
DropFeedback f = super.onDropMove(targetNode, elements, feedback, p);

// HorizontalScrollViews only allow a single child








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IgnoredLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IgnoredLayoutRule.java
//Synthetic comment -- index 999c6a0..3a65a86 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;
//Synthetic comment -- @@ -32,7 +34,8 @@
*/
public abstract class IgnoredLayoutRule extends BaseLayoutRule {
@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView, IDragElement[] elements) {
// Do nothing; this layout rule corresponds to a layout that
// should not be handled as a layout by the visual editor - usually
// because some widget is extending a layout for implementation purposes








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java
//Synthetic comment -- index e1afb56..364a3b6 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -29,7 +30,8 @@
public class ImageButtonRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

// When dropping an include tag, ask the user which layout to include.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index b255c14..08ef17c 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -29,7 +30,8 @@
public class ImageViewRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

// When dropping an include tag, ask the user which layout to include.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IncludeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IncludeRule.java
//Synthetic comment -- index a451257..978455a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -26,7 +27,8 @@
*/
public class IncludeRule extends BaseViewRule {
@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
// When dropping an include tag, ask the user which layout to include.
if (insertType == InsertType.CREATE) { // NOT InsertType.CREATE_PREVIEW
String include = mRulesEngine.displayIncludeSourceInput();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 8f8ea02..04373e1 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BASELINE_ALIGNED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
//Synthetic comment -- @@ -29,7 +28,10 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ZERO_DP;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
//Synthetic comment -- @@ -123,8 +125,10 @@
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);
if (supportsOrientation()) {
Choices action = RuleAction.createChoices(
//Synthetic comment -- @@ -146,7 +150,7 @@
if (!isVertical(parentNode)) {
String current = parentNode.getStringAttr(ANDROID_URI, ATTR_BASELINE_ALIGNED);
boolean isAligned =  current == null || Boolean.valueOf(current);
            actions.add(RuleAction.createToggle(null, "Toggle Baseline Alignment",
isAligned,
new PropertyCallback(Collections.singletonList(parentNode),
"Change Baseline Alignment",
//Synthetic comment -- @@ -167,11 +171,14 @@
// Weights
IMenuCallback actionCallback = new IMenuCallback() {
@Override
                public void action(final RuleAction action, List<? extends INode> selectedNodes,
                        final String valueId, final Boolean newValue) {
parentNode.editXml("Change Weight", new INodeHandler() {
@Override
                        public void handle(INode n) {
String id = action.getId();
if (id.equals(ACTION_WEIGHT)) {
String weight =
//Synthetic comment -- @@ -266,8 +273,8 @@
// ==== Drag'n'drop support ====

@Override
    public DropFeedback onDropEnter(final INode targetNode, Object targetView,
            final IDragElement[] elements) {

if (elements.length == 0) {
return null;
//Synthetic comment -- @@ -345,7 +352,8 @@
new IFeedbackPainter() {

@Override
                    public void paint(IGraphics gc, INode node, DropFeedback feedback) {
// Paint callback for the LinearLayout. This is called
// by the canvas when a draw is needed.
drawFeedback(gc, node, elements, feedback);
//Synthetic comment -- @@ -466,8 +474,8 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return feedback;
//Synthetic comment -- @@ -532,13 +540,14 @@
}

@Override
    public void onDropLeave(INode targetNode, IDragElement[] elements, DropFeedback feedback) {
// ignore
}

@Override
    public void onDropped(final INode targetNode, final IDragElement[] elements,
            final DropFeedback feedback, final Point p) {

LinearDropData data = (LinearDropData) feedback.userData;
final int initialInsertPos = data.getInsertPos();
//Synthetic comment -- @@ -546,7 +555,8 @@
}

@Override
    public void onChildInserted(INode node, INode parent, InsertType insertType) {
if (insertType == InsertType.MOVE_WITHIN) {
// Don't adjust widths/heights/weights when just moving within a single
// LinearLayout
//Synthetic comment -- @@ -775,7 +785,8 @@
unweightedSizes = mRulesEngine.measureChildren(layout,
new IClientRulesEngine.AttributeFilter() {
@Override
                        public String getAttribute(INode n, String namespace, String localName) {
// Clear out layout weights; we need to measure the unweighted sizes
// of the children
if (ATTR_LAYOUT_WEIGHT.equals(localName)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java
//Synthetic comment -- index 7420714..4088ab8 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -32,7 +33,8 @@
public class ListViewRule extends AdapterViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, getFillParentValueName());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java
//Synthetic comment -- index b6d0ba2..c2e78a4 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -31,7 +32,8 @@
public class MapViewRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java
//Synthetic comment -- index 12358f9..9cef9c4 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.RuleAction;

//Synthetic comment -- @@ -29,7 +30,8 @@
// on top of each other at (0,0)

@Override
    public void addContextMenuActions(List<RuleAction> actions, final INode selectedNode) {
// Deliberately ignore super.getContextMenu(); we don't want to attempt to list
// properties for the <merge> tag
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/PropertyCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/PropertyCallback.java
//Synthetic comment -- index ac1635c..da2614e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -54,8 +56,8 @@

// ---- Implements IMenuCallback ----
@Override
    public void action(RuleAction action, List<? extends INode> selectedNodes,
            final String valueId, final Boolean newValue) {
if (mTargetNodes != null && mTargetNodes.size() > 0) {
selectedNodes = mTargetNodes;
}
//Synthetic comment -- @@ -65,7 +67,7 @@
final List<? extends INode> nodes = selectedNodes;
selectedNodes.get(0).editXml(mUndoLabel, new INodeHandler() {
@Override
            public void handle(INode n) {
for (INode targetNode : nodes) {
if (valueId != null) {
targetNode.setAttribute(mUri, mAttribute, valueId);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/PropertySettingNodeHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/PropertySettingNodeHandler.java
//Synthetic comment -- index ad3ddad..13c8842 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;

//Synthetic comment -- @@ -35,7 +36,7 @@
}

@Override
    public void handle(INode node) {
node.setAttribute(mNamespaceUri, mAttribute, mValue);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/QuickContactBadgeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/QuickContactBadgeRule.java
//Synthetic comment -- index f6372fd..0164794 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -24,7 +25,8 @@
*/
public class QuickContactBadgeRule extends ImageViewRule {
@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
// Deliberately override onCreate such that we don't populate a default
// image; at design time layoutlib will supply the system default contacts
// image.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java
//Synthetic comment -- index 280019e..5ae0e92 100644

//Synthetic comment -- @@ -15,11 +15,12 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CHECKED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -30,7 +31,8 @@
*/
public class RadioGroupRule extends LinearLayoutRule {
@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index f587bef..e9cd5d5 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
//Synthetic comment -- @@ -40,7 +39,10 @@
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
//Synthetic comment -- @@ -93,7 +95,7 @@
// ==== Selection ====

@Override
    public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(18);
addAttr(ATTR_LAYOUT_ABOVE, childNode, infos);
addAttr(ATTR_LAYOUT_BELOW, childNode, infos);
//Synthetic comment -- @@ -131,8 +133,8 @@
}

@Override
    public void paintSelectionFeedback(IGraphics graphics, INode parentNode,
            List<? extends INode> childNodes, Object view) {
super.paintSelectionFeedback(graphics, parentNode, childNodes, view);

boolean showDependents = true;
//Synthetic comment -- @@ -150,14 +152,15 @@
// ==== Drag'n'drop support ====

@Override
    public DropFeedback onDropEnter(INode targetNode, Object targetView, IDragElement[] elements) {
return new DropFeedback(new MoveHandler(targetNode, elements, mRulesEngine),
new GuidelinePainter());
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
if (elements == null || elements.length == 0) {
return null;
}
//Synthetic comment -- @@ -174,12 +177,13 @@
}

@Override
    public void onDropLeave(INode targetNode, IDragElement[] elements, DropFeedback feedback) {
}

@Override
    public void onDropped(final INode targetNode, final IDragElement[] elements,
            final DropFeedback feedback, final Point p) {
final MoveHandler state = (MoveHandler) feedback.userData;

final Map<String, Pair<String, String>> idMap = getDropIdMap(targetNode, elements,
//Synthetic comment -- @@ -187,7 +191,7 @@

targetNode.editXml("Dropped", new INodeHandler() {
@Override
            public void handle(INode n) {
int index = -1;

// Remove cycles
//Synthetic comment -- @@ -235,7 +239,8 @@
}

@Override
    public void onChildInserted(INode node, INode parent, InsertType insertType) {
// TODO: Handle more generically some way to ensure that widgets with no
// intrinsic size get some minimum size until they are attached on multiple
// opposing sides.
//Synthetic comment -- @@ -246,7 +251,7 @@
}

@Override
    public void onRemovingChildren(List<INode> deleted, INode parent) {
super.onRemovingChildren(deleted, parent);

// Remove any attachments pointing to the deleted nodes.
//Synthetic comment -- @@ -286,29 +291,30 @@
// ==== Resize Support ====

@Override
    public DropFeedback onResizeBegin(INode child, INode parent,
            SegmentType horizontalEdgeType, SegmentType verticalEdgeType,
            Object childView, Object parentView) {
ResizeHandler state = new ResizeHandler(parent, child, mRulesEngine,
horizontalEdgeType, verticalEdgeType);
return new DropFeedback(state, new GuidelinePainter());
}

@Override
    public void onResizeUpdate(DropFeedback feedback, INode child, INode parent, Rect newBounds,
int modifierMask) {
ResizeHandler state = (ResizeHandler) feedback.userData;
state.updateResize(feedback, child, newBounds, modifierMask);
}

@Override
    public void onResizeEnd(DropFeedback feedback, INode child, INode parent,
            final Rect newBounds) {
final ResizeHandler state = (ResizeHandler) feedback.userData;

child.editXml("Resize", new INodeHandler() {
@Override
            public void handle(INode n) {
state.removeCycles();
state.applyConstraints(n);
}
//Synthetic comment -- @@ -318,8 +324,10 @@
// ==== Layout Actions Bar ====

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);

actions.add(createGravityAction(Collections.<INode>singletonList(parentNode),
//Synthetic comment -- @@ -329,13 +337,15 @@

IMenuCallback callback = new IMenuCallback() {
@Override
            public void action(RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
final String id = action.getId();
if (id.equals(ACTION_CENTER_VERTICAL)|| id.equals(ACTION_CENTER_HORIZONTAL)) {
parentNode.editXml("Center", new INodeHandler() {
@Override
                        public void handle(INode n) {
if (id.equals(ACTION_CENTER_VERTICAL)) {
for (INode child : children) {
centerVertically(child);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 2114f39..1296c76 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -37,7 +39,8 @@
public class ScrollViewRule extends FrameLayoutRule {

@Override
    public void onChildInserted(INode child, INode parent, InsertType insertType) {
super.onChildInserted(child, parent, insertType);

// The child of the ScrollView should fill in both directions
//Synthetic comment -- @@ -47,7 +50,8 @@
}

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {
//Synthetic comment -- @@ -59,8 +63,8 @@
}

@Override
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
DropFeedback f = super.onDropMove(targetNode, elements, feedback, p);

// ScrollViews only allow a single child








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java
//Synthetic comment -- index c65dec9..7c26859 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -29,7 +30,8 @@
public class SeekBarRule extends BaseViewRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

// A SeekBar isn't useful with wrap_content because it packs itself down to








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
//Synthetic comment -- index 73a5031..12ab448 100644

//Synthetic comment -- @@ -15,14 +15,15 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HANDLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -34,7 +35,8 @@
public class SlidingDrawerRule extends BaseLayoutRule {

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java
//Synthetic comment -- index 099a760..6724392 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
//Synthetic comment -- @@ -26,7 +25,9 @@
import static com.android.ide.common.layout.LayoutConstants.FQCN_TAB_WIDGET;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -40,7 +41,8 @@
// the child elements yourself, e.g. via addTab() etc.

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java
//Synthetic comment -- index ceb562d..b2cb1e4 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_ROW;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IMenuCallback;
//Synthetic comment -- @@ -60,7 +62,8 @@
}

@Override
    public void onChildInserted(INode child, INode parent, InsertType insertType) {
// Overridden to inhibit the setting of layout_width/layout_height since
// it should always be match_parent
}
//Synthetic comment -- @@ -69,13 +72,17 @@
* Add an explicit "Add Row" action to the context menu
*/
@Override
    public void addContextMenuActions(List<RuleAction> actions, final INode selectedNode) {
super.addContextMenuActions(actions, selectedNode);

IMenuCallback addTab = new IMenuCallback() {
@Override
            public void action(RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, Boolean newValue) {
final INode node = selectedNode;
INode newRow = node.appendChild(FQCN_TABLE_ROW);
mRulesEngine.select(Collections.singletonList(newRow));
//Synthetic comment -- @@ -85,8 +92,10 @@
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);
addTableLayoutActions(mRulesEngine, actions, parentNode, children);
}
//Synthetic comment -- @@ -99,11 +108,14 @@
final List<? extends INode> children) {
IMenuCallback actionCallback = new IMenuCallback() {
@Override
            public void action(final RuleAction action, List<? extends INode> selectedNodes,
                    final String valueId, final Boolean newValue) {
parentNode.editXml("Add/Remove Table Row", new INodeHandler() {
@Override
                    public void handle(INode n) {
if (action.getId().equals(ACTION_ADD_ROW)) {
// Determine the index of the selection, if any; if there is
// a selection, insert the row before the current row, otherwise
//Synthetic comment -- @@ -171,7 +183,8 @@
}

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {
//Synthetic comment -- @@ -183,8 +196,9 @@
}

@Override
    public DropFeedback onResizeBegin(INode child, INode parent, SegmentType horizontalEdge,
            SegmentType verticalEdge, Object childView, Object parentView) {
// Children of a table layout cannot set their widths (it is controlled by column
// settings on the table). They can set their heights (though for TableRow, the
// height is always wrap_content).








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java
//Synthetic comment -- index f372866..af6f7a0 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_LAYOUT;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -41,15 +43,18 @@
}

@Override
    public void onChildInserted(INode child, INode parent, InsertType insertType) {
// Overridden to inhibit the setting of layout_width/layout_height since
// the table row will enforce match_parent and wrap_content for width and height
// respectively.
}

@Override
    public void addLayoutActions(List<RuleAction> actions, final INode parentNode,
            final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);

// Also apply table-specific actions on the table row such that you can
//Synthetic comment -- @@ -65,8 +70,9 @@
}

@Override
    public DropFeedback onResizeBegin(INode child, INode parent, SegmentType horizontalEdge,
            SegmentType verticalEdge, Object childView, Object parentView) {
// No resizing in TableRows; the width is *always* match_parent and the height is
// *always* wrap_content.
return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java
//Synthetic comment -- index 5224df0..b2c8413 100644

//Synthetic comment -- @@ -16,10 +16,11 @@

package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -32,7 +33,8 @@
// into; it's an AbsoluteLayout for implementation purposes.

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java
//Synthetic comment -- index 5714392..3456fb9 100644

//Synthetic comment -- @@ -15,15 +15,17 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;

public class ZoomButtonRule extends BaseViewRule {
@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java
//Synthetic comment -- index 3a73558..461ca2b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -127,7 +128,8 @@

// Implements IFeedbackPainter
@Override
        public void paint(IGraphics gc, INode node, DropFeedback feedback) {
Rect b = node.getBounds();
if (!b.isValid()) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelinePainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelinePainter.java
//Synthetic comment -- index b37a6ed..46038ee 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IFeedbackPainter;
//Synthetic comment -- @@ -45,7 +46,7 @@
public final class GuidelinePainter implements IFeedbackPainter {
// ---- Implements IFeedbackPainter ----
@Override
    public void paint(IGraphics gc, INode node, DropFeedback feedback) {
GuidelineHandler state = (GuidelineHandler) feedback.userData;

for (INode dragged : state.mDraggedNodes) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index 7b3e8a3..05f0adf 100755

//Synthetic comment -- @@ -86,13 +86,13 @@

/** Returns the XML Name of the attribute */
@Override
    public String getName() {
return mName;
}
/** Returns the formats of the attribute. Cannot be null.
*  Should have at least one format. */
@Override
    public EnumSet<Format> getFormats() {
return mFormats;
}
/** Returns the values for enums. null for other types. */
//Synthetic comment -- @@ -107,7 +107,7 @@
}
/** Returns a short javadoc, .i.e. the first sentence. */
@Override
    public String getJavaDoc() {
return mJavaDoc;
}
/** Returns the documentation for deprecated attributes. Null if not deprecated. */
//Synthetic comment -- @@ -157,7 +157,7 @@
*         this attribute
*/
@Override
    public String getDefinedBy() {
return mDefinedBy;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java
//Synthetic comment -- index 7345a04..48e01df 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -172,7 +173,7 @@
Wait.WAIT_FOR_READERS,
new IProcessOutput() {
@Override
                            public void out(String line) {
if (line != null) {
try {
writer.write(line);
//Synthetic comment -- @@ -182,7 +183,7 @@
}

@Override
                            public void err(String line) {
if (line != null) {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
project, line);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 0ff50b5..ce9030e 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -98,12 +99,12 @@
Wait.ASYNC,
new IProcessOutput() {
@Override
                        public void out(String line) {
// Ignore stdout
}

@Override
                        public void err(String line) {
if (line != null) {
logger.printf("[SDK Manager] %s", line);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 0f556f9..799cf0e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
//Synthetic comment -- @@ -1065,7 +1066,7 @@

@SuppressWarnings("unused")
@Override
                    public void out(String line) {
if (line != null) {
// If benchmarking always print the lines that
// correspond to benchmarking info returned by ADT
//Synthetic comment -- @@ -1080,7 +1081,7 @@
}

@Override
                    public void err(String line) {
if (line != null) {
results.add(line);
if (BuildVerbosity.VERBOSE == AdtPrefs.getPrefs().getBuildVerbosity()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimationEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimationEditorDelegate.java
//Synthetic comment -- index 153bc79..7c7051d 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.eclipse.adt.AdtConstants.EDITORS_NAMESPACE;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
//Synthetic comment -- @@ -41,8 +43,8 @@
@Override
@SuppressWarnings("unchecked")
public AnimationEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.ANIM == type || ResourceFolderType.ANIMATOR == type) {
return new AnimationEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/color/ColorEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/color/ColorEditorDelegate.java
//Synthetic comment -- index 53edea9..3389683 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.eclipse.adt.AdtConstants.EDITORS_NAMESPACE;

import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -39,8 +41,8 @@
@Override
@SuppressWarnings("unchecked")
public ColorEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.COLOR == type) {
return new ColorEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/drawable/DrawableEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/drawable/DrawableEditorDelegate.java
//Synthetic comment -- index 69d82bd..a54fa8c 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.eclipse.adt.AdtConstants.EDITORS_NAMESPACE;

import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
//Synthetic comment -- @@ -40,8 +42,8 @@
@Override
@SuppressWarnings("unchecked")
public DrawableEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.DRAWABLE == type) {
return new DrawableEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java
//Synthetic comment -- index 23a5d35..41795d2 100644

//Synthetic comment -- @@ -270,7 +270,7 @@
replaceEnd = document.getLength();
} else {
root = DomUtilities.getCommonAncestor(startNode, endNode);
            initialDepth = DomUtilities.getDepth(root) - 1;

// Regions must be non-null since the DOM nodes are non null, but Eclipse null
// analysis doesn't realize it:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 89dd263..8d7b02e 100644

//Synthetic comment -- @@ -100,8 +100,8 @@
@Override
@SuppressWarnings("unchecked")
public LayoutEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.LAYOUT == type) {
return new LayoutEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java
//Synthetic comment -- index 664d473..354517e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.IColor;
import com.android.ide.common.api.IGraphics;
//Synthetic comment -- @@ -135,7 +136,7 @@
//-------------

@Override
    public IColor registerColor(int rgb) {
checkGC();

Integer key = Integer.valueOf(rgb);
//Synthetic comment -- @@ -163,13 +164,13 @@
}

@Override
    public IColor getForeground() {
Color c = getGc().getForeground();
return new ColorWrapper(c);
}

@Override
    public IColor getBackground() {
Color c = getGc().getBackground();
return new ColorWrapper(c);
}
//Synthetic comment -- @@ -180,13 +181,13 @@
}

@Override
    public void setForeground(IColor color) {
checkGC();
getGc().setForeground(((ColorWrapper) color).getColor());
}

@Override
    public void setBackground(IColor color) {
checkGC();
getGc().setBackground(((ColorWrapper) color).getColor());
}
//Synthetic comment -- @@ -203,7 +204,7 @@
}

@Override
    public void setLineStyle(LineStyle style) {
int swtStyle = 0;
switch (style) {
case LINE_SOLID:
//Synthetic comment -- @@ -254,7 +255,7 @@
}

@Override
    public void drawLine(Point p1, Point p2) {
drawLine(p1.x, p1.y, p2.x, p2.y);
}

//Synthetic comment -- @@ -272,12 +273,12 @@
}

@Override
    public void drawRect(Point p1, Point p2) {
drawRect(p1.x, p1.y, p2.x, p2.y);
}

@Override
    public void drawRect(Rect r) {
checkGC();
useStrokeAlpha();
int x = mHScale.translate(r.x);
//Synthetic comment -- @@ -299,12 +300,12 @@
}

@Override
    public void fillRect(Point p1, Point p2) {
fillRect(p1.x, p1.y, p2.x, p2.y);
}

@Override
    public void fillRect(Rect r) {
checkGC();
useFillAlpha();
int x = mHScale.translate(r.x);
//Synthetic comment -- @@ -368,7 +369,7 @@
// strings

@Override
    public void drawString(String string, int x, int y) {
checkGC();
useStrokeAlpha();
x = mHScale.translate(x);
//Synthetic comment -- @@ -382,7 +383,7 @@
}

@Override
    public void drawBoxedStrings(int x, int y, List<?> strings) {
checkGC();

x = mHScale.translate(x);
//Synthetic comment -- @@ -414,14 +415,14 @@
}

@Override
    public void drawString(String string, Point topLeft) {
drawString(string, topLeft.x, topLeft.y);
}

// Styles

@Override
    public void useStyle(DrawingStyle style) {
checkGC();

// Look up the specific SWT style which defines the actual








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java
//Synthetic comment -- index b4a4772..198c164 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode.IAttribute;

import java.util.regex.Matcher;
//Synthetic comment -- @@ -58,19 +59,19 @@
* Can be empty for an attribute without a namespace but is never null.
*/
@Override
    public String getUri() {
return mUri;
}

/** Returns the XML local name of the attribute. Cannot be null nor empty. */
@Override
    public String getName() {
return mName;
}

/** Returns the value of the attribute. Cannot be null. Can be empty. */
@Override
    public String getValue() {
return mValue;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleElement.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleElement.java
//Synthetic comment -- index e9abb06..4feff25 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.Rect;

//Synthetic comment -- @@ -69,7 +71,7 @@
* a View to inflate.
*/
@Override
    public String getFqcn() {
return mFqcn;
}

//Synthetic comment -- @@ -79,7 +81,7 @@
* from the object palette (unless it successfully rendered a preview)
*/
@Override
    public Rect getBounds() {
return mBounds;
}

//Synthetic comment -- @@ -98,12 +100,12 @@
* is no suitable parent. This is null when {@link #getParentFqcn()} is null.
*/
@Override
    public Rect getParentBounds() {
return mParentBounds;
}

@Override
    public IDragAttribute[] getAttributes() {
if (mCachedAttributes == null) {
mCachedAttributes = mAttributes.toArray(new IDragAttribute[mAttributes.size()]);
}
//Synthetic comment -- @@ -111,7 +113,7 @@
}

@Override
    public IDragAttribute getAttribute(String uri, String localName) {
for (IDragAttribute attr : mAttributes) {
if (attr.getUri().equals(uri) && attr.getName().equals(localName)) {
return attr;
//Synthetic comment -- @@ -122,7 +124,7 @@
}

@Override
    public IDragElement[] getInnerElements() {
if (mCachedElements == null) {
mCachedElements = mElements.toArray(new IDragElement[mElements.size()]);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index e08bfc1..e793983 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IValidator;
//Synthetic comment -- @@ -110,12 +112,12 @@
}

@Override
    public String getFqcn() {
return mFqcn;
}

@Override
    public void debugPrintf(String msg, Object... params) {
AdtPlugin.printToConsole(
mFqcn == null ? "<unknown>" : mFqcn,
String.format(msg, params)
//Synthetic comment -- @@ -123,12 +125,12 @@
}

@Override
    public IViewRule loadRule(String fqcn) {
return mRulesEngine.loadRule(fqcn, fqcn);
}

@Override
    public void displayAlert(String message) {
MessageDialog.openInformation(
AdtPlugin.getDisplay().getActiveShell(),
mFqcn,  // title
//Synthetic comment -- @@ -136,7 +138,8 @@
}

@Override
    public String displayInput(String message, String value, final IValidator filter) {
IInputValidator validator = null;
if (filter != null) {
validator = new IInputValidator() {
//Synthetic comment -- @@ -166,26 +169,26 @@
}

@Override
    public IViewMetadata getMetadata(final String fqcn) {
return new IViewMetadata() {
@Override
            public String getDisplayName() {
// This also works when there is no "."
return fqcn.substring(fqcn.lastIndexOf('.') + 1);
}

@Override
            public FillPreference getFillPreference() {
return ViewMetadataRepository.get().getFillPreference(fqcn);
}

@Override
            public Margins getInsets() {
return mRulesEngine.getEditor().getCanvasControl().getInsets(fqcn);
}

@Override
            public List<String> getTopAttributes() {
return ViewMetadataRepository.get().getTopAttributes(fqcn);
}
};
//Synthetic comment -- @@ -205,10 +208,9 @@
}

@Override
    public IValidator getResourceValidator() {
        // When https://review.source.android.com/#change,20168 is integrated,
        // change this to
        //return ResourceNameValidator.create(false, mDelegate.getProject(), ResourceType.ID);
return null;
}

//Synthetic comment -- @@ -242,7 +244,8 @@
}

@Override
    public String displayResourceInput(String resourceTypeName, String currentValue) {
return displayResourceInput(resourceTypeName, currentValue, null);
}

//Synthetic comment -- @@ -254,8 +257,8 @@
}

@Override
    public String[] displayMarginInput(String all, String left, String right, String top,
            String bottom) {
GraphicalEditorPart editor = mRulesEngine.getEditor();
IProject project = editor.getProject();
if (project != null) {
//Synthetic comment -- @@ -282,7 +285,7 @@
}

@Override
    public void select(final Collection<INode> nodes) {
LayoutCanvas layoutCanvas = mRulesEngine.getEditor().getCanvasControl();
final SelectionManager selectionManager = layoutCanvas.getSelectionManager();
selectionManager.select(nodes);
//Synthetic comment -- @@ -440,8 +443,8 @@
}

@Override
    public Map<INode, Rect> measureChildren(INode parent,
            IClientRulesEngine.AttributeFilter filter) {
RenderService renderService = RenderService.create(mRulesEngine.getEditor());
Map<INode, Rect> map = renderService.measureChildren(parent, filter);
if (map == null) {
//Synthetic comment -- @@ -502,7 +505,7 @@
}

@Override
    public String getUniqueId(String fqcn) {
UiDocumentNode root = mRulesEngine.getEditor().getModel();
String prefix = fqcn.substring(fqcn.lastIndexOf('.') + 1);
prefix = Character.toLowerCase(prefix.charAt(0)) + prefix.substring(1);
//Synthetic comment -- @@ -510,7 +513,7 @@
}

@Override
    public String getAppNameSpace() {
IProject project = mRulesEngine.getEditor().getProject();

ProjectState projectState = Sdk.getProjectState(project);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index ea464c1..3cd9729 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -86,16 +88,19 @@
}

@Override
    public Rect getBounds() {
return mBounds;
}

@Override
    public Margins getMargins() {
ViewHierarchy viewHierarchy = mFactory.getCanvas().getViewHierarchy();
CanvasViewInfo view = viewHierarchy.findViewInfoFor(this);
if (view != null) {
            return view.getMargins();
}

return NO_MARGINS;
//Synthetic comment -- @@ -133,14 +138,15 @@
}

@Override
    public String getFqcn() {
if (mNode != null) {
ElementDescriptor desc = mNode.getDescriptor();
if (desc instanceof ViewElementDescriptor) {
return ((ViewElementDescriptor) desc).getFullClassName();
}
}
        return null;
}


//Synthetic comment -- @@ -189,7 +195,7 @@
}

@Override
    public INode[] getChildren() {
if (mNode != null) {
List<UiElementNode> uiChildren = mNode.getUiChildren();
List<INode> nodes = new ArrayList<INode>(uiChildren.size());
//Synthetic comment -- @@ -209,7 +215,7 @@
// ---- XML Editing ---

@Override
    public void editXml(String undoName, final INodeHandler c) {
final AndroidXmlEditor editor = mNode.getEditor();

if (editor != null) {
//Synthetic comment -- @@ -238,17 +244,17 @@
}

@Override
    public INode appendChild(String viewFqcn) {
return insertOrAppend(viewFqcn, -1);
}

@Override
    public INode insertChildAt(String viewFqcn, int index) {
return insertOrAppend(viewFqcn, index);
}

@Override
    public void removeChild(INode node) {
checkEditOK();

((NodeProxy) node).mNode.deleteXmlNode();
//Synthetic comment -- @@ -320,7 +326,10 @@
}

@Override
    public boolean setAttribute(String uri, String name, String value) {
checkEditOK();
UiAttributeNode attr = mNode.setAttributeValue(name, uri, value, true /* override */);

//Synthetic comment -- @@ -345,7 +354,7 @@
}

@Override
    public String getStringAttr(String uri, String attrName) {
UiElementNode uiNode = mNode;

if (attrName == null) {
//Synthetic comment -- @@ -378,7 +387,7 @@
}

@Override
    public IAttributeInfo getAttributeInfo(String uri, String attrName) {
UiElementNode uiNode = mNode;

if (attrName == null) {
//Synthetic comment -- @@ -399,7 +408,7 @@
}

@Override
    public IAttributeInfo[] getDeclaredAttributes() {

AttributeDescriptor[] descs = mNode.getAttributeDescriptors();
int n = descs.length;
//Synthetic comment -- @@ -413,7 +422,7 @@
}

@Override
    public List<String> getAttributeSources() {
ElementDescriptor descriptor = mNode.getDescriptor();
if (descriptor instanceof ViewElementDescriptor) {
return ((ViewElementDescriptor) descriptor).getAttributeSources();
//Synthetic comment -- @@ -423,7 +432,7 @@
}

@Override
    public IAttribute[] getLiveAttributes() {
UiElementNode uiNode = mNode;

if (uiNode.getXmlNode() != null) {
//Synthetic comment -- @@ -446,7 +455,8 @@
}
}
}
        return null;

}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index 628cda6..a7a863c 100644

//Synthetic comment -- @@ -309,21 +309,26 @@
ResourceType type = null;
List<ResourceType> types = null;
if (formats.contains(Format.FLAG)) {
                FlagXmlPropertyDialog dialog =
new FlagXmlPropertyDialog(propertyTable.getShell(),
"Select Flag Values", false /* radio */,
                                attributeInfo.getFlagValues(), xmlProperty);

                dialog.open();
                return;

} else if (formats.contains(Format.ENUM)) {
                FlagXmlPropertyDialog dialog =
new FlagXmlPropertyDialog(propertyTable.getShell(),
"Select Enum Value", true /* radio */,
                                attributeInfo.getEnumValues(), xmlProperty);
                dialog.open();
                return;
} else {
for (Format format : formats) {
ResourceType t = format.getResourceType();
//Synthetic comment -- @@ -379,16 +384,18 @@
} else if (type != null) {
// Single resource type: use a resource chooser
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
                String currentValue = (String) property.getValue();
                // TODO: Add validator factory?
                String resource = ResourceChooser.chooseResource(graphicalEditor,
                        type, currentValue, null /* validator */);
                // Returns null for cancel, "" for clear and otherwise a new value
                if (resource != null) {
                    if (resource.length() > 0) {
                        property.setValue(resource);
                    } else {
                        property.setValue(null);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java
//Synthetic comment -- index abbfa11..99369ee 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.util.XmlUtils.ANDROID_NS_NAME_PREFIX;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -218,7 +219,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
String name = getViewClass(mTypeFqcn);

IFile file = mDelegate.getEditor().getInputFile();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoring.java
//Synthetic comment -- index 967a880..9cf3a3f 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
//Synthetic comment -- @@ -150,7 +151,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
String name = getViewClass(mTypeFqcn);

IFile file = mDelegate.getEditor().getInputFile();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java
//Synthetic comment -- index eb89304..657c9ec 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import static com.android.util.XmlUtils.XMLNS_COLON;

import com.android.AndroidConstants;
import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -221,7 +222,7 @@
// ---- Actual implementation of Extract as Include modification computation ----

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
String extractedText = getExtractedText();

String namespaceDeclarations = computeNamespaceDeclarations();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java
//Synthetic comment -- index c6e965d..1c7dd72 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import static com.android.util.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.util.XmlUtils.XMLNS_COLON;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceResolver;
//Synthetic comment -- @@ -322,7 +323,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
List<Change> changes = new ArrayList<Change>();
if (mChosenAttributes.size() == 0) {
return changes;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UnwrapRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UnwrapRefactoring.java
//Synthetic comment -- index 050a787..e333629 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
//Synthetic comment -- @@ -166,7 +167,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
// (1) If the removed parent is the root container, transfer its
//   namespace declarations
// (2) Remove the root element completely








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UseCompoundDrawableRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UseCompoundDrawableRefactoring.java
//Synthetic comment -- index 8f9beab..453daa8 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
//Synthetic comment -- @@ -189,7 +190,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
String androidNsPrefix = getAndroidNamespacePrefix();
IFile file = mDelegate.getEditor().getInputFile();
List<Change> changes = new ArrayList<Change>();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java
//Synthetic comment -- index d043085..08a951b 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.util.XmlUtils.ANDROID_NS_NAME_PREFIX;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
//Synthetic comment -- @@ -175,7 +176,7 @@
}

@Override
    protected List<Change> computeChanges(IProgressMonitor monitor) {
// (1) Insert the new container in front of the beginning of the
//      first wrapped view
// (2) If the container is the new root, transfer namespace declarations








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 2f10f68..8479b0d 100644

//Synthetic comment -- @@ -78,6 +78,7 @@
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
//Synthetic comment -- @@ -488,7 +489,7 @@
try {
IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
if (javaProject == null) {
                return null;
}
// TODO - look around a bit more and see if we can figure out whether the
// call if from within a setContentView call!








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditorDelegate.java
//Synthetic comment -- index faca295..b5056d9 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.menu;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
//Synthetic comment -- @@ -43,8 +45,8 @@
@Override
@SuppressWarnings("unchecked")
public MenuEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.MENU == type) {
return new MenuEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/otherxml/OtherXmlEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/otherxml/OtherXmlEditorDelegate.java
//Synthetic comment -- index 138ff95..7d74516 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.otherxml;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
//Synthetic comment -- @@ -36,8 +38,8 @@
@Override
@SuppressWarnings("unchecked")
public OtherXmlEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.XML == type) {
return new OtherXmlEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/ValuesEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/ValuesEditorDelegate.java
//Synthetic comment -- index f253b30..94a6771 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.values;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
//Synthetic comment -- @@ -43,8 +45,8 @@
@Override
@SuppressWarnings("unchecked")
public ValuesEditorDelegate createForFile(
                CommonXmlEditor delegator,
                ResourceFolderType type) {
if (ResourceFolderType.VALUES == type) {
return new ValuesEditorDelegate(delegator);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index b5810a4..e7c81c5 100644

//Synthetic comment -- @@ -188,7 +188,8 @@
// ----- Extends LintClient -----

@Override
    public void log(Severity severity, Throwable exception, String format, Object... args) {
if (exception == null) {
AdtPlugin.log(IStatus.WARNING, format, args);
} else {
//Synthetic comment -- @@ -213,7 +214,7 @@
// ----- Implements IDomParser -----

@Override
    public Document parseXml(XmlContext context) {
// Map File to IFile
IFile file = AdtUtils.fileToIFile(context.file);
if (file == null || !file.exists()) {
//Synthetic comment -- @@ -277,7 +278,19 @@

@NonNull
@Override
    public Configuration getConfiguration(Project project) {
if (project != null) {
IProject eclipseProject = getProject(project);
if (eclipseProject != null) {
//Synthetic comment -- @@ -287,10 +300,10 @@

return GlobalLintConfiguration.get();
}

@Override
    public void report(Context context, Issue issue, Severity s, Location location,
            String message, Object data) {
int severity = getMarkerSeverity(s);
IMarker marker = null;
if (location != null) {
//Synthetic comment -- @@ -399,7 +412,8 @@
}
}

        LayoutEditorDelegate delegate = LayoutEditorDelegate.fromEditor(AdtUtils.getActiveEditor());
if (delegate != null) {
delegate.getGraphicalEditor().getLayoutActionBar().updateErrorIndicator();
}
//Synthetic comment -- @@ -645,7 +659,7 @@
}

@Override
    public String readFile(File f) {
// Map File to IFile
IFile file = AdtUtils.fileToIFile(f);
if (file == null || !file.exists()) {
//Synthetic comment -- @@ -684,13 +698,14 @@
}

@Override
    public Location getLocation(XmlContext context, Node node) {
IStructuredModel model = (IStructuredModel) context.getProperty(MODEL_PROPERTY);
return new LazyLocation(context.file, model.getStructuredDocument(), (IndexedRegion) node);
}

@Override
    public Handle createLocationHandle(final XmlContext context, final Node node) {
IStructuredModel model = (IStructuredModel) context.getProperty(MODEL_PROPERTY);
return new LazyLocation(context.file, model.getStructuredDocument(), (IndexedRegion) node);
}
//Synthetic comment -- @@ -810,12 +825,13 @@
}

@Override
    public Class<? extends Detector> replaceDetector(Class<? extends Detector> detectorClass) {
return detectorClass;
}

@Override
    public void dispose(XmlContext context, Document document) {
IStructuredModel model = (IStructuredModel) context.getProperty(MODEL_PROPERTY);
assert model != null : context.file;
if (model != null) {
//Synthetic comment -- @@ -885,7 +901,7 @@
}

@Override
        public Location resolve() {
return this;
}
}
//Synthetic comment -- @@ -915,7 +931,7 @@
}

@Override
        public lombok.ast.Node parseJava(JavaContext context) {
if (USE_ECLIPSE_PARSER) {
// Use Eclipse's compiler
EcjTreeConverter converter = new EcjTreeConverter();
//Synthetic comment -- @@ -1010,19 +1026,22 @@
}

@Override
        public Location getLocation(JavaContext context, lombok.ast.Node node) {
lombok.ast.Position position = node.getPosition();
return Location.create(context.file, context.getContents(),
position.getStart(), position.getEnd());
}

@Override
        public Handle createLocationHandle(JavaContext context, lombok.ast.Node node) {
return new LocationHandle(context.file, node);
}

@Override
        public void dispose(JavaContext context, lombok.ast.Node compilationUnit) {
}

/* Handle for creating positions cheaply and returning full fledged locations later */
//Synthetic comment -- @@ -1037,7 +1056,7 @@
}

@Override
            public Location resolve() {
lombok.ast.Position pos = mNode.getPosition();
return Location.create(mFile, null /*contents*/, pos.getStart(), pos.getEnd());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/GlobalLintConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/GlobalLintConfiguration.java
//Synthetic comment -- index 646d752..5870501 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ide.eclipse.adt.internal.lint;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.tools.lint.client.api.Configuration;
//Synthetic comment -- @@ -54,7 +55,7 @@
}

@Override
    public Severity getSeverity(Issue issue) {
if (mSeverities == null) {
IssueRegistry registry = EclipseLintClient.getRegistry();
mSeverities = new HashMap<Issue, Severity>();
//Synthetic comment -- @@ -94,14 +95,15 @@
}

@Override
    public void ignore(Context context, Issue issue, Location location, String message,
            Object data) {
throw new UnsupportedOperationException(
"Can't ignore() in global configurations"); //$NON-NLS-1$
}

@Override
    public void setSeverity(Issue issue, Severity severity) {
if (mSeverities == null) {
// Force initialization
getSeverity(issue);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintColumn.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintColumn.java
//Synthetic comment -- index 34af83b..297d94b 100644

//Synthetic comment -- @@ -146,7 +146,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Description";
}

//Synthetic comment -- @@ -156,12 +156,12 @@
}

@Override
        public String getValue(IMarker marker) {
return getStyledValue(marker).toString();
}

@Override
        public StyledString getStyledValue(IMarker marker) {
StyledString styledString = new StyledString();

String message = marker.getAttribute(IMarker.MESSAGE, "");
//Synthetic comment -- @@ -177,7 +177,7 @@
}

@Override
        public Image getImage(IMarker marker) {
int severity = marker.getAttribute(IMarker.SEVERITY, 0);
ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
switch (severity) {
//Synthetic comment -- @@ -199,7 +199,7 @@
}

@Override
        public Font getFont(IMarker marker) {
int severity = marker.getAttribute(IMarker.SEVERITY, 0);
if (severity == IMarker.SEVERITY_ERROR) {
return JFaceResources.getFontRegistry().getBold(
//Synthetic comment -- @@ -277,7 +277,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Category";
}

//Synthetic comment -- @@ -287,7 +287,7 @@
}

@Override
        public String getValue(IMarker marker) {
Issue issue = mList.getIssue(marker);
if (issue != null) {
return issue.getCategory().getFullName();
//Synthetic comment -- @@ -303,7 +303,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Location";
}

//Synthetic comment -- @@ -313,12 +313,12 @@
}

@Override
        public String getValue(IMarker marker) {
return getStyledValue(marker).toString();
}

@Override
        public StyledString getStyledValue(IMarker marker) {
StyledString styledString = new StyledString();

// Combined location
//Synthetic comment -- @@ -378,7 +378,7 @@
}

@Override
        public String getColumnHeaderText() {
return "File";
}

//Synthetic comment -- @@ -393,7 +393,7 @@
}

@Override
        public String getValue(IMarker marker) {
if (marker.getResource() instanceof IFile) {
return marker.getResource().getName();
} else {
//Synthetic comment -- @@ -408,7 +408,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Path";
}

//Synthetic comment -- @@ -423,7 +423,7 @@
}

@Override
        public String getValue(IMarker marker) {
return marker.getResource().getFullPath().toOSString();
}
}
//Synthetic comment -- @@ -434,7 +434,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Line";
}

//Synthetic comment -- @@ -454,7 +454,7 @@
}

@Override
        public String getValue(IMarker marker) {
int line = getLine(marker);
if (line >= 1) {
return Integer.toString(line);
//Synthetic comment -- @@ -483,7 +483,7 @@
}

@Override
        public String getColumnHeaderText() {
return "Priority";
}

//Synthetic comment -- @@ -503,7 +503,7 @@
}

@Override
        public String getValue(IMarker marker) {
int priority = getPriority(marker);
if (priority > 0) {
return Integer.toString(priority);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index c81c0fc..32da203 100644

//Synthetic comment -- @@ -226,7 +226,7 @@
File dir = AdtUtils.getAbsolutePath(eclipseProject).toFile();
project = mClient.getProject(dir, dir);
}
            Configuration configuration = mClient.getConfiguration(project);
if (thisFileOnly && configuration instanceof DefaultConfiguration) {
File file = AdtUtils.getAbsolutePath(resource).toFile();
((DefaultConfiguration) configuration).ignore(issue, file);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ProjectLintConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ProjectLintConfiguration.java
//Synthetic comment -- index 77cd115..9e4ca12 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
//Synthetic comment -- @@ -79,7 +80,7 @@
}

@Override
    public Severity getSeverity(Issue issue) {
Severity severity = super.getSeverity(issue);
if (mFatalOnly && severity != Severity.FATAL) {
return Severity.IGNORE;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java
//Synthetic comment -- index b2d7361..0bc2bfd 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
File dir = AdtUtils.getAbsolutePath(mProject).toFile();
project = mClient.getProject(dir, dir);
}
        mConfiguration = mClient.getConfiguration(project);

mSearch = new Text(container, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
mSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java
//Synthetic comment -- index 8fcf902..d7ace9a 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ExportHelper;
//Synthetic comment -- @@ -547,7 +548,7 @@
Wait.WAIT_FOR_READERS,
new IProcessOutput() {
@Override
                        public void out(String line) {
if (line != null) {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
project, line);
//Synthetic comment -- @@ -555,7 +556,7 @@
}

@Override
                        public void err(String line) {
if (line != null) {
output.add(line);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index dc0c898..4f107fb 100644

//Synthetic comment -- @@ -532,6 +532,9 @@
xml = out.toString();
} else {
xml = readTemplateTextResource(from);
}

String currentXml = Files.toString(to, Charsets.UTF_8);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/lint/ProjectLintConfigurationTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/lint/ProjectLintConfigurationTest.java
//Synthetic comment -- index 202d768..9f0783f 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.tools.lint.checks.DuplicateIdDetector;
import com.android.tools.lint.checks.UnusedResourceDetector;
//Synthetic comment -- @@ -208,12 +210,14 @@

private static class TestClient extends LintClient {
@Override
        public void report(Context context, Issue issue, Severity severity, Location location,
                String message, Object data) {
}

@Override
        public void log(Severity severity, Throwable exception, String format, Object... args) {
}

@Override
//Synthetic comment -- @@ -222,7 +226,7 @@
}

@Override
        public String readFile(File file) {
return null;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index e382132..312df7d 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -198,47 +200,48 @@
}

@Override
        public void debugPrintf(String msg, Object... params) {
fail("Not supported in tests yet");
}

@Override
        public void displayAlert(String message) {
fail("Not supported in tests yet");
}

@Override
        public String displayInput(String message, String value, IValidator filter) {
fail("Not supported in tests yet");
return null;
}

@Override
        public String getFqcn() {
return mFqn;
}

@Override
        public IViewMetadata getMetadata(final String fqcn) {
return new IViewMetadata() {
@Override
                public String getDisplayName() {
// This also works when there is no "."
return fqcn.substring(fqcn.lastIndexOf('.') + 1);
}

@Override
                public FillPreference getFillPreference() {
return ViewMetadataRepository.get().getFillPreference(fqcn);
}

@Override
                public Margins getInsets() {
return null;
}

@Override
                public List<String> getTopAttributes() {
return ViewMetadataRepository.get().getTopAttributes(fqcn);
}
};
//Synthetic comment -- @@ -250,7 +253,7 @@
}

@Override
        public IViewRule loadRule(String fqcn) {
fail("Not supported in tests yet");
return null;
}
//Synthetic comment -- @@ -262,20 +265,21 @@
}

@Override
        public IValidator getResourceValidator() {
fail("Not supported in tests yet");
return null;
}

@Override
        public String displayResourceInput(String resourceTypeName, String currentValue) {
fail("Not supported in tests yet");
return null;
}

@Override
        public String[] displayMarginInput(String all, String left, String right, String top,
                String bottom) {
fail("Not supported in tests yet");
return null;
}
//Synthetic comment -- @@ -287,7 +291,7 @@
}

@Override
        public void select(Collection<INode> nodes) {
fail("Not supported in tests yet");
}

//Synthetic comment -- @@ -308,7 +312,8 @@
}

@Override
        public Map<INode, Rect> measureChildren(INode parent, AttributeFilter filter) {
return null;
}

//Synthetic comment -- @@ -319,7 +324,7 @@
}

@Override
        public String getUniqueId(String prefix) {
fail("Not supported in tests yet");
return null;
}
//Synthetic comment -- @@ -337,7 +342,7 @@
}

@Override
        public String getAppNameSpace() {
fail("Not supported in tests yet");
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java
//Synthetic comment -- index a1a2af7..76840b1 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -35,17 +36,17 @@
}

@Override
    public String getName() {
return mName;
}

@Override
    public String getUri() {
return mUri;
}

@Override
    public String getValue() {
return mValue;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java
//Synthetic comment -- index f4f83c1..69984bd 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout;

import com.android.ide.common.api.IAttributeInfo;

import java.util.EnumSet;
//Synthetic comment -- @@ -59,22 +60,22 @@
}

@Override
    public EnumSet<Format> getFormats() {
return mFormats;
}

@Override
    public String getJavaDoc() {
return mJavadoc;
}

@Override
    public String getName() {
return mName;
}

@Override
    public String getDefinedBy() {
return mDefinedBy;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
//Synthetic comment -- index 4a3b4e8..eb0a432 100644

//Synthetic comment -- @@ -18,6 +18,8 @@
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.Rect;

//Synthetic comment -- @@ -100,7 +102,7 @@
// ==== IDragElement ====

@Override
    public IDragAttribute getAttribute(String uri, String localName) {
if (mAttributes == null) {
return new TestAttribute(uri, localName, "");
}
//Synthetic comment -- @@ -109,22 +111,22 @@
}

@Override
    public IDragAttribute[] getAttributes() {
return mAttributes.values().toArray(new IDragAttribute[mAttributes.size()]);
}

@Override
    public Rect getBounds() {
return mRect;
}

@Override
    public String getFqcn() {
return mFqcn;
}

@Override
    public IDragElement[] getInnerElements() {
if (mChildren == null) {
return new IDragElement[0];
}
//Synthetic comment -- @@ -133,7 +135,7 @@
}

@Override
    public Rect getParentBounds() {
return mParent != null ? mParent.getBounds() : null;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java
//Synthetic comment -- index 04f6259..ec93210 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.IColor;
import com.android.ide.common.api.IGraphics;
//Synthetic comment -- @@ -51,7 +52,7 @@
// ==== IGraphics ====

@Override
    public void drawBoxedStrings(int x, int y, List<?> strings) {
mDrawn.add("drawBoxedStrings(" + x + "," + y + "," + strings + ")");
}

//Synthetic comment -- @@ -61,7 +62,7 @@
}

@Override
    public void drawLine(Point p1, Point p2) {
mDrawn.add("drawLine(" + p1 + "," + p2 + ")");
}

//Synthetic comment -- @@ -71,22 +72,22 @@
}

@Override
    public void drawRect(Point p1, Point p2) {
mDrawn.add("drawRect(" + p1 + "," + p2 + ")");
}

@Override
    public void drawRect(Rect r) {
mDrawn.add("drawRect(" + rectToString(r) + ")");
}

@Override
    public void drawString(String string, int x, int y) {
mDrawn.add("drawString(" + x + "," + y + "," + string + ")");
}

@Override
    public void drawString(String string, Point topLeft) {
mDrawn.add("drawString(" + string + "," + topLeft + ")");
}

//Synthetic comment -- @@ -96,12 +97,12 @@
}

@Override
    public void fillRect(Point p1, Point p2) {
mDrawn.add("fillRect(" + p1 + "," + p2 + ")");
}

@Override
    public void fillRect(Rect r) {
mDrawn.add("fillRect(" + rectToString(r) + ")");
}

//Synthetic comment -- @@ -111,7 +112,7 @@
}

@Override
    public IColor getBackground() {
return mBackground;
}

//Synthetic comment -- @@ -121,12 +122,12 @@
}

@Override
    public IColor getForeground() {
return mForeground;
}

@Override
    public IColor registerColor(int rgb) {
mDrawn.add("registerColor(" + Integer.toHexString(rgb) + ")");
return new TestColor(rgb);
}
//Synthetic comment -- @@ -138,19 +139,19 @@
}

@Override
    public void setBackground(IColor color) {
mDrawn.add("setBackground(" + color + ")");
mBackground = color;
}

@Override
    public void setForeground(IColor color) {
mDrawn.add("setForeground(" + color + ")");
mForeground = color;
}

@Override
    public void setLineStyle(LineStyle style) {
mDrawn.add("setLineStyle(" + style + ")");
}

//Synthetic comment -- @@ -160,7 +161,7 @@
}

@Override
    public void useStyle(DrawingStyle style) {
mDrawn.add("useStyle(" + style + ")");
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index 575c958..8984f38 100644

//Synthetic comment -- @@ -18,6 +18,8 @@
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -94,12 +96,12 @@
// ==== INODE ====

@Override
    public INode appendChild(String viewFqcn) {
return insertChildAt(viewFqcn, mChildren.size());
}

@Override
    public void editXml(String undoName, INodeHandler callback) {
callback.handle(this);
}

//Synthetic comment -- @@ -108,32 +110,32 @@
}

@Override
    public IAttributeInfo getAttributeInfo(String uri, String attrName) {
return mAttributeInfos.get(uri + attrName);
}

@Override
    public Rect getBounds() {
return mBounds;
}

@Override
    public INode[] getChildren() {
return mChildren.toArray(new INode[mChildren.size()]);
}

@Override
    public IAttributeInfo[] getDeclaredAttributes() {
return mAttributeInfos.values().toArray(new IAttributeInfo[mAttributeInfos.size()]);
}

@Override
    public String getFqcn() {
return mFqcn;
}

@Override
    public IAttribute[] getLiveAttributes() {
return mAttributes.values().toArray(new IAttribute[mAttributes.size()]);
}

//Synthetic comment -- @@ -153,7 +155,7 @@
}

@Override
    public String getStringAttr(String uri, String attrName) {
IAttribute attr = mAttributes.get(uri + attrName);
if (attr == null) {
return null;
//Synthetic comment -- @@ -163,7 +165,7 @@
}

@Override
    public INode insertChildAt(String viewFqcn, int index) {
TestNode child = new TestNode(viewFqcn);
if (index == -1) {
mChildren.add(child);
//Synthetic comment -- @@ -175,7 +177,7 @@
}

@Override
    public void removeChild(INode node) {
int index = mChildren.indexOf(node);
if (index != -1) {
removeChild(index);
//Synthetic comment -- @@ -183,7 +185,8 @@
}

@Override
    public boolean setAttribute(String uri, String localName, String value) {
mAttributes.put(uri + localName, new TestAttribute(uri, localName, value));
return true;
}
//Synthetic comment -- @@ -200,12 +203,12 @@
}

@Override
    public Margins getMargins() {
return null;
}

@Override
    public List<String> getAttributeSources() {
return mAttributeSources != null ? mAttributeSources : Collections.<String>emptyList();
}









//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/LintCliXmlParser.java b/lint/cli/src/com/android/tools/lint/LintCliXmlParser.java
//Synthetic comment -- index e777e99..c13c41d 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint;

import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.IDomParser;
import com.android.tools.lint.client.api.IssueRegistry;
//Synthetic comment -- @@ -40,7 +41,7 @@
*/
public class LintCliXmlParser extends PositionXmlParser implements IDomParser {
@Override
    public Document parseXml(XmlContext context) {
try {
// Do we need to provide an input stream for encoding?
String xml = context.getContents();
//Synthetic comment -- @@ -70,22 +71,22 @@
}

@Override
    public Location getLocation(XmlContext context, Node node) {
OffsetPosition pos = (OffsetPosition) getPosition(node);
if (pos != null) {
return Location.create(context.file, pos, (OffsetPosition) pos.getEnd());
}

        return null;
}

@Override
    public Handle createLocationHandle(XmlContext context, Node node) {
return new LocationHandle(context.file, node);
}

@Override
    protected OffsetPosition createPosition(int line, int column, int offset) {
return new OffsetPosition(line, column, offset);
}

//Synthetic comment -- @@ -143,7 +144,7 @@
}

@Override
        public void setEnd(com.android.util.PositionXmlParser.Position end) {
mEnd = end;
}

//Synthetic comment -- @@ -155,7 +156,7 @@
}

@Override
    public void dispose(XmlContext context, Document document) {
}

/* Handle for creating DOM positions cheaply and returning full fledged locations later */
//Synthetic comment -- @@ -170,13 +171,13 @@
}

@Override
        public Location resolve() {
OffsetPosition pos = (OffsetPosition) getPosition(mNode);
if (pos != null) {
return Location.create(mFile, pos, (OffsetPosition) pos.getEnd());
}

            return null;
}

@Override








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/LombokParser.java b/lint/cli/src/com/android/tools/lint/LombokParser.java
//Synthetic comment -- index 15b1073..2c263c6 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint;

import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.IJavaParser;
import com.android.tools.lint.detector.api.JavaContext;
//Synthetic comment -- @@ -39,7 +40,7 @@
public class LombokParser implements IJavaParser {

@Override
    public Node parseJava(JavaContext context) {
try {
Source source = new Source(context.getContents(), context.file.getName());
List<Node> nodes = source.getNodes();
//Synthetic comment -- @@ -95,19 +96,21 @@
}

@Override
    public Location getLocation(JavaContext context, lombok.ast.Node node) {
lombok.ast.Position position = node.getPosition();
return Location.create(context.file, context.getContents(),
position.getStart(), position.getEnd());
}

@Override
    public Handle createLocationHandle(JavaContext context, Node node) {
return new LocationHandle(context.file, node);
}

@Override
    public void dispose(JavaContext context, Node compilationUnit) {
}

/* Handle for creating positions cheaply and returning full fledged locations later */
//Synthetic comment -- @@ -122,7 +125,7 @@
}

@Override
        public Location resolve() {
Position pos = mNode.getPosition();
return Location.create(mFile, null /*contents*/, pos.getStart(), pos.getEnd());
}








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index 6a40cb3..68d68b4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.Nullable;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.client.api.Configuration;
//Synthetic comment -- @@ -886,7 +887,11 @@
}

@Override
    public void log(Severity severity, Throwable exception, String format, Object... args) {
System.out.flush();
if (!mQuiet) {
// Place the error message on a line of its own since we're printing '.' etc
//Synthetic comment -- @@ -907,7 +912,7 @@
}

@Override
    public Configuration getConfiguration(Project project) {
return new CliConfiguration(mDefaultConfiguration, project);
}

//Synthetic comment -- @@ -934,8 +939,13 @@
}

@Override
    public void report(Context context, Issue issue, Severity severity, Location location,
            String message, Object data) {
assert context.isEnabled(issue);

if (severity == Severity.IGNORE) {
//Synthetic comment -- @@ -1041,7 +1051,7 @@
}

@Override
    public String readFile(File file) {
try {
return LintUtils.getEncodedString(file);
} catch (IOException e) {
//Synthetic comment -- @@ -1067,7 +1077,7 @@
}

@Override
        public Severity getSeverity(Issue issue) {
Severity severity = computeSeverity(issue);

if (mAllErrors && severity != Severity.IGNORE) {
//Synthetic comment -- @@ -1082,7 +1092,7 @@
}

@Override
        protected Severity getDefaultSeverity(Issue issue) {
if (mWarnAll) {
return issue.getDefaultSeverity();
}
//Synthetic comment -- @@ -1090,7 +1100,7 @@
return super.getDefaultSeverity(issue);
}

        private Severity computeSeverity(Issue issue) {
Severity severity = super.getSeverity(issue);

String id = issue.getId();
//Synthetic comment -- @@ -1123,25 +1133,32 @@

private class ProgressPrinter implements LintListener {
@Override
        public void update(LintDriver lint, EventType type, Context context) {
switch (type) {
                case SCANNING_PROJECT:
if (lint.getPhase() > 1) {
System.out.print(String.format(
"\nScanning %1$s (Phase %2$d): ",
                                context.getProject().getName(),
lint.getPhase()));
} else {
System.out.print(String.format(
"\nScanning %1$s: ",
                                context.getProject().getName()));
}
break;
                case SCANNING_LIBRARY_PROJECT:
System.out.print(String.format(
"\n         - %1$s: ",
                            context.getProject().getName()));
break;
case SCANNING_FILE:
System.out.print('.');
break;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/DefaultConfiguration.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/DefaultConfiguration.java
//Synthetic comment -- index 6dc4804..665368c 100644

//Synthetic comment -- @@ -202,7 +202,12 @@
message = String.format(message, args);
}
message = "Failed to parse lint.xml configuration file: " + message;
        mClient.report(new Context(null, mProject, mProject, mConfigFile),
IssueRegistry.LINT_ERROR,
mProject.getConfiguration().getSeverity(IssueRegistry.LINT_ERROR),
Location.create(mConfigFile), message, null);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index 74df385..cf45b2e 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
Category.CORRECTNESS,
10,
Severity.ERROR,
            null,
Scope.RESOURCE_FILE_SCOPE);

/**
//Synthetic comment -- @@ -78,7 +78,7 @@
Category.LINT,
10,
Severity.ERROR,
            null,
Scope.RESOURCE_FILE_SCOPE);

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/JavaVisitor.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/JavaVisitor.java
//Synthetic comment -- index 1dbf915..3b641d0 100644

//Synthetic comment -- @@ -1172,7 +1172,7 @@
}

@Override
        public boolean visitMethodInvocation(@NonNull MethodInvocation node) {
if (mVisitMethods) {
String methodName = node.astName().getDescription();
List<VisitingDetector> list = mMethodDetectors.get(methodName);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 27741b1..93e311e 100644

//Synthetic comment -- @@ -232,7 +232,7 @@
*         the create flag was false, or if for some reason the directory
*         could not be created
*/
    @NonNull
public File getCacheDir(boolean create) {
String home = System.getProperty("user.home");
String relative = ".android" + File.separator + "cache"; //$NON-NLS-1$ //$NON-NLS-2$








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index f0bbae6..05c5765 100644

//Synthetic comment -- @@ -538,7 +538,7 @@
continue;
} else {
parent = parent.getParentFile();
                            if (isProjectDir(parent)) {
registerProjectFile(fileToProject, file, parent, parent);
continue;
}
//Synthetic comment -- @@ -718,7 +718,7 @@
// Must provide an issue since API guarantees that the issue parameter
// is valid
Issue.create("Lint", "", "", Category.PERFORMANCE, 0, Severity.INFORMATIONAL, //$NON-NLS-1$
                        null, EnumSet.noneOf(Scope.class)),
Severity.INFORMATIONAL,
null /*range*/,
"Lint canceled by user", null);
//Synthetic comment -- @@ -762,9 +762,9 @@
}
}
if (xmlDetectors.size() > 0) {
                    if (project.getSubset() != null) {
                        checkIndividualResources(project, main, xmlDetectors,
                                project.getSubset());
} else {
File res = new File(project.getDir(), RES_FOLDER);
if (res.exists() && xmlDetectors.size() > 0) {
//Synthetic comment -- @@ -783,9 +783,9 @@
List<Detector> checks = union(mScopeDetectors.get(Scope.JAVA_FILE),
mScopeDetectors.get(Scope.ALL_JAVA_FILES));
if (checks != null && checks.size() > 0) {
                if (project.getSubset() != null) {
                    checkIndividualJavaFiles(project, main, checks,
                            project.getSubset());
} else {
List<File> sourceFolders = project.getJavaSourceFolders();
checkJava(project, main, sourceFolders, checks);
//Synthetic comment -- @@ -931,8 +931,9 @@

/** Check the classes in this project (and if applicable, in any library projects */
private void checkClasses(Project project, Project main) {
        if (project.getSubset() != null) {
            checkIndividualClassFiles(project, main, project.getSubset());
return;
}

//Synthetic comment -- @@ -1119,7 +1120,8 @@
for (ClassEntry entry : entries) {
try {
ClassReader reader = new ClassReader(entry.bytes);
                int flags = ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
reader.accept(visitor, flags);
} catch (Throwable t) {
mClient.log(null, "Error processing %1$s: broken class file?", entry.path());
//Synthetic comment -- @@ -1345,7 +1347,10 @@
return null;
}

            mCurrentVisitor = new XmlVisitor(mClient.getDomParser(), applicableChecks);
}

return mCurrentVisitor;
//Synthetic comment -- @@ -1466,7 +1471,7 @@
}

/** Notifies listeners, if any, that the given event has occurred */
    private void fireEvent(@NonNull LintListener.EventType type, @NonNull Context context) {
if (mListeners != null) {
for (int i = 0, n = mListeners.size(); i < n; i++) {
LintListener listener = mListeners.get(i);
//Synthetic comment -- @@ -1553,7 +1558,7 @@
}

@Override
        public List<File> getJavaLibraries(Project project) {
return mDelegate.getJavaLibraries(project);
}

//Synthetic comment -- @@ -1565,13 +1570,14 @@

@Override
@NonNull
        public Class<? extends Detector> replaceDetector(Class<? extends Detector> detectorClass) {
return mDelegate.replaceDetector(detectorClass);
}

@Override
@NonNull
        public SdkInfo getSdkInfo(Project project) {
return mDelegate.getSdkInfo(project);
}

//Synthetic comment -- @@ -1588,7 +1594,7 @@
}

@Override
        public File findResource(String relativePath) {
return mDelegate.findResource(relativePath);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintListener.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintListener.java
//Synthetic comment -- index 777100d..2247a6d 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Context;
import com.google.common.annotations.Beta;

//Synthetic comment -- @@ -65,5 +66,5 @@
* @param context the context providing additional information
*/
public void update(@NonNull LintDriver driver, @NonNull EventType type,
            @NonNull Context context);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/SdkInfo.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/SdkInfo.java
//Synthetic comment -- index b16747c..5ff7f90 100644

//Synthetic comment -- @@ -42,11 +42,12 @@
if (parentViewFqcn.equals(childViewFqcn)) {
return true;
}
            childViewFqcn = getParentViewClass(childViewFqcn);
            if (childViewFqcn == null) {
// Unknown view - err on the side of caution
return true;
}
}

return false;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Category.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Category.java
//Synthetic comment -- index 6978ef0..ba8e5b5 100644

//Synthetic comment -- @@ -125,7 +125,7 @@
}

@Override
    public int compareTo(@NonNull Category other) {
if (other.mPriority == mPriority) {
if (mParent == other) {
return 1;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 7b3cffd..3c6842e 100644

//Synthetic comment -- @@ -254,7 +254,11 @@
* @param data any associated data, or null
*/
@Override
    public void report(Issue issue, Location location, String message, Object data) {
if (mDriver.isSuppressed(issue, mClassNode)) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index c795606..c8f9fd2 100644

//Synthetic comment -- @@ -372,7 +372,9 @@
* @return the expected speed of this detector
*/
@NonNull
    public abstract Speed getSpeed();

// ---- Dummy implementations to make implementing XmlScanner easier: ----









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index 9f42fb4..0ac8805 100644

//Synthetic comment -- @@ -219,7 +219,7 @@
* @param other the {@link Issue} to compare this issue to
*/
@Override
    public int compareTo(@NonNull Issue other) {
return getId().compareTo(other.getId());
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java
//Synthetic comment -- index d7c1e6e..ae86568 100644

//Synthetic comment -- @@ -74,7 +74,8 @@
}

@Override
    public void report(Issue issue, Location location, String message, Object data) {
if (mDriver.isSuppressed(issue, compilationUnit)) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index 9d61047..167de42 100644

//Synthetic comment -- @@ -110,7 +110,7 @@
*
* @param secondary a secondary location associated with this location
*/
    public void setSecondary(@NonNull Location secondary) {
this.mSecondary = secondary;
}

//Synthetic comment -- @@ -197,7 +197,7 @@
public static Location create(
@NonNull File file,
@NonNull Position start,
            @NonNull Position end) {
return new Location(file, start, end);
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Severity.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Severity.java
//Synthetic comment -- index d0bbd7b..cde61bd 100644

//Synthetic comment -- @@ -31,30 +31,36 @@
* Fatal: Use sparingly because a warning marked as fatal will be
* considered critical and will abort Export APK etc in ADT
*/
FATAL("Fatal"),

/**
* Errors: The issue is known to be a real error that must be addressed.
*/
ERROR("Error"),

/**
* Warning: Probably a problem.
*/
WARNING("Warning"),

/**
* Information only: Might not be a problem, but the check has found
* something interesting to say about the code.
*/
INFORMATIONAL("Information"),

/**
* Ignore: The user doesn't want to see this issue
*/
IGNORE("Ignore");

    private String mDisplay;

private Severity(@NonNull String display) {
mDisplay = display;
//Synthetic comment -- @@ -65,8 +71,7 @@
*
* @return a description of the severity
*/
    @NonNull
    public String getDescription() {
return mDisplay;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java
//Synthetic comment -- index abc08b2..323f88a 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -62,7 +63,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -75,7 +76,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (!element.hasAttributeNS(ANDROID_URI, ATTR_CONTENT_DESCRIPTION)) {
context.report(ISSUE, element, context.getLocation(element),
"[Accessibility] Missing contentDescription attribute on image", null);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java
//Synthetic comment -- index 9bbb4c1..bc5eaae 100644

//Synthetic comment -- @@ -98,7 +98,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -108,12 +108,12 @@
}

@Override
    public void beforeCheckFile(Context context) {
mFileAttributes = null;
}

@Override
    public void afterCheckFile(Context context) {
if (mFileAttributes != null) {
assert context instanceof XmlContext; // mAFilettributes is only set in XML files

//Synthetic comment -- @@ -161,7 +161,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (mAlwaysFields != null && !mHasIfRoomRefs) {
for (Location location : mAlwaysFields) {
context.report(ISSUE, location,
//Synthetic comment -- @@ -174,7 +174,7 @@
// ---- Implements XmlScanner ----

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
if (mFileAttributes == null) {
mFileAttributes = new ArrayList<Attr>();
}
//Synthetic comment -- @@ -190,7 +190,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new FieldAccessChecker(context);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 4f6df4f..9eae7c4 100644

//Synthetic comment -- @@ -94,17 +94,17 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

@Override
    public void beforeCheckProject(Context context) {
mApiDatabase = ApiLookup.get(context.getClient());
// We can't look up the minimum API required by the project here:
// The manifest file hasn't been processed yet in the -before- project hook.
//Synthetic comment -- @@ -130,7 +130,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
if (mApiDatabase == null) {
return;
}
//Synthetic comment -- @@ -158,7 +158,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (mApiDatabase == null) {
return;
}
//Synthetic comment -- @@ -234,7 +234,7 @@

@SuppressWarnings("rawtypes")
@Override
    public void checkClass(final ClassContext context, ClassNode classNode) {
if (mApiDatabase == null) {
return;
}
//Synthetic comment -- @@ -319,7 +319,7 @@
owner = classNode.superName;
}

                    do {
int api = mApiDatabase.getCallVersion(owner, name, desc);
if (api > minSdk) {
String fqcn = owner.replace('/', '.') + '#' + name;
//Synthetic comment -- @@ -339,7 +339,7 @@
} else {
owner = null;
}
                    } while (owner != null);
} else if (type == AbstractInsnNode.FIELD_INSN) {
FieldInsnNode node = (FieldInsnNode) instruction;
String name = node.name;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index dd5585b..4186028 100644

//Synthetic comment -- @@ -739,7 +739,7 @@
return -1;
}

    private int findMember(int classNumber, @NonNull String name, @NonNull String desc) {
// The index array contains class indexes from 0 to classCount and
// member indices from classCount to mIndices.length.
int low = mClassCount;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 568e885..92743b8 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_INTEGER_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -32,7 +33,6 @@
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.util.Pair;

//Synthetic comment -- @@ -91,16 +91,11 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.VALUES;
}

@Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
public Collection<String> getApplicableElements() {
return Arrays.asList(
TAG_ARRAY,
//Synthetic comment -- @@ -110,14 +105,14 @@
}

@Override
    public void beforeCheckProject(Context context) {
if (context.getPhase() == 1) {
mFileToArrayCount = new HashMap<File, Pair<String,Integer>>(30);
}
}

@Override
    public void afterCheckProject(Context context) {
if (context.getPhase() == 1) {
// Check that all arrays for the same name have the same number of translations

//Synthetic comment -- @@ -222,7 +217,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
int phase = context.getPhase();

Attr attribute = element.getAttributeNode(ATTR_NAME);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 0645d56..146da7f 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.tools.lint.detector.api.LintUtils.assertionsEnabled;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.tools.lint.client.api.IssueRegistry;
//Synthetic comment -- @@ -173,7 +174,7 @@
}

@Override
    public List<Issue> getIssues() {
return sIssues;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index 9321561..b644b14 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -193,7 +193,7 @@
}

@Override
    public void afterCheckProject(Context context) {
int phase = context.getPhase();
if (phase == 1 && mApplicableResources != null) {
// We found resources for the string "Cancel"; perform a second pass
//Synthetic comment -- @@ -214,7 +214,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
// This detector works in two passes.
// In pass 1, it looks in layout files for hardcoded strings of "Cancel", or
// references to @string/cancel or @android:string/cancel.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ChildCountDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ChildCountDetector.java
//Synthetic comment -- index 5a14f75..2f4503b 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.REQUEST_FOCUS;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -74,7 +75,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -90,7 +91,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
int childCount = LintUtils.getChildCount(element);
String tagName = element.getTagName();
if (tagName.equals(SCROLL_VIEW) || tagName.equals(HORIZONTAL_SCROLL_VIEW)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index e6d077a..dc3410b 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.tools.lint.detector.api.LintConstants.RESOURCE_CLZ_COLOR;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -60,12 +62,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -77,8 +79,8 @@
}

@Override
    public void visitResourceReference(JavaContext context, AstVisitor visitor,
            Node select, String type, String name, boolean isFramework) {
if (type.equals(RESOURCE_CLZ_COLOR)) {
while (select.getParent() instanceof Select) {
select = select.getParent();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DeprecationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DeprecationDetector.java
//Synthetic comment -- index 25c247d..952224c 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PHONE_NUMBER;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_SINGLE_LINE;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -64,7 +65,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -119,13 +120,13 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
context.report(ISSUE, element, context.getLocation(element),
String.format("%1$s is deprecated", element.getTagName()), null);
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
if (!ANDROID_URI.equals(attribute.getNamespaceURI())) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DetectMissingPrefix.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DetectMissingPrefix.java
//Synthetic comment -- index 6ea7899..8a9c0a9 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;
import static com.android.tools.lint.detector.api.LintConstants.XMLNS_PREFIX;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -70,7 +71,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -80,7 +81,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String uri = attribute.getNamespaceURI();
if (uri == null || uri.length() == 0) {
String name = attribute.getName();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index bbb3de5..c8e34c5 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static com.android.tools.lint.detector.api.LintConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -103,12 +104,12 @@


@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.MENU;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -123,14 +124,14 @@
}

@Override
    public void beforeCheckFile(Context context) {
if (context.getPhase() == 1) {
mIds = new HashSet<String>();
}
}

@Override
    public void afterCheckFile(Context context) {
if (context.getPhase() == 1) {
// Store this layout's set of ids for full project analysis in afterCheckProject
mFileToIds.put(context.file, mIds);
//Synthetic comment -- @@ -140,7 +141,7 @@
}

@Override
    public void beforeCheckProject(Context context) {
if (context.getPhase() == 1) {
mFileToIds = new HashMap<File, Set<String>>();
mIncludes = new HashMap<File, List<String>>();
//Synthetic comment -- @@ -148,7 +149,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (context.getPhase() == 1) {
// Look for duplicates
if (mIncludes.size() > 0) {
//Synthetic comment -- @@ -201,7 +202,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
// Record include graph such that we can look for inter-layout duplicates after the
// project has been fully checked

//Synthetic comment -- @@ -241,7 +242,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
assert attribute.getName().equals(ATTR_ID) || attribute.getLocalName().equals(ATTR_ID);
String id = attribute.getValue();
if (context.getPhase() == 1) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ExtraTextDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ExtraTextDetector.java
//Synthetic comment -- index e0665df..7e2ecb2 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.DefaultPosition;
//Synthetic comment -- @@ -59,7 +60,7 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.LAYOUT
|| folderType == ResourceFolderType.MENU
|| folderType == ResourceFolderType.ANIMATOR
//Synthetic comment -- @@ -69,12 +70,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public void visitDocument(XmlContext context, Document document) {
mFoundText = false;
visitNode(context, document);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index 656c226..7d8deab 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -72,12 +73,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -85,7 +86,7 @@

@SuppressWarnings("rawtypes")
@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
List<Entry> pendingCalls = null;
int currentLine = 0;
List methodList = classNode.methods;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 37b22ea..6839004 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.tools.lint.detector.api.LintConstants.FRAGMENT;
import static com.android.tools.lint.detector.api.LintConstants.FRAGMENT_V4;

import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
//Synthetic comment -- @@ -77,19 +78,19 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

// ---- Implements ClassScanner ----

@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
if ((classNode.access & Opcodes.ACC_ABSTRACT) != 0) {
// Ignore abstract classes since they are clearly (and by definition) not intended to
// be instantiated. We're looking for accidental non-static or missing constructor








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java
//Synthetic comment -- index 762a4b5..6e8d8a3 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ROW;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ROW_COUNT;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -60,7 +61,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -85,7 +86,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
int declaredRowCount = getInt(element, ATTR_ROW_COUNT, -1);
int declaredColumnCount = getInt(element, ATTR_COLUMN_COUNT, -1);









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 425edac..9efd666 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -60,19 +61,19 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

// ---- Implements ClassScanner ----

@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
if (classNode.name.indexOf('$') == -1) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index df8c728..1237e1b 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_DEBUGGABLE;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -65,12 +66,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return file.getName().equals(ANDROID_MANIFEST_XML);
}

//Synthetic comment -- @@ -82,7 +83,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
if (attribute.getNamespaceURI().equals(ANDROID_URI)) {
//if (attribute.getOwnerElement().getTagName().equals(TAG_APPLICATION)) {
context.report(ISSUE, attribute, context.getLocation(attribute),








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java
//Synthetic comment -- index 09a3ffc..3665303 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PROMPT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -66,7 +67,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -82,7 +83,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String value = attribute.getValue();
if (value.length() > 0 && (value.charAt(0) != '@' && value.charAt(0) != '?')) {
// Make sure this is really one of the android: attributes








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index c687d74..adee77d 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -258,22 +259,22 @@
}

@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

@Override
    public void beforeCheckProject(Context context) {
mApplicationIcon = null;
}

@Override
    public void afterCheckLibraryProject(Context context) {
checkResourceFolder(context, context.getProject().getDir());
}

@Override
    public void afterCheckProject(Context context) {
checkResourceFolder(context, context.getProject().getDir());
}

//Synthetic comment -- @@ -1203,7 +1204,7 @@
// XML detector: Skim manifest

@Override
    public boolean appliesTo(Context context, File file) {
return file.getName().equals(ANDROID_MANIFEST_XML);
}

//Synthetic comment -- @@ -1213,7 +1214,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
assert element.getTagName().equals(TAG_APPLICATION);
mApplicationIcon = element.getAttributeNS(ANDROID_URI, ATTR_ICON);
if (mApplicationIcon.startsWith(DRAWABLE_RESOURCE_PREFIX)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index 84216d1..3fd5573 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_VERTICAL;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -100,7 +101,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -110,7 +111,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
List<Element> children = LintUtils.getChildren(element);
// See if there is exactly one child with a weight
boolean multipleWeights = false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index e99005c..0073d06 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -139,12 +140,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -160,7 +161,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new PerformanceVisitor(context);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index cf75ae5..b116eb9 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_SDK;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -159,23 +160,23 @@
private String mPackage;

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return file.getName().equals(ANDROID_MANIFEST_XML);
}

@Override
    public void beforeCheckFile(Context context) {
mSeenApplication = false;
mSeenUsesSdk = 0;
}

@Override
    public void afterCheckFile(Context context) {
if (mSeenUsesSdk == 0 && context.isEnabled(USES_SDK)) {
context.report(USES_SDK, Location.create(context.file),
"Manifest should specify a minimum API level with " +
//Synthetic comment -- @@ -209,7 +210,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String tag = element.getTagName();
Node parentNode = element.getParentNode();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index 06258dd..daefda7 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -68,12 +69,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -81,7 +82,7 @@

@SuppressWarnings("rawtypes")
@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
List methodList = classNode.methods;
for (Object m : methodList) {
MethodNode method = (MethodNode) m;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index 4cb96b5..5ed0257 100644

//Synthetic comment -- @@ -103,17 +103,18 @@
}

@Override
public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return LintUtils.isXmlFile(file) || LintUtils.endsWith(file.getName(), DOT_JAVA);
}

@Override
    public void afterCheckProject(Context context) {
if (mPending != null && mWhitelistedLayouts != null) {
// Process all the root FrameLayouts that are eligible, and generate
// suggestions for <merge> replacements for any layouts that are included
//Synthetic comment -- @@ -146,7 +147,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String tag = element.getTagName();
if (tag.equals(INCLUDE)) {
String layout = element.getAttribute(ATTR_LAYOUT); // NOTE: Not in android: namespace








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NamespaceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NamespaceDetector.java
//Synthetic comment -- index 88a9dd5..e760a9c 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.XMLNS_PREFIX;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -102,12 +103,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public void visitDocument(XmlContext context, Document document) {
boolean haveCustomNamespace = false;
Element root = document.getDocumentElement();
NamedNodeMap attributes = root.getAttributes();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java
//Synthetic comment -- index 9ca8017..d6cdbd6 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -62,13 +63,13 @@
}

@Override
    public void beforeCheckFile(Context context) {
mVisitingHorizontalScroll = 0;
mVisitingVerticalScroll = 0;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -103,7 +104,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
boolean vertical = isVerticalScroll(element);
if (vertical) {
mVisitingVerticalScroll++;
//Synthetic comment -- @@ -129,7 +130,7 @@
}

@Override
    public void visitElementAfter(XmlContext context, Element element) {
if (isVerticalScroll(element)) {
mVisitingVerticalScroll--;
assert mVisitingVerticalScroll >= 0;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index 4002bd5..9972604 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;

import com.android.tools.lint.client.api.IDomParser;
import com.android.tools.lint.client.api.SdkInfo;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -218,7 +219,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -233,7 +234,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String name = attribute.getLocalName();
if (name != null && name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attribute.getNamespaceURI())) {
//Synthetic comment -- @@ -296,7 +297,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String layout = element.getAttribute(ATTR_LAYOUT);
if (layout.startsWith(LAYOUT_RESOURCE_PREFIX)) { // Ignore @android:layout/ layouts
layout = layout.substring(LAYOUT_RESOURCE_PREFIX.length());
//Synthetic comment -- @@ -320,7 +321,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (mIncludes == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 14370f3..75fda79 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.tools.lint.detector.api.LintConstants.ATTR_ON_CLICK;

import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
//Synthetic comment -- @@ -81,17 +82,17 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public void afterCheckProject(Context context) {
if (mNames != null && mNames.size() > 0 && mHaveBytecode) {
List<String> names = new ArrayList<String>(mNames.keySet());
Collections.sort(names);
//Synthetic comment -- @@ -129,7 +130,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String value = attribute.getValue();
if (value.isEmpty() || value.trim().isEmpty()) {
context.report(ISSUE, attribute, context.getLocation(attribute),
//Synthetic comment -- @@ -170,7 +171,7 @@

@SuppressWarnings("rawtypes")
@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
if (mNames == null) {
// No onClick attributes in the XML files
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index 51790e5..12f9b04 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import static com.android.tools.lint.detector.api.LintConstants.VALUE_DISABLED;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -148,7 +149,7 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
// Look in layouts for drawable resources
return super.appliesTo(folderType)
// and in resource files for theme definitions
//Synthetic comment -- @@ -158,12 +159,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return LintUtils.isXmlFile(file) || LintUtils.endsWith(file.getName(), DOT_JAVA);
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -194,7 +195,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (mRootAttributes != null) {
for (Pair<Location, String> pair : mRootAttributes) {
Location location = pair.getFirst();
//Synthetic comment -- @@ -260,7 +261,7 @@
// ---- Implements XmlScanner ----

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
// Only consider the root element's background
if (attribute.getOwnerDocument().getDocumentElement() == attribute.getOwnerElement()) {
// If the drawable is a non-repeated pattern then the overdraw might be
//Synthetic comment -- @@ -321,7 +322,7 @@
}

@Override
    public void beforeCheckFile(Context context) {
if (endsWith(context.file.getName(), DOT_XML)) {
// Drawable XML files should not be considered for overdraw, except for <bitmap>'s.
// The bitmap elements are handled in the scanBitmap() method; it will clear
//Synthetic comment -- @@ -339,7 +340,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String tag = element.getTagName();
if (tag.equals(TAG_STYLE)) {
scanTheme(element);
//Synthetic comment -- @@ -470,7 +471,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new OverdrawVisitor();
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateResourceDetector.java
//Synthetic comment -- index 2b37f82..7d909ae 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
//Synthetic comment -- @@ -52,7 +53,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -62,7 +63,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String value = attribute.getNodeValue();
if (value.startsWith("@*android:")) { //$NON-NLS-1$
context.report(ISSUE, attribute, context.getLocation(attribute),








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java
//Synthetic comment -- index c062507..41af3b1 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_CONFIG;
import static com.android.tools.lint.detector.api.LintConstants.PROJECT_PROPERTIES;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -91,7 +92,7 @@
EnumSet.of(Scope.PROGUARD_FILE));

@Override
    public void run(Context context) {
String contents = context.getContents();
if (contents != null) {
if (context.isEnabled(WRONGKEEP)) {
//Synthetic comment -- @@ -151,12 +152,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index cb37d5f..f87abcc 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -59,7 +60,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -69,7 +70,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String value = attribute.getValue();
if (value.endsWith("px") && value.matches("\\d+px")) { //$NON-NLS-1$
if (value.charAt(0) == '0') {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 0f16244..1d55109 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -85,12 +86,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

//Synthetic comment -- @@ -102,7 +103,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String fqcn = getFqcn(element);
String tag = element.getTagName();
String frameworkClass = tagToClass(tag);
//Synthetic comment -- @@ -148,7 +149,7 @@
// ---- Implements ClassScanner ----

@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
// Abstract classes do not need to be registered
if ((classNode.access & Opcodes.ACC_ABSTRACT) != 0) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java
//Synthetic comment -- index 8ba2bc4..e5db5f1 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static com.android.tools.lint.detector.api.LintConstants.VALUE_FILL_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_MATCH_PARENT;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -64,7 +65,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -77,13 +78,16 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
List<Element> children = LintUtils.getChildren(element);
boolean isHorizontal = HORIZONTAL_SCROLL_VIEW.equals(element.getTagName());
String attributeName = isHorizontal ? ATTR_LAYOUT_WIDTH : ATTR_LAYOUT_HEIGHT;
for (Element child : children) {
Attr sizeNode = child.getAttributeNodeNS(ANDROID_URI, attributeName);
            String value = sizeNode != null ? sizeNode.getValue() : null;
if (VALUE_FILL_PARENT.equals(value) || VALUE_MATCH_PARENT.equals(value)) {
String msg = String.format("This %1$s should use android:%2$s=\"wrap_content\"",
child.getTagName(), attributeName);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java
//Synthetic comment -- index b239d57..d5e3a88 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -59,12 +60,12 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -76,7 +77,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new StringChecker(context);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 807d515..c9848eb 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -147,11 +149,11 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}
@Override
    public boolean appliesTo(Context context, File file) {
return file.getName().equals(ANDROID_MANIFEST_XML);
}

//Synthetic comment -- @@ -167,7 +169,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
String tag = element.getTagName();
if (tag.equals(TAG_SERVICE)) {
checkService(context, element);
//Synthetic comment -- @@ -264,7 +266,8 @@
}

if (!hasPermission) {
                            context.report(EXPORTED_PROVIDER, element, context.getLocation(element),
"Exported content providers can provide access to " +
"potentially sensitive data",
null);
//Synthetic comment -- @@ -288,7 +291,8 @@
}

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
StrictListAccessor<Expression,MethodInvocation> args = node.astArguments();
Iterator<Expression> iterator = args.iterator();
while (iterator.hasNext()) {
//Synthetic comment -- @@ -297,7 +301,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new IdentifierVisitor(context);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java
//Synthetic comment -- index e711816..8653944 100644

//Synthetic comment -- @@ -16,20 +16,20 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import lombok.ast.MethodInvocation;

import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;

/**
* Looks for invocations of android.webkit.WebSettings.setJavaScriptEnabled.
//Synthetic comment -- @@ -52,15 +52,11 @@
public SetJavaScriptEnabledDetector() {
}

    @Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

// ---- Implements JavaScanner ----

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
if (node.astArguments().size() == 1
&& !node.astArguments().first().toString().equals("false")) { //$NON-NLS-1$
context.report(ISSUE, node, context.getLocation(node),








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 525ab6d..b8c86bf 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -22,7 +24,6 @@
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import java.io.File;
import java.util.Collections;
//Synthetic comment -- @@ -61,16 +62,11 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}


    @Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

// ---- Implements JavaScanner ----

@Override
//Synthetic comment -- @@ -94,7 +90,8 @@
}

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
assert node.astName().astValue().equals("edit");
if (node.astOperand() == null) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StateListDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StateListDetector.java
//Synthetic comment -- index 3235c9a..d7f0d99 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -57,17 +58,17 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.DRAWABLE;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public void visitDocument(XmlContext context, Document document) {
// TODO: Look for views that don't specify
// Display the error token somewhere so it can be suppressed
// Emit warning at the end "run with --help to learn how to suppress types of errors/checks";








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index 5fae363..f5d6d80 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;

import com.android.annotations.VisibleForTesting;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.client.api.IJavaParser;
//Synthetic comment -- @@ -35,7 +37,6 @@
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.util.Pair;

//Synthetic comment -- @@ -162,12 +163,12 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.VALUES;
}

@Override
    public boolean appliesTo(Context context, File file) {
if (LintUtils.endsWith(file.getName(), DOT_JAVA)) {
return mFormatStrings != null;
}
//Synthetic comment -- @@ -176,17 +177,12 @@
}

@Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
public Collection<String> getApplicableElements() {
return Collections.singletonList(TAG_STRING);
}

@Override
    public void visitElement(XmlContext context, Element element) {
NodeList childNodes = element.getChildNodes();
if (childNodes.getLength() > 0) {
if (childNodes.getLength() == 1) {
//Synthetic comment -- @@ -309,7 +305,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (mFormatStrings != null) {
Formatter formatter = new Formatter();

//Synthetic comment -- @@ -773,7 +769,8 @@
}

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
if (mFormatStrings == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StyleCycleDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StyleCycleDetector.java
//Synthetic comment -- index f248d6e..03766b7 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.STYLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -58,12 +59,12 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.VALUES;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -73,7 +74,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
Attr parentNode = element.getAttributeNode(ATTR_PARENT);
if (parentNode != null) {
String parent = parentNode.getValue();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java
//Synthetic comment -- index 2cc81ab..c133ce2 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.EDIT_TEXT;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -63,7 +64,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -73,7 +74,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (!element.hasAttributeNS(ANDROID_URI, ATTR_INPUT_TYPE) &&
!element.hasAttributeNS(ANDROID_URI, ATTR_HINT)) {
// Also make sure the EditText does not set an inputMethod in which case








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java
//Synthetic comment -- index 09608b1..e6e0371 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import static com.android.tools.lint.detector.api.LintConstants.VALUE_NONE;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_TRUE;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -91,7 +92,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -109,7 +110,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
NamedNodeMap attributes = element.getAttributes();
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index f199438..d876c53 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -22,7 +24,6 @@
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import java.io.File;
import java.util.Collections;
//Synthetic comment -- @@ -60,16 +61,11 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}


    @Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

// ---- Implements JavaScanner ----

@Override
//Synthetic comment -- @@ -93,7 +89,8 @@
}

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
assert node.astName().astValue().equals("makeText");
if (node.astOperand() == null) {
// "makeText()" in the code with no operand








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TooManyViewsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TooManyViewsDetector.java
//Synthetic comment -- index 257d06c..ed3480a 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -102,12 +103,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public void beforeCheckFile(Context context) {
mViewCount = mDepth = 0;
mWarnedAboutDepth = false;
}
//Synthetic comment -- @@ -118,7 +119,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
mViewCount++;
mDepth++;

//Synthetic comment -- @@ -140,7 +141,7 @@
}

@Override
    public void visitElementAfter(XmlContext context, Element element) {
mDepth--;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 98d56b0..70e0723 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.annotations.VisibleForTesting;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -33,7 +34,6 @@
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.Sets;

//Synthetic comment -- @@ -116,16 +116,11 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.VALUES;
}

@Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
public Collection<String> getApplicableElements() {
return Arrays.asList(
TAG_STRING,
//Synthetic comment -- @@ -134,14 +129,14 @@
}

@Override
    public void beforeCheckProject(Context context) {
if (context.getDriver().getPhase() == 1) {
mFileToNames = new HashMap<File, Set<String>>();
}
}

@Override
    public void beforeCheckFile(Context context) {
if (context.getPhase() == 1) {
mNames = new HashSet<String>();
}
//Synthetic comment -- @@ -151,7 +146,7 @@
}

@Override
    public void afterCheckFile(Context context) {
if (context.getPhase() == 1) {
// Store this layout's set of ids for full project analysis in afterCheckProject
mFileToNames.put(context.file, mNames);
//Synthetic comment -- @@ -161,7 +156,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (context.getPhase() == 1) {
// NOTE - this will look for the presence of translation strings.
// If you create a resource folder but don't actually place a file in it
//Synthetic comment -- @@ -406,7 +401,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (mIgnoreFile) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index c5a9302..5ad52b3 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.annotations.VisibleForTesting;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -192,12 +193,12 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.VALUES;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -210,7 +211,7 @@
}

@Override
    public void beforeCheckProject(Context context) {
mCheckDashes = context.isEnabled(DASHES);
mCheckQuotes = context.isEnabled(QUOTES);
mCheckFractions = context.isEnabled(FRACTIONS);
//Synthetic comment -- @@ -219,7 +220,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
NodeList childNodes = element.getChildNodes();
for (int i = 0, n = childNodes.getLength(); i < n; i++) {
Node child = childNodes.item(i);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 3ea1c14..f9b5a67 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -121,17 +123,17 @@
}

@Override
    public void run(Context context) {
assert false;
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public void beforeCheckProject(Context context) {
if (context.getPhase() == 1) {
mDeclarations = new HashSet<String>(300);
mReferences = new HashSet<String>(300);
//Synthetic comment -- @@ -141,7 +143,7 @@
// ---- Implements JavaScanner ----

@Override
    public void beforeCheckFile(Context context) {
File file = context.file;

String fileName = file.getName();
//Synthetic comment -- @@ -188,7 +190,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (context.getPhase() == 1) {
mDeclarations.removeAll(mReferences);
Set<String> unused = mDeclarations;
//Synthetic comment -- @@ -330,7 +332,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (TAG_RESOURCES.equals(element.getTagName())) {
for (Element item : LintUtils.getChildren(element)) {
String name = item.getAttribute(ATTR_NAME);
//Synthetic comment -- @@ -401,7 +403,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String value = attribute.getValue();

if (value.startsWith("@+") && !value.startsWith("@+android")) { //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -437,7 +439,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

//Synthetic comment -- @@ -452,8 +454,9 @@
}

@Override
    public void visitResourceReference(JavaContext context, AstVisitor visitor,
            lombok.ast.Node node, String type, String name, boolean isFramework) {
if (mReferences != null && !isFramework) {
String reference = R_PREFIX + type + '.' + name;
mReferences.add(reference);
//Synthetic comment -- @@ -461,7 +464,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
if (mReferences != null) {
return new UnusedResourceVisitor();
} else {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java
//Synthetic comment -- index 540a0f1..d091ce1 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -63,7 +64,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -75,7 +76,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
int childCount = LintUtils.getChildCount(element);
if (childCount == 2) {
List<Element> children = LintUtils.getChildren(element);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UselessViewDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UselessViewDetector.java
//Synthetic comment -- index fd4ff58..b913588 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import static com.android.tools.lint.detector.api.LintConstants.TABLE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -85,7 +86,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -127,7 +128,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
int childCount = LintUtils.getChildCount(element);
if (childCount == 0) {
// Check to see if this is a leaf layout that can be removed








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/Utf8Detector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/Utf8Detector.java
//Synthetic comment -- index 6a87e94..875de62 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -57,12 +58,12 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

@Override
    public void visitDocument(XmlContext context, Document document) {
String xml = context.getContents();

// AAPT: The prologue must be in the first line








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 72aaa77..538c0d6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -73,19 +74,19 @@
}

@Override
    public boolean appliesTo(Context context, File file) {
return true;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements ClassScanner ----

@Override
    public void checkClass(ClassContext context, ClassNode classNode) {
if (classNode.name.indexOf('$') != -1
&& (classNode.access & Opcodes.ACC_STATIC) == 0) {
// Ignore inner classes that aren't static: we can't create these








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewTypeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewTypeDetector.java
//Synthetic comment -- index 78a06af..30def17 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -69,17 +71,17 @@
private Map<String, String> mIdToViewTag = new HashMap<String, String>(50);

@Override
    public Speed getSpeed() {
return Speed.SLOW;
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.LAYOUT;
}

@Override
    public boolean appliesTo(Context context, File file) {
if (LintUtils.endsWith(file.getName(), DOT_JAVA)) {
return true;
}
//Synthetic comment -- @@ -100,7 +102,7 @@
private static final String IGNORE = "#ignore#";

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
String view = attribute.getOwnerElement().getTagName();
String value = attribute.getValue();
String id = null;
//Synthetic comment -- @@ -131,7 +133,8 @@
}

@Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
assert node.astName().getDescription().equals("findViewById");
if (node.getParent() instanceof Cast) {
Cast cast = (Cast) node.getParent();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index ef45c09..a9652d1 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.tools.lint.detector.api.LintConstants.VALUE_ID;
import static com.android.tools.lint.detector.api.LintUtils.stripIdPrefix;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.client.api.IDomParser;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -127,12 +128,12 @@
}

@Override
    public boolean appliesTo(ResourceFolderType folderType) {
return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.VALUES;
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -147,13 +148,13 @@
}

@Override
    public void beforeCheckFile(Context context) {
mFileIds = new HashSet<String>();
mRelativeLayouts = null;
}

@Override
    public void afterCheckFile(Context context) {
if (mRelativeLayouts != null) {
for (Element layout : mRelativeLayouts) {
NodeList children = layout.getChildNodes();
//Synthetic comment -- @@ -196,7 +197,7 @@
}

@Override
    public void afterCheckProject(Context context) {
if (mHandles != null) {
boolean checkSameLayout = context.isEnabled(UNKNOWN_ID_LAYOUT);
boolean checkExists = context.isEnabled(UNKNOWN_ID);
//Synthetic comment -- @@ -257,7 +258,7 @@
}

@Override
    public void visitElement(XmlContext context, Element element) {
if (element.getTagName().equals(RELATIVE_LAYOUT)) {
if (mRelativeLayouts == null) {
mRelativeLayouts = new ArrayList<Element>();
//Synthetic comment -- @@ -279,7 +280,7 @@
}

@Override
    public void visitAttribute(XmlContext context, Attr attribute) {
assert attribute.getName().equals(ATTR_ID) || attribute.getLocalName().equals(ATTR_ID);
String id = attribute.getValue();
mFileIds.add(id);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongImportDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongImportDetector.java
//Synthetic comment -- index 2aa416c..fc6cde2 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -67,7 +68,7 @@
}

@Override
    public Speed getSpeed() {
return Speed.FAST;
}

//Synthetic comment -- @@ -80,7 +81,7 @@
}

@Override
    public AstVisitor createJavaVisitor(JavaContext context) {
return new ImportVisitor(context);
}









//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/AbstractViewRule.java b/rule_api/src/com/android/ide/common/api/AbstractViewRule.java
//Synthetic comment -- index 3bbf8e7..7f05809 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.api;

import com.android.annotations.Nullable;
import com.google.common.annotations.Beta;

//Synthetic comment -- @@ -31,7 +32,7 @@
@Beta
public class AbstractViewRule implements IViewRule {
@Override
    public boolean onInitialize(String fqcn, IClientRulesEngine engine) {
return true;
}

//Synthetic comment -- @@ -50,22 +51,22 @@

@Override
@Nullable
    public List<String> getSelectionHint(INode parentNode, INode childNode) {
return null;
}

@Override
    public void addLayoutActions(List<RuleAction> actions, INode parentNode,
            List<? extends INode> children) {
}

@Override
    public void addContextMenuActions(List<RuleAction> actions, INode node) {
}

@Override
    public void paintSelectionFeedback(IGraphics graphics, INode parentNode,
            List<? extends INode> childNodes, Object view) {
}

// ==== Drag & drop support ====
//Synthetic comment -- @@ -73,66 +74,66 @@
// By default Views do not accept drag'n'drop.
@Override
@Nullable
    public DropFeedback onDropEnter(INode targetNode, Object targetView, IDragElement[] elements) {
return null;
}

@Override
@Nullable
    public DropFeedback onDropMove(INode targetNode, IDragElement[] elements,
            DropFeedback feedback, Point p) {
return null;
}

@Override
    public void onDropLeave(INode targetNode, IDragElement[] elements, DropFeedback feedback) {
// ignore
}

@Override
public void onDropped(
            INode targetNode,
            IDragElement[] elements,
            DropFeedback feedback,
            Point p) {
// ignore
}


@Override
    public void onPaste(INode targetNode, Object targetView, IDragElement[] pastedElements) {
}

// ==== Create/Remove hooks ====

@Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
}

@Override
    public void onChildInserted(INode child, INode parent, InsertType insertType) {
}

@Override
    public void onRemovingChildren(List<INode> deleted, INode parent) {
}

// ==== Resizing ====

@Override
@Nullable
    public DropFeedback onResizeBegin(INode child, INode parent, SegmentType horizontalEdge,
            SegmentType verticalEdge, Object childView, Object parentView) {
return null;
}

@Override
    public void onResizeUpdate(DropFeedback feedback, INode child, INode parent, Rect newBounds,
int modifierMask) {
}

@Override
    public void onResizeEnd(DropFeedback feedback, INode child, final INode parent,
            final Rect newBounds) {
}
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java b/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index ec28520..b9ea6cb 100644

//Synthetic comment -- @@ -106,10 +106,10 @@
/**
* Returns a resource name validator for the current project
*
     * @return an {@link IValidator} for validating new resource name in the current
*         project
*/
    @NonNull
IValidator getResourceValidator();

/**








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IMenuCallback.java b/rule_api/src/com/android/ide/common/api/IMenuCallback.java
//Synthetic comment -- index fd100ee..2ff3f8d 100644

//Synthetic comment -- @@ -49,4 +49,17 @@
@NonNull List<? extends INode> selectedNodes,
@Nullable String valueId,
@Nullable Boolean newValue);
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/INode.java b/rule_api/src/com/android/ide/common/api/INode.java
//Synthetic comment -- index d957419..b137699 100644

//Synthetic comment -- @@ -180,7 +180,7 @@
* @param value It's value. Cannot be null. An empty value <em>removes</em> the attribute.
* @return Whether the attribute was actually set or not.
*/
    boolean setAttribute(@Nullable String uri, @NonNull String localName, @NonNull String value);

/**
* Returns a given XML attribute.








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/RuleAction.java b/rule_api/src/com/android/ide/common/api/RuleAction.java
//Synthetic comment -- index f6c7e8c..489a3cc 100644

//Synthetic comment -- @@ -448,9 +448,9 @@
return mCallback;
}

    // Implements Comparable<MenuAciton>
@Override
    public int compareTo(@NonNull RuleAction other) {
if (mSortPriority != other.mSortPriority) {
return mSortPriority - other.mSortPriority;
}
//Synthetic comment -- @@ -468,7 +468,7 @@
public static class Separator extends RuleAction {
/** Construct using the factory {@link #createSeparator(int)} */
private Separator(int sortPriority, boolean supportsMultipleNodes) {
            super("_separator", "", null, sortPriority,  //$NON-NLS-1$ //$NON-NLS-2$
supportsMultipleNodes);
}
}
//Synthetic comment -- @@ -689,13 +689,13 @@
}

@Override
        public List<String> getIds() {
ensureInitialized();
return mIds;
}

@Override
        public List<String> getTitles() {
ensureInitialized();
return mTitles;
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/Segment.java b/rule_api/src/com/android/ide/common/api/Segment.java
//Synthetic comment -- index 9f03286..d31d9f8 100644

//Synthetic comment -- @@ -51,7 +51,7 @@
public final MarginType marginType;

/** The node that contains this edge */
    @NonNull
public final INode node;

/**
//Synthetic comment -- @@ -61,7 +61,7 @@
@Nullable
public final String id;

    public Segment(int at, int from, int to, @NonNull INode node, @Nullable String id,
@NonNull SegmentType edgeType, @NonNull MarginType marginType) {
this.at = at;
this.from = from;








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/SegmentType.java b/rule_api/src/com/android/ide/common/api/SegmentType.java
//Synthetic comment -- index 25635c7..9da248a 100644

//Synthetic comment -- @@ -27,7 +27,22 @@
*/
@Beta
public enum SegmentType {
    LEFT, TOP, RIGHT, BOTTOM, BASELINE, CENTER_VERTICAL, CENTER_HORIZONTAL, UNKNOWN;

public boolean isHorizontal() {
return this == TOP || this == BOTTOM || this == BASELINE || this == CENTER_HORIZONTAL;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index dc19287..768a835 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.avd;

import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -1404,14 +1405,14 @@
Wait.WAIT_FOR_READERS,
new IProcessOutput() {
@Override
                        public void out(String line) {
if (line != null) {
stdOutput.add(line);
}
}

@Override
                        public void err(String line) {
if (line != null) {
errorOutput.add(line);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/KeystoreHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/KeystoreHelper.java
//Synthetic comment -- index af5b401..ba4ce8c 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.util.GrabProcessOutput;
//Synthetic comment -- @@ -111,7 +112,7 @@
Wait.WAIT_FOR_READERS,
new IProcessOutput() {
@Override
                        public void out(String line) {
if (line != null) {
if (output != null) {
output.out(line);
//Synthetic comment -- @@ -122,7 +123,7 @@
}

@Override
                        public void err(String line) {
if (line != null) {
if (output != null) {
output.err(line);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 3addb31..c2e11cd 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.archives;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -609,14 +610,14 @@
Wait.WAIT_FOR_READERS,
new IProcessOutput() {
@Override
                            public void out(String line) {
if (line != null) {
result.append(line).append("\n");
}
}

@Override
                            public void err(String line) {
if (line != null) {
monitor.logError("[find_lock] Error: %1$s", line);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 8ae469e..e2052c4 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -315,14 +316,14 @@
Wait.WAIT_FOR_PROCESS,
new IProcessOutput() {
@Override
                        public void out(String line) {
if (line != null) {
monitor.log("[%1$s] %2$s", tag, line);
}
}

@Override
                        public void err(String line) {
if (line != null) {
monitor.logError("[%1$s] Error: %2$s", tag, line);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index daacf10..5d24a07 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.widgets;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
//Synthetic comment -- @@ -1112,14 +1113,14 @@
Wait.ASYNC,
new IProcessOutput() {
@Override
                                    public void out(String line) {
if (line != null) {
filterStdOut(line);
}
}

@Override
                                    public void err(String line) {
if (line != null) {
filterStdErr(line);
}







