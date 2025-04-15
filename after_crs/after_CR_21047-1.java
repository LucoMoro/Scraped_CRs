/*Fix Contacts_PeopleTest

Bug 3188260

Fixed some incorrect column indices and queries.

Change-Id:Ief0b6cbdbd1f433bb5421b1bd291436d4ac36925*/




//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Contacts_PeopleTest.java b/tests/tests/provider/src/android/provider/cts/Contacts_PeopleTest.java
//Synthetic comment -- index 230a541..732e75d 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package android.provider.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
//Synthetic comment -- @@ -31,12 +36,6 @@
import android.provider.Contacts.People;
import android.test.InstrumentationTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
//Synthetic comment -- @@ -58,7 +57,7 @@
private static final int PEOPLE_LAST_CONTACTED_INDEX = 1;

private static final int MEMBERSHIP_PERSON_ID_INDEX = 1;
    private static final int MEMBERSHIP_GROUP_ID_INDEX = 5;

private static final String[] GROUPS_PROJECTION = new String[] {
Groups._ID,
//Synthetic comment -- @@ -146,7 +145,6 @@
args = {android.content.ContentResolver.class, android.content.ContentValues.class}
)
})
public void testAddToGroup() {
Cursor cursor;
try {
//Synthetic comment -- @@ -158,9 +156,8 @@
cursor.close();
mRowsAdded.add(People.addToMyContactsGroup(mContentResolver, personId));
cursor = mProvider.query(Groups.CONTENT_URI, GROUPS_PROJECTION,
                    Groups.SYSTEM_ID + "='" + Groups.GROUP_MY_CONTACTS + "'", null, null);
cursor.moveToFirst();
int groupId = cursor.getInt(GROUPS_ID_INDEX);
cursor.close();
cursor = People.queryGroups(mContentResolver, personId);
//Synthetic comment -- @@ -183,7 +180,7 @@
mRowsAdded.add(ContentUris.withAppendedId(People.CONTENT_URI, personId));
cursor.close();
cursor = mProvider.query(Groups.CONTENT_URI, GROUPS_PROJECTION,
                    Groups.SYSTEM_ID + "='" + Groups.GROUP_MY_CONTACTS + "'", null, null);
cursor.moveToFirst();
groupId = cursor.getInt(GROUPS_ID_INDEX);
cursor.close();
//Synthetic comment -- @@ -281,7 +278,7 @@
level = TestLevel.COMPLETE,
notes = "Test methods access the photo data of person",
method = "loadContactPhoto",
            args = {android.content.Context.class, android.net.Uri.class, int.class,
android.graphics.BitmapFactory.Options.class}
),
@TestTargetNew(
//Synthetic comment -- @@ -291,7 +288,6 @@
args = {android.content.ContentResolver.class, android.net.Uri.class}
)
})
public void testAccessPhotoData() {
Context context = getInstrumentation().getTargetContext();
try {
//Synthetic comment -- @@ -308,10 +304,6 @@
Bitmap bitmap = BitmapFactory.decodeStream(photoStream, null, null);
assertEquals(212, bitmap.getWidth());
assertEquals(142, bitmap.getHeight());

photoStream = People.openContactPhotoInputStream(mContentResolver,
mPeopleRowsAdded.get(1));
//Synthetic comment -- @@ -324,8 +316,7 @@

bitmap = People.loadContactPhoto(context, null,
com.android.cts.stub.R.drawable.size_48x48, null);
            assertNotNull(bitmap);
} catch (IOException e) {
fail("Unexpected IOException");
}







