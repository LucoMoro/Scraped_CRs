/*21296: Layout editor error when using numColumns attribute. DO NOT MERGE

Change-Id:Ib05a5ad79cad87ce3bbb9240187dd80a8d47691b*/




//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index ad6274d..f51000f 100644

//Synthetic comment -- @@ -801,6 +801,7 @@
public static final String VALUE_VERTICAL = "vertical";             //$NON-NLS-1$
public static final String VALUE_TRUE = "true";                     //$NON-NLS-1$
public static final String VALUE_EDITABLE = "editable";             //$NON-NLS-1$
    public static final String VALUE_AUTO_FIT = "auto_fit";             //$NON-NLS-1$


// Values: Resources








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 82172fc..94bd058 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import static com.android.SdkConstants.GRID_VIEW;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.SdkConstants.VALUE_AUTO_FIT;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -341,10 +342,16 @@
Element element = (Element) xmlNode;
String columns = element.getAttributeNS(ANDROID_URI, ATTR_NUM_COLUMNS);
int multiplier = 2;
                if (columns != null && columns.length() > 0 &&
                        !columns.equals(VALUE_AUTO_FIT)) {
                    try {
                        int c = Integer.parseInt(columns);
                        if (c >= 1 && c <= 10) {
                            multiplier = c;
                        }
                    } catch (NumberFormatException nufe) {
                        // some unexpected numColumns value: just stick with 2 columns for
                        // preview purposes
}
}
count *= multiplier;







