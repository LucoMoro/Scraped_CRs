/*Check Contacts.CONTENT_URI Activity Support

Issue 10143

Add a test that checks ContactsContract.Contacts.CONTENT_URI
activity intent is supported.

(Ported from Eclair since there is no automerging to Froyo)

Change-Id:I7924818f16f14423838552c9a15f39a50756d263*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/ContactsContract_ContactsTest.java b/tests/tests/provider/src/android/provider/cts/ContactsContract_ContactsTest.java
//Synthetic comment -- index b9be9cf..a3c6a25 100644

//Synthetic comment -- @@ -20,14 +20,21 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.content.ContentResolver;
import android.content.IContentProvider;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.cts.ContactsContract_TestDataBuilder.TestContact;
import android.provider.cts.ContactsContract_TestDataBuilder.TestRawContact;
import android.test.InstrumentationTestCase;

@TestTargetClass(ContactsContract.Contacts.class)
public class ContactsContract_ContactsTest extends InstrumentationTestCase {
private ContentResolver mContentResolver;
//Synthetic comment -- @@ -71,5 +78,15 @@
lastContacted = contact.getLong(Contacts.LAST_TIME_CONTACTED);
assertTrue(oldLastContacted < lastContacted);
}
}








