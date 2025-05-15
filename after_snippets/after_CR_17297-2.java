
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>








