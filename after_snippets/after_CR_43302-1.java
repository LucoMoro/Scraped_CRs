
//<Beginning of snippet n. 0>


// library specific options
if (mVariant.getType() == VariantConfiguration.Type.LIBRARY) {
command.add("--non-constant-id");
        } else {
            // only create the R class from library dependencies if this is not a library itself.
            String extraPackages = mVariant.getLibraryPackages();
            if (extraPackages != null) {
                command.add("--extra-packages");
                command.add(extraPackages);
            }
}

// AAPT options

//<End of snippet n. 0>








