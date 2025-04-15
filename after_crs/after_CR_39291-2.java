/*35098: ADT resource editor doesn't understand plurals

This CL adds code completion support for <plurals>
elements in resource files.

Change-Id:I96c4bdb4fc677437a7426d394f57e8429c322219*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java
//Synthetic comment -- index e50c541..8f4d5c8 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.EnumAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
//Synthetic comment -- @@ -45,6 +46,7 @@
public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
public static final String STRING_ARRAY_ELEMENT = "string-array";   //$NON-NLS-1$
    public static final String PLURALS_ELEMENT = "plurals";             //$NON-NLS-1$

public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
//Synthetic comment -- @@ -255,6 +257,42 @@
},
false /* not mandatory */);

         ElementDescriptor plurals_element = new ElementDescriptor(
                 PLURALS_ELEMENT,
                 "Quantity Strings (Plurals)",
                 "A quantity string",
                 null, // tooltips
                 new AttributeDescriptor[] {
                         new TextAttributeDescriptor(NAME_ATTR,
                                 null /* nsUri */,
                                 nameAttrInfo)
                         .setTooltip("A name for the pair of strings. This name will be used as the resource ID."),
                 },
                 new ElementDescriptor[] {
                     new ElementDescriptor(
                         ITEM_TAG,
                         "Item",
                         "A plural or singular string",
                         null, // tooltip
                         new AttributeDescriptor[] {
                             new EnumAttributeDescriptor(
                                 "quantity", "Quantity", null,
                                 "A keyword value indicating when this string should be used",
                                 new AttributeInfo("quantity", Format.ENUM_SET)
                                 .setEnumValues(new String[] {
                                         "zero", //$NON-NLS-1$
                                         "one",  //$NON-NLS-1$
                                         "two",  //$NON-NLS-1$
                                         "few",  //$NON-NLS-1$
                                         "many", //$NON-NLS-1$
                                         "other" //$NON-NLS-1$
                                 }))
                         },
                         null,  // no child nodes
                         false /* not mandatory */)
                 },
                 false /* not mandatory */);

ElementDescriptor integer_array_element = new ElementDescriptor(
INTEGER_ARRAY_ELEMENT,
"Integer Array",
//Synthetic comment -- @@ -297,6 +335,7 @@
item_element,
string_array_element,
integer_array_element,
                                plurals_element,
},
true /* mandatory */);
}







