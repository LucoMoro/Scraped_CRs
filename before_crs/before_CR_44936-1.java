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
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
//Synthetic comment -- @@ -360,7 +361,8 @@
* adjusted (for example to make the edit smaller if the beginning and/or end is
* identical, and so on)
*/
    private static ReplaceEdit createReplaceEdit(IStructuredDocument document, int replaceStart,
int replaceEnd, String formatted, XmlFormatPreferences prefs) {
// If replacing a node somewhere in the middle, start the replacement at the
// beginning of the current line
//Synthetic comment -- @@ -399,7 +401,7 @@
if (c == '\n') {
beginsWithNewline = true;
break;
            } else if (!Character.isWhitespace(c)) {
break;
}
}
//Synthetic comment -- @@ -411,6 +413,9 @@
replaceStart = prevNewlineIndex;
}
prevNewlineIndex = index;
} else if (!Character.isWhitespace(c)) {
break;
}
//Synthetic comment -- @@ -423,16 +428,16 @@
}

// Search forwards too
        prevNewlineIndex = -1;
try {
int max = document.getLength();
for (index = replaceEnd; index < max; index++) {
char c = document.getChar(index);
if (c == '\n') {
                    if (prevNewlineIndex != -1) {
                        replaceEnd = prevNewlineIndex + 1;
}
                    prevNewlineIndex = index;
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

        if (prefs.removeEmptyLines && prevNewlineIndex != -1 && endsWithNewline) {
            replaceEnd = prevNewlineIndex + 1;
}

// Figure out how much of the before and after strings are identical and narrow








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlFormatPreferences.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlFormatPreferences.java
//Synthetic comment -- index 04441fd..05c8a7f 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.formatting;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AttributeSortOrder;
//Synthetic comment -- @@ -67,6 +68,7 @@
*
* @return an {@link XmlFormatPreferences} object
*/
public static XmlFormatPreferences create() {
XmlFormatPreferences p = new XmlFormatPreferences();
AdtPrefs prefs = AdtPrefs.getPrefs();
//Synthetic comment -- @@ -80,6 +82,16 @@
return p;
}

// The XML module settings do not have a public API. We should replace this with JDT
// settings anyway since that's more likely what users have configured and want applied
// to their XML files








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/DosLineEndingsFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/DosLineEndingsFix.java
new file mode 100644
//Synthetic comment -- index 0000000..9a5456b

//Synthetic comment -- @@ -0,0 +1,64 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index ab850e1..18d72db 100644

//Synthetic comment -- @@ -531,6 +531,9 @@
*/
private static Pair<Integer, Integer> adjustOffsets(IDocument doc, int startOffset,
int endOffset) {
if (doc != null) {
while (endOffset > startOffset && endOffset < doc.getLength()) {
try {
//Synthetic comment -- @@ -552,6 +555,9 @@
char c = doc.getChar(lineEnd);
if (c == '\n' || c == '\r') {
endOffset = lineEnd;
break;
}
} catch (BadLocationException e) {
//Synthetic comment -- @@ -562,6 +568,13 @@
}
}

return Pair.of(startOffset, endOffset);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java
//Synthetic comment -- index 0b074bb..feb6bb5 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.checks.DetectMissingPrefix;
import com.android.tools.lint.checks.HardcodedValuesDetector;
import com.android.tools.lint.checks.InefficientWeightDetector;
import com.android.tools.lint.checks.ManifestOrderDetector;
//Synthetic comment -- @@ -170,6 +171,7 @@
sFixes.put(UseCompoundDrawableDetector.ISSUE.getId(),
UseCompoundDrawableDetectorFix.class);
sFixes.put(TypoDetector.ISSUE.getId(), TypoFix.class);
// ApiDetector.UNSUPPORTED is provided as a marker resolution rather than
// a quick assistant (the marker resolution adds a suitable @TargetApi annotation)
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/TypoFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/TypoFix.java
//Synthetic comment -- index 4358410..8a83364 100644

//Synthetic comment -- @@ -105,7 +105,7 @@
String message = mMarker.getAttribute(IMarker.MESSAGE, "");
String typo = TypoDetector.getTypo(message);
List<String> replacements = TypoDetector.getSuggestions(message);
        if (!replacements.isEmpty() && typo != null) {
List<LintFix> allFixes = new ArrayList<LintFix>(replacements.size());
for (String replacement : replacements) {
TypoFix fix = new TypoFix(mId, mMarker);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategyTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategyTest.java
new file mode 100644
//Synthetic comment -- index 0000000..4fe2a7f

//Synthetic comment -- @@ -0,0 +1,331 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index f574189..183e7c1 100644

//Synthetic comment -- @@ -235,6 +235,7 @@
Position start = null;
int line = 0;
int lineOffset = 0;
for (int offset = 0; offset <= size; offset++) {
if (offset == startOffset) {
start = new DefaultPosition(line, offset - lineOffset, offset);
//Synthetic comment -- @@ -246,8 +247,14 @@
char c = contents.charAt(offset);
if (c == '\n') {
lineOffset = offset + 1;
line++;
}
}
return Location.create(file);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index aafcd5c..22a6c9d 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 120;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -102,6 +102,7 @@
issues.add(TranslationDetector.MISSING);
issues.add(HardcodedValuesDetector.ISSUE);
issues.add(Utf8Detector.ISSUE);
issues.add(ProguardDetector.WRONGKEEP);
issues.add(ProguardDetector.SPLITCONFIG);
issues.add(PxUsageDetector.PX_ISSUE);
//Synthetic comment -- @@ -306,7 +307,7 @@
// to give a hint to the user that some fixes don't require manual work

if (sAdtFixes == null) {
            sAdtFixes = new HashSet<Issue>(20);
sAdtFixes.add(InefficientWeightDetector.INEFFICIENT_WEIGHT);
sAdtFixes.add(AccessibilityDetector.ISSUE);
sAdtFixes.add(InefficientWeightDetector.BASELINE_WEIGHTS);
//Synthetic comment -- @@ -327,6 +328,10 @@
sAdtFixes.add(UseCompoundDrawableDetector.ISSUE);
sAdtFixes.add(ApiDetector.UNSUPPORTED);
sAdtFixes.add(TypoDetector.ISSUE);
}

return sAdtFixes.contains(issue);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DosLineEndingDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DosLineEndingDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..c2e735c

//Synthetic comment -- @@ -0,0 +1,114 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DosLineEndingDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DosLineEndingDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3682420

//Synthetic comment -- @@ -0,0 +1,49 @@







