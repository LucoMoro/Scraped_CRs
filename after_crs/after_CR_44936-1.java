/*32849: Fix CRLF line ending handling in the partial formatter

Fix the handling of \r characters in the code which applies
formatting deltas into existing documents. It could end up
inserting the formatted portion in the middle of a \r\n pair,
which made Eclipse extremely confused
(https://bugs.eclipse.org/bugs/show_bug.cgi?id=375421)

This fixes
32849: Eclipse android adt xml editing artifacting (unsynced) lines of
       text when changes are made in the graphical interface

It also adds a lint check to identify *existing* files that already
have these mangled line endings, along with a quickfix to make the
correction.

Change-Id:I1e7024f2786e4cb0233c2c6b98c3d3f942703ea0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java
//Synthetic comment -- index 9c29077..35735dc 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import static org.eclipse.wst.xml.core.internal.regions.DOMRegionContext.XML_TAG_OPEN;

import com.android.SdkConstants;
import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
//Synthetic comment -- @@ -360,7 +361,8 @@
* adjusted (for example to make the edit smaller if the beginning and/or end is
* identical, and so on)
*/
    @VisibleForTesting
    static ReplaceEdit createReplaceEdit(IDocument document, int replaceStart,
int replaceEnd, String formatted, XmlFormatPreferences prefs) {
// If replacing a node somewhere in the middle, start the replacement at the
// beginning of the current line
//Synthetic comment -- @@ -399,7 +401,7 @@
if (c == '\n') {
beginsWithNewline = true;
break;
            } else if (!Character.isWhitespace(c)) { // \r is whitespace so is handled correctly
break;
}
}
//Synthetic comment -- @@ -411,6 +413,9 @@
replaceStart = prevNewlineIndex;
}
prevNewlineIndex = index;
                    if (index > 0 && document.getChar(index - 1) == '\r') {
                        prevNewlineIndex--;
                    }
} else if (!Character.isWhitespace(c)) {
break;
}
//Synthetic comment -- @@ -423,16 +428,16 @@
}

// Search forwards too
        int nextNewlineIndex = -1;
try {
int max = document.getLength();
for (index = replaceEnd; index < max; index++) {
char c = document.getChar(index);
if (c == '\n') {
                    if (nextNewlineIndex != -1) {
                        replaceEnd = nextNewlineIndex + 1;
}
                    nextNewlineIndex = index;
} else if (!Character.isWhitespace(c)) {
break;
}
//Synthetic comment -- @@ -440,7 +445,6 @@
} catch (BadLocationException e) {
AdtPlugin.log(e, null);
}
boolean endsWithNewline = false;
for (int i = formatted.length() - 1; i >= 0; i--) {
char c = formatted.charAt(i);
//Synthetic comment -- @@ -452,8 +456,8 @@
}
}

        if (prefs.removeEmptyLines && nextNewlineIndex != -1 && endsWithNewline) {
            replaceEnd = nextNewlineIndex + 1;
}

// Figure out how much of the before and after strings are identical and narrow








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlFormatPreferences.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlFormatPreferences.java
//Synthetic comment -- index 04441fd..05c8a7f 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.formatting;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AttributeSortOrder;
//Synthetic comment -- @@ -67,6 +68,7 @@
*
* @return an {@link XmlFormatPreferences} object
*/
    @NonNull
public static XmlFormatPreferences create() {
XmlFormatPreferences p = new XmlFormatPreferences();
AdtPrefs prefs = AdtPrefs.getPrefs();
//Synthetic comment -- @@ -80,6 +82,16 @@
return p;
}

    /**
     * Returns a new preferences object initialized with the defaults
     *
     * @return an {@link XmlFormatPreferences} object
     */
    @NonNull
    static XmlFormatPreferences defaults() {
        return new XmlFormatPreferences();
    }

// The XML module settings do not have a public API. We should replace this with JDT
// settings anyway since that's more likely what users have configured and want applied
// to their XML files








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/DosLineEndingsFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/DosLineEndingsFix.java
new file mode 100644
//Synthetic comment -- index 0000000..9a5456b

//Synthetic comment -- @@ -0,0 +1,64 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.lint;

import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/** Quickfix for correcting line endings in the file */
class DosLineEndingsFix extends LintFix {

    protected DosLineEndingsFix(String id, IMarker marker) {
        super(id, marker);
    }

    @Override
    public boolean needsFocus() {
        return false;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return "Fix line endings";
    }

    @Override
    public void apply(IDocument document) {
        char next = 0;
        for (int i = document.getLength() - 1; i >= 0; i--) {
            try {
                char c = document.getChar(i);
                if (c == '\r' && next != '\n') {
                    document.replace(i, 1, "\n"); //$NON-NLS-1$
                }
                next = c;
            } catch (BadLocationException e) {
                AdtPlugin.log(e, null);
                return;
            }
        }

        deleteMarker();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index ab850e1..18d72db 100644

//Synthetic comment -- @@ -531,6 +531,9 @@
*/
private static Pair<Integer, Integer> adjustOffsets(IDocument doc, int startOffset,
int endOffset) {
        int originalStart = startOffset;
        int originalEnd = endOffset;

if (doc != null) {
while (endOffset > startOffset && endOffset < doc.getLength()) {
try {
//Synthetic comment -- @@ -552,6 +555,9 @@
char c = doc.getChar(lineEnd);
if (c == '\n' || c == '\r') {
endOffset = lineEnd;
                        if (endOffset > 0 && doc.getChar(endOffset - 1) == '\r') {
                            endOffset--;
                        }
break;
}
} catch (BadLocationException e) {
//Synthetic comment -- @@ -562,6 +568,13 @@
}
}

        if (startOffset >= endOffset) {
            // Selecting nothing (for example, for the mangled CRLF delimiter issue selecting
            // just the newline)
            // In that case, use the real range
            return Pair.of(originalStart, originalEnd);
        }

return Pair.of(startOffset, endOffset);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java
//Synthetic comment -- index 0b074bb..feb6bb5 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.checks.DetectMissingPrefix;
import com.android.tools.lint.checks.DosLineEndingDetector;
import com.android.tools.lint.checks.HardcodedValuesDetector;
import com.android.tools.lint.checks.InefficientWeightDetector;
import com.android.tools.lint.checks.ManifestOrderDetector;
//Synthetic comment -- @@ -170,6 +171,7 @@
sFixes.put(UseCompoundDrawableDetector.ISSUE.getId(),
UseCompoundDrawableDetectorFix.class);
sFixes.put(TypoDetector.ISSUE.getId(), TypoFix.class);
        sFixes.put(DosLineEndingDetector.ISSUE.getId(), DosLineEndingsFix.class);
// ApiDetector.UNSUPPORTED is provided as a marker resolution rather than
// a quick assistant (the marker resolution adds a suitable @TargetApi annotation)
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/TypoFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/TypoFix.java
//Synthetic comment -- index 4358410..8a83364 100644

//Synthetic comment -- @@ -105,7 +105,7 @@
String message = mMarker.getAttribute(IMarker.MESSAGE, "");
String typo = TypoDetector.getTypo(message);
List<String> replacements = TypoDetector.getSuggestions(message);
        if (replacements != null && !replacements.isEmpty() && typo != null) {
List<LintFix> allFixes = new ArrayList<LintFix>(replacements.size());
for (String replacement : replacements) {
TypoFix fix = new TypoFix(mId, mMarker);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategyTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategyTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4fe2a7f

//Synthetic comment -- @@ -0,0 +1,331 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.editors.formatting;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.ReplaceEdit;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class AndroidXmlFormattingStrategyTest extends TestCase {
    // In the given before document, replace in the range replaceStart to replaceEnd
    // the formatted string, and assert that it's identical to the given after string
    private void check(String before, int replaceStart, int replaceEnd, String formatted,
            String expected, XmlFormatPreferences prefs)
                    throws MalformedTreeException, BadLocationException {
        Document document = new Document();
        document.set(before);
        ReplaceEdit edit = AndroidXmlFormattingStrategy.createReplaceEdit(document, replaceStart,
                replaceEnd, formatted, prefs);
        assertNotNull(edit);
        edit.apply(document);
        String contents = document.get();
        // Ensure that we don't have any mangled CRLFs
        char prev =  0;
        boolean haveCrlf = false;
        for (int i = 0, n = contents.length(); i < n; i++) {
            char c = contents.charAt(i);
            if (c == '\r') {
                haveCrlf = true;
            }
            if (!(c != '\r' || prev != '\r')) {
                fail("Mangled document: Found adjacent \\r's starting at " + i
                        + ": " + contents.substring(i - 1, Math.min(contents.length(), i + 10))
                                + "...");
            }
            if (haveCrlf && c == '\n' && prev != '\r') {
                fail("Mangled document: In a CRLF document, found \\n without preceeding \\r");
            }

            prev = c;
        }

        assertEquals(expected, contents);
    }

    // In the given before document, replace the range indicated by [ and ] with the given
    // formatted string, and assert that it's identical to the given after string
    private void check(String before, String insert, String expected, XmlFormatPreferences prefs)
            throws MalformedTreeException, BadLocationException {
        int replaceStart = before.indexOf('[');
        assertTrue(replaceStart != -1);
        before = before.substring(0, replaceStart) + before.substring(replaceStart + 1);

        int replaceEnd = before.indexOf(']');
        assertTrue(replaceEnd != -1);
        before = before.substring(0, replaceEnd) + before.substring(replaceEnd + 1);

        check(before, replaceStart, replaceEnd, insert, expected, prefs);
    }

    public void test1() throws Exception {
        check(
            // Before
            "<root>\n" +
            "[     <element/>\n" +
            "   <second/>\n" +
            "]\n" +
            "</root>\n",

            // Insert
            "    <element/>\n" +
            "    <second/>\n",

            // After
            "<root>\n" +
            "    <element/>\n" +
            "    <second/>\n" +
            "\n" +
            "</root>\n",

            XmlFormatPreferences.defaults());
    }

    public void test2() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;

        check(
                // Before
                "<root>\n" +
                "\n" +
                "\n" +
                "[     <element/>\n" +
                "   <second/>\n" +
                "]\n" +
                "\n" +
                "\n" +
                "</root>\n",

                // Insert
                "    <element/>\n" +
                "    <second/>\n",

                // After
                "<root>\n" +
                "    <element/>\n" +
                "    <second/>\n" +
                "</root>\n",

                prefs);
    }

    public void test3() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;

        check(
                // Before
                "<root>\n" +
                "\n" +
                "\n" +
                "     [<element/>\n" +
                "   <second/>]\n" +
                "\n" +
                "\n" +
                "\n" +
                "</root>\n",

                // Insert
                "    <element/>\n" +
                "    <second/>",

                // After
                "<root>\n" +
                "    <element/>\n" +
                "    <second/>\n" +
                "</root>\n",

                prefs);
    }

    public void test4() throws Exception {
        check(
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\" >\n" +
            "\n" +
            "    [<TextView\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "          android:layout_centerHorizontal=\"true\"\n" +
            "        android:layout_centerVertical=\"true\"\n" +
            "        android:text=\"foo\"\n" +
            "        tools:context=\".MainActivity\" />]\n" +
            "\n" +
            "</RelativeLayout>\n",

            // Insert
            "\n" +
            "    <TextView\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_centerHorizontal=\"true\"\n" +
            "        android:layout_centerVertical=\"true\"\n" +
            "        android:text=\"foo\"\n" +
            "        tools:context=\".MainActivity\" />\n",

            // After
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\" >\n" +
            "\n" +
            "    <TextView\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_centerHorizontal=\"true\"\n" +
            "        android:layout_centerVertical=\"true\"\n" +
            "        android:text=\"foo\"\n" +
            "        tools:context=\".MainActivity\" />\n" +
            "\n" +
            "</RelativeLayout>\n",

            XmlFormatPreferences.defaults());
    }

    public void testCrLf1() throws Exception {
        check(
            // Before
            "<root>\r\n" +
            "[     <element/>\r\n" +
            "   <second/>\r\n" +
            "]\r\n" +
            "</root>\r\n",

            // Insert
            "    <element/>\r\n" +
            "    <second/>\r\n",

            // After
            "<root>\r\n" +
            "    <element/>\r\n" +
            "    <second/>\r\n" +
            "\r\n" +
            "</root>\r\n",

            XmlFormatPreferences.defaults());
    }

    public void testCrLf2() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;

        check(
                // Before
                "<root>\r\n" +
                "\r\n" +
                "\r\n" +
                "[     <element/>\r\n" +
                "   <second/>\r\n" +
                "]\r\n" +
                "\r\n" +
                "\r\n" +
                "</root>\r\n",

                // Insert
                "    <element/>\r\n" +
                "    <second/>\r\n",

                // After
                "<root>\r\n" +
                "    <element/>\r\n" +
                "    <second/>\r\n" +
                "</root>\r\n",

                prefs);
    }

    public void testCrLf3() throws Exception {
        XmlFormatPreferences prefs = XmlFormatPreferences.defaults();
        prefs.removeEmptyLines = true;

        check(
                // Before
                "<root>\r\n" +
                "\r\n" +
                "\r\n" +
                "     [<element/>\r\n" +
                "   <second/>]\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "</root>\r\n",

                // Insert
                "    <element/>\r\n" +
                "    <second/>",

                // After
                "<root>\r\n" +
                "    <element/>\r\n" +
                "    <second/>\r\n" +
                "</root>\r\n",

                prefs);
    }


    public void testCrlf4() throws Exception {
        check(
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\r\n" +
            "    android:layout_width=\"match_parent\"\r\n" +
            "    android:layout_height=\"match_parent\" >\r\n" +
            "\r\n" +
            "    [<TextView\r\n" +
            "        android:layout_width=\"wrap_content\"\r\n" +
            "        android:layout_height=\"wrap_content\"\r\n" +
            "          android:layout_centerHorizontal=\"true\"\r\n" +
            "        android:layout_centerVertical=\"true\"\r\n" +
            "        android:text=\"foo\"\r\n" +
            "        tools:context=\".MainActivity\" />]\r\n" +
            "\r\n" +
            "</RelativeLayout>\r\n",

            // Insert
            "\r\n" +
            "    <TextView\r\n" +
            "        android:layout_width=\"wrap_content\"\r\n" +
            "        android:layout_height=\"wrap_content\"\r\n" +
            "        android:layout_centerHorizontal=\"true\"\r\n" +
            "        android:layout_centerVertical=\"true\"\r\n" +
            "        android:text=\"foo\"\r\n" +
            "        tools:context=\".MainActivity\" />\r\n",

            // After
            "<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\r\n" +
            "    android:layout_width=\"match_parent\"\r\n" +
            "    android:layout_height=\"match_parent\" >\r\n" +
            "\r\n" +
            "    <TextView\r\n" +
            "        android:layout_width=\"wrap_content\"\r\n" +
            "        android:layout_height=\"wrap_content\"\r\n" +
            "        android:layout_centerHorizontal=\"true\"\r\n" +
            "        android:layout_centerVertical=\"true\"\r\n" +
            "        android:text=\"foo\"\r\n" +
            "        tools:context=\".MainActivity\" />\r\n" +
            "\r\n" +
            "</RelativeLayout>\r\n",

            XmlFormatPreferences.defaults());
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index f574189..183e7c1 100644

//Synthetic comment -- @@ -235,6 +235,7 @@
Position start = null;
int line = 0;
int lineOffset = 0;
        char prev = 0;
for (int offset = 0; offset <= size; offset++) {
if (offset == startOffset) {
start = new DefaultPosition(line, offset - lineOffset, offset);
//Synthetic comment -- @@ -246,8 +247,14 @@
char c = contents.charAt(offset);
if (c == '\n') {
lineOffset = offset + 1;
                if (prev != '\r') {
                    line++;
                }
            } else if (c == '\r') {
line++;
                lineOffset = offset + 1;
}
            prev = c;
}
return Location.create(file);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index aafcd5c..22a6c9d 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 121;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -102,6 +102,7 @@
issues.add(TranslationDetector.MISSING);
issues.add(HardcodedValuesDetector.ISSUE);
issues.add(Utf8Detector.ISSUE);
        issues.add(DosLineEndingDetector.ISSUE);
issues.add(ProguardDetector.WRONGKEEP);
issues.add(ProguardDetector.SPLITCONFIG);
issues.add(PxUsageDetector.PX_ISSUE);
//Synthetic comment -- @@ -306,7 +307,7 @@
// to give a hint to the user that some fixes don't require manual work

if (sAdtFixes == null) {
            sAdtFixes = new HashSet<Issue>(25);
sAdtFixes.add(InefficientWeightDetector.INEFFICIENT_WEIGHT);
sAdtFixes.add(AccessibilityDetector.ISSUE);
sAdtFixes.add(InefficientWeightDetector.BASELINE_WEIGHTS);
//Synthetic comment -- @@ -327,6 +328,10 @@
sAdtFixes.add(UseCompoundDrawableDetector.ISSUE);
sAdtFixes.add(ApiDetector.UNSUPPORTED);
sAdtFixes.add(TypoDetector.ISSUE);
            sAdtFixes.add(ManifestOrderDetector.ALLOW_BACKUP);
            sAdtFixes.add(MissingIdDetector.ISSUE);
            sAdtFixes.add(TranslationDetector.MISSING);
            sAdtFixes.add(DosLineEndingDetector.ISSUE);
}

return sAdtFixes.contains(issue);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DosLineEndingDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DosLineEndingDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..c2e735c

//Synthetic comment -- @@ -0,0 +1,114 @@
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

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Document;

/**
 * Checks that the line endings in DOS files are consistent
 */
public class DosLineEndingDetector extends LayoutDetector {
    /** Detects mangled DOS line ending documents */
    public static final Issue ISSUE = Issue.create(
            "MangledCRLF", //$NON-NLS-1$
            "Checks that files with DOS line endings are consistent",

            "On Windows, line endings are typically recorded as carriage return plus " +
            "newline: \\r\\n.\n" +
            "\n" +
            "This detector looks for invalid line endings with repeated carriage return " +
            "characters (without newlines). Previous versions of the ADT plugin could " +
            "accidentally introduce these into the file, and when editing the file, the " +
            "editor could produce confusing visual artifacts.",

            Category.CORRECTNESS,
            2,
            Severity.ERROR,
            DosLineEndingDetector.class,
            Scope.RESOURCE_FILE_SCOPE)
            .setMoreInfo("https://bugs.eclipse.org/bugs/show_bug.cgi?id=375421"); //$NON-NLS-1$

     /** Constructs a new {@link DosLineEndingDetector} */
    public DosLineEndingDetector() {
    }

    @Override
    public @NonNull Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
    public void visitDocument(@NonNull XmlContext context, @NonNull Document document) {
        String contents = context.getContents();
        if (contents == null) {
            return;
        }

        // We could look for *consistency* and complain if you mix \n and \r\n too,
        // but that isn't really a problem (most editors handle it) so let's
        // not complain needlessly.

        char prev =  0;
        for (int i = 0, n = contents.length(); i < n; i++) {
            char c = contents.charAt(i);
            if (c == '\r' && prev == '\r') {
                String message = "Incorrect line ending: found carriage return (\\r) without " +
                        "corresponding newline (\\n)";

                // Mark the whole line as the error range, since pointing just to the
                // line ending makes the error invisible in IDEs and error reports etc
                // Find the most recent non-blank line
                boolean blankLine = true;
                for (int index = i - 2; index < i; index++) {
                    char d = contents.charAt(index);
                    if (!Character.isWhitespace(d)) {
                        blankLine = false;
                    }
                }

                int lineBegin = i;
                for (int index = i - 2; index >= 0; index--) {
                    char d = contents.charAt(index);
                    if (d == '\n') {
                        lineBegin = index + 1;
                        if (!blankLine) {
                            break;
                        }
                    } else if (!Character.isWhitespace(d)) {
                        blankLine = false;
                    }
                }

                int lineEnd = Math.min(contents.length(), i + 1);
                Location location = Location.create(context.file, contents, lineBegin, lineEnd);
                context.report(ISSUE, document.getDocumentElement(), location, message, null);
                return;
            }
            prev = c;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DosLineEndingDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DosLineEndingDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3682420

//Synthetic comment -- @@ -0,0 +1,49 @@
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

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class DosLineEndingDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new DosLineEndingDetector();
    }

    public void test() throws Exception {
        assertEquals(
            "res/layout/crcrlf.xml:4: Error: Incorrect line ending: found carriage return (\\r) without corresponding newline (\\n) [MangledCRLF]\n" +
            "    android:layout_height=\"match_parent\" >\r\n" +
            "^\n" +
            "1 errors, 0 warnings\n",
            lintProject("res/layout/crcrlf.xml"));
    }

    public void testIgnore() throws Exception {
        assertEquals(
            "No warnings.",
            lintProject("res/layout/crcrlf_ignore.xml"));
    }

    public void testNegative() throws Exception {
        // Make sure we don't get warnings for a correct file
        assertEquals(
            "No warnings.",
            lintProject("res/layout/layout1.xml"));
    }
}







