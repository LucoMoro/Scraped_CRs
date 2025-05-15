
//<Beginning of snippet n. 0>


import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.EnumAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
public static final String STRING_ARRAY_ELEMENT = "string-array";   //$NON-NLS-1$
    public static final String PLURALS_ELEMENT = "plurals";             //$NON-NLS-1$

public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
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
                                         "zero", "one", "two", "few", "many", "other"
                                 }))
                         },
                         null,  // no child nodes
                         false /* not mandatory */)
                 },
                 false /* not mandatory */);

ElementDescriptor integer_array_element = new ElementDescriptor(
INTEGER_ARRAY_ELEMENT,
"Integer Array",
item_element,
string_array_element,
integer_array_element,
                                plurals_element,
},
true /* mandatory */);
}

//<End of snippet n. 0>








