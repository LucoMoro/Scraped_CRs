/*Prevent output to console when closing several projects+libs at once.

This is due to link updates on closed projects.

Change-Id:I64eda43cbe574efc6e76f2a83490b5cb2585dfc4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index a4c216c..0b34753 100644

//Synthetic comment -- @@ -1245,6 +1245,9 @@
* @return an {@link IStatus} with the status of the action.
*/
private IStatus linkProjectAndLibrary(LinkLibraryBundle bundle, IProgressMonitor monitor) {
        if (bundle.mProject.isOpen() == false) {
            return Status.OK_STATUS;
        }
try {
// add the library to the list of dynamic references. This is necessary to receive
// notifications that the library content changed in the builders.
//Synthetic comment -- @@ -1308,6 +1311,9 @@

// loop on the projects to add.
for (IProject library : bundle.mLibraryProjects) {
                if (library.isOpen() == false) {
                    continue;
                }
final String libName = library.getName();
final String varName = getLibraryVariableName(libName);








