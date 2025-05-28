//<Beginning of snippet n. 0>
if ((modeFlagsToClear & Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
    globalModeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
    modeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
    writeOwners.clear();
    if (writeOwners != null && !writeOwners.isEmpty()) {
        for (UriPermissionOwner r : writeOwners) {
            r.removeWritePermission(this);
        }
    }
    if (readOwners != null) {
        readOwners.clear();
    }
}
//<End of snippet n. 0>