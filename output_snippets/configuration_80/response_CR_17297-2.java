//<Beginning of snippet n. 0>
removeDerivedResources(rootResource, false, monitor);
}

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
            if (!hasNonDerivedResources && deleteRoot) {
                boolean deleted = rootResource.getLocation().toFile().delete();
                if (deleted) {
                    // Add logging for successful deletion
                    System.out.println("Deleted folder: " + folder.getName());
                } else {
                    // Handle deletion failure
                    System.err.println("Failed to delete folder: " + folder.getName());
                }
            }
        } else if (rootResource.isDerived()) {
            boolean deleted = rootResource.getLocation().toFile().delete();
            if (deleted) {
                // Add logging for deletion of derived resource
                System.out.println("Deleted resource: " + rootResource.getName());
            } else {
                // Handle deletion failure
                System.err.println("Failed to delete resource: " + rootResource.getName());
            }
        }
    }
}
//<End of snippet n. 0>