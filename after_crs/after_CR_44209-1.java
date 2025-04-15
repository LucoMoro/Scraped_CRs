/*Fix create new layout configuration bugs

There were two bugs:
(1) It would fail if you added a configuration into a folder that
    already existed
(2) It would duplicate the on-disk contents of the layout rather
    than the currently edited (not yet saved) contents of the layout

Change-Id:I63b2f80e61d62cdd6f41c9ac19b9cb57b0548319*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CreateNewConfigJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CreateNewConfigJob.java
//Synthetic comment -- index f0698e6..44cd081 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceFolderType;
import com.google.common.base.Charsets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -35,8 +36,8 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/** Job which creates a new layout file for a given configuration */
class CreateNewConfigJob extends Job {
//Synthetic comment -- @@ -64,19 +65,17 @@
IFolder res = (IFolder) mFromFile.getParent().getParent();

IFolder newParentFolder = res.getFolder(folderName);
            AdtUtils.ensureExists(newParentFolder);
            final IFile file = newParentFolder.getFile(mFromFile.getName());
            if (file.exists()) {
                String message = String.format("File 'res/%1$s/%2$s' already exists!",
                        folderName, mFromFile.getName());
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, message);
}

            // Read current document contents instead of from file: mFromFile.getContents()
            String text = mEditor.getEditorDelegate().getEditor().getStructuredDocument().get();
            ByteArrayInputStream input = new ByteArrayInputStream(text.getBytes(Charsets.UTF_8));
file.create(input, false, monitor);
input.close();








