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
        boolean missingDependencies = pkg.isMissingDependencies();
        boolean hasMinPlatformToolsRev = pkg.hasMinPlatformToolsRev();

        if (missingDependencies) {
            suggestInstall(pkg.getMissingDependencies());
        }

        if (!hasMinPlatformToolsRev) {
            suggestHighestAvailableRevision(pkg);
        }
    }

    if (localPackages.stream().anyMatch(Package::isMissingDependencies)) {
        suggestPlatformToolsInstallation();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return "";
//<End of snippet n. 1>