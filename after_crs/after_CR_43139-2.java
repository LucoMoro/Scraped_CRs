/*Move some utility functions from AdtUtils to common

Change-Id:Ia6f5c55e07c7f60712472c8e850b7c4595c46671*/




//Synthetic comment -- diff --git a/common/src/com/android/SdkUtils.java b/common/src/com/android/SdkUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..598985b

//Synthetic comment -- @@ -0,0 +1,136 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android;

import com.android.annotations.NonNull;

public class SdkUtils {
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
     * Returns true if the given sequence ends with the given suffix (case
     * sensitive).
     *
     * @param sequence the character sequence to be checked
     * @param suffix the suffix to look for
     * @return true if the given sequence ends with the given suffix
     */
    public static boolean endsWith(CharSequence sequence, CharSequence suffix) {
        return endsWith(sequence, sequence.length(), suffix);
    }

    /**
     * Returns true if the given sequence ends at the given offset with the given suffix (case
     * sensitive)
     *
     * @param sequence the character sequence to be checked
     * @param endOffset the offset at which the sequence is considered to end
     * @param suffix the suffix to look for
     * @return true if the given sequence ends with the given suffix
     */
    public static boolean endsWith(CharSequence sequence, int endOffset, CharSequence suffix) {
        if (endOffset < suffix.length()) {
            return false;
        }

        for (int i = endOffset - 1, j = suffix.length() - 1; j >= 0; i--, j--) {
            if (sequence.charAt(i) != suffix.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the given string starts with the given prefix, using a
     * case-insensitive comparison.
     *
     * @param string the full string to be checked
     * @param prefix the prefix to be checked for
     * @return true if the string case-insensitively starts with the given prefix
     */
    public static boolean startsWithIgnoreCase(String string, String prefix) {
        return string.regionMatches(true /* ignoreCase */, 0, prefix, 0, prefix.length());
    }

    /**
     * Returns true if the given string starts at the given offset with the
     * given prefix, case insensitively.
     *
     * @param string the full string to be checked
     * @param offset the offset in the string to start looking
     * @param prefix the prefix to be checked for
     * @return true if the string case-insensitively starts at the given offset
     *         with the given prefix
     */
    public static boolean startsWith(String string, int offset, String prefix) {
        return string.regionMatches(true /* ignoreCase */, offset, prefix, 0, prefix.length());
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

    /** For use by {@link #getLineSeparator()} */
    private static String sLineSeparator;

    /**
     * Returns the default line separator to use.
     * <p>
     * NOTE: If you have an associated {@link IDocument}, it is better to call
     * {@link TextUtilities#getDefaultLineDelimiter(IDocument)} since that will
     * allow (for example) editing a \r\n-delimited document on a \n-delimited
     * platform and keep a consistent usage of delimiters in the file.
     *
     * @return the delimiter string to use
     */
    @NonNull
    public static String getLineSeparator() {
        if (sLineSeparator == null) {
            // This is guaranteed to exist:
            sLineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
        }

        return sLineSeparator;
    }
}








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/SdkUtilsTest.java b/common/tests/src/com/android/utils/SdkUtilsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a68e3ae

//Synthetic comment -- @@ -0,0 +1,86 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.utils;

import com.android.SdkUtils;

import junit.framework.TestCase;

public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        assertTrue(SdkUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(SdkUtils.endsWithIgnoreCase("foo", "Foo"));
        assertTrue(SdkUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(SdkUtils.endsWithIgnoreCase("Barfoo", "foo"));
        assertTrue(SdkUtils.endsWithIgnoreCase("BarFoo", "foo"));
        assertTrue(SdkUtils.endsWithIgnoreCase("BarFoo", "foO"));

        assertFalse(SdkUtils.endsWithIgnoreCase("foob", "foo"));
        assertFalse(SdkUtils.endsWithIgnoreCase("foo", "fo"));
    }

    public void testStartsWithIgnoreCase() {
        assertTrue(SdkUtils.startsWithIgnoreCase("foo", "foo"));
        assertTrue(SdkUtils.startsWithIgnoreCase("foo", "Foo"));
        assertTrue(SdkUtils.startsWithIgnoreCase("foo", "foo"));
        assertTrue(SdkUtils.startsWithIgnoreCase("barfoo", "bar"));
        assertTrue(SdkUtils.startsWithIgnoreCase("BarFoo", "bar"));
        assertTrue(SdkUtils.startsWithIgnoreCase("BarFoo", "bAr"));

        assertFalse(SdkUtils.startsWithIgnoreCase("bfoo", "foo"));
        assertFalse(SdkUtils.startsWithIgnoreCase("fo", "foo"));
    }

    public void testStartsWith() {
        assertTrue(SdkUtils.startsWith("foo", 0, "foo"));
        assertTrue(SdkUtils.startsWith("foo", 0, "Foo"));
        assertTrue(SdkUtils.startsWith("Foo", 0, "foo"));
        assertTrue(SdkUtils.startsWith("aFoo", 1, "foo"));

        assertFalse(SdkUtils.startsWith("aFoo", 0, "foo"));
        assertFalse(SdkUtils.startsWith("aFoo", 2, "foo"));
    }

    public void testEndsWith() {
        assertTrue(SdkUtils.endsWith("foo", "foo"));
        assertTrue(SdkUtils.endsWith("foobar", "obar"));
        assertTrue(SdkUtils.endsWith("foobar", "bar"));
        assertTrue(SdkUtils.endsWith("foobar", "ar"));
        assertTrue(SdkUtils.endsWith("foobar", "r"));
        assertTrue(SdkUtils.endsWith("foobar", ""));

        assertTrue(SdkUtils.endsWith(new StringBuilder("foobar"), "bar"));
        assertTrue(SdkUtils.endsWith(new StringBuilder("foobar"), new StringBuffer("obar")));
        assertTrue(SdkUtils.endsWith("foobar", new StringBuffer("obar")));

        assertFalse(SdkUtils.endsWith("foo", "fo"));
        assertFalse(SdkUtils.endsWith("foobar", "Bar"));
        assertFalse(SdkUtils.endsWith("foobar", "longfoobar"));
    }

    public void testEndsWith2() {
        assertTrue(SdkUtils.endsWith("foo", "foo".length(), "foo"));
        assertTrue(SdkUtils.endsWith("foo", "fo".length(), "fo"));
        assertTrue(SdkUtils.endsWith("foo", "f".length(), "f"));
    }

    public void testStripWhitespace() {
        assertEquals("foo", SdkUtils.stripWhitespace("foo"));
        assertEquals("foobar", SdkUtils.stripWhitespace("foo bar"));
        assertEquals("foobar", SdkUtils.stripWhitespace("  foo bar  \n\t"));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index cf79c90..d5fa567 100644

//Synthetic comment -- @@ -53,7 +53,6 @@
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -90,98 +89,6 @@
@SuppressWarnings("restriction") // WST API
public class AdtUtils {
/**
* Creates a Java class name out of the given string, if possible. For
* example, "My Project" becomes "MyProject", "hello" becomes "Hello",
* "Java's" becomes "Java", and so on.
//Synthetic comment -- @@ -344,29 +251,6 @@
return sb.toString();
}

/**
* Returns the current editor (the currently visible and active editor), or null if
* not found








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java
//Synthetic comment -- index 98d931f..68ac16e 100755

//Synthetic comment -- @@ -17,9 +17,9 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.SdkConstants;
import com.android.SdkUtils;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.util.GrabProcessOutput;
//Synthetic comment -- @@ -166,7 +166,7 @@

final BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));
try {
                    final String lineSep = SdkUtils.getLineSeparator();

int err = GrabProcessOutput.grabProcessOutput(
process,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 6f15d83..1e4cf02 100644

//Synthetic comment -- @@ -22,9 +22,9 @@
import static com.android.SdkConstants.TAG_STYLE;
import static com.android.SdkConstants.XMLNS;

import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.utils.XmlUtils;

//Synthetic comment -- @@ -86,7 +86,7 @@
mPrefs = prefs;
mStyle = style;
if (lineSeparator == null) {
            lineSeparator = SdkUtils.getLineSeparator();
}
mLineSeparator = lineSeparator;
}
//Synthetic comment -- @@ -877,8 +877,8 @@
return false;
}

        return SdkUtils.endsWith(mOut, mLineSeparator) &&
                SdkUtils.endsWith(mOut, mOut.length() - mLineSeparator.length(), mLineSeparator);
}

private boolean newlineAfterElementClose(Element element, int depth) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 0178173..1c0ea50 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import static com.android.SdkConstants.DOT_GIF;
import static com.android.SdkConstants.DOT_JPG;
import static com.android.SdkConstants.DOT_PNG;
import static com.android.SdkUtils.endsWithIgnoreCase;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java
//Synthetic comment -- index f6b80d7..c4c9912 100644

//Synthetic comment -- @@ -22,9 +22,9 @@
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.PREFIX_THEME_REF;

import com.android.SdkUtils;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -172,7 +172,7 @@
prefix.length() <= nameStart ? "" : prefix.substring(nameStart);
for (ResourceItem item : repository.getResourceItemsOfType(type)) {
String name = item.getName();
                    if (SdkUtils.startsWithIgnoreCase(name, namePrefix)) {
results.add(base + name);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java
//Synthetic comment -- index 132855d..abe8181 100644

//Synthetic comment -- @@ -31,11 +31,11 @@
import static com.android.ide.common.api.IAttributeInfo.Format.REFERENCE;
import static com.android.ide.common.api.IAttributeInfo.Format.STRING;

import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;

//Synthetic comment -- @@ -147,7 +147,7 @@
}

for (String value : values) {
                    if (SdkUtils.startsWithIgnoreCase(value, prefix)) {
if (prepend != null && prepend.contains(value)) {
continue;
}
//Synthetic comment -- @@ -165,13 +165,13 @@
String[] values = info.getEnumValues();
if (values != null) {
for (String value : values) {
                    if (SdkUtils.startsWithIgnoreCase(value, prefix)) {
proposals.add(new ContentProposal(value));
}
}

for (String value : values) {
                    if (!SdkUtils.startsWithIgnoreCase(value, prefix)) {
proposals.add(new ContentProposal(value));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java
//Synthetic comment -- index 16519fe..755ae82 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.manifest.pages;

import com.android.SdkUtils;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.UiElementPart;
//Synthetic comment -- @@ -220,7 +220,7 @@
}
mUndoXmlParent.insertBefore(mUndoXmlNode, next);
if (next == null) {
                    Text sep = mUndoXmlDocument.createTextNode(SdkUtils.getLineSeparator());
mUndoXmlParent.insertBefore(sep, null);  // insert separator before end tag
}
success = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index b521d78..293ca08 100644

//Synthetic comment -- @@ -23,11 +23,11 @@
import static com.android.SdkConstants.NEW_ID_PREFIX;

import com.android.SdkConstants;
import com.android.SdkUtils;
import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -1035,7 +1035,7 @@
if (document != null) {
newLine = TextUtilities.getDefaultLineDelimiter(document);
} else {
            newLine = SdkUtils.getLineSeparator();
}
Text indentNode = doc.createTextNode(newLine + indent);
parentXmlNode.insertBefore(indentNode, xmlNextSibling);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 1937a94..fb597bc 100644

//Synthetic comment -- @@ -15,12 +15,13 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.SdkConstants.DOT_JAR;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.FD_NATIVE_LIBS;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_LINT;
import static com.android.ide.eclipse.adt.AdtUtils.workspacePathToFile;

import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -673,7 +674,7 @@
return readPlainFile(f);
}

        if (SdkUtils.endsWithIgnoreCase(file.getName(), DOT_XML)) {
IStructuredModel model = null;
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
//Synthetic comment -- @@ -813,7 +814,7 @@
File[] jars = libs.listFiles();
if (jars != null) {
for (File jar : jars) {
                                if (SdkUtils.endsWith(jar.getPath(), DOT_JAR)) {
libraries.add(jar);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 47aac0c..03e30bc 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.DOT_XML;

import com.android.SdkUtils;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
//Synthetic comment -- @@ -258,7 +259,7 @@
}
IFile file = (IFile) resource;
boolean isJava = file.getName().endsWith(DOT_JAVA);
            boolean isXml = SdkUtils.endsWith(file.getName(), DOT_XML);
if (!isJava && !isXml) {
return;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java
//Synthetic comment -- index 0442f18..35ce823 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.SdkConstants.DOT_XML;

import com.android.SdkConstants;
import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -93,7 +94,7 @@
scope = Scope.ALL;
} else {
String name = resource.getName();
                    if (SdkUtils.endsWithIgnoreCase(name, DOT_XML)) {
if (name.equals(SdkConstants.FN_ANDROID_MANIFEST_XML)) {
scope = EnumSet.of(Scope.MANIFEST);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
//Synthetic comment -- index d97d176..d37341f 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.ui;

import static com.android.SdkConstants.DOT_9PNG;
import static com.android.SdkUtils.endsWithIgnoreCase;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceResolver;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 31c7225..4342167 100644

//Synthetic comment -- @@ -17,8 +17,8 @@

import static com.android.SdkConstants.FN_PROJECT_PROGUARD_FILE;
import static com.android.SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;
import static com.android.SdkUtils.stripWhitespace;
import static com.android.ide.eclipse.adt.AdtUtils.capitalize;
import static com.android.ide.eclipse.adt.internal.wizards.newproject.ApplicationInfoPage.ACTIVITY_NAME_SUFFIX;

import com.android.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index e2d061b..19daef6 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import static com.android.ide.eclipse.adt.internal.wizards.newxmlfile.ChooseConfigurationPage.RES_FOLDER_ABS;

import com.android.SdkConstants;
import com.android.SdkUtils;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -648,7 +649,7 @@
if (res.getType() == IResource.FOLDER) {
wsFolderPath = res.getProjectRelativePath();
} else if (res.getType() == IResource.FILE) {
                    if (SdkUtils.endsWithIgnoreCase(res.getName(), DOT_XML)) {
fileName = res.getName();
}
wsFolderPath = res.getParent().getProjectRelativePath();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index cb45522..62cf132 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateManager.getTemplateRootFolder;

import com.android.SdkConstants;
import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -688,7 +689,7 @@
} else {
// Just insert into file along with comment, using the "standard" conflict
// syntax that many tools and editors recognize.
            String sep = SdkUtils.getLineSeparator();
contents =
"<<<<<<< Original" + sep
+ currentXml + sep








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 0d4e02e..c1e94ac 100644

//Synthetic comment -- @@ -15,75 +15,12 @@
*/
package com.android.ide.eclipse.adt;

import junit.framework.TestCase;

import java.util.Locale;

@SuppressWarnings("javadoc")
public class AdtUtilsTest extends TestCase {
public void testExtractClassName() {
assertEquals("Foo", AdtUtils.extractClassName("foo"));
assertEquals("Foobar", AdtUtils.extractClassName("foo bar"));







