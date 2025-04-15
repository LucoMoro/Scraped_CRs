/*Fix Contacts_PeopleTest#testAddToGroup

Bug 3188260

The My Contacts group used in the test is not created automatically,
so add the group before starting the test.

Change-Id:I3a84f5234ae97241ef17bf821c72bbc60607674c*/




//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Contacts_PeopleTest.java b/tests/tests/provider/src/android/provider/cts/Contacts_PeopleTest.java
//Synthetic comment -- index 732e75d..d8d6baa 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.Contacts.Groups;
import android.provider.Contacts.GroupsColumns;
import android.provider.Contacts.People;
import android.test.InstrumentationTestCase;

//Synthetic comment -- @@ -148,6 +149,11 @@
public void testAddToGroup() {
Cursor cursor;
try {
            // Add the My Contacts group, since it is no longer automatically created.
            ContentValues testValues = new ContentValues();
            testValues.put(GroupsColumns.SYSTEM_ID, Groups.GROUP_MY_CONTACTS);
            mProvider.insert(Groups.CONTENT_URI, testValues);

// People: test_people_0, Group: Groups.GROUP_MY_CONTACTS
cursor = mProvider.query(mPeopleRowsAdded.get(0), PEOPLE_PROJECTION,
null, null, null);







