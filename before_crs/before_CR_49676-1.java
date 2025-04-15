/*39612: Question Mark causes Eclipse Graphical Layout Editor to Freak Out

Handle string values starting with ? and @ even if they do not correspond
to actual theme or resource URLs.

Also fix the code which handles processing strings read from XML files;
apply unescaping rules (for unicode, newlines and tabs, removing quotes,
etc).

Also make the style warning include the full resource URI (it was only
logging the stripped URI).

Change-Id:I9b9a87ac4841faeacd1d94a43fa091702e60f4d8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 62821ae..c77c853 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.google.common.collect.Maps;

//Synthetic comment -- @@ -158,10 +158,8 @@
return VALUE_FILL_PARENT;
}

        // Handle unicode escapes
        if (value != null && value.indexOf('\\') != -1) {
            value = AdtUtils.replaceUnicodeEscapes(value);
        }

return value;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index c89a81b..e8e0d79 100644

//Synthetic comment -- @@ -36,7 +36,7 @@

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.FragmentMenu;
//Synthetic comment -- @@ -398,10 +398,8 @@
return VALUE_FILL_PARENT;
}

                // Handle unicode escapes
                if (value.indexOf('\\') != -1) {
                    value = AdtUtils.replaceUnicodeEscapes(value);
                }

return value;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index e9d386e..7d0f926 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.LT_ENTITY;
import static com.android.SdkConstants.QUOT_ENTITY;
import static com.android.SdkConstants.STRING_PREFIX;

import com.android.SdkConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -1218,7 +1217,7 @@
IStructuredModel smodel = null;

// Single and double quotes must be escaped in the <string>value</string> declaration
        tokenString = escapeString(tokenString);

try {
IStructuredDocument sdoc = null;
//Synthetic comment -- @@ -1450,79 +1449,6 @@
}

/**
     * Escape a string value to be placed in a string resource file such that it complies with
     * the escaping rules described here:
     *   http://developer.android.com/guide/topics/resources/string-resource.html
     * More examples of the escaping rules can be found here:
     *   http://androidcookbook.com/Recipe.seam?recipeId=2219&recipeFrom=ViewTOC
     * This method assumes that the String is not escaped already.
     *
     * Rules:
     * <ul>
     * <li>Double quotes are needed if string starts or ends with at least one space.
     * <li>{@code @, ?} at beginning of string have to be escaped with a backslash.
     * <li>{@code ', ", \} have to be escaped with a backslash.
     * <li>{@code <, >, &} have to be replaced by their predefined xml entity.
     * <li>{@code \n, \t} have to be replaced by a backslash and the appropriate character.
     * </ul>
     * @param s the string to be escaped
     * @return the escaped string as it would appear in the XML text in a values file
     */
    public static String escapeString(String s) {
        int n = s.length();
        if (n == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(s.length() * 2);
        boolean hasSpace = s.charAt(0) == ' ' || s.charAt(n - 1) == ' ';

        if (hasSpace) {
            sb.append('"');
        } else if (s.charAt(0) == '@' || s.charAt(0) == '?') {
            sb.append('\\');
        }

        for (int i = 0; i < n; ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '\'':
                    if (!hasSpace) {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '<':
                    sb.append(LT_ENTITY);
                    break;
                case '&':
                    sb.append(AMP_ENTITY);
                    break;
                case '\n':
                    sb.append("\\n"); //$NON-NLS-1$
                    break;
                case '\t':
                    sb.append("\\t"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        if (hasSpace) {
            sb.append('"');
        }

        return sb.toString();
    }

    /**
* Computes the changes to be made to the source Android XML file and
* returns a list of {@link Change}.
* <p/>








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index 245d84e..19a7101 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -34,7 +35,6 @@
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
//Synthetic comment -- @@ -1058,7 +1058,7 @@
String value = strings.get(key);

// Escape values if necessary
                value = ExtractStringRefactoring.escapeString(value);

// place them in the template
String stringDef = stringTemplate.replace(PARAM_STRING_NAME, key);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/FmEscapeXmlStringMethod.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/FmEscapeXmlStringMethod.java
//Synthetic comment -- index 7e5866e..ffcfa3e 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.templates;

import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
//Synthetic comment -- @@ -38,6 +38,6 @@
throw new TemplateModelException("Wrong arguments");
}
String string = args.get(0).toString();
        return new SimpleScalar(ExtractStringRefactoring.escapeString(string));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoringTest.java
deleted file mode 100644
//Synthetic comment -- index f7d1d4b..0000000

//Synthetic comment -- @@ -1,57 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import junit.framework.TestCase;

public class ExtractStringRefactoringTest extends TestCase {

    public void testEscapeStringShouldEscapeXmlSpecialCharacters() throws Exception {
        assertEquals("&lt;", escape("<")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("&amp;", escape("&")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testEscapeStringShouldEscapeQuotes() throws Exception {
        assertEquals("\\'", escape("'")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\\\"", escape("\"")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\" ' \"", escape(" ' ")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testEscapeStringShouldPreserveWhitespace() throws Exception {
        assertEquals("\"at end  \"", escape("at end  ")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\"  at begin\"", escape("  at begin")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testEscapeStringShouldEscapeAtSignAndQuestionMarkOnlyAtBeginning()
            throws Exception {
        assertEquals("\\@text", escape("@text")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("a@text", escape("a@text")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\\?text", escape("?text")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("a?text", escape("a?text")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\" ?text\"", escape(" ?text")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testEscapeStringShouldEscapeJavaEscapeSequences() throws Exception {
        assertEquals("\\n", escape("\n")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\\t", escape("\t")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("\\\\", escape("\\")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String escape(String unescaped) {
        return ExtractStringRefactoring.escapeString(unescaped);
    }
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java b/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 756ea53..219c93f 100644

//Synthetic comment -- @@ -189,7 +189,8 @@
if (reference == null) {
return null;
}
        if (reference.startsWith(PREFIX_THEME_REF)) {
// no theme? no need to go further!
if (mTheme == null) {
return null;
//Synthetic comment -- @@ -198,6 +199,7 @@
boolean frameworkOnly = false;

// eliminate the prefix from the string
if (reference.startsWith(ANDROID_THEME_PREFIX)) {
frameworkOnly = true;
reference = reference.substring(ANDROID_THEME_PREFIX.length());
//Synthetic comment -- @@ -207,7 +209,7 @@

// at this point, value can contain type/name (drawable/foo for instance).
// split it to make sure.
            String[] segments = reference.split("\\/");

// we look for the referenced item name.
String referenceName = null;
//Synthetic comment -- @@ -224,6 +226,18 @@
} else {
// it's just an item name.
referenceName = segments[0];
}

// now we look for android: in the referenceName in order to support format
//Synthetic comment -- @@ -241,7 +255,7 @@
mLogger.warning(LayoutLog.TAG_RESOURCES_RESOLVE_THEME_ATTR,
String.format("Couldn't find theme resource %1$s for the current theme",
reference),
                        new ResourceValue(ResourceType.ATTR, referenceName, frameworkOnly));
}

return item;
//Synthetic comment -- @@ -262,16 +276,17 @@
}

// at this point, value contains type/[android:]name (drawable/foo for instance)
            String[] segments = reference.split("\\/");
            if (segments.length <= 1) {
return null;
}

// now we look for android: in the resource name in order to support format
// such as: @drawable/android:name
            if (segments[1].startsWith(PREFIX_ANDROID)) {
frameworkOnly = true;
                segments[1] = segments[1].substring(PREFIX_ANDROID.length());
}

ResourceType type = ResourceType.getEnum(segments[0]);
//Synthetic comment -- @@ -281,7 +296,19 @@
return null;
}

            return findResValue(type, segments[1],
forceFrameworkOnly ? true :frameworkOnly);
}









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java b/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index aabfd35..aa1f4b8 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.AttrResourceValue;
import com.android.ide.common.rendering.api.DeclareStyleableResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -64,7 +69,7 @@
@Override
public void endElement(String uri, String localName, String qName) throws SAXException {
if (mCurrentValue != null) {
            mCurrentValue.setValue(trimXmlWhitespaces(mCurrentValue.getValue()));
}

if (inResources && qName.equals(NODE_RESOURCES)) {
//Synthetic comment -- @@ -213,96 +218,263 @@
}
}

    public static String trimXmlWhitespaces(String value) {
        if (value == null) {
return null;
}

        // look for carriage return and replace all whitespace around it by just 1 space.
        int index;

        while ((index = value.indexOf('\n')) != -1) {
            // look for whitespace on each side
            int left = index - 1;
            while (left >= 0) {
                if (Character.isWhitespace(value.charAt(left))) {
                    left--;
                } else {
break;
}
}

            int right = index + 1;
            int count = value.length();
            while (right < count) {
                if (Character.isWhitespace(value.charAt(right))) {
                    right++;
                } else {
break;
}
}

            // remove all between left and right (non inclusive) and replace by a single space.
            String leftString = null;
            if (left >= 0) {
                leftString = value.substring(0, left + 1);
}
            String rightString = null;
            if (right < count) {
                rightString = value.substring(right);
}

            if (leftString != null) {
                value = leftString;
                if (rightString != null) {
                    value += " " + rightString;
}
} else {
                value = rightString != null ? rightString : "";
            }
        }

        // now we un-escape the string
        int length = value.length();
        char[] buffer = value.toCharArray();

        for (int i = 0 ; i < length ; i++) {
            if (buffer[i] == '\\' && i + 1 < length) {
                if (buffer[i+1] == 'u') {
                    if (i + 5 < length) {
                        // this is unicode char \u1234
                        int unicodeChar = Integer.parseInt(new String(buffer, i+2, 4), 16);

                        // put the unicode char at the location of the \
                        buffer[i] = (char)unicodeChar;

                        // offset the rest of the buffer since we go from 6 to 1 char
                        if (i + 6 < buffer.length) {
                            System.arraycopy(buffer, i+6, buffer, i+1, length - i - 6);
                        }
                        length -= 5;
}
                } else {
                    if (buffer[i+1] == 'n') {
                        // replace the 'n' char with \n
                        buffer[i+1] = '\n';
                    }

                    // offset the buffer to erase the \
                    System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
                    length--;
}
            } else if (buffer[i] == '"') {
                // if the " was escaped it would have been processed above.
                // offset the buffer to erase the "
                System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
                length--;

                // unlike when unescaping, we want to process the next char too
                i--;
}
}

        return new String(buffer, 0, length);
}
}








//Synthetic comment -- diff --git a/sdk_common/tests/src/com/android/ide/common/resources/ValueResourceParserTest.java b/sdk_common/tests/src/com/android/ide/common/resources/ValueResourceParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..aed6060

//Synthetic comment -- @@ -0,0 +1,148 @@







