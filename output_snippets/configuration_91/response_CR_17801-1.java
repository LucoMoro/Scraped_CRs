//<Beginning of snippet n. 0>
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc
*/
//<End of snippet n. 0>


//<Beginning of snippet n. 1>

if (pkg instanceof ToolPackage) {
    // Check for missing dependencies
    List<Package> dependencies = pkg.getDependencies();
    for (Package dep : dependencies) {
        if (!isPackageInstalled(dep)) {
            suggestInstalling(dep);
        }
    }
    
    // Handle missing min-platform-tools-rev
    if (pkg.getMinPlatformToolsRev() == null) {
        int highestRev = getHighestAvailableRevision(pkg);
        pkg.setMinPlatformToolsRev(highestRev);
    }
}
return "";
}
//<End of snippet n. 1>