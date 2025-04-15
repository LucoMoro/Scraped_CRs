/*Fix an issue where src files can be deleted in ADT.

Older projects generated the java class (R, aidl) into
the main src folder.

There is code in ADT to migrate them to the new model
(where generated classes go in gen/), to remove derived
resources from the source folder. This is also used
by the clean feature of the pre-compiler builder
to clean the content of gen.

To make it better, in ADT 0.9.8, we added something to
delete the folder containing the derived resources.
Except this doesn't check if the folder is not empty
from the derived resource deletion.

this means importing older projects (or possibly
team projects -- seehttp://b.android.com/11347)
would delete the whole content of the source folder.

This change makes sure that only folder for which
all members have been deleted are deleted.

Change-Id:I04b6d986fb3e454a259213fcda197400e9e7c974*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 4e9c7f4..c74b6da 100644

//Synthetic comment -- @@ -368,23 +368,43 @@
removeDerivedResources(rootResource, false, monitor);
}

    /**
     * delete a resource and its children. returns true if the root resource was deleted.
     * @param rootResource the root resource
     * @param deleteRoot whether to delete the root folder.
     * @param monitor a progress monitor.
     * @return true if the root resource was deleted.
     * @throws CoreException
     */
    private boolean removeDerivedResources(IResource rootResource, boolean deleteRoot,
IProgressMonitor monitor)
throws CoreException {
if (rootResource.exists()) {
            // if it's a folder, delete derived member.
if (rootResource.getType() == IResource.FOLDER) {
IFolder folder = (IFolder)rootResource;
IResource[] members = folder.members();
                boolean allMembersDeleted = true;
for (IResource member : members) {
                    allMembersDeleted &= removeDerivedResources(member, true /*deleteRoot*/,
                            monitor);
}

                // if all the members were deleted and we're asked to delete the root
                // then it gets deleted too.
                if (deleteRoot && allMembersDeleted) {
                    rootResource.getLocation().toFile().delete();
                    return true;
                }
}

            // if the root resource is derived, delete it.
            if (rootResource.isDerived()) {
rootResource.getLocation().toFile().delete();
                return true;
}
}

        return false;
}
}







