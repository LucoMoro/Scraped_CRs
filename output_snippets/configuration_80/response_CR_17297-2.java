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
                    break;
                }
            }

            for (IResource member : members) {
                removeDerivedResources(member, true /*deleteRoot*/, monitor);
            }

            if (!hasNonDerivedResources && deleteRoot && isFolderEmpty(folder)) {
                rootResource.getLocation().toFile().delete();
            }
        } else if (rootResource.isDerived()) {
            rootResource.getLocation().toFile().delete();
        }
    }
}

private boolean isFolderEmpty(IFolder folder) throws CoreException {
    IResource[] members = folder.members();
    for (IResource member : members) {
        if (!member.isDerived()) {
            return false;
        }
    }
    return true;
}
//<End of snippet n. 0>