/*ADT: always give fill_parent to layoutlib.

If a layout is created for Android 2.3 but then rendered in 1.5 it'll
fail because 1.5 doesn't know what match_parent is.

This change makes the parser given to layoutlib replace on the fly
match_parent with fill_parent.

Change-Id:I45a22dea98388f8f0c5673bdaa9cbbf9a88f5422*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 3f06968..ad7152e 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;

//Synthetic comment -- @@ -29,6 +30,11 @@
*/
public class ContextPullParser extends KXmlParser implements IXmlPullParser {

    private final static String ATTR_LAYOUT_WIDTH = "layout_width"; //$NON-NLS-1$
    private final static String ATTR_LAYOUT_HEIGHT = "layout_height"; //$NON-NLS-1$
    private final static String ATTR_VALUE_MATCH_PARENT = "match_parent"; //$NON-NLS-1$
    private final static String ATTR_VALUE_FILL_PARENT = "fill_parent"; //$NON-NLS-1$

private final String mName;
private final IXmlPullParser mEmbeddedParser;

//Synthetic comment -- @@ -51,4 +57,21 @@
public Object getViewKey() {
return null; // never any key to return
}

    // --- KXMLParser override

    @Override
    public String getAttributeValue(String namespace, String localName) {
        String value = super.getAttributeValue(namespace, localName);

        // on the fly convert match_parent to fill_parent for compatibility with older
        // platforms.
        if ((ATTR_LAYOUT_WIDTH.equals(localName) || ATTR_LAYOUT_HEIGHT.equals(localName)) &&
                SdkConstants.NS_RESOURCES.equals(namespace) &&
                ATTR_VALUE_MATCH_PARENT.equals(value)) {
            return ATTR_VALUE_FILL_PARENT;
        }

        return value;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index a7cdf35..c7d5bf0 100644

//Synthetic comment -- @@ -50,6 +50,10 @@
*/
public final class UiElementPullParser extends BasePullParser {
private final static String ATTR_PADDING = "padding"; //$NON-NLS-1$
    private final static String ATTR_LAYOUT_WIDTH = "layout_width"; //$NON-NLS-1$
    private final static String ATTR_LAYOUT_HEIGHT = "layout_height"; //$NON-NLS-1$
    private final static String ATTR_VALUE_MATCH_PARENT = "match_parent"; //$NON-NLS-1$
    private final static String ATTR_VALUE_FILL_PARENT = "fill_parent"; //$NON-NLS-1$
private final static Pattern FLOAT_PATTERN = Pattern.compile("(-?[0-9]+(?:\\.[0-9]+)?)(.*)"); //$NON-NLS-1$

private final int[] sIntOut = new int[1];
//Synthetic comment -- @@ -344,6 +348,15 @@
// add the padding and return the value
return addPaddingToValue(value);
}

                // on the fly convert match_parent to fill_parent for compatibility with older
                // platforms.
                if ((ATTR_LAYOUT_WIDTH.equals(localName) || ATTR_LAYOUT_HEIGHT.equals(localName)) &&
                        SdkConstants.NS_RESOURCES.equals(namespace) &&
                        ATTR_VALUE_MATCH_PARENT.equals(value)) {
                    return ATTR_VALUE_FILL_PARENT;
                }

return value;
}
}







