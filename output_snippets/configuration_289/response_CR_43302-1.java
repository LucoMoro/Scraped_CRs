//<Beginning of snippet n. 0>


// non-library specific options
if (mVariant.getType() != VariantConfiguration.Type.LIBRARY) {
    command.add("--non-constant-id");

    String extraPackages = mVariant.getLibraryPackages();
    if (extraPackages != null) {
        command.add("--extra-packages");
        command.add(extraPackages);
    }

    // Create R class only for non-library projects
    command.add("--create-r-class");
}

// AAPT options

//<End of snippet n. 0>