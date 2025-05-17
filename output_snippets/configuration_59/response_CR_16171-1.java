//<Beginning of snippet n. 0>
} else if (isUpdate()) {
    final String groupId = getGroupId();
    if (groupId == null || groupId.isEmpty()) {
        throw new IllegalArgumentException("Group ID cannot be null or empty");
    }
    final Builder builder = ContentProviderOperation
                            .newUpdate(addCallerIsSyncAdapterParameter(Uri.withAppendedPath(Groups.CONTENT_URI, groupId)));
    builder.withSelection(Settings.ACCOUNT_NAME + "=? AND " + Settings.ACCOUNT_TYPE + "=?", new String[] {
        getAccountName(), getAccountType()
    });
//<End of snippet n. 0>