//<Beginning of snippet n. 0>
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
if (updateAllSelected) {
    List<String> suggestions = new ArrayList<>();
    Map<Package, String> pkgRevisionMap = new HashMap<>();
    
    for (Package pkg : localPackages) {
        if (!pkg.hasMinPlatformToolsRev()) {
            String highestRevision = queryHighestAvailableRevision("platform-tools", pkg);
            pkgRevisionMap.put(pkg, highestRevision);
            suggestions.add("Update required: Package " + pkg.getName() + " needs platform-tools version: " + highestRevision);
        }
    }
    
    if (!suggestions.isEmpty()) {
        String urgencyMessage = "The following updates are required due to missing min-platform-tools-rev:\n";
        return urgencyMessage + String.join("\n", suggestions);
    }
}
return "";
}
//<End of snippet n. 1>