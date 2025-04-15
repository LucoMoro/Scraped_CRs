/*Add XML float formatting utility method

And fix incorrect assertion.

Change-Id:I2f0e9797b892fc7a184e9dd067c07e5a8066881a*/
//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/XmlUtils.java b/common/src/main/java/com/android/utils/XmlUtils.java
//Synthetic comment -- index 2c45055..b001d52 100644

//Synthetic comment -- @@ -15,6 +15,17 @@
*/
package com.android.utils;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -29,21 +40,11 @@

import java.io.StringReader;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.ANDROID_NS_NAME;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.APOS_ENTITY;
import static com.android.SdkConstants.APP_PREFIX;
import static com.android.SdkConstants.LT_ENTITY;
import static com.android.SdkConstants.QUOT_ENTITY;
import static com.android.SdkConstants.XMLNS;
import static com.android.SdkConstants.XMLNS_PREFIX;
import static com.android.SdkConstants.XMLNS_URI;

/** XML Utilities */
public class XmlUtils {
public static final String XML_COMMENT_BEGIN = "<!--"; //$NON-NLS-1$
//Synthetic comment -- @@ -411,4 +412,21 @@
"Unsupported node type " + nodeType + ": not yet implemented");
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/XmlUtilsTest.java b/common/src/test/java/com/android/utils/XmlUtilsTest.java
//Synthetic comment -- index 02fd465..b76d0b7 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.utils;

import com.android.SdkConstants;
import com.android.annotations.Nullable;

//Synthetic comment -- @@ -28,12 +30,11 @@
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.android.SdkConstants.XMLNS;

@SuppressWarnings("javadoc")
public class XmlUtilsTest extends TestCase {
public void testlookupNamespacePrefix() throws Exception {
//Synthetic comment -- @@ -271,4 +272,34 @@
builder = factory.newDocumentBuilder();
return builder.parse(new InputSource(new StringReader(xml)));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index eb54b6c..60a52fb 100644

//Synthetic comment -- @@ -623,7 +623,7 @@
}
}

            List<Detector> otherDetectors = mScopeDetectors.get(Scope.OTHER_SCOPE);
if (otherDetectors != null) {
for (Detector detector : otherDetectors) {
assert detector instanceof Detector.OtherFileScanner : detector;







