/*Fix an issue where src files can be deleted in ADT.

Older projects generated the java class (R, aidl) into
the main src folder.

There is code in ADT to migrate them to the new model
(where generated classes go in gen/) by removing derived
resources from the source folder. This is also used
by the clean feature of the pre-compiler builder
to clean the content of gen.

To make it better, in ADT 0.9.8, we added something to
delete the folder containing the derived resources.
Except this doesn't check if the folder is not empty
after the derived resources have been deleted (or
not if it contained non-derived resources).

this means importing older projects (or possibly
team projects -- seehttp://b.android.com/11347)
would delete the whole content of the source folder.

This change makes sure that only folders for which
all members have been deleted are deleted.

Change-Id:I1ddbb3bd4c37859c7ddbf660377c598bae246269*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BaseBuilder.java
//Synthetic comment -- index 72e09c1..be3c10a 100644

//Synthetic comment -- @@ -434,21 +434,34 @@
removeDerivedResources(rootResource, false, monitor);
}

private void removeDerivedResources(IResource rootResource, boolean deleteRoot,
            IProgressMonitor monitor)
            throws CoreException {
if (rootResource.exists()) {
if (rootResource.getType() == IResource.FOLDER) {
IFolder folder = (IFolder)rootResource;
IResource[] members = folder.members();
for (IResource member : members) {
removeDerivedResources(member, true /*deleteRoot*/, monitor);
}
            } else if (rootResource.isDerived()) {
                rootResource.getLocation().toFile().delete();
}

            if (deleteRoot) {
rootResource.getLocation().toFile().delete();
}
}







