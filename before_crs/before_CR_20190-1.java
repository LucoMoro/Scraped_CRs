/*Support for editing id, text, string and reference properties

We have context menu items for editing the properties of the currently
selected view, but it is only available for boolean and enumerated
properties.

This changeset makes it possible to edit these properties in three
ways:

1. It adds in all the other properties to the Property context menu,
   but instead of pullright menus, the actions have "..." as a suffix,
   and when selected will open a dialog asking for the new value.
   (This is similar to how custom layout width/height values are
   handled.)

   If the attribute represents a reference-type, then the Reference
   Chooser (also used by the property sheet) is shown, and if not just
   a plain text field.

   (This context-menu feature was requested by external users.)

2. It adds the "ID" property as a top level item that can be edited
   directly via an "Edit ID..." menu item, right next to the "Layout
   Width" and "Layout Height" properties which are also special-cased
   at the top level.

3. For Views that have a "Text" property, this property is also added
   at the top level to be able to set it quickly and conveniently.  As
   soon as we get inline editing we can consider whether this is still
   needed.

In addition, the changeset extracts constants for the various ids used
for action identity, and makes a common function for setting a
particular node attribute.

Change-Id:Ib86a8a5412c39117fe250ce5788d8457a0e3fbe8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index a8c131b..d6ff1bd 100755

//Synthetic comment -- @@ -91,5 +91,22 @@
* @return the minimum API level to be supported, or -1 if it cannot be determined
*/
int getMinApiLevel();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index c9858e1..4377782 100644

//Synthetic comment -- @@ -17,8 +17,12 @@
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
//Synthetic comment -- @@ -30,6 +34,7 @@
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
//Synthetic comment -- @@ -49,6 +54,17 @@
* Common IViewRule processing to all view and layout classes.
*/
public class BaseViewRule implements IViewRule {
private static final String ZCUSTOM = "zcustom"; //$NON-NLS-1$

protected IClientRulesEngine mRulesEngine;
//Synthetic comment -- @@ -132,31 +148,48 @@
final String valueId,
final Boolean newValue) {
String fullActionId = action.getId();
                boolean isProp = fullActionId.startsWith("@prop@"); //$NON-NLS-1$
final String actionId = isProp ? fullActionId.substring(6) : fullActionId;
final INode node = selectedNode;

                if (fullActionId.equals("layout_1width")) { //$NON-NLS-1$
final String newAttrValue = getValue(valueId, customWidth);
if (newAttrValue != null) {
                        node.editXml("Change attribute " + ATTR_LAYOUT_WIDTH, new INodeHandler() {
                            public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, newAttrValue);
                            }
                        });
}
return;
                } else if (fullActionId.equals("layout_2height")) { //$NON-NLS-1$
// Ask the user
final String newAttrValue = getValue(valueId, customHeight);
if (newAttrValue != null) {
                        node.editXml("Change attribute " + ATTR_LAYOUT_HEIGHT, new INodeHandler() {
                            public void handle(INode n) {
                                n.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, newAttrValue);
                            }
                        });
}
return;
}

if (isProp) {
//Synthetic comment -- @@ -164,21 +197,26 @@
final Prop prop = (props != null) ? props.get(actionId) : null;

if (prop != null) {
node.editXml("Change attribute " + actionId, new INodeHandler() {
public void handle(INode n) {
if (prop.isToggle()) {
// case of toggle
String value = "";                  //$NON-NLS-1$
                                    if (valueId.equals("1t")) {         //$NON-NLS-1$
value = newValue ? "true" : ""; //$NON-NLS-1$ //$NON-NLS-2$
                                    } else if (valueId.equals("2f")) {
value = newValue ? "false" : "";//$NON-NLS-1$ //$NON-NLS-2$
}
n.setAttribute(ANDROID_URI, actionId, value);
} else if (prop.isFlag()) {
// case of a flag
String values = "";                 //$NON-NLS-1$
                                    if (!valueId.equals("~2clr")) {     //$NON-NLS-1$
values = n.getStringAttr(ANDROID_URI, actionId);
Set<String> newValues = new HashSet<String>();
if (values != null) {
//Synthetic comment -- @@ -193,13 +231,17 @@
values = join('|', newValues);
}
n.setAttribute(ANDROID_URI, actionId, values);
                                } else {
// case of an enum
String value = "";                   //$NON-NLS-1$
                                    if (!valueId.equals("~2clr")) {      //$NON-NLS-1$
value = newValue ? valueId : ""; //$NON-NLS-1$
}
n.setAttribute(ANDROID_URI, actionId, value);
}
}
});
//Synthetic comment -- @@ -208,6 +250,23 @@
}

/**
* Returns the value (which will ask the user if the value is the special
* {@link #ZCUSTOM} marker
*/
//Synthetic comment -- @@ -230,8 +289,17 @@
}
};

List<MenuAction> list1 = Arrays.asList(new MenuAction[] {
            new MenuAction.Choices("layout_1width", "Layout Width", //$NON-NLS-1$
mapify(
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
//Synthetic comment -- @@ -241,7 +309,7 @@
),
curr_w,
onChange ),
           new MenuAction.Choices("layout_2height", "Layout Height", //$NON-NLS-1$
mapify(
VALUE_WRAP_CONTENT, "Wrap Content",
canMatchParent ? VALUE_MATCH_PARENT : VALUE_FILL_PARENT,
//Synthetic comment -- @@ -251,7 +319,7 @@
),
curr_h,
onChange ),
           new MenuAction.Group("properties", "Properties") //$NON-NLS-1$
});

// Prepare a list of all simple properties.
//Synthetic comment -- @@ -295,6 +363,8 @@
}

props.put(id, new Prop(title, false, true, values));
}
}
mAttributesMap.put(key, props);
//Synthetic comment -- @@ -313,44 +383,50 @@
if (value != null)
value = value.toLowerCase();
if ("true".equals(value)) {         //$NON-NLS-1$
                    value = "1t";                   //$NON-NLS-1$
} else if ("false".equals(value)) { //$NON-NLS-1$
                    value = "2f";                   //$NON-NLS-1$
} else {
value = "4clr";                 //$NON-NLS-1$
}

a = new MenuAction.Choices(
                    "@prop@" + id,          //$NON-NLS-1$
p.getTitle(),
mapify(
                        "1t", "True",       //$NON-NLS-1$
                        "2f", "False",      //$NON-NLS-1$
"3sep", MenuAction.Choices.SEPARATOR, //$NON-NLS-1$
"4clr", "Default"), //$NON-NLS-1$
value,
                    "properties",           //$NON-NLS-1$
onChange);
            } else {
// Enum or flags. Their possible values are the multiple-choice
// items, with an extra "clear" option to remove everything.
String current = selectedNode.getStringAttr(ANDROID_URI, id);
if (current == null || current.length() == 0) {
                    current = "~2clr";   //$NON-NLS-1$
}
a = new MenuAction.Choices(
                    "@prop@" + id,       //$NON-NLS-1$
p.getTitle(),
concatenate(
p.getChoices(),
mapify(
                            "~1sep", MenuAction.Choices.SEPARATOR,  //$NON-NLS-1$
                            "~2clr", "Default"                      //$NON-NLS-1$
)
),
current,
                    "properties", //$NON-NLS-1$
onChange);
}
list2.add(a);
}
//Synthetic comment -- @@ -511,7 +587,11 @@
}

private boolean isFlag() {
            return mFlag;
}

private String getTitle() {
//Synthetic comment -- @@ -521,6 +601,10 @@
private Map<String, String> getChoices() {
return mChoices;
}
}

/**
//Synthetic comment -- @@ -544,4 +628,37 @@

public void onChildInserted(INode node, INode parent, InsertType insertType) {
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index da408fa..5077488 100644

//Synthetic comment -- @@ -126,4 +126,9 @@

public static String VALUE_VERTICAL = "vertical"; //$NON-NLS-1$

}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index e37b0ae..0b5a2d2 100644

//Synthetic comment -- @@ -320,6 +320,14 @@

boolean foundAction = false;
for (MenuAction action : viewActions) {
if (action.getId() == null || action.getTitle() == null) {
// TODO Log verbose error for invalid action.
continue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 1a0319e..24366da 100755

//Synthetic comment -- @@ -35,9 +35,12 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;

//Synthetic comment -- @@ -47,6 +50,7 @@
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import java.io.File;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -779,5 +783,38 @@

return -1;
}
}
}







