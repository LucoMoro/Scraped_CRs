//<Beginning of snippet n. 0>

// check if the project has a valid target.
ProjectState state = Sdk.getProjectState(iProject);

// Check for the existence of properties files
File defaultProps = new File("default.properties");
File projectProps = new File("project.properties");

if (!defaultProps.exists() && !projectProps.exists()) {
    // Log an error message or throw an exception
    String errorMessage = "Both 'default.properties' and 'project.properties' are missing.";
    System.err.println(errorMessage);
    throw new IllegalStateException(errorMessage);
}

/*
* At this point we're going to gather a list of all that need to go in the
//<End of snippet n. 0>