/*Fix 33912: Can't create a project, delete, then recreate it

Fixhttp://code.google.com/p/android/issues/detail?id=33912Change-Id:Ieaeb9a4db641efa6591bd9777b3c7731d74952fb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 53b6c3c..434384c 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
//Synthetic comment -- @@ -66,6 +67,7 @@
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
//Synthetic comment -- @@ -856,7 +858,7 @@

contents = format(mProject, contents, to);
IFile targetFile = getTargetFile(to);
            TextFileChange change = createTextChange(targetFile);
MultiTextEdit rootEdit = new MultiTextEdit();
rootEdit.addChild(new InsertEdit(0, contents));
change.setEdit(rootEdit);
//Synthetic comment -- @@ -908,7 +910,7 @@
return contents;
}

    private static TextFileChange createTextChange(IFile targetFile) {
String fileName = targetFile.getName();
String message;
if (targetFile.exists()) {
//Synthetic comment -- @@ -917,7 +919,29 @@
message = String.format("Create %1$s", fileName);
}

        TextFileChange change = new TextFileChange(message, targetFile);
change.setTextType(fileName.substring(fileName.lastIndexOf('.') + 1));
return change;
}
//Synthetic comment -- @@ -989,7 +1013,7 @@
String newFile = Files.toString(src, Charsets.UTF_8);
newFile = format(mProject, newFile, path);

                TextFileChange addFile = createTextChange(file);
addFile.setEdit(new InsertEdit(0, newFile));
mTextChanges.add(addFile);
} else {







