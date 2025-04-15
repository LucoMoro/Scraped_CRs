/*Merge 756d19df from master to r12. do not merge.

Handle "ffffffff" in attribute flag values

Change-Id:I1ff6dbf959e2c1ff1b475c5cdcebcd0c73975c01*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 1e14f9f..f8d041c 100644

//Synthetic comment -- @@ -30,8 +30,8 @@
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -532,9 +532,8 @@
} else {
String value = valueNode.getNodeValue();
try {
                            // Integer.decode cannot handle "ffffffff", see JDK issue 6624867
                            int i = (int) (long) Long.decode(value);

Map<String, Integer> map = mEnumFlagValues.get(attrName);
if (map == null) {







