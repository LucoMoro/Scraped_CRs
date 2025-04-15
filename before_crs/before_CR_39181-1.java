/*Make the flag property editor editable

For "enum" properties (such as layout_width) we were using the
WindowBuilder combobox to let the user pick which of the enumeration
values to use. However, the enum values in Android often allow not
just the enumeration values, but other values too; for example, while
layout_width's enum values are match_parent and wrap_content, you can
also specify 42dp.

This changeset changes the property editor to use a similar property
editor to the one used for flag values; a free-form text field along
with field completion offering the available enum values. (We can't
simply just modify the property editor for enums to have an editable
combobox, since that property editor is a custom SWT component which
doesn't support editing.)

Change-Id:I1bff568233b4727d80ffbe7c17742177c6596381*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/EnumValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/EnumValueCompleter.java
new file mode 100644
//Synthetic comment -- index 0000000..4746a72

//Synthetic comment -- @@ -0,0 +1,56 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagValueCompleter.java
//Synthetic comment -- index d3707a5..6958e0f 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import java.util.ArrayList;
import java.util.List;

/** Resource value completion for the given property */
class FlagValueCompleter implements IContentProposalProvider {
protected final XmlProperty mProperty;
private String[] mValues;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java
//Synthetic comment -- index e160079..68a4c07 100644

//Synthetic comment -- @@ -182,7 +182,14 @@
if (formats.contains(Format.BOOLEAN)) {
editor = BooleanXmlPropertyEditor.INSTANCE;
} else if (formats.contains(Format.ENUM)) {
                    editor = EnumXmlPropertyEditor.INSTANCE;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java
//Synthetic comment -- index 6591575..d3985a9 100644

//Synthetic comment -- @@ -141,7 +141,7 @@
if (formats.contains(Format.FLAG)) {
return adapter.cast(new FlagValueCompleter(this, info.getFlagValues()));
} else if (formats.contains(Format.ENUM)) {
                    return adapter.cast(new FlagValueCompleter(this, info.getEnumValues()));
}
}
// Fallback: complete values on resource values
//Synthetic comment -- @@ -202,7 +202,11 @@

Object viewObject = getFactory().getCurrentViewObject();
if (viewObject != null) {
            ViewHierarchy views = getGraphicalEditor().getCanvasControl().getViewHierarchy();
Map<String, String> defaultProperties = views.getDefaultProperties(viewObject);
if (defaultProperties != null) {
return defaultProperties.get(name);







