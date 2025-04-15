/*ADT: fix unittests.

Change-Id:I258d393b5297d916b58ed3574b77f521968a13c0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index 2c53bea..04dc8ec 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.manager.SingleResourceFile;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
//Synthetic comment -- @@ -253,11 +254,11 @@
/**
* Adds a folder to the given {@link ProjectResources} with the given
* {@link FolderConfiguration}. The folder is filled with files from the provided list.
     * @param resources the {@link ResourceRepository} in which to add the folder.
* @param config the {@link FolderConfiguration} for the created folder.
* @param memberList the list of files for the folder.
*/
    private void addFolder(ResourceRepository resources, FolderConfiguration config,
IFile[] memberList) throws Exception {

// figure out the folder name based on the configuration
//Synthetic comment -- @@ -275,15 +276,15 @@
}
}

    /** Calls ResourceRepository.add() method via reflection to circumvent access
* restrictions that are enforced when running in the plug-in environment
* ie cannot access package or protected members in a different plug-in, even
* if they are in the same declared package as the accessor
*/
    private ResourceFolder _addProjectResourceFolder(ResourceRepository resources,
FolderConfiguration config, IFolder folder) throws Exception {

        Method addMethod = ResourceRepository.class.getDeclaredMethod("add",
ResourceFolderType.class, FolderConfiguration.class,
IAbstractFolder.class);
addMethod.setAccessible(true);







