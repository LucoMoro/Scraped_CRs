//<Beginning of snippet n. 0>

// check if the project has a valid target.
ProjectState state = Sdk.getProjectState(iProject);
if (state == null) {
    throw new IllegalStateException("ProjectState is null. Unable to proceed.");
}

// Check for existence of property files
File projectPropFile = new File("project.prop");
File defaultPropertiesFile = new File("default.properties");

if (!projectPropFile.exists() && !defaultPropertiesFile.exists()) {
    throw new FileNotFoundException("Neither project.prop nor default.properties found.");
}

// Continue initialization using either project.prop or default.properties
Properties properties = new Properties();
try (InputStream input = projectPropFile.exists() ? new FileInputStream(projectPropFile) : new FileInputStream(defaultPropertiesFile)) {
    properties.load(input);
} catch (IOException e) {
    throw new IOException("Error loading properties file.", e);
}

/*
* At this point we're going to gather a list of all that need to go in the
*/

//<End of snippet n. 0>