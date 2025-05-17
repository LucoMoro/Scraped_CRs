//<Beginning of snippet n. 0>

import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;

public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
public static final String STRING_ARRAY_ELEMENT = "string-array"; //$NON-NLS-1$
public static final String PLURALS_ELEMENT = "plurals"; //$NON-NLS-1$

public static final String ITEM_TAG = "item"; //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
public static final String QUANTITY_ATTR = "quantity"; //$NON-NLS-1$

ElementDescriptor integer_array_element = new ElementDescriptor(
        INTEGER_ARRAY_ELEMENT,
        "Integer Array",
        item_element,
        string_array_element,
        integer_array_element,
        true /* mandatory */
);

ElementDescriptor plurals_element = new ElementDescriptor(
        PLURALS_ELEMENT,
        "Plurals",
        createPluralsItemDescriptor(),
        null,
        null,
        true /* mandatory */
);

private static AttributeDescriptor createPluralsItemDescriptor() {
    ListAttributeDescriptor listDescriptor = new ListAttributeDescriptor(ITEM_TAG, true);
    listDescriptor.addAttribute(new AttributeDescriptor(NAME_ATTR, true));
    listDescriptor.addAttribute(new AttributeDescriptor(QUANTITY_ATTR, true, validateQuantity()));
    return listDescriptor;
}

private static ValidationFunction validateQuantity() {
    return new ValidationFunction() {
        @Override
        public boolean validate(String quantity) {
            if (quantity == null || quantity.isEmpty()) return false; // Handle missing values
            try {
                int qty = Integer.parseInt(quantity);
                return qty >= 0; // Quantity must be non-negative
            } catch (NumberFormatException e) {
                return false; // Not a valid number
            }
        }
    };
}

//<End of snippet n. 0>