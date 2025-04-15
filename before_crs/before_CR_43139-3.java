/*Move some utility functions from AdtUtils to common

Change-Id:Ia6f5c55e07c7f60712472c8e850b7c4595c46671*/
//Synthetic comment -- diff --git a/common/src/com/android/utils/SdkUtils.java b/common/src/com/android/utils/SdkUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..b49c120

//Synthetic comment -- @@ -0,0 +1,136 @@








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/SdkUtilsTest.java b/common/tests/src/com/android/utils/SdkUtilsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..29a4d51

//Synthetic comment -- @@ -0,0 +1,84 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index cf79c90..d5fa567 100644

//Synthetic comment -- @@ -53,7 +53,6 @@
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -90,98 +89,6 @@
@SuppressWarnings("restriction") // WST API
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

    /**
* Creates a Java class name out of the given string, if possible. For
* example, "My Project" becomes "MyProject", "hello" becomes "Hello",
* "Java's" becomes "Java", and so on.
//Synthetic comment -- @@ -344,29 +251,6 @@
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

/**
* Returns the current editor (the currently visible and active editor), or null if
* not found








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java
//Synthetic comment -- index 98d931f..a483e9b 100755

//Synthetic comment -- @@ -19,12 +19,12 @@
import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
//Synthetic comment -- @@ -166,7 +166,7 @@

final BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));
try {
                    final String lineSep = AdtUtils.getLineSeparator();

int err = GrabProcessOutput.grabProcessOutput(
process,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 6f15d83..1dd32c7 100644

//Synthetic comment -- @@ -24,8 +24,8 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.utils.XmlUtils;

import org.eclipse.wst.xml.core.internal.document.DocumentTypeImpl;
//Synthetic comment -- @@ -86,7 +86,7 @@
mPrefs = prefs;
mStyle = style;
if (lineSeparator == null) {
            lineSeparator = AdtUtils.getLineSeparator();
}
mLineSeparator = lineSeparator;
}
//Synthetic comment -- @@ -877,8 +877,8 @@
return false;
}

        return AdtUtils.endsWith(mOut, mLineSeparator) &&
                AdtUtils.endsWith(mOut, mOut.length() - mLineSeparator.length(), mLineSeparator);
}

private boolean newlineAfterElementClose(Element element, int depth) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 0178173..c55d0d8 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import static com.android.SdkConstants.DOT_GIF;
import static com.android.SdkConstants.DOT_JPG;
import static com.android.SdkConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java
//Synthetic comment -- index f6b80d7..081ec80 100644

//Synthetic comment -- @@ -24,7 +24,6 @@

import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -32,6 +31,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.fieldassist.ContentProposal;
//Synthetic comment -- @@ -172,7 +172,7 @@
prefix.length() <= nameStart ? "" : prefix.substring(nameStart);
for (ResourceItem item : repository.getResourceItemsOfType(type)) {
String name = item.getName();
                    if (AdtUtils.startsWithIgnoreCase(name, namePrefix)) {
results.add(base + name);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java
//Synthetic comment -- index 132855d..5559349 100644

//Synthetic comment -- @@ -35,9 +35,9 @@
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
//Synthetic comment -- @@ -147,7 +147,7 @@
}

for (String value : values) {
                    if (AdtUtils.startsWithIgnoreCase(value, prefix)) {
if (prepend != null && prepend.contains(value)) {
continue;
}
//Synthetic comment -- @@ -165,13 +165,13 @@
String[] values = info.getEnumValues();
if (values != null) {
for (String value : values) {
                    if (AdtUtils.startsWithIgnoreCase(value, prefix)) {
proposals.add(new ContentProposal(value));
}
}

for (String value : values) {
                    if (!AdtUtils.startsWithIgnoreCase(value, prefix)) {
proposals.add(new ContentProposal(value));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java
//Synthetic comment -- index 16519fe..159f089 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.pages;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.UiElementPart;
//Synthetic comment -- @@ -25,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.IUiUpdateListener.UiUpdateState;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
//Synthetic comment -- @@ -220,7 +220,7 @@
}
mUndoXmlParent.insertBefore(mUndoXmlNode, next);
if (next == null) {
                    Text sep = mUndoXmlDocument.createTextNode(AdtUtils.getLineSeparator());
mUndoXmlParent.insertBefore(sep, null);  // insert separator before end tag
}
success = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index b521d78..f905c73 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -42,6 +41,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.IUiUpdateListener.UiUpdateState;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.utils.XmlUtils;

import org.eclipse.jface.text.TextUtilities;
//Synthetic comment -- @@ -1035,7 +1035,7 @@
if (document != null) {
newLine = TextUtilities.getDefaultLineDelimiter(document);
} else {
            newLine = AdtUtils.getLineSeparator();
}
Text indentNode = doc.createTextNode(newLine + indent);
parentXmlNode.insertBefore(indentNode, xmlNextSibling);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 1937a94..b3303b3 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.SdkConstants.FD_NATIVE_LIBS;
import static com.android.SdkConstants.DOT_JAR;
import static com.android.SdkConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_LINT;
import static com.android.ide.eclipse.adt.AdtUtils.workspacePathToFile;

//Synthetic comment -- @@ -50,6 +50,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.utils.Pair;
import com.google.common.collect.Maps;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -673,7 +674,7 @@
return readPlainFile(f);
}

        if (AdtUtils.endsWithIgnoreCase(file.getName(), DOT_XML)) {
IStructuredModel model = null;
try {
IModelManager modelManager = StructuredModelManager.getModelManager();
//Synthetic comment -- @@ -813,7 +814,7 @@
File[] jars = libs.listFiles();
if (jars != null) {
for (File jar : jars) {
                                if (AdtUtils.endsWith(jar.getPath(), DOT_JAR)) {
libraries.add(jar);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 47aac0c..5d2a2bb 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -258,7 +259,7 @@
}
IFile file = (IFile) resource;
boolean isJava = file.getName().endsWith(DOT_JAVA);
            boolean isXml = AdtUtils.endsWith(file.getName(), DOT_XML);
if (!isJava && !isXml) {
return;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java
//Synthetic comment -- index 0442f18..2851c47 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -93,7 +94,7 @@
scope = Scope.ALL;
} else {
String name = resource.getName();
                    if (AdtUtils.endsWithIgnoreCase(name, DOT_XML)) {
if (name.equals(SdkConstants.FN_ANDROID_MANIFEST_XML)) {
scope = EnumSet.of(Scope.MANIFEST);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
//Synthetic comment -- index d97d176..eeaca0c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.ui;

import static com.android.SdkConstants.DOT_9PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceResolver;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 31c7225..6bf5e28 100644

//Synthetic comment -- @@ -18,8 +18,8 @@
import static com.android.SdkConstants.FN_PROJECT_PROGUARD_FILE;
import static com.android.SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;
import static com.android.ide.eclipse.adt.AdtUtils.capitalize;
import static com.android.ide.eclipse.adt.AdtUtils.stripWhitespace;
import static com.android.ide.eclipse.adt.internal.wizards.newproject.ApplicationInfoPage.ACTIVITY_NAME_SUFFIX;

import com.android.SdkConstants;
import com.android.ide.common.xml.ManifestData;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index e2d061b..68c7f4c 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
import com.android.resources.ResourceFolderType;
import com.android.sdklib.IAndroidTarget;
import com.android.utils.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -648,7 +649,7 @@
if (res.getType() == IResource.FOLDER) {
wsFolderPath = res.getProjectRelativePath();
} else if (res.getType() == IResource.FILE) {
                    if (AdtUtils.endsWithIgnoreCase(res.getName(), DOT_XML)) {
fileName = res.getName();
}
wsFolderPath = res.getParent().getProjectRelativePath();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index cb45522..f1bd840 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.manifmerger.ManifestMerger;
import com.android.manifmerger.MergerLog;
import com.android.resources.ResourceFolderType;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
//Synthetic comment -- @@ -688,7 +689,7 @@
} else {
// Just insert into file along with comment, using the "standard" conflict
// syntax that many tools and editors recognize.
            String sep = AdtUtils.getLineSeparator();
contents =
"<<<<<<< Original" + sep
+ currentXml + sep








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 0d4e02e..c1e94ac 100644

//Synthetic comment -- @@ -15,75 +15,12 @@
*/
package com.android.ide.eclipse.adt;

import java.util.Locale;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class AdtUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "Foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("Barfoo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("BarFoo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("BarFoo", "foO"));

        assertFalse(AdtUtils.endsWithIgnoreCase("foob", "foo"));
        assertFalse(AdtUtils.endsWithIgnoreCase("foo", "fo"));
    }

    public void testStartsWithIgnoreCase() {
        assertTrue(AdtUtils.startsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.startsWithIgnoreCase("foo", "Foo"));
        assertTrue(AdtUtils.startsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.startsWithIgnoreCase("barfoo", "bar"));
        assertTrue(AdtUtils.startsWithIgnoreCase("BarFoo", "bar"));
        assertTrue(AdtUtils.startsWithIgnoreCase("BarFoo", "bAr"));

        assertFalse(AdtUtils.startsWithIgnoreCase("bfoo", "foo"));
        assertFalse(AdtUtils.startsWithIgnoreCase("fo", "foo"));
    }

    public void testStartsWith() {
        assertTrue(AdtUtils.startsWith("foo", 0, "foo"));
        assertTrue(AdtUtils.startsWith("foo", 0, "Foo"));
        assertTrue(AdtUtils.startsWith("Foo", 0, "foo"));
        assertTrue(AdtUtils.startsWith("aFoo", 1, "foo"));

        assertFalse(AdtUtils.startsWith("aFoo", 0, "foo"));
        assertFalse(AdtUtils.startsWith("aFoo", 2, "foo"));
    }

    public void testEndsWith() {
        assertTrue(AdtUtils.endsWith("foo", "foo"));
        assertTrue(AdtUtils.endsWith("foobar", "obar"));
        assertTrue(AdtUtils.endsWith("foobar", "bar"));
        assertTrue(AdtUtils.endsWith("foobar", "ar"));
        assertTrue(AdtUtils.endsWith("foobar", "r"));
        assertTrue(AdtUtils.endsWith("foobar", ""));

        assertTrue(AdtUtils.endsWith(new StringBuilder("foobar"), "bar"));
        assertTrue(AdtUtils.endsWith(new StringBuilder("foobar"), new StringBuffer("obar")));
        assertTrue(AdtUtils.endsWith("foobar", new StringBuffer("obar")));

        assertFalse(AdtUtils.endsWith("foo", "fo"));
        assertFalse(AdtUtils.endsWith("foobar", "Bar"));
        assertFalse(AdtUtils.endsWith("foobar", "longfoobar"));
    }

    public void testEndsWith2() {
        assertTrue(AdtUtils.endsWith("foo", "foo".length(), "foo"));
        assertTrue(AdtUtils.endsWith("foo", "fo".length(), "fo"));
        assertTrue(AdtUtils.endsWith("foo", "f".length(), "f"));
    }

    public void testStripWhitespace() {
        assertEquals("foo", AdtUtils.stripWhitespace("foo"));
        assertEquals("foobar", AdtUtils.stripWhitespace("foo bar"));
        assertEquals("foobar", AdtUtils.stripWhitespace("  foo bar  \n\t"));
    }

public void testExtractClassName() {
assertEquals("Foo", AdtUtils.extractClassName("foo"));
assertEquals("Foobar", AdtUtils.extractClassName("foo bar"));







