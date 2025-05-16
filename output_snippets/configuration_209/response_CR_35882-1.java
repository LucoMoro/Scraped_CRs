<<Beginning of snippet n. 0>>
// check if the project has a valid target.
ProjectState state = Sdk.getProjectState(iProject);

File defaultProperties = new File("default.properties");
File projectProperties = new File("project.prop");

if (!defaultProperties.exists() && !projectProperties.exists()) {
    throw new IllegalStateException("Neither default.properties nor project.prop exists.");
}

// Proceed to initialize library classpath container using available properties
//<End of snippet n. 0>