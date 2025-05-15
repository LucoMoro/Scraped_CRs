//<Beginning of snippet n. 0>

if (mVariant.getType() != VariantConfiguration.Type.LIBRARY) {
    String extraPackages = mVariant.getLibraryPackages();
    if (extraPackages != null) {
        command.add("--extra-packages");
        command.add(extraPackages);
    }
}

// AAPT options

//<End of snippet n. 0>