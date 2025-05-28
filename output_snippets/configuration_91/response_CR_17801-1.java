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
    for (Package pkg : localPackages) {
        if (!pkg.hasMinPlatformToolsRev()) {
            String highestRevision = queryHighestAvailableRevision("platform-tools");
            pkg.updateConfigurationWithMinPlatformToolsRev(highestRevision);
            suggestions.add("Install platform-tools version: " + highestRevision + " for package: " + pkg.getName());
        }
    }
    if (!suggestions.isEmpty()) {
        return String.join("\n", suggestions);
    }
}
return "";
}
//<End of snippet n. 1>