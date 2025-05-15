
//<Beginning of snippet n. 0>


} else if (isUpdate()) {
// When has changes and "before" exists, then "update"
final Builder builder = ContentProviderOperation
                        .newUpdate(mUngrouped ? Settings.CONTENT_URI : addCallerIsSyncAdapterParameter(Groups.CONTENT_URI));
if (mUngrouped) {
builder.withSelection(Settings.ACCOUNT_NAME + "=? AND " + Settings.ACCOUNT_TYPE
+ "=?", new String[] {

//<End of snippet n. 0>








