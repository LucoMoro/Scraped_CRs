/*17466: Strip Whitespace from Project Name to Activity

Also moved the capitalize() method from DescriptorsUtils
to the AdtUtils method with the other general string
utilities.

Change-Id:Iffdbd906c5a4ecbd89c2f037d95f007e44733513*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index bd40d1b..d73ed87 100644

//Synthetic comment -- @@ -19,8 +19,52 @@

/** Utility methods for ADT */
public class AdtUtils {
    /**
     * Returns true if the given string ends with the given suffix, using a
     * case-insensitive comparison.
     *
     * @param string the full string to be checked
     * @param suffix the suffix to be checked for
     * @return true if the string case-insensitively ends with the given suffix
     */
public static boolean endsWithIgnoreCase(String string, String suffix) {
return string.regionMatches(true /* ignoreCase */, string.length() - suffix.length(),
suffix, 0, suffix.length());
}

    /**
     * Strips the whitespace from the given string
     *
     * @param string the string to be cleaned up
     * @return the string, without whitespace
     */
    public static String stripWhitespace(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0, n = string.length(); i < n; i++) {
            char c = string.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Capitalizes the string, i.e. transforms the initial [a-z] into [A-Z].
     * Returns the string unmodified if the first character is not [a-z].
     *
     * @param str The string to capitalize.
     * @return The capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.length() < 1 || Character.isUpperCase(str.charAt(0))) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index c7bc5a1..62cd172 100644

//Synthetic comment -- @@ -352,24 +352,6 @@
}

/**
* Formats the javadoc tooltip to be usable in a tooltip.
*/
public static String formatTooltip(String javadoc) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java
//Synthetic comment -- index f093a9b..e80333d 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME;

import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -185,7 +186,7 @@
* - a "Menu" prefix, except for <menu> itself which is just "Menu".
*/
private String getStyleName(String xmlName) {
        String styleName = AdtUtils.capitalize(xmlName);

// This is NOT the UI Name but the expected internal style name
final String MENU_STYLE_BASE_NAME = "Menu"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/uimodel/UiItemElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/uimodel/UiItemElementNode.java
//Synthetic comment -- index 539881a..33316e4 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.resources.uimodel;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ItemElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -48,7 +48,7 @@
String type = elem.getAttribute(ResourcesDescriptors.TYPE_ATTR);
String name = elem.getAttribute(ResourcesDescriptors.NAME_ATTR);
if (type != null && name != null && type.length() > 0 && name.length() > 0) {
                type = AdtUtils.capitalize(type);
return String.format("%1$s (%2$s %3$s)", name, type, getDescriptor().getUiName());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index c02f773..6b6ade0 100644

//Synthetic comment -- @@ -22,9 +22,11 @@

package com.android.ide.eclipse.adt.internal.wizards.newproject;

import static com.android.ide.eclipse.adt.AdtUtils.capitalize;
import static com.android.ide.eclipse.adt.AdtUtils.stripWhitespace;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -906,7 +908,7 @@
mProjectNameModifiedByUser = true;

if (!mApplicationNameModifiedByUser) {
                String name = capitalize(mProjectNameField.getText());
try {
mInternalApplicationNameUpdate = true;
mApplicationNameField.setText(name);
//Synthetic comment -- @@ -915,10 +917,10 @@
}
}
if (!mActivityNameModifiedByUser) {
                String name = capitalize(mProjectNameField.getText());
try {
mInternalActivityNameUpdate = true;
                    mActivityNameField.setText(stripWhitespace(name) + ACTIVITY_NAME_SUFFIX);
} finally {
mInternalActivityNameUpdate = false;
}
//Synthetic comment -- @@ -938,10 +940,10 @@
if (!mInternalApplicationNameUpdate) {
mApplicationNameModifiedByUser = true;
if (!mActivityNameModifiedByUser) {
                   String name = capitalize(mApplicationNameField.getText());
try {
mInternalActivityNameUpdate = true;
                       mActivityNameField.setText(stripWhitespace(name) + ACTIVITY_NAME_SUFFIX);
} finally {
mInternalActivityNameUpdate = false;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 01fbe1b..2589841 100644

//Synthetic comment -- @@ -29,4 +29,20 @@
assertFalse(AdtUtils.endsWithIgnoreCase("foob", "foo"));
assertFalse(AdtUtils.endsWithIgnoreCase("foo", "fo"));
}

    public void testStripWhitespace() {
        assertEquals("foo", AdtUtils.stripWhitespace("foo"));
        assertEquals("foobar", AdtUtils.stripWhitespace("foo bar"));
        assertEquals("foobar", AdtUtils.stripWhitespace("  foo bar  \n\t"));
    }

    public void testCapitalize() {
        assertEquals("UPPER", AdtUtils.capitalize("UPPER"));
        assertEquals("Lower", AdtUtils.capitalize("lower"));
        assertEquals("Capital", AdtUtils.capitalize("Capital"));
        assertEquals("CamelCase", AdtUtils.capitalize("camelCase"));
        assertEquals("", AdtUtils.capitalize(""));
        assertSame("Foo", AdtUtils.capitalize("Foo"));
        assertNull(null, AdtUtils.capitalize(null));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtilsTest.java
//Synthetic comment -- index 580fbaa..3fa1cf9 100644

//Synthetic comment -- @@ -59,15 +59,6 @@
assertEquals("The t axis", DescriptorsUtils.prettyAttributeUiName("theTAxis"));
}

public void testFormatTooltip() {
assertEquals("", DescriptorsUtils.formatTooltip(""));








