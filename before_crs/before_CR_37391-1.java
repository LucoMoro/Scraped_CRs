/*Fix 32527: Menu XML editor "add" button problem on Eclipse 4.2

This adds a workaround forhttps://bugs.eclipse.org/bugs/show_bug.cgi?id=381418Change-Id:I6a68602cd65a8656e4a6f62bf3b181045956136c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/NewItemSelectionDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/NewItemSelectionDialog.java
//Synthetic comment -- index d880f3d..385a53a 100644

//Synthetic comment -- @@ -29,9 +29,11 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
//Synthetic comment -- @@ -278,6 +280,15 @@
createFilterText(contents);
createFilteredList(contents);

// Initialize the list state.
// This must be done after the filtered list as been created.
chooseNode(mChosenRootNode);







