<<Beginning of snippet n. 0>>
ProjectState state = Sdk.getProjectState(iProject);
if (state == null) {
    // Handle null project state
    return; // or throw an exception based on the context
}

File defaultProperties = new File("default.properties");
File projectProperties = new File("project.properties");

if (!defaultProperties.exists() && !projectProperties.exists()) {
    // Log error about missing property files
    System.err.println("Both default.properties and project.properties are missing.");
    return; // or handle it as necessary
}

// Initialize library classpath container only if at least one properties file exists
if (defaultProperties.exists() || projectProperties.exists()) {
    // Proceed with processing properties files
}
<<End of snippet n. 0>>