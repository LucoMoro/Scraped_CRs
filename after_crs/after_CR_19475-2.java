/*ADT: always give fill_parent to layoutlib.

If a layout is created for Android 2.3 but then rendered in 1.5 it'll
fail because 1.5 doesn't know what match_parent is.

This change makes the parser given to layoutlib replace on the fly
match_parent with fill_parent.

Change-Id:I45a22dea98388f8f0c5673bdaa9cbbf9a88f5422*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 3f06968..3a94fd6 100644

//Synthetic comment -- @@ -16,7 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;

//Synthetic comment -- @@ -51,4 +57,22 @@
public Object getViewKey() {
return null; // never any key to return
}

    // --- KXMLParser override

    @Override
    public String getAttributeValue(String namespace, String localName) {
        String value = super.getAttributeValue(namespace, localName);

        // on the fly convert match_parent to fill_parent for compatibility with older
        // platforms.
        if (VALUE_MATCH_PARENT.equals(value) &&
                (ATTR_LAYOUT_WIDTH.equals(localName) ||
                        ATTR_LAYOUT_HEIGHT.equals(localName)) &&
                SdkConstants.NS_RESOURCES.equals(namespace)) {
            return VALUE_FILL_PARENT;
        }

        return value;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index a7cdf35..d4b6e94 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;


import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -344,6 +350,16 @@
// add the padding and return the value
return addPaddingToValue(value);
}

                // on the fly convert match_parent to fill_parent for compatibility with older
                // platforms.
                if (VALUE_MATCH_PARENT.equals(value) &&
                        (ATTR_LAYOUT_WIDTH.equals(localName) ||
                                ATTR_LAYOUT_HEIGHT.equals(localName)) &&
                        SdkConstants.NS_RESOURCES.equals(namespace)) {
                    return VALUE_FILL_PARENT;
                }

return value;
}
}







