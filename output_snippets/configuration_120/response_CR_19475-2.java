//<Beginning of snippet n. 0>
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.layoutlib.api.IXmlPullParser;
import org.kxml2.io.KXmlParser;

public void parseLayout(IXmlPullParser parser) throws Exception {
    int eventType = parser.getEventType();
    while (eventType != IXmlPullParser.END_DOCUMENT) {
        String name = parser.getName();
        if (eventType == IXmlPullParser.START_TAG) {
            updateLayoutAttribute(parser, "layout_width");
            updateLayoutAttribute(parser, "layout_height");
            updateLayoutAttribute(parser, "layout_margin");
            updateLayoutAttribute(parser, "layout_gravity");
        }
        eventType = parser.next();
    }
}

private void updateLayoutAttribute(IXmlPullParser parser, String attributeName) throws Exception {
    String attributeValue = parser.getAttributeValue(null, attributeName);
    if (attributeValue != null && attributeValue.equals("match_parent") && isAndroidVersionLowerThan(2.3)) {
        parser.setAttribute(null, attributeName, "fill_parent");
    } else if (attributeValue == null) {
        throw new Exception("Malformed or missing attribute: " + attributeName);
    }
}

private boolean isAndroidVersionLowerThan(double version) {
    return android.os.Build.VERSION.SDK_INT < version;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;

public Object addPaddingAndReturnValue(Object value) {
    return addPaddingToValue(value);
}

private Object addPaddingToValue(Object value) {
    return value; 
}
//<End of snippet n. 1>