
//<Beginning of snippet n. 0>



package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;

import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;

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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;


import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
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

//<End of snippet n. 1>








