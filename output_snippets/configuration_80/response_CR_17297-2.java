//<Beginning of snippet n. 0>
private void removeDerivedResources(IResource rootResource, boolean deleteRoot, IProgressMonitor monitor) 
throws CoreException {
    if (rootResource.exists()) {
        if (rootResource.getType() == IResource.FOLDER) {
            IFolder folder = (IFolder) rootResource;
            IResource[] members = folder.members();
            boolean nonDerivedExists = checkNonDerivedExists(members);

            if (!nonDerivedExists) {
                for (IResource member : members) {
                    removeDerivedResources(member, true, monitor);
                }
                
                // Re-check for non-derived resources after derived resources deletion
                members = folder.members();
                nonDerivedExists = checkNonDerivedExists(members);

                if (!nonDerivedExists && deleteRoot) {
                    deleteResource(rootResource);
                }
            }
        } else if (rootResource.isDerived()) {
            deleteResource(rootResource);
        }

        if (deleteRoot && !rootResource.isDerived()) {
            boolean confirmed = getUserConfirmation();
            if (confirmed) {
                deleteResource(rootResource);
            }
        }
    }
}

private boolean checkNonDerivedExists(IResource[] members) {
    for (IResource member : members) {
        if (!member.isDerived()) {
            return true;
        }
    }
    return false;
}

private void deleteResource(IResource resource) {
    try {
        resource.getLocation().toFile().delete();
        // Log deleted resource
    } catch (Exception e) {
        // Handle specific exceptions and log the error
    }
}

private boolean getUserConfirmation() {
    // Implementation for user confirmation dialog
    return true; // Change this to actual user confirmation logic
}
//<End of snippet n. 0>