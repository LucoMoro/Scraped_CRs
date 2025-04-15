/*Fix adt-tests (broken by last ADT GLE2 AttributeDescriptor change)

Change-Id:I7b494743c2b7e8c2bfd6a89cc031f7eb3989ea6c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index 72dbf81..b9a798a 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.mock.MockXmlNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.resources.Density;

//Synthetic comment -- @@ -39,6 +41,16 @@
private HashMap<String, String> button2Map;
private HashMap<String, String> textMap;

    private TextAttributeDescriptor createTextAttrDesc(String xmlName) {
        return new TextAttributeDescriptor(
                xmlName,    // xmlLocalName
                xmlName,    // uiName
                SdkConstants.NS_RESOURCES, // ns uri
                "",         // tooltip
                new AttributeInfo(xmlName, new Format[] { Format.STRING })
                );
    }

@Override
protected void setUp() throws Exception {
// set up some basic descriptors.
//Synthetic comment -- @@ -47,31 +59,30 @@
// Also add some dummy attributes.
ElementDescriptor buttonDescriptor = new ElementDescriptor("Button", "Button", "", "",
new AttributeDescriptor[] {
                        createTextAttrDesc("name"),
                        createTextAttrDesc("text"),
},
new ElementDescriptor[] {}, false);

ElementDescriptor textDescriptor = new ElementDescriptor("TextView", "TextView", "", "",
new AttributeDescriptor[] {
                        createTextAttrDesc("name"),
                        createTextAttrDesc("text"),
                    },
new ElementDescriptor[] {}, false);

ElementDescriptor linearDescriptor = new ElementDescriptor("LinearLayout", "Linear Layout",
"", "",
new AttributeDescriptor[] {
                        createTextAttrDesc("orientation"),
                    },
new ElementDescriptor[] { }, false);

ElementDescriptor relativeDescriptor = new ElementDescriptor("RelativeLayout",
"Relative Layout", "", "",
new AttributeDescriptor[] {
                        createTextAttrDesc("orientation"),
                    },
new ElementDescriptor[] { }, false);

ElementDescriptor[] a = new ElementDescriptor[] {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java
//Synthetic comment -- index f5c16e7..8894982 100644

//Synthetic comment -- @@ -17,8 +17,7 @@
package com.android.ide.eclipse.adt.internal.resources;


import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo.Format;
import com.android.ide.eclipse.tests.AdtTestData;

import org.w3c.dom.Document;







