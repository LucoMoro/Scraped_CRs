/*39612: Question Mark causes Eclipse Graphical Layout Editor to Freak Out. DO NOT MERGE

Handle string values starting with ? and @ even if they do not correspond
to actual theme or resource URLs.

Also fix the code which handles processing strings read from XML files;
apply unescaping rules (for unicode, newlines and tabs, removing quotes,
etc).

Also make the style warning include the full resource URI (it was only
logging the stripped URI).

Change-Id:Id044d93cdfd44afced7e8c2b4da99b67a730b4be*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 62821ae..c77c853 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.resources.ValueResourceParser;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.google.common.collect.Maps;

//Synthetic comment -- @@ -158,10 +158,8 @@
return VALUE_FILL_PARENT;
}

        // Handle unicode escapes etc
        value = ValueResourceParser.unescapeResourceString(value, false, false);

return value;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index c89a81b..e8e0d79 100644

//Synthetic comment -- @@ -36,7 +36,7 @@

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.resources.ValueResourceParser;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.FragmentMenu;
//Synthetic comment -- @@ -398,10 +398,8 @@
return VALUE_FILL_PARENT;
}

                // Handle unicode escapes etc
                value = ValueResourceParser.unescapeResourceString(value, false, false);

return value;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index e9d386e..7d0f926 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import static com.android.SdkConstants.QUOT_ENTITY;
import static com.android.SdkConstants.STRING_PREFIX;

import com.android.SdkConstants;
import com.android.ide.common.resources.ValueResourceParser;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -1218,7 +1217,7 @@
IStructuredModel smodel = null;

// Single and double quotes must be escaped in the <string>value</string> declaration
        tokenString = ValueResourceParser.escapeResourceString(tokenString);

try {
IStructuredDocument sdoc = null;
//Synthetic comment -- @@ -1450,79 +1449,6 @@
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
import com.android.ide.common.resources.ValueResourceParser;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -34,7 +35,6 @@
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizardState.Mode;
//Synthetic comment -- @@ -1058,7 +1058,7 @@
String value = strings.get(key);

// Escape values if necessary
                value = ValueResourceParser.escapeResourceString(value);

// place them in the template
String stringDef = stringTemplate.replace(PARAM_STRING_NAME, key);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/FmEscapeXmlStringMethod.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/FmEscapeXmlStringMethod.java
//Synthetic comment -- index 7e5866e..ffcfa3e 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.templates;

import com.android.ide.common.resources.ValueResourceParser;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
//Synthetic comment -- @@ -38,6 +38,6 @@
throw new TemplateModelException("Wrong arguments");
}
String string = args.get(0).toString();
        return new SimpleScalar(ValueResourceParser.escapeResourceString(string));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoringTest.java
deleted file mode 100644
//Synthetic comment -- index f7d1d4b..0000000

//Synthetic comment -- @@ -1,57 +0,0 @@








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java b/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 756ea53..219c93f 100644

//Synthetic comment -- @@ -189,7 +189,8 @@
if (reference == null) {
return null;
}
        if (reference.startsWith(PREFIX_THEME_REF)
                && reference.length() > PREFIX_THEME_REF.length()) {
// no theme? no need to go further!
if (mTheme == null) {
return null;
//Synthetic comment -- @@ -198,6 +199,7 @@
boolean frameworkOnly = false;

// eliminate the prefix from the string
            String originalReference = reference;
if (reference.startsWith(ANDROID_THEME_PREFIX)) {
frameworkOnly = true;
reference = reference.substring(ANDROID_THEME_PREFIX.length());
//Synthetic comment -- @@ -207,7 +209,7 @@

// at this point, value can contain type/name (drawable/foo for instance).
// split it to make sure.
            String[] segments = reference.split("/");

// we look for the referenced item name.
String referenceName = null;
//Synthetic comment -- @@ -224,6 +226,18 @@
} else {
// it's just an item name.
referenceName = segments[0];

                // Make sure it looks like a resource name; if not, it could just be a string
                // which starts with a ?
                if (!Character.isJavaIdentifierStart(referenceName.charAt(0))) {
                    return null;
                }
                for (int i = 1, n = referenceName.length(); i < n; i++) {
                    char c = referenceName.charAt(i);
                    if (!Character.isJavaIdentifierPart(c) && c != '.') {
                        return null;
                    }
                }
}

// now we look for android: in the referenceName in order to support format
//Synthetic comment -- @@ -241,7 +255,7 @@
mLogger.warning(LayoutLog.TAG_RESOURCES_RESOLVE_THEME_ATTR,
String.format("Couldn't find theme resource %1$s for the current theme",
reference),
                        new ResourceValue(ResourceType.ATTR, originalReference, frameworkOnly));
}

return item;
//Synthetic comment -- @@ -262,16 +276,17 @@
}

// at this point, value contains type/[android:]name (drawable/foo for instance)
            String[] segments = reference.split("/");
            if (segments.length != 2) {
return null;
}

// now we look for android: in the resource name in order to support format
// such as: @drawable/android:name
            String referenceName = segments[1];
            if (referenceName.startsWith(PREFIX_ANDROID)) {
frameworkOnly = true;
                referenceName = referenceName.substring(PREFIX_ANDROID.length());
}

ResourceType type = ResourceType.getEnum(segments[0]);
//Synthetic comment -- @@ -281,7 +296,19 @@
return null;
}

            // Make sure it looks like a resource name; if not, it could just be a string
            // which starts with a ?
            if (!Character.isJavaIdentifierStart(referenceName.charAt(0))) {
                return null;
            }
            for (int i = 1, n = referenceName.length(); i < n; i++) {
                char c = referenceName.charAt(i);
                if (!Character.isJavaIdentifierPart(c) && c != '.') {
                    return null;
                }
            }

            return findResValue(type, referenceName,
forceFrameworkOnly ? true :frameworkOnly);
}









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java b/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index aabfd35..aa1f4b8 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.common.resources;

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.LT_ENTITY;

import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.ide.common.rendering.api.AttrResourceValue;
import com.android.ide.common.rendering.api.DeclareStyleableResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -64,7 +69,7 @@
@Override
public void endElement(String uri, String localName, String qName) throws SAXException {
if (mCurrentValue != null) {
            mCurrentValue.setValue(unescapeResourceString(mCurrentValue.getValue(), false, true));
}

if (inResources && qName.equals(NODE_RESOURCES)) {
//Synthetic comment -- @@ -213,96 +218,263 @@
}
}

    /**
     * Replaces escapes in an XML resource string with the actual characters,
     * performing unicode substitutions (replacing any {@code \\uNNNN} references in the
     * given string with the corresponding unicode characters), etc.
     *
     *
     *
     * @param s the string to unescape
     * @param escapeEntities XML entities
     * @param trim whether surrounding space and quotes should be trimmed
     * @return the string with the escape characters removed and expanded
     */
    @Nullable
    public static String unescapeResourceString(
            @Nullable String s,
            boolean escapeEntities, boolean trim) {
        if (s == null) {
return null;
}

        // Trim space surrounding optional quotes
        int i = 0;
        int n = s.length();
        if (trim) {
            while (i < n) {
                char c = s.charAt(i);
                if (!Character.isWhitespace(c)) {
break;
}
                i++;
}
            while (n > i) {
                char c = s.charAt(n - 1);
                if (!Character.isWhitespace(c)) {
                    //See if this was a \, and if so, see whether it was escaped
                    if (n < s.length() && isEscaped(s, n)) {
                        n++;
                    }
break;
}
                n--;
}

            // Trim surrounding quotes. Note that there can be *any* number of these, and
            // the left side and right side do not have to match; e.g. you can have
            //    """"f"" => f
            int quoteStart = i;
            int quoteEnd = n;
            while (i < n) {
                char c = s.charAt(i);
                if (c != '"') {
                    break;
                }
                i++;
}
            // Searching backwards is slightly more complicated; make sure we don't trim
            // quotes that have been escaped.
            while (n > i) {
                char c = s.charAt(n - 1);
                if (c != '"') {
                    if (n < s.length() && isEscaped(s, n)) {
                        n++;
                    }
                    break;
                }
                n--;
            }
            if (n == i) {
                return ""; //$NON-NLS-1$
}

            // Only trim leading spaces if we didn't already process a leading quote:
            if (i == quoteStart) {
                while (i < n) {
                    char c = s.charAt(i);
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                    i++;
                }
            }
            // Only trim trailing spaces if we didn't already process a trailing quote:
            if (n == quoteEnd) {
                while (n > i) {
                    char c = s.charAt(n - 1);
                    if (!Character.isWhitespace(c)) {
                        //See if this was a \, and if so, see whether it was escaped
                        if (n < s.length() && isEscaped(s, n)) {
                            n++;
                        }
                        break;
                    }
                    n--;
                }
            }
            if (n == i) {
                return ""; //$NON-NLS-1$
            }
        }

        // If no surrounding whitespace and no escape characters, no need to do any
        // more work
        if (i == 0 && n == s.length() && s.indexOf('\\') == -1
                && (!escapeEntities || s.indexOf('&') == -1)) {
            return s;
        }

        StringBuilder sb = new StringBuilder(n - i);
        for (; i < n; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i < n - 1) {
                char next = s.charAt(i + 1);
                // Unicode escapes
                if (next == 'u' && i < n - 5) { // case sensitive
                    String hex = s.substring(i + 2, i + 6);
                    try {
                        int unicodeValue = Integer.parseInt(hex, 16);
                        sb.append((char) unicodeValue);
                        i += 5;
                        continue;
                    } catch (NumberFormatException e) {
                        // Invalid escape: Just proceed to literally transcribe it
                        sb.append(c);
                    }
                } else if (next == 'n') {
                    sb.append('\n');
                    i++;
                } else if (next == 't') {
                    sb.append('\t');
                    i++;
                } else {
                    sb.append(next);
                    i++;
                    continue;
}
} else {
                if (c == '&' && escapeEntities) {
                    if (s.regionMatches(true, i, LT_ENTITY, 0, LT_ENTITY.length())) {
                        sb.append('<');
                        i += LT_ENTITY.length() - 1;
                        continue;
                    } else if (s.regionMatches(true, i, AMP_ENTITY, 0, AMP_ENTITY.length())) {
                        sb.append('&');
                        i += AMP_ENTITY.length() - 1;
                        continue;
}
}
                sb.append(c);
            }
        }
        s = sb.toString();

        return s;
    }

    /**
     * Returns true if the character at the given offset in the string is escaped
     * (the previous character is a \, and that character isn't itself an escaped \)
     *
     * @param s the string
     * @param index the index of the character in the string to check
     * @return true if the character is escaped
     */
    @VisibleForTesting
    static boolean isEscaped(String s, int index) {
        if (index == 0 || index == s.length()) {
            return false;
        }
        int prevPos = index - 1;
        char prev = s.charAt(prevPos);
        if (prev != '\\') {
            return false;
        }
        // The character *may* be escaped; not sure if the \ we ran into is
        // an escape character, or an escaped backslash; we have to search backwards
        // to be certain.
        int j = prevPos - 1;
        while (j >= 0) {
            if (s.charAt(j) != '\\') {
                break;
            }
            j--;
        }
        // If we passed an odd number of \'s, the space is escaped
        return (prevPos - j) % 2 == 1;
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
    public static String escapeResourceString(String s) {
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
}








//Synthetic comment -- diff --git a/sdk_common/tests/src/com/android/ide/common/resources/ValueResourceParserTest.java b/sdk_common/tests/src/com/android/ide/common/resources/ValueResourceParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..aed6060

//Synthetic comment -- @@ -0,0 +1,148 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.resources;

import static com.android.ide.common.resources.ValueResourceParser.escapeResourceString;
import static com.android.ide.common.resources.ValueResourceParser.isEscaped;
import static com.android.ide.common.resources.ValueResourceParser.unescapeResourceString;

import junit.framework.TestCase;

public class ValueResourceParserTest extends TestCase {

    public void testEscapeStringShouldEscapeXmlSpecialCharacters() throws Exception {
        assertEquals("&lt;", escapeResourceString("<"));
        assertEquals("&amp;", escapeResourceString("&"));
    }

    public void testEscapeStringShouldEscapeQuotes() throws Exception {
        assertEquals("\\'", escapeResourceString("'"));
        assertEquals("\\\"", escapeResourceString("\""));
        assertEquals("\" ' \"", escapeResourceString(" ' "));
    }

    public void testEscapeStringShouldPreserveWhitespace() throws Exception {
        assertEquals("\"at end  \"", escapeResourceString("at end  "));
        assertEquals("\"  at begin\"", escapeResourceString("  at begin"));
    }

    public void testEscapeStringShouldEscapeAtSignAndQuestionMarkOnlyAtBeginning()
            throws Exception {
        assertEquals("\\@text", escapeResourceString("@text"));
        assertEquals("a@text", escapeResourceString("a@text"));
        assertEquals("\\?text", escapeResourceString("?text"));
        assertEquals("a?text", escapeResourceString("a?text"));
        assertEquals("\" ?text\"", escapeResourceString(" ?text"));
    }

    public void testEscapeStringShouldEscapeJavaEscapeSequences() throws Exception {
        assertEquals("\\n", escapeResourceString("\n"));
        assertEquals("\\t", escapeResourceString("\t"));
        assertEquals("\\\\", escapeResourceString("\\"));
    }

    public void testTrim() throws Exception {
        assertEquals("", unescapeResourceString("", false, true));
        assertEquals("", unescapeResourceString("  \n  ", false, true));
        assertEquals("test", unescapeResourceString("  test  ", false, true));
        assertEquals("  test  ", unescapeResourceString("\"  test  \"", false, true));
        assertEquals("test", unescapeResourceString("\n\t  test \t\n ", false, true));

        assertEquals("test\n", unescapeResourceString("  test\\n  ", false, true));
        assertEquals("  test\n  ", unescapeResourceString("\"  test\\n  \"", false, true));
        assertEquals("te\\st", unescapeResourceString("\n\t  te\\\\st \t\n ", false, true));
        assertEquals("te\\st", unescapeResourceString("  te\\\\st  ", false, true));
        assertEquals("test", unescapeResourceString("\"\"\"test\"\"  ", false, true));
        assertEquals("\"test\"", unescapeResourceString("\"\"\\\"test\\\"\"  ", false, true));
        assertEquals("test ", unescapeResourceString("test\\  ", false, true));
        assertEquals("\\\\\\", unescapeResourceString("\\\\\\\\\\\\ ", false, true));
        assertEquals("\\\\\\ ", unescapeResourceString("\\\\\\\\\\\\\\ ", false, true));
    }

    public void testNoTrim() throws Exception {
        assertEquals("", unescapeResourceString("", false, false));
        assertEquals("  \n  ", unescapeResourceString("  \n  ", false, false));
        assertEquals("  test  ", unescapeResourceString("  test  ", false, false));
        assertEquals("\"  test  \"", unescapeResourceString("\"  test  \"", false, false));
        assertEquals("\n\t  test \t\n ", unescapeResourceString("\n\t  test \t\n ", false, false));

        assertEquals("  test\n  ", unescapeResourceString("  test\\n  ", false, false));
        assertEquals("\"  test\n  \"", unescapeResourceString("\"  test\\n  \"", false, false));
        assertEquals("\n\t  te\\st \t\n ", unescapeResourceString("\n\t  te\\\\st \t\n ", false, false));
        assertEquals("  te\\st  ", unescapeResourceString("  te\\\\st  ", false, false));
        assertEquals("\"\"\"test\"\"  ", unescapeResourceString("\"\"\"test\"\"  ", false, false));
        assertEquals("\"\"\"test\"\"  ", unescapeResourceString("\"\"\\\"test\\\"\"  ", false, false));
        assertEquals("test  ", unescapeResourceString("test\\  ", false, false));
        assertEquals("\\\\\\ ", unescapeResourceString("\\\\\\\\\\\\ ", false, false));
        assertEquals("\\\\\\ ", unescapeResourceString("\\\\\\\\\\\\\\ ", false, false));
    }

    public void testUnescapeStringShouldUnescapeXmlSpecialCharacters() throws Exception {
        assertEquals("&lt;", unescapeResourceString("&lt;", false, true));
        assertEquals("<", unescapeResourceString("&lt;", true, true));
        assertEquals("<", unescapeResourceString("  &lt;  ", true, true));
        assertEquals("&amp;", unescapeResourceString("&amp;", false, true));
        assertEquals("&", unescapeResourceString("&amp;", true, true));
        assertEquals("&", unescapeResourceString("  &amp;  ", true, true));
        assertEquals("!<", unescapeResourceString("!&lt;", true, true));
    }

    public void testUnescapeStringShouldUnescapeQuotes() throws Exception {
        assertEquals("'", unescapeResourceString("\\'", false, true));
        assertEquals("\"", unescapeResourceString("\\\"", false, true));
        assertEquals(" ' ", unescapeResourceString("\" ' \"", false, true));
    }

    public void testUnescapeStringShouldPreserveWhitespace() throws Exception {
        assertEquals("at end  ", unescapeResourceString("\"at end  \"", false, true));
        assertEquals("  at begin", unescapeResourceString("\"  at begin\"", false, true));
    }

    public void testUnescapeStringShouldUnescapeAtSignAndQuestionMarkOnlyAtBeginning()
            throws Exception {
        assertEquals("@text", unescapeResourceString("\\@text", false, true));
        assertEquals("a@text", unescapeResourceString("a@text", false, true));
        assertEquals("?text", unescapeResourceString("\\?text", false, true));
        assertEquals("a?text", unescapeResourceString("a?text", false, true));
        assertEquals(" ?text", unescapeResourceString("\" ?text\"", false, true));
    }

    public void testUnescapeStringShouldUnescapeJavaUnescapeSequences() throws Exception {
        assertEquals("\n", unescapeResourceString("\\n", false, true));
        assertEquals("\t", unescapeResourceString("\\t", false, true));
        assertEquals("\\", unescapeResourceString("\\\\", false, true));
    }

    public void testIsEscaped() throws Exception {
        assertFalse(isEscaped("", 0));
        assertFalse(isEscaped(" ", 0));
        assertFalse(isEscaped(" ", 1));
        assertFalse(isEscaped("x\\y ", 0));
        assertFalse(isEscaped("x\\y ", 1));
        assertTrue(isEscaped("x\\y ", 2));
        assertFalse(isEscaped("x\\y ", 3));
        assertFalse(isEscaped("x\\\\y ", 0));
        assertFalse(isEscaped("x\\\\y ", 1));
        assertTrue(isEscaped("x\\\\y ", 2));
        assertFalse(isEscaped("x\\\\y ", 3));
        assertFalse(isEscaped("\\\\\\\\y ", 0));
        assertTrue(isEscaped( "\\\\\\\\y ", 1));
        assertFalse(isEscaped("\\\\\\\\y ", 2));
        assertTrue(isEscaped( "\\\\\\\\y ", 3));
        assertFalse(isEscaped("\\\\\\\\y ", 4));
    }
}







