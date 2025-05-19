//<Beginning of snippet n. 0>

File defaultProperties = new File("default.properties");
File projectProperties = new File("project.properties");
ProjectState state = null;

if (defaultProperties.exists() || projectProperties.exists()) {
    state = Sdk.getProjectState(iProject);
    if (state == null) {
        System.err.println("Error: ProjectState is null. Attempting to recover using default configuration.");
        // Implement recovery mechanism or default settings
        state = new ProjectState(); // Assuming a no-arg constructor sets defaults
    }
} else {
    System.err.println("Warning: Both default.properties and project.properties are missing. Falling back to safe mode operation.");
    // Implement logic to handle safe mode operation
    state = new ProjectState(); // Assuming a no-arg constructor sets defaults
}

// Additional logic for handling properties files and application functionality

//<End of snippet n. 0>