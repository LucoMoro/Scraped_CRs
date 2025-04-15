/*Pick configuration for Java hyperlinks

When you request declaration hyperlinks in an XML file, the hyperlink
support looks at the corresponding Layout editor's configuration
chooser and uses the current configuration to decide which match to
list on top. Therefore, if you are looking at a particular language
and you ask for declarations for a given @string resource, the
translation for the current language is listed first.

In Java files, there is no corresponding layout editor, so it will
just display the base folder (values/) match rather than any
particular language (or other qualifiers for that matter).

This changeset adds a check to the code which computes a configuration
to use for default matching. It now looks for other open layout
editors in the workspace and (for any that are in the same project) it
will use that editor's configuration instead as the default key.

Change-Id:I0dc9d4a7970c5aebd07d3e88877ac41fe49baab8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 1d12e65..5e662bf 100644

//Synthetic comment -- @@ -111,6 +111,7 @@
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
//Synthetic comment -- @@ -750,10 +751,11 @@
}

// Create a configuration from the current file
IEditorInput editorInput = editor.getEditorInput();
if (editorInput instanceof FileEditorInput) {
IFile file = ((FileEditorInput) editorInput).getFile();
                IProject project = file.getProject();
ProjectResources pr = ResourceManager.getInstance().getProjectResources(project);
IContainer parent = file.getParent();
if (parent instanceof IFolder) {
//Synthetic comment -- @@ -763,6 +765,22 @@
}
}
}
}

return null;







