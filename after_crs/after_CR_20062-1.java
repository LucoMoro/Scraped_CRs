/*Enable code specific to Eclipse 3.5

Change-Id:Ib750dab8839f919474fd6e92b9960f14d355e4ca*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/RenamePackageAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/RenamePackageAction.java
//Synthetic comment -- index dfbaf55..d50fa95 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
//Synthetic comment -- @@ -93,8 +94,6 @@
project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
}
if (project != null) {
// It is advisable that the user saves before proceeding,
// revealing any compilation errors. The following lines
// enforce a save as a convenience.
//Synthetic comment -- @@ -103,9 +102,6 @@
if (save_helper.saveEditors(AdtPlugin.getDisplay().getActiveShell())) {
promptNewName(project);
}
}
}
}







