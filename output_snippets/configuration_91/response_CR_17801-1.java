//<Beginning of snippet n. 0>
return pkg instanceof ToolPackage;
}

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc
* Executes update checks and suggests missing dependencies for platform-tools.
*/
public String updatePackages(List<Package> packages, String minPlatformToolsRev) {
    List<String> missingDependencies = new ArrayList<>();
    StringBuilder updateMessages = new StringBuilder();
    String highestAvailableRevision = null;

    for (Package pkg : packages) {
        if (pkg.hasUpdates()) {
            if (pkg instanceof ToolPackage) {
                if (pkg.isMissingDependencies()) {
                    missingDependencies.add(pkg.getName() + " requires installation of missing dependencies.");
                }
                highestAvailableRevision = pkg.getHighestAvailableRevision();
                if (highestAvailableRevision != null) {
                    updateMessages.append("Update ").append(pkg.getName()).append(" to version ").append(highestAvailableRevision).append(". ");
                }
            }
        }
    }

    if (minPlatformToolsRev == null && highestAvailableRevision != null) {
        return "No minimum revision set. Highest available revision is " + highestAvailableRevision + ". " + 
               (missingDependencies.isEmpty() ? "No updates required." : String.join(", ", missingDependencies));
    }

    return missingDependencies.isEmpty() && updateMessages.length() == 0 ? "No updates required." : 
           updateMessages.toString() + String.join(", ", missingDependencies);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
return "";
}
}

//<End of snippet n. 1>