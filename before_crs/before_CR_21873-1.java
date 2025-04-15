/*Fix AAPT error range

Turns out the AAPT errors always list a line number that corresponds
to the line where the element starts.  If the error occurs in an
attribute that is on a subsequent line, the code to identify the
editor offset failed.

This changeset addresses that, by using a document forward search to
locate the attribute. The quickfix code was also checking for an exact
line number match, so that code is tweaked as well.

Change-Id:Ib648d2e2e7ad3ba082fd3c6a48e7f077b3aebc10*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index f433570..bf7287b 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
//Synthetic comment -- @@ -417,7 +418,6 @@
if (f2 instanceof IFile) {
Matcher matcher = sValueRangePattern.matcher(message);
if (matcher.find()) {
                String property = matcher.group(1);
String value = matcher.group(2);
IFile iFile = (IFile) f2;
IDocumentProvider provider = new TextFileDocumentProvider();
//Synthetic comment -- @@ -426,11 +426,19 @@
IDocument document = provider.getDocument(iFile);
if (document != null) {
IRegion lineInfo = document.getLineInformation(line - 1);
                        String text = document.get(lineInfo.getOffset(), lineInfo.getLength());
                        int propertyIndex = text.indexOf(property);
                        int valueIndex = text.indexOf(value, propertyIndex + 1);
                        if (valueIndex != -1) {
                            startOffset = lineInfo.getOffset() + valueIndex;
endOffset = startOffset + value.length();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java
//Synthetic comment -- index 57ba97c..55de6b1 100644

//Synthetic comment -- @@ -139,26 +139,25 @@
// Look for a match on the same line as the caret.
int offset = invocationContext.getOffset();
IDocument document = sourceViewer.getDocument();
                int currentLine = document.getLineOfOffset(offset) + 1;

for (IMarker marker : markers) {
                    int line = marker.getAttribute(IMarker.LINE_NUMBER, -1);
                    if (line == currentLine) {
                        String message = marker.getAttribute(IMarker.MESSAGE, ""); //$NON-NLS-1$
                        if (message.contains(getTargetMarkerErrorMessage())) {
                            int start = marker.getAttribute(IMarker.CHAR_START, 0);
                            int end = marker.getAttribute(IMarker.CHAR_END, 0);
                            if (end > start) {
                                int length = end - start;
                                String resource = document.get(start, length);
                                // Can only offer create value for non-framework value
                                // resources
                                if (ResourceChooser.canCreateResource(resource)) {
                                    IProject project = editor.getProject();
                                    return new ICompletionProposal[] {
                                        new CreateResourceProposal(project, resource)
                                    };
                                }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java
//Synthetic comment -- index f70f9af..a201ae2 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
String filePath = osRoot + File.separator + fileRelativePath;
errors.add(filePath + ":7: error: Error: No resource found that matches the given name"
+ " (at 'text' with value '@string/firststring').");
        errors.add(filePath + ":9: error: Error: No resource found that matches the given name"
+ " (at 'layout_width' with value '@dimen/testdimen').");
errors.add(filePath + ":13: error: Error: No resource found that matches the given name"
+ " (at 'layout' with value '@layout/testlayout').");







