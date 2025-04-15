/*Make hardcoded text detector also check menu files

Change-Id:I1b11d5e6650434fd5e57af05ff86b2d66a0a84db*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ExtractStringFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ExtractStringFix.java
//Synthetic comment -- index 196743b..7eafd43 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.lint;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;

//Synthetic comment -- @@ -28,6 +28,7 @@
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
//Synthetic comment -- @@ -59,16 +60,13 @@
@Override
protected void apply(IDocument document, IStructuredModel model, Node node, int start,
int end) {
        // Invoke refactoring
        LayoutEditorDelegate delegate =
            LayoutEditorDelegate.fromEditor(AdtUtils.getActiveEditor());

        if (delegate != null) {
IFile file = (IFile) mMarker.getResource();
ITextSelection selection = new TextSelection(start, end - start);

ExtractStringRefactoring refactoring =
                new ExtractStringRefactoring(file, delegate.getEditor(), selection);
RefactoringWizard wizard = new ExtractStringWizard(refactoring, file.getProject());
RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
try {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java
//Synthetic comment -- index b7f60df..74522ba 100644

//Synthetic comment -- @@ -22,8 +22,10 @@
import static com.android.SdkConstants.ATTR_LABEL;
import static com.android.SdkConstants.ATTR_PROMPT;
import static com.android.SdkConstants.ATTR_TEXT;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -52,7 +54,10 @@
"you have to repeat the actual text (and keep it up to date when making changes)\n" +
"\n" +
"* The application cannot be translated to other languages by just adding new " +
            "translations for existing string resources.",

Category.I18N,
5,
//Synthetic comment -- @@ -74,15 +79,24 @@
@Override
public Collection<String> getApplicableAttributes() {
return Arrays.asList(
ATTR_TEXT,
ATTR_CONTENT_DESCRIPTION,
ATTR_HINT,
ATTR_LABEL,
                ATTR_PROMPT
);
}

@Override
public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
String value = attribute.getValue();
if (value.length() > 0 && (value.charAt(0) != '@' && value.charAt(0) != '?')) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HardcodedValuesDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HardcodedValuesDetectorTest.java
//Synthetic comment -- index 6bef3b7..b326fd4 100644

//Synthetic comment -- @@ -33,11 +33,30 @@
"res/layout/accessibility.xml:6: Warning: [I18N] Hardcoded string \"Button\", should use @string resource [HardcodedText]\n" +
"    <Button android:text=\"Button\" android:id=\"@+id/button2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\"></Button>\n" +
"            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "0 errors, 2 warnings\n" +
            "",
lintFiles("res/layout/accessibility.xml"));
}

public void testSuppress() throws Exception {
// All but one errors in the file contain ignore attributes - direct, inherited
// and lists







