/*Prevent crash when adding sync group

When adding a sync group the ContactsPreferencesActivity creates a URI for the
Contacts provider. The default Eclair (and Froyo) implemention does not
include the group ID which is required by the Contacts provider. This patch
appends the group ID to the URI path, which is where the Contacts provider
is looking for it.

Change-Id:Icd64d24141cd95b1d538e64d1ab4a04aac25fe76*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ui/ContactsPreferencesActivity.java b/src/com/android/contacts/ui/ContactsPreferencesActivity.java
//Synthetic comment -- index 5a89745..60b061f 100644

//Synthetic comment -- @@ -470,7 +470,7 @@
} else if (isUpdate()) {
// When has changes and "before" exists, then "update"
final Builder builder = ContentProviderOperation
                        .newUpdate(mUngrouped ? Settings.CONTENT_URI : addCallerIsSyncAdapterParameter(Groups.CONTENT_URI.buildUpon().appendPath(this.getId().toString()).build()));
if (mUngrouped) {
builder.withSelection(Settings.ACCOUNT_NAME + "=? AND " + Settings.ACCOUNT_TYPE
+ "=?", new String[] {







