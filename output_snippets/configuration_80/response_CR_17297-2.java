//<Beginning of snippet n. 0>
private void removeDerivedResources(IResource rootResource, boolean deleteRoot,
IProgressMonitor monitor) throws CoreException {
    if (rootResource.exists()) {
        if (rootResource.getType() == IResource.FOLDER) {
            IFolder folder = (IFolder) rootResource;
            IResource[] members = folder.members();
            boolean hasNonDerivedResources = false;
            for (IResource member : members) {
                if (!member.isDerived()) {
                    hasNonDerivedResources = true;
                }
                removeDerivedResources(member, true /*deleteRoot*/, monitor);
            }
            if (!hasNonDerivedResources && members.length == 0) {
                if (deleteRoot) {
                    folder.getLocation().toFile().delete();
                }
            }
        } else if (rootResource.isDerived()) {
            rootResource.getLocation().toFile().delete();
        }
    }
}
//<End of snippet n. 0>