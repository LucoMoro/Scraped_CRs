//<Beginning of snippet n. 0>

if (iProject != null) {
    File defaultProperties = new File("default.properties");
    File projectProperties = new File("project.properties");

    if (defaultProperties.exists() || projectProperties.exists()) {
        ProjectState state = Sdk.getProjectState(iProject);
        
        // Additional logic to gather a list of all that need to go in...
    } else {
        throw new FileNotFoundException("Neither default.properties nor project.properties exist.");
    }
} else {
    throw new IllegalArgumentException("Project cannot be null");
}

//<End of snippet n. 0>