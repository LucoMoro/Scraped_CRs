//<Beginning of snippet n. 0>
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc
*/

// Check and update existing packages
for (Package pkg : existingPackages) {
    // Check for missing dependencies
    if (hasMissingDependencies(pkg)) {
        promptInstallDependencies(pkg);
    }

    // Check for min-platform-tools-rev attribute
    if (!pkg.hasMinPlatformToolsRev()) {
        suggestHighestAvailableRevision(pkg);
    }
}

// Update all packages with the checks included
updateAllPackages(existingPackages);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
}
return "";
}
//<End of snippet n. 1>