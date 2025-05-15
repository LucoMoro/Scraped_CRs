//<Beginning of snippet n. 0>
} else if (isUpdate()) {
    // When has changes and "before" exists, then "update"
    if (groupId == null || groupId.isEmpty()) {
        // Handle invalid group ID scenario
        throw new IllegalArgumentException("Invalid group ID");
    }

    final Builder builder = ContentProviderOperation
                            .newUpdate(mUngrouped ? Settings.CONTENT_URI : addCallerIsSyncAdapterParameter(Groups.CONTENT_URI));

    if (mUngrouped) {
        builder.withSelection(Settings.ACCOUNT_NAME + "=? AND " + Settings.ACCOUNT_TYPE + "=?", new String[] {
            accountName, accountType
        });
    } else {
        try {
            builder.withUri(Groups.getContentUri(groupId));
        } catch (Exception e) {
            // Add robust error handling
            Log.e(TAG, "Error constructing URI for group ID: " + groupId, e);
            return;
        }
    }
    // Continue with the update operation...
}
//<End of snippet n. 0>