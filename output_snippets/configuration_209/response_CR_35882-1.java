//<Beginning of snippet n. 0>

if (iProject != null) {
    ProjectState state = Sdk.getProjectState(iProject);
    
    // Additional logic to gather a list of all that need to go in...
} else {
    throw new IllegalArgumentException("Project cannot be null");
}

//<End of snippet n. 0>