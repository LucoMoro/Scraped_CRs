/*Attempt to work around XML formatter state bug

Seehttps://bugs.eclipse.org/bugs/show_bug.cgi?id=375421Change-Id:I2b0c351bd85b7eb36bfca1fa975fffa8634a7445*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 465c1e6..6098483 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -940,7 +941,20 @@
* @param undoLabel if non null, the edit action will be run as a single undo event
*            and the label used as the name of the undoable action
*/
    private final void wrapEditXmlModel(Runnable editAction, String undoLabel) {
IStructuredModel model = null;
int undoReverseCount = 0;
try {
//Synthetic comment -- @@ -955,7 +969,7 @@
// own -- see http://code.google.com/p/android/issues/detail?id=15901
// for one such call chain. By nesting these calls several times
// we've incrementing the command count such that a couple of
                        // cancellations are ignored. Interfering which this mechanism may
// sound dangerous, but it appears that this undo-termination is
// done for UI reasons to anticipate what the user wants, and we know
// that in *our* scenarios we want the entire unit run as a single
//Synthetic comment -- @@ -995,8 +1009,6 @@
boolean oldIgnore = mIgnoreXmlUpdate;
try {
mIgnoreXmlUpdate = true;
                        // Notify the model we're done modifying it. This must *always* be executed.
                        model.changedModel();

if (AdtPrefs.getPrefs().getFormatGuiXml() && mFormatNode != null) {
if (mFormatNode == getUiRootNode()) {
//Synthetic comment -- @@ -1020,6 +1032,9 @@
mFormatChildren = false;
}

// Clean up the undo unit. This is done more than once as explained
// above for beginRecording.
for (int i = 0; i < undoReverseCount; i++) {







