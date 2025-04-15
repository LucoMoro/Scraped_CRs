/*Fix attribute validation

This is used when incrementally validating XML attributes
when a file is saved in the IDE.

Change-Id:I37d37ab8ac64aec72455c3bfbabc7bbd65595a94*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index be928b0..e246975b 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.SdkConstants.ANDROID_THEME_PREFIX;
import static com.android.SdkConstants.ID_PREFIX;
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.VALUE_FALSE;
import static com.android.SdkConstants.VALUE_TRUE;
import static com.android.ide.common.api.IAttributeInfo.Format.BOOLEAN;
//Synthetic comment -- @@ -211,6 +212,11 @@

// All other formats require a nonempty string
if (value.isEmpty()) {
return false;
}
char first = value.charAt(0);
//Synthetic comment -- @@ -262,6 +268,14 @@
//String name = url.substring(nameBegin);
return true;
}
}
}

//Synthetic comment -- @@ -290,7 +304,7 @@
}

if (mFormats.contains(BOOLEAN)) {
            if (value.equals(VALUE_TRUE) || value.equals(VALUE_FALSE)) {
return true;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java
//Synthetic comment -- index cde12e5..b562922 100644

//Synthetic comment -- @@ -35,6 +35,8 @@
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -47,8 +49,6 @@
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class AttributeInfoTest extends TestCase {
public void testSimple() throws Exception {
//Synthetic comment -- @@ -70,7 +70,10 @@
assertTrue(info.isValid("true", null, null));
assertTrue(info.isValid("false", null, null));
assertFalse(info.isValid("", null, null));
        assertFalse(info.isValid("TRUE", null, null));
}

public void testIsValidInteger() throws Exception {
//Synthetic comment -- @@ -160,6 +163,7 @@
assertTrue(info.isValid("@animator/foo", null, null));
assertTrue(info.isValid("@anim/foo", null, null));
assertTrue(info.isValid("?android:attr/textAppearanceMedium", null, null));

assertFalse(info.isValid("", null, null));
assertFalse(info.isValid("foo", null, null));
//Synthetic comment -- @@ -182,8 +186,8 @@
assertTrue(info.isValid("left", null, null));
assertTrue(info.isValid("top", null, null));
assertTrue(info.isValid("left|top", null, null));

        assertFalse(info.isValid("", null, null));
assertFalse(info.isValid("other", null, null));
assertFalse(info.isValid("50", null, null));
}







