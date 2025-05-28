
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

public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
},
false /* not mandatory */);

ElementDescriptor integer_array_element = new ElementDescriptor(
INTEGER_ARRAY_ELEMENT,
"Integer Array",
item_element,
string_array_element,
integer_array_element,
},
true /* mandatory */);
}

//<End of snippet n. 0>








