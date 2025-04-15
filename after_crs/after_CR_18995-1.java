/*Contact icon cannot be replaced when set from gallery.

It is not possible to replace a contact icon for the
default account type.
This fix adds the default account type to the check that is
made before trying to add or replace a contact icon.

Change-Id:I8fc2a71347c2a9123fe61edece3aef79d0900b14*/




//Synthetic comment -- diff --git a/src/com/android/contacts/AttachImage.java b/src/com/android/contacts/AttachImage.java
//Synthetic comment -- index 6970842..599ab4f 100644

//Synthetic comment -- @@ -175,8 +175,9 @@
/**
* Inserts a photo on the raw contact.
* @param values the photo values
     * @param assertAccount if true, will check to verify that no photos exist for Google,
     *     Exchange and unsynced phone account types. These account types only take one picture,
     *     so if one exists, the account will be updated with the new photo.
*/
private void insertPhoto(ContentValues values, Uri rawContactDataUri,
boolean assertAccount) {
//Synthetic comment -- @@ -185,10 +186,11 @@
new ArrayList<ContentProviderOperation>();

if (assertAccount) {
            // Make sure no pictures exist for Google, Exchange and unsynced phone accounts.
operations.add(ContentProviderOperation.newAssertQuery(rawContactDataUri)
                    .withSelection(Photo.MIMETYPE + "=? AND ("
                            + RawContacts.ACCOUNT_TYPE + " IN (?,?) OR "
                            + RawContacts.ACCOUNT_TYPE + " IS NULL)",
new String[] {Photo.CONTENT_ITEM_TYPE, GoogleSource.ACCOUNT_TYPE,
ExchangeSource.ACCOUNT_TYPE})
.withExpectedCount(0).build());







