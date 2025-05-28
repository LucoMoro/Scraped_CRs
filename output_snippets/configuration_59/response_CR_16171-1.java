<<Beginning of snippet n. 0>>
} else if (isUpdate()) {
    // When has changes and "before" exists, then "update"
    final Builder builder = ContentProviderOperation
                            .newUpdate(mUngrouped ? Settings.CONTENT_URI : addCallerIsSyncAdapterParameter(Groups.CONTENT_URI));
    if (mUngrouped) {
        builder.withSelection(Settings.ACCOUNT_NAME + "=? AND " + Settings.ACCOUNT_TYPE + "=?", new String[] {
            accountName, accountType
        });
    } else {
        // Ensure that the group ID is appended correctly
        if (groupId != null) {
            builder.withSelection(Groups._ID + "=?", new String[]{String.valueOf(groupId)});
        } else {
            throw new IllegalArgumentException("Group ID cannot be null. Cannot perform update.");
        }
    }
    // Further builder operations and ContentProviderOperation creation
}
<<End of snippet n. 0>>