//<Beginning of snippet n. 0>
removeDerivedResources(rootResource, false, monitor);
}

private void removeDerivedResources(IResource rootResource, boolean deleteRoot, IProgressMonitor monitor) throws CoreException {
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

            if (hasNonDerivedResources) {
                if (deleteRoot) {
                    // User confirmation logic
                    if (!confirmDeletion(folder)) {
                        return;
                    }
                }
                return; // Prevent accidental deletion
            }

            try {
                members = folder.members(); // Re-fetch members after derived resources are removed
                if (members.length == 0) {
                    deleteResource(folder, monitor);
                }
            } catch (CoreException e) {
                throw new CoreException(new Status(IStatus.ERROR, "pluginId", "Error deleting folder", e));
            }
        } else if (rootResource.isDerived()) {
            deleteResource(rootResource, monitor);
        }

        if (deleteRoot) {
            deleteResource(rootResource, monitor);
        }
    }
}

private boolean confirmDeletion(IFolder folder) {
    // Implement actual user confirmation logic
    return true; // Placeholder for user confirmation
}

private void deleteResource(IResource resource, IProgressMonitor monitor) throws CoreException {
    try {
        boolean deleted = resource.getLocation().toFile().delete();
        if (!deleted) {
            throw new CoreException(new Status(IStatus.ERROR, "pluginId", "Failed to delete resource: " + resource.getName()));
        }
    } catch (SecurityException e) {
        throw new CoreException(new Status(IStatus.ERROR, "pluginId", "Security exception during deletion of " + resource.getName(), e));
    } catch (Exception e) {
        throw new CoreException(new Status(IStatus.ERROR, "pluginId", "Unexpected error during deletion of " + resource.getName(), e));
    }
}
//<End of snippet n. 0>