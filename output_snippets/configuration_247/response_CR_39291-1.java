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
public static final String PLURALS_ELEMENT = "plurals";  //$NON-NLS-1$
public static final String PLURAL_ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
public static final String QUANTITY_ATTR = "quantity"; //$NON-NLS-1$

ElementDescriptor integer_array_element = new ElementDescriptor(
    INTEGER_ARRAY_ELEMENT,
    "Integer Array",
    item_element,
    string_array_element,
    integer_array_element,
    true /* mandatory */);

ElementDescriptor plurals_element = new ElementDescriptor(
    PLURALS_ELEMENT,
    "Plurals",
    PLURAL_ITEM_TAG,
    null,
    null,
    true /* mandatory */
);

AttributeDescriptor quantityAttribute = new FlagAttributeDescriptor(
    QUANTITY_ATTR,
    "Quantity",
    true /* mandatory */);

plurals_element.addAttribute(quantityAttribute);

public class PluralItemDescriptor extends ElementDescriptor {
    private String quantity;

    public PluralItemDescriptor(String quantity) {
        super(PLURAL_ITEM_TAG, "Plural Item", null, null, null, true);
        this.quantity = quantity;
    }

    @Override
    public boolean validate() {
        if (quantity == null || quantity.isEmpty()) {
            return false;
        }
        return quantity.matches("one|few|many");
    }
}

//<End of snippet n. 0>