/*Remove ContactsTest GMS Dependencies

Bug 2258907

Remove the assertions that relied on GMS being present. Take off the
KnownFailure annotation.

Change-Id:I80c326ce08161f5e79c7d4da4cd62249267d8f69*/




//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/ContactsTest.java b/tests/tests/provider/src/android/provider/cts/ContactsTest.java
//Synthetic comment -- index 0496385..2b0786c 100644

//Synthetic comment -- @@ -869,7 +869,6 @@
* Test case for the behavior of the ContactsProvider's groupmembership table
* It does not test any APIs in android.provider.Contacts.java
*/
public void testGroupMembershipTable() {
final String[] GROUP_MEMBERSHIP_PROJECTION = new String[] {
GroupMembership._ID, GroupMembership.PERSON_ID,
//Synthetic comment -- @@ -909,8 +908,6 @@
assertTrue(cursor.moveToNext());
assertEquals(peopleId, cursor.getInt(PERSON_ID_INDEX));
assertEquals(groupId1, cursor.getInt(GROUP_ID_INDEX));
int id = cursor.getInt(ID_INDEX);
cursor.close();








