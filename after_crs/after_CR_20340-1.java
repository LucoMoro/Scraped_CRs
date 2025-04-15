/*Changes to widget text and id format

This changeset changes the default text and id attributes of newly
dropped widgets in the following ways:

1. Use the node class name rather than the id as the initial text.  In
   other words, the default label for a button is "Button" rather than
   "@+id/Button01".  This is a more common convention in other GUI
   builders, and you typically don't want the actual label to be
   "@+id"-something, you want it to be "@string"-something, so the @id
   prefix is slightly confusing for beginners.

2. The id uses method name capitalization rather than class name
   capitalization, e.g. "checkBox" rather than "CheckBox". This seems
   to be the convention I see in handwritten layouts, which makes
   sense given that the reference will be compiled to and referenced
   as a Java field.

3. In the case of conflicts, don't use a leading 0; in other words,
   rather than button01, button02, button03 we have button1, button2,
   button3. It's unlikely that the user will have ten or more unnamed
   widgets (and if they do the leading zero still isn't needed.)  The
   code which looks for name conflicts now also performs case
   insensitive comparisons.

Change-Id:Ie7f2c5dd8e9852acec2c2e154ee20142b8ece9a6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 63394ad..0a6593d 100644

//Synthetic comment -- @@ -702,10 +702,11 @@
false /* override */);
}

        String widget_type = ui_node.getDescriptor().getUiName();
ui_node.setAttributeValue(
ATTR_TEXT,
SdkConstants.NS_RESOURCES,
                widget_type,
false /*override*/);

if (updateLayout) {
//Synthetic comment -- @@ -731,7 +732,7 @@

/**
* Given a UI node, returns the first available id that matches the
     * pattern "prefix%d".
* <p/>TabWidget is a special case and the method will always return "@android:id/tabs".
*
* @param uiNode The UI node that gives the prefix to match.
//Synthetic comment -- @@ -745,7 +746,7 @@

/**
* Given a UI root node and a potential XML node name, returns the first available
     * id that matches the pattern "prefix%d".
* <p/>TabWidget is a special case and the method will always return "@android:id/tabs".
*
* @param uiRoot The root UI node to search for name conflicts from
//Synthetic comment -- @@ -764,7 +765,7 @@

/**
* Given a UI root node, returns the first available id that matches the
     * pattern "prefix%d".
*
* For recursion purposes, a "context" is given. Since Java doesn't have in-out parameters
* in methods and we're not going to do a dedicated type, we just use an object array which
//Synthetic comment -- @@ -807,12 +808,15 @@
prefix = prefix.replaceAll("[^a-zA-Z]", "");                //$NON-NLS-1$ $NON-NLS-2$
if (prefix.length() == 0) {
prefix = DEFAULT_WIDGET_PREFIX;
            } else {
                // Lowercase initial character
                prefix = Character.toLowerCase(prefix.charAt(0)) + prefix.substring(1);
}

do {
num++;
                generated = String.format("%1$s%2$d", prefix, num);   //$NON-NLS-1$
            } while (map.contains(generated.toLowerCase()));

params[0] = prefix;
params[1] = num;
//Synthetic comment -- @@ -823,12 +827,12 @@
if (id != null) {
id = id.replace(NEW_ID_PREFIX, "");                            //$NON-NLS-1$
id = id.replace(ID_PREFIX, "");                                //$NON-NLS-1$
            if (map.add(id.toLowerCase()) && map.contains(generated.toLowerCase())) {

do {
num++;
                    generated = String.format("%1$s%2$d", prefix, num);   //$NON-NLS-1$
                } while (map.contains(generated.toLowerCase()));

params[1] = num;
params[2] = generated;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index e5e3a13..f3c62ec 100755

//Synthetic comment -- @@ -30,7 +30,6 @@
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
//Synthetic comment -- @@ -723,9 +722,7 @@

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts
            element.setAttributeNS(SdkConstants.NS_RESOURCES, ATTR_TEXT, desc.getUiName());

document.appendChild(element);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index c554310..36cfc80 100644

//Synthetic comment -- @@ -285,7 +285,7 @@
if (attr != null) {
// Don't append the two when it's a repeat, e.g. Button01 (Button),
// only when the ui name is not part of the attribute
            if (attr.toLowerCase().indexOf(uiName.toLowerCase()) == -1) {
styledString.append(attr);
styledString.append(String.format(" (%1$s)", uiName),
StyledString.DECORATIONS_STYLER);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java
//Synthetic comment -- index 4fa143e..5ccb494 100644

//Synthetic comment -- @@ -132,8 +132,8 @@
UiDocumentNode model = new UiDocumentNode(documentDescriptor);
UiElementNode uiRoot = model.getUiRoot();

        assertEquals("@+id/button1", DescriptorsUtils.getFreeWidgetId(uiRoot, "Button"));
        assertEquals("@+id/linearLayout1",
DescriptorsUtils.getFreeWidgetId(uiRoot, "LinearLayout"));
}








