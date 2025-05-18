
//<Beginning of snippet n. 0>


if ((modeFlagsToClear&Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
globalModeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
modeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            if (readOwners.size() > 0) {
for (UriPermissionOwner r : writeOwners) {
r.removeWritePermission(this);
}
                readOwners.clear();
}
}
}

//<End of snippet n. 0>








