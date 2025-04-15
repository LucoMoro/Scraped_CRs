/*35098: ADT resource editor doesn't understand plurals

This CL adds code completion support for <plurals>
elements in resource files.

Change-Id:I96c4bdb4fc677437a7426d394f57e8429c322219*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java
//Synthetic comment -- index e50c541..f1ca3a4 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
//Synthetic comment -- @@ -45,6 +46,7 @@
public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
public static final String STRING_ARRAY_ELEMENT = "string-array";   //$NON-NLS-1$

public static final String ITEM_TAG = "item";  //$NON-NLS-1$
public static final String NAME_ATTR = "name"; //$NON-NLS-1$
//Synthetic comment -- @@ -255,6 +257,37 @@
},
false /* not mandatory */);

ElementDescriptor integer_array_element = new ElementDescriptor(
INTEGER_ARRAY_ELEMENT,
"Integer Array",
//Synthetic comment -- @@ -297,6 +330,7 @@
item_element,
string_array_element,
integer_array_element,
},
true /* mandatory */);
}







