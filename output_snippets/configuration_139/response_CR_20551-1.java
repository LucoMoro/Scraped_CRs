//<Beginning of snippet n. 0>
if ((modeFlagsToClear & Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
    globalModeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
    modeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
    
    for (UriPermissionOwner w : writeOwners) {
        w.removeWritePermission(this);
    }
    writeOwners.clear();
}

if ((modeFlagsToClear & Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) {
    readOwners.clear();
}
//<End of snippet n. 0>