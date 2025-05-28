//<Beginning of snippet n. 0>

File defaultProperties = new File("default.properties");
File projectProperties = new File("project.properties");
ProjectState state = null;

if (defaultProperties.exists() || projectProperties.exists()) {
    state = Sdk.getProjectState(iProject);
    if (state == null) {
        System.err.println("Error: ProjectState is null. Unable to proceed.");
        // Implement additional fallback logic if needed
    }
} else {
    System.err.println("Warning: Both default.properties and project.properties are missing. Falling back to default configuration.");
    // Implement logic to handle default configuration
}

// Additional logic for handling properties files and application functionality

//<End of snippet n. 0>