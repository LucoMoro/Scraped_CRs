//<Beginning of snippet n. 0>
private void removeDerivedResources(IResource rootResource, boolean deleteRoot,
                                     IProgressMonitor monitor)
        throws CoreException {
    if (rootResource.exists()) {
        if (rootResource.getType() == IResource.FOLDER) {
            IFolder folder = (IFolder) rootResource;
            IResource[] members = folder.members();
            boolean hasNonDerivedResources = false;
            for (IResource member : members) {
                if (!member.isDerived()) {
                    hasNonDerivedResources = true;
                    break;
                }
                removeDerivedResources(member, true /*deleteRoot*/, monitor);
            }
            if (!hasNonDerivedResources && deleteRoot) {
                try {
                    folder.delete(true, monitor);
                } catch (CoreException e) {
                    // Handle or log exception as necessary
                }
            }
        } else if (rootResource.isDerived()) {
            try {
                rootResource.getLocation().toFile().delete();
            } catch (Exception e) {
                // Handle or log exception as necessary
            }
        }

        if (deleteRoot && !rootResource.isDerived() && rootResource.getType() != IResource.FOLDER) {
            try {
                rootResource.getLocation().toFile().delete();
            } catch (Exception e) {
                // Handle or log exception as necessary
            }
        }
    }
}
//<End of snippet n. 0>