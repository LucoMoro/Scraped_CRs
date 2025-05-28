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
public static final String PLURALS_ELEMENT = "plurals"; //$NON-NLS-1$

public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
public static final String QUANTITY_ATTR = "quantity"; //$NON-NLS-1$

public static final String QUANTITY_ONE = "one"; //$NON-NLS-1$
public static final String QUANTITY_FEW = "few"; //$NON-NLS-1$
public static final String QUANTITY_MANY = "many"; //$NON-NLS-1$

ElementDescriptor integer_array_element = new ElementDescriptor(
    INTEGER_ARRAY_ELEMENT,
    "Integer Array",
    item_element,
    string_array_element,
    integer_array_element,
    true /* mandatory */);

// New PluralsDescriptor class
class PluralsDescriptor extends ElementDescriptor {
    public PluralsDescriptor() {
        super(PLURALS_ELEMENT,
              "Plurals",
              ITEM_TAG,
              null,
              null,
              true /* mandatory */);
    }
    
    @Override
    public boolean validateAttributes(AttributeInfo[] attrs) {
        for (AttributeInfo attr : attrs) {
            if (attr.getName().equals(NAME_ATTR) || attr.getName().equals(QUANTITY_ATTR)) {
                if (!isValidQuantity(attr.getValue())) {
                    return false; // Invalid quantity value
                }
                continue;
            }
            return false; // Unrecognized attribute
        }
        return true; // All attributes are valid
    }

    private boolean isValidQuantity(String quantity) {
        return QUANTITY_ONE.equals(quantity) || QUANTITY_FEW.equals(quantity) || QUANTITY_MANY.equals(quantity);
    }
    
    @Override
    public void parseItem(String item) {
        // Implement item parsing logic for plurals
        String[] parts = item.split(" ");
        String quantity = null;
        String name = null;
        for (String part : parts) {
            if (part.startsWith(QUANTITY_ATTR + "=")) {
                quantity = part.split("=")[1];
                if (!isValidQuantity(quantity)) {
                    throw new IllegalArgumentException("Invalid quantity attribute: " + quantity);
                }
            } else if (part.startsWith(NAME_ATTR + "=")) {
                name = part.split("=")[1];
            }
        }
        // Additional logic to handle parsed name and quantity
    }
}

ElementDescriptor plurals_element = new PluralsDescriptor();

//<End of snippet n. 0>