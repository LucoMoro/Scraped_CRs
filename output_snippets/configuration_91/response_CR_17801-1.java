//<Beginning of snippet n. 0>
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc
// Check for existing local packages and suggest missing dependencies
if (isUpdateAllSelected()) {
    List<Package> localPackages = getLocalPackages();
    for (Package pkg : localPackages) {
        if (pkg.isMissingDependencies()) {
            suggestInstall(pkg.getMissingDependencies());
        }
        if (!pkg.hasMinPlatformToolsRev()) {
            suggestHighestAvailableRevision(pkg);
        }
    }
    if (isAnyPackageMissingDependencies(localPackages)) {
        suggestPlatformToolsInstallation();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return "";
}
//<End of snippet n. 1>