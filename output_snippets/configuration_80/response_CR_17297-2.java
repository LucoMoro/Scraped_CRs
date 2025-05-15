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

            if (!hasNonDerivedResources) {
                deleteRoot = true; // Set deleteRoot only if no non-derived resources are present
                folder.getLocation().toFile().delete();
                // Logging can be added here to indicate successful deletion of the folder
            }
        } else if (rootResource.isDerived()) {
            rootResource.getLocation().toFile().delete();
            // Logging can be added here for the deletion of derived resources
        }
    }
}
//<End of snippet n. 0>