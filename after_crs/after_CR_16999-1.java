/*Check Contacts.CONTENT_URI Activity Support

Issue 10143

Add a test that checks ContactsContract.Contacts.CONTENT_URI
activity intent is supported.

Change-Id:I73929f3094974a18164636156672f71893f0f544*/




//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/ContactsContract_ContactsTest.java b/tests/tests/provider/src/android/provider/cts/ContactsContract_ContactsTest.java
//Synthetic comment -- index b9be9cf..c239f9a 100644

//Synthetic comment -- @@ -20,8 +20,12 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.cts.ContactsContract_TestDataBuilder.TestContact;
//Synthetic comment -- @@ -71,5 +75,18 @@
lastContacted = contact.getLong(Contacts.LAST_TIME_CONTACTED);
assertTrue(oldLastContacted < lastContacted);
}

    public void testContentUri() {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Instrumentation instrumentation = getInstrumentation();
        Context context = instrumentation.getContext();
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException notFound) {
            fail("Device does not support the activity intent: " + intent);
        }
    }
}








