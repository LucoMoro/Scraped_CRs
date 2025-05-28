//<Beginning of snippet n. 0>
private void removeDerivedResources(IResource rootResource, boolean deleteRoot, IProgressMonitor monitor) 
throws CoreException {
    if (rootResource.exists()) {
        if (rootResource.getType() == IResource.FOLDER) {
            IFolder folder = (IFolder) rootResource;
            IResource[] members = folder.members();
            boolean nonDerivedExists = false;

            for (IResource member : members) {
                if (!member.isDerived()) {
                    nonDerivedExists = true;
                    break;
                }
            }

            if (!nonDerivedExists) {
                for (IResource member : members) {
                    removeDerivedResources(member, true /* deleteRoot */, monitor);
                }
                
                // Re-check for non-derived resources after derived resources deletion
                members = folder.members();
                nonDerivedExists = false;
                for (IResource member : members) {
                    if (!member.isDerived()) {
                        nonDerivedExists = true;
                        break;
                    }
                }

                if (!nonDerivedExists && deleteRoot) {
                    try {
                        rootResource.getLocation().toFile().delete();
                        // Log deleted folder
                    } catch (Exception e) {
                        // Handle exceptions
                    }
                }
            }
        } else if (rootResource.isDerived()) {
            try {
                rootResource.getLocation().toFile().delete();
                // Log deleted resource
            } catch (Exception e) {
                // Handle exceptions
            }
        }

        if (deleteRoot && !rootResource.isDerived()) {
            // Prompt user for confirmation here
            boolean confirmed = getUserConfirmation(); // Placeholder for user confirmation logic
            if (confirmed) {
                try {
                    rootResource.getLocation().toFile().delete();
                    // Log deleted resource
                } catch (Exception e) {
                    // Handle exceptions
                }
            }
        }
    }
}

private boolean getUserConfirmation() {
    // Implementation for user confirmation dialog
    return true; // Change this to actual user confirmation logic
}
//<End of snippet n. 0>