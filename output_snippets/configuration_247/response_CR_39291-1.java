//<Beginning of snippet n. 0>


import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;

public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
public static final String STRING_ARRAY_ELEMENT = "string-array";   //$NON-NLS-1$
public static final String PLURAL_ELEMENT = "plurals";  //$NON-NLS-1$
public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$

ElementDescriptor item_element = new ElementDescriptor(ITEM_TAG, "Item", null, null, null, false);
ElementDescriptor integer_array_element = new ElementDescriptor(
    INTEGER_ARRAY_ELEMENT,
    "Integer Array",
    item_element,
    null,
    null,
    true /* mandatory */);

ElementDescriptor plural_element = new ElementDescriptor(
    PLURAL_ELEMENT,
    "Plural",
    item_element,
    null,
    null,
    true /* mandatory */);

ElementDescriptor[] plural_items = new ElementDescriptor[] {
    new ElementDescriptor(ITEM_TAG, "Plural Item", null, null, null, true /* mandatory */)
};

//<End of snippet n. 0>