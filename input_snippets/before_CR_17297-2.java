
//<Beginning of snippet n. 0>


removeDerivedResources(rootResource, false, monitor);
}

    private void removeDerivedResources(IResource rootResource, boolean deleteRoot,
IProgressMonitor monitor)
throws CoreException {
if (rootResource.exists()) {
if (rootResource.getType() == IResource.FOLDER) {
IFolder folder = (IFolder)rootResource;
IResource[] members = folder.members();
for (IResource member : members) {
                    removeDerivedResources(member, true /*deleteRoot*/, monitor);
}
            } else if (rootResource.isDerived()) {
                rootResource.getLocation().toFile().delete();
}

            if (deleteRoot) {
rootResource.getLocation().toFile().delete();
}
}
}
}

//<End of snippet n. 0>








